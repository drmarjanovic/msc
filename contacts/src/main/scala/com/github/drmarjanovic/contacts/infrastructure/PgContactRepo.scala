package com.github.drmarjanovic.contacts.infrastructure

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import com.github.drmarjanovic.contacts.domain.Filter.{ HasEmail, HasMobile, Vip }
import com.github.drmarjanovic.contacts.domain._
import com.github.drmarjanovic.contacts.infrastructure.PgContactRepo.SQL
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import io.opentracing.Span
import io.opentracing.tag.Tags
import zio.clock.Clock
import zio.interop.catz._
import zio.telemetry.opentracing.OpenTracing
import zio.{ RIO, Task }

trait PgContactRepo extends ContactAlgebra {

  protected def xa: Transactor[Task]

  protected def telemetry: OpenTracing.Service

  override val contactAlgebra: ContactAlgebra.Service[Any] = new ContactAlgebra.Service[Any] {
    override def findByUserId(
      userId: UserId,
      filters: List[Filter],
      offset: Offset,
      limit: Limit
    )(implicit parent: Span): RIO[Any, List[Contact]] =
      (for {
        span     <- telemetry.childOf(parent, "DB: find by user ID")
        query    = SQL.list(userId, filters, offset, limit)
        _        = span.setTag(Tags.DB_TYPE, "sql")
        _        = span.setTag(Tags.DB_STATEMENT, query.sql)
        contacts <- query.to[List].transact(xa)
        _        = span.finish()
      } yield contacts).provide(Clock.Live)
  }
}

object PgContactRepo {

  object SQL {
    def list(userId: Long, filters: List[Filter], offset: Offset, limit: Limit): Query0[Contact] =
      (filters.map {
        case Vip(value)                => fr"AND vip = $value"
        case HasEmail(value) if value  => fr"AND email IS NOT NULL"
        case HasEmail(_)               => fr"AND email IS NULL"
        case HasMobile(value) if value => fr"AND mobile IS NOT NULL"
        case HasMobile(_)              => fr"AND mobile IS NULL"
      }.foldLeft(fr"SELECT * FROM contacts WHERE user_id = $userId")(_ ++ _) ++ fr"OFFSET $offset LIMIT $limit;")
        .query[Contact]
  }

}
