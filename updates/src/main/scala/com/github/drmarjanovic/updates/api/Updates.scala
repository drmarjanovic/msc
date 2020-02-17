package com.github.drmarjanovic.updates.api

import com.github.drmarjanovic.commons.api._
import com.github.drmarjanovic.commons.api.errors.{ MalformedRequest, MalformedResponse }
import com.github.drmarjanovic.tracing.TracingHelper._
import com.github.drmarjanovic.tracing._
import com.github.drmarjanovic.updates.config.Config.ApiConfig
import com.github.drmarjanovic.updates.domain.UpdateType.{ ContactsWithEmail, ContactsWithMobile, VipContacts }
import com.github.drmarjanovic.updates.domain.{ UpdateService, UpdateType }
import com.github.drmarjanovic.updates.external.{ ContactsProxy, MessagesProxy }
import com.github.drmarjanovic.updates.protocol.UpdateRequest
import com.github.drmarjanovic.updates.{ AppEnv, AppTask }
import io.opentracing.Span
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.{ TextMap, TextMapAdapter }
import io.opentracing.tag.Tags.{ HTTP_METHOD, HTTP_URL }
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import sttp.model.Uri
import zio.interop.catz._
import zio.{ UIO, ZIO }

import scala.collection.mutable
import scala.jdk.CollectionConverters._

final class Updates(service: UpdateService, api: ApiConfig) extends Routes {

  def routes(): HttpRoutes[AppTask] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    HttpRoutes.of[AppTask] {
      case req @ GET -> Root / "users" / UserIdVar(userId) / "updates"
            :? OffsetQueryParam(maybeOffset) +& LimitQueryParam(maybeLimit) =>
        val (offset, limit) = getPaginationParams(maybeOffset, maybeLimit)

        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "retrieve updates")
                 )
          _       = span.setTag(HTTP_URL, req.uri.renderString)
          _       = span.setTag(HTTP_METHOD, GET.name)
          updates <- service.findByUserId(userId, offset, limit)(span).catchAll(span.failed)
          _       = span.log(now(), s"Successfully returned updates for user $userId.")
          _       = span.setTag(HTTP_STATUS, Ok.code)
          _       = span.finish()
        } yield updates.withLinks(req.uri, offset, limit)

        handleFailures(zio)

      case req @ POST -> Root / "users" / UserIdVar(userId) / "updates" =>
        def retrieveContacts(`type`: UpdateType, headers: Map[String, String])(implicit span: Span) = {
          val params = `type` match {
            case ContactsWithEmail  => List("filter[email]" -> "true")
            case ContactsWithMobile => List("filter[mobile]" -> "true")
            case VipContacts        => List("filter[vip]" -> "true")
            case _                  => List.empty
          }

          for {
            url <- ZIO.fromEither(Uri.safeApply(api.contacts.host, api.contacts.port))
            res <- ContactsProxy.getContacts(userId, url, params, headers).catchAll(span.failed)
            ids <- res.body.fold(e => span.failed(MalformedResponse(e.getMessage)), r => ZIO.succeed(r.data.map(_.id)))
          } yield ids
        }

        def sendMessageTo(contactIds: List[Long], body: String, headers: Map[String, String])(implicit span: Span) =
          for {
            url <- ZIO.fromEither(Uri.safeApply(api.messages.host, api.messages.port))
            sent <- ZIO.foreachPar(contactIds)(
                     id =>
                       MessagesProxy
                         .sendMessage(userId, id, body, url, headers)
                         .map(_.code)
                         .catchAll(e => span.failed(MalformedResponse(e.getMessage)))
                   )
          } yield sent

        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "create update")
                 )
          _           = span.setTag(HTTP_URL, req.uri.renderString)
          _           = span.setTag(HTTP_METHOD, POST.name)
          spec        <- req.as[UpdateRequest]
          attrs       = spec.data.attributes
          buffer      <- UIO.succeed(new TextMapAdapter(mutable.Map.empty[String, String].asJava))
          _           <- ZIO.accessM[AppEnv](_.telemetry.inject(HttpHeadersFormat, buffer, span))
          headers     <- extractTracingHeaders(buffer)
          updateType  = UpdateType.fromReq(attrs.`type`)
          contacts    <- retrieveContacts(updateType, headers)(span)
          _           = span.log(now(), s"Successfully retrieved update subscribers for user $userId.")
          _           <- sendMessageTo(contacts, attrs.message, headers)(span)
          saved       <- service.save(userId, updateType, attrs.message)(span)
          maybeUpdate <- saved.fold(e => span.failed(e), id => service.one(id)(span).catchAll(span.failed))
          update      <- maybeUpdate.foldZ(MalformedRequest("Failed retrieving saved update."))(span)
          _           = span.setTag(HTTP_STATUS, Ok.code)
          _           = span.finish()
        } yield update.toResponse

        handleFailures(zio)
    }
  }

}
