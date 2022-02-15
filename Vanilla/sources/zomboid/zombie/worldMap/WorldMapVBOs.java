package zombie.worldMap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.VBO.IGLBufferObject;

public final class WorldMapVBOs {
   private static final int VERTEX_SIZE = 12;
   private static final int COLOR_SIZE = 16;
   private static final int ELEMENT_SIZE = 28;
   private static final int COLOR_OFFSET = 12;
   public static final int NUM_ELEMENTS = 2340;
   private static final int INDEX_SIZE = 2;
   private static final WorldMapVBOs instance = new WorldMapVBOs();
   private final ArrayList m_vbos = new ArrayList();
   private ByteBuffer m_elements;
   private ByteBuffer m_indices;

   public static WorldMapVBOs getInstance() {
      return instance;
   }

   public void create() {
      this.m_elements = BufferUtils.createByteBuffer(65520);
      this.m_indices = BufferUtils.createByteBuffer(4680);
   }

   private void flush() {
      if (this.m_vbos.isEmpty()) {
         WorldMapVBOs.WorldMapVBO var1 = new WorldMapVBOs.WorldMapVBO();
         var1.create();
         this.m_vbos.add(var1);
      }

      this.m_elements.flip();
      this.m_indices.flip();
      ((WorldMapVBOs.WorldMapVBO)this.m_vbos.get(this.m_vbos.size() - 1)).flush(this.m_elements, this.m_indices);
      this.m_elements.position(this.m_elements.limit());
      this.m_elements.limit(this.m_elements.capacity());
      this.m_indices.position(this.m_indices.limit());
      this.m_indices.limit(this.m_indices.capacity());
   }

   private void addVBO() {
      WorldMapVBOs.WorldMapVBO var1 = new WorldMapVBOs.WorldMapVBO();
      var1.create();
      this.m_vbos.add(var1);
      this.m_elements.clear();
      this.m_indices.clear();
   }

   public void reserveVertices(int var1, int[] var2) {
      if (this.m_indices == null) {
         this.create();
      }

      int var3 = this.m_indices.position() / 2;
      if (var3 + var1 > 2340) {
         this.flush();
         this.addVBO();
      }

      var2[0] = this.m_vbos.isEmpty() ? 0 : this.m_vbos.size() - 1;
      var2[1] = this.m_indices.position() / 2;
   }

   public void addElement(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.m_elements.putFloat(var1);
      this.m_elements.putFloat(var2);
      this.m_elements.putFloat(var3);
      this.m_elements.putFloat(var4);
      this.m_elements.putFloat(var5);
      this.m_elements.putFloat(var6);
      this.m_elements.putFloat(var7);
      short var8 = (short)(this.m_indices.position() / 2);
      this.m_indices.putShort(var8);
   }

   public void drawElements(int var1, int var2, int var3, int var4) {
      if (var2 >= 0 && var2 < this.m_vbos.size()) {
         WorldMapVBOs.WorldMapVBO var5 = (WorldMapVBOs.WorldMapVBO)this.m_vbos.get(var2);
         if (var3 >= 0 && var3 + var4 <= var5.m_elementCount) {
            var5.m_vbo.bind();
            var5.m_ibo.bind();
            GL11.glEnableClientState(32884);
            GL11.glDisableClientState(32886);
            GL11.glVertexPointer(3, 5126, 28, 0L);

            for(int var6 = 7; var6 >= 0; --var6) {
               GL13.glActiveTexture('蓀' + var6);
               GL11.glDisable(3553);
            }

            GL11.glDisable(2929);
            GL12.glDrawRangeElements(var1, var3, var3 + var4, var4, 5123, (long)(var3 * 2));
            var5.m_vbo.bindNone();
            var5.m_ibo.bindNone();
         }
      }
   }

   public void reset() {
   }

   private static final class WorldMapVBO {
      GLVertexBufferObject m_vbo;
      GLVertexBufferObject m_ibo;
      int m_elementCount = 0;

      void create() {
         IGLBufferObject var1 = GLVertexBufferObject.funcs;
         this.m_vbo = new GLVertexBufferObject(65520L, var1.GL_ARRAY_BUFFER(), var1.GL_STREAM_DRAW());
         this.m_vbo.create();
         this.m_ibo = new GLVertexBufferObject(4680L, var1.GL_ELEMENT_ARRAY_BUFFER(), var1.GL_STREAM_DRAW());
         this.m_ibo.create();
      }

      void flush(ByteBuffer var1, ByteBuffer var2) {
         this.m_vbo.bind();
         this.m_vbo.bufferData(var1);
         this.m_ibo.bind();
         this.m_ibo.bufferData(var2);
         this.m_elementCount = var2.limit() / 2;
      }
   }
}
