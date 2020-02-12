package com.github.drmarjanovic.users.config

import pureconfig.ConfigSource
import pureconfig.generic.auto._
import zio.{ RIO, Task }

trait Configuration extends Serializable {
  val config: Configuration.Service[Any]
}

object Configuration {

  trait Service[R] {
    val load: RIO[R, Config]
  }

  trait Live extends Configuration {
    val config: Service[Any] = new Service[Any] {
      override val load: Task[Config] = Task.effectTotal(ConfigSource.default.loadOrThrow[Config])
    }
  }

  object Live extends Live

}
