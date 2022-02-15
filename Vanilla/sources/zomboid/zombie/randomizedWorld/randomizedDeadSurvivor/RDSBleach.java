package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSBleach extends RandomizedDeadSurvivorBase {
   public RDSBleach() {
      this.name = "Suicide by Bleach";
      this.setChance(10);
      this.setMinimumDays(60);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      IsoDeadBody var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2, 0);
      if (var3 != null) {
         int var4 = Rand.Next(1, 3);

         for(int var5 = 0; var5 < var4; ++var5) {
            InventoryItem var6 = InventoryItemFactory.CreateItem("Base.BleachEmpty");
            var3.getSquare().AddWorldInventoryItem(var6, Rand.Next(0.5F, 1.0F), Rand.Next(0.5F, 1.0F), 0.0F);
         }

         var3.setPrimaryHandItem(InventoryItemFactory.CreateItem("Base.BleachEmpty"));
      }
   }
}
