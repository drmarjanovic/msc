package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageResponseAttributes(body: String, sentAt: String)

object MessageResponseAttributes {
  implicit val encoder: Encoder[MessageResponseAttributes] = deriveEncoder
  implicit val decoder: Decoder[MessageResponseAttributes] = deriveDecoder
}
