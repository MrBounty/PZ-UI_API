package zombie.erosion.categories;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.debug.DebugLog;
import zombie.erosion.ErosionData;
import zombie.erosion.ErosionMain;
import zombie.erosion.ErosionRegions;
import zombie.erosion.obj.ErosionObj;
import zombie.erosion.season.ErosionSeason;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SpriteDetails.IsoFlagType;

public abstract class ErosionCategory {
   public int ID;
   public ErosionRegions.Region region;
   protected ErosionCategory.SeasonDisplay[] seasonDisp = new ErosionCategory.SeasonDisplay[6];

   public ErosionCategory() {
      for(int var1 = 0; var1 < 6; ++var1) {
         this.seasonDisp[var1] = new ErosionCategory.SeasonDisplay();
      }

   }

   protected ErosionCategory.Data getCatModData(ErosionData.Square var1) {
      for(int var2 = 0; var2 < var1.regions.size(); ++var2) {
         ErosionCategory.Data var3 = (ErosionCategory.Data)var1.regions.get(var2);
         if (var3.regionID == this.region.ID && var3.categoryID == this.ID) {
            return var3;
         }
      }

      return null;
   }

   protected ErosionCategory.Data setCatModData(ErosionData.Square var1) {
      ErosionCategory.Data var2 = this.getCatModData(var1);
      if (var2 == null) {
         var2 = this.allocData();
         var2.regionID = this.region.ID;
         var2.categoryID = this.ID;
         var1.regions.add(var2);
         if (var1.regions.size() > 5) {
            DebugLog.log("> 5 regions on a square");
         }
      }

      return var2;
   }

   protected IsoObject validWall(IsoGridSquare var1, boolean var2, boolean var3) {
      if (var1 == null) {
         return null;
      } else {
         IsoGridSquare var4 = var2 ? var1.getTileInDirection(IsoDirections.N) : var1.getTileInDirection(IsoDirections.W);
         Object var5 = null;
         if (var1.isWallTo(var4)) {
            if (var2 && var1.Is(IsoFlagType.cutN) && !var1.Is(IsoFlagType.canPathN) || !var2 && var1.Is(IsoFlagType.cutW) && !var1.Is(IsoFlagType.canPathW)) {
               var5 = var1.getWall(var2);
            }
         } else if (var3 && (var1.isWindowBlockedTo(var4) || var1.isWindowTo(var4))) {
            var5 = var1.getWindowTo(var4);
            if (var5 == null) {
               var5 = var1.getWall(var2);
            }
         }

         if (var5 != null) {
            if (var1.getZ() > 0) {
               String var6 = ((IsoObject)var5).getSprite().getName();
               return (IsoObject)(var6 != null && !var6.contains("roof") ? var5 : null);
            } else {
               return (IsoObject)var5;
            }
         } else {
            return null;
         }
      }
   }

   protected float clerp(float var1, float var2, float var3) {
      float var4 = (float)(1.0D - Math.cos((double)var1 * 3.141592653589793D)) / 2.0F;
      return var2 * (1.0F - var4) + var3 * var4;
   }

   protected int currentSeason(float var1, ErosionObj var2) {
      boolean var3 = false;
      ErosionSeason var4 = ErosionMain.getInstance().getSeasons();
      int var5 = var4.getSeason();
      float var6 = var4.getSeasonDay();
      float var7 = var4.getSeasonDays();
      float var8 = var7 / 2.0F;
      float var9 = var8 * var1;
      ErosionCategory.SeasonDisplay var10 = this.seasonDisp[var5];
      int var12;
      if (var10.split && var6 >= var8 + var9) {
         var12 = var10.season2;
      } else if ((!var10.split || !(var6 >= var9)) && !(var6 >= var7 * var1)) {
         ErosionCategory.SeasonDisplay var11;
         if (var5 == 5) {
            var11 = this.seasonDisp[4];
         } else if (var5 == 1) {
            var11 = this.seasonDisp[5];
         } else if (var5 == 2) {
            var11 = this.seasonDisp[1];
         } else {
            var11 = this.seasonDisp[2];
         }

         if (var11.split) {
            var12 = var11.season2;
         } else {
            var12 = var11.season1;
         }
      } else {
         var12 = var10.season1;
      }

      return var12;
   }

