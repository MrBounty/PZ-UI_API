package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoZombie;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public class RBKateAndBaldspot extends RandomizedBuildingBase {
   public RBKateAndBaldspot() {
      this.name = "K&B story";
      this.setChance(0);
      this.setUnique(true);
   }

   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      var1.setHasBeenVisited(true);
      var1.setAllExplored(true);
      ArrayList var2 = this.addZombiesOnSquare(1, "Kate", 100, this.getSq(10746, 9412, 1));
      if (var2 != null && !var2.isEmpty()) {
         IsoZombie var3 = (IsoZombie)var2.get(0);
         HumanVisual var4 = (HumanVisual)var3.getVisual();
         var4.setHairModel("Rachel");
         var4.setHairColor(new ImmutableColor(0.83F, 0.67F, 0.27F));

         for(int var5 = 0; var5 < var3.getItemVisuals().size(); ++var5) {
            ItemVisual var6 = (ItemVisual)var3.getItemVisuals().get(var5);
            if (var6.getClothingItemName().equals("Skirt_Knees")) {
               var6.setTint(new ImmutableColor(0.54F, 0.54F, 0.54F));
            }
         }

         var3.getHumanVisual().setSkinTextureIndex(1);
         var3.addBlood(BloodBodyPartType.LowerLeg_L, true, true, true);
         var3.addBlood(BloodBodyPartType.LowerLeg_L, true, true, true);
         var3.addBlood(BloodBodyPartType.UpperLeg_L, true, true, true);
         var3.addBlood(BloodBodyPartType.UpperLeg_L, true, true, true);
         var3.setCrawler(true);
         var3.setCanWalk(false);
         var3.setCrawlerType(1);
         var3.resetModelNextFrame();
         var2 = this.addZombiesOnSquare(1, "Bob", 0, this.getSq(10747, 9412, 1));
         if (var2 != null && !var2.isEmpty()) {
            IsoZombie var15 = (IsoZombie)var2.get(0);
            var4 = (HumanVisual)var15.getVisual();
            var4.setHairModel("Baldspot");
            var4.setHairColor(new ImmutableColor(0.337F, 0.173F, 0.082F));
            var4.setBeardModel("");

            for(int var16 = 0; var16 < var15.getItemVisuals().size(); ++var16) {
               ItemVisual var7 = (ItemVisual)var15.getItemVisuals().get(var16);
               if (var7.getClothingItemName().equals("Trousers_DefaultTEXTURE_TINT")) {
                  var7.setTint(new ImmutableColor(0.54F, 0.54F, 0.54F));
               }

               if (var7.getClothingItemName().equals("Shirt_FormalTINT")) {
                  var7.setTint(new ImmutableColor(0.63F, 0.71F, 0.82F));
               }
            }

            var15.getHumanVisual().setSkinTextureIndex(1);
            var15.resetModelNextFrame();
            var15.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("KatePic"));
            var15.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("RippedSheets"));
            var15.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("Pills"));
            InventoryItem var17 = InventoryItemFactory.CreateItem("Hammer");
            var17.setCondition(1);
            var15.addItemToSpawnAtDeath(var17);
            var15.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("Nails"));
            var15.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("Plank"));
            var2 = this.addZombiesOnSquare(1, "Raider", 0, this.getSq(10745, 9411, 0));
            if (var2 != null && !var2.isEmpty()) {
               IsoZombie var18 = (IsoZombie)var2.get(0);
               var4 = (HumanVisual)var18.getVisual();
               var4.setHairModel("Crewcut");
               var4.setHairColor(new ImmutableColor(0.37F, 0.27F, 0.23F));
               var4.setBeardModel("Goatee");

               for(int var8 = 0; var8 < var18.getItemVisuals().size(); ++var8) {
                  ItemVisual var9 = (ItemVisual)var18.getItemVisuals().get(var8);
                  if (var9.getClothingItemName().equals("Trousers_DefaultTEXTURE_TINT")) {
                     var9.setTint(new ImmutableColor(0.54F, 0.54F, 0.54F));
                  }

                  if (var9.getClothingItemName().equals("Vest_DefaultTEXTURE_TINT")) {
                     var9.setTint(new ImmutableColor(0.22F, 0.25F, 0.27F));
                  }
               }

               var18.getHumanVisual().setSkinTextureIndex(1);
               InventoryItem var19 = InventoryItemFactory.CreateItem("Shotgun");
               var19.setCondition(0);
               var18.setAttachedItem("Rifle On Back", var19);
               InventoryItem var20 = InventoryItemFactory.CreateItem("BaseballBat");
               var20.setCondition(1);
               var18.addItemToSpawnAtDeath(var20);
               var18.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem("ShotgunShells"));
               var18.resetModelNextFrame();
               this.addItemOnGround(this.getSq(10747, 9412, 1), InventoryItemFactory.CreateItem("Pillow"));
               IsoGridSquare var10 = this.getSq(10745, 9410, 0);
               var10.Burn();
               var10 = this.getSq(10745, 9411, 0);
               var10.Burn();
               var10 = this.getSq(10746, 9411, 0);
               var10.Burn();
               var10 = this.getSq(10745, 9410, 0);
               var10.Burn();
               var10 = this.getSq(10745, 9412, 0);
               var10.Burn();
               var10 = this.getSq(10747, 9410, 0);
               var10.Burn();
               var10 = this.getSq(10746, 9409, 0);
               var10.Burn();
               var10 = this.getSq(10745, 9409, 0);
               var10.Burn();
               var10 = this.getSq(10744, 9410, 0);
               var10.Burn();
               var10 = this.getSq(10747, 9411, 0);
               var10.Burn();
               var10 = this.getSq(10746, 9412, 0);
               var10.Burn();
               IsoGridSquare var11 = this.getSq(10746, 9410, 0);

               for(int var12 = 0; var12 < var11.getObjects().size(); ++var12) {
                  IsoObject var13 = (IsoObject)var11.getObjects().get(var12);
                  if (var13.getContainer() != null) {
                     InventoryItem var14 = InventoryItemFactory.CreateItem("PotOfSoup");
                     var14.setCooked(true);
                     var14.setBurnt(true);
                     var13.getContainer().AddItem(var14);
                     break;
                  }
               }

               this.addBarricade(this.getSq(10747, 9417, 0), 3);
               this.addBarricade(this.getSq(10745, 9417, 0), 3);
               this.addBarricade(this.getSq(10744, 9413, 0), 3);
               this.addBarricade(this.getSq(10744, 9412, 0), 3);
               this.addBarricade(this.getSq(10752, 9413, 0), 3);
            }
         }
      }
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      this.debugLine = "";
      if (var1.x == 10744 && var1.y == 9409) {
         return true;
      } else {
         this.debugLine = "Need to be the K&B house";
         return false;
      }
   }
}
