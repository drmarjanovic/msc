package com.github.drmarjanovic.updates.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateResponseData(`type`: String = UpdatesType, id: Long, attributes: UpdateResponseAttributes)

object UpdateResponseData {
  implicit val decoder: Decoder[UpdateResponseData] = deriveDecoder
  implicit val encoder: Encoder[UpdateResponseData] = deriveEncoder
}
