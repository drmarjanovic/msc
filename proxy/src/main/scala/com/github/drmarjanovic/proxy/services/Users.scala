package com.github.drmarjanovic.proxy.services

import com.github.drmarjanovic.proxy.config.Config.UsersServiceConfig
import com.github.drmarjanovic.proxy.external._
import com.github.drmarjanovic.proxy.model.{ BearerToken, User }
import com.github.drmarjanovic.proxy.{ AppEnv, UserId }
import com.github.drmarjanovic.tracing.TracingHelper.extractTracingHeaders
import com.github.drmarjanovic.tracing._
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.TextMapAdapter
import sttp.model.Uri
import zio.{ UIO, URIO, ZIO }

import scala.collection.mutable
import scala.jdk.CollectionConverters._

final class Users(conf: UsersServiceConfig) {

  def one(id: UserId): URIO[AppEnv, Option[User]] =
    (for {
      span    <- ZIO.accessM[AppEnv](_.telemetry.root("me"))
      _       = span.setTag(GRAPHQL_OPERATION_NAME, "me")
      buffer  <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _       <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers <- extractTracingHeaders(buffer)
      url     <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res     <- UsersProxy.getUser(id, url, headers).map(_.body)
      a       <- res.fold(_ => ZIO.fail("Failed retrieving user."), r => ZIO.succeed(Some(r.toDomain)))
      _       = span.finish()
    } yield a).foldM(_ => URIO.succeed(None), b => URIO.succeed(b))

  def auth(email: String, password: String): URIO[AppEnv, Option[BearerToken]] =
    (for {
      span    <- ZIO.accessM[AppEnv](_.telemetry.root("auth"))
      _       = span.setTag(GRAPHQL_OPERATION_NAME, "auth")
      buffer  <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _       <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers <- extractTracingHeaders(buffer)
      url     <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res     <- UsersProxy.auth(email, password, url, headers).map(_.body)
      _       = span.finish()
      a       <- res.fold(_ => ZIO.fail("Failed auth."), r => ZIO.succeed(Some(BearerToken(token = r.token))))
    } yield a).foldM(_ => URIO.succeed(None), b => URIO.succeed(b))

}
