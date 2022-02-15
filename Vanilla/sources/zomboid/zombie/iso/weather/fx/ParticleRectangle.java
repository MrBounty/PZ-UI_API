package zombie.iso.weather.fx;

import zombie.debug.LineDrawer;
import zombie.iso.IsoCamera;

public class ParticleRectangle {
   protected boolean DEBUG_BOUNDS = false;
   private int width;
   private int height;
   private WeatherParticle[] particles;
   private int particlesToRender;
   private int particlesReqUpdCnt = 0;

   public ParticleRectangle(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void SetParticles(WeatherParticle[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].setParent(this);
      }

      this.particles = var1;
      this.particlesToRender = var1.length;
   }

   public void SetParticlesStrength(float var1) {
      this.particlesToRender = (int)((float)this.particles.length * var1);
   }

   public boolean requiresUpdate() {
      return this.particlesToRender > 0 || this.particlesReqUpdCnt > 0;
   }

   public void update(float var1) {
      this.particlesReqUpdCnt = 0;

      for(int var2 = 0; var2 < this.particles.length; ++var2) {
         WeatherParticle var3 = this.particles[var2];
         if (var2 < this.particlesToRender) {
            var3.alphaFadeMod.setTarget(1.0F);
         } else if (var2 >= this.particlesToRender) {
            var3.alphaFadeMod.setTarget(0.0F);
         }

         var3.update(var1);
         if (var3.renderAlpha > 0.0F) {
            ++this.particlesReqUpdCnt;
         }
      }

   }

   public void render() {
      int var1 = IsoCamera.frameState.playerIndex;
      int var2 = IsoCamera.frameState.OffscreenWidth;
      int var3 = IsoCamera.frameState.OffscreenHeight;
      int var4 = (int)Math.ceil((double)(var2 / this.width)) + 2;
      int var5 = (int)Math.ceil((double)(var3 / this.height)) + 2;
      int var6;
      if (IsoCamera.frameState.OffX >= 0.0F) {
         var6 = (int)IsoCamera.frameState.OffX % this.width;
      } else {
         var6 = this.width - (int)Math.abs(IsoCamera.frameState.OffX) % this.width;
      }

      int var7;
      if (IsoCamera.frameState.OffY >= 0.0F) {
         var7 = (int)IsoCamera.frameState.OffY % this.height;
      } else {
         var7 = this.height - (int)Math.abs(IsoCamera.frameState.OffY) % this.height;
      }

      int var8 = -var6;
      int var9 = -var7;

      for(int var12 = 0; var12 < var5; ++var12) {
         for(int var13 = 0; var13 < var4; ++var13) {
            int var10 = var8 + var13 * this.width;
            int var11 = var9 + var12 * this.height;
            if (this.DEBUG_BOUNDS || IsoWeatherFX.DEBUG_BOUNDS) {
               LineDrawer.drawRect((float)var10, (float)var11, (float)this.width, (float)this.height, 0.0F, 1.0F, 0.0F, 1.0F, 1);
            }

            for(int var14 = 0; var14 < this.particles.length; ++var14) {
               WeatherParticle var15 = this.particles[var14];
               if (!(var15.renderAlpha <= 0.0F)) {
                  var15.render((float)var10, (float)var11);
                  if (this.DEBUG_BOUNDS || IsoWeatherFX.DEBUG_BOUNDS) {
                     LineDrawer.drawRect((float)(var10 + var15.bounds.getX()), (float)(var11 + var15.bounds.getY()), (float)var15.bounds.getWidth(), (float)var15.bounds.getHeight(), 0.0F, 0.0F, 1.0F, 0.5F, 1);
                  }
               }
            }
         }
      }

   }
}
