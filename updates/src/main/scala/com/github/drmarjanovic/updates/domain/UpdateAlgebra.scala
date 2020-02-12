package com.github.drmarjanovic.updates.domain

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import io.opentracing.Span
import zio.RIO

trait UpdateAlgebra extends Serializable {
  val updateAlgebra: UpdateAlgebra.Service[Any]
}

object UpdateAlgebra {

  trait Service[R] extends Serializable {

    def one(id: UpdateId)(implicit parent: Span): RIO[R, Option[Update]]

    def findByUserId(userId: UserId, offset: Offset, limit: Limit)(implicit parent: Span): RIO[R, List[Update]]

    def save(
      userId: UserId,
      `type`: UpdateType,
      message: String
    )(implicit parent: Span): RIO[R, Either[Throwable, UpdateId]]

  }

}
