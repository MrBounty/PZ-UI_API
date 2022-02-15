package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSBanditRoad extends RandomizedVehicleStoryBase {
   public RVSBanditRoad() {
      this.name = "Bandits on Road";
      this.minZoneWidth = 7;
      this.minZoneHeight = 9;
      this.setMinimumDays(30);
      this.setChance(3);
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      this.callVehicleStorySpawner(var1, var2, 0.0F);
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      VehicleStorySpawner var4 = VehicleStorySpawner.getInstance();
      var4.clear();
      float var5 = 0.5235988F;
      if (var3) {
         var5 = 0.0F;
      }

      Vector2 var6 = IsoDirections.N.ToVector();
      var6.rotate(Rand.Next(-var5, var5));
      var4.addElement("vehicle1", 0.0F, 2.0F, var6.getDirection(), 2.0F, 5.0F);
      boolean var7 = Rand.NextBool(2);
      var6 = var7 ? IsoDirections.E.ToVector() : IsoDirections.W.ToVector();
      var6.rotate(Rand.Next(-var5, var5));
      float var8 = 0.0F;
      float var9 = -1.5F;
      var4.addElement("vehicle2", var8, var9, var6.getDirection(), 2.0F, 5.0F);
      int var10 = Rand.Next(3, 6);

      for(int var11 = 0; var11 < var10; ++var11) {
         float var12 = Rand.Next(var8 - 3.0F, var8 + 3.0F);
         float var13 = Rand.Next(var9 - 3.0F, var9 + 3.0F);
         var4.addElement("corpse", var12, var13, Rand.Next(0.0F, 6.2831855F), 1.0F, 2.0F);
      }

      var4.setParameter("zone", var1);
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         String var6 = var2.id;
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -1354663044:
            if (var6.equals("corpse")) {
               var7 = 0;
            }
            break;
         case 2014205573:
            if (var6.equals("vehicle1")) {
               var7 = 1;
            }
            break;
         case 2014205574:
            if (var6.equals("vehicle2")) {
               var7 = 2;
            }
         }

         BaseVehicle var8;
         switch(var7) {
         case 0:
            var8 = (BaseVehicle)var1.getParameter("vehicle1", BaseVehicle.class);
            if (var8 != null) {
               createRandomDeadBody(var2.position.x, var2.position.y, var2.z, var2.direction, false, 6, 0, (String)null);
               this.addTrailOfBlood(var2.position.x, var2.position.y, var2.z, Vector2.getDirection(var2.position.x - var8.x, var2.position.y - var8.y), 15);
            }
            break;
         case 1:
            var8 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, "bad", (String)null, (Integer)null, (String)null);
            if (var8 != null) {
               var8 = var8.setSmashed("Front");
               this.addZombiesOnVehicle(Rand.Next(3, 6), "Bandit", (Integer)null, var8);
               var1.setParameter("vehicle1", var8);
            }
            break;
         case 2:
            var8 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, "bad", (String)null, (Integer)null, (String)null);
            if (var8 != null) {
               this.addZombiesOnVehicle(Rand.Next(3, 5), (String)null, (Integer)null, var8);
               var1.setParameter("vehicle2", var8);
            }
         }

      }
   }
}