   protected boolean currentBloom(float var1, ErosionObj var2) {
      boolean var3 = false;
      ErosionSeason var4 = ErosionMain.getInstance().getSeasons();
      int var5 = var4.getSeason();
      if (var2.hasFlower && var5 == 2) {
         float var6 = var4.getSeasonDay();
         float var7 = var4.getSeasonDays();
         float var8 = var7 / 2.0F;
         float var9 = var8 * var1;
         float var10 = var7 - var9;
         float var11 = var6 - var9;
         float var12 = var10 * var2.bloomEnd;
         float var13 = var10 * var2.bloomStart;
         float var14 = (var12 - var13) / 2.0F;
         float var15 = var14 * var1;
         var12 = var13 + var14 + var15;
         var13 += var15;
         if (var11 >= var13 && var11 <= var12) {
            var3 = true;
         }
      }

      return var3;
   }

   public void updateObj(ErosionData.Square var1, ErosionCategory.Data var2, IsoGridSquare var3, ErosionObj var4, boolean var5, int var6, int var7, boolean var8) {
      if (!var2.hasSpawned) {
         if (!var4.placeObject(var3, var6, var5, var7, var8)) {
            this.clearCatModData(var1);
            return;
         }

         var2.hasSpawned = true;
      } else if (var2.stage != var6 || var2.dispSeason != var7 || var2.dispBloom != var8) {
         IsoObject var9 = var4.getObject(var3, false);
         if (var9 == null) {
            this.clearCatModData(var1);
            return;
         }

         var4.setStageObject(var6, var9, var7, var8);
      }

      var2.stage = var6;
      var2.dispSeason = var7;
      var2.dispBloom = var8;
   }

   protected void clearCatModData(ErosionData.Square var1) {
      for(int var2 = 0; var2 < var1.regions.size(); ++var2) {
         ErosionCategory.Data var3 = (ErosionCategory.Data)var1.regions.get(var2);
         if (var3.regionID == this.region.ID && var3.categoryID == this.ID) {
            var1.regions.remove(var2);
            return;
         }
      }

   }

   public abstract void init();

   public abstract boolean replaceExistingObject(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5);

   public abstract boolean validateSpawn(IsoGridSquare var1, ErosionData.Square var2, ErosionData.Chunk var3, boolean var4, boolean var5, boolean var6);

   public abstract void update(IsoGridSquare var1, ErosionData.Square var2, ErosionCategory.Data var3, ErosionData.Chunk var4, int var5);

   protected abstract ErosionCategory.Data allocData();

   public static ErosionCategory.Data loadCategoryData(ByteBuffer var0, int var1) {
      byte var2 = var0.get();
      byte var3 = var0.get();
      ErosionCategory var4 = ErosionRegions.getCategory(var2, var3);
      ErosionCategory.Data var5 = var4.allocData();
      var5.regionID = var2;
      var5.categoryID = var3;
      var5.load(var0, var1);
      return var5;
   }

   public abstract void getObjectNames(ArrayList var1);

   protected class SeasonDisplay {
      int season1;
      int season2;
      boolean split;
   }

   public static class Data {
      public int regionID;
      public int categoryID;
      public boolean doNothing;
      public boolean hasSpawned;
      public int stage;
      public int dispSeason;
      public boolean dispBloom;

      public void save(ByteBuffer var1) {
         byte var2 = 0;
         if (this.doNothing) {
            var2 = (byte)(var2 | 1);
         }

         if (this.hasSpawned) {
            var2 = (byte)(var2 | 2);
         }

         if (this.dispBloom) {
            var2 = (byte)(var2 | 4);
         }

         if (this.stage == 1) {
            var2 = (byte)(var2 | 8);
         } else if (this.stage == 2) {
            var2 = (byte)(var2 | 16);
         } else if (this.stage == 3) {
            var2 = (byte)(var2 | 32);
         } else if (this.stage == 4) {
            var2 = (byte)(var2 | 64);
         } else if (this.stage > 4) {
            var2 = (byte)(var2 | 128);
         }

         var1.put((byte)this.regionID);
         var1.put((byte)this.categoryID);
         var1.put((byte)this.dispSeason);
         var1.put(var2);
         if (this.stage > 4) {
            var1.put((byte)this.stage);
         }

      }

      public void load(ByteBuffer var1, int var2) {
         this.stage = 0;
         this.dispSeason = var1.get();
         byte var3 = var1.get();
         this.doNothing = (var3 & 1) != 0;
         this.hasSpawned = (var3 & 2) != 0;
         this.dispBloom = (var3 & 4) != 0;
         if ((var3 & 8) != 0) {
            this.stage = 1;
         } else if ((var3 & 16) != 0) {
            this.stage = 2;
         } else if ((var3 & 32) != 0) {
            this.stage = 3;
         } else if ((var3 & 64) != 0) {
            this.stage = 4;
         } else if ((var3 & 128) != 0) {
            this.stage = var1.get();
         }

      }
   }
}
