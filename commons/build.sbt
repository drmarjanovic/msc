import Dependencies._

lazy val api =
  project
    .in(file("modules/api"))
    .settings(
      version := "1.0.3",
      organization := "com.github.drmarjanovic.commons",
      name := "api"
    )
    .settings {
      scalaVersion := "2.13.1"
      libraryDependencies ++= List(circe, http4sDsl, jodaTime)
    }

lazy val tracing =
  project
    .in(file("modules/tracing"))
    .settings(
      version := "1.0.2",
      organization := "com.github.drmarjanovic.commons",
      name := "tracing"
    )
    .settings {
      scalaVersion := "2.13.1"
      libraryDependencies ++= {
        val tracing = List(jaegerCore, jaegerClient, jaegerZipkin, zipkinReporter, zipkinSender)
        val utils   = List(jodaTime)
        val zio     = List(zioCore, zioTelemetry)

        tracing ++ utils ++ zio
      }
    }
