package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageRequest(data: MessageRequestData)

object MessageRequest {
  implicit val decoder: Decoder[MessageRequest] = deriveDecoder
  implicit val encoder: Encoder[MessageRequest] = deriveEncoder
}
