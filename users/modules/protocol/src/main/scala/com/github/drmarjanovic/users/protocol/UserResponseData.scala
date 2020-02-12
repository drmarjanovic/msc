package com.github.drmarjanovic.users.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UserResponseData(`type`: String = UsersType, id: Long, attributes: UserResponseAttributes)

object UserResponseData {
  implicit val decoder: Decoder[UserResponseData] = deriveDecoder
  implicit val encoder: Encoder[UserResponseData] = deriveEncoder
}
