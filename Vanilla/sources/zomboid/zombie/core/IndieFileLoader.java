package zombie.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import zombie.ZomboidFileSystem;

public class IndieFileLoader {
   public static InputStreamReader getStreamReader(String var0) throws FileNotFoundException {
      return getStreamReader(var0, false);
   }

   public static InputStreamReader getStreamReader(String var0, boolean var1) throws FileNotFoundException {
      InputStreamReader var2 = null;
      Object var3 = null;
      if (var3 != null && !var1) {
         var2 = new InputStreamReader((InputStream)var3);
      } else {
         try {
            FileInputStream var4 = new FileInputStream(ZomboidFileSystem.instance.getString(var0));
            var2 = new InputStreamReader(var4, "UTF-8");
         } catch (Exception var6) {
            String var10002 = Core.getMyDocumentFolder();
            FileInputStream var5 = new FileInputStream(var10002 + File.separator + "mods" + File.separator + var0);
            var2 = new InputStreamReader(var5);
         }
      }

      return var2;
   }
}
