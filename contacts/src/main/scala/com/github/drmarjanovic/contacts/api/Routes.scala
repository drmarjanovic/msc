package com.github.drmarjanovic.contacts.api

import com.github.drmarjanovic.commons.api.errors.{ MalformedRequest, UnexpectedError }
import com.github.drmarjanovic.contacts.{ AppEnv, AppTask }
import io.circe.syntax._
import io.circe.{ Encoder, Json }
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher
import org.http4s.{ DecodeFailure, EntityEncoder, Response }
import zio.interop.catz._
import zio.{ RIO, ZIO }

trait Routes {

  private val FilterVipQueryParamKey    = "filter[vip]"
  private val FilterEmailQueryParamKey  = "filter[email]"
  private val FilterMobileQueryParamKey = "filter[mobile]"

  object FilterVipQueryParam    extends OptionalQueryParamDecoderMatcher[Boolean](FilterVipQueryParamKey)
  object FilterEmailQueryParam  extends OptionalQueryParamDecoderMatcher[Boolean](FilterEmailQueryParamKey)
  object FilterMobileQueryParam extends OptionalQueryParamDecoderMatcher[Boolean](FilterMobileQueryParamKey)

  implicit def encoder[A: Encoder]: EntityEncoder[AppTask, A] = jsonEncoderOf[AppTask, A]

  def handleFailures[A](zio: ZIO[AppEnv, Any, A])(implicit encoder: Encoder[A],
                                                  w: EntityEncoder[AppTask, Json]): RIO[AppEnv, Response[AppTask]] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    zio.foldM(
      {
        case e: DecodeFailure => BadRequest(MalformedRequest(e.getMessage).toResponse.asJson)
        case e: Throwable     => InternalServerError(UnexpectedError(e.getMessage).toResponse.asJson)
      },
      entity => Ok(entity.asJson)
    )
  }

}
