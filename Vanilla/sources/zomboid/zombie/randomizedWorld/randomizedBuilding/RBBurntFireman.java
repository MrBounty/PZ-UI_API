package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.vehicles.BaseVehicle;

public final class RBBurntFireman extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      int var2 = Rand.Next(1, 4);
      var1.setHasBeenVisited(true);
      IsoCell var3 = IsoWorld.instance.CurrentCell;

      int var5;
      for(int var4 = var1.x - 1; var4 < var1.x2 + 1; ++var4) {
         for(var5 = var1.y - 1; var5 < var1.y2 + 1; ++var5) {
            for(int var6 = 0; var6 < 8; ++var6) {
               IsoGridSquare var7 = var3.getGridSquare(var4, var5, var6);
               if (var7 != null && Rand.Next(100) < 70) {
                  var7.Burn(false);
               }
            }
         }
      }

      var1.setAllExplored(true);
      ArrayList var8 = this.addZombies(var1, var2, "FiremanFullSuit", 35, this.getLivingRoomOrKitchen(var1));

      for(var5 = 0; var5 < var8.size(); ++var5) {
         ((IsoZombie)var8.get(var5)).getInventory().setExplored(true);
      }

      BaseVehicle var9;
      if (Rand.NextBool(2)) {
         var9 = this.spawnCarOnNearestNav("Base.PickUpVanLightsFire", var1);
      } else {
         var9 = this.spawnCarOnNearestNav("Base.PickUpTruckLightsFire", var1);
      }

      if (var9 != null && !var8.isEmpty()) {
         ((IsoZombie)var8.get(Rand.Next(var8.size()))).addItemToSpawnAtDeath(var9.createVehicleKey());
      }

   }

   public RBBurntFireman() {
      this.name = "Burnt Fireman";
      this.setChance(2);
   }
}
