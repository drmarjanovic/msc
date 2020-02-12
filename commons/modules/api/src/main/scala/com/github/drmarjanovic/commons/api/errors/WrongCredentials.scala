package com.github.drmarjanovic.commons.api.errors

import com.github.drmarjanovic.commons.api.{ Error, ErrorResponse, Statuses }

final case class WrongCredentials(message: String) extends Throwable {
  def toResponse: ErrorResponse = ErrorResponse(List(Error(Statuses.Unauthorized, message)))
}
