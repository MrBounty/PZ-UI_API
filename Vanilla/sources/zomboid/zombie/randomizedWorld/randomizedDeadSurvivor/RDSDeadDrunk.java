package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSDeadDrunk extends RandomizedDeadSurvivorBase {
   final ArrayList alcoholList = new ArrayList();

   public RDSDeadDrunk() {
      this.name = "Dead Drunk";
      this.setChance(10);
      this.alcoholList.add("Base.WhiskeyFull");
      this.alcoholList.add("Base.WhiskeyEmpty");
      this.alcoholList.add("Base.Wine");
      this.alcoholList.add("Base.WineEmpty");
      this.alcoholList.add("Base.Wine2");
      this.alcoholList.add("Base.WineEmpty2");
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      IsoDeadBody var3 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2, 0);
      if (var3 != null) {
         int var4 = Rand.Next(2, 4);

         for(int var5 = 0; var5 < var4; ++var5) {
            InventoryItem var6 = InventoryItemFactory.CreateItem((String)this.alcoholList.get(Rand.Next(0, this.alcoholList.size())));
            var3.getSquare().AddWorldInventoryItem(var6, Rand.Next(0.5F, 1.0F), Rand.Next(0.5F, 1.0F), 0.0F);
         }

         var3.setPrimaryHandItem(InventoryItemFactory.CreateItem("Base.WhiskeyEmpty"));
      }
   }
}
