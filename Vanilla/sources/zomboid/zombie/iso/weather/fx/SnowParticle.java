package zombie.iso.weather.fx;

import zombie.core.Rand;
import zombie.core.textures.Texture;
import zombie.iso.Vector2;

public class SnowParticle extends WeatherParticle {
   private double angleRadians = 0.0D;
   private float lastAngle = -1.0F;
   private float lastIntensity = -1.0F;
   protected float angleOffset = 0.0F;
   private float alphaMod = 0.0F;
   private float incarnateAlpha = 1.0F;
   private float life = 0.0F;
   private float fadeTime = 80.0F;
   private float tmpAngle = 0.0F;

   public SnowParticle(Texture var1) {
      super(var1);
      this.recalcSizeOnZoom = true;
      this.zoomMultiW = 1.0F;
      this.zoomMultiH = 1.0F;
   }

   protected void setLife() {
      this.life = this.fadeTime + (float)Rand.Next(60, 500);
   }

   public void update(float var1) {
      if (this.lastAngle != IsoWeatherFX.instance.windAngle || this.lastIntensity != IsoWeatherFX.instance.windPrecipIntensity.value()) {
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
      }

      if (this.life >= this.fadeTime) {
         Vector2 var10000 = this.position;
         var10000.x += this.velocity.x * IsoWeatherFX.instance.windSpeed * var1;
         var10000 = this.position;
         var10000.y += this.velocity.y * IsoWeatherFX.instance.windSpeed * var1;
      } else {
         this.incarnateAlpha = this.life / this.fadeTime;
      }

      --this.life;
      if (this.life < 0.0F) {
         this.setLife();
         this.incarnateAlpha = 0.0F;
         this.position.set((float)Rand.Next(0, this.parent.getWidth()), (float)Rand.Next(0, this.parent.getHeight()));
      }

      if (this.incarnateAlpha < 1.0F) {
         this.incarnateAlpha += 0.05F;
         if (this.incarnateAlpha > 1.0F) {
            this.incarnateAlpha = 1.0F;
         }
      }

      super.update(var1);
      this.updateZoomSize();
      this.alphaMod = 1.0F - 0.2F * IsoWeatherFX.instance.windIntensity.value();
      this.renderAlpha = this.alpha * this.alphaMod * this.alphaFadeMod.value() * IsoWeatherFX.instance.indoorsAlphaMod.value() * this.incarnateAlpha;
      this.renderAlpha *= 0.7F;
   }

   public void render(float var1, float var2) {
      super.render(var1, var2);
   }
}
