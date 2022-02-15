package zombie.characters.skills;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.gameStates.ChooseGameInfo;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class CustomPerks {
   private static final int VERSION1 = 1;
   private static final int VERSION = 1;
   public static final CustomPerks instance = new CustomPerks();
   private final ArrayList m_perks = new ArrayList();

   public void init() {
      ArrayList var1 = ZomboidFileSystem.instance.getModIDs();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = (String)var1.get(var2);
         ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
         if (var4 != null) {
            String var10002 = var4.getDir();
            File var5 = new File(var10002 + File.separator + "media" + File.separator + "perks.txt");
            if (var5.exists() && !var5.isDirectory()) {
               this.readFile(var5.getAbsolutePath());
            }
         }
      }

      Iterator var7 = this.m_perks.iterator();

      while(true) {
         CustomPerk var8;
         PerkFactory.Perk var9;
         do {
            if (!var7.hasNext()) {
               var7 = this.m_perks.iterator();

               while(var7.hasNext()) {
                  var8 = (CustomPerk)var7.next();
                  var9 = PerkFactory.Perks.FromString(var8.m_id);
                  PerkFactory.Perk var10 = PerkFactory.Perks.FromString(var8.m_parent);
                  if (var10 == null || var10 == PerkFactory.Perks.None || var10 == PerkFactory.Perks.MAX) {
                     var10 = PerkFactory.Perks.None;
                  }

                  int[] var6 = var8.m_xp;
                  PerkFactory.AddPerk(var9, var8.m_translation, var10, var6[0], var6[1], var6[2], var6[3], var6[4], var6[5], var6[6], var6[7], var6[8], var6[9], var8.m_bPassive);
               }

               return;
            }

            var8 = (CustomPerk)var7.next();
            var9 = PerkFactory.Perks.FromString(var8.m_id);
         } while(var9 != null && var9 != PerkFactory.Perks.None && var9 != PerkFactory.Perks.MAX);

         var9 = new PerkFactory.Perk(var8.m_id);
         var9.setCustom();
      }
   }

   public void initLua() {
      KahluaTable var1 = (KahluaTable)LuaManager.env.rawget("Perks");
      Iterator var2 = this.m_perks.iterator();

      while(var2.hasNext()) {
         CustomPerk var3 = (CustomPerk)var2.next();
         PerkFactory.Perk var4 = PerkFactory.Perks.FromString(var3.m_id);
         var1.rawset(var4.getId(), var4);
      }

   }

   public static void Reset() {
      instance.m_perks.clear();
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
            if (!var6.type.equalsIgnoreCase("perk")) {
               throw new RuntimeException("unknown block type \"" + var6.type + "\"");
            }

            CustomPerk var7 = this.parsePerk(var6);
            if (var7 == null) {
               DebugLog.General.warn("failed to parse custom perk \"%s\"", var6.id);
            } else {
               this.m_perks.add(var7);
            }
         }

      } else {
         throw new RuntimeException("invalid or missing VERSION");
      }
   }

   private CustomPerk parsePerk(ScriptParser.Block var1) {
      if (StringUtils.isNullOrWhitespace(var1.id)) {
         DebugLog.General.warn("missing or empty perk id");
         return null;
      } else {
         CustomPerk var2 = new CustomPerk(var1.id);
         ScriptParser.Value var3 = var1.getValue("parent");
         if (var3 != null && !StringUtils.isNullOrWhitespace(var3.getValue())) {
            var2.m_parent = var3.getValue().trim();
         }

         ScriptParser.Value var4 = var1.getValue("translation");
         if (var4 != null) {
            var2.m_translation = StringUtils.discardNullOrWhitespace(var4.getValue().trim());
         }

         if (StringUtils.isNullOrWhitespace(var2.m_translation)) {
            var2.m_translation = var2.m_id;
         }

         ScriptParser.Value var5 = var1.getValue("passive");
         if (var5 != null) {
            var2.m_bPassive = StringUtils.tryParseBoolean(var5.getValue().trim());
         }

         for(int var6 = 1; var6 <= 10; ++var6) {
            ScriptParser.Value var7 = var1.getValue("xp" + var6);
            if (var7 != null) {
               int var8 = PZMath.tryParseInt(var7.getValue().trim(), -1);
               if (var8 > 0) {
                  var2.m_xp[var6 - 1] = var8;
               }
            }
         }

         return var2;
      }
   }
}
