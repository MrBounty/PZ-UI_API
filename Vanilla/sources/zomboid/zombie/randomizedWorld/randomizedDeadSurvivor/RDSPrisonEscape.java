package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;

public final class RDSPrisonEscape extends RandomizedDeadSurvivorBase {
   public RDSPrisonEscape() {
      this.name = "Prison Escape";
      this.setChance(3);
      this.setMaximumDays(90);
      this.setUnique(true);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      this.addZombies(var1, Rand.Next(2, 4), "InmateEscaped", 0, var2);
   }
}
