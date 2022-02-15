package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Quaternionf implements Externalizable, Quaternionfc {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;
   public float w;

   public Quaternionf() {
      this.w = 1.0F;
   }

   public Quaternionf(double var1, double var3, double var5, double var7) {
      this.x = (float)var1;
      this.y = (float)var3;
      this.z = (float)var5;
      this.w = (float)var7;
   }

   public Quaternionf(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Quaternionf(Quaternionfc var1) {
      this.set(var1);
   }

   public Quaternionf(Quaterniondc var1) {
      this.set(var1);
   }

   public Quaternionf(AxisAngle4f var1) {
      float var2 = Math.sin(var1.angle * 0.5F);
      float var3 = Math.cosFromSin(var2, var1.angle * 0.5F);
      this.x = var1.x * var2;
      this.y = var1.y * var2;
      this.z = var1.z * var2;
      this.w = var3;
   }

   public Quaternionf(AxisAngle4d var1) {
      double var2 = Math.sin(var1.angle * 0.5D);
      double var4 = Math.cosFromSin(var2, var1.angle * 0.5D);
      this.x = (float)(var1.x * var2);
      this.y = (float)(var1.y * var2);
      this.z = (float)(var1.z * var2);
      this.w = (float)var4;
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }

   public float z() {
      return this.z;
   }

   public float w() {
      return this.w;
   }

   public Quaternionf normalize() {
      return this.normalize(this);
   }

   public Quaternionf normalize(Quaternionf var1) {
      float var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Quaternionf add(float var1, float var2, float var3, float var4) {
      return this.add(var1, var2, var3, var4, this);
   }

   public Quaternionf add(float var1, float var2, float var3, float var4, Quaternionf var5) {
      var5.x = this.x + var1;
      var5.y = this.y + var2;
      var5.z = this.z + var3;
      var5.w = this.w + var4;
      return var5;
   }

   public Quaternionf add(Quaternionfc var1) {
      return this.add(var1, this);
   }

   public Quaternionf add(Quaternionfc var1, Quaternionf var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      var2.w = this.w + var1.w();
      return var2;
   }

   public float dot(Quaternionf var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z + this.w * var1.w;
   }

   public float angle() {
      return (float)(2.0D * (double)Math.safeAcos(this.w));
   }

   public Matrix3f get(Matrix3f var1) {
      return var1.set((Quaternionfc)this);
   }

   public Matrix3d get(Matrix3d var1) {
      return var1.set((Quaternionfc)this);
   }

   public Matrix4f get(Matrix4f var1) {
      return var1.set((Quaternionfc)this);
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set((Quaternionfc)this);
   }

   public Matrix4x3f get(Matrix4x3f var1) {
      return var1.set((Quaternionfc)this);
   }

   public Matrix4x3d get(Matrix4x3d var1) {
      return var1.set((Quaternionfc)this);
   }

   public AxisAngle4f get(AxisAngle4f var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      float var6;
      if (var5 > 1.0F) {
         var6 = Math.invsqrt(Math.fma(var2, var2, Math.fma(var3, var3, Math.fma(var4, var4, var5 * var5))));
         var2 *= var6;
         var3 *= var6;
         var4 *= var6;
         var5 *= var6;
      }

      var1.angle = 2.0F * Math.acos(var5);
      var6 = Math.sqrt(1.0F - var5 * var5);
      if (var6 < 0.001F) {
         var1.x = var2;
         var1.y = var3;
         var1.z = var4;
      } else {
         var6 = 1.0F / var6;
         var1.x = var2 * var6;
         var1.y = var3 * var6;
         var1.z = var4 * var6;
      }

      return var1;
   }

   public AxisAngle4d get(AxisAngle4d var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      float var6;
      if (var5 > 1.0F) {
         var6 = Math.invsqrt(Math.fma(var2, var2, Math.fma(var3, var3, Math.fma(var4, var4, var5 * var5))));
         var2 *= var6;
         var3 *= var6;
         var4 *= var6;
         var5 *= var6;
      }

      var1.angle = (double)(2.0F * Math.acos(var5));
      var6 = Math.sqrt(1.0F - var5 * var5);
      if (var6 < 0.001F) {
         var1.x = (double)var2;
         var1.y = (double)var3;
         var1.z = (double)var4;
      } else {
         var6 = 1.0F / var6;
         var1.x = (double)(var2 * var6);
         var1.y = (double)(var3 * var6);
         var1.z = (double)(var4 * var6);
      }

      return var1;
   }

   public Quaterniond get(Quaterniond var1) {
      return var1.set((Quaternionfc)this);
   }

   public Quaternionf get(Quaternionf var1) {
      return var1.set((Quaternionfc)this);
   }

   public ByteBuffer getAsMatrix3f(ByteBuffer var1) {
      MemUtil.INSTANCE.putMatrix3f(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer getAsMatrix3f(FloatBuffer var1) {
      MemUtil.INSTANCE.putMatrix3f(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer getAsMatrix4f(ByteBuffer var1) {
      MemUtil.INSTANCE.putMatrix4f(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer getAsMatrix4f(FloatBuffer var1) {
      MemUtil.INSTANCE.putMatrix4f(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer getAsMatrix4x3f(ByteBuffer var1) {
      MemUtil.INSTANCE.putMatrix4x3f(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer getAsMatrix4x3f(FloatBuffer var1) {
      MemUtil.INSTANCE.putMatrix4x3f(this, var1.position(), var1);
      return var1;
   }

   public Quaternionf set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
      return this;
   }

   public Quaternionf set(Quaternionfc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
      return this;
   }

   public Quaternionf set(Quaterniondc var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = (float)var1.w();
      return this;
   }

   public Quaternionf set(AxisAngle4f var1) {
      return this.setAngleAxis(var1.angle, var1.x, var1.y, var1.z);
   }

   public Quaternionf set(AxisAngle4d var1) {
      return this.setAngleAxis(var1.angle, var1.x, var1.y, var1.z);
   }

   public Quaternionf setAngleAxis(float var1, float var2, float var3, float var4) {
      float var5 = Math.sin(var1 * 0.5F);
      this.x = var2 * var5;
      this.y = var3 * var5;
      this.z = var4 * var5;
      this.w = Math.cosFromSin(var5, var1 * 0.5F);
      return this;
   }

   public Quaternionf setAngleAxis(double var1, double var3, double var5, double var7) {
      double var9 = Math.sin(var1 * 0.5D);
      this.x = (float)(var3 * var9);
      this.y = (float)(var5 * var9);
      this.z = (float)(var7 * var9);
      this.w = (float)Math.cosFromSin(var9, var1 * 0.5D);
      return this;
   }

   public Quaternionf rotationAxis(AxisAngle4f var1) {
      return this.rotationAxis(var1.angle, var1.x, var1.y, var1.z);
   }

   public Quaternionf rotationAxis(float var1, float var2, float var3, float var4) {
      float var5 = var1 / 2.0F;
      float var6 = Math.sin(var5);
      float var7 = Math.invsqrt(var2 * var2 + var3 * var3 + var4 * var4);
      return this.set(var2 * var7 * var6, var3 * var7 * var6, var4 * var7 * var6, Math.cosFromSin(var6, var5));
   }

   public Quaternionf rotationAxis(float var1, Vector3fc var2) {
      return this.rotationAxis(var1, var2.x(), var2.y(), var2.z());
   }

   public Quaternionf rotationX(float var1) {
      float var2 = Math.sin(var1 * 0.5F);
      float var3 = Math.cosFromSin(var2, var1 * 0.5F);
      return this.set(var2, 0.0F, 0.0F, var3);
   }

   public Quaternionf rotationY(float var1) {
      float var2 = Math.sin(var1 * 0.5F);
      float var3 = Math.cosFromSin(var2, var1 * 0.5F);
      return this.set(0.0F, var2, 0.0F, var3);
   }

   public Quaternionf rotationZ(float var1) {
      float var2 = Math.sin(var1 * 0.5F);
      float var3 = Math.cosFromSin(var2, var1 * 0.5F);
      return this.set(0.0F, 0.0F, var2, var3);
   }

   private void setFromUnnormalized(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var19 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var20 = Math.invsqrt(var4 * var4 + var5 * var5 + var6 * var6);
      float var21 = Math.invsqrt(var7 * var7 + var8 * var8 + var9 * var9);
      float var10 = var1 * var19;
      float var11 = var2 * var19;
      float var12 = var3 * var19;
      float var13 = var4 * var20;
      float var14 = var5 * var20;
      float var15 = var6 * var20;
      float var16 = var7 * var21;
      float var17 = var8 * var21;
      float var18 = var9 * var21;
      this.setFromNormalized(var10, var11, var12, var13, var14, var15, var16, var17, var18);
   }

   private void setFromNormalized(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var11 = var1 + var5 + var9;
      float var10;
      if (var11 >= 0.0F) {
         var10 = Math.sqrt(var11 + 1.0F);
         this.w = var10 * 0.5F;
         var10 = 0.5F / var10;
         this.x = (var6 - var8) * var10;
         this.y = (var7 - var3) * var10;
         this.z = (var2 - var4) * var10;
      } else if (var1 >= var5 && var1 >= var9) {
         var10 = Math.sqrt(var1 - (var5 + var9) + 1.0F);
         this.x = var10 * 0.5F;
         var10 = 0.5F / var10;
         this.y = (var4 + var2) * var10;
         this.z = (var3 + var7) * var10;
         this.w = (var6 - var8) * var10;
      } else if (var5 > var9) {
         var10 = Math.sqrt(var5 - (var9 + var1) + 1.0F);
         this.y = var10 * 0.5F;
         var10 = 0.5F / var10;
         this.z = (var8 + var6) * var10;
         this.x = (var4 + var2) * var10;
         this.w = (var7 - var3) * var10;
      } else {
         var10 = Math.sqrt(var9 - (var1 + var5) + 1.0F);
         this.z = var10 * 0.5F;
         var10 = 0.5F / var10;
         this.x = (var3 + var7) * var10;
         this.y = (var8 + var6) * var10;
         this.w = (var2 - var4) * var10;
      }

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
         this.w = (float)(var19 * 0.5D);
         var19 = 0.5D / var19;
         this.x = (float)((var11 - var15) * var19);
         this.y = (float)((var13 - var5) * var19);
         this.z = (float)((var3 - var7) * var19);
      } else if (var1 >= var9 && var1 >= var17) {
         var19 = Math.sqrt(var1 - (var9 + var17) + 1.0D);
         this.x = (float)(var19 * 0.5D);
         var19 = 0.5D / var19;
         this.y = (float)((var7 + var3) * var19);
         this.z = (float)((var5 + var13) * var19);
         this.w = (float)((var11 - var15) * var19);
      } else if (var9 > var17) {
         var19 = Math.sqrt(var9 - (var17 + var1) + 1.0D);
         this.y = (float)(var19 * 0.5D);
         var19 = 0.5D / var19;
         this.z = (float)((var15 + var11) * var19);
         this.x = (float)((var7 + var3) * var19);
         this.w = (float)((var13 - var5) * var19);
      } else {
         var19 = Math.sqrt(var17 - (var1 + var9) + 1.0D);
         this.z = (float)(var19 * 0.5D);
         var19 = 0.5D / var19;
         this.x = (float)((var5 + var13) * var19);
         this.y = (float)((var15 + var11) * var19);
         this.w = (float)((var3 - var7) * var19);
      }

   }

   public Quaternionf setFromUnnormalized(Matrix4fc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromUnnormalized(Matrix4x3fc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromUnnormalized(Matrix4x3dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix4fc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix4x3fc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix4x3dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromUnnormalized(Matrix4dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix4dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromUnnormalized(Matrix3fc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix3fc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromUnnormalized(Matrix3dc var1) {
      this.setFromUnnormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf setFromNormalized(Matrix3dc var1) {
      this.setFromNormalized(var1.m00(), var1.m01(), var1.m02(), var1.m10(), var1.m11(), var1.m12(), var1.m20(), var1.m21(), var1.m22());
      return this;
   }

   public Quaternionf fromAxisAngleRad(Vector3fc var1, float var2) {
      return this.fromAxisAngleRad(var1.x(), var1.y(), var1.z(), var2);
   }

   public Quaternionf fromAxisAngleRad(float var1, float var2, float var3, float var4) {
      float var5 = var4 / 2.0F;
      float var6 = Math.sin(var5);
      float var7 = Math.sqrt(var1 * var1 + var2 * var2 + var3 * var3);
      this.x = var1 / var7 * var6;
      this.y = var2 / var7 * var6;
      this.z = var3 / var7 * var6;
      this.w = Math.cosFromSin(var6, var5);
      return this;
   }

   public Quaternionf fromAxisAngleDeg(Vector3fc var1, float var2) {
      return this.fromAxisAngleRad(var1.x(), var1.y(), var1.z(), Math.toRadians(var2));
   }

   public Quaternionf fromAxisAngleDeg(float var1, float var2, float var3, float var4) {
      return this.fromAxisAngleRad(var1, var2, var3, Math.toRadians(var4));
   }

   public Quaternionf mul(Quaternionfc var1) {
      return this.mul(var1, this);
   }

   public Quaternionf mul(Quaternionfc var1, Quaternionf var2) {
      return var2.set(Math.fma(this.w, var1.x(), Math.fma(this.x, var1.w(), Math.fma(this.y, var1.z(), -this.z * var1.y()))), Math.fma(this.w, var1.y(), Math.fma(-this.x, var1.z(), Math.fma(this.y, var1.w(), this.z * var1.x()))), Math.fma(this.w, var1.z(), Math.fma(this.x, var1.y(), Math.fma(-this.y, var1.x(), this.z * var1.w()))), Math.fma(this.w, var1.w(), Math.fma(-this.x, var1.x(), Math.fma(-this.y, var1.y(), -this.z * var1.z()))));
   }

   public Quaternionf mul(float var1, float var2, float var3, float var4) {
      return this.mul(var1, var2, var3, var4, this);
   }

   public Quaternionf mul(float var1, float var2, float var3, float var4, Quaternionf var5) {
      return var5.set(Math.fma(this.w, var1, Math.fma(this.x, var4, Math.fma(this.y, var3, -this.z * var2))), Math.fma(this.w, var2, Math.fma(-this.x, var3, Math.fma(this.y, var4, this.z * var1))), Math.fma(this.w, var3, Math.fma(this.x, var2, Math.fma(-this.y, var1, this.z * var4))), Math.fma(this.w, var4, Math.fma(-this.x, var1, Math.fma(-this.y, var2, -this.z * var3))));
   }

   public Quaternionf premul(Quaternionfc var1) {
      return this.premul(var1, this);
   }

   public Quaternionf premul(Quaternionfc var1, Quaternionf var2) {
      return var2.set(Math.fma(var1.w(), this.x, Math.fma(var1.x(), this.w, Math.fma(var1.y(), this.z, -var1.z() * this.y))), Math.fma(var1.w(), this.y, Math.fma(-var1.x(), this.z, Math.fma(var1.y(), this.w, var1.z() * this.x))), Math.fma(var1.w(), this.z, Math.fma(var1.x(), this.y, Math.fma(-var1.y(), this.x, var1.z() * this.w))), Math.fma(var1.w(), this.w, Math.fma(-var1.x(), this.x, Math.fma(-var1.y(), this.y, -var1.z() * this.z))));
   }

   public Quaternionf premul(float var1, float var2, float var3, float var4) {
      return this.premul(var1, var2, var3, var4, this);
   }

   public Quaternionf premul(float var1, float var2, float var3, float var4, Quaternionf var5) {
      return var5.set(Math.fma(var4, this.x, Math.fma(var1, this.w, Math.fma(var2, this.z, -var3 * this.y))), Math.fma(var4, this.y, Math.fma(-var1, this.z, Math.fma(var2, this.w, var3 * this.x))), Math.fma(var4, this.z, Math.fma(var1, this.y, Math.fma(-var2, this.x, var3 * this.w))), Math.fma(var4, this.w, Math.fma(-var1, this.x, Math.fma(-var2, this.y, -var3 * this.z))));
   }

   public Vector3f transform(Vector3f var1) {
      return this.transform(var1.x, var1.y, var1.z, var1);
   }

   public Vector3f transformInverse(Vector3f var1) {
      return this.transformInverse(var1.x, var1.y, var1.z, var1);
   }

   public Vector3f transformPositiveX(Vector3f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.x * this.z;
      float var9 = this.y * this.w;
      var1.x = var2 + var3 - var5 - var4;
      var1.y = var7 + var6 + var6 + var7;
      var1.z = var8 - var9 + var8 - var9;
      return var1;
   }

   public Vector4f transformPositiveX(Vector4f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.x * this.z;
      float var9 = this.y * this.w;
      var1.x = var2 + var3 - var5 - var4;
      var1.y = var7 + var6 + var6 + var7;
      var1.z = var8 - var9 + var8 - var9;
      return var1;
   }

   public Vector3f transformUnitPositiveX(Vector3f var1) {
      float var2 = this.x * this.y;
      float var3 = this.x * this.z;
      float var4 = this.y * this.y;
      float var5 = this.y * this.w;
      float var6 = this.z * this.z;
      float var7 = this.z * this.w;
      var1.x = 1.0F - var4 - var6 - var4 - var6;
      var1.y = var2 + var7 + var2 + var7;
      var1.z = var3 - var5 + var3 - var5;
      return var1;
   }

   public Vector4f transformUnitPositiveX(Vector4f var1) {
      float var2 = this.y * this.y;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.x * this.z;
      float var6 = this.y * this.w;
      float var7 = this.z * this.w;
      var1.x = 1.0F - var2 - var2 - var3 - var3;
      var1.y = var4 + var7 + var4 + var7;
      var1.z = var5 - var6 + var5 - var6;
      return var1;
   }

   public Vector3f transformPositiveY(Vector3f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = -var6 + var7 - var6 + var7;
      var1.y = var4 - var5 + var2 - var3;
      var1.z = var8 + var8 + var9 + var9;
      return var1;
   }

   public Vector4f transformPositiveY(Vector4f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = -var6 + var7 - var6 + var7;
      var1.y = var4 - var5 + var2 - var3;
      var1.z = var8 + var8 + var9 + var9;
      return var1;
   }

   public Vector4f transformUnitPositiveY(Vector4f var1) {
      float var2 = this.x * this.x;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.z * this.w;
      var1.x = var4 - var7 + var4 - var7;
      var1.y = 1.0F - var2 - var2 - var3 - var3;
      var1.z = var5 + var5 + var6 + var6;
      return var1;
   }

   public Vector3f transformUnitPositiveY(Vector3f var1) {
      float var2 = this.x * this.x;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.z * this.w;
      var1.x = var4 - var7 + var4 - var7;
      var1.y = 1.0F - var2 - var2 - var3 - var3;
      var1.z = var5 + var5 + var6 + var6;
      return var1;
   }

   public Vector3f transformPositiveZ(Vector3f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.x * this.z;
      float var7 = this.y * this.w;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = var7 + var6 + var6 + var7;
      var1.y = var8 + var8 - var9 - var9;
      var1.z = var5 - var4 - var3 + var2;
      return var1;
   }

   public Vector4f transformPositiveZ(Vector4f var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.x * this.z;
      float var7 = this.y * this.w;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = var7 + var6 + var6 + var7;
      var1.y = var8 + var8 - var9 - var9;
      var1.z = var5 - var4 - var3 + var2;
      return var1;
   }

   public Vector4f transformUnitPositiveZ(Vector4f var1) {
      float var2 = this.x * this.x;
      float var3 = this.y * this.y;
      float var4 = this.x * this.z;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.y * this.w;
      var1.x = var4 + var7 + var4 + var7;
      var1.y = var5 + var5 - var6 - var6;
      var1.z = 1.0F - var2 - var2 - var3 - var3;
      return var1;
   }

   public Vector3f transformUnitPositiveZ(Vector3f var1) {
      float var2 = this.x * this.x;
      float var3 = this.y * this.y;
      float var4 = this.x * this.z;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.y * this.w;
      var1.x = var4 + var7 + var4 + var7;
      var1.y = var5 + var5 - var6 - var6;
      var1.z = 1.0F - var2 - var2 - var3 - var3;
      return var1;
   }

   public Vector4f transform(Vector4f var1) {
      return this.transform((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector4f transformInverse(Vector4f var1) {
      return this.transformInverse((Vector4fc)var1, (Vector4f)var1);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      return this.transform(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transformInverse(Vector3fc var1, Vector3f var2) {
      return this.transformInverse(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transform(float var1, float var2, float var3, Vector3f var4) {
      float var5 = this.x * this.x;
      float var6 = this.y * this.y;
      float var7 = this.z * this.z;
      float var8 = this.w * this.w;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.y * this.z;
      float var12 = this.x * this.w;
      float var13 = this.z * this.w;
      float var14 = this.y * this.w;
      float var15 = 1.0F / (var5 + var6 + var7 + var8);
      return var4.set(Math.fma((var5 - var6 - var7 + var8) * var15, var1, Math.fma(2.0F * (var9 - var13) * var15, var2, 2.0F * (var10 + var14) * var15 * var3)), Math.fma(2.0F * (var9 + var13) * var15, var1, Math.fma((var6 - var5 - var7 + var8) * var15, var2, 2.0F * (var11 - var12) * var15 * var3)), Math.fma(2.0F * (var10 - var14) * var15, var1, Math.fma(2.0F * (var11 + var12) * var15, var2, (var7 - var5 - var6 + var8) * var15 * var3)));
   }

   public Vector3f transformInverse(float var1, float var2, float var3, Vector3f var4) {
      float var5 = 1.0F / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      float var6 = this.x * var5;
      float var7 = this.y * var5;
      float var8 = this.z * var5;
      float var9 = this.w * var5;
      float var10 = var6 * var6;
      float var11 = var7 * var7;
      float var12 = var8 * var8;
      float var13 = var9 * var9;
      float var14 = var6 * var7;
      float var15 = var6 * var8;
      float var16 = var7 * var8;
      float var17 = var6 * var9;
      float var18 = var8 * var9;
      float var19 = var7 * var9;
      float var20 = 1.0F / (var10 + var11 + var12 + var13);
      return var4.set(Math.fma((var10 - var11 - var12 + var13) * var20, var1, Math.fma(2.0F * (var14 + var18) * var20, var2, 2.0F * (var15 - var19) * var20 * var3)), Math.fma(2.0F * (var14 - var18) * var20, var1, Math.fma((var11 - var10 - var12 + var13) * var20, var2, 2.0F * (var16 + var17) * var20 * var3)), Math.fma(2.0F * (var15 + var19) * var20, var1, Math.fma(2.0F * (var16 - var17) * var20, var2, (var12 - var10 - var11 + var13) * var20 * var3)));
   }

   public Vector3f transformUnit(Vector3f var1) {
      return this.transformUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector3f transformInverseUnit(Vector3f var1) {
      return this.transformInverseUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector3f transformUnit(Vector3fc var1, Vector3f var2) {
      return this.transformUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transformInverseUnit(Vector3fc var1, Vector3f var2) {
      return this.transformInverseUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transformUnit(float var1, float var2, float var3, Vector3f var4) {
      float var5 = this.x * this.x;
      float var6 = this.x * this.y;
      float var7 = this.x * this.z;
      float var8 = this.x * this.w;
      float var9 = this.y * this.y;
      float var10 = this.y * this.z;
      float var11 = this.y * this.w;
      float var12 = this.z * this.z;
      float var13 = this.z * this.w;
      return var4.set(Math.fma(Math.fma(-2.0F, var9 + var12, 1.0F), var1, Math.fma(2.0F * (var6 - var13), var2, 2.0F * (var7 + var11) * var3)), Math.fma(2.0F * (var6 + var13), var1, Math.fma(Math.fma(-2.0F, var5 + var12, 1.0F), var2, 2.0F * (var10 - var8) * var3)), Math.fma(2.0F * (var7 - var11), var1, Math.fma(2.0F * (var10 + var8), var2, Math.fma(-2.0F, var5 + var9, 1.0F) * var3)));
   }

   public Vector3f transformInverseUnit(float var1, float var2, float var3, Vector3f var4) {
      float var5 = this.x * this.x;
      float var6 = this.x * this.y;
      float var7 = this.x * this.z;
      float var8 = this.x * this.w;
      float var9 = this.y * this.y;
      float var10 = this.y * this.z;
      float var11 = this.y * this.w;
      float var12 = this.z * this.z;
      float var13 = this.z * this.w;
      return var4.set(Math.fma(Math.fma(-2.0F, var9 + var12, 1.0F), var1, Math.fma(2.0F * (var6 + var13), var2, 2.0F * (var7 - var11) * var3)), Math.fma(2.0F * (var6 - var13), var1, Math.fma(Math.fma(-2.0F, var5 + var12, 1.0F), var2, 2.0F * (var10 + var8) * var3)), Math.fma(2.0F * (var7 + var11), var1, Math.fma(2.0F * (var10 - var8), var2, Math.fma(-2.0F, var5 + var9, 1.0F) * var3)));
   }

   public Vector4f transform(Vector4fc var1, Vector4f var2) {
      return this.transform(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4f transformInverse(Vector4fc var1, Vector4f var2) {
      return this.transformInverse(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4f transform(float var1, float var2, float var3, Vector4f var4) {
      float var5 = this.x * this.x;
      float var6 = this.y * this.y;
      float var7 = this.z * this.z;
      float var8 = this.w * this.w;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.y * this.z;
      float var12 = this.x * this.w;
      float var13 = this.z * this.w;
      float var14 = this.y * this.w;
      float var15 = 1.0F / (var5 + var6 + var7 + var8);
      return var4.set(Math.fma((var5 - var6 - var7 + var8) * var15, var1, Math.fma(2.0F * (var9 - var13) * var15, var2, 2.0F * (var10 + var14) * var15 * var3)), Math.fma(2.0F * (var9 + var13) * var15, var1, Math.fma((var6 - var5 - var7 + var8) * var15, var2, 2.0F * (var11 - var12) * var15 * var3)), Math.fma(2.0F * (var10 - var14) * var15, var1, Math.fma(2.0F * (var11 + var12) * var15, var2, (var7 - var5 - var6 + var8) * var15 * var3)));
   }

   public Vector4f transformInverse(float var1, float var2, float var3, Vector4f var4) {
      float var5 = 1.0F / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      float var6 = this.x * var5;
      float var7 = this.y * var5;
      float var8 = this.z * var5;
      float var9 = this.w * var5;
      float var10 = var6 * var6;
      float var11 = var7 * var7;
      float var12 = var8 * var8;
      float var13 = var9 * var9;
      float var14 = var6 * var7;
      float var15 = var6 * var8;
      float var16 = var7 * var8;
      float var17 = var6 * var9;
      float var18 = var8 * var9;
      float var19 = var7 * var9;
      float var20 = 1.0F / (var10 + var11 + var12 + var13);
      return var4.set(Math.fma((var10 - var11 - var12 + var13) * var20, var1, Math.fma(2.0F * (var14 + var18) * var20, var2, 2.0F * (var15 - var19) * var20 * var3)), Math.fma(2.0F * (var14 - var18) * var20, var1, Math.fma((var11 - var10 - var12 + var13) * var20, var2, 2.0F * (var16 + var17) * var20 * var3)), Math.fma(2.0F * (var15 + var19) * var20, var1, Math.fma(2.0F * (var16 - var17) * var20, var2, (var12 - var10 - var11 + var13) * var20 * var3)));
   }

   public Vector3d transform(Vector3d var1) {
      return this.transform(var1.x, var1.y, var1.z, var1);
   }

   public Vector3d transformInverse(Vector3d var1) {
      return this.transformInverse(var1.x, var1.y, var1.z, var1);
   }

   public Vector4f transformUnit(Vector4f var1) {
      return this.transformUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector4f transformInverseUnit(Vector4f var1) {
      return this.transformInverseUnit(var1.x, var1.y, var1.z, var1);
   }

   public Vector4f transformUnit(Vector4fc var1, Vector4f var2) {
      return this.transformUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4f transformInverseUnit(Vector4fc var1, Vector4f var2) {
      return this.transformInverseUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4f transformUnit(float var1, float var2, float var3, Vector4f var4) {
      float var5 = this.x * this.x;
      float var6 = this.x * this.y;
      float var7 = this.x * this.z;
      float var8 = this.x * this.w;
      float var9 = this.y * this.y;
      float var10 = this.y * this.z;
      float var11 = this.y * this.w;
      float var12 = this.z * this.z;
      float var13 = this.z * this.w;
      return var4.set(Math.fma(Math.fma(-2.0F, var9 + var12, 1.0F), var1, Math.fma(2.0F * (var6 - var13), var2, 2.0F * (var7 + var11) * var3)), Math.fma(2.0F * (var6 + var13), var1, Math.fma(Math.fma(-2.0F, var5 + var12, 1.0F), var2, 2.0F * (var10 - var8) * var3)), Math.fma(2.0F * (var7 - var11), var1, Math.fma(2.0F * (var10 + var8), var2, Math.fma(-2.0F, var5 + var9, 1.0F) * var3)));
   }

   public Vector4f transformInverseUnit(float var1, float var2, float var3, Vector4f var4) {
      float var5 = this.x * this.x;
      float var6 = this.x * this.y;
      float var7 = this.x * this.z;
      float var8 = this.x * this.w;
      float var9 = this.y * this.y;
      float var10 = this.y * this.z;
      float var11 = this.y * this.w;
      float var12 = this.z * this.z;
      float var13 = this.z * this.w;
      return var4.set(Math.fma(Math.fma(-2.0F, var9 + var12, 1.0F), var1, Math.fma(2.0F * (var6 + var13), var2, 2.0F * (var7 - var11) * var3)), Math.fma(2.0F * (var6 - var13), var1, Math.fma(Math.fma(-2.0F, var5 + var12, 1.0F), var2, 2.0F * (var10 + var8) * var3)), Math.fma(2.0F * (var7 + var11), var1, Math.fma(2.0F * (var10 - var8), var2, Math.fma(-2.0F, var5 + var9, 1.0F) * var3)));
   }

   public Vector3d transformPositiveX(Vector3d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.x * this.z;
      float var9 = this.y * this.w;
      var1.x = (double)(var2 + var3 - var5 - var4);
      var1.y = (double)(var7 + var6 + var6 + var7);
      var1.z = (double)(var8 - var9 + var8 - var9);
      return var1;
   }

   public Vector4d transformPositiveX(Vector4d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.x * this.z;
      float var9 = this.y * this.w;
      var1.x = (double)(var2 + var3 - var5 - var4);
      var1.y = (double)(var7 + var6 + var6 + var7);
      var1.z = (double)(var8 - var9 + var8 - var9);
      return var1;
   }

   public Vector3d transformUnitPositiveX(Vector3d var1) {
      float var2 = this.y * this.y;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.x * this.z;
      float var6 = this.y * this.w;
      float var7 = this.z * this.w;
      var1.x = (double)(1.0F - var2 - var2 - var3 - var3);
      var1.y = (double)(var4 + var7 + var4 + var7);
      var1.z = (double)(var5 - var6 + var5 - var6);
      return var1;
   }

   public Vector4d transformUnitPositiveX(Vector4d var1) {
      float var2 = this.y * this.y;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.x * this.z;
      float var6 = this.y * this.w;
      float var7 = this.z * this.w;
      var1.x = (double)(1.0F - var2 - var2 - var3 - var3);
      var1.y = (double)(var4 + var7 + var4 + var7);
      var1.z = (double)(var5 - var6 + var5 - var6);
      return var1;
   }

   public Vector3d transformPositiveY(Vector3d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = (double)(-var6 + var7 - var6 + var7);
      var1.y = (double)(var4 - var5 + var2 - var3);
      var1.z = (double)(var8 + var8 + var9 + var9);
      return var1;
   }

   public Vector4d transformPositiveY(Vector4d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.z * this.w;
      float var7 = this.x * this.y;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = (double)(-var6 + var7 - var6 + var7);
      var1.y = (double)(var4 - var5 + var2 - var3);
      var1.z = (double)(var8 + var8 + var9 + var9);
      return var1;
   }

   public Vector4d transformUnitPositiveY(Vector4d var1) {
      float var2 = this.x * this.x;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.z * this.w;
      var1.x = (double)(var4 - var7 + var4 - var7);
      var1.y = (double)(1.0F - var2 - var2 - var3 - var3);
      var1.z = (double)(var5 + var5 + var6 + var6);
      return var1;
   }

   public Vector3d transformUnitPositiveY(Vector3d var1) {
      float var2 = this.x * this.x;
      float var3 = this.z * this.z;
      float var4 = this.x * this.y;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.z * this.w;
      var1.x = (double)(var4 - var7 + var4 - var7);
      var1.y = (double)(1.0F - var2 - var2 - var3 - var3);
      var1.z = (double)(var5 + var5 + var6 + var6);
      return var1;
   }

   public Vector3d transformPositiveZ(Vector3d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.x * this.z;
      float var7 = this.y * this.w;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = (double)(var7 + var6 + var6 + var7);
      var1.y = (double)(var8 + var8 - var9 - var9);
      var1.z = (double)(var5 - var4 - var3 + var2);
      return var1;
   }

   public Vector4d transformPositiveZ(Vector4d var1) {
      float var2 = this.w * this.w;
      float var3 = this.x * this.x;
      float var4 = this.y * this.y;
      float var5 = this.z * this.z;
      float var6 = this.x * this.z;
      float var7 = this.y * this.w;
      float var8 = this.y * this.z;
      float var9 = this.x * this.w;
      var1.x = (double)(var7 + var6 + var6 + var7);
      var1.y = (double)(var8 + var8 - var9 - var9);
      var1.z = (double)(var5 - var4 - var3 + var2);
      return var1;
   }

   public Vector4d transformUnitPositiveZ(Vector4d var1) {
      float var2 = this.x * this.x;
      float var3 = this.y * this.y;
      float var4 = this.x * this.z;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.y * this.w;
      var1.x = (double)(var4 + var7 + var4 + var7);
      var1.y = (double)(var5 + var5 - var6 - var6);
      var1.z = (double)(1.0F - var2 - var2 - var3 - var3);
      return var1;
   }

   public Vector3d transformUnitPositiveZ(Vector3d var1) {
      float var2 = this.x * this.x;
      float var3 = this.y * this.y;
      float var4 = this.x * this.z;
      float var5 = this.y * this.z;
      float var6 = this.x * this.w;
      float var7 = this.y * this.w;
      var1.x = (double)(var4 + var7 + var4 + var7);
      var1.y = (double)(var5 + var5 - var6 - var6);
      var1.z = (double)(1.0F - var2 - var2 - var3 - var3);
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

   public Vector3d transform(float var1, float var2, float var3, Vector3d var4) {
      return this.transform((double)var1, (double)var2, (double)var3, var4);
   }

   public Vector3d transformInverse(float var1, float var2, float var3, Vector3d var4) {
      return this.transformInverse((double)var1, (double)var2, (double)var3, var4);
   }

   public Vector3d transform(double var1, double var3, double var5, Vector3d var7) {
      float var8 = this.x * this.x;
      float var9 = this.y * this.y;
      float var10 = this.z * this.z;
      float var11 = this.w * this.w;
      float var12 = this.x * this.y;
      float var13 = this.x * this.z;
      float var14 = this.y * this.z;
      float var15 = this.x * this.w;
      float var16 = this.z * this.w;
      float var17 = this.y * this.w;
      float var18 = 1.0F / (var8 + var9 + var10 + var11);
      return var7.set(Math.fma((double)((var8 - var9 - var10 + var11) * var18), var1, Math.fma((double)(2.0F * (var12 - var16) * var18), var3, (double)(2.0F * (var13 + var17) * var18) * var5)), Math.fma((double)(2.0F * (var12 + var16) * var18), var1, Math.fma((double)((var9 - var8 - var10 + var11) * var18), var3, (double)(2.0F * (var14 - var15) * var18) * var5)), Math.fma((double)(2.0F * (var13 - var17) * var18), var1, Math.fma((double)(2.0F * (var14 + var15) * var18), var3, (double)((var10 - var8 - var9 + var11) * var18) * var5)));
   }

   public Vector3d transformInverse(double var1, double var3, double var5, Vector3d var7) {
      float var8 = 1.0F / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      float var9 = this.x * var8;
      float var10 = this.y * var8;
      float var11 = this.z * var8;
      float var12 = this.w * var8;
      float var13 = var9 * var9;
      float var14 = var10 * var10;
      float var15 = var11 * var11;
      float var16 = var12 * var12;
      float var17 = var9 * var10;
      float var18 = var9 * var11;
      float var19 = var10 * var11;
      float var20 = var9 * var12;
      float var21 = var11 * var12;
      float var22 = var10 * var12;
      float var23 = 1.0F / (var13 + var14 + var15 + var16);
      return var7.set(Math.fma((double)((var13 - var14 - var15 + var16) * var23), var1, Math.fma((double)(2.0F * (var17 + var21) * var23), var3, (double)(2.0F * (var18 - var22) * var23) * var5)), Math.fma((double)(2.0F * (var17 - var21) * var23), var1, Math.fma((double)((var14 - var13 - var15 + var16) * var23), var3, (double)(2.0F * (var19 + var20) * var23) * var5)), Math.fma((double)(2.0F * (var18 + var22) * var23), var1, Math.fma((double)(2.0F * (var19 - var20) * var23), var3, (double)((var15 - var13 - var14 + var16) * var23) * var5)));
   }

   public Vector4d transform(Vector4dc var1, Vector4d var2) {
      return this.transform(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformInverse(Vector4dc var1, Vector4d var2) {
      return this.transformInverse(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transform(double var1, double var3, double var5, Vector4d var7) {
      float var8 = this.x * this.x;
      float var9 = this.y * this.y;
      float var10 = this.z * this.z;
      float var11 = this.w * this.w;
      float var12 = this.x * this.y;
      float var13 = this.x * this.z;
      float var14 = this.y * this.z;
      float var15 = this.x * this.w;
      float var16 = this.z * this.w;
      float var17 = this.y * this.w;
      float var18 = 1.0F / (var8 + var9 + var10 + var11);
      return var7.set(Math.fma((double)((var8 - var9 - var10 + var11) * var18), var1, Math.fma((double)(2.0F * (var12 - var16) * var18), var3, (double)(2.0F * (var13 + var17) * var18) * var5)), Math.fma((double)(2.0F * (var12 + var16) * var18), var1, Math.fma((double)((var9 - var8 - var10 + var11) * var18), var3, (double)(2.0F * (var14 - var15) * var18) * var5)), Math.fma((double)(2.0F * (var13 - var17) * var18), var1, Math.fma((double)(2.0F * (var14 + var15) * var18), var3, (double)((var10 - var8 - var9 + var11) * var18) * var5)));
   }

   public Vector4d transformInverse(double var1, double var3, double var5, Vector4d var7) {
      float var8 = 1.0F / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      float var9 = this.x * var8;
      float var10 = this.y * var8;
      float var11 = this.z * var8;
      float var12 = this.w * var8;
      float var13 = var9 * var9;
      float var14 = var10 * var10;
      float var15 = var11 * var11;
      float var16 = var12 * var12;
      float var17 = var9 * var10;
      float var18 = var9 * var11;
      float var19 = var10 * var11;
      float var20 = var9 * var12;
      float var21 = var11 * var12;
      float var22 = var10 * var12;
      float var23 = 1.0F / (var13 + var14 + var15 + var16);
      return var7.set(Math.fma((double)((var13 - var14 - var15 + var16) * var23), var1, Math.fma((double)(2.0F * (var17 + var21) * var23), var3, (double)(2.0F * (var18 - var22) * var23) * var5)), Math.fma((double)(2.0F * (var17 - var21) * var23), var1, Math.fma((double)((var14 - var13 - var15 + var16) * var23), var3, (double)(2.0F * (var19 + var20) * var23) * var5)), Math.fma((double)(2.0F * (var18 + var22) * var23), var1, Math.fma((double)(2.0F * (var19 - var20) * var23), var3, (double)((var15 - var13 - var14 + var16) * var23) * var5)));
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

   public Vector3d transformUnit(float var1, float var2, float var3, Vector3d var4) {
      return this.transformUnit((double)var1, (double)var2, (double)var3, var4);
   }

   public Vector3d transformInverseUnit(float var1, float var2, float var3, Vector3d var4) {
      return this.transformInverseUnit((double)var1, (double)var2, (double)var3, var4);
   }

   public Vector3d transformUnit(double var1, double var3, double var5, Vector3d var7) {
      float var8 = this.x * this.x;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.x * this.w;
      float var12 = this.y * this.y;
      float var13 = this.y * this.z;
      float var14 = this.y * this.w;
      float var15 = this.z * this.z;
      float var16 = this.z * this.w;
      return var7.set(Math.fma((double)Math.fma(-2.0F, var12 + var15, 1.0F), var1, Math.fma((double)(2.0F * (var9 - var16)), var3, (double)(2.0F * (var10 + var14)) * var5)), Math.fma((double)(2.0F * (var9 + var16)), var1, Math.fma((double)Math.fma(-2.0F, var8 + var15, 1.0F), var3, (double)(2.0F * (var13 - var11)) * var5)), Math.fma((double)(2.0F * (var10 - var14)), var1, Math.fma((double)(2.0F * (var13 + var11)), var3, (double)Math.fma(-2.0F, var8 + var12, 1.0F) * var5)));
   }

   public Vector3d transformInverseUnit(double var1, double var3, double var5, Vector3d var7) {
      float var8 = this.x * this.x;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.x * this.w;
      float var12 = this.y * this.y;
      float var13 = this.y * this.z;
      float var14 = this.y * this.w;
      float var15 = this.z * this.z;
      float var16 = this.z * this.w;
      return var7.set(Math.fma((double)Math.fma(-2.0F, var12 + var15, 1.0F), var1, Math.fma((double)(2.0F * (var9 + var16)), var3, (double)(2.0F * (var10 - var14)) * var5)), Math.fma((double)(2.0F * (var9 - var16)), var1, Math.fma((double)Math.fma(-2.0F, var8 + var15, 1.0F), var3, (double)(2.0F * (var13 + var11)) * var5)), Math.fma((double)(2.0F * (var10 + var14)), var1, Math.fma((double)(2.0F * (var13 - var11)), var3, (double)Math.fma(-2.0F, var8 + var12, 1.0F) * var5)));
   }

   public Vector4d transformUnit(Vector4dc var1, Vector4d var2) {
      return this.transformUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformInverseUnit(Vector4dc var1, Vector4d var2) {
      return this.transformInverseUnit(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector4d transformUnit(double var1, double var3, double var5, Vector4d var7) {
      float var8 = this.x * this.x;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.x * this.w;
      float var12 = this.y * this.y;
      float var13 = this.y * this.z;
      float var14 = this.y * this.w;
      float var15 = this.z * this.z;
      float var16 = this.z * this.w;
      return var7.set(Math.fma((double)Math.fma(-2.0F, var12 + var15, 1.0F), var1, Math.fma((double)(2.0F * (var9 - var16)), var3, (double)(2.0F * (var10 + var14)) * var5)), Math.fma((double)(2.0F * (var9 + var16)), var1, Math.fma((double)Math.fma(-2.0F, var8 + var15, 1.0F), var3, (double)(2.0F * (var13 - var11)) * var5)), Math.fma((double)(2.0F * (var10 - var14)), var1, Math.fma((double)(2.0F * (var13 + var11)), var3, (double)Math.fma(-2.0F, var8 + var12, 1.0F) * var5)));
   }

   public Vector4d transformInverseUnit(double var1, double var3, double var5, Vector4d var7) {
      float var8 = this.x * this.x;
      float var9 = this.x * this.y;
      float var10 = this.x * this.z;
      float var11 = this.x * this.w;
      float var12 = this.y * this.y;
      float var13 = this.y * this.z;
      float var14 = this.y * this.w;
      float var15 = this.z * this.z;
      float var16 = this.z * this.w;
      return var7.set(Math.fma((double)Math.fma(-2.0F, var12 + var15, 1.0F), var1, Math.fma((double)(2.0F * (var9 + var16)), var3, (double)(2.0F * (var10 - var14)) * var5)), Math.fma((double)(2.0F * (var9 - var16)), var1, Math.fma((double)Math.fma(-2.0F, var8 + var15, 1.0F), var3, (double)(2.0F * (var13 + var11)) * var5)), Math.fma((double)(2.0F * (var10 + var14)), var1, Math.fma((double)(2.0F * (var13 - var11)), var3, (double)Math.fma(-2.0F, var8 + var12, 1.0F) * var5)));
   }

   public Quaternionf invert(Quaternionf var1) {
      float var2 = 1.0F / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      var1.x = -this.x * var2;
      var1.y = -this.y * var2;
      var1.z = -this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Quaternionf invert() {
      return this.invert(this);
   }

   public Quaternionf div(Quaternionfc var1, Quaternionf var2) {
      float var3 = 1.0F / Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), Math.fma(var1.z(), var1.z(), var1.w() * var1.w())));
      float var4 = -var1.x() * var3;
      float var5 = -var1.y() * var3;
      float var6 = -var1.z() * var3;
      float var7 = var1.w() * var3;
      return var2.set(Math.fma(this.w, var4, Math.fma(this.x, var7, Math.fma(this.y, var6, -this.z * var5))), Math.fma(this.w, var5, Math.fma(-this.x, var6, Math.fma(this.y, var7, this.z * var4))), Math.fma(this.w, var6, Math.fma(this.x, var5, Math.fma(-this.y, var4, this.z * var7))), Math.fma(this.w, var7, Math.fma(-this.x, var4, Math.fma(-this.y, var5, -this.z * var6))));
   }

   public Quaternionf div(Quaternionfc var1) {
      return this.div(var1, this);
   }

   public Quaternionf conjugate() {
      return this.conjugate(this);
   }

   public Quaternionf conjugate(Quaternionf var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      var1.w = this.w;
      return var1;
   }

   public Quaternionf identity() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 1.0F;
      return this;
   }

   public Quaternionf rotateXYZ(float var1, float var2, float var3) {
      return this.rotateXYZ(var1, var2, var3, this);
   }

   public Quaternionf rotateXYZ(float var1, float var2, float var3, Quaternionf var4) {
      float var5 = Math.sin(var1 * 0.5F);
      float var6 = Math.cosFromSin(var5, var1 * 0.5F);
      float var7 = Math.sin(var2 * 0.5F);
      float var8 = Math.cosFromSin(var7, var2 * 0.5F);
      float var9 = Math.sin(var3 * 0.5F);
      float var10 = Math.cosFromSin(var9, var3 * 0.5F);
      float var11 = var8 * var10;
      float var12 = var7 * var9;
      float var13 = var7 * var10;
      float var14 = var8 * var9;
      float var15 = var6 * var11 - var5 * var12;
      float var16 = var5 * var11 + var6 * var12;
      float var17 = var6 * var13 - var5 * var14;
      float var18 = var6 * var14 + var5 * var13;
      return var4.set(Math.fma(this.w, var16, Math.fma(this.x, var15, Math.fma(this.y, var18, -this.z * var17))), Math.fma(this.w, var17, Math.fma(-this.x, var18, Math.fma(this.y, var15, this.z * var16))), Math.fma(this.w, var18, Math.fma(this.x, var17, Math.fma(-this.y, var16, this.z * var15))), Math.fma(this.w, var15, Math.fma(-this.x, var16, Math.fma(-this.y, var17, -this.z * var18))));
   }

   public Quaternionf rotateZYX(float var1, float var2, float var3) {
      return this.rotateZYX(var1, var2, var3, this);
   }

   public Quaternionf rotateZYX(float var1, float var2, float var3, Quaternionf var4) {
      float var5 = Math.sin(var3 * 0.5F);
      float var6 = Math.cosFromSin(var5, var3 * 0.5F);
      float var7 = Math.sin(var2 * 0.5F);
      float var8 = Math.cosFromSin(var7, var2 * 0.5F);
      float var9 = Math.sin(var1 * 0.5F);
      float var10 = Math.cosFromSin(var9, var1 * 0.5F);
      float var11 = var8 * var10;
      float var12 = var7 * var9;
      float var13 = var7 * var10;
      float var14 = var8 * var9;
      float var15 = var6 * var11 + var5 * var12;
      float var16 = var5 * var11 - var6 * var12;
      float var17 = var6 * var13 + var5 * var14;
      float var18 = var6 * var14 - var5 * var13;
      return var4.set(Math.fma(this.w, var16, Math.fma(this.x, var15, Math.fma(this.y, var18, -this.z * var17))), Math.fma(this.w, var17, Math.fma(-this.x, var18, Math.fma(this.y, var15, this.z * var16))), Math.fma(this.w, var18, Math.fma(this.x, var17, Math.fma(-this.y, var16, this.z * var15))), Math.fma(this.w, var15, Math.fma(-this.x, var16, Math.fma(-this.y, var17, -this.z * var18))));
   }

   public Quaternionf rotateYXZ(float var1, float var2, float var3) {
      return this.rotateYXZ(var1, var2, var3, this);
   }

   public Quaternionf rotateYXZ(float var1, float var2, float var3, Quaternionf var4) {
      float var5 = Math.sin(var2 * 0.5F);
      float var6 = Math.cosFromSin(var5, var2 * 0.5F);
      float var7 = Math.sin(var1 * 0.5F);
      float var8 = Math.cosFromSin(var7, var1 * 0.5F);
      float var9 = Math.sin(var3 * 0.5F);
      float var10 = Math.cosFromSin(var9, var3 * 0.5F);
      float var11 = var8 * var5;
      float var12 = var7 * var6;
      float var13 = var7 * var5;
      float var14 = var8 * var6;
      float var15 = var11 * var10 + var12 * var9;
      float var16 = var12 * var10 - var11 * var9;
      float var17 = var14 * var9 - var13 * var10;
      float var18 = var14 * var10 + var13 * var9;
      return var4.set(Math.fma(this.w, var15, Math.fma(this.x, var18, Math.fma(this.y, var17, -this.z * var16))), Math.fma(this.w, var16, Math.fma(-this.x, var17, Math.fma(this.y, var18, this.z * var15))), Math.fma(this.w, var17, Math.fma(this.x, var16, Math.fma(-this.y, var15, this.z * var18))), Math.fma(this.w, var18, Math.fma(-this.x, var15, Math.fma(-this.y, var16, -this.z * var17))));
   }

   public Vector3f getEulerAnglesXYZ(Vector3f var1) {
      var1.x = Math.atan2(2.0F * (this.x * this.w - this.y * this.z), 1.0F - 2.0F * (this.x * this.x + this.y * this.y));
      var1.y = Math.safeAsin(2.0F * (this.x * this.z + this.y * this.w));
      var1.z = Math.atan2(2.0F * (this.z * this.w - this.x * this.y), 1.0F - 2.0F * (this.y * this.y + this.z * this.z));
      return var1;
   }

   public float lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
   }

   public Quaternionf rotationXYZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var1 * 0.5F);
      float var5 = Math.cosFromSin(var4, var1 * 0.5F);
      float var6 = Math.sin(var2 * 0.5F);
      float var7 = Math.cosFromSin(var6, var2 * 0.5F);
      float var8 = Math.sin(var3 * 0.5F);
      float var9 = Math.cosFromSin(var8, var3 * 0.5F);
      float var10 = var7 * var9;
      float var11 = var6 * var8;
      float var12 = var6 * var9;
      float var13 = var7 * var8;
      this.w = var5 * var10 - var4 * var11;
      this.x = var4 * var10 + var5 * var11;
      this.y = var5 * var12 - var4 * var13;
      this.z = var5 * var13 + var4 * var12;
      return this;
   }

   public Quaternionf rotationZYX(float var1, float var2, float var3) {
      float var4 = Math.sin(var3 * 0.5F);
      float var5 = Math.cosFromSin(var4, var3 * 0.5F);
      float var6 = Math.sin(var2 * 0.5F);
      float var7 = Math.cosFromSin(var6, var2 * 0.5F);
      float var8 = Math.sin(var1 * 0.5F);
      float var9 = Math.cosFromSin(var8, var1 * 0.5F);
      float var10 = var7 * var9;
      float var11 = var6 * var8;
      float var12 = var6 * var9;
      float var13 = var7 * var8;
      this.w = var5 * var10 + var4 * var11;
      this.x = var4 * var10 - var5 * var11;
      this.y = var5 * var12 + var4 * var13;
      this.z = var5 * var13 - var4 * var12;
      return this;
   }

   public Quaternionf rotationYXZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var2 * 0.5F);
      float var5 = Math.cosFromSin(var4, var2 * 0.5F);
      float var6 = Math.sin(var1 * 0.5F);
      float var7 = Math.cosFromSin(var6, var1 * 0.5F);
      float var8 = Math.sin(var3 * 0.5F);
      float var9 = Math.cosFromSin(var8, var3 * 0.5F);
      float var10 = var7 * var4;
      float var11 = var6 * var5;
      float var12 = var6 * var4;
      float var13 = var7 * var5;
      this.x = var10 * var9 + var11 * var8;
      this.y = var11 * var9 - var10 * var8;
      this.z = var13 * var8 - var12 * var9;
      this.w = var13 * var9 + var12 * var8;
      return this;
   }

   public Quaternionf slerp(Quaternionfc var1, float var2) {
      return this.slerp(var1, var2, this);
   }

   public Quaternionf slerp(Quaternionfc var1, float var2, Quaternionf var3) {
      float var4 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
      float var5 = Math.abs(var4);
      float var6;
      float var7;
      if (1.0F - var5 > 1.0E-6F) {
         float var8 = 1.0F - var5 * var5;
         float var9 = Math.invsqrt(var8);
         float var10 = Math.atan2(var8 * var9, var5);
         var6 = (float)(Math.sin((1.0D - (double)var2) * (double)var10) * (double)var9);
         var7 = Math.sin(var2 * var10) * var9;
      } else {
         var6 = 1.0F - var2;
         var7 = var2;
      }

      var7 = var4 >= 0.0F ? var7 : -var7;
      var3.x = Math.fma(var6, this.x, var7 * var1.x());
      var3.y = Math.fma(var6, this.y, var7 * var1.y());
      var3.z = Math.fma(var6, this.z, var7 * var1.z());
      var3.w = Math.fma(var6, this.w, var7 * var1.w());
      return var3;
   }

   public static Quaternionfc slerp(Quaternionf[] var0, float[] var1, Quaternionf var2) {
      var2.set((Quaternionfc)var0[0]);
      float var3 = var1[0];

      for(int var4 = 1; var4 < var0.length; ++var4) {
         float var6 = var1[var4];
         float var7 = var6 / (var3 + var6);
         var3 += var6;
         var2.slerp(var0[var4], var7);
      }

      return var2;
   }

   public Quaternionf scale(float var1) {
      return this.scale(var1, this);
   }

   public Quaternionf scale(float var1, Quaternionf var2) {
      float var3 = Math.sqrt(var1);
      var2.x = var3 * this.x;
      var2.y = var3 * this.y;
      var2.z = var3 * this.z;
      var2.w = var3 * this.w;
      return var2;
   }

   public Quaternionf scaling(float var1) {
      float var2 = Math.sqrt(var1);
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = var2;
      return this;
   }

   public Quaternionf integrate(float var1, float var2, float var3, float var4) {
      return this.integrate(var1, var2, var3, var4, this);
   }

   public Quaternionf integrate(float var1, float var2, float var3, float var4, Quaternionf var5) {
      float var6 = var1 * var2 * 0.5F;
      float var7 = var1 * var3 * 0.5F;
      float var8 = var1 * var4 * 0.5F;
      float var9 = var6 * var6 + var7 * var7 + var8 * var8;
      float var10;
      float var14;
      if (var9 * var9 / 24.0F < 1.0E-8F) {
         var14 = 1.0F - var9 * 0.5F;
         var10 = 1.0F - var9 / 6.0F;
      } else {
         float var15 = Math.sqrt(var9);
         float var16 = Math.sin(var15);
         var10 = var16 / var15;
         var14 = Math.cosFromSin(var16, var15);
      }

      float var11 = var6 * var10;
      float var12 = var7 * var10;
      float var13 = var8 * var10;
      return var5.set(Math.fma(var14, this.x, Math.fma(var11, this.w, Math.fma(var12, this.z, -var13 * this.y))), Math.fma(var14, this.y, Math.fma(-var11, this.z, Math.fma(var12, this.w, var13 * this.x))), Math.fma(var14, this.z, Math.fma(var11, this.y, Math.fma(-var12, this.x, var13 * this.w))), Math.fma(var14, this.w, Math.fma(-var11, this.x, Math.fma(-var12, this.y, -var13 * this.z))));
   }

   public Quaternionf nlerp(Quaternionfc var1, float var2) {
      return this.nlerp(var1, var2, this);
   }

   public Quaternionf nlerp(Quaternionfc var1, float var2, Quaternionf var3) {
      float var4 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
      float var5 = 1.0F - var2;
      float var6 = var4 >= 0.0F ? var2 : -var2;
      var3.x = Math.fma(var5, this.x, var6 * var1.x());
      var3.y = Math.fma(var5, this.y, var6 * var1.y());
      var3.z = Math.fma(var5, this.z, var6 * var1.z());
      var3.w = Math.fma(var5, this.w, var6 * var1.w());
      float var7 = Math.invsqrt(Math.fma(var3.x, var3.x, Math.fma(var3.y, var3.y, Math.fma(var3.z, var3.z, var3.w * var3.w))));
      var3.x *= var7;
      var3.y *= var7;
      var3.z *= var7;
      var3.w *= var7;
      return var3;
   }

   public static Quaternionfc nlerp(Quaternionfc[] var0, float[] var1, Quaternionf var2) {
      var2.set(var0[0]);
      float var3 = var1[0];

      for(int var4 = 1; var4 < var0.length; ++var4) {
         float var6 = var1[var4];
         float var7 = var6 / (var3 + var6);
         var3 += var6;
         var2.nlerp(var0[var4], var7);
      }

      return var2;
   }

   public Quaternionf nlerpIterative(Quaternionfc var1, float var2, float var3, Quaternionf var4) {
      float var5 = this.x;
      float var6 = this.y;
      float var7 = this.z;
      float var8 = this.w;
      float var9 = var1.x();
      float var10 = var1.y();
      float var11 = var1.z();
      float var12 = var1.w();
      float var13 = Math.fma(var5, var9, Math.fma(var6, var10, Math.fma(var7, var11, var8 * var12)));
      float var14 = Math.abs(var13);
      if (0.999999F < var14) {
         return var4.set((Quaternionfc)this);
      } else {
         float var15;
         float var16;
         float var17;
         float var18;
         for(var15 = var2; var14 < var3; var14 = Math.abs(var13)) {
            var16 = 0.5F;
            var17 = var13 >= 0.0F ? 0.5F : -0.5F;
            if (var15 < 0.5F) {
               var9 = Math.fma(var16, var9, var17 * var5);
               var10 = Math.fma(var16, var10, var17 * var6);
               var11 = Math.fma(var16, var11, var17 * var7);
               var12 = Math.fma(var16, var12, var17 * var8);
               var18 = Math.invsqrt(Math.fma(var9, var9, Math.fma(var10, var10, Math.fma(var11, var11, var12 * var12))));
               var9 *= var18;
               var10 *= var18;
               var11 *= var18;
               var12 *= var18;
               var15 += var15;
            } else {
               var5 = Math.fma(var16, var5, var17 * var9);
               var6 = Math.fma(var16, var6, var17 * var10);
               var7 = Math.fma(var16, var7, var17 * var11);
               var8 = Math.fma(var16, var8, var17 * var12);
               var18 = Math.invsqrt(Math.fma(var5, var5, Math.fma(var6, var6, Math.fma(var7, var7, var8 * var8))));
               var5 *= var18;
               var6 *= var18;
               var7 *= var18;
               var8 *= var18;
               var15 = var15 + var15 - 1.0F;
            }

            var13 = Math.fma(var5, var9, Math.fma(var6, var10, Math.fma(var7, var11, var8 * var12)));
         }

         var16 = 1.0F - var15;
         var17 = var13 >= 0.0F ? var15 : -var15;
         var18 = Math.fma(var16, var5, var17 * var9);
         float var19 = Math.fma(var16, var6, var17 * var10);
         float var20 = Math.fma(var16, var7, var17 * var11);
         float var21 = Math.fma(var16, var8, var17 * var12);
         float var22 = Math.invsqrt(Math.fma(var18, var18, Math.fma(var19, var19, Math.fma(var20, var20, var21 * var21))));
         var4.x = var18 * var22;
         var4.y = var19 * var22;
         var4.z = var20 * var22;
         var4.w = var21 * var22;
         return var4;
      }
   }

   public Quaternionf nlerpIterative(Quaternionfc var1, float var2, float var3) {
      return this.nlerpIterative(var1, var2, var3, this);
   }

   public static Quaternionfc nlerpIterative(Quaternionf[] var0, float[] var1, float var2, Quaternionf var3) {
      var3.set((Quaternionfc)var0[0]);
      float var4 = var1[0];

      for(int var5 = 1; var5 < var0.length; ++var5) {
         float var7 = var1[var5];
         float var8 = var7 / (var4 + var7);
         var4 += var7;
         var3.nlerpIterative(var0[var5], var8, var2);
      }

      return var3;
   }

   public Quaternionf lookAlong(Vector3fc var1, Vector3fc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Quaternionf lookAlong(Vector3fc var1, Vector3fc var2, Quaternionf var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Quaternionf lookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.lookAlong(var1, var2, var3, var4, var5, var6, this);
   }

   public Quaternionf lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Quaternionf var7) {
      float var8 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var9 = -var1 * var8;
      float var10 = -var2 * var8;
      float var11 = -var3 * var8;
      float var12 = var5 * var11 - var6 * var10;
      float var13 = var6 * var9 - var4 * var11;
      float var14 = var4 * var10 - var5 * var9;
      float var15 = Math.invsqrt(var12 * var12 + var13 * var13 + var14 * var14);
      var12 *= var15;
      var13 *= var15;
      var14 *= var15;
      float var16 = var10 * var14 - var11 * var13;
      float var17 = var11 * var12 - var9 * var14;
      float var18 = var9 * var13 - var10 * var12;
      double var25 = (double)(var12 + var17 + var11);
      float var19;
      float var20;
      float var21;
      float var22;
      double var23;
      if (var25 >= 0.0D) {
         var23 = Math.sqrt(var25 + 1.0D);
         var22 = (float)(var23 * 0.5D);
         var23 = 0.5D / var23;
         var19 = (float)((double)(var10 - var18) * var23);
         var20 = (float)((double)(var14 - var9) * var23);
         var21 = (float)((double)(var16 - var13) * var23);
      } else if (var12 > var17 && var12 > var11) {
         var23 = Math.sqrt(1.0D + (double)var12 - (double)var17 - (double)var11);
         var19 = (float)(var23 * 0.5D);
         var23 = 0.5D / var23;
         var20 = (float)((double)(var13 + var16) * var23);
         var21 = (float)((double)(var9 + var14) * var23);
         var22 = (float)((double)(var10 - var18) * var23);
      } else if (var17 > var11) {
         var23 = Math.sqrt(1.0D + (double)var17 - (double)var12 - (double)var11);
         var20 = (float)(var23 * 0.5D);
         var23 = 0.5D / var23;
         var19 = (float)((double)(var13 + var16) * var23);
         var21 = (float)((double)(var18 + var10) * var23);
         var22 = (float)((double)(var14 - var9) * var23);
      } else {
         var23 = Math.sqrt(1.0D + (double)var11 - (double)var12 - (double)var17);
         var21 = (float)(var23 * 0.5D);
         var23 = 0.5D / var23;
         var19 = (float)((double)(var9 + var14) * var23);
         var20 = (float)((double)(var18 + var10) * var23);
         var22 = (float)((double)(var16 - var13) * var23);
      }

      return var7.set(Math.fma(this.w, var19, Math.fma(this.x, var22, Math.fma(this.y, var21, -this.z * var20))), Math.fma(this.w, var20, Math.fma(-this.x, var21, Math.fma(this.y, var22, this.z * var19))), Math.fma(this.w, var21, Math.fma(this.x, var20, Math.fma(-this.y, var19, this.z * var22))), Math.fma(this.w, var22, Math.fma(-this.x, var19, Math.fma(-this.y, var20, -this.z * var21))));
   }

   public Quaternionf rotationTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Math.invsqrt(Math.fma(var1, var1, Math.fma(var2, var2, var3 * var3)));
      float var8 = Math.invsqrt(Math.fma(var4, var4, Math.fma(var5, var5, var6 * var6)));
      float var9 = var1 * var7;
      float var10 = var2 * var7;
      float var11 = var3 * var7;
      float var12 = var4 * var8;
      float var13 = var5 * var8;
      float var14 = var6 * var8;
      float var15 = var9 * var12 + var10 * var13 + var11 * var14;
      float var16;
      float var17;
      float var18;
      float var19;
      if (var15 < -0.999999F) {
         var16 = var10;
         var17 = -var9;
         var18 = 0.0F;
         var19 = 0.0F;
         if (var10 * var10 + var17 * var17 == 0.0F) {
            var16 = 0.0F;
            var17 = var11;
            var18 = -var10;
            var19 = 0.0F;
         }

         this.x = var16;
         this.y = var17;
         this.z = var18;
         this.w = 0.0F;
      } else {
         float var20 = Math.sqrt((1.0F + var15) * 2.0F);
         float var21 = 1.0F / var20;
         float var22 = var10 * var14 - var11 * var13;
         float var23 = var11 * var12 - var9 * var14;
         float var24 = var9 * var13 - var10 * var12;
         var16 = var22 * var21;
         var17 = var23 * var21;
         var18 = var24 * var21;
         var19 = var20 * 0.5F;
         float var25 = Math.invsqrt(Math.fma(var16, var16, Math.fma(var17, var17, Math.fma(var18, var18, var19 * var19))));
         this.x = var16 * var25;
         this.y = var17 * var25;
         this.z = var18 * var25;
         this.w = var19 * var25;
      }

      return this;
   }

   public Quaternionf rotationTo(Vector3fc var1, Vector3fc var2) {
      return this.rotationTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Quaternionf rotateTo(float var1, float var2, float var3, float var4, float var5, float var6, Quaternionf var7) {
      float var8 = Math.invsqrt(Math.fma(var1, var1, Math.fma(var2, var2, var3 * var3)));
      float var9 = Math.invsqrt(Math.fma(var4, var4, Math.fma(var5, var5, var6 * var6)));
      float var10 = var1 * var8;
      float var11 = var2 * var8;
      float var12 = var3 * var8;
      float var13 = var4 * var9;
      float var14 = var5 * var9;
      float var15 = var6 * var9;
      float var16 = var10 * var13 + var11 * var14 + var12 * var15;
      float var17;
      float var18;
      float var19;
      float var20;
      if (var16 < -0.999999F) {
         var17 = var11;
         var18 = -var10;
         var19 = 0.0F;
         var20 = 0.0F;
         if (var11 * var11 + var18 * var18 == 0.0F) {
            var17 = 0.0F;
            var18 = var12;
            var19 = -var11;
            var20 = 0.0F;
         }
      } else {
         float var21 = Math.sqrt((1.0F + var16) * 2.0F);
         float var22 = 1.0F / var21;
         float var23 = var11 * var15 - var12 * var14;
         float var24 = var12 * var13 - var10 * var15;
         float var25 = var10 * var14 - var11 * var13;
         var17 = var23 * var22;
         var18 = var24 * var22;
         var19 = var25 * var22;
         var20 = var21 * 0.5F;
         float var26 = Math.invsqrt(Math.fma(var17, var17, Math.fma(var18, var18, Math.fma(var19, var19, var20 * var20))));
         var17 *= var26;
         var18 *= var26;
         var19 *= var26;
         var20 *= var26;
      }

      return var7.set(Math.fma(this.w, var17, Math.fma(this.x, var20, Math.fma(this.y, var19, -this.z * var18))), Math.fma(this.w, var18, Math.fma(-this.x, var19, Math.fma(this.y, var20, this.z * var17))), Math.fma(this.w, var19, Math.fma(this.x, var18, Math.fma(-this.y, var17, this.z * var20))), Math.fma(this.w, var20, Math.fma(-this.x, var17, Math.fma(-this.y, var18, -this.z * var19))));
   }

   public Quaternionf rotateTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.rotateTo(var1, var2, var3, var4, var5, var6, this);
   }

   public Quaternionf rotateTo(Vector3fc var1, Vector3fc var2, Quaternionf var3) {
      return this.rotateTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Quaternionf rotateTo(Vector3fc var1, Vector3fc var2) {
      return this.rotateTo(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Quaternionf rotateX(float var1) {
      return this.rotateX(var1, this);
   }

   public Quaternionf rotateX(float var1, Quaternionf var2) {
      float var3 = Math.sin(var1 * 0.5F);
      float var4 = Math.cosFromSin(var3, var1 * 0.5F);
      return var2.set(this.w * var3 + this.x * var4, this.y * var4 + this.z * var3, this.z * var4 - this.y * var3, this.w * var4 - this.x * var3);
   }

   public Quaternionf rotateY(float var1) {
      return this.rotateY(var1, this);
   }

   public Quaternionf rotateY(float var1, Quaternionf var2) {
      float var3 = Math.sin(var1 * 0.5F);
      float var4 = Math.cosFromSin(var3, var1 * 0.5F);
      return var2.set(this.x * var4 - this.z * var3, this.w * var3 + this.y * var4, this.x * var3 + this.z * var4, this.w * var4 - this.y * var3);
   }

   public Quaternionf rotateZ(float var1) {
      return this.rotateZ(var1, this);
   }

   public Quaternionf rotateZ(float var1, Quaternionf var2) {
      float var3 = Math.sin(var1 * 0.5F);
      float var4 = Math.cosFromSin(var3, var1 * 0.5F);
      return var2.set(this.x * var4 + this.y * var3, this.y * var4 - this.x * var3, this.w * var3 + this.z * var4, this.w * var4 - this.z * var3);
   }

   public Quaternionf rotateLocalX(float var1) {
      return this.rotateLocalX(var1, this);
   }

   public Quaternionf rotateLocalX(float var1, Quaternionf var2) {
      float var3 = var1 * 0.5F;
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      var2.set(var5 * this.x + var4 * this.w, var5 * this.y - var4 * this.z, var5 * this.z + var4 * this.y, var5 * this.w - var4 * this.x);
      return var2;
   }

   public Quaternionf rotateLocalY(float var1) {
      return this.rotateLocalY(var1, this);
   }

   public Quaternionf rotateLocalY(float var1, Quaternionf var2) {
      float var3 = var1 * 0.5F;
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      var2.set(var5 * this.x + var4 * this.z, var5 * this.y + var4 * this.w, var5 * this.z - var4 * this.x, var5 * this.w - var4 * this.y);
      return var2;
   }

   public Quaternionf rotateLocalZ(float var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Quaternionf rotateLocalZ(float var1, Quaternionf var2) {
      float var3 = var1 * 0.5F;
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      var2.set(var5 * this.x - var4 * this.y, var5 * this.y + var4 * this.x, var5 * this.z + var4 * this.w, var5 * this.w - var4 * this.z);
      return var2;
   }

   public Quaternionf rotateAxis(float var1, float var2, float var3, float var4, Quaternionf var5) {
      float var6 = var1 / 2.0F;
      float var7 = Math.sin(var6);
      float var8 = Math.invsqrt(Math.fma(var2, var2, Math.fma(var3, var3, var4 * var4)));
      float var9 = var2 * var8 * var7;
      float var10 = var3 * var8 * var7;
      float var11 = var4 * var8 * var7;
      float var12 = Math.cosFromSin(var7, var6);
      return var5.set(Math.fma(this.w, var9, Math.fma(this.x, var12, Math.fma(this.y, var11, -this.z * var10))), Math.fma(this.w, var10, Math.fma(-this.x, var11, Math.fma(this.y, var12, this.z * var9))), Math.fma(this.w, var11, Math.fma(this.x, var10, Math.fma(-this.y, var9, this.z * var12))), Math.fma(this.w, var12, Math.fma(-this.x, var9, Math.fma(-this.y, var10, -this.z * var11))));
   }

   public Quaternionf rotateAxis(float var1, Vector3fc var2, Quaternionf var3) {
      return this.rotateAxis(var1, var2.x(), var2.y(), var2.z(), var3);
   }

   public Quaternionf rotateAxis(float var1, Vector3fc var2) {
      return this.rotateAxis(var1, var2.x(), var2.y(), var2.z(), this);
   }

   public Quaternionf rotateAxis(float var1, float var2, float var3, float var4) {
      return this.rotateAxis(var1, var2, var3, var4, this);
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format((double)this.x, var1);
      return "(" + var10000 + " " + Runtime.format((double)this.y, var1) + " " + Runtime.format((double)this.z, var1) + " " + Runtime.format((double)this.w, var1) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.x);
      var1.writeFloat(this.y);
      var1.writeFloat(this.z);
      var1.writeFloat(this.w);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readFloat();
      this.y = var1.readFloat();
      this.z = var1.readFloat();
      this.w = var1.readFloat();
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.w);
      var2 = 31 * var2 + Float.floatToIntBits(this.x);
      var2 = 31 * var2 + Float.floatToIntBits(this.y);
      var2 = 31 * var2 + Float.floatToIntBits(this.z);
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Quaternionf var2 = (Quaternionf)var1;
         if (Float.floatToIntBits(this.w) != Float.floatToIntBits(var2.w)) {
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

   public Quaternionf difference(Quaternionf var1) {
      return this.difference(var1, this);
   }

   public Quaternionf difference(Quaternionfc var1, Quaternionf var2) {
      float var3 = 1.0F / this.lengthSquared();
      float var4 = -this.x * var3;
      float var5 = -this.y * var3;
      float var6 = -this.z * var3;
      float var7 = this.w * var3;
      var2.set(Math.fma(var7, var1.x(), Math.fma(var4, var1.w(), Math.fma(var5, var1.z(), -var6 * var1.y()))), Math.fma(var7, var1.y(), Math.fma(-var4, var1.z(), Math.fma(var5, var1.w(), var6 * var1.x()))), Math.fma(var7, var1.z(), Math.fma(var4, var1.y(), Math.fma(-var5, var1.x(), var6 * var1.w()))), Math.fma(var7, var1.w(), Math.fma(-var4, var1.x(), Math.fma(-var5, var1.y(), -var6 * var1.z()))));
      return var2;
   }

   public Vector3f positiveX(Vector3f var1) {
      float var2 = 1.0F / this.lengthSquared();
      float var3 = -this.x * var2;
      float var4 = -this.y * var2;
      float var5 = -this.z * var2;
      float var6 = this.w * var2;
      float var7 = var4 + var4;
      float var8 = var5 + var5;
      var1.x = -var4 * var7 - var5 * var8 + 1.0F;
      var1.y = var3 * var7 + var6 * var8;
      var1.z = var3 * var8 - var6 * var7;
      return var1;
   }

   public Vector3f normalizedPositiveX(Vector3f var1) {
      float var2 = this.y + this.y;
      float var3 = this.z + this.z;
      var1.x = -this.y * var2 - this.z * var3 + 1.0F;
      var1.y = this.x * var2 - this.w * var3;
      var1.z = this.x * var3 + this.w * var2;
      return var1;
   }

   public Vector3f positiveY(Vector3f var1) {
      float var2 = 1.0F / this.lengthSquared();
      float var3 = -this.x * var2;
      float var4 = -this.y * var2;
      float var5 = -this.z * var2;
      float var6 = this.w * var2;
      float var7 = var3 + var3;
      float var8 = var4 + var4;
      float var9 = var5 + var5;
      var1.x = var3 * var8 - var6 * var9;
      var1.y = -var3 * var7 - var5 * var9 + 1.0F;
      var1.z = var4 * var9 + var6 * var7;
      return var1;
   }

   public Vector3f normalizedPositiveY(Vector3f var1) {
      float var2 = this.x + this.x;
      float var3 = this.y + this.y;
      float var4 = this.z + this.z;
      var1.x = this.x * var3 + this.w * var4;
      var1.y = -this.x * var2 - this.z * var4 + 1.0F;
      var1.z = this.y * var4 - this.w * var2;
      return var1;
   }

   public Vector3f positiveZ(Vector3f var1) {
      float var2 = 1.0F / this.lengthSquared();
      float var3 = -this.x * var2;
      float var4 = -this.y * var2;
      float var5 = -this.z * var2;
      float var6 = this.w * var2;
      float var7 = var3 + var3;
      float var8 = var4 + var4;
      float var9 = var5 + var5;
      var1.x = var3 * var9 + var6 * var8;
      var1.y = var4 * var9 - var6 * var7;
      var1.z = -var3 * var7 - var4 * var8 + 1.0F;
      return var1;
   }

   public Vector3f normalizedPositiveZ(Vector3f var1) {
      float var2 = this.x + this.x;
      float var3 = this.y + this.y;
      float var4 = this.z + this.z;
      var1.x = this.x * var4 - this.w * var3;
      var1.y = this.y * var4 + this.w * var2;
      var1.z = -this.x * var2 - this.y * var3 + 1.0F;
      return var1;
   }

   public Quaternionf conjugateBy(Quaternionfc var1) {
      return this.conjugateBy(var1, this);
   }

   public Quaternionf conjugateBy(Quaternionfc var1, Quaternionf var2) {
      float var3 = 1.0F / var1.lengthSquared();
      float var4 = -var1.x() * var3;
      float var5 = -var1.y() * var3;
      float var6 = -var1.z() * var3;
      float var7 = var1.w() * var3;
      float var8 = Math.fma(var1.w(), this.x, Math.fma(var1.x(), this.w, Math.fma(var1.y(), this.z, -var1.z() * this.y)));
      float var9 = Math.fma(var1.w(), this.y, Math.fma(-var1.x(), this.z, Math.fma(var1.y(), this.w, var1.z() * this.x)));
      float var10 = Math.fma(var1.w(), this.z, Math.fma(var1.x(), this.y, Math.fma(-var1.y(), this.x, var1.z() * this.w)));
      float var11 = Math.fma(var1.w(), this.w, Math.fma(-var1.x(), this.x, Math.fma(-var1.y(), this.y, -var1.z() * this.z)));
      return var2.set(Math.fma(var11, var4, Math.fma(var8, var7, Math.fma(var9, var6, -var10 * var5))), Math.fma(var11, var5, Math.fma(-var8, var6, Math.fma(var9, var7, var10 * var4))), Math.fma(var11, var6, Math.fma(var8, var5, Math.fma(-var9, var4, var10 * var7))), Math.fma(var11, var7, Math.fma(-var8, var4, Math.fma(-var9, var5, -var10 * var6))));
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
   }

   public boolean equals(Quaternionfc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Quaternionfc)) {
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

   public boolean equals(float var1, float var2, float var3, float var4) {
      if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var1)) {
         return false;
      } else if (Float.floatToIntBits(this.y) != Float.floatToIntBits(var2)) {
         return false;
      } else if (Float.floatToIntBits(this.z) != Float.floatToIntBits(var3)) {
         return false;
      } else {
         return Float.floatToIntBits(this.w) == Float.floatToIntBits(var4);
      }
   }
}
