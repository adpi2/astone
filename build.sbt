enablePlugins(ScalaJSPlugin)

name := "astone"
scalaVersion := "2.13.3"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "1.0.0",
  "com.lihaoyi" %%% "utest" % "0.7.4" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()