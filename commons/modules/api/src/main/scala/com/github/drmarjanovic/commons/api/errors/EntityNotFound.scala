package com.github.drmarjanovic.commons.api.errors

import com.github.drmarjanovic.commons.api.{ Error, ErrorResponse, Statuses }

final case class EntityNotFound private (message: String) extends Throwable {
  def toResponse: ErrorResponse = ErrorResponse(List(Error(Statuses.NotFound, message)))
}

object EntityNotFound {
  def apply(entity: String, id: Any): EntityNotFound = EntityNotFound(s"$entity with ID = $id does not exist.")
}
