package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public final class RBTableStory extends RandomizedBuildingBase {
   public static ArrayList allStories = new ArrayList();
   private float xOffset = 0.0F;
   private float yOffset = 0.0F;
   private IsoGridSquare currentSquare = null;
   public ArrayList fullTableMap = new ArrayList();
   public IsoObject table1 = null;
   public IsoObject table2 = null;

   public void initStories() {
      if (allStories.isEmpty()) {
         ArrayList var1 = new ArrayList();
         var1.add("livingroom");
         var1.add("kitchen");
         ArrayList var2 = new ArrayList();
         LinkedHashMap var3 = new LinkedHashMap();
         var3.put("BakingPan", 50);
         var3.put("CakePrep", 50);
         var2.add(new RBTableStory.StorySpawnItem(var3, (String)null, 100));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Chocolate", 100));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Butter", 70));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Flour", 70));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Spoon", 100));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "EggCarton", 100));
         var2.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Egg", 100));
         allStories.add(new RBTableStory.StoryDef(var2, var1));
         ArrayList var4 = new ArrayList();
         var4.add(new RBTableStory.StorySpawnItem(var3, (String)null, 100));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Flour", 70));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Butter", 70));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "KitchenKnife", 100));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Egg", 100));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Spoon", 100));
         LinkedHashMap var5 = new LinkedHashMap();
         var5.put("BerryBlack", 50);
         var5.put("BerryBlue", 50);
         var4.add(new RBTableStory.StorySpawnItem(var5, (String)null, 100));
         var4.add(new RBTableStory.StorySpawnItem(var5, (String)null, 70));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Cherry", 100));
         var4.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Pineapple", 70));
         allStories.add(new RBTableStory.StoryDef(var4, var1));
         ArrayList var6 = new ArrayList();
         var6.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Rabbitmeat", 100, 0.1F));
         var6.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Rabbitmeat", 70, 0.1F));
         var6.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "DeadRabbit", 100, 0.15F));
         var6.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Rabbitmeat", 100, 0.1F));
         var6.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "HuntingKnife", 100));
         RBTableStory.StoryDef var7 = new RBTableStory.StoryDef(var6, var1);
         var7.addBlood = true;
         allStories.add(var7);
         ArrayList var8 = new ArrayList();
         var8.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Mugl", 100));
         var8.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Cereal", 100));
         var8.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Spoon", 100));
         LinkedHashMap var9 = new LinkedHashMap();
         var9.put("Coffee2", 50);
         var9.put("Teabag2", 50);
         var8.add(new RBTableStory.StorySpawnItem(var9, (String)null, 100));
         allStories.add(new RBTableStory.StoryDef(var8, var1));
         ArrayList var10 = new ArrayList();
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Socks_Ankle", 100));
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Socks_Long", 70));
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Thread", 100));
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Thread", 50));
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Needle", 100));
         var10.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "RippedSheets", 100));
         allStories.add(new RBTableStory.StoryDef(var10, var1));
         ArrayList var11 = new ArrayList();
         var11.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "BoxOfJars", 100, 0.15F));
         var11.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "JarLid", 100));
         var11.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "EmptyJar", 100));
         var11.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Vinegar", 100));
         var11.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Sugar", 100));
         LinkedHashMap var12 = new LinkedHashMap();
         var12.put("Carrots", 20);
         var12.put("farming.Tomato", 20);
         var12.put("farming.Potato", 20);
         var12.put("Eggplant", 20);
         var12.put("Leek", 20);
         var11.add(new RBTableStory.StorySpawnItem(var12, (String)null, 100));
         allStories.add(new RBTableStory.StoryDef(var11, var1));
         ArrayList var13 = new ArrayList();
         var13.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Screwdriver", 100));
         var13.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "ScrewsBox", 100));
         var13.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Screws", 100));
         var13.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "ElectronicsScrap", 100));
         LinkedHashMap var14 = new LinkedHashMap();
         var14.put("VideoGame", 20);
         var14.put("CDplayer", 20);
         var14.put("CordlessPhone", 20);
         var14.put("HomeAlarm", 20);
         var14.put("MotionSensor", 20);
         var13.add(new RBTableStory.StorySpawnItem(var14, (String)null, 100));
         allStories.add(new RBTableStory.StoryDef(var13, var1));
         ArrayList var15 = new ArrayList();
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Drawer", 100, 0.2F));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Screwdriver", 100));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "NailsBox", 100));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Nails", 100));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Hammer", 50));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Needle", 100));
         var15.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Woodglue", 100));
         allStories.add(new RBTableStory.StoryDef(var15, var1));
         ArrayList var16 = new ArrayList();
         var16.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Sponge", 100, 0.1F));
         var16.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "CleaningLiquid2", 100, 0.1F));
         var16.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "DishCloth", 100));
         allStories.add(new RBTableStory.StoryDef(var16, var1));
         var1 = new ArrayList();
         var1.add("livingroom");
         var1.add("kitchen");
         var1.add("bedroom");
         ArrayList var17 = new ArrayList();
         var17.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "ToyCar", 100));
         var17.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "ToyBear", 100, 0.1F));
         var17.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "CatToy", 70));
         var17.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "ToyCar", 80));
         var17.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Bricktoys", 100));
         RBTableStory.StoryDef var18 = new RBTableStory.StoryDef(var17, var1);
         var18.addBlood = true;
         allStories.add(var18);
         ArrayList var19 = new ArrayList();
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Notebook", 100));
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Pencil", 100));
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Pencil", 70));
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "BluePen", 80));
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "Pen", 80));
         var19.add(new RBTableStory.StorySpawnItem((LinkedHashMap)null, "RedPen", 80));
         allStories.add(new RBTableStory.StoryDef(var19, var1));
      }
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return false;
   }

   public void randomizeBuilding(BuildingDef var1) {
      this.initStories();
      if (this.table1 != null && this.table2 != null) {
         if (this.table1.getSquare() != null && this.table1.getSquare().getRoom() != null) {
            ArrayList var2 = new ArrayList();

            for(int var3 = 0; var3 < allStories.size(); ++var3) {
               RBTableStory.StoryDef var4 = (RBTableStory.StoryDef)allStories.get(var3);
               if (var4.rooms == null || var4.rooms.contains(this.table1.getSquare().getRoom().getName())) {
                  var2.add(var4);
               }
            }

            if (!var2.isEmpty()) {
               RBTableStory.StoryDef var13 = (RBTableStory.StoryDef)var2.get(Rand.Next(0, var2.size()));
               if (var13 != null) {
                  boolean var14 = true;
                  if ((int)this.table1.getY() != (int)this.table2.getY()) {
                     var14 = false;
                  }

                  this.doSpawnTable(var13.items, var14);
                  if (var13.addBlood) {
                     int var5 = (int)this.table1.getX() - 1;
                     int var6 = (int)this.table1.getX() + 1;
                     int var7 = (int)this.table1.getY() - 1;
                     int var8 = (int)this.table2.getY() + 1;
                     if (var14) {
                        var5 = (int)this.table1.getX() - 1;
                        var6 = (int)this.table2.getX() + 1;
                        var7 = (int)this.table1.getY() - 1;
                        var8 = (int)this.table2.getY() + 1;
                     }

                     for(int var9 = var5; var9 < var6 + 1; ++var9) {
                        for(int var10 = var7; var10 < var8 + 1; ++var10) {
                           int var11 = Rand.Next(7, 15);

                           for(int var12 = 0; var12 < var11; ++var12) {
                              this.currentSquare.getChunk().addBloodSplat((float)var9 + Rand.Next(-0.5F, 0.5F), (float)var10 + Rand.Next(-0.5F, 0.5F), this.table1.getZ(), Rand.Next(8));
                           }
                        }
                     }
                  }

               }
            }
         }
      }
   }

   private void doSpawnTable(ArrayList var1, boolean var2) {
      this.xOffset = 0.0F;
      this.yOffset = 0.0F;
      int var3 = 0;
      if (var2) {
         this.xOffset = 0.6F;
         this.yOffset = Rand.Next(0.5F, 1.1F);
      } else {
         this.yOffset = 0.6F;
         this.xOffset = Rand.Next(0.5F, 1.1F);
      }

      for(this.currentSquare = this.table1.getSquare(); var3 < var1.size(); ++var3) {
         RBTableStory.StorySpawnItem var4 = (RBTableStory.StorySpawnItem)var1.get(var3);
         String var5 = this.getItemFromSSI(var4);
         if (var5 != null) {
            InventoryItem var6 = this.currentSquare.AddWorldInventoryItem(var5, this.xOffset, this.yOffset, 0.4F);
            if (var6 != null) {
               var6.setAutoAge();
               this.increaseOffsets(var2, var4);
            }
         }
      }

   }

   private void increaseOffsets(boolean var1, RBTableStory.StorySpawnItem var2) {
      float var3 = 0.15F + var2.forcedOffset;
      float var4;
      if (var1) {
         this.xOffset += var3;
         if (this.xOffset > 1.0F) {
            this.currentSquare = this.table2.getSquare();
            this.xOffset = 0.35F;
         }

         for(var4 = this.yOffset; Math.abs(var4 - this.yOffset) < 0.11F; this.yOffset = Rand.Next(0.5F, 1.1F)) {
         }
      } else {
         this.yOffset += var3;
         if (this.yOffset > 1.0F) {
            this.currentSquare = this.table2.getSquare();
            this.yOffset = 0.35F;
         }

         for(var4 = this.xOffset; Math.abs(var4 - this.xOffset) < 0.11F; this.xOffset = Rand.Next(0.5F, 1.1F)) {
         }
      }

   }

   private String getItemFromSSI(RBTableStory.StorySpawnItem var1) {
      if (Rand.Next(100) > var1.chanceToSpawn) {
         return null;
      } else if (var1.eitherObject != null && !var1.eitherObject.isEmpty()) {
         int var2 = Rand.Next(100);
         int var3 = 0;
         Iterator var4 = var1.eitherObject.keySet().iterator();

         String var5;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (String)var4.next();
            int var6 = (Integer)var1.eitherObject.get(var5);
            var3 += var6;
         } while(var3 < var2);

         return var5;
      } else {
         return var1.object;
      }
   }

   public class StorySpawnItem {
      LinkedHashMap eitherObject = null;
      String object = null;
      Integer chanceToSpawn = null;
      float forcedOffset = 0.0F;

      public StorySpawnItem(LinkedHashMap var2, String var3, Integer var4) {
         this.eitherObject = var2;
         this.object = var3;
         this.chanceToSpawn = var4;
      }

      public StorySpawnItem(LinkedHashMap var2, String var3, Integer var4, float var5) {
         this.eitherObject = var2;
         this.object = var3;
         this.chanceToSpawn = var4;
         this.forcedOffset = var5;
      }
   }

   public class StoryDef {
      public ArrayList items = null;
      public boolean addBlood = false;
      public ArrayList rooms = null;

      public StoryDef(ArrayList var2) {
         this.items = var2;
      }

      public StoryDef(ArrayList var2, ArrayList var3) {
         this.items = var2;
         this.rooms = var3;
      }
   }
}
