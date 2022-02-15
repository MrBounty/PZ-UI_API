package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoMetaGrid;

public class RZSForestCamp extends RandomizedZoneStoryBase {
   public RZSForestCamp() {
      this.name = "Basic Forest Camp";
      this.chance = 10;
      this.minZoneHeight = 6;
      this.minZoneWidth = 6;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
   }

   public static ArrayList getForestClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.Crisps");
      var0.add("Base.Crisps2");
      var0.add("Base.Crisps3");
      var0.add("Base.Crisps4");
      var0.add("Base.Pop");
      var0.add("Base.Pop2");
      var0.add("Base.WaterBottleFull");
      var0.add("Base.CannedSardines");
      var0.add("Base.CannedChili");
      var0.add("Base.CannedBolognese");
      var0.add("Base.CannedCornedBeef");
      var0.add("Base.TinnedSoup");
      var0.add("Base.TinnedBeans");
      var0.add("Base.TunaTin");
      var0.add("Base.WhiskeyFull");
      var0.add("Base.BeerBottle");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      return var0;
   }

   public static ArrayList getCoolerClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.Pop");
      var0.add("Base.Pop2");
      var0.add("Base.BeefJerky");
      var0.add("Base.Ham");
      var0.add("Base.WaterBottleFull");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      return var0;
   }

   public static ArrayList getFireClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.WaterPotRice");
      var0.add("Base.WaterPot");
      var0.add("Base.Pot");
      var0.add("Base.WaterSaucepanRice");
      var0.add("Base.WaterSaucepanPasta");
      var0.add("Base.PotOfStew");
      return var0;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = var1.pickedXForZoneStory;
      int var3 = var1.pickedYForZoneStory;
      ArrayList var4 = getForestClutter();
      ArrayList var5 = getCoolerClutter();
      ArrayList var6 = getFireClutter();
      this.cleanAreaForStory(this, var1);
      this.addTileObject(var2, var3, var1.z, "camping_01_6");
      this.addItemOnGround(this.getSq(var2, var3, var1.z), (String)var6.get(Rand.Next(var6.size())));
      int var7 = Rand.Next(-1, 2);
      int var8 = Rand.Next(-1, 2);
      this.addTentWestEast(var2 + var7 - 2, var3 + var8, var1.z);
      if (Rand.Next(100) < 70) {
         this.addTentNorthSouth(var2 + var7, var3 + var8 - 2, var1.z);
      }

      if (Rand.Next(100) < 30) {
         this.addTentNorthSouth(var2 + var7 + 1, var3 + var8 - 2, var1.z);
      }

      this.addTileObject(var2 + 2, var3, var1.z, "furniture_seating_outdoor_01_19");
      InventoryContainer var9 = (InventoryContainer)InventoryItemFactory.CreateItem("Base.Cooler");
      int var10 = Rand.Next(2, 5);

      int var11;
      for(var11 = 0; var11 < var10; ++var11) {
         var9.getItemContainer().AddItem((String)var5.get(Rand.Next(var5.size())));
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), var9);
      var10 = Rand.Next(3, 7);

      for(var11 = 0; var11 < var10; ++var11) {
         this.addItemOnGround(this.getRandomFreeSquare(this, var1), (String)var4.get(Rand.Next(var4.size())));
      }

      this.addZombiesOnSquare(Rand.Next(1, 3), "Camper", (Integer)null, this.getRandomFreeSquare(this, var1));
   }
}
