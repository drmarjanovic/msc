package com.github.drmarjanovic.messages

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import com.github.drmarjanovic.commons.api.{ CollectionLinks, Limit, LimitQueryParamKey, Offset, OffsetQueryParamKey }
import com.github.drmarjanovic.messages.domain.{ ContactId, Message, UserId }
import com.github.drmarjanovic.messages.protocol.{
  MessageCollectionResponse,
  MessageResponse,
  MessageResponseAttributes,
  MessageResponseData
}
import org.http4s.Uri

package object api {

  object UserIdVar {
    def unapply(id: String): Option[UserId] = UserId.of(id)
  }

  object ContactIdVar {
    def unapply(id: String): Option[ContactId] = ContactId.of(id)
  }

  implicit class UriDecoder(uri: Uri) {
    def decoded: String = URLDecoder.decode(uri.renderString, StandardCharsets.UTF_8.toString)
  }

  implicit class MessageResponseEncoder(message: Message) {
    def toResponse: MessageResponse =
      MessageResponse(
        data = MessageResponseData(
          id = message.id,
          attributes = MessageResponseAttributes(message.body, message.sentAt.toString)
        )
      )
  }

  implicit class MessageCollectionResponseEncoder(underlying: List[Message]) {
    def withLinks(uri: Uri, offset: Offset, limit: Limit): MessageCollectionResponse = {
      val next = Uri
        .unsafeFromString(uri.path)
        .withQueryParam(OffsetQueryParamKey, offset.withLimit(limit).toQueryParam)
        .withQueryParam(LimitQueryParamKey, limit.toQueryParam)
        .decoded

      MessageCollectionResponse(
        data = underlying.map { m =>
          MessageResponseData(
            id = m.id,
            attributes = MessageResponseAttributes(m.body, m.sentAt.toString)
          )
        },
        links = CollectionLinks(uri.decoded, Some(next))
      )
    }
  }

}
