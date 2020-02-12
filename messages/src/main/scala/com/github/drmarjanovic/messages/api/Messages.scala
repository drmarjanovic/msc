package com.github.drmarjanovic.messages.api

import com.github.drmarjanovic.commons.api._
import com.github.drmarjanovic.commons.api.errors.MalformedRequest
import com.github.drmarjanovic.messages.domain._
import com.github.drmarjanovic.messages.protocol._
import com.github.drmarjanovic.messages.{ AppEnv, AppTask }
import com.github.drmarjanovic.tracing._
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.{ TextMap, TextMapAdapter }
import io.opentracing.tag.Tags.{ HTTP_METHOD, HTTP_URL }
import org.http4s._
import org.http4s.dsl.Http4sDsl
import zio.interop.catz._
import zio.{ Task, ZIO }

import scala.jdk.CollectionConverters._

final class Messages(service: MessageService) extends Routes {

  def routes(): HttpRoutes[AppTask] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    HttpRoutes.of[AppTask] {
      case req @ GET -> Root / "users" / UserIdVar(userId) / "contacts" / ContactIdVar(contactId) / "messages"
            :? OffsetQueryParam(maybeOffset) +& LimitQueryParam(maybeLimit) =>
        val (offset, limit) = getPaginationParams(maybeOffset, maybeLimit)

        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "retrieve messages")
                 )
          _        = span.setTag(HTTP_URL, req.uri.renderString)
          _        = span.setTag(HTTP_METHOD, GET.name)
          messages <- service.findByUserIdAndContactId(userId, contactId, offset, limit)(span)
          _        = span.setTag(HTTP_STATUS, Ok.code)
          _        = span.finish()
        } yield messages.withLinks(req.uri, offset, limit)

        handleFailures(zio)

      case req @ POST -> Root / "users" / UserIdVar(userId) / "contacts" / ContactIdVar(contactId) / "messages" =>
        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry.spanFrom(HttpHeadersFormat, new TextMapAdapter(headers.asJava), "send message")
                 )
          _     = span.setTag(HTTP_URL, req.uri.renderString)
          _     = span.setTag(HTTP_METHOD, POST.name)
          spec  <- req.as[MessageRequest]
          saved <- service.save(userId, contactId, spec.data.attributes.body)(span)
          maybeMessage <- saved.fold(e => {
                           span.failed()
                           Task.fail(e)
                         }, id => service.one(id)(span))
          message <- maybeMessage.foldZ(MalformedRequest("Failed retrieving saved message."))(span)
          _       = span.setTag(HTTP_STATUS, Ok.code)
          _       = span.finish()
        } yield message.toResponse

        handleFailures(zio)
    }
  }

}
