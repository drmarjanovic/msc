package com.github.drmarjanovic.tracing

import io.opentracing.propagation.TextMapAdapter
import org.joda.time.DateTime
import zio.UIO

import scala.collection.mutable

object TracingHelper {

  def now(): Long = DateTime.now().getMillis * 1000

  def extractTracingHeaders(adapter: TextMapAdapter): UIO[Map[String, String]] = {
    val m = mutable.Map.empty[String, String]
    UIO(adapter.forEach { entry =>
      m.put(entry.getKey, entry.getValue)
      ()
    }).as(m.toMap)
  }

}
