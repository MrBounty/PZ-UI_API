package zombie;

import gnu.trove.map.hash.THashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream.Filter;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.debug.DebugLog;
import zombie.debug.LogSeverity;
import zombie.gameStates.ChooseGameInfo;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.modding.ActiveMods;
import zombie.modding.ActiveModsFile;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.StringUtils;

public final class ZomboidFileSystem {
   public static final ZomboidFileSystem instance = new ZomboidFileSystem();
   private final ArrayList loadList = new ArrayList();
   private final Map modIdToDir = new HashMap();
   private final Map modDirToMod = new HashMap();
   private ArrayList modFolders;
   private ArrayList modFoldersOrder;
   public final HashMap ActiveFileMap = new HashMap();
   public File base;
   public URI baseURI;
   private File workdir;
   private URI workdirURI;
   private File localWorkdir;
   private File anims;
   private URI animsURI;
   private File animsX;
   private URI animsXURI;
   private File animSets;
   private URI animSetsURI;
   private File actiongroups;
   private URI actiongroupsURI;
   private File cacheDir;
   private final THashMap RelativeMap = new THashMap();
   public boolean IgnoreActiveFileMap = false;
   private final ArrayList mods = new ArrayList();
   private final HashSet LoadedPacks = new HashSet();
   private FileGuidTable m_fileGuidTable = null;
   private boolean m_fileGuidTableWatcherActive = false;
   private final PredicatedFileWatcher m_modFileWatcher = new PredicatedFileWatcher(this::isModFile, this::onModFileChanged);
   private final HashSet m_watchedModFolders = new HashSet();
   private long m_modsChangedTime = 0L;

   private ZomboidFileSystem() {
   }

   public void init() throws IOException {
      this.base = (new File("./")).getAbsoluteFile().getCanonicalFile();
      this.baseURI = this.base.toURI();
      this.workdir = (new File(this.base, "media")).getAbsoluteFile().getCanonicalFile();
      this.workdirURI = this.workdir.toURI();
      this.localWorkdir = this.base.toPath().relativize(this.workdir.toPath()).toFile();
      this.anims = new File(this.workdir, "anims");
      this.animsURI = this.anims.toURI();
      this.animsX = new File(this.workdir, "anims_X");
      this.animsXURI = this.animsX.toURI();
      this.animSets = new File(this.workdir, "AnimSets");
      this.animSetsURI = this.animSets.toURI();
      this.actiongroups = new File(this.workdir, "actiongroups");
      this.actiongroupsURI = this.actiongroups.toURI();
      this.searchFolders(this.workdir);

      for(int var1 = 0; var1 < this.loadList.size(); ++var1) {
         String var2 = this.getRelativeFile((String)this.loadList.get(var1));
         File var3 = (new File((String)this.loadList.get(var1))).getAbsoluteFile();
         String var4 = var3.getAbsolutePath();
         if (var3.isDirectory()) {
            var4 = var4 + File.separator;
         }

         this.ActiveFileMap.put(var2.toLowerCase(Locale.ENGLISH), var4);
      }

      this.loadList.clear();
   }

   public String getGameModeCacheDir() {
      if (Core.GameMode == null) {
         Core.GameMode = "Sandbox";
      }

      String var1 = this.getSaveDir();
      return var1 + File.separator + Core.GameMode + File.separator;
   }

   public String getFileNameInCurrentSave(String var1) {
      String var10000 = this.getGameModeCacheDir();
      return var10000 + File.separator + Core.GameSaveWorld + File.separator + var1;
   }

   public String getFileNameInCurrentSave(String var1, String var2) {
      return this.getFileNameInCurrentSave(var1 + File.separator + var2);
   }

   public String getFileNameInCurrentSave(String var1, String var2, String var3) {
      return this.getFileNameInCurrentSave(var1 + File.separator + var2 + File.separator + var3);
   }

   public File getFileInCurrentSave(String var1) {
      return new File(this.getFileNameInCurrentSave(var1));
   }

   public File getFileInCurrentSave(String var1, String var2) {
      return new File(this.getFileNameInCurrentSave(var1, var2));
   }

   public File getFileInCurrentSave(String var1, String var2, String var3) {
      return new File(this.getFileNameInCurrentSave(var1, var2, var3));
   }

   public String getSaveDir() {
      String var1 = this.getCacheDirSub("Saves");
      ensureFolderExists(var1);
      return var1;
   }

   public String getSaveDirSub(String var1) {
      String var10000 = this.getSaveDir();
      return var10000 + File.separator + var1;
   }

   public String getScreenshotDir() {
      String var1 = this.getCacheDirSub("Screenshots");
      ensureFolderExists(var1);
      return var1;
   }

   public String getScreenshotDirSub(String var1) {
      String var10000 = this.getScreenshotDir();
      return var10000 + File.separator + var1;
   }

