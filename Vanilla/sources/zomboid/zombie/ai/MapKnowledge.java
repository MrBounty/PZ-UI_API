package zombie.ai;

import java.util.ArrayList;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;

public final class MapKnowledge {
   private final ArrayList knownBlockedEdges = new ArrayList();

   public ArrayList getKnownBlockedEdges() {
      return this.knownBlockedEdges;
   }

   public KnownBlockedEdges getKnownBlockedEdges(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.knownBlockedEdges.size(); ++var4) {
         KnownBlockedEdges var5 = (KnownBlockedEdges)this.knownBlockedEdges.get(var4);
         if (var5.x == var1 && var5.y == var2 && var5.z == var3) {
            return var5;
         }
      }

      return null;
   }

   private KnownBlockedEdges createKnownBlockedEdges(int var1, int var2, int var3) {
      assert this.getKnownBlockedEdges(var1, var2, var3) == null;

      KnownBlockedEdges var4 = KnownBlockedEdges.alloc();
      var4.init(var1, var2, var3);
      this.knownBlockedEdges.add(var4);
      return var4;
   }

   public KnownBlockedEdges getOrCreateKnownBlockedEdges(int var1, int var2, int var3) {
      KnownBlockedEdges var4 = this.getKnownBlockedEdges(var1, var2, var3);
      if (var4 == null) {
         var4 = this.createKnownBlockedEdges(var1, var2, var3);
      }

      return var4;
   }

   private void releaseIfEmpty(KnownBlockedEdges var1) {
      if (!var1.n && !var1.w) {
         this.knownBlockedEdges.remove(var1);
         var1.release();
      }

   }

   public void setKnownBlockedEdgeW(int var1, int var2, int var3, boolean var4) {
      KnownBlockedEdges var5 = this.getOrCreateKnownBlockedEdges(var1, var2, var3);
      var5.w = var4;
      this.releaseIfEmpty(var5);
   }

   public void setKnownBlockedEdgeN(int var1, int var2, int var3, boolean var4) {
      KnownBlockedEdges var5 = this.getOrCreateKnownBlockedEdges(var1, var2, var3);
      var5.n = var4;
      this.releaseIfEmpty(var5);
   }

   public void setKnownBlockedDoor(IsoDoor var1, boolean var2) {
      IsoGridSquare var3 = var1.getSquare();
      if (var1.getNorth()) {
         this.setKnownBlockedEdgeN(var3.x, var3.y, var3.z, var2);
      } else {
         this.setKnownBlockedEdgeW(var3.x, var3.y, var3.z, var2);
      }

   }

   public void setKnownBlockedDoor(IsoThumpable var1, boolean var2) {
      if (var1.isDoor()) {
         IsoGridSquare var3 = var1.getSquare();
         if (var1.getNorth()) {
            this.setKnownBlockedEdgeN(var3.x, var3.y, var3.z, var2);
         } else {
            this.setKnownBlockedEdgeW(var3.x, var3.y, var3.z, var2);
         }

      }
   }

   public void setKnownBlockedWindow(IsoWindow var1, boolean var2) {
      IsoGridSquare var3 = var1.getSquare();
      if (var1.getNorth()) {
         this.setKnownBlockedEdgeN(var3.x, var3.y, var3.z, var2);
      } else {
         this.setKnownBlockedEdgeW(var3.x, var3.y, var3.z, var2);
      }

   }

   public void setKnownBlockedWindowFrame(IsoObject var1, boolean var2) {
      IsoGridSquare var3 = var1.getSquare();
      if (IsoWindowFrame.isWindowFrame(var1, true)) {
         this.setKnownBlockedEdgeN(var3.x, var3.y, var3.z, var2);
      } else if (IsoWindowFrame.isWindowFrame(var1, false)) {
         this.setKnownBlockedEdgeW(var3.x, var3.y, var3.z, var2);
      }

   }

   public void forget() {
      KnownBlockedEdges.releaseAll(this.knownBlockedEdges);
      this.knownBlockedEdges.clear();
   }
}
