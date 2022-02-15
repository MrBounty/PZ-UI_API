package zombie.world;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import zombie.GameWindow;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;
import zombie.scripting.ScriptManager;
import zombie.world.logger.Log;
import zombie.world.logger.WorldDictionaryLogger;

public class DictionaryData {
   protected final Map itemIdToInfoMap = new HashMap();
   protected final Map itemTypeToInfoMap = new HashMap();
   protected final Map spriteNameToIdMap = new HashMap();
   protected final Map spriteIdToNameMap = new HashMap();
   protected final Map objectNameToIdMap = new HashMap();
   protected final Map objectIdToNameMap = new HashMap();
   protected final ArrayList unsetObject = new ArrayList();
   protected final ArrayList unsetSprites = new ArrayList();
   protected short NextItemID = 0;
   protected int NextSpriteNameID = 0;
   protected byte NextObjectNameID = 0;
   protected byte[] serverDataCache;
   private File dataBackupPath;

   protected boolean isClient() {
      return false;
   }

   protected void reset() {
      this.NextItemID = 0;
      this.NextSpriteNameID = 0;
      this.NextObjectNameID = 0;
      this.itemIdToInfoMap.clear();
      this.itemTypeToInfoMap.clear();
      this.objectIdToNameMap.clear();
      this.objectNameToIdMap.clear();
      this.spriteIdToNameMap.clear();
      this.spriteNameToIdMap.clear();
   }

   protected final ItemInfo getItemInfoFromType(String var1) {
      return (ItemInfo)this.itemTypeToInfoMap.get(var1);
   }

   protected final ItemInfo getItemInfoFromID(short var1) {
      return (ItemInfo)this.itemIdToInfoMap.get(var1);
   }

   protected final short getItemRegistryID(String var1) {
      ItemInfo var2 = (ItemInfo)this.itemTypeToInfoMap.get(var1);
      if (var2 != null) {
         return var2.registryID;
      } else {
         if (Core.bDebug) {
            DebugLog.log("WARNING: Cannot get registry id for item: " + var1);
         }

         return -1;
      }
   }

   protected final String getItemTypeFromID(short var1) {
      ItemInfo var2 = (ItemInfo)this.itemIdToInfoMap.get(var1);
      return var2 != null ? var2.fullType : null;
   }

   protected final String getItemTypeDebugString(short var1) {
      String var2 = this.getItemTypeFromID(var1);
      if (var2 == null) {
         var2 = "Unknown";
      }

      return var2;
   }

   protected final String getSpriteNameFromID(int var1) {
      if (var1 >= 0) {
         if (this.spriteIdToNameMap.containsKey(var1)) {
            return (String)this.spriteIdToNameMap.get(var1);
         }

         IsoSprite var2 = IsoSprite.getSprite(IsoSpriteManager.instance, var1);
         if (var2 != null && var2.name != null) {
            return var2.name;
         }
      }

      DebugLog.log("WorldDictionary, Couldnt find sprite name for ID '" + var1 + "'.");
      return null;
   }

   protected final int getIdForSpriteName(String var1) {
      if (var1 != null) {
         if (this.spriteNameToIdMap.containsKey(var1)) {
            return (Integer)this.spriteNameToIdMap.get(var1);
         }

         IsoSprite var2 = IsoSpriteManager.instance.getSprite(var1);
         if (var2 != null && var2.ID >= 0 && var2.ID != 20000000 && var2.name.equals(var1)) {
            return var2.ID;
         }
      }

      return -1;
   }

   protected final String getObjectNameFromID(byte var1) {
      if (var1 >= 0) {
         if (this.objectIdToNameMap.containsKey(var1)) {
            return (String)this.objectIdToNameMap.get(var1);
         }

         if (Core.bDebug) {
            DebugLog.log("WorldDictionary, Couldnt find object name for ID '" + var1 + "'.");
         }
      }

      return null;
   }

   protected final byte getIdForObjectName(String var1) {
      if (var1 != null) {
         if (this.objectNameToIdMap.containsKey(var1)) {
            return (Byte)this.objectNameToIdMap.get(var1);
         }

         if (Core.bDebug) {
         }
      }

      return -1;
   }

