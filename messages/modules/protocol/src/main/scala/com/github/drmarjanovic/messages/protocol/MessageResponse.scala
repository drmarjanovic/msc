package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageResponse(data: MessageResponseData)

object MessageResponse {
  implicit val encoder: Encoder[MessageResponse] = deriveEncoder
  implicit val decoder: Decoder[MessageResponse] = deriveDecoder
}
