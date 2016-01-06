//
// build.sbt
//

import sbt._
import Keys._


val appOrg  = "info.rgomes"
val appName = "jmx4s"
val appDesc = "Simplistic JMX for Scala."
val appLicenses   = Seq( ("BSD Simplified", url("http://opensource.org/licenses/BSD-2-Clause")) )
val appLabels     = Seq( "scala", "jmx" )
val appRepo       = appOrg     // if not a sbtPlugin, falls back to "maven"
val appMavenStyle = true // if not a sbtPlugin, falls back to false
val appExtra = 
      <scm>
        <url>git@github.com:frgomes/{appName}.git</url>
        <connection>scm:git:git@github.com:frgomes/{appName}.git</connection>
      </scm>
      <developers>
        <developer>
          <id>frgomes</id>
          <name>Richard Gomes</name>
          <url>https://github.com/frgomes</url>
        </developer>
      </developers>


def newModule(module: String): Project =
  Project(module, file(module))
    .settings(
      organization := appOrg,
      name         := s"${appName}-${module}",
      version      := (version in ThisBuild).value,
      description  := appDesc,
      licenses     := appLicenses,
      pomExtra     := appExtra)
    .settings(
      publishMavenStyle := appMavenStyle,
      publishArtifact in Test := false)
    .settings(
      name                 in bintray := s"${name.value}-${module}",
      bintrayRepository    in bintray := appRepo,
      bintrayPackageLabels in bintray := appLabels,
      bintrayOrganization  in bintray := None)

lazy val scalaSettings: Seq[Setting[_]] =
  Seq(
    scalaVersion       := "2.11.7",
    crossScalaVersions := Seq("2.11.7"),
    crossPaths         := true,
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-Xlint", "-language:_",
      "-target:jvm-1.6", "-encoding", "UTF-8"
    ))

lazy val deps_scala : Seq[Setting[_]] =
  Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.11.7"
    ))


lazy val root =
  project.in(file("."))
    .settings( publish := { }, publishLocal := { }, bintrayUnpublish := { } )
    .settings(scalaSettings:_*)
    .aggregate(core, app)

lazy val core =
  newModule("core")
    .settings(scalaSettings:_*)
    .settings(deps_scala:_*)

lazy val app =
  project.in(file("app"))
    .settings( publish := { }, publishLocal := { }, bintrayUnpublish := { } )
    .settings(scalaSettings:_*)
    .dependsOn(core)
