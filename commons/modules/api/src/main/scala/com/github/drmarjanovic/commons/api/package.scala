package com.github.drmarjanovic.commons

import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

package object api {

  val LimitQueryParamKey  = "page[limit]"
  val OffsetQueryParamKey = "page[offset]"

  implicit val limitQueryParamDecoder: QueryParamDecoder[Limit]   = QueryParamDecoder[Long].map(Limit.apply)
  implicit val offsetQueryParamDecoder: QueryParamDecoder[Offset] = QueryParamDecoder[Long].map(Offset.apply)

  object LimitQueryParam extends OptionalQueryParamDecoderMatcher[Limit](LimitQueryParamKey)

  object OffsetQueryParam extends OptionalQueryParamDecoderMatcher[Offset](OffsetQueryParamKey)

  def getPaginationParams(offset: Option[Offset], limit: Option[Limit]): (Offset, Limit) =
    (offset.fold(Offset.Default)(identity), limit.fold(Limit.Default)(identity))

}
