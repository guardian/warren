import com.gu.riffraff.artifact.BuildInfo
import play.sbt.PlayImport.PlayKeys._

name := "warren"
description := "Hunt tories, not rabbits"

scalaVersion in ThisBuild := "2.12.5"

val awsVersion = "1.11.416"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  ws,
  // General
  "com.typesafe.play"        %% "play-json"                    % "2.6.9",
  "com.beachape"             %% "enumeratum-play"              % "1.5.12-2.6.0-M5",
  "org.typelevel"            %% "cats-core"                    % "1.4.0",
  "commons-io"                % "commons-io"                   % "2.6",
  "com.iheart"               %% "ficus"                        % "1.4.2",

  // Database
  "com.sksamuel.elastic4s"   %% "elastic4s-http"                   % "6.3.7",
  "org.elasticsearch.client" % "elasticsearch-rest-client-sniffer" % "6.4.2"
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, JDebPackaging, SystemdPlugin, BuildInfoPlugin)
  .settings(
    buildInfoKeys ++= {
      import sys.process._
      val buildInfo = BuildInfo(baseDirectory.value)

      Seq[BuildInfoKey](
        name,
        version,
        scalaVersion,
        sbtVersion,
        "vcsCommitId" -> buildInfo.revision,
        "vcsBranch" -> buildInfo.branch,
        "buildNumber" -> buildInfo.buildIdentifier,
        "builtOnHost" -> Option(System.getenv("HOSTNAME"))
          .orElse(Option("hostname".!!.trim).filter(_.nonEmpty))
          .getOrElse("<unknown>")
          .replace("\"", "").trim
      )
    },

    buildInfoOptions ++= Seq(
      BuildInfoOption.ToJson,
      BuildInfoOption.ToMap
    ),
    buildInfoPackage := "utils.buildinfo",

    debianPackageDependencies := Seq("openjdk-8-jre-headless"),
    maintainer in Linux := "Guardian Developers <dig.dev.software@theguardian.com>",
    packageSummary in Linux := description.value,
    packageDescription := description.value,

    mappings in Universal ~= { _.filterNot { case (_, fileName) => fileName == "conf/site.conf" }},

		//assetsPrefix := "build/",
		resourceDirectory in Assets := baseDirectory.value / "build",

    playDefaultPort := 7000,
    javaOptions in Universal ++= Seq(
      "-Dpidfile.path=/dev/null",
      "-J-XX:MaxRAMFraction=2",
      "-J-XX:InitialRAMFraction=2",
      "-J-XX:MaxMetaspaceSize=500m",
      "-J-XX:+UseConcMarkSweepGC",
      "-J-XX:+PrintGCDetails",
      "-J-XX:+PrintGCDateStamps",
      "-J-XX:+HeapDumpOnOutOfMemoryError",
      "-J-Dhttp.port=7000",
      s"-J-Xloggc:/var/log/${name.value}/gc.log",
    )
  )
