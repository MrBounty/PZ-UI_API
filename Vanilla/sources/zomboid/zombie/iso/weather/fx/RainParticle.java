package zombie.iso.weather.fx;

import java.util.function.Consumer;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.iso.Vector2;

public class RainParticle extends WeatherParticle {
   private double angleRadians = 0.0D;
   private float lastAngle = -1.0F;
   private float lastIntensity = -1.0F;
   protected float angleOffset = 0.0F;
   private float alphaMod = 0.0F;
   private float incarnateAlpha = 1.0F;
   private float life = 0.0F;
   private RainParticle.RenderPoints rp;
   private boolean angleUpdate = false;
   private float tmpAngle = 0.0F;

   public RainParticle(Texture var1, int var2) {
      super(var1);
      if (var2 > 6) {
         this.bounds.setSize(Rand.Next(1, 2), var2);
      } else {
         this.bounds.setSize(1, var2);
      }

      this.oWidth = (float)this.bounds.getWidth();
      this.oHeight = (float)this.bounds.getHeight();
      this.recalcSizeOnZoom = true;
      this.zoomMultiW = 0.0F;
      this.zoomMultiH = 2.0F;
      this.setLife();
      this.rp = new RainParticle.RenderPoints();
      this.rp.setDimensions(this.oWidth, this.oHeight);
   }

   protected void setLife() {
      this.life = (float)Rand.Next(20, 60);
   }

   public void update(float var1) {
      this.angleUpdate = false;
      if (this.updateZoomSize()) {
         this.rp.setDimensions(this.oWidth, this.oHeight);
         this.angleUpdate = true;
      }

      if (this.angleUpdate || this.lastAngle != IsoWeatherFX.instance.windAngle || this.lastIntensity != IsoWeatherFX.instance.windPrecipIntensity.value()) {
         this.tmpAngle = IsoWeatherFX.instance.windAngle + (this.angleOffset - this.angleOffset * 0.5F * IsoWeatherFX.instance.windPrecipIntensity.value());
         if (this.tmpAngle > 360.0F) {
            this.tmpAngle -= 360.0F;
         }

         if (this.tmpAngle < 0.0F) {
            this.tmpAngle += 360.0F;
         }

         this.angleRadians = Math.toRadians((double)this.tmpAngle);
         this.velocity.set((float)Math.cos(this.angleRadians) * this.speed, (float)Math.sin(this.angleRadians) * this.speed);
         this.lastAngle = IsoWeatherFX.instance.windAngle;
         this.lastIntensity = IsoWeatherFX.instance.windPrecipIntensity.value();
         this.angleUpdate = true;
      }

      Vector2 var10000 = this.position;
      var10000.x += this.velocity.x * (1.0F + IsoWeatherFX.instance.windSpeed * 0.1F) * var1;
      var10000 = this.position;
      var10000.y += this.velocity.y * (1.0F + IsoWeatherFX.instance.windSpeed * 0.1F) * var1;
      --this.life;
      if (this.life < 0.0F) {
         this.setLife();
         this.incarnateAlpha = 0.0F;
         this.position.set((float)Rand.Next(0, this.parent.getWidth()), (float)Rand.Next(0, this.parent.getHeight()));
      }

      if (this.incarnateAlpha < 1.0F) {
         this.incarnateAlpha += 0.035F;
         if (this.incarnateAlpha > 1.0F) {
            this.incarnateAlpha = 1.0F;
         }
      }

      super.update(var1, false);
      this.bounds.setLocation((int)this.position.x, (int)this.position.y);
      if (this.angleUpdate) {
         this.tmpAngle += 90.0F;
         if (this.tmpAngle > 360.0F) {
            this.tmpAngle -= 360.0F;
         }

         if (this.tmpAngle < 0.0F) {
            this.tmpAngle += 360.0F;
         }

         this.angleRadians = Math.toRadians((double)this.tmpAngle);
         this.rp.rotate(this.angleRadians);
      }

      this.alphaMod = 1.0F - 0.2F * IsoWeatherFX.instance.windIntensity.value();
      this.renderAlpha = this.alpha * this.alphaMod * this.alphaFadeMod.value() * IsoWeatherFX.instance.indoorsAlphaMod.value() * this.incarnateAlpha;
      this.renderAlpha *= 0.55F;
      if (IsoWeatherFX.instance.playerIndoors) {
         this.renderAlpha *= 0.5F;
      }

   }

   public void render(float var1, float var2) {
      double var3 = (double)(var1 + (float)this.bounds.getX());
      double var5 = (double)(var2 + (float)this.bounds.getY());
      SpriteRenderer.instance.render(this.texture, var3 + this.rp.getX(0), var5 + this.rp.getY(0), var3 + this.rp.getX(1), var5 + this.rp.getY(1), var3 + this.rp.getX(2), var5 + this.rp.getY(2), var3 + this.rp.getX(3), var5 + this.rp.getY(3), this.color.r, this.color.g, this.color.b, this.renderAlpha, (Consumer)null);
   }

   private class RenderPoints {
      RainParticle.Point[] points = new RainParticle.Point[4];
      RainParticle.Point center = RainParticle.this.new Point();
      RainParticle.Point dim = RainParticle.this.new Point();

      public RenderPoints() {
         for(int var2 = 0; var2 < this.points.length; ++var2) {
            this.points[var2] = RainParticle.this.new Point();
         }

      }

      public double getX(int var1) {
         return this.points[var1].x;
      }

      public double getY(int var1) {
         return this.points[var1].y;
      }

      public void setCenter(float var1, float var2) {
         this.center.set((double)var1, (double)var2);
      }

      public void setDimensions(float var1, float var2) {
         this.dim.set((double)var1, (double)var2);
         this.points[0].setOrig((double)(-var1 / 2.0F), (double)(-var2 / 2.0F));
         this.points[1].setOrig((double)(var1 / 2.0F), (double)(-var2 / 2.0F));
         this.points[2].setOrig((double)(var1 / 2.0F), (double)(var2 / 2.0F));
         this.points[3].setOrig((double)(-var1 / 2.0F), (double)(var2 / 2.0F));
      }

      public void rotate(double var1) {
         double var3 = Math.cos(var1);
         double var5 = Math.sin(var1);

         for(int var7 = 0; var7 < this.points.length; ++var7) {
            this.points[var7].x = this.points[var7].origx * var3 - this.points[var7].origy * var5;
            this.points[var7].y = this.points[var7].origx * var5 + this.points[var7].origy * var3;
         }

      }
   }

   private class Point {
      private double origx;
      private double origy;
      private double x;
      private double y;

      public void setOrig(double var1, double var3) {
         this.origx = var1;
         this.origy = var3;
         this.x = var1;
         this.y = var3;
      }

      public void set(double var1, double var3) {
         this.x = var1;
         this.y = var3;
      }
   }
}
