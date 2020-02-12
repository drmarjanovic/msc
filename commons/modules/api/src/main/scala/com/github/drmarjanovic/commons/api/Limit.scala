package com.github.drmarjanovic.commons.api

final case class Limit(underlying: Long) extends AnyVal {

  def toQueryParam: String = underlying.toString

}

object Limit {

  implicit def limit2Long(limit: Limit): Long = limit.underlying

  implicit def limit2String(limit: Limit): String = limit.toString

  val Default = Limit(10)

}
