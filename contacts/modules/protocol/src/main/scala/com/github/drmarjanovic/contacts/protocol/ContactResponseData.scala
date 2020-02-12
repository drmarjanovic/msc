package com.github.drmarjanovic.contacts.protocol

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class ContactResponseData(`type`: String = ContactsType, id: Long, attributes: ContactResponseAttributes)

object ContactResponseData {
  implicit val decoder: Decoder[ContactResponseData] = deriveDecoder
  implicit val encoder: Encoder[ContactResponseData] = deriveEncoder
}
