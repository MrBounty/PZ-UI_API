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

public class Matrix4x3d implements Externalizable, Matrix4x3dc {
   private static final long serialVersionUID = 1L;
   double m00;
   double m01;
   double m02;
   double m10;
   double m11;
   double m12;
   double m20;
   double m21;
   double m22;
   double m30;
   double m31;
   double m32;
   int properties;

   public Matrix4x3d() {
      this.m00 = 1.0D;
      this.m11 = 1.0D;
      this.m22 = 1.0D;
      this.properties = 28;
   }

   public Matrix4x3d(Matrix4x3dc var1) {
      this.set(var1);
   }

   public Matrix4x3d(Matrix4x3fc var1) {
      this.set(var1);
   }

   public Matrix4x3d(Matrix3dc var1) {
      this.set(var1);
   }

   public Matrix4x3d(Matrix3fc var1) {
      this.set(var1);
   }

   public Matrix4x3d(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.m00 = var1;
      this.m01 = var3;
      this.m02 = var5;
      this.m10 = var7;
      this.m11 = var9;
      this.m12 = var11;
      this.m20 = var13;
      this.m21 = var15;
      this.m22 = var17;
      this.m30 = var19;
      this.m31 = var21;
      this.m32 = var23;
      this.determineProperties();
   }

