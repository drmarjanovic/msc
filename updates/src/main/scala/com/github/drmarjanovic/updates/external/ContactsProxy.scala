package com.github.drmarjanovic.updates.external

import com.github.drmarjanovic.contacts.protocol.ContactCollectionResponse
import com.github.drmarjanovic.updates.domain.UserId
import io.circe.Error
import sttp.client._
import sttp.client.asynchttpclient.zio._
import sttp.client.circe.asJson
import sttp.model.Uri
import zio.Task
import zio.interop.catz._

object ContactsProxy {

  private val backend = AsyncHttpClientZioBackend()

  def getContacts(
    userId: UserId,
    uri: Uri,
    params: List[(String, String)],
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], ContactCollectionResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .get(uri.path("api", "users", userId, "contacts").params(params: _*))
          .headers(headers)
          .response(asJson[ContactCollectionResponse])
      )
    }

}
