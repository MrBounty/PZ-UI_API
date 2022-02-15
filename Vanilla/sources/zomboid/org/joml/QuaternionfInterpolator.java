package org.joml;

public class QuaternionfInterpolator {
   private final QuaternionfInterpolator.SvdDecomposition3f svdDecomposition3f = new QuaternionfInterpolator.SvdDecomposition3f();
   private final float[] m = new float[9];
   private final Matrix3f u = new Matrix3f();
   private final Matrix3f v = new Matrix3f();

   public Quaternionf computeWeightedAverage(Quaternionfc[] var1, float[] var2, int var3, Quaternionf var4) {
      float var5 = 0.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      float var12 = 0.0F;
      float var13 = 0.0F;

      for(int var14 = 0; var14 < var1.length; ++var14) {
         Quaternionfc var15 = var1[var14];
         float var16 = var15.x() + var15.x();
         float var17 = var15.y() + var15.y();
         float var18 = var15.z() + var15.z();
         float var19 = var16 * var15.x();
         float var20 = var17 * var15.y();
         float var21 = var18 * var15.z();
         float var22 = var16 * var15.y();
         float var23 = var16 * var15.z();
         float var24 = var16 * var15.w();
         float var25 = var17 * var15.z();
         float var26 = var17 * var15.w();
         float var27 = var18 * var15.w();
         var5 += var2[var14] * (1.0F - var20 - var21);
         var6 += var2[var14] * (var22 + var27);
         var7 += var2[var14] * (var23 - var26);
         var8 += var2[var14] * (var22 - var27);
         var9 += var2[var14] * (1.0F - var21 - var19);
         var10 += var2[var14] * (var25 + var24);
         var11 += var2[var14] * (var23 + var26);
         var12 += var2[var14] * (var25 - var24);
         var13 += var2[var14] * (1.0F - var20 - var19);
      }

      this.m[0] = var5;
      this.m[1] = var6;
      this.m[2] = var7;
      this.m[3] = var8;
      this.m[4] = var9;
      this.m[5] = var10;
      this.m[6] = var11;
      this.m[7] = var12;
      this.m[8] = var13;
      this.svdDecomposition3f.svd(this.m, var3, this.u, this.v);
      this.u.mul(this.v.transpose());
      return var4.setFromNormalized((Matrix3fc)this.u).normalize();
   }

   private static class SvdDecomposition3f {
      private final float[] rv1 = new float[3];
      private final float[] w = new float[3];
      private final float[] v = new float[9];

      SvdDecomposition3f() {
      }

      private float SIGN(float var1, float var2) {
         return (double)var2 >= 0.0D ? Math.abs(var1) : -Math.abs(var1);
      }

      void svd(float[] var1, int var2, Matrix3f var3, Matrix3f var4) {
         int var11 = 0;
         int var12 = 0;
         float var20 = 0.0F;
         float var21 = 0.0F;
         float var22 = 0.0F;

         int var6;
         int var8;
         int var10;
         float var14;
         float var15;
         float var16;
         for(var6 = 0; var6 < 3; ++var6) {
            var11 = var6 + 1;
            this.rv1[var6] = var22 * var21;
            var22 = 0.0F;
            var16 = 0.0F;
            var21 = 0.0F;

            for(var10 = var6; var10 < 3; ++var10) {
               var22 += Math.abs(var1[var10 + 3 * var6]);
            }

            if (var22 != 0.0F) {
               for(var10 = var6; var10 < 3; ++var10) {
                  var1[var10 + 3 * var6] /= var22;
                  var16 += var1[var10 + 3 * var6] * var1[var10 + 3 * var6];
               }

               var14 = var1[var6 + 3 * var6];
               var21 = -this.SIGN(Math.sqrt(var16), var14);
               var15 = var14 * var21 - var16;
               var1[var6 + 3 * var6] = var14 - var21;
               if (var6 != 2) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     var16 = 0.0F;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var16 += var1[var10 + 3 * var6] * var1[var10 + 3 * var8];
                     }

                     var14 = var16 / var15;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var1[var10 + 3 * var8] += var14 * var1[var10 + 3 * var6];
                     }
                  }
               }

