package com.github.drmarjanovic.updates

import cats.effect.ExitCode
import com.github.drmarjanovic.tracing.JaegerTracer
import com.github.drmarjanovic.updates.api.Updates
import com.github.drmarjanovic.updates.config.Config.DatabaseConfig
import com.github.drmarjanovic.updates.config.Configuration
import com.github.drmarjanovic.updates.domain.UpdateService
import com.github.drmarjanovic.updates.infrastructure.PgUpdateRepo
import doobie.util.transactor.Transactor
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing
import zio.{Task, ZEnv, ZIO, App => ZApp}

object Server extends ZApp {

  override def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    (for {
      conf <- config.load.provide(Configuration.Live)
      _    <- DatabaseConfig.init(conf.database)

      transactorR = DatabaseConfig.makeTransactor(conf.database)
      tracerR  = JaegerTracer.makeService(conf.tracer.host, ServiceName)

      service = new UpdateService
      routes  = Router[AppTask](Api -> new Updates(service, conf.api).routes()).orNotFound

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
            new Blocking with Clock with PgUpdateRepo with OpenTracing {
              override protected def xa: Transactor[Task] = transactor
              override def telemetry: OpenTracing.Service = jaeger.telemetry

              override val blocking: Blocking.Service[Any] = base.blocking
              override val clock: Clock.Service[Any] = base.clock
            }
          }
        }
      }
    } yield zio).foldM(_ => ZIO.succeed(1), _ => ZIO.succeed(0))

}
