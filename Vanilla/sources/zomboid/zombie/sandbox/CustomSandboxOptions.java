package zombie.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.SandboxOptions;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.gameStates.ChooseGameInfo;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class CustomSandboxOptions {
   private static final int VERSION1 = 1;
   private static final int VERSION = 1;
   public static final CustomSandboxOptions instance = new CustomSandboxOptions();
   private final ArrayList m_options = new ArrayList();

   public void init() {
      ArrayList var1 = ZomboidFileSystem.instance.getModIDs();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = (String)var1.get(var2);
         ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
         if (var4 != null) {
            String var10002 = var4.getDir();
            File var5 = new File(var10002 + File.separator + "media" + File.separator + "sandbox-options.txt");
            if (var5.exists() && !var5.isDirectory()) {
               this.readFile(var5.getAbsolutePath());
            }
         }
      }

   }

   public static void Reset() {
      instance.m_options.clear();
   }

   public void initInstance(SandboxOptions var1) {
      for(int var2 = 0; var2 < this.m_options.size(); ++var2) {
         CustomSandboxOption var3 = (CustomSandboxOption)this.m_options.get(var2);
         var1.newCustomOption(var3);
      }

   }

   private boolean readFile(String var1) {
      try {
         FileReader var2 = new FileReader(var1);

         boolean var6;
         try {
            BufferedReader var3 = new BufferedReader(var2);

            try {
               StringBuilder var4 = new StringBuilder();

               for(String var5 = var3.readLine(); var5 != null; var5 = var3.readLine()) {
                  var4.append(var5);
               }

               this.parse(var4.toString());
               var6 = true;
            } catch (Throwable var9) {
               try {
                  var3.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var3.close();
         } catch (Throwable var10) {
            try {
               var2.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var2.close();
         return var6;
      } catch (FileNotFoundException var11) {
         return false;
      } catch (Exception var12) {
         ExceptionLogger.logException(var12);
         return false;
      }
   }

   private void parse(String var1) {
      var1 = ScriptParser.stripComments(var1);
      ScriptParser.Block var2 = ScriptParser.parse(var1);
      int var3 = -1;
      ScriptParser.Value var4 = var2.getValue("VERSION");
      if (var4 != null) {
         var3 = PZMath.tryParseInt(var4.getValue(), -1);
      }

      if (var3 >= 1 && var3 <= 1) {
         Iterator var5 = var2.children.iterator();

         while(var5.hasNext()) {
            ScriptParser.Block var6 = (ScriptParser.Block)var5.next();
            if (!var6.type.equalsIgnoreCase("option")) {
               throw new RuntimeException("unknown block type \"" + var6.type + "\"");
            }

            CustomSandboxOption var7 = this.parseOption(var6);
            if (var7 == null) {
               DebugLog.General.warn("failed to parse custom sandbox option \"%s\"", var6.id);
            } else {
               this.m_options.add(var7);
            }
         }

      } else {
         throw new RuntimeException("invalid or missing VERSION");
      }
   }

   private CustomSandboxOption parseOption(ScriptParser.Block var1) {
      if (StringUtils.isNullOrWhitespace(var1.id)) {
         DebugLog.General.warn("missing or empty option id");
         return null;
      } else {
         ScriptParser.Value var2 = var1.getValue("type");
         if (var2 != null && !StringUtils.isNullOrWhitespace(var2.getValue())) {
            String var3 = var2.getValue().trim();
            byte var4 = -1;
            switch(var3.hashCode()) {
            case -1325958191:
               if (var3.equals("double")) {
                  var4 = 1;
               }
               break;
            case -891985903:
               if (var3.equals("string")) {
                  var4 = 4;
               }
               break;
            case 3118337:
               if (var3.equals("enum")) {
                  var4 = 2;
               }
               break;
            case 64711720:
               if (var3.equals("boolean")) {
                  var4 = 0;
               }
               break;
            case 1958052158:
               if (var3.equals("integer")) {
                  var4 = 3;
               }
            }

            switch(var4) {
            case 0:
               return CustomBooleanSandboxOption.parse(var1);
            case 1:
               return CustomDoubleSandboxOption.parse(var1);
            case 2:
               return CustomEnumSandboxOption.parse(var1);
            case 3:
               return CustomIntegerSandboxOption.parse(var1);
            case 4:
               return CustomStringSandboxOption.parse(var1);
            default:
               DebugLog.General.warn("unknown option type \"%s\"", var2.getValue().trim());
               return null;
            }
         } else {
            DebugLog.General.warn("missing or empty value \"type\"");
            return null;
         }
      }
   }
}
