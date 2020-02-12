package com.github.drmarjanovic.users.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class AuthResponse(token: String)

object AuthResponse {
  implicit val decoder: Decoder[AuthResponse] = deriveDecoder
  implicit val encoder: Encoder[AuthResponse] = deriveEncoder
}
