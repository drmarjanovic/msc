package com.github.drmarjanovic.updates.infrastructure

import java.util.{ Date => JDate }

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import com.github.drmarjanovic.updates.domain._
import com.github.drmarjanovic.updates.infrastructure.PgUpdateRepo.SQL
import doobie.implicits._
import doobie.util.meta.Meta
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import io.opentracing.Span
import io.opentracing.tag.Tags
import org.joda.time.DateTime
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing
import zio.{ RIO, Task }

trait PgUpdateRepo extends UpdateAlgebra {

  protected def xa: Transactor[Task]

  protected def telemetry: OpenTracing.Service

  override val updateAlgebra: UpdateAlgebra.Service[Any] = new UpdateAlgebra.Service[Any] {
    override def one(id: UpdateId)(implicit parent: Span): RIO[Any, Option[Update]] =
      (for {
        span        <- telemetry.childOf(parent, "DB: find by ID")
        query       = SQL.one(id)
        _           = span.setTag(Tags.DB_TYPE, "sql")
        _           = span.setTag(Tags.DB_STATEMENT, query.sql)
        maybeUpdate <- query.option.transact(xa)
        _           = span.finish()
      } yield maybeUpdate).provide(Clock.Live)

    override def findByUserId(
      userId: UserId,
      offset: Offset,
      limit: Limit
    )(implicit parent: Span): RIO[Any, List[Update]] =
      (for {
        span    <- telemetry.childOf(parent, "DB: find by user ID")
        query   = SQL.list(userId, offset, limit)
        _       = span.setTag(Tags.DB_TYPE, "sql")
        _       = span.setTag(Tags.DB_STATEMENT, query.sql)
        updates <- query.to[List].transact(xa)
        _       = span.finish()
      } yield updates).provide(Clock.Live)

    override def save(
      userId: UserId,
      `type`: UpdateType,
      message: String
    )(implicit parent: Span): RIO[Any, Either[Throwable, UpdateId]] =
      (for {
        span  <- telemetry.childOf(parent, "DB: create update")
        query = SQL.create(userId, `type`.toString, message)
        _     = span.setTag(Tags.DB_TYPE, "sql")
        _     = span.setTag(Tags.DB_STATEMENT, query.sql)
        updateId <- query
                     .withUniqueGeneratedKeys[Long]("id")
                     .attemptSql
                     .map(_.right.map(UpdateId.apply))
                     .transact(xa)
        _ = span.finish()
      } yield updateId).provide(Clock.Live)
  }

}

object PgUpdateRepo {

  object SQL {
    implicit val jodaDateTimeMeta: Meta[DateTime] =
      Meta[JDate].imap(ts => new DateTime(ts))(dt => new JDate(dt.getMillis))

    implicit val updateTypeMeta: Meta[UpdateType] =
      Meta[String].imap(UpdateType.of)(_.toString)

    def one(id: UpdateId): Query0[Update] =
      sql"""SELECT * FROM updates WHERE id = $id;""".query[Update]

    def list(userId: UserId, offset: Offset, limit: Limit): Query0[Update] =
      sql"""SELECT * FROM updates WHERE user_id = $userId ORDER BY created_at DESC OFFSET $offset LIMIT $limit;"""
        .query[Update]

    def create(userId: UserId, `type`: String, message: String): Update0 =
      sql"""INSERT INTO updates (user_id, type, message) VALUES ($userId, ${`type`}, $message);""".update
  }

}
