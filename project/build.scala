import sbt._
import Keys._

object VoxelEngineBuild extends Build {
  // Libraries under development, use latest Github version
  //lazy val macrogl = RootProject(uri("git://github.com/storm-enroute/macrogl.git"))

  // The VoxelEngine library and the Example project
  lazy val voxelengine = project in file(".")
  lazy val example = project dependsOn voxelengine
}