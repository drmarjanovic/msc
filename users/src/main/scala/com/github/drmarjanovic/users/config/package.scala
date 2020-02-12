package com.github.drmarjanovic.users

import zio.RIO

package object config extends Configuration.Service[Configuration] {
  override val load: RIO[Configuration, Config] = RIO.accessM(_.config.load)
}
