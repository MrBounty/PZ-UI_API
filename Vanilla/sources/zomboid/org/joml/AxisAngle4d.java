package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.NumberFormat;

public class AxisAngle4d implements Externalizable {
   private static final long serialVersionUID = 1L;
   public double angle;
   public double x;
   public double y;
   public double z;

   public AxisAngle4d() {
      this.z = 1.0D;
   }

   public AxisAngle4d(AxisAngle4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = (var1.angle < 0.0D ? 6.283185307179586D + var1.angle % 6.283185307179586D : var1.angle) % 6.283185307179586D;
   }

   public AxisAngle4d(AxisAngle4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.angle = ((double)var1.angle < 0.0D ? 6.283185307179586D + (double)var1.angle % 6.283185307179586D : (double)var1.angle) % 6.283185307179586D;
   }

   public AxisAngle4d(Quaternionfc var1) {
      float var2 = Math.safeAcos(var1.w());
      float var3 = Math.invsqrt(1.0F - var1.w() * var1.w());
      if (Float.isInfinite(var3)) {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      } else {
         this.x = (double)(var1.x() * var3);
         this.y = (double)(var1.y() * var3);
         this.z = (double)(var1.z() * var3);
      }

      this.angle = (double)(var2 + var2);
   }

   public AxisAngle4d(Quaterniondc var1) {
      double var2 = Math.safeAcos(var1.w());
      double var4 = Math.invsqrt(1.0D - var1.w() * var1.w());
      if (Double.isInfinite(var4)) {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      } else {
         this.x = var1.x() * var4;
         this.y = var1.y() * var4;
         this.z = var1.z() * var4;
      }

      this.angle = var2 + var2;
   }

   public AxisAngle4d(double var1, double var3, double var5, double var7) {
      this.x = var3;
      this.y = var5;
      this.z = var7;
      this.angle = (var1 < 0.0D ? 6.283185307179586D + var1 % 6.283185307179586D : var1) % 6.283185307179586D;
   }

   public AxisAngle4d(double var1, Vector3dc var3) {
      this(var1, var3.x(), var3.y(), var3.z());
   }

   public AxisAngle4d(double var1, Vector3f var3) {
      this(var1, (double)var3.x, (double)var3.y, (double)var3.z);
   }

   public AxisAngle4d set(AxisAngle4d var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = (var1.angle < 0.0D ? 6.283185307179586D + var1.angle % 6.283185307179586D : var1.angle) % 6.283185307179586D;
      return this;
   }

