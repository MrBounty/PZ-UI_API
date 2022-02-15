package zombie.core.VBO;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.ARBMapBufferRange;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjglx.opengl.OpenGLException;
import zombie.core.skinnedmodel.model.VertexBufferObject;

public class GLVertexBufferObject {
   public static IGLBufferObject funcs;
   private long size;
   private final int type;
   private final int usage;
   private transient int id;
   private transient boolean mapped;
   private transient boolean cleared;
   private transient ByteBuffer buffer;
   private int m_vertexAttribArray = -1;

   public static void init() {
      if (GL.getCapabilities().OpenGL15) {
         System.out.println("OpenGL 1.5 buffer objects supported");
         funcs = new GLBufferObject15();
      } else {
         if (!GL.getCapabilities().GL_ARB_vertex_buffer_object) {
            throw new RuntimeException("Neither OpenGL 1.5 nor GL_ARB_vertex_buffer_object supported");
         }

         System.out.println("GL_ARB_vertex_buffer_object supported");
         funcs = new GLBufferObjectARB();
      }

      VertexBufferObject.funcs = funcs;
   }

   public GLVertexBufferObject(long var1, int var3, int var4) {
      this.size = var1;
      this.type = var3;
      this.usage = var4;
   }

   public GLVertexBufferObject(int var1, int var2) {
      this.size = 0L;
      this.type = var1;
      this.usage = var2;
   }

   public void create() {
      this.id = funcs.glGenBuffers();
   }

   public void clear() {
      if (!this.cleared) {
         funcs.glBufferData(this.type, this.size, this.usage);
         this.cleared = true;
      }

   }

   protected void doDestroy() {
      if (this.id != 0) {
         this.unmap();
         funcs.glDeleteBuffers(this.id);
         this.id = 0;
      }

   }

   public ByteBuffer map(int var1) {
      if (!this.mapped) {
         if (this.size != (long)var1) {
            this.size = (long)var1;
            this.clear();
         }

         if (this.buffer != null && this.buffer.capacity() < var1) {
            this.buffer = null;
         }

         ByteBuffer var2 = this.buffer;
         byte var3;
         if (GL.getCapabilities().OpenGL30) {
            var3 = 34;
            this.buffer = GL30.glMapBufferRange(this.type, 0L, (long)var1, var3, this.buffer);
         } else if (GL.getCapabilities().GL_ARB_map_buffer_range) {
            var3 = 34;
            this.buffer = ARBMapBufferRange.glMapBufferRange(this.type, 0L, (long)var1, var3, this.buffer);
         } else {
            this.buffer = funcs.glMapBuffer(this.type, funcs.GL_WRITE_ONLY(), (long)var1, this.buffer);
         }

         if (this.buffer == null) {
            throw new OpenGLException("Failed to map buffer " + this);
         }

         if (this.buffer != var2 && var2 != null) {
         }

         this.buffer.order(ByteOrder.nativeOrder()).clear().limit(var1);
         this.mapped = true;
         this.cleared = false;
      }

      return this.buffer;
   }

   public ByteBuffer map() {
      if (!this.mapped) {
         assert this.size > 0L;

         this.clear();
         ByteBuffer var1 = this.buffer;
         byte var2;
         if (GL.getCapabilities().OpenGL30) {
            var2 = 34;
            this.buffer = GL30.glMapBufferRange(this.type, 0L, this.size, var2, this.buffer);
         } else if (GL.getCapabilities().GL_ARB_map_buffer_range) {
            var2 = 34;
            this.buffer = ARBMapBufferRange.glMapBufferRange(this.type, 0L, this.size, var2, this.buffer);
         } else {
            this.buffer = funcs.glMapBuffer(this.type, funcs.GL_WRITE_ONLY(), this.size, this.buffer);
         }

         if (this.buffer == null) {
            throw new OpenGLException("Failed to map a buffer " + this.size + " bytes long");
         }

         if (this.buffer != var1 && var1 != null) {
         }

         this.buffer.order(ByteOrder.nativeOrder()).clear().limit((int)this.size);
         this.mapped = true;
         this.cleared = false;
      }

      return this.buffer;
   }

   public void orphan() {
      funcs.glMapBuffer(this.type, this.usage, this.size, (ByteBuffer)null);
   }

   public boolean unmap() {
      if (this.mapped) {
         this.mapped = false;
         return funcs.glUnmapBuffer(this.type);
      } else {
         return true;
      }
   }

   public boolean isMapped() {
      return this.mapped;
   }

   public void bufferData(ByteBuffer var1) {
      funcs.glBufferData(this.type, var1, this.usage);
   }

   public String toString() {
      return "GLVertexBufferObject[" + this.id + ", " + this.size + "]";
   }

   public void bind() {
      funcs.glBindBuffer(this.type, this.id);
   }

   public void bindNone() {
      funcs.glBindBuffer(this.type, 0);
   }

   public int getID() {
      return this.id;
   }

   public void enableVertexAttribArray(int var1) {
      if (this.m_vertexAttribArray != var1) {
         this.disableVertexAttribArray();
         if (var1 >= 0) {
            GL20.glEnableVertexAttribArray(var1);
         }

         this.m_vertexAttribArray = var1 >= 0 ? var1 : -1;
      }

   }

   public void disableVertexAttribArray() {
      if (this.m_vertexAttribArray != -1) {
         GL20.glDisableVertexAttribArray(this.m_vertexAttribArray);
         this.m_vertexAttribArray = -1;
      }

   }
}
