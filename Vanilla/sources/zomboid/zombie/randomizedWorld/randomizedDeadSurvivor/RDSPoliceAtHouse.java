package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.RoomDef;
import zombie.vehicles.BaseVehicle;

public final class RDSPoliceAtHouse extends RandomizedDeadSurvivorBase {
   public RDSPoliceAtHouse() {
      this.name = "Police at House";
      this.setChance(4);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      this.addZombies(var1, Rand.Next(2, 4), (String)null, 0, var2);
      ArrayList var3 = this.addZombies(var1, Rand.Next(1, 3), "Police", (Integer)null, var2);
      BaseVehicle var4 = this.spawnCarOnNearestNav("Base.CarLightsPolice", var1);
      if (var4 != null) {
         IsoGridSquare var5 = var4.getSquare().getCell().getGridSquare(var4.getSquare().x - 2, var4.getSquare().y - 2, 0);
         ArrayList var6 = this.addZombiesOnSquare(2, "Police", (Integer)null, var5);
         createRandomDeadBody(var2, Rand.Next(7, 10));
         createRandomDeadBody(var2, Rand.Next(7, 10));
         if (!var3.isEmpty()) {
            var3.addAll(var6);
            ((IsoZombie)var3.get(Rand.Next(var3.size()))).addItemToSpawnAtDeath(var4.createVehicleKey());
         }
      }
   }
}
