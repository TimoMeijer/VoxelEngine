name := """VoxelEngine"""

version := "nextVersion"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "org.lwjgl.lwjgl" % "lwjgl-platform" % "2.9.1",
  "org.lwjgl.lwjgl" % "lwjgl" % "2.9.1",
  "org.lwjgl.lwjgl" % "lwjgl_util" % "2.9.1"
)