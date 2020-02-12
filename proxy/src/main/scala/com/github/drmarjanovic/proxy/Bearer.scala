package com.github.drmarjanovic.proxy

import com.auth0.jwt.JWT

object Bearer {

  private val IdClaimName = "id"

  def extract(header: String): UserId = {
    val token = header.split(" ")(1)
    UserId(JWT.decode(token).getClaim(IdClaimName).asLong())
  }

}
