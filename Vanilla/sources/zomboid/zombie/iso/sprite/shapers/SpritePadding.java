package zombie.iso.sprite.shapers;

import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;

public class SpritePadding {
   public static void applyPadding(TextureDraw var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = var0.x0;
      float var10 = var0.y0;
      float var11 = var0.x1;
      float var12 = var0.y1;
      float var13 = var0.x2;
      float var14 = var0.y2;
      float var15 = var0.x3;
      float var16 = var0.y3;
      float var17 = var0.u0;
      float var18 = var0.v0;
      float var19 = var0.u1;
      float var20 = var0.v1;
      float var21 = var0.u2;
      float var22 = var0.v2;
      float var23 = var0.u3;
      float var24 = var0.v3;
      var0.x0 = var9 - var1;
      var0.y0 = var10 - var2;
      var0.u0 = var17 - var5;
      var0.v0 = var18 - var6;
      var0.x1 = var11 + var3;
      var0.y1 = var12 - var2;
      var0.u1 = var19 + var7;
      var0.v1 = var20 - var6;
      var0.x2 = var13 + var3;
      var0.y2 = var14 + var4;
      var0.u2 = var21 + var7;
      var0.v2 = var22 + var8;
      var0.x3 = var15 - var1;
      var0.y3 = var16 + var4;
      var0.u3 = var23 - var5;
      var0.v3 = var24 + var8;
   }

   public static void applyPaddingBorder(TextureDraw var0, float var1, float var2) {
      float var3 = var0.x1 - var0.x0;
      float var4 = var0.y2 - var0.y1;
      float var5 = var0.u1 - var0.u0;
      float var6 = var0.v2 - var0.v1;
      float var9 = var5 * var1 / var3;
      float var10 = var6 * var1 / var4;
      float var11 = var2 * var9;
      float var12 = var2 * var10;
      applyPadding(var0, var1, var1, var1, var1, var11, var12, var11, var12);
   }

   public static void applyIsoPadding(TextureDraw var0, SpritePadding.IsoPaddingSettings var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.IsoPadding.getValue()) {
         SpritePadding.IsoPaddingSettings.IsoBorderSetting var2 = var1.getCurrentZoomSetting();
         float var3 = var2.borderThickness;
         float var4 = var2.uvFraction;
         applyPaddingBorder(var0, var3, var4);
      }
   }

   public static class IsoPaddingSettings extends SpritePaddingSettings.GenericZoomBasedSettingGroup {
      public SpritePadding.IsoPaddingSettings.IsoBorderSetting ZoomedIn = new SpritePadding.IsoPaddingSettings.IsoBorderSetting(1.0F, 0.99F);
      public SpritePadding.IsoPaddingSettings.IsoBorderSetting NotZoomed = new SpritePadding.IsoPaddingSettings.IsoBorderSetting(1.0F, 0.99F);
      public SpritePadding.IsoPaddingSettings.IsoBorderSetting ZoomedOut = new SpritePadding.IsoPaddingSettings.IsoBorderSetting(2.0F, 0.01F);

      public SpritePadding.IsoPaddingSettings.IsoBorderSetting getCurrentZoomSetting() {
         return (SpritePadding.IsoPaddingSettings.IsoBorderSetting)getCurrentZoomSetting(this.ZoomedIn, this.NotZoomed, this.ZoomedOut);
      }

      public static class IsoBorderSetting {
         public float borderThickness;
         public float uvFraction;

         public IsoBorderSetting() {
         }

         public IsoBorderSetting(float var1, float var2) {
            this.borderThickness = var1;
            this.uvFraction = var2;
         }
      }
   }
}
