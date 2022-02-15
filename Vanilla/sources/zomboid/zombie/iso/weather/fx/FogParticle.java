package zombie.iso.weather.fx;

import zombie.core.textures.Texture;
import zombie.iso.Vector2;

public class FogParticle extends WeatherParticle {
   private double angleRadians = 0.0D;
   private float lastAngle = -1.0F;
   private float lastIntensity = -1.0F;
   protected float angleOffset = 0.0F;
   private float alphaMod = 0.0F;
   private float tmpAngle = 0.0F;

   public FogParticle(Texture var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public void update(float var1) {
      if (this.lastAngle != IsoWeatherFX.instance.windAngle || this.lastIntensity != IsoWeatherFX.instance.windIntensity.value()) {
         this.tmpAngle = IsoWeatherFX.instance.windAngle + (this.angleOffset - this.angleOffset * 1.0F * IsoWeatherFX.instance.windIntensity.value());
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

      Vector2 var10000 = this.position;
      var10000.x += this.velocity.x * IsoWeatherFX.instance.windSpeedFog * var1;
      var10000 = this.position;
      var10000.y += this.velocity.y * IsoWeatherFX.instance.windSpeedFog * var1;
      super.update(var1);
      this.alphaMod = IsoWeatherFX.instance.fogIntensity.value();
      this.renderAlpha = this.alpha * this.alphaMod * this.alphaFadeMod.value() * IsoWeatherFX.instance.indoorsAlphaMod.value();
   }
}
