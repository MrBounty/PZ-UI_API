package org.joml.sampling;

import java.nio.FloatBuffer;

public class Convolution {
   public static void gaussianKernel(int var0, int var1, float var2, FloatBuffer var3) {
      if ((var0 & 1) == 0) {
         throw new IllegalArgumentException("rows must be an odd number");
      } else if ((var1 & 1) == 0) {
         throw new IllegalArgumentException("cols must be an odd number");
      } else if (var3 == null) {
         throw new IllegalArgumentException("dest must not be null");
      } else if (var3.remaining() < var0 * var1) {
         throw new IllegalArgumentException("dest must have at least " + var0 * var1 + " remaining values");
      } else {
         float var4 = 0.0F;
         int var5 = var3.position();
         int var6 = 0;

         for(int var7 = -(var0 - 1) / 2; var7 <= (var0 - 1) / 2; ++var7) {
            for(int var8 = -(var1 - 1) / 2; var8 <= (var1 - 1) / 2; ++var6) {
               float var9 = (float)org.joml.Math.exp((double)(-(var7 * var7 + var8 * var8)) / (2.0D * (double)var2 * (double)var2));
               var3.put(var5 + var6, var9);
               var4 += var9;
               ++var8;
            }
         }

         for(var6 = 0; var6 < var0 * var1; ++var6) {
            var3.put(var5 + var6, var3.get(var5 + var6) / var4);
         }

      }
   }

   public static void gaussianKernel(int var0, int var1, float var2, float[] var3) {
      if ((var0 & 1) == 0) {
         throw new IllegalArgumentException("rows must be an odd number");
      } else if ((var1 & 1) == 0) {
         throw new IllegalArgumentException("cols must be an odd number");
      } else if (var3 == null) {
         throw new IllegalArgumentException("dest must not be null");
      } else if (var3.length < var0 * var1) {
         throw new IllegalArgumentException("dest must have a size of at least " + var0 * var1);
      } else {
         float var4 = 0.0F;
         int var5 = 0;

         for(int var6 = -(var0 - 1) / 2; var6 <= (var0 - 1) / 2; ++var6) {
            for(int var7 = -(var1 - 1) / 2; var7 <= (var1 - 1) / 2; ++var5) {
               float var8 = (float)org.joml.Math.exp((double)(-(var6 * var6 + var7 * var7)) / (2.0D * (double)var2 * (double)var2));
               var3[var5] = var8;
               var4 += var8;
               ++var7;
            }
         }

         for(var5 = 0; var5 < var0 * var1; ++var5) {
            var3[var5] /= var4;
         }

      }
   }
}
