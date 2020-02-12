package com.github.drmarjanovic.contacts.protocol

import com.github.drmarjanovic.commons.api.CollectionLinks
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class ContactCollectionResponse(data: List[ContactResponseData], links: CollectionLinks)

object ContactCollectionResponse {
  implicit val decoder: Decoder[ContactCollectionResponse] = deriveDecoder
  implicit val encoder: Encoder[ContactCollectionResponse] = deriveEncoder
}
