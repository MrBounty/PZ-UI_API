package zombie.iso.areas.isoregion.regions;

import java.util.ArrayList;
import zombie.core.Color;
import zombie.core.Core;
import zombie.iso.areas.isoregion.IsoRegions;

public final class IsoWorldRegion implements IWorldRegion {
   private final IsoRegionManager manager;
   private boolean isInPool = false;
   private int ID;
   private Color color;
   private boolean enclosed = true;
   private ArrayList isoChunkRegions = new ArrayList();
   private int squareSize = 0;
   private int roofCnt = 0;
   private boolean isDirtyEnclosed = false;
   private boolean isDirtyRoofed = false;
   private ArrayList neighbors = new ArrayList();

   public int getID() {
      return this.ID;
   }

   public Color getColor() {
      return this.color;
   }

   public int size() {
      return this.isoChunkRegions.size();
   }

   public int getSquareSize() {
      return this.squareSize;
   }

   protected boolean isInPool() {
      return this.isInPool;
   }

   protected IsoWorldRegion(IsoRegionManager var1) {
      this.manager = var1;
   }

   protected void init(int var1) {
      this.isInPool = false;
      this.ID = var1;
      if (this.color == null) {
         this.color = this.manager.getColor();
      }

      this.squareSize = 0;
      this.roofCnt = 0;
      this.enclosed = true;
      this.isDirtyEnclosed = false;
      this.isDirtyRoofed = false;
   }

   protected IsoWorldRegion reset() {
      this.isInPool = true;
      this.ID = -1;
      this.squareSize = 0;
      this.roofCnt = 0;
      this.enclosed = true;
      this.isDirtyRoofed = false;
      this.isDirtyEnclosed = false;
      this.unlinkNeighbors();
      if (this.isoChunkRegions.size() > 0) {
         if (Core.bDebug) {
            throw new RuntimeException("MasterRegion.reset Resetting master region which still has chunk regions");
         }

         IsoRegions.warn("MasterRegion.reset Resetting master region which still has chunk regions");

         for(int var2 = 0; var2 < this.isoChunkRegions.size(); ++var2) {
            IsoChunkRegion var1 = (IsoChunkRegion)this.isoChunkRegions.get(var2);
            var1.setIsoWorldRegion((IsoWorldRegion)null);
         }

         this.isoChunkRegions.clear();
      }

      return this;
   }

   public void unlinkNeighbors() {
      for(int var2 = 0; var2 < this.neighbors.size(); ++var2) {
         IsoWorldRegion var1 = (IsoWorldRegion)this.neighbors.get(var2);
         var1.removeNeighbor(this);
      }

      this.neighbors.clear();
   }

   public void linkNeighbors() {
      for(int var3 = 0; var3 < this.isoChunkRegions.size(); ++var3) {
         IsoChunkRegion var1 = (IsoChunkRegion)this.isoChunkRegions.get(var3);

         for(int var4 = 0; var4 < var1.getAllNeighbors().size(); ++var4) {
            IsoChunkRegion var2 = (IsoChunkRegion)var1.getAllNeighbors().get(var4);
            if (var2.getIsoWorldRegion() != null && var2.getIsoWorldRegion() != this) {
               this.addNeighbor(var2.getIsoWorldRegion());
               var2.getIsoWorldRegion().addNeighbor(this);
            }
         }
      }

   }

   private void addNeighbor(IsoWorldRegion var1) {
      if (!this.neighbors.contains(var1)) {
         this.neighbors.add(var1);
      }

   }

   private void removeNeighbor(IsoWorldRegion var1) {
      this.neighbors.remove(var1);
   }

   public ArrayList getNeighbors() {
      return this.neighbors;
   }

   public ArrayList getDebugConnectedNeighborCopy() {
      ArrayList var1 = new ArrayList();
      if (this.neighbors.size() == 0) {
         return var1;
      } else {
         var1.addAll(this.neighbors);
         return var1;
      }
   }

