package zombie.fileSystem;

import java.io.IOException;
import java.io.InputStream;

public final class DeviceList {
   private final IFileDevice[] m_devices = new IFileDevice[8];

   public void add(IFileDevice var1) {
      for(int var2 = 0; var2 < this.m_devices.length; ++var2) {
         if (this.m_devices[var2] == null) {
            this.m_devices[var2] = var1;
            break;
         }
      }

   }

   public IFile createFile() {
      IFile var1 = null;

      for(int var2 = 0; var2 < this.m_devices.length && this.m_devices[var2] != null; ++var2) {
         var1 = this.m_devices[var2].createFile(var1);
      }

      return var1;
   }

   public InputStream createStream(String var1) throws IOException {
      InputStream var2 = null;

      for(int var3 = 0; var3 < this.m_devices.length && this.m_devices[var3] != null; ++var3) {
         var2 = this.m_devices[var3].createStream(var1, var2);
      }

      return var2;
   }
}
