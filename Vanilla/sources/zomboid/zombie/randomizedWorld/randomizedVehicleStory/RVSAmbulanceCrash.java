package zombie.randomizedWorld.randomizedVehicleStory;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSAmbulanceCrash extends RandomizedVehicleStoryBase {
   public RVSAmbulanceCrash() {
      this.name = "Ambulance Crash";
      this.minZoneWidth = 5;
      this.minZoneHeight = 7;
      this.setChance(5);
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
            break;
         case 2014205574:
            if (var6.equals("vehicle2")) {
               var7 = 1;
            }
         }

         BaseVehicle var8;
         switch(var7) {
         case 0:
            var8 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, (String)null, "Base.VanAmbulance", (Integer)null, (String)null);
            if (var8 != null) {
               this.addZombiesOnVehicle(Rand.Next(1, 3), "AmbulanceDriver", (Integer)null, var8);
               ArrayList var9 = this.addZombiesOnVehicle(Rand.Next(1, 3), "HospitalPatient", (Integer)null, var8);

               for(int var10 = 0; var10 < var9.size(); ++var10) {
                  for(int var11 = 0; var11 < 7; ++var11) {
                     if (Rand.NextBool(2)) {
                        ((IsoZombie)var9.get(var10)).addVisualBandage(BodyPartType.getRandom(), true);
                     }
                  }
               }
            }
            break;
         case 1:
            var8 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, "bad", (String)null, (Integer)null, (String)null);
            if (var8 == null) {
            }
         }

      }
   }
}
