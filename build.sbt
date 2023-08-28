import play.core.PlayVersion.akkaVersion

name := """business_review_insights"""
organization := "sarthak"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, LauncherJarPlugin)

resolvers += "confluent" at "https://packages.confluent.io/maven/"

scalaVersion := "2.13.11"



lazy val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

lazy val kafkaDependency = "com.typesafe.akka" %% "akka-stream-kafka" % "2.1.0"

lazy val kafkaAvroDependency = Seq(
  "org.apache.kafka" % "kafka-clients" % "3.5.1",
    "io.confluent" % "kafka-avro-serializer" % "5.3.0")

lazy val avroDependency = "com.sksamuel.avro4s" %% "avro4s-core" % "3.1.1"

lazy val cassandraDependencies = Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "4.0.0",
  "com.datastax.cassandra" % "cassandra-driver-core" % "4.0.0"
)

libraryDependencies += avroDependency
libraryDependencies += kafkaDependency
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test



libraryDependencies ++= (akkaDependencies ++ kafkaAvroDependency ++ cassandraDependencies)

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.4"



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "sarthak.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "sarthak.binders._"
