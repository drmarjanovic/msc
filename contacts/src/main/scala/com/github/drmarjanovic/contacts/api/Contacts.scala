package com.github.drmarjanovic.contacts.api

import com.github.drmarjanovic.commons.api._
import com.github.drmarjanovic.contacts.domain.Filter._
import com.github.drmarjanovic.contacts.domain.{ ContactService, Filter }
import com.github.drmarjanovic.contacts.protocol._
import com.github.drmarjanovic.contacts.{ AppEnv, AppTask }
import com.github.drmarjanovic.tracing.HTTP_STATUS
import io.opentracing.propagation.Format.Builtin.{ HTTP_HEADERS => HttpHeadersFormat }
import io.opentracing.propagation.{ TextMap, TextMapAdapter }
import io.opentracing.tag.Tags.{ HTTP_METHOD, HTTP_URL }
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio.ZIO
import zio.interop.catz._

import scala.jdk.CollectionConverters._

final class Contacts(service: ContactService) extends Routes {

  def routes(): HttpRoutes[AppTask] = {
    val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
    import dsl._

    HttpRoutes.of[AppTask] {
      case req @ GET -> Root / "users" / UserIdVar(userId) / "contacts"
            :? OffsetQueryParam(maybeOffset) +& LimitQueryParam(maybeLimit) +& FilterVipQueryParam(vip)
              +& FilterEmailQueryParam(email) +& FilterMobileQueryParam(mobile) =>
        val (offset, limit) = getPaginationParams(maybeOffset, maybeLimit)
        val filters: List[Filter] =
          List(vip.map(Vip.apply), email.map(HasEmail.apply), mobile.map(HasMobile.apply)).flatten

        val headers = req.headers.toList.map(h => h.name.value -> h.value).toMap
        val zio = for {
          span <- ZIO.accessM[AppEnv](
                   _.telemetry
                     .spanFrom[TextMap](HttpHeadersFormat, new TextMapAdapter(headers.asJava), "retrieve contacts")
                 )
          _        = span.setTag(HTTP_URL, req.uri.renderString)
          _        = span.setTag(HTTP_METHOD, GET.name)
          contacts <- service.findByUserId(userId, filters, offset, limit)(span)
          _        = span.log(s"Successfully returned contacts with filters [${filters.mkString("; ")}] for user $userId.")
          _        = span.setTag(HTTP_STATUS, Ok.code)
          _        = span.finish()
        } yield contacts

        handleFailures[ContactCollectionResponse](zio.map(_.withLinks(req.uri, offset, limit)))
    }
  }

}
