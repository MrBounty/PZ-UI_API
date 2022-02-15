package zombie.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.SandboxOptions;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.profanity.ProfanityFilter;
import zombie.util.StringUtils;

public class ServerSettings {
   protected String name;
   protected ServerOptions serverOptions;
   protected SandboxOptions sandboxOptions;
   protected ArrayList spawnRegions;
   protected ArrayList spawnPoints;
   private boolean valid = true;
   private String errorMsg = null;

   public ServerSettings(String var1) {
      this.errorMsg = null;
      this.valid = true;
      this.name = var1;
      String var2 = ProfanityFilter.getInstance().validateString(var1, true, true, true);
      if (!StringUtils.isNullOrEmpty(var2)) {
         this.errorMsg = Translator.getText("UI_BadWordCheck", var2);
         this.valid = false;
      }

   }

   public String getName() {
      return this.name;
   }

   public void resetToDefault() {
      this.serverOptions = new ServerOptions();
      this.sandboxOptions = new SandboxOptions();
      this.spawnRegions = (new SpawnRegions()).getDefaultServerRegions();
      this.spawnPoints = null;
   }

   public boolean loadFiles() {
      this.serverOptions = new ServerOptions();
      this.serverOptions.loadServerTextFile(this.name);
      this.sandboxOptions = new SandboxOptions();
      this.sandboxOptions.loadServerLuaFile(this.name);
      this.sandboxOptions.loadServerZombiesFile(this.name);
      SpawnRegions var1 = new SpawnRegions();
      this.spawnRegions = var1.loadRegionsFile(ServerSettingsManager.instance.getNameInSettingsFolder(this.name + "_spawnregions.lua"));
      if (this.spawnRegions == null) {
         this.spawnRegions = var1.getDefaultServerRegions();
      }

      this.spawnPoints = var1.loadPointsFile(ServerSettingsManager.instance.getNameInSettingsFolder(this.name + "_spawnpoints.lua"));
      return true;
   }

   public boolean saveFiles() {
      if (this.serverOptions == null) {
         return false;
      } else {
         this.serverOptions.saveServerTextFile(this.name);
         this.sandboxOptions.saveServerLuaFile(this.name);
         if (this.spawnRegions != null) {
            (new SpawnRegions()).saveRegionsFile(ServerSettingsManager.instance.getNameInSettingsFolder(this.name + "_spawnregions.lua"), this.spawnRegions);
         }

         if (this.spawnPoints != null) {
            (new SpawnRegions()).savePointsFile(ServerSettingsManager.instance.getNameInSettingsFolder(this.name + "_spawnpoints.lua"), this.spawnPoints);
         }

         this.tryDeleteFile(this.name + "_zombies.ini");
         return true;
      }
   }

   private boolean tryDeleteFile(String var1) {
      try {
         File var2 = new File(ServerSettingsManager.instance.getNameInSettingsFolder(var1));
         if (var2.exists()) {
            DebugLog.log("deleting " + var2.getAbsolutePath());
            var2.delete();
         }

         return true;
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
         return false;
      }
   }

   public boolean deleteFiles() {
      this.tryDeleteFile(this.name + ".ini");
      this.tryDeleteFile(this.name + "_SandboxVars.lua");
      this.tryDeleteFile(this.name + "_spawnregions.lua");
      this.tryDeleteFile(this.name + "_spawnpoints.lua");
      this.tryDeleteFile(this.name + "_zombies.ini");
      return true;
   }

   public boolean duplicateFiles(String var1) {
      if (!ServerSettingsManager.instance.isValidNewName(var1)) {
         return false;
      } else {
         ServerSettings var2 = new ServerSettings(this.name);
         var2.loadFiles();
         if (var2.spawnRegions != null) {
            Iterator var3 = var2.spawnRegions.iterator();

            while(var3.hasNext()) {
               SpawnRegions.Region var4 = (SpawnRegions.Region)var3.next();
               if (var4.serverfile != null && var4.serverfile.equals(this.name + "_spawnpoints.lua")) {
                  var4.serverfile = var1 + "_spawnpoints.lua";
               }
            }
         }

         var2.name = var1;
         var2.saveFiles();
         return true;
      }
   }

   public boolean rename(String var1) {
      if (!ServerSettingsManager.instance.isValidNewName(var1)) {
         return false;
      } else {
         this.loadFiles();
         this.deleteFiles();
         if (this.spawnRegions != null) {
            Iterator var2 = this.spawnRegions.iterator();

            while(var2.hasNext()) {
               SpawnRegions.Region var3 = (SpawnRegions.Region)var2.next();
               if (var3.serverfile != null && var3.serverfile.equals(this.name + "_spawnpoints.lua")) {
                  var3.serverfile = var1 + "_spawnpoints.lua";
               }
            }
         }

         this.name = var1;
         this.saveFiles();
         return true;
      }
   }

   public ServerOptions getServerOptions() {
      return this.serverOptions;
   }

   public SandboxOptions getSandboxOptions() {
      return this.sandboxOptions;
   }

   public int getNumSpawnRegions() {
      return this.spawnRegions.size();
   }

   public String getSpawnRegionName(int var1) {
      return ((SpawnRegions.Region)this.spawnRegions.get(var1)).name;
   }

   public String getSpawnRegionFile(int var1) {
      SpawnRegions.Region var2 = (SpawnRegions.Region)this.spawnRegions.get(var1);
      return var2.file != null ? var2.file : var2.serverfile;
   }

   public void clearSpawnRegions() {
      this.spawnRegions.clear();
   }

   public void addSpawnRegion(String var1, String var2) {
      if (var1 != null && var2 != null) {
         SpawnRegions.Region var3 = new SpawnRegions.Region();
         var3.name = var1;
         if (var2.startsWith("media")) {
            var3.file = var2;
         } else {
            var3.serverfile = var2;
         }

         this.spawnRegions.add(var3);
      } else {
         throw new NullPointerException();
      }
   }

   public void removeSpawnRegion(int var1) {
      this.spawnRegions.remove(var1);
   }

   public KahluaTable loadSpawnPointsFile(String var1) {
      SpawnRegions var2 = new SpawnRegions();
      return var2.loadPointsTable(ServerSettingsManager.instance.getNameInSettingsFolder(var1));
   }

   public boolean saveSpawnPointsFile(String var1, KahluaTable var2) {
      SpawnRegions var3 = new SpawnRegions();
      return var3.savePointsTable(ServerSettingsManager.instance.getNameInSettingsFolder(var1), var2);
   }

   public boolean isValid() {
      return this.valid;
   }

   public String getErrorMsg() {
      return this.errorMsg;
   }
}
