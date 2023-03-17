val scala3Version = "2.13.1"
val DoobieVersion = "1.0.0-RC1"
val NewTypeVersion = "0.4.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-goodreads",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      // Productive Dependencies
      "org.typelevel" %% "cats-effect" % "3.3.12",
      "org.typelevel" %% "cats-effect-kernel" % "3.4.7",
      "org.typelevel" %% "cats-effect-std" % "3.4.7",

      "com.github.gekomad" %% "itto-csv" % "1.2.0",

      "com.monovore" %% "decline" % "2.4.1",
      "com.monovore" %% "decline-effect" % "2.4.1",

      // Test Dependencies
      "org.typelevel" %% "cats-effect-testing-specs2" % "1.4.0" % Test,
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
    )
  )
