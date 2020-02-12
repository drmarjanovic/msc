package com.github.drmarjanovic.updates.domain

final case class UpdateId(underlying: Long) extends AnyVal {
  override def toString: String = underlying.toString
}

object UpdateId {

  implicit def updateId2Long(updateId: UpdateId): Long = updateId.underlying

}
