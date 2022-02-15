package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.iso.BuildingDef;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoDeadBody;

public final class RDSGunslinger extends RandomizedDeadSurvivorBase {
   public void randomizeDeadSurvivor(BuildingDef var1) {
      IsoGridSquare var2 = var1.getFreeSquareInRoom();
      if (var2 != null) {
         IsoDeadBody var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2.getX(), var2.getY(), var2.getZ(), (IsoDirections)null, 0);
         if (var3 != null) {
            var3.setPrimaryHandItem(super.addRandomRangedWeapon(var3.getContainer(), true, false, false));
            int var4 = Rand.Next(1, 4);

            for(int var5 = 0; var5 < var4; ++var5) {
               var3.getContainer().AddItem((InventoryItem)super.addRandomRangedWeapon(var3.getContainer(), true, true, true));
            }

         }
      }
   }

   public RDSGunslinger() {
      this.name = "Gunslinger";
      this.setChance(5);
   }
}
