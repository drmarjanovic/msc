package com.github.drmarjanovic.proxy

import zio.RIO

package object config {
  val load: RIO[Configuration, Config] = RIO.accessM(_.config.load)
}
