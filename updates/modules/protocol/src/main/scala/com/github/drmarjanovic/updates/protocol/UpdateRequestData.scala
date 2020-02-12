package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateRequestData(`type`: String, attributes: UpdateRequestAttributes)

object UpdateRequestData {
  implicit val decoder: Decoder[UpdateRequestData] = deriveDecoder
  implicit val encoder: Encoder[UpdateRequestData] = deriveEncoder
}
