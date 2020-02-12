package com.github.drmarjanovic.updates.api

import com.github.drmarjanovic.commons.api.errors._
import com.github.drmarjanovic.updates.{ AppEnv, AppTask }
import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.dsl.Http4sDsl
import org.http4s.{ DecodeFailure, EntityDecoder, EntityEncoder, Response }
import zio.interop.catz._
import zio.{ RIO, ZIO }

trait Routes {

  implicit def decoder[A: Decoder]: EntityDecoder[AppTask, A] = jsonOf[AppTask, A]

  implicit def encoder[A: Encoder]: EntityEncoder[AppTask, A] = jsonEncoderOf[AppTask, A]

  def handleFailures[A](zio: ZIO[AppEnv, Any, A])(implicit encoder: Encoder[A],
                                                  w: EntityEncoder[AppTask, Json]): RIO[AppEnv, Response[AppTask]] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    zio.foldM(
      {
        case e: DecodeFailure               => BadRequest(MalformedRequest(e.getMessage).toResponse.asJson)
        case e: WrongCredentials            => Forbidden(e.toResponse.asJson)
        case e: EntityNotFound              => NotFound(e.toResponse.asJson)
        case e: ServiceCommunicationFailure => InternalServerError(e.toResponse.asJson)
        case e: Throwable                   => InternalServerError(UnexpectedError(e.getMessage).toResponse.asJson)
      },
      entity => Ok(entity.asJson)
    )
  }

}
