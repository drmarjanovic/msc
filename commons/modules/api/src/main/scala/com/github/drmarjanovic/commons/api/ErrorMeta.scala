package com.github.drmarjanovic.commons.api

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import org.joda.time.DateTime

final case class ErrorMeta(createdAt: String = DateTime.now.toString)

object ErrorMeta {
  implicit val decoder: Decoder[ErrorMeta] = deriveDecoder
  implicit val encoder: Encoder[ErrorMeta] = deriveEncoder
}
