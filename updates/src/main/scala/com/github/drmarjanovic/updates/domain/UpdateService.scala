package com.github.drmarjanovic.updates.domain

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import io.opentracing.Span
import zio.{ RIO, ZIO }

final class UpdateService extends UpdateAlgebra.Service[UpdateAlgebra] {

  override def one(id: UpdateId)(implicit parent: Span): RIO[UpdateAlgebra, Option[Update]] =
    ZIO.accessM(_.updateAlgebra.one(id))

  override def findByUserId(
    userId: UserId,
    offset: Offset,
    limit: Limit
  )(implicit parent: Span): RIO[UpdateAlgebra, List[Update]] =
    ZIO.accessM(_.updateAlgebra.findByUserId(userId, offset, limit))

  override def save(
    userId: UserId,
    `type`: UpdateType,
    message: String
  )(implicit parent: Span): RIO[UpdateAlgebra, Either[Throwable, UpdateId]] =
    ZIO.accessM(_.updateAlgebra.save(userId, `type`, message))

}
