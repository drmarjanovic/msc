package com.github.drmarjanovic.proxy.services

import com.github.drmarjanovic.proxy.{AppEnv, UserId}
import com.github.drmarjanovic.proxy.config.Config.UpdatesServiceConfig
import com.github.drmarjanovic.proxy.external._
import com.github.drmarjanovic.proxy.model.{Update, UpdateType}
import com.github.drmarjanovic.tracing.TracingHelper._
import com.github.drmarjanovic.tracing._
import io.opentracing.propagation.Format.Builtin.{HTTP_HEADERS => HttpHeadersFormat}
import io.opentracing.propagation.TextMapAdapter
import sttp.model.Uri
import zio.{UIO, URIO, ZIO}

import scala.collection.mutable
import scala.jdk.CollectionConverters._

final class Updates(conf: UpdatesServiceConfig) {

  def get(userId: UserId, offset: Long, limit: Long): URIO[AppEnv, List[Update]] =
    (for {
      span    <- ZIO.accessM[AppEnv](_.telemetry.root("updates"))
      _       = span.setTag(GRAPHQL_OPERATION_NAME, "updates")
      buffer  <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _       <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers <- extractTracingHeaders(buffer)
      url     <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res     <- UpdatesProxy.getUpdates(userId, url, headers).map(_.body)
      _       = span.finish()
      a       <- res.fold(_ => ZIO.fail("Failed retrieving updates."), r => ZIO.succeed(r.toDomain))
    } yield a).foldM(_ => URIO.succeed(List.empty), b => URIO.succeed(b))

  def create(userId: UserId, to: UpdateType, body: String): URIO[AppEnv, Option[Update]] =
    (for {
      span    <- ZIO.accessM[AppEnv](_.telemetry.root("send update"))
      _       = span.setTag(GRAPHQL_OPERATION_NAME, "sendUpdate")
      buffer  <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _       <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers <- extractTracingHeaders(buffer)
      url     <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res     <- UpdatesProxy.sendUpdate(userId, body, to, url, headers).map(_.body)
      _       = span.finish()
      a       <- res.fold(_ => ZIO.fail("Failed retrieving update."), r => ZIO.succeed(Some(r.toDomain)))
    } yield a).foldM(_ => URIO.succeed(None), b => URIO.succeed(b))

}
