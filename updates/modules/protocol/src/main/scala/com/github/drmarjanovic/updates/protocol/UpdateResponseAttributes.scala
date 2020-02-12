package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateResponseAttributes(message: String, `type`: String, createdAt: String)

object UpdateResponseAttributes {
  implicit val decoder: Decoder[UpdateResponseAttributes] = deriveDecoder
  implicit val encoder: Encoder[UpdateResponseAttributes] = deriveEncoder
}
