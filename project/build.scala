import sbt._
import Keys._

object VoxelEngineBuild extends Build {
    lazy val voxelengine = project in file(".")
    lazy val example = project dependsOn voxelengine
}