               for(var10 = var6; var10 < 3; ++var10) {
                  var1[var10 + 3 * var6] *= var22;
               }
            }

            this.w[var6] = var22 * var21;
            var22 = 0.0F;
            var16 = 0.0F;
            var21 = 0.0F;
            if (var6 < 3 && var6 != 2) {
               for(var10 = var11; var10 < 3; ++var10) {
                  var22 += Math.abs(var1[var6 + 3 * var10]);
               }

               if (var22 != 0.0F) {
                  for(var10 = var11; var10 < 3; ++var10) {
                     var1[var6 + 3 * var10] /= var22;
                     var16 += var1[var6 + 3 * var10] * var1[var6 + 3 * var10];
                  }

                  var14 = var1[var6 + 3 * var11];
                  var21 = -this.SIGN(Math.sqrt(var16), var14);
                  var15 = var14 * var21 - var16;
                  var1[var6 + 3 * var11] = var14 - var21;

                  for(var10 = var11; var10 < 3; ++var10) {
                     this.rv1[var10] = var1[var6 + 3 * var10] / var15;
                  }

                  if (var6 != 2) {
                     for(var8 = var11; var8 < 3; ++var8) {
                        var16 = 0.0F;

                        for(var10 = var11; var10 < 3; ++var10) {
                           var16 += var1[var8 + 3 * var10] * var1[var6 + 3 * var10];
                        }

                        for(var10 = var11; var10 < 3; ++var10) {
                           var1[var8 + 3 * var10] += var16 * this.rv1[var10];
                        }
                     }
                  }

                  for(var10 = var11; var10 < 3; ++var10) {
                     var1[var6 + 3 * var10] *= var22;
                  }
               }
            }

            var20 = Math.max(var20, Math.abs(this.w[var6]) + Math.abs(this.rv1[var6]));
         }

         for(var6 = 2; var6 >= 0; var11 = var6--) {
            if (var6 < 2) {
               if (var21 != 0.0F) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     this.v[var8 + 3 * var6] = var1[var6 + 3 * var8] / var1[var6 + 3 * var11] / var21;
                  }

                  for(var8 = var11; var8 < 3; ++var8) {
                     var16 = 0.0F;

                     for(var10 = var11; var10 < 3; ++var10) {
                        var16 += var1[var6 + 3 * var10] * this.v[var10 + 3 * var8];
                     }

                     for(var10 = var11; var10 < 3; ++var10) {
                        float[] var10000 = this.v;
                        var10000[var10 + 3 * var8] += var16 * this.v[var10 + 3 * var6];
                     }
                  }
               }

               for(var8 = var11; var8 < 3; ++var8) {
                  this.v[var6 + 3 * var8] = this.v[var8 + 3 * var6] = 0.0F;
               }
            }

            this.v[var6 + 3 * var6] = 1.0F;
            var21 = this.rv1[var6];
         }

         for(var6 = 2; var6 >= 0; --var6) {
            var11 = var6 + 1;
            var21 = this.w[var6];
            if (var6 < 2) {
               for(var8 = var11; var8 < 3; ++var8) {
                  var1[var6 + 3 * var8] = 0.0F;
               }
            }

            if (var21 == 0.0F) {
               for(var8 = var6; var8 < 3; ++var8) {
                  var1[var8 + 3 * var6] = 0.0F;
               }
            } else {
               var21 = 1.0F / var21;
               if (var6 != 2) {
                  for(var8 = var11; var8 < 3; ++var8) {
                     var16 = 0.0F;

                     for(var10 = var11; var10 < 3; ++var10) {
                        var16 += var1[var10 + 3 * var6] * var1[var10 + 3 * var8];
                     }

                     var14 = var16 / var1[var6 + 3 * var6] * var21;

                     for(var10 = var6; var10 < 3; ++var10) {
                        var1[var10 + 3 * var8] += var14 * var1[var10 + 3 * var6];
                     }
                  }
               }

               for(var8 = var6; var8 < 3; ++var8) {
                  var1[var8 + 3 * var6] *= var21;
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
                  if (Math.abs(this.rv1[var11]) + var20 == var20) {
                     var5 = false;
                     break;
                  }

                  if (Math.abs(this.w[var12]) + var20 == var20) {
                     break;
                  }
               }

               float var13;
               float var18;
               float var19;
               if (var5) {
                  var13 = 0.0F;
                  var16 = 1.0F;

                  for(var6 = var11; var6 <= var10; ++var6) {
                     var14 = var16 * this.rv1[var6];
                     if (Math.abs(var14) + var20 != var20) {
                        var21 = this.w[var6];
                        var15 = PYTHAG(var14, var21);
                        this.w[var6] = var15;
                        var15 = 1.0F / var15;
                        var13 = var21 * var15;
                        var16 = -var14 * var15;

                        for(var8 = 0; var8 < 3; ++var8) {
                           var18 = var1[var8 + 3 * var12];
                           var19 = var1[var8 + 3 * var6];
                           var1[var8 + 3 * var12] = var18 * var13 + var19 * var16;
                           var1[var8 + 3 * var6] = var19 * var13 - var18 * var16;
                        }
                     }
                  }
               }

               var19 = this.w[var10];
               if (var11 == var10) {
                  if (!(var19 < 0.0F)) {
                     break;
                  }

                  this.w[var10] = -var19;
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

               float var17 = this.w[var11];
               var12 = var10 - 1;
               var18 = this.w[var12];
               var21 = this.rv1[var12];
               var15 = this.rv1[var10];
               var14 = ((var18 - var19) * (var18 + var19) + (var21 - var15) * (var21 + var15)) / (2.0F * var15 * var18);
               var21 = PYTHAG(var14, 1.0F);
               var14 = ((var17 - var19) * (var17 + var19) + var15 * (var18 / (var14 + this.SIGN(var21, var14)) - var15)) / var17;
               var16 = 1.0F;
               var13 = 1.0F;

               for(var8 = var11; var8 <= var12; ++var8) {
                  var6 = var8 + 1;
                  var21 = this.rv1[var6];
                  var18 = this.w[var6];
                  var15 = var16 * var21;
                  var21 = var13 * var21;
                  var19 = PYTHAG(var14, var15);
                  this.rv1[var8] = var19;
                  var13 = var14 / var19;
                  var16 = var15 / var19;
                  var14 = var17 * var13 + var21 * var16;
                  var21 = var21 * var13 - var17 * var16;
                  var15 = var18 * var16;
                  var18 *= var13;

                  int var9;
                  for(var9 = 0; var9 < 3; ++var9) {
                     var17 = this.v[var9 + 3 * var8];
                     var19 = this.v[var9 + 3 * var6];
                     this.v[var9 + 3 * var8] = var17 * var13 + var19 * var16;
                     this.v[var9 + 3 * var6] = var19 * var13 - var17 * var16;
                  }

                  var19 = PYTHAG(var14, var15);
                  this.w[var8] = var19;
                  if (var19 != 0.0F) {
                     var19 = 1.0F / var19;
                     var13 = var14 * var19;
                     var16 = var15 * var19;
                  }

                  var14 = var13 * var21 + var16 * var18;
                  var17 = var13 * var18 - var16 * var21;

                  for(var9 = 0; var9 < 3; ++var9) {
                     var18 = var1[var9 + 3 * var8];
                     var19 = var1[var9 + 3 * var6];
                     var1[var9 + 3 * var8] = var18 * var13 + var19 * var16;
                     var1[var9 + 3 * var6] = var19 * var13 - var18 * var16;
                  }
               }

               this.rv1[var11] = 0.0F;
               this.rv1[var10] = var14;
               this.w[var10] = var17;
            }
         }

         var3.set(var1);
         var4.set(this.v);
      }

      private static float PYTHAG(float var0, float var1) {
         float var2 = Math.abs(var0);
         float var3 = Math.abs(var1);
         float var4;
         float var5;
         if (var2 > var3) {
            var4 = var3 / var2;
            var5 = var2 * (float)Math.sqrt(1.0D + (double)(var4 * var4));
         } else if (var3 > 0.0F) {
            var4 = var2 / var3;
            var5 = var3 * (float)Math.sqrt(1.0D + (double)(var4 * var4));
         } else {
            var5 = 0.0F;
         }

         return var5;
      }
   }
}
