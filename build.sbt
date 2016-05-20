name := """semestral-project"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  evolutions,
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"