   public Matrix4x3d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      this.determineProperties();
   }

   public Matrix4x3d assume(int var1) {
      this.properties = var1;
      return this;
   }

   public Matrix4x3d determineProperties() {
      int var1 = 0;
      if (this.m00 == 1.0D && this.m01 == 0.0D && this.m02 == 0.0D && this.m10 == 0.0D && this.m11 == 1.0D && this.m12 == 0.0D && this.m20 == 0.0D && this.m21 == 0.0D && this.m22 == 1.0D) {
         var1 |= 24;
         if (this.m30 == 0.0D && this.m31 == 0.0D && this.m32 == 0.0D) {
            var1 |= 4;
         }
      }

      this.properties = var1;
      return this;
   }

   public int properties() {
      return this.properties;
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

   public double m30() {
      return this.m30;
   }

   public double m31() {
      return this.m31;
   }

   public double m32() {
      return this.m32;
   }

   Matrix4x3d _properties(int var1) {
      this.properties = var1;
      return this;
   }

   Matrix4x3d _m00(double var1) {
      this.m00 = var1;
      return this;
   }

   Matrix4x3d _m01(double var1) {
      this.m01 = var1;
      return this;
   }

   Matrix4x3d _m02(double var1) {
      this.m02 = var1;
      return this;
   }

   Matrix4x3d _m10(double var1) {
      this.m10 = var1;
      return this;
   }

   Matrix4x3d _m11(double var1) {
      this.m11 = var1;
      return this;
   }

   Matrix4x3d _m12(double var1) {
      this.m12 = var1;
      return this;
   }

   Matrix4x3d _m20(double var1) {
      this.m20 = var1;
      return this;
   }

   Matrix4x3d _m21(double var1) {
      this.m21 = var1;
      return this;
   }

   Matrix4x3d _m22(double var1) {
      this.m22 = var1;
      return this;
   }

   Matrix4x3d _m30(double var1) {
      this.m30 = var1;
      return this;
   }

   Matrix4x3d _m31(double var1) {
      this.m31 = var1;
      return this;
   }

   Matrix4x3d _m32(double var1) {
      this.m32 = var1;
      return this;
   }

   public Matrix4x3d m00(double var1) {
      this.m00 = var1;
      this.properties &= -17;
      if (var1 != 1.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m01(double var1) {
      this.m01 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m02(double var1) {
      this.m02 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m10(double var1) {
      this.m10 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m11(double var1) {
      this.m11 = var1;
      this.properties &= -17;
      if (var1 != 1.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m12(double var1) {
      this.m12 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m20(double var1) {
      this.m20 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m21(double var1) {
      this.m21 = var1;
      this.properties &= -17;
      if (var1 != 0.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m22(double var1) {
      this.m22 = var1;
      this.properties &= -17;
      if (var1 != 1.0D) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3d m30(double var1) {
      this.m30 = var1;
      if (var1 != 0.0D) {
         this.properties &= -5;
      }

      return this;
   }

   public Matrix4x3d m31(double var1) {
      this.m31 = var1;
      if (var1 != 0.0D) {
         this.properties &= -5;
      }

      return this;
   }

   public Matrix4x3d m32(double var1) {
      this.m32 = var1;
      if (var1 != 0.0D) {
         this.properties &= -5;
      }

      return this;
   }

   public Matrix4x3d identity() {
      if ((this.properties & 4) != 0) {
         return this;
      } else {
         this.m00 = 1.0D;
         this.m01 = 0.0D;
         this.m02 = 0.0D;
         this.m10 = 0.0D;
         this.m11 = 1.0D;
         this.m12 = 0.0D;
         this.m20 = 0.0D;
         this.m21 = 0.0D;
         this.m22 = 1.0D;
         this.m30 = 0.0D;
         this.m31 = 0.0D;
         this.m32 = 0.0D;
         this.properties = 28;
         return this;
      }
   }

   public Matrix4x3d set(Matrix4x3dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.m30 = var1.m30();
      this.m31 = var1.m31();
      this.m32 = var1.m32();
      this.properties = var1.properties();
      return this;
   }

   public Matrix4x3d set(Matrix4x3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = (double)var1.m02();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = (double)var1.m12();
      this.m20 = (double)var1.m20();
      this.m21 = (double)var1.m21();
      this.m22 = (double)var1.m22();
      this.m30 = (double)var1.m30();
      this.m31 = (double)var1.m31();
      this.m32 = (double)var1.m32();
      this.properties = var1.properties();
      return this;
   }

   public Matrix4x3d set(Matrix4dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.m30 = var1.m30();
      this.m31 = var1.m31();
      this.m32 = var1.m32();
      this.properties = var1.properties() & 28;
      return this;
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set4x3((Matrix4x3dc)this);
   }

   public Matrix4x3d set(Matrix3dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      return this.determineProperties();
   }

   public Matrix4x3d set(Matrix3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = (double)var1.m02();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = (double)var1.m12();
      this.m20 = (double)var1.m20();
      this.m21 = (double)var1.m21();
      this.m22 = (double)var1.m22();
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      return this.determineProperties();
   }

   public Matrix4x3d set(Vector3dc var1, Vector3dc var2, Vector3dc var3, Vector3dc var4) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m02 = var1.z();
      this.m10 = var2.x();
      this.m11 = var2.y();
      this.m12 = var2.z();
      this.m20 = var3.x();
      this.m21 = var3.y();
      this.m22 = var3.z();
      this.m30 = var4.x();
      this.m31 = var4.y();
      this.m32 = var4.z();
      return this.determineProperties();
   }

   public Matrix4x3d set3x3(Matrix4x3dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.properties &= var1.properties();
      return this;
   }

   public Matrix4x3d set(AxisAngle4f var1) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d set(AxisAngle4d var1) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d set(Quaternionfc var1) {
      return this.rotation(var1);
   }

   public Matrix4x3d set(Quaterniondc var1) {
      return this.rotation(var1);
   }

   public Matrix4x3d mul(Matrix4x3dc var1) {
      return this.mul(var1, this);
   }

   public Matrix4x3d mul(Matrix4x3dc var1, Matrix4x3d var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4x3dc)this);
      } else {
         return (this.properties & 8) != 0 ? this.mulTranslation(var1, var2) : this.mulGeneric(var1, var2);
      }
   }

   private Matrix4x3d mulGeneric(Matrix4x3dc var1, Matrix4x3d var2) {
      double var3 = this.m00;
      double var5 = this.m01;
      double var7 = this.m02;
      double var9 = this.m10;
      double var11 = this.m11;
      double var13 = this.m12;
      double var15 = this.m20;
      double var17 = this.m21;
      double var19 = this.m22;
      double var21 = var1.m00();
      double var23 = var1.m01();
      double var25 = var1.m02();
      double var27 = var1.m10();
      double var29 = var1.m11();
      double var31 = var1.m12();
      double var33 = var1.m20();
      double var35 = var1.m21();
      double var37 = var1.m22();
      double var39 = var1.m30();
      double var41 = var1.m31();
      double var43 = var1.m32();
      return var2._m00(Math.fma(var3, var21, Math.fma(var9, var23, var15 * var25)))._m01(Math.fma(var5, var21, Math.fma(var11, var23, var17 * var25)))._m02(Math.fma(var7, var21, Math.fma(var13, var23, var19 * var25)))._m10(Math.fma(var3, var27, Math.fma(var9, var29, var15 * var31)))._m11(Math.fma(var5, var27, Math.fma(var11, var29, var17 * var31)))._m12(Math.fma(var7, var27, Math.fma(var13, var29, var19 * var31)))._m20(Math.fma(var3, var33, Math.fma(var9, var35, var15 * var37)))._m21(Math.fma(var5, var33, Math.fma(var11, var35, var17 * var37)))._m22(Math.fma(var7, var33, Math.fma(var13, var35, var19 * var37)))._m30(Math.fma(var3, var39, Math.fma(var9, var41, Math.fma(var15, var43, this.m30))))._m31(Math.fma(var5, var39, Math.fma(var11, var41, Math.fma(var17, var43, this.m31))))._m32(Math.fma(var7, var39, Math.fma(var13, var41, Math.fma(var19, var43, this.m32))))._properties(this.properties & var1.properties() & 16);
   }

   public Matrix4x3d mul(Matrix4x3fc var1) {
      return this.mul(var1, this);
   }

   public Matrix4x3d mul(Matrix4x3fc var1, Matrix4x3d var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4x3dc)this);
      } else {
         return (this.properties & 8) != 0 ? this.mulTranslation(var1, var2) : this.mulGeneric(var1, var2);
      }
   }

   private Matrix4x3d mulGeneric(Matrix4x3fc var1, Matrix4x3d var2) {
      double var3 = this.m00;
      double var5 = this.m01;
      double var7 = this.m02;
      double var9 = this.m10;
      double var11 = this.m11;
      double var13 = this.m12;
      double var15 = this.m20;
      double var17 = this.m21;
      double var19 = this.m22;
      double var21 = (double)var1.m00();
      double var23 = (double)var1.m01();
      double var25 = (double)var1.m02();
      double var27 = (double)var1.m10();
      double var29 = (double)var1.m11();
      double var31 = (double)var1.m12();
      double var33 = (double)var1.m20();
      double var35 = (double)var1.m21();
      double var37 = (double)var1.m22();
      double var39 = (double)var1.m30();
      double var41 = (double)var1.m31();
      double var43 = (double)var1.m32();
      return var2._m00(Math.fma(var3, var21, Math.fma(var9, var23, var15 * var25)))._m01(Math.fma(var5, var21, Math.fma(var11, var23, var17 * var25)))._m02(Math.fma(var7, var21, Math.fma(var13, var23, var19 * var25)))._m10(Math.fma(var3, var27, Math.fma(var9, var29, var15 * var31)))._m11(Math.fma(var5, var27, Math.fma(var11, var29, var17 * var31)))._m12(Math.fma(var7, var27, Math.fma(var13, var29, var19 * var31)))._m20(Math.fma(var3, var33, Math.fma(var9, var35, var15 * var37)))._m21(Math.fma(var5, var33, Math.fma(var11, var35, var17 * var37)))._m22(Math.fma(var7, var33, Math.fma(var13, var35, var19 * var37)))._m30(Math.fma(var3, var39, Math.fma(var9, var41, Math.fma(var15, var43, this.m30))))._m31(Math.fma(var5, var39, Math.fma(var11, var41, Math.fma(var17, var43, this.m31))))._m32(Math.fma(var7, var39, Math.fma(var13, var41, Math.fma(var19, var43, this.m32))))._properties(this.properties & var1.properties() & 16);
   }

   public Matrix4x3d mulTranslation(Matrix4x3dc var1, Matrix4x3d var2) {
      return var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30() + this.m30)._m31(var1.m31() + this.m31)._m32(var1.m32() + this.m32)._properties(var1.properties() & 16);
   }

   public Matrix4x3d mulTranslation(Matrix4x3fc var1, Matrix4x3d var2) {
      return var2._m00((double)var1.m00())._m01((double)var1.m01())._m02((double)var1.m02())._m10((double)var1.m10())._m11((double)var1.m11())._m12((double)var1.m12())._m20((double)var1.m20())._m21((double)var1.m21())._m22((double)var1.m22())._m30((double)var1.m30() + this.m30)._m31((double)var1.m31() + this.m31)._m32((double)var1.m32() + this.m32)._properties(var1.properties() & 16);
   }

   public Matrix4x3d mulOrtho(Matrix4x3dc var1) {
      return this.mulOrtho(var1, this);
   }

   public Matrix4x3d mulOrtho(Matrix4x3dc var1, Matrix4x3d var2) {
      double var3 = this.m00 * var1.m00();
      double var5 = this.m11 * var1.m01();
      double var7 = this.m22 * var1.m02();
      double var9 = this.m00 * var1.m10();
      double var11 = this.m11 * var1.m11();
      double var13 = this.m22 * var1.m12();
      double var15 = this.m00 * var1.m20();
      double var17 = this.m11 * var1.m21();
      double var19 = this.m22 * var1.m22();
      double var21 = this.m00 * var1.m30() + this.m30;
      double var23 = this.m11 * var1.m31() + this.m31;
      double var25 = this.m22 * var1.m32() + this.m32;
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m02 = var7;
      var2.m10 = var9;
      var2.m11 = var11;
      var2.m12 = var13;
      var2.m20 = var15;
      var2.m21 = var17;
      var2.m22 = var19;
      var2.m30 = var21;
      var2.m31 = var23;
      var2.m32 = var25;
      var2.properties = this.properties & var1.properties() & 16;
      return var2;
   }

   public Matrix4x3d fma(Matrix4x3dc var1, double var2) {
      return this.fma(var1, var2, this);
   }

   public Matrix4x3d fma(Matrix4x3dc var1, double var2, Matrix4x3d var4) {
      var4._m00(Math.fma(var1.m00(), var2, this.m00))._m01(Math.fma(var1.m01(), var2, this.m01))._m02(Math.fma(var1.m02(), var2, this.m02))._m10(Math.fma(var1.m10(), var2, this.m10))._m11(Math.fma(var1.m11(), var2, this.m11))._m12(Math.fma(var1.m12(), var2, this.m12))._m20(Math.fma(var1.m20(), var2, this.m20))._m21(Math.fma(var1.m21(), var2, this.m21))._m22(Math.fma(var1.m22(), var2, this.m22))._m30(Math.fma(var1.m30(), var2, this.m30))._m31(Math.fma(var1.m31(), var2, this.m31))._m32(Math.fma(var1.m32(), var2, this.m32))._properties(0);
      return var4;
   }

   public Matrix4x3d fma(Matrix4x3fc var1, double var2) {
      return this.fma(var1, var2, this);
   }

   public Matrix4x3d fma(Matrix4x3fc var1, double var2, Matrix4x3d var4) {
      var4._m00(Math.fma((double)var1.m00(), var2, this.m00))._m01(Math.fma((double)var1.m01(), var2, this.m01))._m02(Math.fma((double)var1.m02(), var2, this.m02))._m10(Math.fma((double)var1.m10(), var2, this.m10))._m11(Math.fma((double)var1.m11(), var2, this.m11))._m12(Math.fma((double)var1.m12(), var2, this.m12))._m20(Math.fma((double)var1.m20(), var2, this.m20))._m21(Math.fma((double)var1.m21(), var2, this.m21))._m22(Math.fma((double)var1.m22(), var2, this.m22))._m30(Math.fma((double)var1.m30(), var2, this.m30))._m31(Math.fma((double)var1.m31(), var2, this.m31))._m32(Math.fma((double)var1.m32(), var2, this.m32))._properties(0);
      return var4;
   }

   public Matrix4x3d add(Matrix4x3dc var1) {
      return this.add(var1, this);
   }

   public Matrix4x3d add(Matrix4x3dc var1, Matrix4x3d var2) {
      var2.m00 = this.m00 + var1.m00();
      var2.m01 = this.m01 + var1.m01();
      var2.m02 = this.m02 + var1.m02();
      var2.m10 = this.m10 + var1.m10();
      var2.m11 = this.m11 + var1.m11();
      var2.m12 = this.m12 + var1.m12();
      var2.m20 = this.m20 + var1.m20();
      var2.m21 = this.m21 + var1.m21();
      var2.m22 = this.m22 + var1.m22();
      var2.m30 = this.m30 + var1.m30();
      var2.m31 = this.m31 + var1.m31();
      var2.m32 = this.m32 + var1.m32();
      var2.properties = 0;
      return var2;
   }

   public Matrix4x3d add(Matrix4x3fc var1) {
      return this.add(var1, this);
   }

   public Matrix4x3d add(Matrix4x3fc var1, Matrix4x3d var2) {
      var2.m00 = this.m00 + (double)var1.m00();
      var2.m01 = this.m01 + (double)var1.m01();
      var2.m02 = this.m02 + (double)var1.m02();
      var2.m10 = this.m10 + (double)var1.m10();
      var2.m11 = this.m11 + (double)var1.m11();
      var2.m12 = this.m12 + (double)var1.m12();
      var2.m20 = this.m20 + (double)var1.m20();
      var2.m21 = this.m21 + (double)var1.m21();
      var2.m22 = this.m22 + (double)var1.m22();
      var2.m30 = this.m30 + (double)var1.m30();
      var2.m31 = this.m31 + (double)var1.m31();
      var2.m32 = this.m32 + (double)var1.m32();
      var2.properties = 0;
      return var2;
   }

   public Matrix4x3d sub(Matrix4x3dc var1) {
      return this.sub(var1, this);
   }

   public Matrix4x3d sub(Matrix4x3dc var1, Matrix4x3d var2) {
      var2.m00 = this.m00 - var1.m00();
      var2.m01 = this.m01 - var1.m01();
      var2.m02 = this.m02 - var1.m02();
      var2.m10 = this.m10 - var1.m10();
      var2.m11 = this.m11 - var1.m11();
      var2.m12 = this.m12 - var1.m12();
      var2.m20 = this.m20 - var1.m20();
      var2.m21 = this.m21 - var1.m21();
      var2.m22 = this.m22 - var1.m22();
      var2.m30 = this.m30 - var1.m30();
      var2.m31 = this.m31 - var1.m31();
      var2.m32 = this.m32 - var1.m32();
      var2.properties = 0;
      return var2;
   }

   public Matrix4x3d sub(Matrix4x3fc var1) {
      return this.sub(var1, this);
   }

   public Matrix4x3d sub(Matrix4x3fc var1, Matrix4x3d var2) {
      var2.m00 = this.m00 - (double)var1.m00();
      var2.m01 = this.m01 - (double)var1.m01();
      var2.m02 = this.m02 - (double)var1.m02();
      var2.m10 = this.m10 - (double)var1.m10();
      var2.m11 = this.m11 - (double)var1.m11();
      var2.m12 = this.m12 - (double)var1.m12();
      var2.m20 = this.m20 - (double)var1.m20();
      var2.m21 = this.m21 - (double)var1.m21();
      var2.m22 = this.m22 - (double)var1.m22();
      var2.m30 = this.m30 - (double)var1.m30();
      var2.m31 = this.m31 - (double)var1.m31();
      var2.m32 = this.m32 - (double)var1.m32();
      var2.properties = 0;
      return var2;
   }

   public Matrix4x3d mulComponentWise(Matrix4x3dc var1) {
      return this.mulComponentWise(var1, this);
   }

   public Matrix4x3d mulComponentWise(Matrix4x3dc var1, Matrix4x3d var2) {
      var2.m00 = this.m00 * var1.m00();
      var2.m01 = this.m01 * var1.m01();
      var2.m02 = this.m02 * var1.m02();
      var2.m10 = this.m10 * var1.m10();
      var2.m11 = this.m11 * var1.m11();
      var2.m12 = this.m12 * var1.m12();
      var2.m20 = this.m20 * var1.m20();
      var2.m21 = this.m21 * var1.m21();
      var2.m22 = this.m22 * var1.m22();
      var2.m30 = this.m30 * var1.m30();
      var2.m31 = this.m31 * var1.m31();
      var2.m32 = this.m32 * var1.m32();
      var2.properties = 0;
      return var2;
   }

   public Matrix4x3d set(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.m00 = var1;
      this.m10 = var7;
      this.m20 = var13;
      this.m30 = var19;
      this.m01 = var3;
      this.m11 = var9;
      this.m21 = var15;
      this.m31 = var21;
      this.m02 = var5;
      this.m12 = var11;
      this.m22 = var17;
      this.m32 = var23;
      return this.determineProperties();
   }

   public Matrix4x3d set(double[] var1, int var2) {
      this.m00 = var1[var2 + 0];
      this.m01 = var1[var2 + 1];
      this.m02 = var1[var2 + 2];
      this.m10 = var1[var2 + 3];
      this.m11 = var1[var2 + 4];
      this.m12 = var1[var2 + 5];
      this.m20 = var1[var2 + 6];
      this.m21 = var1[var2 + 7];
      this.m22 = var1[var2 + 8];
      this.m30 = var1[var2 + 9];
      this.m31 = var1[var2 + 10];
      this.m32 = var1[var2 + 11];
      return this.determineProperties();
   }

   public Matrix4x3d set(double[] var1) {
      return this.set((double[])var1, 0);
   }

   public Matrix4x3d set(float[] var1, int var2) {
      this.m00 = (double)var1[var2 + 0];
      this.m01 = (double)var1[var2 + 1];
      this.m02 = (double)var1[var2 + 2];
      this.m10 = (double)var1[var2 + 3];
      this.m11 = (double)var1[var2 + 4];
      this.m12 = (double)var1[var2 + 5];
      this.m20 = (double)var1[var2 + 6];
      this.m21 = (double)var1[var2 + 7];
      this.m22 = (double)var1[var2 + 8];
      this.m30 = (double)var1[var2 + 9];
      this.m31 = (double)var1[var2 + 10];
      this.m32 = (double)var1[var2 + 11];
      return this.determineProperties();
   }

   public Matrix4x3d set(float[] var1) {
      return this.set((float[])var1, 0);
   }

   public Matrix4x3d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3d set(FloatBuffer var1) {
      MemUtil.INSTANCE.getf(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3d setFloats(ByteBuffer var1) {
      MemUtil.INSTANCE.getf(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this.determineProperties();
      }
   }

   public double determinant() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public Matrix4x3d invert() {
      return this.invert(this);
   }

   public Matrix4x3d invert(Matrix4x3d var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.invertOrthonormal(var1) : this.invertGeneric(var1);
      }
   }

   private Matrix4x3d invertGeneric(Matrix4x3d var1) {
      double var2 = this.m00 * this.m11;
      double var4 = this.m01 * this.m10;
      double var6 = this.m02 * this.m10;
      double var8 = this.m00 * this.m12;
      double var10 = this.m01 * this.m12;
      double var12 = this.m02 * this.m11;
      double var14 = 1.0D / ((var2 - var4) * this.m22 + (var6 - var8) * this.m21 + (var10 - var12) * this.m20);
      double var16 = this.m10 * this.m22;
      double var18 = this.m10 * this.m21;
      double var20 = this.m11 * this.m22;
      double var22 = this.m11 * this.m20;
      double var24 = this.m12 * this.m21;
      double var26 = this.m12 * this.m20;
      double var28 = this.m20 * this.m02;
      double var30 = this.m20 * this.m01;
      double var32 = this.m21 * this.m02;
      double var34 = this.m21 * this.m00;
      double var36 = this.m22 * this.m01;
      double var38 = this.m22 * this.m00;
      double var40 = (var20 - var24) * var14;
      double var42 = (var32 - var36) * var14;
      double var44 = (var10 - var12) * var14;
      double var46 = (var26 - var16) * var14;
      double var48 = (var38 - var28) * var14;
      double var50 = (var6 - var8) * var14;
      double var52 = (var18 - var22) * var14;
      double var54 = (var30 - var34) * var14;
      double var56 = (var2 - var4) * var14;
      double var58 = (var16 * this.m31 - var18 * this.m32 + var22 * this.m32 - var20 * this.m30 + var24 * this.m30 - var26 * this.m31) * var14;
      double var60 = (var28 * this.m31 - var30 * this.m32 + var34 * this.m32 - var32 * this.m30 + var36 * this.m30 - var38 * this.m31) * var14;
      double var62 = (var12 * this.m30 - var10 * this.m30 + var8 * this.m31 - var6 * this.m31 + var4 * this.m32 - var2 * this.m32) * var14;
      var1.m00 = var40;
      var1.m01 = var42;
      var1.m02 = var44;
      var1.m10 = var46;
      var1.m11 = var48;
      var1.m12 = var50;
      var1.m20 = var52;
      var1.m21 = var54;
      var1.m22 = var56;
      var1.m30 = var58;
      var1.m31 = var60;
      var1.m32 = var62;
      var1.properties = 0;
      return var1;
   }

   private Matrix4x3d invertOrthonormal(Matrix4x3d var1) {
      double var2 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
      double var4 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
      double var6 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
      double var8 = this.m01;
      double var10 = this.m02;
      double var12 = this.m12;
      var1.m00 = this.m00;
      var1.m01 = this.m10;
      var1.m02 = this.m20;
      var1.m10 = var8;
      var1.m11 = this.m11;
      var1.m12 = this.m21;
      var1.m20 = var10;
      var1.m21 = var12;
      var1.m22 = this.m22;
      var1.m30 = var2;
      var1.m31 = var4;
      var1.m32 = var6;
      var1.properties = 16;
      return var1;
   }

   public Matrix4x3d invertOrtho(Matrix4x3d var1) {
      double var2 = 1.0D / this.m00;
      double var4 = 1.0D / this.m11;
      double var6 = 1.0D / this.m22;
      var1.set(var2, 0.0D, 0.0D, 0.0D, var4, 0.0D, 0.0D, 0.0D, var6, -this.m30 * var2, -this.m31 * var4, -this.m32 * var6);
      var1.properties = 0;
      return var1;
   }

   public Matrix4x3d invertOrtho() {
      return this.invertOrtho(this);
   }

   public Matrix4x3d transpose3x3() {
      return this.transpose3x3(this);
   }

   public Matrix4x3d transpose3x3(Matrix4x3d var1) {
      double var2 = this.m00;
      double var4 = this.m10;
      double var6 = this.m20;
      double var8 = this.m01;
      double var10 = this.m11;
      double var12 = this.m21;
      double var14 = this.m02;
      double var16 = this.m12;
      double var18 = this.m22;
      var1.m00 = var2;
      var1.m01 = var4;
      var1.m02 = var6;
      var1.m10 = var8;
      var1.m11 = var10;
      var1.m12 = var12;
      var1.m20 = var14;
      var1.m21 = var16;
      var1.m22 = var18;
      var1.properties = this.properties;
      return var1;
   }

   public Matrix3d transpose3x3(Matrix3d var1) {
      var1.m00(this.m00);
      var1.m01(this.m10);
      var1.m02(this.m20);
      var1.m10(this.m01);
      var1.m11(this.m11);
      var1.m12(this.m21);
      var1.m20(this.m02);
      var1.m21(this.m12);
      var1.m22(this.m22);
      return var1;
   }

   public Matrix4x3d translation(double var1, double var3, double var5) {
      if ((this.properties & 4) == 0) {
         this.identity();
      }

      this.m30 = var1;
      this.m31 = var3;
      this.m32 = var5;
      this.properties = 24;
      return this;
   }

   public Matrix4x3d translation(Vector3fc var1) {
      return this.translation((double)var1.x(), (double)var1.y(), (double)var1.z());
   }

   public Matrix4x3d translation(Vector3dc var1) {
      return this.translation(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3d setTranslation(double var1, double var3, double var5) {
      this.m30 = var1;
      this.m31 = var3;
      this.m32 = var5;
      this.properties &= -5;
      return this;
   }

   public Matrix4x3d setTranslation(Vector3dc var1) {
      return this.setTranslation(var1.x(), var1.y(), var1.z());
   }

   public Vector3d getTranslation(Vector3d var1) {
      var1.x = this.m30;
      var1.y = this.m31;
      var1.z = this.m32;
      return var1;
   }

   public Vector3d getScale(Vector3d var1) {
      var1.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      var1.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      var1.z = Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
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
      return var10000 + " " + Runtime.format(this.m10, var1) + " " + Runtime.format(this.m20, var1) + " " + Runtime.format(this.m30, var1) + "\n" + Runtime.format(this.m01, var1) + " " + Runtime.format(this.m11, var1) + " " + Runtime.format(this.m21, var1) + " " + Runtime.format(this.m31, var1) + "\n" + Runtime.format(this.m02, var1) + " " + Runtime.format(this.m12, var1) + " " + Runtime.format(this.m22, var1) + " " + Runtime.format(this.m32, var1) + "\n";
   }

   public Matrix4x3d get(Matrix4x3d var1) {
      return var1.set((Matrix4x3dc)this);
   }

   public Quaternionf getUnnormalizedRotation(Quaternionf var1) {
      return var1.setFromUnnormalized((Matrix4x3dc)this);
   }

   public Quaternionf getNormalizedRotation(Quaternionf var1) {
      return var1.setFromNormalized((Matrix4x3dc)this);
   }

   public Quaterniond getUnnormalizedRotation(Quaterniond var1) {
      return var1.setFromUnnormalized((Matrix4x3dc)this);
   }

   public Quaterniond getNormalizedRotation(Quaterniond var1) {
      return var1.setFromNormalized((Matrix4x3dc)this);
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

   public Matrix4x3dc getToAddress(long var1) {
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
      var1[var2 + 9] = this.m30;
      var1[var2 + 10] = this.m31;
      var1[var2 + 11] = this.m32;
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
      var1[var2 + 9] = (float)this.m30;
      var1[var2 + 10] = (float)this.m31;
      var1[var2 + 11] = (float)this.m32;
      return var1;
   }

   public float[] get(float[] var1) {
      return this.get((float[])var1, 0);
   }

   public float[] get4x4(float[] var1, int var2) {
      MemUtil.INSTANCE.copy4x4(this, var1, var2);
      return var1;
   }

   public float[] get4x4(float[] var1) {
      return this.get4x4((float[])var1, 0);
   }

   public double[] get4x4(double[] var1, int var2) {
      MemUtil.INSTANCE.copy4x4(this, var1, var2);
      return var1;
   }

   public double[] get4x4(double[] var1) {
      return this.get4x4((double[])var1, 0);
   }

   public DoubleBuffer get4x4(DoubleBuffer var1) {
      return this.get4x4(var1.position(), var1);
   }

   public DoubleBuffer get4x4(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public ByteBuffer get4x4(ByteBuffer var1) {
      return this.get4x4(var1.position(), var1);
   }

   public ByteBuffer get4x4(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public DoubleBuffer getTransposed(DoubleBuffer var1) {
      return this.getTransposed(var1.position(), var1);
   }

   public DoubleBuffer getTransposed(int var1, DoubleBuffer var2) {
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

   public FloatBuffer getTransposed(FloatBuffer var1) {
      return this.getTransposed(var1.position(), var1);
   }

   public FloatBuffer getTransposed(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.putfTransposed(this, var1, var2);
      return var2;
   }

   public ByteBuffer getTransposedFloats(ByteBuffer var1) {
      return this.getTransposed(var1.position(), var1);
   }

   public ByteBuffer getTransposedFloats(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putfTransposed(this, var1, var2);
      return var2;
   }

   public double[] getTransposed(double[] var1, int var2) {
      var1[var2 + 0] = this.m00;
      var1[var2 + 1] = this.m10;
      var1[var2 + 2] = this.m20;
      var1[var2 + 3] = this.m30;
      var1[var2 + 4] = this.m01;
      var1[var2 + 5] = this.m11;
      var1[var2 + 6] = this.m21;
      var1[var2 + 7] = this.m31;
      var1[var2 + 8] = this.m02;
      var1[var2 + 9] = this.m12;
      var1[var2 + 10] = this.m22;
      var1[var2 + 11] = this.m32;
      return var1;
   }

   public double[] getTransposed(double[] var1) {
      return this.getTransposed(var1, 0);
   }

   public Matrix4x3d zero() {
      this.m00 = 0.0D;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 0.0D;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 0.0D;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d scaling(double var1) {
      return this.scaling(var1, var1, var1);
   }

   public Matrix4x3d scaling(double var1, double var3, double var5) {
      if ((this.properties & 4) == 0) {
         this.identity();
      }

      this.m00 = var1;
      this.m11 = var3;
      this.m22 = var5;
      boolean var7 = Math.absEqualsOne(var1) && Math.absEqualsOne(var3) && Math.absEqualsOne(var5);
      this.properties = var7 ? 16 : 0;
      return this;
   }

   public Matrix4x3d scaling(Vector3dc var1) {
      return this.scaling(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3d rotation(double var1, double var3, double var5, double var7) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotationX(var3 * var1);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotationY(var5 * var1);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotationZ(var7 * var1) : this.rotationInternal(var1, var3, var5, var7);
      }
   }

   private Matrix4x3d rotationInternal(double var1, double var3, double var5, double var7) {
      double var9 = Math.sin(var1);
      double var11 = Math.cosFromSin(var9, var1);
      double var13 = 1.0D - var11;
      double var15 = var3 * var5;
      double var17 = var3 * var7;
      double var19 = var5 * var7;
      this.m00 = var11 + var3 * var3 * var13;
      this.m01 = var15 * var13 + var7 * var9;
      this.m02 = var17 * var13 - var5 * var9;
      this.m10 = var15 * var13 - var7 * var9;
      this.m11 = var11 + var5 * var5 * var13;
      this.m12 = var19 * var13 + var3 * var9;
      this.m20 = var17 * var13 + var5 * var9;
      this.m21 = var19 * var13 - var3 * var9;
      this.m22 = var11 + var7 * var7 * var13;
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationX(double var1) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationY(double var1) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationZ(double var1) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationXYZ(double var1, double var3, double var5) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationZYX(double var1, double var3, double var5) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotationYXZ(double var1, double var3, double var5) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d setRotationXYZ(double var1, double var3, double var5) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3d setRotationZYX(double var1, double var3, double var5) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3d setRotationYXZ(double var1, double var3, double var5) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3d rotation(double var1, Vector3dc var3) {
      return this.rotation(var1, var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d rotation(double var1, Vector3fc var3) {
      return this.rotation(var1, (double)var3.x(), (double)var3.y(), (double)var3.z());
   }

   public Vector4d transform(Vector4d var1) {
      return var1.mul((Matrix4x3dc)this);
   }

   public Vector4d transform(Vector4dc var1, Vector4d var2) {
      return var1.mul((Matrix4x3dc)this, var2);
   }

   public Vector3d transformPosition(Vector3d var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20 * var1.z + this.m30, this.m01 * var1.x + this.m11 * var1.y + this.m21 * var1.z + this.m31, this.m02 * var1.x + this.m12 * var1.y + this.m22 * var1.z + this.m32);
      return var1;
   }

   public Vector3d transformPosition(Vector3dc var1, Vector3d var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20 * var1.z() + this.m30, this.m01 * var1.x() + this.m11 * var1.y() + this.m21 * var1.z() + this.m31, this.m02 * var1.x() + this.m12 * var1.y() + this.m22 * var1.z() + this.m32);
      return var2;
   }

   public Vector3d transformDirection(Vector3d var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20 * var1.z, this.m01 * var1.x + this.m11 * var1.y + this.m21 * var1.z, this.m02 * var1.x + this.m12 * var1.y + this.m22 * var1.z);
      return var1;
   }

   public Vector3d transformDirection(Vector3dc var1, Vector3d var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20 * var1.z(), this.m01 * var1.x() + this.m11 * var1.y() + this.m21 * var1.z(), this.m02 * var1.x() + this.m12 * var1.y() + this.m22 * var1.z());
      return var2;
   }

   public Matrix4x3d set3x3(Matrix3dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.properties = 0;
      return this;
   }

   public Matrix4x3d set3x3(Matrix3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m02 = (double)var1.m02();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      this.m12 = (double)var1.m12();
      this.m20 = (double)var1.m20();
      this.m21 = (double)var1.m21();
      this.m22 = (double)var1.m22();
      this.properties = 0;
      return this;
   }

   public Matrix4x3d scale(Vector3dc var1, Matrix4x3d var2) {
      return this.scale(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3d scale(Vector3dc var1) {
      return this.scale(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix4x3d scale(double var1, double var3, double var5, Matrix4x3d var7) {
      return (this.properties & 4) != 0 ? var7.scaling(var1, var3, var5) : this.scaleGeneric(var1, var3, var5, var7);
   }

   private Matrix4x3d scaleGeneric(double var1, double var3, double var5, Matrix4x3d var7) {
      var7.m00 = this.m00 * var1;
      var7.m01 = this.m01 * var1;
      var7.m02 = this.m02 * var1;
      var7.m10 = this.m10 * var3;
      var7.m11 = this.m11 * var3;
      var7.m12 = this.m12 * var3;
      var7.m20 = this.m20 * var5;
      var7.m21 = this.m21 * var5;
      var7.m22 = this.m22 * var5;
      var7.m30 = this.m30;
      var7.m31 = this.m31;
      var7.m32 = this.m32;
      var7.properties = this.properties & -29;
      return var7;
   }

   public Matrix4x3d scale(double var1, double var3, double var5) {
      return this.scale(var1, var3, var5, this);
   }

   public Matrix4x3d scale(double var1, Matrix4x3d var3) {
      return this.scale(var1, var1, var1, var3);
   }

   public Matrix4x3d scale(double var1) {
      return this.scale(var1, var1, var1);
   }

   public Matrix4x3d scaleXY(double var1, double var3, Matrix4x3d var5) {
      return this.scale(var1, var3, 1.0D, var5);
   }

   public Matrix4x3d scaleXY(double var1, double var3) {
      return this.scale(var1, var3, 1.0D);
   }

   public Matrix4x3d scaleLocal(double var1, double var3, double var5, Matrix4x3d var7) {
      if ((this.properties & 4) != 0) {
         return var7.scaling(var1, var3, var5);
      } else {
         double var8 = var1 * this.m00;
         double var10 = var3 * this.m01;
         double var12 = var5 * this.m02;
         double var14 = var1 * this.m10;
         double var16 = var3 * this.m11;
         double var18 = var5 * this.m12;
         double var20 = var1 * this.m20;
         double var22 = var3 * this.m21;
         double var24 = var5 * this.m22;
         double var26 = var1 * this.m30;
         double var28 = var3 * this.m31;
         double var30 = var5 * this.m32;
         var7.m00 = var8;
         var7.m01 = var10;
         var7.m02 = var12;
         var7.m10 = var14;
         var7.m11 = var16;
         var7.m12 = var18;
         var7.m20 = var20;
         var7.m21 = var22;
         var7.m22 = var24;
         var7.m30 = var26;
         var7.m31 = var28;
         var7.m32 = var30;
         var7.properties = this.properties & -29;
         return var7;
      }
   }

   public Matrix4x3d scaleLocal(double var1, double var3, double var5) {
      return this.scaleLocal(var1, var3, var5, this);
   }

   public Matrix4x3d rotate(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      if ((this.properties & 4) != 0) {
         return var9.rotation(var1, var3, var5, var7);
      } else {
         return (this.properties & 8) != 0 ? this.rotateTranslation(var1, var3, var5, var7, var9) : this.rotateGeneric(var1, var3, var5, var7, var9);
      }
   }

   private Matrix4x3d rotateGeneric(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateX(var3 * var1, var9);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateY(var5 * var1, var9);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateZ(var7 * var1, var9) : this.rotateGenericInternal(var1, var3, var5, var7, var9);
      }
   }

   private Matrix4x3d rotateGenericInternal(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
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
      var9.m30 = this.m30;
      var9.m31 = this.m31;
      var9.m32 = this.m32;
      var9.properties = this.properties & -13;
      return var9;
   }

   public Matrix4x3d rotate(double var1, double var3, double var5, double var7) {
      return this.rotate(var1, var3, var5, var7, this);
   }

   public Matrix4x3d rotateTranslation(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      double var10 = this.m30;
      double var12 = this.m31;
      double var14 = this.m32;
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return var9.rotationX(var3 * var1).setTranslation(var10, var12, var14);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return var9.rotationY(var5 * var1).setTranslation(var10, var12, var14);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? var9.rotationZ(var7 * var1).setTranslation(var10, var12, var14) : this.rotateTranslationInternal(var1, var3, var5, var7, var9);
      }
   }

   private Matrix4x3d rotateTranslationInternal(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
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
      var9.m20 = var40;
      var9.m21 = var42;
      var9.m22 = var44;
      var9.m00 = var28;
      var9.m01 = var30;
      var9.m02 = var32;
      var9.m10 = var34;
      var9.m11 = var36;
      var9.m12 = var38;
      var9.m30 = this.m30;
      var9.m31 = this.m31;
      var9.m32 = this.m32;
      var9.properties = this.properties & -13;
      return var9;
   }

   public Matrix4x3d rotateAround(Quaterniondc var1, double var2, double var4, double var6) {
      return this.rotateAround(var1, var2, var4, var6, this);
   }

   private Matrix4x3d rotateAroundAffine(Quaterniondc var1, double var2, double var4, double var6, Matrix4x3d var8) {
      double var9 = var1.w() * var1.w();
      double var11 = var1.x() * var1.x();
      double var13 = var1.y() * var1.y();
      double var15 = var1.z() * var1.z();
      double var17 = var1.z() * var1.w();
      double var19 = var17 + var17;
      double var21 = var1.x() * var1.y();
      double var23 = var21 + var21;
      double var25 = var1.x() * var1.z();
      double var27 = var25 + var25;
      double var29 = var1.y() * var1.w();
      double var31 = var29 + var29;
      double var33 = var1.y() * var1.z();
      double var35 = var33 + var33;
      double var37 = var1.x() * var1.w();
      double var39 = var37 + var37;
      double var41 = var9 + var11 - var15 - var13;
      double var43 = var23 + var19;
      double var45 = var27 - var31;
      double var47 = var23 - var19;
      double var49 = var13 - var15 + var9 - var11;
      double var51 = var35 + var39;
      double var53 = var31 + var27;
      double var55 = var35 - var39;
      double var57 = var15 - var13 - var11 + var9;
      double var59 = this.m00 * var2 + this.m10 * var4 + this.m20 * var6 + this.m30;
      double var61 = this.m01 * var2 + this.m11 * var4 + this.m21 * var6 + this.m31;
      double var63 = this.m02 * var2 + this.m12 * var4 + this.m22 * var6 + this.m32;
      double var65 = this.m00 * var41 + this.m10 * var43 + this.m20 * var45;
      double var67 = this.m01 * var41 + this.m11 * var43 + this.m21 * var45;
      double var69 = this.m02 * var41 + this.m12 * var43 + this.m22 * var45;
      double var71 = this.m00 * var47 + this.m10 * var49 + this.m20 * var51;
      double var73 = this.m01 * var47 + this.m11 * var49 + this.m21 * var51;
      double var75 = this.m02 * var47 + this.m12 * var49 + this.m22 * var51;
      var8._m20(this.m00 * var53 + this.m10 * var55 + this.m20 * var57)._m21(this.m01 * var53 + this.m11 * var55 + this.m21 * var57)._m22(this.m02 * var53 + this.m12 * var55 + this.m22 * var57)._m00(var65)._m01(var67)._m02(var69)._m10(var71)._m11(var73)._m12(var75)._m30(-var65 * var2 - var71 * var4 - this.m20 * var6 + var59)._m31(-var67 * var2 - var73 * var4 - this.m21 * var6 + var61)._m32(-var69 * var2 - var75 * var4 - this.m22 * var6 + var63)._properties(this.properties & -13);
      return var8;
   }

   public Matrix4x3d rotateAround(Quaterniondc var1, double var2, double var4, double var6, Matrix4x3d var8) {
      return (this.properties & 4) != 0 ? this.rotationAround(var1, var2, var4, var6) : this.rotateAroundAffine(var1, var2, var4, var6, var8);
   }

   public Matrix4x3d rotationAround(Quaterniondc var1, double var2, double var4, double var6) {
      double var8 = var1.w() * var1.w();
      double var10 = var1.x() * var1.x();
      double var12 = var1.y() * var1.y();
      double var14 = var1.z() * var1.z();
      double var16 = var1.z() * var1.w();
      double var18 = var16 + var16;
      double var20 = var1.x() * var1.y();
      double var22 = var20 + var20;
      double var24 = var1.x() * var1.z();
      double var26 = var24 + var24;
      double var28 = var1.y() * var1.w();
      double var30 = var28 + var28;
      double var32 = var1.y() * var1.z();
      double var34 = var32 + var32;
      double var36 = var1.x() * var1.w();
      double var38 = var36 + var36;
      this._m20(var30 + var26);
      this._m21(var34 - var38);
      this._m22(var14 - var12 - var10 + var8);
      this._m00(var8 + var10 - var14 - var12);
      this._m01(var22 + var18);
      this._m02(var26 - var30);
      this._m10(var22 - var18);
      this._m11(var12 - var14 + var8 - var10);
      this._m12(var34 + var38);
      this._m30(-this.m00 * var2 - this.m10 * var4 - this.m20 * var6 + var2);
      this._m31(-this.m01 * var2 - this.m11 * var4 - this.m21 * var6 + var4);
      this._m32(-this.m02 * var2 - this.m12 * var4 - this.m22 * var6 + var6);
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotateLocal(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateLocalX(var3 * var1, var9);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateLocalY(var5 * var1, var9);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateLocalZ(var7 * var1, var9) : this.rotateLocalInternal(var1, var3, var5, var7, var9);
      }
   }

   private Matrix4x3d rotateLocalInternal(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
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
      double var64 = var28 * this.m30 + var34 * this.m31 + var40 * this.m32;
      double var66 = var30 * this.m30 + var36 * this.m31 + var42 * this.m32;
      double var68 = var32 * this.m30 + var38 * this.m31 + var44 * this.m32;
      var9.m00 = var46;
      var9.m01 = var48;
      var9.m02 = var50;
      var9.m10 = var52;
      var9.m11 = var54;
      var9.m12 = var56;
      var9.m20 = var58;
      var9.m21 = var60;
      var9.m22 = var62;
      var9.m30 = var64;
      var9.m31 = var66;
      var9.m32 = var68;
      var9.properties = this.properties & -13;
      return var9;
   }

   public Matrix4x3d rotateLocal(double var1, double var3, double var5, double var7) {
      return this.rotateLocal(var1, var3, var5, var7, this);
   }

   public Matrix4x3d rotateLocalX(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m01 - var4 * this.m02;
      double var10 = var4 * this.m01 + var6 * this.m02;
      double var12 = var6 * this.m11 - var4 * this.m12;
      double var14 = var4 * this.m11 + var6 * this.m12;
      double var16 = var6 * this.m21 - var4 * this.m22;
      double var18 = var4 * this.m21 + var6 * this.m22;
      double var20 = var6 * this.m31 - var4 * this.m32;
      double var22 = var4 * this.m31 + var6 * this.m32;
      var3.m00 = this.m00;
      var3.m01 = var8;
      var3.m02 = var10;
      var3.m10 = this.m10;
      var3.m11 = var12;
      var3.m12 = var14;
      var3.m20 = this.m20;
      var3.m21 = var16;
      var3.m22 = var18;
      var3.m30 = this.m30;
      var3.m31 = var20;
      var3.m32 = var22;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateLocalX(double var1) {
      return this.rotateLocalX(var1, this);
   }

   public Matrix4x3d rotateLocalY(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 + var4 * this.m02;
      double var10 = -var4 * this.m00 + var6 * this.m02;
      double var12 = var6 * this.m10 + var4 * this.m12;
      double var14 = -var4 * this.m10 + var6 * this.m12;
      double var16 = var6 * this.m20 + var4 * this.m22;
      double var18 = -var4 * this.m20 + var6 * this.m22;
      double var20 = var6 * this.m30 + var4 * this.m32;
      double var22 = -var4 * this.m30 + var6 * this.m32;
      var3.m00 = var8;
      var3.m01 = this.m01;
      var3.m02 = var10;
      var3.m10 = var12;
      var3.m11 = this.m11;
      var3.m12 = var14;
      var3.m20 = var16;
      var3.m21 = this.m21;
      var3.m22 = var18;
      var3.m30 = var20;
      var3.m31 = this.m31;
      var3.m32 = var22;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateLocalY(double var1) {
      return this.rotateLocalY(var1, this);
   }

   public Matrix4x3d rotateLocalZ(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 - var4 * this.m01;
      double var10 = var4 * this.m00 + var6 * this.m01;
      double var12 = var6 * this.m10 - var4 * this.m11;
      double var14 = var4 * this.m10 + var6 * this.m11;
      double var16 = var6 * this.m20 - var4 * this.m21;
      double var18 = var4 * this.m20 + var6 * this.m21;
      double var20 = var6 * this.m30 - var4 * this.m31;
      double var22 = var4 * this.m30 + var6 * this.m31;
      var3.m00 = var8;
      var3.m01 = var10;
      var3.m02 = this.m02;
      var3.m10 = var12;
      var3.m11 = var14;
      var3.m12 = this.m12;
      var3.m20 = var16;
      var3.m21 = var18;
      var3.m22 = this.m22;
      var3.m30 = var20;
      var3.m31 = var22;
      var3.m32 = this.m32;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateLocalZ(double var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Matrix4x3d translate(Vector3dc var1) {
      return this.translate(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3d translate(Vector3dc var1, Matrix4x3d var2) {
      return this.translate(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3d translate(Vector3fc var1) {
      return this.translate((double)var1.x(), (double)var1.y(), (double)var1.z());
   }

   public Matrix4x3d translate(Vector3fc var1, Matrix4x3d var2) {
      return this.translate((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Matrix4x3d translate(double var1, double var3, double var5, Matrix4x3d var7) {
      return (this.properties & 4) != 0 ? var7.translation(var1, var3, var5) : this.translateGeneric(var1, var3, var5, var7);
   }

   private Matrix4x3d translateGeneric(double var1, double var3, double var5, Matrix4x3d var7) {
      var7.m00 = this.m00;
      var7.m01 = this.m01;
      var7.m02 = this.m02;
      var7.m10 = this.m10;
      var7.m11 = this.m11;
      var7.m12 = this.m12;
      var7.m20 = this.m20;
      var7.m21 = this.m21;
      var7.m22 = this.m22;
      var7.m30 = this.m00 * var1 + this.m10 * var3 + this.m20 * var5 + this.m30;
      var7.m31 = this.m01 * var1 + this.m11 * var3 + this.m21 * var5 + this.m31;
      var7.m32 = this.m02 * var1 + this.m12 * var3 + this.m22 * var5 + this.m32;
      var7.properties = this.properties & -5;
      return var7;
   }

   public Matrix4x3d translate(double var1, double var3, double var5) {
      if ((this.properties & 4) != 0) {
         return this.translation(var1, var3, var5);
      } else {
         this.m30 += this.m00 * var1 + this.m10 * var3 + this.m20 * var5;
         this.m31 += this.m01 * var1 + this.m11 * var3 + this.m21 * var5;
         this.m32 += this.m02 * var1 + this.m12 * var3 + this.m22 * var5;
         this.properties &= -5;
         return this;
      }
   }

   public Matrix4x3d translateLocal(Vector3fc var1) {
      return this.translateLocal((double)var1.x(), (double)var1.y(), (double)var1.z());
   }

   public Matrix4x3d translateLocal(Vector3fc var1, Matrix4x3d var2) {
      return this.translateLocal((double)var1.x(), (double)var1.y(), (double)var1.z(), var2);
   }

   public Matrix4x3d translateLocal(Vector3dc var1) {
      return this.translateLocal(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3d translateLocal(Vector3dc var1, Matrix4x3d var2) {
      return this.translateLocal(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3d translateLocal(double var1, double var3, double var5, Matrix4x3d var7) {
      var7.m00 = this.m00;
      var7.m01 = this.m01;
      var7.m02 = this.m02;
      var7.m10 = this.m10;
      var7.m11 = this.m11;
      var7.m12 = this.m12;
      var7.m20 = this.m20;
      var7.m21 = this.m21;
      var7.m22 = this.m22;
      var7.m30 = this.m30 + var1;
      var7.m31 = this.m31 + var3;
      var7.m32 = this.m32 + var5;
      var7.properties = this.properties & -5;
      return var7;
   }

   public Matrix4x3d translateLocal(double var1, double var3, double var5) {
      return this.translateLocal(var1, var3, var5, this);
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
      var1.writeDouble(this.m30);
      var1.writeDouble(this.m31);
      var1.writeDouble(this.m32);
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
      this.m30 = var1.readDouble();
      this.m31 = var1.readDouble();
      this.m32 = var1.readDouble();
      this.determineProperties();
   }

   public Matrix4x3d rotateX(double var1, Matrix4x3d var3) {
      if ((this.properties & 4) != 0) {
         return var3.rotationX(var1);
      } else if ((this.properties & 8) != 0) {
         double var4 = this.m30;
         double var6 = this.m31;
         double var8 = this.m32;
         return var3.rotationX(var1).setTranslation(var4, var6, var8);
      } else {
         return this.rotateXInternal(var1, var3);
      }
   }

   private Matrix4x3d rotateXInternal(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var12 = -var4;
      double var16 = this.m10 * var6 + this.m20 * var4;
      double var18 = this.m11 * var6 + this.m21 * var4;
      double var20 = this.m12 * var6 + this.m22 * var4;
      var3.m20 = this.m10 * var12 + this.m20 * var6;
      var3.m21 = this.m11 * var12 + this.m21 * var6;
      var3.m22 = this.m12 * var12 + this.m22 * var6;
      var3.m10 = var16;
      var3.m11 = var18;
      var3.m12 = var20;
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m02 = this.m02;
      var3.m30 = this.m30;
      var3.m31 = this.m31;
      var3.m32 = this.m32;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateX(double var1) {
      return this.rotateX(var1, this);
   }

   public Matrix4x3d rotateY(double var1, Matrix4x3d var3) {
      if ((this.properties & 4) != 0) {
         return var3.rotationY(var1);
      } else if ((this.properties & 8) != 0) {
         double var4 = this.m30;
         double var6 = this.m31;
         double var8 = this.m32;
         return var3.rotationY(var1).setTranslation(var4, var6, var8);
      } else {
         return this.rotateYInternal(var1, var3);
      }
   }

   private Matrix4x3d rotateYInternal(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var10 = -var4;
      double var16 = this.m00 * var6 + this.m20 * var10;
      double var18 = this.m01 * var6 + this.m21 * var10;
      double var20 = this.m02 * var6 + this.m22 * var10;
      var3.m20 = this.m00 * var4 + this.m20 * var6;
      var3.m21 = this.m01 * var4 + this.m21 * var6;
      var3.m22 = this.m02 * var4 + this.m22 * var6;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m02 = var20;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      var3.m12 = this.m12;
      var3.m30 = this.m30;
      var3.m31 = this.m31;
      var3.m32 = this.m32;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateY(double var1) {
      return this.rotateY(var1, this);
   }

   public Matrix4x3d rotateZ(double var1, Matrix4x3d var3) {
      if ((this.properties & 4) != 0) {
         return var3.rotationZ(var1);
      } else if ((this.properties & 8) != 0) {
         double var4 = this.m30;
         double var6 = this.m31;
         double var8 = this.m32;
         return var3.rotationZ(var1).setTranslation(var4, var6, var8);
      } else {
         return this.rotateZInternal(var1, var3);
      }
   }

   private Matrix4x3d rotateZInternal(double var1, Matrix4x3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var12 = -var4;
      double var16 = this.m00 * var6 + this.m10 * var4;
      double var18 = this.m01 * var6 + this.m11 * var4;
      double var20 = this.m02 * var6 + this.m12 * var4;
      var3.m10 = this.m00 * var12 + this.m10 * var6;
      var3.m11 = this.m01 * var12 + this.m11 * var6;
      var3.m12 = this.m02 * var12 + this.m12 * var6;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m02 = var20;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      var3.m22 = this.m22;
      var3.m30 = this.m30;
      var3.m31 = this.m31;
      var3.m32 = this.m32;
      var3.properties = this.properties & -13;
      return var3;
   }

   public Matrix4x3d rotateZ(double var1) {
      return this.rotateZ(var1, this);
   }

   public Matrix4x3d rotateXYZ(Vector3d var1) {
      return this.rotateXYZ(var1.x, var1.y, var1.z);
   }

   public Matrix4x3d rotateXYZ(double var1, double var3, double var5) {
      return this.rotateXYZ(var1, var3, var5, this);
   }

   public Matrix4x3d rotateXYZ(double var1, double var3, double var5, Matrix4x3d var7) {
      if ((this.properties & 4) != 0) {
         return var7.rotationXYZ(var1, var3, var5);
      } else if ((this.properties & 8) != 0) {
         double var8 = this.m30;
         double var10 = this.m31;
         double var12 = this.m32;
         return var7.rotationXYZ(var1, var3, var5).setTranslation(var8, var10, var12);
      } else {
         return this.rotateXYZInternal(var1, var3, var5, var7);
      }
   }

   private Matrix4x3d rotateXYZInternal(double var1, double var3, double var5, Matrix4x3d var7) {
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
      var7.m30 = this.m30;
      var7.m31 = this.m31;
      var7.m32 = this.m32;
      var7.properties = this.properties & -13;
      return var7;
   }

   public Matrix4x3d rotateZYX(Vector3d var1) {
      return this.rotateZYX(var1.z, var1.y, var1.x);
   }

   public Matrix4x3d rotateZYX(double var1, double var3, double var5) {
      return this.rotateZYX(var1, var3, var5, this);
   }

   public Matrix4x3d rotateZYX(double var1, double var3, double var5, Matrix4x3d var7) {
      if ((this.properties & 4) != 0) {
         return var7.rotationZYX(var1, var3, var5);
      } else if ((this.properties & 8) != 0) {
         double var8 = this.m30;
         double var10 = this.m31;
         double var12 = this.m32;
         return var7.rotationZYX(var1, var3, var5).setTranslation(var8, var10, var12);
      } else {
         return this.rotateZYXInternal(var1, var3, var5, var7);
      }
   }

   private Matrix4x3d rotateZYXInternal(double var1, double var3, double var5, Matrix4x3d var7) {
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
      var7.m30 = this.m30;
      var7.m31 = this.m31;
      var7.m32 = this.m32;
      var7.properties = this.properties & -13;
      return var7;
   }

   public Matrix4x3d rotateYXZ(Vector3d var1) {
      return this.rotateYXZ(var1.y, var1.x, var1.z);
   }

   public Matrix4x3d rotateYXZ(double var1, double var3, double var5) {
      return this.rotateYXZ(var1, var3, var5, this);
   }

   public Matrix4x3d rotateYXZ(double var1, double var3, double var5, Matrix4x3d var7) {
      if ((this.properties & 4) != 0) {
         return var7.rotationYXZ(var1, var3, var5);
      } else if ((this.properties & 8) != 0) {
         double var8 = this.m30;
         double var10 = this.m31;
         double var12 = this.m32;
         return var7.rotationYXZ(var1, var3, var5).setTranslation(var8, var10, var12);
      } else {
         return this.rotateYXZInternal(var1, var3, var5, var7);
      }
   }

   private Matrix4x3d rotateYXZInternal(double var1, double var3, double var5, Matrix4x3d var7) {
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
      var7.m30 = this.m30;
      var7.m31 = this.m31;
      var7.m32 = this.m32;
      var7.properties = this.properties & -13;
      return var7;
   }

   public Matrix4x3d rotation(AxisAngle4f var1) {
      return this.rotation((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Matrix4x3d rotation(AxisAngle4d var1) {
      return this.rotation(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4x3d rotation(Quaterniondc var1) {
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
      this._m00(var2 + var4 - var8 - var6);
      this._m01(var16 + var12);
      this._m02(var20 - var24);
      this._m10(var16 - var12);
      this._m11(var6 - var8 + var2 - var4);
      this._m12(var28 + var32);
      this._m20(var24 + var20);
      this._m21(var28 - var32);
      this._m22(var8 - var6 - var4 + var2);
      this._m30(0.0D);
      this._m31(0.0D);
      this._m32(0.0D);
      this.properties = 16;
      return this;
   }

   public Matrix4x3d rotation(Quaternionfc var1) {
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
      this._m00(var2 + var4 - var8 - var6);
      this._m01(var16 + var12);
      this._m02(var20 - var24);
      this._m10(var16 - var12);
      this._m11(var6 - var8 + var2 - var4);
      this._m12(var28 + var32);
      this._m20(var24 + var20);
      this._m21(var28 - var32);
      this._m22(var8 - var6 - var4 + var2);
      this._m30(0.0D);
      this._m31(0.0D);
      this._m32(0.0D);
      this.properties = 16;
      return this;
   }

   public Matrix4x3d translationRotateScale(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19) {
      double var21 = var7 + var7;
      double var23 = var9 + var9;
      double var25 = var11 + var11;
      double var27 = var21 * var7;
      double var29 = var23 * var9;
      double var31 = var25 * var11;
      double var33 = var21 * var9;
      double var35 = var21 * var11;
      double var37 = var21 * var13;
      double var39 = var23 * var11;
      double var41 = var23 * var13;
      double var43 = var25 * var13;
      this.m00 = var15 - (var29 + var31) * var15;
      this.m01 = (var33 + var43) * var15;
      this.m02 = (var35 - var41) * var15;
      this.m10 = (var33 - var43) * var17;
      this.m11 = var17 - (var31 + var27) * var17;
      this.m12 = (var39 + var37) * var17;
      this.m20 = (var35 + var41) * var19;
      this.m21 = (var39 - var37) * var19;
      this.m22 = var19 - (var29 + var27) * var19;
      this.m30 = var1;
      this.m31 = var3;
      this.m32 = var5;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d translationRotateScale(Vector3fc var1, Quaternionfc var2, Vector3fc var3) {
      return this.translationRotateScale((double)var1.x(), (double)var1.y(), (double)var1.z(), (double)var2.x(), (double)var2.y(), (double)var2.z(), (double)var2.w(), (double)var3.x(), (double)var3.y(), (double)var3.z());
   }

   public Matrix4x3d translationRotateScale(Vector3dc var1, Quaterniondc var2, Vector3dc var3) {
      return this.translationRotateScale(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d translationRotateScaleMul(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, Matrix4x3dc var21) {
      double var22 = var7 + var7;
      double var24 = var9 + var9;
      double var26 = var11 + var11;
      double var28 = var22 * var7;
      double var30 = var24 * var9;
      double var32 = var26 * var11;
      double var34 = var22 * var9;
      double var36 = var22 * var11;
      double var38 = var22 * var13;
      double var40 = var24 * var11;
      double var42 = var24 * var13;
      double var44 = var26 * var13;
      double var46 = var15 - (var30 + var32) * var15;
      double var48 = (var34 + var44) * var15;
      double var50 = (var36 - var42) * var15;
      double var52 = (var34 - var44) * var17;
      double var54 = var17 - (var32 + var28) * var17;
      double var56 = (var40 + var38) * var17;
      double var58 = (var36 + var42) * var19;
      double var60 = (var40 - var38) * var19;
      double var62 = var19 - (var30 + var28) * var19;
      double var64 = var46 * var21.m00() + var52 * var21.m01() + var58 * var21.m02();
      double var66 = var48 * var21.m00() + var54 * var21.m01() + var60 * var21.m02();
      this.m02 = var50 * var21.m00() + var56 * var21.m01() + var62 * var21.m02();
      this.m00 = var64;
      this.m01 = var66;
      double var68 = var46 * var21.m10() + var52 * var21.m11() + var58 * var21.m12();
      double var70 = var48 * var21.m10() + var54 * var21.m11() + var60 * var21.m12();
      this.m12 = var50 * var21.m10() + var56 * var21.m11() + var62 * var21.m12();
      this.m10 = var68;
      this.m11 = var70;
      double var72 = var46 * var21.m20() + var52 * var21.m21() + var58 * var21.m22();
      double var74 = var48 * var21.m20() + var54 * var21.m21() + var60 * var21.m22();
      this.m22 = var50 * var21.m20() + var56 * var21.m21() + var62 * var21.m22();
      this.m20 = var72;
      this.m21 = var74;
      double var76 = var46 * var21.m30() + var52 * var21.m31() + var58 * var21.m32() + var1;
      double var78 = var48 * var21.m30() + var54 * var21.m31() + var60 * var21.m32() + var3;
      this.m32 = var50 * var21.m30() + var56 * var21.m31() + var62 * var21.m32() + var5;
      this.m30 = var76;
      this.m31 = var78;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d translationRotateScaleMul(Vector3dc var1, Quaterniondc var2, Vector3dc var3, Matrix4x3dc var4) {
      return this.translationRotateScaleMul(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3d translationRotate(double var1, double var3, double var5, Quaterniondc var7) {
      double var8 = var7.x() + var7.x();
      double var10 = var7.y() + var7.y();
      double var12 = var7.z() + var7.z();
      double var14 = var8 * var7.x();
      double var16 = var10 * var7.y();
      double var18 = var12 * var7.z();
      double var20 = var8 * var7.y();
      double var22 = var8 * var7.z();
      double var24 = var8 * var7.w();
      double var26 = var10 * var7.z();
      double var28 = var10 * var7.w();
      double var30 = var12 * var7.w();
      this.m00 = 1.0D - (var16 + var18);
      this.m01 = var20 + var30;
      this.m02 = var22 - var28;
      this.m10 = var20 - var30;
      this.m11 = 1.0D - (var18 + var14);
      this.m12 = var26 + var24;
      this.m20 = var22 + var28;
      this.m21 = var26 - var24;
      this.m22 = 1.0D - (var16 + var14);
      this.m30 = var1;
      this.m31 = var3;
      this.m32 = var5;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d translationRotateMul(double var1, double var3, double var5, Quaternionfc var7, Matrix4x3dc var8) {
      return this.translationRotateMul(var1, var3, var5, (double)var7.x(), (double)var7.y(), (double)var7.z(), (double)var7.w(), var8);
   }

   public Matrix4x3d translationRotateMul(double var1, double var3, double var5, double var7, double var9, double var11, double var13, Matrix4x3dc var15) {
      double var16 = var13 * var13;
      double var18 = var7 * var7;
      double var20 = var9 * var9;
      double var22 = var11 * var11;
      double var24 = var11 * var13;
      double var26 = var7 * var9;
      double var28 = var7 * var11;
      double var30 = var9 * var13;
      double var32 = var9 * var11;
      double var34 = var7 * var13;
      double var36 = var16 + var18 - var22 - var20;
      double var38 = var26 + var24 + var24 + var26;
      double var40 = var28 - var30 + var28 - var30;
      double var42 = -var24 + var26 - var24 + var26;
      double var44 = var20 - var22 + var16 - var18;
      double var46 = var32 + var32 + var34 + var34;
      double var48 = var30 + var28 + var28 + var30;
      double var50 = var32 + var32 - var34 - var34;
      double var52 = var22 - var20 - var18 + var16;
      this.m00 = var36 * var15.m00() + var42 * var15.m01() + var48 * var15.m02();
      this.m01 = var38 * var15.m00() + var44 * var15.m01() + var50 * var15.m02();
      this.m02 = var40 * var15.m00() + var46 * var15.m01() + var52 * var15.m02();
      this.m10 = var36 * var15.m10() + var42 * var15.m11() + var48 * var15.m12();
      this.m11 = var38 * var15.m10() + var44 * var15.m11() + var50 * var15.m12();
      this.m12 = var40 * var15.m10() + var46 * var15.m11() + var52 * var15.m12();
      this.m20 = var36 * var15.m20() + var42 * var15.m21() + var48 * var15.m22();
      this.m21 = var38 * var15.m20() + var44 * var15.m21() + var50 * var15.m22();
      this.m22 = var40 * var15.m20() + var46 * var15.m21() + var52 * var15.m22();
      this.m30 = var36 * var15.m30() + var42 * var15.m31() + var48 * var15.m32() + var1;
      this.m31 = var38 * var15.m30() + var44 * var15.m31() + var50 * var15.m32() + var3;
      this.m32 = var40 * var15.m30() + var46 * var15.m31() + var52 * var15.m32() + var5;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d rotate(Quaterniondc var1, Matrix4x3d var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotation(var1);
      } else {
         return (this.properties & 8) != 0 ? this.rotateTranslation(var1, var2) : this.rotateGeneric(var1, var2);
      }
   }

   private Matrix4x3d rotateGeneric(Quaterniondc var1, Matrix4x3d var2) {
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
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotate(Quaternionfc var1, Matrix4x3d var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotation(var1);
      } else {
         return (this.properties & 8) != 0 ? this.rotateTranslation(var1, var2) : this.rotateGeneric(var1, var2);
      }
   }

   private Matrix4x3d rotateGeneric(Quaternionfc var1, Matrix4x3d var2) {
      double var3 = (double)(var1.w() * var1.w());
      double var5 = (double)(var1.x() * var1.x());
      double var7 = (double)(var1.y() * var1.y());
      double var9 = (double)(var1.z() * var1.z());
      double var11 = (double)(var1.z() * var1.w());
      double var13 = (double)(var1.x() * var1.y());
      double var15 = (double)(var1.x() * var1.z());
      double var17 = (double)(var1.y() * var1.w());
      double var19 = (double)(var1.y() * var1.z());
      double var21 = (double)(var1.x() * var1.w());
      double var23 = var3 + var5 - var9 - var7;
      double var25 = var13 + var11 + var11 + var13;
      double var27 = var15 - var17 + var15 - var17;
      double var29 = -var11 + var13 - var11 + var13;
      double var31 = var7 - var9 + var3 - var5;
      double var33 = var19 + var19 + var21 + var21;
      double var35 = var17 + var15 + var15 + var17;
      double var37 = var19 + var19 - var21 - var21;
      double var39 = var9 - var7 - var5 + var3;
      double var41 = this.m00 * var23 + this.m10 * var25 + this.m20 * var27;
      double var43 = this.m01 * var23 + this.m11 * var25 + this.m21 * var27;
      double var45 = this.m02 * var23 + this.m12 * var25 + this.m22 * var27;
      double var47 = this.m00 * var29 + this.m10 * var31 + this.m20 * var33;
      double var49 = this.m01 * var29 + this.m11 * var31 + this.m21 * var33;
      double var51 = this.m02 * var29 + this.m12 * var31 + this.m22 * var33;
      var2.m20 = this.m00 * var35 + this.m10 * var37 + this.m20 * var39;
      var2.m21 = this.m01 * var35 + this.m11 * var37 + this.m21 * var39;
      var2.m22 = this.m02 * var35 + this.m12 * var37 + this.m22 * var39;
      var2.m00 = var41;
      var2.m01 = var43;
      var2.m02 = var45;
      var2.m10 = var47;
      var2.m11 = var49;
      var2.m12 = var51;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotate(Quaterniondc var1) {
      return this.rotate(var1, this);
   }

   public Matrix4x3d rotate(Quaternionfc var1) {
      return this.rotate(var1, this);
   }

   public Matrix4x3d rotateTranslation(Quaterniondc var1, Matrix4x3d var2) {
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
      var2.m20 = var47;
      var2.m21 = var49;
      var2.m22 = var51;
      var2.m00 = var35;
      var2.m01 = var37;
      var2.m02 = var39;
      var2.m10 = var41;
      var2.m11 = var43;
      var2.m12 = var45;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotateTranslation(Quaternionfc var1, Matrix4x3d var2) {
      double var3 = (double)(var1.w() * var1.w());
      double var5 = (double)(var1.x() * var1.x());
      double var7 = (double)(var1.y() * var1.y());
      double var9 = (double)(var1.z() * var1.z());
      double var11 = (double)(var1.z() * var1.w());
      double var13 = (double)(var1.x() * var1.y());
      double var15 = (double)(var1.x() * var1.z());
      double var17 = (double)(var1.y() * var1.w());
      double var19 = (double)(var1.y() * var1.z());
      double var21 = (double)(var1.x() * var1.w());
      double var23 = var3 + var5 - var9 - var7;
      double var25 = var13 + var11 + var11 + var13;
      double var27 = var15 - var17 + var15 - var17;
      double var29 = -var11 + var13 - var11 + var13;
      double var31 = var7 - var9 + var3 - var5;
      double var33 = var19 + var19 + var21 + var21;
      double var35 = var17 + var15 + var15 + var17;
      double var37 = var19 + var19 - var21 - var21;
      double var39 = var9 - var7 - var5 + var3;
      var2.m20 = var35;
      var2.m21 = var37;
      var2.m22 = var39;
      var2.m00 = var23;
      var2.m01 = var25;
      var2.m02 = var27;
      var2.m10 = var29;
      var2.m11 = var31;
      var2.m12 = var33;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotateLocal(Quaterniondc var1, Matrix4x3d var2) {
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
      double var71 = var35 * this.m30 + var41 * this.m31 + var47 * this.m32;
      double var73 = var37 * this.m30 + var43 * this.m31 + var49 * this.m32;
      double var75 = var39 * this.m30 + var45 * this.m31 + var51 * this.m32;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      var2.m20 = var65;
      var2.m21 = var67;
      var2.m22 = var69;
      var2.m30 = var71;
      var2.m31 = var73;
      var2.m32 = var75;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotateLocal(Quaterniondc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix4x3d rotateLocal(Quaternionfc var1, Matrix4x3d var2) {
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
      double var71 = var35 * this.m30 + var41 * this.m31 + var47 * this.m32;
      double var73 = var37 * this.m30 + var43 * this.m31 + var49 * this.m32;
      double var75 = var39 * this.m30 + var45 * this.m31 + var51 * this.m32;
      var2.m00 = var53;
      var2.m01 = var55;
      var2.m02 = var57;
      var2.m10 = var59;
      var2.m11 = var61;
      var2.m12 = var63;
      var2.m20 = var65;
      var2.m21 = var67;
      var2.m22 = var69;
      var2.m30 = var71;
      var2.m31 = var73;
      var2.m32 = var75;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3d rotateLocal(Quaternionfc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix4x3d rotate(AxisAngle4f var1) {
      return this.rotate((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Matrix4x3d rotate(AxisAngle4f var1, Matrix4x3d var2) {
      return this.rotate((double)var1.angle, (double)var1.x, (double)var1.y, (double)var1.z, var2);
   }

   public Matrix4x3d rotate(AxisAngle4d var1) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4x3d rotate(AxisAngle4d var1, Matrix4x3d var2) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z, var2);
   }

   public Matrix4x3d rotate(double var1, Vector3dc var3) {
      return this.rotate(var1, var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d rotate(double var1, Vector3dc var3, Matrix4x3d var4) {
      return this.rotate(var1, var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3d rotate(double var1, Vector3fc var3) {
      return this.rotate(var1, (double)var3.x(), (double)var3.y(), (double)var3.z());
   }

   public Matrix4x3d rotate(double var1, Vector3fc var3, Matrix4x3d var4) {
      return this.rotate(var1, (double)var3.x(), (double)var3.y(), (double)var3.z(), var4);
   }

   public Vector4d getRow(int var1, Vector4d var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         var2.x = this.m00;
         var2.y = this.m10;
         var2.z = this.m20;
         var2.w = this.m30;
         break;
      case 1:
         var2.x = this.m01;
         var2.y = this.m11;
         var2.z = this.m21;
         var2.w = this.m31;
         break;
      case 2:
         var2.x = this.m02;
         var2.y = this.m12;
         var2.z = this.m22;
         var2.w = this.m32;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return var2;
   }

   public Matrix4x3d setRow(int var1, Vector4dc var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2.x();
         this.m10 = var2.y();
         this.m20 = var2.z();
         this.m30 = var2.w();
         break;
      case 1:
         this.m01 = var2.x();
         this.m11 = var2.y();
         this.m21 = var2.z();
         this.m31 = var2.w();
         break;
      case 2:
         this.m02 = var2.x();
         this.m12 = var2.y();
         this.m22 = var2.z();
         this.m32 = var2.w();
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      this.properties = 0;
      return this;
   }

   public Vector3d getColumn(int var1, Vector3d var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         var2.x = this.m00;
         var2.y = this.m01;
         var2.z = this.m02;
         break;
      case 1:
         var2.x = this.m10;
         var2.y = this.m11;
         var2.z = this.m12;
         break;
      case 2:
         var2.x = this.m20;
         var2.y = this.m21;
         var2.z = this.m22;
         break;
      case 3:
         var2.x = this.m30;
         var2.y = this.m31;
         var2.z = this.m32;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return var2;
   }

   public Matrix4x3d setColumn(int var1, Vector3dc var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2.x();
         this.m01 = var2.y();
         this.m02 = var2.z();
         break;
      case 1:
         this.m10 = var2.x();
         this.m11 = var2.y();
         this.m12 = var2.z();
         break;
      case 2:
         this.m20 = var2.x();
         this.m21 = var2.y();
         this.m22 = var2.z();
         break;
      case 3:
         this.m30 = var2.x();
         this.m31 = var2.y();
         this.m32 = var2.z();
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      this.properties = 0;
      return this;
   }

   public Matrix4x3d normal() {
      return this.normal(this);
   }

   public Matrix4x3d normal(Matrix4x3d var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
      }
   }

   private Matrix4x3d normalOrthonormal(Matrix4x3d var1) {
      if (var1 != this) {
         var1.set((Matrix4x3dc)this);
      }

      return var1._properties(16);
   }

   private Matrix4x3d normalGeneric(Matrix4x3d var1) {
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
      var1.m30 = 0.0D;
      var1.m31 = 0.0D;
      var1.m32 = 0.0D;
      var1.properties = this.properties & -9;
      return var1;
   }

   public Matrix3d normal(Matrix3d var1) {
      return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
   }

   private Matrix3d normalOrthonormal(Matrix3d var1) {
      return var1.set((Matrix4x3dc)this);
   }

   private Matrix3d normalGeneric(Matrix3d var1) {
      double var2 = this.m00 * this.m11;
      double var4 = this.m01 * this.m10;
      double var6 = this.m02 * this.m10;
      double var8 = this.m00 * this.m12;
      double var10 = this.m01 * this.m12;
      double var12 = this.m02 * this.m11;
      double var14 = (var2 - var4) * this.m22 + (var6 - var8) * this.m21 + (var10 - var12) * this.m20;
      double var16 = 1.0D / var14;
      var1.m00((this.m11 * this.m22 - this.m21 * this.m12) * var16);
      var1.m01((this.m20 * this.m12 - this.m10 * this.m22) * var16);
      var1.m02((this.m10 * this.m21 - this.m20 * this.m11) * var16);
      var1.m10((this.m21 * this.m02 - this.m01 * this.m22) * var16);
      var1.m11((this.m00 * this.m22 - this.m20 * this.m02) * var16);
      var1.m12((this.m20 * this.m01 - this.m00 * this.m21) * var16);
      var1.m20((var10 - var12) * var16);
      var1.m21((var6 - var8) * var16);
      var1.m22((var2 - var4) * var16);
      return var1;
   }

   public Matrix4x3d cofactor3x3() {
      return this.cofactor3x3(this);
   }

   public Matrix3d cofactor3x3(Matrix3d var1) {
      var1.m00 = this.m11 * this.m22 - this.m21 * this.m12;
      var1.m01 = this.m20 * this.m12 - this.m10 * this.m22;
      var1.m02 = this.m10 * this.m21 - this.m20 * this.m11;
      var1.m10 = this.m21 * this.m02 - this.m01 * this.m22;
      var1.m11 = this.m00 * this.m22 - this.m20 * this.m02;
      var1.m12 = this.m20 * this.m01 - this.m00 * this.m21;
      var1.m20 = this.m01 * this.m12 - this.m02 * this.m11;
      var1.m21 = this.m02 * this.m10 - this.m00 * this.m12;
      var1.m22 = this.m00 * this.m11 - this.m01 * this.m10;
      return var1;
   }

   public Matrix4x3d cofactor3x3(Matrix4x3d var1) {
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
      var1.m30 = 0.0D;
      var1.m31 = 0.0D;
      var1.m32 = 0.0D;
      var1.properties = this.properties & -9;
      return var1;
   }

   public Matrix4x3d normalize3x3() {
      return this.normalize3x3(this);
   }

   public Matrix4x3d normalize3x3(Matrix4x3d var1) {
      double var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      double var4 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      double var6 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      var1.m00 = this.m00 * var2;
      var1.m01 = this.m01 * var2;
      var1.m02 = this.m02 * var2;
      var1.m10 = this.m10 * var4;
      var1.m11 = this.m11 * var4;
      var1.m12 = this.m12 * var4;
      var1.m20 = this.m20 * var6;
      var1.m21 = this.m21 * var6;
      var1.m22 = this.m22 * var6;
      return var1;
   }

   public Matrix3d normalize3x3(Matrix3d var1) {
      double var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      double var4 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      double var6 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      var1.m00(this.m00 * var2);
      var1.m01(this.m01 * var2);
      var1.m02(this.m02 * var2);
      var1.m10(this.m10 * var4);
      var1.m11(this.m11 * var4);
      var1.m12(this.m12 * var4);
      var1.m20(this.m20 * var6);
      var1.m21(this.m21 * var6);
      var1.m22(this.m22 * var6);
      return var1;
   }

   public Matrix4x3d reflect(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      if ((this.properties & 4) != 0) {
         return var9.reflection(var1, var3, var5, var7);
      } else {
         double var10 = var1 + var1;
         double var12 = var3 + var3;
         double var14 = var5 + var5;
         double var16 = var7 + var7;
         double var18 = 1.0D - var10 * var1;
         double var20 = -var10 * var3;
         double var22 = -var10 * var5;
         double var24 = -var12 * var1;
         double var26 = 1.0D - var12 * var3;
         double var28 = -var12 * var5;
         double var30 = -var14 * var1;
         double var32 = -var14 * var3;
         double var34 = 1.0D - var14 * var5;
         double var36 = -var16 * var1;
         double var38 = -var16 * var3;
         double var40 = -var16 * var5;
         var9.m30 = this.m00 * var36 + this.m10 * var38 + this.m20 * var40 + this.m30;
         var9.m31 = this.m01 * var36 + this.m11 * var38 + this.m21 * var40 + this.m31;
         var9.m32 = this.m02 * var36 + this.m12 * var38 + this.m22 * var40 + this.m32;
         double var42 = this.m00 * var18 + this.m10 * var20 + this.m20 * var22;
         double var44 = this.m01 * var18 + this.m11 * var20 + this.m21 * var22;
         double var46 = this.m02 * var18 + this.m12 * var20 + this.m22 * var22;
         double var48 = this.m00 * var24 + this.m10 * var26 + this.m20 * var28;
         double var50 = this.m01 * var24 + this.m11 * var26 + this.m21 * var28;
         double var52 = this.m02 * var24 + this.m12 * var26 + this.m22 * var28;
         var9.m20 = this.m00 * var30 + this.m10 * var32 + this.m20 * var34;
         var9.m21 = this.m01 * var30 + this.m11 * var32 + this.m21 * var34;
         var9.m22 = this.m02 * var30 + this.m12 * var32 + this.m22 * var34;
         var9.m00 = var42;
         var9.m01 = var44;
         var9.m02 = var46;
         var9.m10 = var48;
         var9.m11 = var50;
         var9.m12 = var52;
         var9.properties = this.properties & -13;
         return var9;
      }
   }

   public Matrix4x3d reflect(double var1, double var3, double var5, double var7) {
      return this.reflect(var1, var3, var5, var7, this);
   }

   public Matrix4x3d reflect(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.reflect(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix4x3d reflect(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
      double var14 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var16 = var1 * var14;
      double var18 = var3 * var14;
      double var20 = var5 * var14;
      return this.reflect(var16, var18, var20, -var16 * var7 - var18 * var9 - var20 * var11, var13);
   }

   public Matrix4x3d reflect(Vector3dc var1, Vector3dc var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3d reflect(Quaterniondc var1, Vector3dc var2) {
      return this.reflect(var1, var2, this);
   }

   public Matrix4x3d reflect(Quaterniondc var1, Vector3dc var2, Matrix4x3d var3) {
      double var4 = var1.x() + var1.x();
      double var6 = var1.y() + var1.y();
      double var8 = var1.z() + var1.z();
      double var10 = var1.x() * var8 + var1.w() * var6;
      double var12 = var1.y() * var8 - var1.w() * var4;
      double var14 = 1.0D - (var1.x() * var4 + var1.y() * var6);
      return this.reflect(var10, var12, var14, var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3d reflect(Vector3dc var1, Vector3dc var2, Matrix4x3d var3) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3d reflection(double var1, double var3, double var5, double var7) {
      double var9 = var1 + var1;
      double var11 = var3 + var3;
      double var13 = var5 + var5;
      double var15 = var7 + var7;
      this.m00 = 1.0D - var9 * var1;
      this.m01 = -var9 * var3;
      this.m02 = -var9 * var5;
      this.m10 = -var11 * var1;
      this.m11 = 1.0D - var11 * var3;
      this.m12 = -var11 * var5;
      this.m20 = -var13 * var1;
      this.m21 = -var13 * var3;
      this.m22 = 1.0D - var13 * var5;
      this.m30 = -var15 * var1;
      this.m31 = -var15 * var3;
      this.m32 = -var15 * var5;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d reflection(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = Math.invsqrt(var1 * var1 + var3 * var3 + var5 * var5);
      double var15 = var1 * var13;
      double var17 = var3 * var13;
      double var19 = var5 * var13;
      return this.reflection(var15, var17, var19, -var15 * var7 - var17 * var9 - var19 * var11);
   }

   public Matrix4x3d reflection(Vector3dc var1, Vector3dc var2) {
      return this.reflection(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3d reflection(Quaterniondc var1, Vector3dc var2) {
      double var3 = var1.x() + var1.x();
      double var5 = var1.y() + var1.y();
      double var7 = var1.z() + var1.z();
      double var9 = var1.x() * var7 + var1.w() * var5;
      double var11 = var1.y() * var7 - var1.w() * var3;
      double var13 = 1.0D - (var1.x() * var3 + var1.y() * var5);
      return this.reflection(var9, var11, var13, var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4x3d var14) {
      double var15 = 2.0D / (var3 - var1);
      double var17 = 2.0D / (var7 - var5);
      double var19 = (var13 ? 1.0D : 2.0D) / (var9 - var11);
      double var21 = (var1 + var3) / (var1 - var3);
      double var23 = (var7 + var5) / (var5 - var7);
      double var25 = (var13 ? var9 : var11 + var9) / (var9 - var11);
      var14.m30 = this.m00 * var21 + this.m10 * var23 + this.m20 * var25 + this.m30;
      var14.m31 = this.m01 * var21 + this.m11 * var23 + this.m21 * var25 + this.m31;
      var14.m32 = this.m02 * var21 + this.m12 * var23 + this.m22 * var25 + this.m32;
      var14.m00 = this.m00 * var15;
      var14.m01 = this.m01 * var15;
      var14.m02 = this.m02 * var15;
      var14.m10 = this.m10 * var17;
      var14.m11 = this.m11 * var17;
      var14.m12 = this.m12 * var17;
      var14.m20 = this.m20 * var19;
      var14.m21 = this.m21 * var19;
      var14.m22 = this.m22 * var19;
      var14.properties = this.properties & -29;
      return var14;
   }

   public Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
      return this.ortho(var1, var3, var5, var7, var9, var11, false, var13);
   }

   public Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      return this.ortho(var1, var3, var5, var7, var9, var11, var13, this);
   }

   public Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.ortho(var1, var3, var5, var7, var9, var11, false);
   }

   public Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4x3d var14) {
      double var15 = 2.0D / (var3 - var1);
      double var17 = 2.0D / (var7 - var5);
      double var19 = (var13 ? 1.0D : 2.0D) / (var11 - var9);
      double var21 = (var1 + var3) / (var1 - var3);
      double var23 = (var7 + var5) / (var5 - var7);
      double var25 = (var13 ? var9 : var11 + var9) / (var9 - var11);
      var14.m30 = this.m00 * var21 + this.m10 * var23 + this.m20 * var25 + this.m30;
      var14.m31 = this.m01 * var21 + this.m11 * var23 + this.m21 * var25 + this.m31;
      var14.m32 = this.m02 * var21 + this.m12 * var23 + this.m22 * var25 + this.m32;
      var14.m00 = this.m00 * var15;
      var14.m01 = this.m01 * var15;
      var14.m02 = this.m02 * var15;
      var14.m10 = this.m10 * var17;
      var14.m11 = this.m11 * var17;
      var14.m12 = this.m12 * var17;
      var14.m20 = this.m20 * var19;
      var14.m21 = this.m21 * var19;
      var14.m22 = this.m22 * var19;
      var14.properties = this.properties & -29;
      return var14;
   }

   public Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
      return this.orthoLH(var1, var3, var5, var7, var9, var11, false, var13);
   }

   public Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      return this.orthoLH(var1, var3, var5, var7, var9, var11, var13, this);
   }

   public Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.orthoLH(var1, var3, var5, var7, var9, var11, false);
   }

   public Matrix4x3d setOrtho(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      this.m00 = 2.0D / (var3 - var1);
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / (var7 - var5);
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = (var13 ? 1.0D : 2.0D) / (var9 - var11);
      this.m30 = (var3 + var1) / (var1 - var3);
      this.m31 = (var7 + var5) / (var5 - var7);
      this.m32 = (var13 ? var9 : var11 + var9) / (var9 - var11);
      this.properties = 0;
      return this;
   }

   public Matrix4x3d setOrtho(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.setOrtho(var1, var3, var5, var7, var9, var11, false);
   }

   public Matrix4x3d setOrthoLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
      this.m00 = 2.0D / (var3 - var1);
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / (var7 - var5);
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = (var13 ? 1.0D : 2.0D) / (var11 - var9);
      this.m30 = (var3 + var1) / (var1 - var3);
      this.m31 = (var7 + var5) / (var5 - var7);
      this.m32 = (var13 ? var9 : var11 + var9) / (var9 - var11);
      this.properties = 0;
      return this;
   }

   public Matrix4x3d setOrthoLH(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.setOrthoLH(var1, var3, var5, var7, var9, var11, false);
   }

   public Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7, boolean var9, Matrix4x3d var10) {
      double var11 = 2.0D / var1;
      double var13 = 2.0D / var3;
      double var15 = (var9 ? 1.0D : 2.0D) / (var5 - var7);
      double var17 = (var9 ? var5 : var7 + var5) / (var5 - var7);
      var10.m30 = this.m20 * var17 + this.m30;
      var10.m31 = this.m21 * var17 + this.m31;
      var10.m32 = this.m22 * var17 + this.m32;
      var10.m00 = this.m00 * var11;
      var10.m01 = this.m01 * var11;
      var10.m02 = this.m02 * var11;
      var10.m10 = this.m10 * var13;
      var10.m11 = this.m11 * var13;
      var10.m12 = this.m12 * var13;
      var10.m20 = this.m20 * var15;
      var10.m21 = this.m21 * var15;
      var10.m22 = this.m22 * var15;
      var10.properties = this.properties & -29;
      return var10;
   }

   public Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      return this.orthoSymmetric(var1, var3, var5, var7, false, var9);
   }

   public Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7, boolean var9) {
      return this.orthoSymmetric(var1, var3, var5, var7, var9, this);
   }

   public Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7) {
      return this.orthoSymmetric(var1, var3, var5, var7, false, this);
   }

   public Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7, boolean var9, Matrix4x3d var10) {
      double var11 = 2.0D / var1;
      double var13 = 2.0D / var3;
      double var15 = (var9 ? 1.0D : 2.0D) / (var7 - var5);
      double var17 = (var9 ? var5 : var7 + var5) / (var5 - var7);
      var10.m30 = this.m20 * var17 + this.m30;
      var10.m31 = this.m21 * var17 + this.m31;
      var10.m32 = this.m22 * var17 + this.m32;
      var10.m00 = this.m00 * var11;
      var10.m01 = this.m01 * var11;
      var10.m02 = this.m02 * var11;
      var10.m10 = this.m10 * var13;
      var10.m11 = this.m11 * var13;
      var10.m12 = this.m12 * var13;
      var10.m20 = this.m20 * var15;
      var10.m21 = this.m21 * var15;
      var10.m22 = this.m22 * var15;
      var10.properties = this.properties & -29;
      return var10;
   }

   public Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      return this.orthoSymmetricLH(var1, var3, var5, var7, false, var9);
   }

   public Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7, boolean var9) {
      return this.orthoSymmetricLH(var1, var3, var5, var7, var9, this);
   }

   public Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7) {
      return this.orthoSymmetricLH(var1, var3, var5, var7, false, this);
   }

   public Matrix4x3d setOrthoSymmetric(double var1, double var3, double var5, double var7, boolean var9) {
      this.m00 = 2.0D / var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / var3;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = (var9 ? 1.0D : 2.0D) / (var5 - var7);
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = (var9 ? var5 : var7 + var5) / (var5 - var7);
      this.properties = 0;
      return this;
   }

   public Matrix4x3d setOrthoSymmetric(double var1, double var3, double var5, double var7) {
      return this.setOrthoSymmetric(var1, var3, var5, var7, false);
   }

   public Matrix4x3d setOrthoSymmetricLH(double var1, double var3, double var5, double var7, boolean var9) {
      this.m00 = 2.0D / var1;
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / var3;
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = (var9 ? 1.0D : 2.0D) / (var7 - var5);
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = (var9 ? var5 : var7 + var5) / (var5 - var7);
      this.properties = 0;
      return this;
   }

   public Matrix4x3d setOrthoSymmetricLH(double var1, double var3, double var5, double var7) {
      return this.setOrthoSymmetricLH(var1, var3, var5, var7, false);
   }

   public Matrix4x3d ortho2D(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      double var10 = 2.0D / (var3 - var1);
      double var12 = 2.0D / (var7 - var5);
      double var14 = -(var3 + var1) / (var3 - var1);
      double var16 = -(var7 + var5) / (var7 - var5);
      var9.m30 = this.m00 * var14 + this.m10 * var16 + this.m30;
      var9.m31 = this.m01 * var14 + this.m11 * var16 + this.m31;
      var9.m32 = this.m02 * var14 + this.m12 * var16 + this.m32;
      var9.m00 = this.m00 * var10;
      var9.m01 = this.m01 * var10;
      var9.m02 = this.m02 * var10;
      var9.m10 = this.m10 * var12;
      var9.m11 = this.m11 * var12;
      var9.m12 = this.m12 * var12;
      var9.m20 = -this.m20;
      var9.m21 = -this.m21;
      var9.m22 = -this.m22;
      var9.properties = this.properties & -29;
      return var9;
   }

   public Matrix4x3d ortho2D(double var1, double var3, double var5, double var7) {
      return this.ortho2D(var1, var3, var5, var7, this);
   }

   public Matrix4x3d ortho2DLH(double var1, double var3, double var5, double var7, Matrix4x3d var9) {
      double var10 = 2.0D / (var3 - var1);
      double var12 = 2.0D / (var7 - var5);
      double var14 = -(var3 + var1) / (var3 - var1);
      double var16 = -(var7 + var5) / (var7 - var5);
      var9.m30 = this.m00 * var14 + this.m10 * var16 + this.m30;
      var9.m31 = this.m01 * var14 + this.m11 * var16 + this.m31;
      var9.m32 = this.m02 * var14 + this.m12 * var16 + this.m32;
      var9.m00 = this.m00 * var10;
      var9.m01 = this.m01 * var10;
      var9.m02 = this.m02 * var10;
      var9.m10 = this.m10 * var12;
      var9.m11 = this.m11 * var12;
      var9.m12 = this.m12 * var12;
      var9.m20 = this.m20;
      var9.m21 = this.m21;
      var9.m22 = this.m22;
      var9.properties = this.properties & -29;
      return var9;
   }

   public Matrix4x3d ortho2DLH(double var1, double var3, double var5, double var7) {
      return this.ortho2DLH(var1, var3, var5, var7, this);
   }

   public Matrix4x3d setOrtho2D(double var1, double var3, double var5, double var7) {
      this.m00 = 2.0D / (var3 - var1);
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / (var7 - var5);
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = -1.0D;
      this.m30 = -(var3 + var1) / (var3 - var1);
      this.m31 = -(var7 + var5) / (var7 - var5);
      this.m32 = 0.0D;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d setOrtho2DLH(double var1, double var3, double var5, double var7) {
      this.m00 = 2.0D / (var3 - var1);
      this.m01 = 0.0D;
      this.m02 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / (var7 - var5);
      this.m12 = 0.0D;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      this.m22 = 1.0D;
      this.m30 = -(var3 + var1) / (var3 - var1);
      this.m31 = -(var7 + var5) / (var7 - var5);
      this.m32 = 0.0D;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d lookAlong(Vector3dc var1, Vector3dc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4x3d lookAlong(Vector3dc var1, Vector3dc var2, Matrix4x3d var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
      if ((this.properties & 4) != 0) {
         return this.setLookAlong(var1, var3, var5, var7, var9, var11);
      } else {
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
         var13.m30 = this.m30;
         var13.m31 = this.m31;
         var13.m32 = this.m32;
         var13.properties = this.properties & -13;
         return var13;
      }
   }

   public Matrix4x3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.lookAlong(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix4x3d setLookAlong(Vector3dc var1, Vector3dc var2) {
      return this.setLookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3d setLookAlong(double var1, double var3, double var5, double var7, double var9, double var11) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d setLookAt(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      return this.setLookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d setLookAt(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - var7;
      double var21 = var3 - var9;
      double var23 = var5 - var11;
      double var25 = Math.invsqrt(var19 * var19 + var21 * var21 + var23 * var23);
      var19 *= var25;
      var21 *= var25;
      var23 *= var25;
      double var27 = var15 * var23 - var17 * var21;
      double var29 = var17 * var19 - var13 * var23;
      double var31 = var13 * var21 - var15 * var19;
      double var33 = Math.invsqrt(var27 * var27 + var29 * var29 + var31 * var31);
      var27 *= var33;
      var29 *= var33;
      var31 *= var33;
      double var35 = var21 * var31 - var23 * var29;
      double var37 = var23 * var27 - var19 * var31;
      double var39 = var19 * var29 - var21 * var27;
      this.m00 = var27;
      this.m01 = var35;
      this.m02 = var19;
      this.m10 = var29;
      this.m11 = var37;
      this.m12 = var21;
      this.m20 = var31;
      this.m21 = var39;
      this.m22 = var23;
      this.m30 = -(var27 * var1 + var29 * var3 + var31 * var5);
      this.m31 = -(var35 * var1 + var37 * var3 + var39 * var5);
      this.m32 = -(var19 * var1 + var21 * var3 + var23 * var5);
      this.properties = 16;
      return this;
   }

   public Matrix4x3d lookAt(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4x3d var4) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3d lookAt(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4x3d lookAt(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19) {
      return (this.properties & 4) != 0 ? var19.setLookAt(var1, var3, var5, var7, var9, var11, var13, var15, var17) : this.lookAtGeneric(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19);
   }

   private Matrix4x3d lookAtGeneric(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19) {
      double var20 = var1 - var7;
      double var22 = var3 - var9;
      double var24 = var5 - var11;
      double var26 = Math.invsqrt(var20 * var20 + var22 * var22 + var24 * var24);
      var20 *= var26;
      var22 *= var26;
      var24 *= var26;
      double var28 = var15 * var24 - var17 * var22;
      double var30 = var17 * var20 - var13 * var24;
      double var32 = var13 * var22 - var15 * var20;
      double var34 = Math.invsqrt(var28 * var28 + var30 * var30 + var32 * var32);
      var28 *= var34;
      var30 *= var34;
      var32 *= var34;
      double var36 = var22 * var32 - var24 * var30;
      double var38 = var24 * var28 - var20 * var32;
      double var40 = var20 * var30 - var22 * var28;
      double var60 = -(var28 * var1 + var30 * var3 + var32 * var5);
      double var62 = -(var36 * var1 + var38 * var3 + var40 * var5);
      double var64 = -(var20 * var1 + var22 * var3 + var24 * var5);
      var19.m30 = this.m00 * var60 + this.m10 * var62 + this.m20 * var64 + this.m30;
      var19.m31 = this.m01 * var60 + this.m11 * var62 + this.m21 * var64 + this.m31;
      var19.m32 = this.m02 * var60 + this.m12 * var62 + this.m22 * var64 + this.m32;
      double var66 = this.m00 * var28 + this.m10 * var36 + this.m20 * var20;
      double var68 = this.m01 * var28 + this.m11 * var36 + this.m21 * var20;
      double var70 = this.m02 * var28 + this.m12 * var36 + this.m22 * var20;
      double var72 = this.m00 * var30 + this.m10 * var38 + this.m20 * var22;
      double var74 = this.m01 * var30 + this.m11 * var38 + this.m21 * var22;
      double var76 = this.m02 * var30 + this.m12 * var38 + this.m22 * var22;
      var19.m20 = this.m00 * var32 + this.m10 * var40 + this.m20 * var24;
      var19.m21 = this.m01 * var32 + this.m11 * var40 + this.m21 * var24;
      var19.m22 = this.m02 * var32 + this.m12 * var40 + this.m22 * var24;
      var19.m00 = var66;
      var19.m01 = var68;
      var19.m02 = var70;
      var19.m10 = var72;
      var19.m11 = var74;
      var19.m12 = var76;
      var19.properties = this.properties & -13;
      return var19;
   }

   public Matrix4x3d lookAt(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      return this.lookAt(var1, var3, var5, var7, var9, var11, var13, var15, var17, this);
   }

   public Matrix4x3d setLookAtLH(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      return this.setLookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d setLookAtLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      double var25 = Math.invsqrt(var19 * var19 + var21 * var21 + var23 * var23);
      var19 *= var25;
      var21 *= var25;
      var23 *= var25;
      double var27 = var15 * var23 - var17 * var21;
      double var29 = var17 * var19 - var13 * var23;
      double var31 = var13 * var21 - var15 * var19;
      double var33 = Math.invsqrt(var27 * var27 + var29 * var29 + var31 * var31);
      var27 *= var33;
      var29 *= var33;
      var31 *= var33;
      double var35 = var21 * var31 - var23 * var29;
      double var37 = var23 * var27 - var19 * var31;
      double var39 = var19 * var29 - var21 * var27;
      this.m00 = var27;
      this.m01 = var35;
      this.m02 = var19;
      this.m10 = var29;
      this.m11 = var37;
      this.m12 = var21;
      this.m20 = var31;
      this.m21 = var39;
      this.m22 = var23;
      this.m30 = -(var27 * var1 + var29 * var3 + var31 * var5);
      this.m31 = -(var35 * var1 + var37 * var3 + var39 * var5);
      this.m32 = -(var19 * var1 + var21 * var3 + var23 * var5);
      this.properties = 16;
      return this;
   }

   public Matrix4x3d lookAtLH(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4x3d var4) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3d lookAtLH(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4x3d lookAtLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19) {
      return (this.properties & 4) != 0 ? var19.setLookAtLH(var1, var3, var5, var7, var9, var11, var13, var15, var17) : this.lookAtLHGeneric(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19);
   }

   private Matrix4x3d lookAtLHGeneric(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19) {
      double var20 = var7 - var1;
      double var22 = var9 - var3;
      double var24 = var11 - var5;
      double var26 = Math.invsqrt(var20 * var20 + var22 * var22 + var24 * var24);
      var20 *= var26;
      var22 *= var26;
      var24 *= var26;
      double var28 = var15 * var24 - var17 * var22;
      double var30 = var17 * var20 - var13 * var24;
      double var32 = var13 * var22 - var15 * var20;
      double var34 = Math.invsqrt(var28 * var28 + var30 * var30 + var32 * var32);
      var28 *= var34;
      var30 *= var34;
      var32 *= var34;
      double var36 = var22 * var32 - var24 * var30;
      double var38 = var24 * var28 - var20 * var32;
      double var40 = var20 * var30 - var22 * var28;
      double var60 = -(var28 * var1 + var30 * var3 + var32 * var5);
      double var62 = -(var36 * var1 + var38 * var3 + var40 * var5);
      double var64 = -(var20 * var1 + var22 * var3 + var24 * var5);
      var19.m30 = this.m00 * var60 + this.m10 * var62 + this.m20 * var64 + this.m30;
      var19.m31 = this.m01 * var60 + this.m11 * var62 + this.m21 * var64 + this.m31;
      var19.m32 = this.m02 * var60 + this.m12 * var62 + this.m22 * var64 + this.m32;
      double var66 = this.m00 * var28 + this.m10 * var36 + this.m20 * var20;
      double var68 = this.m01 * var28 + this.m11 * var36 + this.m21 * var20;
      double var70 = this.m02 * var28 + this.m12 * var36 + this.m22 * var20;
      double var72 = this.m00 * var30 + this.m10 * var38 + this.m20 * var22;
      double var74 = this.m01 * var30 + this.m11 * var38 + this.m21 * var22;
      double var76 = this.m02 * var30 + this.m12 * var38 + this.m22 * var22;
      var19.m20 = this.m00 * var32 + this.m10 * var40 + this.m20 * var24;
      var19.m21 = this.m01 * var32 + this.m11 * var40 + this.m21 * var24;
      var19.m22 = this.m02 * var32 + this.m12 * var40 + this.m22 * var24;
      var19.m00 = var66;
      var19.m01 = var68;
      var19.m02 = var70;
      var19.m10 = var72;
      var19.m11 = var74;
      var19.m12 = var76;
      var19.properties = this.properties & -13;
      return var19;
   }

   public Matrix4x3d lookAtLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      return this.lookAtLH(var1, var3, var5, var7, var9, var11, var13, var15, var17, this);
   }

   public Vector4d frustumPlane(int var1, Vector4d var2) {
      switch(var1) {
      case 0:
         var2.set(this.m00, this.m10, this.m20, 1.0D + this.m30).normalize();
         break;
      case 1:
         var2.set(-this.m00, -this.m10, -this.m20, 1.0D - this.m30).normalize();
         break;
      case 2:
         var2.set(this.m01, this.m11, this.m21, 1.0D + this.m31).normalize();
         break;
      case 3:
         var2.set(-this.m01, -this.m11, -this.m21, 1.0D - this.m31).normalize();
         break;
      case 4:
         var2.set(this.m02, this.m12, this.m22, 1.0D + this.m32).normalize();
         break;
      case 5:
         var2.set(-this.m02, -this.m12, -this.m22, 1.0D - this.m32).normalize();
         break;
      default:
         throw new IllegalArgumentException("which");
      }

      return var2;
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

   public Vector3d origin(Vector3d var1) {
      double var2 = this.m00 * this.m11 - this.m01 * this.m10;
      double var4 = this.m00 * this.m12 - this.m02 * this.m10;
      double var6 = this.m01 * this.m12 - this.m02 * this.m11;
      double var8 = this.m20 * this.m31 - this.m21 * this.m30;
      double var10 = this.m20 * this.m32 - this.m22 * this.m30;
      double var12 = this.m21 * this.m32 - this.m22 * this.m31;
      var1.x = -this.m10 * var12 + this.m11 * var10 - this.m12 * var8;
      var1.y = this.m00 * var12 - this.m01 * var10 + this.m02 * var8;
      var1.z = -this.m30 * var6 + this.m31 * var4 - this.m32 * var2;
      return var1;
   }

   public Matrix4x3d shadow(Vector4dc var1, double var2, double var4, double var6, double var8) {
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var2, var4, var6, var8, this);
   }

   public Matrix4x3d shadow(Vector4dc var1, double var2, double var4, double var6, double var8, Matrix4x3d var10) {
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var2, var4, var6, var8, var10);
   }

   public Matrix4x3d shadow(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      return this.shadow(var1, var3, var5, var7, var9, var11, var13, var15, this);
   }

   public Matrix4x3d shadow(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, Matrix4x3d var17) {
      double var18 = Math.invsqrt(var9 * var9 + var11 * var11 + var13 * var13);
      double var20 = var9 * var18;
      double var22 = var11 * var18;
      double var24 = var13 * var18;
      double var26 = var15 * var18;
      double var28 = var20 * var1 + var22 * var3 + var24 * var5 + var26 * var7;
      double var30 = var28 - var20 * var1;
      double var32 = -var20 * var3;
      double var34 = -var20 * var5;
      double var36 = -var20 * var7;
      double var38 = -var22 * var1;
      double var40 = var28 - var22 * var3;
      double var42 = -var22 * var5;
      double var44 = -var22 * var7;
      double var46 = -var24 * var1;
      double var48 = -var24 * var3;
      double var50 = var28 - var24 * var5;
      double var52 = -var24 * var7;
      double var54 = -var26 * var1;
      double var56 = -var26 * var3;
      double var58 = -var26 * var5;
      double var60 = var28 - var26 * var7;
      double var62 = this.m00 * var30 + this.m10 * var32 + this.m20 * var34 + this.m30 * var36;
      double var64 = this.m01 * var30 + this.m11 * var32 + this.m21 * var34 + this.m31 * var36;
      double var66 = this.m02 * var30 + this.m12 * var32 + this.m22 * var34 + this.m32 * var36;
      double var68 = this.m00 * var38 + this.m10 * var40 + this.m20 * var42 + this.m30 * var44;
      double var70 = this.m01 * var38 + this.m11 * var40 + this.m21 * var42 + this.m31 * var44;
      double var72 = this.m02 * var38 + this.m12 * var40 + this.m22 * var42 + this.m32 * var44;
      double var74 = this.m00 * var46 + this.m10 * var48 + this.m20 * var50 + this.m30 * var52;
      double var76 = this.m01 * var46 + this.m11 * var48 + this.m21 * var50 + this.m31 * var52;
      double var78 = this.m02 * var46 + this.m12 * var48 + this.m22 * var50 + this.m32 * var52;
      var17.m30 = this.m00 * var54 + this.m10 * var56 + this.m20 * var58 + this.m30 * var60;
      var17.m31 = this.m01 * var54 + this.m11 * var56 + this.m21 * var58 + this.m31 * var60;
      var17.m32 = this.m02 * var54 + this.m12 * var56 + this.m22 * var58 + this.m32 * var60;
      var17.m00 = var62;
      var17.m01 = var64;
      var17.m02 = var66;
      var17.m10 = var68;
      var17.m11 = var70;
      var17.m12 = var72;
      var17.m20 = var74;
      var17.m21 = var76;
      var17.m22 = var78;
      var17.properties = this.properties & -29;
      return var17;
   }

   public Matrix4x3d shadow(Vector4dc var1, Matrix4x3dc var2, Matrix4x3d var3) {
      double var4 = var2.m10();
      double var6 = var2.m11();
      double var8 = var2.m12();
      double var10 = -var4 * var2.m30() - var6 * var2.m31() - var8 * var2.m32();
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var4, var6, var8, var10, var3);
   }

   public Matrix4x3d shadow(Vector4dc var1, Matrix4x3dc var2) {
      return this.shadow(var1, var2, this);
   }

   public Matrix4x3d shadow(double var1, double var3, double var5, double var7, Matrix4x3dc var9, Matrix4x3d var10) {
      double var11 = var9.m10();
      double var13 = var9.m11();
      double var15 = var9.m12();
      double var17 = -var11 * var9.m30() - var13 * var9.m31() - var15 * var9.m32();
      return this.shadow(var1, var3, var5, var7, var11, var13, var15, var17, var10);
   }

   public Matrix4x3d shadow(double var1, double var3, double var5, double var7, Matrix4x3dc var9) {
      return this.shadow(var1, var3, var5, var7, var9, this);
   }

   public Matrix4x3d billboardCylindrical(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      double var4 = var2.x() - var1.x();
      double var6 = var2.y() - var1.y();
      double var8 = var2.z() - var1.z();
      double var10 = var3.y() * var8 - var3.z() * var6;
      double var12 = var3.z() * var4 - var3.x() * var8;
      double var14 = var3.x() * var6 - var3.y() * var4;
      double var16 = Math.invsqrt(var10 * var10 + var12 * var12 + var14 * var14);
      var10 *= var16;
      var12 *= var16;
      var14 *= var16;
      var4 = var12 * var3.z() - var14 * var3.y();
      var6 = var14 * var3.x() - var10 * var3.z();
      var8 = var10 * var3.y() - var12 * var3.x();
      double var18 = Math.invsqrt(var4 * var4 + var6 * var6 + var8 * var8);
      var4 *= var18;
      var6 *= var18;
      var8 *= var18;
      this.m00 = var10;
      this.m01 = var12;
      this.m02 = var14;
      this.m10 = var3.x();
      this.m11 = var3.y();
      this.m12 = var3.z();
      this.m20 = var4;
      this.m21 = var6;
      this.m22 = var8;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
   }

   public Matrix4x3d billboardSpherical(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      double var4 = var2.x() - var1.x();
      double var6 = var2.y() - var1.y();
      double var8 = var2.z() - var1.z();
      double var10 = Math.invsqrt(var4 * var4 + var6 * var6 + var8 * var8);
      var4 *= var10;
      var6 *= var10;
      var8 *= var10;
      double var12 = var3.y() * var8 - var3.z() * var6;
      double var14 = var3.z() * var4 - var3.x() * var8;
      double var16 = var3.x() * var6 - var3.y() * var4;
      double var18 = Math.invsqrt(var12 * var12 + var14 * var14 + var16 * var16);
      var12 *= var18;
      var14 *= var18;
      var16 *= var18;
      double var20 = var6 * var16 - var8 * var14;
      double var22 = var8 * var12 - var4 * var16;
      double var24 = var4 * var14 - var6 * var12;
      this.m00 = var12;
      this.m01 = var14;
      this.m02 = var16;
      this.m10 = var20;
      this.m11 = var22;
      this.m12 = var24;
      this.m20 = var4;
      this.m21 = var6;
      this.m22 = var8;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
   }

   public Matrix4x3d billboardSpherical(Vector3dc var1, Vector3dc var2) {
      double var3 = var2.x() - var1.x();
      double var5 = var2.y() - var1.y();
      double var7 = var2.z() - var1.z();
      double var9 = -var5;
      double var13 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7) + var7;
      double var15 = Math.invsqrt(var9 * var9 + var3 * var3 + var13 * var13);
      var9 *= var15;
      double var11 = var3 * var15;
      var13 *= var15;
      double var17 = (var9 + var9) * var9;
      double var19 = (var11 + var11) * var11;
      double var21 = (var9 + var9) * var11;
      double var23 = (var9 + var9) * var13;
      double var25 = (var11 + var11) * var13;
      this.m00 = 1.0D - var19;
      this.m01 = var21;
      this.m02 = -var25;
      this.m10 = var21;
      this.m11 = 1.0D - var17;
      this.m12 = var23;
      this.m20 = var25;
      this.m21 = -var23;
      this.m22 = 1.0D - var19 - var17;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
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
      var2 = Double.doubleToLongBits(this.m30);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m31);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m32);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      return var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4x3d)) {
         return false;
      } else {
         Matrix4x3d var2 = (Matrix4x3d)var1;
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
         } else if (Double.doubleToLongBits(this.m22) != Double.doubleToLongBits(var2.m22)) {
            return false;
         } else if (Double.doubleToLongBits(this.m30) != Double.doubleToLongBits(var2.m30)) {
            return false;
         } else if (Double.doubleToLongBits(this.m31) != Double.doubleToLongBits(var2.m31)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.m32) == Double.doubleToLongBits(var2.m32);
         }
      }
   }

   public boolean equals(Matrix4x3dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4x3d)) {
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
      } else if (!Runtime.equals(this.m22, var1.m22(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m30, var1.m30(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m31, var1.m31(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.m32, var1.m32(), var2);
      }
   }

   public Matrix4x3d pick(double var1, double var3, double var5, double var7, int[] var9, Matrix4x3d var10) {
      double var11 = (double)var9[2] / var5;
      double var13 = (double)var9[3] / var7;
      double var15 = ((double)var9[2] + 2.0D * ((double)var9[0] - var1)) / var5;
      double var17 = ((double)var9[3] + 2.0D * ((double)var9[1] - var3)) / var7;
      var10.m30 = this.m00 * var15 + this.m10 * var17 + this.m30;
      var10.m31 = this.m01 * var15 + this.m11 * var17 + this.m31;
      var10.m32 = this.m02 * var15 + this.m12 * var17 + this.m32;
      var10.m00 = this.m00 * var11;
      var10.m01 = this.m01 * var11;
      var10.m02 = this.m02 * var11;
      var10.m10 = this.m10 * var13;
      var10.m11 = this.m11 * var13;
      var10.m12 = this.m12 * var13;
      var10.properties = 0;
      return var10;
   }

   public Matrix4x3d pick(double var1, double var3, double var5, double var7, int[] var9) {
      return this.pick(var1, var3, var5, var7, var9, this);
   }

   public Matrix4x3d swap(Matrix4x3d var1) {
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
      var2 = this.m30;
      this.m30 = var1.m30;
      var1.m30 = var2;
      var2 = this.m31;
      this.m31 = var1.m31;
      var1.m31 = var2;
      var2 = this.m32;
      this.m32 = var1.m32;
      var1.m32 = var2;
      int var4 = this.properties;
      this.properties = var1.properties;
      var1.properties = var4;
      return this;
   }

   public Matrix4x3d arcball(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
      double var14 = this.m20 * -var1 + this.m30;
      double var16 = this.m21 * -var1 + this.m31;
      double var18 = this.m22 * -var1 + this.m32;
      double var20 = Math.sin(var9);
      double var22 = Math.cosFromSin(var20, var9);
      double var24 = this.m10 * var22 + this.m20 * var20;
      double var26 = this.m11 * var22 + this.m21 * var20;
      double var28 = this.m12 * var22 + this.m22 * var20;
      double var30 = this.m20 * var22 - this.m10 * var20;
      double var32 = this.m21 * var22 - this.m11 * var20;
      double var34 = this.m22 * var22 - this.m12 * var20;
      var20 = Math.sin(var11);
      var22 = Math.cosFromSin(var20, var11);
      double var36 = this.m00 * var22 - var30 * var20;
      double var38 = this.m01 * var22 - var32 * var20;
      double var40 = this.m02 * var22 - var34 * var20;
      double var42 = this.m00 * var20 + var30 * var22;
      double var44 = this.m01 * var20 + var32 * var22;
      double var46 = this.m02 * var20 + var34 * var22;
      var13.m30 = -var36 * var3 - var24 * var5 - var42 * var7 + var14;
      var13.m31 = -var38 * var3 - var26 * var5 - var44 * var7 + var16;
      var13.m32 = -var40 * var3 - var28 * var5 - var46 * var7 + var18;
      var13.m20 = var42;
      var13.m21 = var44;
      var13.m22 = var46;
      var13.m10 = var24;
      var13.m11 = var26;
      var13.m12 = var28;
      var13.m00 = var36;
      var13.m01 = var38;
      var13.m02 = var40;
      var13.properties = this.properties & -13;
      return var13;
   }

   public Matrix4x3d arcball(double var1, Vector3dc var3, double var4, double var6, Matrix4x3d var8) {
      return this.arcball(var1, var3.x(), var3.y(), var3.z(), var4, var6, var8);
   }

   public Matrix4x3d arcball(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.arcball(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix4x3d arcball(double var1, Vector3dc var3, double var4, double var6) {
      return this.arcball(var1, var3.x(), var3.y(), var3.z(), var4, var6, this);
   }

   public Matrix4x3d transformAab(double var1, double var3, double var5, double var7, double var9, double var11, Vector3d var13, Vector3d var14) {
      double var15 = this.m00 * var1;
      double var17 = this.m01 * var1;
      double var19 = this.m02 * var1;
      double var21 = this.m00 * var7;
      double var23 = this.m01 * var7;
      double var25 = this.m02 * var7;
      double var27 = this.m10 * var3;
      double var29 = this.m11 * var3;
      double var31 = this.m12 * var3;
      double var33 = this.m10 * var9;
      double var35 = this.m11 * var9;
      double var37 = this.m12 * var9;
      double var39 = this.m20 * var5;
      double var41 = this.m21 * var5;
      double var43 = this.m22 * var5;
      double var45 = this.m20 * var11;
      double var47 = this.m21 * var11;
      double var49 = this.m22 * var11;
      double var69;
      double var51;
      if (var15 < var21) {
         var51 = var15;
         var69 = var21;
      } else {
         var51 = var21;
         var69 = var15;
      }

      double var71;
      double var53;
      if (var17 < var23) {
         var53 = var17;
         var71 = var23;
      } else {
         var53 = var23;
         var71 = var17;
      }

      double var73;
      double var55;
      if (var19 < var25) {
         var55 = var19;
         var73 = var25;
      } else {
         var55 = var25;
         var73 = var19;
      }

      double var75;
      double var57;
      if (var27 < var33) {
         var57 = var27;
         var75 = var33;
      } else {
         var57 = var33;
         var75 = var27;
      }

      double var77;
      double var59;
      if (var29 < var35) {
         var59 = var29;
         var77 = var35;
      } else {
         var59 = var35;
         var77 = var29;
      }

      double var79;
      double var61;
      if (var31 < var37) {
         var61 = var31;
         var79 = var37;
      } else {
         var61 = var37;
         var79 = var31;
      }

      double var81;
      double var63;
      if (var39 < var45) {
         var63 = var39;
         var81 = var45;
      } else {
         var63 = var45;
         var81 = var39;
      }

      double var65;
      double var83;
      if (var41 < var47) {
         var65 = var41;
         var83 = var47;
      } else {
         var65 = var47;
         var83 = var41;
      }

      double var67;
      double var85;
      if (var43 < var49) {
         var67 = var43;
         var85 = var49;
      } else {
         var67 = var49;
         var85 = var43;
      }

      var13.x = var51 + var57 + var63 + this.m30;
      var13.y = var53 + var59 + var65 + this.m31;
      var13.z = var55 + var61 + var67 + this.m32;
      var14.x = var69 + var75 + var81 + this.m30;
      var14.y = var71 + var77 + var83 + this.m31;
      var14.z = var73 + var79 + var85 + this.m32;
      return this;
   }

   public Matrix4x3d transformAab(Vector3dc var1, Vector3dc var2, Vector3d var3, Vector3d var4) {
      return this.transformAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3, var4);
   }

   public Matrix4x3d lerp(Matrix4x3dc var1, double var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix4x3d lerp(Matrix4x3dc var1, double var2, Matrix4x3d var4) {
      var4.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var4.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var4.m02 = Math.fma(var1.m02() - this.m02, var2, this.m02);
      var4.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var4.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      var4.m12 = Math.fma(var1.m12() - this.m12, var2, this.m12);
      var4.m20 = Math.fma(var1.m20() - this.m20, var2, this.m20);
      var4.m21 = Math.fma(var1.m21() - this.m21, var2, this.m21);
      var4.m22 = Math.fma(var1.m22() - this.m22, var2, this.m22);
      var4.m30 = Math.fma(var1.m30() - this.m30, var2, this.m30);
      var4.m31 = Math.fma(var1.m31() - this.m31, var2, this.m31);
      var4.m32 = Math.fma(var1.m32() - this.m32, var2, this.m32);
      var4.properties = this.properties & var1.properties();
      return var4;
   }

   public Matrix4x3d rotateTowards(Vector3dc var1, Vector3dc var2, Matrix4x3d var3) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3d rotateTowards(Vector3dc var1, Vector3dc var2) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4x3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.rotateTowards(var1, var3, var5, var7, var9, var11, this);
   }

   public Matrix4x3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13) {
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
      var13.m30 = this.m30;
      var13.m31 = this.m31;
      var13.m32 = this.m32;
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
      var13.properties = this.properties & -13;
      return var13;
   }

   public Matrix4x3d rotationTowards(Vector3dc var1, Vector3dc var2) {
      return this.rotationTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3d rotationTowards(double var1, double var3, double var5, double var7, double var9, double var11) {
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
      this.m30 = 0.0D;
      this.m31 = 0.0D;
      this.m32 = 0.0D;
      this.properties = 16;
      return this;
   }

   public Matrix4x3d translationRotateTowards(Vector3dc var1, Vector3dc var2, Vector3dc var3) {
      return this.translationRotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3d translationRotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = Math.invsqrt(var7 * var7 + var9 * var9 + var11 * var11);
      double var21 = var7 * var19;
      double var23 = var9 * var19;
      double var25 = var11 * var19;
      double var27 = var15 * var25 - var17 * var23;
      double var29 = var17 * var21 - var13 * var25;
      double var31 = var13 * var23 - var15 * var21;
      double var33 = Math.invsqrt(var27 * var27 + var29 * var29 + var31 * var31);
      var27 *= var33;
      var29 *= var33;
      var31 *= var33;
      double var35 = var23 * var31 - var25 * var29;
      double var37 = var25 * var27 - var21 * var31;
      double var39 = var21 * var29 - var23 * var27;
      this.m00 = var27;
      this.m01 = var29;
      this.m02 = var31;
      this.m10 = var35;
      this.m11 = var37;
      this.m12 = var39;
      this.m20 = var21;
      this.m21 = var23;
      this.m22 = var25;
      this.m30 = var1;
      this.m31 = var3;
      this.m32 = var5;
      this.properties = 16;
      return this;
   }

   public Vector3d getEulerAnglesZYX(Vector3d var1) {
      var1.x = Math.atan2(this.m12, this.m22);
      var1.y = Math.atan2(-this.m02, Math.sqrt(this.m12 * this.m12 + this.m22 * this.m22));
      var1.z = Math.atan2(this.m01, this.m00);
      return var1;
   }

   public Matrix4x3d obliqueZ(double var1, double var3) {
      this.m20 += this.m00 * var1 + this.m10 * var3;
      this.m21 += this.m01 * var1 + this.m11 * var3;
      this.m22 += this.m02 * var1 + this.m12 * var3;
      this.properties = 0;
      return this;
   }

   public Matrix4x3d obliqueZ(double var1, double var3, Matrix4x3d var5) {
      var5.m00 = this.m00;
      var5.m01 = this.m01;
      var5.m02 = this.m02;
      var5.m10 = this.m10;
      var5.m11 = this.m11;
      var5.m12 = this.m12;
      var5.m20 = this.m00 * var1 + this.m10 * var3 + this.m20;
      var5.m21 = this.m01 * var1 + this.m11 * var3 + this.m21;
      var5.m22 = this.m02 * var1 + this.m12 * var3 + this.m22;
      var5.m30 = this.m30;
      var5.m31 = this.m31;
      var5.m32 = this.m32;
      var5.properties = 0;
      return var5;
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32);
   }
}
