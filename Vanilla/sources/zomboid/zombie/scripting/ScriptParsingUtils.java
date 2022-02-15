package zombie.scripting;

import java.util.Stack;

public final class ScriptParsingUtils {
   public static String[] SplitExceptInbetween(String var0, String var1, String var2) {
      Stack var3 = new Stack();
      boolean var4 = false;

      int var6;
      while(var0.contains(var1)) {
         int var8;
         if (!var4) {
            int var5 = var0.indexOf(var1);
            var6 = var0.indexOf(var2);
            String[] var11;
            if (var6 == -1) {
               var11 = var0.split(var1);

               for(var8 = 0; var8 < var11.length; ++var8) {
                  var3.add(var11[var8].trim());
               }

               var11 = new String[var3.size()];

               for(var8 = 0; var8 < var3.size(); ++var8) {
                  var11[var8] = (String)var3.get(var8);
               }

               return var11;
            }

            if (var5 == -1) {
               var11 = new String[var3.size()];
               if (!var0.trim().isEmpty()) {
                  var3.add(var0.trim());
               }

               for(var8 = 0; var8 < var3.size(); ++var8) {
                  var11[var8] = (String)var3.get(var8);
               }

               return var11;
            }

            if (var5 < var6) {
               var3.add(var0.substring(0, var5));
               var0 = var0.substring(var5 + 1);
            } else {
               var4 = true;
            }
         } else {
            var0.indexOf(var2);
            var0.indexOf(var2);
            int var7 = var0.indexOf(var2, var0.indexOf(var2) + 1);
            var8 = var0.indexOf(var1, var7 + 1);
            if (var8 == -1) {
               break;
            }

            String var9 = var0.substring(0, var8).trim();
            if (!var9.isEmpty()) {
               var3.add(var9);
            }

            var0 = var0.substring(var8 + 1);
            var4 = false;
         }
      }

      if (!var0.trim().isEmpty()) {
         var3.add(var0.trim());
      }

      String[] var10 = new String[var3.size()];

      for(var6 = 0; var6 < var3.size(); ++var6) {
         var10[var6] = (String)var3.get(var6);
      }

      return var10;
   }

   public static String[] SplitExceptInbetween(String var0, String var1, String var2, String var3) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      int var15 = 0;
      int var12 = 0;
      int var13 = 0;
      int var14 = 0;
      Stack var9 = new Stack();
      if (var0.indexOf(var2, var12) == -1) {
         return var0.split(var1);
      } else {
         do {
            var12 = var0.indexOf(var2, var12 + 1);
            var13 = var0.indexOf(var3, var13 + 1);
            var14 = var0.indexOf(var1, var14 + 1);
            if (var14 == -1) {
               var9.add(var0.trim());
               var0 = "";
            } else if ((var14 < var12 || var12 == -1 && var14 != -1) && var15 == 0) {
               var9.add(var0.substring(0, var14));
               var0 = var0.substring(var14 + 1);
               var12 = 0;
               var13 = 0;
               var14 = 0;
            } else if ((var13 >= var12 || var13 == -1) && var12 != -1) {
               if (var12 != -1 && var13 == -1) {
                  var13 = var12;
                  ++var15;
               } else if (var12 != -1 && var13 != -1 && var12 < var13 && (var12 > var14 || var13 < var14)) {
                  var9.add(var0.substring(0, var14));
                  var0 = var0.substring(var14 + 1);
                  var12 = 0;
                  var13 = 0;
                  var14 = 0;
               }
            } else {
               var12 = var13;
               --var15;
               if (var15 == 0) {
                  var9.add(var0.substring(0, var13 + 1));
                  var0 = var0.substring(var13 + 1);
                  var12 = 0;
                  var13 = 0;
                  var14 = 0;
               }
            }
         } while(var0.trim().length() > 0);

         if (!var0.trim().isEmpty()) {
            var9.add(var0.trim());
         }

         String[] var10 = new String[var9.size()];

         for(int var11 = 0; var11 < var9.size(); ++var11) {
            var10[var11] = ((String)var9.get(var11)).trim();
         }

         return var10;
      }
   }
}
