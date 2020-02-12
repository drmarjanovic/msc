package com.github.drmarjanovic

import zio.RIO
import zio.clock.Clock
import zio.console.Console
import zio.telemetry.opentracing.OpenTracing

package object proxy {

  val ServiceName: String = "proxy"

  type AppEnv = Console with Clock with OpenTracing

  type AppTask[A] = RIO[AppEnv, A]

  val AuthUserId = UserId(1)

}
