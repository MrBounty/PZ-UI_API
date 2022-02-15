package zombie.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WinReqistry {
   public static String getSteamDirectory() {
      String var0 = readRegistry("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamPath");
      return var0;
   }

   public static final String readRegistry(String var0, String var1) {
      try {
         Process var2 = Runtime.getRuntime().exec("reg query \"" + var0 + "\" /v " + var1);
         WinReqistry.StreamReader var3 = new WinReqistry.StreamReader(var2.getInputStream());
         var3.start();
         var2.waitFor();
         var3.join();
         String var4 = var3.getResult();
         if (var4 != null && !var4.equals("")) {
            var4 = var4.substring(var4.indexOf("REG_SZ") + 7).trim();
            String[] var5 = var4.split("\t");
            return var5[var5.length - 1];
         } else {
            return null;
         }
      } catch (Exception var6) {
         return null;
      }
   }

   static class StreamReader extends Thread {
      private InputStream is;
      private StringWriter sw = new StringWriter();

      public StreamReader(InputStream var1) {
         this.is = var1;
      }

      public void run() {
         while(true) {
            try {
               int var1;
               if ((var1 = this.is.read()) != -1) {
                  this.sw.write(var1);
                  continue;
               }
            } catch (IOException var2) {
            }

            return;
         }
      }

      public String getResult() {
         return this.sw.toString();
      }
   }
}
