package com.github.drmarjanovic.contacts.domain

import scala.util.Try

final case class UserId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object UserId {

  implicit def userId2Long(userId: UserId): Long = userId.underlying

  def of(id: String): Option[UserId] = Try(UserId(id.toLong)).toOption

}
