package zombie.fileSystem;

import java.io.IOException;
import java.io.InputStream;

public interface IFileDevice {
   IFile createFile(IFile var1);

   void destroyFile(IFile var1);

   InputStream createStream(String var1, InputStream var2) throws IOException;

   void destroyStream(InputStream var1);

   String name();
}
