package zombie.erosion.categories;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import zombie.erosion.ErosionData;
import zombie.erosion.ErosionMain;
import zombie.erosion.obj.ErosionObjOverlay;
import zombie.erosion.obj.ErosionObjOverlaySprites;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;

public final class WallVines extends ErosionCategory {
   private ArrayList objs = new ArrayList();
   private static final int DIRNW = 0;
   private static final int DIRN = 1;
   private static final int DIRW = 2;
   private int[][] objsRef = new int[3][2];
   private HashMap spriteToObj = new HashMap();
   private HashMap spriteToStage = new HashMap();
   private int[] spawnChance = new int[100];

   public boolean replaceExistingObject(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5) {
      int var6 = var1.getObjects().size();

      for(int var7 = var6 - 1; var7 >= 1; --var7) {
         IsoObject var8 = (IsoObject)var1.getObjects().get(var7);
         if (var8.AttachedAnimSprite != null) {
            for(int var9 = 0; var9 < var8.AttachedAnimSprite.size(); ++var9) {
               IsoSprite var10 = ((IsoSpriteInstance)var8.AttachedAnimSprite.get(var9)).parentSprite;
               if (var10 != null && var10.getName() != null && var10.getName().startsWith("f_wallvines_1_") && this.spriteToObj.containsKey(var10.getName())) {
                  WallVines.CategoryData var11 = (WallVines.CategoryData)this.setCatModData(var2);
                  var11.gameObj = (Integer)this.spriteToObj.get(var10.getName());
                  int var12 = (Integer)this.spriteToStage.get(var10.getName());
                  var11.stage = var12;
                  var11.maxStage = 2;
                  var11.spawnTime = 0;
                  var8.AttachedAnimSprite.remove(var9);
                  if (var8.AttachedAnimSprite != null && var9 < var8.AttachedAnimSprite.size()) {
                     var8.AttachedAnimSprite.remove(var9);
                  }

                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean validateSpawn(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5, boolean var6) {
      if (!var4) {
         return false;
      } else {
         int var7 = var2.noiseMainInt;
         int var8 = this.spawnChance[var7];
         if (var8 == 0) {
            return false;
         } else if (var2.rand(var1.x, var1.y, 101) >= var8) {
            return false;
         } else {
            boolean var9 = true;
            IsoObject var10 = this.validWall(var1, true, true);
            IsoObject var11 = this.validWall(var1, false, true);
            byte var17;
            if (var10 != null && var11 != null) {
               var17 = 0;
            } else if (var10 != null) {
               var17 = 1;
            } else {
               if (var11 == null) {
                  return false;
               }

               var17 = 2;
            }

            WallVines.CategoryData var12 = (WallVines.CategoryData)this.setCatModData(var2);
            var12.gameObj = this.objsRef[var17][var2.rand(var1.x, var1.y, this.objsRef[var17].length)];
            var12.maxStage = var7 > 65 ? 3 : (var7 > 60 ? 2 : (var7 > 55 ? 1 : 0));
            var12.stage = 0;
            var12.spawnTime = 100 - var7;
            if (var12.maxStage == 3) {
               IsoGridSquare var13 = IsoWorld.instance.CurrentCell.getGridSquare(var1.getX(), var1.getY(), var1.getZ() + 1);
               if (var13 != null) {
                  IsoObject var14 = this.validWall(var13, var17 == 1, true);
                  ErosionObjOverlay var15 = (ErosionObjOverlay)this.objs.get(var12.gameObj);
                  if (var14 != null && var15 != null) {
                     WallVines.CategoryData var16 = new WallVines.CategoryData();
                     var16.gameObj = this.objsRef[var17][var2.rand(var1.x, var1.y, this.objsRef[var17].length)];
                     var16.maxStage = var7 > 75 ? 2 : (var7 > 70 ? 1 : 0);
                     var16.stage = 0;
                     var16.spawnTime = var12.spawnTime + (int)((float)var15.cycleTime / ((float)var12.maxStage + 1.0F) * 4.0F);
                     var12.hasTop = var16;
                  } else {
                     var12.maxStage = 2;
                  }
               } else {
                  var12.maxStage = 2;
               }
            }

            return true;
         }
      }
   }

   public void update(IsoGridSquare var1, ErosionData.Square var2, ErosionCategory.Data var3, ErosionData.Chunk var4, int var5) {
      WallVines.CategoryData var6 = (WallVines.CategoryData)var3;
      if (var5 >= var6.spawnTime && !var6.doNothing) {
         if (var6.gameObj >= 0 && var6.gameObj < this.objs.size()) {
            ErosionObjOverlay var7 = (ErosionObjOverlay)this.objs.get(var6.gameObj);
            int var8 = var6.maxStage;
            int var9 = (int)Math.floor((double)((float)(var5 - var6.spawnTime) / ((float)var7.cycleTime / ((float)var8 + 1.0F))));
            if (var9 < var6.stage) {
               var9 = var6.stage;
            }

            if (var9 > var8) {
               var9 = var8;
            }

            if (var9 > var7.stages) {
               var9 = var7.stages;
            }

            if (var9 == 3 && var6.hasTop != null && var6.hasTop.spawnTime > var5) {
               var9 = 2;
            }

            int var10 = ErosionMain.getInstance().getSeasons().getSeason();
            if (var9 != var6.stage || var6.dispSeason != var10) {
               IsoObject var11 = null;
               IsoObject var12 = this.validWall(var1, true, true);
               IsoObject var13 = this.validWall(var1, false, true);
               if (var12 != null && var13 != null) {
                  var11 = var12;
               } else if (var12 != null) {
                  var11 = var12;
               } else if (var13 != null) {
                  var11 = var13;
               }

               var6.dispSeason = var10;
               if (var11 != null) {
                  int var14 = var6.curID;
                  int var15 = var7.setOverlay(var11, var14, var9, var10, 0.0F);
                  if (var15 >= 0) {
                     var6.curID = var15;
                  }
               } else {
                  var6.doNothing = true;
               }

               if (var9 == 3 && var6.hasTop != null) {
                  IsoGridSquare var16 = IsoWorld.instance.CurrentCell.getGridSquare(var1.getX(), var1.getY(), var1.getZ() + 1);
                  if (var16 != null) {
                     this.update(var16, var2, var6.hasTop, var4, var5);
                  }
               }
            }
         } else {
            var6.doNothing = true;
         }

      }
   }

   public void init() {
      for(int var1 = 0; var1 < 100; ++var1) {
         this.spawnChance[var1] = var1 >= 50 ? 100 : 0;
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
      String var10 = "f_wallvines_1_";
      int[] var2 = new int[]{5, 2, 4, 1};
      int[] var3 = new int[]{2, 2, 1, 1, 0, 0};
      int[] var4 = new int[3];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         ErosionObjOverlaySprites var6 = new ErosionObjOverlaySprites(4, "WallVines");

         for(int var7 = 0; var7 <= 3; ++var7) {
            for(int var8 = 0; var8 <= 2; ++var8) {
               int var9 = var8 * 24 + var7 * 6 + var5;
               var6.setSprite(var7, var10 + var9, var2[var8]);
               if (var8 == 2) {
                  var6.setSprite(var7, var10 + var9, var2[var8 + 1]);
               }

               this.spriteToObj.put(var10 + var9, this.objs.size());
               this.spriteToStage.put(var10 + var9, var7);
            }
         }

         this.objs.add(new ErosionObjOverlay(var6, 60, false));
         int[] var10000 = this.objsRef[var3[var5]];
         int var10002 = var3[var5];
         int var10004 = var4[var3[var5]];
         var4[var10002] = var4[var3[var5]] + 1;
         var10000[var10004] = this.objs.size() - 1;
      }

   }

   protected ErosionCategory.Data allocData() {
      return new WallVines.CategoryData();
   }

   public void getObjectNames(ArrayList var1) {
      for(int var2 = 0; var2 < this.objs.size(); ++var2) {
         if (((ErosionObjOverlay)this.objs.get(var2)).name != null && !var1.contains(((ErosionObjOverlay)this.objs.get(var2)).name)) {
            var1.add(((ErosionObjOverlay)this.objs.get(var2)).name);
         }
      }

   }

   private static final class CategoryData extends ErosionCategory.Data {
      public int gameObj;
      public int maxStage;
      public int spawnTime;
      public int curID = -999999;
      public WallVines.CategoryData hasTop;

      public void save(ByteBuffer var1) {
         super.save(var1);
         var1.put((byte)this.gameObj);
         var1.put((byte)this.maxStage);
         var1.putShort((short)this.spawnTime);
         var1.putInt(this.curID);
         if (this.hasTop != null) {
            var1.put((byte)1);
            var1.put((byte)this.hasTop.gameObj);
            var1.putShort((short)this.hasTop.spawnTime);
            var1.putInt(this.hasTop.curID);
         } else {
            var1.put((byte)0);
         }

      }

      public void load(ByteBuffer var1, int var2) {
         super.load(var1, var2);
         this.gameObj = var1.get();
         this.maxStage = var1.get();
         this.spawnTime = var1.getShort();
         this.curID = var1.getInt();
         boolean var3 = var1.get() == 1;
         if (var3) {
            this.hasTop = new WallVines.CategoryData();
            this.hasTop.gameObj = var1.get();
            this.hasTop.spawnTime = var1.getShort();
            this.hasTop.curID = var1.getInt();
         }

      }
   }
}
