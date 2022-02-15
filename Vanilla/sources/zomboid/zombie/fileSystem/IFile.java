package zombie.fileSystem;

import java.io.InputStream;

public interface IFile {
   boolean open(String var1, int var2);

   void close();

   boolean read(byte[] var1, long var2);

   boolean write(byte[] var1, long var2);

   byte[] getBuffer();

   long size();

   boolean seek(FileSeekMode var1, long var2);

   long pos();

   InputStream getInputStream();

   IFileDevice getDevice();

   void release();
}
