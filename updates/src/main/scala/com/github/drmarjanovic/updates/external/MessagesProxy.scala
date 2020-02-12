package com.github.drmarjanovic.updates.external

import com.github.drmarjanovic.messages.protocol.MessageResponse._
import com.github.drmarjanovic.messages.protocol._
import com.github.drmarjanovic.updates.domain.UserId
import io.circe.Error
import sttp.client._
import sttp.client.asynchttpclient.zio._
import sttp.client.circe._
import sttp.model.Uri
import zio.Task

object MessagesProxy {

  private val backend = AsyncHttpClientZioBackend()

  def sendMessage(
    userId: UserId,
    contactId: Long,
    body: String,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], MessageResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .post(uri.path("api", "users", userId, "contacts", contactId.toString, "messages"))
          .headers(headers)
          .body(MessageRequest(MessageRequestData(MessagesType, MessageRequestAttributes(body))))
          .response(asJson[MessageResponse])
      )
    }

}
