package com.github.drmarjanovic.proxy.model

import caliban.schema.Annotations.GQLDescription

@GQLDescription("Represents `User` datatype.")
final case class User(
  @GQLDescription("Unique identifier of the user.")
  id: Long,
  @GQLDescription("First name of the user.")
  firstName: String,
  @GQLDescription("Last name of the user.")
  lastName: String,
  @GQLDescription("Email address of the user.")
  email: String
)
