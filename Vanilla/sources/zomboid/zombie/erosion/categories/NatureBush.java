package zombie.erosion.categories;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.erosion.ErosionData;
import zombie.erosion.obj.ErosionObj;
import zombie.erosion.obj.ErosionObjSprites;
import zombie.erosion.season.ErosionIceQueen;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.sprite.IsoSprite;

public final class NatureBush extends ErosionCategory {
   private final int[][] soilRef = new int[][]{{11, 11, 12, 13}, {5, 5, 7, 8, 11, 11, 12, 13, 11, 11, 12, 13}, {5, 5, 7, 8, 5, 5, 7, 8, 11, 11, 12, 13}, {1, 1, 4, 5}, {5, 5, 7, 8, 1, 1, 4, 5, 1, 1, 4, 5}, {5, 5, 7, 8, 5, 5, 7, 8, 1, 1, 4, 5}, {9, 10, 14, 15}, {5, 5, 7, 8, 9, 10, 14, 15, 9, 10, 14, 15}, {5, 5, 7, 8, 5, 5, 7, 8, 9, 10, 14, 15}, {2, 3, 16, 16}, {5, 5, 7, 8, 2, 3, 16, 16, 2, 3, 16, 16}, {5, 5, 7, 8, 5, 5, 7, 8, 2, 3, 16, 16}};
   private ArrayList objs = new ArrayList();
   private int[] spawnChance = new int[100];
   private NatureBush.BushInit[] bush = new NatureBush.BushInit[]{new NatureBush.BushInit("Spicebush", 0.05F, 0.35F, false), new NatureBush.BushInit("Ninebark", 0.65F, 0.75F, true), new NatureBush.BushInit("Ninebark", 0.65F, 0.75F, true), new NatureBush.BushInit("Blueberry", 0.4F, 0.5F, true), new NatureBush.BushInit("Blackberry", 0.4F, 0.5F, true), new NatureBush.BushInit("Piedmont azalea", 0.0F, 0.15F, true), new NatureBush.BushInit("Piedmont azalea", 0.0F, 0.15F, true), new NatureBush.BushInit("Arrowwood viburnum", 0.3F, 0.8F, true), new NatureBush.BushInit("Red chokeberry", 0.9F, 1.0F, true), new NatureBush.BushInit("Red chokeberry", 0.9F, 1.0F, true), new NatureBush.BushInit("Beautyberry", 0.7F, 0.85F, true), new NatureBush.BushInit("New jersey tea", 0.4F, 0.8F, true), new NatureBush.BushInit("New jersey tea", 0.4F, 0.8F, true), new NatureBush.BushInit("Wild hydrangea", 0.2F, 0.35F, true), new NatureBush.BushInit("Wild hydrangea", 0.2F, 0.35F, true), new NatureBush.BushInit("Shrubby St. John's wort", 0.35F, 0.75F, true)};

   public boolean replaceExistingObject(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5) {
      int var6 = var1.getObjects().size();
      boolean var7 = false;

      for(int var8 = var6 - 1; var8 >= 1; --var8) {
         IsoObject var9 = (IsoObject)var1.getObjects().get(var8);
         IsoSprite var10 = var9.getSprite();
         if (var10 != null && var10.getName() != null) {
            int var11;
            if (var10.getName().startsWith("vegetation_foliage")) {
               var11 = var2.soil;
               if (var11 < 0 || var11 >= this.soilRef.length) {
                  var11 = var2.rand(var1.x, var1.y, this.soilRef.length);
               }

               int[] var12 = this.soilRef[var11];
               int var13 = var2.noiseMainInt;
               NatureBush.CategoryData var14 = (NatureBush.CategoryData)this.setCatModData(var2);
               var14.gameObj = var12[var2.rand(var1.x, var1.y, var12.length)] - 1;
               var14.maxStage = (int)Math.floor((double)((float)var13 / 60.0F));
               var14.stage = var14.maxStage;
               var14.spawnTime = 0;
               var1.RemoveTileObject(var9);
               var7 = true;
            }

            if (var10.getName().startsWith("f_bushes_1_")) {
               var11 = Integer.parseInt(var10.getName().replace("f_bushes_1_", ""));
               NatureBush.CategoryData var15 = (NatureBush.CategoryData)this.setCatModData(var2);
               var15.gameObj = var11 % 16;
               var15.maxStage = 1;
               var15.stage = var15.maxStage;
               var15.spawnTime = 0;
               var1.RemoveTileObject(var9);
               var7 = true;
            }
         }
      }

      return var7;
   }

