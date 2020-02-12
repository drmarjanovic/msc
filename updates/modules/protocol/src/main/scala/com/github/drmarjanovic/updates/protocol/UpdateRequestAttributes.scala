package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateRequestAttributes(message: String, `type`: String)

object UpdateRequestAttributes {
  implicit val decoder: Decoder[UpdateRequestAttributes] = deriveDecoder
  implicit val encoder: Encoder[UpdateRequestAttributes] = deriveEncoder
}
