val Version = new {
  val CatsEffect = "3.2.1"
  val MunitCatsEffect = "1.0.5"
  val Scala2 = "2.13.6"
  val Scala3 = "3.0.1"
}

ThisBuild / crossScalaVersions := Version.Scala2 :: Version.Scala3 :: Nil
ThisBuild / developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/")))
ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/taig/scala-linguist/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/scala-linguist/main/LICENSE"))
ThisBuild / scalaVersion := Version.Scala2
ThisBuild / versionScheme := Some("early-semver")

noPublishSettings
name := "scala-pygments"

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    name := "scala-pygments",
    libraryDependencies ++=
      "org.typelevel" %%% "cats-effect" % Version.CatsEffect % "test" ::
        "org.typelevel" %%% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        Nil
  )

lazy val graalvm = project
  .in(file("modules/graalvm"))
  .settings(
    name := "scala-pygments-graalvm",
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil
  )
  .dependsOn(core.jvm % "compile->compile;test->test")
