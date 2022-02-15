package zombie.fileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import zombie.core.logger.ExceptionLogger;

public final class DiskFileDevice implements IFileDevice {
   private final String m_name;

   public DiskFileDevice(String var1) {
      this.m_name = var1;
   }

   public IFile createFile(IFile var1) {
      return new DiskFileDevice.DiskFile(var1, this);
   }

   public void destroyFile(IFile var1) {
   }

   public InputStream createStream(String var1, InputStream var2) throws IOException {
      return new FileInputStream(var1);
   }

   public void destroyStream(InputStream var1) {
   }

   public String name() {
      return this.m_name;
   }

   private static final class DiskFile implements IFile {
      final DiskFileDevice m_device;
      RandomAccessFile m_file;
      InputStream m_inputStream;
      final IFile m_fallthrough;
      boolean m_use_fallthrough;

      DiskFile(IFile var1, DiskFileDevice var2) {
         this.m_device = var2;
         this.m_fallthrough = var1;
         this.m_use_fallthrough = false;
      }

      public boolean open(String var1, int var2) {
         File var3 = new File(var1);
         boolean var4 = (var2 & 1) != 0;
         if (var4 && !var3.exists() && this.m_fallthrough != null) {
            this.m_use_fallthrough = true;
            return this.m_fallthrough.open(var1, var2);
         } else {
            try {
               if ((var2 & 16) == 0) {
                  this.m_file = new RandomAccessFile(var1, FileOpenMode.toStringMode(var2));
               } else {
                  this.m_inputStream = new FileInputStream(var1);
               }

               return true;
            } catch (IOException var6) {
               ExceptionLogger.logException(var6);
               return false;
            }
         }
      }

      public void close() {
         if (this.m_fallthrough != null) {
            this.m_fallthrough.close();
         }

         if (this.m_file != null || this.m_inputStream != null) {
            try {
               if (this.m_file != null) {
                  this.m_file.close();
               }

               if (this.m_inputStream != null) {
                  this.m_inputStream.close();
               }
            } catch (IOException var2) {
               ExceptionLogger.logException(var2);
            }

            this.m_file = null;
            this.m_inputStream = null;
            this.m_use_fallthrough = false;
         }
      }

      public boolean read(byte[] var1, long var2) {
         if (this.m_use_fallthrough) {
            return this.m_fallthrough.read(var1, var2);
         } else if (this.m_file == null) {
            return false;
         } else {
            try {
               return (long)this.m_file.read(var1, 0, (int)var2) == var2;
            } catch (IOException var5) {
               ExceptionLogger.logException(var5);
               return false;
            }
         }
      }

      public boolean write(byte[] var1, long var2) {
         if (this.m_use_fallthrough) {
            return this.m_fallthrough.write(var1, var2);
         } else if (this.m_file == null) {
            return false;
         } else {
            try {
               this.m_file.write(var1, 0, (int)var2);
               return true;
            } catch (IOException var5) {
               ExceptionLogger.logException(var5);
               return false;
            }
         }
      }

      public byte[] getBuffer() {
         return this.m_use_fallthrough ? this.m_fallthrough.getBuffer() : null;
      }

      public long size() {
         if (this.m_use_fallthrough) {
            return this.m_fallthrough.size();
         } else if (this.m_file == null) {
            return 0L;
         } else {
            try {
               return this.m_file.length();
            } catch (IOException var2) {
               ExceptionLogger.logException(var2);
               return 0L;
            }
         }
      }

      public boolean seek(FileSeekMode var1, long var2) {
         if (this.m_use_fallthrough) {
            return this.m_fallthrough.seek(var1, var2);
         } else if (this.m_file == null) {
            return false;
         } else {
            try {
               switch(var1) {
               case BEGIN:
               default:
                  break;
               case CURRENT:
                  var2 += this.m_file.getFilePointer();
                  break;
               case END:
                  var2 += this.m_file.length();
               }

               this.m_file.seek(var2);
               return true;
            } catch (IOException var5) {
               ExceptionLogger.logException(var5);
               return false;
            }
         }
      }

      public long pos() {
         if (this.m_use_fallthrough) {
            return this.m_fallthrough.pos();
         } else if (this.m_file == null) {
            return 0L;
         } else {
            try {
               return this.m_file.getFilePointer();
            } catch (IOException var2) {
               ExceptionLogger.logException(var2);
               return 0L;
            }
         }
      }

      public InputStream getInputStream() {
         return this.m_inputStream;
      }

      public IFileDevice getDevice() {
         return this.m_device;
      }

      public void release() {
         this.getDevice().destroyFile(this);
      }
   }
}
