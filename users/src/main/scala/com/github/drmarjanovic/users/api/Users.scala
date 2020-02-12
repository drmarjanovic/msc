package com.github.drmarjanovic.users.api

import com.github.drmarjanovic.commons.api.errors.{ EntityNotFound, WrongCredentials }
import com.github.drmarjanovic.tracing.TracingHelper._
import com.github.drmarjanovic.tracing._
import com.github.drmarjanovic.users.config.Config.ApplicationSecret
import com.github.drmarjanovic.users.domain.{ Bearer, UserService }
import com.github.drmarjanovic.users.protocol._
import com.github.drmarjanovic.users.{ AppEnv, AppTask }
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.{ TextMap, TextMapAdapter }
import io.opentracing.tag.Tags.{ HTTP_METHOD, HTTP_URL }
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio.ZIO
import zio.interop.catz._

import scala.jdk.CollectionConverters._

final class Users(secret: ApplicationSecret, service: UserService) extends Routes {

  def routes(): HttpRoutes[AppTask] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    HttpRoutes.of[AppTask] {
      case req @ POST -> Root / "users" / "auth" =>
        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          spec <- req.as[AuthRequest]
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "auth user")
                 )
          _         = span.setTag(HTTP_URL, req.uri.renderString)
          _         = span.setTag(HTTP_METHOD, POST.name)
          maybeUser <- service.findByEmailAndPassword(spec.email, spec.password)(span)
          bearer <- maybeUser.foldZ(WrongCredentials("You've provided wrong credentials."))(span).map { u =>
                     span.log(now(), s"User ${spec.email} successfully authenticated.")
                     Bearer.of(secret, u)
                   }
          _ = span.setTag(HTTP_STATUS, Ok.code)
          _ = span.finish()
        } yield bearer.toResponse

        handleFailures[AuthResponse](zio)

      case req @ GET -> Root / "users" / UserIdVar(userId) =>
        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "retrieve user")
                 )
          _         = span.setTag(HTTP_URL, req.uri.renderString)
          _         = span.setTag(HTTP_METHOD, GET.name)
          maybeUser <- service.one(userId)(span)
          user <- maybeUser.foldZ(EntityNotFound("User", userId))(span).map { u =>
                   span.log(now(), s"User ${u.email} successfully retrieved.")
                   u
                 }
          _ = span.setTag(HTTP_STATUS, Ok.code)
          _ = span.finish()
        } yield user.toResponse

        handleFailures[UserResponse](zio)
    }
  }

}
