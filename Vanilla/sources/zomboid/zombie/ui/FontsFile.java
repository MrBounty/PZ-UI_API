package zombie.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class FontsFile {
   private static final int VERSION1 = 1;
   private static final int VERSION = 1;

   public boolean read(String var1, HashMap var2) {
      try {
         FileReader var3 = new FileReader(var1);

         boolean var7;
         try {
            BufferedReader var4 = new BufferedReader(var3);

            try {
               StringBuilder var5 = new StringBuilder();

               for(String var6 = var4.readLine(); var6 != null; var6 = var4.readLine()) {
                  var5.append(var6);
               }

               this.fromString(var5.toString(), var2);
               var7 = true;
            } catch (Throwable var10) {
               try {
                  var4.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var4.close();
         } catch (Throwable var11) {
            try {
               var3.close();
            } catch (Throwable var8) {
               var11.addSuppressed(var8);
            }

            throw var11;
         }

         var3.close();
         return var7;
      } catch (FileNotFoundException var12) {
         return false;
      } catch (Exception var13) {
         ExceptionLogger.logException(var13);
         return false;
      }
   }

   private void fromString(String var1, HashMap var2) {
      var1 = ScriptParser.stripComments(var1);
      ScriptParser.Block var3 = ScriptParser.parse(var1);
      int var4 = -1;
      ScriptParser.Value var5 = var3.getValue("VERSION");
      if (var5 != null) {
         var4 = PZMath.tryParseInt(var5.getValue(), -1);
      }

      if (var4 >= 1 && var4 <= 1) {
         Iterator var6 = var3.children.iterator();

         while(true) {
            while(var6.hasNext()) {
               ScriptParser.Block var7 = (ScriptParser.Block)var6.next();
               if (!var7.type.equalsIgnoreCase("font")) {
                  throw new RuntimeException("unknown block type \"" + var7.type + "\"");
               }

               if (StringUtils.isNullOrWhitespace(var7.id)) {
                  DebugLog.General.warn("missing or empty font id");
               } else {
                  ScriptParser.Value var8 = var7.getValue("fnt");
                  ScriptParser.Value var9 = var7.getValue("img");
                  if (var8 != null && !StringUtils.isNullOrWhitespace(var8.getValue())) {
                     FontsFileFont var10 = new FontsFileFont();
                     var10.id = var7.id;
                     var10.fnt = var8.getValue().trim();
                     if (var9 != null && !StringUtils.isNullOrWhitespace(var9.getValue())) {
                        var10.img = var9.getValue().trim();
                     }

                     var2.put(var10.id, var10);
                  } else {
                     DebugLog.General.warn("missing or empty value \"fnt\"");
                  }
               }
            }

            return;
         }
      } else {
         throw new RuntimeException("invalid or missing VERSION");
      }
   }
}
