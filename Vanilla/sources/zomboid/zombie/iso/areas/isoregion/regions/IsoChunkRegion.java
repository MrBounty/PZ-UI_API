package zombie.iso.areas.isoregion.regions;

import java.util.ArrayList;
import zombie.core.Color;
import zombie.core.Core;
import zombie.iso.areas.isoregion.IsoRegions;

public final class IsoChunkRegion implements IChunkRegion {
   private final IsoRegionManager manager;
   private boolean isInPool = false;
   private Color color;
   private int ID;
   private byte zLayer;
   private byte squareSize = 0;
   private byte roofCnt = 0;
   private byte chunkBorderSquaresCnt = 0;
   private final boolean[] enclosed = new boolean[4];
   private boolean enclosedCache = true;
   private final ArrayList connectedNeighbors = new ArrayList();
   private final ArrayList allNeighbors = new ArrayList();
   private boolean isDirtyEnclosed = false;
   private IsoWorldRegion isoWorldRegion;

   public int getID() {
      return this.ID;
   }

   public int getSquareSize() {
      return this.squareSize;
   }

   public Color getColor() {
      return this.color;
   }

   public int getzLayer() {
      return this.zLayer;
   }

   public IsoWorldRegion getIsoWorldRegion() {
      return this.isoWorldRegion;
   }

   public void setIsoWorldRegion(IsoWorldRegion var1) {
      this.isoWorldRegion = var1;
   }

   protected boolean isInPool() {
      return this.isInPool;
   }

   protected IsoChunkRegion(IsoRegionManager var1) {
      this.manager = var1;
   }

   protected void init(int var1, int var2) {
      this.isInPool = false;
      this.ID = var1;
      this.zLayer = (byte)var2;
      this.resetChunkBorderSquaresCnt();
      if (this.color == null) {
         this.color = this.manager.getColor();
      }

      this.squareSize = 0;
      this.roofCnt = 0;
      this.resetEnclosed();
   }

   protected IsoChunkRegion reset() {
      this.isInPool = true;
      this.unlinkNeighbors();
      IsoWorldRegion var1 = this.unlinkFromIsoWorldRegion();
      if (var1 != null && var1.size() <= 0) {
         if (Core.bDebug) {
            throw new RuntimeException("ChunkRegion.reset IsoChunkRegion has IsoWorldRegion with 0 members.");
         }

         this.manager.releaseIsoWorldRegion(var1);
         IsoRegions.warn("ChunkRegion.reset IsoChunkRegion has IsoWorldRegion with 0 members.");
      }

      this.resetChunkBorderSquaresCnt();
      this.ID = -1;
      this.squareSize = 0;
      this.roofCnt = 0;
      this.resetEnclosed();
      return this;
   }

   public IsoWorldRegion unlinkFromIsoWorldRegion() {
      if (this.isoWorldRegion != null) {
         IsoWorldRegion var1 = this.isoWorldRegion;
         this.isoWorldRegion.removeIsoChunkRegion(this);
         this.isoWorldRegion = null;
         return var1;
      } else {
         return null;
      }
   }

   public int getRoofCnt() {
      return this.roofCnt;
   }

   public void addRoof() {
      ++this.roofCnt;
      if (this.roofCnt > this.squareSize) {
         IsoRegions.warn("ChunkRegion.addRoof roofCount exceed squareSize.");
         this.roofCnt = this.squareSize;
      } else {
         if (this.isoWorldRegion != null) {
            this.isoWorldRegion.addRoof();
         }

      }
   }

   public void resetRoofCnt() {
      if (this.isoWorldRegion != null) {
         this.isoWorldRegion.removeRoofs(this.roofCnt);
      }

      this.roofCnt = 0;
   }

   public void addSquareCount() {
      ++this.squareSize;
   }

   public int getChunkBorderSquaresCnt() {
      return this.chunkBorderSquaresCnt;
   }

   public void addChunkBorderSquaresCnt() {
      ++this.chunkBorderSquaresCnt;
   }

   protected void removeChunkBorderSquaresCnt() {
      --this.chunkBorderSquaresCnt;
      if (this.chunkBorderSquaresCnt < 0) {
         this.chunkBorderSquaresCnt = 0;
      }

   }

   protected void resetChunkBorderSquaresCnt() {
      this.chunkBorderSquaresCnt = 0;
   }

   private void resetEnclosed() {
      for(byte var1 = 0; var1 < 4; ++var1) {
         this.enclosed[var1] = true;
      }

      this.isDirtyEnclosed = false;
      this.enclosedCache = true;
   }

