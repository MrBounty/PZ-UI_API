package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoBarbecue;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;

public class RZSBBQParty extends RandomizedZoneStoryBase {
   public RZSBBQParty() {
      this.name = "BBQ Party";
      this.chance = 10;
      this.minZoneHeight = 12;
      this.minZoneWidth = 12;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Beach.toString());
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Lake.toString());
   }

   public static ArrayList getBeachClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.Crisps");
      var0.add("Base.Crisps3");
      var0.add("Base.MuttonChop");
      var0.add("Base.PorkChop");
      var0.add("Base.Steak");
      var0.add("Base.Pop");
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
      IsoGridSquare var6 = this.getSq(var2, var3, var1.z);
      IsoBarbecue var7 = new IsoBarbecue(IsoWorld.instance.getCell(), var6, (IsoSprite)IsoSpriteManager.instance.NamedMap.get("appliances_cooking_01_35"));
      var6.getObjects().add(var7);
      int var8 = Rand.Next(1, 4);

      for(int var9 = 0; var9 < var8; ++var9) {
         this.addTileObject(this.getRandomFreeSquare(this, var1), "furniture_seating_outdoor_01_" + Rand.Next(16, 20));
      }

      InventoryContainer var13 = (InventoryContainer)InventoryItemFactory.CreateItem("Base.Cooler");
      int var10 = Rand.Next(4, 8);

      int var11;
      for(var11 = 0; var11 < var10; ++var11) {
         var13.getItemContainer().AddItem((String)var5.get(Rand.Next(var5.size())));
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), var13);
      var10 = Rand.Next(3, 7);

      for(var11 = 0; var11 < var10; ++var11) {
         this.addItemOnGround(this.getRandomFreeSquare(this, var1), (String)var4.get(Rand.Next(var4.size())));
      }

      var11 = Rand.Next(3, 8);

      for(int var12 = 0; var12 < var11; ++var12) {
         this.addZombiesOnSquare(1, "Tourist", (Integer)null, this.getRandomFreeSquare(this, var1));
      }

   }
}
