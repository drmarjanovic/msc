package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageRequestData(`type`: String, attributes: MessageRequestAttributes)

object MessageRequestData {
  implicit val decoder: Decoder[MessageRequestData] = deriveDecoder
  implicit val encoder: Encoder[MessageRequestData] = deriveEncoder
}
