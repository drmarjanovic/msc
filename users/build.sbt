import Dependencies._

lazy val info = List(
  name := "users",
  version := "1.0.0",
  organization := "com.github.drmarjanovic",
  description := "Service used for managing users."
)

lazy val protocol =
  project
    .in(file("modules/protocol"))
    .settings(
      version := "1.0.0",
      organization := "com.github.drmarjanovic.users",
      name := "protocol"
    )
    .settings {
      scalaVersion := "2.13.1"
      libraryDependencies ++= List(circe)
    }

lazy val root = (project in file("."))
  .settings(info: _*)
  .settings {
    scalaVersion := "2.13.1"
    libraryDependencies ++= {
      val commons = List(api, tracing)
      val core    = List(cats, http4sBlazeServer, http4sCirce, http4sDsl, pureConfig)
      val db      = List(doobieCore, doobieHikari, doobiePostgres, flyway)
      val utils   = List(javaJwt, jodaTime)
      val zio     = List(zioCore, zioInteropCats, zioMacros)
      val test    = List(zioTest).map(_ % Test)

      commons ++ core ++ db ++ utils ++ zio ++ test
    }
  }
  .dependsOn(protocol)
