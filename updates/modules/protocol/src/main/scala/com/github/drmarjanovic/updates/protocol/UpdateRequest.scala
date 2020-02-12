package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateRequest(data: UpdateRequestData)

object UpdateRequest {
  implicit val decoder: Decoder[UpdateRequest] = deriveDecoder
  implicit val encoder: Encoder[UpdateRequest] = deriveEncoder
}
