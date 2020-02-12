package com.github.drmarjanovic.proxy.external

import com.github.drmarjanovic.proxy.UserId
import com.github.drmarjanovic.users.protocol.{AuthRequest, AuthResponse, UserResponse}
import io.circe.Error
import io.circe.syntax._
import sttp.client._
import sttp.client.asynchttpclient.zio._
import sttp.client.circe._
import sttp.model.Uri
import zio.Task
import zio.interop.catz._

object UsersProxy {

  private val backend = AsyncHttpClientZioBackend()

  def auth(
    email: String,
    password: String,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], AuthResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .post(uri.path("api", "users", "auth"))
          .headers(headers)
          .body(AuthRequest(email, password).asJson)
          .response(asJson[AuthResponse])
      )
    }

  def getUser(
    userId: UserId,
    uri: Uri,
    headers: Map[String, String] = Map.empty
  ): Task[Response[Either[ResponseError[Error], UserResponse]]] =
    backend.flatMap { implicit backend =>
      backend.send(
        basicRequest
          .get(uri.path("api", "users", userId))
          .headers(headers)
          .response(asJson[UserResponse])
      )
    }

}
