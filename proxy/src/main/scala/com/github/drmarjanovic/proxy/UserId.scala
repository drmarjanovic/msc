package com.github.drmarjanovic.proxy

final case class UserId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object UserId {

  implicit def userId2String(userId: UserId): String = userId.toString

}
