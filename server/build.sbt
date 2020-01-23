name := "felix-debugger"

enablePlugins(UniversalPlugin)
enablePlugins(JavaAppPackaging)

packageName in Universal := name.value

scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-Ywarn-unused:imports",
  "-Ywarn-dead-code",
  "-explaintypes",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings"
)

scalacOptions in (Compile, console) --= Seq(
  "-Ywarn-unused:imports",
  "-Xfatal-warnings"
)

val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "cask" % "0.2.9",
  "com.lihaoyi" %% "upickle" % "0.9.5",

  "com.github.pureconfig" %% "pureconfig" % "0.12.1",

  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

scalastyleConfig := file("project/scalastyle_config.xml")
