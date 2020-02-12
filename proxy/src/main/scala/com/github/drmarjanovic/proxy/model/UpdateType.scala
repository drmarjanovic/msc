package com.github.drmarjanovic.proxy.model

sealed trait UpdateType {
  override def toString: String = {
    import UpdateType._
    this match {
      case ALL         => "all"
      case VIPS        => "vip"
      case WITH_EMAIL  => "email"
      case WITH_MOBILE => "mobile"
    }
  }
}

object UpdateType {

  case object ALL extends UpdateType

  case object VIPS extends UpdateType

  case object WITH_EMAIL extends UpdateType

  case object WITH_MOBILE extends UpdateType

}
