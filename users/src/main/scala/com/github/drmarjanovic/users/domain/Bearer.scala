package com.github.drmarjanovic.users.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.drmarjanovic.users.ServiceName
import com.github.drmarjanovic.users.config.Config.ApplicationSecret
import org.joda.time.DateTime

final case class Bearer private (token: String) extends AnyVal

object Bearer {

  def of(secret: ApplicationSecret, user: User): Bearer = {
    val token = JWT
      .create()
      .withIssuer(ServiceName)
      .withClaim(IdClaimName, user.id)
      .withClaim(EmailClaimName, user.email)
      .withIssuedAt(DateTime.now.toDate)
      .sign(Algorithm.HMAC256(secret.key))

    Bearer(token)
  }

  private val IdClaimName    = "id"
  private val EmailClaimName = "email"

}
