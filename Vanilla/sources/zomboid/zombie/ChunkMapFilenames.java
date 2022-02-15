package zombie;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import zombie.core.Core;

public final class ChunkMapFilenames {
   public static ChunkMapFilenames instance = new ChunkMapFilenames();
   public final ConcurrentHashMap Map = new ConcurrentHashMap();
   public final ConcurrentHashMap HeaderMap = new ConcurrentHashMap();
   String prefix = "map_";
   private File dirFile;
   private String cacheDir;

   public void clear() {
      this.dirFile = null;
      this.cacheDir = null;
      this.Map.clear();
      this.HeaderMap.clear();
   }

   public File getFilename(int var1, int var2) {
      long var3 = (long)var1 << 32 | (long)var2;
      if (this.Map.containsKey(var3)) {
         return (File)this.Map.get(var3);
      } else {
         if (this.cacheDir == null) {
            this.cacheDir = ZomboidFileSystem.instance.getGameModeCacheDir();
         }

         String var5 = this.cacheDir + File.separator + Core.GameSaveWorld + File.separator + this.prefix + var1 + "_" + var2 + ".bin";
         File var6 = new File(var5);
         this.Map.put(var3, var6);
         return var6;
      }
   }

   public File getDir(String var1) {
      if (this.cacheDir == null) {
         this.cacheDir = ZomboidFileSystem.instance.getGameModeCacheDir();
      }

      if (this.dirFile == null) {
         this.dirFile = new File(this.cacheDir + File.separator + var1);
      }

      return this.dirFile;
   }

   public String getHeader(int var1, int var2) {
      long var3 = (long)var1 << 32 | (long)var2;
      if (this.HeaderMap.containsKey(var3)) {
         return this.HeaderMap.get(var3).toString();
      } else {
         String var5 = var1 + "_" + var2 + ".lotheader";
         this.HeaderMap.put(var3, var5);
         return var5;
      }
   }
}
