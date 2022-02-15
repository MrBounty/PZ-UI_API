package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.RoomDef;

public final class RDSBedroomZed extends RandomizedDeadSurvivorBase {
   private final ArrayList pantsMaleItems = new ArrayList();
   private final ArrayList pantsFemaleItems = new ArrayList();
   private final ArrayList topItems = new ArrayList();
   private final ArrayList shoesItems = new ArrayList();

   public RDSBedroomZed() {
      this.name = "Bedroom Zed";
      this.setChance(7);
      this.shoesItems.add("Base.Shoes_Random");
      this.shoesItems.add("Base.Shoes_TrainerTINT");
      this.pantsMaleItems.add("Base.TrousersMesh_DenimLight");
      this.pantsMaleItems.add("Base.Trousers_DefaultTEXTURE_TINT");
      this.pantsMaleItems.add("Base.Trousers_Denim");
      this.pantsFemaleItems.add("Base.Skirt_Knees");
      this.pantsFemaleItems.add("Base.Skirt_Long");
      this.pantsFemaleItems.add("Base.Skirt_Short");
      this.pantsFemaleItems.add("Base.Skirt_Normal");
      this.topItems.add("Base.Shirt_FormalWhite");
      this.topItems.add("Base.Shirt_FormalWhite_ShortSleeve");
      this.topItems.add("Base.Tshirt_DefaultTEXTURE_TINT");
      this.topItems.add("Base.Tshirt_PoloTINT");
      this.topItems.add("Base.Tshirt_WhiteLongSleeveTINT");
      this.topItems.add("Base.Tshirt_WhiteTINT");
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getRoom(var1, "bedroom");
      boolean var3 = Rand.Next(7) == 0;
      boolean var4 = Rand.Next(7) == 0;
      if (var3) {
         this.addZombies(var1, 2, "Naked", 0, var2);
         this.addItemsOnGround(var2, true);
         this.addItemsOnGround(var2, true);
      } else if (var4) {
         this.addZombies(var1, 2, "Naked", 100, var2);
         this.addItemsOnGround(var2, false);
         this.addItemsOnGround(var2, false);
      } else {
         this.addZombies(var1, 1, "Naked", 0, var2);
         this.addItemsOnGround(var2, true);
         this.addZombies(var1, 1, "Naked", 100, var2);
         this.addItemsOnGround(var2, false);
      }

   }

   private void addItemsOnGround(RoomDef var1, boolean var2) {
      IsoGridSquare var3 = getRandomSpawnSquare(var1);
      this.addRandomItemOnGround(var3, this.shoesItems);
      this.addRandomItemOnGround(var3, this.topItems);
      this.addRandomItemOnGround(var3, var2 ? this.pantsMaleItems : this.pantsFemaleItems);
   }
}
