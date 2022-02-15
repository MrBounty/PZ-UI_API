package zombie.creative.creativerects;

public class OpenSimplexNoise {
   private static final double STRETCH_CONSTANT_2D = -0.211324865405187D;
   private static final double SQUISH_CONSTANT_2D = 0.366025403784439D;
   private static final double STRETCH_CONSTANT_3D = -0.16666666666666666D;
   private static final double SQUISH_CONSTANT_3D = 0.3333333333333333D;
   private static final double STRETCH_CONSTANT_4D = -0.138196601125011D;
   private static final double SQUISH_CONSTANT_4D = 0.309016994374947D;
   private static final double NORM_CONSTANT_2D = 47.0D;
   private static final double NORM_CONSTANT_3D = 103.0D;
   private static final double NORM_CONSTANT_4D = 30.0D;
   private static final long DEFAULT_SEED = 0L;
   private short[] perm;
   private short[] permGradIndex3D;
   private static byte[] gradients2D = new byte[]{5, 2, 2, 5, -5, 2, -2, 5, 5, -2, 2, -5, -5, -2, -2, -5};
   private static byte[] gradients3D = new byte[]{-11, 4, 4, -4, 11, 4, -4, 4, 11, 11, 4, 4, 4, 11, 4, 4, 4, 11, -11, -4, 4, -4, -11, 4, -4, -4, 11, 11, -4, 4, 4, -11, 4, 4, -4, 11, -11, 4, -4, -4, 11, -4, -4, 4, -11, 11, 4, -4, 4, 11, -4, 4, 4, -11, -11, -4, -4, -4, -11, -4, -4, -4, -11, 11, -4, -4, 4, -11, -4, 4, -4, -11};
   private static byte[] gradients4D = new byte[]{3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, -3, 1, 1, 1, -1, 3, 1, 1, -1, 1, 3, 1, -1, 1, 1, 3, 3, -1, 1, 1, 1, -3, 1, 1, 1, -1, 3, 1, 1, -1, 1, 3, -3, -1, 1, 1, -1, -3, 1, 1, -1, -1, 3, 1, -1, -1, 1, 3, 3, 1, -1, 1, 1, 3, -1, 1, 1, 1, -3, 1, 1, 1, -1, 3, -3, 1, -1, 1, -1, 3, -1, 1, -1, 1, -3, 1, -1, 1, -1, 3, 3, -1, -1, 1, 1, -3, -1, 1, 1, -1, -3, 1, 1, -1, -1, 3, -3, -1, -1, 1, -1, -3, -1, 1, -1, -1, -3, 1, -1, -1, -1, 3, 3, 1, 1, -1, 1, 3, 1, -1, 1, 1, 3, -1, 1, 1, 1, -3, -3, 1, 1, -1, -1, 3, 1, -1, -1, 1, 3, -1, -1, 1, 1, -3, 3, -1, 1, -1, 1, -3, 1, -1, 1, -1, 3, -1, 1, -1, 1, -3, -3, -1, 1, -1, -1, -3, 1, -1, -1, -1, 3, -1, -1, -1, 1, -3, 3, 1, -1, -1, 1, 3, -1, -1, 1, 1, -3, -1, 1, 1, -1, -3, -3, 1, -1, -1, -1, 3, -1, -1, -1, 1, -3, -1, -1, 1, -1, -3, 3, -1, -1, -1, 1, -3, -1, -1, 1, -1, -3, -1, 1, -1, -1, -3, -3, -1, -1, -1, -1, -3, -1, -1, -1, -1, -3, -1, -1, -1, -1, -3};

   public OpenSimplexNoise() {
      this(0L);
   }

   public OpenSimplexNoise(short[] var1) {
      this.perm = var1;
      this.permGradIndex3D = new short[256];

      for(int var2 = 0; var2 < 256; ++var2) {
         this.permGradIndex3D[var2] = (short)(var1[var2] % (gradients3D.length / 3) * 3);
      }

   }

   public OpenSimplexNoise(long var1) {
      this.perm = new short[256];
      this.permGradIndex3D = new short[256];
      short[] var3 = new short[256];

      for(short var4 = 0; var4 < 256; var3[var4] = var4++) {
      }

      var1 = var1 * 6364136223846793005L + 1442695040888963407L;
      var1 = var1 * 6364136223846793005L + 1442695040888963407L;
      var1 = var1 * 6364136223846793005L + 1442695040888963407L;

      for(int var6 = 255; var6 >= 0; --var6) {
         var1 = var1 * 6364136223846793005L + 1442695040888963407L;
         int var5 = (int)((var1 + 31L) % (long)(var6 + 1));
         if (var5 < 0) {
            var5 += var6 + 1;
         }

         this.perm[var6] = var3[var5];
         this.permGradIndex3D[var6] = (short)(this.perm[var6] % (gradients3D.length / 3) * 3);
         var3[var5] = var3[var6];
      }

   }

   public double eval(double var1, double var3) {
      double var5 = (var1 + var3) * -0.211324865405187D;
      double var7 = var1 + var5;
      double var9 = var3 + var5;
      int var11 = fastFloor(var7);
      int var12 = fastFloor(var9);
      double var13 = (double)(var11 + var12) * 0.366025403784439D;
      double var15 = (double)var11 + var13;
      double var17 = (double)var12 + var13;
      double var19 = var7 - (double)var11;
      double var21 = var9 - (double)var12;
      double var23 = var19 + var21;
      double var25 = var1 - var15;
      double var27 = var3 - var17;
      double var35 = 0.0D;
      double var37 = var25 - 1.0D - 0.366025403784439D;
      double var39 = var27 - 0.0D - 0.366025403784439D;
      double var41 = 2.0D - var37 * var37 - var39 * var39;
      if (var41 > 0.0D) {
         var41 *= var41;
         var35 += var41 * var41 * this.extrapolate(var11 + 1, var12 + 0, var37, var39);
      }

      double var43 = var25 - 0.0D - 0.366025403784439D;
      double var45 = var27 - 1.0D - 0.366025403784439D;
      double var47 = 2.0D - var43 * var43 - var45 * var45;
      if (var47 > 0.0D) {
         var47 *= var47;
         var35 += var47 * var47 * this.extrapolate(var11 + 0, var12 + 1, var43, var45);
      }

      double var29;
      double var31;
      int var33;
      int var34;
      double var49;
      if (var23 <= 1.0D) {
         var49 = 1.0D - var23;
         if (!(var49 > var19) && !(var49 > var21)) {
            var33 = var11 + 1;
            var34 = var12 + 1;
            var29 = var25 - 1.0D - 0.732050807568878D;
            var31 = var27 - 1.0D - 0.732050807568878D;
         } else if (var19 > var21) {
            var33 = var11 + 1;
            var34 = var12 - 1;
            var29 = var25 - 1.0D;
            var31 = var27 + 1.0D;
         } else {
            var33 = var11 - 1;
            var34 = var12 + 1;
            var29 = var25 + 1.0D;
            var31 = var27 - 1.0D;
         }
      } else {
         var49 = 2.0D - var23;
         if (!(var49 < var19) && !(var49 < var21)) {
            var29 = var25;
            var31 = var27;
            var33 = var11;
            var34 = var12;
         } else if (var19 > var21) {
            var33 = var11 + 2;
            var34 = var12 + 0;
            var29 = var25 - 2.0D - 0.732050807568878D;
            var31 = var27 + 0.0D - 0.732050807568878D;
         } else {
            var33 = var11 + 0;
            var34 = var12 + 2;
            var29 = var25 + 0.0D - 0.732050807568878D;
            var31 = var27 - 2.0D - 0.732050807568878D;
         }

         ++var11;
         ++var12;
         var25 = var25 - 1.0D - 0.732050807568878D;
         var27 = var27 - 1.0D - 0.732050807568878D;
      }

      var49 = 2.0D - var25 * var25 - var27 * var27;
      if (var49 > 0.0D) {
         var49 *= var49;
         var35 += var49 * var49 * this.extrapolate(var11, var12, var25, var27);
      }

      double var51 = 2.0D - var29 * var29 - var31 * var31;
      if (var51 > 0.0D) {
         var51 *= var51;
         var35 += var51 * var51 * this.extrapolate(var33, var34, var29, var31);
      }

      return var35 / 47.0D;
   }

