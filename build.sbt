name			:= "agatetepe"
organization	:= "com.github.evbruno"
version			:= "1.0-SNAPSHOT"
scalaVersion	:= "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint", "-feature")

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
	"ch.qos.logback"            % "logback-classic" % "1.1.5",

	"com.github.nikita-volkov"  % "sext"            % "0.2.4" % "test",
	"org.scalatest"             %% "scalatest"      % "2.2.6" % "test",
	"co.freeside"               % "betamax"         % "1.1.2" % "test",
	"org.codehaus.groovy"       % "groovy-all"      % "1.8.8" % "test"
)

parallelExecution in Test := false

coverageExcludedPackages := ".*HelloWorld.*"

resolvers += Resolver.url("scoverage-bintray", url("https://dl.bintray.com/sksamuel/sbt-plugins/"))(Resolver.ivyStylePatterns)

// deploy config stuff

// sbt publishSigned
// sbt sonatypeRelease
// crossScalaVersions := Seq("2.10.0")

useGpg := true

publishMavenStyle := true

publishTo := {
	val nexus = "https://oss.sonatype.org/"
	if (isSnapshot.value)
		Some("snapshots" at nexus + "content/repositories/snapshots")
	else
		Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

pomExtra := (
	<url>https://github.com/evbruno/agatetepe</url>
		<licenses>
			<license>
				<name>WTFPL</name>
				<url>http://www.wtfpl.net/</url>
				<distribution>repo</distribution>
			</license>
		</licenses>
		<scm>
			<url>git@github.com:evbruno/agatetepe.git</url>
			<connection>scm:git:git@github.com:evbruno/agatetepe.git</connection>
		</scm>
		<developers>
			<developer>
				<id>evbruno</id>
				<name>Eduardo V. Bruno</name>
				<url>https://github.com/evbruno/</url>
			</developer>
		</developers>)
