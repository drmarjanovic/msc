package com.github.drmarjanovic.proxy.external

import com.github.drmarjanovic.messages.protocol.MessageCollectionResponse
import com.github.drmarjanovic.proxy.{ ContactId, UserId }
import io.circe.Error
import sttp.client._
import sttp.client.asynchttpclient.zio._
import sttp.client.circe.asJson
import sttp.model.Uri
import zio.Task
import zio.interop.catz._

object MessagesProxy {

  private val backend = AsyncHttpClientZioBackend()

  def getMessages(
    userId: UserId,
    contactId: ContactId,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], MessageCollectionResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .get(uri.path("api", "users", userId, "contacts", contactId, "messages"))
          .headers(headers)
          .response(asJson[MessageCollectionResponse])
      )
    }

}