   public double eval(double var1, double var3, double var5) {
      double var7 = (var1 + var3 + var5) * -0.16666666666666666D;
      double var9 = var1 + var7;
      double var11 = var3 + var7;
      double var13 = var5 + var7;
      int var15 = fastFloor(var9);
      int var16 = fastFloor(var11);
      int var17 = fastFloor(var13);
      double var18 = (double)(var15 + var16 + var17) * 0.3333333333333333D;
      double var20 = (double)var15 + var18;
      double var22 = (double)var16 + var18;
      double var24 = (double)var17 + var18;
      double var26 = var9 - (double)var15;
      double var28 = var11 - (double)var16;
      double var30 = var13 - (double)var17;
      double var32 = var26 + var28 + var30;
      double var34 = var1 - var20;
      double var36 = var3 - var22;
      double var38 = var5 - var24;
      double var58 = 0.0D;
      double var40;
      double var42;
      double var44;
      double var46;
      double var48;
      double var50;
      int var52;
      int var53;
      int var54;
      int var55;
      int var56;
      int var57;
      byte var60;
      double var61;
      byte var63;
      double var64;
      double var66;
      byte var68;
      double var70;
      double var72;
      double var74;
      double var76;
      double var78;
      double var80;
      double var84;
      double var90;
      double var92;
      double var122;
      byte var126;
      double var127;
      if (var32 <= 1.0D) {
         var60 = 1;
         var61 = var26;
         var63 = 2;
         var64 = var28;
         if (var26 >= var28 && var30 > var28) {
            var64 = var30;
            var63 = 4;
         } else if (var26 < var28 && var30 > var26) {
            var61 = var30;
            var60 = 4;
         }

         var66 = 1.0D - var32;
         if (!(var66 > var61) && !(var66 > var64)) {
            var126 = (byte)(var60 | var63);
            if ((var126 & 1) == 0) {
               var52 = var15;
               var55 = var15 - 1;
               var40 = var34 - 0.6666666666666666D;
               var46 = var34 + 1.0D - 0.3333333333333333D;
            } else {
               var52 = var55 = var15 + 1;
               var40 = var34 - 1.0D - 0.6666666666666666D;
               var46 = var34 - 1.0D - 0.3333333333333333D;
            }

            if ((var126 & 2) == 0) {
               var53 = var16;
               var56 = var16 - 1;
               var42 = var36 - 0.6666666666666666D;
               var48 = var36 + 1.0D - 0.3333333333333333D;
            } else {
               var53 = var56 = var16 + 1;
               var42 = var36 - 1.0D - 0.6666666666666666D;
               var48 = var36 - 1.0D - 0.3333333333333333D;
            }

            if ((var126 & 4) == 0) {
               var54 = var17;
               var57 = var17 - 1;
               var44 = var38 - 0.6666666666666666D;
               var50 = var38 + 1.0D - 0.3333333333333333D;
            } else {
               var54 = var57 = var17 + 1;
               var44 = var38 - 1.0D - 0.6666666666666666D;
               var50 = var38 - 1.0D - 0.3333333333333333D;
            }
         } else {
            var68 = var64 > var61 ? var63 : var60;
            if ((var68 & 1) == 0) {
               var52 = var15 - 1;
               var55 = var15;
               var40 = var34 + 1.0D;
               var46 = var34;
            } else {
               var52 = var55 = var15 + 1;
               var40 = var46 = var34 - 1.0D;
            }

            if ((var68 & 2) == 0) {
               var56 = var16;
               var53 = var16;
               var48 = var36;
               var42 = var36;
               if ((var68 & 1) == 0) {
                  var56 = var16 - 1;
                  var48 = var36 + 1.0D;
               } else {
                  var53 = var16 - 1;
                  var42 = var36 + 1.0D;
               }
            } else {
               var53 = var56 = var16 + 1;
               var42 = var48 = var36 - 1.0D;
            }

            if ((var68 & 4) == 0) {
               var54 = var17;
               var57 = var17 - 1;
               var44 = var38;
               var50 = var38 + 1.0D;
            } else {
               var54 = var57 = var17 + 1;
               var44 = var50 = var38 - 1.0D;
            }
         }

         var127 = 2.0D - var34 * var34 - var36 * var36 - var38 * var38;
         if (var127 > 0.0D) {
            var127 *= var127;
            var58 += var127 * var127 * this.extrapolate(var15 + 0, var16 + 0, var17 + 0, var34, var36, var38);
         }

         var70 = var34 - 1.0D - 0.3333333333333333D;
         var72 = var36 - 0.0D - 0.3333333333333333D;
         var74 = var38 - 0.0D - 0.3333333333333333D;
         var76 = 2.0D - var70 * var70 - var72 * var72 - var74 * var74;
         if (var76 > 0.0D) {
            var76 *= var76;
            var58 += var76 * var76 * this.extrapolate(var15 + 1, var16 + 0, var17 + 0, var70, var72, var74);
         }

         var78 = var34 - 0.0D - 0.3333333333333333D;
         var80 = var36 - 1.0D - 0.3333333333333333D;
         var84 = 2.0D - var78 * var78 - var80 * var80 - var74 * var74;
         if (var84 > 0.0D) {
            var84 *= var84;
            var58 += var84 * var84 * this.extrapolate(var15 + 0, var16 + 1, var17 + 0, var78, var80, var74);
         }

         var90 = var38 - 1.0D - 0.3333333333333333D;
         var92 = 2.0D - var78 * var78 - var72 * var72 - var90 * var90;
         if (var92 > 0.0D) {
            var92 *= var92;
            var58 += var92 * var92 * this.extrapolate(var15 + 0, var16 + 0, var17 + 1, var78, var72, var90);
         }
      } else {
         double var82;
         if (var32 >= 2.0D) {
            var60 = 6;
            var61 = var26;
            var63 = 5;
            var64 = var28;
            if (var26 <= var28 && var30 < var28) {
               var64 = var30;
               var63 = 3;
            } else if (var26 > var28 && var30 < var26) {
               var61 = var30;
               var60 = 3;
            }

            var66 = 3.0D - var32;
            if (!(var66 < var61) && !(var66 < var64)) {
               var126 = (byte)(var60 & var63);
               if ((var126 & 1) != 0) {
                  var52 = var15 + 1;
                  var55 = var15 + 2;
                  var40 = var34 - 1.0D - 0.3333333333333333D;
                  var46 = var34 - 2.0D - 0.6666666666666666D;
               } else {
                  var55 = var15;
                  var52 = var15;
                  var40 = var34 - 0.3333333333333333D;
                  var46 = var34 - 0.6666666666666666D;
               }

               if ((var126 & 2) != 0) {
                  var53 = var16 + 1;
                  var56 = var16 + 2;
                  var42 = var36 - 1.0D - 0.3333333333333333D;
                  var48 = var36 - 2.0D - 0.6666666666666666D;
               } else {
                  var56 = var16;
                  var53 = var16;
                  var42 = var36 - 0.3333333333333333D;
                  var48 = var36 - 0.6666666666666666D;
               }

               if ((var126 & 4) != 0) {
                  var54 = var17 + 1;
                  var57 = var17 + 2;
                  var44 = var38 - 1.0D - 0.3333333333333333D;
                  var50 = var38 - 2.0D - 0.6666666666666666D;
               } else {
                  var57 = var17;
                  var54 = var17;
                  var44 = var38 - 0.3333333333333333D;
                  var50 = var38 - 0.6666666666666666D;
               }
            } else {
               var68 = var64 < var61 ? var63 : var60;
               if ((var68 & 1) != 0) {
                  var52 = var15 + 2;
                  var55 = var15 + 1;
                  var40 = var34 - 2.0D - 1.0D;
                  var46 = var34 - 1.0D - 1.0D;
               } else {
                  var55 = var15;
                  var52 = var15;
                  var40 = var46 = var34 - 1.0D;
               }

               if ((var68 & 2) != 0) {
                  var53 = var56 = var16 + 1;
                  var42 = var48 = var36 - 1.0D - 1.0D;
                  if ((var68 & 1) != 0) {
                     ++var56;
                     --var48;
                  } else {
                     ++var53;
                     --var42;
                  }
               } else {
                  var56 = var16;
                  var53 = var16;
                  var42 = var48 = var36 - 1.0D;
               }

               if ((var68 & 4) != 0) {
                  var54 = var17 + 1;
                  var57 = var17 + 2;
                  var44 = var38 - 1.0D - 1.0D;
                  var50 = var38 - 2.0D - 1.0D;
               } else {
                  var57 = var17;
                  var54 = var17;
                  var44 = var50 = var38 - 1.0D;
               }
            }

            var127 = var34 - 1.0D - 0.6666666666666666D;
            var70 = var36 - 1.0D - 0.6666666666666666D;
            var72 = var38 - 0.0D - 0.6666666666666666D;
            var74 = 2.0D - var127 * var127 - var70 * var70 - var72 * var72;
            if (var74 > 0.0D) {
               var74 *= var74;
               var58 += var74 * var74 * this.extrapolate(var15 + 1, var16 + 1, var17 + 0, var127, var70, var72);
            }

            var78 = var36 - 0.0D - 0.6666666666666666D;
            var80 = var38 - 1.0D - 0.6666666666666666D;
            var82 = 2.0D - var127 * var127 - var78 * var78 - var80 * var80;
            if (var82 > 0.0D) {
               var82 *= var82;
               var58 += var82 * var82 * this.extrapolate(var15 + 1, var16 + 0, var17 + 1, var127, var78, var80);
            }

            var84 = var34 - 0.0D - 0.6666666666666666D;
            var90 = 2.0D - var84 * var84 - var70 * var70 - var80 * var80;
            if (var90 > 0.0D) {
               var90 *= var90;
               var58 += var90 * var90 * this.extrapolate(var15 + 0, var16 + 1, var17 + 1, var84, var70, var80);
            }

            var34 = var34 - 1.0D - 1.0D;
            var36 = var36 - 1.0D - 1.0D;
            var38 = var38 - 1.0D - 1.0D;
            var92 = 2.0D - var34 * var34 - var36 * var36 - var38 * var38;
            if (var92 > 0.0D) {
               var92 *= var92;
               var58 += var92 * var92 * this.extrapolate(var15 + 1, var16 + 1, var17 + 1, var34, var36, var38);
            }
         } else {
            var127 = var26 + var28;
            byte var62;
            boolean var124;
            if (var127 > 1.0D) {
               var122 = var127 - 1.0D;
               var62 = 3;
               var124 = true;
            } else {
               var122 = 1.0D - var127;
               var62 = 4;
               var124 = false;
            }

            var70 = var26 + var30;
            boolean var67;
            byte var125;
            if (var70 > 1.0D) {
               var64 = var70 - 1.0D;
               var125 = 5;
               var67 = true;
            } else {
               var64 = 1.0D - var70;
               var125 = 2;
               var67 = false;
            }

            var72 = var28 + var30;
            if (var72 > 1.0D) {
               var74 = var72 - 1.0D;
               if (var122 <= var64 && var122 < var74) {
                  var62 = 6;
                  var124 = true;
               } else if (var122 > var64 && var64 < var74) {
                  var125 = 6;
                  var67 = true;
               }
            } else {
               var74 = 1.0D - var72;
               if (var122 <= var64 && var122 < var74) {
                  var62 = 1;
                  var124 = false;
               } else if (var122 > var64 && var64 < var74) {
                  var125 = 1;
                  var67 = false;
               }
            }

            if (var124 == var67) {
               byte var128;
               if (var124) {
                  var40 = var34 - 1.0D - 1.0D;
                  var42 = var36 - 1.0D - 1.0D;
                  var44 = var38 - 1.0D - 1.0D;
                  var52 = var15 + 1;
                  var53 = var16 + 1;
                  var54 = var17 + 1;
                  var128 = (byte)(var62 & var125);
                  if ((var128 & 1) != 0) {
                     var46 = var34 - 2.0D - 0.6666666666666666D;
                     var48 = var36 - 0.6666666666666666D;
                     var50 = var38 - 0.6666666666666666D;
                     var55 = var15 + 2;
                     var56 = var16;
                     var57 = var17;
                  } else if ((var128 & 2) != 0) {
                     var46 = var34 - 0.6666666666666666D;
                     var48 = var36 - 2.0D - 0.6666666666666666D;
                     var50 = var38 - 0.6666666666666666D;
                     var55 = var15;
                     var56 = var16 + 2;
                     var57 = var17;
                  } else {
                     var46 = var34 - 0.6666666666666666D;
                     var48 = var36 - 0.6666666666666666D;
                     var50 = var38 - 2.0D - 0.6666666666666666D;
                     var55 = var15;
                     var56 = var16;
                     var57 = var17 + 2;
                  }
               } else {
                  var40 = var34;
                  var42 = var36;
                  var44 = var38;
                  var52 = var15;
                  var53 = var16;
                  var54 = var17;
                  var128 = (byte)(var62 | var125);
                  if ((var128 & 1) == 0) {
                     var46 = var34 + 1.0D - 0.3333333333333333D;
                     var48 = var36 - 1.0D - 0.3333333333333333D;
                     var50 = var38 - 1.0D - 0.3333333333333333D;
                     var55 = var15 - 1;
                     var56 = var16 + 1;
                     var57 = var17 + 1;
                  } else if ((var128 & 2) == 0) {
                     var46 = var34 - 1.0D - 0.3333333333333333D;
                     var48 = var36 + 1.0D - 0.3333333333333333D;
                     var50 = var38 - 1.0D - 0.3333333333333333D;
                     var55 = var15 + 1;
                     var56 = var16 - 1;
                     var57 = var17 + 1;
                  } else {
                     var46 = var34 - 1.0D - 0.3333333333333333D;
                     var48 = var36 - 1.0D - 0.3333333333333333D;
                     var50 = var38 + 1.0D - 0.3333333333333333D;
                     var55 = var15 + 1;
                     var56 = var16 + 1;
                     var57 = var17 - 1;
                  }
               }
            } else {
               byte var129;
               byte var75;
               if (var124) {
                  var129 = var62;
                  var75 = var125;
               } else {
                  var129 = var125;
                  var75 = var62;
               }

               if ((var129 & 1) == 0) {
                  var40 = var34 + 1.0D - 0.3333333333333333D;
                  var42 = var36 - 1.0D - 0.3333333333333333D;
                  var44 = var38 - 1.0D - 0.3333333333333333D;
                  var52 = var15 - 1;
                  var53 = var16 + 1;
                  var54 = var17 + 1;
               } else if ((var129 & 2) == 0) {
                  var40 = var34 - 1.0D - 0.3333333333333333D;
                  var42 = var36 + 1.0D - 0.3333333333333333D;
                  var44 = var38 - 1.0D - 0.3333333333333333D;
                  var52 = var15 + 1;
                  var53 = var16 - 1;
                  var54 = var17 + 1;
               } else {
                  var40 = var34 - 1.0D - 0.3333333333333333D;
                  var42 = var36 - 1.0D - 0.3333333333333333D;
                  var44 = var38 + 1.0D - 0.3333333333333333D;
                  var52 = var15 + 1;
                  var53 = var16 + 1;
                  var54 = var17 - 1;
               }

               var46 = var34 - 0.6666666666666666D;
               var48 = var36 - 0.6666666666666666D;
               var50 = var38 - 0.6666666666666666D;
               var55 = var15;
               var56 = var16;
               var57 = var17;
               if ((var75 & 1) != 0) {
                  var46 -= 2.0D;
                  var55 = var15 + 2;
               } else if ((var75 & 2) != 0) {
                  var48 -= 2.0D;
                  var56 = var16 + 2;
               } else {
                  var50 -= 2.0D;
                  var57 = var17 + 2;
               }
            }

            var74 = var34 - 1.0D - 0.3333333333333333D;
            var76 = var36 - 0.0D - 0.3333333333333333D;
            var78 = var38 - 0.0D - 0.3333333333333333D;
            var80 = 2.0D - var74 * var74 - var76 * var76 - var78 * var78;
            if (var80 > 0.0D) {
               var80 *= var80;
               var58 += var80 * var80 * this.extrapolate(var15 + 1, var16 + 0, var17 + 0, var74, var76, var78);
            }

            var82 = var34 - 0.0D - 0.3333333333333333D;
            var84 = var36 - 1.0D - 0.3333333333333333D;
            double var88 = 2.0D - var82 * var82 - var84 * var84 - var78 * var78;
            if (var88 > 0.0D) {
               var88 *= var88;
               var58 += var88 * var88 * this.extrapolate(var15 + 0, var16 + 1, var17 + 0, var82, var84, var78);
            }

            double var94 = var38 - 1.0D - 0.3333333333333333D;
            double var96 = 2.0D - var82 * var82 - var76 * var76 - var94 * var94;
            if (var96 > 0.0D) {
               var96 *= var96;
               var58 += var96 * var96 * this.extrapolate(var15 + 0, var16 + 0, var17 + 1, var82, var76, var94);
            }

            double var98 = var34 - 1.0D - 0.6666666666666666D;
            double var100 = var36 - 1.0D - 0.6666666666666666D;
            double var102 = var38 - 0.0D - 0.6666666666666666D;
            double var104 = 2.0D - var98 * var98 - var100 * var100 - var102 * var102;
            if (var104 > 0.0D) {
               var104 *= var104;
               var58 += var104 * var104 * this.extrapolate(var15 + 1, var16 + 1, var17 + 0, var98, var100, var102);
            }

            double var108 = var36 - 0.0D - 0.6666666666666666D;
            double var110 = var38 - 1.0D - 0.6666666666666666D;
            double var112 = 2.0D - var98 * var98 - var108 * var108 - var110 * var110;
            if (var112 > 0.0D) {
               var112 *= var112;
               var58 += var112 * var112 * this.extrapolate(var15 + 1, var16 + 0, var17 + 1, var98, var108, var110);
            }

            double var114 = var34 - 0.0D - 0.6666666666666666D;
            double var120 = 2.0D - var114 * var114 - var100 * var100 - var110 * var110;
            if (var120 > 0.0D) {
               var120 *= var120;
               var58 += var120 * var120 * this.extrapolate(var15 + 0, var16 + 1, var17 + 1, var114, var100, var110);
            }
         }
      }

      var122 = 2.0D - var40 * var40 - var42 * var42 - var44 * var44;
      if (var122 > 0.0D) {
         var122 *= var122;
         var58 += var122 * var122 * this.extrapolate(var52, var53, var54, var40, var42, var44);
      }

      double var123 = 2.0D - var46 * var46 - var48 * var48 - var50 * var50;
      if (var123 > 0.0D) {
         var123 *= var123;
         var58 += var123 * var123 * this.extrapolate(var55, var56, var57, var46, var48, var50);
      }

      return var58 / 103.0D;
   }

