package org.joml.sampling;

import org.joml.Random;

public class StratifiedSampling {
   private final Random rnd;

   public StratifiedSampling(long var1) {
      this.rnd = new Random(var1);
   }

   public void generateRandom(int var1, Callback2d var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         for(int var4 = 0; var4 < var1; ++var4) {
            float var5 = (this.rnd.nextFloat() / (float)var1 + (float)var4 / (float)var1) * 2.0F - 1.0F;
            float var6 = (this.rnd.nextFloat() / (float)var1 + (float)var3 / (float)var1) * 2.0F - 1.0F;
            var2.onNewSample(var5, var6);
         }
      }

   }

   public void generateCentered(int var1, float var2, Callback2d var3) {
      float var4 = var2 * 0.5F;
      float var5 = 1.0F - var2;

      for(int var6 = 0; var6 < var1; ++var6) {
         for(int var7 = 0; var7 < var1; ++var7) {
            float var8 = ((var4 + this.rnd.nextFloat() * var5) / (float)var1 + (float)var7 / (float)var1) * 2.0F - 1.0F;
            float var9 = ((var4 + this.rnd.nextFloat() * var5) / (float)var1 + (float)var6 / (float)var1) * 2.0F - 1.0F;
            var3.onNewSample(var8, var9);
         }
      }

   }
}
