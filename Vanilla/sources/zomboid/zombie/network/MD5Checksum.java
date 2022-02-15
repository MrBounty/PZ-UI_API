package zombie.network;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.CRC32;

public class MD5Checksum {
   public static long createChecksum(String var0) throws Exception {
      File var1 = new File(var0);
      if (!var1.exists()) {
         return 0L;
      } else {
         FileInputStream var2 = new FileInputStream(var0);
         CRC32 var3 = new CRC32();
         byte[] var4 = new byte[1024];

         int var5;
         while((var5 = var2.read(var4)) != -1) {
            var3.update(var4, 0, var5);
         }

         long var6 = var3.getValue();
         var2.close();
         return var6;
      }
   }

   public static void main(String[] var0) {
   }
}
