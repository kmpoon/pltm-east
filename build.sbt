// -*- mode: scala -*-

name := "pltm-east"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= 
  "org.scalatest" %% "scalatest" % "2.2.6" % "test" ::
  "org.scalactic" %% "scalactic" % "2.2.6" ::
  Nil

// EclipseKeys.withSource := true

// EclipseKeys.withJavadoc := true

assemblyJarName in assembly := "pltm-east.jar"

// EclipseKeys.eclipseOutput := Some("target")

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
// EclipseKeys.preTasks := Seq(compile in Compile)

// To skip test during assembly
// test in assembly := {}

