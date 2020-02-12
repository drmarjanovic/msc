import sbt._

object Dependencies {

  private object Versions {
    val Circe        = "0.12.3"
    val Http4s       = "0.21.0-M6"
    val Jaeger       = "1.0.0"
    val JodaTime     = "2.10.5"
    val Zio          = "1.0.0-RC17"
    val ZioTelemetry = "0.0.0+37-647d7336+20200208-2014"
    val Zipkin       = "2.11.1"
  }

  val circe: ModuleID          = "io.circe"            %% "circe-generic"        % Versions.Circe
  val http4sDsl: ModuleID      = "org.http4s"          %% "http4s-dsl"           % Versions.Http4s
  val jaegerCore: ModuleID     = "io.jaegertracing"    % "jaeger-core"           % Versions.Jaeger
  val jaegerClient: ModuleID   = "io.jaegertracing"    % "jaeger-client"         % Versions.Jaeger
  val jaegerZipkin: ModuleID   = "io.jaegertracing"    % "jaeger-zipkin"         % Versions.Jaeger
  val jodaTime: ModuleID       = "joda-time"           % "joda-time"             % Versions.JodaTime
  val zioCore: ModuleID        = "dev.zio"             %% "zio"                  % Versions.Zio
  val zioTelemetry: ModuleID   = "dev.zio"             %% "zio-telemetry"        % Versions.ZioTelemetry
  val zipkinReporter: ModuleID = "io.zipkin.reporter2" % "zipkin-reporter"       % Versions.Zipkin
  val zipkinSender: ModuleID   = "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % Versions.Zipkin

}
