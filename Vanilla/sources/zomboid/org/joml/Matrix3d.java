package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix3d implements Externalizable, Matrix3dc {
   private static final long serialVersionUID = 1L;
   public double m00;
   public double m01;
   public double m02;
   public double m10;
   public double m11;
   public double m12;
   public double m20;
   public double m21;
   public double m22;

   public Matrix3d() {
      this.m00 = 1.0D;
      this.m11 = 1.0D;
      this.m22 = 1.0D;
   }

   public Matrix3d(Matrix2dc var1) {
      this.set(var1);
   }

   public Matrix3d(Matrix2fc var1) {
      this.set(var1);
   }

   public Matrix3d(Matrix3dc var1) {
      this.set(var1);
   }

   public Matrix3d(Matrix3fc var1) {
      this.set(var1);
   }

   public Matrix3d(Matrix4fc var1) {
      this.set(var1);
   }

   public Matrix3d(Matrix4dc var1) {
      this.set(var1);
   }

   public Matrix3d(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      this.m00 = var1;
      this.m01 = var3;
      this.m02 = var5;
      this.m10 = var7;
      this.m11 = var9;
      this.m12 = var11;
      this.m20 = var13;
      this.m21 = var15;
      this.m22 = var17;
   }

   public Matrix3d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Matrix3d(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      this.set(var1, var2, var3);
   }

   public double m00() {
      return this.m00;
   }

   public double m01() {
      return this.m01;
   }

   public double m02() {
      return this.m02;
   }

   public double m10() {
      return this.m10;
   }

   public double m11() {
      return this.m11;
   }

   public double m12() {
      return this.m12;
   }

   public double m20() {
      return this.m20;
   }

   public double m21() {
      return this.m21;
   }

   public double m22() {
      return this.m22;
   }

   public Matrix3d m00(double var1) {
      this.m00 = var1;
      return this;
   }

   public Matrix3d m01(double var1) {
      this.m01 = var1;
      return this;
   }

   public Matrix3d m02(double var1) {
      this.m02 = var1;
      return this;
   }

   public Matrix3d m10(double var1) {
      this.m10 = var1;
      return this;
   }

   public Matrix3d m11(double var1) {
      this.m11 = var1;
      return this;
   }

   public Matrix3d m12(double var1) {
      this.m12 = var1;
      return this;
   }

   public Matrix3d m20(double var1) {
      this.m20 = var1;
      return this;
   }

   public Matrix3d m21(double var1) {
      this.m21 = var1;
      return this;
   }

   public Matrix3d m22(double var1) {
      this.m22 = var1;
      return this;
   }

   Matrix3d _m00(double var1) {
      this.m00 = var1;
      return this;
   }

   Matrix3d _m01(double var1) {
      this.m01 = var1;
      return this;
   }

   Matrix3d _m02(double var1) {
      this.m02 = var1;
      return this;
   }

   Matrix3d _m10(double var1) {
      this.m10 = var1;
      return this;
   }

   Matrix3d _m11(double var1) {
      this.m11 = var1;
      return this;
   }

   Matrix3d _m12(double var1) {
      this.m12 = var1;
      return this;
   }

   Matrix3d _m20(double var1) {
      this.m20 = var1;
      return this;
   }

   Matrix3d _m21(double var1) {
      this.m21 = var1;
      return this;
   }

   Matrix3d _m22(double var1) {
      this.m22 = var1;
      return this;
   }

   public Matrix3d set(Matrix3dc var1) {
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

   public Matrix3d setTransposed(Matrix3dc var1) {
      double var2 = var1.m01();
      double var4 = var1.m21();
      double var6 = var1.m02();
      double var8 = var1.m12();
      return this._m00(var1.m00())._m01(var1.m10())._m02(var1.m20())._m10(var2)._m11(var1.m11())._m12(var4)._m20(var6)._m21(var8)._m22(var1.m22());
   }

   public Matrix3d set(Matrix3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = (double)var1.m02();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = (double)var1.m12();
      this.m20 = (double)var1.m20();
      this.m21 = (double)var1.m21();
      this.m22 = (double)var1.m22();
      return this;
   }

   public Matrix3d setTransposed(Matrix3fc var1) {
      float var2 = var1.m01();
      float var3 = var1.m21();
      float var4 = var1.m02();
      float var5 = var1.m12();
      return this._m00((double)var1.m00())._m01((double)var1.m10())._m02((double)var1.m20())._m10((double)var2)._m11((double)var1.m11())._m12((double)var3)._m20((double)var4)._m21((double)var5)._m22((double)var1.m22());
   }

   public Matrix3d set(Matrix4x3dc var1) {
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

   public Matrix3d set(Matrix4fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = (double)var1.m02();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = (double)var1.m12();
      this.m20 = (double)var1.m20();
      this.m21 = (double)var1.m21();
      this.m22 = (double)var1.m22();
      return this;
   }

   public Matrix3d set(Matrix4dc var1) {
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

   public Matrix3d set(Matrix2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = 0.0D;
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      return this;
   }

   public Matrix3d set(Matrix2dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = 0.0D;
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      return this;
   }

   public Matrix3d set(AxisAngle4f var1) {
      double var2 = (double)var1.x;
      double var4 = (double)var1.y;
      double var6 = (double)var1.z;
      double var8 = (double)var1.angle;
      double var10 = Math.invsqrt(var2 * var2 + var4 * var4 + var6 * var6);
      var2 *= var10;
      var4 *= var10;
      var6 *= var10;
      double var12 = Math.sin(var8);
      double var14 = Math.cosFromSin(var12, var8);
      double var16 = 1.0D - var14;
      this.m00 = var14 + var2 * var2 * var16;
      this.m11 = var14 + var4 * var4 * var16;
      this.m22 = var14 + var6 * var6 * var16;
      double var18 = var2 * var4 * var16;
      double var20 = var6 * var12;
      this.m10 = var18 - var20;
      this.m01 = var18 + var20;
      var18 = var2 * var6 * var16;
      var20 = var4 * var12;
      this.m20 = var18 + var20;
      this.m02 = var18 - var20;
      var18 = var4 * var6 * var16;
      var20 = var2 * var12;
      this.m21 = var18 - var20;
      this.m12 = var18 + var20;
      return this;
   }

   public Matrix3d set(AxisAngle4d var1) {
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
      this.m00 = var14 + var2 * var2 * var16;
      this.m11 = var14 + var4 * var4 * var16;
      this.m22 = var14 + var6 * var6 * var16;
      double var18 = var2 * var4 * var16;
      double var20 = var6 * var12;
      this.m10 = var18 - var20;
      this.m01 = var18 + var20;
      var18 = var2 * var6 * var16;
      var20 = var4 * var12;
      this.m20 = var18 + var20;
      this.m02 = var18 - var20;
      var18 = var4 * var6 * var16;
      var20 = var2 * var12;
      this.m21 = var18 - var20;
      this.m12 = var18 + var20;
      return this;
   }

   public Matrix3d set(Quaternionfc var1) {
      return this.rotation(var1);
   }

   public Matrix3d set(Quaterniondc var1) {
      return this.rotation(var1);
   }

   public Matrix3d mul(Matrix3dc var1) {
      return this.mul(var1, this);
   }

   public Matrix3d mul(Matrix3dc var1, Matrix3d var2) {
      double var3 = Math.fma(this.m00, var1.m00(), Math.fma(this.m10, var1.m01(), this.m20 * var1.m02()));
      double var5 = Math.fma(this.m01, var1.m00(), Math.fma(this.m11, var1.m01(), this.m21 * var1.m02()));
      double var7 = Math.fma(this.m02, var1.m00(), Math.fma(this.m12, var1.m01(), this.m22 * var1.m02()));
      double var9 = Math.fma(this.m00, var1.m10(), Math.fma(this.m10, var1.m11(), this.m20 * var1.m12()));
      double var11 = Math.fma(this.m01, var1.m10(), Math.fma(this.m11, var1.m11(), this.m21 * var1.m12()));
      double var13 = Math.fma(this.m02, var1.m10(), Math.fma(this.m12, var1.m11(), this.m22 * var1.m12()));
      double var15 = Math.fma(this.m00, var1.m20(), Math.fma(this.m10, var1.m21(), this.m20 * var1.m22()));
      double var17 = Math.fma(this.m01, var1.m20(), Math.fma(this.m11, var1.m21(), this.m21 * var1.m22()));
      double var19 = Math.fma(this.m02, var1.m20(), Math.fma(this.m12, var1.m21(), this.m22 * var1.m22()));
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m02 = var7;
      var2.m10 = var9;
      var2.m11 = var11;
      var2.m12 = var13;
      var2.m20 = var15;
      var2.m21 = var17;
      var2.m22 = var19;
      return var2;
   }

   public Matrix3d mulLocal(Matrix3dc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix3d mulLocal(Matrix3dc var1, Matrix3d var2) {
      double var3 = var1.m00() * this.m00 + var1.m10() * this.m01 + var1.m20() * this.m02;
      double var5 = var1.m01() * this.m00 + var1.m11() * this.m01 + var1.m21() * this.m02;
      double var7 = var1.m02() * this.m00 + var1.m12() * this.m01 + var1.m22() * this.m02;
      double var9 = var1.m00() * this.m10 + var1.m10() * this.m11 + var1.m20() * this.m12;
      double var11 = var1.m01() * this.m10 + var1.m11() * this.m11 + var1.m21() * this.m12;
      double var13 = var1.m02() * this.m10 + var1.m12() * this.m11 + var1.m22() * this.m12;
      double var15 = var1.m00() * this.m20 + var1.m10() * this.m21 + var1.m20() * this.m22;
      double var17 = var1.m01() * this.m20 + var1.m11() * this.m21 + var1.m21() * this.m22;
      double var19 = var1.m02() * this.m20 + var1.m12() * this.m21 + var1.m22() * this.m22;
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m02 = var7;
      var2.m10 = var9;
      var2.m11 = var11;
      var2.m12 = var13;
      var2.m20 = var15;
      var2.m21 = var17;
      var2.m22 = var19;
      return var2;
   }

   public Matrix3d mul(Matrix3fc var1) {
      return this.mul(var1, this);
   }

   public Matrix3d mul(Matrix3fc var1, Matrix3d var2) {
      double var3 = Math.fma(this.m00, (double)var1.m00(), Math.fma(this.m10, (double)var1.m01(), this.m20 * (double)var1.m02()));
      double var5 = Math.fma(this.m01, (double)var1.m00(), Math.fma(this.m11, (double)var1.m01(), this.m21 * (double)var1.m02()));
      double var7 = Math.fma(this.m02, (double)var1.m00(), Math.fma(this.m12, (double)var1.m01(), this.m22 * (double)var1.m02()));
      double var9 = Math.fma(this.m00, (double)var1.m10(), Math.fma(this.m10, (double)var1.m11(), this.m20 * (double)var1.m12()));
      double var11 = Math.fma(this.m01, (double)var1.m10(), Math.fma(this.m11, (double)var1.m11(), this.m21 * (double)var1.m12()));
      double var13 = Math.fma(this.m02, (double)var1.m10(), Math.fma(this.m12, (double)var1.m11(), this.m22 * (double)var1.m12()));
      double var15 = Math.fma(this.m00, (double)var1.m20(), Math.fma(this.m10, (double)var1.m21(), this.m20 * (double)var1.m22()));
      double var17 = Math.fma(this.m01, (double)var1.m20(), Math.fma(this.m11, (double)var1.m21(), this.m21 * (double)var1.m22()));
      double var19 = Math.fma(this.m02, (double)var1.m20(), Math.fma(this.m12, (double)var1.m21(), this.m22 * (double)var1.m22()));
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m02 = var7;
      var2.m10 = var9;
      var2.m11 = var11;
      var2.m12 = var13;
      var2.m20 = var15;
      var2.m21 = var17;
      var2.m22 = var19;
      return var2;
   }

   public Matrix3d set(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      this.m00 = var1;
      this.m01 = var3;
      this.m02 = var5;
      this.m10 = var7;
      this.m11 = var9;
      this.m12 = var11;
      this.m20 = var13;
      this.m21 = var15;
      this.m22 = var17;
      return this;
   }

   public Matrix3d set(double[] var1) {
      this.m00 = var1[0];
      this.m01 = var1[1];
      this.m02 = var1[2];
      this.m10 = var1[3];
      this.m11 = var1[4];
      this.m12 = var1[5];
      this.m20 = var1[6];
      this.m21 = var1[7];
      this.m22 = var1[8];
      return this;
   }

   public Matrix3d set(float[] var1) {
      this.m00 = (double)var1[0];
      this.m01 = (double)var1[1];
      this.m02 = (double)var1[2];
      this.m10 = (double)var1[3];
      this.m11 = (double)var1[4];
      this.m12 = (double)var1[5];
      this.m20 = (double)var1[6];
      this.m21 = (double)var1[7];
      this.m22 = (double)var1[8];
      return this;
   }

   public double determinant() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public Matrix3d invert() {
      return this.invert(this);
   }

   public Matrix3d invert(Matrix3d var1) {
      double var2 = Math.fma(this.m00, this.m11, -this.m01 * this.m10);
      double var4 = Math.fma(this.m02, this.m10, -this.m00 * this.m12);
      double var6 = Math.fma(this.m01, this.m12, -this.m02 * this.m11);
      double var8 = Math.fma(var2, this.m22, Math.fma(var4, this.m21, var6 * this.m20));
      double var10 = 1.0D / var8;
      double var12 = Math.fma(this.m11, this.m22, -this.m21 * this.m12) * var10;
      double var14 = Math.fma(this.m21, this.m02, -this.m01 * this.m22) * var10;
      double var16 = var6 * var10;
      double var18 = Math.fma(this.m20, this.m12, -this.m10 * this.m22) * var10;
      double var20 = Math.fma(this.m00, this.m22, -this.m20 * this.m02) * var10;
      double var22 = var4 * var10;
      double var24 = Math.fma(this.m10, this.m21, -this.m20 * this.m11) * var10;
      double var26 = Math.fma(this.m20, this.m01, -this.m00 * this.m21) * var10;
      double var28 = var2 * var10;
      var1.m00 = var12;
      var1.m01 = var14;
      var1.m02 = var16;
      var1.m10 = var18;
      var1.m11 = var20;
      var1.m12 = var22;
      var1.m20 = var24;
      var1.m21 = var26;
      var1.m22 = var28;
      return var1;
   }

   public Matrix3d transpose() {
      return this.transpose(this);
   }

   public Matrix3d transpose(Matrix3d var1) {
      var1.set(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
      return var1;
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
      String var10000 = Runtime.format(this.m00, var1);
      return var10000 + " " + Runtime.format(this.m10, var1) + " " + Runtime.format(this.m20, var1) + "\n" + Runtime.format(this.m01, var1) + " " + Runtime.format(this.m11, var1) + " " + Runtime.format(this.m21, var1) + "\n" + Runtime.format(this.m02, var1) + " " + Runtime.format(this.m12, var1) + " " + Runtime.format(this.m22, var1) + "\n";
   }

   public Matrix3d get(Matrix3d var1) {
      return var1.set((Matrix3dc)this);
   }

   public AxisAngle4f getRotation(AxisAngle4f var1) {
      return var1.set((Matrix3dc)this);
   }

   public Quaternionf getUnnormalizedRotation(Quaternionf var1) {
      return var1.setFromUnnormalized((Matrix3dc)this);
   }

   public Quaternionf getNormalizedRotation(Quaternionf var1) {
      return var1.setFromNormalized((Matrix3dc)this);
   }

   public Quaterniond getUnnormalizedRotation(Quaterniond var1) {
      return var1.setFromUnnormalized((Matrix3dc)this);
   }

   public Quaterniond getNormalizedRotation(Quaterniond var1) {
      return var1.setFromNormalized((Matrix3dc)this);
   }

   public DoubleBuffer get(DoubleBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public DoubleBuffer get(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public FloatBuffer get(FloatBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public FloatBuffer get(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.putf(this, var1, var2);
      return var2;
   }

   public ByteBuffer get(ByteBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public ByteBuffer get(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public ByteBuffer getFloats(ByteBuffer var1) {
      return this.getFloats(var1.position(), var1);
   }

   public ByteBuffer getFloats(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putf(this, var1, var2);
      return var2;
   }

   public Matrix3dc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public double[] get(double[] var1, int var2) {
      var1[var2 + 0] = this.m00;
      var1[var2 + 1] = this.m01;
      var1[var2 + 2] = this.m02;
      var1[var2 + 3] = this.m10;
      var1[var2 + 4] = this.m11;
      var1[var2 + 5] = this.m12;
      var1[var2 + 6] = this.m20;
      var1[var2 + 7] = this.m21;
      var1[var2 + 8] = this.m22;
      return var1;
   }

   public double[] get(double[] var1) {
      return this.get((double[])var1, 0);
   }

   public float[] get(float[] var1, int var2) {
      var1[var2 + 0] = (float)this.m00;
      var1[var2 + 1] = (float)this.m01;
      var1[var2 + 2] = (float)this.m02;
      var1[var2 + 3] = (float)this.m10;
      var1[var2 + 4] = (float)this.m11;
      var1[var2 + 5] = (float)this.m12;
      var1[var2 + 6] = (float)this.m20;
      var1[var2 + 7] = (float)this.m21;
      var1[var2 + 8] = (float)this.m22;
      return var1;
   }

   public float[] get(float[] var1) {
      return this.get((float[])var1, 0);
   }

   public Matrix3d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix3d set(FloatBuffer var1) {
      MemUtil.INSTANCE.getf(this, var1.position(), var1);
      return this;
   }

   public Matrix3d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix3d setFloats(ByteBuffer var1) {
      MemUtil.INSTANCE.getf(this, var1.position(), var1);
      return this;
   }

   public Matrix3d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix3d set(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
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

   public Matrix3d zero() {
      this.m00 = 0.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 0.0D;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 0.0D;
      return this;
   }

   public Matrix3d identity() {
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      return this;
   }

   public Matrix3d scaling(double var1) {
      this.m00 = var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var1;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var1;
      return this;
   }

   public Matrix3d scaling(double var1, double var3, double var5) {
      this.m00 = var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var3;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var5;
      return this;
   }

   public Matrix3d scaling(Vector3dc var1) {
      this.m00 = var1.x();
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var1.y();
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = var1.z();
      return this;
   }

   public Matrix3d scale(Vector3dc var1, Matrix3d var2) {
      return this.scale(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix3d scale(Vector3dc var1) {
      return this.scale(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix3d scale(double var1, double var3, double var5, Matrix3d var7) {
      var7.m00 = this.m00 * var1;
      var7.m01 = this.m01 * var1;
      var7.m02 = this.m02 * var1;
      var7.m10 = this.m10 * var3;
      var7.m11 = this.m11 * var3;
      var7.m12 = this.m12 * var3;
      var7.m20 = this.m20 * var5;
      var7.m21 = this.m21 * var5;
      var7.m22 = this.m22 * var5;
      return var7;
   }

   public Matrix3d scale(double var1, double var3, double var5) {
      return this.scale(var1, var3, var5, this);
   }

   public Matrix3d scale(double var1, Matrix3d var3) {
      return this.scale(var1, var1, var1, var3);
   }

   public Matrix3d scale(double var1) {
      return this.scale(var1, var1, var1);
   }

   public Matrix3d scaleLocal(double var1, double var3, double var5, Matrix3d var7) {
      double var8 = var1 * this.m00;
      double var10 = var3 * this.m01;
      double var12 = var5 * this.m02;
      double var14 = var1 * this.m10;
      double var16 = var3 * this.m11;
      double var18 = var5 * this.m12;
      double var20 = var1 * this.m20;
      double var22 = var3 * this.m21;
      double var24 = var5 * this.m22;
      var7.m00 = var8;
      var7.m01 = var10;
      var7.m02 = var12;
      var7.m10 = var14;
      var7.m11 = var16;
      var7.m12 = var18;
      var7.m20 = var20;
      var7.m21 = var22;
      var7.m22 = var24;
      return var7;
   }

   public Matrix3d scaleLocal(double var1, double var3, double var5) {
      return this.scaleLocal(var1, var3, var5, this);
   }

   public Matrix3d rotation(double var1, Vector3dc var3) {
      return this.rotation(var1, var3.x(), var3.y(), var3.z());
   }

   public Matrix3d rotation(double var1, Vector3fc var3) {
      return this.rotation(var1, (double)var3.x(), (double)var3.y(), (double)var3.z());
   }

   public Matrix3d rotation(AxisAngle4f var1) {
      return this.rotation((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Matrix3d rotation(AxisAngle4d var1) {
      return this.rotation(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix3d rotation(double var1, double var3, double var5, double var7) {
      double var9 = Math.sin(var1);
      double var11 = Math.cosFromSin(var9, var1);
      double var13 = 1.0D - var11;
      double var15 = var3 * var5;
      double var17 = var3 * var7;
      double var19 = var5 * var7;
      this.m00 = var11 + var3 * var3 * var13;
      this.m10 = var15 * var13 - var7 * var9;
      this.m20 = var17 * var13 + var5 * var9;
      this.m01 = var15 * var13 + var7 * var9;
      this.m11 = var11 + var5 * var5 * var13;
      this.m21 = var19 * var13 - var3 * var9;
      this.m02 = var17 * var13 - var5 * var9;
      this.m12 = var19 * var13 + var3 * var9;
      this.m22 = var11 + var7 * var7 * var13;
      return this;
   }

   public Matrix3d rotationX(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var5;
      this.m12 = var3;
      this.m20 = 0.0D;
      this.m21 = -var3;
      this.m22 = var5;
      return this;
   }

   public Matrix3d rotationY(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      this.m00 = var5;
      this.m01 = 0.0D;
      this.m02 = -var3;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m12 = 0.0D;
      this.m20 = var3;
      this.m21 = 0.0D;
      this.m22 = var5;
      return this;
   }

   public Matrix3d rotationZ(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      this.m00 = var5;
      this.m01 = var3;
      this.m02 = 0.0D;
      this.m10 = -var3;
      this.m11 = var5;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      return this;
   }

   public Matrix3d rotationXYZ(double var1, double var3, double var5) {
      double var7 = Math.sin(var1);
      double var9 = Math.cosFromSin(var7, var1);
      double var11 = Math.sin(var3);
      double var13 = Math.cosFromSin(var11, var3);
      double var15 = Math.sin(var5);
      double var17 = Math.cosFromSin(var15, var5);
      double var19 = -var7;
      double var21 = -var11;
      double var23 = -var15;
      double var35 = var19 * var21;
      double var37 = var9 * var21;
      this.m20 = var11;
      this.m21 = var19 * var13;
      this.m22 = var9 * var13;
      this.m00 = var13 * var17;
      this.m01 = var35 * var17 + var9 * var15;
      this.m02 = var37 * var17 + var7 * var15;
      this.m10 = var13 * var23;
      this.m11 = var35 * var23 + var9 * var17;
      this.m12 = var37 * var23 + var7 * var17;
      return this;
   }

   public Matrix3d rotationZYX(double var1, double var3, double var5) {
      double var7 = Math.sin(var5);
      double var9 = Math.cosFromSin(var7, var5);
      double var11 = Math.sin(var3);
      double var13 = Math.cosFromSin(var11, var3);
      double var15 = Math.sin(var1);
      double var17 = Math.cosFromSin(var15, var1);
      double var19 = -var15;
      double var21 = -var11;
      double var23 = -var7;
      double var33 = var17 * var11;
      double var35 = var15 * var11;
      this.m00 = var17 * var13;
      this.m01 = var15 * var13;
      this.m02 = var21;
      this.m10 = var19 * var9 + var33 * var7;
      this.m11 = var17 * var9 + var35 * var7;
      this.m12 = var13 * var7;
      this.m20 = var19 * var23 + var33 * var9;
      this.m21 = var17 * var23 + var35 * var9;
      this.m22 = var13 * var9;
      return this;
   }

   public Matrix3d rotationYXZ(double var1, double var3, double var5) {
      double var7 = Math.sin(var3);
      double var9 = Math.cosFromSin(var7, var3);
      double var11 = Math.sin(var1);
      double var13 = Math.cosFromSin(var11, var1);
      double var15 = Math.sin(var5);
      double var17 = Math.cosFromSin(var15, var5);
      double var19 = -var11;
      double var21 = -var7;
      double var23 = -var15;
      double var33 = var11 * var7;
      double var37 = var13 * var7;
      this.m20 = var11 * var9;
      this.m21 = var21;
      this.m22 = var13 * var9;
      this.m00 = var13 * var17 + var33 * var15;
      this.m01 = var9 * var15;
      this.m02 = var19 * var17 + var37 * var15;
      this.m10 = var13 * var23 + var33 * var17;
      this.m11 = var9 * var17;
      this.m12 = var19 * var23 + var37 * var17;
      return this;
   }

   public Matrix3d rotation(Quaterniondc var1) {
      double var2 = var1.w() * var1.w();
      double var4 = var1.x() * var1.x();
      double var6 = var1.y() * var1.y();
      double var8 = var1.z() * var1.z();
      double var10 = var1.z() * var1.w();
      double var12 = var10 + var10;
      double var14 = var1.x() * var1.y();
      double var16 = var14 + var14;
      double var18 = var1.x() * var1.z();
      double var20 = var18 + var18;
      double var22 = var1.y() * var1.w();
      double var24 = var22 + var22;
      double var26 = var1.y() * var1.z();
      double var28 = var26 + var26;
      double var30 = var1.x() * var1.w();
      double var32 = var30 + var30;
      this.m00 = var2 + var4 - var8 - var6;
      this.m01 = var16 + var12;
      this.m02 = var20 - var24;
      this.m10 = -var12 + var16;
      this.m11 = var6 - var8 + var2 - var4;
      this.m12 = var28 + var32;
      this.m20 = var24 + var20;
      this.m21 = var28 - var32;
      this.m22 = var8 - var6 - var4 + var2;
      return this;
   }

   public Matrix3d rotation(Quaternionfc var1) {
      double var2 = (double)(var1.w() * var1.w());
      double var4 = (double)(var1.x() * var1.x());
      double var6 = (double)(var1.y() * var1.y());
      double var8 = (double)(var1.z() * var1.z());
      double var10 = (double)(var1.z() * var1.w());
      double var12 = var10 + var10;
      double var14 = (double)(var1.x() * var1.y());
      double var16 = var14 + var14;
      double var18 = (double)(var1.x() * var1.z());
      double var20 = var18 + var18;
      double var22 = (double)(var1.y() * var1.w());
      double var24 = var22 + var22;
      double var26 = (double)(var1.y() * var1.z());
      double var28 = var26 + var26;
      double var30 = (double)(var1.x() * var1.w());
      double var32 = var30 + var30;
      this.m00 = var2 + var4 - var8 - var6;
      this.m01 = var16 + var12;
      this.m02 = var20 - var24;
      this.m10 = -var12 + var16;
      this.m11 = var6 - var8 + var2 - var4;
      this.m12 = var28 + var32;
      this.m20 = var24 + var20;
      this.m21 = var28 - var32;
      this.m22 = var8 - var6 - var4 + var2;
      return this;
   }

   public Vector3d transform(Vector3d var1) {
      return var1.mul((Matrix3dc)this);
   }

   public Vector3d transform(Vector3dc var1, Vector3d var2) {
      var1.mul((Matrix3dc)this, (Vector3d)var2);
      return var2;
   }

   public Vector3f transform(Vector3f var1) {
      return var1.mul((Matrix3dc)this);
   }

   public Vector3f transform(Vector3fc var1, Vector3f var2) {
      return var1.mul((Matrix3dc)this, var2);
   }

   public Vector3d transform(double var1, double var3, double var5, Vector3d var7) {
      return var7.set(Math.fma(this.m00, var1, Math.fma(this.m10, var3, this.m20 * var5)), Math.fma(this.m01, var1, Math.fma(this.m11, var3, this.m21 * var5)), Math.fma(this.m02, var1, Math.fma(this.m12, var3, this.m22 * var5)));
   }

   public Vector3d transformTranspose(Vector3d var1) {
      return var1.mulTranspose((Matrix3dc)this);
   }

   public Vector3d transformTranspose(Vector3dc var1, Vector3d var2) {
      return var1.mulTranspose((Matrix3dc)this, var2);
   }

   public Vector3d transformTranspose(double var1, double var3, double var5, Vector3d var7) {
      return var7.set(Math.fma(this.m00, var1, Math.fma(this.m01, var3, this.m02 * var5)), Math.fma(this.m10, var1, Math.fma(this.m11, var3, this.m12 * var5)), Math.fma(this.m20, var1, Math.fma(this.m21, var3, this.m22 * var5)));
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.m00);
      var1.writeDouble(this.m01);
      var1.writeDouble(this.m02);
      var1.writeDouble(this.m10);
      var1.writeDouble(this.m11);
      var1.writeDouble(this.m12);
      var1.writeDouble(this.m20);
      var1.writeDouble(this.m21);
      var1.writeDouble(this.m22);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readDouble();
      this.m01 = var1.readDouble();
      this.m02 = var1.readDouble();
      this.m10 = var1.readDouble();
      this.m11 = var1.readDouble();
      this.m12 = var1.readDouble();
      this.m20 = var1.readDouble();
      this.m21 = var1.readDouble();
      this.m22 = var1.readDouble();
   }

   public Matrix3d rotateX(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var10 = -var4;
      double var16 = this.m10 * var6 + this.m20 * var4;
      double var18 = this.m11 * var6 + this.m21 * var4;
      double var20 = this.m12 * var6 + this.m22 * var4;
      var3.m20 = this.m10 * var10 + this.m20 * var6;
      var3.m21 = this.m11 * var10 + this.m21 * var6;
      var3.m22 = this.m12 * var10 + this.m22 * var6;
      var3.m10 = var16;
      var3.m11 = var18;
      var3.m12 = var20;
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m02 = this.m02;
      return var3;
   }

   public Matrix3d rotateX(double var1) {
      return this.rotateX(var1, this);
   }

   public Matrix3d rotateY(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var12 = -var4;
      double var16 = this.m00 * var6 + this.m20 * var12;
      double var18 = this.m01 * var6 + this.m21 * var12;
      double var20 = this.m02 * var6 + this.m22 * var12;
      var3.m20 = this.m00 * var4 + this.m20 * var6;
      var3.m21 = this.m01 * var4 + this.m21 * var6;
      var3.m22 = this.m02 * var4 + this.m22 * var6;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m02 = var20;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      var3.m12 = this.m12;
      return var3;
   }

   public Matrix3d rotateY(double var1) {
      return this.rotateY(var1, this);
   }

   public Matrix3d rotateZ(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var10 = -var4;
      double var16 = this.m00 * var6 + this.m10 * var4;
      double var18 = this.m01 * var6 + this.m11 * var4;
      double var20 = this.m02 * var6 + this.m12 * var4;
      var3.m10 = this.m00 * var10 + this.m10 * var6;
      var3.m11 = this.m01 * var10 + this.m11 * var6;
      var3.m12 = this.m02 * var10 + this.m12 * var6;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m02 = var20;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      var3.m22 = this.m22;
      return var3;
   }

   public Matrix3d rotateZ(double var1) {
      return this.rotateZ(var1, this);
   }

   public Matrix3d rotateXYZ(double var1, double var3, double var5) {
      return this.rotateXYZ(var1, var3, var5, this);
   }

   public Matrix3d rotateXYZ(double var1, double var3, double var5, Matrix3d var7) {
      double var8 = Math.sin(var1);
      double var10 = Math.cosFromSin(var8, var1);
      double var12 = Math.sin(var3);
      double var14 = Math.cosFromSin(var12, var3);
      double var16 = Math.sin(var5);
      double var18 = Math.cosFromSin(var16, var5);
      double var20 = -var8;
      double var22 = -var12;
      double var24 = -var16;
      double var26 = this.m10 * var10 + this.m20 * var8;
      double var28 = this.m11 * var10 + this.m21 * var8;
      double var30 = this.m12 * var10 + this.m22 * var8;
      double var32 = this.m10 * var20 + this.m20 * var10;
      double var34 = this.m11 * var20 + this.m21 * var10;
      double var36 = this.m12 * var20 + this.m22 * var10;
      double var38 = this.m00 * var14 + var32 * var22;
      double var40 = this.m01 * var14 + var34 * var22;
      double var42 = this.m02 * var14 + var36 * var22;
      var7.m20 = this.m00 * var12 + var32 * var14;
      var7.m21 = this.m01 * var12 + var34 * var14;
      var7.m22 = this.m02 * var12 + var36 * var14;
      var7.m00 = var38 * var18 + var26 * var16;
      var7.m01 = var40 * var18 + var28 * var16;
      var7.m02 = var42 * var18 + var30 * var16;
      var7.m10 = var38 * var24 + var26 * var18;
      var7.m11 = var40 * var24 + var28 * var18;
      var7.m12 = var42 * var24 + var30 * var18;
      return var7;
   }

   public Matrix3d rotateZYX(double var1, double var3, double var5) {
      return this.rotateZYX(var1, var3, var5, this);
   }

   public Matrix3d rotateZYX(double var1, double var3, double var5, Matrix3d var7) {
      double var8 = Math.sin(var5);
      double var10 = Math.cosFromSin(var8, var5);
      double var12 = Math.sin(var3);
      double var14 = Math.cosFromSin(var12, var3);
      double var16 = Math.sin(var1);
      double var18 = Math.cosFromSin(var16, var1);
      double var20 = -var16;
      double var22 = -var12;
      double var24 = -var8;
      double var26 = this.m00 * var18 + this.m10 * var16;
      double var28 = this.m01 * var18 + this.m11 * var16;
      double var30 = this.m02 * var18 + this.m12 * var16;
      double var32 = this.m00 * var20 + this.m10 * var18;
      double var34 = this.m01 * var20 + this.m11 * var18;
      double var36 = this.m02 * var20 + this.m12 * var18;
      double var38 = var26 * var12 + this.m20 * var14;
      double var40 = var28 * var12 + this.m21 * var14;
      double var42 = var30 * var12 + this.m22 * var14;
      var7.m00 = var26 * var14 + this.m20 * var22;
      var7.m01 = var28 * var14 + this.m21 * var22;
      var7.m02 = var30 * var14 + this.m22 * var22;
      var7.m10 = var32 * var10 + var38 * var8;
      var7.m11 = var34 * var10 + var40 * var8;
      var7.m12 = var36 * var10 + var42 * var8;
      var7.m20 = var32 * var24 + var38 * var10;
      var7.m21 = var34 * var24 + var40 * var10;
      var7.m22 = var36 * var24 + var42 * var10;
      return var7;
   }

   public Matrix3d rotateYXZ(Vector3d var1) {
      return this.rotateYXZ(var1.y, var1.x, var1.z);
   }

   public Matrix3d rotateYXZ(double var1, double var3, double var5) {
      return this.rotateYXZ(var1, var3, var5, this);
   }

   public Matrix3d rotateYXZ(double var1, double var3, double var5, Matrix3d var7) {
      double var8 = Math.sin(var3);
      double var10 = Math.cosFromSin(var8, var3);
      double var12 = Math.sin(var1);
      double var14 = Math.cosFromSin(var12, var1);
      double var16 = Math.sin(var5);
      double var18 = Math.cosFromSin(var16, var5);
      double var20 = -var12;
      double var22 = -var8;
      double var24 = -var16;
      double var26 = this.m00 * var12 + this.m20 * var14;
      double var28 = this.m01 * var12 + this.m21 * var14;
      double var30 = this.m02 * var12 + this.m22 * var14;
      double var32 = this.m00 * var14 + this.m20 * var20;
      double var34 = this.m01 * var14 + this.m21 * var20;
      double var36 = this.m02 * var14 + this.m22 * var20;
      double var38 = this.m10 * var10 + var26 * var8;
      double var40 = this.m11 * var10 + var28 * var8;
      double var42 = this.m12 * var10 + var30 * var8;
      var7.m20 = this.m10 * var22 + var26 * var10;
      var7.m21 = this.m11 * var22 + var28 * var10;
      var7.m22 = this.m12 * var22 + var30 * var10;
      var7.m00 = var32 * var18 + var38 * var16;
      var7.m01 = var34 * var18 + var40 * var16;
      var7.m02 = var36 * var18 + var42 * var16;
      var7.m10 = var32 * var24 + var38 * var18;
      var7.m11 = var34 * var24 + var40 * var18;
      var7.m12 = var36 * var24 + var42 * var18;
      return var7;
   }

   public Matrix3d rotate(double var1, double var3, double var5, double var7) {
      return this.rotate(var1, var3, var5, var7, this);
   }

   public Matrix3d rotate(double var1, double var3, double var5, double var7, Matrix3d var9) {
      double var10 = Math.sin(var1);
      double var12 = Math.cosFromSin(var10, var1);
      double var14 = 1.0D - var12;
      double var16 = var3 * var3;
      double var18 = var3 * var5;
      double var20 = var3 * var7;
      double var22 = var5 * var5;
      double var24 = var5 * var7;
      double var26 = var7 * var7;
      double var28 = var16 * var14 + var12;
      double var30 = var18 * var14 + var7 * var10;
      double var32 = var20 * var14 - var5 * var10;
      double var34 = var18 * var14 - var7 * var10;
      double var36 = var22 * var14 + var12;
      double var38 = var24 * var14 + var3 * var10;
      double var40 = var20 * var14 + var5 * var10;
      double var42 = var24 * var14 - var3 * var10;
      double var44 = var26 * var14 + var12;
      double var46 = this.m00 * var28 + this.m10 * var30 + this.m20 * var32;
      double var48 = this.m01 * var28 + this.m11 * var30 + this.m21 * var32;
      double var50 = this.m02 * var28 + this.m12 * var30 + this.m22 * var32;
      double var52 = this.m00 * var34 + this.m10 * var36 + this.m20 * var38;
      double var54 = this.m01 * var34 + this.m11 * var36 + this.m21 * var38;
      double var56 = this.m02 * var34 + this.m12 * var36 + this.m22 * var38;
      var9.m20 = this.m00 * var40 + this.m10 * var42 + this.m20 * var44;
      var9.m21 = this.m01 * var40 + this.m11 * var42 + this.m21 * var44;
      var9.m22 = this.m02 * var40 + this.m12 * var42 + this.m22 * var44;
      var9.m00 = var46;
      var9.m01 = var48;
      var9.m02 = var50;
      var9.m10 = var52;
      var9.m11 = var54;
      var9.m12 = var56;
      return var9;
   }

   public Matrix3d rotateLocal(double var1, double var3, double var5, double var7, Matrix3d var9) {
      double var10 = Math.sin(var1);
      double var12 = Math.cosFromSin(var10, var1);
      double var14 = 1.0D - var12;
      double var16 = var3 * var3;
      double var18 = var3 * var5;
      double var20 = var3 * var7;
      double var22 = var5 * var5;
      double var24 = var5 * var7;
      double var26 = var7 * var7;
      double var28 = var16 * var14 + var12;
      double var30 = var18 * var14 + var7 * var10;
      double var32 = var20 * var14 - var5 * var10;
      double var34 = var18 * var14 - var7 * var10;
      double var36 = var22 * var14 + var12;
      double var38 = var24 * var14 + var3 * var10;
      double var40 = var20 * var14 + var5 * var10;
      double var42 = var24 * var14 - var3 * var10;
      double var44 = var26 * var14 + var12;
      double var46 = var28 * this.m00 + var34 * this.m01 + var40 * this.m02;
      double var48 = var30 * this.m00 + var36 * this.m01 + var42 * this.m02;
      double var50 = var32 * this.m00 + var38 * this.m01 + var44 * this.m02;
      double var52 = var28 * this.m10 + var34 * this.m11 + var40 * this.m12;
      double var54 = var30 * this.m10 + var36 * this.m11 + var42 * this.m12;
      double var56 = var32 * this.m10 + var38 * this.m11 + var44 * this.m12;
      double var58 = var28 * this.m20 + var34 * this.m21 + var40 * this.m22;
      double var60 = var30 * this.m20 + var36 * this.m21 + var42 * this.m22;
      double var62 = var32 * this.m20 + var38 * this.m21 + var44 * this.m22;
      var9.m00 = var46;
      var9.m01 = var48;
      var9.m02 = var50;
      var9.m10 = var52;
      var9.m11 = var54;
      var9.m12 = var56;
      var9.m20 = var58;
      var9.m21 = var60;
      var9.m22 = var62;
      return var9;
   }

   public Matrix3d rotateLocal(double var1, double var3, double var5, double var7) {
      return this.rotateLocal(var1, var3, var5, var7, this);
   }

   public Matrix3d rotateLocalX(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m01 - var4 * this.m02;
      double var10 = var4 * this.m01 + var6 * this.m02;
      double var12 = var6 * this.m11 - var4 * this.m12;
      double var14 = var4 * this.m11 + var6 * this.m12;
      double var16 = var6 * this.m21 - var4 * this.m22;
      double var18 = var4 * this.m21 + var6 * this.m22;
      var3.m00 = this.m00;
      var3.m01 = var8;
      var3.m02 = var10;
      var3.m10 = this.m10;
      var3.m11 = var12;
      var3.m12 = var14;
      var3.m20 = this.m20;
      var3.m21 = var16;
      var3.m22 = var18;
      return var3;
   }

   public Matrix3d rotateLocalX(double var1) {
      return this.rotateLocalX(var1, this);
   }

   public Matrix3d rotateLocalY(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 + var4 * this.m02;
      double var10 = -var4 * this.m00 + var6 * this.m02;
      double var12 = var6 * this.m10 + var4 * this.m12;
      double var14 = -var4 * this.m10 + var6 * this.m12;
      double var16 = var6 * this.m20 + var4 * this.m22;
      double var18 = -var4 * this.m20 + var6 * this.m22;
      var3.m00 = var8;
      var3.m01 = this.m01;
      var3.m02 = var10;
      var3.m10 = var12;
      var3.m11 = this.m11;
      var3.m12 = var14;
      var3.m20 = var16;
      var3.m21 = this.m21;
      var3.m22 = var18;
      return var3;
   }

   public Matrix3d rotateLocalY(double var1) {
      return this.rotateLocalY(var1, this);
   }

   public Matrix3d rotateLocalZ(double var1, Matrix3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 - var4 * this.m01;
      double var10 = var4 * this.m00 + var6 * this.m01;
      double var12 = var6 * this.m10 - var4 * this.m11;
      double var14 = var4 * this.m10 + var6 * this.m11;
      double var16 = var6 * this.m20 - var4 * this.m21;
      double var18 = var4 * this.m20 + var6 * this.m21;
      var3.m00 = var8;
      var3.m01 = var10;
      var3.m02 = this.m02;
      var3.m10 = var12;
      var3.m11 = var14;
      var3.m12 = this.m12;
      var3.m20 = var16;
      var3.m21 = var18;
      var3.m22 = this.m22;
      return var3;
   }

   public Matrix3d rotateLocalZ(double var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Matrix3d rotateLocal(Quaterniondc var1, Matrix3d var2) {
      double var3 = var1.w() * var1.w();
      double var5 = var1.x() * var1.x();
      double var7 = var1.y() * var1.y();
      double var9 = var1.z() * var1.z();
      double var11 = var1.z() * var1.w();
      double var13 = var11 + var11;
      double var15 = var1.x() * var1.y();
      double var17 = var15 + var15;
      double var19 = var1.x() * var1.z();
      double var21 = var19 + var19;
      double var23 = var1.y() * var1.w();
      double var25 = var23 + var23;
      double var27 = var1.y() * var1.z();
      double var29 = var27 + var27;
      double var31 = var1.x() * var1.w();
      double var33 = var31 + var31;
      double var35 = var3 + var5 - var9 - var7;
      double var37 = var17 + var13;
      double var39 = var21 - var25;
      double var41 = var17 - var13;
      double var43 = var7 - var9 + var3 - var5;
      double var45 = var29 + var33;
      double var47 = var25 + var21;
      double var49 = var29 - var33;
      double var51 = var9 - var7 - var5 + var3;
      double var53 = var35 * this.m00 + var41 * this.m01 + var47 * this.m02;
      double var55 = var37 * this.m00 + var43 * this.m01 + var49 * this.m02;
      double var57 = var39 * this.m00 + var45 * this.m01 + var51 * this.m02;
      double var59 = var35 * this.m10 + var41 * this.m11 + var47 * this.m12;
      double var61 = var37 * this.m10 + var43 * this.m11 + var49 * this.m12;
      double var63 = var39 * this.m10 + var45 * this.m11 + var51 * this.m12;
      double var65 = var35 * this.m20 + var41 * this.m21 + var47 * this.m22;
      double var67 = var37 * this.m20 + var43 * this.m21 + var49 * this.m22;
      double var69 = var39 * this.m20 + var45 * this.m21 + var51 * this.m22;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      var2.m20 = var65;
      var2.m21 = var67;
      var2.m22 = var69;
      return var2;
   }

   public Matrix3d rotateLocal(Quaterniondc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix3d rotateLocal(Quaternionfc var1, Matrix3d var2) {
      double var3 = (double)(var1.w() * var1.w());
      double var5 = (double)(var1.x() * var1.x());
      double var7 = (double)(var1.y() * var1.y());
      double var9 = (double)(var1.z() * var1.z());
      double var11 = (double)(var1.z() * var1.w());
      double var13 = var11 + var11;
      double var15 = (double)(var1.x() * var1.y());
      double var17 = var15 + var15;
      double var19 = (double)(var1.x() * var1.z());
      double var21 = var19 + var19;
      double var23 = (double)(var1.y() * var1.w());
      double var25 = var23 + var23;
      double var27 = (double)(var1.y() * var1.z());
      double var29 = var27 + var27;
      double var31 = (double)(var1.x() * var1.w());
      double var33 = var31 + var31;
      double var35 = var3 + var5 - var9 - var7;
      double var37 = var17 + var13;
      double var39 = var21 - var25;
      double var41 = var17 - var13;
      double var43 = var7 - var9 + var3 - var5;
      double var45 = var29 + var33;
      double var47 = var25 + var21;
      double var49 = var29 - var33;
      double var51 = var9 - var7 - var5 + var3;
      double var53 = var35 * this.m00 + var41 * this.m01 + var47 * this.m02;
      double var55 = var37 * this.m00 + var43 * this.m01 + var49 * this.m02;
      double var57 = var39 * this.m00 + var45 * this.m01 + var51 * this.m02;
      double var59 = var35 * this.m10 + var41 * this.m11 + var47 * this.m12;
      double var61 = var37 * this.m10 + var43 * this.m11 + var49 * this.m12;
      double var63 = var39 * this.m10 + var45 * this.m11 + var51 * this.m12;
      double var65 = var35 * this.m20 + var41 * this.m21 + var47 * this.m22;
      double var67 = var37 * this.m20 + var43 * this.m21 + var49 * this.m22;
      double var69 = var39 * this.m20 + var45 * this.m21 + var51 * this.m22;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      var2.m20 = var65;
      var2.m21 = var67;
      var2.m22 = var69;
      return var2;
   }

   public Matrix3d rotateLocal(Quaternionfc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix3d rotate(Quaterniondc var1) {
      return this.rotate(var1, this);
   }

   public Matrix3d rotate(Quaterniondc var1, Matrix3d var2) {
      double var3 = var1.w() * var1.w();
      double var5 = var1.x() * var1.x();
      double var7 = var1.y() * var1.y();
      double var9 = var1.z() * var1.z();
      double var11 = var1.z() * var1.w();
      double var13 = var11 + var11;
      double var15 = var1.x() * var1.y();
      double var17 = var15 + var15;
      double var19 = var1.x() * var1.z();
      double var21 = var19 + var19;
      double var23 = var1.y() * var1.w();
      double var25 = var23 + var23;
      double var27 = var1.y() * var1.z();
      double var29 = var27 + var27;
      double var31 = var1.x() * var1.w();
      double var33 = var31 + var31;
      double var35 = var3 + var5 - var9 - var7;
      double var37 = var17 + var13;
      double var39 = var21 - var25;
      double var41 = var17 - var13;
      double var43 = var7 - var9 + var3 - var5;
      double var45 = var29 + var33;
      double var47 = var25 + var21;
      double var49 = var29 - var33;
      double var51 = var9 - var7 - var5 + var3;
      double var53 = this.m00 * var35 + this.m10 * var37 + this.m20 * var39;
      double var55 = this.m01 * var35 + this.m11 * var37 + this.m21 * var39;
      double var57 = this.m02 * var35 + this.m12 * var37 + this.m22 * var39;
      double var59 = this.m00 * var41 + this.m10 * var43 + this.m20 * var45;
      double var61 = this.m01 * var41 + this.m11 * var43 + this.m21 * var45;
      double var63 = this.m02 * var41 + this.m12 * var43 + this.m22 * var45;
      var2.m20 = this.m00 * var47 + this.m10 * var49 + this.m20 * var51;
      var2.m21 = this.m01 * var47 + this.m11 * var49 + this.m21 * var51;
      var2.m22 = this.m02 * var47 + this.m12 * var49 + this.m22 * var51;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      return var2;
   }

   public Matrix3d rotate(Quaternionfc var1) {
      return this.rotate(var1, this);
   }

   public Matrix3d rotate(Quaternionfc var1, Matrix3d var2) {
      double var3 = (double)(var1.w() * var1.w());
      double var5 = (double)(var1.x() * var1.x());
      double var7 = (double)(var1.y() * var1.y());
      double var9 = (double)(var1.z() * var1.z());
      double var11 = (double)(var1.z() * var1.w());
      double var13 = var11 + var11;
      double var15 = (double)(var1.x() * var1.y());
      double var17 = var15 + var15;
      double var19 = (double)(var1.x() * var1.z());
      double var21 = var19 + var19;
      double var23 = (double)(var1.y() * var1.w());
      double var25 = var23 + var23;
      double var27 = (double)(var1.y() * var1.z());
      double var29 = var27 + var27;
      double var31 = (double)(var1.x() * var1.w());
      double var33 = var31 + var31;
      double var35 = var3 + var5 - var9 - var7;
      double var37 = var17 + var13;
      double var39 = var21 - var25;
      double var41 = var17 - var13;
      double var43 = var7 - var9 + var3 - var5;
      double var45 = var29 + var33;
      double var47 = var25 + var21;
      double var49 = var29 - var33;
      double var51 = var9 - var7 - var5 + var3;
      double var53 = this.m00 * var35 + this.m10 * var37 + this.m20 * var39;
      double var55 = this.m01 * var35 + this.m11 * var37 + this.m21 * var39;
      double var57 = this.m02 * var35 + this.m12 * var37 + this.m22 * var39;
      double var59 = this.m00 * var41 + this.m10 * var43 + this.m20 * var45;
      double var61 = this.m01 * var41 + this.m11 * var43 + this.m21 * var45;
      double var63 = this.m02 * var41 + this.m12 * var43 + this.m22 * var45;
      var2.m20 = this.m00 * var47 + this.m10 * var49 + this.m20 * var51;
      var2.m21 = this.m01 * var47 + this.m11 * var49 + this.m21 * var51;
      var2.m22 = this.m02 * var47 + this.m12 * var49 + this.m22 * var51;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      return var2;
   }

   public Matrix3d rotate(AxisAngle4f var1) {
      return this.rotate((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Matrix3d rotate(AxisAngle4f var1, Matrix3d var2) {
      return this.rotate((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z, var2);
   }

   public Matrix3d rotate(AxisAngle4d var1) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix3d rotate(AxisAngle4d var1, Matrix3d var2) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z, var2);
   }

   public Matrix3d rotate(double var1, Vector3dc var3) {
      return this.rotate(var1, var3.x(), var3.y(), var3.z());
   }

   public Matrix3d rotate(double var1, Vector3dc var3, Matrix3d var4) {
      return this.rotate(var1, var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix3d rotate(double var1, Vector3fc var3) {
      return this.rotate(var1, (double)var3.x(), (double)var3.y(), (double)var3.z());
   }

   public Matrix3d rotate(double var1, Vector3fc var3, Matrix3d var4) {
      return this.rotate(var1, (double)var3.x(), (double)var3.y(), (double)var3.z(), var4);
   }

   public Vector3d getRow(int var1, Vector3d var2) throws IndexOutOfBoundsException {
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

   public Matrix3d setRow(int var1, Vector3dc var2) throws IndexOutOfBoundsException {
      return this.setRow(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3d setRow(int var1, double var2, double var4, double var6) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var4;
         this.m20 = var6;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var4;
         this.m21 = var6;
         break;
      case 2:
         this.m02 = var2;
         this.m12 = var4;
         this.m22 = var6;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public Vector3d getColumn(int var1, Vector3d var2) throws IndexOutOfBoundsException {
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

   public Matrix3d setColumn(int var1, Vector3dc var2) throws IndexOutOfBoundsException {
      return this.setColumn(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix3d setColumn(int var1, double var2, double var4, double var6) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var4;
         this.m02 = var6;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var4;
         this.m12 = var6;
         break;
      case 2:
         this.m20 = var2;
         this.m21 = var4;
         this.m22 = var6;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public double get(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Matrix3d set(int var1, int var2, double var3) {
      return MemUtil.INSTANCE.set(this, var1, var2, var3);
   }

   public double getRowColumn(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var2, var1);
   }

   public Matrix3d setRowColumn(int var1, int var2, double var3) {
      return MemUtil.INSTANCE.set(this, var2, var1, var3);
   }

   public Matrix3d normal() {
      return this.normal(this);
   }

   public Matrix3d normal(Matrix3d var1) {
      double var2 = this.m00 * this.m11;
      double var4 = this.m01 * this.m10;
      double var6 = this.m02 * this.m10;
      double var8 = this.m00 * this.m12;
      double var10 = this.m01 * this.m12;
      double var12 = this.m02 * this.m11;
      double var14 = (var2 - var4) * this.m22 + (var6 - var8) * this.m21 + (var10 - var12) * this.m20;
      double var16 = 1.0D / var14;
      double var18 = (this.m11 * this.m22 - this.m21 * this.m12) * var16;
      double var20 = (this.m20 * this.m12 - this.m10 * this.m22) * var16;
      double var22 = (this.m10 * this.m21 - this.m20 * this.m11) * var16;
      double var24 = (this.m21 * this.m02 - this.m01 * this.m22) * var16;
      double var26 = (this.m00 * this.m22 - this.m20 * this.m02) * var16;
      double var28 = (this.m20 * this.m01 - this.m00 * this.m21) * var16;
      double var30 = (var10 - var12) * var16;
      double var32 = (var6 - var8) * var16;
      double var34 = (var2 - var4) * var16;
      var1.m00 = var18;
      var1.m01 = var20;
      var1.m02 = var22;
      var1.m10 = var24;
      var1.m11 = var26;
      var1.m12 = var28;
      var1.m20 = var30;
      var1.m21 = var32;
      var1.m22 = var34;
      return var1;
   }

   public Matrix3d cofactor() {
      return this.cofactor(this);
   }

   public Matrix3d cofactor(Matrix3d var1) {
      double var2 = this.m11 * this.m22 - this.m21 * this.m12;
      double var4 = this.m20 * this.m12 - this.m10 * this.m22;
      double var6 = this.m10 * this.m21 - this.m20 * this.m11;
      double var8 = this.m21 * this.m02 - this.m01 * this.m22;
      double var10 = this.m00 * this.m22 - this.m20 * this.m02;
      double var12 = this.m20 * this.m01 - this.m00 * this.m21;
      double var14 = this.m01 * this.m12 - this.m11 * this.m02;
      double var16 = this.m02 * this.m10 - this.m12 * this.m00;
      double var18 = this.m00 * this.m11 - this.m10 * this.m01;
      var1.m00 = var2;
      var1.m01 = var4;
      var1.m02 = var6;
      var1.m10 = var8;
      var1.m11 = var10;
      var1.m12 = var12;
      var1.m20 = var14;
      var1.m21 = var16;
      var1.m22 = var18;
      return var1;
   }

   public Matrix3d lookAlong(Vector3dc var1, Vector3dc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix3d lookAlong(Vector3dc var1, Vector3dc var2, Matrix3d var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Matrix3d var13) {
      double var14 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      var1 *= -var14;
      var3 *= -var14;
      var5 *= -var14;
      double var16 = var9 * var5 - var11 * var3;
      double var18 = var11 * var1 - var7 * var5;
      double var20 = var7 * var3 - var9 * var1;
      double var22 = Math.invsqrt(var16 * var16 + var18 * var18 + var20 * var20);
      var16 *= var22;
      var18 *= var22;
      var20 *= var22;
      double var24 = var3 * var20 - var5 * var18;
      double var26 = var5 * var16 - var1 * var20;
      double var28 = var1 * var18 - var3 * var16;
      double var48 = this.m00 * var16 + this.m10 * var24 + this.m20 * var1;
      double var50 = this.m01 * var16 + this.m11 * var24 + this.m21 * var1;
      double var52 = this.m02 * var16 + this.m12 * var24 + this.m22 * var1;
      double var54 = this.m00 * var18 + this.m10 * var26 + this.m20 * var3;
      double var56 = this.m01 * var18 + this.m11 * var26 + this.m21 * var3;
      double var58 = this.m02 * var18 + this.m12 * var26 + this.m22 * var3;
      var13.m20 = this.m00 * var20 + this.m10 * var28 + this.m20 * var5;
      var13.m21 = this.m01 * var20 + this.m11 * var28 + this.m21 * var5;
      var13.m22 = this.m02 * var20 + this.m12 * var28 + this.m22 * var5;
      var13.m00 = var48;
      var13.m01 = var50;
      var13.m02 = var52;
      var13.m10 = var54;
      var13.m11 = var56;
      var13.m12 = var58;
      return var13;
   }

   public Matrix3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.lookAlong(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix3d setLookAlong(Vector3dc var1, Vector3dc var2) {
      return this.setLookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix3d setLookAlong(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      var1 *= -var13;
      var3 *= -var13;
      var5 *= -var13;
      double var15 = var9 * var5 - var11 * var3;
      double var17 = var11 * var1 - var7 * var5;
      double var19 = var7 * var3 - var9 * var1;
      double var21 = Math.invsqrt(var15 * var15 + var17 * var17 + var19 * var19);
      var15 *= var21;
      var17 *= var21;
      var19 *= var21;
      double var23 = var3 * var19 - var5 * var17;
      double var25 = var5 * var15 - var1 * var19;
      double var27 = var1 * var17 - var3 * var15;
      this.m00 = var15;
      this.m01 = var23;
      this.m02 = var1;
      this.m10 = var17;
      this.m11 = var25;
      this.m12 = var3;
      this.m20 = var19;
      this.m21 = var27;
      this.m22 = var5;
      return this;
   }

   public Vector3d getScale(Vector3d var1) {
      var1.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      var1.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      var1.z = Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      return var1;
   }

   public Vector3d positiveZ(Vector3d var1) {
      var1.x = this.m10 * this.m21 - this.m11 * this.m20;
      var1.y = this.m20 * this.m01 - this.m21 * this.m00;
      var1.z = this.m00 * this.m11 - this.m01 * this.m10;
      return var1.normalize(var1);
   }

   public Vector3d normalizedPositiveZ(Vector3d var1) {
      var1.x = this.m02;
      var1.y = this.m12;
      var1.z = this.m22;
      return var1;
   }

   public Vector3d positiveX(Vector3d var1) {
      var1.x = this.m11 * this.m22 - this.m12 * this.m21;
      var1.y = this.m02 * this.m21 - this.m01 * this.m22;
      var1.z = this.m01 * this.m12 - this.m02 * this.m11;
      return var1.normalize(var1);
   }

   public Vector3d normalizedPositiveX(Vector3d var1) {
      var1.x = this.m00;
      var1.y = this.m10;
      var1.z = this.m20;
      return var1;
   }

   public Vector3d positiveY(Vector3d var1) {
      var1.x = this.m12 * this.m20 - this.m10 * this.m22;
      var1.y = this.m00 * this.m22 - this.m02 * this.m20;
      var1.z = this.m02 * this.m10 - this.m00 * this.m12;
      return var1.normalize(var1);
   }

   public Vector3d normalizedPositiveY(Vector3d var1) {
      var1.x = this.m01;
      var1.y = this.m11;
      var1.z = this.m21;
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.m00);
      int var4 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m01);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m02);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m10);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m11);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m12);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m20);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m21);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m22);
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
         Matrix3d var2 = (Matrix3d)var1;
         if (Double.doubleToLongBits(this.m00) != Double.doubleToLongBits(var2.m00)) {
            return false;
         } else if (Double.doubleToLongBits(this.m01) != Double.doubleToLongBits(var2.m01)) {
            return false;
         } else if (Double.doubleToLongBits(this.m02) != Double.doubleToLongBits(var2.m02)) {
            return false;
         } else if (Double.doubleToLongBits(this.m10) != Double.doubleToLongBits(var2.m10)) {
            return false;
         } else if (Double.doubleToLongBits(this.m11) != Double.doubleToLongBits(var2.m11)) {
            return false;
         } else if (Double.doubleToLongBits(this.m12) != Double.doubleToLongBits(var2.m12)) {
            return false;
         } else if (Double.doubleToLongBits(this.m20) != Double.doubleToLongBits(var2.m20)) {
            return false;
         } else if (Double.doubleToLongBits(this.m21) != Double.doubleToLongBits(var2.m21)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.m22) == Double.doubleToLongBits(var2.m22);
         }
      }
   }

   public boolean equals(Matrix3dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix3d)) {
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

   public Matrix3d swap(Matrix3d var1) {
      double var2 = this.m00;
      this.m00 = var1.m00;
      var1.m00 = var2;
      var2 = this.m01;
      this.m01 = var1.m01;
      var1.m01 = var2;
      var2 = this.m02;
      this.m02 = var1.m02;
      var1.m02 = var2;
      var2 = this.m10;
      this.m10 = var1.m10;
      var1.m10 = var2;
      var2 = this.m11;
      this.m11 = var1.m11;
      var1.m11 = var2;
      var2 = this.m12;
      this.m12 = var1.m12;
      var1.m12 = var2;
      var2 = this.m20;
      this.m20 = var1.m20;
      var1.m20 = var2;
      var2 = this.m21;
      this.m21 = var1.m21;
      var1.m21 = var2;
      var2 = this.m22;
      this.m22 = var1.m22;
      var1.m22 = var2;
      return this;
   }

   public Matrix3d add(Matrix3dc var1) {
      return this.add(var1, this);
   }

   public Matrix3d add(Matrix3dc var1, Matrix3d var2) {
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

   public Matrix3d sub(Matrix3dc var1) {
      return this.sub(var1, this);
   }

   public Matrix3d sub(Matrix3dc var1, Matrix3d var2) {
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

   public Matrix3d mulComponentWise(Matrix3dc var1) {
      return this.mulComponentWise(var1, this);
   }

   public Matrix3d mulComponentWise(Matrix3dc var1, Matrix3d var2) {
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

   public Matrix3d setSkewSymmetric(double var1, double var3, double var5) {
      this.m00 = this.m11 = this.m22 = 0.0D;
      this.m01 = -var1;
      this.m02 = var3;
      this.m10 = var1;
      this.m12 = -var5;
      this.m20 = -var3;
      this.m21 = var5;
      return this;
   }

   public Matrix3d lerp(Matrix3dc var1, double var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix3d lerp(Matrix3dc var1, double var2, Matrix3d var4) {
      var4.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var4.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var4.m02 = Math.fma(var1.m02() - this.m02, var2, this.m02);
      var4.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var4.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      var4.m12 = Math.fma(var1.m12() - this.m12, var2, this.m12);
      var4.m20 = Math.fma(var1.m20() - this.m20, var2, this.m20);
      var4.m21 = Math.fma(var1.m21() - this.m21, var2, this.m21);
      var4.m22 = Math.fma(var1.m22() - this.m22, var2, this.m22);
      return var4;
   }

   public Matrix3d rotateTowards(Vector3dc var1, Vector3dc var2, Matrix3d var3) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix3d rotateTowards(Vector3dc var1, Vector3dc var2) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.rotateTowards(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, Matrix3d var13) {
      double var14 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var16 = var1 * var14;
      double var18 = var3 * var14;
      double var20 = var5 * var14;
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
      double var54 = this.m00 * var22 + this.m10 * var24 + this.m20 * var26;
      double var56 = this.m01 * var22 + this.m11 * var24 + this.m21 * var26;
      double var58 = this.m02 * var22 + this.m12 * var24 + this.m22 * var26;
      double var60 = this.m00 * var30 + this.m10 * var32 + this.m20 * var34;
      double var62 = this.m01 * var30 + this.m11 * var32 + this.m21 * var34;
      double var64 = this.m02 * var30 + this.m12 * var32 + this.m22 * var34;
      var13.m20 = this.m00 * var16 + this.m10 * var18 + this.m20 * var20;
      var13.m21 = this.m01 * var16 + this.m11 * var18 + this.m21 * var20;
      var13.m22 = this.m02 * var16 + this.m12 * var18 + this.m22 * var20;
      var13.m00 = var54;
      var13.m01 = var56;
      var13.m02 = var58;
      var13.m10 = var60;
      var13.m11 = var62;
      var13.m12 = var64;
      return var13;
   }

   public Matrix3d rotationTowards(Vector3dc var1, Vector3dc var2) {
      return this.rotationTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix3d rotationTowards(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var15 = var1 * var13;
      double var17 = var3 * var13;
      double var19 = var5 * var13;
      double var21 = var9 * var19 - var11 * var17;
      double var23 = var11 * var15 - var7 * var19;
      double var25 = var7 * var17 - var9 * var15;
      double var27 = Math.invsqrt(var21 * var21 + var23 * var23 + var25 * var25);
      var21 *= var27;
      var23 *= var27;
      var25 *= var27;
      double var29 = var17 * var25 - var19 * var23;
      double var31 = var19 * var21 - var15 * var25;
      double var33 = var15 * var23 - var17 * var21;
      this.m00 = var21;
      this.m01 = var23;
      this.m02 = var25;
      this.m10 = var29;
      this.m11 = var31;
      this.m12 = var33;
      this.m20 = var15;
      this.m21 = var17;
      this.m22 = var19;
      return this;
   }

   public Vector3d getEulerAnglesZYX(Vector3d var1) {
      var1.x = (double)((float)Math.atan2(this.m12, this.m22));
      var1.y = (double)((float)Math.atan2(-this.m02, Math.sqrt(this.m12 * this.m12 + this.m22 * this.m22)));
      var1.z = (double)((float)Math.atan2(this.m01, this.m00));
      return var1;
   }

   public Matrix3d obliqueZ(double var1, double var3) {
      this.m20 += this.m00 * var1 + this.m10 * var3;
      this.m21 += this.m01 * var1 + this.m11 * var3;
      this.m22 += this.m02 * var1 + this.m12 * var3;
      return this;
   }

   public Matrix3d obliqueZ(double var1, double var3, Matrix3d var5) {
      var5.m00 = this.m00;
      var5.m01 = this.m01;
      var5.m02 = this.m02;
      var5.m10 = this.m10;
      var5.m11 = this.m11;
      var5.m12 = this.m12;
      var5.m20 = this.m00 * var1 + this.m10 * var3 + this.m20;
      var5.m21 = this.m01 * var1 + this.m11 * var3 + this.m21;
      var5.m22 = this.m02 * var1 + this.m12 * var3 + this.m22;
      return var5;
   }

   public Matrix3d reflect(double var1, double var3, double var5, Matrix3d var7) {
      double var8 = var1 + var1;
      double var10 = var3 + var3;
      double var12 = var5 + var5;
      double var14 = 1.0D - var8 * var1;
      double var16 = -var8 * var3;
      double var18 = -var8 * var5;
      double var20 = -var10 * var1;
      double var22 = 1.0D - var10 * var3;
      double var24 = -var10 * var5;
      double var26 = -var12 * var1;
      double var28 = -var12 * var3;
      double var30 = 1.0D - var12 * var5;
      double var32 = this.m00 * var14 + this.m10 * var16 + this.m20 * var18;
      double var34 = this.m01 * var14 + this.m11 * var16 + this.m21 * var18;
      double var36 = this.m02 * var14 + this.m12 * var16 + this.m22 * var18;
      double var38 = this.m00 * var20 + this.m10 * var22 + this.m20 * var24;
      double var40 = this.m01 * var20 + this.m11 * var22 + this.m21 * var24;
      double var42 = this.m02 * var20 + this.m12 * var22 + this.m22 * var24;
      return var7._m20(this.m00 * var26 + this.m10 * var28 + this.m20 * var30)._m21(this.m01 * var26 + this.m11 * var28 + this.m21 * var30)._m22(this.m02 * var26 + this.m12 * var28 + this.m22 * var30)._m00(var32)._m01(var34)._m02(var36)._m10(var38)._m11(var40)._m12(var42);
   }

   public Matrix3d reflect(double var1, double var3, double var5) {
      return this.reflect(var1, var3, var5, this);
   }

   public Matrix3d reflect(Vector3dc var1) {
      return this.reflect(var1.x(), var1.y(), var1.z());
   }

   public Matrix3d reflect(Quaterniondc var1) {
      return this.reflect(var1, this);
   }

   public Matrix3d reflect(Quaterniondc var1, Matrix3d var2) {
      double var3 = var1.x() + var1.x();
      double var5 = var1.y() + var1.y();
      double var7 = var1.z() + var1.z();
      double var9 = var1.x() * var7 + var1.w() * var5;
      double var11 = var1.y() * var7 - var1.w() * var3;
      double var13 = 1.0D - (var1.x() * var3 + var1.y() * var5);
      return this.reflect(var9, var11, var13, var2);
   }

   public Matrix3d reflect(Vector3dc var1, Matrix3d var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix3d reflection(double var1, double var3, double var5) {
      double var7 = var1 + var1;
      double var9 = var3 + var3;
      double var11 = var5 + var5;
      this._m00(1.0D - var7 * var1);
      this._m01(-var7 * var3);
      this._m02(-var7 * var5);
      this._m10(-var9 * var1);
      this._m11(1.0D - var9 * var3);
      this._m12(-var9 * var5);
      this._m20(-var11 * var1);
      this._m21(-var11 * var3);
      this._m22(1.0D - var11 * var5);
      return this;
   }

   public Matrix3d reflection(Vector3dc var1) {
      return this.reflection(var1.x(), var1.y(), var1.z());
   }

   public Matrix3d reflection(Quaterniondc var1) {
      double var2 = var1.x() + var1.x();
      double var4 = var1.y() + var1.y();
      double var6 = var1.z() + var1.z();
      double var8 = var1.x() * var6 + var1.w() * var4;
      double var10 = var1.y() * var6 - var1.w() * var2;
      double var12 = 1.0D - (var1.x() * var2 + var1.y() * var4);
      return this.reflection(var8, var10, var12);
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22);
   }

   public double quadraticFormProduct(double var1, double var3, double var5) {
      double var7 = this.m00 * var1 + this.m10 * var3 + this.m20 * var5;
      double var9 = this.m01 * var1 + this.m11 * var3 + this.m21 * var5;
      double var11 = this.m02 * var1 + this.m12 * var3 + this.m22 * var5;
      return var1 * var7 + var3 * var9 + var5 * var11;
   }

   public double quadraticFormProduct(Vector3dc var1) {
      return this.quadraticFormProduct(var1.x(), var1.y(), var1.z());
   }

   public double quadraticFormProduct(Vector3fc var1) {
      return this.quadraticFormProduct((double)var1.x(), (double)var1.y(), (double)var1.z());
   }
}
