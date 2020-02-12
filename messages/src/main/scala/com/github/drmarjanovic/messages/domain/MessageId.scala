package com.github.drmarjanovic.messages.domain

final case class MessageId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object MessageId {

  implicit def messageId2Long(messageId: MessageId): Long = messageId.underlying

}
