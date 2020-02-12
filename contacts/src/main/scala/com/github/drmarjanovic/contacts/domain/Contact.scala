package com.github.drmarjanovic.contacts.domain

final case class Contact(
  id: ContactId,
  userId: UserId,
  firstName: String,
  lastName: String,
  email: Option[String],
  mobile: Option[String],
  vip: Boolean
)
