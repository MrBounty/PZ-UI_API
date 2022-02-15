package org.joml;

public class Interpolationd {
   public static double interpolateTriangle(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20) {
      double var22 = var8 - var14;
      double var24 = var12 - var6;
      double var26 = var0 - var12;
      double var28 = var20 - var14;
      double var30 = var18 - var12;
      double var32 = var2 - var14;
      double var34 = 1.0D / (var22 * var26 + var24 * var32);
      double var36 = (var22 * var30 + var24 * var28) * var34;
      double var38 = (var26 * var28 - var32 * var30) * var34;
      return var36 * var4 + var38 * var10 + (1.0D - var36 - var38) * var16;
   }

   public static Vector2d interpolateTriangle(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, double var26, Vector2d var28) {
      double var29 = var10 - var18;
      double var31 = var16 - var8;
      double var33 = var0 - var16;
      double var35 = var26 - var18;
      double var37 = var24 - var16;
      double var39 = var2 - var18;
      double var41 = 1.0D / (var29 * var33 + var31 * var39);
      double var43 = (var29 * var37 + var31 * var35) * var41;
      double var45 = (var33 * var35 - var39 * var37) * var41;
      double var47 = 1.0D - var43 - var45;
      var28.x = var43 * var4 + var45 * var12 + var47 * var20;
      var28.y = var43 * var6 + var45 * var14 + var47 * var22;
      return var28;
   }

   public static Vector2d dFdxLinear(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, Vector2d var24) {
      double var25 = var10 - var18;
      double var27 = var2 - var18;
      double var29 = var25 * (var0 - var16) + (var16 - var8) * var27;
      double var31 = var29 - var25 + var27;
      double var33 = 1.0D / var29;
      var24.x = var33 * (var25 * var4 - var27 * var12 + var31 * var20) - var20;
      var24.y = var33 * (var25 * var6 - var27 * var14 + var31 * var22) - var22;
      return var24;
   }

   public static Vector2d dFdyLinear(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, Vector2d var24) {
      double var25 = var16 - var8;
      double var27 = var0 - var16;
      double var29 = (var10 - var18) * var27 + var25 * (var2 - var18);
      double var31 = var29 - var25 - var27;
      double var33 = 1.0D / var29;
      var24.x = var33 * (var25 * var4 + var27 * var12 + var31 * var20) - var20;
      var24.y = var33 * (var25 * var6 + var27 * var14 + var31 * var22) - var22;
      return var24;
   }

   public static Vector3d interpolateTriangle(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, double var26, double var28, double var30, double var32, Vector3d var34) {
      interpolationFactorsTriangle(var0, var2, var10, var12, var20, var22, var30, var32, var34);
      return var34.set(var34.x * var4 + var34.y * var14 + var34.z * var24, var34.x * var6 + var34.y * var16 + var34.z * var26, var34.x * var8 + var34.y * var18 + var34.z * var28);
   }

   public static Vector3d interpolationFactorsTriangle(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, Vector3d var16) {
      double var17 = var6 - var10;
      double var19 = var8 - var4;
      double var21 = var0 - var8;
      double var23 = var14 - var10;
      double var25 = var12 - var8;
      double var27 = var2 - var10;
      double var29 = 1.0D / (var17 * var21 + var19 * var27);
      var16.x = (var17 * var25 + var19 * var23) * var29;
      var16.y = (var21 * var23 - var27 * var25) * var29;
      var16.z = 1.0D - var16.x - var16.y;
      return var16;
   }
}
