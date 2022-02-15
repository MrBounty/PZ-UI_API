package zombie.iso;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import zombie.GameTime;
import zombie.core.Core;
import zombie.core.textures.ColorInfo;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;

public final class IsoWallBloodSplat {
   private static final ColorInfo info = new ColorInfo();
   public float worldAge;
   public IsoSprite sprite;

   public IsoWallBloodSplat() {
   }

   public IsoWallBloodSplat(float var1, IsoSprite var2) {
      this.worldAge = var1;
      this.sprite = var2;
   }

   public void render(float var1, float var2, float var3, ColorInfo var4) {
      if (this.sprite != null) {
         if (this.sprite.CurrentAnim != null && !this.sprite.CurrentAnim.Frames.isEmpty()) {
            int var5 = Core.TileScale;
            int var6 = 32 * var5;
            int var7 = 96 * var5;
            if (IsoSprite.globalOffsetX == -1.0F) {
               IsoSprite.globalOffsetX = -IsoCamera.frameState.OffX;
               IsoSprite.globalOffsetY = -IsoCamera.frameState.OffY;
            }

            float var8 = IsoUtils.XToScreen(var1, var2, var3, 0);
            float var9 = IsoUtils.YToScreen(var1, var2, var3, 0);
            var8 -= (float)var6;
            var9 -= (float)var7;
            var8 += IsoSprite.globalOffsetX;
            var9 += IsoSprite.globalOffsetY;
            if (!(var8 >= (float)IsoCamera.frameState.OffscreenWidth) && !(var8 + (float)(64 * var5) <= 0.0F)) {
               if (!(var9 >= (float)IsoCamera.frameState.OffscreenHeight) && !(var9 + (float)(128 * var5) <= 0.0F)) {
                  info.r = 0.7F * var4.r;
                  info.g = 0.9F * var4.g;
                  info.b = 0.9F * var4.b;
                  info.a = 0.4F;
                  float var10 = (float)GameTime.getInstance().getWorldAgeHours();
                  float var11 = var10 - this.worldAge;
                  ColorInfo var10000;
                  if (var11 >= 0.0F && var11 < 72.0F) {
                     float var12 = 1.0F - var11 / 72.0F;
                     var10000 = info;
                     var10000.r *= 0.2F + var12 * 0.8F;
                     var10000 = info;
                     var10000.g *= 0.2F + var12 * 0.8F;
                     var10000 = info;
                     var10000.b *= 0.2F + var12 * 0.8F;
                     var10000 = info;
                     var10000.a *= 0.25F + var12 * 0.75F;
                  } else {
                     var10000 = info;
                     var10000.r *= 0.2F;
                     var10000 = info;
                     var10000.g *= 0.2F;
                     var10000 = info;
                     var10000.b *= 0.2F;
                     var10000 = info;
                     var10000.a *= 0.25F;
                  }

                  info.a = Math.max(info.a, 0.15F);
                  ((IsoDirectionFrame)this.sprite.CurrentAnim.Frames.get(0)).render(var8, var9, IsoDirections.N, info, false, (Consumer)null);
               }
            }
         }
      }
   }

   public void save(ByteBuffer var1) {
      var1.putFloat(this.worldAge);
      var1.putInt(this.sprite.ID);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.worldAge = var1.getFloat();
      int var3 = var1.getInt();
      this.sprite = IsoSprite.getSprite(IsoSpriteManager.instance, var3);
   }
}
