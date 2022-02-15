package zombie.iso.weather;

import java.util.ArrayList;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderSettings;
import zombie.debug.LineDrawer;
import zombie.iso.IsoUtils;
import zombie.iso.Vector2;
import zombie.iso.weather.fx.SteppedUpdateFloat;

public class WorldFlares {
   public static final boolean ENABLED = true;
   public static boolean DEBUG_DRAW = false;
   public static int NEXT_ID = 0;
   private static ArrayList flares = new ArrayList();

   public static void Clear() {
      flares.clear();
   }

   public static int getFlareCount() {
      return flares.size();
   }

   public static WorldFlares.Flare getFlare(int var0) {
      return (WorldFlares.Flare)flares.get(var0);
   }

   public static WorldFlares.Flare getFlareID(int var0) {
      for(int var1 = 0; var1 < flares.size(); ++var1) {
         if (((WorldFlares.Flare)flares.get(var1)).id == var0) {
            return (WorldFlares.Flare)flares.get(var1);
         }
      }

      return null;
   }

   public static void launchFlare(float var0, int var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      if (flares.size() > 100) {
         flares.remove(0);
      }

      WorldFlares.Flare var11 = new WorldFlares.Flare();
      var11.id = NEXT_ID++;
      var11.x = (float)var1;
      var11.y = (float)var2;
      var11.range = var3;
      var11.windSpeed = var4;
      var11.color.setExterior(var5, var6, var7, 1.0F);
      var11.color.setInterior(var8, var9, var10, 1.0F);
      var11.hasLaunched = true;
      var11.maxLifeTime = var0;
      flares.add(var11);
   }

   public static void update() {
      for(int var0 = flares.size() - 1; var0 >= 0; --var0) {
         ((WorldFlares.Flare)flares.get(var0)).update();
         if (!((WorldFlares.Flare)flares.get(var0)).hasLaunched) {
            flares.remove(var0);
         }
      }

   }

   public static void applyFlaresForPlayer(RenderSettings.PlayerRenderSettings var0, int var1, IsoPlayer var2) {
      for(int var3 = flares.size() - 1; var3 >= 0; --var3) {
         if (((WorldFlares.Flare)flares.get(var3)).hasLaunched) {
            ((WorldFlares.Flare)flares.get(var3)).applyFlare(var0, var1, var2);
         }
      }

   }

   public static void setDebugDraw(boolean var0) {
      DEBUG_DRAW = var0;
   }

   public static boolean getDebugDraw() {
      return DEBUG_DRAW;
   }

   public static void debugRender() {
      if (DEBUG_DRAW) {
         float var0 = 0.0F;

         for(int var1 = flares.size() - 1; var1 >= 0; --var1) {
            WorldFlares.Flare var2 = (WorldFlares.Flare)flares.get(var1);
            float var3 = 0.5F;

            for(double var4 = 0.0D; var4 < 6.283185307179586D; var4 += 0.15707963267948966D) {
               DrawIsoLine(var2.x + (float)var2.range * (float)Math.cos(var4), var2.y + (float)var2.range * (float)Math.sin(var4), var2.x + (float)var2.range * (float)Math.cos(var4 + 0.15707963267948966D), var2.y + (float)var2.range * (float)Math.sin(var4 + 0.15707963267948966D), var0, 1.0F, 1.0F, 1.0F, 0.25F, 1);
               DrawIsoLine(var2.x + var3 * (float)Math.cos(var4), var2.y + var3 * (float)Math.sin(var4), var2.x + var3 * (float)Math.cos(var4 + 0.15707963267948966D), var2.y + var3 * (float)Math.sin(var4 + 0.15707963267948966D), var0, 1.0F, 1.0F, 1.0F, 0.25F, 1);
            }
         }

      }
   }

