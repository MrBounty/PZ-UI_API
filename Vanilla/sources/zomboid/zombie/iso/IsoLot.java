package zombie.iso;

import gnu.trove.list.array.TIntArrayList;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import zombie.ChunkMapFilenames;
import zombie.core.logger.ExceptionLogger;
import zombie.popman.ObjectPool;
import zombie.util.BufferedRandomAccessFile;

public class IsoLot {
   public static final HashMap InfoHeaders = new HashMap();
   public static final ArrayList InfoHeaderNames = new ArrayList();
   public static final HashMap InfoFileNames = new HashMap();
   public static final ObjectPool pool = new ObjectPool(IsoLot::new);
   private String m_lastUsedPath = "";
   public int wx = 0;
   public int wy = 0;
   final int[] m_offsetInData = new int[800];
   final TIntArrayList m_data = new TIntArrayList();
   private RandomAccessFile m_in = null;
   LotHeader info;

   public static void Dispose() {
      InfoHeaders.clear();
      InfoHeaderNames.clear();
      InfoFileNames.clear();
      pool.forEach((var0) -> {
         RandomAccessFile var1 = var0.m_in;
         if (var1 != null) {
            var0.m_in = null;

            try {
               var1.close();
            } catch (IOException var3) {
               ExceptionLogger.logException(var3);
            }
         }

      });
   }

   public static String readString(BufferedRandomAccessFile var0) throws EOFException, IOException {
      String var1 = var0.getNextLine();
      return var1;
   }

   public static int readInt(RandomAccessFile var0) throws EOFException, IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   public static int readShort(RandomAccessFile var0) throws EOFException, IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      if ((var1 | var2) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8);
      }
   }

   public static synchronized void put(IsoLot var0) {
      var0.info = null;
      var0.m_data.resetQuick();
      pool.release((Object)var0);
   }

   public static synchronized IsoLot get(Integer var0, Integer var1, Integer var2, Integer var3, IsoChunk var4) {
      IsoLot var5 = (IsoLot)pool.alloc();
      var5.load(var0, var1, var2, var3, var4);
      return var5;
   }

   public void load(Integer var1, Integer var2, Integer var3, Integer var4, IsoChunk var5) {
      String var6 = ChunkMapFilenames.instance.getHeader(var1, var2);
      this.info = (LotHeader)InfoHeaders.get(var6);
      this.wx = var3;
      this.wy = var4;
      var5.lotheader = this.info;

      try {
         var6 = "world_" + var1 + "_" + var2 + ".lotpack";
         File var7 = new File((String)InfoFileNames.get(var6));
         if (this.m_in == null || !this.m_lastUsedPath.equals(var7.getAbsolutePath())) {
            if (this.m_in != null) {
               this.m_in.close();
            }

            this.m_in = new BufferedRandomAccessFile(var7.getAbsolutePath(), "r", 4096);
            this.m_lastUsedPath = var7.getAbsolutePath();
         }

         int var8 = 0;
         int var9 = this.wx - var1 * 30;
         int var10 = this.wy - var2 * 30;
         int var11 = var9 * 30 + var10;
         this.m_in.seek((long)(4 + var11 * 8));
         int var12 = readInt(this.m_in);
         this.m_in.seek((long)var12);
         this.m_data.resetQuick();
         int var13 = Math.min(this.info.levels, 8);

         for(int var14 = 0; var14 < var13; ++var14) {
            for(int var15 = 0; var15 < 10; ++var15) {
               for(int var16 = 0; var16 < 10; ++var16) {
                  int var17 = var15 + var16 * 10 + var14 * 100;
                  this.m_offsetInData[var17] = -1;
                  if (var8 > 0) {
                     --var8;
                  } else {
                     int var18 = readInt(this.m_in);
                     if (var18 == -1) {
                        var8 = readInt(this.m_in);
                        if (var8 > 0) {
                           --var8;
                           continue;
                        }
                     }

                     if (var18 > 1) {
                        this.m_offsetInData[var17] = this.m_data.size();
                        this.m_data.add(var18 - 1);
                        int var19 = readInt(this.m_in);

                        for(int var20 = 1; var20 < var18; ++var20) {
                           int var21 = readInt(this.m_in);
                           this.m_data.add(var21);
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var22) {
         Arrays.fill(this.m_offsetInData, -1);
         this.m_data.resetQuick();
         ExceptionLogger.logException(var22);
      }

   }
}
