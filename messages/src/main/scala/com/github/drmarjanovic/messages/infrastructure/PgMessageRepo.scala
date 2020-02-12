package com.github.drmarjanovic.messages.infrastructure

import java.util.{ Date => JDate }

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import com.github.drmarjanovic.messages.domain._
import com.github.drmarjanovic.messages.infrastructure.PgMessageRepo.SQL
import doobie.implicits._
import doobie.util.meta._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import io.opentracing.Span
import io.opentracing.tag.Tags
import org.joda.time.DateTime
import zio.Task
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing

trait PgMessageRepo extends MessageAlgebra {

  protected def xa: Transactor[Task]

  protected def telemetry: OpenTracing.Service

  override val messageAlgebra: MessageAlgebra.Service[Any] = new MessageAlgebra.Service[Any] {
    override def one(id: MessageId)(implicit parent: Span): Task[Option[Message]] =
      (for {
        span         <- telemetry.childOf(parent, "DB: find by ID")
        query        = SQL.one(id)
        _            = span.setTag(Tags.DB_TYPE, "sql")
        _            = span.setTag(Tags.DB_STATEMENT, query.sql)
        maybeMessage <- query.option.transact(xa)
        _            = span.finish()
      } yield maybeMessage).provide(Clock.Live)

    override def findByUserIdAndContactId(
      userId: UserId,
      contactId: ContactId,
      offset: Offset,
      limit: Limit
    )(implicit parent: Span): Task[List[Message]] =
      (for {
        span     <- telemetry.childOf(parent, "DB: find by user ID and contact ID")
        query    = SQL.list(userId, contactId, offset, limit)
        _        = span.setTag(Tags.DB_TYPE, "sql")
        _        = span.setTag(Tags.DB_STATEMENT, query.sql)
        messages <- query.to[List].transact(xa)
        _        = span.finish()
      } yield messages).provide(Clock.Live)

    override def save(
      userId: UserId,
      contactId: ContactId,
      body: String
    )(implicit parent: Span): Task[Either[Throwable, MessageId]] =
      (for {
        span  <- telemetry.childOf(parent, "DB: create message")
        query = SQL.create(userId, contactId, body)
        _     = span.setTag(Tags.DB_TYPE, "sql")
        _     = span.setTag(Tags.DB_STATEMENT, query.sql)
        messageId <- query
                      .withUniqueGeneratedKeys[Long]("id")
                      .attemptSql
                      .map(_.right.map(MessageId.apply))
                      .transact(xa)
        _ = span.finish()
      } yield messageId).provide(Clock.Live)
  }

}

object PgMessageRepo {

  object SQL {

    implicit val jodaDateTimeMeta: Meta[DateTime] =
      Meta[JDate].imap(ts => new DateTime(ts))(dt => new JDate(dt.getMillis))

    def one(id: MessageId): Query0[Message] =
      sql"""SELECT * FROM messages WHERE id = $id;""".query[Message]

    def list(userId: UserId, contactId: ContactId, offset: Offset, limit: Limit): Query0[Message] =
      sql"""SELECT * FROM messages WHERE user_id = $userId AND contact_id = $contactId ORDER BY sent_at DESC OFFSET $offset LIMIT $limit;"""
        .query[Message]

    def create(userId: UserId, contactId: ContactId, body: String): Update0 =
      sql"""INSERT INTO messages (user_id, contact_id, body) VALUES ($userId, $contactId, $body);""".update
  }

}
