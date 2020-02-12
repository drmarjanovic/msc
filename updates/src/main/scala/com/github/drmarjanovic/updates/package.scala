package com.github.drmarjanovic

import com.github.drmarjanovic.updates.domain.UpdateAlgebra
import zio.RIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.telemetry.opentracing.OpenTracing

package object updates {

  val Api: String         = "/api"
  val ServiceName: String = "updates"

  type AppEnv = Blocking with Clock with UpdateAlgebra with OpenTracing

  type AppTask[A] = RIO[AppEnv, A]

}
