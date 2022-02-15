package org.joml;

public class SimplexNoise {
   private static final SimplexNoise.Vector3b[] grad3 = new SimplexNoise.Vector3b[]{new SimplexNoise.Vector3b(1, 1, 0), new SimplexNoise.Vector3b(-1, 1, 0), new SimplexNoise.Vector3b(1, -1, 0), new SimplexNoise.Vector3b(-1, -1, 0), new SimplexNoise.Vector3b(1, 0, 1), new SimplexNoise.Vector3b(-1, 0, 1), new SimplexNoise.Vector3b(1, 0, -1), new SimplexNoise.Vector3b(-1, 0, -1), new SimplexNoise.Vector3b(0, 1, 1), new SimplexNoise.Vector3b(0, -1, 1), new SimplexNoise.Vector3b(0, 1, -1), new SimplexNoise.Vector3b(0, -1, -1)};
   private static final SimplexNoise.Vector4b[] grad4 = new SimplexNoise.Vector4b[]{new SimplexNoise.Vector4b(0, 1, 1, 1), new SimplexNoise.Vector4b(0, 1, 1, -1), new SimplexNoise.Vector4b(0, 1, -1, 1), new SimplexNoise.Vector4b(0, 1, -1, -1), new SimplexNoise.Vector4b(0, -1, 1, 1), new SimplexNoise.Vector4b(0, -1, 1, -1), new SimplexNoise.Vector4b(0, -1, -1, 1), new SimplexNoise.Vector4b(0, -1, -1, -1), new SimplexNoise.Vector4b(1, 0, 1, 1), new SimplexNoise.Vector4b(1, 0, 1, -1), new SimplexNoise.Vector4b(1, 0, -1, 1), new SimplexNoise.Vector4b(1, 0, -1, -1), new SimplexNoise.Vector4b(-1, 0, 1, 1), new SimplexNoise.Vector4b(-1, 0, 1, -1), new SimplexNoise.Vector4b(-1, 0, -1, 1), new SimplexNoise.Vector4b(-1, 0, -1, -1), new SimplexNoise.Vector4b(1, 1, 0, 1), new SimplexNoise.Vector4b(1, 1, 0, -1), new SimplexNoise.Vector4b(1, -1, 0, 1), new SimplexNoise.Vector4b(1, -1, 0, -1), new SimplexNoise.Vector4b(-1, 1, 0, 1), new SimplexNoise.Vector4b(-1, 1, 0, -1), new SimplexNoise.Vector4b(-1, -1, 0, 1), new SimplexNoise.Vector4b(-1, -1, 0, -1), new SimplexNoise.Vector4b(1, 1, 1, 0), new SimplexNoise.Vector4b(1, 1, -1, 0), new SimplexNoise.Vector4b(1, -1, 1, 0), new SimplexNoise.Vector4b(1, -1, -1, 0), new SimplexNoise.Vector4b(-1, 1, 1, 0), new SimplexNoise.Vector4b(-1, 1, -1, 0), new SimplexNoise.Vector4b(-1, -1, 1, 0), new SimplexNoise.Vector4b(-1, -1, -1, 0)};
   private static final byte[] p = new byte[]{-105, -96, -119, 91, 90, 15, -125, 13, -55, 95, 96, 53, -62, -23, 7, -31, -116, 36, 103, 30, 69, -114, 8, 99, 37, -16, 21, 10, 23, -66, 6, -108, -9, 120, -22, 75, 0, 26, -59, 62, 94, -4, -37, -53, 117, 35, 11, 32, 57, -79, 33, 88, -19, -107, 56, 87, -82, 20, 125, -120, -85, -88, 68, -81, 74, -91, 71, -122, -117, 48, 27, -90, 77, -110, -98, -25, 83, 111, -27, 122, 60, -45, -123, -26, -36, 105, 92, 41, 55, 46, -11, 40, -12, 102, -113, 54, 65, 25, 63, -95, 1, -40, 80, 73, -47, 76, -124, -69, -48, 89, 18, -87, -56, -60, -121, -126, 116, -68, -97, 86, -92, 100, 109, -58, -83, -70, 3, 64, 52, -39, -30, -6, 124, 123, 5, -54, 38, -109, 118, 126, -1, 82, 85, -44, -49, -50, 59, -29, 47, 16, 58, 17, -74, -67, 28, 42, -33, -73, -86, -43, 119, -8, -104, 2, 44, -102, -93, 70, -35, -103, 101, -101, -89, 43, -84, 9, -127, 22, 39, -3, 19, 98, 108, 110, 79, 113, -32, -24, -78, -71, 112, 104, -38, -10, 97, -28, -5, 34, -14, -63, -18, -46, -112, 12, -65, -77, -94, -15, 81, 51, -111, -21, -7, 14, -17, 107, 49, -64, -42, 31, -75, -57, 106, -99, -72, 84, -52, -80, 115, 121, 50, 45, 127, 4, -106, -2, -118, -20, -51, 93, -34, 114, 67, 29, 24, 72, -13, -115, -128, -61, 78, 66, -41, 61, -100, -76};
   private static final byte[] perm = new byte[512];
   private static final byte[] permMod12 = new byte[512];
   private static final float F2 = 0.36602542F;
   private static final float G2 = 0.21132487F;
   private static final float F3 = 0.33333334F;
   private static final float G3 = 0.16666667F;
   private static final float F4 = 0.309017F;
   private static final float G4 = 0.1381966F;

