package com.github.drmarjanovic.proxy.external

import com.github.drmarjanovic.proxy.UserId
import com.github.drmarjanovic.proxy.model.UpdateType
import com.github.drmarjanovic.updates.protocol._
import io.circe.Error
import io.circe.syntax._
import sttp.client._
import sttp.client.asynchttpclient.zio._
import sttp.client.circe._
import sttp.model.Uri
import zio.Task
import zio.interop.catz._

object UpdatesProxy {

  private val backend = AsyncHttpClientZioBackend()

  def sendUpdate(
    userId: UserId,
    body: String,
    to: UpdateType,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], UpdateResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .post(uri.path("api", "users", userId, "updates"))
          .headers(headers)
          .body(
            UpdateRequest(
              data = UpdateRequestData(UpdatesType, attributes = UpdateRequestAttributes(body, to.toString))
            ).asJson
          )
          .response(asJson[UpdateResponse])
      )
    }

  def getUpdates(
    userId: UserId,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], UpdateCollectionResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .get(uri.path("api", "users", userId, "updates"))
          .headers(headers)
          .response(asJson[UpdateCollectionResponse])
      )
    }

}
