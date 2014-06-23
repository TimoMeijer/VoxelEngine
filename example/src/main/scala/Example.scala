package net.timomeijer.voxelengine.example

import net.timomeijer.voxelengine._
import java.nio.ByteBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.util.glu.GLU

import scala.io._
import org.lwjgl.util.Color
import org.lwjgl.opengl._
import GL11._

object Example extends Natives {
  def main(args: Array[String]) {
    // Test if interaction with VoxelEngine & build works
    println(VoxelEngine.test)

    Display.setDisplayMode(new DisplayMode(640,360))
    Display.create()

    val programId = GL20.glCreateProgram()

    val vertexShaderId = loadShader("example\\src\\main\\resources\\shaders\\vertex.glsl", GL20.GL_VERTEX_SHADER)
    val fragmentShaderId = loadShader("example\\src\\main\\resources\\shaders\\fragment.glsl", GL20.GL_FRAGMENT_SHADER)

    GL20.glAttachShader(programId, vertexShaderId)
    GL20.glAttachShader(programId, fragmentShaderId)

    // Bind shader input
    GL20.glBindAttribLocation(programId, 0, "in_Position")
    GL20.glBindAttribLocation(programId, 1, "in_TextureCoord")

    GL20.glLinkProgram(programId)
    GL20.glValidateProgram(programId)

    if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
      throw new Exception("Validation failed: "+GL20.glGetProgramInfoLog(programId, Int.MaxValue))
    }

    val textureId = GL11.glGenTextures()
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL31.GL_TEXTURE_RECTANGLE, textureId)

    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)

    val vertices = BufferUtils.createFloatBuffer(4 * (4 + 2))
    vertices.put(Array[Float](-1, 1, 0))
    vertices.put(Array[Float](0, 0))
    vertices.put(Array[Float](-1, -1, 0))
    vertices.put(Array[Float](0, 640))
    vertices.put(Array[Float](1, -1, 0))
    vertices.put(Array[Float](360, 640))
    vertices.put(Array[Float](1, 1, 0))
    vertices.put(Array[Float](360, 0))
    vertices.flip()

    val indices = BufferUtils.createByteBuffer(6)
    indices.put(Array[Byte](0, 1, 2, 2, 3, 0))
    indices.flip()

    val vaoId = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vaoId)

    val vboId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)

    GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 4 * (4 + 2), 0)
    GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * (4 + 2), 4 * 4)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)

    val vboiId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW)

    // Render loop
    while (!Display.isCloseRequested) {
      glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      GL20.glUseProgram(programId)

      val pixels = Array.tabulate[Color](640,360) { (x,y) => new Color(255, 0, 0) }
      val width = pixels(0).length
      val height = pixels.length

      val buffer = ByteBuffer.allocateDirect(4 * width * height)
      pixels.foreach(_.foreach(_.writeRGBA(buffer)))
      buffer.flip()

      GL11.glTexImage2D(GL31.GL_TEXTURE_RECTANGLE, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)

      GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0)

      Display.update()
    }

    // Cleanup
    Display.destroy()
  }

  def loadShader(fileName: String, shaderType: Int): Int = {
    val shaderSource = Source.fromFile(fileName)
    val shader = shaderSource.mkString

    shaderSource.close()

    val shaderId = GL20.glCreateShader(shaderType)
    GL20.glShaderSource(shaderId, shader)
    GL20.glCompileShader(shaderId)

    if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      throw new Exception("Compilation failed: "+GL20.glGetShaderInfoLog(shaderId, Int.MaxValue))
    }

    shaderId
  }
}