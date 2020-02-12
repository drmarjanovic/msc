package com.github.drmarjanovic.users.infrastructure

import com.github.drmarjanovic.users.domain.UserAlgebra.Service
import com.github.drmarjanovic.users.domain.{ User, UserAlgebra, UserId }
import com.github.drmarjanovic.users.infrastructure.PgUserRepo.SQL
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import io.opentracing.Span
import io.opentracing.tag.Tags
import zio.Task
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing

trait PgUserRepo extends UserAlgebra {

  protected def xa: Transactor[Task]

  protected def telemetry: OpenTracing.Service

  override val userAlgebra: UserAlgebra.Service[Any] = new Service[Any] {
    override def one(id: UserId)(implicit parent: Span): Task[Option[User]] =
      (for {
        span      <- telemetry.childOf(parent, "DB: find by ID")
        query     = SQL.one(id)
        _         = span.setTag(Tags.DB_TYPE, "sql")
        _         = span.setTag(Tags.DB_STATEMENT, query.sql)
        maybeUser <- query.option.transact(xa)
        _         = span.finish()
      } yield maybeUser).provide(Clock.Live)

    override def findByEmailAndPassword(email: String, password: String)(implicit parent: Span): Task[Option[User]] =
      (for {
        span      <- telemetry.childOf(parent, "DB: find by email and password")
        query     = SQL.findByEmailAndPassword(email, password)
        _         = span.setTag(Tags.DB_TYPE, "sql")
        _         = span.setTag(Tags.DB_STATEMENT, query.sql)
        maybeUser <- SQL.findByEmailAndPassword(email, password).option.transact(xa)
        _         = span.finish()
      } yield maybeUser).provide(Clock.Live)

  }
}

object PgUserRepo {

  object SQL {
    def one(id: Long): Query0[User] =
      sql"""SELECT * FROM users WHERE id = $id;""".query[User]

    def findByEmailAndPassword(email: String, password: String): Query0[User] =
      sql"""SELECT * FROM users WHERE email = $email AND password = $password;""".query[User]
  }

}