   public void setCacheDir(String var1) {
      var1 = var1.replace("/", File.separator);
      this.cacheDir = (new File(var1)).getAbsoluteFile();
      ensureFolderExists(this.cacheDir);
   }

   public String getCacheDir() {
      if (this.cacheDir == null) {
         String var1 = System.getProperty("deployment.user.cachedir");
         if (var1 == null || System.getProperty("os.name").startsWith("Win")) {
            var1 = System.getProperty("user.home");
         }

         String var2 = var1 + File.separator + "Zomboid";
         this.setCacheDir(var2);
      }

      return this.cacheDir.getPath();
   }

   public String getCacheDirSub(String var1) {
      String var10000 = this.getCacheDir();
      return var10000 + File.separator + var1;
   }

   public String getMessagingDir() {
      String var1 = this.getCacheDirSub("messaging");
      ensureFolderExists(var1);
      return var1;
   }

   public String getMessagingDirSub(String var1) {
      String var10000 = this.getMessagingDir();
      return var10000 + File.separator + var1;
   }

   public File getMediaRootFile() {
      assert this.workdir != null;

      return this.workdir;
   }

   public String getMediaRootPath() {
      return this.workdir.getPath();
   }

   public File getMediaFile(String var1) {
      assert this.workdir != null;

      return new File(this.workdir, var1);
   }

   public String getMediaPath(String var1) {
      return this.getMediaFile(var1).getPath();
   }

   public String getAbsoluteWorkDir() {
      return this.workdir.getPath();
   }

   public String getLocalWorkDir() {
      return this.localWorkdir.getPath();
   }

   public String getLocalWorkDirSub(String var1) {
      String var10000 = this.getLocalWorkDir();
      return var10000 + File.separator + var1;
   }

   public String getAnimSetsPath() {
      return this.animSets.getPath();
   }

   public String getActionGroupsPath() {
      return this.actiongroups.getPath();
   }

   public static boolean ensureFolderExists(String var0) {
      return ensureFolderExists((new File(var0)).getAbsoluteFile());
   }

   public static boolean ensureFolderExists(File var0) {
      return var0.exists() || var0.mkdirs();
   }

