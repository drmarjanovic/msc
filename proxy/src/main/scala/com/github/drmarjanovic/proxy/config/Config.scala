package com.github.drmarjanovic.proxy.config

import com.github.drmarjanovic.proxy.config.Config.{ApiConfig, ApplicationSecret, HttpConfig, TracerHost}

final case class Config(secret: ApplicationSecret, api: ApiConfig, http: HttpConfig, tracer: TracerHost)

object Config {

  final case class ApplicationSecret(key: String) extends AnyVal

  final case class ContactsServiceConfig(host: String, port: Int)

  final case class MessagesServiceConfig(host: String, port: Int)

  final case class UpdatesServiceConfig(host: String, port: Int)

  final case class UsersServiceConfig(host: String, port: Int)

  final case class ApiConfig(
    contacts: ContactsServiceConfig,
    messages: MessagesServiceConfig,
    updates: UpdatesServiceConfig,
    users: UsersServiceConfig
  )

  final case class HttpConfig(host: String, port: Int)

  final case class TracerHost(host: String) extends AnyVal

}
