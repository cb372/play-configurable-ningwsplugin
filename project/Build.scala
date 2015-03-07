import sbt._
import Keys._
import xerial.sbt.Sonatype._
import SonatypeKeys._
import com.typesafe.sbt.pgp.PgpKeys
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._

import scala.language.postfixOps

object ConfigurableNingWSPluginBuild extends Build {
  
  val playVersion = "2.3.8"

  lazy val plugin = Project(
    id = "play-configurable-ningwsplugin",
    base = file("."), 
    settings = 
      releaseSettings ++
      sonatypeSettings ++
      mavenSettings ++ Seq(
      organization := "com.github.cb372",
      scalaVersion := "2.11.5",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
      resolvers += Resolver.typesafeRepo("releases"),
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play-ws" % playVersion % Provided,
        "com.typesafe.play" %% "play-test" % playVersion % Test,
        "com.typesafe.play" %% "play-test" % playVersion % Test,
        "org.scalatestplus" %% "play" % "1.2.0" % Test
      ),
      publishArtifactsAction := PgpKeys.publishSigned.value,
      ReleaseKeys.releaseProcess := Seq[ReleaseStep](
        checkSnapshotDependencies,
        inquireVersions,
        runClean,
        runTest,
        setReleaseVersion,
        commitReleaseVersion,
        tagRelease,
        publishArtifacts,
        setNextVersion,
        commitNextVersion,
        pushChanges,
        deployToMavenCentral
      )
    )
  )

  lazy val mavenSettings = Seq(
    pomExtra :=
      <url>https://github.com/cb372/play-configurable-ningwsplugin</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:cb372/play-configurable-ningwsplugin.git</url>
        <connection>scm:git:git@github.com:cb372/play-configurable-ningwsplugin.git</connection>
      </scm>
      <developers>
        <developer>
          <id>cb372</id>
          <name>Chris Birchall</name>
          <url>https://github.com/cb372</url>
        </developer>
      </developers>,
    publishTo <<= version { v =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )

  lazy val deployToMavenCentral = ReleaseStep(action = releaseTask(sonatypeReleaseAll))
}


