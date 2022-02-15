package zombie.fileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class MemoryFileDevice implements IFileDevice {
   public IFile createFile(IFile var1) {
      return new MemoryFileDevice.MemoryFile(var1, this);
   }

   public void destroyFile(IFile var1) {
   }

   public InputStream createStream(String var1, InputStream var2) throws IOException {
      return null;
   }

   public void destroyStream(InputStream var1) {
   }

   public String name() {
      return "memory";
   }

   private static class MemoryFile implements IFile {
      final MemoryFileDevice m_device;
      byte[] m_buffer;
      long m_size;
      long m_pos;
      IFile m_file;
      boolean m_write;

      MemoryFile(IFile var1, MemoryFileDevice var2) {
         this.m_device = var2;
         this.m_buffer = null;
         this.m_size = 0L;
         this.m_pos = 0L;
         this.m_file = var1;
         this.m_write = false;
      }

      public boolean open(String var1, int var2) {
         assert this.m_buffer == null;

         this.m_write = (var2 & 2) != 0;
         if (this.m_file != null) {
            if (this.m_file.open(var1, var2)) {
               if ((var2 & 1) != 0) {
                  this.m_size = this.m_file.size();
                  this.m_buffer = new byte[(int)this.m_size];
                  this.m_file.read(this.m_buffer, this.m_size);
                  this.m_pos = 0L;
               }

               return true;
            }
         } else if ((var2 & 2) != 0) {
            return true;
         }

         return false;
      }

      public void close() {
         if (this.m_file != null) {
            if (this.m_write) {
               this.m_file.seek(FileSeekMode.BEGIN, 0L);
               this.m_file.write(this.m_buffer, this.m_size);
            }

            this.m_file.close();
         }

         this.m_buffer = null;
      }

      public boolean read(byte[] var1, long var2) {
         long var4 = this.m_pos + var2 < this.m_size ? var2 : this.m_size - this.m_pos;
         System.arraycopy(this.m_buffer, (int)this.m_pos, var1, 0, (int)var4);
         this.m_pos += var4;
         return false;
      }

      public boolean write(byte[] var1, long var2) {
         long var4 = this.m_pos;
         long var6 = (long)this.m_buffer.length;
         long var8 = this.m_size;
         if (var4 + var2 > var6) {
            long var10 = Math.max(var6 * 2L, var4 + var2);
            this.m_buffer = Arrays.copyOf(this.m_buffer, (int)var10);
         }

         System.arraycopy(var1, 0, this.m_buffer, (int)var4, (int)var2);
         this.m_pos += var2;
         this.m_size = var4 + var2 > var8 ? var4 + var2 : var8;
         return true;
      }

      public byte[] getBuffer() {
         return this.m_buffer;
      }

      public long size() {
         return this.m_size;
      }

      public boolean seek(FileSeekMode var1, long var2) {
         switch(var1) {
         case BEGIN:
            assert var2 <= this.m_size;

            this.m_pos = var2;
            break;
         case CURRENT:
            assert 0L <= this.m_pos + var2 && this.m_pos + var2 <= this.m_size;

            this.m_pos += var2;
            break;
         case END:
            assert var2 <= this.m_size;

            this.m_pos = this.m_size - var2;
         }

         boolean var4 = this.m_pos <= this.m_size;
         this.m_pos = Math.min(this.m_pos, this.m_size);
         return var4;
      }

      public long pos() {
         return this.m_pos;
      }

      public InputStream getInputStream() {
         return this.m_file != null ? this.m_file.getInputStream() : null;
      }

      public IFileDevice getDevice() {
         return this.m_device;
      }

      public void release() {
         this.m_buffer = null;
      }
   }
}