   protected final void getItemMods(List var1) {
      var1.clear();
      Iterator var2 = this.itemIdToInfoMap.entrySet().iterator();

      while(true) {
         Entry var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (Entry)var2.next();
            if (!var1.contains(((ItemInfo)var3.getValue()).modID)) {
               var1.add(((ItemInfo)var3.getValue()).modID);
            }
         } while(((ItemInfo)var3.getValue()).modOverrides == null);

         List var4 = ((ItemInfo)var3.getValue()).modOverrides;

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            if (!var1.contains(var4.get(var5))) {
               var1.add((String)var4.get(var5));
            }
         }
      }
   }

   protected final void getModuleList(List var1) {
      Iterator var2 = this.itemIdToInfoMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (!var1.contains(((ItemInfo)var3.getValue()).moduleName)) {
            var1.add(((ItemInfo)var3.getValue()).moduleName);
         }
      }

   }

   protected void parseItemLoadList(Map var1) throws WorldDictionaryException {
      Iterator var2 = var1.entrySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            ItemInfo var4 = (ItemInfo)var3.getValue();
            ItemInfo var5 = (ItemInfo)this.itemTypeToInfoMap.get(var4.fullType);
            if (var5 == null) {
               if (!var4.obsolete) {
                  if (this.NextItemID >= 32767) {
                     throw new WorldDictionaryException("Max item ID value reached for WorldDictionary!");
                  }

                  short var10003 = this.NextItemID;
                  this.NextItemID = (short)(var10003 + 1);
                  var4.registryID = var10003;
                  var4.isLoaded = true;
                  this.itemTypeToInfoMap.put(var4.fullType, var4);
                  this.itemIdToInfoMap.put(var4.registryID, var4);
                  WorldDictionaryLogger.log((Log.BaseLog)(new Log.RegisterItem(var4.copy())));
               }
            } else {
               if (var5.removed && !var4.obsolete) {
                  var5.removed = false;
                  WorldDictionaryLogger.log((Log.BaseLog)(new Log.ReinstateItem(var5.copy())));
               }

               if (!var5.modID.equals(var4.modID)) {
                  String var6 = var5.modID;
                  var5.modID = var4.modID;
                  var5.isModded = !var4.modID.equals("pz-vanilla");
                  WorldDictionaryLogger.log((Log.BaseLog)(new Log.ModIDChangedItem(var5.copy(), var6, var5.modID)));
               }

               if (var4.obsolete && (!var5.obsolete || !var5.removed)) {
                  var5.obsolete = true;
                  var5.removed = true;
                  WorldDictionaryLogger.log((Log.BaseLog)(new Log.ObsoleteItem(var5.copy())));
               }

               var5.isLoaded = true;
            }
         }

         return;
      }
   }

   protected void parseCurrentItemSet() throws WorldDictionaryException {
      Iterator var1 = this.itemTypeToInfoMap.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         ItemInfo var3 = (ItemInfo)var2.getValue();
         if (!var3.isLoaded) {
            var3.removed = true;
            WorldDictionaryLogger.log((Log.BaseLog)(new Log.RemovedItem(var3.copy(), false)));
         }

         if (var3.scriptItem == null) {
            var3.scriptItem = ScriptManager.instance.getItem(var3.fullType);
         }

         if (var3.scriptItem != null) {
            var3.scriptItem.setRegistry_id(var3.registryID);
         } else {
            var3.removed = true;
            WorldDictionaryLogger.log((Log.BaseLog)(new Log.RemovedItem(var3.copy(), true)));
         }
      }

   }

   protected void parseObjectNameLoadList(List var1) throws WorldDictionaryException {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String var3 = (String)var1.get(var2);
         if (!this.objectNameToIdMap.containsKey(var3)) {
            if (this.NextObjectNameID >= 127) {
               WorldDictionaryLogger.log("Max value for object names reached.");
               if (Core.bDebug) {
                  throw new WorldDictionaryException("Max value for object names reached.");
               }
            } else {
               byte var10002 = this.NextObjectNameID;
               this.NextObjectNameID = (byte)(var10002 + 1);
               byte var4 = var10002;
               this.objectIdToNameMap.put(var4, var3);
               this.objectNameToIdMap.put(var3, var4);
               WorldDictionaryLogger.log((Log.BaseLog)(new Log.RegisterObject(var3, var4)));
            }
         }
      }

   }

   protected void backupCurrentDataSet() throws IOException {
      this.dataBackupPath = null;
      if (!Core.getInstance().isNoSave()) {
         String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
         File var1 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary.bin");
         if (var1.exists()) {
            long var2 = Instant.now().getEpochSecond();
            String var10003 = ZomboidFileSystem.instance.getGameModeCacheDir();
            this.dataBackupPath = new File(var10003 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary_" + var2 + ".bak");
            Files.copy(var1, this.dataBackupPath);
         }

      }
   }

   protected void deleteBackupCurrentDataSet() throws IOException {
      if (Core.getInstance().isNoSave()) {
         this.dataBackupPath = null;
      } else {
         if (this.dataBackupPath != null) {
            this.dataBackupPath.delete();
         }

         this.dataBackupPath = null;
      }
   }

   protected void createErrorBackups() {
      if (!Core.getInstance().isNoSave()) {
         try {
            WorldDictionary.log("Attempting to copy WorldDictionary backups...");
            long var1 = Instant.now().getEpochSecond();
            String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
            String var3 = var10000 + File.separator + Core.GameSaveWorld + File.separator + "WD_ERROR_" + var1 + File.separator;
            WorldDictionary.log("path = " + var3);
            File var4 = new File(var3);
            boolean var5 = true;
            if (!var4.exists()) {
               var5 = var4.mkdir();
            }

            if (!var5) {
               WorldDictionary.log("Could not create backup folder folder.");
               return;
            }

            File var6;
            if (this.dataBackupPath != null) {
               var6 = new File(var3 + "WorldDictionary_backup.bin");
               if (this.dataBackupPath.exists()) {
                  Files.copy(this.dataBackupPath, var6);
               }
            }

            String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            var6 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionaryLog.lua");
            File var7 = new File(var3 + "WorldDictionaryLog.lua");
            if (var6.exists()) {
               Files.copy(var6, var7);
            }

            var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            File var8 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionaryReadable.lua");
            File var9 = new File(var3 + "WorldDictionaryReadable.lua");
            if (var8.exists()) {
               Files.copy(var8, var9);
            }

            var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            File var10 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary.bin");
            File var11 = new File(var3 + "WorldDictionary.bin");
            if (var10.exists()) {
               Files.copy(var10, var11);
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }

      }
   }

   protected void load() throws IOException, WorldDictionaryException {
      if (!Core.getInstance().isNoSave()) {
         String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
         String var1 = var10000 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary.bin";
         File var2 = new File(var1);
         if (!var2.exists()) {
            if (!WorldDictionary.isIsNewGame()) {
               throw new WorldDictionaryException("WorldDictionary data file is missing from world folder.");
            }
         } else {
            try {
               FileInputStream var3 = new FileInputStream(var2);

               try {
                  DebugLog.log("Loading WorldDictionary:" + var1);
                  ByteBuffer var4 = ByteBuffer.allocate((int)var2.length());
                  var4.clear();
                  int var5 = var3.read(var4.array());
                  var4.limit(var5);
                  this.loadFromByteBuffer(var4);
               } catch (Throwable var7) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }

                  throw var7;
               }

               var3.close();
            } catch (Exception var8) {
               var8.printStackTrace();
               throw new WorldDictionaryException("Error loading WorldDictionary.", var8);
            }
         }
      }
   }

   protected void loadFromByteBuffer(ByteBuffer var1) throws IOException {
      this.NextItemID = var1.getShort();
      this.NextObjectNameID = var1.get();
      this.NextSpriteNameID = var1.getInt();
      ArrayList var2 = new ArrayList();
      int var3 = var1.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.add(GameWindow.ReadString(var1));
      }

      ArrayList var12 = new ArrayList();
      int var5 = var1.getInt();

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         var12.add(GameWindow.ReadString(var1));
      }

      var6 = var1.getInt();

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         ItemInfo var8 = new ItemInfo();
         var8.load(var1, 186, var2, var12);
         this.itemIdToInfoMap.put(var8.registryID, var8);
         this.itemTypeToInfoMap.put(var8.fullType, var8);
      }

      var7 = var1.getInt();

      int var13;
      for(var13 = 0; var13 < var7; ++var13) {
         byte var9 = var1.get();
         String var10 = GameWindow.ReadString(var1);
         this.objectIdToNameMap.put(var9, var10);
         this.objectNameToIdMap.put(var10, var9);
      }

      var13 = var1.getInt();

      for(int var14 = 0; var14 < var13; ++var14) {
         int var15 = var1.getInt();
         String var11 = GameWindow.ReadString(var1);
         this.spriteIdToNameMap.put(var15, var11);
         this.spriteNameToIdMap.put(var11, var15);
      }

   }

   protected void save() throws IOException, WorldDictionaryException {
      if (!Core.getInstance().isNoSave()) {
         try {
            byte[] var1 = new byte[5242880];
            ByteBuffer var2 = ByteBuffer.wrap(var1);
            this.saveToByteBuffer(var2);
            var2.flip();
            if (GameServer.bServer) {
               var1 = new byte[var2.limit()];
               var2.get(var1, 0, var1.length);
               this.serverDataCache = var1;
            }

            String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            File var3 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary.tmp");
            FileOutputStream var4 = new FileOutputStream(var3);
            var4.getChannel().truncate(0L);
            var4.write(var2.array(), 0, var2.limit());
            var4.flush();
            var4.close();
            var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            File var5 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "WorldDictionary.bin");
            Files.copy(var3, var5);
            var3.delete();
         } catch (Exception var6) {
            var6.printStackTrace();
            throw new WorldDictionaryException("Error saving WorldDictionary.", var6);
         }
      }
   }

   protected void saveToByteBuffer(ByteBuffer var1) throws IOException {
      var1.putShort(this.NextItemID);
      var1.put(this.NextObjectNameID);
      var1.putInt(this.NextSpriteNameID);
      ArrayList var2 = new ArrayList();
      this.getItemMods(var2);
      var1.putInt(var2.size());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         GameWindow.WriteString(var1, var4);
      }

      ArrayList var7 = new ArrayList();
      this.getModuleList(var7);
      var1.putInt(var7.size());
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         String var5 = (String)var8.next();
         GameWindow.WriteString(var1, var5);
      }

      var1.putInt(this.itemIdToInfoMap.size());
      var8 = this.itemIdToInfoMap.entrySet().iterator();

      Entry var9;
      while(var8.hasNext()) {
         var9 = (Entry)var8.next();
         ItemInfo var6 = (ItemInfo)var9.getValue();
         var6.save(var1, var2, var7);
      }

      var1.putInt(this.objectIdToNameMap.size());
      var8 = this.objectIdToNameMap.entrySet().iterator();

      while(var8.hasNext()) {
         var9 = (Entry)var8.next();
         var1.put((Byte)var9.getKey());
         GameWindow.WriteString(var1, (String)var9.getValue());
      }

      var1.putInt(this.spriteIdToNameMap.size());
      var8 = this.spriteIdToNameMap.entrySet().iterator();

      while(var8.hasNext()) {
         var9 = (Entry)var8.next();
         var1.putInt((Integer)var9.getKey());
         GameWindow.WriteString(var1, (String)var9.getValue());
      }

   }

   protected void saveAsText(String var1) throws IOException, WorldDictionaryException {
      if (!Core.getInstance().isNoSave()) {
         String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
         File var2 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator);
         if (var2.exists() && var2.isDirectory()) {
            String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
            String var3 = var10000 + File.separator + Core.GameSaveWorld + File.separator + var1;
            File var4 = new File(var3);

            try {
               FileWriter var5 = new FileWriter(var4, false);

               try {
                  var5.write("--[[ ---- ITEMS ---- --]]" + System.lineSeparator());
                  var5.write("items = {" + System.lineSeparator());
                  Iterator var6 = this.itemIdToInfoMap.entrySet().iterator();

                  Entry var7;
                  while(var6.hasNext()) {
                     var7 = (Entry)var6.next();
                     var5.write("\t{" + System.lineSeparator());
                     ((ItemInfo)var7.getValue()).saveAsText(var5, "\t\t");
                     var5.write("\t}," + System.lineSeparator());
                  }

                  var5.write("}" + System.lineSeparator());
                  var5.write(System.lineSeparator().makeConcatWithConstants<invokedynamic>(System.lineSeparator()));
                  var5.write("--[[ ---- OBJECTS ---- --]]" + System.lineSeparator());
                  var5.write("objects = {" + System.lineSeparator());
                  var6 = this.objectIdToNameMap.entrySet().iterator();

                  Object var10001;
                  while(var6.hasNext()) {
                     var7 = (Entry)var6.next();
                     var10001 = var7.getKey();
                     var5.write("\t" + var10001 + " = \"" + (String)var7.getValue() + "\"," + System.lineSeparator());
                  }

                  var5.write("}" + System.lineSeparator());
                  var5.write(System.lineSeparator().makeConcatWithConstants<invokedynamic>(System.lineSeparator()));
                  var5.write("--[[ ---- SPRITES ---- --]]" + System.lineSeparator());
                  var5.write("sprites = {" + System.lineSeparator());
                  var6 = this.spriteIdToNameMap.entrySet().iterator();

                  while(var6.hasNext()) {
                     var7 = (Entry)var6.next();
                     var10001 = var7.getKey();
                     var5.write("\t" + var10001 + " = \"" + (String)var7.getValue() + "\"," + System.lineSeparator());
                  }

                  var5.write("}" + System.lineSeparator());
               } catch (Throwable var9) {
                  try {
                     var5.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               var5.close();
            } catch (Exception var10) {
               var10.printStackTrace();
               throw new WorldDictionaryException("Error saving WorldDictionary as text.", var10);
            }
         }

      }
   }
}
