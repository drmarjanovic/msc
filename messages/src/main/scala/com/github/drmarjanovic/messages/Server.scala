package com.github.drmarjanovic.messages

import cats.effect.ExitCode
import com.github.drmarjanovic.messages.api.Messages
import com.github.drmarjanovic.messages.config.Config.DatabaseConfig
import com.github.drmarjanovic.messages.config.Configuration
import com.github.drmarjanovic.messages.domain.MessageService
import com.github.drmarjanovic.messages.infrastructure.PgMessageRepo
import com.github.drmarjanovic.tracing.JaegerTracer
import doobie.util.transactor.Transactor
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing
import zio.{ Task, ZEnv, ZIO, App => ZApp }

object Server extends ZApp {

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    (for {
      conf <- config.load.provide(Configuration.Live)
      _    <- DatabaseConfig.init(conf.database)

      transactorR = DatabaseConfig.makeTransactor(conf.database)
      tracerR     = JaegerTracer.makeService(conf.tracer.host, ServiceName)

      service = new MessageService
      routes  = Router[AppTask](Api -> new Messages(service).routes()).orNotFound

      server = ZIO.runtime[AppEnv].flatMap { implicit rts =>
        BlazeServerBuilder[AppTask]
          .bindHttp(conf.http.port, conf.http.host)
          .withHttpApp(routes)
          .serve
          .compile[AppTask, AppTask, ExitCode]
          .drain
      }

      zio <- tracerR.use { jaeger =>
              transactorR.use { transactor =>
                server.provideSome[ZEnv] { base =>
                  new Blocking with Clock with PgMessageRepo with OpenTracing {
                    override protected def xa: Transactor[Task] = transactor
                    override def telemetry: OpenTracing.Service = jaeger.telemetry

                    override val blocking: Blocking.Service[Any] = base.blocking
                    override val clock: Clock.Service[Any]       = base.clock
                  }
                }
              }
            }
    } yield zio).foldM(_ => ZIO.succeed(1), _ => ZIO.succeed(0))

}
