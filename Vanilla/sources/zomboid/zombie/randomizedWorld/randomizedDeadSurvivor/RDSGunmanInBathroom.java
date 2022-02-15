package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSGunmanInBathroom extends RandomizedDeadSurvivorBase {
   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = super.getRoom(var1, "bathroom");
      IsoDeadBody var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2, Rand.Next(5, 10));
      if (var3 != null) {
         var3.setPrimaryHandItem(super.addRandomRangedWeapon(var3.getContainer(), true, false, false));
         int var4 = Rand.Next(1, 4);

         for(int var5 = 0; var5 < var4; ++var5) {
            var3.getContainer().AddItem((InventoryItem)super.addRandomRangedWeapon(var3.getContainer(), true, true, true));
         }

      }
   }

   public RDSGunmanInBathroom() {
      this.name = "Bathroom Gunman";
      this.setChance(5);
   }
}
