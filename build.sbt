name := "AkkaHttpSample"

version := "1.0"

scalaVersion := "2.11.11"

val akkav = "2.5.3"
val akkahttpv = "10.0.9"

libraryDependencies ++= Seq(
  //akka
  "com.typesafe.akka" %% "akka-actor" % akkav,
  "com.typesafe.akka" %% "akka-stream" % akkav,
  "com.typesafe.akka" %% "akka-testkit" % akkav,
  "com.typesafe.akka" %% "akka-http-core" % akkahttpv,
  "com.typesafe.akka" %% "akka-http" % akkahttpv,

  "com.typesafe" % "config" % "1.3.1",

  //common
  "commons-codec" % "commons-codec" % "1.10",
  "commons-lang" % "commons-lang" % "2.6",
  "commons-io" % "commons-io" % "2.5",
  "commons-daemon" % "commons-daemon" % "1.0.15",
  "joda-time" % "joda-time" % "2.9.6",

  //ログ用
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" % "compile",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "compile",
  "ch.qos.logback" % "logback-classic" % "1.1.7" % "runtime"
)