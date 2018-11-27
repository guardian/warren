resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.vafer" % "jdeb" % "1.6" artifacts (Artifact("jdeb", "jar", "jar"))

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.18")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.1.0-M1")
addSbtPlugin("com.gu" % "sbt-riffraff-artifact" % "1.1.4")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")