   private static void DrawIsoLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      float var10 = IsoUtils.XToScreenExact(var0, var1, var4, 0);
      float var11 = IsoUtils.YToScreenExact(var0, var1, var4, 0);
      float var12 = IsoUtils.XToScreenExact(var2, var3, var4, 0);
      float var13 = IsoUtils.YToScreenExact(var2, var3, var4, 0);
      LineDrawer.drawLine(var10, var11, var12, var13, var5, var6, var7, var8, var9);
   }

   public static class Flare {
      private int id;
      private float x;
      private float y;
      private int range;
      private float windSpeed = 0.0F;
      private ClimateColorInfo color = new ClimateColorInfo(1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F);
      private boolean hasLaunched = false;
      private SteppedUpdateFloat intensity = new SteppedUpdateFloat(0.0F, 0.01F, 0.0F, 1.0F);
      private float maxLifeTime;
      private float lifeTime;
      private int nextRandomTargetIntens = 10;
      private float perc = 0.0F;
      private WorldFlares.PlayerFlareLightInfo[] infos = new WorldFlares.PlayerFlareLightInfo[4];

      public Flare() {
         for(int var1 = 0; var1 < this.infos.length; ++var1) {
            this.infos[var1] = new WorldFlares.PlayerFlareLightInfo();
         }

      }

      public int getId() {
         return this.id;
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public int getRange() {
         return this.range;
      }

      public float getWindSpeed() {
         return this.windSpeed;
      }

      public ClimateColorInfo getColor() {
         return this.color;
      }

      public boolean isHasLaunched() {
         return this.hasLaunched;
      }

      public float getIntensity() {
         return this.intensity.value();
      }

      public float getMaxLifeTime() {
         return this.maxLifeTime;
      }

      public float getLifeTime() {
         return this.lifeTime;
      }

      public float getPercent() {
         return this.perc;
      }

      public float getIntensityPlayer(int var1) {
         return this.infos[var1].intensity;
      }

      public float getLerpPlayer(int var1) {
         return this.infos[var1].lerp;
      }

      public float getDistModPlayer(int var1) {
         return this.infos[var1].distMod;
      }

      public ClimateColorInfo getColorPlayer(int var1) {
         return this.infos[var1].flareCol;
      }

      public ClimateColorInfo getOutColorPlayer(int var1) {
         return this.infos[var1].outColor;
      }

      private int GetDistance(int var1, int var2, int var3, int var4) {
         return (int)Math.sqrt(Math.pow((double)(var1 - var3), 2.0D) + Math.pow((double)(var2 - var4), 2.0D));
      }

      private void update() {
         if (this.hasLaunched) {
            if (this.lifeTime > this.maxLifeTime) {
               this.hasLaunched = false;
               return;
            }

            this.perc = this.lifeTime / this.maxLifeTime;
            this.nextRandomTargetIntens = (int)((float)this.nextRandomTargetIntens - GameTime.instance.getMultiplier());
            if (this.nextRandomTargetIntens <= 0) {
               this.intensity.setTarget(Rand.Next(0.8F, 1.0F));
               this.nextRandomTargetIntens = Rand.Next(5, 30);
            }

            this.intensity.update(GameTime.instance.getMultiplier());
            if (this.windSpeed > 0.0F) {
               Vector2 var1 = new Vector2(this.windSpeed / 60.0F * ClimateManager.getInstance().getWindIntensity() * (float)Math.sin((double)ClimateManager.getInstance().getWindAngleRadians()), this.windSpeed / 60.0F * ClimateManager.getInstance().getWindIntensity() * (float)Math.cos((double)ClimateManager.getInstance().getWindAngleRadians()));
               this.x += var1.x * GameTime.instance.getMultiplier();
               this.y += var1.y * GameTime.instance.getMultiplier();
            }

            for(int var6 = 0; var6 < 4; ++var6) {
               WorldFlares.PlayerFlareLightInfo var2 = this.infos[var6];
               IsoPlayer var3 = IsoPlayer.players[var6];
               if (var3 == null) {
                  var2.intensity = 0.0F;
               } else {
                  int var4 = this.GetDistance((int)this.x, (int)this.y, (int)var3.getX(), (int)var3.getY());
                  if (var4 > this.range) {
                     var2.intensity = 0.0F;
                     var2.lerp = 1.0F;
                  } else {
                     var2.distMod = 1.0F - (float)var4 / (float)this.range;
                     if (this.perc < 0.75F) {
                        var2.lerp = 0.0F;
                     } else {
                        var2.lerp = (this.perc - 0.75F) / 0.25F;
                     }

                     var2.intensity = this.intensity.value();
                  }

                  float var5 = (1.0F - var2.lerp) * var2.distMod * var2.intensity;
                  ClimateManager.ClimateFloat var10000 = ClimateManager.getInstance().dayLightStrength;
                  var10000.finalValue += (1.0F - ClimateManager.getInstance().dayLightStrength.finalValue) * var5;
                  if (var3 != null) {
                     var3.dirtyRecalcGridStackTime = 1.0F;
                  }
               }
            }

            this.lifeTime += GameTime.instance.getMultiplier();
         }

      }

      private void applyFlare(RenderSettings.PlayerRenderSettings var1, int var2, IsoPlayer var3) {
         WorldFlares.PlayerFlareLightInfo var4 = this.infos[var2];
         if (var4.distMod > 0.0F) {
            float var5 = 1.0F - var1.CM_DayLightStrength;
            var5 = var1.CM_NightStrength > var5 ? var1.CM_NightStrength : var5;
            var5 = PZMath.clamp(var5 * 2.0F, 0.0F, 1.0F);
            float var6 = 1.0F - var4.lerp;
            var6 *= var4.distMod;
            ClimateColorInfo var7 = var1.CM_GlobalLight;
            var4.outColor.setTo(var7);
            Color var10000 = var4.outColor.getExterior();
            float var10003 = var5 * var6 * var4.intensity;
            var10000.g = var4.outColor.getExterior().g * (1.0F - var10003 * 0.5F);
            var10000 = var4.outColor.getInterior();
            var10003 = var5 * var6 * var4.intensity;
            var10000.g = var4.outColor.getInterior().g * (1.0F - var10003 * 0.5F);
            var10000 = var4.outColor.getExterior();
            var10003 = var5 * var6 * var4.intensity;
            var10000.b = var4.outColor.getExterior().b * (1.0F - var10003 * 0.8F);
            var10000 = var4.outColor.getInterior();
            var10003 = var5 * var6 * var4.intensity;
            var10000.b = var4.outColor.getInterior().b * (1.0F - var10003 * 0.8F);
            var4.flareCol.setTo(this.color);
            var4.flareCol.scale(var5);
            var4.flareCol.getExterior().a = 1.0F;
            var4.flareCol.getInterior().a = 1.0F;
            var4.outColor.getExterior().r = var4.outColor.getExterior().r > var4.flareCol.getExterior().r ? var4.outColor.getExterior().r : var4.flareCol.getExterior().r;
            var4.outColor.getExterior().g = var4.outColor.getExterior().g > var4.flareCol.getExterior().g ? var4.outColor.getExterior().g : var4.flareCol.getExterior().g;
            var4.outColor.getExterior().b = var4.outColor.getExterior().b > var4.flareCol.getExterior().b ? var4.outColor.getExterior().b : var4.flareCol.getExterior().b;
            var4.outColor.getExterior().a = var4.outColor.getExterior().a > var4.flareCol.getExterior().a ? var4.outColor.getExterior().a : var4.flareCol.getExterior().a;
            var4.outColor.getInterior().r = var4.outColor.getInterior().r > var4.flareCol.getInterior().r ? var4.outColor.getInterior().r : var4.flareCol.getInterior().r;
            var4.outColor.getInterior().g = var4.outColor.getInterior().g > var4.flareCol.getInterior().g ? var4.outColor.getInterior().g : var4.flareCol.getInterior().g;
            var4.outColor.getInterior().b = var4.outColor.getInterior().b > var4.flareCol.getInterior().b ? var4.outColor.getInterior().b : var4.flareCol.getInterior().b;
            var4.outColor.getInterior().a = var4.outColor.getInterior().a > var4.flareCol.getInterior().a ? var4.outColor.getInterior().a : var4.flareCol.getInterior().a;
            float var8 = 1.0F - var6 * var4.intensity;
            var4.outColor.interp(var7, var8, var7);
            float var9 = ClimateManager.lerp(var8, 0.35F, var1.CM_Ambient);
            var1.CM_Ambient = var1.CM_Ambient > var9 ? var1.CM_Ambient : var9;
            float var10 = ClimateManager.lerp(var8, 0.6F * var4.intensity, var1.CM_DayLightStrength);
            var1.CM_DayLightStrength = var1.CM_DayLightStrength > var10 ? var1.CM_DayLightStrength : var10;
            if (Core.getInstance().RenderShader != null && Core.getInstance().getOffscreenBuffer() != null) {
               float var11 = ClimateManager.lerp(var8, 1.0F * var5, var1.CM_Desaturation);
               var1.CM_Desaturation = var1.CM_Desaturation > var11 ? var1.CM_Desaturation : var11;
            }
         }

      }
   }

   private static class PlayerFlareLightInfo {
      private float intensity;
      private float lerp;
      private float distMod;
      private ClimateColorInfo flareCol = new ClimateColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
      private ClimateColorInfo outColor = new ClimateColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
