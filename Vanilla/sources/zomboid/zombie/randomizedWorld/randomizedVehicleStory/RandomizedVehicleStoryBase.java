package zombie.randomizedWorld.randomizedVehicleStory;

import java.util.HashMap;
import java.util.Iterator;
import org.joml.Vector2f;
import zombie.SandboxOptions;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.randomizedWorld.RandomizedWorldBase;
import zombie.vehicles.BaseVehicle;

public class RandomizedVehicleStoryBase extends RandomizedWorldBase {
   private int chance = 0;
   private static int totalChance = 0;
   private static HashMap rvsMap = new HashMap();
   protected boolean horizontalZone = false;
   protected int zoneWidth = 0;
   public static final float baseChance = 12.5F;
   protected int minX = 0;
   protected int minY = 0;
   protected int maxX = 0;
   protected int maxY = 0;
   protected int minZoneWidth = 0;
   protected int minZoneHeight = 0;

   public static void initAllRVSMapChance(IsoMetaGrid.Zone var0, IsoChunk var1) {
      totalChance = 0;
      rvsMap.clear();

      for(int var2 = 0; var2 < IsoWorld.instance.getRandomizedVehicleStoryList().size(); ++var2) {
         RandomizedVehicleStoryBase var3 = (RandomizedVehicleStoryBase)IsoWorld.instance.getRandomizedVehicleStoryList().get(var2);
         if (var3.isValid(var0, var1, false) && var3.isTimeValid(false)) {
            totalChance += var3.getChance();
            rvsMap.put(var3, var3.getChance());
         }
      }

   }

