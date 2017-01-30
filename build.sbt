lazy val root = (project in file(".")).
  settings(
    name := "scalaga",
    version := "0.1.0",
    scalaVersion := "2.10.4",
    mainClass in Compile := Some("scalaga.Main")
  )

// assemblyOutputPath in assembly := file("./deploy/scalaga" + ".jar")

libraryDependencies ++= Seq(
  //groupID % artifactID % revision % optionally test
  "org.scalacheck" % "scalacheck_2.10" % "1.13.4" % "test"
)
