package zombie.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public final class BufferedRandomAccessFile extends RandomAccessFile {
   private byte[] buffer;
   private int buf_end = 0;
   private int buf_pos = 0;
   private long real_pos = 0L;
   private final int BUF_SIZE;

   public BufferedRandomAccessFile(String var1, String var2, int var3) throws IOException {
      super(var1, var2);
      this.invalidate();
      this.BUF_SIZE = var3;
      this.buffer = new byte[this.BUF_SIZE];
   }

   public BufferedRandomAccessFile(File var1, String var2, int var3) throws IOException {
      super(var1, var2);
      this.invalidate();
      this.BUF_SIZE = var3;
      this.buffer = new byte[this.BUF_SIZE];
   }

   public final int read() throws IOException {
      if (this.buf_pos >= this.buf_end && this.fillBuffer() < 0) {
         return -1;
      } else {
         return this.buf_end == 0 ? -1 : this.buffer[this.buf_pos++] & 255;
      }
   }

   private int fillBuffer() throws IOException {
      int var1 = super.read(this.buffer, 0, this.BUF_SIZE);
      if (var1 >= 0) {
         this.real_pos += (long)var1;
         this.buf_end = var1;
         this.buf_pos = 0;
      }

      return var1;
   }

   private void invalidate() throws IOException {
      this.buf_end = 0;
      this.buf_pos = 0;
      this.real_pos = super.getFilePointer();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.buf_end - this.buf_pos;
      if (var3 <= var4) {
         System.arraycopy(this.buffer, this.buf_pos, var1, var2, var3);
         this.buf_pos += var3;
         return var3;
      } else {
         for(int var5 = 0; var5 < var3; ++var5) {
            int var6 = this.read();
            if (var6 == -1) {
               return var5;
            }

            var1[var2 + var5] = (byte)var6;
         }

         return var3;
      }
   }

   public long getFilePointer() throws IOException {
      long var1 = this.real_pos;
      return var1 - (long)this.buf_end + (long)this.buf_pos;
   }

   public void seek(long var1) throws IOException {
      int var3 = (int)(this.real_pos - var1);
      if (var3 >= 0 && var3 <= this.buf_end) {
         this.buf_pos = this.buf_end - var3;
      } else {
         super.seek(var1);
         this.invalidate();
      }

   }

   public final String getNextLine() throws IOException {
      String var1 = null;
      if (this.buf_end - this.buf_pos <= 0 && this.fillBuffer() < 0) {
         throw new IOException("error in filling buffer!");
      } else {
         int var2 = -1;

         for(int var3 = this.buf_pos; var3 < this.buf_end; ++var3) {
            if (this.buffer[var3] == 10) {
               var2 = var3;
               break;
            }
         }

         if (var2 < 0) {
            StringBuilder var5 = new StringBuilder(128);

            int var4;
            while((var4 = this.read()) != -1 && var4 != 10) {
               var5.append((char)var4);
            }

            return var4 == -1 && var5.length() == 0 ? null : var5.toString();
         } else {
            if (var2 > 0 && this.buffer[var2 - 1] == 13) {
               var1 = new String(this.buffer, this.buf_pos, var2 - this.buf_pos - 1, StandardCharsets.UTF_8);
            } else {
               var1 = new String(this.buffer, this.buf_pos, var2 - this.buf_pos, StandardCharsets.UTF_8);
            }

            this.buf_pos = var2 + 1;
            return var1;
         }
      }
   }
}
