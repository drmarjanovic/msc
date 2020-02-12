package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateResponse(data: UpdateResponseData)

object UpdateResponse {
  implicit val decoder: Decoder[UpdateResponse] = deriveDecoder
  implicit val encoder: Encoder[UpdateResponse] = deriveEncoder
}
