import com.gu.riffraff.artifact.BuildInfo
import play.sbt.PlayImport.PlayKeys._

name := "warren"
description := "Don't hunt rabbits"

scalaVersion in ThisBuild := "2.12.5"

val awsVersion = "1.11.416"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  ws,
  // General
  "com.amazonaws"             % "aws-java-sdk-s3"              % awsVersion,
  "com.amazonaws"             % "aws-java-sdk-ec2"             % awsVersion,
  "com.amazonaws"             % "aws-java-sdk-ssm"             % awsVersion,
  "com.typesafe.play"        %% "play-json"                    % "2.6.9",
  "com.beachape"             %% "enumeratum-play"              % "1.5.12-2.6.0-M5",
  "org.bouncycastle"          % "bcprov-jdk15on"               % "1.58",
  "org.typelevel"            %% "cats-core"                    % "1.4.0",
  "commons-io"                % "commons-io"                   % "2.6",
  "com.iheart"               %% "ficus"                        % "1.4.2",
  "com.gu"                   %% "play-googleauth"              % "0.7.0",
  "com.pauldijou"            %% "jwt-play"                     % "0.18.0",

  // Extraction
  "org.apache.tika"           % "tika-parsers"                 % "1.18" exclude("org.slf4j", "slf4j-jdk14"),
  "com.levigo.jbig2"          % "levigo-jbig2-imageio"         % "2.0",

  // Database
  "org.postgresql"           %  "postgresql"                   % "42.1.4",
  "org.scalikejdbc"          %% "scalikejdbc"                  % "3.1.0",
  "org.scalikejdbc"          %% "scalikejdbc-config"           % "3.1.0",
  "org.scalikejdbc"          %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.1"
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

    playDefaultPort := 9119,
    javaOptions in Universal ++= Seq(
      "-Dpidfile.path=/dev/null",
      "-J-XX:MaxRAMFraction=2",
      "-J-XX:InitialRAMFraction=2",
      "-J-XX:MaxMetaspaceSize=500m",
      "-J-XX:+UseConcMarkSweepGC",
      "-J-XX:+PrintGCDetails",
      "-J-XX:+PrintGCDateStamps",
      "-J-XX:+HeapDumpOnOutOfMemoryError",
      "-J-Dhttp.port=9119",
      s"-J-Xloggc:/var/log/${name.value}/gc.log",
    )
  )
