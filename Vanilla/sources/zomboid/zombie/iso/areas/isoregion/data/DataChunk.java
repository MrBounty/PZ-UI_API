package zombie.iso.areas.isoregion.data;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import zombie.debug.DebugLog;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.regions.IsoChunkRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;

public final class DataChunk {
   private final DataCell cell;
   private final int hashId;
   private final int chunkX;
   private final int chunkY;
   protected int highestZ = 0;
   protected long lastUpdateStamp = 0L;
   private final boolean[] activeZLayers = new boolean[8];
   private final boolean[] dirtyZLayers = new boolean[8];
   private byte[] squareFlags;
   private byte[] regionIDs;
   private final ArrayList chunkRegions = new ArrayList(8);
   private static byte selectedFlags;
   private static final ArrayDeque tmpSquares = new ArrayDeque();
   private static final HashSet tmpLinkedChunks = new HashSet();
   private static final boolean[] exploredPositions = new boolean[100];
   private static IsoChunkRegion lastCurRegion;
   private static IsoChunkRegion lastOtherRegionFullConnect;
   private static ArrayList oldList = new ArrayList();
   private static final ArrayDeque chunkQueue = new ArrayDeque();

   protected DataChunk(int var1, int var2, DataCell var3, int var4) {
      this.cell = var3;
      this.hashId = var4 < 0 ? IsoRegions.hash(var1, var2) : var4;
      this.chunkX = var1;
      this.chunkY = var2;

      for(int var5 = 0; var5 < 8; ++var5) {
         this.chunkRegions.add(new ArrayList());
      }

   }

