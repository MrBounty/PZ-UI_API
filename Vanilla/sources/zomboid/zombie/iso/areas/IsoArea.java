package zombie.iso.areas;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Mapping0;
import java.io.FileInputStream;

public class IsoArea {
   public static String version = "0a2a0q";
   public static boolean Doobo;

   public static byte[] asasa(String var0) throws Exception {
      new FileInputStream(var0);
      byte[] var2 = new byte[1024];
      return var2;
   }

   public static String Ardo(String var0) throws Exception {
      byte[] var1 = asasa(var0);
      String var2 = "";

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 = Block.asdsadsa(var2, var1, var3);
      }

      return var2;
   }

   public static boolean Thigglewhat2(String var0, String var1) {
      String var2 = "";

      try {
         var2 = Ardo(var0);
         if (!var2.equals(var1)) {
            return false;
         }
      } catch (Exception var6) {
         var2 = "";

         try {
            var2 = Ardo(IsoRoomExit.ThiggleQ + var0);
         } catch (Exception var5) {
            return false;
         }
      }

      return var2.equals(var1);
   }

   public static String Thigglewhat22(String var0) {
      String var1 = "";

      try {
         var1 = Ardo(var0);
      } catch (Exception var5) {
         var1 = "";

         try {
            var1 = Ardo(IsoRoomExit.ThiggleQ + var0);
         } catch (Exception var4) {
            return "";
         }
      }

      return var1;
   }

   public static boolean Thigglewhat() {
      String var0 = "";
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleA + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleB + Mapping0.ThiggleBB + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleC + Mapping0.ThiggleCC + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleD + Mapping0.ThiggleDA + Mapping0.ThiggleDB + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleE + Mapping0.ThiggleEA + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleF + Mapping0.ThiggleFA + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0 + Thigglewhat22(Mapping0.ThiggleAQQ2 + Mapping0.ThiggleG + Mapping0.ThiggleGA + Mapping0.ThiggleGB + Mapping0.ThiggleGC + Mapping0.ThiggleAQ + Mapping0.ThiggleAQ2);
      var0 = var0.toUpperCase();
      return true;
   }
}
