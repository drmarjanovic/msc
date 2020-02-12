package com.github.drmarjanovic.contacts.domain

import com.github.drmarjanovic.commons.api.{ Limit, Offset }
import io.opentracing.Span
import zio.RIO

trait ContactAlgebra extends Serializable {
  val contactAlgebra: ContactAlgebra.Service[Any]
}

object ContactAlgebra {

  trait Service[R] extends Serializable {

    def findByUserId(
      userId: UserId,
      filters: List[Filter],
      offset: Offset,
      limit: Limit
    )(implicit parent: Span): RIO[R, List[Contact]]

  }

}
