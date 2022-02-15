package zombie.iso.sprite;

import java.util.function.Consumer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.iso.IsoDirections;
import zombie.iso.objects.ObjectRenderEffects;

public final class IsoDirectionFrame {
   public final Texture[] directions = new Texture[8];
   boolean bDoFlip = true;

   public IsoDirectionFrame(Texture var1) {
      this.SetAllDirections(var1);
   }

   public IsoDirectionFrame() {
   }

   public IsoDirectionFrame(Texture var1, Texture var2, Texture var3, Texture var4, Texture var5) {
      this.directions[0] = var2;
      this.directions[1] = var1;
      this.directions[2] = var2;
      this.directions[3] = var3;
      this.directions[4] = var4;
      this.directions[5] = var5;
      this.directions[6] = var4;
      this.directions[7] = var3;
   }

   public IsoDirectionFrame(Texture var1, Texture var2, Texture var3, Texture var4, Texture var5, Texture var6, Texture var7, Texture var8) {
      if (var5 == null) {
         boolean var9 = false;
      }

      this.directions[0] = var1;
      this.directions[1] = var8;
      this.directions[2] = var7;
      this.directions[3] = var6;
      this.directions[4] = var5;
      this.directions[5] = var4;
      this.directions[6] = var3;
      this.directions[7] = var2;
      this.bDoFlip = false;
   }

   public IsoDirectionFrame(Texture var1, Texture var2, Texture var3, Texture var4) {
      this.directions[0] = var1;
      this.directions[1] = var1;
      this.directions[2] = var4;
      this.directions[3] = var4;
      this.directions[4] = var2;
      this.directions[5] = var2;
      this.directions[6] = var3;
      this.directions[7] = var3;
      this.bDoFlip = false;
   }

   public Texture getTexture(IsoDirections var1) {
      Texture var2 = this.directions[var1.index()];
      return var2;
   }

   public void SetAllDirections(Texture var1) {
      this.directions[0] = var1;
      this.directions[1] = var1;
      this.directions[2] = var1;
      this.directions[3] = var1;
      this.directions[4] = var1;
      this.directions[5] = var1;
      this.directions[6] = var1;
      this.directions[7] = var1;
   }

   public void SetDirection(Texture var1, IsoDirections var2) {
      this.directions[var2.index()] = var1;
   }

   public void render(float var1, float var2, IsoDirections var3, ColorInfo var4, boolean var5, Consumer var6) {
      Texture var7 = this.directions[var3.index()];
      if (var7 != null) {
         if (var5) {
            var7.flip = !var7.flip;
         }

         if (var7 != null) {
            if (!this.bDoFlip) {
               var7.flip = false;
            }

            var7.render(var1, var2, (float)var7.getWidth(), (float)var7.getHeight(), var4.r, var4.g, var4.b, var4.a, var6);
            var7.flip = false;
         }
      }
   }

   void render(float var1, float var2, float var3, float var4, IsoDirections var5, ColorInfo var6, boolean var7, Consumer var8) {
      Texture var9 = this.directions[var5.index()];
      if (var9 != null) {
         if (var7) {
            var9.flip = !var9.flip;
         }

         if (!this.bDoFlip) {
            var9.flip = false;
         }

         var9.render(var1, var2, var3, var4, var6.r, var6.g, var6.b, var6.a, var8);
         var9.flip = false;
      }
   }

   void render(ObjectRenderEffects var1, float var2, float var3, float var4, float var5, IsoDirections var6, ColorInfo var7, boolean var8, Consumer var9) {
      Texture var10 = this.directions[var6.index()];
      if (var10 != null) {
         if (var8) {
            var10.flip = !var10.flip;
         }

         if (!this.bDoFlip) {
            var10.flip = false;
         }

         var10.render(var1, var2, var3, var4, var5, var7.r, var7.g, var7.b, var7.a, var9);
         var10.flip = false;
      }
   }

   public void renderexplicit(int var1, int var2, IsoDirections var3, float var4) {
      this.renderexplicit(var1, var2, var3, var4, (ColorInfo)null);
   }

   public void renderexplicit(int var1, int var2, IsoDirections var3, float var4, ColorInfo var5) {
      Texture var6 = this.directions[var3.index()];
      if (var6 != null) {
         float var7 = 1.0F;
         float var8 = 1.0F;
         float var9 = 1.0F;
         float var10 = 1.0F;
         if (var5 != null) {
            var7 *= var5.a;
            var8 *= var5.r;
            var9 *= var5.g;
            var10 *= var5.b;
         }

         var6.renderstrip(var1, var2, (int)((float)var6.getWidth() * var4), (int)((float)var6.getHeight() * var4), var8, var9, var10, var7, (Consumer)null);
      }
   }
}