   public void searchFolders(File var1) {
      if (!GameServer.bServer) {
         Thread.yield();
         Core.getInstance().DoFrameReady();
      }

      if (var1.isDirectory()) {
         String var2 = var1.getAbsolutePath().replace("\\", "/").replace("./", "");
         if (var2.contains("media/maps/")) {
            this.loadList.add(var2);
         }

         String[] var3 = var1.list();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var10003 = var1.getAbsolutePath();
            this.searchFolders(new File(var10003 + File.separator + var3[var4]));
         }
      } else {
         this.loadList.add(var1.getAbsolutePath().replace("\\", "/").replace("./", ""));
      }

   }

   public Object[] getAllPathsContaining(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.ActiveFileMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (((String)var4.getKey()).contains(var1)) {
            var2.add((String)var4.getValue());
         }
      }

      return var2.toArray();
   }

   public Object[] getAllPathsContaining(String var1, String var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.ActiveFileMap.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         if (((String)var5.getKey()).contains(var1) && ((String)var5.getKey()).contains(var2)) {
            var3.add((String)var5.getValue());
         }
      }

      return var3.toArray();
   }

   public synchronized String getString(String var1) {
      if (this.IgnoreActiveFileMap) {
         return var1;
      } else {
         String var2 = var1.toLowerCase(Locale.ENGLISH);
         String var3 = (String)this.RelativeMap.get(var2);
         String var4;
         if (var3 != null) {
            var2 = var3;
         } else {
            var4 = var2;
            var2 = this.getRelativeFile(var1);
            var2 = var2.toLowerCase(Locale.ENGLISH);
            this.RelativeMap.put(var4, var2);
         }

         var4 = (String)this.ActiveFileMap.get(var2);
         return var4 != null ? var4 : var1;
      }
   }

   public String getAbsolutePath(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      return (String)this.ActiveFileMap.get(var2);
   }

   public void Reset() {
      this.loadList.clear();
      this.ActiveFileMap.clear();
      this.modIdToDir.clear();
      this.modDirToMod.clear();
      this.mods.clear();
      this.modFolders = null;
      ActiveMods.Reset();
      if (this.m_fileGuidTable != null) {
         this.m_fileGuidTable.clear();
         this.m_fileGuidTable = null;
      }

   }

   public void resetModFolders() {
      this.modFolders = null;
   }

   public void getInstalledItemModsFolders(ArrayList var1) {
      if (SteamUtils.isSteamModeEnabled()) {
         String[] var2 = SteamWorkshop.instance.GetInstalledItemFolders();
         if (var2 != null) {
            String[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               File var7 = new File(var6 + File.separator + "mods");
               if (var7.exists()) {
                  var1.add(var7.getAbsolutePath());
               }
            }
         }
      }

   }

   public void getStagedItemModsFolders(ArrayList var1) {
      if (SteamUtils.isSteamModeEnabled()) {
         ArrayList var2 = SteamWorkshop.instance.getStageFolders();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            String var10002 = (String)var2.get(var3);
            File var4 = new File(var10002 + File.separator + "Contents" + File.separator + "mods");
            if (var4.exists()) {
               var1.add(var4.getAbsolutePath());
            }
         }
      }

   }

   private void getAllModFoldersAux(String var1, List var2) {
      Filter var3 = new Filter() {
         public boolean accept(Path var1) throws IOException {
            return Files.isDirectory(var1, new LinkOption[0]) && Files.exists(var1.resolve("mod.info"), new LinkOption[0]);
         }
      };
      Path var4 = FileSystems.getDefault().getPath(var1);
      if (Files.exists(var4, new LinkOption[0])) {
         try {
            DirectoryStream var5 = Files.newDirectoryStream(var4, var3);

            try {
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  Path var7 = (Path)var6.next();
                  if (var7.getFileName().toString().toLowerCase().equals("examplemod")) {
                     DebugLog.Mod.println("refusing to list " + var7.getFileName());
                  } else {
                     String var8 = var7.toAbsolutePath().toString();
                     if (!this.m_watchedModFolders.contains(var8)) {
                        this.m_watchedModFolders.add(var8);
                        DebugFileWatcher.instance.addDirectory(var8);
                        Path var9 = var7.resolve("media");
                        if (Files.exists(var9, new LinkOption[0])) {
                           DebugFileWatcher.instance.addDirectoryRecurse(var9.toAbsolutePath().toString());
                        }
                     }

                     var2.add(var8);
                  }
               }
            } catch (Throwable var11) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }

      }
   }

   public void setModFoldersOrder(String var1) {
      this.modFoldersOrder = new ArrayList(Arrays.asList(var1.split(",")));
   }

   public void getAllModFolders(List var1) {
      if (this.modFolders == null) {
         this.modFolders = new ArrayList();
         if (this.modFoldersOrder == null) {
            this.setModFoldersOrder("workshop,steam,mods");
         }

         ArrayList var2 = new ArrayList();

         int var3;
         String var4;
         for(var3 = 0; var3 < this.modFoldersOrder.size(); ++var3) {
            var4 = (String)this.modFoldersOrder.get(var3);
            if ("workshop".equals(var4)) {
               this.getStagedItemModsFolders(var2);
            }

            if ("steam".equals(var4)) {
               this.getInstalledItemModsFolders(var2);
            }

            if ("mods".equals(var4)) {
               String var10001 = Core.getMyDocumentFolder();
               var2.add(var10001 + File.separator + "mods");
            }
         }

         for(var3 = 0; var3 < var2.size(); ++var3) {
            var4 = (String)var2.get(var3);
            if (!this.m_watchedModFolders.contains(var4)) {
               this.m_watchedModFolders.add(var4);
               DebugFileWatcher.instance.addDirectory(var4);
            }

            this.getAllModFoldersAux(var4, this.modFolders);
         }

         DebugFileWatcher.instance.add(this.m_modFileWatcher);
      }

      var1.clear();
      var1.addAll(this.modFolders);
   }

   public ArrayList getWorkshopItemMods(long var1) {
      ArrayList var3 = new ArrayList();
      if (!SteamUtils.isSteamModeEnabled()) {
         return var3;
      } else {
         String var4 = SteamWorkshop.instance.GetItemInstallFolder(var1);
         if (var4 == null) {
            return var3;
         } else {
            File var5 = new File(var4 + File.separator + "mods");
            if (var5.exists() && var5.isDirectory()) {
               File[] var6 = var5.listFiles();
               File[] var7 = var6;
               int var8 = var6.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  File var10 = var7[var9];
                  if (var10.isDirectory()) {
                     ChooseGameInfo.Mod var11 = ChooseGameInfo.readModInfo(var10.getAbsolutePath());
                     if (var11 != null) {
                        var3.add(var11);
                     }
                  }
               }

               return var3;
            } else {
               return var3;
            }
         }
      }
   }

   public ChooseGameInfo.Mod searchForModInfo(File var1, String var2, ArrayList var3) {
      if (var1.isDirectory()) {
         String[] var4 = var1.list();
         if (var4 == null) {
            return null;
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var10002 = var1.getAbsolutePath();
            File var6 = new File(var10002 + File.separator + var4[var5]);
            ChooseGameInfo.Mod var7 = this.searchForModInfo(var6, var2, var3);
            if (var7 != null) {
               return var7;
            }
         }
      } else if (var1.getAbsolutePath().endsWith("mod.info")) {
         ChooseGameInfo.Mod var8 = ChooseGameInfo.readModInfo(var1.getAbsoluteFile().getParent());
         if (var8 == null) {
            return null;
         }

         if (!StringUtils.isNullOrWhitespace(var8.getId())) {
            this.modIdToDir.put(var8.getId(), var8.getDir());
            var3.add(var8);
         }

         if (var8.getId().equals(var2)) {
            return var8;
         }
      }

      return null;
   }

   public void loadMod(String var1) {
      if (this.getModDir(var1) != null) {
         DebugLog.Mod.println("loading " + var1);
         File var2 = new File(this.getModDir(var1));
         URI var3 = var2.toURI();
         this.loadList.clear();
         this.searchFolders(var2);

         for(int var4 = 0; var4 < this.loadList.size(); ++var4) {
            String var5 = this.getRelativeFile(var3, (String)this.loadList.get(var4));
            var5 = var5.toLowerCase(Locale.ENGLISH);
            if (this.ActiveFileMap.containsKey(var5) && !var5.endsWith("mod.info") && !var5.endsWith("poster.png")) {
               DebugLog.Mod.println("mod \"" + var1 + "\" overrides " + var5);
            }

            this.ActiveFileMap.put(var5, (new File((String)this.loadList.get(var4))).getAbsolutePath());
         }

         this.loadList.clear();
      }

   }

   private ArrayList readLoadedDotTxt() {
      String var10000 = Core.getMyDocumentFolder();
      String var1 = var10000 + File.separator + "mods" + File.separator + "loaded.txt";
      File var2 = new File(var1);
      if (!var2.exists()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();

         try {
            FileReader var4 = new FileReader(var1);

            try {
               BufferedReader var5 = new BufferedReader(var4);

               try {
                  for(String var6 = var5.readLine(); var6 != null; var6 = var5.readLine()) {
                     var6 = var6.trim();
                     if (!var6.isEmpty()) {
                        var3.add(var6);
                     }
                  }
               } catch (Throwable var11) {
                  try {
                     var5.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }

                  throw var11;
               }

               var5.close();
            } catch (Throwable var12) {
               try {
                  var4.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }

               throw var12;
            }

            var4.close();
         } catch (Exception var13) {
            ExceptionLogger.logException(var13);
            var3 = null;
         }

         try {
            var2.delete();
         } catch (Exception var8) {
            ExceptionLogger.logException(var8);
         }

         return var3;
      }
   }

   private ActiveMods readDefaultModsTxt() {
      ActiveMods var1 = ActiveMods.getById("default");
      ArrayList var2 = this.readLoadedDotTxt();
      if (var2 != null) {
         var1.getMods().addAll(var2);
         this.saveModsFile();
      }

      var1.clear();
      String var10000 = Core.getMyDocumentFolder();
      String var3 = var10000 + File.separator + "mods" + File.separator + "default.txt";

      try {
         ActiveModsFile var4 = new ActiveModsFile();
         if (var4.read(var3, var1)) {
         }
      } catch (Exception var5) {
         ExceptionLogger.logException(var5);
      }

      return var1;
   }

   public void loadMods(String var1) {
      if (Core.OptionModsEnabled) {
         if (GameClient.bClient) {
            ArrayList var5 = new ArrayList();
            this.loadTranslationMods(var5);
            var5.addAll(GameClient.instance.ServerMods);
            this.loadMods(var5);
         } else {
            ActiveMods var2 = ActiveMods.getById(var1);
            if (!"default".equalsIgnoreCase(var1)) {
               ActiveMods.setLoadedMods(var2);
               this.loadMods(var2.getMods());
            } else {
               try {
                  var2 = this.readDefaultModsTxt();
                  var2.checkMissingMods();
                  var2.checkMissingMaps();
                  ActiveMods.setLoadedMods(var2);
                  this.loadMods(var2.getMods());
               } catch (Exception var4) {
                  ExceptionLogger.logException(var4);
               }

            }
         }
      }
   }

   private boolean isTranslationMod(String var1) {
      ChooseGameInfo.Mod var2 = ChooseGameInfo.getAvailableModDetails(var1);
      if (var2 == null) {
         return false;
      } else {
         boolean var3 = false;
         File var4 = new File(var2.getDir());
         URI var5 = var4.toURI();
         this.loadList.clear();
         this.searchFolders(var4);

         for(int var6 = 0; var6 < this.loadList.size(); ++var6) {
            String var7 = this.getRelativeFile(var5, (String)this.loadList.get(var6));
            if (var7.endsWith(".lua")) {
               return false;
            }

            if (var7.startsWith("media/maps/")) {
               return false;
            }

            if (var7.startsWith("media/scripts/")) {
               return false;
            }

            if (var7.startsWith("media/lua/")) {
               if (!var7.startsWith("media/lua/shared/Translate/")) {
                  return false;
               }

               var3 = true;
            }
         }

         this.loadList.clear();
         return var3;
      }
   }

   private void loadTranslationMods(ArrayList var1) {
      if (GameClient.bClient) {
         ActiveMods var2 = this.readDefaultModsTxt();
         ArrayList var3 = new ArrayList();
         if (this.loadModsAux(var2.getMods(), var3) == null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               if (this.isTranslationMod(var5)) {
                  DebugLog.Mod.println("loading translation mod \"" + var5 + "\"");
                  if (!var1.contains(var5)) {
                     var1.add(var5);
                  }
               }
            }
         }

      }
   }

   private String loadModAndRequired(String var1, ArrayList var2) {
      if (var1.isEmpty()) {
         return null;
      } else if (var1.toLowerCase().equals("examplemod")) {
         DebugLog.Mod.warn("refusing to load " + var1);
         return null;
      } else if (var2.contains(var1)) {
         return null;
      } else {
         ChooseGameInfo.Mod var3 = ChooseGameInfo.getAvailableModDetails(var1);
         if (var3 == null) {
            if (GameServer.bServer) {
               GameServer.ServerMods.remove(var1);
            }

            DebugLog.Mod.warn("required mod \"" + var1 + "\" not found");
            return var1;
         } else {
            if (var3.getRequire() != null) {
               String var4 = this.loadModsAux(var3.getRequire(), var2);
               if (var4 != null) {
                  return var4;
               }
            }

            var2.add(var1);
            return null;
         }
      }
   }

   public String loadModsAux(ArrayList var1, ArrayList var2) {
      Iterator var3 = var1.iterator();

      String var5;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         String var4 = (String)var3.next();
         var5 = this.loadModAndRequired(var4, var2);
      } while(var5 == null);

      return var5;
   }

   public void loadMods(ArrayList var1) {
      this.mods.clear();
      Iterator var2 = var1.iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         this.loadModAndRequired(var3, this.mods);
      }

      var2 = this.mods.iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         this.loadMod(var3);
      }

   }

   public ArrayList getModIDs() {
      return this.mods;
   }

   public String getModDir(String var1) {
      return (String)this.modIdToDir.get(var1);
   }

   public ChooseGameInfo.Mod getModInfoForDir(String var1) {
      ChooseGameInfo.Mod var2 = (ChooseGameInfo.Mod)this.modDirToMod.get(var1);
      if (var2 == null) {
         var2 = new ChooseGameInfo.Mod(var1);
         this.modDirToMod.put(var1, var2);
      }

      return var2;
   }

   public String getRelativeFile(File var1) {
      return this.getRelativeFile(this.baseURI, var1.getAbsolutePath());
   }

   public String getRelativeFile(String var1) {
      return this.getRelativeFile(this.baseURI, var1);
   }

   public String getRelativeFile(URI var1, File var2) {
      return this.getRelativeFile(var1, var2.getAbsolutePath());
   }

   public String getRelativeFile(URI var1, String var2) {
      URI var3 = (new File(var2)).getAbsoluteFile().toURI();
      URI var4 = var1.relativize(var3);
      if (var4.equals(var3)) {
         return var2;
      } else {
         String var5 = var4.getPath();
         if (var2.endsWith("/") && !var5.endsWith("/")) {
            var5 = var5 + "/";
         }

         return var5;
      }
   }

   public String getAnimName(URI var1, File var2) {
      String var3 = this.getRelativeFile(var1, var2);
      String var4 = var3.toLowerCase(Locale.ENGLISH);
      int var5 = var4.lastIndexOf(46);
      if (var5 > -1) {
         var4 = var4.substring(0, var5);
      }

      if (var4.startsWith("anims/")) {
         var4 = var4.substring("anims/".length());
      } else if (var4.startsWith("anims_x/")) {
         var4 = var4.substring("anims_x/".length());
      }

      return var4;
   }

   public String resolveRelativePath(String var1, String var2) {
      Path var3 = Paths.get(var1);
      Path var4 = var3.getParent();
      Path var5 = var4.resolve(var2);
      String var6 = var5.toString();
      var6 = this.getRelativeFile(var6);
      return var6;
   }

   public void saveModsFile() {
      try {
         String var10000 = Core.getMyDocumentFolder();
         ensureFolderExists(var10000 + File.separator + "mods");
         var10000 = Core.getMyDocumentFolder();
         String var1 = var10000 + File.separator + "mods" + File.separator + "default.txt";
         ActiveModsFile var2 = new ActiveModsFile();
         var2.write(var1, ActiveMods.getById("default"));
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }

   }

   public void loadModPackFiles() {
      Iterator var1 = this.mods.iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();

         try {
            ChooseGameInfo.Mod var3 = ChooseGameInfo.getAvailableModDetails(var2);
            if (var3 != null) {
               Iterator var4 = var3.getPacks().iterator();

               while(var4.hasNext()) {
                  ChooseGameInfo.PackFile var5 = (ChooseGameInfo.PackFile)var4.next();
                  String var6 = this.getRelativeFile("media/texturepacks/" + var5.name + ".pack");
                  var6 = var6.toLowerCase(Locale.ENGLISH);
                  if (!this.ActiveFileMap.containsKey(var6)) {
                     DebugLog.Mod.warn("pack file \"" + var5.name + "\" needed by " + var2 + " not found");
                  } else {
                     String var7 = instance.getString("media/texturepacks/" + var5.name + ".pack");
                     if (!this.LoadedPacks.contains(var7)) {
                        GameWindow.LoadTexturePack(var5.name, var5.flags, var2);
                        this.LoadedPacks.add(var7);
                     }
                  }
               }
            }
         } catch (Exception var8) {
            ExceptionLogger.logException(var8);
         }
      }

      GameWindow.setTexturePackLookup();
   }

   public void loadModTileDefs() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.mods.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();

         try {
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
            if (var4 != null) {
               Iterator var5 = var4.getTileDefs().iterator();

               while(var5.hasNext()) {
                  ChooseGameInfo.TileDef var6 = (ChooseGameInfo.TileDef)var5.next();
                  if (var1.contains(var6.fileNumber)) {
                     DebugLog.Mod.error("tiledef fileNumber " + var6.fileNumber + " used by more than one mod");
                  } else {
                     String var7 = var6.name;
                     String var8 = this.getRelativeFile("media/" + var7 + ".tiles");
                     var8 = var8.toLowerCase(Locale.ENGLISH);
                     if (!this.ActiveFileMap.containsKey(var8)) {
                        DebugLog.Mod.error("tiledef file \"" + var6.name + "\" needed by " + var3 + " not found");
                     } else {
                        var7 = (String)this.ActiveFileMap.get(var8);
                        IsoWorld.instance.LoadTileDefinitions(IsoSpriteManager.instance, var7, var6.fileNumber);
                        var1.add(var6.fileNumber);
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

   }

   public void loadModTileDefPropertyStrings() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.mods.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();

         try {
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
            if (var4 != null) {
               Iterator var5 = var4.getTileDefs().iterator();

               while(var5.hasNext()) {
                  ChooseGameInfo.TileDef var6 = (ChooseGameInfo.TileDef)var5.next();
                  if (var1.contains(var6.fileNumber)) {
                     DebugLog.Mod.error("tiledef fileNumber " + var6.fileNumber + " used by more than one mod");
                  } else {
                     String var7 = var6.name;
                     String var8 = this.getRelativeFile("media/" + var7 + ".tiles");
                     var8 = var8.toLowerCase(Locale.ENGLISH);
                     if (!this.ActiveFileMap.containsKey(var8)) {
                        DebugLog.Mod.error("tiledef file \"" + var6.name + "\" needed by " + var3 + " not found");
                     } else {
                        var7 = (String)this.ActiveFileMap.get(var8);
                        IsoWorld.instance.LoadTileDefinitionsPropertyStrings(IsoSpriteManager.instance, var7, var6.fileNumber);
                        var1.add(var6.fileNumber);
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

   }

   public void loadFileGuidTable() {
      File var1 = instance.getMediaFile("fileGuidTable.xml");

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            JAXBContext var3 = JAXBContext.newInstance(new Class[]{FileGuidTable.class});
            Unmarshaller var4 = var3.createUnmarshaller();
            this.m_fileGuidTable = (FileGuidTable)var4.unmarshal(var2);
            this.m_fileGuidTable.setModID("game");
         } catch (Throwable var15) {
            try {
               var2.close();
            } catch (Throwable var10) {
               var15.addSuppressed(var10);
            }

            throw var15;
         }

         var2.close();
      } catch (IOException | JAXBException var16) {
         System.err.println("Failed to load file Guid table.");
         ExceptionLogger.logException(var16);
         return;
      }

      try {
         JAXBContext var18 = JAXBContext.newInstance(new Class[]{FileGuidTable.class});
         Unmarshaller var19 = var18.createUnmarshaller();
         Iterator var20 = this.getModIDs().iterator();

         while(var20.hasNext()) {
            String var5 = (String)var20.next();
            ChooseGameInfo.Mod var6 = ChooseGameInfo.getAvailableModDetails(var5);
            if (var6 != null) {
               try {
                  String var10002 = this.getModDir(var5);
                  FileInputStream var7 = new FileInputStream(var10002 + "/media/fileGuidTable.xml");

                  try {
                     FileGuidTable var8 = (FileGuidTable)var19.unmarshal(var7);
                     var8.setModID(var5);
                     this.m_fileGuidTable.mergeFrom(var8);
                  } catch (Throwable var12) {
                     try {
                        var7.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }

                     throw var12;
                  }

                  var7.close();
               } catch (FileNotFoundException var13) {
               } catch (Exception var14) {
                  ExceptionLogger.logException(var14);
               }
            }
         }
      } catch (Exception var17) {
         ExceptionLogger.logException(var17);
      }

      this.m_fileGuidTable.loaded();
      if (!this.m_fileGuidTableWatcherActive) {
         DebugFileWatcher.instance.add(new PredicatedFileWatcher("media/fileGuidTable.xml", (var1x) -> {
            this.loadFileGuidTable();
         }));
         this.m_fileGuidTableWatcherActive = true;
      }

   }

   public FileGuidTable getFileGuidTable() {
      if (this.m_fileGuidTable == null) {
         this.loadFileGuidTable();
      }

      return this.m_fileGuidTable;
   }

   public String getFilePathFromGuid(String var1) {
      FileGuidTable var2 = this.getFileGuidTable();
      return var2 != null ? var2.getFilePathFromGuid(var1) : null;
   }

   public String getGuidFromFilePath(String var1) {
      FileGuidTable var2 = this.getFileGuidTable();
      return var2 != null ? var2.getGuidFromFilePath(var1) : null;
   }

   public String resolveFileOrGUID(String var1) {
      String var2 = var1;
      String var3 = this.getFilePathFromGuid(var1);
      if (var3 != null) {
         var2 = var3;
      }

      String var4 = var2.toLowerCase(Locale.ENGLISH);
      return this.ActiveFileMap.containsKey(var4) ? (String)this.ActiveFileMap.get(var4) : var2;
   }

   public boolean isValidFilePathGuid(String var1) {
      return this.getFilePathFromGuid(var1) != null;
   }

   public static File[] listAllDirectories(String var0, FileFilter var1, boolean var2) {
      File var3 = (new File(var0)).getAbsoluteFile();
      return listAllDirectories(var3, var1, var2);
   }

   public static File[] listAllDirectories(File var0, FileFilter var1, boolean var2) {
      if (!var0.isDirectory()) {
         return new File[0];
      } else {
         ArrayList var3 = new ArrayList();
         listAllDirectoriesInternal(var0, var1, var2, var3);
         return (File[])var3.toArray(new File[0]);
      }
   }

   private static void listAllDirectoriesInternal(File var0, FileFilter var1, boolean var2, ArrayList var3) {
      File[] var4 = var0.listFiles();
      if (var4 != null) {
         File[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            if (!var8.isFile() && var8.isDirectory()) {
               if (var1.accept(var8)) {
                  var3.add(var8);
               }

               if (var2) {
                  listAllFilesInternal(var8, var1, true, var3);
               }
            }
         }

      }
   }

   public static File[] listAllFiles(String var0, FileFilter var1, boolean var2) {
      File var3 = (new File(var0)).getAbsoluteFile();
      return listAllFiles(var3, var1, var2);
   }

   public static File[] listAllFiles(File var0, FileFilter var1, boolean var2) {
      if (!var0.isDirectory()) {
         return new File[0];
      } else {
         ArrayList var3 = new ArrayList();
         listAllFilesInternal(var0, var1, var2, var3);
         return (File[])var3.toArray(new File[0]);
      }
   }

   private static void listAllFilesInternal(File var0, FileFilter var1, boolean var2, ArrayList var3) {
      File[] var4 = var0.listFiles();
      if (var4 != null) {
         File[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            if (var8.isFile()) {
               if (var1.accept(var8)) {
                  var3.add(var8);
               }
            } else if (var8.isDirectory() && var2) {
               listAllFilesInternal(var8, var1, true, var3);
            }
         }

      }
   }

   public void walkGameAndModFiles(String var1, boolean var2, ZomboidFileSystem.IWalkFilesVisitor var3) {
      this.walkGameAndModFilesInternal(this.base, var1, var2, var3);
      ArrayList var4 = this.getModIDs();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         String var6 = this.getModDir((String)var4.get(var5));
         if (var6 != null) {
            this.walkGameAndModFilesInternal(new File(var6), var1, var2, var3);
         }
      }

   }

   private void walkGameAndModFilesInternal(File var1, String var2, boolean var3, ZomboidFileSystem.IWalkFilesVisitor var4) {
      File var5 = new File(var1, var2);
      if (var5.isDirectory()) {
         File[] var6 = var5.listFiles();
         if (var6 != null) {
            File[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               File var10 = var7[var9];
               var4.visit(var10, var2);
               if (var3 && var10.isDirectory()) {
                  this.walkGameAndModFilesInternal(var1, var2 + "/" + var10.getName(), true, var4);
               }
            }

         }
      }
   }

   public String[] resolveAllDirectories(String var1, FileFilter var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      this.walkGameAndModFiles(var1, var3, (var2x, var3x) -> {
         if (var2x.isDirectory() && var2.accept(var2x)) {
            String var4x = var3x + "/" + var2x.getName();
            if (!var4.contains(var4x)) {
               var4.add(var4x);
            }
         }

      });
      return (String[])var4.toArray(new String[0]);
   }

   public String[] resolveAllFiles(String var1, FileFilter var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      this.walkGameAndModFiles(var1, var3, (var2x, var3x) -> {
         if (var2x.isFile() && var2.accept(var2x)) {
            String var4x = var3x + "/" + var2x.getName();
            if (!var4.contains(var4x)) {
               var4.add(var4x);
            }
         }

      });
      return (String[])var4.toArray(new String[0]);
   }

   public String normalizeFolderPath(String var1) {
      var1 = var1.toLowerCase(Locale.ENGLISH).replace('\\', '/');
      var1 = var1 + "/";
      var1 = var1.replace("///", "/").replace("//", "/");
      return var1;
   }

   public static String processFilePath(String var0, char var1) {
      if (var1 != '\\') {
         var0 = var0.replace('\\', var1);
      }

      if (var1 != '/') {
         var0 = var0.replace('/', var1);
      }

      return var0;
   }

   public boolean tryDeleteFile(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         return false;
      } else {
         try {
            return this.deleteFile(var1);
         } catch (AccessControlException | IOException var3) {
            ExceptionLogger.logException(var3, String.format("Failed to delete file: \"%s\"", var1), DebugLog.FileIO, LogSeverity.General);
            return false;
         }
      }
   }

   public boolean deleteFile(String var1) throws IOException {
      File var2 = (new File(var1)).getAbsoluteFile();
      if (!var2.isFile()) {
         throw new FileNotFoundException(String.format("File path not found: \"%s\"", var1));
      } else if (var2.delete()) {
         DebugLog.FileIO.debugln("File deleted successfully: \"%s\"", var1);
         return true;
      } else {
         DebugLog.FileIO.debugln("Failed to delete file: \"%s\"", var1);
         return false;
      }
   }

   public void update() {
      if (this.m_modsChangedTime != 0L) {
         long var1 = System.currentTimeMillis();
         if (this.m_modsChangedTime <= var1) {
            this.m_modsChangedTime = 0L;
            this.modFolders = null;
            this.modIdToDir.clear();
            this.modDirToMod.clear();
            ChooseGameInfo.Reset();
            Iterator var3 = this.getModIDs().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               ChooseGameInfo.getModDetails(var4);
            }

            LuaEventManager.triggerEvent("OnModsModified");
         }
      }
   }

   private boolean isModFile(String var1) {
      if (this.m_modsChangedTime > 0L) {
         return false;
      } else if (this.modFolders == null) {
         return false;
      } else {
         var1 = var1.toLowerCase().replace('\\', '/');
         if (var1.endsWith("/mods/default.txt")) {
            return false;
         } else {
            for(int var2 = 0; var2 < this.modFolders.size(); ++var2) {
               String var3 = ((String)this.modFolders.get(var2)).toLowerCase().replace('\\', '/');
               if (var1.startsWith(var3)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private void onModFileChanged(String var1) {
      this.m_modsChangedTime = System.currentTimeMillis() + 2000L;
   }

   public void cleanMultiplayerSaves() {
      DebugLog.FileIO.println("Start cleaning save fs");
      String var1 = this.getSaveDir();
      String var2 = var1 + File.separator + "Multiplayer" + File.separator;
      File var3 = new File(var2);
      if (!var3.exists()) {
         var3.mkdir();
      }

      try {
         File[] var4 = var3.listFiles();
         File[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            DebugLog.FileIO.println("Checking " + var8.getAbsoluteFile() + " dir");
            if (var8.isDirectory()) {
               String var10002 = var8.toString();
               File var9 = new File(var10002 + File.separator + "map.bin");
               if (var9.exists()) {
                  DebugLog.FileIO.println("Processing " + var8.getAbsoluteFile() + " dir");

                  try {
                     Stream var10 = Files.walk(var8.toPath());
                     var10.forEach((var0) -> {
                        if (var0.getFileName().toString().matches("map_\\d+_\\d+.bin")) {
                           DebugLog.FileIO.println("Delete " + var0.getFileName().toString());
                           var0.toFile().delete();
                        }

                     });
                  } catch (IOException var11) {
                     throw new RuntimeException(var11);
                  }
               }
            }
         }
      } catch (RuntimeException var12) {
         var12.printStackTrace();
      }

   }

   public void resetDefaultModsForNewRelease(String var1) {
      ensureFolderExists(this.getCacheDirSub("mods"));
      String var10000 = this.getCacheDirSub("mods");
      String var2 = var10000 + File.separator + "reset-mods-" + var1 + ".txt";
      File var3 = new File(var2);
      if (!var3.exists()) {
         try {
            FileWriter var4 = new FileWriter(var3);

            try {
               BufferedWriter var5 = new BufferedWriter(var4);

               try {
                  String var6 = "If this file does not exist, default.txt will be reset to empty (no mods active).";
                  var5.write(var6);
               } catch (Throwable var10) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }

                  throw var10;
               }

               var5.close();
            } catch (Throwable var11) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }

               throw var11;
            }

            var4.close();
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
            return;
         }

         ActiveMods var13 = ActiveMods.getById("default");
         var13.clear();
         this.saveModsFile();
      }
   }

   public interface IWalkFilesVisitor {
      void visit(File var1, String var2);
   }
}
