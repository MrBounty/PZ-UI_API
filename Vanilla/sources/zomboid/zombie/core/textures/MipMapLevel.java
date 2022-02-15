package zombie.core.textures;

import java.nio.ByteBuffer;
import zombie.core.utils.DirectBufferAllocator;
import zombie.core.utils.WrappedBuffer;

public final class MipMapLevel {
   public final int width;
   public final int height;
   public final WrappedBuffer data;

   public MipMapLevel(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      this.data = DirectBufferAllocator.allocate(var1 * var2 * 4);
   }

   public MipMapLevel(int var1, int var2, WrappedBuffer var3) {
      this.width = var1;
      this.height = var2;
      this.data = var3;
   }

   public void dispose() {
      if (this.data != null) {
         this.data.dispose();
      }

   }

   public boolean isDisposed() {
      return this.data != null && this.data.isDisposed();
   }

   public void rewind() {
      if (this.data != null) {
         this.data.getBuffer().rewind();
      }

   }

   public ByteBuffer getBuffer() {
      return this.data == null ? null : this.data.getBuffer();
   }

   public int getDataSize() {
      return this.width * this.height * 4;
   }
}
