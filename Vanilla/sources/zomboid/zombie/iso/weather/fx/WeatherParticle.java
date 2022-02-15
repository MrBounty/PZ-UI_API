package zombie.iso.weather.fx;

import java.util.function.Consumer;
import org.lwjgl.util.Rectangle;
import zombie.core.Color;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.iso.Vector2;

public abstract class WeatherParticle {
   protected ParticleRectangle parent;
   protected Rectangle bounds;
   protected Texture texture;
   protected Color color;
   protected Vector2 position;
   protected Vector2 velocity;
   protected float alpha;
   protected float speed;
   protected SteppedUpdateFloat alphaFadeMod;
   protected float renderAlpha;
   protected float oWidth;
   protected float oHeight;
   protected float zoomMultiW;
   protected float zoomMultiH;
   protected boolean recalcSizeOnZoom;
   protected float lastZoomMod;

   public WeatherParticle(Texture var1) {
      this.color = Color.white;
      this.position = new Vector2(0.0F, 0.0F);
      this.velocity = new Vector2(0.0F, 0.0F);
      this.alpha = 1.0F;
      this.speed = 0.0F;
      this.alphaFadeMod = new SteppedUpdateFloat(0.0F, 0.1F, 0.0F, 1.0F);
      this.renderAlpha = 0.0F;
      this.zoomMultiW = 0.0F;
      this.zoomMultiH = 0.0F;
      this.recalcSizeOnZoom = false;
      this.lastZoomMod = -1.0F;
      this.texture = var1;
      this.bounds = new Rectangle(0, 0, var1.getWidth(), var1.getHeight());
      this.oWidth = (float)this.bounds.getWidth();
      this.oHeight = (float)this.bounds.getHeight();
   }

   public WeatherParticle(Texture var1, int var2, int var3) {
      this.color = Color.white;
      this.position = new Vector2(0.0F, 0.0F);
      this.velocity = new Vector2(0.0F, 0.0F);
      this.alpha = 1.0F;
      this.speed = 0.0F;
      this.alphaFadeMod = new SteppedUpdateFloat(0.0F, 0.1F, 0.0F, 1.0F);
      this.renderAlpha = 0.0F;
      this.zoomMultiW = 0.0F;
      this.zoomMultiH = 0.0F;
      this.recalcSizeOnZoom = false;
      this.lastZoomMod = -1.0F;
      this.texture = var1;
      this.bounds = new Rectangle(0, 0, var2, var3);
      this.oWidth = (float)this.bounds.getWidth();
      this.oHeight = (float)this.bounds.getHeight();
   }

   protected void setParent(ParticleRectangle var1) {
      this.parent = var1;
   }

   public void update(float var1) {
      this.update(var1, true);
   }

   public void update(float var1, boolean var2) {
      this.alphaFadeMod.update(var1);
      Vector2 var10000;
      if (this.position.x > (float)this.parent.getWidth()) {
         var10000 = this.position;
         var10000.x -= (float)((int)(this.position.x / (float)this.parent.getWidth()) * this.parent.getWidth());
      } else if (this.position.x < 0.0F) {
         var10000 = this.position;
         var10000.x -= (float)((int)((this.position.x - (float)this.parent.getWidth()) / (float)this.parent.getWidth()) * this.parent.getWidth());
      }

      if (this.position.y > (float)this.parent.getHeight()) {
         var10000 = this.position;
         var10000.y -= (float)((int)(this.position.y / (float)this.parent.getHeight()) * this.parent.getHeight());
      } else if (this.position.y < 0.0F) {
         var10000 = this.position;
         var10000.y -= (float)((int)((this.position.y - (float)this.parent.getHeight()) / (float)this.parent.getHeight()) * this.parent.getHeight());
      }

      if (var2) {
         this.bounds.setLocation((int)this.position.x - this.bounds.getWidth() / 2, (int)this.position.y - this.bounds.getHeight() / 2);
      }

   }

   protected boolean updateZoomSize() {
      if (this.recalcSizeOnZoom && this.lastZoomMod != IsoWeatherFX.ZoomMod) {
         this.lastZoomMod = IsoWeatherFX.ZoomMod;
         this.oWidth = (float)this.bounds.getWidth();
         this.oHeight = (float)this.bounds.getHeight();
         if (this.lastZoomMod > 0.0F) {
            this.oWidth *= 1.0F + IsoWeatherFX.ZoomMod * this.zoomMultiW;
            this.oHeight *= 1.0F + IsoWeatherFX.ZoomMod * this.zoomMultiH;
         }

         return true;
      } else {
         return false;
      }
   }

   public void render(float var1, float var2) {
      SpriteRenderer.instance.render(this.texture, var1 + (float)this.bounds.getX(), var2 + (float)this.bounds.getY(), this.oWidth, this.oHeight, this.color.r, this.color.g, this.color.b, this.renderAlpha, (Consumer)null);
   }
}
