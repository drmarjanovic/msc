package com.github.drmarjanovic.proxy.model

import caliban.schema.Annotations.GQLDescription

@GQLDescription("Represents `Contact` datatype.")
final case class Contact(
  @GQLDescription("Unique identifier of the contact.")
  id: Long,
  @GQLDescription("First name of the contact.")
  firstName: String,
  @GQLDescription("Last name of the contact.")
  lastName: String,
  @GQLDescription("Email address of the contact.")
  email: Option[String],
  @GQLDescription("Mobile number of the contact.")
  mobile: Option[String],
  @GQLDescription("Indicates whether contact is VIP or not.")
  isVip: Boolean //,
  //  @GQLDescription("List of messages for specified contact.")
  //  messages: MyQuery[List[Message]]
)
