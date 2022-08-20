val scala3Version = "2.13.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-goodreads",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.apache.commons" % "commons-csv" % "1.9.0"
    )
)
