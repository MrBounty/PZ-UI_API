package org.joml.sampling;

import org.joml.Random;

public class SpiralSampling {
   private final Random rnd;

   public SpiralSampling(long var1) {
      this.rnd = new Random(var1);
   }

   public void createEquiAngle(float var1, int var2, int var3, Callback2d var4) {
      for(int var5 = 0; var5 < var3; ++var5) {
         float var6 = 6.2831855F * (float)(var5 * var2) / (float)var3;
         float var7 = var1 * (float)var5 / (float)(var3 - 1);
         float var8 = (float)Math.sin_roquen_9((double)(var6 + 1.5707964F)) * var7;
         float var9 = (float)Math.sin_roquen_9((double)var6) * var7;
         var4.onNewSample(var8, var9);
      }

   }

   public void createEquiAngle(float var1, int var2, int var3, float var4, Callback2d var5) {
      float var6 = var1 / (float)var2;

      for(int var7 = 0; var7 < var3; ++var7) {
         float var8 = 6.2831855F * (float)(var7 * var2) / (float)var3;
         float var9 = var1 * (float)var7 / (float)(var3 - 1) + (this.rnd.nextFloat() * 2.0F - 1.0F) * var6 * var4;
         float var10 = (float)Math.sin_roquen_9((double)(var8 + 1.5707964F)) * var9;
         float var11 = (float)Math.sin_roquen_9((double)var8) * var9;
         var5.onNewSample(var10, var11);
      }

   }
}
