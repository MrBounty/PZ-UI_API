package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSBurntCar extends RandomizedVehicleStoryBase {
   public RVSBurntCar() {
      this.name = "Burnt Car";
      this.minZoneWidth = 2;
      this.minZoneHeight = 5;
      this.setChance(13);
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      this.callVehicleStorySpawner(var1, var2, 0.0F);
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      VehicleStorySpawner var4 = VehicleStorySpawner.getInstance();
      var4.clear();
      Vector2 var5 = IsoDirections.N.ToVector();
      float var6 = 0.5235988F;
      if (var3) {
         var6 = 0.0F;
      }

      var5.rotate(Rand.Next(-var6, var6));
      var4.addElement("vehicle1", 0.0F, 0.0F, var5.getDirection(), 2.0F, 5.0F);
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
         case 2014205573:
            if (var6.equals("vehicle1")) {
               var7 = 0;
            }
         default:
            switch(var7) {
            case 0:
               BaseVehicle var8 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, (String)null, "Base.CarNormal", (Integer)null, (String)null);
               if (var8 != null) {
                  var8 = var8.setSmashed("right");
               }
            default:
            }
         }
      }
   }
}