   protected int getHashId() {
      return this.hashId;
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public int getChunkY() {
      return this.chunkY;
   }

   protected ArrayList getChunkRegions(int var1) {
      return (ArrayList)this.chunkRegions.get(var1);
   }

   public long getLastUpdateStamp() {
      return this.lastUpdateStamp;
   }

   public void setLastUpdateStamp(long var1) {
      this.lastUpdateStamp = var1;
   }

   protected boolean isDirty(int var1) {
      return this.activeZLayers[var1] ? this.dirtyZLayers[var1] : false;
   }

   protected void setDirty(int var1) {
      if (this.activeZLayers[var1]) {
         this.dirtyZLayers[var1] = true;
         this.cell.dataRoot.EnqueueDirtyDataChunk(this);
      }

   }

   public void setDirtyAllActive() {
      boolean var1 = false;

      for(int var2 = 0; var2 < 8; ++var2) {
         if (this.activeZLayers[var2]) {
            this.dirtyZLayers[var2] = true;
            if (!var1) {
               this.cell.dataRoot.EnqueueDirtyDataChunk(this);
               var1 = true;
            }
         }
      }

   }

   protected void unsetDirtyAll() {
      for(int var1 = 0; var1 < 8; ++var1) {
         this.dirtyZLayers[var1] = false;
      }

   }

   private boolean validCoords(int var1, int var2, int var3) {
      return var1 >= 0 && var1 < 10 && var2 >= 0 && var2 < 10 && var3 >= 0 && var3 < this.highestZ + 1;
   }

   private int getCoord1D(int var1, int var2, int var3) {
      return var3 * 10 * 10 + var2 * 10 + var1;
   }

   public byte getSquare(int var1, int var2, int var3) {
      return this.getSquare(var1, var2, var3, false);
   }

   public byte getSquare(int var1, int var2, int var3, boolean var4) {
      if (this.squareFlags != null && (var4 || this.validCoords(var1, var2, var3))) {
         return this.activeZLayers[var3] ? this.squareFlags[this.getCoord1D(var1, var2, var3)] : -1;
      } else {
         return -1;
      }
   }

   protected byte setOrAddSquare(int var1, int var2, int var3, byte var4) {
      return this.setOrAddSquare(var1, var2, var3, var4, false);
   }

   protected byte setOrAddSquare(int var1, int var2, int var3, byte var4, boolean var5) {
      if (!var5 && !this.validCoords(var1, var2, var3)) {
         return -1;
      } else {
         this.ensureSquares(var3);
         int var6 = this.getCoord1D(var1, var2, var3);
         if (this.squareFlags[var6] != var4) {
            this.setDirty(var3);
         }

         this.squareFlags[var6] = var4;
         return var4;
      }
   }

   private void ensureSquares(int var1) {
      if (var1 >= 0 && var1 < 8) {
         if (!this.activeZLayers[var1]) {
            this.ensureSquareArray(var1);
            this.activeZLayers[var1] = true;
            if (var1 > this.highestZ) {
               this.highestZ = var1;
            }

            for(int var3 = 0; var3 < 10; ++var3) {
               for(int var4 = 0; var4 < 10; ++var4) {
                  int var2 = this.getCoord1D(var4, var3, var1);
                  this.squareFlags[var2] = (byte)(var1 == 0 ? 16 : 0);
               }
            }
         }

      }
   }

   private void ensureSquareArray(int var1) {
      int var2 = (var1 + 1) * 10 * 10;
      if (this.squareFlags == null || this.squareFlags.length < var2) {
         byte[] var3 = this.squareFlags;
         byte[] var4 = this.regionIDs;
         this.squareFlags = new byte[var2];
         this.regionIDs = new byte[var2];
         if (var3 != null) {
            for(int var5 = 0; var5 < var3.length; ++var5) {
               this.squareFlags[var5] = var3[var5];
               this.regionIDs[var5] = var4[var5];
            }
         }
      }

   }

   public void save(ByteBuffer var1) {
      try {
         int var2 = var1.position();
         var1.putInt(0);
         var1.putInt(this.highestZ);
         int var3 = (this.highestZ + 1) * 100;
         var1.putInt(var3);

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            var1.put(this.squareFlags[var4]);
         }

         var4 = var1.position();
         var1.position(var2);
         var1.putInt(var4 - var2);
         var1.position(var4);
      } catch (Exception var5) {
         DebugLog.log(var5.getMessage());
         var5.printStackTrace();
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) {
      try {
         if (var3) {
            var1.getInt();
         }

         this.highestZ = var1.getInt();

         int var4;
         for(var4 = this.highestZ; var4 >= 0; --var4) {
            this.ensureSquares(var4);
         }

         var4 = var1.getInt();

         for(int var5 = 0; var5 < var4; ++var5) {
            this.squareFlags[var5] = var1.get();
         }
      } catch (Exception var6) {
         DebugLog.log(var6.getMessage());
         var6.printStackTrace();
      }

   }

   public void setSelectedFlags(int var1, int var2, int var3) {
      if (var3 >= 0 && var3 <= this.highestZ) {
         selectedFlags = this.squareFlags[this.getCoord1D(var1, var2, var3)];
      } else {
         selectedFlags = -1;
      }

   }

   public boolean selectedHasFlags(byte var1) {
      return (selectedFlags & var1) == var1;
   }

   protected boolean squareHasFlags(int var1, int var2, int var3, byte var4) {
      return this.squareHasFlags(this.getCoord1D(var1, var2, var3), var4);
   }

   private boolean squareHasFlags(int var1, byte var2) {
      byte var3 = this.squareFlags[var1];
      return (var3 & var2) == var2;
   }

   public byte squareGetFlags(int var1, int var2, int var3) {
      return this.squareGetFlags(this.getCoord1D(var1, var2, var3));
   }

   private byte squareGetFlags(int var1) {
      return this.squareFlags[var1];
   }

   protected void squareAddFlags(int var1, int var2, int var3, byte var4) {
      this.squareAddFlags(this.getCoord1D(var1, var2, var3), var4);
   }

   private void squareAddFlags(int var1, byte var2) {
      byte[] var10000 = this.squareFlags;
      var10000[var1] |= var2;
   }

   protected void squareRemoveFlags(int var1, int var2, int var3, byte var4) {
      this.squareRemoveFlags(this.getCoord1D(var1, var2, var3), var4);
   }

   private void squareRemoveFlags(int var1, byte var2) {
      byte[] var10000 = this.squareFlags;
      var10000[var1] ^= var2;
   }

   protected boolean squareCanConnect(int var1, int var2, int var3, byte var4) {
      return this.squareCanConnect(this.getCoord1D(var1, var2, var3), var3, var4);
   }

   private boolean squareCanConnect(int var1, int var2, byte var3) {
      if (var2 >= 0 && var2 < this.highestZ + 1) {
         if (var3 == 0) {
            return !this.squareHasFlags(var1, (byte)1);
         }

         if (var3 == 1) {
            return !this.squareHasFlags(var1, (byte)2);
         }

         if (var3 == 2) {
            return true;
         }

         if (var3 == 3) {
            return true;
         }

         if (var3 == 4) {
            return !this.squareHasFlags(var1, (byte)64);
         }

         if (var3 == 5) {
            return !this.squareHasFlags(var1, (byte)16);
         }
      }

      return false;
   }

   public IsoChunkRegion getIsoChunkRegion(int var1, int var2, int var3) {
      return this.getIsoChunkRegion(this.getCoord1D(var1, var2, var3), var3);
   }

   private IsoChunkRegion getIsoChunkRegion(int var1, int var2) {
      if (var2 >= 0 && var2 < this.highestZ + 1) {
         byte var3 = this.regionIDs[var1];
         if (var3 >= 0 && var3 < ((ArrayList)this.chunkRegions.get(var2)).size()) {
            return (IsoChunkRegion)((ArrayList)this.chunkRegions.get(var2)).get(var3);
         }
      }

      return null;
   }

   public void setRegion(int var1, int var2, int var3, byte var4) {
      this.regionIDs[this.getCoord1D(var1, var2, var3)] = var4;
   }

   protected void recalculate() {
      for(int var1 = 0; var1 <= this.highestZ; ++var1) {
         if (this.dirtyZLayers[var1] && this.activeZLayers[var1]) {
            this.recalculate(var1);
         }
      }

   }

   private void recalculate(int var1) {
      ArrayList var2 = (ArrayList)this.chunkRegions.get(var1);

      for(int var3 = var2.size() - 1; var3 >= 0; --var3) {
         IsoChunkRegion var4 = (IsoChunkRegion)var2.get(var3);
         IsoWorldRegion var5 = var4.unlinkFromIsoWorldRegion();
         if (var5 != null && var5.size() <= 0) {
            this.cell.dataRoot.regionManager.releaseIsoWorldRegion(var5);
         }

         this.cell.dataRoot.regionManager.releaseIsoChunkRegion(var4);
         var2.remove(var3);
      }

      var2.clear();
      byte var7 = 100;
      Arrays.fill(this.regionIDs, var1 * var7, var1 * var7 + var7, (byte)-1);

      for(int var8 = 0; var8 < 10; ++var8) {
         for(int var9 = 0; var9 < 10; ++var9) {
            if (this.regionIDs[this.getCoord1D(var9, var8, var1)] == -1) {
               this.floodFill(var9, var8, var1);
            }
         }
      }

   }

   private IsoChunkRegion floodFill(int var1, int var2, int var3) {
      IsoChunkRegion var4 = this.cell.dataRoot.regionManager.allocIsoChunkRegion(var3);
      byte var5 = (byte)((ArrayList)this.chunkRegions.get(var3)).size();
      ((ArrayList)this.chunkRegions.get(var3)).add(var4);
      this.clearExploredPositions();
      tmpSquares.clear();
      tmpLinkedChunks.clear();
      tmpSquares.add(DataSquarePos.alloc(var1, var2, var3));

      while(true) {
         DataSquarePos var6;
         int var8;
         do {
            if ((var6 = (DataSquarePos)tmpSquares.poll()) == null) {
               return var4;
            }

            var8 = this.getCoord1D(var6.x, var6.y, var6.z);
            this.setExploredPosition(var8, var6.z);
         } while(this.regionIDs[var8] != -1);

         this.regionIDs[var8] = var5;
         var4.addSquareCount();

         for(byte var10 = 0; var10 < 4; ++var10) {
            DataSquarePos var7 = this.getNeighbor(var6, var10);
            if (var7 != null) {
               int var9 = this.getCoord1D(var7.x, var7.y, var7.z);
               if (this.isExploredPosition(var9, var7.z)) {
                  DataSquarePos.release(var7);
               } else {
                  if (this.squareCanConnect(var8, var6.z, var10) && this.squareCanConnect(var9, var7.z, IsoRegions.GetOppositeDir(var10))) {
                     if (this.regionIDs[var9] == -1) {
                        tmpSquares.add(var7);
                        this.setExploredPosition(var9, var7.z);
                        continue;
                     }
                  } else {
                     IsoChunkRegion var11 = this.getIsoChunkRegion(var9, var7.z);
                     if (var11 != null && var11 != var4) {
                        if (!tmpLinkedChunks.contains(var11.getID())) {
                           var4.addNeighbor(var11);
                           var11.addNeighbor(var4);
                           tmpLinkedChunks.add(var11.getID());
                        }

                        this.setExploredPosition(var9, var7.z);
                        DataSquarePos.release(var7);
                        continue;
                     }
                  }

                  DataSquarePos.release(var7);
               }
            } else if (this.squareCanConnect(var8, var6.z, var10)) {
               var4.addChunkBorderSquaresCnt();
            }
         }
      }
   }

   private boolean isExploredPosition(int var1, int var2) {
      int var3 = var1 - var2 * 10 * 10;
      return exploredPositions[var3];
   }

   private void setExploredPosition(int var1, int var2) {
      int var3 = var1 - var2 * 10 * 10;
      exploredPositions[var3] = true;
   }

   private void clearExploredPositions() {
      Arrays.fill(exploredPositions, false);
   }

   private DataSquarePos getNeighbor(DataSquarePos var1, byte var2) {
      int var3 = var1.x;
      int var4 = var1.y;
      if (var2 == 1) {
         var3 = var1.x - 1;
      } else if (var2 == 3) {
         var3 = var1.x + 1;
      }

      if (var2 == 0) {
         var4 = var1.y - 1;
      } else if (var2 == 2) {
         var4 = var1.y + 1;
      }

      return var3 >= 0 && var3 < 10 && var4 >= 0 && var4 < 10 ? DataSquarePos.alloc(var3, var4, var1.z) : null;
   }

   protected void link(DataChunk var1, DataChunk var2, DataChunk var3, DataChunk var4) {
      for(int var5 = 0; var5 <= this.highestZ; ++var5) {
         if (this.dirtyZLayers[var5] && this.activeZLayers[var5]) {
            this.linkRegionsOnSide(var5, var1, (byte)0);
            this.linkRegionsOnSide(var5, var2, (byte)1);
            this.linkRegionsOnSide(var5, var3, (byte)2);
            this.linkRegionsOnSide(var5, var4, (byte)3);
         }
      }

   }

   private void linkRegionsOnSide(int var1, DataChunk var2, byte var3) {
      int var4;
      int var5;
      int var6;
      int var7;
      if (var3 != 0 && var3 != 2) {
         var4 = var3 == 1 ? 0 : 9;
         var5 = var4 + 1;
         var6 = 0;
         var7 = 10;
      } else {
         var4 = 0;
         var5 = 10;
         var6 = var3 == 0 ? 0 : 9;
         var7 = var6 + 1;
      }

      if (var2 != null && var2.isDirty(var1)) {
         var2.resetEnclosedSide(var1, IsoRegions.GetOppositeDir(var3));
      }

      lastCurRegion = null;
      lastOtherRegionFullConnect = null;

      for(int var14 = var6; var14 < var7; ++var14) {
         for(int var15 = var4; var15 < var5; ++var15) {
            int var8;
            int var9;
            if (var3 != 0 && var3 != 2) {
               var8 = var3 == 1 ? 9 : 0;
               var9 = var14;
            } else {
               var8 = var15;
               var9 = var3 == 0 ? 9 : 0;
            }

            int var10 = this.getCoord1D(var15, var14, var1);
            int var11 = this.getCoord1D(var8, var9, var1);
            IsoChunkRegion var12 = this.getIsoChunkRegion(var10, var1);
            IsoChunkRegion var13 = var2 != null ? var2.getIsoChunkRegion(var11, var1) : null;
            if (var12 == null) {
               IsoRegions.warn("ds.getRegion()==null, shouldnt happen at this point.");
            } else {
               if (lastCurRegion != null && lastCurRegion != var12) {
                  lastOtherRegionFullConnect = null;
               }

               if (lastCurRegion == null || lastCurRegion != var12 || var13 == null || lastOtherRegionFullConnect != var13) {
                  if (var2 != null && var13 != null) {
                     if (this.squareCanConnect(var10, var1, var3) && var2.squareCanConnect(var11, var1, IsoRegions.GetOppositeDir(var3))) {
                        var12.addConnectedNeighbor(var13);
                        var13.addConnectedNeighbor(var12);
                        var12.addNeighbor(var13);
                        var13.addNeighbor(var12);
                        if (!var13.getIsEnclosed()) {
                           var13.setEnclosed(IsoRegions.GetOppositeDir(var3), true);
                        }

                        lastOtherRegionFullConnect = var13;
                     } else {
                        var12.addNeighbor(var13);
                        var13.addNeighbor(var12);
                        if (!var13.getIsEnclosed()) {
                           var13.setEnclosed(IsoRegions.GetOppositeDir(var3), true);
                        }

                        lastOtherRegionFullConnect = null;
                     }
                  } else if (this.squareCanConnect(var10, var1, var3)) {
                     var12.setEnclosed(var3, false);
                  }

                  lastCurRegion = var12;
               }
            }
         }
      }

   }

   private void resetEnclosedSide(int var1, byte var2) {
      ArrayList var3 = (ArrayList)this.chunkRegions.get(var1);

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         IsoChunkRegion var5 = (IsoChunkRegion)var3.get(var4);
         if (var5.getzLayer() == var1) {
            var5.setEnclosed(var2, true);
         }
      }

   }

