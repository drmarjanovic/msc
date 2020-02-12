package com.github.drmarjanovic.proxy.model

sealed trait ContactFilter

object ContactFilter {

  case object ALL extends ContactFilter

  case object VIP extends ContactFilter

  case object WITH_EMAIL extends ContactFilter

  case object WITH_MOBILE extends ContactFilter

}
