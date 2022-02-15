package zombie.core.VBO;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ARBVertexBufferObject;

public final class GLBufferObjectARB implements IGLBufferObject {
   public int GL_ARRAY_BUFFER() {
      return 34962;
   }

   public int GL_ELEMENT_ARRAY_BUFFER() {
      return 34963;
   }

   public int GL_STATIC_DRAW() {
      return 35044;
   }

   public int GL_STREAM_DRAW() {
      return 35040;
   }

   public int GL_BUFFER_SIZE() {
      return 34660;
   }

   public int GL_WRITE_ONLY() {
      return 35001;
   }

   public int glGenBuffers() {
      return ARBVertexBufferObject.glGenBuffersARB();
   }

   public void glBindBuffer(int var1, int var2) {
      ARBVertexBufferObject.glBindBufferARB(var1, var2);
   }

   public void glDeleteBuffers(int var1) {
      ARBVertexBufferObject.glDeleteBuffersARB(var1);
   }

   public void glBufferData(int var1, ByteBuffer var2, int var3) {
      ARBVertexBufferObject.glBufferDataARB(var1, var2, var3);
   }

   public void glBufferData(int var1, long var2, int var4) {
      ARBVertexBufferObject.glBufferDataARB(var1, var2, var4);
   }

   public ByteBuffer glMapBuffer(int var1, int var2, long var3, ByteBuffer var5) {
      return ARBVertexBufferObject.glMapBufferARB(var1, var2, var3, var5);
   }

   public boolean glUnmapBuffer(int var1) {
      return ARBVertexBufferObject.glUnmapBufferARB(var1);
   }

   public void glGetBufferParameter(int var1, int var2, IntBuffer var3) {
      ARBVertexBufferObject.glGetBufferParameterivARB(var1, var2, var3);
   }
}
