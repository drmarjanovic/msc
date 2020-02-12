package com.github.drmarjanovic.users.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UserResponse(data: UserResponseData)

object UserResponse {
  implicit val encoder: Encoder[UserResponse] = deriveEncoder
  implicit val decoder: Decoder[UserResponse] = deriveDecoder
}
