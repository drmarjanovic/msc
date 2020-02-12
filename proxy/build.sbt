import Dependencies._

lazy val info = List(
  name := "proxy",
  version := "1.0.0",
  organization := "com.github.drmarjanovic",
  description := "Service used as proxy to other services."
)

lazy val root = (project in file("."))
  .settings(info: _*)
  .settings {
    scalaVersion := "2.13.1"
    libraryDependencies ++= {
      val commons = List(contacts, messages, updates, users, tracing)
      val core    = List(caliban, calibanHttp4s, http4sBlazeServer, http4sCirce, http4sDsl, pureConfig)
      val sttp    = List(sttpClient, sttpCirce)
      val utils   = List(javaJwt)
      val zio     = List(zioCore)

      commons ++ core ++ sttp ++ utils ++ zio
    }
  }
