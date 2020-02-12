package com.github.drmarjanovic.users

import com.github.drmarjanovic.users.domain.{ Bearer, User, UserId }
import com.github.drmarjanovic.users.protocol.{ AuthResponse, UserResponse, UserResponseAttributes, UserResponseData }

package object api {

  object UserIdVar {
    def unapply(id: String): Option[UserId] = UserId.of(id)
  }

  implicit class AuthResponseDecoder(bearer: Bearer) {
    def toResponse: AuthResponse = AuthResponse(bearer.token)
  }

  implicit class UserResponseDecoder(user: User) {
    def toResponse: UserResponse =
      UserResponse(
        data = UserResponseData(
          id = user.id,
          attributes = UserResponseAttributes(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
          )
        )
      )
  }

}
