package com.github.drmarjanovic.users.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class AuthRequest(email: String, password: String)

object AuthRequest {
  implicit val decoder: Decoder[AuthRequest] = deriveDecoder
  implicit val encoder: Encoder[AuthRequest] = deriveEncoder
}
