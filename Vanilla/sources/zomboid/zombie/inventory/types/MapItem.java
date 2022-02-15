package zombie.inventory.types;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import zombie.GameWindow;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.inventory.InventoryItem;
import zombie.iso.SliceY;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.worldMap.symbols.WorldMapSymbols;

public class MapItem extends InventoryItem {
   public static MapItem WORLD_MAP_INSTANCE;
   private static final byte[] FILE_MAGIC = new byte[]{87, 77, 83, 89};
   private String m_mapID;
   private final WorldMapSymbols m_symbols = new WorldMapSymbols();

   public static MapItem getSingleton() {
      if (WORLD_MAP_INSTANCE == null) {
         Item var0 = ScriptManager.instance.FindItem("Base.Map");
         if (var0 == null) {
            return null;
         }

         WORLD_MAP_INSTANCE = new MapItem("Base", "World Map", "WorldMap", var0);
      }

      return WORLD_MAP_INSTANCE;
   }

   public static void SaveWorldMap() {
      if (WORLD_MAP_INSTANCE != null) {
         try {
            ByteBuffer var0 = SliceY.SliceBuffer;
            var0.clear();
            var0.put(FILE_MAGIC);
            var0.putInt(186);
            WORLD_MAP_INSTANCE.getSymbols().save(var0);
            String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
            File var1 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "map_symbols.bin");
            FileOutputStream var2 = new FileOutputStream(var1);

            try {
               BufferedOutputStream var3 = new BufferedOutputStream(var2);

               try {
                  var3.write(var0.array(), 0, var0.position());
               } catch (Throwable var8) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }

               var3.close();
            } catch (Throwable var9) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }

               throw var9;
            }

            var2.close();
         } catch (Exception var10) {
            ExceptionLogger.logException(var10);
         }

      }
   }

   public static void LoadWorldMap() {
      if (getSingleton() != null) {
         String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
         File var0 = new File(var10002 + File.separator + Core.GameSaveWorld + File.separator + "map_symbols.bin");

         try {
            FileInputStream var1 = new FileInputStream(var0);

            try {
               BufferedInputStream var2 = new BufferedInputStream(var1);

               try {
                  ByteBuffer var3 = SliceY.SliceBuffer;
                  var3.clear();
                  int var4 = var2.read(var3.array());
                  var3.limit(var4);
                  byte[] var5 = new byte[4];
                  var3.get(var5);
                  if (!Arrays.equals(var5, FILE_MAGIC)) {
                     throw new IOException(var0.getAbsolutePath() + " does not appear to be map_symbols.bin");
                  }

                  int var6 = var3.getInt();
                  getSingleton().getSymbols().load(var3, var6);
               } catch (Throwable var9) {
                  try {
                     var2.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               var2.close();
            } catch (Throwable var10) {
               try {
                  var1.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            var1.close();
         } catch (FileNotFoundException var11) {
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
         }

      }
   }

   public static void Reset() {
      if (WORLD_MAP_INSTANCE != null) {
         WORLD_MAP_INSTANCE.getSymbols().clear();
         WORLD_MAP_INSTANCE = null;
      }
   }

   public MapItem(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public MapItem(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
   }

   public int getSaveType() {
      return Item.Type.Map.ordinal();
   }

   public boolean IsMap() {
      return true;
   }

   public void setMapID(String var1) {
      this.m_mapID = var1;
   }

   public String getMapID() {
      return this.m_mapID;
   }

   public WorldMapSymbols getSymbols() {
      return this.m_symbols;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      GameWindow.WriteString(var1, this.m_mapID);
      this.m_symbols.save(var1);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      this.m_mapID = GameWindow.ReadString(var1);
      this.m_symbols.load(var1, var2);
   }
}
