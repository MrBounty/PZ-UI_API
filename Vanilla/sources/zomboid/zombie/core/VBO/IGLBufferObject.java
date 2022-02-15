package zombie.core.VBO;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface IGLBufferObject {
   int GL_ARRAY_BUFFER();

   int GL_ELEMENT_ARRAY_BUFFER();

   int GL_STATIC_DRAW();

   int GL_STREAM_DRAW();

   int GL_BUFFER_SIZE();

   int GL_WRITE_ONLY();

   int glGenBuffers();

   void glBindBuffer(int var1, int var2);

   void glDeleteBuffers(int var1);

   void glBufferData(int var1, ByteBuffer var2, int var3);

   void glBufferData(int var1, long var2, int var4);

   ByteBuffer glMapBuffer(int var1, int var2, long var3, ByteBuffer var5);

   boolean glUnmapBuffer(int var1);

   void glGetBufferParameter(int var1, int var2, IntBuffer var3);
}
