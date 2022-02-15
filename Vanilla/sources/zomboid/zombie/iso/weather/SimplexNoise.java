package zombie.iso.weather;

public class SimplexNoise {
   private static SimplexNoise.Grad[] grad3 = new SimplexNoise.Grad[]{new SimplexNoise.Grad(1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(1.0D, -1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, -1.0D, 0.0D), new SimplexNoise.Grad(1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(0.0D, 1.0D, 1.0D), new SimplexNoise.Grad(0.0D, -1.0D, 1.0D), new SimplexNoise.Grad(0.0D, 1.0D, -1.0D), new SimplexNoise.Grad(0.0D, -1.0D, -1.0D)};
   private static SimplexNoise.Grad[] grad4 = new SimplexNoise.Grad[]{new SimplexNoise.Grad(0.0D, 1.0D, 1.0D, 1.0D), new SimplexNoise.Grad(0.0D, 1.0D, 1.0D, -1.0D), new SimplexNoise.Grad(0.0D, 1.0D, -1.0D, 1.0D), new SimplexNoise.Grad(0.0D, 1.0D, -1.0D, -1.0D), new SimplexNoise.Grad(0.0D, -1.0D, 1.0D, 1.0D), new SimplexNoise.Grad(0.0D, -1.0D, 1.0D, -1.0D), new SimplexNoise.Grad(0.0D, -1.0D, -1.0D, 1.0D), new SimplexNoise.Grad(0.0D, -1.0D, -1.0D, -1.0D), new SimplexNoise.Grad(1.0D, 0.0D, 1.0D, 1.0D), new SimplexNoise.Grad(1.0D, 0.0D, 1.0D, -1.0D), new SimplexNoise.Grad(1.0D, 0.0D, -1.0D, 1.0D), new SimplexNoise.Grad(1.0D, 0.0D, -1.0D, -1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, 1.0D, 1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, 1.0D, -1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, -1.0D, 1.0D), new SimplexNoise.Grad(-1.0D, 0.0D, -1.0D, -1.0D), new SimplexNoise.Grad(1.0D, 1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(1.0D, 1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(1.0D, -1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(1.0D, -1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(-1.0D, 1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(-1.0D, 1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(-1.0D, -1.0D, 0.0D, 1.0D), new SimplexNoise.Grad(-1.0D, -1.0D, 0.0D, -1.0D), new SimplexNoise.Grad(1.0D, 1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(1.0D, 1.0D, -1.0D, 0.0D), new SimplexNoise.Grad(1.0D, -1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(1.0D, -1.0D, -1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, 1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, 1.0D, -1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, -1.0D, 1.0D, 0.0D), new SimplexNoise.Grad(-1.0D, -1.0D, -1.0D, 0.0D)};
   private static short[] p = new short[]{151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};
   private static short[] perm = new short[512];
   private static short[] permMod12 = new short[512];
   private static final double F2;
   private static final double G2;
   private static final double F3 = 0.3333333333333333D;
   private static final double G3 = 0.16666666666666666D;
   private static final double F4;
   private static final double G4;

   private static int fastfloor(double var0) {
      int var2 = (int)var0;
      return var0 < (double)var2 ? var2 - 1 : var2;
   }

   private static double dot(SimplexNoise.Grad var0, double var1, double var3) {
      return var0.x * var1 + var0.y * var3;
   }

   private static double dot(SimplexNoise.Grad var0, double var1, double var3, double var5) {
      return var0.x * var1 + var0.y * var3 + var0.z * var5;
   }

   private static double dot(SimplexNoise.Grad var0, double var1, double var3, double var5, double var7) {
      return var0.x * var1 + var0.y * var3 + var0.z * var5 + var0.w * var7;
   }

   public static double noise(double var0, double var2) {
      double var10 = (var0 + var2) * F2;
      int var12 = fastfloor(var0 + var10);
      int var13 = fastfloor(var2 + var10);
      double var14 = (double)(var12 + var13) * G2;
      double var16 = (double)var12 - var14;
      double var18 = (double)var13 - var14;
      double var20 = var0 - var16;
      double var22 = var2 - var18;
      byte var24;
      byte var25;
      if (var20 > var22) {
         var24 = 1;
         var25 = 0;
      } else {
         var24 = 0;
         var25 = 1;
      }

      double var26 = var20 - (double)var24 + G2;
      double var28 = var22 - (double)var25 + G2;
      double var30 = var20 - 1.0D + 2.0D * G2;
      double var32 = var22 - 1.0D + 2.0D * G2;
      int var34 = var12 & 255;
      int var35 = var13 & 255;
      short var36 = permMod12[var34 + perm[var35]];
      short var37 = permMod12[var34 + var24 + perm[var35 + var25]];
      short var38 = permMod12[var34 + 1 + perm[var35 + 1]];
      double var39 = 0.5D - var20 * var20 - var22 * var22;
      double var4;
      if (var39 < 0.0D) {
         var4 = 0.0D;
      } else {
         var39 *= var39;
         var4 = var39 * var39 * dot(grad3[var36], var20, var22);
      }

      double var41 = 0.5D - var26 * var26 - var28 * var28;
      double var6;
      if (var41 < 0.0D) {
         var6 = 0.0D;
      } else {
         var41 *= var41;
         var6 = var41 * var41 * dot(grad3[var37], var26, var28);
      }

      double var43 = 0.5D - var30 * var30 - var32 * var32;
      double var8;
      if (var43 < 0.0D) {
         var8 = 0.0D;
      } else {
         var43 *= var43;
         var8 = var43 * var43 * dot(grad3[var38], var30, var32);
      }

      return 70.0D * (var4 + var6 + var8);
   }

   public static double noise(double var0, double var2, double var4) {
      double var14 = (var0 + var2 + var4) * 0.3333333333333333D;
      int var16 = fastfloor(var0 + var14);
      int var17 = fastfloor(var2 + var14);
      int var18 = fastfloor(var4 + var14);
      double var19 = (double)(var16 + var17 + var18) * 0.16666666666666666D;
      double var21 = (double)var16 - var19;
      double var23 = (double)var17 - var19;
      double var25 = (double)var18 - var19;
      double var27 = var0 - var21;
      double var29 = var2 - var23;
      double var31 = var4 - var25;
      byte var33;
      byte var34;
      byte var35;
      byte var36;
      byte var37;
      byte var38;
      if (var27 >= var29) {
         if (var29 >= var31) {
            var33 = 1;
            var34 = 0;
            var35 = 0;
            var36 = 1;
            var37 = 1;
            var38 = 0;
         } else if (var27 >= var31) {
            var33 = 1;
            var34 = 0;
            var35 = 0;
            var36 = 1;
            var37 = 0;
            var38 = 1;
         } else {
            var33 = 0;
            var34 = 0;
            var35 = 1;
            var36 = 1;
            var37 = 0;
            var38 = 1;
         }
      } else if (var29 < var31) {
         var33 = 0;
         var34 = 0;
         var35 = 1;
         var36 = 0;
         var37 = 1;
         var38 = 1;
      } else if (var27 < var31) {
         var33 = 0;
         var34 = 1;
         var35 = 0;
         var36 = 0;
         var37 = 1;
         var38 = 1;
      } else {
         var33 = 0;
         var34 = 1;
         var35 = 0;
         var36 = 1;
         var37 = 1;
         var38 = 0;
      }

      double var39 = var27 - (double)var33 + 0.16666666666666666D;
      double var41 = var29 - (double)var34 + 0.16666666666666666D;
      double var43 = var31 - (double)var35 + 0.16666666666666666D;
      double var45 = var27 - (double)var36 + 0.3333333333333333D;
      double var47 = var29 - (double)var37 + 0.3333333333333333D;
      double var49 = var31 - (double)var38 + 0.3333333333333333D;
      double var51 = var27 - 1.0D + 0.5D;
      double var53 = var29 - 1.0D + 0.5D;
      double var55 = var31 - 1.0D + 0.5D;
      int var57 = var16 & 255;
      int var58 = var17 & 255;
      int var59 = var18 & 255;
      short var60 = permMod12[var57 + perm[var58 + perm[var59]]];
      short var61 = permMod12[var57 + var33 + perm[var58 + var34 + perm[var59 + var35]]];
      short var62 = permMod12[var57 + var36 + perm[var58 + var37 + perm[var59 + var38]]];
      short var63 = permMod12[var57 + 1 + perm[var58 + 1 + perm[var59 + 1]]];
      double var64 = 0.6D - var27 * var27 - var29 * var29 - var31 * var31;
      double var6;
      if (var64 < 0.0D) {
         var6 = 0.0D;
      } else {
         var64 *= var64;
         var6 = var64 * var64 * dot(grad3[var60], var27, var29, var31);
      }

      double var66 = 0.6D - var39 * var39 - var41 * var41 - var43 * var43;
      double var8;
      if (var66 < 0.0D) {
         var8 = 0.0D;
      } else {
         var66 *= var66;
         var8 = var66 * var66 * dot(grad3[var61], var39, var41, var43);
      }

      double var68 = 0.6D - var45 * var45 - var47 * var47 - var49 * var49;
      double var10;
      if (var68 < 0.0D) {
         var10 = 0.0D;
      } else {
         var68 *= var68;
         var10 = var68 * var68 * dot(grad3[var62], var45, var47, var49);
      }

      double var70 = 0.6D - var51 * var51 - var53 * var53 - var55 * var55;
      double var12;
      if (var70 < 0.0D) {
         var12 = 0.0D;
      } else {
         var70 *= var70;
         var12 = var70 * var70 * dot(grad3[var63], var51, var53, var55);
      }

      return 32.0D * (var6 + var8 + var10 + var12);
   }

   public static double noise(double var0, double var2, double var4, double var6) {
      double var18 = (var0 + var2 + var4 + var6) * F4;
      int var20 = fastfloor(var0 + var18);
      int var21 = fastfloor(var2 + var18);
      int var22 = fastfloor(var4 + var18);
      int var23 = fastfloor(var6 + var18);
      double var24 = (double)(var20 + var21 + var22 + var23) * G4;
      double var26 = (double)var20 - var24;
      double var28 = (double)var21 - var24;
      double var30 = (double)var22 - var24;
      double var32 = (double)var23 - var24;
      double var34 = var0 - var26;
      double var36 = var2 - var28;
      double var38 = var4 - var30;
      double var40 = var6 - var32;
      int var42 = 0;
      int var43 = 0;
      int var44 = 0;
      int var45 = 0;
      if (var34 > var36) {
         ++var42;
      } else {
         ++var43;
      }

      if (var34 > var38) {
         ++var42;
      } else {
         ++var44;
      }

      if (var34 > var40) {
         ++var42;
      } else {
         ++var45;
      }

      if (var36 > var38) {
         ++var43;
      } else {
         ++var44;
      }

      if (var36 > var40) {
         ++var43;
      } else {
         ++var45;
      }

      if (var38 > var40) {
         ++var44;
      } else {
         ++var45;
      }

      int var46 = var42 >= 3 ? 1 : 0;
      int var47 = var43 >= 3 ? 1 : 0;
      int var48 = var44 >= 3 ? 1 : 0;
      int var49 = var45 >= 3 ? 1 : 0;
      int var50 = var42 >= 2 ? 1 : 0;
      int var51 = var43 >= 2 ? 1 : 0;
      int var52 = var44 >= 2 ? 1 : 0;
      int var53 = var45 >= 2 ? 1 : 0;
      int var54 = var42 >= 1 ? 1 : 0;
      int var55 = var43 >= 1 ? 1 : 0;
      int var56 = var44 >= 1 ? 1 : 0;
      int var57 = var45 >= 1 ? 1 : 0;
      double var58 = var34 - (double)var46 + G4;
      double var60 = var36 - (double)var47 + G4;
      double var62 = var38 - (double)var48 + G4;
      double var64 = var40 - (double)var49 + G4;
      double var66 = var34 - (double)var50 + 2.0D * G4;
      double var68 = var36 - (double)var51 + 2.0D * G4;
      double var70 = var38 - (double)var52 + 2.0D * G4;
      double var72 = var40 - (double)var53 + 2.0D * G4;
      double var74 = var34 - (double)var54 + 3.0D * G4;
      double var76 = var36 - (double)var55 + 3.0D * G4;
      double var78 = var38 - (double)var56 + 3.0D * G4;
      double var80 = var40 - (double)var57 + 3.0D * G4;
      double var82 = var34 - 1.0D + 4.0D * G4;
      double var84 = var36 - 1.0D + 4.0D * G4;
      double var86 = var38 - 1.0D + 4.0D * G4;
      double var88 = var40 - 1.0D + 4.0D * G4;
      int var90 = var20 & 255;
      int var91 = var21 & 255;
      int var92 = var22 & 255;
      int var93 = var23 & 255;
      int var94 = perm[var90 + perm[var91 + perm[var92 + perm[var93]]]] % 32;
      int var95 = perm[var90 + var46 + perm[var91 + var47 + perm[var92 + var48 + perm[var93 + var49]]]] % 32;
      int var96 = perm[var90 + var50 + perm[var91 + var51 + perm[var92 + var52 + perm[var93 + var53]]]] % 32;
      int var97 = perm[var90 + var54 + perm[var91 + var55 + perm[var92 + var56 + perm[var93 + var57]]]] % 32;
      int var98 = perm[var90 + 1 + perm[var91 + 1 + perm[var92 + 1 + perm[var93 + 1]]]] % 32;
      double var99 = 0.6D - var34 * var34 - var36 * var36 - var38 * var38 - var40 * var40;
      double var8;
      if (var99 < 0.0D) {
         var8 = 0.0D;
      } else {
         var99 *= var99;
         var8 = var99 * var99 * dot(grad4[var94], var34, var36, var38, var40);
      }

      double var101 = 0.6D - var58 * var58 - var60 * var60 - var62 * var62 - var64 * var64;
      double var10;
      if (var101 < 0.0D) {
         var10 = 0.0D;
      } else {
         var101 *= var101;
         var10 = var101 * var101 * dot(grad4[var95], var58, var60, var62, var64);
      }

      double var103 = 0.6D - var66 * var66 - var68 * var68 - var70 * var70 - var72 * var72;
      double var12;
      if (var103 < 0.0D) {
         var12 = 0.0D;
      } else {
         var103 *= var103;
         var12 = var103 * var103 * dot(grad4[var96], var66, var68, var70, var72);
      }

      double var105 = 0.6D - var74 * var74 - var76 * var76 - var78 * var78 - var80 * var80;
      double var14;
      if (var105 < 0.0D) {
         var14 = 0.0D;
      } else {
         var105 *= var105;
         var14 = var105 * var105 * dot(grad4[var97], var74, var76, var78, var80);
      }

      double var107 = 0.6D - var82 * var82 - var84 * var84 - var86 * var86 - var88 * var88;
      double var16;
      if (var107 < 0.0D) {
         var16 = 0.0D;
      } else {
         var107 *= var107;
         var16 = var107 * var107 * dot(grad4[var98], var82, var84, var86, var88);
      }

      return 27.0D * (var8 + var10 + var12 + var14 + var16);
   }

   static {
      for(int var0 = 0; var0 < 512; ++var0) {
         perm[var0] = p[var0 & 255];
         permMod12[var0] = (short)(perm[var0] % 12);
      }

      F2 = 0.5D * (Math.sqrt(3.0D) - 1.0D);
      G2 = (3.0D - Math.sqrt(3.0D)) / 6.0D;
      F4 = (Math.sqrt(5.0D) - 1.0D) / 4.0D;
      G4 = (5.0D - Math.sqrt(5.0D)) / 20.0D;
   }

   private static class Grad {
      double x;
      double y;
      double z;
      double w;

      Grad(double var1, double var3, double var5) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
      }

      Grad(double var1, double var3, double var5, double var7) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.w = var7;
      }
   }
}
