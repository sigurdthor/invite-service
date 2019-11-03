name := """kafka-websocket-service"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala, PlayAkkaHttp2Support)
 // .disablePlugins(PlayAkkaHttpServer)


scalaVersion := "2.12.6"

val playPac4jVersion = "5.0.0"
val pac4jVersion = "2.2.1"
val playVersion = "2.6.13"

libraryDependencies ++= Seq(
  cacheApi,
  "org.scalatestplus.play" % "scalatestplus-play_2.12" % "3.1.1" % "test",
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.13.0-play26",
  "org.reactivemongo" %% "reactivemongo-akkastream" % "0.13.0",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.19",
  "org.pac4j" %% "play-pac4j" % playPac4jVersion,
  "org.pac4j" % "pac4j-oidc" % pac4jVersion exclude("commons-io" , "commons-io"),
  "com.github.karelcemus" %% "play-redis" % "2.0.2",
  "commons-io" % "commons-io" % "2.4"
)

import play.sbt.routes.RoutesKeys

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

resolvers ++= Seq(Resolver.mavenLocal, "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases", "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

fork in run := true

enablePlugins(AkkaGrpcPlugin)