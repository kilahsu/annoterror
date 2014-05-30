import scala.language.postfixOps

import sbt._
import sbt.inc._
import Keys._

object RootBuild extends Build {

  val scalaV = "2.10.3"

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version       := "1.0",
    organization  := "com.company",
    scalaVersion  := scalaV,
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-Xlint",
      "-encoding", "utf8"
    ),
    javaOptions in run ++= Seq(
      "-Xmx8G"
    ),
    javaOptions in Test ++= Seq(
      "-Xmx4G",
      "-XX:MaxPermSize=512m"
    ),
    parallelExecution in Test := false
  )

  lazy val top = Project(
    id       = "top",
    base     = file("."),
    settings = sharedSettings
  ) aggregate(testprj)

  lazy val testprj = Project(
    id       = "testprj",
    base     = file("testprj"),
    settings = testProjectSettings
  )

  val repositories = Seq(
    "clojars"                  at "http://clojars.org/repo",
    "spray repo"               at "http://repo.spray.io/"
  )

  lazy val sharedSettings =
    buildSettings ++
    Seq(
      resolvers ++= repositories,
      libraryDependencies ++= Dependencies.shared,
      fork := true,
      incOptions in Compile := IncOptions.Default
    )

  lazy val testProjectSettings =
    sharedSettings ++ Seq(
      name := "testprj",
      addCompilerPlugin(Dependencies.Plugins.scalaMacros)
    )
}

object Dependencies {

  object Plugins {
    val scalaMacros = "org.scalamacros" %% "paradise" % "2.0.0" cross CrossVersion.full
  }

  object Compile {
    val swaggerCore        = "com.wordnik"                 % "swagger-core_2.10"             % "1.3.5"
    val swaggerAnnotations = "com.wordnik"                 %% "swagger-annotations"          % "1.3.0"
    val spraySwagger       = "com.gettyimages"             % "spray-swagger_2.10"            % "0.3.3"
    val sprayJson          = "io.spray"                    %%  "spray-json"                  % "1.2.6"
    val compiler           = "org.scala-lang"              % "scala-compiler"                % RootBuild.scalaV
  }
  import Compile._

  val shared          = Seq(compiler,swaggerAnnotations, spraySwagger, sprayJson)
}

