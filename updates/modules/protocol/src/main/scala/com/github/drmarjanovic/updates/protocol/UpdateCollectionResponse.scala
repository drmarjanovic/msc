package com.github.drmarjanovic.updates.protocol

import com.github.drmarjanovic.commons.api.CollectionLinks
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class UpdateCollectionResponse(data: List[UpdateResponseData], links: CollectionLinks)

object UpdateCollectionResponse {
  implicit val decoder: Decoder[UpdateCollectionResponse] = deriveDecoder
  implicit val encoder: Encoder[UpdateCollectionResponse] = deriveEncoder
}
