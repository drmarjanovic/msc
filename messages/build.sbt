import Dependencies._

lazy val info = List(
  name := "messages",
  version := "1.0.0",
  organization := "com.github.drmarjanovic",
  description := "Service used for managing messages."
)

lazy val protocol =
  project
    .in(file("modules/protocol"))
    .settings(
      version := "1.0.0",
      organization := "com.github.drmarjanovic.messages",
      name := "protocol"
    )
    .settings {
      scalaVersion := "2.13.1"
      libraryDependencies ++= List(api, circe)
    }

lazy val root = (project in file("."))
  .settings(info: _*)
  .settings {
    scalaVersion := "2.13.1"
    libraryDependencies ++= {
      val commons = List(tracing)
      val core    = List(cats, http4sBlazeServer, http4sCirce, http4sDsl, pureConfig)
      val db      = List(doobieCore, doobieHikari, doobiePostgres, flyway)
      val zio     = List(zioCore, zioInteropCats)
      val utils   = List(jodaTime)
      val test    = List(zioTest).map(_ % Test)

      commons ++ core ++ db ++ utils ++ zio ++ test
    }
  }
  .dependsOn(protocol)
