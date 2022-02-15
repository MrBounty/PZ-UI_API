package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMetaGrid;

public class RZSFishingTrip extends RandomizedZoneStoryBase {
   public RZSFishingTrip() {
      this.name = "Fishing Trip";
      this.chance = 10;
      this.minZoneHeight = 8;
      this.minZoneWidth = 8;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Beach.toString());
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Lake.toString());
   }

   public static ArrayList getFishes() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.Catfish");
      var0.add("Base.Bass");
      var0.add("Base.Perch");
      var0.add("Base.Crappie");
      var0.add("Base.Panfish");
      var0.add("Base.Pike");
      var0.add("Base.Trout");
      var0.add("Base.BaitFish");
      return var0;
   }

   public static ArrayList getFishingTools() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.FishingTackle");
      var0.add("Base.FishingTackle");
      var0.add("Base.FishingTackle2");
      var0.add("Base.FishingTackle2");
      var0.add("Base.FishingLine");
      var0.add("Base.FishingLine");
      var0.add("Base.FishingNet");
      var0.add("Base.Worm");
      var0.add("Base.Worm");
      var0.add("Base.Worm");
      var0.add("Base.Worm");
      return var0;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      ArrayList var2 = getFishes();
      ArrayList var3 = getFishingTools();
      this.cleanAreaForStory(this, var1);
      this.addVehicle(var1, this.getSq(var1.x, var1.y, var1.z), (IsoChunk)null, (String)null, "Base.PickUpTruck", (Integer)null, (IsoDirections)null, "Fisherman");
      int var4 = Rand.Next(1, 3);

      for(int var5 = 0; var5 < var4; ++var5) {
         this.addTileObject(this.getRandomFreeSquare(this, var1), "furniture_seating_outdoor_01_" + Rand.Next(16, 20));
      }

      InventoryContainer var9 = (InventoryContainer)InventoryItemFactory.CreateItem("Base.Cooler");
      int var6 = Rand.Next(4, 10);

      for(int var7 = 0; var7 < var6; ++var7) {
         var9.getItemContainer().AddItem((String)var2.get(Rand.Next(var2.size())));
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), var9);
      InventoryContainer var10 = (InventoryContainer)InventoryItemFactory.CreateItem("Base.Toolbox");
      var6 = Rand.Next(3, 8);

      int var8;
      for(var8 = 0; var8 < var6; ++var8) {
         var10.getItemContainer().AddItem((String)var3.get(Rand.Next(var3.size())));
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), var10);
      var6 = Rand.Next(2, 5);

      for(var8 = 0; var8 < var6; ++var8) {
         this.addItemOnGround(this.getRandomFreeSquare(this, var1), "FishingRod");
      }

      this.addZombiesOnSquare(Rand.Next(2, 5), "Fisherman", 0, this.getRandomFreeSquare(this, var1));
   }
}