   public static boolean doRandomStory(IsoMetaGrid.Zone var0, IsoChunk var1, boolean var2) {
      float var3 = Rand.Next(0.0F, 500.0F);
      switch(SandboxOptions.instance.VehicleStoryChance.getValue()) {
      case 1:
         return false;
      case 2:
         var3 = Rand.Next(0.0F, 1000.0F);
      case 3:
      default:
         break;
      case 4:
         var3 = Rand.Next(0.0F, 300.0F);
         break;
      case 5:
         var3 = Rand.Next(0.0F, 175.0F);
         break;
      case 6:
         var3 = Rand.Next(0.0F, 50.0F);
      }

      if (var3 < 12.5F) {
         if (!var1.vehicles.isEmpty()) {
            return false;
         } else {
            RandomizedVehicleStoryBase var4 = null;
            initAllRVSMapChance(var0, var1);
            var4 = getRandomStory();
            if (var4 == null) {
               return false;
            } else {
               VehicleStorySpawnData var5 = var4.initSpawnDataForChunk(var0, var1);
               var1.setRandomVehicleStoryToSpawnLater(var5);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static RandomizedVehicleStoryBase getRandomStory() {
      int var0 = Rand.Next(totalChance);
      Iterator var1 = rvsMap.keySet().iterator();
      int var2 = 0;

      RandomizedVehicleStoryBase var3;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var3 = (RandomizedVehicleStoryBase)var1.next();
         var2 += (Integer)rvsMap.get(var3);
      } while(var0 >= var2);

      return var3;
   }

   public int getMinZoneWidth() {
      return this.minZoneWidth <= 0 ? 10 : this.minZoneWidth;
   }

   public int getMinZoneHeight() {
      return this.minZoneHeight <= 0 ? 5 : this.minZoneHeight;
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
   }

   public IsoGridSquare getCenterOfChunk(IsoMetaGrid.Zone var1, IsoChunk var2) {
      int var3 = Math.max(var1.x, var2.wx * 10);
      int var4 = Math.max(var1.y, var2.wy * 10);
      int var5 = Math.min(var1.x + var1.w, (var2.wx + 1) * 10);
      int var6 = Math.min(var1.y + var1.h, (var2.wy + 1) * 10);
      boolean var7 = false;
      boolean var8 = false;
      int var9;
      int var10;
      if (this.horizontalZone) {
         var10 = (var1.y + var1.y + var1.h) / 2;
         var9 = (var3 + var5) / 2;
      } else {
         var10 = (var4 + var6) / 2;
         var9 = (var1.x + var1.x + var1.w) / 2;
      }

      return IsoCell.getInstance().getGridSquare(var9, var10, var1.z);
   }

   public boolean isValid(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      this.horizontalZone = false;
      this.zoneWidth = 0;
      this.debugLine = "";
      if (!var3 && var1.hourLastSeen != 0) {
         return false;
      } else if (!var3 && var1.haveConstruction) {
         return false;
      } else if (!"Nav".equals(var1.getType())) {
         this.debugLine = this.debugLine + "Not a 'Nav' zone.";
         return false;
      } else {
         this.minX = Math.max(var1.x, var2.wx * 10);
         this.minY = Math.max(var1.y, var2.wy * 10);
         this.maxX = Math.min(var1.x + var1.w, (var2.wx + 1) * 10);
         this.maxY = Math.min(var1.y + var1.h, (var2.wy + 1) * 10);
         return this.getSpawnPoint(var1, var2, (float[])null);
      }
   }

   public VehicleStorySpawnData initSpawnDataForChunk(IsoMetaGrid.Zone var1, IsoChunk var2) {
      int var3 = this.getMinZoneWidth();
      int var4 = this.getMinZoneHeight();
      float[] var5 = new float[3];
      if (!this.getSpawnPoint(var1, var2, var5)) {
         return null;
      } else {
         float var6 = var5[0];
         float var7 = var5[1];
         float var8 = var5[2];
         int[] var9 = new int[4];
         VehicleStorySpawner.getInstance().getAABB(var6, var7, (float)var3, (float)var4, var8, var9);
         return new VehicleStorySpawnData(this, var1, var6, var7, var8, var9[0], var9[1], var9[2], var9[3]);
      }
   }

   public boolean getSpawnPoint(IsoMetaGrid.Zone var1, IsoChunk var2, float[] var3) {
      return this.getRectangleSpawnPoint(var1, var2, var3) || this.getPolylineSpawnPoint(var1, var2, var3);
   }

   public boolean getRectangleSpawnPoint(IsoMetaGrid.Zone var1, IsoChunk var2, float[] var3) {
      if (!var1.isRectangle()) {
         return false;
      } else {
         int var4 = this.getMinZoneWidth();
         int var5 = this.getMinZoneHeight();
         int var10001;
         float var6;
         float var7;
         float var8;
         if (var1.w > 30 && var1.h < 15) {
            this.horizontalZone = true;
            this.zoneWidth = var1.h;
            if (var1.getWidth() < var5) {
               var10001 = var1.getWidth();
               this.debugLine = "Horizontal street is too small, w:" + var10001 + " h:" + var1.getHeight();
               return false;
            } else if (var1.getHeight() < var4) {
               var10001 = var1.getWidth();
               this.debugLine = "Horizontal street is too small, w:" + var10001 + " h:" + var1.getHeight();
               return false;
            } else if (var3 == null) {
               return true;
            } else {
               var6 = (float)var1.getX();
               var7 = (float)(var1.getX() + var1.getWidth());
               var8 = (float)var1.getY() + (float)var1.getHeight() / 2.0F;
               var3[0] = PZMath.clamp((float)(var2.wx * 10) + 5.0F, var6 + (float)var5 / 2.0F, var7 - (float)var5 / 2.0F);
               var3[1] = var8;
               var3[2] = Vector2.getDirection(var7 - var6, 0.0F);
               return true;
            }
         } else if (var1.h > 30 && var1.w < 15) {
            this.horizontalZone = false;
            this.zoneWidth = var1.w;
            if (var1.getWidth() < var4) {
               var10001 = var1.getWidth();
               this.debugLine = "Vertical street is too small, w:" + var10001 + " h:" + var1.getHeight();
               return false;
            } else if (var1.getHeight() < var5) {
               var10001 = var1.getWidth();
               this.debugLine = "Vertical street is too small, w:" + var10001 + " h:" + var1.getHeight();
               return false;
            } else if (var3 == null) {
               return true;
            } else {
               var6 = (float)var1.getY();
               var7 = (float)(var1.getY() + var1.getHeight());
               var8 = (float)var1.getX() + (float)var1.getWidth() / 2.0F;
               var3[0] = var8;
               var3[1] = PZMath.clamp((float)(var2.wy * 10) + 5.0F, var6 + (float)var5 / 2.0F, var7 - (float)var5 / 2.0F);
               var3[2] = Vector2.getDirection(0.0F, var6 - var7);
               return true;
            }
         } else {
            this.debugLine = "Zone too small or too large";
            return false;
         }
      }
   }

   public boolean getPolylineSpawnPoint(IsoMetaGrid.Zone var1, IsoChunk var2, float[] var3) {
      if (var1.isPolyline() && var1.polylineWidth > 0) {
         int var4 = this.getMinZoneWidth();
         int var5 = this.getMinZoneHeight();
         if (var1.polylineWidth < var4) {
            this.debugLine = "Polyline zone is too narrow, width:" + var1.polylineWidth;
            return false;
         } else {
            double[] var6 = new double[2];
            int var7 = var1.getClippedSegmentOfPolyline(var2.wx * 10, var2.wy * 10, (var2.wx + 1) * 10, (var2.wy + 1) * 10, var6);
            if (var7 == -1) {
               return false;
            } else {
               double var8 = var6[0];
               double var10 = var6[1];
               float var12 = var1.polylineWidth % 2 == 0 ? 0.0F : 0.5F;
               float var13 = (float)var1.points.get(var7 * 2) + var12;
               float var14 = (float)var1.points.get(var7 * 2 + 1) + var12;
               float var15 = (float)var1.points.get(var7 * 2 + 2) + var12;
               float var16 = (float)var1.points.get(var7 * 2 + 3) + var12;
               float var17 = var15 - var13;
               float var18 = var16 - var14;
               float var19 = Vector2f.length(var17, var18);
               if (var19 < (float)var5) {
                  return false;
               } else {
                  this.zoneWidth = var1.polylineWidth;
                  if (var3 == null) {
                     return true;
                  } else {
                     float var20 = (float)var5 / 2.0F / var19;
                     float var21 = PZMath.max((float)var8 - var20, var20);
                     float var22 = PZMath.min((float)var10 + var20, 1.0F - var20);
                     float var23 = var13 + var17 * var21;
                     float var24 = var14 + var18 * var21;
                     float var25 = var13 + var17 * var22;
                     float var26 = var14 + var18 * var22;
                     float var27 = Rand.Next(0.0F, 1.0F);
                     if (Core.bDebug) {
                        var27 = (float)(System.currentTimeMillis() / 20L % 360L) / 360.0F;
                     }

                     var3[0] = var23 + (var25 - var23) * var27;
                     var3[1] = var24 + (var26 - var24) * var27;
                     var3[2] = Vector2.getDirection(var17, var18);
                     return true;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean isFullyStreamedIn(int var1, int var2, int var3, int var4) {
      byte var5 = 10;
      int var6 = var1 / var5;
      int var7 = var2 / var5;
      int var8 = (var3 - 1) / var5;
      int var9 = (var4 - 1) / var5;

      for(int var10 = var7; var10 <= var9; ++var10) {
         for(int var11 = var6; var11 <= var8; ++var11) {
            if (!this.isChunkLoaded(var11, var10)) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isChunkLoaded(int var1, int var2) {
      IsoChunk var3 = GameServer.bServer ? ServerMap.instance.getChunk(var1, var2) : IsoWorld.instance.CurrentCell.getChunk(var1, var2);
      return var3 != null && var3.bLoaded;
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      return false;
   }

   public boolean callVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, float var3) {
      float[] var4 = new float[3];
      if (!this.getSpawnPoint(var1, var2, var4)) {
         return false;
      } else {
         this.initVehicleStorySpawner(var1, var2, false);
         VehicleStorySpawner var5 = VehicleStorySpawner.getInstance();
         float var6 = var4[2];
         if (Rand.NextBool(2)) {
            var6 += 3.1415927F;
         }

         var6 += var3;
         ++var6;
         var5.spawn(var4[0], var4[1], 0.0F, var6, this::spawnElement);
         return true;
      }
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
   }

   public BaseVehicle[] addSmashedOverlay(BaseVehicle var1, BaseVehicle var2, int var3, int var4, boolean var5, boolean var6) {
      IsoDirections var7 = var1.getDir();
      IsoDirections var8 = var2.getDir();
      String var9 = null;
      String var10 = null;
      if (!var5) {
         var9 = "Front";
         if (var8 == IsoDirections.W) {
            if (var7 == IsoDirections.S) {
               var10 = "Right";
            } else {
               var10 = "Left";
            }
         } else if (var7 == IsoDirections.S) {
            var10 = "Left";
         } else {
            var10 = "Right";
         }
      } else {
         if (var7 == IsoDirections.S) {
            if (var3 > 0) {
               var9 = "Left";
            } else {
               var9 = "Right";
            }
         } else if (var3 < 0) {
            var9 = "Left";
         } else {
            var9 = "Right";
         }

         var10 = "Front";
      }

      var1 = var1.setSmashed(var9);
      var2 = var2.setSmashed(var10);
      if (var6) {
         var1.setBloodIntensity(var9, 1.0F);
         var2.setBloodIntensity(var10, 1.0F);
      }

      return new BaseVehicle[]{var1, var2};
   }

   public int getChance() {
      return this.chance;
   }

   public void setChance(int var1) {
      this.chance = var1;
   }

   public int getMinimumDays() {
      return this.minimumDays;
   }

   public void setMinimumDays(int var1) {
      this.minimumDays = var1;
   }

   public int getMaximumDays() {
      return this.maximumDays;
   }

   public void setMaximumDays(int var1) {
      this.maximumDays = var1;
   }

   public String getName() {
      return this.name;
   }

   public String getDebugLine() {
      return this.debugLine;
   }

   public void registerCustomOutfits() {
   }
}
