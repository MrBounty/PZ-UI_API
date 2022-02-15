package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.NumberFormat;

public class AxisAngle4f implements Externalizable {
   private static final long serialVersionUID = 1L;
   public float angle;
   public float x;
   public float y;
   public float z;

   public AxisAngle4f() {
      this.z = 1.0F;
   }

   public AxisAngle4f(AxisAngle4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = (float)(((double)var1.angle < 0.0D ? 6.283185307179586D + (double)var1.angle % 6.283185307179586D : (double)var1.angle) % 6.283185307179586D);
   }

   public AxisAngle4f(Quaternionfc var1) {
      float var2 = Math.safeAcos(var1.w());
      float var3 = Math.invsqrt(1.0F - var1.w() * var1.w());
      if (Float.isInfinite(var3)) {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      } else {
         this.x = var1.x() * var3;
         this.y = var1.y() * var3;
         this.z = var1.z() * var3;
      }

      this.angle = var2 + var2;
   }

   public AxisAngle4f(float var1, float var2, float var3, float var4) {
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.angle = (float)(((double)var1 < 0.0D ? 6.283185307179586D + (double)var1 % 6.283185307179586D : (double)var1) % 6.283185307179586D);
   }

   public AxisAngle4f(float var1, Vector3fc var2) {
      this(var1, var2.x(), var2.y(), var2.z());
   }

   public AxisAngle4f set(AxisAngle4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.angle = var1.angle;
      this.angle = (float)(((double)this.angle < 0.0D ? 6.283185307179586D + (double)this.angle % 6.283185307179586D : (double)this.angle) % 6.283185307179586D);
      return this;
   }

   public AxisAngle4f set(AxisAngle4d var1) {
      this.x = (float)var1.x;
      this.y = (float)var1.y;
      this.z = (float)var1.z;
      this.angle = (float)var1.angle;
      this.angle = (float)(((double)this.angle < 0.0D ? 6.283185307179586D + (double)this.angle % 6.283185307179586D : (double)this.angle) % 6.283185307179586D);
      return this;
   }

   public AxisAngle4f set(float var1, float var2, float var3, float var4) {
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.angle = (float)(((double)var1 < 0.0D ? 6.283185307179586D + (double)var1 % 6.283185307179586D : (double)var1) % 6.283185307179586D);
      return this;
   }

   public AxisAngle4f set(float var1, Vector3fc var2) {
      return this.set(var1, var2.x(), var2.y(), var2.z());
   }

   public AxisAngle4f set(Quaternionfc var1) {
      float var2 = Math.safeAcos(var1.w());
      float var3 = Math.invsqrt(1.0F - var1.w() * var1.w());
      if (Float.isInfinite(var3)) {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      } else {
         this.x = var1.x() * var3;
         this.y = var1.y() * var3;
         this.z = var1.z() * var3;
      }

      this.angle = var2 + var2;
      return this;
   }

   public AxisAngle4f set(Quaterniondc var1) {
      double var2 = Math.safeAcos(var1.w());
      double var4 = Math.invsqrt(1.0D - var1.w() * var1.w());
      if (Double.isInfinite(var4)) {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 1.0F;
      } else {
         this.x = (float)(var1.x() * var4);
         this.y = (float)(var1.y() * var4);
         this.z = (float)(var1.z() * var4);
      }

      this.angle = (float)(var2 + var2);
      return this;
   }

