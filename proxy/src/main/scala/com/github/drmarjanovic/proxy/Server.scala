package com.github.drmarjanovic.proxy

import caliban.GraphQL._
import caliban.schema.Annotations.GQLDescription
import caliban.schema.GenericSchema
import caliban.{ GraphQL, Http4sAdapter, RootResolver }
import cats.effect.ExitCode
import com.github.drmarjanovic.proxy.config.Configuration
import com.github.drmarjanovic.proxy.model._
import com.github.drmarjanovic.proxy.services.{ Contacts, Updates, Users }
import com.github.drmarjanovic.tracing.JaegerTracer
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import org.http4s.syntax.kleisli._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing
import zio.{ URIO, ZEnv, ZIO, App => ZApp }
// import zquery.{ DataSource, Request, ZQuery }

import scala.language.postfixOps

object Server extends ZApp with GenericSchema[AppEnv] {

  //  type MyQuery[+A] = ZQuery[Any, Nothing, A]
  //
  //  val messages = new Messages(null)
  //
  //  final case class GetMessages(contactId: Long) extends Request[Nothing, List[Message]]
  //
  //  val MessagesDataSource: DataSource[Any, GetMessages] =
  //    DataSource.fromFunctionM("MessagesDataSource")(req => messages.get(1, req.contactId))
  //
  //  def getMessages(contactId: Long): MyQuery[List[Message]] =
  //    ZQuery.fromRequest(GetMessages(contactId))(MessagesDataSource)

  // me; contacts / messages; updates
  final case class Queries(
    @GQLDescription("Return info about authenticated user.")
    me: () => URIO[AppEnv, Option[User]],
    @GQLDescription("Return paginated contacts for authenticated user.")
    contacts: ContactsArgs => URIO[AppEnv, List[Contact]],
    @GQLDescription("Return paginated updates for authenticated user.")
    updates: UpdatesArgs => URIO[AppEnv, List[Update]]
  )

  // auth; sendUpdate
  final case class Mutations(
    @GQLDescription("Authenticate user.")
    auth: AuthArgs => URIO[AppEnv, Option[BearerToken]],
    @GQLDescription("Send specified update.")
    sendUpdate: SendUpdateArgs => URIO[AppEnv, Option[Update]],
  )

  def makeApi(contacts: Contacts, updates: Updates, users: Users): GraphQL[AppEnv] =
    graphQL(
      RootResolver(
        Queries(
          () => users.one(AuthUserId),
          args => contacts.get(AuthUserId, args.filters, args.offset, args.limit),
          args => updates.get(AuthUserId, args.offset, args.limit)
        ),
        Mutations(
          args => users.auth(args.email, args.password),
          args => updates.create(AuthUserId, args.to, args.body)
        )
      )
    )

  //  val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
  //  import dsl._

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    (for {
      conf <- config.load.provide(Configuration.Live)

      tracerR = JaegerTracer.makeService(conf.tracer.host, ServiceName)

      contacts = new Contacts(conf.api.contacts)
      updates  = new Updates(conf.api.updates)
      users    = new Users(conf.api.users)

      interpreter = makeApi(contacts, updates, users).interpreter
      server = ZIO.runtime[AppEnv].flatMap { implicit rts =>
        BlazeServerBuilder[AppTask]
          .bindHttp(conf.http.port, conf.http.host)
          .withHttpApp(
            Router[AppTask]("api/graphql" -> Http4sAdapter.makeHttpService(interpreter)).orNotFound
          )
          .serve
          .compile[AppTask, AppTask, ExitCode]
          .drain
      }

      zio <- tracerR.use { jaeger =>
              server.provideSome[ZEnv] { base =>
                new Console with Clock with OpenTracing {
                  override def telemetry: OpenTracing.Service = jaeger.telemetry

                  override val console: Console.Service[Any] = base.console
                  override val clock: Clock.Service[Any]     = base.clock
                }
              }
            }
    } yield zio).foldM(_ => ZIO.succeed(1), _ => ZIO.succeed(0))

}
