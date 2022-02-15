package zombie.iso.sprite.shapers;

import javax.xml.bind.annotation.XmlType;
import zombie.core.Color;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;

public class FloorShaperDeDiamond extends FloorShaper {
   public static final FloorShaperDeDiamond instance = new FloorShaperDeDiamond();

   public void accept(TextureDraw var1) {
      int var2 = this.colTint;
      this.colTint = 0;
      super.accept(var1);
      this.applyDeDiamondPadding(var1);
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Floor.Lighting.getValue()) {
         int var3 = this.col[0];
         int var4 = this.col[1];
         int var5 = this.col[2];
         int var6 = this.col[3];
         int var7 = Color.lerpABGR(var3, var6, 0.5F);
         int var8 = Color.lerpABGR(var4, var3, 0.5F);
         int var9 = Color.lerpABGR(var5, var4, 0.5F);
         int var10 = Color.lerpABGR(var6, var5, 0.5F);
         var1.col0 = Color.blendBGR(var1.col0, var7);
         var1.col1 = Color.blendBGR(var1.col1, var8);
         var1.col2 = Color.blendBGR(var1.col2, var9);
         var1.col3 = Color.blendBGR(var1.col3, var10);
         if (var2 != 0) {
            var1.col0 = Color.tintABGR(var1.col0, var2);
            var1.col1 = Color.tintABGR(var1.col1, var2);
            var1.col2 = Color.tintABGR(var1.col2, var2);
            var1.col3 = Color.tintABGR(var1.col3, var2);
         }

      }
   }

   private void applyDeDiamondPadding(TextureDraw var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.IsoPaddingDeDiamond.getValue()) {
         FloorShaperDeDiamond.Settings var2 = this.getSettings();
         FloorShaperDeDiamond.Settings.BorderSetting var3 = var2.getCurrentZoomSetting();
         float var4 = var3.borderThicknessUp;
         float var5 = var3.borderThicknessDown;
         float var6 = var3.borderThicknessLR;
         float var7 = var3.uvFraction;
         float var8 = var1.x1 - var1.x0;
         float var9 = var1.y2 - var1.y1;
         float var10 = var1.u1 - var1.u0;
         float var11 = var1.v2 - var1.v1;
         float var15 = var10 * var6 / var8;
         float var16 = var11 * var4 / var9;
         float var17 = var11 * var5 / var9;
         float var18 = var7 * var15;
         float var19 = var7 * var16;
         float var20 = var7 * var17;
         SpritePadding.applyPadding(var1, var6, var4, var6, var5, var18, var19, var18, var20);
      }
   }

   private FloorShaperDeDiamond.Settings getSettings() {
      return SpritePaddingSettings.getSettings().FloorDeDiamond;
   }

   @XmlType(
      name = "FloorShaperDeDiamondSettings"
   )
   public static class Settings extends SpritePaddingSettings.GenericZoomBasedSettingGroup {
      public FloorShaperDeDiamond.Settings.BorderSetting ZoomedIn = new FloorShaperDeDiamond.Settings.BorderSetting(2.0F, 1.0F, 2.0F, 0.01F);
      public FloorShaperDeDiamond.Settings.BorderSetting NotZoomed = new FloorShaperDeDiamond.Settings.BorderSetting(2.0F, 1.0F, 2.0F, 0.01F);
      public FloorShaperDeDiamond.Settings.BorderSetting ZoomedOut = new FloorShaperDeDiamond.Settings.BorderSetting(2.0F, 0.0F, 2.5F, 0.0F);

      public FloorShaperDeDiamond.Settings.BorderSetting getCurrentZoomSetting() {
         return (FloorShaperDeDiamond.Settings.BorderSetting)getCurrentZoomSetting(this.ZoomedIn, this.NotZoomed, this.ZoomedOut);
      }

      public static class BorderSetting {
         public float borderThicknessUp = 3.0F;
         public float borderThicknessDown = 3.0F;
         public float borderThicknessLR = 0.0F;
         public float uvFraction = 0.01F;

         public BorderSetting() {
         }

         public BorderSetting(float var1, float var2, float var3, float var4) {
            this.borderThicknessUp = var1;
            this.borderThicknessDown = var2;
            this.borderThicknessLR = var3;
            this.uvFraction = var4;
         }
      }
   }
}
