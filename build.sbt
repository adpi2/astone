enablePlugins(ScalaJSPlugin)

semanticdbEnabled := true

name := "astone"
scalaVersion := "3.0.0-M2"
scalaJSStage := FullOptStage

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" % "scalajs-dom_sjs1_2.13" % "1.0.0"
)

testFrameworks += new TestFramework("utest.runner.Framework")

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()