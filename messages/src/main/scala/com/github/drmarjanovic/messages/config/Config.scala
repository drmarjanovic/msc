package com.github.drmarjanovic.messages.config

import cats.effect.Blocker
import com.github.drmarjanovic.messages.config.Config.{ DatabaseConfig, HttpConfig, TracerHost }
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import zio.blocking.Blocking
import zio.interop.catz._
import zio.{ Task, ZIO, ZManaged }

final case class Config(http: HttpConfig, database: DatabaseConfig, tracer: TracerHost)

object Config {

  final case class DatabaseConfig(driver: String, url: String, user: String, password: String)

  object DatabaseConfig {
    def init(config: DatabaseConfig): Task[Unit] =
      Task {
        Flyway
          .configure()
          .dataSource(config.url, config.user, config.password)
          .load()
          .migrate()
      }.unit

    def makeTransactor(config: DatabaseConfig): ZManaged[Blocking, Throwable, HikariTransactor[Task]] =
      ZIO.runtime[Blocking].toManaged_.flatMap { implicit rt =>
        for {
          transactEC <- rt.environment.blocking.blockingExecutor.map(_.asEC).toManaged_
          connectEC  = rt.platform.executor.asEC
          transactor <- HikariTransactor
                         .newHikariTransactor[Task](
                           config.driver,
                           config.url,
                           config.user,
                           config.password,
                           connectEC,
                           Blocker.liftExecutionContext(transactEC)
                         )
                         .toManaged
        } yield transactor
      }
  }

  final case class HttpConfig(host: String, port: Int)

  final case class TracerHost(host: String) extends AnyVal

}
