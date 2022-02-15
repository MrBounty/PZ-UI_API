package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.VirtualZombieManager;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSZombiesEating extends RandomizedDeadSurvivorBase {
   public RDSZombiesEating() {
      this.name = "Eating zombies";
      this.setChance(7);
      this.setMaximumDays(60);
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return IsoWorld.getZombiesEnabled() && super.isValid(var1, var2);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      IsoDeadBody var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2, Rand.Next(5, 10));
      if (var3 != null) {
         VirtualZombieManager.instance.createEatingZombies(var3, Rand.Next(1, 3));
         RoomDef var4 = this.getRoom(var1, "kitchen");
         RoomDef var5 = this.getRoom(var1, "livingroom");
         if ("kitchen".equals(var2.name) && var5 != null && Rand.Next(3) == 0) {
            var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var5, Rand.Next(5, 10));
            if (var3 == null) {
               return;
            }

            VirtualZombieManager.instance.createEatingZombies(var3, Rand.Next(1, 3));
         }

         if ("livingroom".equals(var2.name) && var4 != null && Rand.Next(3) == 0) {
            var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var4, Rand.Next(5, 10));
            if (var3 == null) {
               return;
            }

            VirtualZombieManager.instance.createEatingZombies(var3, Rand.Next(1, 3));
         }

      }
   }
}
