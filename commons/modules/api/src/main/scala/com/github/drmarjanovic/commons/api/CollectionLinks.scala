package com.github.drmarjanovic.commons.api

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class CollectionLinks(self: String, next: Option[String])

object CollectionLinks {
  implicit val decoder: Decoder[CollectionLinks] = deriveDecoder
  implicit val encoder: Encoder[CollectionLinks] = deriveEncoder
}
