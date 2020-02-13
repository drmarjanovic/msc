package com.github.drmarjanovic.proxy

import caliban.schema.Schema
import org.joda.time.DateTime

package object model {

  implicit val dateTimeSchema: Schema[Any, DateTime] = Schema.stringSchema.contramap(_.toString)

  // Queries arguments
  final case class ContactsArgs(filters: List[ContactFilter], offset: Long = 0, limit: Long = 10)

  final case class MessagesArgs(offset: Long = 0, limit: Long = 10)

  final case class UpdatesArgs(offset: Long = 0, limit: Long = 10)

  // Mutations arguments
  final case class AuthArgs(email: String, password: String)

  final case class SendUpdateArgs(to: UpdateType, body: String)

}
