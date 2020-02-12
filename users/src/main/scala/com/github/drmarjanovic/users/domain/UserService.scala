package com.github.drmarjanovic.users.domain

import io.opentracing.Span
import zio.{ RIO, ZIO }

final class UserService extends UserAlgebra.Service[UserAlgebra] {

  override def one(id: UserId)(implicit parent: Span): RIO[UserAlgebra, Option[User]] =
    ZIO.accessM(_.userAlgebra.one(id))

  override def findByEmailAndPassword(
    email: String,
    password: String
  )(implicit parent: Span): RIO[UserAlgebra, Option[User]] =
    ZIO.accessM(_.userAlgebra.findByEmailAndPassword(email, password))

}
