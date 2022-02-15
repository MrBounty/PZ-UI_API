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

public final class NatureGeneric extends ErosionCategory {
   private ArrayList objs = new ArrayList();
   private static final int GRASS = 0;
   private static final int FERNS = 1;
   private static final int GENERIC = 2;
   private ArrayList objsRef = new ArrayList();
   private int[] spawnChance = new int[100];

   public boolean replaceExistingObject(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5) {
      int var6 = var1.getObjects().size();

      for(int var7 = var6 - 1; var7 >= 1; --var7) {
         IsoObject var8 = (IsoObject)var1.getObjects().get(var7);
         IsoSprite var9 = var8.getSprite();
         if (var9 != null && var9.getName() != null && var9.getName().startsWith("blends_grassoverlays")) {
            float var10 = 0.3F;
            float var11 = 12.0F;
            if ("Forest".equals(var1.getZoneType())) {
               var10 = 0.5F;
               var11 = 6.0F;
            } else if ("DeepForest".equals(var1.getZoneType())) {
               var10 = 0.7F;
               var11 = 3.0F;
            }

            NatureGeneric.CategoryData var12 = (NatureGeneric.CategoryData)this.setCatModData(var2);
            ArrayList var13 = (ArrayList)this.objsRef.get(0);
            int var14 = var2.noiseMainInt;
            int var15 = var2.rand(var1.x, var1.y, 101);
            if ((float)var15 < (float)var14 / var11) {
               if (var2.magicNum < var10) {
                  var13 = (ArrayList)this.objsRef.get(1);
               } else {
                  var13 = (ArrayList)this.objsRef.get(2);
               }

               var12.notGrass = true;
               var12.maxStage = var14 > 60 ? 1 : 0;
            } else {
               var12.maxStage = var14 > 67 ? 2 : (var14 > 50 ? 1 : 0);
            }

            var12.gameObj = (Integer)var13.get(var2.rand(var1.x, var1.y, var13.size()));
            var12.stage = var12.maxStage;
            var12.spawnTime = 0;
            var12.dispSeason = -1;
            ErosionObj var16 = (ErosionObj)this.objs.get(var12.gameObj);
            var8.setName(var16.name);
            var8.doNotSync = true;
            var12.hasSpawned = true;
            return true;
         }
      }

      return false;
   }

   public boolean validateSpawn(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5, boolean var6) {
      if (var1.getObjects().size() > (var5 ? 2 : 1)) {
         return false;
      } else {
         int var7 = var2.noiseMainInt;
         if (var2.rand(var1.x, var1.y, 101) < this.spawnChance[var7]) {
            float var8 = 0.3F;
            float var9 = 12.0F;
            if ("Forest".equals(var1.getZoneType())) {
               var8 = 0.5F;
               var9 = 6.0F;
            } else if ("DeepForest".equals(var1.getZoneType())) {
               var8 = 0.7F;
               var9 = 3.0F;
            }

            NatureGeneric.CategoryData var10 = (NatureGeneric.CategoryData)this.setCatModData(var2);
            ArrayList var11 = (ArrayList)this.objsRef.get(0);
            int var12 = var2.rand(var1.x, var1.y, 101);
            if ((float)var12 < (float)var7 / var9) {
               if (var2.magicNum < var8) {
                  var11 = (ArrayList)this.objsRef.get(1);
               } else {
                  var11 = (ArrayList)this.objsRef.get(2);
               }

               var10.notGrass = true;
               var10.maxStage = var7 > 60 ? 1 : 0;
            } else {
               var10.maxStage = var7 > 67 ? 2 : (var7 > 50 ? 1 : 0);
            }

            var10.gameObj = (Integer)var11.get(var2.rand(var1.x, var1.y, var11.size()));
            var10.stage = 0;
            var10.spawnTime = 100 - var7;
            return true;
         } else {
            return false;
         }
      }
   }

   public void update(IsoGridSquare var1, ErosionData.Square var2, ErosionCategory.Data var3, ErosionData.Chunk var4, int var5) {
      NatureGeneric.CategoryData var6 = (NatureGeneric.CategoryData)var3;
      if (var5 >= var6.spawnTime && !var6.doNothing) {
         if (var6.gameObj >= 0 && var6.gameObj < this.objs.size()) {
            ErosionObj var7 = (ErosionObj)this.objs.get(var6.gameObj);
            int var8 = var6.maxStage;
            int var9 = (int)Math.floor((double)((float)(var5 - var6.spawnTime) / ((float)var7.cycleTime / ((float)var8 + 1.0F))));
            if (var9 > var8) {
               var9 = var8;
            }

            if (var9 >= var7.stages) {
               var9 = var7.stages - 1;
            }

            if (var6.stage == var6.maxStage) {
               var9 = var6.maxStage;
            }

            int var10 = 0;
            if (!var6.notGrass) {
               var10 = this.currentSeason(var2.magicNum, var7);
               int var11 = this.getGroundGrassType(var1);
               if (var11 == 2) {
                  var10 = Math.max(var10, 3);
               } else if (var11 == 3) {
                  var10 = Math.max(var10, 4);
               }
            }

            boolean var13 = false;
            boolean var12 = false;
            this.updateObj(var2, var3, var1, var7, var13, var9, var10, var12);
         } else {
            var6.doNothing = true;
         }

      }
   }