   public AxisAngle4f set(Matrix3fc var1) {
      float var2 = var1.m00();
      float var3 = var1.m01();
      float var4 = var1.m02();
      float var5 = var1.m10();
      float var6 = var1.m11();
      float var7 = var1.m12();
      float var8 = var1.m20();
      float var9 = var1.m21();
      float var10 = var1.m22();
      float var11 = Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      float var12 = Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      float var13 = Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var11;
      var3 *= var11;
      var4 *= var11;
      var5 *= var12;
      var6 *= var12;
      var7 *= var12;
      var8 *= var13;
      var9 *= var13;
      var10 *= var13;
      float var14 = 1.0E-4F;
      float var15 = 0.001F;
      float var16;
      if (Math.abs(var5 - var3) < var14 && Math.abs(var8 - var4) < var14 && Math.abs(var9 - var7) < var14) {
         if (Math.abs(var5 + var3) < var15 && Math.abs(var8 + var4) < var15 && Math.abs(var9 + var7) < var15 && Math.abs(var2 + var6 + var10 - 3.0F) < var15) {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 1.0F;
            this.angle = 0.0F;
            return this;
         } else {
            this.angle = 3.1415927F;
            var16 = (var2 + 1.0F) / 2.0F;
            float var17 = (var6 + 1.0F) / 2.0F;
            float var18 = (var10 + 1.0F) / 2.0F;
            float var19 = (var5 + var3) / 4.0F;
            float var20 = (var8 + var4) / 4.0F;
            float var21 = (var9 + var7) / 4.0F;
            if (var16 > var17 && var16 > var18) {
               this.x = Math.sqrt(var16);
               this.y = var19 / this.x;
               this.z = var20 / this.x;
            } else if (var17 > var18) {
               this.y = Math.sqrt(var17);
               this.x = var19 / this.y;
               this.z = var21 / this.y;
            } else {
               this.z = Math.sqrt(var18);
               this.x = var20 / this.z;
               this.y = var21 / this.z;
            }

            return this;
         }
      } else {
         var16 = Math.sqrt((var7 - var9) * (var7 - var9) + (var8 - var4) * (var8 - var4) + (var3 - var5) * (var3 - var5));
         this.angle = Math.safeAcos((var2 + var6 + var10 - 1.0F) / 2.0F);
         this.x = (var7 - var9) / var16;
         this.y = (var8 - var4) / var16;
         this.z = (var3 - var5) / var16;
         return this;
      }
   }

