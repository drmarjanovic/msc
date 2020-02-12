package com.github.drmarjanovic.contacts.domain

final case class ContactId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object ContactId {

  implicit def contactId2Long(contactId: ContactId): Long = contactId.underlying

}
