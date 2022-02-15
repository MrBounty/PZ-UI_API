package zombie.iso.sprite.shapers;

import java.util.function.Consumer;
import zombie.core.Color;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;

public class FloorShaper implements Consumer {
   protected final int[] col = new int[4];
   protected int colTint = 0;
   protected boolean isShore = false;
   protected final float[] waterDepth = new float[4];

   public void setVertColors(int var1, int var2, int var3, int var4) {
      this.col[0] = var1;
      this.col[1] = var2;
      this.col[2] = var3;
      this.col[3] = var4;
   }

   public void setAlpha4(float var1) {
      int var2 = (int)(var1 * 255.0F) & 255;
      this.col[0] = this.col[0] & 16777215 | var2 << 24;
      this.col[1] = this.col[1] & 16777215 | var2 << 24;
      this.col[2] = this.col[2] & 16777215 | var2 << 24;
      this.col[3] = this.col[3] & 16777215 | var2 << 24;
   }

   public void setShore(boolean var1) {
      this.isShore = var1;
   }

   public void setWaterDepth(float var1, float var2, float var3, float var4) {
      this.waterDepth[0] = var1;
      this.waterDepth[1] = var2;
      this.waterDepth[2] = var3;
      this.waterDepth[3] = var4;
   }

   public void setTintColor(int var1) {
      this.colTint = var1;
   }

   public void accept(TextureDraw var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.Floor.Lighting.getValue()) {
         var1.col0 = Color.blendBGR(var1.col0, this.col[0]);
         var1.col1 = Color.blendBGR(var1.col1, this.col[1]);
         var1.col2 = Color.blendBGR(var1.col2, this.col[2]);
         var1.col3 = Color.blendBGR(var1.col3, this.col[3]);
      }

      if (this.isShore && DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.ShoreFade.getValue()) {
         var1.col0 = Color.setAlphaChannelToABGR(var1.col0, 1.0F - this.waterDepth[0]);
         var1.col1 = Color.setAlphaChannelToABGR(var1.col1, 1.0F - this.waterDepth[1]);
         var1.col2 = Color.setAlphaChannelToABGR(var1.col2, 1.0F - this.waterDepth[2]);
         var1.col3 = Color.setAlphaChannelToABGR(var1.col3, 1.0F - this.waterDepth[3]);
      }

      if (this.colTint != 0) {
         var1.col0 = Color.tintABGR(var1.col0, this.colTint);
         var1.col1 = Color.tintABGR(var1.col1, this.colTint);
         var1.col2 = Color.tintABGR(var1.col2, this.colTint);
         var1.col3 = Color.tintABGR(var1.col3, this.colTint);
      }

      SpritePadding.applyIsoPadding(var1, this.getIsoPaddingSettings());
   }

   private SpritePadding.IsoPaddingSettings getIsoPaddingSettings() {
      return SpritePaddingSettings.getSettings().IsoPadding;
   }
}
