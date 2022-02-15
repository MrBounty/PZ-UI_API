package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSCarCrash extends RandomizedVehicleStoryBase {
   public RVSCarCrash() {
      this.name = "Basic Car Crash";
      this.minZoneWidth = 5;
      this.minZoneHeight = 7;
      this.setChance(25);
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
      var4.addElement("vehicle1", 0.0F, 1.0F, var6.getDirection(), 2.0F, 5.0F);
      boolean var7 = Rand.NextBool(2);
      var6 = var7 ? IsoDirections.E.ToVector() : IsoDirections.W.ToVector();
      var6.rotate(Rand.Next(-var5, var5));
      var4.addElement("vehicle2", 0.0F, -2.5F, var6.getDirection(), 2.0F, 5.0F);
      var4.setParameter("zone", var1);
      var4.setParameter("smashed", Rand.NextBool(3));
      var4.setParameter("east", var7);
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         boolean var6 = var1.getParameterBoolean("smashed");
         boolean var7 = var1.getParameterBoolean("east");
         String var8 = var2.id;
         byte var9 = -1;
         switch(var8.hashCode()) {
         case 2014205573:
            if (var8.equals("vehicle1")) {
               var9 = 0;
            }
            break;
         case 2014205574:
            if (var8.equals("vehicle2")) {
               var9 = 1;
            }
         }

         switch(var9) {
         case 0:
         case 1:
            BaseVehicle var10 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, "bad", (String)null, (Integer)null, (String)null);
            if (var10 != null) {
               if (var6) {
                  String var11 = "Front";
                  if ("vehicle2".equals(var2.id)) {
                     var11 = var7 ? "Right" : "Left";
                  }

                  var10 = var10.setSmashed(var11);
                  var10.setBloodIntensity(var11, 1.0F);
               }

               if ("vehicle1".equals(var2.id) && Rand.Next(10) < 4) {
                  this.addZombiesOnVehicle(Rand.Next(2, 5), (String)null, (Integer)null, var10);
               }
            }
         default:
         }
      }
   }
}
