package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageRequestAttributes(body: String)

object MessageRequestAttributes {
  implicit val decoder: Decoder[MessageRequestAttributes] = deriveDecoder
  implicit val encoder: Encoder[MessageRequestAttributes] = deriveEncoder
}
