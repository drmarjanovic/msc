package com.github.drmarjanovic.commons.api.errors

import com.github.drmarjanovic.commons.api.{ Error, ErrorResponse, Statuses }

final case class ServiceCommunicationFailure private (message: String) extends Throwable {
  def toResponse: ErrorResponse = ErrorResponse(List(Error(Statuses.InternalServerError, message)))
}

object ServiceCommunicationFailure {
  def `with`(service: String): ServiceCommunicationFailure =
    ServiceCommunicationFailure(s"Service communication with '$service' failed.")
}
