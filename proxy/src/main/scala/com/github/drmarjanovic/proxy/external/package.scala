package com.github.drmarjanovic.proxy

import com.github.drmarjanovic.contacts.protocol.{ContactCollectionResponse, ContactResponseData}
import com.github.drmarjanovic.messages.protocol.{MessageCollectionResponse, MessageResponseData}
import com.github.drmarjanovic.proxy.model._
import com.github.drmarjanovic.updates.protocol.{UpdateCollectionResponse, UpdateResponse, UpdateResponseData}
import com.github.drmarjanovic.users.protocol.UserResponse
import org.joda.time.DateTime

package object external {

  implicit class ContactResponseDataToDomain(data: ContactResponseData) {
    def toDomain: Contact = {
      val attrs = data.attributes
      Contact(
        id = data.id,
        firstName = attrs.firstName,
        lastName = attrs.lastName,
        email = attrs.email,
        mobile = attrs.mobile,
        isVip = attrs.vip
      )
    }
  }

  implicit class ContactResponseCollectionToDomain(response: ContactCollectionResponse) {
    def toDomain: List[Contact] = response.data.map(_.toDomain)
  }

  implicit class MessageResponseDataToDomain(data: MessageResponseData) {
    def toDomain: Message = {
      val attrs = data.attributes
      Message(
        id = data.id,
        body = attrs.body,
        sentAt = DateTime.parse(attrs.sentAt).toString()
      )
    }
  }

  implicit class MessageResponseCollectionToDomain(response: MessageCollectionResponse) {
    def toDomain: List[Message] = response.data.map(_.toDomain)
  }

  implicit class UpdateResponseDataToDomain(data: UpdateResponseData) {
    def toDomain: Update = {
      val attrs = data.attributes
      Update(
        id = data.id,
        to = UpdateType.of(attrs.`type`),
        message = attrs.message,
        createdAt = DateTime.parse(attrs.createdAt).toString()
      )
    }
  }

  implicit class UpdateResponseToDomain(response: UpdateResponse) {
    def toDomain: Update = response.data.toDomain
  }

  implicit class UpdateResponseCollectionToDomain(response: UpdateCollectionResponse) {
    def toDomain: List[Update] = response.data.map(_.toDomain)
  }

  implicit class UserResponseToDomain(response: UserResponse) {
    def toDomain: User = {
      val attrs = response.data.attributes
      User(
        id = response.data.id,
        firstName = attrs.firstName,
        lastName = attrs.lastName,
        email = attrs.email
      )
    }
  }

}
