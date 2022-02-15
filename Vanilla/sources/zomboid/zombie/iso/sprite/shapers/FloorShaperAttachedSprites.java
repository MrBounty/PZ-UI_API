package zombie.iso.sprite.shapers;

import javax.xml.bind.annotation.XmlType;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;

public class FloorShaperAttachedSprites extends FloorShaper {
   public static final FloorShaperAttachedSprites instance = new FloorShaperAttachedSprites();

   public void accept(TextureDraw var1) {
      super.accept(var1);
      this.applyAttachedSpritesPadding(var1);
   }

   private void applyAttachedSpritesPadding(TextureDraw var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.IsoPaddingAttached.getValue()) {
         FloorShaperAttachedSprites.Settings var2 = this.getSettings();
         FloorShaperAttachedSprites.Settings.ASBorderSetting var3 = var2.getCurrentZoomSetting();
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

   private FloorShaperAttachedSprites.Settings getSettings() {
      return SpritePaddingSettings.getSettings().AttachedSprites;
   }

   @XmlType(
      name = "FloorShaperAttachedSpritesSettings"
   )
   public static class Settings extends SpritePaddingSettings.GenericZoomBasedSettingGroup {
      public FloorShaperAttachedSprites.Settings.ASBorderSetting ZoomedIn = new FloorShaperAttachedSprites.Settings.ASBorderSetting(2.0F, 1.0F, 3.0F, 0.01F);
      public FloorShaperAttachedSprites.Settings.ASBorderSetting NotZoomed = new FloorShaperAttachedSprites.Settings.ASBorderSetting(2.0F, 1.0F, 3.0F, 0.01F);
      public FloorShaperAttachedSprites.Settings.ASBorderSetting ZoomedOut = new FloorShaperAttachedSprites.Settings.ASBorderSetting(2.0F, 0.0F, 2.5F, 0.0F);

      public FloorShaperAttachedSprites.Settings.ASBorderSetting getCurrentZoomSetting() {
         return (FloorShaperAttachedSprites.Settings.ASBorderSetting)getCurrentZoomSetting(this.ZoomedIn, this.NotZoomed, this.ZoomedOut);
      }

      public static class ASBorderSetting {
         public float borderThicknessUp;
         public float borderThicknessDown;
         public float borderThicknessLR;
         public float uvFraction;

         public ASBorderSetting() {
         }

         public ASBorderSetting(float var1, float var2, float var3, float var4) {
            this.borderThicknessUp = var1;
            this.borderThicknessDown = var2;
            this.borderThicknessLR = var3;
            this.uvFraction = var4;
         }
      }
   }
}
