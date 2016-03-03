name			:= "aga-te-te-pe"
organization	:= "etc.bruno"
version 		:= "1.0-SNAPSHOT"
scalaVersion	:= "2.11.7"

scalacOptions 	:= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
	"ch.qos.logback" % "logback-classic" % "1.1.5",

	"com.github.nikita-volkov" % "sext" % "0.2.4" % "test",
	"org.scalatest"   %% "scalatest" % "2.2.6" % "test",

	"co.freeside" % "betamax" % "1.1.2" % "test",
	"org.codehaus.groovy" % "groovy-all" % "1.8.8" % "test"
)

parallelExecution in Test := false
