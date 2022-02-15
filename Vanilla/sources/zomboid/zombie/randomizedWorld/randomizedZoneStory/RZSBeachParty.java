package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;

public class RZSBeachParty extends RandomizedZoneStoryBase {
   public RZSBeachParty() {
      this.name = "Beach Party";
      this.chance = 10;
      this.minZoneHeight = 13;
      this.minZoneWidth = 13;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Beach.toString());
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Lake.toString());
   }

   public static ArrayList getBeachClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.Crisps");
      var0.add("Base.Crisps3");
      var0.add("Base.Pop");
      var0.add("Base.WhiskeyFull");
      var0.add("Base.Cigarettes");
      var0.add("Base.BeerBottle");
      var0.add("Base.BeerBottle");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      var0.add("Base.BeerCan");
      return var0;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = var1.pickedXForZoneStory;
      int var3 = var1.pickedYForZoneStory;
      ArrayList var4 = getBeachClutter();
      ArrayList var5 = RZSForestCamp.getCoolerClutter();
      if (Rand.NextBool(2)) {
         this.addTileObject(var2, var3, var1.z, "camping_01_6");
      }

      int var6 = Rand.Next(1, 4);

      int var7;
      int var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = Rand.Next(4) + 1;
         switch(var8) {
         case 1:
            var8 = 25;
            break;
         case 2:
            var8 = 26;
            break;
         case 3:
            var8 = 28;
            break;
         case 4:
            var8 = 31;
         }

         IsoGridSquare var9 = this.getRandomFreeSquare(this, var1);
         this.addTileObject(var9, "furniture_seating_outdoor_01_" + var8);
         if (var8 == 25) {
            var9 = this.getSq(var9.x, var9.y + 1, var9.z);
            this.addTileObject(var9, "furniture_seating_outdoor_01_24");
         } else if (var8 == 26) {
            var9 = this.getSq(var9.x + 1, var9.y, var9.z);
            this.addTileObject(var9, "furniture_seating_outdoor_01_27");
         } else if (var8 == 28) {
            var9 = this.getSq(var9.x, var9.y - 1, var9.z);
            this.addTileObject(var9, "furniture_seating_outdoor_01_29");
         } else {
            var9 = this.getSq(var9.x - 1, var9.y, var9.z);
            this.addTileObject(var9, "furniture_seating_outdoor_01_30");
         }
      }

      var6 = Rand.Next(1, 3);

      for(var7 = 0; var7 < var6; ++var7) {
         this.addTileObject(this.getRandomFreeSquare(this, var1), "furniture_seating_outdoor_01_" + Rand.Next(16, 20));
      }

      InventoryContainer var11 = (InventoryContainer)InventoryItemFactory.CreateItem("Base.Cooler");
      var8 = Rand.Next(4, 8);

      int var12;
      for(var12 = 0; var12 < var8; ++var12) {
         var11.getItemContainer().AddItem((String)var5.get(Rand.Next(var5.size())));
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), var11);
      var8 = Rand.Next(3, 7);

      for(var12 = 0; var12 < var8; ++var12) {
         this.addItemOnGround(this.getRandomFreeSquare(this, var1), (String)var4.get(Rand.Next(var4.size())));
      }

      var12 = Rand.Next(3, 8);

      int var10;
      for(var10 = 0; var10 < var12; ++var10) {
         this.addZombiesOnSquare(1, "Swimmer", (Integer)null, this.getRandomFreeSquare(this, var1));
      }

      var12 = Rand.Next(1, 3);

      for(var10 = 0; var10 < var12; ++var10) {
         this.addZombiesOnSquare(1, "Tourist", (Integer)null, this.getRandomFreeSquare(this, var1));
      }

   }
}