   private static int fastfloor(float var0) {
      int var1 = (int)var0;
      return var0 < (float)var1 ? var1 - 1 : var1;
   }

   private static float dot(SimplexNoise.Vector3b var0, float var1, float var2) {
      return (float)var0.x * var1 + (float)var0.y * var2;
   }

   private static float dot(SimplexNoise.Vector3b var0, float var1, float var2, float var3) {
      return (float)var0.x * var1 + (float)var0.y * var2 + (float)var0.z * var3;
   }

   private static float dot(SimplexNoise.Vector4b var0, float var1, float var2, float var3, float var4) {
      return (float)var0.x * var1 + (float)var0.y * var2 + (float)var0.z * var3 + (float)var0.w * var4;
   }

   public static float noise(float var0, float var1) {
      float var5 = (var0 + var1) * 0.36602542F;
      int var6 = fastfloor(var0 + var5);
      int var7 = fastfloor(var1 + var5);
      float var8 = (float)(var6 + var7) * 0.21132487F;
      float var9 = (float)var6 - var8;
      float var10 = (float)var7 - var8;
      float var11 = var0 - var9;
      float var12 = var1 - var10;
      byte var13;
      byte var14;
      if (var11 > var12) {
         var13 = 1;
         var14 = 0;
      } else {
         var13 = 0;
         var14 = 1;
      }

      float var15 = var11 - (float)var13 + 0.21132487F;
      float var16 = var12 - (float)var14 + 0.21132487F;
      float var17 = var11 - 1.0F + 0.42264974F;
      float var18 = var12 - 1.0F + 0.42264974F;
      int var19 = var6 & 255;
      int var20 = var7 & 255;
      int var21 = permMod12[var19 + perm[var20] & 255] & 255;
      int var22 = permMod12[var19 + var13 + perm[var20 + var14] & 255] & 255;
      int var23 = permMod12[var19 + 1 + perm[var20 + 1] & 255] & 255;
      float var24 = 0.5F - var11 * var11 - var12 * var12;
      float var2;
      if (var24 < 0.0F) {
         var2 = 0.0F;
      } else {
         var24 *= var24;
         var2 = var24 * var24 * dot(grad3[var21], var11, var12);
      }

      float var25 = 0.5F - var15 * var15 - var16 * var16;
      float var3;
      if (var25 < 0.0F) {
         var3 = 0.0F;
      } else {
         var25 *= var25;
         var3 = var25 * var25 * dot(grad3[var22], var15, var16);
      }

      float var26 = 0.5F - var17 * var17 - var18 * var18;
      float var4;
      if (var26 < 0.0F) {
         var4 = 0.0F;
      } else {
         var26 *= var26;
         var4 = var26 * var26 * dot(grad3[var23], var17, var18);
      }

      return 70.0F * (var2 + var3 + var4);
   }

