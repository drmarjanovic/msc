package com.github.drmarjanovic.users.domain

import io.opentracing.Span
import zio.RIO

trait UserAlgebra extends Serializable {
  val userAlgebra: UserAlgebra.Service[Any]
}

object UserAlgebra {

  trait Service[R] extends Serializable {

    def one(id: UserId)(implicit parent: Span): RIO[R, Option[User]]

    def findByEmailAndPassword(email: String, password: String)(implicit parent: Span): RIO[R, Option[User]]

  }

}
