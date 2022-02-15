package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.RoomDef;

public final class RDSStudentNight extends RandomizedDeadSurvivorBase {
   private final ArrayList items = new ArrayList();
   private final ArrayList otherItems = new ArrayList();
   private final ArrayList pantsMaleItems = new ArrayList();
   private final ArrayList pantsFemaleItems = new ArrayList();
   private final ArrayList topItems = new ArrayList();
   private final ArrayList shoesItems = new ArrayList();

   public RDSStudentNight() {
      this.name = "Student Night";
      this.setChance(4);
      this.setMaximumDays(60);
      this.otherItems.add("Base.Cigarettes");
      this.otherItems.add("Base.WhiskeyFull");
      this.otherItems.add("Base.Wine");
      this.otherItems.add("Base.Wine2");
      this.items.add("Base.Crisps");
      this.items.add("Base.Crisps2");
      this.items.add("Base.Crisps3");
      this.items.add("Base.Pop");
      this.items.add("Base.Pop2");
      this.items.add("Base.Pop3");
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
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      this.addZombies(var1, Rand.Next(2, 5), (String)null, (Integer)null, var2);
      RoomDef var3 = this.getRoom(var1, "bedroom");
      this.addZombies(var1, 1, "Naked", 0, var3);
      this.addItemsOnGround(var3, true);
      this.addZombies(var1, 1, "Naked", 100, var3);
      this.addItemsOnGround(var3, false);
      this.addRandomItemsOnGround(var2, this.items, Rand.Next(3, 7));
      this.addRandomItemsOnGround(var2, this.otherItems, Rand.Next(2, 6));
   }

   private void addItemsOnGround(RoomDef var1, boolean var2) {
      IsoGridSquare var3 = getRandomSpawnSquare(var1);
      this.addRandomItemOnGround(var3, this.shoesItems);
      this.addRandomItemOnGround(var3, this.topItems);
      this.addRandomItemOnGround(var3, var2 ? this.pantsMaleItems : this.pantsFemaleItems);
   }
}
