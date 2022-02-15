package zombie.iso.areas.isoregion.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import zombie.core.Colors;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.regions.IsoChunkRegion;
import zombie.iso.areas.isoregion.regions.IsoRegionManager;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;

public final class DataRoot {
   private final Map cellMap = new HashMap();
   public final DataRoot.SelectInfo select = new DataRoot.SelectInfo(this);
   private final DataRoot.SelectInfo selectInternal = new DataRoot.SelectInfo(this);
   public final IsoRegionManager regionManager = new IsoRegionManager(this);
   private final ArrayList dirtyIsoWorldRegions = new ArrayList();
   private final ArrayList dirtyChunks = new ArrayList();
   protected static int recalcs;
   protected static int floodFills;
   protected static int merges;
   private static final long[] t_start = new long[5];
   private static final long[] t_end = new long[5];
   private static final long[] t_time = new long[5];

   public void getAllChunks(List var1) {
      Iterator var2 = this.cellMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ((DataCell)var3.getValue()).getAllChunks(var1);
      }

   }

   private DataCell getCell(int var1) {
      return (DataCell)this.cellMap.get(var1);
   }

   private DataCell addCell(int var1, int var2, int var3) {
      DataCell var4 = new DataCell(this, var1, var2, var3);
      this.cellMap.put(var3, var4);
      return var4;
   }

   public DataChunk getDataChunk(int var1, int var2) {
      int var3 = IsoRegions.hash(var1 / 30, var2 / 30);
      DataCell var4 = (DataCell)this.cellMap.get(var3);
      if (var4 != null) {
         int var5 = IsoRegions.hash(var1, var2);
         return var4.getChunk(var5);
      } else {
         return null;
      }
   }

   private void setDataChunk(DataChunk var1) {
      int var2 = IsoRegions.hash(var1.getChunkX() / 30, var1.getChunkY() / 30);
      DataCell var3 = (DataCell)this.cellMap.get(var2);
      if (var3 == null) {
         var3 = this.addCell(var1.getChunkX() / 30, var1.getChunkY() / 30, var2);
      }

      var3.setChunk(var1);
   }

   public IsoWorldRegion getIsoWorldRegion(int var1, int var2, int var3) {
      this.selectInternal.reset(var1, var2, var3, false);
      if (this.selectInternal.chunk != null) {
         IsoChunkRegion var4 = this.selectInternal.chunk.getIsoChunkRegion(this.selectInternal.chunkSquareX, this.selectInternal.chunkSquareY, var3);
         if (var4 != null) {
            return var4.getIsoWorldRegion();
         }
      }

      return null;
   }

   public byte getSquareFlags(int var1, int var2, int var3) {
      this.selectInternal.reset(var1, var2, var3, false);
      return this.selectInternal.square;
   }

   public IsoChunkRegion getIsoChunkRegion(int var1, int var2, int var3) {
      this.selectInternal.reset(var1, var2, var3, false);
      return this.selectInternal.chunk != null ? this.selectInternal.chunk.getIsoChunkRegion(this.selectInternal.chunkSquareX, this.selectInternal.chunkSquareY, var3) : null;
   }

   public void resetAllData() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.cellMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         DataCell var4 = (DataCell)var3.getValue();
         Iterator var5 = var4.dataChunks.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            DataChunk var7 = (DataChunk)var6.getValue();

            for(int var8 = 0; var8 < 8; ++var8) {
               Iterator var9 = var7.getChunkRegions(var8).iterator();

               while(var9.hasNext()) {
                  IsoChunkRegion var10 = (IsoChunkRegion)var9.next();
                  if (var10.getIsoWorldRegion() != null && !var1.contains(var10.getIsoWorldRegion())) {
                     var1.add(var10.getIsoWorldRegion());
                  }

                  var10.setIsoWorldRegion((IsoWorldRegion)null);
                  this.regionManager.releaseIsoChunkRegion(var10);
               }
            }
         }

         var4.dataChunks.clear();
      }

      this.cellMap.clear();
      var2 = var1.iterator();

      while(var2.hasNext()) {
         IsoWorldRegion var11 = (IsoWorldRegion)var2.next();
         this.regionManager.releaseIsoWorldRegion(var11);
      }

   }

   public void EnqueueDirtyDataChunk(DataChunk var1) {
      if (!this.dirtyChunks.contains(var1)) {
         this.dirtyChunks.add(var1);
      }

   }

   public void EnqueueDirtyIsoWorldRegion(IsoWorldRegion var1) {
      if (!this.dirtyIsoWorldRegions.contains(var1)) {
         this.dirtyIsoWorldRegions.add(var1);
      }

   }

   public void DequeueDirtyIsoWorldRegion(IsoWorldRegion var1) {
      this.dirtyIsoWorldRegions.remove(var1);
   }

   public void updateExistingSquare(int var1, int var2, int var3, byte var4) {
      this.select.reset(var1, var2, var3, false);
      if (this.select.chunk != null) {
         byte var5 = -1;
         if (this.select.square != -1) {
            var5 = this.select.square;
         }

         if (var4 == var5) {
            return;
         }

         this.select.chunk.setOrAddSquare(this.select.chunkSquareX, this.select.chunkSquareY, this.select.z, var4, true);
      } else {
         IsoRegions.warn("DataRoot.updateExistingSquare -> trying to change a square on a unknown chunk");
      }

   }

   public void processDirtyChunks() {
      if (this.dirtyChunks.size() > 0) {
         long var1 = System.nanoTime();
         recalcs = 0;
         floodFills = 0;
         merges = 0;
         t_start[0] = System.nanoTime();

         DataChunk var3;
         int var4;
         for(var4 = 0; var4 < this.dirtyChunks.size(); ++var4) {
            var3 = (DataChunk)this.dirtyChunks.get(var4);
            var3.recalculate();
            ++recalcs;
         }

         t_end[0] = System.nanoTime();
         t_start[1] = System.nanoTime();

         for(var4 = 0; var4 < this.dirtyChunks.size(); ++var4) {
            var3 = (DataChunk)this.dirtyChunks.get(var4);
            DataChunk var5 = this.getDataChunk(var3.getChunkX(), var3.getChunkY() - 1);
            DataChunk var6 = this.getDataChunk(var3.getChunkX() - 1, var3.getChunkY());
            DataChunk var7 = this.getDataChunk(var3.getChunkX(), var3.getChunkY() + 1);
            DataChunk var8 = this.getDataChunk(var3.getChunkX() + 1, var3.getChunkY());
            var3.link(var5, var6, var7, var8);
         }

         t_end[1] = System.nanoTime();
         t_start[2] = System.nanoTime();

         for(var4 = 0; var4 < this.dirtyChunks.size(); ++var4) {
            var3 = (DataChunk)this.dirtyChunks.get(var4);
            var3.interConnect();
         }

         t_end[2] = System.nanoTime();
         t_start[3] = System.nanoTime();

         for(var4 = 0; var4 < this.dirtyChunks.size(); ++var4) {
            var3 = (DataChunk)this.dirtyChunks.get(var4);
            var3.recalcRoofs();
            var3.unsetDirtyAll();
         }

         t_end[3] = System.nanoTime();
         t_start[4] = System.nanoTime();
         if (this.dirtyIsoWorldRegions.size() > 0) {
            int var9;
            IsoWorldRegion var11;
            for(var9 = 0; var9 < this.dirtyIsoWorldRegions.size(); ++var9) {
               var11 = (IsoWorldRegion)this.dirtyIsoWorldRegions.get(var9);
               var11.unlinkNeighbors();
            }

            for(var9 = 0; var9 < this.dirtyIsoWorldRegions.size(); ++var9) {
               var11 = (IsoWorldRegion)this.dirtyIsoWorldRegions.get(var9);
               var11.linkNeighbors();
            }

            this.dirtyIsoWorldRegions.clear();
         }

         t_end[4] = System.nanoTime();
         this.dirtyChunks.clear();
         long var12 = System.nanoTime();
         long var10 = var12 - var1;
         if (IsoRegions.PRINT_D) {
            t_time[0] = t_end[0] - t_start[0];
            t_time[1] = t_end[1] - t_start[1];
            t_time[2] = t_end[2] - t_start[2];
            t_time[3] = t_end[3] - t_start[3];
            t_time[4] = t_end[4] - t_start[4];
            String var10000 = String.format("%.6f", (double)var10 / 1000000.0D);
            IsoRegions.log("--- IsoRegion update: " + var10000 + " ms, recalc: " + String.format("%.6f", (double)t_time[0] / 1000000.0D) + " ms, link: " + String.format("%.6f", (double)t_time[1] / 1000000.0D) + " ms, interconnect: " + String.format("%.6f", (double)t_time[2] / 1000000.0D) + " ms, roofs: " + String.format("%.6f", (double)t_time[3] / 1000000.0D) + " ms, worldRegion: " + String.format("%.6f", (double)t_time[4] / 1000000.0D) + " ms, recalcs = " + recalcs + ", merges = " + merges + ", floodfills = " + floodFills, Colors.CornFlowerBlue);
         }
      }

   }

   public static final class SelectInfo {
      public int x;
      public int y;
      public int z;
      public int chunkSquareX;
      public int chunkSquareY;
      public int chunkx;
      public int chunky;
      public int cellx;
      public int celly;
      public int chunkID;
      public int cellID;
      public DataCell cell;
      public DataChunk chunk;
      public byte square;
      private final DataRoot root;

      private SelectInfo(DataRoot var1) {
         this.root = var1;
      }

      public void reset(int var1, int var2, int var3, boolean var4) {
         this.reset(var1, var2, var3, var4, var4);
      }

      public void reset(int var1, int var2, int var3, boolean var4, boolean var5) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.chunkSquareX = var1 % 10;
         this.chunkSquareY = var2 % 10;
         this.chunkx = var1 / 10;
         this.chunky = var2 / 10;
         this.cellx = var1 / 300;
         this.celly = var2 / 300;
         this.chunkID = IsoRegions.hash(this.chunkx, this.chunky);
         this.cellID = IsoRegions.hash(this.cellx, this.celly);
         this.cell = null;
         this.chunk = null;
         this.square = -1;
         this.ensureSquare(var5);
         if (this.chunk == null && var4) {
            this.ensureChunk(var4);
         }

      }

      private void ensureCell(boolean var1) {
         if (this.cell == null) {
            this.cell = this.root.getCell(this.cellID);
         }

         if (this.cell == null && var1) {
            this.cell = this.root.addCell(this.cellx, this.celly, this.cellID);
         }

      }

      private void ensureChunk(boolean var1) {
         this.ensureCell(var1);
         if (this.cell != null) {
            if (this.chunk == null) {
               this.chunk = this.cell.getChunk(this.chunkID);
            }

            if (this.chunk == null && var1) {
               this.chunk = this.cell.addChunk(this.chunkx, this.chunky, this.chunkID);
            }

         }
      }

      private void ensureSquare(boolean var1) {
         this.ensureCell(var1);
         if (this.cell != null) {
            this.ensureChunk(var1);
            if (this.chunk != null) {
               if (this.square == -1) {
                  this.square = this.chunk.getSquare(this.chunkSquareX, this.chunkSquareY, this.z, true);
               }

               if (this.square == -1 && var1) {
                  this.square = this.chunk.setOrAddSquare(this.chunkSquareX, this.chunkSquareY, this.z, (byte)0, true);
               }

            }
         }
      }
   }
}
