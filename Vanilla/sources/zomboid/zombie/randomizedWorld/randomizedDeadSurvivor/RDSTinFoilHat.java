package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;

public final class RDSTinFoilHat extends RandomizedDeadSurvivorBase {
   public RDSTinFoilHat() {
      this.name = "Tin foil hat family";
      this.setUnique(true);
      this.setChance(2);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      this.addZombies(var1, Rand.Next(2, 5), "TinFoilHat", (Integer)null, var2);
   }
}
