package org.joml;

public class Interpolationf {
   public static float interpolateTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      float var11 = var4 - var7;
      float var12 = var6 - var3;
      float var13 = var0 - var6;
      float var14 = var10 - var7;
      float var15 = var9 - var6;
      float var16 = var1 - var7;
      float var17 = 1.0F / (var11 * var13 + var12 * var16);
      float var18 = (var11 * var15 + var12 * var14) * var17;
      float var19 = (var13 * var14 - var16 * var15) * var17;
      return var18 * var2 + var19 * var5 + (1.0F - var18 - var19) * var8;
   }

   public static Vector2f interpolateTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, Vector2f var14) {
      float var15 = var5 - var9;
      float var16 = var8 - var4;
      float var17 = var0 - var8;
      float var18 = var13 - var9;
      float var19 = var12 - var8;
      float var20 = var1 - var9;
      float var21 = 1.0F / (var15 * var17 + var16 * var20);
      float var22 = (var15 * var19 + var16 * var18) * var21;
      float var23 = (var17 * var18 - var20 * var19) * var21;
      float var24 = 1.0F - var22 - var23;
      var14.x = var22 * var2 + var23 * var6 + var24 * var10;
      var14.y = var22 * var3 + var23 * var7 + var24 * var11;
      return var14;
   }

   public static Vector2f dFdxLinear(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, Vector2f var12) {
      float var13 = var5 - var9;
      float var14 = var1 - var9;
      float var15 = var13 * (var0 - var8) + (var8 - var4) * var14;
      float var16 = var15 - var13 + var14;
      float var17 = 1.0F / var15;
      var12.x = var17 * (var13 * var2 - var14 * var6 + var16 * var10) - var10;
      var12.y = var17 * (var13 * var3 - var14 * var7 + var16 * var11) - var11;
      return var12;
   }

   public static Vector2f dFdyLinear(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, Vector2f var12) {
      float var13 = var8 - var4;
      float var14 = var0 - var8;
      float var15 = (var5 - var9) * var14 + var13 * (var1 - var9);
      float var16 = var15 - var13 - var14;
      float var17 = 1.0F / var15;
      var12.x = var17 * (var13 * var2 + var14 * var6 + var16 * var10) - var10;
      var12.y = var17 * (var13 * var3 + var14 * var7 + var16 * var11) - var11;
      return var12;
   }

   public static Vector3f interpolateTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, Vector3f var17) {
      interpolationFactorsTriangle(var0, var1, var5, var6, var10, var11, var15, var16, var17);
      return var17.set(var17.x * var2 + var17.y * var7 + var17.z * var12, var17.x * var3 + var17.y * var8 + var17.z * var13, var17.x * var4 + var17.y * var9 + var17.z * var14);
   }

   public static Vector3f interpolationFactorsTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, Vector3f var8) {
      float var9 = var3 - var5;
      float var10 = var4 - var2;
      float var11 = var0 - var4;
      float var12 = var7 - var5;
      float var13 = var6 - var4;
      float var14 = var1 - var5;
      float var15 = 1.0F / (var9 * var11 + var10 * var14);
      var8.x = (var9 * var13 + var10 * var12) * var15;
      var8.y = (var11 * var12 - var14 * var13) * var15;
      var8.z = 1.0F - var8.x - var8.y;
      return var8;
   }
}
