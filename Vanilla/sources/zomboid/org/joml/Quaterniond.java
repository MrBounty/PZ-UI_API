package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.NumberFormat;

public class Quaterniond implements Externalizable, Quaterniondc {
   private static final long serialVersionUID = 1L;
   public double x;
   public double y;
   public double z;
   public double w;

   public Quaterniond() {
      this.w = 1.0D;
   }

   public Quaterniond(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public Quaterniond(Quaterniondc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
   }

   public Quaterniond(Quaternionfc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
   }

   public Quaterniond(AxisAngle4f var1) {
      double var2 = Math.sin((double)var1.angle * 0.5D);
      this.x = (double)var1.x * var2;
      this.y = (double)var1.y * var2;
      this.z = (double)var1.z * var2;
      this.w = Math.cosFromSin(var2, (double)var1.angle * 0.5D);
   }

   public Quaterniond(AxisAngle4d var1) {
      double var2 = Math.sin(var1.angle * 0.5D);
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = Math.cosFromSin(var2, var1.angle * 0.5D);
   }

   public double x() {
      return this.x;
   }

   public double y() {
      return this.y;
   }

   public double z() {
      return this.z;
   }

   public double w() {
      return this.w;
   }

   public Quaterniond normalize() {
      double var1 = Math.invsqrt(this.lengthSquared());
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Quaterniond normalize(Quaterniond var1) {
      double var2 = Math.invsqrt(this.lengthSquared());
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Quaterniond add(double var1, double var3, double var5, double var7) {
      return this.add(var1, var3, var5, var7, this);
   }

   public Quaterniond add(double var1, double var3, double var5, double var7, Quaterniond var9) {
      var9.x = this.x + var1;
      var9.y = this.y + var3;
      var9.z = this.z + var5;
      var9.w = this.w + var7;
      return var9;
   }

   public Quaterniond add(Quaterniondc var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      this.w += var1.w();
      return this;
   }

   public Quaterniond add(Quaterniondc var1, Quaterniond var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      var2.w = this.w + var1.w();
      return var2;
   }

   public double dot(Quaterniondc var1) {
      return this.x * var1.x() + this.y * var1.y() + this.z * var1.z() + this.w * var1.w();
   }

   public double angle() {
      return 2.0D * Math.safeAcos(this.w);
   }

   public Matrix3d get(Matrix3d var1) {
      return var1.set((Quaterniondc)this);
   }

   public Matrix3f get(Matrix3f var1) {
      return var1.set((Quaterniondc)this);
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set((Quaterniondc)this);
   }

   public Matrix4f get(Matrix4f var1) {
      return var1.set((Quaterniondc)this);
   }

   public AxisAngle4f get(AxisAngle4f var1) {
      double var2 = this.x;
      double var4 = this.y;
      double var6 = this.z;
      double var8 = this.w;
      double var10;
      if (var8 > 1.0D) {
         var10 = Math.invsqrt(this.lengthSquared());
         var2 *= var10;
         var4 *= var10;
         var6 *= var10;
         var8 *= var10;
      }

      var1.angle = (float)(2.0D * Math.acos(var8));
      var10 = Math.sqrt(1.0D - var8 * var8);
      if (var10 < 0.001D) {
         var1.x = (float)var2;
         var1.y = (float)var4;
         var1.z = (float)var6;
      } else {
         var10 = 1.0D / var10;
         var1.x = (float)(var2 * var10);
         var1.y = (float)(var4 * var10);
         var1.z = (float)(var6 * var10);
      }

      return var1;
   }

   public AxisAngle4d get(AxisAngle4d var1) {
      double var2 = this.x;
      double var4 = this.y;
      double var6 = this.z;
      double var8 = this.w;
      double var10;
      if (var8 > 1.0D) {
         var10 = Math.invsqrt(this.lengthSquared());
         var2 *= var10;
         var4 *= var10;
         var6 *= var10;
         var8 *= var10;
      }

      var1.angle = 2.0D * Math.acos(var8);
      var10 = Math.sqrt(1.0D - var8 * var8);
      if (var10 < 0.001D) {
         var1.x = var2;
         var1.y = var4;
         var1.z = var6;
      } else {
         var10 = 1.0D / var10;
         var1.x = var2 * var10;
         var1.y = var4 * var10;
         var1.z = var6 * var10;
      }

      return var1;
   }

   public Quaterniond get(Quaterniond var1) {
      return var1.set((Quaterniondc)this);
   }

   public Quaternionf get(Quaternionf var1) {
      return var1.set((Quaterniondc)this);
   }

   public Quaterniond set(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
      return this;
   }

   public Quaterniond set(Quaterniondc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
      return this;
   }

   public Quaterniond set(Quaternionfc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
      return this;
   }

   public Quaterniond set(AxisAngle4f var1) {
      return this.setAngleAxis((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Quaterniond set(AxisAngle4d var1) {
      return this.setAngleAxis(var1.angle, var1.x, var1.y, var1.z);
   }

   public Quaterniond setAngleAxis(double var1, double var3, double var5, double var7) {
      double var9 = Math.sin(var1 * 0.5D);
      this.x = var3 * var9;
      this.y = var5 * var9;
      this.z = var7 * var9;
      this.w = Math.cosFromSin(var9, var1 * 0.5D);
      return this;
   }

   public Quaterniond setAngleAxis(double var1, Vector3dc var3) {
      return this.setAngleAxis(var1, var3.x(), var3.y(), var3.z());
   }

   private void setFromUnnormalized(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var37 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var39 = Math.invsqrt(var7 * var7 + var9 * var9 + var11 * var11);
      double var41 = Math.invsqrt(var13 * var13 + var15 * var15 + var17 * var17);
      double var19 = var1 * var37;
      double var21 = var3 * var37;
      double var23 = var5 * var37;
      double var25 = var7 * var39;
      double var27 = var9 * var39;
      double var29 = var11 * var39;
      double var31 = var13 * var41;
      double var33 = var15 * var41;
      double var35 = var17 * var41;
      this.setFromNormalized(var19, var21, var23, var25, var27, var29, var31, var33, var35);
   }

   private void setFromNormalized(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var21 = var1 + var9 + var17;
      double var19;
      if (var21 >= 0.0D) {
         var19 = Math.sqrt(var21 + 1.0D);
         this.w = var19 * 0.5D;
         var19 = 0.5D / var19;
         this.x = (var11 - var15) * var19;
         this.y = (var13 - var5) * var19;
         this.z = (var3 - var7) * var19;
      } else if (var1 >= var9 && var1 >= var17) {
         var19 = Math.sqrt(var1 - (var9 + var17) + 1.0D);
         this.x = var19 * 0.5D;
         var19 = 0.5D / var19;
         this.y = (var7 + var3) * var19;
         this.z = (var5 + var13) * var19;
         this.w = (var11 - var15) * var19;
      } else if (var9 > var17) {
         var19 = Math.sqrt(var9 - (var17 + var1) + 1.0D);
         this.y = var19 * 0.5D;
         var19 = 0.5D / var19;
         this.z = (var15 + var11) * var19;
         this.x = (var7 + var3) * var19;
         this.w = (var13 - var5) * var19;
      } else {
         var19 = Math.sqrt(var17 - (var1 + var9) + 1.0D);
         this.z = var19 * 0.5D;
         var19 = 0.5D / var19;
         this.x = (var5 + var13) * var19;
         this.y = (var15 + var11) * var19;
         this.w = (var3 - var7) * var19;
      }

   }

   public Quaterniond setFromUnnormalized(Matrix4fc var1) {
      this.setFromUnnormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromUnnormalized(Matrix4x3fc var1) {
      this.setFromUnnormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromUnnormalized(Matrix4x3dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix4fc var1) {
      this.setFromNormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix4x3fc var1) {
      this.setFromNormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix4x3dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond setFromUnnormalized(Matrix4dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix4dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond setFromUnnormalized(Matrix3fc var1) {
      this.setFromUnnormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix3fc var1) {
      this.setFromNormalized((double)var1.m00(), (double)var1.m01(), (double)var1.m02(), (double)var1.m10(), (double)var1.m11(), (double)var1.m12(), (double)var1.m20(), (double)var1.m21(), (double)var1.m22());
      return this;
   }

   public Quaterniond setFromUnnormalized(Matrix3dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond setFromNormalized(Matrix3dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaterniond fromAxisAngleRad(Vector3dc var1, double var2) {
      return this.fromAxisAngleRad(var1.x(), var1.y(), var1.z(), var2);
   }

   public Quaterniond fromAxisAngleRad(double var1, double var3, double var5, double var7) {
      double var9 = var7 / 2.0D;
      double var11 = Math.sin(var9);
      double var13 = Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      this.x = var1 / var13 * var11;
      this.y = var3 / var13 * var11;
      this.z = var5 / var13 * var11;
      this.w = Math.cosFromSin(var11, var9);
      return this;
   }

   public Quaterniond fromAxisAngleDeg(Vector3dc var1, double var2) {
      return this.fromAxisAngleRad(var1.x(), var1.y(), var1.z(), Math.toRadians(var2));
   }

   public Quaterniond fromAxisAngleDeg(double var1, double var3, double var5, double var7) {
      return this.fromAxisAngleRad(var1, var3, var5, Math.toRadians(var7));
   }

   public Quaterniond mul(Quaterniondc var1) {
      return this.mul(var1, this);
   }

   public Quaterniond mul(Quaterniondc var1, Quaterniond var2) {
      return this.mul(var1.x(), var1.y(), var1.z(), var1.w(), var2);
   }

   public Quaterniond mul(double var1, double var3, double var5, double var7) {
      return this.mul(var1, var3, var5, var7, this);
   }

   public Quaterniond mul(double var1, double var3, double var5, double var7, Quaterniond var9) {
      return var9.set(Math.fma(this.w, var1, Math.fma(this.x, var7, Math.fma(this.y, var5, -this.z * var3))), Math.fma(this.w, var3, Math.fma(-this.x, var5, Math.fma(this.y, var7, this.z * var1))), Math.fma(this.w, var5, Math.fma(this.x, var3, Math.fma(-this.y, var1, this.z * var7))), Math.fma(this.w, var7, Math.fma(-this.x, var1, Math.fma(-this.y, var3, -this.z * var5))));
   }

   public Quaterniond premul(Quaterniondc var1) {
      return this.premul(var1, this);
   }

   public Quaterniond premul(Quaterniondc var1, Quaterniond var2) {
      return this.premul(var1.x(), var1.y(), var1.z(), var1.w(), var2);
   }

   public Quaterniond premul(double var1, double var3, double var5, double var7) {
      return this.premul(var1, var3, var5, var7, this);
   }

   public Quaterniond premul(double var1, double var3, double var5, double var7, Quaterniond var9) {
      return var9.set(Math.fma(var7, this.x, Math.fma(var1, this.w, Math.fma(var3, this.z, -var5 * this.y))), Math.fma(var7, this.y, Math.fma(-var1, this.z, Math.fma(var3, this.w, var5 * this.x))), Math.fma(var7, this.z, Math.fma(var1, this.y, Math.fma(-var3, this.x, var5 * this.w))), Math.fma(var7, this.w, Math.fma(-var1, this.x, Math.fma(-var3, this.y, -var5 * this.z))));
   }

   public Vector3d transform(Vector3d var1) {
      return this.transform(var1.x, var1.y, var1.z, var1);
   }

   public Vector3d transformInverse(Vector3d var1) {
      return this.transformInverse(var1.x, var1.y, var1.z, var1);
   }

   public Vector3d transformUnit(Vector3d var1) {
      return this.transformUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector3d transformInverseUnit(Vector3d var1) {
      return this.transformInverseUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector3d transformPositiveX(Vector3d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.x * this.z;
      double var16 = this.y * this.w;
      var1.x = var2 + var4 - var8 - var6;
      var1.y = var12 + var10 + var10 + var12;
      var1.z = var14 - var16 + var14 - var16;
      return var1;
   }

   public Vector4d transformPositiveX(Vector4d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.x * this.z;
      double var16 = this.y * this.w;
      var1.x = var2 + var4 - var8 - var6;
      var1.y = var12 + var10 + var10 + var12;
      var1.z = var14 - var16 + var14 - var16;
      return var1;
   }

   public Vector3d transformUnitPositiveX(Vector3d var1) {
      double var2 = this.y * this.y;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.x * this.z;
      double var10 = this.y * this.w;
      double var12 = this.z * this.w;
      var1.x = 1.0D - var2 - var2 - var4 - var4;
      var1.y = var6 + var12 + var6 + var12;
      var1.z = var8 - var10 + var8 - var10;
      return var1;
   }

   public Vector4d transformUnitPositiveX(Vector4d var1) {
      double var2 = this.y * this.y;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.x * this.z;
      double var10 = this.y * this.w;
      double var12 = this.z * this.w;
      var1.x = 1.0D - var2 - var2 - var4 - var4;
      var1.y = var6 + var12 + var6 + var12;
      var1.z = var8 - var10 + var8 - var10;
      return var1;
   }

   public Vector3d transformPositiveY(Vector3d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = -var10 + var12 - var10 + var12;
      var1.y = var6 - var8 + var2 - var4;
      var1.z = var14 + var14 + var16 + var16;
      return var1;
   }

   public Vector4d transformPositiveY(Vector4d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = -var10 + var12 - var10 + var12;
      var1.y = var6 - var8 + var2 - var4;
      var1.z = var14 + var14 + var16 + var16;
      return var1;
   }

   public Vector4d transformUnitPositiveY(Vector4d var1) {
      double var2 = this.x * this.x;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.z * this.w;
      var1.x = var6 - var12 + var6 - var12;
      var1.y = 1.0D - var2 - var2 - var4 - var4;
      var1.z = var8 + var8 + var10 + var10;
      return var1;
   }

   public Vector3d transformUnitPositiveY(Vector3d var1) {
      double var2 = this.x * this.x;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.z * this.w;
      var1.x = var6 - var12 + var6 - var12;
      var1.y = 1.0D - var2 - var2 - var4 - var4;
      var1.z = var8 + var8 + var10 + var10;
      return var1;
   }

   public Vector3d transformPositiveZ(Vector3d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.x * this.z;
      double var12 = this.y * this.w;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = var12 + var10 + var10 + var12;
      var1.y = var14 + var14 - var16 - var16;
      var1.z = var8 - var6 - var4 + var2;
      return var1;
   }

   public Vector4d transformPositiveZ(Vector4d var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.x * this.z;
      double var12 = this.y * this.w;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = var12 + var10 + var10 + var12;
      var1.y = var14 + var14 - var16 - var16;
      var1.z = var8 - var6 - var4 + var2;
      return var1;
   }

   public Vector4d transformUnitPositiveZ(Vector4d var1) {
      double var2 = this.x * this.x;
      double var4 = this.y * this.y;
      double var6 = this.x * this.z;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.y * this.w;
      var1.x = var6 + var12 + var6 + var12;
      var1.y = var8 + var8 - var10 - var10;
      var1.z = 1.0D - var2 - var2 - var4 - var4;
      return var1;
   }

   public Vector3d transformUnitPositiveZ(Vector3d var1) {
      double var2 = this.x * this.x;
      double var4 = this.y * this.y;
      double var6 = this.x * this.z;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.y * this.w;
      var1.x = var6 + var12 + var6 + var12;
      var1.y = var8 + var8 - var10 - var10;
      var1.z = 1.0D - var2 - var2 - var4 - var4;
      return var1;
   }

   public Vector4d transform(Vector4d var1) {
      return this.transform((Vector4dc)var1, (Vector4d)var1);
   }

   public Vector4d transformInverse(Vector4d var1) {
      return this.transformInverse((Vector4dc)var1, (Vector4d)var1);
   }

   public Vector3d transform(Vector3dc var1, Vector3d var2) {
      return this.transform(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3d transformInverse(Vector3dc var1, Vector3d var2) {
      return this.transformInverse(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3d transform(double var1, double var3, double var5, Vector3d var7) {
      double var8 = this.x * this.x;
      double var10 = this.y * this.y;
      double var12 = this.z * this.z;
      double var14 = this.w * this.w;
      double var16 = this.x * this.y;
      double var18 = this.x * this.z;
      double var20 = this.y * this.z;
      double var22 = this.x * this.w;
      double var24 = this.z * this.w;
      double var26 = this.y * this.w;
      double var28 = 1.0D / (var8 + var10 + var12 + var14);
      return var7.set(Math.fma((var8 - var10 - var12 + var14) * var28, var1, Math.fma(2.0D * (var16 - var24) * var28, var3, 2.0D * (var18 + var26) * var28 * var5)), Math.fma(2.0D * (var16 + var24) * var28, var1, Math.fma((var10 - var8 - var12 + var14) * var28, var3, 2.0D * (var20 - var22) * var28 * var5)), Math.fma(2.0D * (var18 - var26) * var28, var1, Math.fma(2.0D * (var20 + var22) * var28, var3, (var12 - var8 - var10 + var14) * var28 * var5)));
   }

   public Vector3d transformInverse(double var1, double var3, double var5, Vector3d var7) {
      double var8 = 1.0D / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      double var10 = this.x * var8;
      double var12 = this.y * var8;
      double var14 = this.z * var8;
      double var16 = this.w * var8;
      double var18 = var10 * var10;
      double var20 = var12 * var12;
      double var22 = var14 * var14;
      double var24 = var16 * var16;
      double var26 = var10 * var12;
      double var28 = var10 * var14;
      double var30 = var12 * var14;
      double var32 = var10 * var16;
      double var34 = var14 * var16;
      double var36 = var12 * var16;
      double var38 = 1.0D / (var18 + var20 + var22 + var24);
      return var7.set(Math.fma((var18 - var20 - var22 + var24) * var38, var1, Math.fma(2.0D * (var26 + var34) * var38, var3, 2.0D * (var28 - var36) * var38 * var5)), Math.fma(2.0D * (var26 - var34) * var38, var1, Math.fma((var20 - var18 - var22 + var24) * var38, var3, 2.0D * (var30 + var32) * var38 * var5)), Math.fma(2.0D * (var28 + var36) * var38, var1, Math.fma(2.0D * (var30 - var32) * var38, var3, (var22 - var18 - var20 + var24) * var38 * var5)));
   }

   public Vector4d transform(Vector4dc var1, Vector4d var2) {
      return this.transform(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformInverse(Vector4dc var1, Vector4d var2) {
      return this.transformInverse(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transform(double var1, double var3, double var5, Vector4d var7) {
      double var8 = this.x * this.x;
      double var10 = this.y * this.y;
      double var12 = this.z * this.z;
      double var14 = this.w * this.w;
      double var16 = this.x * this.y;
      double var18 = this.x * this.z;
      double var20 = this.y * this.z;
      double var22 = this.x * this.w;
      double var24 = this.z * this.w;
      double var26 = this.y * this.w;
      double var28 = 1.0D / (var8 + var10 + var12 + var14);
      return var7.set(Math.fma((var8 - var10 - var12 + var14) * var28, var1, Math.fma(2.0D * (var16 - var24) * var28, var3, 2.0D * (var18 + var26) * var28 * var5)), Math.fma(2.0D * (var16 + var24) * var28, var1, Math.fma((var10 - var8 - var12 + var14) * var28, var3, 2.0D * (var20 - var22) * var28 * var5)), Math.fma(2.0D * (var18 - var26) * var28, var1, Math.fma(2.0D * (var20 + var22) * var28, var3, (var12 - var8 - var10 + var14) * var28 * var5)), var7.w);
   }

   public Vector4d transformInverse(double var1, double var3, double var5, Vector4d var7) {
      double var8 = 1.0D / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      double var10 = this.x * var8;
      double var12 = this.y * var8;
      double var14 = this.z * var8;
      double var16 = this.w * var8;
      double var18 = var10 * var10;
      double var20 = var12 * var12;
      double var22 = var14 * var14;
      double var24 = var16 * var16;
      double var26 = var10 * var12;
      double var28 = var10 * var14;
      double var30 = var12 * var14;
      double var32 = var10 * var16;
      double var34 = var14 * var16;
      double var36 = var12 * var16;
      double var38 = 1.0D / (var18 + var20 + var22 + var24);
      return var7.set(Math.fma((var18 - var20 - var22 + var24) * var38, var1, Math.fma(2.0D * (var26 + var34) * var38, var3, 2.0D * (var28 - var36) * var38 * var5)), Math.fma(2.0D * (var26 - var34) * var38, var1, Math.fma((var20 - var18 - var22 + var24) * var38, var3, 2.0D * (var30 + var32) * var38 * var5)), Math.fma(2.0D * (var28 + var36) * var38, var1, Math.fma(2.0D * (var30 - var32) * var38, var3, (var22 - var18 - var20 + var24) * var38 * var5)));
   }

   public Vector3f transform(Vector3f var1) {
      return this.transform((double)var1.x, (double)var1.y, (double)var1.z, var1);
   }

   public Vector3f transformInverse(Vector3f var1) {
      return this.transformInverse((double)var1.x, (double)var1.y, (double)var1.z, var1);
   }

   public Vector4d transformUnit(Vector4d var1) {
      return this.transformUnit((Vector4dc)var1, (Vector4d)var1);
   }

   public Vector4d transformInverseUnit(Vector4d var1) {
      return this.transformInverseUnit((Vector4dc)var1, (Vector4d)var1);
   }

   public Vector3d transformUnit(Vector3dc var1, Vector3d var2) {
      return this.transformUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3d transformInverseUnit(Vector3dc var1, Vector3d var2) {
      return this.transformInverseUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3d transformUnit(double var1, double var3, double var5, Vector3d var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set(Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 - var24), var3, 2.0D * (var12 + var20) * var5)), Math.fma(2.0D * (var10 + var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 - var14) * var5)), Math.fma(2.0D * (var12 - var20), var1, Math.fma(2.0D * (var18 + var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Vector3d transformInverseUnit(double var1, double var3, double var5, Vector3d var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set(Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 + var24), var3, 2.0D * (var12 - var20) * var5)), Math.fma(2.0D * (var10 - var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 + var14) * var5)), Math.fma(2.0D * (var12 + var20), var1, Math.fma(2.0D * (var18 - var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Vector4d transformUnit(Vector4dc var1, Vector4d var2) {
      return this.transformUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformInverseUnit(Vector4dc var1, Vector4d var2) {
      return this.transformInverseUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformUnit(double var1, double var3, double var5, Vector4d var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set(Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 - var24), var3, 2.0D * (var12 + var20) * var5)), Math.fma(2.0D * (var10 + var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 - var14) * var5)), Math.fma(2.0D * (var12 - var20), var1, Math.fma(2.0D * (var18 + var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)), var7.w);
   }

   public Vector4d transformInverseUnit(double var1, double var3, double var5, Vector4d var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set(Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 + var24), var3, 2.0D * (var12 - var20) * var5)), Math.fma(2.0D * (var10 - var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 + var14) * var5)), Math.fma(2.0D * (var12 + var20), var1, Math.fma(2.0D * (var18 - var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)), var7.w);
   }

   public Vector3f transformUnit(Vector3f var1) {
      return this.transformUnit((double)var1.x, (double)var1.y, (double)var1.z, var1);
   }

   public Vector3f transformInverseUnit(Vector3f var1) {
      return this.transformInverseUnit((double)var1.x, (double)var1.y, (double)var1.z, var1);
   }

   public Vector3f transformPositiveX(Vector3f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.x * this.z;
      double var16 = this.y * this.w;
      var1.x = (float)(var2 + var4 - var8 - var6);
      var1.y = (float)(var12 + var10 + var10 + var12);
      var1.z = (float)(var14 - var16 + var14 - var16);
      return var1;
   }

   public Vector4f transformPositiveX(Vector4f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.x * this.z;
      double var16 = this.y * this.w;
      var1.x = (float)(var2 + var4 - var8 - var6);
      var1.y = (float)(var12 + var10 + var10 + var12);
      var1.z = (float)(var14 - var16 + var14 - var16);
      return var1;
   }

   public Vector3f transformUnitPositiveX(Vector3f var1) {
      double var2 = this.y * this.y;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.x * this.z;
      double var10 = this.y * this.w;
      double var12 = this.z * this.w;
      var1.x = (float)(1.0D - var2 - var2 - var4 - var4);
      var1.y = (float)(var6 + var12 + var6 + var12);
      var1.z = (float)(var8 - var10 + var8 - var10);
      return var1;
   }

   public Vector4f transformUnitPositiveX(Vector4f var1) {
      double var2 = this.y * this.y;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.x * this.z;
      double var10 = this.y * this.w;
      double var12 = this.z * this.w;
      var1.x = (float)(1.0D - var2 - var2 - var4 - var4);
      var1.y = (float)(var6 + var12 + var6 + var12);
      var1.z = (float)(var8 - var10 + var8 - var10);
      return var1;
   }

   public Vector3f transformPositiveY(Vector3f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = (float)(-var10 + var12 - var10 + var12);
      var1.y = (float)(var6 - var8 + var2 - var4);
      var1.z = (float)(var14 + var14 + var16 + var16);
      return var1;
   }

   public Vector4f transformPositiveY(Vector4f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.z * this.w;
      double var12 = this.x * this.y;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = (float)(-var10 + var12 - var10 + var12);
      var1.y = (float)(var6 - var8 + var2 - var4);
      var1.z = (float)(var14 + var14 + var16 + var16);
      return var1;
   }

   public Vector4f transformUnitPositiveY(Vector4f var1) {
      double var2 = this.x * this.x;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.z * this.w;
      var1.x = (float)(var6 - var12 + var6 - var12);
      var1.y = (float)(1.0D - var2 - var2 - var4 - var4);
      var1.z = (float)(var8 + var8 + var10 + var10);
      return var1;
   }

   public Vector3f transformUnitPositiveY(Vector3f var1) {
      double var2 = this.x * this.x;
      double var4 = this.z * this.z;
      double var6 = this.x * this.y;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.z * this.w;
      var1.x = (float)(var6 - var12 + var6 - var12);
      var1.y = (float)(1.0D - var2 - var2 - var4 - var4);
      var1.z = (float)(var8 + var8 + var10 + var10);
      return var1;
   }

   public Vector3f transformPositiveZ(Vector3f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.x * this.z;
      double var12 = this.y * this.w;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = (float)(var12 + var10 + var10 + var12);
      var1.y = (float)(var14 + var14 - var16 - var16);
      var1.z = (float)(var8 - var6 - var4 + var2);
      return var1;
   }

   public Vector4f transformPositiveZ(Vector4f var1) {
      double var2 = this.w * this.w;
      double var4 = this.x * this.x;
      double var6 = this.y * this.y;
      double var8 = this.z * this.z;
      double var10 = this.x * this.z;
      double var12 = this.y * this.w;
      double var14 = this.y * this.z;
      double var16 = this.x * this.w;
      var1.x = (float)(var12 + var10 + var10 + var12);
      var1.y = (float)(var14 + var14 - var16 - var16);
      var1.z = (float)(var8 - var6 - var4 + var2);
      return var1;
   }

   public Vector4f transformUnitPositiveZ(Vector4f var1) {
      double var2 = this.x * this.x;
      double var4 = this.y * this.y;
      double var6 = this.x * this.z;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.y * this.w;
      var1.x = (float)(var6 + var12 + var6 + var12);
      var1.y = (float)(var8 + var8 - var10 - var10);
      var1.z = (float)(1.0D - var2 - var2 - var4 - var4);
      return var1;
   }

   public Vector3f transformUnitPositiveZ(Vector3f var1) {
      double var2 = this.x * this.x;
      double var4 = this.y * this.y;
      double var6 = this.x * this.z;
      double var8 = this.y * this.z;
      double var10 = this.x * this.w;
      double var12 = this.y * this.w;
      var1.x = (float)(var6 + var12 + var6 + var12);
      var1.y = (float)(var8 + var8 - var10 - var10);
      var1.z = (float)(1.0D - var2 - var2 - var4 - var4);
      return var1;
   }

   public Vector4f transform(Vector4f var1) {
      return this.transform((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector4f transformInverse(Vector4f var1) {
      return this.transformInverse((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      return this.transform((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector3f transformInverse(Vector3fc var1, Vector3f var2) {
      return this.transformInverse((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector3f transform(double var1, double var3, double var5, Vector3f var7) {
      double var8 = this.x * this.x;
      double var10 = this.y * this.y;
      double var12 = this.z * this.z;
      double var14 = this.w * this.w;
      double var16 = this.x * this.y;
      double var18 = this.x * this.z;
      double var20 = this.y * this.z;
      double var22 = this.x * this.w;
      double var24 = this.z * this.w;
      double var26 = this.y * this.w;
      double var28 = 1.0D / (var8 + var10 + var12 + var14);
      return var7.set(Math.fma((var8 - var10 - var12 + var14) * var28, var1, Math.fma(2.0D * (var16 - var24) * var28, var3, 2.0D * (var18 + var26) * var28 * var5)), Math.fma(2.0D * (var16 + var24) * var28, var1, Math.fma((var10 - var8 - var12 + var14) * var28, var3, 2.0D * (var20 - var22) * var28 * var5)), Math.fma(2.0D * (var18 - var26) * var28, var1, Math.fma(2.0D * (var20 + var22) * var28, var3, (var12 - var8 - var10 + var14) * var28 * var5)));
   }

   public Vector3f transformInverse(double var1, double var3, double var5, Vector3f var7) {
      double var8 = 1.0D / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      double var10 = this.x * var8;
      double var12 = this.y * var8;
      double var14 = this.z * var8;
      double var16 = this.w * var8;
      double var18 = var10 * var10;
      double var20 = var12 * var12;
      double var22 = var14 * var14;
      double var24 = var16 * var16;
      double var26 = var10 * var12;
      double var28 = var10 * var14;
      double var30 = var12 * var14;
      double var32 = var10 * var16;
      double var34 = var14 * var16;
      double var36 = var12 * var16;
      double var38 = 1.0D / (var18 + var20 + var22 + var24);
      return var7.set(Math.fma((var18 - var20 - var22 + var24) * var38, var1, Math.fma(2.0D * (var26 + var34) * var38, var3, 2.0D * (var28 - var36) * var38 * var5)), Math.fma(2.0D * (var26 - var34) * var38, var1, Math.fma((var20 - var18 - var22 + var24) * var38, var3, 2.0D * (var30 + var32) * var38 * var5)), Math.fma(2.0D * (var28 + var36) * var38, var1, Math.fma(2.0D * (var30 - var32) * var38, var3, (var22 - var18 - var20 + var24) * var38 * var5)));
   }

   public Vector4f transform(Vector4fc var1, Vector4f var2) {
      return this.transform((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector4f transformInverse(Vector4fc var1, Vector4f var2) {
      return this.transformInverse((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector4f transform(double var1, double var3, double var5, Vector4f var7) {
      double var8 = this.x * this.x;
      double var10 = this.y * this.y;
      double var12 = this.z * this.z;
      double var14 = this.w * this.w;
      double var16 = this.x * this.y;
      double var18 = this.x * this.z;
      double var20 = this.y * this.z;
      double var22 = this.x * this.w;
      double var24 = this.z * this.w;
      double var26 = this.y * this.w;
      double var28 = 1.0D / (var8 + var10 + var12 + var14);
      return var7.set((float)Math.fma((var8 - var10 - var12 + var14) * var28, var1, Math.fma(2.0D * (var16 - var24) * var28, var3, 2.0D * (var18 + var26) * var28 * var5)), (float)Math.fma(2.0D * (var16 + var24) * var28, var1, Math.fma((var10 - var8 - var12 + var14) * var28, var3, 2.0D * (var20 - var22) * var28 * var5)), (float)Math.fma(2.0D * (var18 - var26) * var28, var1, Math.fma(2.0D * (var20 + var22) * var28, var3, (var12 - var8 - var10 + var14) * var28 * var5)), var7.w);
   }

   public Vector4f transformInverse(double var1, double var3, double var5, Vector4f var7) {
      double var8 = 1.0D / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      double var10 = this.x * var8;
      double var12 = this.y * var8;
      double var14 = this.z * var8;
      double var16 = this.w * var8;
      double var18 = var10 * var10;
      double var20 = var12 * var12;
      double var22 = var14 * var14;
      double var24 = var16 * var16;
      double var26 = var10 * var12;
      double var28 = var10 * var14;
      double var30 = var12 * var14;
      double var32 = var10 * var16;
      double var34 = var14 * var16;
      double var36 = var12 * var16;
      double var38 = 1.0D / (var18 + var20 + var22 + var24);
      return var7.set(Math.fma((var18 - var20 - var22 + var24) * var38, var1, Math.fma(2.0D * (var26 + var34) * var38, var3, 2.0D * (var28 - var36) * var38 * var5)), Math.fma(2.0D * (var26 - var34) * var38, var1, Math.fma((var20 - var18 - var22 + var24) * var38, var3, 2.0D * (var30 + var32) * var38 * var5)), Math.fma(2.0D * (var28 + var36) * var38, var1, Math.fma(2.0D * (var30 - var32) * var38, var3, (var22 - var18 - var20 + var24) * var38 * var5)), (double)var7.w);
   }

   public Vector4f transformUnit(Vector4f var1) {
      return this.transformUnit((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector4f transformInverseUnit(Vector4f var1) {
      return this.transformInverseUnit((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector3f transformUnit(Vector3fc var1, Vector3f var2) {
      return this.transformUnit((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector3f transformInverseUnit(Vector3fc var1, Vector3f var2) {
      return this.transformInverseUnit((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector3f transformUnit(double var1, double var3, double var5, Vector3f var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set((float)Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 - var24), var3, 2.0D * (var12 + var20) * var5)), (float)Math.fma(2.0D * (var10 + var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 - var14) * var5)), (float)Math.fma(2.0D * (var12 - var20), var1, Math.fma(2.0D * (var18 + var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Vector3f transformInverseUnit(double var1, double var3, double var5, Vector3f var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set((float)Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 + var24), var3, 2.0D * (var12 - var20) * var5)), (float)Math.fma(2.0D * (var10 - var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 + var14) * var5)), (float)Math.fma(2.0D * (var12 + var20), var1, Math.fma(2.0D * (var18 - var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Vector4f transformUnit(Vector4fc var1, Vector4f var2) {
      return this.transformUnit((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector4f transformInverseUnit(Vector4fc var1, Vector4f var2) {
      return this.transformInverseUnit((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Vector4f transformUnit(double var1, double var3, double var5, Vector4f var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set((float)Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 - var24), var3, 2.0D * (var12 + var20) * var5)), (float)Math.fma(2.0D * (var10 + var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 - var14) * var5)), (float)Math.fma(2.0D * (var12 - var20), var1, Math.fma(2.0D * (var18 + var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Vector4f transformInverseUnit(double var1, double var3, double var5, Vector4f var7) {
      double var8 = this.x * this.x;
      double var10 = this.x * this.y;
      double var12 = this.x * this.z;
      double var14 = this.x * this.w;
      double var16 = this.y * this.y;
      double var18 = this.y * this.z;
      double var20 = this.y * this.w;
      double var22 = this.z * this.z;
      double var24 = this.z * this.w;
      return var7.set((float)Math.fma(Math.fma(-2.0D, var16 + var22, 1.0D), var1, Math.fma(2.0D * (var10 + var24), var3, 2.0D * (var12 - var20) * var5)), (float)Math.fma(2.0D * (var10 - var24), var1, Math.fma(Math.fma(-2.0D, var8 + var22, 1.0D), var3, 2.0D * (var18 + var14) * var5)), (float)Math.fma(2.0D * (var12 + var20), var1, Math.fma(2.0D * (var18 - var14), var3, Math.fma(-2.0D, var8 + var16, 1.0D) * var5)));
   }

   public Quaterniond invert(Quaterniond var1) {
      double var2 = 1.0D / this.lengthSquared();
      var1.x = -this.x * var2;
      var1.y = -this.y * var2;
      var1.z = -this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Quaterniond invert() {
      return this.invert(this);
   }

   public Quaterniond div(Quaterniondc var1, Quaterniond var2) {
      double var3 = 1.0D / Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), Math.fma(var1.z(), var1.z(), var1.w() * var1.w())));
      double var5 = -var1.x() * var3;
      double var7 = -var1.y() * var3;
      double var9 = -var1.z() * var3;
      double var11 = var1.w() * var3;
      return var2.set(Math.fma(this.w, var5, Math.fma(this.x, var11, Math.fma(this.y, var9, -this.z * var7))), Math.fma(this.w, var7, Math.fma(-this.x, var9, Math.fma(this.y, var11, this.z * var5))), Math.fma(this.w, var9, Math.fma(this.x, var7, Math.fma(-this.y, var5, this.z * var11))), Math.fma(this.w, var11, Math.fma(-this.x, var5, Math.fma(-this.y, var7, -this.z * var9))));
   }

   public Quaterniond div(Quaterniondc var1) {
      return this.div(var1, this);
   }

   public Quaterniond conjugate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Quaterniond conjugate(Quaterniond var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      var1.w = this.w;
      return var1;
   }

   public Quaterniond identity() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = 1.0D;
      return this;
   }

   public double lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
   }

   public Quaterniond rotationXYZ(double var1, double var3, double var5) {
      double var7 = Math.sin(var1 * 0.5D);
      double var9 = Math.cosFromSin(var7, var1 * 0.5D);
      double var11 = Math.sin(var3 * 0.5D);
      double var13 = Math.cosFromSin(var11, var3 * 0.5D);
      double var15 = Math.sin(var5 * 0.5D);
      double var17 = Math.cosFromSin(var15, var5 * 0.5D);
      double var19 = var13 * var17;
      double var21 = var11 * var15;
      double var23 = var11 * var17;
      double var25 = var13 * var15;
      this.w = var9 * var19 - var7 * var21;
      this.x = var7 * var19 + var9 * var21;
      this.y = var9 * var23 - var7 * var25;
      this.z = var9 * var25 + var7 * var23;
      return this;
   }

   public Quaterniond rotationZYX(double var1, double var3, double var5) {
      double var7 = Math.sin(var5 * 0.5D);
      double var9 = Math.cosFromSin(var7, var5 * 0.5D);
      double var11 = Math.sin(var3 * 0.5D);
      double var13 = Math.cosFromSin(var11, var3 * 0.5D);
      double var15 = Math.sin(var1 * 0.5D);
      double var17 = Math.cosFromSin(var15, var1 * 0.5D);
      double var19 = var13 * var17;
      double var21 = var11 * var15;
      double var23 = var11 * var17;
      double var25 = var13 * var15;
      this.w = var9 * var19 + var7 * var21;
      this.x = var7 * var19 - var9 * var21;
      this.y = var9 * var23 + var7 * var25;
      this.z = var9 * var25 - var7 * var23;
      return this;
   }

   public Quaterniond rotationYXZ(double var1, double var3, double var5) {
      double var7 = Math.sin(var3 * 0.5D);
      double var9 = Math.cosFromSin(var7, var3 * 0.5D);
      double var11 = Math.sin(var1 * 0.5D);
      double var13 = Math.cosFromSin(var11, var1 * 0.5D);
      double var15 = Math.sin(var5 * 0.5D);
      double var17 = Math.cosFromSin(var15, var5 * 0.5D);
      double var19 = var13 * var7;
      double var21 = var11 * var9;
      double var23 = var11 * var7;
      double var25 = var13 * var9;
      this.x = var19 * var17 + var21 * var15;
      this.y = var21 * var17 - var19 * var15;
      this.z = var25 * var15 - var23 * var17;
      this.w = var25 * var17 + var23 * var15;
      return this;
   }

   public Quaterniond slerp(Quaterniondc var1, double var2) {
      return this.slerp(var1, var2, this);
   }

   public Quaterniond slerp(Quaterniondc var1, double var2, Quaterniond var4) {
      double var5 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
      double var7 = Math.abs(var5);
      double var9;
      double var11;
      if (1.0D - var7 > 1.0E-6D) {
         double var13 = 1.0D - var7 * var7;
         double var15 = Math.invsqrt(var13);
         double var17 = Math.atan2(var13 * var15, var7);
         var9 = Math.sin((1.0D - var2) * var17) * var15;
         var11 = Math.sin(var2 * var17) * var15;
      } else {
         var9 = 1.0D - var2;
         var11 = var2;
      }

      var11 = var5 >= 0.0D ? var11 : -var11;
      var4.x = Math.fma(var9, this.x, var11 * var1.x());
      var4.y = Math.fma(var9, this.y, var11 * var1.y());
      var4.z = Math.fma(var9, this.z, var11 * var1.z());
      var4.w = Math.fma(var9, this.w, var11 * var1.w());
      return var4;
   }

   public static Quaterniondc slerp(Quaterniond[] var0, double[] var1, Quaterniond var2) {
      var2.set((Quaterniondc)var0[0]);
      double var3 = var1[0];

      for(int var5 = 1; var5 < var0.length; ++var5) {
         double var8 = var1[var5];
         double var10 = var8 / (var3 + var8);
         var3 += var8;
         var2.slerp(var0[var5], var10);
      }

      return var2;
   }

   public Quaterniond scale(double var1) {
      return this.scale(var1, this);
   }

   public Quaterniond scale(double var1, Quaterniond var3) {
      double var4 = Math.sqrt(var1);
      var3.x = var4 * this.x;
      var3.y = var4 * this.y;
      var3.z = var4 * this.z;
      var3.w = var4 * this.w;
      return var3;
   }

   public Quaterniond scaling(double var1) {
      double var3 = Math.sqrt(var1);
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = var3;
      return this;
   }

   public Quaterniond integrate(double var1, double var3, double var5, double var7) {
      return this.integrate(var1, var3, var5, var7, this);
   }

   public Quaterniond integrate(double var1, double var3, double var5, double var7, Quaterniond var9) {
      double var10 = var1 * var3 * 0.5D;
      double var12 = var1 * var5 * 0.5D;
      double var14 = var1 * var7 * 0.5D;
      double var16 = var10 * var10 + var12 * var12 + var14 * var14;
      double var18;
      double var26;
      if (var16 * var16 / 24.0D < 1.0E-8D) {
         var26 = 1.0D - var16 * 0.5D;
         var18 = 1.0D - var16 / 6.0D;
      } else {
         double var28 = Math.sqrt(var16);
         double var30 = Math.sin(var28);
         var18 = var30 / var28;
         var26 = Math.cosFromSin(var30, var28);
      }

      double var20 = var10 * var18;
      double var22 = var12 * var18;
      double var24 = var14 * var18;
      return var9.set(Math.fma(var26, this.x, Math.fma(var20, this.w, Math.fma(var22, this.z, -var24 * this.y))), Math.fma(var26, this.y, Math.fma(-var20, this.z, Math.fma(var22, this.w, var24 * this.x))), Math.fma(var26, this.z, Math.fma(var20, this.y, Math.fma(-var22, this.x, var24 * this.w))), Math.fma(var26, this.w, Math.fma(-var20, this.x, Math.fma(-var22, this.y, -var24 * this.z))));
   }

   public Quaterniond nlerp(Quaterniondc var1, double var2) {
      return this.nlerp(var1, var2, this);
   }

   public Quaterniond nlerp(Quaterniondc var1, double var2, Quaterniond var4) {
      double var5 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
      double var7 = 1.0D - var2;
      double var9 = var5 >= 0.0D ? var2 : -var2;
      var4.x = Math.fma(var7, this.x, var9 * var1.x());
      var4.y = Math.fma(var7, this.y, var9 * var1.y());
      var4.z = Math.fma(var7, this.z, var9 * var1.z());
      var4.w = Math.fma(var7, this.w, var9 * var1.w());
      double var11 = Math.invsqrt(Math.fma(var4.x, var4.x, Math.fma(var4.y, var4.y, Math.fma(var4.z, var4.z, var4.w * var4.w))));
      var4.x *= var11;
      var4.y *= var11;
      var4.z *= var11;
      var4.w *= var11;
      return var4;
   }

   public static Quaterniondc nlerp(Quaterniond[] var0, double[] var1, Quaterniond var2) {
      var2.set((Quaterniondc)var0[0]);
      double var3 = var1[0];

      for(int var5 = 1; var5 < var0.length; ++var5) {
         double var8 = var1[var5];
         double var10 = var8 / (var3 + var8);
         var3 += var8;
         var2.nlerp(var0[var5], var10);
      }

      return var2;
   }

   public Quaterniond nlerpIterative(Quaterniondc var1, double var2, double var4, Quaterniond var6) {
      double var7 = this.x;
      double var9 = this.y;
      double var11 = this.z;
      double var13 = this.w;
      double var15 = var1.x();
      double var17 = var1.y();
      double var19 = var1.z();
      double var21 = var1.w();
      double var23 = Math.fma(var7, var15, Math.fma(var9, var17, Math.fma(var11, var19, var13 * var21)));
      double var25 = Math.abs(var23);
      if (0.999999D < var25) {
         return var6.set((Quaterniondc)this);
      } else {
         double var27;
         double var29;
         double var31;
         for(var27 = var2; var25 < var4; var25 = Math.abs(var23)) {
            var29 = 0.5D;
            var31 = var23 >= 0.0D ? 0.5D : -0.5D;
            float var33;
            if (var27 < 0.5D) {
               var15 = Math.fma(var29, var15, var31 * var7);
               var17 = Math.fma(var29, var17, var31 * var9);
               var19 = Math.fma(var29, var19, var31 * var11);
               var21 = Math.fma(var29, var21, var31 * var13);
               var33 = (float)Math.invsqrt(Math.fma(var15, var15, Math.fma(var17, var17, Math.fma(var19, var19, var21 * var21))));
               var15 *= (double)var33;
               var17 *= (double)var33;
               var19 *= (double)var33;
               var21 *= (double)var33;
               var27 += var27;
            } else {
               var7 = Math.fma(var29, var7, var31 * var15);
               var9 = Math.fma(var29, var9, var31 * var17);
               var11 = Math.fma(var29, var11, var31 * var19);
               var13 = Math.fma(var29, var13, var31 * var21);
               var33 = (float)Math.invsqrt(Math.fma(var7, var7, Math.fma(var9, var9, Math.fma(var11, var11, var13 * var13))));
               var7 *= (double)var33;
               var9 *= (double)var33;
               var11 *= (double)var33;
               var13 *= (double)var33;
               var27 = var27 + var27 - 1.0D;
            }

            var23 = Math.fma(var7, var15, Math.fma(var9, var17, Math.fma(var11, var19, var13 * var21)));
         }

         var29 = 1.0D - var27;
         var31 = var23 >= 0.0D ? var27 : -var27;
         double var43 = Math.fma(var29, var7, var31 * var15);
         double var35 = Math.fma(var29, var9, var31 * var17);
         double var37 = Math.fma(var29, var11, var31 * var19);
         double var39 = Math.fma(var29, var13, var31 * var21);
         double var41 = Math.invsqrt(Math.fma(var43, var43, Math.fma(var35, var35, Math.fma(var37, var37, var39 * var39))));
         var6.x = var43 * var41;
         var6.y = var35 * var41;
         var6.z = var37 * var41;
         var6.w = var39 * var41;
         return var6;
      }
   }

   public Quaterniond nlerpIterative(Quaterniondc var1, double var2, double var4) {
      return this.nlerpIterative(var1, var2, var4, this);
   }

   public static Quaterniond nlerpIterative(Quaterniondc[] var0, double[] var1, double var2, Quaterniond var4) {
      var4.set(var0[0]);
      double var5 = var1[0];

      for(int var7 = 1; var7 < var0.length; ++var7) {
         double var10 = var1[var7];
         double var12 = var10 / (var5 + var10);
         var5 += var10;
         var4.nlerpIterative(var0[var7], var12, var2);
      }

      return var4;
   }

   public Quaterniond lookAlong(Vector3dc var1, Vector3dc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Quaterniond lookAlong(Vector3dc var1, Vector3dc var2, Quaterniond var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Quaterniond lookAlong(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.lookAlong(var1, var3, var5, var7, var9, var11, this);
   }

   public Quaterniond lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Quaterniond var13) {
      double var14 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var16 = -var1 * var14;
      double var18 = -var3 * var14;
      double var20 = -var5 * var14;
      double var22 = var9 * var20 - var11 * var18;
      double var24 = var11 * var16 - var7 * var20;
      double var26 = var7 * var18 - var9 * var16;
      double var28 = Math.invsqrt(var22 * var22 + var24 * var24 + var26 * var26);
      var22 *= var28;
      var24 *= var28;
      var26 *= var28;
      double var30 = var18 * var26 - var20 * var24;
      double var32 = var20 * var22 - var16 * var26;
      double var34 = var16 * var24 - var18 * var22;
      double var46 = var22 + var32 + var20;
      double var36;
      double var38;
      double var40;
      double var42;
      double var44;
      if (var46 >= 0.0D) {
         var44 = Math.sqrt(var46 + 1.0D);
         var42 = var44 * 0.5D;
         var44 = 0.5D / var44;
         var36 = (var18 - var34) * var44;
         var38 = (var26 - var16) * var44;
         var40 = (var30 - var24) * var44;
      } else if (var22 > var32 && var22 > var20) {
         var44 = Math.sqrt(1.0D + var22 - var32 - var20);
         var36 = var44 * 0.5D;
         var44 = 0.5D / var44;
         var38 = (var24 + var30) * var44;
         var40 = (var16 + var26) * var44;
         var42 = (var18 - var34) * var44;
      } else if (var32 > var20) {
         var44 = Math.sqrt(1.0D + var32 - var22 - var20);
         var38 = var44 * 0.5D;
         var44 = 0.5D / var44;
         var36 = (var24 + var30) * var44;
         var40 = (var34 + var18) * var44;
         var42 = (var26 - var16) * var44;
      } else {
         var44 = Math.sqrt(1.0D + var20 - var22 - var32);
         var40 = var44 * 0.5D;
         var44 = 0.5D / var44;
         var36 = (var16 + var26) * var44;
         var38 = (var34 + var18) * var44;
         var42 = (var30 - var24) * var44;
      }

      return var13.set(Math.fma(this.w, var36, Math.fma(this.x, var42, Math.fma(this.y, var40, -this.z * var38))), Math.fma(this.w, var38, Math.fma(-this.x, var40, Math.fma(this.y, var42, this.z * var36))), Math.fma(this.w, var40, Math.fma(this.x, var38, Math.fma(-this.y, var36, this.z * var42))), Math.fma(this.w, var42, Math.fma(-this.x, var36, Math.fma(-this.y, var38, -this.z * var40))));
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format(this.x, var1);
      return "(" + var10000 + " " + Runtime.format(this.y, var1) + " " + Runtime.format(this.z, var1) + " " + Runtime.format(this.w, var1) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.x);
      var1.writeDouble(this.y);
      var1.writeDouble(this.z);
      var1.writeDouble(this.w);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readDouble();
      this.y = var1.readDouble();
      this.z = var1.readDouble();
      this.w = var1.readDouble();
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.w);
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
         Quaterniond var2 = (Quaterniond)var1;
         if (Double.doubleToLongBits(this.w) != Double.doubleToLongBits(var2.w)) {
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

   public Quaterniond difference(Quaterniondc var1) {
      return this.difference(var1, this);
   }

   public Quaterniond difference(Quaterniondc var1, Quaterniond var2) {
      double var3 = 1.0D / this.lengthSquared();
      double var5 = -this.x * var3;
      double var7 = -this.y * var3;
      double var9 = -this.z * var3;
      double var11 = this.w * var3;
      var2.set(Math.fma(var11, var1.x(), Math.fma(var5, var1.w(), Math.fma(var7, var1.z(), -var9 * var1.y()))), Math.fma(var11, var1.y(), Math.fma(-var5, var1.z(), Math.fma(var7, var1.w(), var9 * var1.x()))), Math.fma(var11, var1.z(), Math.fma(var5, var1.y(), Math.fma(-var7, var1.x(), var9 * var1.w()))), Math.fma(var11, var1.w(), Math.fma(-var5, var1.x(), Math.fma(-var7, var1.y(), -var9 * var1.z()))));
      return var2;
   }

   public Quaterniond rotationTo(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = Math.invsqrt(Math.fma(var1, var1, Math.fma(var3, var3, var5 * var5)));
      double var15 = Math.invsqrt(Math.fma(var7, var7, Math.fma(var9, var9, var11 * var11)));
      double var17 = var1 * var13;
      double var19 = var3 * var13;
      double var21 = var5 * var13;
      double var23 = var7 * var15;
      double var25 = var9 * var15;
      double var27 = var11 * var15;
      double var29 = var17 * var23 + var19 * var25 + var21 * var27;
      double var31;
      double var33;
      double var35;
      double var37;
      if (var29 < -0.999999D) {
         var31 = var19;
         var33 = -var17;
         var35 = 0.0D;
         var37 = 0.0D;
         if (var19 * var19 + var33 * var33 == 0.0D) {
            var31 = 0.0D;
            var33 = var21;
            var35 = -var19;
            var37 = 0.0D;
         }

         this.x = var31;
         this.y = var33;
         this.z = var35;
         this.w = 0.0D;
      } else {
         double var39 = Math.sqrt((1.0D + var29) * 2.0D);
         double var41 = 1.0D / var39;
         double var43 = var19 * var27 - var21 * var25;
         double var45 = var21 * var23 - var17 * var27;
         double var47 = var17 * var25 - var19 * var23;
         var31 = var43 * var41;
         var33 = var45 * var41;
         var35 = var47 * var41;
         var37 = var39 * 0.5D;
         double var49 = Math.invsqrt(Math.fma(var31, var31, Math.fma(var33, var33, Math.fma(var35, var35, var37 * var37))));
         this.x = var31 * var49;
         this.y = var33 * var49;
         this.z = var35 * var49;
         this.w = var37 * var49;
      }

      return this;
   }

   public Quaterniond rotationTo(Vector3dc var1, Vector3dc var2) {
      return this.rotationTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Quaterniond rotateTo(double var1, double var3, double var5, double var7, double var9, double var11, Quaterniond var13) {
      double var14 = Math.invsqrt(Math.fma(var1, var1, Math.fma(var3, var3, var5 * var5)));
      double var16 = Math.invsqrt(Math.fma(var7, var7, Math.fma(var9, var9, var11 * var11)));
      double var18 = var1 * var14;
      double var20 = var3 * var14;
      double var22 = var5 * var14;
      double var24 = var7 * var16;
      double var26 = var9 * var16;
      double var28 = var11 * var16;
      double var30 = var18 * var24 + var20 * var26 + var22 * var28;
      double var32;
      double var34;
      double var36;
      double var38;
      if (var30 < -0.999999D) {
         var32 = var20;
         var34 = -var18;
         var36 = 0.0D;
         var38 = 0.0D;
         if (var20 * var20 + var34 * var34 == 0.0D) {
            var32 = 0.0D;
            var34 = var22;
            var36 = -var20;
            var38 = 0.0D;
         }
      } else {
         double var40 = Math.sqrt((1.0D + var30) * 2.0D);
         double var42 = 1.0D / var40;
         double var44 = var20 * var28 - var22 * var26;
         double var46 = var22 * var24 - var18 * var28;
         double var48 = var18 * var26 - var20 * var24;
         var32 = var44 * var42;
         var34 = var46 * var42;
         var36 = var48 * var42;
         var38 = var40 * 0.5D;
         double var50 = Math.invsqrt(Math.fma(var32, var32, Math.fma(var34, var34, Math.fma(var36, var36, var38 * var38))));
         var32 *= var50;
         var34 *= var50;
         var36 *= var50;
         var38 *= var50;
      }

      return var13.set(Math.fma(this.w, var32, Math.fma(this.x, var38, Math.fma(this.y, var36, -this.z * var34))), Math.fma(this.w, var34, Math.fma(-this.x, var36, Math.fma(this.y, var38, this.z * var32))), Math.fma(this.w, var36, Math.fma(this.x, var34, Math.fma(-this.y, var32, this.z * var38))), Math.fma(this.w, var38, Math.fma(-this.x, var32, Math.fma(-this.y, var34, -this.z * var36))));
   }

   public Quaterniond rotationAxis(AxisAngle4f var1) {
      return this.rotationAxis((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Quaterniond rotationAxis(double var1, double var3, double var5, double var7) {
      double var9 = var1 / 2.0D;
      double var11 = Math.sin(var9);
      double var13 = Math.invsqrt(var3 * var3 + var5 * var5 + var7 * var7);
      return this.set(var3 * var13 * var11, var5 * var13 * var11, var7 * var13 * var11, Math.cosFromSin(var11, var9));
   }

   public Quaterniond rotationX(double var1) {
      double var3 = Math.sin(var1 * 0.5D);
      double var5 = Math.cosFromSin(var3, var1 * 0.5D);
      return this.set(var3, 0.0D, var5, 0.0D);
   }

   public Quaterniond rotationY(double var1) {
      double var3 = Math.sin(var1 * 0.5D);
      double var5 = Math.cosFromSin(var3, var1 * 0.5D);
      return this.set(0.0D, var3, 0.0D, var5);
   }

   public Quaterniond rotationZ(double var1) {
      double var3 = Math.sin(var1 * 0.5D);
      double var5 = Math.cosFromSin(var3, var1 * 0.5D);
      return this.set(0.0D, 0.0D, var3, var5);
   }

   public Quaterniond rotateTo(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.rotateTo(var1, var3, var5, var7, var9, var11, this);
   }

   public Quaterniond rotateTo(Vector3dc var1, Vector3dc var2, Quaterniond var3) {
      return this.rotateTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Quaterniond rotateTo(Vector3dc var1, Vector3dc var2) {
      return this.rotateTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Quaterniond rotateX(double var1) {
      return this.rotateX(var1, this);
   }

   public Quaterniond rotateX(double var1, Quaterniond var3) {
      double var4 = Math.sin(var1 * 0.5D);
      double var6 = Math.cosFromSin(var4, var1 * 0.5D);
      return var3.set(this.w * var4 + this.x * var6, this.y * var6 + this.z * var4, this.z * var6 - this.y * var4, this.w * var6 - this.x * var4);
   }

   public Quaterniond rotateY(double var1) {
      return this.rotateY(var1, this);
   }

   public Quaterniond rotateY(double var1, Quaterniond var3) {
      double var4 = Math.sin(var1 * 0.5D);
      double var6 = Math.cosFromSin(var4, var1 * 0.5D);
      return var3.set(this.x * var6 - this.z * var4, this.w * var4 + this.y * var6, this.x * var4 + this.z * var6, this.w * var6 - this.y * var4);
   }

   public Quaterniond rotateZ(double var1) {
      return this.rotateZ(var1, this);
   }

   public Quaterniond rotateZ(double var1, Quaterniond var3) {
      double var4 = Math.sin(var1 * 0.5D);
      double var6 = Math.cosFromSin(var4, var1 * 0.5D);
      return var3.set(this.x * var6 + this.y * var4, this.y * var6 - this.x * var4, this.w * var4 + this.z * var6, this.w * var6 - this.z * var4);
   }

   public Quaterniond rotateLocalX(double var1) {
      return this.rotateLocalX(var1, this);
   }

   public Quaterniond rotateLocalX(double var1, Quaterniond var3) {
      double var4 = var1 * 0.5D;
      double var6 = Math.sin(var4);
      double var8 = Math.cosFromSin(var6, var4);
      var3.set(var8 * this.x + var6 * this.w, var8 * this.y - var6 * this.z, var8 * this.z + var6 * this.y, var8 * this.w - var6 * this.x);
      return var3;
   }

   public Quaterniond rotateLocalY(double var1) {
      return this.rotateLocalY(var1, this);
   }

   public Quaterniond rotateLocalY(double var1, Quaterniond var3) {
      double var4 = var1 * 0.5D;
      double var6 = Math.sin(var4);
      double var8 = Math.cosFromSin(var6, var4);
      var3.set(var8 * this.x + var6 * this.z, var8 * this.y + var6 * this.w, var8 * this.z - var6 * this.x, var8 * this.w - var6 * this.y);
      return var3;
   }

   public Quaterniond rotateLocalZ(double var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Quaterniond rotateLocalZ(double var1, Quaterniond var3) {
      double var4 = var1 * 0.5D;
      double var6 = Math.sin(var4);
      double var8 = Math.cosFromSin(var6, var4);
      var3.set(var8 * this.x - var6 * this.y, var8 * this.y + var6 * this.x, var8 * this.z + var6 * this.w, var8 * this.w - var6 * this.z);
      return var3;
   }

   public Quaterniond rotateXYZ(double var1, double var3, double var5) {
      return this.rotateXYZ(var1, var3, var5, this);
   }

   public Quaterniond rotateXYZ(double var1, double var3, double var5, Quaterniond var7) {
      double var8 = Math.sin(var1 * 0.5D);
      double var10 = Math.cosFromSin(var8, var1 * 0.5D);
      double var12 = Math.sin(var3 * 0.5D);
      double var14 = Math.cosFromSin(var12, var3 * 0.5D);
      double var16 = Math.sin(var5 * 0.5D);
      double var18 = Math.cosFromSin(var16, var5 * 0.5D);
      double var20 = var14 * var18;
      double var22 = var12 * var16;
      double var24 = var12 * var18;
      double var26 = var14 * var16;
      double var28 = var10 * var20 - var8 * var22;
      double var30 = var8 * var20 + var10 * var22;
      double var32 = var10 * var24 - var8 * var26;
      double var34 = var10 * var26 + var8 * var24;
      return var7.set(Math.fma(this.w, var30, Math.fma(this.x, var28, Math.fma(this.y, var34, -this.z * var32))), Math.fma(this.w, var32, Math.fma(-this.x, var34, Math.fma(this.y, var28, this.z * var30))), Math.fma(this.w, var34, Math.fma(this.x, var32, Math.fma(-this.y, var30, this.z * var28))), Math.fma(this.w, var28, Math.fma(-this.x, var30, Math.fma(-this.y, var32, -this.z * var34))));
   }

   public Quaterniond rotateZYX(double var1, double var3, double var5) {
      return this.rotateZYX(var1, var3, var5, this);
   }

   public Quaterniond rotateZYX(double var1, double var3, double var5, Quaterniond var7) {
      double var8 = Math.sin(var5 * 0.5D);
      double var10 = Math.cosFromSin(var8, var5 * 0.5D);
      double var12 = Math.sin(var3 * 0.5D);
      double var14 = Math.cosFromSin(var12, var3 * 0.5D);
      double var16 = Math.sin(var1 * 0.5D);
      double var18 = Math.cosFromSin(var16, var1 * 0.5D);
      double var20 = var14 * var18;
      double var22 = var12 * var16;
      double var24 = var12 * var18;
      double var26 = var14 * var16;
      double var28 = var10 * var20 + var8 * var22;
      double var30 = var8 * var20 - var10 * var22;
      double var32 = var10 * var24 + var8 * var26;
      double var34 = var10 * var26 - var8 * var24;
      return var7.set(Math.fma(this.w, var30, Math.fma(this.x, var28, Math.fma(this.y, var34, -this.z * var32))), Math.fma(this.w, var32, Math.fma(-this.x, var34, Math.fma(this.y, var28, this.z * var30))), Math.fma(this.w, var34, Math.fma(this.x, var32, Math.fma(-this.y, var30, this.z * var28))), Math.fma(this.w, var28, Math.fma(-this.x, var30, Math.fma(-this.y, var32, -this.z * var34))));
   }

   public Quaterniond rotateYXZ(double var1, double var3, double var5) {
      return this.rotateYXZ(var1, var3, var5, this);
   }

   public Quaterniond rotateYXZ(double var1, double var3, double var5, Quaterniond var7) {
      double var8 = Math.sin(var3 * 0.5D);
      double var10 = Math.cosFromSin(var8, var3 * 0.5D);
      double var12 = Math.sin(var1 * 0.5D);
      double var14 = Math.cosFromSin(var12, var1 * 0.5D);
      double var16 = Math.sin(var5 * 0.5D);
      double var18 = Math.cosFromSin(var16, var5 * 0.5D);
      double var20 = var14 * var8;
      double var22 = var12 * var10;
      double var24 = var12 * var8;
      double var26 = var14 * var10;
      double var28 = var20 * var18 + var22 * var16;
      double var30 = var22 * var18 - var20 * var16;
      double var32 = var26 * var16 - var24 * var18;
      double var34 = var26 * var18 + var24 * var16;
      return var7.set(Math.fma(this.w, var28, Math.fma(this.x, var34, Math.fma(this.y, var32, -this.z * var30))), Math.fma(this.w, var30, Math.fma(-this.x, var32, Math.fma(this.y, var34, this.z * var28))), Math.fma(this.w, var32, Math.fma(this.x, var30, Math.fma(-this.y, var28, this.z * var34))), Math.fma(this.w, var34, Math.fma(-this.x, var28, Math.fma(-this.y, var30, -this.z * var32))));
   }

   public Vector3d getEulerAnglesXYZ(Vector3d var1) {
      var1.x = Math.atan2(2.0D * (this.x * this.w - this.y * this.z), 1.0D - 2.0D * (this.x * this.x + this.y * this.y));
      var1.y = Math.safeAsin(2.0D * (this.x * this.z + this.y * this.w));
      var1.z = Math.atan2(2.0D * (this.z * this.w - this.x * this.y), 1.0D - 2.0D * (this.y * this.y + this.z * this.z));
      return var1;
   }

   public Quaterniond rotateAxis(double var1, double var3, double var5, double var7, Quaterniond var9) {
      double var10 = var1 / 2.0D;
      double var12 = Math.sin(var10);
      double var14 = Math.invsqrt(Math.fma(var3, var3, Math.fma(var5, var5, var7 * var7)));
      double var16 = var3 * var14 * var12;
      double var18 = var5 * var14 * var12;
      double var20 = var7 * var14 * var12;
      double var22 = Math.cosFromSin(var12, var10);
      return var9.set(Math.fma(this.w, var16, Math.fma(this.x, var22, Math.fma(this.y, var20, -this.z * var18))), Math.fma(this.w, var18, Math.fma(-this.x, var20, Math.fma(this.y, var22, this.z * var16))), Math.fma(this.w, var20, Math.fma(this.x, var18, Math.fma(-this.y, var16, this.z * var22))), Math.fma(this.w, var22, Math.fma(-this.x, var16, Math.fma(-this.y, var18, -this.z * var20))));
   }

   public Quaterniond rotateAxis(double var1, Vector3dc var3, Quaterniond var4) {
      return this.rotateAxis(var1, var3.x(), var3.y(), var3.z(), var4);
   }

   public Quaterniond rotateAxis(double var1, Vector3dc var3) {
      return this.rotateAxis(var1, var3.x(), var3.y(), var3.z(), this);
   }

   public Quaterniond rotateAxis(double var1, double var3, double var5, double var7) {
      return this.rotateAxis(var1, var3, var5, var7, this);
   }

   public Vector3d positiveX(Vector3d var1) {
      double var2 = 1.0D / this.lengthSquared();
      double var4 = -this.x * var2;
      double var6 = -this.y * var2;
      double var8 = -this.z * var2;
      double var10 = this.w * var2;
      double var12 = var6 + var6;
      double var14 = var8 + var8;
      var1.x = -var6 * var12 - var8 * var14 + 1.0D;
      var1.y = var4 * var12 + var10 * var14;
      var1.z = var4 * var14 - var10 * var12;
      return var1;
   }

   public Vector3d normalizedPositiveX(Vector3d var1) {
      double var2 = this.y + this.y;
      double var4 = this.z + this.z;
      var1.x = -this.y * var2 - this.z * var4 + 1.0D;
      var1.y = this.x * var2 - this.w * var4;
      var1.z = this.x * var4 + this.w * var2;
      return var1;
   }

   public Vector3d positiveY(Vector3d var1) {
      double var2 = 1.0D / this.lengthSquared();
      double var4 = -this.x * var2;
      double var6 = -this.y * var2;
      double var8 = -this.z * var2;
      double var10 = this.w * var2;
      double var12 = var4 + var4;
      double var14 = var6 + var6;
      double var16 = var8 + var8;
      var1.x = var4 * var14 - var10 * var16;
      var1.y = -var4 * var12 - var8 * var16 + 1.0D;
      var1.z = var6 * var16 + var10 * var12;
      return var1;
   }

   public Vector3d normalizedPositiveY(Vector3d var1) {
      double var2 = this.x + this.x;
      double var4 = this.y + this.y;
      double var6 = this.z + this.z;
      var1.x = this.x * var4 + this.w * var6;
      var1.y = -this.x * var2 - this.z * var6 + 1.0D;
      var1.z = this.y * var6 - this.w * var2;
      return var1;
   }

   public Vector3d positiveZ(Vector3d var1) {
      double var2 = 1.0D / this.lengthSquared();
      double var4 = -this.x * var2;
      double var6 = -this.y * var2;
      double var8 = -this.z * var2;
      double var10 = this.w * var2;
      double var12 = var4 + var4;
      double var14 = var6 + var6;
      double var16 = var8 + var8;
      var1.x = var4 * var16 + var10 * var14;
      var1.y = var6 * var16 - var10 * var12;
      var1.z = -var4 * var12 - var6 * var14 + 1.0D;
      return var1;
   }

   public Vector3d normalizedPositiveZ(Vector3d var1) {
      double var2 = this.x + this.x;
      double var4 = this.y + this.y;
      double var6 = this.z + this.z;
      var1.x = this.x * var6 - this.w * var4;
      var1.y = this.y * var6 + this.w * var2;
      var1.z = -this.x * var2 - this.y * var4 + 1.0D;
      return var1;
   }

   public Quaterniond conjugateBy(Quaterniondc var1) {
      return this.conjugateBy(var1, this);
   }

   public Quaterniond conjugateBy(Quaterniondc var1, Quaterniond var2) {
      double var3 = 1.0D / var1.lengthSquared();
      double var5 = -var1.x() * var3;
      double var7 = -var1.y() * var3;
      double var9 = -var1.z() * var3;
      double var11 = var1.w() * var3;
      double var13 = Math.fma(var1.w(), this.x, Math.fma(var1.x(), this.w, Math.fma(var1.y(), this.z, -var1.z() * this.y)));
      double var15 = Math.fma(var1.w(), this.y, Math.fma(-var1.x(), this.z, Math.fma(var1.y(), this.w, var1.z() * this.x)));
      double var17 = Math.fma(var1.w(), this.z, Math.fma(var1.x(), this.y, Math.fma(-var1.y(), this.x, var1.z() * this.w)));
      double var19 = Math.fma(var1.w(), this.w, Math.fma(-var1.x(), this.x, Math.fma(-var1.y(), this.y, -var1.z() * this.z)));
      return var2.set(Math.fma(var19, var5, Math.fma(var13, var11, Math.fma(var15, var9, -var17 * var7))), Math.fma(var19, var7, Math.fma(-var13, var9, Math.fma(var15, var11, var17 * var5))), Math.fma(var19, var9, Math.fma(var13, var7, Math.fma(-var15, var5, var17 * var11))), Math.fma(var19, var11, Math.fma(-var13, var5, Math.fma(-var15, var7, -var17 * var9))));
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
   }

   public boolean equals(Quaterniondc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Quaterniondc)) {
         return false;
      } else if (!Runtime.equals(this.x, var1.x(), var2)) {
         return false;
      } else if (!Runtime.equals(this.y, var1.y(), var2)) {
         return false;
      } else if (!Runtime.equals(this.z, var1.z(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.w, var1.w(), var2);
      }
   }

   public boolean equals(double var1, double var3, double var5, double var7) {
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var1)) {
         return false;
      } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(var3)) {
         return false;
      } else if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(var5)) {
         return false;
      } else {
         return Double.doubleToLongBits(this.w) == Double.doubleToLongBits(var7);
      }
   }
}