   public AxisAngle4d set(AxisAngle4f var1) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.z = (double)var1.z;
      this.angle = ((double)var1.angle < 0.0D ? 6.283185307179586D + (double)var1.angle % 6.283185307179586D : (double)var1.angle) % 6.283185307179586D;
      return this;
   }

   public AxisAngle4d set(double var1, double var3, double var5, double var7) {
      this.x = var3;
      this.y = var5;
      this.z = var7;
      this.angle = (var1 < 0.0D ? 6.283185307179586D + var1 % 6.283185307179586D : var1) % 6.283185307179586D;
      return this;
   }

   public AxisAngle4d set(double var1, Vector3dc var3) {
      return this.set(var1, var3.x(), var3.y(), var3.z());
   }

   public AxisAngle4d set(double var1, Vector3f var3) {
      return this.set(var1, (double)var3.x, (double)var3.y, (double)var3.z);
   }

   public AxisAngle4d set(Quaternionfc var1) {
      float var2 = Math.safeAcos(var1.w());
      float var3 = Math.invsqrt(1.0F - var1.w() * var1.w());
      if (Float.isInfinite(var3)) {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      } else {
         this.x = (double)(var1.x() * var3);
         this.y = (double)(var1.y() * var3);
         this.z = (double)(var1.z() * var3);
      }

      this.angle = (double)(var2 + var2);
      return this;
   }

   public AxisAngle4d set(Quaterniondc var1) {
      double var2 = Math.safeAcos(var1.w());
      double var4 = Math.invsqrt(1.0D - var1.w() * var1.w());
      if (Double.isInfinite(var4)) {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 1.0D;
      } else {
         this.x = var1.x() * var4;
         this.y = var1.y() * var4;
         this.z = var1.z() * var4;
      }

      this.angle = var2 + var2;
      return this;
   }

   public AxisAngle4d set(Matrix3fc var1) {
      double var2 = (double)var1.m00();
      double var4 = (double)var1.m01();
      double var6 = (double)var1.m02();
      double var8 = (double)var1.m10();
      double var10 = (double)var1.m11();
      double var12 = (double)var1.m12();
      double var14 = (double)var1.m20();
      double var16 = (double)var1.m21();
      double var18 = (double)var1.m22();
      double var20 = (double)Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      double var22 = (double)Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      double var24 = (double)Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var20;
      var4 *= var20;
      var6 *= var20;
      var8 *= var22;
      var10 *= var22;
      var12 *= var22;
      var14 *= var24;
      var16 *= var24;
      var18 *= var24;
      double var26 = 1.0E-4D;
      double var28 = 0.001D;
      double var30;
      if (Math.abs(var8 - var4) < var26 && Math.abs(var14 - var6) < var26 && Math.abs(var16 - var12) < var26) {
         if (Math.abs(var8 + var4) < var28 && Math.abs(var14 + var6) < var28 && Math.abs(var16 + var12) < var28 && Math.abs(var2 + var10 + var18 - 3.0D) < var28) {
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 1.0D;
            this.angle = 0.0D;
            return this;
         } else {
            this.angle = 3.141592653589793D;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = Math.sqrt(var30);
               this.y = var36 / this.x;
               this.z = var38 / this.x;
            } else if (var32 > var34) {
               this.y = Math.sqrt(var32);
               this.x = var36 / this.y;
               this.z = var40 / this.y;
            } else {
               this.z = Math.sqrt(var34);
               this.x = var38 / this.z;
               this.y = var40 / this.z;
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (var12 - var16) / var30;
         this.y = (var14 - var6) / var30;
         this.z = (var4 - var8) / var30;
         return this;
      }
   }

   public AxisAngle4d set(Matrix3dc var1) {
      double var2 = var1.m00();
      double var4 = var1.m01();
      double var6 = var1.m02();
      double var8 = var1.m10();
      double var10 = var1.m11();
      double var12 = var1.m12();
      double var14 = var1.m20();
      double var16 = var1.m21();
      double var18 = var1.m22();
      double var20 = Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      double var22 = Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      double var24 = Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var20;
      var4 *= var20;
      var6 *= var20;
      var8 *= var22;
      var10 *= var22;
      var12 *= var22;
      var14 *= var24;
      var16 *= var24;
      var18 *= var24;
      double var26 = 1.0E-4D;
      double var28 = 0.001D;
      double var30;
      if (Math.abs(var8 - var4) < var26 && Math.abs(var14 - var6) < var26 && Math.abs(var16 - var12) < var26) {
         if (Math.abs(var8 + var4) < var28 && Math.abs(var14 + var6) < var28 && Math.abs(var16 + var12) < var28 && Math.abs(var2 + var10 + var18 - 3.0D) < var28) {
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 1.0D;
            this.angle = 0.0D;
            return this;
         } else {
            this.angle = 3.141592653589793D;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = Math.sqrt(var30);
               this.y = var36 / this.x;
               this.z = var38 / this.x;
            } else if (var32 > var34) {
               this.y = Math.sqrt(var32);
               this.x = var36 / this.y;
               this.z = var40 / this.y;
            } else {
               this.z = Math.sqrt(var34);
               this.x = var38 / this.z;
               this.y = var40 / this.z;
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (var12 - var16) / var30;
         this.y = (var14 - var6) / var30;
         this.z = (var4 - var8) / var30;
         return this;
      }
   }

   public AxisAngle4d set(Matrix4fc var1) {
      double var2 = (double)var1.m00();
      double var4 = (double)var1.m01();
      double var6 = (double)var1.m02();
      double var8 = (double)var1.m10();
      double var10 = (double)var1.m11();
      double var12 = (double)var1.m12();
      double var14 = (double)var1.m20();
      double var16 = (double)var1.m21();
      double var18 = (double)var1.m22();
      double var20 = (double)Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      double var22 = (double)Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      double var24 = (double)Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var20;
      var4 *= var20;
      var6 *= var20;
      var8 *= var22;
      var10 *= var22;
      var12 *= var22;
      var14 *= var24;
      var16 *= var24;
      var18 *= var24;
      double var26 = 1.0E-4D;
      double var28 = 0.001D;
      double var30;
      if (Math.abs(var8 - var4) < var26 && Math.abs(var14 - var6) < var26 && Math.abs(var16 - var12) < var26) {
         if (Math.abs(var8 + var4) < var28 && Math.abs(var14 + var6) < var28 && Math.abs(var16 + var12) < var28 && Math.abs(var2 + var10 + var18 - 3.0D) < var28) {
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 1.0D;
            this.angle = 0.0D;
            return this;
         } else {
            this.angle = 3.141592653589793D;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = Math.sqrt(var30);
               this.y = var36 / this.x;
               this.z = var38 / this.x;
            } else if (var32 > var34) {
               this.y = Math.sqrt(var32);
               this.x = var36 / this.y;
               this.z = var40 / this.y;
            } else {
               this.z = Math.sqrt(var34);
               this.x = var38 / this.z;
               this.y = var40 / this.z;
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (var12 - var16) / var30;
         this.y = (var14 - var6) / var30;
         this.z = (var4 - var8) / var30;
         return this;
      }
   }

   public AxisAngle4d set(Matrix4x3fc var1) {
      double var2 = (double)var1.m00();
      double var4 = (double)var1.m01();
      double var6 = (double)var1.m02();
      double var8 = (double)var1.m10();
      double var10 = (double)var1.m11();
      double var12 = (double)var1.m12();
      double var14 = (double)var1.m20();
      double var16 = (double)var1.m21();
      double var18 = (double)var1.m22();
      double var20 = (double)Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      double var22 = (double)Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      double var24 = (double)Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var20;
      var4 *= var20;
      var6 *= var20;
      var8 *= var22;
      var10 *= var22;
      var12 *= var22;
      var14 *= var24;
      var16 *= var24;
      var18 *= var24;
      double var26 = 1.0E-4D;
      double var28 = 0.001D;
      double var30;
      if (Math.abs(var8 - var4) < var26 && Math.abs(var14 - var6) < var26 && Math.abs(var16 - var12) < var26) {
         if (Math.abs(var8 + var4) < var28 && Math.abs(var14 + var6) < var28 && Math.abs(var16 + var12) < var28 && Math.abs(var2 + var10 + var18 - 3.0D) < var28) {
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 1.0D;
            this.angle = 0.0D;
            return this;
         } else {
            this.angle = 3.141592653589793D;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = Math.sqrt(var30);
               this.y = var36 / this.x;
               this.z = var38 / this.x;
            } else if (var32 > var34) {
               this.y = Math.sqrt(var32);
               this.x = var36 / this.y;
               this.z = var40 / this.y;
            } else {
               this.z = Math.sqrt(var34);
               this.x = var38 / this.z;
               this.y = var40 / this.z;
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (var12 - var16) / var30;
         this.y = (var14 - var6) / var30;
         this.z = (var4 - var8) / var30;
         return this;
      }
   }

   public AxisAngle4d set(Matrix4dc var1) {
      double var2 = var1.m00();
      double var4 = var1.m01();
      double var6 = var1.m02();
      double var8 = var1.m10();
      double var10 = var1.m11();
      double var12 = var1.m12();
      double var14 = var1.m20();
      double var16 = var1.m21();
      double var18 = var1.m22();
      double var20 = Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      double var22 = Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      double var24 = Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var20;
      var4 *= var20;
      var6 *= var20;
      var8 *= var22;
      var10 *= var22;
      var12 *= var22;
      var14 *= var24;
      var16 *= var24;
      var18 *= var24;
      double var26 = 1.0E-4D;
      double var28 = 0.001D;
      double var30;
      if (Math.abs(var8 - var4) < var26 && Math.abs(var14 - var6) < var26 && Math.abs(var16 - var12) < var26) {
         if (Math.abs(var8 + var4) < var28 && Math.abs(var14 + var6) < var28 && Math.abs(var16 + var12) < var28 && Math.abs(var2 + var10 + var18 - 3.0D) < var28) {
            this.x = 0.0D;
            this.y = 0.0D;
            this.z = 1.0D;
            this.angle = 0.0D;
            return this;
         } else {
            this.angle = 3.141592653589793D;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = Math.sqrt(var30);
               this.y = var36 / this.x;
               this.z = var38 / this.x;
            } else if (var32 > var34) {
               this.y = Math.sqrt(var32);
               this.x = var36 / this.y;
               this.z = var40 / this.y;
            } else {
               this.z = Math.sqrt(var34);
               this.x = var38 / this.z;
               this.y = var40 / this.z;
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (var12 - var16) / var30;
         this.y = (var14 - var6) / var30;
         this.z = (var4 - var8) / var30;
         return this;
      }
   }

   public Quaternionf get(Quaternionf var1) {
      return var1.set(this);
   }

   public Quaterniond get(Quaterniond var1) {
      return var1.set(this);
   }

   public Matrix4f get(Matrix4f var1) {
      return var1.set(this);
   }

   public Matrix3f get(Matrix3f var1) {
      return var1.set(this);
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set(this);
   }

   public Matrix3d get(Matrix3d var1) {
      return var1.set(this);
   }

   public AxisAngle4d get(AxisAngle4d var1) {
      return var1.set(this);
   }

   public AxisAngle4f get(AxisAngle4f var1) {
      return var1.set(this);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.angle);
      var1.writeDouble(this.x);
      var1.writeDouble(this.y);
      var1.writeDouble(this.z);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.angle = var1.readDouble();
      this.x = var1.readDouble();
      this.y = var1.readDouble();
      this.z = var1.readDouble();
   }

   public AxisAngle4d normalize() {
      double var1 = Math.invsqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public AxisAngle4d rotate(double var1) {
      this.angle += var1;
      this.angle = (this.angle < 0.0D ? 6.283185307179586D + this.angle % 6.283185307179586D : this.angle) % 6.283185307179586D;
      return this;
   }

   public Vector3d transform(Vector3d var1) {
      return this.transform((Vector3dc)var1, (Vector3d)var1);
   }

   public Vector3d transform(Vector3dc var1, Vector3d var2) {
      double var3 = Math.sin(this.angle);
      double var5 = Math.cosFromSin(var3, this.angle);
      double var7 = this.x * var1.x() + this.y * var1.y() + this.z * var1.z();
      var2.set(var1.x() * var5 + var3 * (this.y * var1.z() - this.z * var1.y()) + (1.0D - var5) * var7 * this.x, var1.y() * var5 + var3 * (this.z * var1.x() - this.x * var1.z()) + (1.0D - var5) * var7 * this.y, var1.z() * var5 + var3 * (this.x * var1.y() - this.y * var1.x()) + (1.0D - var5) * var7 * this.z);
      return var2;
   }

   public Vector3f transform(Vector3f var1) {
      return this.transform((Vector3fc)var1, (Vector3f)var1);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      double var3 = Math.sin(this.angle);
      double var5 = Math.cosFromSin(var3, this.angle);
      double var7 = this.x * (double)var1.x() + this.y * (double)var1.y() + this.z * (double)var1.z();
      var2.set((float)((double)var1.x() * var5 + var3 * (this.y * (double)var1.z() - this.z * (double)var1.y()) + (1.0D - var5) * var7 * this.x), (float)((double)var1.y() * var5 + var3 * (this.z * (double)var1.x() - this.x * (double)var1.z()) + (1.0D - var5) * var7 * this.y), (float)((double)var1.z() * var5 + var3 * (this.x * (double)var1.y() - this.y * (double)var1.x()) + (1.0D - var5) * var7 * this.z));
      return var2;
   }

   public Vector4d transform(Vector4d var1) {
      return this.transform((Vector4dc)var1, (Vector4d)var1);
   }

   public Vector4d transform(Vector4dc var1, Vector4d var2) {
      double var3 = Math.sin(this.angle);
      double var5 = Math.cosFromSin(var3, this.angle);
      double var7 = this.x * var1.x() + this.y * var1.y() + this.z * var1.z();
      var2.set(var1.x() * var5 + var3 * (this.y * var1.z() - this.z * var1.y()) + (1.0D - var5) * var7 * this.x, var1.y() * var5 + var3 * (this.z * var1.x() - this.x * var1.z()) + (1.0D - var5) * var7 * this.y, var1.z() * var5 + var3 * (this.x * var1.y() - this.y * var1.x()) + (1.0D - var5) * var7 * this.z, var2.w);
      return var2;
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format(this.x, var1);
      return "(" + var10000 + " " + Runtime.format(this.y, var1) + " " + Runtime.format(this.z, var1) + " <| " + Runtime.format(this.angle, var1) + ")";
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits((this.angle < 0.0D ? 6.283185307179586D + this.angle % 6.283185307179586D : this.angle) % 6.283185307179586D);
      int var4 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.x);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.y);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.z);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      return var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AxisAngle4d var2 = (AxisAngle4d)var1;
         if (Double.doubleToLongBits((this.angle < 0.0D ? 6.283185307179586D + this.angle % 6.283185307179586D : this.angle) % 6.283185307179586D) != Double.doubleToLongBits((var2.angle < 0.0D ? 6.283185307179586D + var2.angle % 6.283185307179586D : var2.angle) % 6.283185307179586D)) {
            return false;
         } else if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var2.x)) {
            return false;
         } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(var2.y)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(var2.z);
         }
      }
   }
}
