package org.joml;

public class FrustumIntersection {
   public static final int PLANE_NX = 0;
   public static final int PLANE_PX = 1;
   public static final int PLANE_NY = 2;
   public static final int PLANE_PY = 3;
   public static final int PLANE_NZ = 4;
   public static final int PLANE_PZ = 5;
   public static final int INTERSECT = -1;
   public static final int INSIDE = -2;
   public static final int OUTSIDE = -3;
   public static final int PLANE_MASK_NX = 1;
   public static final int PLANE_MASK_PX = 2;
   public static final int PLANE_MASK_NY = 4;
   public static final int PLANE_MASK_PY = 8;
   public static final int PLANE_MASK_NZ = 16;
   public static final int PLANE_MASK_PZ = 32;
   private float nxX;
   private float nxY;
   private float nxZ;
   private float nxW;
   private float pxX;
   private float pxY;
   private float pxZ;
   private float pxW;
   private float nyX;
   private float nyY;
   private float nyZ;
   private float nyW;
   private float pyX;
   private float pyY;
   private float pyZ;
   private float pyW;
   private float nzX;
   private float nzY;
   private float nzZ;
   private float nzW;
   private float pzX;
   private float pzY;
   private float pzZ;
   private float pzW;
   private final Vector4f[] planes = new Vector4f[6];

   public FrustumIntersection() {
      for(int var1 = 0; var1 < 6; ++var1) {
         this.planes[var1] = new Vector4f();
      }

   }

   public FrustumIntersection(Matrix4fc var1) {
      for(int var2 = 0; var2 < 6; ++var2) {
         this.planes[var2] = new Vector4f();
      }

      this.set(var1, true);
   }

   public FrustumIntersection(Matrix4fc var1, boolean var2) {
      for(int var3 = 0; var3 < 6; ++var3) {
         this.planes[var3] = new Vector4f();
      }

      this.set(var1, var2);
   }

   public FrustumIntersection set(Matrix4fc var1) {
      return this.set(var1, true);
   }

   public FrustumIntersection set(Matrix4fc var1, boolean var2) {
      this.nxX = var1.m03() + var1.m00();
      this.nxY = var1.m13() + var1.m10();
      this.nxZ = var1.m23() + var1.m20();
      this.nxW = var1.m33() + var1.m30();
      float var3;
      if (var2) {
         var3 = Math.invsqrt(this.nxX * this.nxX + this.nxY * this.nxY + this.nxZ * this.nxZ);
         this.nxX *= var3;
         this.nxY *= var3;
         this.nxZ *= var3;
         this.nxW *= var3;
      }

      this.planes[0].set(this.nxX, this.nxY, this.nxZ, this.nxW);
      this.pxX = var1.m03() - var1.m00();
      this.pxY = var1.m13() - var1.m10();
      this.pxZ = var1.m23() - var1.m20();
      this.pxW = var1.m33() - var1.m30();
      if (var2) {
         var3 = Math.invsqrt(this.pxX * this.pxX + this.pxY * this.pxY + this.pxZ * this.pxZ);
         this.pxX *= var3;
         this.pxY *= var3;
         this.pxZ *= var3;
         this.pxW *= var3;
      }

      this.planes[1].set(this.pxX, this.pxY, this.pxZ, this.pxW);
      this.nyX = var1.m03() + var1.m01();
      this.nyY = var1.m13() + var1.m11();
      this.nyZ = var1.m23() + var1.m21();
      this.nyW = var1.m33() + var1.m31();
      if (var2) {
         var3 = Math.invsqrt(this.nyX * this.nyX + this.nyY * this.nyY + this.nyZ * this.nyZ);
         this.nyX *= var3;
         this.nyY *= var3;
         this.nyZ *= var3;
         this.nyW *= var3;
      }

      this.planes[2].set(this.nyX, this.nyY, this.nyZ, this.nyW);
      this.pyX = var1.m03() - var1.m01();
      this.pyY = var1.m13() - var1.m11();
      this.pyZ = var1.m23() - var1.m21();
      this.pyW = var1.m33() - var1.m31();
      if (var2) {
         var3 = Math.invsqrt(this.pyX * this.pyX + this.pyY * this.pyY + this.pyZ * this.pyZ);
         this.pyX *= var3;
         this.pyY *= var3;
         this.pyZ *= var3;
         this.pyW *= var3;
      }

      this.planes[3].set(this.pyX, this.pyY, this.pyZ, this.pyW);
      this.nzX = var1.m03() + var1.m02();
      this.nzY = var1.m13() + var1.m12();
      this.nzZ = var1.m23() + var1.m22();
      this.nzW = var1.m33() + var1.m32();
      if (var2) {
         var3 = Math.invsqrt(this.nzX * this.nzX + this.nzY * this.nzY + this.nzZ * this.nzZ);
         this.nzX *= var3;
         this.nzY *= var3;
         this.nzZ *= var3;
         this.nzW *= var3;
      }

      this.planes[4].set(this.nzX, this.nzY, this.nzZ, this.nzW);
      this.pzX = var1.m03() - var1.m02();
      this.pzY = var1.m13() - var1.m12();
      this.pzZ = var1.m23() - var1.m22();
      this.pzW = var1.m33() - var1.m32();
      if (var2) {
         var3 = Math.invsqrt(this.pzX * this.pzX + this.pzY * this.pzY + this.pzZ * this.pzZ);
         this.pzX *= var3;
         this.pzY *= var3;
         this.pzZ *= var3;
         this.pzW *= var3;
      }

      this.planes[5].set(this.pzX, this.pzY, this.pzZ, this.pzW);
      return this;
   }

