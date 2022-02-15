package zombie.ui;

import se.krka.kahlua.vm.KahluaTable;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.iso.Vector2;

public final class RadialProgressBar extends UIElement {
   private static final boolean DEBUG = false;
   Texture radialTexture;
   float deltaValue = 1.0F;
   private static final RadialProgressBar.RadSegment[] segments = new RadialProgressBar.RadSegment[8];
   private final float PIx2 = 6.283185F;
   private final float PiOver2 = 1.570796F;

   public RadialProgressBar(KahluaTable var1, Texture var2) {
      super(var1);
      this.radialTexture = var2;
   }

   public void update() {
      super.update();
   }

   public void render() {
      if (this.enabled) {
         if (this.isVisible()) {
            if (this.Parent == null || this.Parent.maxDrawHeight == -1 || !((double)this.Parent.maxDrawHeight <= this.y)) {
               if (this.radialTexture != null) {
                  float var1 = (float)(this.x + this.xScroll + this.getAbsoluteX());
                  float var2 = (float)(this.y + this.yScroll + this.getAbsoluteY());
                  float var3 = this.radialTexture.xStart;
                  float var4 = this.radialTexture.yStart;
                  float var5 = this.radialTexture.xEnd - this.radialTexture.xStart;
                  float var6 = this.radialTexture.yEnd - this.radialTexture.yStart;
                  float var7 = var1 + 0.5F * this.width;
                  float var8 = var2 + 0.5F * this.height;
                  float var9 = this.deltaValue;
                  float var10 = var9 * 6.283185F - 1.570796F;
                  Vector2 var11 = new Vector2((float)Math.cos((double)var10), (float)Math.sin((double)var10));
                  float var12;
                  float var13;
                  if (Math.abs(this.width / 2.0F / var11.x) < Math.abs(this.height / 2.0F / var11.y)) {
                     var12 = Math.abs(this.width / 2.0F / var11.x);
                     var13 = Math.abs(0.5F / var11.x);
                  } else {
                     var12 = Math.abs(this.height / 2.0F / var11.y);
                     var13 = Math.abs(0.5F / var11.y);
                  }

                  float var14 = var7 + var11.x * var12;
                  float var15 = var8 + var11.y * var12;
                  float var16 = 0.5F + var11.x * var13;
                  float var17 = 0.5F + var11.y * var13;
                  int var18 = (int)(var9 * 8.0F);
                  if (var9 <= 0.0F) {
                     var18 = -1;
                  }

                  for(int var19 = 0; var19 < segments.length; ++var19) {
                     RadialProgressBar.RadSegment var20 = segments[var19];
                     if (var20 != null && var19 <= var18) {
                        if (var19 != var18) {
                           SpriteRenderer.instance.renderPoly(this.radialTexture, var1 + var20.vertex[0].x * this.width, var2 + var20.vertex[0].y * this.height, var1 + var20.vertex[1].x * this.width, var2 + var20.vertex[1].y * this.height, var1 + var20.vertex[2].x * this.width, var2 + var20.vertex[2].y * this.height, var1 + var20.vertex[2].x * this.width, var2 + var20.vertex[2].y * this.height, 1.0F, 1.0F, 1.0F, 1.0F, var3 + var20.uv[0].x * var5, var4 + var20.uv[0].y * var6, var3 + var20.uv[1].x * var5, var4 + var20.uv[1].y * var6, var3 + var20.uv[2].x * var5, var4 + var20.uv[2].y * var6, var3 + var20.uv[2].x * var5, var4 + var20.uv[2].y * var6);
                        } else {
                           SpriteRenderer.instance.renderPoly(this.radialTexture, var1 + var20.vertex[0].x * this.width, var2 + var20.vertex[0].y * this.height, var14, var15, var1 + var20.vertex[2].x * this.width, var2 + var20.vertex[2].y * this.height, var1 + var20.vertex[2].x * this.width, var2 + var20.vertex[2].y * this.height, 1.0F, 1.0F, 1.0F, 1.0F, var3 + var20.uv[0].x * var5, var4 + var20.uv[0].y * var6, var3 + var16 * var5, var4 + var17 * var6, var3 + var20.uv[2].x * var5, var4 + var20.uv[2].y * var6, var3 + var20.uv[2].x * var5, var4 + var20.uv[2].y * var6);
                        }
                     }
                  }

               }
            }
         }
      }
   }

   public void setValue(float var1) {
      this.deltaValue = PZMath.clamp(var1, 0.0F, 1.0F);
   }

   public float getValue() {
      return this.deltaValue;
   }

   public void setTexture(Texture var1) {
      this.radialTexture = var1;
   }

   public Texture getTexture() {
      return this.radialTexture;
   }

   static {
      segments[0] = new RadialProgressBar.RadSegment();
      segments[0].set(0.5F, 0.0F, 1.0F, 0.0F, 0.5F, 0.5F);
      segments[1] = new RadialProgressBar.RadSegment();
      segments[1].set(1.0F, 0.0F, 1.0F, 0.5F, 0.5F, 0.5F);
      segments[2] = new RadialProgressBar.RadSegment();
      segments[2].set(1.0F, 0.5F, 1.0F, 1.0F, 0.5F, 0.5F);
      segments[3] = new RadialProgressBar.RadSegment();
      segments[3].set(1.0F, 1.0F, 0.5F, 1.0F, 0.5F, 0.5F);
      segments[4] = new RadialProgressBar.RadSegment();
      segments[4].set(0.5F, 1.0F, 0.0F, 1.0F, 0.5F, 0.5F);
      segments[5] = new RadialProgressBar.RadSegment();
      segments[5].set(0.0F, 1.0F, 0.0F, 0.5F, 0.5F, 0.5F);
      segments[6] = new RadialProgressBar.RadSegment();
      segments[6].set(0.0F, 0.5F, 0.0F, 0.0F, 0.5F, 0.5F);
      segments[7] = new RadialProgressBar.RadSegment();
      segments[7].set(0.0F, 0.0F, 0.5F, 0.0F, 0.5F, 0.5F);
   }

   private static class RadSegment {
      Vector2[] vertex = new Vector2[3];
      Vector2[] uv = new Vector2[3];

      private RadialProgressBar.RadSegment set(int var1, float var2, float var3, float var4, float var5) {
         this.vertex[var1] = new Vector2(var2, var3);
         this.uv[var1] = new Vector2(var4, var5);
         return this;
      }

      private void set(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.vertex[0] = new Vector2(var1, var2);
         this.vertex[1] = new Vector2(var3, var4);
         this.vertex[2] = new Vector2(var5, var6);
         this.uv[0] = new Vector2(var1, var2);
         this.uv[1] = new Vector2(var3, var4);
         this.uv[2] = new Vector2(var5, var6);
      }
   }
}
