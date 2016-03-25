name := "axon"

organization := "edu.luc.etl"

version := "0.1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

//libraryDependencies ++= Seq(
// "org.scalaz" %% "scalaz-core" % "7.2.1"
//  "com.github.mpilquist" %% "simulacrum" % "0.7.0"
//)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % Test
)
