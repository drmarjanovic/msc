package com.github.drmarjanovic

import com.github.drmarjanovic.messages.domain.MessageAlgebra
import zio.RIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.telemetry.opentracing.OpenTracing

package object messages {

  val Api: String         = "/api"
  val ServiceName: String = "messages"

  type AppEnv = Blocking with Clock with MessageAlgebra with OpenTracing

  type AppTask[A] = RIO[AppEnv, A]

}
