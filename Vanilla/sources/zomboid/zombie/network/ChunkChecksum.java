package zombie.network;

import gnu.trove.map.hash.TIntLongHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import zombie.ZomboidFileSystem;
import zombie.core.Core;

public class ChunkChecksum {
   private static final TIntLongHashMap checksumCache = new TIntLongHashMap();
   private static final StringBuilder stringBuilder = new StringBuilder(128);
   private static final CRC32 crc32 = new CRC32();
   private static final byte[] bytes = new byte[1024];

   private static void noise(String var0) {
      if (Core.bDebug) {
      }

   }

   public static long getChecksum(int var0, int var1) throws IOException {
      MPStatistic.getInstance().ChunkChecksum.Start();
      long var2 = 0L;
      synchronized(checksumCache) {
         int var5 = var0 + var1 * 30 * 1000;
         if (checksumCache.containsKey(var5)) {
            noise(var0 + "," + var1 + " found in cache crc=" + checksumCache.get(var5));
            var2 = checksumCache.get(var5);
         } else {
            stringBuilder.setLength(0);
            stringBuilder.append(ZomboidFileSystem.instance.getGameModeCacheDir());
            stringBuilder.append(File.separator);
            stringBuilder.append(Core.GameSaveWorld);
            stringBuilder.append(File.separator);
            stringBuilder.append("map_");
            stringBuilder.append(var0);
            stringBuilder.append("_");
            stringBuilder.append(var1);
            stringBuilder.append(".bin");
            var2 = createChecksum(stringBuilder.toString());
            checksumCache.put(var5, var2);
            noise(var0 + "," + var1 + " read from disk crc=" + var2);
         }
      }

      MPStatistic.getInstance().ChunkChecksum.End();
      return var2;
   }

   public static long getChecksumIfExists(int var0, int var1) throws IOException {
      long var2 = 0L;
      MPStatistic.getInstance().ChunkChecksum.Start();
      synchronized(checksumCache) {
         int var5 = var0 + var1 * 30 * 1000;
         if (checksumCache.containsKey(var5)) {
            var2 = checksumCache.get(var5);
         }
      }

      MPStatistic.getInstance().ChunkChecksum.End();
      return var2;
   }

   public static void setChecksum(int var0, int var1, long var2) {
      MPStatistic.getInstance().ChunkChecksum.Start();
      synchronized(checksumCache) {
         int var5 = var0 + var1 * 30 * 1000;
         checksumCache.put(var5, var2);
         noise(var0 + "," + var1 + " set crc=" + var2);
      }

      MPStatistic.getInstance().ChunkChecksum.End();
   }

   public static long createChecksum(String var0) throws IOException {
      MPStatistic.getInstance().ChunkChecksum.Start();
      File var1 = new File(var0);
      if (!var1.exists()) {
         MPStatistic.getInstance().ChunkChecksum.End();
         return 0L;
      } else {
         FileInputStream var2 = new FileInputStream(var0);

         long var6;
         try {
            crc32.reset();

            while(true) {
               int var3;
               if ((var3 = var2.read(bytes)) == -1) {
                  long var4 = crc32.getValue();
                  MPStatistic.getInstance().ChunkChecksum.End();
                  var6 = var4;
                  break;
               }

               crc32.update(bytes, 0, var3);
            }
         } catch (Throwable var9) {
            try {
               var2.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var2.close();
         return var6;
      }
   }

   public static void Reset() {
      MPStatistic.getInstance().ChunkChecksum.Start();
      checksumCache.clear();
      MPStatistic.getInstance().ChunkChecksum.End();
   }
}