   public void init() {
      for(int var1 = 0; var1 < 100; ++var1) {
         this.spawnChance[var1] = (int)this.clerp((float)(var1 - 0) / 100.0F, 0.0F, 99.0F);
      }

      this.seasonDisp[5].season1 = 5;
      this.seasonDisp[5].season2 = 0;
      this.seasonDisp[5].split = false;
      this.seasonDisp[1].season1 = 1;
      this.seasonDisp[1].season2 = 0;
      this.seasonDisp[1].split = false;
      this.seasonDisp[2].season1 = 2;
      this.seasonDisp[2].season2 = 3;
      this.seasonDisp[2].split = true;
      this.seasonDisp[4].season1 = 4;
      this.seasonDisp[4].season2 = 5;
      this.seasonDisp[4].split = true;
      int[] var8 = new int[]{1, 2, 3, 4, 5};
      int[] var2 = new int[]{2, 1, 0};

      int var3;
      for(var3 = 0; var3 < 3; ++var3) {
         this.objsRef.add(new ArrayList());
      }

      ErosionObjSprites var4;
      int var5;
      int var6;
      int var7;
      ErosionObj var11;
      for(var3 = 0; var3 <= 5; ++var3) {
         var4 = new ErosionObjSprites(3, "Grass", false, false, false);

         for(var5 = 0; var5 < var8.length; ++var5) {
            for(var6 = 0; var6 < var2.length; ++var6) {
               var7 = 0 + var5 * 18 + var6 * 6 + var3;
               var4.setBase(var2[var6], "e_newgrass_1_" + var7, var8[var5]);
            }
         }

         var11 = new ErosionObj(var4, 60, 0.0F, 0.0F, false);
         this.objs.add(var11);
         ((ArrayList)this.objsRef.get(0)).add(this.objs.size() - 1);
      }

      for(var3 = 0; var3 <= 15; ++var3) {
         var4 = new ErosionObjSprites(2, "Generic", false, false, false);

         for(var5 = 0; var5 <= 1; ++var5) {
            var6 = var5 * 16 + var3;
            var4.setBase(var5, (String)("d_generic_1_" + var6), 0);
         }

         var11 = new ErosionObj(var4, 60, 0.0F, 0.0F, true);
         this.objs.add(var11);
         ((ArrayList)this.objsRef.get(2)).add(this.objs.size() - 1);
      }

      ErosionIceQueen var10 = ErosionIceQueen.instance;

      for(int var9 = 0; var9 <= 7; ++var9) {
         ErosionObjSprites var12 = new ErosionObjSprites(2, "Fern", true, false, false);

         for(var6 = 0; var6 <= 1; ++var6) {
            var7 = 48 + var6 * 32 + var9;
            var12.setBase(var6, (String)("d_generic_1_" + var7), 0);
            var10.addSprite("d_generic_1_" + var7, "d_generic_1_" + (var7 + 16));
         }

         ErosionObj var13 = new ErosionObj(var12, 60, 0.0F, 0.0F, true);
         this.objs.add(var13);
         ((ArrayList)this.objsRef.get(1)).add(this.objs.size() - 1);
      }

   }

   protected ErosionCategory.Data allocData() {
      return new NatureGeneric.CategoryData();
   }

   private int toInt(char var1) {
      switch(var1) {
      case '0':
         return 0;
      case '1':
         return 1;
      case '2':
         return 2;
      case '3':
         return 3;
      case '4':
         return 4;
      case '5':
         return 5;
      case '6':
         return 6;
      case '7':
         return 7;
      case '8':
         return 8;
      case '9':
         return 9;
      default:
         return 0;
      }
   }

   private int getGroundGrassType(IsoGridSquare var1) {
      IsoObject var2 = var1.getFloor();
      if (var2 == null) {
         return 0;
      } else {
         IsoSprite var3 = var2.getSprite();
         if (var3 != null && var3.getName() != null && var3.getName().startsWith("blends_natural_01_")) {
            int var4 = 0;

            int var5;
            for(var5 = 18; var5 < var3.getName().length(); ++var5) {
               var4 += this.toInt(var3.getName().charAt(var5));
               if (var5 < var3.getName().length() - 1) {
                  var4 *= 10;
               }
            }

            var5 = var4 / 8;
            int var6 = var4 % 8;
            if (var5 == 2 && (var6 == 0 || var6 >= 5)) {
               return 1;
            }

            if (var5 == 4 && (var6 == 0 || var6 >= 5)) {
               return 2;
            }

            if (var5 == 6 && (var6 == 0 || var6 >= 5)) {
               return 3;
            }
         }

         return 0;
      }
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
      public boolean notGrass;

      public void save(ByteBuffer var1) {
         super.save(var1);
         var1.put((byte)this.gameObj);
         var1.put((byte)this.maxStage);
         var1.putShort((short)this.spawnTime);
         var1.put((byte)(this.notGrass ? 1 : 0));
      }

      public void load(ByteBuffer var1, int var2) {
         super.load(var1, var2);
         this.gameObj = var1.get();
         this.maxStage = var1.get();
         this.spawnTime = var1.getShort();
         this.notGrass = var1.get() == 1;
      }
   }
}
