package com.github.drmarjanovic.proxy

package object model {

  // Queries arguments
  final case class ContactsArgs(filters: List[ContactFilter], offset: Long = 0, limit: Long = 10)

  final case class MessagesArgs(offset: Long = 0, limit: Long = 10)

  final case class UpdatesArgs(offset: Long = 0, limit: Long = 10)

  // Mutations arguments
  final case class AuthArgs(email: String, password: String)

  final case class SendUpdateArgs(to: UpdateType, body: String)

}
