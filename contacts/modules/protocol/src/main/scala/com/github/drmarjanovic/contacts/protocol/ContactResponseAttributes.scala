package com.github.drmarjanovic.contacts.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class ContactResponseAttributes(
  firstName: String,
  lastName: String,
  email: Option[String],
  mobile: Option[String],
  vip: Boolean
)

object ContactResponseAttributes {
  implicit val decoder: Decoder[ContactResponseAttributes] = deriveDecoder
  implicit val encoder: Encoder[ContactResponseAttributes] = deriveEncoder
}
