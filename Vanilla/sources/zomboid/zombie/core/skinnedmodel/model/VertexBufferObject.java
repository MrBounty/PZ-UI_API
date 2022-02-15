package zombie.core.skinnedmodel.model;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import org.lwjglx.BufferUtils;
import zombie.core.VBO.IGLBufferObject;
import zombie.core.opengl.RenderThread;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.debug.DebugOptions;
import zombie.util.list.PZArrayUtil;

public final class VertexBufferObject {
   public static IGLBufferObject funcs;
   int[] elements;
   VertexBufferObject.Vbo _handle;
   private final VertexBufferObject.VertexFormat m_vertexFormat;
   private VertexBufferObject.BeginMode _beginMode;
   public boolean bStatic = false;

   public VertexBufferObject() {
      this.bStatic = false;
      this.m_vertexFormat = new VertexBufferObject.VertexFormat(4);
      this.m_vertexFormat.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      this.m_vertexFormat.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      this.m_vertexFormat.setElement(2, VertexBufferObject.VertexType.ColorArray, 4);
      this.m_vertexFormat.setElement(3, VertexBufferObject.VertexType.TextureCoordArray, 8);
      this.m_vertexFormat.calculate();
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
   }

   /** @deprecated */
   @Deprecated
   public VertexBufferObject(VertexPositionNormalTangentTexture[] var1, int[] var2) {
      this.elements = var2;
      this.bStatic = true;
      RenderThread.invokeOnRenderContext(this, var1, var2, (var1x, var2x, var3) -> {
         var1x._handle = this.LoadVBO(var2x, var3);
      });
      this.m_vertexFormat = new VertexBufferObject.VertexFormat(4);
      this.m_vertexFormat.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      this.m_vertexFormat.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      this.m_vertexFormat.setElement(2, VertexBufferObject.VertexType.TangentArray, 12);
      this.m_vertexFormat.setElement(3, VertexBufferObject.VertexType.TextureCoordArray, 8);
      this.m_vertexFormat.calculate();
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
   }

   /** @deprecated */
   @Deprecated
   public VertexBufferObject(VertexPositionNormalTangentTextureSkin[] var1, int[] var2, boolean var3) {
      this.elements = var2;
      if (var3) {
         int[] var4 = new int[var2.length];
         int var5 = 0;

         for(int var6 = var2.length - 1 - 2; var6 >= 0; var6 -= 3) {
            var4[var5] = var2[var6];
            var4[var5 + 1] = var2[var6 + 1];
            var4[var5 + 2] = var2[var6 + 2];
            var5 += 3;
         }

         var2 = var4;
      }

      this.bStatic = false;
      this._handle = this.LoadVBO(var1, var2);
      this.m_vertexFormat = new VertexBufferObject.VertexFormat(6);
      this.m_vertexFormat.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      this.m_vertexFormat.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      this.m_vertexFormat.setElement(2, VertexBufferObject.VertexType.TangentArray, 12);
      this.m_vertexFormat.setElement(3, VertexBufferObject.VertexType.TextureCoordArray, 8);
      this.m_vertexFormat.setElement(4, VertexBufferObject.VertexType.BlendWeightArray, 16);
      this.m_vertexFormat.setElement(5, VertexBufferObject.VertexType.BlendIndexArray, 16);
      this.m_vertexFormat.calculate();
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
   }

   public VertexBufferObject(VertexBufferObject.VertexArray var1, int[] var2) {
      this.m_vertexFormat = var1.m_format;
      this.elements = var2;
      this.bStatic = true;
      RenderThread.invokeOnRenderContext(this, var1, var2, (var1x, var2x, var3) -> {
         var1x._handle = this.LoadVBO(var2x, var3);
      });
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
   }

   public VertexBufferObject(VertexBufferObject.VertexArray var1, int[] var2, boolean var3) {
      this.m_vertexFormat = var1.m_format;
      if (var3) {
         int[] var4 = new int[var2.length];
         int var5 = 0;

         for(int var6 = var2.length - 1 - 2; var6 >= 0; var6 -= 3) {
            var4[var5] = var2[var6];
            var4[var5 + 1] = var2[var6 + 1];
            var4[var5 + 2] = var2[var6 + 2];
            var5 += 3;
         }

         var2 = var4;
      }

      this.elements = var2;
      this.bStatic = false;
      this._handle = this.LoadVBO(var1, var2);
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
   }

