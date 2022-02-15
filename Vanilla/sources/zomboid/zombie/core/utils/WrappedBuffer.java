package zombie.core.utils;

import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryUtil;

public final class WrappedBuffer {
   private ByteBuffer buf;
   private final int capacity;
   private boolean disposed;

   public WrappedBuffer(int var1) {
      this.buf = MemoryUtil.memAlloc(var1);
      MemoryUtil.memSet(this.buf, 0);
      this.capacity = this.buf.capacity();
   }

   public ByteBuffer getBuffer() {
      if (this.disposed) {
         throw new IllegalStateException("Can't get buffer after disposal");
      } else {
         return this.buf;
      }
   }

   public int capacity() {
      return this.capacity;
   }

   public void dispose() {
      if (this.disposed) {
         throw new IllegalStateException("WrappedBuffer was already disposed");
      } else {
         this.disposed = true;
         MemoryUtil.memFree(this.buf);
         this.buf = null;
      }
   }

   public boolean isDisposed() {
      return this.disposed;
   }
}
