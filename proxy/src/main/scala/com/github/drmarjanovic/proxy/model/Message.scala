package com.github.drmarjanovic.proxy.model

import caliban.schema.Annotations.GQLDescription
import org.joda.time.DateTime

@GQLDescription("Represents `Message` datatype.")
final case class Message(
  @GQLDescription("Unique identifier of the message.")
  id: Long,
  // contactId: Long,
  @GQLDescription("Body of the message.")
  body: String,
  @GQLDescription("Identifies the date and time when the message was sent.")
  sentAt: String // FIXME - Use DateTime
)
