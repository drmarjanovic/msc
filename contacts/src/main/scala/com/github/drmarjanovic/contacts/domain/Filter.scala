package com.github.drmarjanovic.contacts.domain

sealed trait Filter {
  val value: Boolean
}

object Filter {

  final case class Vip(value: Boolean) extends Filter

  final case class HasEmail(value: Boolean) extends Filter

  final case class HasMobile(value: Boolean) extends Filter

}