   public AxisAngle4f set(Matrix3dc var1) {
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
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 1.0F;
            this.angle = 0.0F;
            return this;
         } else {
            this.angle = 3.1415927F;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = (float)Math.sqrt(var30);
               this.y = (float)(var36 / (double)this.x);
               this.z = (float)(var38 / (double)this.x);
            } else if (var32 > var34) {
               this.y = (float)Math.sqrt(var32);
               this.x = (float)(var36 / (double)this.y);
               this.z = (float)(var40 / (double)this.y);
            } else {
               this.z = (float)Math.sqrt(var34);
               this.x = (float)(var38 / (double)this.z);
               this.y = (float)(var40 / (double)this.z);
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = (float)Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (float)((var12 - var16) / var30);
         this.y = (float)((var14 - var6) / var30);
         this.z = (float)((var4 - var8) / var30);
         return this;
      }
   }

   public AxisAngle4f set(Matrix4fc var1) {
      float var2 = var1.m00();
      float var3 = var1.m01();
      float var4 = var1.m02();
      float var5 = var1.m10();
      float var6 = var1.m11();
      float var7 = var1.m12();
      float var8 = var1.m20();
      float var9 = var1.m21();
      float var10 = var1.m22();
      float var11 = Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      float var12 = Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      float var13 = Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var11;
      var3 *= var11;
      var4 *= var11;
      var5 *= var12;
      var6 *= var12;
      var7 *= var12;
      var8 *= var13;
      var9 *= var13;
      var10 *= var13;
      float var14 = 1.0E-4F;
      float var15 = 0.001F;
      float var16;
      if (Math.abs(var5 - var3) < var14 && Math.abs(var8 - var4) < var14 && Math.abs(var9 - var7) < var14) {
         if (Math.abs(var5 + var3) < var15 && Math.abs(var8 + var4) < var15 && Math.abs(var9 + var7) < var15 && Math.abs(var2 + var6 + var10 - 3.0F) < var15) {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 1.0F;
            this.angle = 0.0F;
            return this;
         } else {
            this.angle = 3.1415927F;
            var16 = (var2 + 1.0F) / 2.0F;
            float var17 = (var6 + 1.0F) / 2.0F;
            float var18 = (var10 + 1.0F) / 2.0F;
            float var19 = (var5 + var3) / 4.0F;
            float var20 = (var8 + var4) / 4.0F;
            float var21 = (var9 + var7) / 4.0F;
            if (var16 > var17 && var16 > var18) {
               this.x = Math.sqrt(var16);
               this.y = var19 / this.x;
               this.z = var20 / this.x;
            } else if (var17 > var18) {
               this.y = Math.sqrt(var17);
               this.x = var19 / this.y;
               this.z = var21 / this.y;
            } else {
               this.z = Math.sqrt(var18);
               this.x = var20 / this.z;
               this.y = var21 / this.z;
            }

            return this;
         }
      } else {
         var16 = Math.sqrt((var7 - var9) * (var7 - var9) + (var8 - var4) * (var8 - var4) + (var3 - var5) * (var3 - var5));
         this.angle = Math.safeAcos((var2 + var6 + var10 - 1.0F) / 2.0F);
         this.x = (var7 - var9) / var16;
         this.y = (var8 - var4) / var16;
         this.z = (var3 - var5) / var16;
         return this;
      }
   }

   public AxisAngle4f set(Matrix4x3fc var1) {
      float var2 = var1.m00();
      float var3 = var1.m01();
      float var4 = var1.m02();
      float var5 = var1.m10();
      float var6 = var1.m11();
      float var7 = var1.m12();
      float var8 = var1.m20();
      float var9 = var1.m21();
      float var10 = var1.m22();
      float var11 = Math.invsqrt(var1.m00() * var1.m00() + var1.m01() * var1.m01() + var1.m02() * var1.m02());
      float var12 = Math.invsqrt(var1.m10() * var1.m10() + var1.m11() * var1.m11() + var1.m12() * var1.m12());
      float var13 = Math.invsqrt(var1.m20() * var1.m20() + var1.m21() * var1.m21() + var1.m22() * var1.m22());
      var2 *= var11;
      var3 *= var11;
      var4 *= var11;
      var5 *= var12;
      var6 *= var12;
      var7 *= var12;
      var8 *= var13;
      var9 *= var13;
      var10 *= var13;
      float var14 = 1.0E-4F;
      float var15 = 0.001F;
      float var16;
      if (Math.abs(var5 - var3) < var14 && Math.abs(var8 - var4) < var14 && Math.abs(var9 - var7) < var14) {
         if (Math.abs(var5 + var3) < var15 && Math.abs(var8 + var4) < var15 && Math.abs(var9 + var7) < var15 && Math.abs(var2 + var6 + var10 - 3.0F) < var15) {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 1.0F;
            this.angle = 0.0F;
            return this;
         } else {
            this.angle = 3.1415927F;
            var16 = (var2 + 1.0F) / 2.0F;
            float var17 = (var6 + 1.0F) / 2.0F;
            float var18 = (var10 + 1.0F) / 2.0F;
            float var19 = (var5 + var3) / 4.0F;
            float var20 = (var8 + var4) / 4.0F;
            float var21 = (var9 + var7) / 4.0F;
            if (var16 > var17 && var16 > var18) {
               this.x = Math.sqrt(var16);
               this.y = var19 / this.x;
               this.z = var20 / this.x;
            } else if (var17 > var18) {
               this.y = Math.sqrt(var17);
               this.x = var19 / this.y;
               this.z = var21 / this.y;
            } else {
               this.z = Math.sqrt(var18);
               this.x = var20 / this.z;
               this.y = var21 / this.z;
            }

            return this;
         }
      } else {
         var16 = Math.sqrt((var7 - var9) * (var7 - var9) + (var8 - var4) * (var8 - var4) + (var3 - var5) * (var3 - var5));
         this.angle = Math.safeAcos((var2 + var6 + var10 - 1.0F) / 2.0F);
         this.x = (var7 - var9) / var16;
         this.y = (var8 - var4) / var16;
         this.z = (var3 - var5) / var16;
         return this;
      }
   }

   public AxisAngle4f set(Matrix4dc var1) {
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
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 1.0F;
            this.angle = 0.0F;
            return this;
         } else {
            this.angle = 3.1415927F;
            var30 = (var2 + 1.0D) / 2.0D;
            double var32 = (var10 + 1.0D) / 2.0D;
            double var34 = (var18 + 1.0D) / 2.0D;
            double var36 = (var8 + var4) / 4.0D;
            double var38 = (var14 + var6) / 4.0D;
            double var40 = (var16 + var12) / 4.0D;
            if (var30 > var32 && var30 > var34) {
               this.x = (float)Math.sqrt(var30);
               this.y = (float)(var36 / (double)this.x);
               this.z = (float)(var38 / (double)this.x);
            } else if (var32 > var34) {
               this.y = (float)Math.sqrt(var32);
               this.x = (float)(var36 / (double)this.y);
               this.z = (float)(var40 / (double)this.y);
            } else {
               this.z = (float)Math.sqrt(var34);
               this.x = (float)(var38 / (double)this.z);
               this.y = (float)(var40 / (double)this.z);
            }

            return this;
         }
      } else {
         var30 = Math.sqrt((var12 - var16) * (var12 - var16) + (var14 - var6) * (var14 - var6) + (var4 - var8) * (var4 - var8));
         this.angle = (float)Math.safeAcos((var2 + var10 + var18 - 1.0D) / 2.0D);
         this.x = (float)((var12 - var16) / var30);
         this.y = (float)((var14 - var6) / var30);
         this.z = (float)((var4 - var8) / var30);
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
      var1.writeFloat(this.angle);
      var1.writeFloat(this.x);
      var1.writeFloat(this.y);
      var1.writeFloat(this.z);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.angle = var1.readFloat();
      this.x = var1.readFloat();
      this.y = var1.readFloat();
      this.z = var1.readFloat();
   }

   public AxisAngle4f normalize() {
      float var1 = Math.invsqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public AxisAngle4f rotate(float var1) {
      this.angle += var1;
      this.angle = (float)(((double)this.angle < 0.0D ? 6.283185307179586D + (double)this.angle % 6.283185307179586D : (double)this.angle) % 6.283185307179586D);
      return this;
   }

   public Vector3f transform(Vector3f var1) {
      return this.transform((Vector3fc)var1, (Vector3f)var1);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      double var3 = (double)Math.sin(this.angle);
      double var5 = Math.cosFromSin(var3, (double)this.angle);
      float var7 = this.x * var1.x() + this.y * var1.y() + this.z * var1.z();
      var2.set((float)((double)var1.x() * var5 + var3 * (double)(this.y * var1.z() - this.z * var1.y()) + (1.0D - var5) * (double)var7 * (double)this.x), (float)((double)var1.y() * var5 + var3 * (double)(this.z * var1.x() - this.x * var1.z()) + (1.0D - var5) * (double)var7 * (double)this.y), (float)((double)var1.z() * var5 + var3 * (double)(this.x * var1.y() - this.y * var1.x()) + (1.0D - var5) * (double)var7 * (double)this.z));
      return var2;
   }

   public Vector4f transform(Vector4f var1) {
      return this.transform((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector4f transform(Vector4fc var1, Vector4f var2) {
      double var3 = (double)Math.sin(this.angle);
      double var5 = Math.cosFromSin(var3, (double)this.angle);
      float var7 = this.x * var1.x() + this.y * var1.y() + this.z * var1.z();
      var2.set((float)((double)var1.x() * var5 + var3 * (double)(this.y * var1.z() - this.z * var1.y()) + (1.0D - var5) * (double)var7 * (double)this.x), (float)((double)var1.y() * var5 + var3 * (double)(this.z * var1.x() - this.x * var1.z()) + (1.0D - var5) * (double)var7 * (double)this.y), (float)((double)var1.z() * var5 + var3 * (double)(this.x * var1.y() - this.y * var1.x()) + (1.0D - var5) * (double)var7 * (double)this.z), var2.w);
      return var2;
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format((double)this.x, var1);
      return "(" + var10000 + " " + Runtime.format((double)this.y, var1) + " " + Runtime.format((double)this.z, var1) + " <| " + Runtime.format((double)this.angle, var1) + ")";
   }

   public int hashCode() {
      byte var1 = 1;
      float var2 = (float)(((double)this.angle < 0.0D ? 6.283185307179586D + (double)this.angle % 6.283185307179586D : (double)this.angle) % 6.283185307179586D);
      int var3 = 31 * var1 + Float.floatToIntBits(var2);
      var3 = 31 * var3 + Float.floatToIntBits(this.x);
      var3 = 31 * var3 + Float.floatToIntBits(this.y);
      var3 = 31 * var3 + Float.floatToIntBits(this.z);
      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AxisAngle4f var2 = (AxisAngle4f)var1;
         float var3 = (float)(((double)this.angle < 0.0D ? 6.283185307179586D + (double)this.angle % 6.283185307179586D : (double)this.angle) % 6.283185307179586D);
         float var4 = (float)(((double)var2.angle < 0.0D ? 6.283185307179586D + (double)var2.angle % 6.283185307179586D : (double)var2.angle) % 6.283185307179586D);
         if (Float.floatToIntBits(var3) != Float.floatToIntBits(var4)) {
            return false;
         } else if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var2.x)) {
            return false;
         } else if (Float.floatToIntBits(this.y) != Float.floatToIntBits(var2.y)) {
            return false;
         } else {
            return Float.floatToIntBits(this.z) == Float.floatToIntBits(var2.z);
         }
      }
   }
}
