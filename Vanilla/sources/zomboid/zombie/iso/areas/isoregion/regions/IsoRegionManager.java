package zombie.iso.areas.isoregion.regions;

import java.util.ArrayDeque;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.data.DataRoot;

public final class IsoRegionManager {
   private final ArrayDeque poolIsoWorldRegion = new ArrayDeque();
   private final ArrayDeque poolIsoChunkRegion = new ArrayDeque();
   private final DataRoot dataRoot;
   private final ArrayDeque regionIdStack = new ArrayDeque();
   private int nextID = 0;
   private int colorIndex = 0;
   private int worldRegionCount = 0;
   private int chunkRegionCount = 0;

   public IsoRegionManager(DataRoot var1) {
      this.dataRoot = var1;
   }

   public IsoWorldRegion allocIsoWorldRegion() {
      IsoWorldRegion var1 = !this.poolIsoWorldRegion.isEmpty() ? (IsoWorldRegion)this.poolIsoWorldRegion.pop() : new IsoWorldRegion(this);
      int var10000;
      if (this.regionIdStack.isEmpty()) {
         int var10002 = this.nextID;
         var10000 = var10002;
         this.nextID = var10002 + 1;
      } else {
         var10000 = (Integer)this.regionIdStack.pop();
      }

      int var2 = var10000;
      var1.init(var2);
      ++this.worldRegionCount;
      return var1;
   }

   public void releaseIsoWorldRegion(IsoWorldRegion var1) {
      this.dataRoot.DequeueDirtyIsoWorldRegion(var1);
      if (!var1.isInPool()) {
         this.regionIdStack.push(var1.getID());
         var1.reset();
         this.poolIsoWorldRegion.push(var1);
         --this.worldRegionCount;
      } else {
         IsoRegions.warn("IsoRegionManager -> Trying to release a MasterRegion twice.");
      }

   }

   public IsoChunkRegion allocIsoChunkRegion(int var1) {
      IsoChunkRegion var2 = !this.poolIsoChunkRegion.isEmpty() ? (IsoChunkRegion)this.poolIsoChunkRegion.pop() : new IsoChunkRegion(this);
      int var10000;
      if (this.regionIdStack.isEmpty()) {
         int var10002 = this.nextID;
         var10000 = var10002;
         this.nextID = var10002 + 1;
      } else {
         var10000 = (Integer)this.regionIdStack.pop();
      }

      int var3 = var10000;
      var2.init(var3, var1);
      ++this.chunkRegionCount;
      return var2;
   }

   public void releaseIsoChunkRegion(IsoChunkRegion var1) {
      if (!var1.isInPool()) {
         this.regionIdStack.push(var1.getID());
         var1.reset();
         this.poolIsoChunkRegion.push(var1);
         --this.chunkRegionCount;
      } else {
         IsoRegions.warn("IsoRegionManager -> Trying to release a ChunkRegion twice.");
      }

   }

   public Color getColor() {
      Color var1 = Colors.GetColorFromIndex(this.colorIndex++);
      if (this.colorIndex >= Colors.GetColorsCount()) {
         this.colorIndex = 0;
      }

      return var1;
   }

   public int getWorldRegionCount() {
      return this.worldRegionCount;
   }

   public int getChunkRegionCount() {
      return this.chunkRegionCount;
   }
}
