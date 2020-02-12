package com.github.drmarjanovic.commons.api

final case class Offset(underlying: Long) extends AnyVal {

  def withLimit(limit: Limit): Offset = Offset(underlying + limit.underlying)

  def toQueryParam: String = underlying.toString

}

object Offset {

  implicit def offset2Long(offset: Offset): Long = offset.underlying

  implicit def offset2String(offset: Offset): String = offset.toString

  val Default = Offset(0)

}
