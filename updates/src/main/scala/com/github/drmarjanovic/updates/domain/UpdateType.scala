package com.github.drmarjanovic.updates.domain

sealed trait UpdateType

object UpdateType {

  case object AllContacts extends UpdateType

  case object VipContacts extends UpdateType

  case object ContactsWithEmail extends UpdateType

  case object ContactsWithMobile extends UpdateType

  def fromReq(value: String): UpdateType =
    value match {
      case "vip"    => VipContacts
      case "email"  => ContactsWithEmail
      case "mobile" => ContactsWithMobile
      case _        => AllContacts
    }

  def of(value: String): UpdateType =
    value match {
      case "VipContacts"        => VipContacts
      case "ContactsWithEmail"  => ContactsWithEmail
      case "ContactsWithMobile" => ContactsWithMobile
      case _                    => AllContacts
    }
}
