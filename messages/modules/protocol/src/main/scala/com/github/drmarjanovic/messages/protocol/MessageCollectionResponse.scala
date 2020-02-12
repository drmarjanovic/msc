package com.github.drmarjanovic.messages.protocol

import com.github.drmarjanovic.commons.api.CollectionLinks
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class MessageCollectionResponse(data: List[MessageResponseData], links: CollectionLinks)

object MessageCollectionResponse {
  implicit val decoder: Decoder[MessageCollectionResponse] = deriveDecoder
  implicit val encoder: Encoder[MessageCollectionResponse] = deriveEncoder
}
