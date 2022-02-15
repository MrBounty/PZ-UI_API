package zombie.core;

import zombie.core.math.PZMath;
import zombie.iso.IsoPuddles;
import zombie.iso.IsoWater;
import zombie.ui.UIManager;

public final class PerformanceSettings {
   public static int ManualFrameSkips = 0;
   private static int s_lockFPS = 60;
   private static boolean s_uncappedFPS = false;
   public static int LightingFrameSkip = 0;
   public static int WaterQuality = 0;
   public static int PuddlesQuality = 0;
   public static boolean NewRoofHiding = true;
   public static boolean LightingThread = true;
   public static int LightingFPS = 15;
   public static boolean auto3DZombies = false;
   public static final PerformanceSettings instance = new PerformanceSettings();
   public static boolean InterpolateAnims = true;
   public static int AnimationSkip = 1;
   public static boolean ModelLighting = true;
   public static int ZombieAnimationSpeedFalloffCount = 6;
   public static int ZombieBonusFullspeedFalloff = 3;
   public static int BaseStaticAnimFramerate = 60;
   public static boolean UseFBOs = false;
   public static int numberZombiesBlended = 20;
   public static int FogQuality = 0;

   public static int getLockFPS() {
      return s_lockFPS;
   }

   public static void setLockFPS(int var0) {
      s_lockFPS = var0;
   }

   public static boolean isUncappedFPS() {
      return s_uncappedFPS;
   }

   public static void setUncappedFPS(boolean var0) {
      s_uncappedFPS = var0;
   }

   public int getFramerate() {
      return getLockFPS();
   }

   public void setFramerate(int var1) {
      setLockFPS(var1);
   }

   public boolean isFramerateUncapped() {
      return isUncappedFPS();
   }

   public void setFramerateUncapped(boolean var1) {
      setUncappedFPS(var1);
   }

   public void setLightingQuality(int var1) {
      LightingFrameSkip = var1;
   }

   public int getLightingQuality() {
      return LightingFrameSkip;
   }

   public void setWaterQuality(int var1) {
      WaterQuality = var1;
      IsoWater.getInstance().applyWaterQuality();
   }

   public int getWaterQuality() {
      return WaterQuality;
   }

   public void setPuddlesQuality(int var1) {
      PuddlesQuality = var1;
      if (var1 > 2 || var1 < 0) {
         PuddlesQuality = 0;
      }

      IsoPuddles.getInstance().applyPuddlesQuality();
   }

   public int getPuddlesQuality() {
      return PuddlesQuality;
   }

   public void setNewRoofHiding(boolean var1) {
      NewRoofHiding = var1;
   }

   public boolean getNewRoofHiding() {
      return NewRoofHiding;
   }

   public void setLightingFPS(int var1) {
      var1 = Math.max(1, Math.min(120, var1));
      LightingFPS = var1;
      System.out.println("LightingFPS set to " + LightingFPS);
   }

   public int getLightingFPS() {
      return LightingFPS;
   }

   public int getUIRenderFPS() {
      return UIManager.useUIFBO ? Core.OptionUIRenderFPS : s_lockFPS;
   }

   public int getFogQuality() {
      return FogQuality;
   }

   public void setFogQuality(int var1) {
      FogQuality = PZMath.clamp(var1, 0, 2);
   }
}
