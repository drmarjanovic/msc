package com.github.drmarjanovic.proxy.external

import com.github.drmarjanovic.contacts.protocol.ContactCollectionResponse
import com.github.drmarjanovic.proxy.UserId
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
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], ContactCollectionResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .get(uri.path("api", "users", userId, "contacts"))
          .headers(headers)
          .response(asJson[ContactCollectionResponse])
      )
    }

}
