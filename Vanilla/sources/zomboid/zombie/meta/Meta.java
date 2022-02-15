package zombie.meta;

import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.areas.SafeHouse;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class Meta {
   public static final Meta instance = new Meta();
   final ArrayList SquaresProcessing = new ArrayList();
   private final ArrayList SquaresSeen = new ArrayList(2000);
   private final TIntHashSet SquaresSeenSet = new TIntHashSet();

   public void dealWithSquareSeen(IsoGridSquare var1) {
      if (!GameClient.bClient) {
         if (var1.hourLastSeen != (int)GameTime.getInstance().getWorldAgeHours()) {
            synchronized(this.SquaresSeen) {
               if (!this.SquaresSeenSet.contains(var1.getID())) {
                  this.SquaresSeen.add(var1);
                  this.SquaresSeenSet.add(var1.getID());
               }

            }
         }
      }
   }

   public void dealWithSquareSeenActual(IsoGridSquare var1) {
      if (!GameClient.bClient) {
         IsoMetaGrid.Zone var2 = var1.zone;
         if (var2 != null) {
            var2.setHourSeenToCurrent();
         }

         if (GameServer.bServer) {
            SafeHouse var3 = SafeHouse.getSafeHouse(var1);
            if (var3 != null) {
               var3.updateSafehouse((IsoPlayer)null);
            }
         }

         var1.setHourSeenToCurrent();
      }
   }

   public void update() {
      if (!GameClient.bClient) {
         this.SquaresProcessing.clear();
         synchronized(this.SquaresSeen) {
            this.SquaresProcessing.addAll(this.SquaresSeen);
            this.SquaresSeen.clear();
            this.SquaresSeenSet.clear();
         }

         for(int var1 = 0; var1 < this.SquaresProcessing.size(); ++var1) {
            this.dealWithSquareSeenActual((IsoGridSquare)this.SquaresProcessing.get(var1));
         }

         this.SquaresProcessing.clear();
      }
   }
}
