name := "common"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.3.2",
  "org.gnieh" %% "sohva-client" % "0.5",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"
)

play.Project.playScalaSettings