   public static float noise(float var0, float var1, float var2) {
      float var7 = (var0 + var1 + var2) * 0.33333334F;
      int var8 = fastfloor(var0 + var7);
      int var9 = fastfloor(var1 + var7);
      int var10 = fastfloor(var2 + var7);
      float var11 = (float)(var8 + var9 + var10) * 0.16666667F;
      float var12 = (float)var8 - var11;
      float var13 = (float)var9 - var11;
      float var14 = (float)var10 - var11;
      float var15 = var0 - var12;
      float var16 = var1 - var13;
      float var17 = var2 - var14;
      byte var18;
      byte var19;
      byte var20;
      byte var21;
      byte var22;
      byte var23;
      if (var15 >= var16) {
         if (var16 >= var17) {
            var18 = 1;
            var19 = 0;
            var20 = 0;
            var21 = 1;
            var22 = 1;
            var23 = 0;
         } else if (var15 >= var17) {
            var18 = 1;
            var19 = 0;
            var20 = 0;
            var21 = 1;
            var22 = 0;
            var23 = 1;
         } else {
            var18 = 0;
            var19 = 0;
            var20 = 1;
            var21 = 1;
            var22 = 0;
            var23 = 1;
         }
      } else if (var16 < var17) {
         var18 = 0;
         var19 = 0;
         var20 = 1;
         var21 = 0;
         var22 = 1;
         var23 = 1;
      } else if (var15 < var17) {
         var18 = 0;
         var19 = 1;
         var20 = 0;
         var21 = 0;
         var22 = 1;
         var23 = 1;
      } else {
         var18 = 0;
         var19 = 1;
         var20 = 0;
         var21 = 1;
         var22 = 1;
         var23 = 0;
      }

      float var24 = var15 - (float)var18 + 0.16666667F;
      float var25 = var16 - (float)var19 + 0.16666667F;
      float var26 = var17 - (float)var20 + 0.16666667F;
      float var27 = var15 - (float)var21 + 0.33333334F;
      float var28 = var16 - (float)var22 + 0.33333334F;
      float var29 = var17 - (float)var23 + 0.33333334F;
      float var30 = var15 - 1.0F + 0.5F;
      float var31 = var16 - 1.0F + 0.5F;
      float var32 = var17 - 1.0F + 0.5F;
      int var33 = var8 & 255;
      int var34 = var9 & 255;
      int var35 = var10 & 255;
      int var36 = permMod12[var33 + perm[var34 + perm[var35] & 255] & 255] & 255;
      int var37 = permMod12[var33 + var18 + perm[var34 + var19 + perm[var35 + var20] & 255] & 255] & 255;
      int var38 = permMod12[var33 + var21 + perm[var34 + var22 + perm[var35 + var23] & 255] & 255] & 255;
      int var39 = permMod12[var33 + 1 + perm[var34 + 1 + perm[var35 + 1] & 255] & 255] & 255;
      float var40 = 0.6F - var15 * var15 - var16 * var16 - var17 * var17;
      float var3;
      if (var40 < 0.0F) {
         var3 = 0.0F;
      } else {
         var40 *= var40;
         var3 = var40 * var40 * dot(grad3[var36], var15, var16, var17);
      }

      float var41 = 0.6F - var24 * var24 - var25 * var25 - var26 * var26;
      float var4;
      if (var41 < 0.0F) {
         var4 = 0.0F;
      } else {
         var41 *= var41;
         var4 = var41 * var41 * dot(grad3[var37], var24, var25, var26);
      }

      float var42 = 0.6F - var27 * var27 - var28 * var28 - var29 * var29;
      float var5;
      if (var42 < 0.0F) {
         var5 = 0.0F;
      } else {
         var42 *= var42;
         var5 = var42 * var42 * dot(grad3[var38], var27, var28, var29);
      }

      float var43 = 0.6F - var30 * var30 - var31 * var31 - var32 * var32;
      float var6;
      if (var43 < 0.0F) {
         var6 = 0.0F;
      } else {
         var43 *= var43;
         var6 = var43 * var43 * dot(grad3[var39], var30, var31, var32);
      }

      return 32.0F * (var3 + var4 + var5 + var6);
   }

