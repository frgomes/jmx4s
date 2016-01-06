//
// build.sbt
//

def newModule(module: String): Project =
  Project(module, file(module))
    .settings(
      organization := "info.rgomes",
      name := s"jmx4s-${module}",
      version := (version in ThisBuild).value,
      description := s"Simplistic JMX for Scala. ${module} module.",
      licenses += ("BSD Simplified", url("http://opensource.org/licenses/BSD-2-Clause")),
      publishMavenStyle := false,
      publishArtifact in Test := false)
    .settings(
      name in bintray:= s"${name.value}-${module}",
      bintrayRepository    in bintray := s"${name.value}",
      bintrayPackageLabels in bintray := Seq("scala", "sbt", "plugin"),
      bintrayOrganization  in bintray := None)
    .settings(
    pomExtra := (
      <scm>
        <url>git@github.com:frgomes/{name.value}.git</url>
        <connection>scm:git:git@github.com:frgomes/{name.value}.git</connection>
        </scm>
        <developers>
          <developer>
            <id>frgomes</id>
            <name>Richard Gomes</name>
            <url>https://github.com/frgomes</url>
          </developer>
        </developers> ))

lazy val scalaSettings: Seq[Setting[_]] =
  Seq(
    scalaVersion       := "2.11.7",
    crossScalaVersions := Seq("2.11.7", "2.10.6"),
    crossPaths         := true,
    scalacOptions ++= Seq(
      "-unchecked", "-deprecation",
      "-Xlint", "-language:_",
      "-target:jvm-1.6", "-encoding", "UTF-8"
    ))

lazy val deps_scala : Seq[Setting[_]] =
  Seq(
    resolvers += Resolver.jcenterRepo,
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