   public double eval(double var1, double var3, double var5, double var7) {
      double var9 = (var1 + var3 + var5 + var7) * -0.138196601125011D;
      double var11 = var1 + var9;
      double var13 = var3 + var9;
      double var15 = var5 + var9;
      double var17 = var7 + var9;
      int var19 = fastFloor(var11);
      int var20 = fastFloor(var13);
      int var21 = fastFloor(var15);
      int var22 = fastFloor(var17);
      double var23 = (double)(var19 + var20 + var21 + var22) * 0.309016994374947D;
      double var25 = (double)var19 + var23;
      double var27 = (double)var20 + var23;
      double var29 = (double)var21 + var23;
      double var31 = (double)var22 + var23;
      double var33 = var11 - (double)var19;
      double var35 = var13 - (double)var20;
      double var37 = var15 - (double)var21;
      double var39 = var17 - (double)var22;
      double var41 = var33 + var35 + var37 + var39;
      double var43 = var1 - var25;
      double var45 = var3 - var27;
      double var47 = var5 - var29;
      double var49 = var7 - var31;
      double var87 = 0.0D;
      double var51;
      double var53;
      double var55;
      double var57;
      double var59;
      double var61;
      double var63;
      double var65;
      double var67;
      double var69;
      double var71;
      double var73;
      int var75;
      int var76;
      int var77;
      int var78;
      int var79;
      int var80;
      int var81;
      int var82;
      int var83;
      int var84;
      int var85;
      int var86;
      byte var89;
      double var90;
      byte var92;
      double var93;
      double var95;
      byte var97;
      double var99;
      double var101;
      double var103;
      double var105;
      double var107;
      double var109;
      double var111;
      double var117;
      double var123;
      double var127;
      double var135;
      double var137;
      double var205;
      byte var208;
      double var209;
      if (var41 <= 1.0D) {
         var89 = 1;
         var90 = var33;
         var92 = 2;
         var93 = var35;
         if (var33 >= var35 && var37 > var35) {
            var93 = var37;
            var92 = 4;
         } else if (var33 < var35 && var37 > var33) {
            var90 = var37;
            var89 = 4;
         }

         if (var90 >= var93 && var39 > var93) {
            var93 = var39;
            var92 = 8;
         } else if (var90 < var93 && var39 > var90) {
            var90 = var39;
            var89 = 8;
         }

         var95 = 1.0D - var41;
         if (!(var95 > var90) && !(var95 > var93)) {
            var208 = (byte)(var89 | var92);
            if ((var208 & 1) == 0) {
               var83 = var19;
               var75 = var19;
               var79 = var19 - 1;
               var51 = var43 - 0.618033988749894D;
               var59 = var43 + 1.0D - 0.309016994374947D;
               var67 = var43 - 0.309016994374947D;
            } else {
               var75 = var79 = var83 = var19 + 1;
               var51 = var43 - 1.0D - 0.618033988749894D;
               var59 = var67 = var43 - 1.0D - 0.309016994374947D;
            }

            if ((var208 & 2) == 0) {
               var84 = var20;
               var80 = var20;
               var76 = var20;
               var53 = var45 - 0.618033988749894D;
               var61 = var69 = var45 - 0.309016994374947D;
               if ((var208 & 1) == 1) {
                  var80 = var20 - 1;
                  ++var61;
               } else {
                  var84 = var20 - 1;
                  ++var69;
               }
            } else {
               var76 = var80 = var84 = var20 + 1;
               var53 = var45 - 1.0D - 0.618033988749894D;
               var61 = var69 = var45 - 1.0D - 0.309016994374947D;
            }

            if ((var208 & 4) == 0) {
               var85 = var21;
               var81 = var21;
               var77 = var21;
               var55 = var47 - 0.618033988749894D;
               var63 = var71 = var47 - 0.309016994374947D;
               if ((var208 & 3) == 3) {
                  var81 = var21 - 1;
                  ++var63;
               } else {
                  var85 = var21 - 1;
                  ++var71;
               }
            } else {
               var77 = var81 = var85 = var21 + 1;
               var55 = var47 - 1.0D - 0.618033988749894D;
               var63 = var71 = var47 - 1.0D - 0.309016994374947D;
            }

            if ((var208 & 8) == 0) {
               var82 = var22;
               var78 = var22;
               var86 = var22 - 1;
               var57 = var49 - 0.618033988749894D;
               var65 = var49 - 0.309016994374947D;
               var73 = var49 + 1.0D - 0.309016994374947D;
            } else {
               var78 = var82 = var86 = var22 + 1;
               var57 = var49 - 1.0D - 0.618033988749894D;
               var65 = var73 = var49 - 1.0D - 0.309016994374947D;
            }
         } else {
            var97 = var93 > var90 ? var92 : var89;
            if ((var97 & 1) == 0) {
               var75 = var19 - 1;
               var83 = var19;
               var79 = var19;
               var51 = var43 + 1.0D;
               var67 = var43;
               var59 = var43;
            } else {
               var75 = var79 = var83 = var19 + 1;
               var51 = var59 = var67 = var43 - 1.0D;
            }

            if ((var97 & 2) == 0) {
               var84 = var20;
               var80 = var20;
               var76 = var20;
               var69 = var45;
               var61 = var45;
               var53 = var45;
               if ((var97 & 1) == 1) {
                  var76 = var20 - 1;
                  var53 = var45 + 1.0D;
               } else {
                  var80 = var20 - 1;
                  var61 = var45 + 1.0D;
               }
            } else {
               var76 = var80 = var84 = var20 + 1;
               var53 = var61 = var69 = var45 - 1.0D;
            }

            if ((var97 & 4) == 0) {
               var85 = var21;
               var81 = var21;
               var77 = var21;
               var71 = var47;
               var63 = var47;
               var55 = var47;
               if ((var97 & 3) != 0) {
                  if ((var97 & 3) == 3) {
                     var77 = var21 - 1;
                     var55 = var47 + 1.0D;
                  } else {
                     var81 = var21 - 1;
                     var63 = var47 + 1.0D;
                  }
               } else {
                  var85 = var21 - 1;
                  var71 = var47 + 1.0D;
               }
            } else {
               var77 = var81 = var85 = var21 + 1;
               var55 = var63 = var71 = var47 - 1.0D;
            }

            if ((var97 & 8) == 0) {
               var82 = var22;
               var78 = var22;
               var86 = var22 - 1;
               var65 = var49;
               var57 = var49;
               var73 = var49 + 1.0D;
            } else {
               var78 = var82 = var86 = var22 + 1;
               var57 = var65 = var73 = var49 - 1.0D;
            }
         }

         var209 = 2.0D - var43 * var43 - var45 * var45 - var47 * var47 - var49 * var49;
         if (var209 > 0.0D) {
            var209 *= var209;
            var87 += var209 * var209 * this.extrapolate(var19 + 0, var20 + 0, var21 + 0, var22 + 0, var43, var45, var47, var49);
         }

         var99 = var43 - 1.0D - 0.309016994374947D;
         var101 = var45 - 0.0D - 0.309016994374947D;
         var103 = var47 - 0.0D - 0.309016994374947D;
         var105 = var49 - 0.0D - 0.309016994374947D;
         var107 = 2.0D - var99 * var99 - var101 * var101 - var103 * var103 - var105 * var105;
         if (var107 > 0.0D) {
            var107 *= var107;
            var87 += var107 * var107 * this.extrapolate(var19 + 1, var20 + 0, var21 + 0, var22 + 0, var99, var101, var103, var105);
         }

         var109 = var43 - 0.0D - 0.309016994374947D;
         var111 = var45 - 1.0D - 0.309016994374947D;
         var117 = 2.0D - var109 * var109 - var111 * var111 - var103 * var103 - var105 * var105;
         if (var117 > 0.0D) {
            var117 *= var117;
            var87 += var117 * var117 * this.extrapolate(var19 + 0, var20 + 1, var21 + 0, var22 + 0, var109, var111, var103, var105);
         }

         var123 = var47 - 1.0D - 0.309016994374947D;
         var127 = 2.0D - var109 * var109 - var101 * var101 - var123 * var123 - var105 * var105;
         if (var127 > 0.0D) {
            var127 *= var127;
            var87 += var127 * var127 * this.extrapolate(var19 + 0, var20 + 0, var21 + 1, var22 + 0, var109, var101, var123, var105);
         }

         var135 = var49 - 1.0D - 0.309016994374947D;
         var137 = 2.0D - var109 * var109 - var101 * var101 - var103 * var103 - var135 * var135;
         if (var137 > 0.0D) {
            var137 *= var137;
            var87 += var137 * var137 * this.extrapolate(var19 + 0, var20 + 0, var21 + 0, var22 + 1, var109, var101, var103, var135);
         }
      } else {
         double var113;
         double var115;
         double var119;
         if (var41 >= 3.0D) {
            var89 = 14;
            var90 = var33;
            var92 = 13;
            var93 = var35;
            if (var33 <= var35 && var37 < var35) {
               var93 = var37;
               var92 = 11;
            } else if (var33 > var35 && var37 < var33) {
               var90 = var37;
               var89 = 11;
            }

            if (var90 <= var93 && var39 < var93) {
               var93 = var39;
               var92 = 7;
            } else if (var90 > var93 && var39 < var90) {
               var90 = var39;
               var89 = 7;
            }

            var95 = 4.0D - var41;
            if (!(var95 < var90) && !(var95 < var93)) {
               var208 = (byte)(var89 & var92);
               if ((var208 & 1) != 0) {
                  var75 = var83 = var19 + 1;
                  var79 = var19 + 2;
                  var51 = var43 - 1.0D - 0.618033988749894D;
                  var59 = var43 - 2.0D - 0.927050983124841D;
                  var67 = var43 - 1.0D - 0.927050983124841D;
               } else {
                  var83 = var19;
                  var79 = var19;
                  var75 = var19;
                  var51 = var43 - 0.618033988749894D;
                  var59 = var67 = var43 - 0.927050983124841D;
               }

               if ((var208 & 2) != 0) {
                  var76 = var80 = var84 = var20 + 1;
                  var53 = var45 - 1.0D - 0.618033988749894D;
                  var61 = var69 = var45 - 1.0D - 0.927050983124841D;
                  if ((var208 & 1) != 0) {
                     ++var84;
                     --var69;
                  } else {
                     ++var80;
                     --var61;
                  }
               } else {
                  var84 = var20;
                  var80 = var20;
                  var76 = var20;
                  var53 = var45 - 0.618033988749894D;
                  var61 = var69 = var45 - 0.927050983124841D;
               }

               if ((var208 & 4) != 0) {
                  var77 = var81 = var85 = var21 + 1;
                  var55 = var47 - 1.0D - 0.618033988749894D;
                  var63 = var71 = var47 - 1.0D - 0.927050983124841D;
                  if ((var208 & 3) != 0) {
                     ++var85;
                     --var71;
                  } else {
                     ++var81;
                     --var63;
                  }
               } else {
                  var85 = var21;
                  var81 = var21;
                  var77 = var21;
                  var55 = var47 - 0.618033988749894D;
                  var63 = var71 = var47 - 0.927050983124841D;
               }

               if ((var208 & 8) != 0) {
                  var78 = var82 = var22 + 1;
                  var86 = var22 + 2;
                  var57 = var49 - 1.0D - 0.618033988749894D;
                  var65 = var49 - 1.0D - 0.927050983124841D;
                  var73 = var49 - 2.0D - 0.927050983124841D;
               } else {
                  var86 = var22;
                  var82 = var22;
                  var78 = var22;
                  var57 = var49 - 0.618033988749894D;
                  var65 = var73 = var49 - 0.927050983124841D;
               }
            } else {
               var97 = var93 < var90 ? var92 : var89;
               if ((var97 & 1) != 0) {
                  var75 = var19 + 2;
                  var79 = var83 = var19 + 1;
                  var51 = var43 - 2.0D - 1.236067977499788D;
                  var59 = var67 = var43 - 1.0D - 1.236067977499788D;
               } else {
                  var83 = var19;
                  var79 = var19;
                  var75 = var19;
                  var51 = var59 = var67 = var43 - 1.236067977499788D;
               }

               if ((var97 & 2) != 0) {
                  var76 = var80 = var84 = var20 + 1;
                  var53 = var61 = var69 = var45 - 1.0D - 1.236067977499788D;
                  if ((var97 & 1) != 0) {
                     ++var80;
                     --var61;
                  } else {
                     ++var76;
                     --var53;
                  }
               } else {
                  var84 = var20;
                  var80 = var20;
                  var76 = var20;
                  var53 = var61 = var69 = var45 - 1.236067977499788D;
               }

               if ((var97 & 4) != 0) {
                  var77 = var81 = var85 = var21 + 1;
                  var55 = var63 = var71 = var47 - 1.0D - 1.236067977499788D;
                  if ((var97 & 3) != 3) {
                     if ((var97 & 3) == 0) {
                        ++var77;
                        --var55;
                     } else {
                        ++var81;
                        --var63;
                     }
                  } else {
                     ++var85;
                     --var71;
                  }
               } else {
                  var85 = var21;
                  var81 = var21;
                  var77 = var21;
                  var55 = var63 = var71 = var47 - 1.236067977499788D;
               }

               if ((var97 & 8) != 0) {
                  var78 = var82 = var22 + 1;
                  var86 = var22 + 2;
                  var57 = var65 = var49 - 1.0D - 1.236067977499788D;
                  var73 = var49 - 2.0D - 1.236067977499788D;
               } else {
                  var86 = var22;
                  var82 = var22;
                  var78 = var22;
                  var57 = var65 = var73 = var49 - 1.236067977499788D;
               }
            }

            var209 = var43 - 1.0D - 0.927050983124841D;
            var99 = var45 - 1.0D - 0.927050983124841D;
            var101 = var47 - 1.0D - 0.927050983124841D;
            var103 = var49 - 0.927050983124841D;
            var105 = 2.0D - var209 * var209 - var99 * var99 - var101 * var101 - var103 * var103;
            if (var105 > 0.0D) {
               var105 *= var105;
               var87 += var105 * var105 * this.extrapolate(var19 + 1, var20 + 1, var21 + 1, var22 + 0, var209, var99, var101, var103);
            }

            var111 = var47 - 0.927050983124841D;
            var113 = var49 - 1.0D - 0.927050983124841D;
            var115 = 2.0D - var209 * var209 - var99 * var99 - var111 * var111 - var113 * var113;
            if (var115 > 0.0D) {
               var115 *= var115;
               var87 += var115 * var115 * this.extrapolate(var19 + 1, var20 + 1, var21 + 0, var22 + 1, var209, var99, var111, var113);
            }

            var119 = var45 - 0.927050983124841D;
            double var125 = 2.0D - var209 * var209 - var119 * var119 - var101 * var101 - var113 * var113;
            if (var125 > 0.0D) {
               var125 *= var125;
               var87 += var125 * var125 * this.extrapolate(var19 + 1, var20 + 0, var21 + 1, var22 + 1, var209, var119, var101, var113);
            }

            var127 = var43 - 0.927050983124841D;
            var135 = 2.0D - var127 * var127 - var99 * var99 - var101 * var101 - var113 * var113;
            if (var135 > 0.0D) {
               var135 *= var135;
               var87 += var135 * var135 * this.extrapolate(var19 + 0, var20 + 1, var21 + 1, var22 + 1, var127, var99, var101, var113);
            }

            var43 = var43 - 1.0D - 1.236067977499788D;
            var45 = var45 - 1.0D - 1.236067977499788D;
            var47 = var47 - 1.0D - 1.236067977499788D;
            var49 = var49 - 1.0D - 1.236067977499788D;
            var137 = 2.0D - var43 * var43 - var45 * var45 - var47 * var47 - var49 * var49;
            if (var137 > 0.0D) {
               var137 *= var137;
               var87 += var137 * var137 * this.extrapolate(var19 + 1, var20 + 1, var21 + 1, var22 + 1, var43, var45, var47, var49);
            }
         } else {
            byte var91;
            boolean var96;
            byte var106;
            double var133;
            double var143;
            double var145;
            double var147;
            double var149;
            double var151;
            double var153;
            double var155;
            double var157;
            double var159;
            double var161;
            double var163;
            double var165;
            double var167;
            double var169;
            double var171;
            double var173;
            double var175;
            double var177;
            double var179;
            double var181;
            double var183;
            double var185;
            double var187;
            double var189;
            double var191;
            double var193;
            double var195;
            double var197;
            double var199;
            double var201;
            double var203;
            boolean var206;
            byte var207;
            byte var211;
            byte var212;
            byte var213;
            if (var41 <= 2.0D) {
               var206 = true;
               var96 = true;
               if (var33 + var35 > var37 + var39) {
                  var205 = var33 + var35;
                  var91 = 3;
               } else {
                  var205 = var37 + var39;
                  var91 = 12;
               }

               if (var33 + var37 > var35 + var39) {
                  var93 = var33 + var37;
                  var207 = 5;
               } else {
                  var93 = var35 + var39;
                  var207 = 10;
               }

               if (var33 + var39 > var35 + var37) {
                  var209 = var33 + var39;
                  if (var205 >= var93 && var209 > var93) {
                     var93 = var209;
                     var207 = 9;
                  } else if (var205 < var93 && var209 > var205) {
                     var205 = var209;
                     var91 = 9;
                  }
               } else {
                  var209 = var35 + var37;
                  if (var205 >= var93 && var209 > var93) {
                     var93 = var209;
                     var207 = 6;
                  } else if (var205 < var93 && var209 > var205) {
                     var205 = var209;
                     var91 = 6;
                  }
               }

               var209 = 2.0D - var41 + var33;
               if (var205 >= var93 && var209 > var93) {
                  var93 = var209;
                  var207 = 1;
                  var96 = false;
               } else if (var205 < var93 && var209 > var205) {
                  var205 = var209;
                  var91 = 1;
                  var206 = false;
               }

               var99 = 2.0D - var41 + var35;
               if (var205 >= var93 && var99 > var93) {
                  var93 = var99;
                  var207 = 2;
                  var96 = false;
               } else if (var205 < var93 && var99 > var205) {
                  var205 = var99;
                  var91 = 2;
                  var206 = false;
               }

               var101 = 2.0D - var41 + var37;
               if (var205 >= var93 && var101 > var93) {
                  var93 = var101;
                  var207 = 4;
                  var96 = false;
               } else if (var205 < var93 && var101 > var205) {
                  var205 = var101;
                  var91 = 4;
                  var206 = false;
               }

               var103 = 2.0D - var41 + var39;
               if (var205 >= var93 && var103 > var93) {
                  var207 = 8;
                  var96 = false;
               } else if (var205 < var93 && var103 > var205) {
                  var91 = 8;
                  var206 = false;
               }

               if (var206 == var96) {
                  if (var206) {
                     var211 = (byte)(var91 | var207);
                     var106 = (byte)(var91 & var207);
                     if ((var211 & 1) == 0) {
                        var75 = var19;
                        var79 = var19 - 1;
                        var51 = var43 - 0.927050983124841D;
                        var59 = var43 + 1.0D - 0.618033988749894D;
                     } else {
                        var75 = var79 = var19 + 1;
                        var51 = var43 - 1.0D - 0.927050983124841D;
                        var59 = var43 - 1.0D - 0.618033988749894D;
                     }

                     if ((var211 & 2) == 0) {
                        var76 = var20;
                        var80 = var20 - 1;
                        var53 = var45 - 0.927050983124841D;
                        var61 = var45 + 1.0D - 0.618033988749894D;
                     } else {
                        var76 = var80 = var20 + 1;
                        var53 = var45 - 1.0D - 0.927050983124841D;
                        var61 = var45 - 1.0D - 0.618033988749894D;
                     }

                     if ((var211 & 4) == 0) {
                        var77 = var21;
                        var81 = var21 - 1;
                        var55 = var47 - 0.927050983124841D;
                        var63 = var47 + 1.0D - 0.618033988749894D;
                     } else {
                        var77 = var81 = var21 + 1;
                        var55 = var47 - 1.0D - 0.927050983124841D;
                        var63 = var47 - 1.0D - 0.618033988749894D;
                     }

                     if ((var211 & 8) == 0) {
                        var78 = var22;
                        var82 = var22 - 1;
                        var57 = var49 - 0.927050983124841D;
                        var65 = var49 + 1.0D - 0.618033988749894D;
                     } else {
                        var78 = var82 = var22 + 1;
                        var57 = var49 - 1.0D - 0.927050983124841D;
                        var65 = var49 - 1.0D - 0.618033988749894D;
                     }

                     var83 = var19;
                     var84 = var20;
                     var85 = var21;
                     var86 = var22;
                     var67 = var43 - 0.618033988749894D;
                     var69 = var45 - 0.618033988749894D;
                     var71 = var47 - 0.618033988749894D;
                     var73 = var49 - 0.618033988749894D;
                     if ((var106 & 1) != 0) {
                        var83 = var19 + 2;
                        var67 -= 2.0D;
                     } else if ((var106 & 2) != 0) {
                        var84 = var20 + 2;
                        var69 -= 2.0D;
                     } else if ((var106 & 4) != 0) {
                        var85 = var21 + 2;
                        var71 -= 2.0D;
                     } else {
                        var86 = var22 + 2;
                        var73 -= 2.0D;
                     }
                  } else {
                     var83 = var19;
                     var84 = var20;
                     var85 = var21;
                     var86 = var22;
                     var67 = var43;
                     var69 = var45;
                     var71 = var47;
                     var73 = var49;
                     var211 = (byte)(var91 | var207);
                     if ((var211 & 1) == 0) {
                        var75 = var19 - 1;
                        var79 = var19;
                        var51 = var43 + 1.0D - 0.309016994374947D;
                        var59 = var43 - 0.309016994374947D;
                     } else {
                        var75 = var79 = var19 + 1;
                        var51 = var59 = var43 - 1.0D - 0.309016994374947D;
                     }

                     if ((var211 & 2) == 0) {
                        var80 = var20;
                        var76 = var20;
                        var53 = var61 = var45 - 0.309016994374947D;
                        if ((var211 & 1) == 1) {
                           var76 = var20 - 1;
                           ++var53;
                        } else {
                           var80 = var20 - 1;
                           ++var61;
                        }
                     } else {
                        var76 = var80 = var20 + 1;
                        var53 = var61 = var45 - 1.0D - 0.309016994374947D;
                     }

                     if ((var211 & 4) == 0) {
                        var81 = var21;
                        var77 = var21;
                        var55 = var63 = var47 - 0.309016994374947D;
                        if ((var211 & 3) == 3) {
                           var77 = var21 - 1;
                           ++var55;
                        } else {
                           var81 = var21 - 1;
                           ++var63;
                        }
                     } else {
                        var77 = var81 = var21 + 1;
                        var55 = var63 = var47 - 1.0D - 0.309016994374947D;
                     }

                     if ((var211 & 8) == 0) {
                        var78 = var22;
                        var82 = var22 - 1;
                        var57 = var49 - 0.309016994374947D;
                        var65 = var49 + 1.0D - 0.309016994374947D;
                     } else {
                        var78 = var82 = var22 + 1;
                        var57 = var65 = var49 - 1.0D - 0.309016994374947D;
                     }
                  }
               } else {
                  if (var206) {
                     var213 = var91;
                     var212 = var207;
                  } else {
                     var213 = var207;
                     var212 = var91;
                  }

                  if ((var213 & 1) == 0) {
                     var75 = var19 - 1;
                     var79 = var19;
                     var51 = var43 + 1.0D - 0.309016994374947D;
                     var59 = var43 - 0.309016994374947D;
                  } else {
                     var75 = var79 = var19 + 1;
                     var51 = var59 = var43 - 1.0D - 0.309016994374947D;
                  }

                  if ((var213 & 2) == 0) {
                     var80 = var20;
                     var76 = var20;
                     var53 = var61 = var45 - 0.309016994374947D;
                     if ((var213 & 1) == 1) {
                        var76 = var20 - 1;
                        ++var53;
                     } else {
                        var80 = var20 - 1;
                        ++var61;
                     }
                  } else {
                     var76 = var80 = var20 + 1;
                     var53 = var61 = var45 - 1.0D - 0.309016994374947D;
                  }

                  if ((var213 & 4) == 0) {
                     var81 = var21;
                     var77 = var21;
                     var55 = var63 = var47 - 0.309016994374947D;
                     if ((var213 & 3) == 3) {
                        var77 = var21 - 1;
                        ++var55;
                     } else {
                        var81 = var21 - 1;
                        ++var63;
                     }
                  } else {
                     var77 = var81 = var21 + 1;
                     var55 = var63 = var47 - 1.0D - 0.309016994374947D;
                  }

                  if ((var213 & 8) == 0) {
                     var78 = var22;
                     var82 = var22 - 1;
                     var57 = var49 - 0.309016994374947D;
                     var65 = var49 + 1.0D - 0.309016994374947D;
                  } else {
                     var78 = var82 = var22 + 1;
                     var57 = var65 = var49 - 1.0D - 0.309016994374947D;
                  }

                  var83 = var19;
                  var84 = var20;
                  var85 = var21;
                  var86 = var22;
                  var67 = var43 - 0.618033988749894D;
                  var69 = var45 - 0.618033988749894D;
                  var71 = var47 - 0.618033988749894D;
                  var73 = var49 - 0.618033988749894D;
                  if ((var212 & 1) != 0) {
                     var83 = var19 + 2;
                     var67 -= 2.0D;
                  } else if ((var212 & 2) != 0) {
                     var84 = var20 + 2;
                     var69 -= 2.0D;
                  } else if ((var212 & 4) != 0) {
                     var85 = var21 + 2;
                     var71 -= 2.0D;
                  } else {
                     var86 = var22 + 2;
                     var73 -= 2.0D;
                  }
               }

               var105 = var43 - 1.0D - 0.309016994374947D;
               var107 = var45 - 0.0D - 0.309016994374947D;
               var109 = var47 - 0.0D - 0.309016994374947D;
               var111 = var49 - 0.0D - 0.309016994374947D;
               var113 = 2.0D - var105 * var105 - var107 * var107 - var109 * var109 - var111 * var111;
               if (var113 > 0.0D) {
                  var113 *= var113;
                  var87 += var113 * var113 * this.extrapolate(var19 + 1, var20 + 0, var21 + 0, var22 + 0, var105, var107, var109, var111);
               }

               var115 = var43 - 0.0D - 0.309016994374947D;
               var117 = var45 - 1.0D - 0.309016994374947D;
               var123 = 2.0D - var115 * var115 - var117 * var117 - var109 * var109 - var111 * var111;
               if (var123 > 0.0D) {
                  var123 *= var123;
                  var87 += var123 * var123 * this.extrapolate(var19 + 0, var20 + 1, var21 + 0, var22 + 0, var115, var117, var109, var111);
               }

               double var129 = var47 - 1.0D - 0.309016994374947D;
               var133 = 2.0D - var115 * var115 - var107 * var107 - var129 * var129 - var111 * var111;
               if (var133 > 0.0D) {
                  var133 *= var133;
                  var87 += var133 * var133 * this.extrapolate(var19 + 0, var20 + 0, var21 + 1, var22 + 0, var115, var107, var129, var111);
               }

               double var141 = var49 - 1.0D - 0.309016994374947D;
               var143 = 2.0D - var115 * var115 - var107 * var107 - var109 * var109 - var141 * var141;
               if (var143 > 0.0D) {
                  var143 *= var143;
                  var87 += var143 * var143 * this.extrapolate(var19 + 0, var20 + 0, var21 + 0, var22 + 1, var115, var107, var109, var141);
               }

               var145 = var43 - 1.0D - 0.618033988749894D;
               var147 = var45 - 1.0D - 0.618033988749894D;
               var149 = var47 - 0.0D - 0.618033988749894D;
               var151 = var49 - 0.0D - 0.618033988749894D;
               var153 = 2.0D - var145 * var145 - var147 * var147 - var149 * var149 - var151 * var151;
               if (var153 > 0.0D) {
                  var153 *= var153;
                  var87 += var153 * var153 * this.extrapolate(var19 + 1, var20 + 1, var21 + 0, var22 + 0, var145, var147, var149, var151);
               }

               var155 = var43 - 1.0D - 0.618033988749894D;
               var157 = var45 - 0.0D - 0.618033988749894D;
               var159 = var47 - 1.0D - 0.618033988749894D;
               var161 = var49 - 0.0D - 0.618033988749894D;
               var163 = 2.0D - var155 * var155 - var157 * var157 - var159 * var159 - var161 * var161;
               if (var163 > 0.0D) {
                  var163 *= var163;
                  var87 += var163 * var163 * this.extrapolate(var19 + 1, var20 + 0, var21 + 1, var22 + 0, var155, var157, var159, var161);
               }

               var165 = var43 - 1.0D - 0.618033988749894D;
               var167 = var45 - 0.0D - 0.618033988749894D;
               var169 = var47 - 0.0D - 0.618033988749894D;
               var171 = var49 - 1.0D - 0.618033988749894D;
               var173 = 2.0D - var165 * var165 - var167 * var167 - var169 * var169 - var171 * var171;
               if (var173 > 0.0D) {
                  var173 *= var173;
                  var87 += var173 * var173 * this.extrapolate(var19 + 1, var20 + 0, var21 + 0, var22 + 1, var165, var167, var169, var171);
               }

               var175 = var43 - 0.0D - 0.618033988749894D;
               var177 = var45 - 1.0D - 0.618033988749894D;
               var179 = var47 - 1.0D - 0.618033988749894D;
               var181 = var49 - 0.0D - 0.618033988749894D;
               var183 = 2.0D - var175 * var175 - var177 * var177 - var179 * var179 - var181 * var181;
               if (var183 > 0.0D) {
                  var183 *= var183;
                  var87 += var183 * var183 * this.extrapolate(var19 + 0, var20 + 1, var21 + 1, var22 + 0, var175, var177, var179, var181);
               }

               var185 = var43 - 0.0D - 0.618033988749894D;
               var187 = var45 - 1.0D - 0.618033988749894D;
               var189 = var47 - 0.0D - 0.618033988749894D;
               var191 = var49 - 1.0D - 0.618033988749894D;
               var193 = 2.0D - var185 * var185 - var187 * var187 - var189 * var189 - var191 * var191;
               if (var193 > 0.0D) {
                  var193 *= var193;
                  var87 += var193 * var193 * this.extrapolate(var19 + 0, var20 + 1, var21 + 0, var22 + 1, var185, var187, var189, var191);
               }

               var195 = var43 - 0.0D - 0.618033988749894D;
               var197 = var45 - 0.0D - 0.618033988749894D;
               var199 = var47 - 1.0D - 0.618033988749894D;
               var201 = var49 - 1.0D - 0.618033988749894D;
               var203 = 2.0D - var195 * var195 - var197 * var197 - var199 * var199 - var201 * var201;
               if (var203 > 0.0D) {
                  var203 *= var203;
                  var87 += var203 * var203 * this.extrapolate(var19 + 0, var20 + 0, var21 + 1, var22 + 1, var195, var197, var199, var201);
               }
            } else {
               var206 = true;
               var96 = true;
               if (var33 + var35 < var37 + var39) {
                  var205 = var33 + var35;
                  var91 = 12;
               } else {
                  var205 = var37 + var39;
                  var91 = 3;
               }

               if (var33 + var37 < var35 + var39) {
                  var93 = var33 + var37;
                  var207 = 10;
               } else {
                  var93 = var35 + var39;
                  var207 = 5;
               }

               if (var33 + var39 < var35 + var37) {
                  var209 = var33 + var39;
                  if (var205 <= var93 && var209 < var93) {
                     var93 = var209;
                     var207 = 6;
                  } else if (var205 > var93 && var209 < var205) {
                     var205 = var209;
                     var91 = 6;
                  }
               } else {
                  var209 = var35 + var37;
                  if (var205 <= var93 && var209 < var93) {
                     var93 = var209;
                     var207 = 9;
                  } else if (var205 > var93 && var209 < var205) {
                     var205 = var209;
                     var91 = 9;
                  }
               }

               var209 = 3.0D - var41 + var33;
               if (var205 <= var93 && var209 < var93) {
                  var93 = var209;
                  var207 = 14;
                  var96 = false;
               } else if (var205 > var93 && var209 < var205) {
                  var205 = var209;
                  var91 = 14;
                  var206 = false;
               }

               var99 = 3.0D - var41 + var35;
               if (var205 <= var93 && var99 < var93) {
                  var93 = var99;
                  var207 = 13;
                  var96 = false;
               } else if (var205 > var93 && var99 < var205) {
                  var205 = var99;
                  var91 = 13;
                  var206 = false;
               }

               var101 = 3.0D - var41 + var37;
               if (var205 <= var93 && var101 < var93) {
                  var93 = var101;
                  var207 = 11;
                  var96 = false;
               } else if (var205 > var93 && var101 < var205) {
                  var205 = var101;
                  var91 = 11;
                  var206 = false;
               }

               var103 = 3.0D - var41 + var39;
               if (var205 <= var93 && var103 < var93) {
                  var207 = 7;
                  var96 = false;
               } else if (var205 > var93 && var103 < var205) {
                  var91 = 7;
                  var206 = false;
               }

               if (var206 == var96) {
                  if (var206) {
                     var211 = (byte)(var91 & var207);
                     var106 = (byte)(var91 | var207);
                     var79 = var19;
                     var75 = var19;
                     var80 = var20;
                     var76 = var20;
                     var81 = var21;
                     var77 = var21;
                     var82 = var22;
                     var78 = var22;
                     var51 = var43 - 0.309016994374947D;
                     var53 = var45 - 0.309016994374947D;
                     var55 = var47 - 0.309016994374947D;
                     var57 = var49 - 0.309016994374947D;
                     var59 = var43 - 0.618033988749894D;
                     var61 = var45 - 0.618033988749894D;
                     var63 = var47 - 0.618033988749894D;
                     var65 = var49 - 0.618033988749894D;
                     if ((var211 & 1) != 0) {
                        var75 = var19 + 1;
                        --var51;
                        var79 = var19 + 2;
                        var59 -= 2.0D;
                     } else if ((var211 & 2) != 0) {
                        var76 = var20 + 1;
                        --var53;
                        var80 = var20 + 2;
                        var61 -= 2.0D;
                     } else if ((var211 & 4) != 0) {
                        var77 = var21 + 1;
                        --var55;
                        var81 = var21 + 2;
                        var63 -= 2.0D;
                     } else {
                        var78 = var22 + 1;
                        --var57;
                        var82 = var22 + 2;
                        var65 -= 2.0D;
                     }

                     var83 = var19 + 1;
                     var84 = var20 + 1;
                     var85 = var21 + 1;
                     var86 = var22 + 1;
                     var67 = var43 - 1.0D - 0.618033988749894D;
                     var69 = var45 - 1.0D - 0.618033988749894D;
                     var71 = var47 - 1.0D - 0.618033988749894D;
                     var73 = var49 - 1.0D - 0.618033988749894D;
                     if ((var106 & 1) == 0) {
                        var83 -= 2;
                        var67 += 2.0D;
                     } else if ((var106 & 2) == 0) {
                        var84 -= 2;
                        var69 += 2.0D;
                     } else if ((var106 & 4) == 0) {
                        var85 -= 2;
                        var71 += 2.0D;
                     } else {
                        var86 -= 2;
                        var73 += 2.0D;
                     }
                  } else {
                     var83 = var19 + 1;
                     var84 = var20 + 1;
                     var85 = var21 + 1;
                     var86 = var22 + 1;
                     var67 = var43 - 1.0D - 1.236067977499788D;
                     var69 = var45 - 1.0D - 1.236067977499788D;
                     var71 = var47 - 1.0D - 1.236067977499788D;
                     var73 = var49 - 1.0D - 1.236067977499788D;
                     var211 = (byte)(var91 & var207);
                     if ((var211 & 1) != 0) {
                        var75 = var19 + 2;
                        var79 = var19 + 1;
                        var51 = var43 - 2.0D - 0.927050983124841D;
                        var59 = var43 - 1.0D - 0.927050983124841D;
                     } else {
                        var79 = var19;
                        var75 = var19;
                        var51 = var59 = var43 - 0.927050983124841D;
                     }

                     if ((var211 & 2) != 0) {
                        var76 = var80 = var20 + 1;
                        var53 = var61 = var45 - 1.0D - 0.927050983124841D;
                        if ((var211 & 1) == 0) {
                           ++var76;
                           --var53;
                        } else {
                           ++var80;
                           --var61;
                        }
                     } else {
                        var80 = var20;
                        var76 = var20;
                        var53 = var61 = var45 - 0.927050983124841D;
                     }

                     if ((var211 & 4) != 0) {
                        var77 = var81 = var21 + 1;
                        var55 = var63 = var47 - 1.0D - 0.927050983124841D;
                        if ((var211 & 3) == 0) {
                           ++var77;
                           --var55;
                        } else {
                           ++var81;
                           --var63;
                        }
                     } else {
                        var81 = var21;
                        var77 = var21;
                        var55 = var63 = var47 - 0.927050983124841D;
                     }

                     if ((var211 & 8) != 0) {
                        var78 = var22 + 1;
                        var82 = var22 + 2;
                        var57 = var49 - 1.0D - 0.927050983124841D;
                        var65 = var49 - 2.0D - 0.927050983124841D;
                     } else {
                        var82 = var22;
                        var78 = var22;
                        var57 = var65 = var49 - 0.927050983124841D;
                     }
                  }
               } else {
                  if (var206) {
                     var213 = var91;
                     var212 = var207;
                  } else {
                     var213 = var207;
                     var212 = var91;
                  }

                  if ((var213 & 1) != 0) {
                     var75 = var19 + 2;
                     var79 = var19 + 1;
                     var51 = var43 - 2.0D - 0.927050983124841D;
                     var59 = var43 - 1.0D - 0.927050983124841D;
                  } else {
                     var79 = var19;
                     var75 = var19;
                     var51 = var59 = var43 - 0.927050983124841D;
                  }

                  if ((var213 & 2) != 0) {
                     var76 = var80 = var20 + 1;
                     var53 = var61 = var45 - 1.0D - 0.927050983124841D;
                     if ((var213 & 1) == 0) {
                        ++var76;
                        --var53;
                     } else {
                        ++var80;
                        --var61;
                     }
                  } else {
                     var80 = var20;
                     var76 = var20;
                     var53 = var61 = var45 - 0.927050983124841D;
                  }

                  if ((var213 & 4) != 0) {
                     var77 = var81 = var21 + 1;
                     var55 = var63 = var47 - 1.0D - 0.927050983124841D;
                     if ((var213 & 3) == 0) {
                        ++var77;
                        --var55;
                     } else {
                        ++var81;
                        --var63;
                     }
                  } else {
                     var81 = var21;
                     var77 = var21;
                     var55 = var63 = var47 - 0.927050983124841D;
                  }

                  if ((var213 & 8) != 0) {
                     var78 = var22 + 1;
                     var82 = var22 + 2;
                     var57 = var49 - 1.0D - 0.927050983124841D;
                     var65 = var49 - 2.0D - 0.927050983124841D;
                  } else {
                     var82 = var22;
                     var78 = var22;
                     var57 = var65 = var49 - 0.927050983124841D;
                  }

                  var83 = var19 + 1;
                  var84 = var20 + 1;
                  var85 = var21 + 1;
                  var86 = var22 + 1;
                  var67 = var43 - 1.0D - 0.618033988749894D;
                  var69 = var45 - 1.0D - 0.618033988749894D;
                  var71 = var47 - 1.0D - 0.618033988749894D;
                  var73 = var49 - 1.0D - 0.618033988749894D;
                  if ((var212 & 1) == 0) {
                     var83 -= 2;
                     var67 += 2.0D;
                  } else if ((var212 & 2) == 0) {
                     var84 -= 2;
                     var69 += 2.0D;
                  } else if ((var212 & 4) == 0) {
                     var85 -= 2;
                     var71 += 2.0D;
                  } else {
                     var86 -= 2;
                     var73 += 2.0D;
                  }
               }

               var105 = var43 - 1.0D - 0.927050983124841D;
               var107 = var45 - 1.0D - 0.927050983124841D;
               var109 = var47 - 1.0D - 0.927050983124841D;
               var111 = var49 - 0.927050983124841D;
               var113 = 2.0D - var105 * var105 - var107 * var107 - var109 * var109 - var111 * var111;
               if (var113 > 0.0D) {
                  var113 *= var113;
                  var87 += var113 * var113 * this.extrapolate(var19 + 1, var20 + 1, var21 + 1, var22 + 0, var105, var107, var109, var111);
               }

               var119 = var47 - 0.927050983124841D;
               double var121 = var49 - 1.0D - 0.927050983124841D;
               var123 = 2.0D - var105 * var105 - var107 * var107 - var119 * var119 - var121 * var121;
               if (var123 > 0.0D) {
                  var123 *= var123;
                  var87 += var123 * var123 * this.extrapolate(var19 + 1, var20 + 1, var21 + 0, var22 + 1, var105, var107, var119, var121);
               }

               var127 = var45 - 0.927050983124841D;
               var133 = 2.0D - var105 * var105 - var127 * var127 - var109 * var109 - var121 * var121;
               if (var133 > 0.0D) {
                  var133 *= var133;
                  var87 += var133 * var133 * this.extrapolate(var19 + 1, var20 + 0, var21 + 1, var22 + 1, var105, var127, var109, var121);
               }

               var135 = var43 - 0.927050983124841D;
               var143 = 2.0D - var135 * var135 - var107 * var107 - var109 * var109 - var121 * var121;
               if (var143 > 0.0D) {
                  var143 *= var143;
                  var87 += var143 * var143 * this.extrapolate(var19 + 0, var20 + 1, var21 + 1, var22 + 1, var135, var107, var109, var121);
               }

               var145 = var43 - 1.0D - 0.618033988749894D;
               var147 = var45 - 1.0D - 0.618033988749894D;
               var149 = var47 - 0.0D - 0.618033988749894D;
               var151 = var49 - 0.0D - 0.618033988749894D;
               var153 = 2.0D - var145 * var145 - var147 * var147 - var149 * var149 - var151 * var151;
               if (var153 > 0.0D) {
                  var153 *= var153;
                  var87 += var153 * var153 * this.extrapolate(var19 + 1, var20 + 1, var21 + 0, var22 + 0, var145, var147, var149, var151);
               }

               var155 = var43 - 1.0D - 0.618033988749894D;
               var157 = var45 - 0.0D - 0.618033988749894D;
               var159 = var47 - 1.0D - 0.618033988749894D;
               var161 = var49 - 0.0D - 0.618033988749894D;
               var163 = 2.0D - var155 * var155 - var157 * var157 - var159 * var159 - var161 * var161;
               if (var163 > 0.0D) {
                  var163 *= var163;
                  var87 += var163 * var163 * this.extrapolate(var19 + 1, var20 + 0, var21 + 1, var22 + 0, var155, var157, var159, var161);
               }

               var165 = var43 - 1.0D - 0.618033988749894D;
               var167 = var45 - 0.0D - 0.618033988749894D;
               var169 = var47 - 0.0D - 0.618033988749894D;
               var171 = var49 - 1.0D - 0.618033988749894D;
               var173 = 2.0D - var165 * var165 - var167 * var167 - var169 * var169 - var171 * var171;
               if (var173 > 0.0D) {
                  var173 *= var173;
                  var87 += var173 * var173 * this.extrapolate(var19 + 1, var20 + 0, var21 + 0, var22 + 1, var165, var167, var169, var171);
               }

               var175 = var43 - 0.0D - 0.618033988749894D;
               var177 = var45 - 1.0D - 0.618033988749894D;
               var179 = var47 - 1.0D - 0.618033988749894D;
               var181 = var49 - 0.0D - 0.618033988749894D;
               var183 = 2.0D - var175 * var175 - var177 * var177 - var179 * var179 - var181 * var181;
               if (var183 > 0.0D) {
                  var183 *= var183;
                  var87 += var183 * var183 * this.extrapolate(var19 + 0, var20 + 1, var21 + 1, var22 + 0, var175, var177, var179, var181);
               }

               var185 = var43 - 0.0D - 0.618033988749894D;
               var187 = var45 - 1.0D - 0.618033988749894D;
               var189 = var47 - 0.0D - 0.618033988749894D;
               var191 = var49 - 1.0D - 0.618033988749894D;
               var193 = 2.0D - var185 * var185 - var187 * var187 - var189 * var189 - var191 * var191;
               if (var193 > 0.0D) {
                  var193 *= var193;
                  var87 += var193 * var193 * this.extrapolate(var19 + 0, var20 + 1, var21 + 0, var22 + 1, var185, var187, var189, var191);
               }

               var195 = var43 - 0.0D - 0.618033988749894D;
               var197 = var45 - 0.0D - 0.618033988749894D;
               var199 = var47 - 1.0D - 0.618033988749894D;
               var201 = var49 - 1.0D - 0.618033988749894D;
               var203 = 2.0D - var195 * var195 - var197 * var197 - var199 * var199 - var201 * var201;
               if (var203 > 0.0D) {
                  var203 *= var203;
                  var87 += var203 * var203 * this.extrapolate(var19 + 0, var20 + 0, var21 + 1, var22 + 1, var195, var197, var199, var201);
               }
            }
         }
      }

      var205 = 2.0D - var51 * var51 - var53 * var53 - var55 * var55 - var57 * var57;
      if (var205 > 0.0D) {
         var205 *= var205;
         var87 += var205 * var205 * this.extrapolate(var75, var76, var77, var78, var51, var53, var55, var57);
      }

      double var210 = 2.0D - var59 * var59 - var61 * var61 - var63 * var63 - var65 * var65;
      if (var210 > 0.0D) {
         var210 *= var210;
         var87 += var210 * var210 * this.extrapolate(var79, var80, var81, var82, var59, var61, var63, var65);
      }

      var93 = 2.0D - var67 * var67 - var69 * var69 - var71 * var71 - var73 * var73;
      if (var93 > 0.0D) {
         var93 *= var93;
         var87 += var93 * var93 * this.extrapolate(var83, var84, var85, var86, var67, var69, var71, var73);
      }

      return var87 / 30.0D;
   }

