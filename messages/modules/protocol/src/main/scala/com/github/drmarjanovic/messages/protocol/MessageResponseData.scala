package com.github.drmarjanovic.messages.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageResponseData(`type`: String = MessagesType, id: Long, attributes: MessageResponseAttributes)

object MessageResponseData {
  implicit val encoder: Encoder[MessageResponseData] = deriveEncoder
  implicit val decoder: Decoder[MessageResponseData] = deriveDecoder
}
