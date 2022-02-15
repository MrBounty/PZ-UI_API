package zombie.worldMap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import zombie.core.SpriteRenderer;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.VBO.IGLBufferObject;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.popman.ObjectPool;

public final class VBOLinesUV {
   private final int VERTEX_SIZE = 12;
   private final int COLOR_SIZE = 16;
   private final int UV_SIZE = 8;
   private final int ELEMENT_SIZE = 36;
   private final int COLOR_OFFSET = 12;
   private final int UV_OFFSET = 28;
   private final int NUM_ELEMENTS = 128;
   private final int INDEX_SIZE = 2;
   private GLVertexBufferObject m_vbo;
   private GLVertexBufferObject m_ibo;
   private ByteBuffer m_elements;
   private ByteBuffer m_indices;
   private float m_lineWidth = 1.0F;
   private float m_dx = 0.0F;
   private float m_dy = 0.0F;
   private float m_dz = 0.0F;
   private int m_mode = 1;
   private final ObjectPool m_runPool = new ObjectPool(VBOLinesUV.Run::new);
   private final ArrayList m_runs = new ArrayList();

   private VBOLinesUV.Run currentRun() {
      return this.m_runs.isEmpty() ? null : (VBOLinesUV.Run)this.m_runs.get(this.m_runs.size() - 1);
   }

   private void create() {
      this.m_elements = BufferUtils.createByteBuffer(4608);
      this.m_indices = BufferUtils.createByteBuffer(256);
      IGLBufferObject var1 = GLVertexBufferObject.funcs;
      this.m_vbo = new GLVertexBufferObject(4608L, var1.GL_ARRAY_BUFFER(), var1.GL_STREAM_DRAW());
      this.m_vbo.create();
      this.m_ibo = new GLVertexBufferObject(256L, var1.GL_ELEMENT_ARRAY_BUFFER(), var1.GL_STREAM_DRAW());
      this.m_ibo.create();
   }

   public void setOffset(float var1, float var2, float var3) {
      this.m_dx = var1;
      this.m_dy = var2;
      this.m_dz = var3;
   }

   public void addElement(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      if (this.isFull()) {
         TextureID var10 = this.currentRun().textureID;
         this.flush();
         this.startRun(var10);
      }

      if (this.m_elements == null) {
         this.create();
      }

      this.m_elements.putFloat(this.m_dx + var1);
      this.m_elements.putFloat(this.m_dy + var2);
      this.m_elements.putFloat(this.m_dz + var3);
      this.m_elements.putFloat(var6);
      this.m_elements.putFloat(var7);
      this.m_elements.putFloat(var8);
      this.m_elements.putFloat(var9);
      this.m_elements.putFloat(var4);
      this.m_elements.putFloat(var5);
      short var11 = (short)(this.m_indices.position() / 2);
      this.m_indices.putShort(var11);
      ++this.currentRun().count;
   }

   public void addElement(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.addElement(var1, var2, var3, 0.0F, 0.0F, var4, var5, var6, var7);
   }

