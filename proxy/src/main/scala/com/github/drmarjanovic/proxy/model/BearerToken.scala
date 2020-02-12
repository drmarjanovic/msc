package com.github.drmarjanovic.proxy.model

import caliban.schema.Annotations.GQLDescription
import com.github.drmarjanovic.proxy.model.BearerToken.Type

@GQLDescription("Represents `BearerToken` datatype.")
final case class BearerToken(
  @GQLDescription("Type of the token.")
  `type`: String = Type,
  @GQLDescription("Token used for authentication.")
  token: String
)

object BearerToken {
  private val Type = "Bearer"
}
