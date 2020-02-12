package com.github.drmarjanovic.contacts

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import com.github.drmarjanovic.commons.api.{ CollectionLinks, Limit, LimitQueryParamKey, Offset, OffsetQueryParamKey }
import com.github.drmarjanovic.contacts.domain.{ Contact, UserId }
import com.github.drmarjanovic.contacts.protocol.{
  ContactCollectionResponse,
  ContactResponseAttributes,
  ContactResponseData
}
import org.http4s.Uri

package object api {

  object UserIdVar {
    def unapply(id: String): Option[UserId] = UserId.of(id)
  }

  implicit class UriDecoder(uri: Uri) {
    def decoded: String = URLDecoder.decode(uri.renderString, StandardCharsets.UTF_8.toString)
  }

  implicit class ContactCollectionResponseEncoder(underlying: List[Contact]) {
    def withLinks(uri: Uri, offset: Offset, limit: Limit): ContactCollectionResponse = {
      val next = Uri
        .unsafeFromString(uri.path)
        .withQueryParam(OffsetQueryParamKey, offset.withLimit(limit).toQueryParam)
        .withQueryParam(LimitQueryParamKey, limit.toQueryParam)
        .decoded

      ContactCollectionResponse(
        data = underlying.map { c =>
          ContactResponseData(
            id = c.id,
            attributes = ContactResponseAttributes(c.firstName, c.lastName, c.email, c.mobile, c.vip)
          )
        },
        links = CollectionLinks(uri.decoded, Some(next))
      )
    }
  }

}
