package zombie.core.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.debug.DebugLog;

public final class SharedVertexBufferObjects {
   private final int bufferSizeBytes = 65536;
   private final int indexBufferSizeBytes;
   public final int bufferSizeVertices;
   private final GLVertexBufferObject[] vbo = new GLVertexBufferObject[48];
   private final GLVertexBufferObject[] ibo = new GLVertexBufferObject[48];
   public FloatBuffer vertices;
   public ShortBuffer indices;
   private int sequence = -1;
   private int mark;

   public SharedVertexBufferObjects(int var1) {
      this.bufferSizeVertices = 65536 / var1;
      this.indexBufferSizeBytes = this.bufferSizeVertices * 3;
   }

   public void startFrame() {
      boolean var1 = true;
      if (var1) {
         this.sequence = -1;
      }

      this.mark = this.sequence;
   }

   public void next() {
      ++this.sequence;
      if (this.sequence == this.vbo.length) {
         this.sequence = 0;
      }

      if (this.sequence == this.mark) {
         DebugLog.General.error("SharedVertexBufferObject overrun.");
      }

      if (this.vbo[this.sequence] == null) {
         this.vbo[this.sequence] = new GLVertexBufferObject(65536L, GLVertexBufferObject.funcs.GL_ARRAY_BUFFER(), GLVertexBufferObject.funcs.GL_STREAM_DRAW());
         this.vbo[this.sequence].create();
         this.ibo[this.sequence] = new GLVertexBufferObject((long)this.indexBufferSizeBytes, GLVertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), GLVertexBufferObject.funcs.GL_STREAM_DRAW());
         this.ibo[this.sequence].create();
      }

      this.vbo[this.sequence].bind();
      this.vertices = this.vbo[this.sequence].map().asFloatBuffer();
      this.vertices.clear();
      this.ibo[this.sequence].bind();
      this.indices = this.ibo[this.sequence].map().asShortBuffer();
      this.indices.clear();
   }

   public void unmap() {
      this.vbo[this.sequence].unmap();
      this.ibo[this.sequence].unmap();
   }
}
