package com.github.drmarjanovic.commons.api

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class Error(status: String, detail: String)

object Error {
  implicit val decoder: Decoder[Error] = deriveDecoder
  implicit val encoder: Encoder[Error] = deriveEncoder
}
