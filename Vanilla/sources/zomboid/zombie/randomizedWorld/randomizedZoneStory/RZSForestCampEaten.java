package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMetaGrid;
import zombie.iso.objects.IsoDeadBody;

public class RZSForestCampEaten extends RandomizedZoneStoryBase {
   public RZSForestCampEaten() {
      this.name = "Forest Camp Eaten";
      this.chance = 10;
      this.minZoneHeight = 6;
      this.minZoneWidth = 10;
      this.minimumDays = 30;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = var1.pickedXForZoneStory;
      int var3 = var1.pickedYForZoneStory;
      ArrayList var4 = RZSForestCamp.getForestClutter();
      ArrayList var5 = RZSForestCamp.getCoolerClutter();
      ArrayList var6 = RZSForestCamp.getFireClutter();
      this.cleanAreaForStory(this, var1);
      this.addTileObject(var2, var3, var1.z, "camping_01_6");
      this.addItemOnGround(this.getSq(var2, var3, var1.z), (String)var6.get(Rand.Next(var6.size())));
      byte var7 = 0;
      byte var8 = 0;
      this.addTentNorthSouth(var2 - 4, var3 + var8 - 2, var1.z);
      int var16 = var7 + Rand.Next(1, 3);
      this.addTentNorthSouth(var2 - 3 + var16, var3 + var8 - 2, var1.z);
      var16 += Rand.Next(1, 3);
      this.addTentNorthSouth(var2 - 2 + var16, var3 + var8 - 2, var1.z);
      if (Rand.NextBool(1)) {
         var16 += Rand.Next(1, 3);
         this.addTentNorthSouth(var2 - 1 + var16, var3 + var8 - 2, var1.z);
      }

      if (Rand.NextBool(2)) {
         var16 += Rand.Next(1, 3);
         this.addTentNorthSouth(var2 + var16, var3 + var8 - 2, var1.z);
      }

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

      ArrayList var17 = this.addZombiesOnSquare(1, "Camper", (Integer)null, this.getRandomFreeSquare(this, var1));
      IsoZombie var12 = var17.isEmpty() ? null : (IsoZombie)var17.get(0);
      int var13 = Rand.Next(3, 7);
      IsoDeadBody var14 = null;

      for(int var15 = 0; var15 < var13; ++var15) {
         var14 = createRandomDeadBody(this.getRandomFreeSquare(this, var1), (IsoDirections)null, Rand.Next(5, 10), 0, "Camper");
         if (var14 != null) {
            this.addBloodSplat(var14.getSquare(), 10);
         }
      }

      var14 = createRandomDeadBody(this.getSq(var2, var3 + 3, var1.z), (IsoDirections)null, Rand.Next(5, 10), 0, "Camper");
      if (var14 != null) {
         this.addBloodSplat(var14.getSquare(), 10);
         if (var12 != null) {
            var12.faceLocationF(var14.x, var14.y);
            var12.setX(var14.x + 1.0F);
            var12.setY(var14.y);
            var12.setEatBodyTarget(var14, true);
         }
      }

   }
}
