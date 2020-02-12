package com.github.drmarjanovic.updates

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import com.github.drmarjanovic.commons.api.{ CollectionLinks, Limit, LimitQueryParamKey, Offset, OffsetQueryParamKey }
import com.github.drmarjanovic.updates.domain.{ Update, UserId }
import com.github.drmarjanovic.updates.protocol.{
  UpdateCollectionResponse,
  UpdateResponse,
  UpdateResponseAttributes,
  UpdateResponseData
}
import org.http4s.Uri

package object api {

  object UserIdVar {
    def unapply(id: String): Option[UserId] = UserId.of(id)
  }

  implicit class UriDecoder(uri: Uri) {
    def decoded: String = URLDecoder.decode(uri.renderString, StandardCharsets.UTF_8.toString)
  }

  implicit class UpdateResponseEncoder(update: Update) {
    def toResponse: UpdateResponse =
      UpdateResponse(
        data = UpdateResponseData(
          id = update.id,
          attributes = UpdateResponseAttributes(update.message, update.`type`.toString, update.createdAt.toString)
        )
      )
  }

  implicit class MessageCollectionResponseEncoder(underlying: List[Update]) {
    def withLinks(uri: Uri, offset: Offset, limit: Limit): UpdateCollectionResponse = {
      val next = Uri
        .unsafeFromString(uri.path)
        .withQueryParam(OffsetQueryParamKey, offset.withLimit(limit).toQueryParam)
        .withQueryParam(LimitQueryParamKey, limit.toQueryParam)
        .decoded

      UpdateCollectionResponse(
        data = underlying.map { u =>
          UpdateResponseData(
            id = u.id,
            attributes = UpdateResponseAttributes(u.message, u.`type`.toString, u.createdAt.toString)
          )
        },
        links = CollectionLinks(uri.decoded, Some(next))
      )
    }
  }

}
