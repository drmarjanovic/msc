package com.github.drmarjanovic.users.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UserResponseAttributes(firstName: String, lastName: String, email: String)

object UserResponseAttributes {
  implicit val decoder: Decoder[UserResponseAttributes] = deriveDecoder
  implicit val encoder: Encoder[UserResponseAttributes] = deriveEncoder
}
