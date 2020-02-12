package com.github.drmarjanovic.updates.domain

import org.joda.time.DateTime

final case class Update(
  id: UpdateId,
  userId: UserId,
  message: String,
  `type`: UpdateType,
  createdAt: DateTime
)
