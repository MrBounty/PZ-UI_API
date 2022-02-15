package org.joml;

public class QuaterniondInterpolator {
   private final QuaterniondInterpolator.SvdDecomposition3d svdDecomposition3d = new QuaterniondInterpolator.SvdDecomposition3d();
   private final double[] m = new double[9];
   private final Matrix3d u = new Matrix3d();
   private final Matrix3d v = new Matrix3d();

   public Quaterniond computeWeightedAverage(Quaterniond[] var1, double[] var2, int var3, Quaterniond var4) {
      double var5 = 0.0D;
      double var7 = 0.0D;
      double var9 = 0.0D;
      double var11 = 0.0D;
      double var13 = 0.0D;
      double var15 = 0.0D;
      double var17 = 0.0D;
      double var19 = 0.0D;
      double var21 = 0.0D;

      for(int var23 = 0; var23 < var1.length; ++var23) {
         Quaterniond var24 = var1[var23];
         double var25 = var24.x + var24.x;
         double var27 = var24.y + var24.y;
         double var29 = var24.z + var24.z;
         double var31 = var25 * var24.x;
         double var33 = var27 * var24.y;
         double var35 = var29 * var24.z;
         double var37 = var25 * var24.y;
         double var39 = var25 * var24.z;
         double var41 = var25 * var24.w;
         double var43 = var27 * var24.z;
         double var45 = var27 * var24.w;
         double var47 = var29 * var24.w;
         var5 += var2[var23] * (1.0D - var33 - var35);
         var7 += var2[var23] * (var37 + var47);
         var9 += var2[var23] * (var39 - var45);
         var11 += var2[var23] * (var37 - var47);
         var13 += var2[var23] * (1.0D - var35 - var31);
         var15 += var2[var23] * (var43 + var41);
         var17 += var2[var23] * (var39 + var45);
         var19 += var2[var23] * (var43 - var41);
         var21 += var2[var23] * (1.0D - var33 - var31);
      }

      this.m[0] = var5;
      this.m[1] = var7;
      this.m[2] = var9;
      this.m[3] = var11;
      this.m[4] = var13;
      this.m[5] = var15;
      this.m[6] = var17;
      this.m[7] = var19;
      this.m[8] = var21;
      this.svdDecomposition3d.svd(this.m, var3, this.u, this.v);
      this.u.mul((Matrix3dc)this.v.transpose());
      return var4.setFromNormalized((Matrix3dc)this.u).normalize();
   }

   private static class SvdDecomposition3d {
      private final double[] rv1 = new double[3];
      private final double[] w = new double[3];
      private final double[] v = new double[9];

      SvdDecomposition3d() {
      }

      private double SIGN(double var1, double var3) {
         return var3 >= 0.0D ? Math.abs(var1) : -Math.abs(var1);
      }

      void svd(double[] var1, int var2, Matrix3d var3, Matrix3d var4) {
         int var11 = 0;
         int var12 = 0;
         double var27 = 0.0D;
         double var29 = 0.0D;
         double var31 = 0.0D;

         int var6;
         int var8;
         int var10;
         double var15;
         double var17;
         double var19;
         for(var6 = 0; var6 < 3; ++var6) {
            var11 = var6 + 1;
            this.rv1[var6] = var31 * var29;
            var31 = 0.0D;
            var19 = 0.0D;
            var29 = 0.0D;

            for(var10 = var6; var10 < 3; ++var10) {
               var31 += Math.abs(var1[var10 + 3 * var6]);
            }

            if (var31 != 0.0D) {
               for(var10 = var6; var10 < 3; ++var10) {
                  var1[var10 + 3 * var6] /= var31;
                  var19 += var1[var10 + 3 * var6] * var1[var10 + 3 * var6];
               }

               var15 = var1[var6 + 3 * var6];
               var29 = -this.SIGN(Math.sqrt(var19), var15);
               var17 = var15 * var29 - var19;
               var1[var6 + 3 * var6] = var15 - var29;
               if (var6 != 2) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     var19 = 0.0D;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var19 += var1[var10 + 3 * var6] * var1[var10 + 3 * var8];
                     }

                     var15 = var19 / var17;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var1[var10 + 3 * var8] += var15 * var1[var10 + 3 * var6];
                     }
                  }
               }

