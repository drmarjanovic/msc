package com.github.drmarjanovic.messages.domain

import com.github.drmarjanovic.commons.api.{Limit, Offset}
import io.opentracing.Span
import zio.{RIO, ZIO}

final class MessageService extends MessageAlgebra.Service[MessageAlgebra] {

  override def one(id: MessageId)(implicit parent: Span): RIO[MessageAlgebra, Option[Message]] =
    ZIO.accessM(_.messageAlgebra.one(id))

  override def findByUserIdAndContactId(
    userId: UserId,
    contactId: ContactId,
    offset: Offset,
    limit: Limit
  )(implicit parent: Span): RIO[MessageAlgebra, List[Message]] =
    ZIO.accessM(_.messageAlgebra.findByUserIdAndContactId(userId, contactId, offset, limit))

  override def save(
    userId: UserId,
    contactId: ContactId,
    body: String
  )(implicit parent: Span): RIO[MessageAlgebra, Either[Throwable, MessageId]] =
    ZIO.accessM(_.messageAlgebra.save(userId, contactId, body))

}