   private double extrapolate(int var1, int var2, double var3, double var5) {
      int var7 = this.perm[this.perm[var1 & 255] + var2 & 255] & 14;
      return (double)gradients2D[var7] * var3 + (double)gradients2D[var7 + 1] * var5;
   }

   private double extrapolate(int var1, int var2, int var3, double var4, double var6, double var8) {
      short var10 = this.permGradIndex3D[this.perm[this.perm[var1 & 255] + var2 & 255] + var3 & 255];
      return (double)gradients3D[var10] * var4 + (double)gradients3D[var10 + 1] * var6 + (double)gradients3D[var10 + 2] * var8;
   }

   private double extrapolate(int var1, int var2, int var3, int var4, double var5, double var7, double var9, double var11) {
      int var13 = this.perm[this.perm[this.perm[this.perm[var1 & 255] + var2 & 255] + var3 & 255] + var4 & 255] & 252;
      return (double)gradients4D[var13] * var5 + (double)gradients4D[var13 + 1] * var7 + (double)gradients4D[var13 + 2] * var9 + (double)gradients4D[var13 + 3] * var11;
   }

   private static int fastFloor(double var0) {
      int var2 = (int)var0;
      return var0 < (double)var2 ? var2 - 1 : var2;
   }

   public double evalOct(float var1, float var2, int var3) {
      boolean var4 = true;
      double var5 = this.eval((double)var1, (double)var2, (double)var3);

      for(int var7 = 2; var7 <= 64; ++var7) {
         var5 += this.eval((double)(var1 * (float)var7 * var1), (double)(var2 * (float)var7 * var2), (double)(var3 * var7 * var3));
      }

      return var5;
   }
}
