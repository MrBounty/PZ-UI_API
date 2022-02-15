package org.joml.sampling;

import org.joml.Random;

public class UniformSampling {
   public static class Sphere {
      private final Random rnd;

      public Sphere(long var1, int var3, Callback3d var4) {
         this.rnd = new Random(var1);
         this.generate(var3, var4);
      }

      public void generate(int var1, Callback3d var2) {
         int var3 = 0;

         while(var3 < var1) {
            float var4 = this.rnd.nextFloat() * 2.0F - 1.0F;
            float var5 = this.rnd.nextFloat() * 2.0F - 1.0F;
            if (!(var4 * var4 + var5 * var5 >= 1.0F)) {
               float var6 = (float)Math.sqrt(1.0D - (double)(var4 * var4) - (double)(var5 * var5));
               float var7 = 2.0F * var4 * var6;
               float var8 = 2.0F * var5 * var6;
               float var9 = 1.0F - 2.0F * (var4 * var4 + var5 * var5);
               var2.onNewSample(var7, var8, var9);
               ++var3;
            }
         }

      }
   }

   public static class Disk {
      private final Random rnd;

      public Disk(long var1, int var3, Callback2d var4) {
         this.rnd = new Random(var1);
         this.generate(var3, var4);
      }

      private void generate(int var1, Callback2d var2) {
         for(int var3 = 0; var3 < var1; ++var3) {
            float var4 = this.rnd.nextFloat();
            float var5 = this.rnd.nextFloat() * 2.0F * 3.1415927F;
            float var6 = Math.sqrt(var4);
            float var7 = var6 * (float)Math.sin_roquen_9((double)var5 + 1.5707963267948966D);
            float var8 = var6 * (float)Math.sin_roquen_9((double)var5);
            var2.onNewSample(var7, var8);
         }

      }
   }
}
