package com.github.drmarjanovic

import io.opentracing.Span
import io.opentracing.tag.{ BooleanTag, IntTag, StringTag, Tags }
import zio.Task

package object tracing {

  val GRAPHQL_OPERATION_NAME = "graphql.operation_name"
  val HTTP_STATUS            = "http.status_code"

  implicit def booleanTag2String(tag: BooleanTag): String = tag.getKey

  implicit def intTag2String(tag: IntTag): String = tag.getKey

  implicit def stringTag2String(tag: StringTag): String = tag.getKey

  implicit class OptionZ[A](underlying: Option[A]) {
    def foldZ(t: Throwable)(implicit span: Span): Task[A] =
      underlying.fold[Task[A]] {
        span.failed()
        Task.fail(t)
      }(Task.succeed)
  }

  implicit class RichSpan[A](span: Span) {
    def failed(): Unit = {
      span.setTag(Tags.ERROR.getKey, true)
      span.finish()
    }
  }

}
