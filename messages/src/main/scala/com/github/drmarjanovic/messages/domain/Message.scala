package com.github.drmarjanovic.messages.domain

import org.joda.time.DateTime

final case class Message(id: MessageId, userId: UserId, contactId: ContactId, body: String, sentAt: DateTime)
