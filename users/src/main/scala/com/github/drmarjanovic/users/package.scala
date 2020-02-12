package com.github.drmarjanovic

import com.github.drmarjanovic.users.domain.UserAlgebra
import zio.RIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.telemetry.opentracing.OpenTracing

package object users {

  val Api: String         = "/api"
  val ServiceName: String = "users"

  type AppEnv = Blocking with Clock with UserAlgebra with OpenTracing

  type AppTask[A] = RIO[AppEnv, A]

}
