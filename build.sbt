enablePlugins(ScalaJSPlugin)

semanticdbEnabled := true

lazy val root = project.in(file("."))
  .aggregate(astone)

lazy val astone = project.in(file("astone"))
  .settings(
    name := "astone",
    scalaVersion := "3.0.0-M2",
    libraryDependencies ++= Seq(
      "org.scala-js" % "scalajs-dom_sjs1_2.13" % "1.0.0"
    ),
    Compile / npmDependencies ++= Seq(
      "three" -> "0.123.0"
    ),
    Compile / npmDevDependencies ++= Seq(
      "url-loader" -> "4.1.1",
      "acorn-dynamic-import" -> "4.0.0"
    ),
    webpack / version := "4.44.2",
    scalaJSUseMainModuleInitializer := true,
    webpackConfigFile := Some(baseDirectory.value / "webpack.config.js")
  )
  .dependsOn(three)
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
