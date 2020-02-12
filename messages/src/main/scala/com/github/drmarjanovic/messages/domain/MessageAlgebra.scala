package com.github.drmarjanovic.messages.domain

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import io.opentracing.Span
import zio.RIO

trait MessageAlgebra extends Serializable {
  val messageAlgebra: MessageAlgebra.Service[Any]
}

object MessageAlgebra {

  trait Service[R] extends Serializable {

    def one(id: MessageId)(implicit parent: Span): RIO[R, Option[Message]]

    def findByUserIdAndContactId(
      userId: UserId,
      contactId: ContactId,
      offset: Offset,
      limit: Limit
    )(implicit parent: Span): RIO[R, List[Message]]

    def save(
      userId: UserId,
      contactId: ContactId,
      body: String
    )(implicit parent: Span): RIO[R, Either[Throwable, MessageId]]

  }

}