   public boolean testPoint(Vector3fc var1) {
      return this.testPoint(var1.x(), var1.y(), var1.z());
   }

   public boolean testPoint(float var1, float var2, float var3) {
      return this.nxX * var1 + this.nxY * var2 + this.nxZ * var3 + this.nxW >= 0.0F && this.pxX * var1 + this.pxY * var2 + this.pxZ * var3 + this.pxW >= 0.0F && this.nyX * var1 + this.nyY * var2 + this.nyZ * var3 + this.nyW >= 0.0F && this.pyX * var1 + this.pyY * var2 + this.pyZ * var3 + this.pyW >= 0.0F && this.nzX * var1 + this.nzY * var2 + this.nzZ * var3 + this.nzW >= 0.0F && this.pzX * var1 + this.pzY * var2 + this.pzZ * var3 + this.pzW >= 0.0F;
   }

   public boolean testSphere(Vector3fc var1, float var2) {
      return this.testSphere(var1.x(), var1.y(), var1.z(), var2);
   }

   public boolean testSphere(float var1, float var2, float var3, float var4) {
      return this.nxX * var1 + this.nxY * var2 + this.nxZ * var3 + this.nxW >= -var4 && this.pxX * var1 + this.pxY * var2 + this.pxZ * var3 + this.pxW >= -var4 && this.nyX * var1 + this.nyY * var2 + this.nyZ * var3 + this.nyW >= -var4 && this.pyX * var1 + this.pyY * var2 + this.pyZ * var3 + this.pyW >= -var4 && this.nzX * var1 + this.nzY * var2 + this.nzZ * var3 + this.nzW >= -var4 && this.pzX * var1 + this.pzY * var2 + this.pzZ * var3 + this.pzW >= -var4;
   }

   public int intersectSphere(Vector3fc var1, float var2) {
      return this.intersectSphere(var1.x(), var1.y(), var1.z(), var2);
   }

