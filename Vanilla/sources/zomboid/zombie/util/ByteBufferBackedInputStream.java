package zombie.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class ByteBufferBackedInputStream extends InputStream {
   final ByteBuffer buf;

   public ByteBufferBackedInputStream(ByteBuffer var1) {
      Objects.requireNonNull(var1);
      this.buf = var1;
   }

   public int read() throws IOException {
      return !this.buf.hasRemaining() ? -1 : this.buf.get() & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.buf.hasRemaining()) {
         return -1;
      } else {
         var3 = Math.min(var3, this.buf.remaining());
         this.buf.get(var1, var2, var3);
         return var3;
      }
   }
}
