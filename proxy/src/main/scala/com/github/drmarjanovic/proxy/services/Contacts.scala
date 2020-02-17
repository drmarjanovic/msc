package com.github.drmarjanovic.proxy.services

import com.github.drmarjanovic.proxy.config.Config.ContactsServiceConfig
import com.github.drmarjanovic.proxy.external._
import com.github.drmarjanovic.proxy.model.{ Contact, ContactFilter }
import com.github.drmarjanovic.proxy.{ AppEnv, UserId }
import com.github.drmarjanovic.tracing.TracingHelper._
import com.github.drmarjanovic.tracing._
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.TextMapAdapter
import sttp.model.Uri
import zio.{ UIO, URIO, ZIO }

import scala.collection.mutable
import scala.jdk.CollectionConverters._

final class Contacts(conf: ContactsServiceConfig) {

  def get(userId: UserId, filters: List[ContactFilter], offset: Long, limit: Long): URIO[AppEnv, List[Contact]] =
    (for {
      span     <- ZIO.accessM[AppEnv](_.telemetry.root("contacts"))
      _        = span.setTag(GRAPHQL_OPERATION_NAME, "contacts")
      buffer   <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
      _        <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
      headers  <- extractTracingHeaders(buffer)
      url      <- ZIO.fromEither(Uri.safeApply(conf.host, conf.port))
      res      <- ContactsProxy.getContacts(userId, url, headers).catchAll(span.failed).map(_.body)
      contacts <- res.fold(span.failed, r => ZIO.succeed(r.toDomain))
      _        = span.finish()
    } yield contacts).foldM(_ => URIO.succeed(List.empty), URIO.succeed)

}