   public int intersectSphere(float var1, float var2, float var3, float var4) {
      boolean var5 = true;
      float var6 = this.nxX * var1 + this.nxY * var2 + this.nxZ * var3 + this.nxW;
      if (var6 >= -var4) {
         var5 &= var6 >= var4;
         var6 = this.pxX * var1 + this.pxY * var2 + this.pxZ * var3 + this.pxW;
         if (var6 >= -var4) {
            var5 &= var6 >= var4;
            var6 = this.nyX * var1 + this.nyY * var2 + this.nyZ * var3 + this.nyW;
            if (var6 >= -var4) {
               var5 &= var6 >= var4;
               var6 = this.pyX * var1 + this.pyY * var2 + this.pyZ * var3 + this.pyW;
               if (var6 >= -var4) {
                  var5 &= var6 >= var4;
                  var6 = this.nzX * var1 + this.nzY * var2 + this.nzZ * var3 + this.nzW;
                  if (var6 >= -var4) {
                     var5 &= var6 >= var4;
                     var6 = this.pzX * var1 + this.pzY * var2 + this.pzZ * var3 + this.pzW;
                     if (var6 >= -var4) {
                        var5 &= var6 >= var4;
                        return var5 ? -2 : -1;
                     }
                  }
               }
            }
         }
      }

      return -3;
   }

