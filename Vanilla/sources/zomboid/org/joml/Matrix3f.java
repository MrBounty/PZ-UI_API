package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix3f implements Externalizable, Matrix3fc {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m02;
   public float m10;
   public float m11;
   public float m12;
   public float m20;
   public float m21;
   public float m22;

   public Matrix3f() {
      this.m00 = 1.0F;
      this.m11 = 1.0F;
      this.m22 = 1.0F;
   }

   public Matrix3f(Matrix2fc var1) {
      this.set(var1);
   }

   public Matrix3f(Matrix3fc var1) {
      this.set(var1);
   }

   public Matrix3f(Matrix4fc var1) {
      this.set(var1);
   }

   public Matrix3f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
   }

   public Matrix3f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Matrix3f(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      this.set(var1, var2, var3);
   }

   public float m00() {
      return this.m00;
   }

   public float m01() {
      return this.m01;
   }

   public float m02() {
      return this.m02;
   }

   public float m10() {
      return this.m10;
   }

   public float m11() {
      return this.m11;
   }

   public float m12() {
      return this.m12;
   }

   public float m20() {
      return this.m20;
   }

   public float m21() {
      return this.m21;
   }

   public float m22() {
      return this.m22;
   }

   public Matrix3f m00(float var1) {
      this.m00 = var1;
      return this;
   }

   public Matrix3f m01(float var1) {
      this.m01 = var1;
      return this;
   }

   public Matrix3f m02(float var1) {
      this.m02 = var1;
      return this;
   }

   public Matrix3f m10(float var1) {
      this.m10 = var1;
      return this;
   }

   public Matrix3f m11(float var1) {
      this.m11 = var1;
      return this;
   }

   public Matrix3f m12(float var1) {
      this.m12 = var1;
      return this;
   }

   public Matrix3f m20(float var1) {
      this.m20 = var1;
      return this;
   }

   public Matrix3f m21(float var1) {
      this.m21 = var1;
      return this;
   }

   public Matrix3f m22(float var1) {
      this.m22 = var1;
      return this;
   }

   Matrix3f _m00(float var1) {
      this.m00 = var1;
      return this;
   }

   Matrix3f _m01(float var1) {
      this.m01 = var1;
      return this;
   }

   Matrix3f _m02(float var1) {
      this.m02 = var1;
      return this;
   }

   Matrix3f _m10(float var1) {
      this.m10 = var1;
      return this;
   }

   Matrix3f _m11(float var1) {
      this.m11 = var1;
      return this;
   }

   Matrix3f _m12(float var1) {
      this.m12 = var1;
      return this;
   }

   Matrix3f _m20(float var1) {
      this.m20 = var1;
      return this;
   }

   Matrix3f _m21(float var1) {
      this.m21 = var1;
      return this;
   }

   Matrix3f _m22(float var1) {
      this.m22 = var1;
      return this;
   }

   public Matrix3f set(Matrix3fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
   }

   public Matrix3f setTransposed(Matrix3fc var1) {
      float var2 = var1.m01();
      float var3 = var1.m21();
      float var4 = var1.m02();
      float var5 = var1.m12();
      return this._m00(var1.m00())._m01(var1.m10())._m02(var1.m20())._m10(var2)._m11(var1.m11())._m12(var3)._m20(var4)._m21(var5)._m22(var1.m22());
   }

   public Matrix3f set(Matrix4x3fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      return this;
   }

   public Matrix3f set(Matrix4fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      return this;
   }

   public Matrix3f set(Matrix2fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = 0.0F;
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
      return this;
   }

   public Matrix3f set(AxisAngle4f var1) {
      float var2 = var1.x;
      float var3 = var1.y;
      float var4 = var1.z;
      float var5 = var1.angle;
      float var6 = Math.invsqrt(var2 * var2 + var3 * var3 + var4 * var4);
      var2 *= var6;
      var3 *= var6;
      var4 *= var6;
      float var7 = Math.sin(var5);
      float var8 = Math.cosFromSin(var7, var5);
      float var9 = 1.0F - var8;
      this.m00 = var8 + var2 * var2 * var9;
      this.m11 = var8 + var3 * var3 * var9;
      this.m22 = var8 + var4 * var4 * var9;
      float var10 = var2 * var3 * var9;
      float var11 = var4 * var7;
      this.m10 = var10 - var11;
      this.m01 = var10 + var11;
      var10 = var2 * var4 * var9;
      var11 = var3 * var7;
      this.m20 = var10 + var11;
      this.m02 = var10 - var11;
      var10 = var3 * var4 * var9;
      var11 = var2 * var7;
      this.m21 = var10 - var11;
      this.m12 = var10 + var11;
      return this;
   }

   public Matrix3f set(AxisAngle4d var1) {
      double var2 = var1.x;
      double var4 = var1.y;
      double var6 = var1.z;
      double var8 = var1.angle;
      double var10 = Math.invsqrt(var2 * var2 + var4 * var4 + var6 * var6);
      var2 *= var10;
      var4 *= var10;
      var6 *= var10;
      double var12 = Math.sin(var8);
      double var14 = Math.cosFromSin(var12, var8);
      double var16 = 1.0D - var14;
      this.m00 = (float)(var14 + var2 * var2 * var16);
      this.m11 = (float)(var14 + var4 * var4 * var16);
      this.m22 = (float)(var14 + var6 * var6 * var16);
      double var18 = var2 * var4 * var16;
      double var20 = var6 * var12;
      this.m10 = (float)(var18 - var20);
      this.m01 = (float)(var18 + var20);
      var18 = var2 * var6 * var16;
      var20 = var4 * var12;
      this.m20 = (float)(var18 + var20);
      this.m02 = (float)(var18 - var20);
      var18 = var4 * var6 * var16;
      var20 = var2 * var12;
      this.m21 = (float)(var18 - var20);
      this.m12 = (float)(var18 + var20);
      return this;
   }

   public Matrix3f set(Quaternionfc var1) {
      return this.rotation(var1);
   }

   public Matrix3f set(Quaterniondc var1) {
      double var2 = var1.w() * var1.w();
      double var4 = var1.x() * var1.x();
      double var6 = var1.y() * var1.y();
      double var8 = var1.z() * var1.z();
      double var10 = var1.z() * var1.w();
      double var12 = var1.x() * var1.y();
      double var14 = var1.x() * var1.z();
      double var16 = var1.y() * var1.w();
      double var18 = var1.y() * var1.z();
      double var20 = var1.x() * var1.w();
      this.m00 = (float)(var2 + var4 - var8 - var6);
      this.m01 = (float)(var12 + var10 + var10 + var12);
      this.m02 = (float)(var14 - var16 + var14 - var16);
      this.m10 = (float)(-var10 + var12 - var10 + var12);
      this.m11 = (float)(var6 - var8 + var2 - var4);
      this.m12 = (float)(var18 + var18 + var20 + var20);
      this.m20 = (float)(var16 + var14 + var14 + var16);
      this.m21 = (float)(var18 + var18 - var20 - var20);
      this.m22 = (float)(var8 - var6 - var4 + var2);
      return this;
   }

   public Matrix3f mul(Matrix3fc var1) {
      return this.mul(var1, this);
   }

   public Matrix3f mul(Matrix3fc var1, Matrix3f var2) {
      float var3 = Math.fma(this.m00, var1.m00(), Math.fma(this.m10, var1.m01(), this.m20 * var1.m02()));
      float var4 = Math.fma(this.m01, var1.m00(), Math.fma(this.m11, var1.m01(), this.m21 * var1.m02()));
      float var5 = Math.fma(this.m02, var1.m00(), Math.fma(this.m12, var1.m01(), this.m22 * var1.m02()));
      float var6 = Math.fma(this.m00, var1.m10(), Math.fma(this.m10, var1.m11(), this.m20 * var1.m12()));
      float var7 = Math.fma(this.m01, var1.m10(), Math.fma(this.m11, var1.m11(), this.m21 * var1.m12()));
      float var8 = Math.fma(this.m02, var1.m10(), Math.fma(this.m12, var1.m11(), this.m22 * var1.m12()));
      float var9 = Math.fma(this.m00, var1.m20(), Math.fma(this.m10, var1.m21(), this.m20 * var1.m22()));
      float var10 = Math.fma(this.m01, var1.m20(), Math.fma(this.m11, var1.m21(), this.m21 * var1.m22()));
      float var11 = Math.fma(this.m02, var1.m20(), Math.fma(this.m12, var1.m21(), this.m22 * var1.m22()));
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m02 = var5;
      var2.m10 = var6;
      var2.m11 = var7;
      var2.m12 = var8;
      var2.m20 = var9;
      var2.m21 = var10;
      var2.m22 = var11;
      return var2;
   }

   public Matrix3f mulLocal(Matrix3fc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix3f mulLocal(Matrix3fc var1, Matrix3f var2) {
      float var3 = var1.m00() * this.m00 + var1.m10() * this.m01 + var1.m20() * this.m02;
      float var4 = var1.m01() * this.m00 + var1.m11() * this.m01 + var1.m21() * this.m02;
      float var5 = var1.m02() * this.m00 + var1.m12() * this.m01 + var1.m22() * this.m02;
      float var6 = var1.m00() * this.m10 + var1.m10() * this.m11 + var1.m20() * this.m12;
      float var7 = var1.m01() * this.m10 + var1.m11() * this.m11 + var1.m21() * this.m12;
      float var8 = var1.m02() * this.m10 + var1.m12() * this.m11 + var1.m22() * this.m12;
      float var9 = var1.m00() * this.m20 + var1.m10() * this.m21 + var1.m20() * this.m22;
      float var10 = var1.m01() * this.m20 + var1.m11() * this.m21 + var1.m21() * this.m22;
      float var11 = var1.m02() * this.m20 + var1.m12() * this.m21 + var1.m22() * this.m22;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m02 = var5;
      var2.m10 = var6;
      var2.m11 = var7;
      var2.m12 = var8;
      var2.m20 = var9;
      var2.m21 = var10;
      var2.m22 = var11;
      return var2;
   }

   public Matrix3f set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
      return this;
   }

   public Matrix3f set(float[] var1) {
      MemUtil.INSTANCE.copy((float[])var1, 0, (Matrix3f)this);
      return this;
   }

   public Matrix3f set(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m02 = var1.z();
      this.m10 = var2.x();
      this.m11 = var2.y();
      this.m12 = var2.z();
      this.m20 = var3.x();
      this.m21 = var3.y();
      this.m22 = var3.z();
      return this;
   }

   public float determinant() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public Matrix3f invert() {
      return this.invert(this);
   }

   public Matrix3f invert(Matrix3f var1) {
      float var2 = Math.fma(this.m00, this.m11, -this.m01 * this.m10);
      float var3 = Math.fma(this.m02, this.m10, -this.m00 * this.m12);
      float var4 = Math.fma(this.m01, this.m12, -this.m02 * this.m11);
      float var5 = Math.fma(var2, this.m22, Math.fma(var3, this.m21, var4 * this.m20));
      float var6 = 1.0F / var5;
      float var7 = Math.fma(this.m11, this.m22, -this.m21 * this.m12) * var6;
      float var8 = Math.fma(this.m21, this.m02, -this.m01 * this.m22) * var6;
      float var9 = var4 * var6;
      float var10 = Math.fma(this.m20, this.m12, -this.m10 * this.m22) * var6;
      float var11 = Math.fma(this.m00, this.m22, -this.m20 * this.m02) * var6;
      float var12 = var3 * var6;
      float var13 = Math.fma(this.m10, this.m21, -this.m20 * this.m11) * var6;
      float var14 = Math.fma(this.m20, this.m01, -this.m00 * this.m21) * var6;
      float var15 = var2 * var6;
      var1.m00 = var7;
      var1.m01 = var8;
      var1.m02 = var9;
      var1.m10 = var10;
      var1.m11 = var11;
      var1.m12 = var12;
      var1.m20 = var13;
      var1.m21 = var14;
      var1.m22 = var15;
      return var1;
   }

   public Matrix3f transpose() {
      return this.transpose(this);
   }

   public Matrix3f transpose(Matrix3f var1) {
      return var1.set(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
   }

   public String toString() {
      DecimalFormat var1 = new DecimalFormat(" 0.000E0;-");
      String var2 = this.toString(var1);
      StringBuffer var3 = new StringBuffer();
      int var4 = Integer.MIN_VALUE;

      for(int var5 = 0; var5 < var2.length(); ++var5) {
         char var6 = var2.charAt(var5);
         if (var6 == 'E') {
            var4 = var5;
         } else {
            if (var6 == ' ' && var4 == var5 - 1) {
               var3.append('+');
               continue;
            }

            if (Character.isDigit(var6) && var4 == var5 - 1) {
               var3.append('+');
            }
         }

         var3.append(var6);
      }

      return var3.toString();
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format((double)this.m00, var1);
      return var10000 + " " + Runtime.format((double)this.m10, var1) + " " + Runtime.format((double)this.m20, var1) + "\n" + Runtime.format((double)this.m01, var1) + " " + Runtime.format((double)this.m11, var1) + " " + Runtime.format((double)this.m21, var1) + "\n" + Runtime.format((double)this.m02, var1) + " " + Runtime.format((double)this.m12, var1) + " " + Runtime.format((double)this.m22, var1) + "\n";
   }

   public Matrix3f get(Matrix3f var1) {
      return var1.set((Matrix3fc)this);
   }

   public Matrix4f get(Matrix4f var1) {
      return var1.set((Matrix3fc)this);
   }

   public AxisAngle4f getRotation(AxisAngle4f var1) {
      return var1.set((Matrix3fc)this);
   }

   public Quaternionf getUnnormalizedRotation(Quaternionf var1) {
      return var1.setFromUnnormalized((Matrix3fc)this);
   }

   public Quaternionf getNormalizedRotation(Quaternionf var1) {
      return var1.setFromNormalized((Matrix3fc)this);
   }

   public Quaterniond getUnnormalizedRotation(Quaterniond var1) {
      return var1.setFromUnnormalized((Matrix3fc)this);
   }

   public Quaterniond getNormalizedRotation(Quaterniond var1) {
      return var1.setFromNormalized((Matrix3fc)this);
   }

   public FloatBuffer get(FloatBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public FloatBuffer get(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public ByteBuffer get(ByteBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public ByteBuffer get(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public FloatBuffer get3x4(FloatBuffer var1) {
      return this.get3x4(var1.position(), var1);
   }

   public FloatBuffer get3x4(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put3x4(this, var1, var2);
      return var2;
   }

   public ByteBuffer get3x4(ByteBuffer var1) {
      return this.get3x4(var1.position(), var1);
   }

   public ByteBuffer get3x4(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put3x4(this, var1, var2);
      return var2;
   }

   public FloatBuffer getTransposed(FloatBuffer var1) {
      return this.getTransposed(var1.position(), var1);
   }

   public FloatBuffer getTransposed(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public ByteBuffer getTransposed(ByteBuffer var1) {
      return this.getTransposed(var1.position(), var1);
   }

   public ByteBuffer getTransposed(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public Matrix3fc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public float[] get(float[] var1, int var2) {
      MemUtil.INSTANCE.copy(this, var1, var2);
      return var1;
   }

   public float[] get(float[] var1) {
      return this.get(var1, 0);
   }

   public Matrix3f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix3f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix3f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix3f zero() {
      MemUtil.INSTANCE.zero(this);
      return this;
   }

   public Matrix3f identity() {
      MemUtil.INSTANCE.identity(this);
      return this;
   }

   public Matrix3f scale(Vector3fc var1, Matrix3f var2) {
      return this.scale(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix3f scale(Vector3fc var1) {
      return this.scale(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix3f scale(float var1, float var2, float var3, Matrix3f var4) {
      var4.m00 = this.m00 * var1;
      var4.m01 = this.m01 * var1;
      var4.m02 = this.m02 * var1;
      var4.m10 = this.m10 * var2;
      var4.m11 = this.m11 * var2;
      var4.m12 = this.m12 * var2;
      var4.m20 = this.m20 * var3;
      var4.m21 = this.m21 * var3;
      var4.m22 = this.m22 * var3;
      return var4;
   }

   public Matrix3f scale(float var1, float var2, float var3) {
      return this.scale(var1, var2, var3, this);
   }

   public Matrix3f scale(float var1, Matrix3f var2) {
      return this.scale(var1, var1, var1, var2);
   }

   public Matrix3f scale(float var1) {
      return this.scale(var1, var1, var1);
   }

   public Matrix3f scaleLocal(float var1, float var2, float var3, Matrix3f var4) {
      float var5 = var1 * this.m00;
      float var6 = var2 * this.m01;
      float var7 = var3 * this.m02;
      float var8 = var1 * this.m10;
      float var9 = var2 * this.m11;
      float var10 = var3 * this.m12;
      float var11 = var1 * this.m20;
      float var12 = var2 * this.m21;
      float var13 = var3 * this.m22;
      var4.m00 = var5;
      var4.m01 = var6;
      var4.m02 = var7;
      var4.m10 = var8;
      var4.m11 = var9;
      var4.m12 = var10;
      var4.m20 = var11;
      var4.m21 = var12;
      var4.m22 = var13;
      return var4;
   }

   public Matrix3f scaleLocal(float var1, float var2, float var3) {
      return this.scaleLocal(var1, var2, var3, this);
   }

   public Matrix3f scaling(float var1) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var1;
      this.m22 = var1;
      return this;
   }

   public Matrix3f scaling(float var1, float var2, float var3) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var2;
      this.m22 = var3;
      return this;
   }

   public Matrix3f scaling(Vector3fc var1) {
      return this.scaling(var1.x(), var1.y(), var1.z());
   }

   public Matrix3f rotation(float var1, Vector3fc var2) {
      return this.rotation(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3f rotation(AxisAngle4f var1) {
      return this.rotation(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix3f rotation(float var1, float var2, float var3, float var4) {
      float var5 = Math.sin(var1);
      float var6 = Math.cosFromSin(var5, var1);
      float var7 = 1.0F - var6;
      float var8 = var2 * var3;
      float var9 = var2 * var4;
      float var10 = var3 * var4;
      this.m00 = var6 + var2 * var2 * var7;
      this.m10 = var8 * var7 - var4 * var5;
      this.m20 = var9 * var7 + var3 * var5;
      this.m01 = var8 * var7 + var4 * var5;
      this.m11 = var6 + var3 * var3 * var7;
      this.m21 = var10 * var7 - var2 * var5;
      this.m02 = var9 * var7 - var3 * var5;
      this.m12 = var10 * var7 + var2 * var5;
      this.m22 = var6 + var4 * var4 * var7;
      return this;
   }

   public Matrix3f rotationX(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var3;
      this.m12 = var2;
      this.m20 = 0.0F;
      this.m21 = -var2;
      this.m22 = var3;
      return this;
   }

   public Matrix3f rotationY(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      this.m00 = var3;
      this.m01 = 0.0F;
      this.m02 = -var2;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m12 = 0.0F;
      this.m20 = var2;
      this.m21 = 0.0F;
      this.m22 = var3;
      return this;
   }

   public Matrix3f rotationZ(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      this.m00 = var3;
      this.m01 = var2;
      this.m02 = 0.0F;
      this.m10 = -var2;
      this.m11 = var3;
      this.m12 = 0.0F;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      this.m22 = 1.0F;
      return this;
   }

   public Matrix3f rotationXYZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var1);
      float var5 = Math.cosFromSin(var4, var1);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      float var10 = -var4;
      float var11 = -var6;
      float var12 = -var8;
      float var18 = var10 * var11;
      float var19 = var5 * var11;
      this.m20 = var6;
      this.m21 = var10 * var7;
      this.m22 = var5 * var7;
      this.m00 = var7 * var9;
      this.m01 = var18 * var9 + var5 * var8;
      this.m02 = var19 * var9 + var4 * var8;
      this.m10 = var7 * var12;
      this.m11 = var18 * var12 + var5 * var9;
      this.m12 = var19 * var12 + var4 * var9;
      return this;
   }

   public Matrix3f rotationZYX(float var1, float var2, float var3) {
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var1);
      float var9 = Math.cosFromSin(var8, var1);
      float var10 = -var8;
      float var11 = -var6;
      float var12 = -var4;
      float var17 = var9 * var6;
      float var18 = var8 * var6;
      this.m00 = var9 * var7;
      this.m01 = var8 * var7;
      this.m02 = var11;
      this.m10 = var10 * var5 + var17 * var4;
      this.m11 = var9 * var5 + var18 * var4;
      this.m12 = var7 * var4;
      this.m20 = var10 * var12 + var17 * var5;
      this.m21 = var9 * var12 + var18 * var5;
      this.m22 = var7 * var5;
      return this;
   }

   public Matrix3f rotationYXZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var2);
      float var5 = Math.cosFromSin(var4, var2);
      float var6 = Math.sin(var1);
      float var7 = Math.cosFromSin(var6, var1);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      float var10 = -var6;
      float var11 = -var4;
      float var12 = -var8;
      float var17 = var6 * var4;
      float var19 = var7 * var4;
      this.m20 = var6 * var5;
      this.m21 = var11;
      this.m22 = var7 * var5;
      this.m00 = var7 * var9 + var17 * var8;
      this.m01 = var5 * var8;
      this.m02 = var10 * var9 + var19 * var8;
      this.m10 = var7 * var12 + var17 * var9;
      this.m11 = var5 * var9;
      this.m12 = var10 * var12 + var19 * var9;
      return this;
   }

   public Matrix3f rotation(Quaternionfc var1) {
      float var2 = var1.w() * var1.w();
      float var3 = var1.x() * var1.x();
      float var4 = var1.y() * var1.y();
      float var5 = var1.z() * var1.z();
      float var6 = var1.z() * var1.w();
      float var7 = var6 + var6;
      float var8 = var1.x() * var1.y();
      float var9 = var8 + var8;
      float var10 = var1.x() * var1.z();
      float var11 = var10 + var10;
      float var12 = var1.y() * var1.w();
      float var13 = var12 + var12;
      float var14 = var1.y() * var1.z();
      float var15 = var14 + var14;
      float var16 = var1.x() * var1.w();
      float var17 = var16 + var16;
      this.m00 = var2 + var3 - var5 - var4;
      this.m01 = var9 + var7;
      this.m02 = var11 - var13;
      this.m10 = -var7 + var9;
      this.m11 = var4 - var5 + var2 - var3;
      this.m12 = var15 + var17;
      this.m20 = var13 + var11;
      this.m21 = var15 - var17;
      this.m22 = var5 - var4 - var3 + var2;
      return this;
   }

   public Vector3f transform(Vector3f var1) {
      return var1.mul((Matrix3fc)this);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      return var1.mul((Matrix3fc)this, var2);
   }

   public Vector3f transform(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(Math.fma(this.m00, var1, Math.fma(this.m10, var2, this.m20 * var3)), Math.fma(this.m01, var1, Math.fma(this.m11, var2, this.m21 * var3)), Math.fma(this.m02, var1, Math.fma(this.m12, var2, this.m22 * var3)));
   }

   public Vector3f transformTranspose(Vector3f var1) {
      return var1.mulTranspose(this);
   }

   public Vector3f transformTranspose(Vector3fc var1, Vector3f var2) {
      return var1.mulTranspose(this, var2);
   }

   public Vector3f transformTranspose(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(Math.fma(this.m00, var1, Math.fma(this.m01, var2, this.m02 * var3)), Math.fma(this.m10, var1, Math.fma(this.m11, var2, this.m12 * var3)), Math.fma(this.m20, var1, Math.fma(this.m21, var2, this.m22 * var3)));
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.m00);
      var1.writeFloat(this.m01);
      var1.writeFloat(this.m02);
      var1.writeFloat(this.m10);
      var1.writeFloat(this.m11);
      var1.writeFloat(this.m12);
      var1.writeFloat(this.m20);
      var1.writeFloat(this.m21);
      var1.writeFloat(this.m22);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readFloat();
      this.m01 = var1.readFloat();
      this.m02 = var1.readFloat();
      this.m10 = var1.readFloat();
      this.m11 = var1.readFloat();
      this.m12 = var1.readFloat();
      this.m20 = var1.readFloat();
      this.m21 = var1.readFloat();
      this.m22 = var1.readFloat();
   }

   public Matrix3f rotateX(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var6 = -var3;
      float var9 = this.m10 * var4 + this.m20 * var3;
      float var10 = this.m11 * var4 + this.m21 * var3;
      float var11 = this.m12 * var4 + this.m22 * var3;
      var2.m20 = this.m10 * var6 + this.m20 * var4;
      var2.m21 = this.m11 * var6 + this.m21 * var4;
      var2.m22 = this.m12 * var6 + this.m22 * var4;
      var2.m10 = var9;
      var2.m11 = var10;
      var2.m12 = var11;
      var2.m00 = this.m00;
      var2.m01 = this.m01;
      var2.m02 = this.m02;
      return var2;
   }

   public Matrix3f rotateX(float var1) {
      return this.rotateX(var1, this);
   }

   public Matrix3f rotateY(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var7 = -var3;
      float var9 = this.m00 * var4 + this.m20 * var7;
      float var10 = this.m01 * var4 + this.m21 * var7;
      float var11 = this.m02 * var4 + this.m22 * var7;
      var2.m20 = this.m00 * var3 + this.m20 * var4;
      var2.m21 = this.m01 * var3 + this.m21 * var4;
      var2.m22 = this.m02 * var3 + this.m22 * var4;
      var2.m00 = var9;
      var2.m01 = var10;
      var2.m02 = var11;
      var2.m10 = this.m10;
      var2.m11 = this.m11;
      var2.m12 = this.m12;
      return var2;
   }

   public Matrix3f rotateY(float var1) {
      return this.rotateY(var1, this);
   }

   public Matrix3f rotateZ(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var6 = -var3;
      float var9 = this.m00 * var4 + this.m10 * var3;
      float var10 = this.m01 * var4 + this.m11 * var3;
      float var11 = this.m02 * var4 + this.m12 * var3;
      var2.m10 = this.m00 * var6 + this.m10 * var4;
      var2.m11 = this.m01 * var6 + this.m11 * var4;
      var2.m12 = this.m02 * var6 + this.m12 * var4;
      var2.m00 = var9;
      var2.m01 = var10;
      var2.m02 = var11;
      var2.m20 = this.m20;
      var2.m21 = this.m21;
      var2.m22 = this.m22;
      return var2;
   }

   public Matrix3f rotateZ(float var1) {
      return this.rotateZ(var1, this);
   }

   public Matrix3f rotateXYZ(Vector3f var1) {
      return this.rotateXYZ(var1.x, var1.y, var1.z);
   }

   public Matrix3f rotateXYZ(float var1, float var2, float var3) {
      return this.rotateXYZ(var1, var2, var3, this);
   }

   public Matrix3f rotateXYZ(float var1, float var2, float var3, Matrix3f var4) {
      float var5 = Math.sin(var1);
      float var6 = Math.cosFromSin(var5, var1);
      float var7 = Math.sin(var2);
      float var8 = Math.cosFromSin(var7, var2);
      float var9 = Math.sin(var3);
      float var10 = Math.cosFromSin(var9, var3);
      float var11 = -var5;
      float var12 = -var7;
      float var13 = -var9;
      float var14 = this.m10 * var6 + this.m20 * var5;
      float var15 = this.m11 * var6 + this.m21 * var5;
      float var16 = this.m12 * var6 + this.m22 * var5;
      float var17 = this.m10 * var11 + this.m20 * var6;
      float var18 = this.m11 * var11 + this.m21 * var6;
      float var19 = this.m12 * var11 + this.m22 * var6;
      float var20 = this.m00 * var8 + var17 * var12;
      float var21 = this.m01 * var8 + var18 * var12;
      float var22 = this.m02 * var8 + var19 * var12;
      var4.m20 = this.m00 * var7 + var17 * var8;
      var4.m21 = this.m01 * var7 + var18 * var8;
      var4.m22 = this.m02 * var7 + var19 * var8;
      var4.m00 = var20 * var10 + var14 * var9;
      var4.m01 = var21 * var10 + var15 * var9;
      var4.m02 = var22 * var10 + var16 * var9;
      var4.m10 = var20 * var13 + var14 * var10;
      var4.m11 = var21 * var13 + var15 * var10;
      var4.m12 = var22 * var13 + var16 * var10;
      return var4;
   }

   public Matrix3f rotateZYX(Vector3f var1) {
      return this.rotateZYX(var1.z, var1.y, var1.x);
   }

   public Matrix3f rotateZYX(float var1, float var2, float var3) {
      return this.rotateZYX(var1, var2, var3, this);
   }

   public Matrix3f rotateZYX(float var1, float var2, float var3, Matrix3f var4) {
      float var5 = Math.sin(var3);
      float var6 = Math.cosFromSin(var5, var3);
      float var7 = Math.sin(var2);
      float var8 = Math.cosFromSin(var7, var2);
      float var9 = Math.sin(var1);
      float var10 = Math.cosFromSin(var9, var1);
      float var11 = -var9;
      float var12 = -var7;
      float var13 = -var5;
      float var14 = this.m00 * var10 + this.m10 * var9;
      float var15 = this.m01 * var10 + this.m11 * var9;
      float var16 = this.m02 * var10 + this.m12 * var9;
      float var17 = this.m00 * var11 + this.m10 * var10;
      float var18 = this.m01 * var11 + this.m11 * var10;
      float var19 = this.m02 * var11 + this.m12 * var10;
      float var20 = var14 * var7 + this.m20 * var8;
      float var21 = var15 * var7 + this.m21 * var8;
      float var22 = var16 * var7 + this.m22 * var8;
      var4.m00 = var14 * var8 + this.m20 * var12;
      var4.m01 = var15 * var8 + this.m21 * var12;
      var4.m02 = var16 * var8 + this.m22 * var12;
      var4.m10 = var17 * var6 + var20 * var5;
      var4.m11 = var18 * var6 + var21 * var5;
      var4.m12 = var19 * var6 + var22 * var5;
      var4.m20 = var17 * var13 + var20 * var6;
      var4.m21 = var18 * var13 + var21 * var6;
      var4.m22 = var19 * var13 + var22 * var6;
      return var4;
   }

   public Matrix3f rotateYXZ(Vector3f var1) {
      return this.rotateYXZ(var1.y, var1.x, var1.z);
   }

   public Matrix3f rotateYXZ(float var1, float var2, float var3) {
      return this.rotateYXZ(var1, var2, var3, this);
   }

   public Matrix3f rotateYXZ(float var1, float var2, float var3, Matrix3f var4) {
      float var5 = Math.sin(var2);
      float var6 = Math.cosFromSin(var5, var2);
      float var7 = Math.sin(var1);
      float var8 = Math.cosFromSin(var7, var1);
      float var9 = Math.sin(var3);
      float var10 = Math.cosFromSin(var9, var3);
      float var11 = -var7;
      float var12 = -var5;
      float var13 = -var9;
      float var14 = this.m00 * var7 + this.m20 * var8;
      float var15 = this.m01 * var7 + this.m21 * var8;
      float var16 = this.m02 * var7 + this.m22 * var8;
      float var17 = this.m00 * var8 + this.m20 * var11;
      float var18 = this.m01 * var8 + this.m21 * var11;
      float var19 = this.m02 * var8 + this.m22 * var11;
      float var20 = this.m10 * var6 + var14 * var5;
      float var21 = this.m11 * var6 + var15 * var5;
      float var22 = this.m12 * var6 + var16 * var5;
      var4.m20 = this.m10 * var12 + var14 * var6;
      var4.m21 = this.m11 * var12 + var15 * var6;
      var4.m22 = this.m12 * var12 + var16 * var6;
      var4.m00 = var17 * var10 + var20 * var9;
      var4.m01 = var18 * var10 + var21 * var9;
      var4.m02 = var19 * var10 + var22 * var9;
      var4.m10 = var17 * var13 + var20 * var10;
      var4.m11 = var18 * var13 + var21 * var10;
      var4.m12 = var19 * var13 + var22 * var10;
      return var4;
   }

   public Matrix3f rotate(float var1, float var2, float var3, float var4) {
      return this.rotate(var1, var2, var3, var4, this);
   }

   public Matrix3f rotate(float var1, float var2, float var3, float var4, Matrix3f var5) {
      float var6 = Math.sin(var1);
      float var7 = Math.cosFromSin(var6, var1);
      float var8 = 1.0F - var7;
      float var9 = var2 * var2;
      float var10 = var2 * var3;
      float var11 = var2 * var4;
      float var12 = var3 * var3;
      float var13 = var3 * var4;
      float var14 = var4 * var4;
      float var15 = var9 * var8 + var7;
      float var16 = var10 * var8 + var4 * var6;
      float var17 = var11 * var8 - var3 * var6;
      float var18 = var10 * var8 - var4 * var6;
      float var19 = var12 * var8 + var7;
      float var20 = var13 * var8 + var2 * var6;
      float var21 = var11 * var8 + var3 * var6;
      float var22 = var13 * var8 - var2 * var6;
      float var23 = var14 * var8 + var7;
      float var24 = this.m00 * var15 + this.m10 * var16 + this.m20 * var17;
      float var25 = this.m01 * var15 + this.m11 * var16 + this.m21 * var17;
      float var26 = this.m02 * var15 + this.m12 * var16 + this.m22 * var17;
      float var27 = this.m00 * var18 + this.m10 * var19 + this.m20 * var20;
      float var28 = this.m01 * var18 + this.m11 * var19 + this.m21 * var20;
      float var29 = this.m02 * var18 + this.m12 * var19 + this.m22 * var20;
      var5.m20 = this.m00 * var21 + this.m10 * var22 + this.m20 * var23;
      var5.m21 = this.m01 * var21 + this.m11 * var22 + this.m21 * var23;
      var5.m22 = this.m02 * var21 + this.m12 * var22 + this.m22 * var23;
      var5.m00 = var24;
      var5.m01 = var25;
      var5.m02 = var26;
      var5.m10 = var27;
      var5.m11 = var28;
      var5.m12 = var29;
      return var5;
   }

   public Matrix3f rotateLocal(float var1, float var2, float var3, float var4, Matrix3f var5) {
      float var6 = Math.sin(var1);
      float var7 = Math.cosFromSin(var6, var1);
      float var8 = 1.0F - var7;
      float var9 = var2 * var2;
      float var10 = var2 * var3;
      float var11 = var2 * var4;
      float var12 = var3 * var3;
      float var13 = var3 * var4;
      float var14 = var4 * var4;
      float var15 = var9 * var8 + var7;
      float var16 = var10 * var8 + var4 * var6;
      float var17 = var11 * var8 - var3 * var6;
      float var18 = var10 * var8 - var4 * var6;
      float var19 = var12 * var8 + var7;
      float var20 = var13 * var8 + var2 * var6;
      float var21 = var11 * var8 + var3 * var6;
      float var22 = var13 * var8 - var2 * var6;
      float var23 = var14 * var8 + var7;
      float var24 = var15 * this.m00 + var18 * this.m01 + var21 * this.m02;
      float var25 = var16 * this.m00 + var19 * this.m01 + var22 * this.m02;
      float var26 = var17 * this.m00 + var20 * this.m01 + var23 * this.m02;
      float var27 = var15 * this.m10 + var18 * this.m11 + var21 * this.m12;
      float var28 = var16 * this.m10 + var19 * this.m11 + var22 * this.m12;
      float var29 = var17 * this.m10 + var20 * this.m11 + var23 * this.m12;
      float var30 = var15 * this.m20 + var18 * this.m21 + var21 * this.m22;
      float var31 = var16 * this.m20 + var19 * this.m21 + var22 * this.m22;
      float var32 = var17 * this.m20 + var20 * this.m21 + var23 * this.m22;
      var5.m00 = var24;
      var5.m01 = var25;
      var5.m02 = var26;
      var5.m10 = var27;
      var5.m11 = var28;
      var5.m12 = var29;
      var5.m20 = var30;
      var5.m21 = var31;
      var5.m22 = var32;
      return var5;
   }

   public Matrix3f rotateLocal(float var1, float var2, float var3, float var4) {
      return this.rotateLocal(var1, var2, var3, var4, this);
   }

   public Matrix3f rotateLocalX(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m01 - var3 * this.m02;
      float var6 = var3 * this.m01 + var4 * this.m02;
      float var7 = var4 * this.m11 - var3 * this.m12;
      float var8 = var3 * this.m11 + var4 * this.m12;
      float var9 = var4 * this.m21 - var3 * this.m22;
      float var10 = var3 * this.m21 + var4 * this.m22;
      var2.m00 = this.m00;
      var2.m01 = var5;
      var2.m02 = var6;
      var2.m10 = this.m10;
      var2.m11 = var7;
      var2.m12 = var8;
      var2.m20 = this.m20;
      var2.m21 = var9;
      var2.m22 = var10;
      return var2;
   }

   public Matrix3f rotateLocalX(float var1) {
      return this.rotateLocalX(var1, this);
   }

   public Matrix3f rotateLocalY(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 + var3 * this.m02;
      float var6 = -var3 * this.m00 + var4 * this.m02;
      float var7 = var4 * this.m10 + var3 * this.m12;
      float var8 = -var3 * this.m10 + var4 * this.m12;
      float var9 = var4 * this.m20 + var3 * this.m22;
      float var10 = -var3 * this.m20 + var4 * this.m22;
      var2.m00 = var5;
      var2.m01 = this.m01;
      var2.m02 = var6;
      var2.m10 = var7;
      var2.m11 = this.m11;
      var2.m12 = var8;
      var2.m20 = var9;
      var2.m21 = this.m21;
      var2.m22 = var10;
      return var2;
   }

   public Matrix3f rotateLocalY(float var1) {
      return this.rotateLocalY(var1, this);
   }

   public Matrix3f rotateLocalZ(float var1, Matrix3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 - var3 * this.m01;
      float var6 = var3 * this.m00 + var4 * this.m01;
      float var7 = var4 * this.m10 - var3 * this.m11;
      float var8 = var3 * this.m10 + var4 * this.m11;
      float var9 = var4 * this.m20 - var3 * this.m21;
      float var10 = var3 * this.m20 + var4 * this.m21;
      var2.m00 = var5;
      var2.m01 = var6;
      var2.m02 = this.m02;
      var2.m10 = var7;
      var2.m11 = var8;
      var2.m12 = this.m12;
      var2.m20 = var9;
      var2.m21 = var10;
      var2.m22 = this.m22;
      return var2;
   }

   public Matrix3f rotateLocalZ(float var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Matrix3f rotate(Quaternionfc var1) {
      return this.rotate(var1, this);
   }

   public Matrix3f rotate(Quaternionfc var1, Matrix3f var2) {
      float var3 = var1.w() * var1.w();
      float var4 = var1.x() * var1.x();
      float var5 = var1.y() * var1.y();
      float var6 = var1.z() * var1.z();
      float var7 = var1.z() * var1.w();
      float var8 = var7 + var7;
      float var9 = var1.x() * var1.y();
      float var10 = var9 + var9;
      float var11 = var1.x() * var1.z();
      float var12 = var11 + var11;
      float var13 = var1.y() * var1.w();
      float var14 = var13 + var13;
      float var15 = var1.y() * var1.z();
      float var16 = var15 + var15;
      float var17 = var1.x() * var1.w();
      float var18 = var17 + var17;
      float var19 = var3 + var4 - var6 - var5;
      float var20 = var10 + var8;
      float var21 = var12 - var14;
      float var22 = var10 - var8;
      float var23 = var5 - var6 + var3 - var4;
      float var24 = var16 + var18;
      float var25 = var14 + var12;
      float var26 = var16 - var18;
      float var27 = var6 - var5 - var4 + var3;
      float var28 = this.m00 * var19 + this.m10 * var20 + this.m20 * var21;
      float var29 = this.m01 * var19 + this.m11 * var20 + this.m21 * var21;
      float var30 = this.m02 * var19 + this.m12 * var20 + this.m22 * var21;
      float var31 = this.m00 * var22 + this.m10 * var23 + this.m20 * var24;
      float var32 = this.m01 * var22 + this.m11 * var23 + this.m21 * var24;
      float var33 = this.m02 * var22 + this.m12 * var23 + this.m22 * var24;
      var2.m20 = this.m00 * var25 + this.m10 * var26 + this.m20 * var27;
      var2.m21 = this.m01 * var25 + this.m11 * var26 + this.m21 * var27;
      var2.m22 = this.m02 * var25 + this.m12 * var26 + this.m22 * var27;
      var2.m00 = var28;
      var2.m01 = var29;
      var2.m02 = var30;
      var2.m10 = var31;
      var2.m11 = var32;
      var2.m12 = var33;
      return var2;
   }

   public Matrix3f rotateLocal(Quaternionfc var1, Matrix3f var2) {
      float var3 = var1.w() * var1.w();
      float var4 = var1.x() * var1.x();
      float var5 = var1.y() * var1.y();
      float var6 = var1.z() * var1.z();
      float var7 = var1.z() * var1.w();
      float var8 = var7 + var7;
      float var9 = var1.x() * var1.y();
      float var10 = var9 + var9;
      float var11 = var1.x() * var1.z();
      float var12 = var11 + var11;
      float var13 = var1.y() * var1.w();
      float var14 = var13 + var13;
      float var15 = var1.y() * var1.z();
      float var16 = var15 + var15;
      float var17 = var1.x() * var1.w();
      float var18 = var17 + var17;
      float var19 = var3 + var4 - var6 - var5;
      float var20 = var10 + var8;
      float var21 = var12 - var14;
      float var22 = var10 - var8;
      float var23 = var5 - var6 + var3 - var4;
      float var24 = var16 + var18;
      float var25 = var14 + var12;
      float var26 = var16 - var18;
      float var27 = var6 - var5 - var4 + var3;
      float var28 = var19 * this.m00 + var22 * this.m01 + var25 * this.m02;
      float var29 = var20 * this.m00 + var23 * this.m01 + var26 * this.m02;
      float var30 = var21 * this.m00 + var24 * this.m01 + var27 * this.m02;
      float var31 = var19 * this.m10 + var22 * this.m11 + var25 * this.m12;
      float var32 = var20 * this.m10 + var23 * this.m11 + var26 * this.m12;
      float var33 = var21 * this.m10 + var24 * this.m11 + var27 * this.m12;
      float var34 = var19 * this.m20 + var22 * this.m21 + var25 * this.m22;
      float var35 = var20 * this.m20 + var23 * this.m21 + var26 * this.m22;
      float var36 = var21 * this.m20 + var24 * this.m21 + var27 * this.m22;
      var2.m00 = var28;
      var2.m01 = var29;
      var2.m02 = var30;
      var2.m10 = var31;
      var2.m11 = var32;
      var2.m12 = var33;
      var2.m20 = var34;
      var2.m21 = var35;
      var2.m22 = var36;
      return var2;
   }

   public Matrix3f rotateLocal(Quaternionfc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix3f rotate(AxisAngle4f var1) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix3f rotate(AxisAngle4f var1, Matrix3f var2) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z, var2);
   }

   public Matrix3f rotate(float var1, Vector3fc var2) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3f rotate(float var1, Vector3fc var2, Matrix3f var3) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix3f lookAlong(Vector3fc var1, Vector3fc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix3f lookAlong(Vector3fc var1, Vector3fc var2, Matrix3f var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix3f var7) {
      float var8 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      var1 *= -var8;
      var2 *= -var8;
      var3 *= -var8;
      float var9 = var5 * var3 - var6 * var2;
      float var10 = var6 * var1 - var4 * var3;
      float var11 = var4 * var2 - var5 * var1;
      float var12 = Math.invsqrt(var9 * var9 + var10 * var10 + var11 * var11);
      var9 *= var12;
      var10 *= var12;
      var11 *= var12;
      float var13 = var2 * var11 - var3 * var10;
      float var14 = var3 * var9 - var1 * var11;
      float var15 = var1 * var10 - var2 * var9;
      float var25 = this.m00 * var9 + this.m10 * var13 + this.m20 * var1;
      float var26 = this.m01 * var9 + this.m11 * var13 + this.m21 * var1;
      float var27 = this.m02 * var9 + this.m12 * var13 + this.m22 * var1;
      float var28 = this.m00 * var10 + this.m10 * var14 + this.m20 * var2;
      float var29 = this.m01 * var10 + this.m11 * var14 + this.m21 * var2;
      float var30 = this.m02 * var10 + this.m12 * var14 + this.m22 * var2;
      var7.m20 = this.m00 * var11 + this.m10 * var15 + this.m20 * var3;
      var7.m21 = this.m01 * var11 + this.m11 * var15 + this.m21 * var3;
      var7.m22 = this.m02 * var11 + this.m12 * var15 + this.m22 * var3;
      var7.m00 = var25;
      var7.m01 = var26;
      var7.m02 = var27;
      var7.m10 = var28;
      var7.m11 = var29;
      var7.m12 = var30;
      return var7;
   }

   public Matrix3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.lookAlong(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix3f setLookAlong(Vector3fc var1, Vector3fc var2) {
      return this.setLookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix3f setLookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      var1 *= -var7;
      var2 *= -var7;
      var3 *= -var7;
      float var8 = var5 * var3 - var6 * var2;
      float var9 = var6 * var1 - var4 * var3;
      float var10 = var4 * var2 - var5 * var1;
      float var11 = Math.invsqrt(var8 * var8 + var9 * var9 + var10 * var10);
      var8 *= var11;
      var9 *= var11;
      var10 *= var11;
      float var12 = var2 * var10 - var3 * var9;
      float var13 = var3 * var8 - var1 * var10;
      float var14 = var1 * var9 - var2 * var8;
      this.m00 = var8;
      this.m01 = var12;
      this.m02 = var1;
      this.m10 = var9;
      this.m11 = var13;
      this.m12 = var2;
      this.m20 = var10;
      this.m21 = var14;
      this.m22 = var3;
      return this;
   }

   public Vector3f getRow(int var1, Vector3f var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         return var2.set(this.m00, this.m10, this.m20);
      case 1:
         return var2.set(this.m01, this.m11, this.m21);
      case 2:
         return var2.set(this.m02, this.m12, this.m22);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Matrix3f setRow(int var1, Vector3fc var2) throws IndexOutOfBoundsException {
      return this.setRow(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3f setRow(int var1, float var2, float var3, float var4) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var3;
         this.m20 = var4;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var3;
         this.m21 = var4;
         break;
      case 2:
         this.m02 = var2;
         this.m12 = var3;
         this.m22 = var4;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         return var2.set(this.m00, this.m01, this.m02);
      case 1:
         return var2.set(this.m10, this.m11, this.m12);
      case 2:
         return var2.set(this.m20, this.m21, this.m22);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Matrix3f setColumn(int var1, Vector3fc var2) throws IndexOutOfBoundsException {
      return this.setColumn(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3f setColumn(int var1, float var2, float var3, float var4) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var3;
         this.m02 = var4;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var3;
         this.m12 = var4;
         break;
      case 2:
         this.m20 = var2;
         this.m21 = var3;
         this.m22 = var4;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public float get(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Matrix3f set(int var1, int var2, float var3) {
      return MemUtil.INSTANCE.set(this, var1, var2, var3);
   }

   public float getRowColumn(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var2, var1);
   }

   public Matrix3f setRowColumn(int var1, int var2, float var3) {
      return MemUtil.INSTANCE.set(this, var2, var1, var3);
   }

   public Matrix3f normal() {
      return this.normal(this);
   }

   public Matrix3f normal(Matrix3f var1) {
      float var2 = this.m00 * this.m11;
      float var3 = this.m01 * this.m10;
      float var4 = this.m02 * this.m10;
      float var5 = this.m00 * this.m12;
      float var6 = this.m01 * this.m12;
      float var7 = this.m02 * this.m11;
      float var8 = (var2 - var3) * this.m22 + (var4 - var5) * this.m21 + (var6 - var7) * this.m20;
      float var9 = 1.0F / var8;
      float var10 = (this.m11 * this.m22 - this.m21 * this.m12) * var9;
      float var11 = (this.m20 * this.m12 - this.m10 * this.m22) * var9;
      float var12 = (this.m10 * this.m21 - this.m20 * this.m11) * var9;
      float var13 = (this.m21 * this.m02 - this.m01 * this.m22) * var9;
      float var14 = (this.m00 * this.m22 - this.m20 * this.m02) * var9;
      float var15 = (this.m20 * this.m01 - this.m00 * this.m21) * var9;
      float var16 = (var6 - var7) * var9;
      float var17 = (var4 - var5) * var9;
      float var18 = (var2 - var3) * var9;
      var1.m00 = var10;
      var1.m01 = var11;
      var1.m02 = var12;
      var1.m10 = var13;
      var1.m11 = var14;
      var1.m12 = var15;
      var1.m20 = var16;
      var1.m21 = var17;
      var1.m22 = var18;
      return var1;
   }

   public Matrix3f cofactor() {
      return this.cofactor(this);
   }

   public Matrix3f cofactor(Matrix3f var1) {
      float var2 = this.m11 * this.m22 - this.m21 * this.m12;
      float var3 = this.m20 * this.m12 - this.m10 * this.m22;
      float var4 = this.m10 * this.m21 - this.m20 * this.m11;
      float var5 = this.m21 * this.m02 - this.m01 * this.m22;
      float var6 = this.m00 * this.m22 - this.m20 * this.m02;
      float var7 = this.m20 * this.m01 - this.m00 * this.m21;
      float var8 = this.m01 * this.m12 - this.m11 * this.m02;
      float var9 = this.m02 * this.m10 - this.m12 * this.m00;
      float var10 = this.m00 * this.m11 - this.m10 * this.m01;
      var1.m00 = var2;
      var1.m01 = var3;
      var1.m02 = var4;
      var1.m10 = var5;
      var1.m11 = var6;
      var1.m12 = var7;
      var1.m20 = var8;
      var1.m21 = var9;
      var1.m22 = var10;
      return var1;
   }

   public Vector3f getScale(Vector3f var1) {
      return var1.set(Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02), Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12), Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22));
   }

   public Vector3f positiveZ(Vector3f var1) {
      var1.x = this.m10 * this.m21 - this.m11 * this.m20;
      var1.y = this.m20 * this.m01 - this.m21 * this.m00;
      var1.z = this.m00 * this.m11 - this.m01 * this.m10;
      return var1.normalize(var1);
   }

   public Vector3f normalizedPositiveZ(Vector3f var1) {
      var1.x = this.m02;
      var1.y = this.m12;
      var1.z = this.m22;
      return var1;
   }

   public Vector3f positiveX(Vector3f var1) {
      var1.x = this.m11 * this.m22 - this.m12 * this.m21;
      var1.y = this.m02 * this.m21 - this.m01 * this.m22;
      var1.z = this.m01 * this.m12 - this.m02 * this.m11;
      return var1.normalize(var1);
   }

   public Vector3f normalizedPositiveX(Vector3f var1) {
      var1.x = this.m00;
      var1.y = this.m10;
      var1.z = this.m20;
      return var1;
   }

   public Vector3f positiveY(Vector3f var1) {
      var1.x = this.m12 * this.m20 - this.m10 * this.m22;
      var1.y = this.m00 * this.m22 - this.m02 * this.m20;
      var1.z = this.m02 * this.m10 - this.m00 * this.m12;
      return var1.normalize(var1);
   }

   public Vector3f normalizedPositiveY(Vector3f var1) {
      var1.x = this.m01;
      var1.y = this.m11;
      var1.z = this.m21;
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.m00);
      var2 = 31 * var2 + Float.floatToIntBits(this.m01);
      var2 = 31 * var2 + Float.floatToIntBits(this.m02);
      var2 = 31 * var2 + Float.floatToIntBits(this.m10);
      var2 = 31 * var2 + Float.floatToIntBits(this.m11);
      var2 = 31 * var2 + Float.floatToIntBits(this.m12);
      var2 = 31 * var2 + Float.floatToIntBits(this.m20);
      var2 = 31 * var2 + Float.floatToIntBits(this.m21);
      var2 = 31 * var2 + Float.floatToIntBits(this.m22);
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
         Matrix3f var2 = (Matrix3f)var1;
         if (Float.floatToIntBits(this.m00) != Float.floatToIntBits(var2.m00)) {
            return false;
         } else if (Float.floatToIntBits(this.m01) != Float.floatToIntBits(var2.m01)) {
            return false;
         } else if (Float.floatToIntBits(this.m02) != Float.floatToIntBits(var2.m02)) {
            return false;
         } else if (Float.floatToIntBits(this.m10) != Float.floatToIntBits(var2.m10)) {
            return false;
         } else if (Float.floatToIntBits(this.m11) != Float.floatToIntBits(var2.m11)) {
            return false;
         } else if (Float.floatToIntBits(this.m12) != Float.floatToIntBits(var2.m12)) {
            return false;
         } else if (Float.floatToIntBits(this.m20) != Float.floatToIntBits(var2.m20)) {
            return false;
         } else if (Float.floatToIntBits(this.m21) != Float.floatToIntBits(var2.m21)) {
            return false;
         } else {
            return Float.floatToIntBits(this.m22) == Float.floatToIntBits(var2.m22);
         }
      }
   }

   public boolean equals(Matrix3fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix3f)) {
         return false;
      } else if (!Runtime.equals(this.m00, var1.m00(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m01, var1.m01(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m02, var1.m02(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m10, var1.m10(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m11, var1.m11(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m12, var1.m12(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m20, var1.m20(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m21, var1.m21(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.m22, var1.m22(), var2);
      }
   }

   public Matrix3f swap(Matrix3f var1) {
      MemUtil.INSTANCE.swap(this, var1);
      return this;
   }

   public Matrix3f add(Matrix3fc var1) {
      return this.add(var1, this);
   }

   public Matrix3f add(Matrix3fc var1, Matrix3f var2) {
      var2.m00 = this.m00 + var1.m00();
      var2.m01 = this.m01 + var1.m01();
      var2.m02 = this.m02 + var1.m02();
      var2.m10 = this.m10 + var1.m10();
      var2.m11 = this.m11 + var1.m11();
      var2.m12 = this.m12 + var1.m12();
      var2.m20 = this.m20 + var1.m20();
      var2.m21 = this.m21 + var1.m21();
      var2.m22 = this.m22 + var1.m22();
      return var2;
   }

   public Matrix3f sub(Matrix3fc var1) {
      return this.sub(var1, this);
   }

   public Matrix3f sub(Matrix3fc var1, Matrix3f var2) {
      var2.m00 = this.m00 - var1.m00();
      var2.m01 = this.m01 - var1.m01();
      var2.m02 = this.m02 - var1.m02();
      var2.m10 = this.m10 - var1.m10();
      var2.m11 = this.m11 - var1.m11();
      var2.m12 = this.m12 - var1.m12();
      var2.m20 = this.m20 - var1.m20();
      var2.m21 = this.m21 - var1.m21();
      var2.m22 = this.m22 - var1.m22();
      return var2;
   }

   public Matrix3f mulComponentWise(Matrix3fc var1) {
      return this.mulComponentWise(var1, this);
   }

   public Matrix3f mulComponentWise(Matrix3fc var1, Matrix3f var2) {
      var2.m00 = this.m00 * var1.m00();
      var2.m01 = this.m01 * var1.m01();
      var2.m02 = this.m02 * var1.m02();
      var2.m10 = this.m10 * var1.m10();
      var2.m11 = this.m11 * var1.m11();
      var2.m12 = this.m12 * var1.m12();
      var2.m20 = this.m20 * var1.m20();
      var2.m21 = this.m21 * var1.m21();
      var2.m22 = this.m22 * var1.m22();
      return var2;
   }

   public Matrix3f setSkewSymmetric(float var1, float var2, float var3) {
      this.m00 = this.m11 = this.m22 = 0.0F;
      this.m01 = -var1;
      this.m02 = var2;
      this.m10 = var1;
      this.m12 = -var3;
      this.m20 = -var2;
      this.m21 = var3;
      return this;
   }

   public Matrix3f lerp(Matrix3fc var1, float var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix3f lerp(Matrix3fc var1, float var2, Matrix3f var3) {
      var3.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var3.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var3.m02 = Math.fma(var1.m02() - this.m02, var2, this.m02);
      var3.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var3.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      var3.m12 = Math.fma(var1.m12() - this.m12, var2, this.m12);
      var3.m20 = Math.fma(var1.m20() - this.m20, var2, this.m20);
      var3.m21 = Math.fma(var1.m21() - this.m21, var2, this.m21);
      var3.m22 = Math.fma(var1.m22() - this.m22, var2, this.m22);
      return var3;
   }

   public Matrix3f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix3f var3) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix3f rotateTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.rotateTowards(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix3f var7) {
      float var8 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var9 = var1 * var8;
      float var10 = var2 * var8;
      float var11 = var3 * var8;
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
      float var28 = this.m00 * var12 + this.m10 * var13 + this.m20 * var14;
      float var29 = this.m01 * var12 + this.m11 * var13 + this.m21 * var14;
      float var30 = this.m02 * var12 + this.m12 * var13 + this.m22 * var14;
      float var31 = this.m00 * var16 + this.m10 * var17 + this.m20 * var18;
      float var32 = this.m01 * var16 + this.m11 * var17 + this.m21 * var18;
      float var33 = this.m02 * var16 + this.m12 * var17 + this.m22 * var18;
      var7.m20 = this.m00 * var9 + this.m10 * var10 + this.m20 * var11;
      var7.m21 = this.m01 * var9 + this.m11 * var10 + this.m21 * var11;
      var7.m22 = this.m02 * var9 + this.m12 * var10 + this.m22 * var11;
      var7.m00 = var28;
      var7.m01 = var29;
      var7.m02 = var30;
      var7.m10 = var31;
      var7.m11 = var32;
      var7.m12 = var33;
      return var7;
   }

   public Matrix3f rotationTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotationTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix3f rotationTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var8 = var1 * var7;
      float var9 = var2 * var7;
      float var10 = var3 * var7;
      float var11 = var5 * var10 - var6 * var9;
      float var12 = var6 * var8 - var4 * var10;
      float var13 = var4 * var9 - var5 * var8;
      float var14 = Math.invsqrt(var11 * var11 + var12 * var12 + var13 * var13);
      var11 *= var14;
      var12 *= var14;
      var13 *= var14;
      float var15 = var9 * var13 - var10 * var12;
      float var16 = var10 * var11 - var8 * var13;
      float var17 = var8 * var12 - var9 * var11;
      this.m00 = var11;
      this.m01 = var12;
      this.m02 = var13;
      this.m10 = var15;
      this.m11 = var16;
      this.m12 = var17;
      this.m20 = var8;
      this.m21 = var9;
      this.m22 = var10;
      return this;
   }

   public Vector3f getEulerAnglesZYX(Vector3f var1) {
      var1.x = Math.atan2(this.m12, this.m22);
      var1.y = Math.atan2(-this.m02, Math.sqrt(this.m12 * this.m12 + this.m22 * this.m22));
      var1.z = Math.atan2(this.m01, this.m00);
      return var1;
   }

   public Matrix3f obliqueZ(float var1, float var2) {
      this.m20 += this.m00 * var1 + this.m10 * var2;
      this.m21 += this.m01 * var1 + this.m11 * var2;
      this.m22 += this.m02 * var1 + this.m12 * var2;
      return this;
   }

   public Matrix3f obliqueZ(float var1, float var2, Matrix3f var3) {
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m02 = this.m02;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      var3.m12 = this.m12;
      var3.m20 = this.m00 * var1 + this.m10 * var2 + this.m20;
      var3.m21 = this.m01 * var1 + this.m11 * var2 + this.m21;
      var3.m22 = this.m02 * var1 + this.m12 * var2 + this.m22;
      return var3;
   }

   public Matrix3f reflect(float var1, float var2, float var3, Matrix3f var4) {
      float var5 = var1 + var1;
      float var6 = var2 + var2;
      float var7 = var3 + var3;
      float var8 = 1.0F - var5 * var1;
      float var9 = -var5 * var2;
      float var10 = -var5 * var3;
      float var11 = -var6 * var1;
      float var12 = 1.0F - var6 * var2;
      float var13 = -var6 * var3;
      float var14 = -var7 * var1;
      float var15 = -var7 * var2;
      float var16 = 1.0F - var7 * var3;
      float var17 = this.m00 * var8 + this.m10 * var9 + this.m20 * var10;
      float var18 = this.m01 * var8 + this.m11 * var9 + this.m21 * var10;
      float var19 = this.m02 * var8 + this.m12 * var9 + this.m22 * var10;
      float var20 = this.m00 * var11 + this.m10 * var12 + this.m20 * var13;
      float var21 = this.m01 * var11 + this.m11 * var12 + this.m21 * var13;
      float var22 = this.m02 * var11 + this.m12 * var12 + this.m22 * var13;
      return var4._m20(this.m00 * var14 + this.m10 * var15 + this.m20 * var16)._m21(this.m01 * var14 + this.m11 * var15 + this.m21 * var16)._m22(this.m02 * var14 + this.m12 * var15 + this.m22 * var16)._m00(var17)._m01(var18)._m02(var19)._m10(var20)._m11(var21)._m12(var22);
   }

   public Matrix3f reflect(float var1, float var2, float var3) {
      return this.reflect(var1, var2, var3, this);
   }

   public Matrix3f reflect(Vector3fc var1) {
      return this.reflect(var1.x(), var1.y(), var1.z());
   }

   public Matrix3f reflect(Quaternionfc var1) {
      return this.reflect(var1, this);
   }

   public Matrix3f reflect(Quaternionfc var1, Matrix3f var2) {
      float var3 = var1.x() + var1.x();
      float var4 = var1.y() + var1.y();
      float var5 = var1.z() + var1.z();
      float var6 = var1.x() * var5 + var1.w() * var4;
      float var7 = var1.y() * var5 - var1.w() * var3;
      float var8 = 1.0F - (var1.x() * var3 + var1.y() * var4);
      return this.reflect(var6, var7, var8, var2);
   }

   public Matrix3f reflect(Vector3fc var1, Matrix3f var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix3f reflection(float var1, float var2, float var3) {
      float var4 = var1 + var1;
      float var5 = var2 + var2;
      float var6 = var3 + var3;
      this._m00(1.0F - var4 * var1);
      this._m01(-var4 * var2);
      this._m02(-var4 * var3);
      this._m10(-var5 * var1);
      this._m11(1.0F - var5 * var2);
      this._m12(-var5 * var3);
      this._m20(-var6 * var1);
      this._m21(-var6 * var2);
      this._m22(1.0F - var6 * var3);
      return this;
   }

   public Matrix3f reflection(Vector3fc var1) {
      return this.reflection(var1.x(), var1.y(), var1.z());
   }

   public Matrix3f reflection(Quaternionfc var1) {
      float var2 = var1.x() + var1.x();
      float var3 = var1.y() + var1.y();
      float var4 = var1.z() + var1.z();
      float var5 = var1.x() * var4 + var1.w() * var3;
      float var6 = var1.y() * var4 - var1.w() * var2;
      float var7 = 1.0F - (var1.x() * var2 + var1.y() * var3);
      return this.reflection(var5, var6, var7);
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22);
   }

   public float quadraticFormProduct(float var1, float var2, float var3) {
      float var4 = this.m00 * var1 + this.m10 * var2 + this.m20 * var3;
      float var5 = this.m01 * var1 + this.m11 * var2 + this.m21 * var3;
      float var6 = this.m02 * var1 + this.m12 * var2 + this.m22 * var3;
      return var1 * var4 + var2 * var5 + var3 * var6;
   }

   public float quadraticFormProduct(Vector3fc var1) {
      return this.quadraticFormProduct(var1.x(), var1.y(), var1.z());
   }
}
