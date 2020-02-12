package com.github.drmarjanovic.commons.api.errors

import com.github.drmarjanovic.commons.api.{ Error, ErrorResponse, Statuses }

final case class MalformedRequest(reason: String) extends Throwable {
  def toResponse: ErrorResponse = ErrorResponse(List(Error(Statuses.BadRequest, reason)))
}