   public boolean testAab(Vector3fc var1, Vector3fc var2) {
      return this.testAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public boolean testAab(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.nxX * (this.nxX < 0.0F ? var1 : var4) + this.nxY * (this.nxY < 0.0F ? var2 : var5) + this.nxZ * (this.nxZ < 0.0F ? var3 : var6) >= -this.nxW && this.pxX * (this.pxX < 0.0F ? var1 : var4) + this.pxY * (this.pxY < 0.0F ? var2 : var5) + this.pxZ * (this.pxZ < 0.0F ? var3 : var6) >= -this.pxW && this.nyX * (this.nyX < 0.0F ? var1 : var4) + this.nyY * (this.nyY < 0.0F ? var2 : var5) + this.nyZ * (this.nyZ < 0.0F ? var3 : var6) >= -this.nyW && this.pyX * (this.pyX < 0.0F ? var1 : var4) + this.pyY * (this.pyY < 0.0F ? var2 : var5) + this.pyZ * (this.pyZ < 0.0F ? var3 : var6) >= -this.pyW && this.nzX * (this.nzX < 0.0F ? var1 : var4) + this.nzY * (this.nzY < 0.0F ? var2 : var5) + this.nzZ * (this.nzZ < 0.0F ? var3 : var6) >= -this.nzW && this.pzX * (this.pzX < 0.0F ? var1 : var4) + this.pzY * (this.pzY < 0.0F ? var2 : var5) + this.pzZ * (this.pzZ < 0.0F ? var3 : var6) >= -this.pzW;
   }

   public boolean testPlaneXY(Vector2fc var1, Vector2fc var2) {
      return this.testPlaneXY(var1.x(), var1.y(), var2.x(), var2.y());
   }

   public boolean testPlaneXY(float var1, float var2, float var3, float var4) {
      return this.nxX * (this.nxX < 0.0F ? var1 : var3) + this.nxY * (this.nxY < 0.0F ? var2 : var4) >= -this.nxW && this.pxX * (this.pxX < 0.0F ? var1 : var3) + this.pxY * (this.pxY < 0.0F ? var2 : var4) >= -this.pxW && this.nyX * (this.nyX < 0.0F ? var1 : var3) + this.nyY * (this.nyY < 0.0F ? var2 : var4) >= -this.nyW && this.pyX * (this.pyX < 0.0F ? var1 : var3) + this.pyY * (this.pyY < 0.0F ? var2 : var4) >= -this.pyW && this.nzX * (this.nzX < 0.0F ? var1 : var3) + this.nzY * (this.nzY < 0.0F ? var2 : var4) >= -this.nzW && this.pzX * (this.pzX < 0.0F ? var1 : var3) + this.pzY * (this.pzY < 0.0F ? var2 : var4) >= -this.pzW;
   }

   public boolean testPlaneXZ(float var1, float var2, float var3, float var4) {
      return this.nxX * (this.nxX < 0.0F ? var1 : var3) + this.nxZ * (this.nxZ < 0.0F ? var2 : var4) >= -this.nxW && this.pxX * (this.pxX < 0.0F ? var1 : var3) + this.pxZ * (this.pxZ < 0.0F ? var2 : var4) >= -this.pxW && this.nyX * (this.nyX < 0.0F ? var1 : var3) + this.nyZ * (this.nyZ < 0.0F ? var2 : var4) >= -this.nyW && this.pyX * (this.pyX < 0.0F ? var1 : var3) + this.pyZ * (this.pyZ < 0.0F ? var2 : var4) >= -this.pyW && this.nzX * (this.nzX < 0.0F ? var1 : var3) + this.nzZ * (this.nzZ < 0.0F ? var2 : var4) >= -this.nzW && this.pzX * (this.pzX < 0.0F ? var1 : var3) + this.pzZ * (this.pzZ < 0.0F ? var2 : var4) >= -this.pzW;
   }

   public int intersectAab(Vector3fc var1, Vector3fc var2) {
      return this.intersectAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public int intersectAab(float var1, float var2, float var3, float var4, float var5, float var6) {
      byte var7 = 0;
      boolean var8 = true;
      if (this.nxX * (this.nxX < 0.0F ? var1 : var4) + this.nxY * (this.nxY < 0.0F ? var2 : var5) + this.nxZ * (this.nxZ < 0.0F ? var3 : var6) >= -this.nxW) {
         var7 = 1;
         var8 &= this.nxX * (this.nxX < 0.0F ? var4 : var1) + this.nxY * (this.nxY < 0.0F ? var5 : var2) + this.nxZ * (this.nxZ < 0.0F ? var6 : var3) >= -this.nxW;
         if (this.pxX * (this.pxX < 0.0F ? var1 : var4) + this.pxY * (this.pxY < 0.0F ? var2 : var5) + this.pxZ * (this.pxZ < 0.0F ? var3 : var6) >= -this.pxW) {
            var7 = 2;
            var8 &= this.pxX * (this.pxX < 0.0F ? var4 : var1) + this.pxY * (this.pxY < 0.0F ? var5 : var2) + this.pxZ * (this.pxZ < 0.0F ? var6 : var3) >= -this.pxW;
            if (this.nyX * (this.nyX < 0.0F ? var1 : var4) + this.nyY * (this.nyY < 0.0F ? var2 : var5) + this.nyZ * (this.nyZ < 0.0F ? var3 : var6) >= -this.nyW) {
               var7 = 3;
               var8 &= this.nyX * (this.nyX < 0.0F ? var4 : var1) + this.nyY * (this.nyY < 0.0F ? var5 : var2) + this.nyZ * (this.nyZ < 0.0F ? var6 : var3) >= -this.nyW;
               if (this.pyX * (this.pyX < 0.0F ? var1 : var4) + this.pyY * (this.pyY < 0.0F ? var2 : var5) + this.pyZ * (this.pyZ < 0.0F ? var3 : var6) >= -this.pyW) {
                  var7 = 4;
                  var8 &= this.pyX * (this.pyX < 0.0F ? var4 : var1) + this.pyY * (this.pyY < 0.0F ? var5 : var2) + this.pyZ * (this.pyZ < 0.0F ? var6 : var3) >= -this.pyW;
                  if (this.nzX * (this.nzX < 0.0F ? var1 : var4) + this.nzY * (this.nzY < 0.0F ? var2 : var5) + this.nzZ * (this.nzZ < 0.0F ? var3 : var6) >= -this.nzW) {
                     var7 = 5;
                     var8 &= this.nzX * (this.nzX < 0.0F ? var4 : var1) + this.nzY * (this.nzY < 0.0F ? var5 : var2) + this.nzZ * (this.nzZ < 0.0F ? var6 : var3) >= -this.nzW;
                     if (this.pzX * (this.pzX < 0.0F ? var1 : var4) + this.pzY * (this.pzY < 0.0F ? var2 : var5) + this.pzZ * (this.pzZ < 0.0F ? var3 : var6) >= -this.pzW) {
                        var8 &= this.pzX * (this.pzX < 0.0F ? var4 : var1) + this.pzY * (this.pzY < 0.0F ? var5 : var2) + this.pzZ * (this.pzZ < 0.0F ? var6 : var3) >= -this.pzW;
                        return var8 ? -2 : -1;
                     }
                  }
               }
            }
         }
      }

      return var7;
   }

   public float distanceToPlane(float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      return this.planes[var7].x * (this.planes[var7].x < 0.0F ? var4 : var1) + this.planes[var7].y * (this.planes[var7].y < 0.0F ? var5 : var2) + this.planes[var7].z * (this.planes[var7].z < 0.0F ? var6 : var3) + this.planes[var7].w;
   }

   public int intersectAab(Vector3fc var1, Vector3fc var2, int var3) {
      return this.intersectAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public int intersectAab(float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      byte var8 = 0;
      boolean var9 = true;
      if ((var7 & 1) == 0 || this.nxX * (this.nxX < 0.0F ? var1 : var4) + this.nxY * (this.nxY < 0.0F ? var2 : var5) + this.nxZ * (this.nxZ < 0.0F ? var3 : var6) >= -this.nxW) {
         var8 = 1;
         var9 &= this.nxX * (this.nxX < 0.0F ? var4 : var1) + this.nxY * (this.nxY < 0.0F ? var5 : var2) + this.nxZ * (this.nxZ < 0.0F ? var6 : var3) >= -this.nxW;
         if ((var7 & 2) == 0 || this.pxX * (this.pxX < 0.0F ? var1 : var4) + this.pxY * (this.pxY < 0.0F ? var2 : var5) + this.pxZ * (this.pxZ < 0.0F ? var3 : var6) >= -this.pxW) {
            var8 = 2;
            var9 &= this.pxX * (this.pxX < 0.0F ? var4 : var1) + this.pxY * (this.pxY < 0.0F ? var5 : var2) + this.pxZ * (this.pxZ < 0.0F ? var6 : var3) >= -this.pxW;
            if ((var7 & 4) == 0 || this.nyX * (this.nyX < 0.0F ? var1 : var4) + this.nyY * (this.nyY < 0.0F ? var2 : var5) + this.nyZ * (this.nyZ < 0.0F ? var3 : var6) >= -this.nyW) {
               var8 = 3;
               var9 &= this.nyX * (this.nyX < 0.0F ? var4 : var1) + this.nyY * (this.nyY < 0.0F ? var5 : var2) + this.nyZ * (this.nyZ < 0.0F ? var6 : var3) >= -this.nyW;
               if ((var7 & 8) == 0 || this.pyX * (this.pyX < 0.0F ? var1 : var4) + this.pyY * (this.pyY < 0.0F ? var2 : var5) + this.pyZ * (this.pyZ < 0.0F ? var3 : var6) >= -this.pyW) {
                  var8 = 4;
                  var9 &= this.pyX * (this.pyX < 0.0F ? var4 : var1) + this.pyY * (this.pyY < 0.0F ? var5 : var2) + this.pyZ * (this.pyZ < 0.0F ? var6 : var3) >= -this.pyW;
                  if ((var7 & 16) == 0 || this.nzX * (this.nzX < 0.0F ? var1 : var4) + this.nzY * (this.nzY < 0.0F ? var2 : var5) + this.nzZ * (this.nzZ < 0.0F ? var3 : var6) >= -this.nzW) {
                     var8 = 5;
                     var9 &= this.nzX * (this.nzX < 0.0F ? var4 : var1) + this.nzY * (this.nzY < 0.0F ? var5 : var2) + this.nzZ * (this.nzZ < 0.0F ? var6 : var3) >= -this.nzW;
                     if ((var7 & 32) == 0 || this.pzX * (this.pzX < 0.0F ? var1 : var4) + this.pzY * (this.pzY < 0.0F ? var2 : var5) + this.pzZ * (this.pzZ < 0.0F ? var3 : var6) >= -this.pzW) {
                        var9 &= this.pzX * (this.pzX < 0.0F ? var4 : var1) + this.pzY * (this.pzY < 0.0F ? var5 : var2) + this.pzZ * (this.pzZ < 0.0F ? var6 : var3) >= -this.pzW;
                        return var9 ? -2 : -1;
                     }
                  }
               }
            }
         }
      }

      return var8;
   }

   public int intersectAab(Vector3fc var1, Vector3fc var2, int var3, int var4) {
      return this.intersectAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3, var4);
   }

   public int intersectAab(float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      int var9 = var8;
      boolean var10 = true;
      Vector4f var11 = this.planes[var8];
      if ((var7 & 1 << var8) != 0 && var11.x * (var11.x < 0.0F ? var1 : var4) + var11.y * (var11.y < 0.0F ? var2 : var5) + var11.z * (var11.z < 0.0F ? var3 : var6) < -var11.w) {
         return var8;
      } else {
         if ((var7 & 1) == 0 || this.nxX * (this.nxX < 0.0F ? var1 : var4) + this.nxY * (this.nxY < 0.0F ? var2 : var5) + this.nxZ * (this.nxZ < 0.0F ? var3 : var6) >= -this.nxW) {
            var9 = 1;
            var10 &= this.nxX * (this.nxX < 0.0F ? var4 : var1) + this.nxY * (this.nxY < 0.0F ? var5 : var2) + this.nxZ * (this.nxZ < 0.0F ? var6 : var3) >= -this.nxW;
            if ((var7 & 2) == 0 || this.pxX * (this.pxX < 0.0F ? var1 : var4) + this.pxY * (this.pxY < 0.0F ? var2 : var5) + this.pxZ * (this.pxZ < 0.0F ? var3 : var6) >= -this.pxW) {
               var9 = 2;
               var10 &= this.pxX * (this.pxX < 0.0F ? var4 : var1) + this.pxY * (this.pxY < 0.0F ? var5 : var2) + this.pxZ * (this.pxZ < 0.0F ? var6 : var3) >= -this.pxW;
               if ((var7 & 4) == 0 || this.nyX * (this.nyX < 0.0F ? var1 : var4) + this.nyY * (this.nyY < 0.0F ? var2 : var5) + this.nyZ * (this.nyZ < 0.0F ? var3 : var6) >= -this.nyW) {
                  var9 = 3;
                  var10 &= this.nyX * (this.nyX < 0.0F ? var4 : var1) + this.nyY * (this.nyY < 0.0F ? var5 : var2) + this.nyZ * (this.nyZ < 0.0F ? var6 : var3) >= -this.nyW;
                  if ((var7 & 8) == 0 || this.pyX * (this.pyX < 0.0F ? var1 : var4) + this.pyY * (this.pyY < 0.0F ? var2 : var5) + this.pyZ * (this.pyZ < 0.0F ? var3 : var6) >= -this.pyW) {
                     var9 = 4;
                     var10 &= this.pyX * (this.pyX < 0.0F ? var4 : var1) + this.pyY * (this.pyY < 0.0F ? var5 : var2) + this.pyZ * (this.pyZ < 0.0F ? var6 : var3) >= -this.pyW;
                     if ((var7 & 16) == 0 || this.nzX * (this.nzX < 0.0F ? var1 : var4) + this.nzY * (this.nzY < 0.0F ? var2 : var5) + this.nzZ * (this.nzZ < 0.0F ? var3 : var6) >= -this.nzW) {
                        var9 = 5;
                        var10 &= this.nzX * (this.nzX < 0.0F ? var4 : var1) + this.nzY * (this.nzY < 0.0F ? var5 : var2) + this.nzZ * (this.nzZ < 0.0F ? var6 : var3) >= -this.nzW;
                        if ((var7 & 32) == 0 || this.pzX * (this.pzX < 0.0F ? var1 : var4) + this.pzY * (this.pzY < 0.0F ? var2 : var5) + this.pzZ * (this.pzZ < 0.0F ? var3 : var6) >= -this.pzW) {
                           var10 &= this.pzX * (this.pzX < 0.0F ? var4 : var1) + this.pzY * (this.pzY < 0.0F ? var5 : var2) + this.pzZ * (this.pzZ < 0.0F ? var6 : var3) >= -this.pzW;
                           return var10 ? -2 : -1;
                        }
                     }
                  }
               }
            }
         }

         return var9;
      }
   }
}