   public boolean isFogMask() {
      return this.isEnclosed() && this.isFullyRoofed();
   }

   public boolean isPlayerRoom() {
      return this.isFogMask();
   }

   public boolean isFullyRoofed() {
      return this.roofCnt == this.squareSize;
   }

   public float getRoofedPercentage() {
      return this.squareSize == 0 ? 0.0F : (float)this.roofCnt / (float)this.squareSize;
   }

   public int getRoofCnt() {
      return this.roofCnt;
   }

   protected void addRoof() {
      ++this.roofCnt;
      if (this.roofCnt > this.squareSize) {
         IsoRegions.warn("WorldRegion.addRoof roofCount exceed squareSize.");
         this.roofCnt = this.squareSize;
      }

   }

   protected void removeRoofs(int var1) {
      if (var1 > 0) {
         this.roofCnt -= var1;
         if (this.roofCnt < 0) {
            IsoRegions.warn("MasterRegion.removeRoofs Roofcount managed to get below zero.");
            this.roofCnt = 0;
         }

      }
   }

   public void addIsoChunkRegion(IsoChunkRegion var1) {
      if (!this.isoChunkRegions.contains(var1)) {
         this.squareSize += var1.getSquareSize();
         this.roofCnt += var1.getRoofCnt();
         this.isDirtyEnclosed = true;
         this.isoChunkRegions.add(var1);
         var1.setIsoWorldRegion(this);
      }

   }

   protected void removeIsoChunkRegion(IsoChunkRegion var1) {
      if (this.isoChunkRegions.remove(var1)) {
         this.squareSize -= var1.getSquareSize();
         this.roofCnt -= var1.getRoofCnt();
         this.isDirtyEnclosed = true;
         var1.setIsoWorldRegion((IsoWorldRegion)null);
      }

   }

   public boolean containsIsoChunkRegion(IsoChunkRegion var1) {
      return this.isoChunkRegions.contains(var1);
   }

   public ArrayList swapIsoChunkRegions(ArrayList var1) {
      ArrayList var2 = this.isoChunkRegions;
      this.isoChunkRegions = var1;
      return var2;
   }

   protected void resetSquareSize() {
      this.squareSize = 0;
   }

   protected void setDirtyEnclosed() {
      this.isDirtyEnclosed = true;
   }

   public boolean isEnclosed() {
      if (this.isDirtyEnclosed) {
         this.recalcEnclosed();
      }

      return this.enclosed;
   }

   private void recalcEnclosed() {
      this.isDirtyEnclosed = false;
      this.enclosed = true;

      for(int var2 = 0; var2 < this.isoChunkRegions.size(); ++var2) {
         IsoChunkRegion var1 = (IsoChunkRegion)this.isoChunkRegions.get(var2);
         if (!var1.getIsEnclosed()) {
            this.enclosed = false;
         }
      }

   }

   public void merge(IsoWorldRegion var1) {
      int var2;
      if (var1.isoChunkRegions.size() > 0) {
         for(var2 = var1.isoChunkRegions.size() - 1; var2 >= 0; --var2) {
            IsoChunkRegion var3 = (IsoChunkRegion)var1.isoChunkRegions.get(var2);
            var1.removeIsoChunkRegion(var3);
            this.addIsoChunkRegion(var3);
         }

         this.isDirtyEnclosed = true;
         var1.isoChunkRegions.clear();
      }

      if (var1.neighbors.size() > 0) {
         for(var2 = 0; var2 < var1.neighbors.size(); ++var2) {
            IsoWorldRegion var4 = (IsoWorldRegion)var1.neighbors.get(var2);
            var4.removeNeighbor(var1);
            this.addNeighbor(var4);
         }

         var1.neighbors.clear();
      }

      this.manager.releaseIsoWorldRegion(var1);
   }

   public ArrayList getDebugIsoChunkRegionCopy() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.isoChunkRegions);
      return var1;
   }
}