   protected void interConnect() {
      for(int var4 = 0; var4 <= this.highestZ; ++var4) {
         if (this.dirtyZLayers[var4] && this.activeZLayers[var4]) {
            ArrayList var3 = (ArrayList)this.chunkRegions.get(var4);

            for(int var5 = 0; var5 < var3.size(); ++var5) {
               IsoChunkRegion var1 = (IsoChunkRegion)var3.get(var5);
               if (var1.getzLayer() == var4 && var1.getIsoWorldRegion() == null) {
                  if (var1.getConnectedNeighbors().size() == 0) {
                     IsoWorldRegion var11 = this.cell.dataRoot.regionManager.allocIsoWorldRegion();
                     this.cell.dataRoot.EnqueueDirtyIsoWorldRegion(var11);
                     var11.addIsoChunkRegion(var1);
                  } else {
                     IsoChunkRegion var6 = var1.getConnectedNeighborWithLargestIsoWorldRegion();
                     IsoWorldRegion var7;
                     if (var6 == null) {
                        var7 = this.cell.dataRoot.regionManager.allocIsoWorldRegion();
                        this.cell.dataRoot.EnqueueDirtyIsoWorldRegion(var7);
                        this.floodFillExpandWorldRegion(var1, var7);
                        ++DataRoot.floodFills;
                     } else {
                        var7 = var6.getIsoWorldRegion();
                        oldList.clear();
                        oldList = var7.swapIsoChunkRegions(oldList);

                        IsoChunkRegion var2;
                        for(int var8 = 0; var8 < oldList.size(); ++var8) {
                           var2 = (IsoChunkRegion)oldList.get(var8);
                           var2.setIsoWorldRegion((IsoWorldRegion)null);
                        }

                        this.cell.dataRoot.regionManager.releaseIsoWorldRegion(var7);
                        IsoWorldRegion var12 = this.cell.dataRoot.regionManager.allocIsoWorldRegion();
                        this.cell.dataRoot.EnqueueDirtyIsoWorldRegion(var12);
                        this.floodFillExpandWorldRegion(var1, var12);

                        for(int var10 = 0; var10 < oldList.size(); ++var10) {
                           var2 = (IsoChunkRegion)oldList.get(var10);
                           if (var2.getIsoWorldRegion() == null) {
                              IsoWorldRegion var9 = this.cell.dataRoot.regionManager.allocIsoWorldRegion();
                              this.cell.dataRoot.EnqueueDirtyIsoWorldRegion(var9);
                              this.floodFillExpandWorldRegion(var2, var9);
                           }
                        }

                        ++DataRoot.floodFills;
                     }
                  }
               }
            }
         }
      }

   }

