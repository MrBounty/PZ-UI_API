package zombie.erosion.categories;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.erosion.ErosionData;
import zombie.erosion.obj.ErosionObj;
import zombie.erosion.obj.ErosionObjOverlay;
import zombie.erosion.obj.ErosionObjOverlaySprites;
import zombie.erosion.obj.ErosionObjSprites;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public final class StreetCracks extends ErosionCategory {
   private ArrayList objs = new ArrayList();
   private ArrayList crackObjs = new ArrayList();
   private int[] spawnChance = new int[100];

   public boolean replaceExistingObject(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5) {
      return false;
   }

   public boolean validateSpawn(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5, boolean var6) {
      int var7 = var2.noiseMainInt;
      int var8 = this.spawnChance[var7];
      if (var8 == 0) {
         return false;
      } else if (var2.rand(var1.x, var1.y, 101) >= var8) {
         return false;
      } else {
         StreetCracks.CategoryData var9 = (StreetCracks.CategoryData)this.setCatModData(var2);
         var9.gameObj = var2.rand(var1.x, var1.y, this.crackObjs.size());
         var9.maxStage = var7 > 65 ? 2 : (var7 > 55 ? 1 : 0);
         var9.stage = 0;
         var9.spawnTime = 50 + (100 - var7);
         if (var2.magicNum > 0.5F) {
            var9.hasGrass = true;
         }

         return true;
      }
   }

   public void update(IsoGridSquare var1, ErosionData.Square var2, ErosionCategory.Data var3, ErosionData.Chunk var4, int var5) {
      StreetCracks.CategoryData var6 = (StreetCracks.CategoryData)var3;
      if (var5 >= var6.spawnTime && !var6.doNothing) {
         IsoObject var7 = var1.getFloor();
         if (var6.gameObj >= 0 && var6.gameObj < this.crackObjs.size() && var7 != null) {
            ErosionObjOverlay var8 = (ErosionObjOverlay)this.crackObjs.get(var6.gameObj);
            int var9 = var6.maxStage;
            int var10 = (int)Math.floor((double)((float)(var5 - var6.spawnTime) / ((float)var8.cycleTime / ((float)var9 + 1.0F))));
            if (var10 < var6.stage) {
               var10 = var6.stage;
            }

            if (var10 >= var8.stages) {
               var10 = var8.stages - 1;
            }

            int var12;
            if (var10 != var6.stage) {
               int var11 = var6.curID;
               var12 = var8.setOverlay(var7, var11, var10, 0, 0.0F);
               if (var12 >= 0) {
                  var6.curID = var12;
               }

               var6.stage = var10;
            } else if (!var6.hasGrass && var10 == var8.stages - 1) {
               var6.doNothing = true;
            }

            if (var6.hasGrass) {
               ErosionObj var15 = (ErosionObj)this.objs.get(var6.gameObj);
               if (var15 != null) {
                  var12 = this.currentSeason(var2.magicNum, var15);
                  boolean var13 = false;
                  boolean var14 = false;
                  this.updateObj(var2, var3, var1, var15, var13, var10, var12, var14);
               }
            }
         } else {
            var6.doNothing = true;
         }

      }
   }

   public void init() {
      for(int var1 = 0; var1 < 100; ++var1) {
         this.spawnChance[var1] = var1 >= 40 ? (int)this.clerp((float)(var1 - 40) / 60.0F, 0.0F, 60.0F) : 0;
      }

      this.seasonDisp[5].season1 = 5;
      this.seasonDisp[5].season2 = 0;
      this.seasonDisp[5].split = false;
      this.seasonDisp[1].season1 = 1;
      this.seasonDisp[1].season2 = 0;
      this.seasonDisp[1].split = false;
      this.seasonDisp[2].season1 = 2;
      this.seasonDisp[2].season2 = 4;
      this.seasonDisp[2].split = true;
      this.seasonDisp[4].season1 = 4;
      this.seasonDisp[4].season2 = 5;
      this.seasonDisp[4].split = true;
      String var9 = "d_streetcracks_1_";
      int[] var2 = new int[]{5, 1, 2, 4};

      for(int var3 = 0; var3 <= 7; ++var3) {
         ErosionObjOverlaySprites var4 = new ErosionObjOverlaySprites(3, "StreeCracks");
         ErosionObjSprites var5 = new ErosionObjSprites(3, "CrackGrass", false, false, false);

         for(int var6 = 0; var6 <= 2; ++var6) {
            for(int var7 = 0; var7 <= var2.length; ++var7) {
               int var8 = var7 * 24 + var6 * 8 + var3;
               if (var7 == 0) {
                  var4.setSprite(var6, var9 + var8, 0);
               } else {
                  var5.setBase(var6, var9 + var8, var2[var7 - 1]);
               }
            }
         }

         this.crackObjs.add(new ErosionObjOverlay(var4, 60, false));
         this.objs.add(new ErosionObj(var5, 60, 0.0F, 0.0F, false));
      }

   }

   protected ErosionCategory.Data allocData() {
      return new StreetCracks.CategoryData();
   }

   public void getObjectNames(ArrayList var1) {
      for(int var2 = 0; var2 < this.objs.size(); ++var2) {
         if (((ErosionObj)this.objs.get(var2)).name != null && !var1.contains(((ErosionObj)this.objs.get(var2)).name)) {
            var1.add(((ErosionObj)this.objs.get(var2)).name);
         }
      }

   }

   private static final class CategoryData extends ErosionCategory.Data {
      public int gameObj;
      public int maxStage;
      public int spawnTime;
      public int curID = -999999;
      public boolean hasGrass;

      public void save(ByteBuffer var1) {
         super.save(var1);
         var1.put((byte)this.gameObj);
         var1.put((byte)this.maxStage);
         var1.putShort((short)this.spawnTime);
         var1.putInt(this.curID);
         var1.put((byte)(this.hasGrass ? 1 : 0));
      }

      public void load(ByteBuffer var1, int var2) {
         super.load(var1, var2);
         this.gameObj = var1.get();
         this.maxStage = var1.get();
         this.spawnTime = var1.getShort();
         this.curID = var1.getInt();
         this.hasGrass = var1.get() == 1;
      }
   }
}
