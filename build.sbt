name := "felix-debugger"
organization := "com.iqvia.rwas.omop"

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

val akkaHttpVersion = "10.0.6"
val akkaVersion = "2.5.1"
val sparkVersion = "2.4.4"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,

  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

  "com.pauldijou" %% "jwt-core" % "0.14.1",

  "com.github.pureconfig" %% "pureconfig" % "0.12.1",

  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "com.iqvia.rwas.omop" %% "omop-akka-http-docroutes" % "0.0.4" % Test
)

fork in Test := true

resolvers += Resolver.mavenLocal
resolvers += "RWAS DEV Local" at "http://rwes-artifactory01.internal.imsglobal.com/artifactory/maven-dev-local"

scalastyleConfig := file("project/scalastyle_config.xml")
