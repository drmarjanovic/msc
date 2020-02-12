package com.github.drmarjanovic.messages.domain

import scala.util.Try

final case class ContactId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object ContactId {

  implicit def contactId2Long(contactId: ContactId): Long = contactId.underlying

  def of(id: String): Option[ContactId] = Try(ContactId(id.toLong)).toOption

}
