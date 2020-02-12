import Dependencies._

lazy val info = Seq(
  name := "updates",
  version := "1.0.0",
  organization := "com.github.drmarjanovic",
  description := "Service used for managing updates."
)

lazy val protocol =
  project
    .in(file("modules/protocol"))
    .settings(
      version := "1.0.0",
      organization := "com.github.drmarjanovic.updates",
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
      val commons   = List(tracing)
      val core      = List(cats, http4sBlazeServer, http4sCirce, http4sDsl, pureConfig)
      val db        = List(doobieCore, doobieHikari, doobiePostgres, flyway)
      val protocols = List(contacts, messages)
      val sttp      = List(sttpClient, sttpCirce)
      val utils     = List(jodaTime)
      val zio       = List(zioCore, zioInteropCats)
      val test      = List(zioTest).map(_ % Test)

      commons ++ core ++ db ++ protocols ++ sttp ++ utils ++ zio ++ test
    }
  }
  .dependsOn(protocol)