   /** @deprecated */
   @Deprecated
   private VertexBufferObject.Vbo LoadVBO(VertexPositionNormalTangentTextureSkin[] var1, int[] var2) {
      VertexBufferObject.Vbo var3 = new VertexBufferObject.Vbo();
      boolean var4 = false;
      byte var5 = 76;
      var3.FaceDataOnly = false;
      ByteBuffer var6 = BufferUtils.createByteBuffer(var1.length * var5);
      ByteBuffer var7 = BufferUtils.createByteBuffer(var2.length * 4);

      int var8;
      for(var8 = 0; var8 < var1.length; ++var8) {
         var1[var8].put(var6);
      }

      for(var8 = 0; var8 < var2.length; ++var8) {
         var7.putInt(var2[var8]);
      }

      var6.flip();
      var7.flip();
      var3.VboID = funcs.glGenBuffers();
      funcs.glBindBuffer(funcs.GL_ARRAY_BUFFER(), var3.VboID);
      funcs.glBufferData(funcs.GL_ARRAY_BUFFER(), var6, funcs.GL_STATIC_DRAW());
      funcs.glGetBufferParameter(funcs.GL_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
      int var9 = var3.b.get();
      if (var1.length * var5 != var9) {
         throw new RuntimeException("Vertex data not uploaded correctly");
      } else {
         var3.EboID = funcs.glGenBuffers();
         funcs.glBindBuffer(funcs.GL_ELEMENT_ARRAY_BUFFER(), var3.EboID);
         funcs.glBufferData(funcs.GL_ELEMENT_ARRAY_BUFFER(), var7, funcs.GL_STATIC_DRAW());
         var3.b.clear();
         funcs.glGetBufferParameter(funcs.GL_ELEMENT_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
         var9 = var3.b.get();
         if (var2.length * 4 != var9) {
            throw new RuntimeException("Element data not uploaded correctly");
         } else {
            var3.NumElements = var2.length;
            var3.VertexStride = var5;
            return var3;
         }
      }
   }

   public VertexBufferObject.Vbo LoadSoftwareVBO(ByteBuffer var1, VertexBufferObject.Vbo var2, int[] var3) {
      VertexBufferObject.Vbo var4 = var2;
      boolean var5 = false;
      ByteBuffer var6 = null;
      if (var2 == null) {
         var5 = true;
         var4 = new VertexBufferObject.Vbo();
         var4.VboID = funcs.glGenBuffers();
         ByteBuffer var7 = BufferUtils.createByteBuffer(var3.length * 4);

         for(int var8 = 0; var8 < var3.length; ++var8) {
            var7.putInt(var3[var8]);
         }

         var7.flip();
         var6 = var7;
         var4.VertexStride = 36;
         var4.NumElements = var3.length;
      } else {
         var2.b.clear();
      }

      var4.FaceDataOnly = false;
      funcs.glBindBuffer(funcs.GL_ARRAY_BUFFER(), var4.VboID);
      funcs.glBufferData(funcs.GL_ARRAY_BUFFER(), var1, funcs.GL_STATIC_DRAW());
      funcs.glGetBufferParameter(funcs.GL_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var4.b);
      if (var6 != null) {
         var4.EboID = funcs.glGenBuffers();
         funcs.glBindBuffer(funcs.GL_ELEMENT_ARRAY_BUFFER(), var4.EboID);
         funcs.glBufferData(funcs.GL_ELEMENT_ARRAY_BUFFER(), var6, funcs.GL_STATIC_DRAW());
      }

      return var4;
   }

   /** @deprecated */
   @Deprecated
   private VertexBufferObject.Vbo LoadVBO(VertexPositionNormalTangentTexture[] var1, int[] var2) {
      VertexBufferObject.Vbo var3 = new VertexBufferObject.Vbo();
      boolean var4 = false;
      byte var5 = 44;
      var3.FaceDataOnly = false;
      ByteBuffer var6 = BufferUtils.createByteBuffer(var1.length * var5);
      ByteBuffer var7 = BufferUtils.createByteBuffer(var2.length * 4);

      int var8;
      for(var8 = 0; var8 < var1.length; ++var8) {
         var1[var8].put(var6);
      }

      for(var8 = 0; var8 < var2.length; ++var8) {
         var7.putInt(var2[var8]);
      }

      var6.flip();
      var7.flip();
      var3.VboID = funcs.glGenBuffers();
      funcs.glBindBuffer(funcs.GL_ARRAY_BUFFER(), var3.VboID);
      funcs.glBufferData(funcs.GL_ARRAY_BUFFER(), var6, funcs.GL_STATIC_DRAW());
      funcs.glGetBufferParameter(funcs.GL_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
      int var9 = var3.b.get();
      if (var1.length * var5 != var9) {
         throw new RuntimeException("Vertex data not uploaded correctly");
      } else {
         var3.EboID = funcs.glGenBuffers();
         funcs.glBindBuffer(funcs.GL_ELEMENT_ARRAY_BUFFER(), var3.EboID);
         funcs.glBufferData(funcs.GL_ELEMENT_ARRAY_BUFFER(), var7, funcs.GL_STATIC_DRAW());
         var3.b.clear();
         funcs.glGetBufferParameter(funcs.GL_ELEMENT_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
         var9 = var3.b.get();
         if (var2.length * 4 != var9) {
            throw new RuntimeException("Element data not uploaded correctly");
         } else {
            var3.NumElements = var2.length;
            var3.VertexStride = var5;
            return var3;
         }
      }
   }

   private VertexBufferObject.Vbo LoadVBO(VertexBufferObject.VertexArray var1, int[] var2) {
      VertexBufferObject.Vbo var3 = new VertexBufferObject.Vbo();
      var3.FaceDataOnly = false;
      ByteBuffer var4 = MemoryUtil.memAlloc(var2.length * 4);

      int var5;
      for(var5 = 0; var5 < var2.length; ++var5) {
         var4.putInt(var2[var5]);
      }

      var1.m_buffer.position(0);
      var1.m_buffer.limit(var1.m_numVertices * var1.m_format.m_stride);
      var4.flip();
      var3.VboID = funcs.glGenBuffers();
      funcs.glBindBuffer(funcs.GL_ARRAY_BUFFER(), var3.VboID);
      funcs.glBufferData(funcs.GL_ARRAY_BUFFER(), var1.m_buffer, funcs.GL_STATIC_DRAW());
      funcs.glGetBufferParameter(funcs.GL_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
      var5 = var3.b.get();
      if (var1.m_numVertices * var1.m_format.m_stride != var5) {
         throw new RuntimeException("Vertex data not uploaded correctly");
      } else {
         var3.EboID = funcs.glGenBuffers();
         funcs.glBindBuffer(funcs.GL_ELEMENT_ARRAY_BUFFER(), var3.EboID);
         funcs.glBufferData(funcs.GL_ELEMENT_ARRAY_BUFFER(), var4, funcs.GL_STATIC_DRAW());
         MemoryUtil.memFree(var4);
         var3.b.clear();
         funcs.glGetBufferParameter(funcs.GL_ELEMENT_ARRAY_BUFFER(), funcs.GL_BUFFER_SIZE(), var3.b);
         var5 = var3.b.get();
         if (var2.length * 4 != var5) {
            throw new RuntimeException("Element data not uploaded correctly");
         } else {
            var3.NumElements = var2.length;
            var3.VertexStride = var1.m_format.m_stride;
            return var3;
         }
      }
   }

   public void clear() {
      if (this._handle != null) {
         if (this._handle.VboID > 0) {
            funcs.glDeleteBuffers(this._handle.VboID);
            this._handle.VboID = -1;
         }

         if (this._handle.EboID > 0) {
            funcs.glDeleteBuffers(this._handle.EboID);
            this._handle.EboID = -1;
         }

         this._handle = null;
      }
   }

   public void Draw(Shader var1) {
      Draw(this._handle, this.m_vertexFormat, var1, 4);
   }

   public void DrawStrip(Shader var1) {
      Draw(this._handle, this.m_vertexFormat, var1, 5);
   }

   private static void Draw(VertexBufferObject.Vbo var0, VertexBufferObject.VertexFormat var1, Shader var2, int var3) {
      if (var0 != null) {
         if (!DebugOptions.instance.DebugDraw_SkipVBODraw.getValue()) {
            int var4 = 33984;
            boolean var5 = false;
            int var6;
            if (!var0.FaceDataOnly) {
               funcs.glBindBuffer(funcs.GL_ARRAY_BUFFER(), var0.VboID);

               for(var6 = 0; var6 < var1.m_elements.length; ++var6) {
                  VertexBufferObject.VertexElement var7 = var1.m_elements[var6];
                  switch(var7.m_type) {
                  case VertexArray:
                     GL20.glVertexPointer(3, 5126, var0.VertexStride, (long)var7.m_byteOffset);
                     GL20.glEnableClientState(32884);
                     break;
                  case NormalArray:
                     GL20.glNormalPointer(5126, var0.VertexStride, (long)var7.m_byteOffset);
                     GL20.glEnableClientState(32885);
                     break;
                  case ColorArray:
                     GL20.glColorPointer(3, 5121, var0.VertexStride, (long)var7.m_byteOffset);
                     GL20.glEnableClientState(32886);
                     break;
                  case TextureCoordArray:
                     GL20.glActiveTexture(var4);
                     GL20.glClientActiveTexture(var4);
                     GL20.glTexCoordPointer(2, 5126, var0.VertexStride, (long)var7.m_byteOffset);
                     ++var4;
                     GL20.glEnableClientState(32888);
                  case TangentArray:
                  default:
                     break;
                  case BlendWeightArray:
                     int var8 = var2.BoneWeightsAttrib;
                     GL20.glVertexAttribPointer(var8, 4, 5126, false, var0.VertexStride, (long)var7.m_byteOffset);
                     GL20.glEnableVertexAttribArray(var8);
                     var5 = true;
                     break;
                  case BlendIndexArray:
                     int var9 = var2.BoneIndicesAttrib;
                     GL20.glVertexAttribPointer(var9, 4, 5126, false, var0.VertexStride, (long)var7.m_byteOffset);
                     GL20.glEnableVertexAttribArray(var9);
                  }
               }
            }

            funcs.glBindBuffer(funcs.GL_ELEMENT_ARRAY_BUFFER(), var0.EboID);
            GL20.glDrawElements(var3, var0.NumElements, 5125, 0L);
            GL20.glDisableClientState(32885);
            if (var5 && var2 != null) {
               var6 = var2.BoneWeightsAttrib;
               GL20.glDisableVertexAttribArray(var6);
               var6 = var2.BoneIndicesAttrib;
               GL20.glDisableVertexAttribArray(var6);
            }

         }
      }
   }

   public static final class VertexFormat {
      final VertexBufferObject.VertexElement[] m_elements;
      int m_stride;

      public VertexFormat(int var1) {
         this.m_elements = (VertexBufferObject.VertexElement[])PZArrayUtil.newInstance(VertexBufferObject.VertexElement.class, var1, VertexBufferObject.VertexElement::new);
      }

      public void setElement(int var1, VertexBufferObject.VertexType var2, int var3) {
         this.m_elements[var1].m_type = var2;
         this.m_elements[var1].m_byteSize = var3;
      }

      public void calculate() {
         this.m_stride = 0;

         for(int var1 = 0; var1 < this.m_elements.length; ++var1) {
            this.m_elements[var1].m_byteOffset = this.m_stride;
            this.m_stride += this.m_elements[var1].m_byteSize;
         }

      }
   }

   public static enum VertexType {
      VertexArray,
      NormalArray,
      ColorArray,
      IndexArray,
      TextureCoordArray,
      TangentArray,
      BlendWeightArray,
      BlendIndexArray;

      // $FF: synthetic method
      private static VertexBufferObject.VertexType[] $values() {
         return new VertexBufferObject.VertexType[]{VertexArray, NormalArray, ColorArray, IndexArray, TextureCoordArray, TangentArray, BlendWeightArray, BlendIndexArray};
      }
   }

   public static enum BeginMode {
      Triangles;

      // $FF: synthetic method
      private static VertexBufferObject.BeginMode[] $values() {
         return new VertexBufferObject.BeginMode[]{Triangles};
      }
   }

   public static final class Vbo {
      public final IntBuffer b = BufferUtils.createIntBuffer(4);
      public int VboID;
      public int EboID;
      public int NumElements;
      public int VertexStride;
      public boolean FaceDataOnly;
   }

   public static final class VertexArray {
      public final VertexBufferObject.VertexFormat m_format;
      public final int m_numVertices;
      public final ByteBuffer m_buffer;

      public VertexArray(VertexBufferObject.VertexFormat var1, int var2) {
         this.m_format = var1;
         this.m_numVertices = var2;
         this.m_buffer = BufferUtils.createByteBuffer(this.m_numVertices * this.m_format.m_stride);
      }

      public void setElement(int var1, int var2, float var3, float var4) {
         int var5 = var1 * this.m_format.m_stride + this.m_format.m_elements[var2].m_byteOffset;
         this.m_buffer.putFloat(var5, var3);
         var5 += 4;
         this.m_buffer.putFloat(var5, var4);
      }

      public void setElement(int var1, int var2, float var3, float var4, float var5) {
         int var6 = var1 * this.m_format.m_stride + this.m_format.m_elements[var2].m_byteOffset;
         this.m_buffer.putFloat(var6, var3);
         var6 += 4;
         this.m_buffer.putFloat(var6, var4);
         var6 += 4;
         this.m_buffer.putFloat(var6, var5);
      }

      public void setElement(int var1, int var2, float var3, float var4, float var5, float var6) {
         int var7 = var1 * this.m_format.m_stride + this.m_format.m_elements[var2].m_byteOffset;
         this.m_buffer.putFloat(var7, var3);
         var7 += 4;
         this.m_buffer.putFloat(var7, var4);
         var7 += 4;
         this.m_buffer.putFloat(var7, var5);
         var7 += 4;
         this.m_buffer.putFloat(var7, var6);
      }

      float getElementFloat(int var1, int var2, int var3) {
         int var4 = var1 * this.m_format.m_stride + this.m_format.m_elements[var2].m_byteOffset + var3 * 4;
         return this.m_buffer.getFloat(var4);
      }
   }

   public static final class VertexElement {
      public VertexBufferObject.VertexType m_type;
      public int m_byteSize;
      public int m_byteOffset;
   }
}
