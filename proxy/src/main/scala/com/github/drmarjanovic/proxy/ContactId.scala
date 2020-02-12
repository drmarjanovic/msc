package com.github.drmarjanovic.proxy

final case class ContactId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object ContactId {

  implicit def contactId2String(contactId: ContactId): String = contactId.toString

}