   public void addLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      this.addElement(var1, var2, var3, var7, var8, var9, var10);
      this.addElement(var4, var5, var6, var7, var8, var9, var10);
   }

   public void addLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14) {
      this.addElement(var1, var2, var3, var7, var8, var9, var10);
      this.addElement(var4, var5, var6, var11, var12, var13, var14);
   }

   public void addTriangle(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19) {
      this.reserve(3);
      this.addElement(var1, var2, var3, var4, var5, var16, var17, var18, var19);
      this.addElement(var6, var7, var8, var9, var10, var16, var17, var18, var19);
      this.addElement(var11, var12, var13, var14, var15, var16, var17, var18, var19);
   }

   public void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      this.reserve(4);
      this.addElement(var1, var2, var9, var3, var4, var10, var11, var12, var13);
      this.addElement(var5, var2, var9, var7, var4, var10, var11, var12, var13);
      this.addElement(var5, var6, var9, var7, var8, var10, var11, var12, var13);
      this.addElement(var1, var6, var9, var3, var8, var10, var11, var12, var13);
   }

   public void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21) {
      this.reserve(4);
      this.addElement(var1, var2, var17, var3, var4, var18, var19, var20, var21);
      this.addElement(var5, var6, var17, var7, var8, var18, var19, var20, var21);
      this.addElement(var9, var10, var17, var11, var12, var18, var19, var20, var21);
      this.addElement(var13, var14, var17, var15, var16, var18, var19, var20, var21);
   }

   boolean isFull() {
      if (this.m_elements == null) {
         return false;
      } else if (this.m_mode == 4 && this.m_elements.position() % 108 == 0 && this.m_elements.position() + 108 > 4608) {
         return true;
      } else {
         return this.m_elements.position() == 4608;
      }
   }

   public void reserve(int var1) {
      if (!this.hasRoomFor(var1)) {
         TextureID var2 = this.currentRun() == null ? null : this.currentRun().textureID;
         this.flush();
         if (var2 != null) {
            this.startRun(var2);
         }
      }

   }

   boolean hasRoomFor(int var1) {
      return this.m_elements == null || this.m_elements.position() / 36 + var1 <= 128;
   }

   public void flush() {
      if (this.m_elements != null && this.m_elements.position() != 0) {
         this.m_elements.flip();
         this.m_indices.flip();
         GL13.glClientActiveTexture(33984);
         GL11.glEnableClientState(32888);
         this.m_vbo.bind();
         this.m_vbo.bufferData(this.m_elements);
         this.m_ibo.bind();
         this.m_ibo.bufferData(this.m_indices);
         GL11.glEnableClientState(32884);
         GL11.glEnableClientState(32886);
         GL11.glVertexPointer(3, 5126, 36, 0L);
         GL11.glColorPointer(4, 5126, 36, 12L);
         GL11.glTexCoordPointer(2, 5126, 36, 28L);
         GL11.glEnable(3553);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glLineWidth(this.m_lineWidth);

         for(int var1 = 0; var1 < this.m_runs.size(); ++var1) {
            VBOLinesUV.Run var2 = (VBOLinesUV.Run)this.m_runs.get(var1);
            int var3 = var2.start;
            int var4 = var2.count;
            int var5 = var2.start;
            int var6 = var5 + var2.count;
            if (var2.textureID.getID() == -1) {
               var2.textureID.bind();
            } else {
               GL11.glBindTexture(3553, Texture.lastTextureID = var2.textureID.getID());
               GL11.glTexParameteri(3553, 10241, 9729);
               GL11.glTexParameteri(3553, 10240, 9728);
            }

            GL12.glDrawRangeElements(this.m_mode, var3, var3 + var4, var6 - var5, 5123, (long)var5 * 2L);
         }

         this.m_vbo.bindNone();
         this.m_ibo.bindNone();
         this.m_elements.clear();
         this.m_indices.clear();
         this.m_runPool.releaseAll(this.m_runs);
         this.m_runs.clear();
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL13.glClientActiveTexture(33984);
         SpriteRenderer.ringBuffer.restoreVBOs = true;
         SpriteRenderer.ringBuffer.restoreBoundTextures = true;
      }
   }

   public void setLineWidth(float var1) {
      if (!PZMath.equal(this.m_lineWidth, var1, 0.01F)) {
         TextureID var2 = this.currentRun() == null ? null : this.currentRun().textureID;
         this.flush();
         if (var2 != null) {
            this.startRun(var2);
         }

         this.m_lineWidth = var1;
      }

   }

   public void setMode(int var1) {
      assert var1 == 1 || var1 == 4;

      if (var1 != this.m_mode) {
         TextureID var2 = this.currentRun() == null ? null : this.currentRun().textureID;
         this.flush();
         if (var2 != null) {
            this.startRun(var2);
         }

         this.m_mode = var1;
      }

   }

   public void startRun(TextureID var1) {
      VBOLinesUV.Run var2 = (VBOLinesUV.Run)this.m_runPool.alloc();
      var2.start = this.m_elements == null ? 0 : this.m_elements.position() / 36;
      var2.count = 0;
      var2.textureID = var1;
      this.m_runs.add(var2);
   }

   private static final class Run {
      int start;
      int count;
      TextureID textureID;
   }
}
