enablePlugins(ScalaJSPlugin)

lazy val root = project.in(file("."))
  .aggregate(astone)

lazy val astone = project.in(file("astone"))
  .settings(
    name := "astone",
    scalaVersion := "3.0.0-M3",
    libraryDependencies ++= Seq(
      "org.scala-js" % "scalajs-dom_sjs1_2.13" % "1.0.0",
      "com.lihaoyi" % "scalatags_sjs1_2.13" % "0.9.2"
    ),
    Compile / npmDependencies ++= Seq(
      "three" -> "0.123.0"
    ),
    Compile / npmDevDependencies ++= Seq(
      // "acorn-dynamic-import" -> "4.0.0",
      "url-loader" -> "4.1.1",
      // "raw-loader" -> "4.0.2"
      // "file-loader" -> "6.2.0"
    ),
    webpack / version := "4.44.2",
    scalaJSUseMainModuleInitializer := true,
    webpackConfigFile := Some(baseDirectory.value / "webpack.config.js")
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
