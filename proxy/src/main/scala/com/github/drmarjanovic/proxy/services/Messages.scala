package com.github.drmarjanovic.proxy.services

import com.github.drmarjanovic.proxy.config.Config.MessagesServiceConfig
import com.github.drmarjanovic.proxy.external._
import com.github.drmarjanovic.proxy.model.Message
import com.github.drmarjanovic.proxy.{ AppEnv, ContactId, UserId }
import com.github.drmarjanovic.tracing.TracingHelper.extractTracingHeaders
import com.github.drmarjanovic.tracing._
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.TextMapAdapter
import sttp.model.Uri
import zio.{ UIO, URIO, ZIO }

import scala.collection.mutable
import scala.jdk.CollectionConverters._

final class Messages(conf: MessagesServiceConfig) {

  def get(userId: UserId, contactId: ContactId): URIO[AppEnv, List[Message]] =
    (for {
      span    <- ZIO.accessM[AppEnv](_.telemetry.root("messages"))
      _       = span.setTag(GRAPHQL_OPERATION_NAME, "contact/messages")
      buffer  <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _       <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers <- extractTracingHeaders(buffer)
      url     <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res     <- MessagesProxy.getMessages(userId, contactId, url, headers).map(_.body)
      a       <- res.fold(_ => ZIO.fail("Failed retrieving messages."), r => ZIO.succeed(r.toDomain))
      _       = span.finish()
    } yield a).foldM(_ => URIO.succeed(List.empty), b => URIO.succeed(b))

}
