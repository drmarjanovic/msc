package com.github.drmarjanovic.contacts.domain

import com.github.drmarjanovic.commons.api.{Limit, Offset}
import io.opentracing.Span
import zio.{RIO, ZIO}

final class ContactService extends ContactAlgebra.Service[ContactAlgebra] {

  override def findByUserId(
    userId: UserId,
    filters: List[Filter],
    offset: Offset,
    limit: Limit
  )(implicit parent: Span): RIO[ContactAlgebra, List[Contact]] =
    ZIO.accessM(_.contactAlgebra.findByUserId(userId, filters, offset, limit))

}
