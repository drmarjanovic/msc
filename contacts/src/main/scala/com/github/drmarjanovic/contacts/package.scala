package com.github.drmarjanovic

import com.github.drmarjanovic.contacts.domain.ContactAlgebra
import zio.RIO
import zio.blocking.Blocking
import zio.clock.Clock
import zio.telemetry.opentracing.OpenTracing

package object contacts {

  val Api: String         = "/api"
  val ServiceName: String = "contacts"

  type AppEnv = Blocking with Clock with ContactAlgebra with OpenTracing

  type AppTask[A] = RIO[AppEnv, A]

}