               for(var10 = var6; var10 < 3; ++var10) {
                  var1[var10 + 3 * var6] *= var31;
               }
            }

            this.w[var6] = var31 * var29;
            var31 = 0.0D;
            var19 = 0.0D;
            var29 = 0.0D;
            if (var6 < 3 && var6 != 2) {
               for(var10 = var11; var10 < 3; ++var10) {
                  var31 += Math.abs(var1[var6 + 3 * var10]);
               }

               if (var31 != 0.0D) {
                  for(var10 = var11; var10 < 3; ++var10) {
                     var1[var6 + 3 * var10] /= var31;
                     var19 += var1[var6 + 3 * var10] * var1[var6 + 3 * var10];
                  }

                  var15 = var1[var6 + 3 * var11];
                  var29 = -this.SIGN(Math.sqrt(var19), var15);
                  var17 = var15 * var29 - var19;
                  var1[var6 + 3 * var11] = var15 - var29;

                  for(var10 = var11; var10 < 3; ++var10) {
                     this.rv1[var10] = var1[var6 + 3 * var10] / var17;
                  }

                  if (var6 != 2) {
                     for(var8 = var11; var8 < 3; ++var8) {
                        var19 = 0.0D;

                        for(var10 = var11; var10 < 3; ++var10) {
                           var19 += var1[var8 + 3 * var10] * var1[var6 + 3 * var10];
                        }

                        for(var10 = var11; var10 < 3; ++var10) {
                           var1[var8 + 3 * var10] += var19 * this.rv1[var10];
                        }
                     }
                  }

                  for(var10 = var11; var10 < 3; ++var10) {
                     var1[var6 + 3 * var10] *= var31;
                  }
               }
            }

            var27 = Math.max(var27, Math.abs(this.w[var6]) + Math.abs(this.rv1[var6]));
         }

         for(var6 = 2; var6 >= 0; var11 = var6--) {
            if (var6 < 2) {
               if (var29 != 0.0D) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     this.v[var8 + 3 * var6] = var1[var6 + 3 * var8] / var1[var6 + 3 * var11] / var29;
                  }

                  for(var8 = var11; var8 < 3; ++var8) {
                     var19 = 0.0D;

                     for(var10 = var11; var10 < 3; ++var10) {
                        var19 += var1[var6 + 3 * var10] * this.v[var10 + 3 * var8];
                     }

                     for(var10 = var11; var10 < 3; ++var10) {
                        double[] var10000 = this.v;
                        var10000[var10 + 3 * var8] += var19 * this.v[var10 + 3 * var6];
                     }
                  }
               }

               for(var8 = var11; var8 < 3; ++var8) {
                  this.v[var6 + 3 * var8] = this.v[var8 + 3 * var6] = 0.0D;
               }
            }

            this.v[var6 + 3 * var6] = 1.0D;
            var29 = this.rv1[var6];
         }

         for(var6 = 2; var6 >= 0; --var6) {
            var11 = var6 + 1;
            var29 = this.w[var6];
            if (var6 < 2) {
               for(var8 = var11; var8 < 3; ++var8) {
                  var1[var6 + 3 * var8] = 0.0D;
               }
            }

            if (var29 == 0.0D) {
               for(var8 = var6; var8 < 3; ++var8) {
                  var1[var8 + 3 * var6] = 0.0D;
               }
            } else {
               var29 = 1.0D / var29;
               if (var6 != 2) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     var19 = 0.0D;

                     for(var10 = var11; var10 < 3; ++var10) {
                        var19 += var1[var10 + 3 * var6] * var1[var10 + 3 * var8];
                     }

                     var15 = var19 / var1[var6 + 3 * var6] * var29;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var1[var10 + 3 * var8] += var15 * var1[var10 + 3 * var6];
                     }
                  }
               }

               for(var8 = var6; var8 < 3; ++var8) {
                  var1[var8 + 3 * var6] *= var29;
               }
            }

            ++var1[var6 + 3 * var6];
         }

         label200:
         for(var10 = 2; var10 >= 0; --var10) {
            for(int var7 = 0; var7 < var2; ++var7) {
               boolean var5 = true;

               for(var11 = var10; var11 >= 0; --var11) {
                  var12 = var11 - 1;
                  if (Math.abs(this.rv1[var11]) + var27 == var27) {
                     var5 = false;
                     break;
                  }

                  if (Math.abs(this.w[var12]) + var27 == var27) {
                     break;
                  }
               }

               double var13;
               double var23;
               double var25;
               if (var5) {
                  var13 = 0.0D;
                  var19 = 1.0D;

                  for(var6 = var11; var6 <= var10; ++var6) {
                     var15 = var19 * this.rv1[var6];
                     if (Math.abs(var15) + var27 != var27) {
                        var29 = this.w[var6];
                        var17 = PYTHAG(var15, var29);
                        this.w[var6] = var17;
                        var17 = 1.0D / var17;
                        var13 = var29 * var17;
                        var19 = -var15 * var17;

                        for(var8 = 0; var8 < 3; ++var8) {
                           var23 = var1[var8 + 3 * var12];
                           var25 = var1[var8 + 3 * var6];
                           var1[var8 + 3 * var12] = var23 * var13 + var25 * var19;
                           var1[var8 + 3 * var6] = var25 * var13 - var23 * var19;
                        }
                     }
                  }
               }

               var25 = this.w[var10];
               if (var11 == var10) {
                  if (!(var25 < 0.0D)) {
                     break;
                  }

                  this.w[var10] = -var25;
                  var8 = 0;

                  while(true) {
                     if (var8 >= 3) {
                        continue label200;
                     }

                     this.v[var8 + 3 * var10] = -this.v[var8 + 3 * var10];
                     ++var8;
                  }
               }

               if (var7 == var2 - 1) {
                  throw new RuntimeException("No convergence after " + var2 + " iterations");
               }

               double var21 = this.w[var11];
               var12 = var10 - 1;
               var23 = this.w[var12];
               var29 = this.rv1[var12];
               var17 = this.rv1[var10];
               var15 = ((var23 - var25) * (var23 + var25) + (var29 - var17) * (var29 + var17)) / (2.0D * var17 * var23);
               var29 = PYTHAG(var15, 1.0D);
               var15 = ((var21 - var25) * (var21 + var25) + var17 * (var23 / (var15 + this.SIGN(var29, var15)) - var17)) / var21;
               var19 = 1.0D;
               var13 = 1.0D;

               for(var8 = var11; var8 <= var12; ++var8) {
                  var6 = var8 + 1;
                  var29 = this.rv1[var6];
                  var23 = this.w[var6];
                  var17 = var19 * var29;
                  var29 = var13 * var29;
                  var25 = PYTHAG(var15, var17);
                  this.rv1[var8] = var25;
                  var13 = var15 / var25;
                  var19 = var17 / var25;
                  var15 = var21 * var13 + var29 * var19;
                  var29 = var29 * var13 - var21 * var19;
                  var17 = var23 * var19;
                  var23 *= var13;

                  int var9;
                  for(var9 = 0; var9 < 3; ++var9) {
                     var21 = this.v[var9 + 3 * var8];
                     var25 = this.v[var9 + 3 * var6];
                     this.v[var9 + 3 * var8] = var21 * var13 + var25 * var19;
                     this.v[var9 + 3 * var6] = var25 * var13 - var21 * var19;
                  }

                  var25 = PYTHAG(var15, var17);
                  this.w[var8] = var25;
                  if (var25 != 0.0D) {
                     var25 = 1.0D / var25;
                     var13 = var15 * var25;
                     var19 = var17 * var25;
                  }

                  var15 = var13 * var29 + var19 * var23;
                  var21 = var13 * var23 - var19 * var29;

                  for(var9 = 0; var9 < 3; ++var9) {
                     var23 = var1[var9 + 3 * var8];
                     var25 = var1[var9 + 3 * var6];
                     var1[var9 + 3 * var8] = var23 * var13 + var25 * var19;
                     var1[var9 + 3 * var6] = var25 * var13 - var23 * var19;
                  }
               }

               this.rv1[var11] = 0.0D;
               this.rv1[var10] = var15;
               this.w[var10] = var21;
            }
         }

         var3.set(var1);
         var4.set(this.v);
      }

      private static double PYTHAG(double var0, double var2) {
         double var4 = Math.abs(var0);
         double var6 = Math.abs(var2);
         double var8;
         double var10;
         if (var4 > var6) {
            var8 = var6 / var4;
            var10 = var4 * Math.sqrt(1.0D + var8 * var8);
         } else if (var6 > 0.0D) {
            var8 = var4 / var6;
            var10 = var6 * Math.sqrt(1.0D + var8 * var8);
         } else {
            var10 = 0.0D;
         }

         return var10;
      }
   }
}
