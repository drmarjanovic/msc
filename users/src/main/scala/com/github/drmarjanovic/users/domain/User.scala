package com.github.drmarjanovic.users.domain

final case class User(id: UserId, firstName: String, lastName: String, email: String, password: String)