   public static float noise(float var0, float var1, float var2, float var3) {
      float var9 = (var0 + var1 + var2 + var3) * 0.309017F;
      int var10 = fastfloor(var0 + var9);
      int var11 = fastfloor(var1 + var9);
      int var12 = fastfloor(var2 + var9);
      int var13 = fastfloor(var3 + var9);
      float var14 = (float)(var10 + var11 + var12 + var13) * 0.1381966F;
      float var15 = (float)var10 - var14;
      float var16 = (float)var11 - var14;
      float var17 = (float)var12 - var14;
      float var18 = (float)var13 - var14;
      float var19 = var0 - var15;
      float var20 = var1 - var16;
      float var21 = var2 - var17;
      float var22 = var3 - var18;
      int var23 = 0;
      int var24 = 0;
      int var25 = 0;
      int var26 = 0;
      if (var19 > var20) {
         ++var23;
      } else {
         ++var24;
      }

      if (var19 > var21) {
         ++var23;
      } else {
         ++var25;
      }

      if (var19 > var22) {
         ++var23;
      } else {
         ++var26;
      }

      if (var20 > var21) {
         ++var24;
      } else {
         ++var25;
      }

      if (var20 > var22) {
         ++var24;
      } else {
         ++var26;
      }

      if (var21 > var22) {
         ++var25;
      } else {
         ++var26;
      }

      int var27 = var23 >= 3 ? 1 : 0;
      int var28 = var24 >= 3 ? 1 : 0;
      int var29 = var25 >= 3 ? 1 : 0;
      int var30 = var26 >= 3 ? 1 : 0;
      int var31 = var23 >= 2 ? 1 : 0;
      int var32 = var24 >= 2 ? 1 : 0;
      int var33 = var25 >= 2 ? 1 : 0;
      int var34 = var26 >= 2 ? 1 : 0;
      int var35 = var23 >= 1 ? 1 : 0;
      int var36 = var24 >= 1 ? 1 : 0;
      int var37 = var25 >= 1 ? 1 : 0;
      int var38 = var26 >= 1 ? 1 : 0;
      float var39 = var19 - (float)var27 + 0.1381966F;
      float var40 = var20 - (float)var28 + 0.1381966F;
      float var41 = var21 - (float)var29 + 0.1381966F;
      float var42 = var22 - (float)var30 + 0.1381966F;
      float var43 = var19 - (float)var31 + 0.2763932F;
      float var44 = var20 - (float)var32 + 0.2763932F;
      float var45 = var21 - (float)var33 + 0.2763932F;
      float var46 = var22 - (float)var34 + 0.2763932F;
      float var47 = var19 - (float)var35 + 0.41458982F;
      float var48 = var20 - (float)var36 + 0.41458982F;
      float var49 = var21 - (float)var37 + 0.41458982F;
      float var50 = var22 - (float)var38 + 0.41458982F;
      float var51 = var19 - 1.0F + 0.5527864F;
      float var52 = var20 - 1.0F + 0.5527864F;
      float var53 = var21 - 1.0F + 0.5527864F;
      float var54 = var22 - 1.0F + 0.5527864F;
      int var55 = var10 & 255;
      int var56 = var11 & 255;
      int var57 = var12 & 255;
      int var58 = var13 & 255;
      int var59 = (perm[var55 + perm[var56 + perm[var57 + perm[var58] & 255] & 255] & 255] & 255) % 32;
      int var60 = (perm[var55 + var27 + perm[var56 + var28 + perm[var57 + var29 + perm[var58 + var30] & 255] & 255] & 255] & 255) % 32;
      int var61 = (perm[var55 + var31 + perm[var56 + var32 + perm[var57 + var33 + perm[var58 + var34] & 255] & 255] & 255] & 255) % 32;
      int var62 = (perm[var55 + var35 + perm[var56 + var36 + perm[var57 + var37 + perm[var58 + var38] & 255] & 255] & 255] & 255) % 32;
      int var63 = (perm[var55 + 1 + perm[var56 + 1 + perm[var57 + 1 + perm[var58 + 1] & 255] & 255] & 255] & 255) % 32;
      float var64 = 0.6F - var19 * var19 - var20 * var20 - var21 * var21 - var22 * var22;
      float var4;
      if (var64 < 0.0F) {
         var4 = 0.0F;
      } else {
         var64 *= var64;
         var4 = var64 * var64 * dot(grad4[var59], var19, var20, var21, var22);
      }

      float var65 = 0.6F - var39 * var39 - var40 * var40 - var41 * var41 - var42 * var42;
      float var5;
      if (var65 < 0.0F) {
         var5 = 0.0F;
      } else {
         var65 *= var65;
         var5 = var65 * var65 * dot(grad4[var60], var39, var40, var41, var42);
      }

      float var66 = 0.6F - var43 * var43 - var44 * var44 - var45 * var45 - var46 * var46;
      float var6;
      if (var66 < 0.0F) {
         var6 = 0.0F;
      } else {
         var66 *= var66;
         var6 = var66 * var66 * dot(grad4[var61], var43, var44, var45, var46);
      }

      float var67 = 0.6F - var47 * var47 - var48 * var48 - var49 * var49 - var50 * var50;
      float var7;
      if (var67 < 0.0F) {
         var7 = 0.0F;
      } else {
         var67 *= var67;
         var7 = var67 * var67 * dot(grad4[var62], var47, var48, var49, var50);
      }

      float var68 = 0.6F - var51 * var51 - var52 * var52 - var53 * var53 - var54 * var54;
      float var8;
      if (var68 < 0.0F) {
         var8 = 0.0F;
      } else {
         var68 *= var68;
         var8 = var68 * var68 * dot(grad4[var63], var51, var52, var53, var54);
      }

      return 27.0F * (var4 + var5 + var6 + var7 + var8);
   }

   static {
      for(int var0 = 0; var0 < 512; ++var0) {
         perm[var0] = p[var0 & 255];
         permMod12[var0] = (byte)((perm[var0] & 255) % 12);
      }

   }

   private static class Vector3b {
      byte x;
      byte y;
      byte z;

      Vector3b(int var1, int var2, int var3) {
         this.x = (byte)var1;
         this.y = (byte)var2;
         this.z = (byte)var3;
      }
   }

   private static class Vector4b {
      byte x;
      byte y;
      byte z;
      byte w;

      Vector4b(int var1, int var2, int var3, int var4) {
         this.x = (byte)var1;
         this.y = (byte)var2;
         this.z = (byte)var3;
         this.w = (byte)var4;
      }
   }
}
