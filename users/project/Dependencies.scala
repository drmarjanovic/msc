import sbt._

object Dependencies {

  private object Commons {
    val Api     = "1.0.3"
    val Tracing = "1.0.2"
  }

  private object Versions {
    val Cats           = "2.1.0"
    val Circe          = "0.12.3"
    val Doobie         = "0.8.8"
    val Flyway         = "6.1.1"
    val Http4s         = "0.20.15"
    val JavaJwt        = "3.9.0"
    val JodaTime       = "2.10.5"
    val PureConfig     = "0.12.2"
    val Zio            = "1.0.0-RC17"
    val ZioInteropCats = "2.0.0.0-RC10"
    val ZioMacros      = "0.6.2"
  }

  val api: ModuleID     = "com.github.drmarjanovic.commons" %% "api"     % Commons.Api
  val tracing: ModuleID = "com.github.drmarjanovic.commons" %% "tracing" % Commons.Tracing

  val cats: ModuleID              = "org.typelevel"         %% "cats-core"           % Versions.Cats
  val circe: ModuleID             = "io.circe"              %% "circe-generic"       % Versions.Circe
  val doobieCore: ModuleID        = "org.tpolecat"          %% "doobie-core"         % Versions.Doobie
  val doobieHikari: ModuleID      = "org.tpolecat"          %% "doobie-hikari"       % Versions.Doobie
  val doobiePostgres: ModuleID    = "org.tpolecat"          %% "doobie-postgres"     % Versions.Doobie
  val flyway: ModuleID            = "org.flywaydb"          % "flyway-core"          % Versions.Flyway
  val http4sBlazeServer: ModuleID = "org.http4s"            %% "http4s-blaze-server" % Versions.Http4s
  val http4sCirce: ModuleID       = "org.http4s"            %% "http4s-circe"        % Versions.Http4s
  val http4sDsl: ModuleID         = "org.http4s"            %% "http4s-dsl"          % Versions.Http4s
  val javaJwt: ModuleID           = "com.auth0"             % "java-jwt"             % Versions.JavaJwt
  val jodaTime: ModuleID          = "joda-time"             % "joda-time"            % Versions.JodaTime
  val pureConfig: ModuleID        = "com.github.pureconfig" %% "pureconfig"          % Versions.PureConfig
  val zioCore: ModuleID           = "dev.zio"               %% "zio"                 % Versions.Zio
  val zioInteropCats: ModuleID    = "dev.zio"               %% "zio-interop-cats"    % Versions.ZioInteropCats
  val zioMacros: ModuleID         = "dev.zio"               %% "zio-macros-core"     % Versions.ZioMacros
  val zioTest: ModuleID           = "dev.zio"               %% "zio-test"            % Versions.Zio

}
