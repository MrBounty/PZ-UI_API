package zombie.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class LanguageFile {
   private static final int VERSION1 = 1;
   private static final int VERSION = 1;

   public boolean read(String var1, LanguageFileData var2) {
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

   private void fromString(String var1, LanguageFileData var2) {
      var1 = ScriptParser.stripComments(var1);
      ScriptParser.Block var3 = ScriptParser.parse(var1);
      int var4 = -1;
      ScriptParser.Value var5 = var3.getValue("VERSION");
      if (var5 != null) {
         var4 = PZMath.tryParseInt(var5.getValue(), -1);
      }

      if (var4 >= 1 && var4 <= 1) {
         ScriptParser.Value var6 = var3.getValue("text");
         if (var6 != null && !StringUtils.isNullOrWhitespace(var6.getValue())) {
            ScriptParser.Value var7 = var3.getValue("charset");
            if (var7 != null && !StringUtils.isNullOrWhitespace(var7.getValue())) {
               var2.text = var6.getValue().trim();
               var2.charset = var7.getValue().trim();
               ScriptParser.Value var8 = var3.getValue("base");
               if (var8 != null && !StringUtils.isNullOrWhitespace(var8.getValue())) {
                  var2.base = var8.getValue().trim();
               }

               ScriptParser.Value var9 = var3.getValue("azerty");
               if (var9 != null) {
                  var2.azerty = StringUtils.tryParseBoolean(var9.getValue());
               }

            } else {
               throw new RuntimeException("missing or empty value \"charset\"");
            }
         } else {
            throw new RuntimeException("missing or empty value \"text\"");
         }
      } else {
         throw new RuntimeException("invalid or missing VERSION");
      }
   }
}
