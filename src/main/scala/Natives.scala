package net.timomeijer.voxelengine

import scala.io._
import java.io._
import java.util.jar._

/** Currently hardcoded to load lwjgl natives */
trait Natives {
  val jar = new JarFile("C:\\Users\\Timo\\.ivy2\\cache\\org.lwjgl.lwjgl\\lwjgl-platform\\jars\\lwjgl-platform-2.9.1-natives-windows.jar")

  def extract(jar: JarFile, toDir: File) {
    val files = jar.entries
    while (files.hasMoreElements) {
      val file = files.nextElement()

      if (!file.getName.startsWith("META-INF")) {

        val in = jar.getInputStream(file)
        val out = new FileOutputStream(new File(toDir, file.getName))

        try {
          var byte = -1

          while ( {
            byte = in.read(); byte != -1
          }) {
            out.write(byte)
          }
        } finally {
          in.close()
          out.close()
        }
      }
    }
  }

  val tempDir = new File(System.getProperty("java.io.tmpdir"), "net.timomeijer.voxelengine-"+System.nanoTime)
  tempDir.mkdir()
  extract(jar, tempDir)
  System.setProperty("org.lwjgl.librarypath", tempDir.getAbsolutePath)
}