   public boolean validateSpawn(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5, boolean var6) {
      if (var1.getObjects().size() > (var5 ? 2 : 1)) {
         return false;
      } else if (var2.soil >= 0 && var2.soil < this.soilRef.length) {
         int[] var7 = this.soilRef[var2.soil];
         int var8 = var2.noiseMainInt;
         int var9 = var2.rand(var1.x, var1.y, 101);
         if (var9 < this.spawnChance[var8]) {
            NatureBush.CategoryData var10 = (NatureBush.CategoryData)this.setCatModData(var2);
            var10.gameObj = var7[var2.rand(var1.x, var1.y, var7.length)] - 1;
            var10.maxStage = (int)Math.floor((double)((float)var8 / 60.0F));
            var10.stage = 0;
            var10.spawnTime = 100 - var8;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void update(IsoGridSquare var1, ErosionData.Square var2, ErosionCategory.Data var3, ErosionData.Chunk var4, int var5) {
      NatureBush.CategoryData var6 = (NatureBush.CategoryData)var3;
      if (var5 >= var6.spawnTime && !var6.doNothing) {
         if (var6.gameObj >= 0 && var6.gameObj < this.objs.size()) {
            ErosionObj var7 = (ErosionObj)this.objs.get(var6.gameObj);
            int var8 = var6.maxStage;
            int var9 = (int)Math.floor((double)((float)(var5 - var6.spawnTime) / ((float)var7.cycleTime / ((float)var8 + 1.0F))));
            if (var9 < var6.stage) {
               var9 = var6.stage;
            }

            if (var9 > var8) {
               var9 = var8;
            }

            int var10 = this.currentSeason(var2.magicNum, var7);
            boolean var11 = this.currentBloom(var2.magicNum, var7);
            boolean var12 = false;
            this.updateObj(var2, var3, var1, var7, var12, var9, var10, var11);
         } else {
            var6.doNothing = true;
         }

      }
   }

   public void init() {
      for(int var1 = 0; var1 < 100; ++var1) {
         if (var1 >= 45 && var1 < 60) {
            this.spawnChance[var1] = (int)this.clerp((float)(var1 - 45) / 15.0F, 0.0F, 20.0F);
         }

         if (var1 >= 60 && var1 < 90) {
            this.spawnChance[var1] = (int)this.clerp((float)(var1 - 60) / 30.0F, 20.0F, 0.0F);
         }
      }

      this.seasonDisp[5].season1 = 0;
      this.seasonDisp[5].season2 = 0;
      this.seasonDisp[5].split = false;
      this.seasonDisp[1].season1 = 1;
      this.seasonDisp[1].season2 = 0;
      this.seasonDisp[1].split = false;
      this.seasonDisp[2].season1 = 2;
      this.seasonDisp[2].season2 = 2;
      this.seasonDisp[2].split = true;
      this.seasonDisp[4].season1 = 4;
      this.seasonDisp[4].season2 = 0;
      this.seasonDisp[4].split = true;
      ErosionIceQueen var17 = ErosionIceQueen.instance;
      String var2 = "f_bushes_1_";

      for(int var3 = 1; var3 <= this.bush.length; ++var3) {
         int var4 = var3 - 1;
         int var5 = var4 - (int)Math.floor((double)((float)var4 / 8.0F)) * 8;
         NatureBush.BushInit var6 = this.bush[var4];
         ErosionObjSprites var7 = new ErosionObjSprites(2, var6.name, true, var6.hasFlower, true);
         int var8 = 0 + var5;
         int var9 = var8 + 16;
         int var10 = var9 + 16;
         int var11 = var10 + 16;
         int var12 = 64 + var4;
         int var13 = var12 + 16;
         var7.setBase(0, (String)(var2 + var8), 0);
         var7.setBase(1, (String)(var2 + (var8 + 8)), 0);
         var17.addSprite(var2 + var8, var2 + var9);
         var17.addSprite(var2 + (var8 + 8), var2 + (var9 + 8));
         var7.setChildSprite(0, (String)(var2 + var10), 1);
         var7.setChildSprite(1, (String)(var2 + (var10 + 8)), 1);
         var7.setChildSprite(0, (String)(var2 + var11), 4);
         var7.setChildSprite(1, (String)(var2 + (var11 + 8)), 4);
         var7.setChildSprite(0, (String)(var2 + var12), 2);
         var7.setChildSprite(1, (String)(var2 + (var12 + 32)), 2);
         if (var6.hasFlower) {
            var7.setFlower(0, (String)(var2 + var13));
            var7.setFlower(1, (String)(var2 + (var13 + 32)));
         }

         float var14 = var6.hasFlower ? var6.bloomstart : 0.0F;
         float var15 = var6.hasFlower ? var6.bloomend : 0.0F;
         ErosionObj var16 = new ErosionObj(var7, 60, var14, var15, true);
         this.objs.add(var16);
      }

   }

   protected ErosionCategory.Data allocData() {
      return new NatureBush.CategoryData();
   }

   public void getObjectNames(ArrayList var1) {
      for(int var2 = 0; var2 < this.objs.size(); ++var2) {
         if (((ErosionObj)this.objs.get(var2)).name != null && !var1.contains(((ErosionObj)this.objs.get(var2)).name)) {
            var1.add(((ErosionObj)this.objs.get(var2)).name);
         }
      }

   }

   private class BushInit {
      public String name;
      public float bloomstart;
      public float bloomend;
      public boolean hasFlower;

      public BushInit(String var2, float var3, float var4, boolean var5) {
         this.name = var2;
         this.bloomstart = var3;
         this.bloomend = var4;
         this.hasFlower = var5;
      }
   }

   private static final class CategoryData extends ErosionCategory.Data {
      public int gameObj;
      public int maxStage;
      public int spawnTime;

      public void save(ByteBuffer var1) {
         super.save(var1);
         var1.put((byte)this.gameObj);
         var1.put((byte)this.maxStage);
         var1.putShort((short)this.spawnTime);
      }

      public void load(ByteBuffer var1, int var2) {
         super.load(var1, var2);
         this.gameObj = var1.get();
         this.maxStage = var1.get();
         this.spawnTime = var1.getShort();
      }
   }
}