   private void floodFillExpandWorldRegion(IsoChunkRegion var1, IsoWorldRegion var2) {
      chunkQueue.add(var1);

      while(true) {
         IsoChunkRegion var3;
         do {
            if ((var3 = (IsoChunkRegion)chunkQueue.poll()) == null) {
               return;
            }

            var2.addIsoChunkRegion(var3);
         } while(var3.getConnectedNeighbors().size() == 0);

         for(int var5 = 0; var5 < var3.getConnectedNeighbors().size(); ++var5) {
            IsoChunkRegion var4 = (IsoChunkRegion)var3.getConnectedNeighbors().get(var5);
            if (!chunkQueue.contains(var4)) {
               if (var4.getIsoWorldRegion() == null) {
                  chunkQueue.add(var4);
               } else if (var4.getIsoWorldRegion() != var2) {
                  var2.merge(var4.getIsoWorldRegion());
               }
            }
         }
      }
   }

   protected void recalcRoofs() {
      if (this.highestZ >= 1) {
         int var1;
         for(var1 = 0; var1 < this.chunkRegions.size(); ++var1) {
            for(int var2 = 0; var2 < ((ArrayList)this.chunkRegions.get(var1)).size(); ++var2) {
               IsoChunkRegion var3 = (IsoChunkRegion)((ArrayList)this.chunkRegions.get(var1)).get(var2);
               var3.resetRoofCnt();
            }
         }

         var1 = this.highestZ;

         for(int var5 = 0; var5 < 10; ++var5) {
            for(int var6 = 0; var6 < 10; ++var6) {
               byte var8 = this.getSquare(var6, var5, var1);
               boolean var9 = false;
               if (var8 > 0) {
                  var9 = this.squareHasFlags(var6, var5, var1, (byte)16);
               }

               if (var1 >= 1) {
                  for(int var7 = var1 - 1; var7 >= 0; --var7) {
                     var8 = this.getSquare(var6, var5, var7);
                     if (var8 <= 0) {
                        var9 = false;
                     } else {
                        var9 = var9 || this.squareHasFlags(var6, var5, var7, (byte)32);
                        if (var9) {
                           IsoChunkRegion var4 = this.getIsoChunkRegion(var6, var5, var7);
                           if (var4 != null) {
                              var4.addRoof();
                              if (var4.getIsoWorldRegion() != null && !var4.getIsoWorldRegion().isEnclosed()) {
                                 var9 = false;
                              }
                           } else {
                              var9 = false;
                           }
                        }

                        if (!var9) {
                           var9 = this.squareHasFlags(var6, var5, var7, (byte)16);
                        }
                     }
                  }
               }
            }
         }

      }
   }
}
