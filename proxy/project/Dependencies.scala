import sbt._

object Dependencies {

  private object Commons {
    val Tracing  = "1.0.1"
    val Contacts = "1.0.0"
    val Updates  = "1.0.0"
    val Users    = "1.0.0"
    val Messages = "1.0.0"
  }

  private object Versions {
    val Caliban    = "0.5.1"
    val JavaJwt    = "3.9.0"
    val Http4s     = "0.21.0"
    val PureConfig = "0.12.2"
    val Sttp       = "2.0.0-RC6"
    val Zio        = "1.0.0-RC17"
  }

  val contacts: ModuleID = "com.github.drmarjanovic.contacts" %% "protocol" % Commons.Contacts
  val messages: ModuleID = "com.github.drmarjanovic.messages" %% "protocol" % Commons.Messages
  val updates: ModuleID  = "com.github.drmarjanovic.updates"  %% "protocol" % Commons.Updates
  val users: ModuleID    = "com.github.drmarjanovic.users"    %% "protocol" % Commons.Users
  val tracing: ModuleID  = "com.github.drmarjanovic.commons"  %% "tracing"  % Commons.Tracing

  val caliban: ModuleID           = "com.github.ghostdogpr"        %% "caliban"                       % Versions.Caliban
  val calibanHttp4s: ModuleID     = "com.github.ghostdogpr"        %% "caliban-http4s"                % Versions.Caliban
  val javaJwt: ModuleID           = "com.auth0"                    % "java-jwt"                       % Versions.JavaJwt
  val http4sBlazeServer: ModuleID = "org.http4s"                   %% "http4s-blaze-server"           % Versions.Http4s
  val http4sCirce: ModuleID       = "org.http4s"                   %% "http4s-circe"                  % Versions.Http4s
  val http4sDsl: ModuleID         = "org.http4s"                   %% "http4s-dsl"                    % Versions.Http4s
  val pureConfig: ModuleID        = "com.github.pureconfig"        %% "pureconfig"                    % Versions.PureConfig
  val sttpClient: ModuleID        = "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % Versions.Sttp
  val sttpCirce: ModuleID         = "com.softwaremill.sttp.client" %% "circe"                         % Versions.Sttp
  val zioCore: ModuleID           = "dev.zio"                      %% "zio"                           % Versions.Zio

}