   public void setEnclosed(byte var1, boolean var2) {
      this.isDirtyEnclosed = true;
      this.enclosed[var1] = var2;
   }

   protected void setDirtyEnclosed() {
      this.isDirtyEnclosed = true;
      if (this.isoWorldRegion != null) {
         this.isoWorldRegion.setDirtyEnclosed();
      }

   }

   public boolean getIsEnclosed() {
      if (!this.isDirtyEnclosed) {
         return this.enclosedCache;
      } else {
         this.isDirtyEnclosed = false;
         this.enclosedCache = true;

         for(byte var1 = 0; var1 < 4; ++var1) {
            if (!this.enclosed[var1]) {
               this.enclosedCache = false;
            }
         }

         if (this.isoWorldRegion != null) {
            this.isoWorldRegion.setDirtyEnclosed();
         }

         return this.enclosedCache;
      }
   }

   public ArrayList getConnectedNeighbors() {
      return this.connectedNeighbors;
   }

   public void addConnectedNeighbor(IsoChunkRegion var1) {
      if (var1 != null) {
         if (!this.connectedNeighbors.contains(var1)) {
            this.connectedNeighbors.add(var1);
         }

      }
   }

   protected void removeConnectedNeighbor(IsoChunkRegion var1) {
      this.connectedNeighbors.remove(var1);
   }

   public int getNeighborCount() {
      return this.allNeighbors.size();
   }

   protected ArrayList getAllNeighbors() {
      return this.allNeighbors;
   }

   public void addNeighbor(IsoChunkRegion var1) {
      if (var1 != null) {
         if (!this.allNeighbors.contains(var1)) {
            this.allNeighbors.add(var1);
         }

      }
   }

   protected void removeNeighbor(IsoChunkRegion var1) {
      this.allNeighbors.remove(var1);
   }

   protected void unlinkNeighbors() {
      IsoChunkRegion var1;
      int var2;
      for(var2 = 0; var2 < this.connectedNeighbors.size(); ++var2) {
         var1 = (IsoChunkRegion)this.connectedNeighbors.get(var2);
         var1.removeConnectedNeighbor(this);
      }

      this.connectedNeighbors.clear();

      for(var2 = 0; var2 < this.allNeighbors.size(); ++var2) {
         var1 = (IsoChunkRegion)this.allNeighbors.get(var2);
         var1.removeNeighbor(this);
      }

      this.allNeighbors.clear();
   }

   public ArrayList getDebugConnectedNeighborCopy() {
      ArrayList var1 = new ArrayList();
      if (this.connectedNeighbors.size() == 0) {
         return var1;
      } else {
         var1.addAll(this.connectedNeighbors);
         return var1;
      }
   }

   public boolean containsConnectedNeighbor(IsoChunkRegion var1) {
      return this.connectedNeighbors.contains(var1);
   }

   public boolean containsConnectedNeighborID(int var1) {
      if (this.connectedNeighbors.size() == 0) {
         return false;
      } else {
         for(int var3 = 0; var3 < this.connectedNeighbors.size(); ++var3) {
            IsoChunkRegion var2 = (IsoChunkRegion)this.connectedNeighbors.get(var3);
            if (var2.getID() == var1) {
               return true;
            }
         }

         return false;
      }
   }

   public IsoChunkRegion getConnectedNeighborWithLargestIsoWorldRegion() {
      if (this.connectedNeighbors.size() == 0) {
         return null;
      } else {
         IsoWorldRegion var1 = null;
         IsoChunkRegion var2 = null;

         for(int var4 = 0; var4 < this.connectedNeighbors.size(); ++var4) {
            IsoChunkRegion var3 = (IsoChunkRegion)this.connectedNeighbors.get(var4);
            IsoWorldRegion var5 = var3.getIsoWorldRegion();
            if (var5 != null && (var1 == null || var5.getSquareSize() > var1.getSquareSize())) {
               var1 = var5;
               var2 = var3;
            }
         }

         return var2;
      }
   }

   protected IsoChunkRegion getFirstNeighborWithIsoWorldRegion() {
      if (this.connectedNeighbors.size() == 0) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.connectedNeighbors.size(); ++var2) {
            IsoChunkRegion var1 = (IsoChunkRegion)this.connectedNeighbors.get(var2);
            IsoWorldRegion var3 = var1.getIsoWorldRegion();
            if (var3 != null) {
               return var1;
            }
         }

         return null;
      }
   }
}
