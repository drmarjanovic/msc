package com.github.drmarjanovic.commons.api

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class ErrorResponse(errors: List[Error], meta: ErrorMeta = ErrorMeta())

object ErrorResponse {
  implicit val decoder: Decoder[ErrorResponse] = deriveDecoder
  implicit val encoder: Encoder[ErrorResponse] = deriveEncoder
}
