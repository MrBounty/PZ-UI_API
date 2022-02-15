package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSCarCrashCorpse extends RandomizedVehicleStoryBase {
   public RVSCarCrashCorpse() {
      this.name = "Basic Car Crash Corpse";
      this.minZoneWidth = 6;
      this.minZoneHeight = 11;
      this.setChance(10);
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      float var3 = 0.5235988F;
      this.callVehicleStorySpawner(var1, var2, Rand.Next(-var3, var3));
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      VehicleStorySpawner var4 = VehicleStorySpawner.getInstance();
      var4.clear();
      Vector2 var5 = IsoDirections.N.ToVector();
      float var6 = 2.5F;
      var4.addElement("vehicle1", 0.0F, var6, var5.getDirection(), 2.0F, 5.0F);
      var4.addElement("corpse", 0.0F, var6 - (float)(var3 ? 7 : Rand.Next(4, 7)), var5.getDirection() + 3.1415927F, 1.0F, 2.0F);
      var4.setParameter("zone", var1);
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         BaseVehicle var6 = (BaseVehicle)var1.getParameter("vehicle1", BaseVehicle.class);
         String var7 = var2.id;
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -1354663044:
            if (var7.equals("corpse")) {
               var8 = 0;
            }
            break;
         case 2014205573:
            if (var7.equals("vehicle1")) {
               var8 = 1;
            }
         }

         switch(var8) {
         case 0:
            if (var6 != null) {
               createRandomDeadBody(var2.position.x, var2.position.y, var2.z, var2.direction, false, 35, 30, (String)null);
               this.addTrailOfBlood(var2.position.x, var2.position.y, var2.z, var2.direction, 15);
            }
            break;
         case 1:
            var6 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, "bad", (String)null, (Integer)null, (String)null);
            if (var6 != null) {
               var6 = var6.setSmashed("Front");
               var6.setBloodIntensity("Front", 1.0F);
               var1.setParameter("vehicle1", var6);
            }
         }

      }
   }
}
