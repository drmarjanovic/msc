package com.github.drmarjanovic.proxy.model

import caliban.schema.Annotations.GQLDescription
import org.joda.time.DateTime

@GQLDescription("Represents `Update` datatype.")
final case class Update(
  @GQLDescription("Unique identifier of the update.")
  id: Long,
  @GQLDescription("Represents update specified subscribers type.")
  to: UpdateType,
  @GQLDescription("Message used in the update.")
  message: String,
  @GQLDescription("Identifies the date and time when the update was created.")
  createdAt: String // FIXME - Use DateTime
)
