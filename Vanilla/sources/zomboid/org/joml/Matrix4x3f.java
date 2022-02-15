package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix4x3f implements Externalizable, Matrix4x3fc {
   private static final long serialVersionUID = 1L;
   float m00;
   float m01;
   float m02;
   float m10;
   float m11;
   float m12;
   float m20;
   float m21;
   float m22;
   float m30;
   float m31;
   float m32;
   int properties;

   public Matrix4x3f() {
      this.m00 = 1.0F;
      this.m11 = 1.0F;
      this.m22 = 1.0F;
      this.properties = 28;
   }

   public Matrix4x3f(Matrix3fc var1) {
      this.set(var1);
   }

   public Matrix4x3f(Matrix4x3fc var1) {
      this.set(var1);
   }

   public Matrix4x3f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
      this.m30 = var10;
      this.m31 = var11;
      this.m32 = var12;
      this.determineProperties();
   }

   public Matrix4x3f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      this.determineProperties();
   }

   public Matrix4x3f(Vector3fc var1, Vector3fc var2, Vector3fc var3, Vector3fc var4) {
      this.set(var1, var2, var3, var4).determineProperties();
   }

   public Matrix4x3f assume(int var1) {
      this.properties = var1;
      return this;
   }

   public Matrix4x3f determineProperties() {
      int var1 = 0;
      if (this.m00 == 1.0F && this.m01 == 0.0F && this.m02 == 0.0F && this.m10 == 0.0F && this.m11 == 1.0F && this.m12 == 0.0F && this.m20 == 0.0F && this.m21 == 0.0F && this.m22 == 1.0F) {
         var1 |= 24;
         if (this.m30 == 0.0F && this.m31 == 0.0F && this.m32 == 0.0F) {
            var1 |= 4;
         }
      }

      this.properties = var1;
      return this;
   }

   public int properties() {
      return this.properties;
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

   public float m30() {
      return this.m30;
   }

   public float m31() {
      return this.m31;
   }

   public float m32() {
      return this.m32;
   }

   public Matrix4x3f m00(float var1) {
      this.m00 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m01(float var1) {
      this.m01 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m02(float var1) {
      this.m02 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m10(float var1) {
      this.m10 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m11(float var1) {
      this.m11 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m12(float var1) {
      this.m12 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m20(float var1) {
      this.m20 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m21(float var1) {
      this.m21 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m22(float var1) {
      this.m22 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4x3f m30(float var1) {
      this.m30 = var1;
      if (var1 != 0.0F) {
         this.properties &= -5;
      }

      return this;
   }

   public Matrix4x3f m31(float var1) {
      this.m31 = var1;
      if (var1 != 0.0F) {
         this.properties &= -5;
      }

      return this;
   }

   public Matrix4x3f m32(float var1) {
      this.m32 = var1;
      if (var1 != 0.0F) {
         this.properties &= -5;
      }

      return this;
   }

   Matrix4x3f _properties(int var1) {
      this.properties = var1;
      return this;
   }

   Matrix4x3f _m00(float var1) {
      this.m00 = var1;
      return this;
   }

   Matrix4x3f _m01(float var1) {
      this.m01 = var1;
      return this;
   }

   Matrix4x3f _m02(float var1) {
      this.m02 = var1;
      return this;
   }

   Matrix4x3f _m10(float var1) {
      this.m10 = var1;
      return this;
   }

   Matrix4x3f _m11(float var1) {
      this.m11 = var1;
      return this;
   }

   Matrix4x3f _m12(float var1) {
      this.m12 = var1;
      return this;
   }

   Matrix4x3f _m20(float var1) {
      this.m20 = var1;
      return this;
   }

   Matrix4x3f _m21(float var1) {
      this.m21 = var1;
      return this;
   }

   Matrix4x3f _m22(float var1) {
      this.m22 = var1;
      return this;
   }

   Matrix4x3f _m30(float var1) {
      this.m30 = var1;
      return this;
   }

   Matrix4x3f _m31(float var1) {
      this.m31 = var1;
      return this;
   }

   Matrix4x3f _m32(float var1) {
      this.m32 = var1;
      return this;
   }

   public Matrix4x3f identity() {
      if ((this.properties & 4) != 0) {
         return this;
      } else {
         MemUtil.INSTANCE.identity(this);
         this.properties = 28;
         return this;
      }
   }

   public Matrix4x3f set(Matrix4x3fc var1) {
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

   public Matrix4x3f set(Matrix4fc var1) {
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

   public Matrix4f get(Matrix4f var1) {
      return var1.set4x3((Matrix4x3fc)this);
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set4x3((Matrix4x3fc)this);
   }

   public Matrix4x3f set(Matrix3fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      return this.determineProperties();
   }

   public Matrix4x3f set(AxisAngle4f var1) {
      float var2 = var1.x;
      float var3 = var1.y;
      float var4 = var1.z;
      float var5 = var1.angle;
      float var6 = Math.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
      var6 = 1.0F / var6;
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f set(AxisAngle4d var1) {
      double var2 = var1.x;
      double var4 = var1.y;
      double var6 = var1.z;
      double var8 = var1.angle;
      double var10 = Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
      var10 = 1.0D / var10;
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f set(Quaternionfc var1) {
      return this.rotation(var1);
   }

   public Matrix4x3f set(Quaterniondc var1) {
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
      this.properties = 16;
      return this;
   }

   public Matrix4x3f set(Vector3fc var1, Vector3fc var2, Vector3fc var3, Vector3fc var4) {
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

   public Matrix4x3f set3x3(Matrix4x3fc var1) {
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

   public Matrix4x3f mul(Matrix4x3fc var1) {
      return this.mul(var1, this);
   }

   public Matrix4x3f mul(Matrix4x3fc var1, Matrix4x3f var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4x3fc)this);
      } else {
         return (this.properties & 8) != 0 ? this.mulTranslation(var1, var2) : this.mulGeneric(var1, var2);
      }
   }

   private Matrix4x3f mulGeneric(Matrix4x3fc var1, Matrix4x3f var2) {
      float var3 = this.m00;
      float var4 = this.m01;
      float var5 = this.m02;
      float var6 = this.m10;
      float var7 = this.m11;
      float var8 = this.m12;
      float var9 = this.m20;
      float var10 = this.m21;
      float var11 = this.m22;
      float var12 = var1.m00();
      float var13 = var1.m01();
      float var14 = var1.m02();
      float var15 = var1.m10();
      float var16 = var1.m11();
      float var17 = var1.m12();
      float var18 = var1.m20();
      float var19 = var1.m21();
      float var20 = var1.m22();
      float var21 = var1.m30();
      float var22 = var1.m31();
      float var23 = var1.m32();
      return var2._m00(Math.fma(var3, var12, Math.fma(var6, var13, var9 * var14)))._m01(Math.fma(var4, var12, Math.fma(var7, var13, var10 * var14)))._m02(Math.fma(var5, var12, Math.fma(var8, var13, var11 * var14)))._m10(Math.fma(var3, var15, Math.fma(var6, var16, var9 * var17)))._m11(Math.fma(var4, var15, Math.fma(var7, var16, var10 * var17)))._m12(Math.fma(var5, var15, Math.fma(var8, var16, var11 * var17)))._m20(Math.fma(var3, var18, Math.fma(var6, var19, var9 * var20)))._m21(Math.fma(var4, var18, Math.fma(var7, var19, var10 * var20)))._m22(Math.fma(var5, var18, Math.fma(var8, var19, var11 * var20)))._m30(Math.fma(var3, var21, Math.fma(var6, var22, Math.fma(var9, var23, this.m30))))._m31(Math.fma(var4, var21, Math.fma(var7, var22, Math.fma(var10, var23, this.m31))))._m32(Math.fma(var5, var21, Math.fma(var8, var22, Math.fma(var11, var23, this.m32))))._properties(this.properties & var1.properties() & 16);
   }

   public Matrix4x3f mulTranslation(Matrix4x3fc var1, Matrix4x3f var2) {
      return var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30() + this.m30)._m31(var1.m31() + this.m31)._m32(var1.m32() + this.m32)._properties(var1.properties() & 16);
   }

   public Matrix4x3f mulOrtho(Matrix4x3fc var1) {
      return this.mulOrtho(var1, this);
   }

   public Matrix4x3f mulOrtho(Matrix4x3fc var1, Matrix4x3f var2) {
      float var3 = this.m00 * var1.m00();
      float var4 = this.m11 * var1.m01();
      float var5 = this.m22 * var1.m02();
      float var6 = this.m00 * var1.m10();
      float var7 = this.m11 * var1.m11();
      float var8 = this.m22 * var1.m12();
      float var9 = this.m00 * var1.m20();
      float var10 = this.m11 * var1.m21();
      float var11 = this.m22 * var1.m22();
      float var12 = this.m00 * var1.m30() + this.m30;
      float var13 = this.m11 * var1.m31() + this.m31;
      float var14 = this.m22 * var1.m32() + this.m32;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m02 = var5;
      var2.m10 = var6;
      var2.m11 = var7;
      var2.m12 = var8;
      var2.m20 = var9;
      var2.m21 = var10;
      var2.m22 = var11;
      var2.m30 = var12;
      var2.m31 = var13;
      var2.m32 = var14;
      var2.properties = this.properties & var1.properties() & 16;
      return var2;
   }

   public Matrix4x3f fma(Matrix4x3fc var1, float var2) {
      return this.fma(var1, var2, this);
   }

   public Matrix4x3f fma(Matrix4x3fc var1, float var2, Matrix4x3f var3) {
      var3._m00(Math.fma(var1.m00(), var2, this.m00))._m01(Math.fma(var1.m01(), var2, this.m01))._m02(Math.fma(var1.m02(), var2, this.m02))._m10(Math.fma(var1.m10(), var2, this.m10))._m11(Math.fma(var1.m11(), var2, this.m11))._m12(Math.fma(var1.m12(), var2, this.m12))._m20(Math.fma(var1.m20(), var2, this.m20))._m21(Math.fma(var1.m21(), var2, this.m21))._m22(Math.fma(var1.m22(), var2, this.m22))._m30(Math.fma(var1.m30(), var2, this.m30))._m31(Math.fma(var1.m31(), var2, this.m31))._m32(Math.fma(var1.m32(), var2, this.m32))._properties(0);
      return var3;
   }

   public Matrix4x3f add(Matrix4x3fc var1) {
      return this.add(var1, this);
   }

   public Matrix4x3f add(Matrix4x3fc var1, Matrix4x3f var2) {
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

   public Matrix4x3f sub(Matrix4x3fc var1) {
      return this.sub(var1, this);
   }

   public Matrix4x3f sub(Matrix4x3fc var1, Matrix4x3f var2) {
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

   public Matrix4x3f mulComponentWise(Matrix4x3fc var1) {
      return this.mulComponentWise(var1, this);
   }

   public Matrix4x3f mulComponentWise(Matrix4x3fc var1, Matrix4x3f var2) {
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

   public Matrix4x3f set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.m00 = var1;
      this.m01 = var2;
      this.m02 = var3;
      this.m10 = var4;
      this.m11 = var5;
      this.m12 = var6;
      this.m20 = var7;
      this.m21 = var8;
      this.m22 = var9;
      this.m30 = var10;
      this.m31 = var11;
      this.m32 = var12;
      return this.determineProperties();
   }

   public Matrix4x3f set(float[] var1, int var2) {
      MemUtil.INSTANCE.copy(var1, var2, this);
      return this.determineProperties();
   }

   public Matrix4x3f set(float[] var1) {
      return this.set(var1, 0);
   }

   public Matrix4x3f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4x3f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this.determineProperties();
      }
   }

   public float determinant() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public Matrix4x3f invert(Matrix4x3f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.invertOrthonormal(var1) : this.invertGeneric(var1);
      }
   }

   private Matrix4x3f invertGeneric(Matrix4x3f var1) {
      float var2 = this.m00 * this.m11;
      float var3 = this.m01 * this.m10;
      float var4 = this.m02 * this.m10;
      float var5 = this.m00 * this.m12;
      float var6 = this.m01 * this.m12;
      float var7 = this.m02 * this.m11;
      float var8 = 1.0F / ((var2 - var3) * this.m22 + (var4 - var5) * this.m21 + (var6 - var7) * this.m20);
      float var9 = this.m10 * this.m22;
      float var10 = this.m10 * this.m21;
      float var11 = this.m11 * this.m22;
      float var12 = this.m11 * this.m20;
      float var13 = this.m12 * this.m21;
      float var14 = this.m12 * this.m20;
      float var15 = this.m20 * this.m02;
      float var16 = this.m20 * this.m01;
      float var17 = this.m21 * this.m02;
      float var18 = this.m21 * this.m00;
      float var19 = this.m22 * this.m01;
      float var20 = this.m22 * this.m00;
      float var21 = (var11 - var13) * var8;
      float var22 = (var17 - var19) * var8;
      float var23 = (var6 - var7) * var8;
      float var24 = (var14 - var9) * var8;
      float var25 = (var20 - var15) * var8;
      float var26 = (var4 - var5) * var8;
      float var27 = (var10 - var12) * var8;
      float var28 = (var16 - var18) * var8;
      float var29 = (var2 - var3) * var8;
      float var30 = (var9 * this.m31 - var10 * this.m32 + var12 * this.m32 - var11 * this.m30 + var13 * this.m30 - var14 * this.m31) * var8;
      float var31 = (var15 * this.m31 - var16 * this.m32 + var18 * this.m32 - var17 * this.m30 + var19 * this.m30 - var20 * this.m31) * var8;
      float var32 = (var7 * this.m30 - var6 * this.m30 + var5 * this.m31 - var4 * this.m31 + var3 * this.m32 - var2 * this.m32) * var8;
      var1.m00 = var21;
      var1.m01 = var22;
      var1.m02 = var23;
      var1.m10 = var24;
      var1.m11 = var25;
      var1.m12 = var26;
      var1.m20 = var27;
      var1.m21 = var28;
      var1.m22 = var29;
      var1.m30 = var30;
      var1.m31 = var31;
      var1.m32 = var32;
      var1.properties = 0;
      return var1;
   }

   private Matrix4x3f invertOrthonormal(Matrix4x3f var1) {
      float var2 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
      float var3 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
      float var4 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
      float var5 = this.m01;
      float var6 = this.m02;
      float var7 = this.m12;
      var1.m00 = this.m00;
      var1.m01 = this.m10;
      var1.m02 = this.m20;
      var1.m10 = var5;
      var1.m11 = this.m11;
      var1.m12 = this.m21;
      var1.m20 = var6;
      var1.m21 = var7;
      var1.m22 = this.m22;
      var1.m30 = var2;
      var1.m31 = var3;
      var1.m32 = var4;
      var1.properties = 16;
      return var1;
   }

   public Matrix4f invert(Matrix4f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.invertOrthonormal(var1) : this.invertGeneric(var1);
      }
   }

   private Matrix4f invertGeneric(Matrix4f var1) {
      float var2 = this.m00 * this.m11;
      float var3 = this.m01 * this.m10;
      float var4 = this.m02 * this.m10;
      float var5 = this.m00 * this.m12;
      float var6 = this.m01 * this.m12;
      float var7 = this.m02 * this.m11;
      float var8 = 1.0F / ((var2 - var3) * this.m22 + (var4 - var5) * this.m21 + (var6 - var7) * this.m20);
      float var9 = this.m10 * this.m22;
      float var10 = this.m10 * this.m21;
      float var11 = this.m11 * this.m22;
      float var12 = this.m11 * this.m20;
      float var13 = this.m12 * this.m21;
      float var14 = this.m12 * this.m20;
      float var15 = this.m20 * this.m02;
      float var16 = this.m20 * this.m01;
      float var17 = this.m21 * this.m02;
      float var18 = this.m21 * this.m00;
      float var19 = this.m22 * this.m01;
      float var20 = this.m22 * this.m00;
      float var21 = (var11 - var13) * var8;
      float var22 = (var17 - var19) * var8;
      float var23 = (var6 - var7) * var8;
      float var24 = (var14 - var9) * var8;
      float var25 = (var20 - var15) * var8;
      float var26 = (var4 - var5) * var8;
      float var27 = (var10 - var12) * var8;
      float var28 = (var16 - var18) * var8;
      float var29 = (var2 - var3) * var8;
      float var30 = (var9 * this.m31 - var10 * this.m32 + var12 * this.m32 - var11 * this.m30 + var13 * this.m30 - var14 * this.m31) * var8;
      float var31 = (var15 * this.m31 - var16 * this.m32 + var18 * this.m32 - var17 * this.m30 + var19 * this.m30 - var20 * this.m31) * var8;
      float var32 = (var7 * this.m30 - var6 * this.m30 + var5 * this.m31 - var4 * this.m31 + var3 * this.m32 - var2 * this.m32) * var8;
      var1.m00 = var21;
      var1.m01 = var22;
      var1.m02 = var23;
      var1.m03 = 0.0F;
      var1.m10 = var24;
      var1.m11 = var25;
      var1.m12 = var26;
      var1.m13 = 0.0F;
      var1.m20 = var27;
      var1.m21 = var28;
      var1.m22 = var29;
      var1.m23 = 0.0F;
      var1.m30 = var30;
      var1.m31 = var31;
      var1.m32 = var32;
      var1.m33 = 0.0F;
      var1.properties = 0;
      return var1;
   }

   private Matrix4f invertOrthonormal(Matrix4f var1) {
      float var2 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
      float var3 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
      float var4 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
      float var5 = this.m01;
      float var6 = this.m02;
      float var7 = this.m12;
      var1.m00 = this.m00;
      var1.m01 = this.m10;
      var1.m02 = this.m20;
      var1.m03 = 0.0F;
      var1.m10 = var5;
      var1.m11 = this.m11;
      var1.m12 = this.m21;
      var1.m13 = 0.0F;
      var1.m20 = var6;
      var1.m21 = var7;
      var1.m22 = this.m22;
      var1.m23 = 0.0F;
      var1.m30 = var2;
      var1.m31 = var3;
      var1.m32 = var4;
      var1.m33 = 0.0F;
      var1.properties = 16;
      return var1;
   }

   public Matrix4x3f invert() {
      return this.invert(this);
   }

   public Matrix4x3f invertOrtho(Matrix4x3f var1) {
      float var2 = 1.0F / this.m00;
      float var3 = 1.0F / this.m11;
      float var4 = 1.0F / this.m22;
      var1.set(var2, 0.0F, 0.0F, 0.0F, var3, 0.0F, 0.0F, 0.0F, var4, -this.m30 * var2, -this.m31 * var3, -this.m32 * var4);
      var1.properties = 0;
      return var1;
   }

   public Matrix4x3f invertOrtho() {
      return this.invertOrtho(this);
   }

   public Matrix4x3f transpose3x3() {
      return this.transpose3x3(this);
   }

   public Matrix4x3f transpose3x3(Matrix4x3f var1) {
      float var2 = this.m00;
      float var3 = this.m10;
      float var4 = this.m20;
      float var5 = this.m01;
      float var6 = this.m11;
      float var7 = this.m21;
      float var8 = this.m02;
      float var9 = this.m12;
      float var10 = this.m22;
      var1.m00 = var2;
      var1.m01 = var3;
      var1.m02 = var4;
      var1.m10 = var5;
      var1.m11 = var6;
      var1.m12 = var7;
      var1.m20 = var8;
      var1.m21 = var9;
      var1.m22 = var10;
      var1.properties = this.properties;
      return var1;
   }

   public Matrix3f transpose3x3(Matrix3f var1) {
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

   public Matrix4x3f translation(float var1, float var2, float var3) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this.m30 = var1;
      this.m31 = var2;
      this.m32 = var3;
      this.properties = 24;
      return this;
   }

   public Matrix4x3f translation(Vector3fc var1) {
      return this.translation(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3f setTranslation(float var1, float var2, float var3) {
      this.m30 = var1;
      this.m31 = var2;
      this.m32 = var3;
      this.properties &= -5;
      return this;
   }

   public Matrix4x3f setTranslation(Vector3fc var1) {
      return this.setTranslation(var1.x(), var1.y(), var1.z());
   }

   public Vector3f getTranslation(Vector3f var1) {
      var1.x = this.m30;
      var1.y = this.m31;
      var1.z = this.m32;
      return var1;
   }

   public Vector3f getScale(Vector3f var1) {
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
      String var10000 = Runtime.format((double)this.m00, var1);
      return var10000 + " " + Runtime.format((double)this.m10, var1) + " " + Runtime.format((double)this.m20, var1) + " " + Runtime.format((double)this.m30, var1) + "\n" + Runtime.format((double)this.m01, var1) + " " + Runtime.format((double)this.m11, var1) + " " + Runtime.format((double)this.m21, var1) + " " + Runtime.format((double)this.m31, var1) + "\n" + Runtime.format((double)this.m02, var1) + " " + Runtime.format((double)this.m12, var1) + " " + Runtime.format((double)this.m22, var1) + " " + Runtime.format((double)this.m32, var1) + "\n";
   }

   public Matrix4x3f get(Matrix4x3f var1) {
      return var1.set((Matrix4x3fc)this);
   }

   public Matrix4x3d get(Matrix4x3d var1) {
      return var1.set((Matrix4x3fc)this);
   }

   public AxisAngle4f getRotation(AxisAngle4f var1) {
      return var1.set((Matrix4x3fc)this);
   }

   public AxisAngle4d getRotation(AxisAngle4d var1) {
      return var1.set((Matrix4x3fc)this);
   }

   public Quaternionf getUnnormalizedRotation(Quaternionf var1) {
      return var1.setFromUnnormalized((Matrix4x3fc)this);
   }

   public Quaternionf getNormalizedRotation(Quaternionf var1) {
      return var1.setFromNormalized((Matrix4x3fc)this);
   }

   public Quaterniond getUnnormalizedRotation(Quaterniond var1) {
      return var1.setFromUnnormalized((Matrix4x3fc)this);
   }

   public Quaterniond getNormalizedRotation(Quaterniond var1) {
      return var1.setFromNormalized((Matrix4x3fc)this);
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

   public Matrix4x3fc getToAddress(long var1) {
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

   public float[] get4x4(float[] var1, int var2) {
      MemUtil.INSTANCE.copy4x4(this, var1, var2);
      return var1;
   }

   public float[] get4x4(float[] var1) {
      return this.get4x4(var1, 0);
   }

   public FloatBuffer get4x4(FloatBuffer var1) {
      return this.get4x4(var1.position(), var1);
   }

   public FloatBuffer get4x4(int var1, FloatBuffer var2) {
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

   public float[] getTransposed(float[] var1, int var2) {
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

   public float[] getTransposed(float[] var1) {
      return this.getTransposed(var1, 0);
   }

   public Matrix4x3f zero() {
      MemUtil.INSTANCE.zero(this);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f scaling(float var1) {
      return this.scaling(var1, var1, var1);
   }

   public Matrix4x3f scaling(float var1, float var2, float var3) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this.m00 = var1;
      this.m11 = var2;
      this.m22 = var3;
      boolean var4 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      this.properties = var4 ? 16 : 0;
      return this;
   }

   public Matrix4x3f scaling(Vector3fc var1) {
      return this.scaling(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3f rotation(float var1, Vector3fc var2) {
      return this.rotation(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f rotation(AxisAngle4f var1) {
      return this.rotation(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4x3f rotation(float var1, float var2, float var3, float var4) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotationX(var2 * var1);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotationY(var3 * var1);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotationZ(var4 * var1) : this.rotationInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4x3f rotationInternal(float var1, float var2, float var3, float var4) {
      float var5 = Math.sin(var1);
      float var6 = Math.cosFromSin(var5, var1);
      float var7 = 1.0F - var6;
      float var8 = var2 * var3;
      float var9 = var2 * var4;
      float var10 = var3 * var4;
      this.m00 = var6 + var2 * var2 * var7;
      this.m01 = var8 * var7 + var4 * var5;
      this.m02 = var9 * var7 - var3 * var5;
      this.m10 = var8 * var7 - var4 * var5;
      this.m11 = var6 + var3 * var3 * var7;
      this.m12 = var10 * var7 + var2 * var5;
      this.m20 = var9 * var7 + var3 * var5;
      this.m21 = var10 * var7 - var2 * var5;
      this.m22 = var6 + var4 * var4 * var7;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationX(float var1) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationY(float var1) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationZ(float var1) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationXYZ(float var1, float var2, float var3) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationZYX(float var1, float var2, float var3) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotationYXZ(float var1, float var2, float var3) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f setRotationXYZ(float var1, float var2, float var3) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3f setRotationZYX(float var1, float var2, float var3) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3f setRotationYXZ(float var1, float var2, float var3) {
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
      this.properties &= -13;
      return this;
   }

   public Matrix4x3f rotation(Quaternionfc var1) {
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
      this._m00(var2 + var3 - var5 - var4);
      this._m01(var9 + var7);
      this._m02(var11 - var13);
      this._m10(var9 - var7);
      this._m11(var4 - var5 + var2 - var3);
      this._m12(var15 + var17);
      this._m20(var13 + var11);
      this._m21(var15 - var17);
      this._m22(var5 - var4 - var3 + var2);
      this._m30(0.0F);
      this._m31(0.0F);
      this._m32(0.0F);
      this.properties = 16;
      return this;
   }

   public Matrix4x3f translationRotateScale(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      float var11 = var4 + var4;
      float var12 = var5 + var5;
      float var13 = var6 + var6;
      float var14 = var11 * var4;
      float var15 = var12 * var5;
      float var16 = var13 * var6;
      float var17 = var11 * var5;
      float var18 = var11 * var6;
      float var19 = var11 * var7;
      float var20 = var12 * var6;
      float var21 = var12 * var7;
      float var22 = var13 * var7;
      this.m00 = var8 - (var15 + var16) * var8;
      this.m01 = (var17 + var22) * var8;
      this.m02 = (var18 - var21) * var8;
      this.m10 = (var17 - var22) * var9;
      this.m11 = var9 - (var16 + var14) * var9;
      this.m12 = (var20 + var19) * var9;
      this.m20 = (var18 + var21) * var10;
      this.m21 = (var20 - var19) * var10;
      this.m22 = var10 - (var15 + var14) * var10;
      this.m30 = var1;
      this.m31 = var2;
      this.m32 = var3;
      this.properties = 0;
      return this;
   }

   public Matrix4x3f translationRotateScale(Vector3fc var1, Quaternionfc var2, Vector3fc var3) {
      return this.translationRotateScale(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3f translationRotateScaleMul(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, Matrix4x3f var11) {
      float var12 = var4 + var4;
      float var13 = var5 + var5;
      float var14 = var6 + var6;
      float var15 = var12 * var4;
      float var16 = var13 * var5;
      float var17 = var14 * var6;
      float var18 = var12 * var5;
      float var19 = var12 * var6;
      float var20 = var12 * var7;
      float var21 = var13 * var6;
      float var22 = var13 * var7;
      float var23 = var14 * var7;
      float var24 = var8 - (var16 + var17) * var8;
      float var25 = (var18 + var23) * var8;
      float var26 = (var19 - var22) * var8;
      float var27 = (var18 - var23) * var9;
      float var28 = var9 - (var17 + var15) * var9;
      float var29 = (var21 + var20) * var9;
      float var30 = (var19 + var22) * var10;
      float var31 = (var21 - var20) * var10;
      float var32 = var10 - (var16 + var15) * var10;
      float var33 = var24 * var11.m00 + var27 * var11.m01 + var30 * var11.m02;
      float var34 = var25 * var11.m00 + var28 * var11.m01 + var31 * var11.m02;
      this.m02 = var26 * var11.m00 + var29 * var11.m01 + var32 * var11.m02;
      this.m00 = var33;
      this.m01 = var34;
      float var35 = var24 * var11.m10 + var27 * var11.m11 + var30 * var11.m12;
      float var36 = var25 * var11.m10 + var28 * var11.m11 + var31 * var11.m12;
      this.m12 = var26 * var11.m10 + var29 * var11.m11 + var32 * var11.m12;
      this.m10 = var35;
      this.m11 = var36;
      float var37 = var24 * var11.m20 + var27 * var11.m21 + var30 * var11.m22;
      float var38 = var25 * var11.m20 + var28 * var11.m21 + var31 * var11.m22;
      this.m22 = var26 * var11.m20 + var29 * var11.m21 + var32 * var11.m22;
      this.m20 = var37;
      this.m21 = var38;
      float var39 = var24 * var11.m30 + var27 * var11.m31 + var30 * var11.m32 + var1;
      float var40 = var25 * var11.m30 + var28 * var11.m31 + var31 * var11.m32 + var2;
      this.m32 = var26 * var11.m30 + var29 * var11.m31 + var32 * var11.m32 + var3;
      this.m30 = var39;
      this.m31 = var40;
      this.properties = 0;
      return this;
   }

   public Matrix4x3f translationRotateScaleMul(Vector3fc var1, Quaternionfc var2, Vector3fc var3, Matrix4x3f var4) {
      return this.translationRotateScaleMul(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3f translationRotate(float var1, float var2, float var3, Quaternionfc var4) {
      float var5 = var4.x() + var4.x();
      float var6 = var4.y() + var4.y();
      float var7 = var4.z() + var4.z();
      float var8 = var5 * var4.x();
      float var9 = var6 * var4.y();
      float var10 = var7 * var4.z();
      float var11 = var5 * var4.y();
      float var12 = var5 * var4.z();
      float var13 = var5 * var4.w();
      float var14 = var6 * var4.z();
      float var15 = var6 * var4.w();
      float var16 = var7 * var4.w();
      this.m00 = 1.0F - (var9 + var10);
      this.m01 = var11 + var16;
      this.m02 = var12 - var15;
      this.m10 = var11 - var16;
      this.m11 = 1.0F - (var10 + var8);
      this.m12 = var14 + var13;
      this.m20 = var12 + var15;
      this.m21 = var14 - var13;
      this.m22 = 1.0F - (var9 + var8);
      this.m30 = var1;
      this.m31 = var2;
      this.m32 = var3;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f translationRotateMul(float var1, float var2, float var3, Quaternionfc var4, Matrix4x3fc var5) {
      return this.translationRotateMul(var1, var2, var3, var4.x(), var4.y(), var4.z(), var4.w(), var5);
   }

   public Matrix4x3f translationRotateMul(float var1, float var2, float var3, float var4, float var5, float var6, float var7, Matrix4x3fc var8) {
      float var9 = var7 * var7;
      float var10 = var4 * var4;
      float var11 = var5 * var5;
      float var12 = var6 * var6;
      float var13 = var6 * var7;
      float var14 = var4 * var5;
      float var15 = var4 * var6;
      float var16 = var5 * var7;
      float var17 = var5 * var6;
      float var18 = var4 * var7;
      float var19 = var9 + var10 - var12 - var11;
      float var20 = var14 + var13 + var13 + var14;
      float var21 = var15 - var16 + var15 - var16;
      float var22 = -var13 + var14 - var13 + var14;
      float var23 = var11 - var12 + var9 - var10;
      float var24 = var17 + var17 + var18 + var18;
      float var25 = var16 + var15 + var15 + var16;
      float var26 = var17 + var17 - var18 - var18;
      float var27 = var12 - var11 - var10 + var9;
      this.m00 = var19 * var8.m00() + var22 * var8.m01() + var25 * var8.m02();
      this.m01 = var20 * var8.m00() + var23 * var8.m01() + var26 * var8.m02();
      this.m02 = var21 * var8.m00() + var24 * var8.m01() + var27 * var8.m02();
      this.m10 = var19 * var8.m10() + var22 * var8.m11() + var25 * var8.m12();
      this.m11 = var20 * var8.m10() + var23 * var8.m11() + var26 * var8.m12();
      this.m12 = var21 * var8.m10() + var24 * var8.m11() + var27 * var8.m12();
      this.m20 = var19 * var8.m20() + var22 * var8.m21() + var25 * var8.m22();
      this.m21 = var20 * var8.m20() + var23 * var8.m21() + var26 * var8.m22();
      this.m22 = var21 * var8.m20() + var24 * var8.m21() + var27 * var8.m22();
      this.m30 = var19 * var8.m30() + var22 * var8.m31() + var25 * var8.m32() + var1;
      this.m31 = var20 * var8.m30() + var23 * var8.m31() + var26 * var8.m32() + var2;
      this.m32 = var21 * var8.m30() + var24 * var8.m31() + var27 * var8.m32() + var3;
      this.properties = 0;
      return this;
   }

   public Matrix4x3f set3x3(Matrix3fc var1) {
      if (var1 instanceof Matrix3f) {
         MemUtil.INSTANCE.copy3x3((Matrix3f)var1, this);
      } else {
         this.set3x3Matrix3fc(var1);
      }

      this.properties = 0;
      return this;
   }

   private void set3x3Matrix3fc(Matrix3fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m02 = var1.m02();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m12 = var1.m12();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
      this.m22 = var1.m22();
   }

   public Vector4f transform(Vector4f var1) {
      return var1.mul((Matrix4x3fc)this);
   }

   public Vector4f transform(Vector4fc var1, Vector4f var2) {
      return var1.mul((Matrix4x3fc)this, var2);
   }

   public Vector3f transformPosition(Vector3f var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20 * var1.z + this.m30, this.m01 * var1.x + this.m11 * var1.y + this.m21 * var1.z + this.m31, this.m02 * var1.x + this.m12 * var1.y + this.m22 * var1.z + this.m32);
      return var1;
   }

   public Vector3f transformPosition(Vector3fc var1, Vector3f var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20 * var1.z() + this.m30, this.m01 * var1.x() + this.m11 * var1.y() + this.m21 * var1.z() + this.m31, this.m02 * var1.x() + this.m12 * var1.y() + this.m22 * var1.z() + this.m32);
      return var2;
   }

   public Vector3f transformDirection(Vector3f var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20 * var1.z, this.m01 * var1.x + this.m11 * var1.y + this.m21 * var1.z, this.m02 * var1.x + this.m12 * var1.y + this.m22 * var1.z);
      return var1;
   }

   public Vector3f transformDirection(Vector3fc var1, Vector3f var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20 * var1.z(), this.m01 * var1.x() + this.m11 * var1.y() + this.m21 * var1.z(), this.m02 * var1.x() + this.m12 * var1.y() + this.m22 * var1.z());
      return var2;
   }

   public Matrix4x3f scale(Vector3fc var1, Matrix4x3f var2) {
      return this.scale(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3f scale(Vector3fc var1) {
      return this.scale(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix4x3f scale(float var1, Matrix4x3f var2) {
      return this.scale(var1, var1, var1, var2);
   }

   public Matrix4x3f scale(float var1) {
      return this.scale(var1, var1, var1);
   }

   public Matrix4x3f scaleXY(float var1, float var2, Matrix4x3f var3) {
      return this.scale(var1, var2, 1.0F, var3);
   }

   public Matrix4x3f scaleXY(float var1, float var2) {
      return this.scale(var1, var2, 1.0F);
   }

   public Matrix4x3f scale(float var1, float var2, float var3, Matrix4x3f var4) {
      return (this.properties & 4) != 0 ? var4.scaling(var1, var2, var3) : this.scaleGeneric(var1, var2, var3, var4);
   }

   private Matrix4x3f scaleGeneric(float var1, float var2, float var3, Matrix4x3f var4) {
      var4.m00 = this.m00 * var1;
      var4.m01 = this.m01 * var1;
      var4.m02 = this.m02 * var1;
      var4.m10 = this.m10 * var2;
      var4.m11 = this.m11 * var2;
      var4.m12 = this.m12 * var2;
      var4.m20 = this.m20 * var3;
      var4.m21 = this.m21 * var3;
      var4.m22 = this.m22 * var3;
      var4.m30 = this.m30;
      var4.m31 = this.m31;
      var4.m32 = this.m32;
      var4.properties = this.properties & -29;
      return var4;
   }

   public Matrix4x3f scale(float var1, float var2, float var3) {
      return this.scale(var1, var2, var3, this);
   }

   public Matrix4x3f scaleLocal(float var1, float var2, float var3, Matrix4x3f var4) {
      if ((this.properties & 4) != 0) {
         return var4.scaling(var1, var2, var3);
      } else {
         float var5 = var1 * this.m00;
         float var6 = var2 * this.m01;
         float var7 = var3 * this.m02;
         float var8 = var1 * this.m10;
         float var9 = var2 * this.m11;
         float var10 = var3 * this.m12;
         float var11 = var1 * this.m20;
         float var12 = var2 * this.m21;
         float var13 = var3 * this.m22;
         float var14 = var1 * this.m30;
         float var15 = var2 * this.m31;
         float var16 = var3 * this.m32;
         var4.m00 = var5;
         var4.m01 = var6;
         var4.m02 = var7;
         var4.m10 = var8;
         var4.m11 = var9;
         var4.m12 = var10;
         var4.m20 = var11;
         var4.m21 = var12;
         var4.m22 = var13;
         var4.m30 = var14;
         var4.m31 = var15;
         var4.m32 = var16;
         var4.properties = this.properties & -29;
         return var4;
      }
   }

   public Matrix4x3f scaleLocal(float var1, float var2, float var3) {
      return this.scaleLocal(var1, var2, var3, this);
   }

   public Matrix4x3f rotateX(float var1, Matrix4x3f var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotationX(var1);
      } else if ((this.properties & 8) != 0) {
         float var3 = this.m30;
         float var4 = this.m31;
         float var5 = this.m32;
         return var2.rotationX(var1).setTranslation(var3, var4, var5);
      } else {
         return this.rotateXInternal(var1, var2);
      }
   }

   private Matrix4x3f rotateXInternal(float var1, Matrix4x3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var7 = -var3;
      float var9 = this.m10 * var4 + this.m20 * var3;
      float var10 = this.m11 * var4 + this.m21 * var3;
      float var11 = this.m12 * var4 + this.m22 * var3;
      var2.m20 = this.m10 * var7 + this.m20 * var4;
      var2.m21 = this.m11 * var7 + this.m21 * var4;
      var2.m22 = this.m12 * var7 + this.m22 * var4;
      var2.m10 = var9;
      var2.m11 = var10;
      var2.m12 = var11;
      var2.m00 = this.m00;
      var2.m01 = this.m01;
      var2.m02 = this.m02;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotateX(float var1) {
      return this.rotateX(var1, this);
   }

   public Matrix4x3f rotateY(float var1, Matrix4x3f var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotationY(var1);
      } else if ((this.properties & 8) != 0) {
         float var3 = this.m30;
         float var4 = this.m31;
         float var5 = this.m32;
         return var2.rotationY(var1).setTranslation(var3, var4, var5);
      } else {
         return this.rotateYInternal(var1, var2);
      }
   }

   private Matrix4x3f rotateYInternal(float var1, Matrix4x3f var2) {
      float var4 = Math.sin(var1);
      float var3 = Math.cosFromSin(var4, var1);
      float var6 = -var4;
      float var9 = this.m00 * var3 + this.m20 * var6;
      float var10 = this.m01 * var3 + this.m21 * var6;
      float var11 = this.m02 * var3 + this.m22 * var6;
      var2.m20 = this.m00 * var4 + this.m20 * var3;
      var2.m21 = this.m01 * var4 + this.m21 * var3;
      var2.m22 = this.m02 * var4 + this.m22 * var3;
      var2.m00 = var9;
      var2.m01 = var10;
      var2.m02 = var11;
      var2.m10 = this.m10;
      var2.m11 = this.m11;
      var2.m12 = this.m12;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotateY(float var1) {
      return this.rotateY(var1, this);
   }

   public Matrix4x3f rotateZ(float var1, Matrix4x3f var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotationZ(var1);
      } else if ((this.properties & 8) != 0) {
         float var3 = this.m30;
         float var4 = this.m31;
         float var5 = this.m32;
         return var2.rotationZ(var1).setTranslation(var3, var4, var5);
      } else {
         return this.rotateZInternal(var1, var2);
      }
   }

   private Matrix4x3f rotateZInternal(float var1, Matrix4x3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var7 = -var3;
      float var9 = this.m00 * var4 + this.m10 * var3;
      float var10 = this.m01 * var4 + this.m11 * var3;
      float var11 = this.m02 * var4 + this.m12 * var3;
      var2.m10 = this.m00 * var7 + this.m10 * var4;
      var2.m11 = this.m01 * var7 + this.m11 * var4;
      var2.m12 = this.m02 * var7 + this.m12 * var4;
      var2.m00 = var9;
      var2.m01 = var10;
      var2.m02 = var11;
      var2.m20 = this.m20;
      var2.m21 = this.m21;
      var2.m22 = this.m22;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotateZ(float var1) {
      return this.rotateZ(var1, this);
   }

   public Matrix4x3f rotateXYZ(Vector3f var1) {
      return this.rotateXYZ(var1.x, var1.y, var1.z);
   }

   public Matrix4x3f rotateXYZ(float var1, float var2, float var3) {
      return this.rotateXYZ(var1, var2, var3, this);
   }

   public Matrix4x3f rotateXYZ(float var1, float var2, float var3, Matrix4x3f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationXYZ(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationXYZ(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return this.rotateXYZInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4x3f rotateXYZInternal(float var1, float var2, float var3, Matrix4x3f var4) {
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
      var4.m30 = this.m30;
      var4.m31 = this.m31;
      var4.m32 = this.m32;
      var4.properties = this.properties & -13;
      return var4;
   }

   public Matrix4x3f rotateZYX(Vector3f var1) {
      return this.rotateZYX(var1.z, var1.y, var1.x);
   }

   public Matrix4x3f rotateZYX(float var1, float var2, float var3) {
      return this.rotateZYX(var1, var2, var3, this);
   }

   public Matrix4x3f rotateZYX(float var1, float var2, float var3, Matrix4x3f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationZYX(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationZYX(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return this.rotateZYXInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4x3f rotateZYXInternal(float var1, float var2, float var3, Matrix4x3f var4) {
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
      var4.m30 = this.m30;
      var4.m31 = this.m31;
      var4.m32 = this.m32;
      var4.properties = this.properties & -13;
      return var4;
   }

   public Matrix4x3f rotateYXZ(Vector3f var1) {
      return this.rotateYXZ(var1.y, var1.x, var1.z);
   }

   public Matrix4x3f rotateYXZ(float var1, float var2, float var3) {
      return this.rotateYXZ(var1, var2, var3, this);
   }

   public Matrix4x3f rotateYXZ(float var1, float var2, float var3, Matrix4x3f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationYXZ(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationYXZ(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return this.rotateYXZInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4x3f rotateYXZInternal(float var1, float var2, float var3, Matrix4x3f var4) {
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
      var4.m30 = this.m30;
      var4.m31 = this.m31;
      var4.m32 = this.m32;
      var4.properties = this.properties & -13;
      return var4;
   }

   public Matrix4x3f rotate(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      if ((this.properties & 4) != 0) {
         return var5.rotation(var1, var2, var3, var4);
      } else {
         return (this.properties & 8) != 0 ? this.rotateTranslation(var1, var2, var3, var4, var5) : this.rotateGeneric(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4x3f rotateGeneric(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, var5) : this.rotateGenericInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4x3f rotateGenericInternal(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
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
      var5.m30 = this.m30;
      var5.m31 = this.m31;
      var5.m32 = this.m32;
      var5.properties = this.properties & -13;
      return var5;
   }

   public Matrix4x3f rotate(float var1, float var2, float var3, float var4) {
      return this.rotate(var1, var2, var3, var4, this);
   }

   public Matrix4x3f rotateTranslation(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      float var6 = this.m30;
      float var7 = this.m31;
      float var8 = this.m32;
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return var5.rotationX(var2 * var1).setTranslation(var6, var7, var8);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return var5.rotationY(var3 * var1).setTranslation(var6, var7, var8);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? var5.rotationZ(var4 * var1).setTranslation(var6, var7, var8) : this.rotateTranslationInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4x3f rotateTranslationInternal(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
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
      var5.m20 = var21;
      var5.m21 = var22;
      var5.m22 = var23;
      var5.m00 = var15;
      var5.m01 = var16;
      var5.m02 = var17;
      var5.m10 = var18;
      var5.m11 = var19;
      var5.m12 = var20;
      var5.m30 = this.m30;
      var5.m31 = this.m31;
      var5.m32 = this.m32;
      var5.properties = this.properties & -13;
      return var5;
   }

   public Matrix4x3f rotateAround(Quaternionfc var1, float var2, float var3, float var4) {
      return this.rotateAround(var1, var2, var3, var4, this);
   }

   private Matrix4x3f rotateAroundAffine(Quaternionfc var1, float var2, float var3, float var4, Matrix4x3f var5) {
      float var6 = var1.w() * var1.w();
      float var7 = var1.x() * var1.x();
      float var8 = var1.y() * var1.y();
      float var9 = var1.z() * var1.z();
      float var10 = var1.z() * var1.w();
      float var11 = var10 + var10;
      float var12 = var1.x() * var1.y();
      float var13 = var12 + var12;
      float var14 = var1.x() * var1.z();
      float var15 = var14 + var14;
      float var16 = var1.y() * var1.w();
      float var17 = var16 + var16;
      float var18 = var1.y() * var1.z();
      float var19 = var18 + var18;
      float var20 = var1.x() * var1.w();
      float var21 = var20 + var20;
      float var22 = var6 + var7 - var9 - var8;
      float var23 = var13 + var11;
      float var24 = var15 - var17;
      float var25 = var13 - var11;
      float var26 = var8 - var9 + var6 - var7;
      float var27 = var19 + var21;
      float var28 = var17 + var15;
      float var29 = var19 - var21;
      float var30 = var9 - var8 - var7 + var6;
      float var31 = this.m00 * var2 + this.m10 * var3 + this.m20 * var4 + this.m30;
      float var32 = this.m01 * var2 + this.m11 * var3 + this.m21 * var4 + this.m31;
      float var33 = this.m02 * var2 + this.m12 * var3 + this.m22 * var4 + this.m32;
      float var34 = this.m00 * var22 + this.m10 * var23 + this.m20 * var24;
      float var35 = this.m01 * var22 + this.m11 * var23 + this.m21 * var24;
      float var36 = this.m02 * var22 + this.m12 * var23 + this.m22 * var24;
      float var37 = this.m00 * var25 + this.m10 * var26 + this.m20 * var27;
      float var38 = this.m01 * var25 + this.m11 * var26 + this.m21 * var27;
      float var39 = this.m02 * var25 + this.m12 * var26 + this.m22 * var27;
      var5._m20(this.m00 * var28 + this.m10 * var29 + this.m20 * var30)._m21(this.m01 * var28 + this.m11 * var29 + this.m21 * var30)._m22(this.m02 * var28 + this.m12 * var29 + this.m22 * var30)._m00(var34)._m01(var35)._m02(var36)._m10(var37)._m11(var38)._m12(var39)._m30(-var34 * var2 - var37 * var3 - this.m20 * var4 + var31)._m31(-var35 * var2 - var38 * var3 - this.m21 * var4 + var32)._m32(-var36 * var2 - var39 * var3 - this.m22 * var4 + var33)._properties(this.properties & -13);
      return var5;
   }

   public Matrix4x3f rotateAround(Quaternionfc var1, float var2, float var3, float var4, Matrix4x3f var5) {
      return (this.properties & 4) != 0 ? this.rotationAround(var1, var2, var3, var4) : this.rotateAroundAffine(var1, var2, var3, var4, var5);
   }

   public Matrix4x3f rotationAround(Quaternionfc var1, float var2, float var3, float var4) {
      float var5 = var1.w() * var1.w();
      float var6 = var1.x() * var1.x();
      float var7 = var1.y() * var1.y();
      float var8 = var1.z() * var1.z();
      float var9 = var1.z() * var1.w();
      float var10 = var9 + var9;
      float var11 = var1.x() * var1.y();
      float var12 = var11 + var11;
      float var13 = var1.x() * var1.z();
      float var14 = var13 + var13;
      float var15 = var1.y() * var1.w();
      float var16 = var15 + var15;
      float var17 = var1.y() * var1.z();
      float var18 = var17 + var17;
      float var19 = var1.x() * var1.w();
      float var20 = var19 + var19;
      this._m20(var16 + var14);
      this._m21(var18 - var20);
      this._m22(var8 - var7 - var6 + var5);
      this._m00(var5 + var6 - var8 - var7);
      this._m01(var12 + var10);
      this._m02(var14 - var16);
      this._m10(var12 - var10);
      this._m11(var7 - var8 + var5 - var6);
      this._m12(var18 + var20);
      this._m30(-this.m00 * var2 - this.m10 * var3 - this.m20 * var4 + var2);
      this._m31(-this.m01 * var2 - this.m11 * var3 - this.m21 * var4 + var3);
      this._m32(-this.m02 * var2 - this.m12 * var3 - this.m22 * var4 + var4);
      this.properties = 16;
      return this;
   }

   public Matrix4x3f rotateLocal(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateLocalX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateLocalY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateLocalZ(var4 * var1, var5) : this.rotateLocalInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4x3f rotateLocalInternal(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
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
      float var33 = var15 * this.m30 + var18 * this.m31 + var21 * this.m32;
      float var34 = var16 * this.m30 + var19 * this.m31 + var22 * this.m32;
      float var35 = var17 * this.m30 + var20 * this.m31 + var23 * this.m32;
      var5.m00 = var24;
      var5.m01 = var25;
      var5.m02 = var26;
      var5.m10 = var27;
      var5.m11 = var28;
      var5.m12 = var29;
      var5.m20 = var30;
      var5.m21 = var31;
      var5.m22 = var32;
      var5.m30 = var33;
      var5.m31 = var34;
      var5.m32 = var35;
      var5.properties = this.properties & -13;
      return var5;
   }

   public Matrix4x3f rotateLocal(float var1, float var2, float var3, float var4) {
      return this.rotateLocal(var1, var2, var3, var4, this);
   }

   public Matrix4x3f rotateLocalX(float var1, Matrix4x3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m01 - var3 * this.m02;
      float var6 = var3 * this.m01 + var4 * this.m02;
      float var7 = var4 * this.m11 - var3 * this.m12;
      float var8 = var3 * this.m11 + var4 * this.m12;
      float var9 = var4 * this.m21 - var3 * this.m22;
      float var10 = var3 * this.m21 + var4 * this.m22;
      float var11 = var4 * this.m31 - var3 * this.m32;
      float var12 = var3 * this.m31 + var4 * this.m32;
      var2._m00(this.m00)._m01(var5)._m02(var6)._m10(this.m10)._m11(var7)._m12(var8)._m20(this.m20)._m21(var9)._m22(var10)._m30(this.m30)._m31(var11)._m32(var12)._properties(this.properties & -13);
      return var2;
   }

   public Matrix4x3f rotateLocalX(float var1) {
      return this.rotateLocalX(var1, this);
   }

   public Matrix4x3f rotateLocalY(float var1, Matrix4x3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 + var3 * this.m02;
      float var6 = -var3 * this.m00 + var4 * this.m02;
      float var7 = var4 * this.m10 + var3 * this.m12;
      float var8 = -var3 * this.m10 + var4 * this.m12;
      float var9 = var4 * this.m20 + var3 * this.m22;
      float var10 = -var3 * this.m20 + var4 * this.m22;
      float var11 = var4 * this.m30 + var3 * this.m32;
      float var12 = -var3 * this.m30 + var4 * this.m32;
      var2._m00(var5)._m01(this.m01)._m02(var6)._m10(var7)._m11(this.m11)._m12(var8)._m20(var9)._m21(this.m21)._m22(var10)._m30(var11)._m31(this.m31)._m32(var12)._properties(this.properties & -13);
      return var2;
   }

   public Matrix4x3f rotateLocalY(float var1) {
      return this.rotateLocalY(var1, this);
   }

   public Matrix4x3f rotateLocalZ(float var1, Matrix4x3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 - var3 * this.m01;
      float var6 = var3 * this.m00 + var4 * this.m01;
      float var7 = var4 * this.m10 - var3 * this.m11;
      float var8 = var3 * this.m10 + var4 * this.m11;
      float var9 = var4 * this.m20 - var3 * this.m21;
      float var10 = var3 * this.m20 + var4 * this.m21;
      float var11 = var4 * this.m30 - var3 * this.m31;
      float var12 = var3 * this.m30 + var4 * this.m31;
      var2._m00(var5)._m01(var6)._m02(this.m02)._m10(var7)._m11(var8)._m12(this.m12)._m20(var9)._m21(var10)._m22(this.m22)._m30(var11)._m31(var12)._m32(this.m32)._properties(this.properties & -13);
      return var2;
   }

   public Matrix4x3f rotateLocalZ(float var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Matrix4x3f translate(Vector3fc var1) {
      return this.translate(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3f translate(Vector3fc var1, Matrix4x3f var2) {
      return this.translate(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3f translate(float var1, float var2, float var3, Matrix4x3f var4) {
      return (this.properties & 4) != 0 ? var4.translation(var1, var2, var3) : this.translateGeneric(var1, var2, var3, var4);
   }

   private Matrix4x3f translateGeneric(float var1, float var2, float var3, Matrix4x3f var4) {
      MemUtil.INSTANCE.copy(this, var4);
      var4.m30 = this.m00 * var1 + this.m10 * var2 + this.m20 * var3 + this.m30;
      var4.m31 = this.m01 * var1 + this.m11 * var2 + this.m21 * var3 + this.m31;
      var4.m32 = this.m02 * var1 + this.m12 * var2 + this.m22 * var3 + this.m32;
      var4.properties = this.properties & -5;
      return var4;
   }

   public Matrix4x3f translate(float var1, float var2, float var3) {
      if ((this.properties & 4) != 0) {
         return this.translation(var1, var2, var3);
      } else {
         this.m30 += this.m00 * var1 + this.m10 * var2 + this.m20 * var3;
         this.m31 += this.m01 * var1 + this.m11 * var2 + this.m21 * var3;
         this.m32 += this.m02 * var1 + this.m12 * var2 + this.m22 * var3;
         this.properties &= -5;
         return this;
      }
   }

   public Matrix4x3f translateLocal(Vector3fc var1) {
      return this.translateLocal(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3f translateLocal(Vector3fc var1, Matrix4x3f var2) {
      return this.translateLocal(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4x3f translateLocal(float var1, float var2, float var3, Matrix4x3f var4) {
      var4.m00 = this.m00;
      var4.m01 = this.m01;
      var4.m02 = this.m02;
      var4.m10 = this.m10;
      var4.m11 = this.m11;
      var4.m12 = this.m12;
      var4.m20 = this.m20;
      var4.m21 = this.m21;
      var4.m22 = this.m22;
      var4.m30 = this.m30 + var1;
      var4.m31 = this.m31 + var2;
      var4.m32 = this.m32 + var3;
      var4.properties = this.properties & -5;
      return var4;
   }

   public Matrix4x3f translateLocal(float var1, float var2, float var3) {
      return this.translateLocal(var1, var2, var3, this);
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
      var1.writeFloat(this.m30);
      var1.writeFloat(this.m31);
      var1.writeFloat(this.m32);
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
      this.m30 = var1.readFloat();
      this.m31 = var1.readFloat();
      this.m32 = var1.readFloat();
      this.determineProperties();
   }

   public Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4x3f var8) {
      float var9 = 2.0F / (var2 - var1);
      float var10 = 2.0F / (var4 - var3);
      float var11 = (var7 ? 1.0F : 2.0F) / (var5 - var6);
      float var12 = (var1 + var2) / (var1 - var2);
      float var13 = (var4 + var3) / (var3 - var4);
      float var14 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      var8.m30 = this.m00 * var12 + this.m10 * var13 + this.m20 * var14 + this.m30;
      var8.m31 = this.m01 * var12 + this.m11 * var13 + this.m21 * var14 + this.m31;
      var8.m32 = this.m02 * var12 + this.m12 * var13 + this.m22 * var14 + this.m32;
      var8.m00 = this.m00 * var9;
      var8.m01 = this.m01 * var9;
      var8.m02 = this.m02 * var9;
      var8.m10 = this.m10 * var10;
      var8.m11 = this.m11 * var10;
      var8.m12 = this.m12 * var10;
      var8.m20 = this.m20 * var11;
      var8.m21 = this.m21 * var11;
      var8.m22 = this.m22 * var11;
      var8.properties = this.properties & -29;
      return var8;
   }

   public Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
      return this.ortho(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.ortho(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.ortho(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4x3f var8) {
      float var9 = 2.0F / (var2 - var1);
      float var10 = 2.0F / (var4 - var3);
      float var11 = (var7 ? 1.0F : 2.0F) / (var6 - var5);
      float var12 = (var1 + var2) / (var1 - var2);
      float var13 = (var4 + var3) / (var3 - var4);
      float var14 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      var8.m30 = this.m00 * var12 + this.m10 * var13 + this.m20 * var14 + this.m30;
      var8.m31 = this.m01 * var12 + this.m11 * var13 + this.m21 * var14 + this.m31;
      var8.m32 = this.m02 * var12 + this.m12 * var13 + this.m22 * var14 + this.m32;
      var8.m00 = this.m00 * var9;
      var8.m01 = this.m01 * var9;
      var8.m02 = this.m02 * var9;
      var8.m10 = this.m10 * var10;
      var8.m11 = this.m11 * var10;
      var8.m12 = this.m12 * var10;
      var8.m20 = this.m20 * var11;
      var8.m21 = this.m21 * var11;
      var8.m22 = this.m22 * var11;
      var8.properties = this.properties & -29;
      return var8;
   }

   public Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4x3f setOrtho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / (var2 - var1);
      this.m11 = 2.0F / (var4 - var3);
      this.m22 = (var7 ? 1.0F : 2.0F) / (var5 - var6);
      this.m30 = (var2 + var1) / (var1 - var2);
      this.m31 = (var4 + var3) / (var3 - var4);
      this.m32 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f setOrtho(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setOrtho(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4x3f setOrthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / (var2 - var1);
      this.m11 = 2.0F / (var4 - var3);
      this.m22 = (var7 ? 1.0F : 2.0F) / (var6 - var5);
      this.m30 = (var2 + var1) / (var1 - var2);
      this.m31 = (var4 + var3) / (var3 - var4);
      this.m32 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f setOrthoLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setOrthoLH(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5, Matrix4x3f var6) {
      float var7 = 2.0F / var1;
      float var8 = 2.0F / var2;
      float var9 = (var5 ? 1.0F : 2.0F) / (var3 - var4);
      float var10 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      var6.m30 = this.m20 * var10 + this.m30;
      var6.m31 = this.m21 * var10 + this.m31;
      var6.m32 = this.m22 * var10 + this.m32;
      var6.m00 = this.m00 * var7;
      var6.m01 = this.m01 * var7;
      var6.m02 = this.m02 * var7;
      var6.m10 = this.m10 * var8;
      var6.m11 = this.m11 * var8;
      var6.m12 = this.m12 * var8;
      var6.m20 = this.m20 * var9;
      var6.m21 = this.m21 * var9;
      var6.m22 = this.m22 * var9;
      var6.properties = this.properties & -29;
      return var6;
   }

   public Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      return this.orthoSymmetric(var1, var2, var3, var4, false, var5);
   }

   public Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5) {
      return this.orthoSymmetric(var1, var2, var3, var4, var5, this);
   }

   public Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4) {
      return this.orthoSymmetric(var1, var2, var3, var4, false, this);
   }

   public Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4x3f var6) {
      float var7 = 2.0F / var1;
      float var8 = 2.0F / var2;
      float var9 = (var5 ? 1.0F : 2.0F) / (var4 - var3);
      float var10 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      var6.m30 = this.m20 * var10 + this.m30;
      var6.m31 = this.m21 * var10 + this.m31;
      var6.m32 = this.m22 * var10 + this.m32;
      var6.m00 = this.m00 * var7;
      var6.m01 = this.m01 * var7;
      var6.m02 = this.m02 * var7;
      var6.m10 = this.m10 * var8;
      var6.m11 = this.m11 * var8;
      var6.m12 = this.m12 * var8;
      var6.m20 = this.m20 * var9;
      var6.m21 = this.m21 * var9;
      var6.m22 = this.m22 * var9;
      var6.properties = this.properties & -29;
      return var6;
   }

   public Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, false, var5);
   }

   public Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, var5, this);
   }

   public Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, false, this);
   }

   public Matrix4x3f setOrthoSymmetric(float var1, float var2, float var3, float var4, boolean var5) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / var1;
      this.m11 = 2.0F / var2;
      this.m22 = (var5 ? 1.0F : 2.0F) / (var3 - var4);
      this.m32 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f setOrthoSymmetric(float var1, float var2, float var3, float var4) {
      return this.setOrthoSymmetric(var1, var2, var3, var4, false);
   }

   public Matrix4x3f setOrthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / var1;
      this.m11 = 2.0F / var2;
      this.m22 = (var5 ? 1.0F : 2.0F) / (var4 - var3);
      this.m32 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f setOrthoSymmetricLH(float var1, float var2, float var3, float var4) {
      return this.setOrthoSymmetricLH(var1, var2, var3, var4, false);
   }

   public Matrix4x3f ortho2D(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      float var6 = 2.0F / (var2 - var1);
      float var7 = 2.0F / (var4 - var3);
      float var8 = -(var2 + var1) / (var2 - var1);
      float var9 = -(var4 + var3) / (var4 - var3);
      var5.m30 = this.m00 * var8 + this.m10 * var9 + this.m30;
      var5.m31 = this.m01 * var8 + this.m11 * var9 + this.m31;
      var5.m32 = this.m02 * var8 + this.m12 * var9 + this.m32;
      var5.m00 = this.m00 * var6;
      var5.m01 = this.m01 * var6;
      var5.m02 = this.m02 * var6;
      var5.m10 = this.m10 * var7;
      var5.m11 = this.m11 * var7;
      var5.m12 = this.m12 * var7;
      var5.m20 = -this.m20;
      var5.m21 = -this.m21;
      var5.m22 = -this.m22;
      var5.properties = this.properties & -29;
      return var5;
   }

   public Matrix4x3f ortho2D(float var1, float var2, float var3, float var4) {
      return this.ortho2D(var1, var2, var3, var4, this);
   }

   public Matrix4x3f ortho2DLH(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      float var6 = 2.0F / (var2 - var1);
      float var7 = 2.0F / (var4 - var3);
      float var8 = -(var2 + var1) / (var2 - var1);
      float var9 = -(var4 + var3) / (var4 - var3);
      var5.m30 = this.m00 * var8 + this.m10 * var9 + this.m30;
      var5.m31 = this.m01 * var8 + this.m11 * var9 + this.m31;
      var5.m32 = this.m02 * var8 + this.m12 * var9 + this.m32;
      var5.m00 = this.m00 * var6;
      var5.m01 = this.m01 * var6;
      var5.m02 = this.m02 * var6;
      var5.m10 = this.m10 * var7;
      var5.m11 = this.m11 * var7;
      var5.m12 = this.m12 * var7;
      var5.m20 = this.m20;
      var5.m21 = this.m21;
      var5.m22 = this.m22;
      var5.properties = this.properties & -29;
      return var5;
   }

   public Matrix4x3f ortho2DLH(float var1, float var2, float var3, float var4) {
      return this.ortho2DLH(var1, var2, var3, var4, this);
   }

   public Matrix4x3f setOrtho2D(float var1, float var2, float var3, float var4) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / (var2 - var1);
      this.m11 = 2.0F / (var4 - var3);
      this.m22 = -1.0F;
      this.m30 = -(var2 + var1) / (var2 - var1);
      this.m31 = -(var4 + var3) / (var4 - var3);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f setOrtho2DLH(float var1, float var2, float var3, float var4) {
      MemUtil.INSTANCE.identity(this);
      this.m00 = 2.0F / (var2 - var1);
      this.m11 = 2.0F / (var4 - var3);
      this.m22 = 1.0F;
      this.m30 = -(var2 + var1) / (var2 - var1);
      this.m31 = -(var4 + var3) / (var4 - var3);
      this.properties = 0;
      return this;
   }

   public Matrix4x3f lookAlong(Vector3fc var1, Vector3fc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4x3f lookAlong(Vector3fc var1, Vector3fc var2, Matrix4x3f var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
      if ((this.properties & 4) != 0) {
         return this.setLookAlong(var1, var2, var3, var4, var5, var6);
      } else {
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
         var7.m30 = this.m30;
         var7.m31 = this.m31;
         var7.m32 = this.m32;
         var7.properties = this.properties & -13;
         return var7;
      }
   }

   public Matrix4x3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.lookAlong(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4x3f setLookAlong(Vector3fc var1, Vector3fc var2) {
      return this.setLookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f setLookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f setLookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.setLookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3f setLookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = var1 - var4;
      float var11 = var2 - var5;
      float var12 = var3 - var6;
      float var13 = Math.invsqrt(var10 * var10 + var11 * var11 + var12 * var12);
      var10 *= var13;
      var11 *= var13;
      var12 *= var13;
      float var14 = var8 * var12 - var9 * var11;
      float var15 = var9 * var10 - var7 * var12;
      float var16 = var7 * var11 - var8 * var10;
      float var17 = Math.invsqrt(var14 * var14 + var15 * var15 + var16 * var16);
      var14 *= var17;
      var15 *= var17;
      var16 *= var17;
      float var18 = var11 * var16 - var12 * var15;
      float var19 = var12 * var14 - var10 * var16;
      float var20 = var10 * var15 - var11 * var14;
      this.m00 = var14;
      this.m01 = var18;
      this.m02 = var10;
      this.m10 = var15;
      this.m11 = var19;
      this.m12 = var11;
      this.m20 = var16;
      this.m21 = var20;
      this.m22 = var12;
      this.m30 = -(var14 * var1 + var15 * var2 + var16 * var3);
      this.m31 = -(var18 * var1 + var19 * var2 + var20 * var3);
      this.m32 = -(var10 * var1 + var11 * var2 + var12 * var3);
      this.properties = 16;
      return this;
   }

   public Matrix4x3f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4x3f var4) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4x3f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10) {
      return (this.properties & 4) != 0 ? var10.setLookAt(var1, var2, var3, var4, var5, var6, var7, var8, var9) : this.lookAtGeneric(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   private Matrix4x3f lookAtGeneric(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10) {
      float var11 = var1 - var4;
      float var12 = var2 - var5;
      float var13 = var3 - var6;
      float var14 = Math.invsqrt(var11 * var11 + var12 * var12 + var13 * var13);
      var11 *= var14;
      var12 *= var14;
      var13 *= var14;
      float var15 = var8 * var13 - var9 * var12;
      float var16 = var9 * var11 - var7 * var13;
      float var17 = var7 * var12 - var8 * var11;
      float var18 = Math.invsqrt(var15 * var15 + var16 * var16 + var17 * var17);
      var15 *= var18;
      var16 *= var18;
      var17 *= var18;
      float var19 = var12 * var17 - var13 * var16;
      float var20 = var13 * var15 - var11 * var17;
      float var21 = var11 * var16 - var12 * var15;
      float var31 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var32 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var33 = -(var11 * var1 + var12 * var2 + var13 * var3);
      var10.m30 = this.m00 * var31 + this.m10 * var32 + this.m20 * var33 + this.m30;
      var10.m31 = this.m01 * var31 + this.m11 * var32 + this.m21 * var33 + this.m31;
      var10.m32 = this.m02 * var31 + this.m12 * var32 + this.m22 * var33 + this.m32;
      float var34 = this.m00 * var15 + this.m10 * var19 + this.m20 * var11;
      float var35 = this.m01 * var15 + this.m11 * var19 + this.m21 * var11;
      float var36 = this.m02 * var15 + this.m12 * var19 + this.m22 * var11;
      float var37 = this.m00 * var16 + this.m10 * var20 + this.m20 * var12;
      float var38 = this.m01 * var16 + this.m11 * var20 + this.m21 * var12;
      float var39 = this.m02 * var16 + this.m12 * var20 + this.m22 * var12;
      var10.m20 = this.m00 * var17 + this.m10 * var21 + this.m20 * var13;
      var10.m21 = this.m01 * var17 + this.m11 * var21 + this.m21 * var13;
      var10.m22 = this.m02 * var17 + this.m12 * var21 + this.m22 * var13;
      var10.m00 = var34;
      var10.m01 = var35;
      var10.m02 = var36;
      var10.m10 = var37;
      var10.m11 = var38;
      var10.m12 = var39;
      var10.properties = this.properties & -13;
      return var10;
   }

   public Matrix4x3f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      return this.lookAt(var1, var2, var3, var4, var5, var6, var7, var8, var9, this);
   }

   public Matrix4x3f setLookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.setLookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3f setLookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = var4 - var1;
      float var11 = var5 - var2;
      float var12 = var6 - var3;
      float var13 = Math.invsqrt(var10 * var10 + var11 * var11 + var12 * var12);
      var10 *= var13;
      var11 *= var13;
      var12 *= var13;
      float var14 = var8 * var12 - var9 * var11;
      float var15 = var9 * var10 - var7 * var12;
      float var16 = var7 * var11 - var8 * var10;
      float var17 = Math.invsqrt(var14 * var14 + var15 * var15 + var16 * var16);
      var14 *= var17;
      var15 *= var17;
      var16 *= var17;
      float var18 = var11 * var16 - var12 * var15;
      float var19 = var12 * var14 - var10 * var16;
      float var20 = var10 * var15 - var11 * var14;
      this.m00 = var14;
      this.m01 = var18;
      this.m02 = var10;
      this.m10 = var15;
      this.m11 = var19;
      this.m12 = var11;
      this.m20 = var16;
      this.m21 = var20;
      this.m22 = var12;
      this.m30 = -(var14 * var1 + var15 * var2 + var16 * var3);
      this.m31 = -(var18 * var1 + var19 * var2 + var20 * var3);
      this.m32 = -(var10 * var1 + var11 * var2 + var12 * var3);
      this.properties = 16;
      return this;
   }

   public Matrix4x3f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4x3f var4) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4x3f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4x3f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10) {
      return (this.properties & 4) != 0 ? var10.setLookAtLH(var1, var2, var3, var4, var5, var6, var7, var8, var9) : this.lookAtLHGeneric(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   private Matrix4x3f lookAtLHGeneric(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10) {
      float var11 = var4 - var1;
      float var12 = var5 - var2;
      float var13 = var6 - var3;
      float var14 = Math.invsqrt(var11 * var11 + var12 * var12 + var13 * var13);
      var11 *= var14;
      var12 *= var14;
      var13 *= var14;
      float var15 = var8 * var13 - var9 * var12;
      float var16 = var9 * var11 - var7 * var13;
      float var17 = var7 * var12 - var8 * var11;
      float var18 = Math.invsqrt(var15 * var15 + var16 * var16 + var17 * var17);
      var15 *= var18;
      var16 *= var18;
      var17 *= var18;
      float var19 = var12 * var17 - var13 * var16;
      float var20 = var13 * var15 - var11 * var17;
      float var21 = var11 * var16 - var12 * var15;
      float var31 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var32 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var33 = -(var11 * var1 + var12 * var2 + var13 * var3);
      var10.m30 = this.m00 * var31 + this.m10 * var32 + this.m20 * var33 + this.m30;
      var10.m31 = this.m01 * var31 + this.m11 * var32 + this.m21 * var33 + this.m31;
      var10.m32 = this.m02 * var31 + this.m12 * var32 + this.m22 * var33 + this.m32;
      float var34 = this.m00 * var15 + this.m10 * var19 + this.m20 * var11;
      float var35 = this.m01 * var15 + this.m11 * var19 + this.m21 * var11;
      float var36 = this.m02 * var15 + this.m12 * var19 + this.m22 * var11;
      float var37 = this.m00 * var16 + this.m10 * var20 + this.m20 * var12;
      float var38 = this.m01 * var16 + this.m11 * var20 + this.m21 * var12;
      float var39 = this.m02 * var16 + this.m12 * var20 + this.m22 * var12;
      var10.m20 = this.m00 * var17 + this.m10 * var21 + this.m20 * var13;
      var10.m21 = this.m01 * var17 + this.m11 * var21 + this.m21 * var13;
      var10.m22 = this.m02 * var17 + this.m12 * var21 + this.m22 * var13;
      var10.m00 = var34;
      var10.m01 = var35;
      var10.m02 = var36;
      var10.m10 = var37;
      var10.m11 = var38;
      var10.m12 = var39;
      var10.properties = this.properties & -13;
      return var10;
   }

   public Matrix4x3f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      return this.lookAtLH(var1, var2, var3, var4, var5, var6, var7, var8, var9, this);
   }

   public Matrix4x3f rotate(Quaternionfc var1, Matrix4x3f var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotation(var1);
      } else {
         return (this.properties & 8) != 0 ? this.rotateTranslation(var1, var2) : this.rotateGeneric(var1, var2);
      }
   }

   private Matrix4x3f rotateGeneric(Quaternionfc var1, Matrix4x3f var2) {
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
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotate(Quaternionfc var1) {
      return this.rotate(var1, this);
   }

   public Matrix4x3f rotateTranslation(Quaternionfc var1, Matrix4x3f var2) {
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
      var2.m20 = var25;
      var2.m21 = var26;
      var2.m22 = var27;
      var2.m00 = var19;
      var2.m01 = var20;
      var2.m02 = var21;
      var2.m10 = var22;
      var2.m11 = var23;
      var2.m12 = var24;
      var2.m30 = this.m30;
      var2.m31 = this.m31;
      var2.m32 = this.m32;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotateLocal(Quaternionfc var1, Matrix4x3f var2) {
      float var3 = var1.w() * var1.w();
      float var4 = var1.x() * var1.x();
      float var5 = var1.y() * var1.y();
      float var6 = var1.z() * var1.z();
      float var7 = var1.z() * var1.w();
      float var8 = var1.x() * var1.y();
      float var9 = var1.x() * var1.z();
      float var10 = var1.y() * var1.w();
      float var11 = var1.y() * var1.z();
      float var12 = var1.x() * var1.w();
      float var13 = var3 + var4 - var6 - var5;
      float var14 = var8 + var7 + var7 + var8;
      float var15 = var9 - var10 + var9 - var10;
      float var16 = -var7 + var8 - var7 + var8;
      float var17 = var5 - var6 + var3 - var4;
      float var18 = var11 + var11 + var12 + var12;
      float var19 = var10 + var9 + var9 + var10;
      float var20 = var11 + var11 - var12 - var12;
      float var21 = var6 - var5 - var4 + var3;
      float var22 = var13 * this.m00 + var16 * this.m01 + var19 * this.m02;
      float var23 = var14 * this.m00 + var17 * this.m01 + var20 * this.m02;
      float var24 = var15 * this.m00 + var18 * this.m01 + var21 * this.m02;
      float var25 = var13 * this.m10 + var16 * this.m11 + var19 * this.m12;
      float var26 = var14 * this.m10 + var17 * this.m11 + var20 * this.m12;
      float var27 = var15 * this.m10 + var18 * this.m11 + var21 * this.m12;
      float var28 = var13 * this.m20 + var16 * this.m21 + var19 * this.m22;
      float var29 = var14 * this.m20 + var17 * this.m21 + var20 * this.m22;
      float var30 = var15 * this.m20 + var18 * this.m21 + var21 * this.m22;
      float var31 = var13 * this.m30 + var16 * this.m31 + var19 * this.m32;
      float var32 = var14 * this.m30 + var17 * this.m31 + var20 * this.m32;
      float var33 = var15 * this.m30 + var18 * this.m31 + var21 * this.m32;
      var2.m00 = var22;
      var2.m01 = var23;
      var2.m02 = var24;
      var2.m10 = var25;
      var2.m11 = var26;
      var2.m12 = var27;
      var2.m20 = var28;
      var2.m21 = var29;
      var2.m22 = var30;
      var2.m30 = var31;
      var2.m31 = var32;
      var2.m32 = var33;
      var2.properties = this.properties & -13;
      return var2;
   }

   public Matrix4x3f rotateLocal(Quaternionfc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix4x3f rotate(AxisAngle4f var1) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4x3f rotate(AxisAngle4f var1, Matrix4x3f var2) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z, var2);
   }

   public Matrix4x3f rotate(float var1, Vector3fc var2) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f rotate(float var1, Vector3fc var2, Matrix4x3f var3) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3f reflect(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      if ((this.properties & 4) != 0) {
         return var5.reflection(var1, var2, var3, var4);
      } else {
         float var6 = var1 + var1;
         float var7 = var2 + var2;
         float var8 = var3 + var3;
         float var9 = var4 + var4;
         float var10 = 1.0F - var6 * var1;
         float var11 = -var6 * var2;
         float var12 = -var6 * var3;
         float var13 = -var7 * var1;
         float var14 = 1.0F - var7 * var2;
         float var15 = -var7 * var3;
         float var16 = -var8 * var1;
         float var17 = -var8 * var2;
         float var18 = 1.0F - var8 * var3;
         float var19 = -var9 * var1;
         float var20 = -var9 * var2;
         float var21 = -var9 * var3;
         var5.m30 = this.m00 * var19 + this.m10 * var20 + this.m20 * var21 + this.m30;
         var5.m31 = this.m01 * var19 + this.m11 * var20 + this.m21 * var21 + this.m31;
         var5.m32 = this.m02 * var19 + this.m12 * var20 + this.m22 * var21 + this.m32;
         float var22 = this.m00 * var10 + this.m10 * var11 + this.m20 * var12;
         float var23 = this.m01 * var10 + this.m11 * var11 + this.m21 * var12;
         float var24 = this.m02 * var10 + this.m12 * var11 + this.m22 * var12;
         float var25 = this.m00 * var13 + this.m10 * var14 + this.m20 * var15;
         float var26 = this.m01 * var13 + this.m11 * var14 + this.m21 * var15;
         float var27 = this.m02 * var13 + this.m12 * var14 + this.m22 * var15;
         var5.m20 = this.m00 * var16 + this.m10 * var17 + this.m20 * var18;
         var5.m21 = this.m01 * var16 + this.m11 * var17 + this.m21 * var18;
         var5.m22 = this.m02 * var16 + this.m12 * var17 + this.m22 * var18;
         var5.m00 = var22;
         var5.m01 = var23;
         var5.m02 = var24;
         var5.m10 = var25;
         var5.m11 = var26;
         var5.m12 = var27;
         var5.properties = this.properties & -13;
         return var5;
      }
   }

   public Matrix4x3f reflect(float var1, float var2, float var3, float var4) {
      return this.reflect(var1, var2, var3, var4, this);
   }

   public Matrix4x3f reflect(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.reflect(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4x3f reflect(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
      float var8 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var9 = var1 * var8;
      float var10 = var2 * var8;
      float var11 = var3 * var8;
      return this.reflect(var9, var10, var11, -var9 * var4 - var10 * var5 - var11 * var6, var7);
   }

   public Matrix4x3f reflect(Vector3fc var1, Vector3fc var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f reflect(Quaternionfc var1, Vector3fc var2) {
      return this.reflect(var1, var2, this);
   }

   public Matrix4x3f reflect(Quaternionfc var1, Vector3fc var2, Matrix4x3f var3) {
      double var4 = (double)(var1.x() + var1.x());
      double var6 = (double)(var1.y() + var1.y());
      double var8 = (double)(var1.z() + var1.z());
      float var10 = (float)((double)var1.x() * var8 + (double)var1.w() * var6);
      float var11 = (float)((double)var1.y() * var8 - (double)var1.w() * var4);
      float var12 = (float)(1.0D - ((double)var1.x() * var4 + (double)var1.y() * var6));
      return this.reflect(var10, var11, var12, var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3f reflect(Vector3fc var1, Vector3fc var2, Matrix4x3f var3) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3f reflection(float var1, float var2, float var3, float var4) {
      float var5 = var1 + var1;
      float var6 = var2 + var2;
      float var7 = var3 + var3;
      float var8 = var4 + var4;
      this.m00 = 1.0F - var5 * var1;
      this.m01 = -var5 * var2;
      this.m02 = -var5 * var3;
      this.m10 = -var6 * var1;
      this.m11 = 1.0F - var6 * var2;
      this.m12 = -var6 * var3;
      this.m20 = -var7 * var1;
      this.m21 = -var7 * var2;
      this.m22 = 1.0F - var7 * var3;
      this.m30 = -var8 * var1;
      this.m31 = -var8 * var2;
      this.m32 = -var8 * var3;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f reflection(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var8 = var1 * var7;
      float var9 = var2 * var7;
      float var10 = var3 * var7;
      return this.reflection(var8, var9, var10, -var8 * var4 - var9 * var5 - var10 * var6);
   }

   public Matrix4x3f reflection(Vector3fc var1, Vector3fc var2) {
      return this.reflection(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f reflection(Quaternionfc var1, Vector3fc var2) {
      double var3 = (double)(var1.x() + var1.x());
      double var5 = (double)(var1.y() + var1.y());
      double var7 = (double)(var1.z() + var1.z());
      float var9 = (float)((double)var1.x() * var7 + (double)var1.w() * var5);
      float var10 = (float)((double)var1.y() * var7 - (double)var1.w() * var3);
      float var11 = (float)(1.0D - ((double)var1.x() * var3 + (double)var1.y() * var5));
      return this.reflection(var9, var10, var11, var2.x(), var2.y(), var2.z());
   }

   public Vector4f getRow(int var1, Vector4f var2) throws IndexOutOfBoundsException {
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

   public Matrix4x3f setRow(int var1, Vector4fc var2) throws IndexOutOfBoundsException {
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

   public Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException {
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

   public Matrix4x3f setColumn(int var1, Vector3fc var2) throws IndexOutOfBoundsException {
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

   public Matrix4x3f normal() {
      return this.normal(this);
   }

   public Matrix4x3f normal(Matrix4x3f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
      }
   }

   private Matrix4x3f normalOrthonormal(Matrix4x3f var1) {
      if (var1 != this) {
         var1.set((Matrix4x3fc)this);
      }

      return var1._properties(16);
   }

   private Matrix4x3f normalGeneric(Matrix4x3f var1) {
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
      var1.m30 = 0.0F;
      var1.m31 = 0.0F;
      var1.m32 = 0.0F;
      var1.properties = this.properties & -9;
      return var1;
   }

   public Matrix3f normal(Matrix3f var1) {
      return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
   }

   private Matrix3f normalOrthonormal(Matrix3f var1) {
      return var1.set((Matrix4x3fc)this);
   }

   private Matrix3f normalGeneric(Matrix3f var1) {
      float var2 = this.m00 * this.m11;
      float var3 = this.m01 * this.m10;
      float var4 = this.m02 * this.m10;
      float var5 = this.m00 * this.m12;
      float var6 = this.m01 * this.m12;
      float var7 = this.m02 * this.m11;
      float var8 = (var2 - var3) * this.m22 + (var4 - var5) * this.m21 + (var6 - var7) * this.m20;
      float var9 = 1.0F / var8;
      var1.m00((this.m11 * this.m22 - this.m21 * this.m12) * var9);
      var1.m01((this.m20 * this.m12 - this.m10 * this.m22) * var9);
      var1.m02((this.m10 * this.m21 - this.m20 * this.m11) * var9);
      var1.m10((this.m21 * this.m02 - this.m01 * this.m22) * var9);
      var1.m11((this.m00 * this.m22 - this.m20 * this.m02) * var9);
      var1.m12((this.m20 * this.m01 - this.m00 * this.m21) * var9);
      var1.m20((var6 - var7) * var9);
      var1.m21((var4 - var5) * var9);
      var1.m22((var2 - var3) * var9);
      return var1;
   }

   public Matrix4x3f cofactor3x3() {
      return this.cofactor3x3(this);
   }

   public Matrix3f cofactor3x3(Matrix3f var1) {
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

   public Matrix4x3f cofactor3x3(Matrix4x3f var1) {
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
      var1.m30 = 0.0F;
      var1.m31 = 0.0F;
      var1.m32 = 0.0F;
      var1.properties = this.properties & -9;
      return var1;
   }

   public Matrix4x3f normalize3x3() {
      return this.normalize3x3(this);
   }

   public Matrix4x3f normalize3x3(Matrix4x3f var1) {
      float var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      float var3 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      float var4 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      var1.m00 = this.m00 * var2;
      var1.m01 = this.m01 * var2;
      var1.m02 = this.m02 * var2;
      var1.m10 = this.m10 * var3;
      var1.m11 = this.m11 * var3;
      var1.m12 = this.m12 * var3;
      var1.m20 = this.m20 * var4;
      var1.m21 = this.m21 * var4;
      var1.m22 = this.m22 * var4;
      var1.properties = this.properties;
      return var1;
   }

   public Matrix3f normalize3x3(Matrix3f var1) {
      float var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      float var3 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      float var4 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      var1.m00(this.m00 * var2);
      var1.m01(this.m01 * var2);
      var1.m02(this.m02 * var2);
      var1.m10(this.m10 * var3);
      var1.m11(this.m11 * var3);
      var1.m12(this.m12 * var3);
      var1.m20(this.m20 * var4);
      var1.m21(this.m21 * var4);
      var1.m22(this.m22 * var4);
      return var1;
   }

   public Vector4f frustumPlane(int var1, Vector4f var2) {
      switch(var1) {
      case 0:
         var2.set(this.m00, this.m10, this.m20, 1.0F + this.m30).normalize();
         break;
      case 1:
         var2.set(-this.m00, -this.m10, -this.m20, 1.0F - this.m30).normalize();
         break;
      case 2:
         var2.set(this.m01, this.m11, this.m21, 1.0F + this.m31).normalize();
         break;
      case 3:
         var2.set(-this.m01, -this.m11, -this.m21, 1.0F - this.m31).normalize();
         break;
      case 4:
         var2.set(this.m02, this.m12, this.m22, 1.0F + this.m32).normalize();
         break;
      case 5:
         var2.set(-this.m02, -this.m12, -this.m22, 1.0F - this.m32).normalize();
         break;
      default:
         throw new IllegalArgumentException("which");
      }

      return var2;
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

   public Vector3f origin(Vector3f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      float var3 = this.m00 * this.m12 - this.m02 * this.m10;
      float var4 = this.m01 * this.m12 - this.m02 * this.m11;
      float var5 = this.m20 * this.m31 - this.m21 * this.m30;
      float var6 = this.m20 * this.m32 - this.m22 * this.m30;
      float var7 = this.m21 * this.m32 - this.m22 * this.m31;
      var1.x = -this.m10 * var7 + this.m11 * var6 - this.m12 * var5;
      var1.y = this.m00 * var7 - this.m01 * var6 + this.m02 * var5;
      var1.z = -this.m30 * var4 + this.m31 * var3 - this.m32 * var2;
      return var1;
   }

   public Matrix4x3f shadow(Vector4fc var1, float var2, float var3, float var4, float var5) {
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var2, var3, var4, var5, this);
   }

   public Matrix4x3f shadow(Vector4fc var1, float var2, float var3, float var4, float var5, Matrix4x3f var6) {
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var2, var3, var4, var5, var6);
   }

   public Matrix4x3f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.shadow(var1, var2, var3, var4, var5, var6, var7, var8, this);
   }

   public Matrix4x3f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Matrix4x3f var9) {
      float var10 = Math.invsqrt(var5 * var5 + var6 * var6 + var7 * var7);
      float var11 = var5 * var10;
      float var12 = var6 * var10;
      float var13 = var7 * var10;
      float var14 = var8 * var10;
      float var15 = var11 * var1 + var12 * var2 + var13 * var3 + var14 * var4;
      float var16 = var15 - var11 * var1;
      float var17 = -var11 * var2;
      float var18 = -var11 * var3;
      float var19 = -var11 * var4;
      float var20 = -var12 * var1;
      float var21 = var15 - var12 * var2;
      float var22 = -var12 * var3;
      float var23 = -var12 * var4;
      float var24 = -var13 * var1;
      float var25 = -var13 * var2;
      float var26 = var15 - var13 * var3;
      float var27 = -var13 * var4;
      float var28 = -var14 * var1;
      float var29 = -var14 * var2;
      float var30 = -var14 * var3;
      float var31 = var15 - var14 * var4;
      float var32 = this.m00 * var16 + this.m10 * var17 + this.m20 * var18 + this.m30 * var19;
      float var33 = this.m01 * var16 + this.m11 * var17 + this.m21 * var18 + this.m31 * var19;
      float var34 = this.m02 * var16 + this.m12 * var17 + this.m22 * var18 + this.m32 * var19;
      float var35 = this.m00 * var20 + this.m10 * var21 + this.m20 * var22 + this.m30 * var23;
      float var36 = this.m01 * var20 + this.m11 * var21 + this.m21 * var22 + this.m31 * var23;
      float var37 = this.m02 * var20 + this.m12 * var21 + this.m22 * var22 + this.m32 * var23;
      float var38 = this.m00 * var24 + this.m10 * var25 + this.m20 * var26 + this.m30 * var27;
      float var39 = this.m01 * var24 + this.m11 * var25 + this.m21 * var26 + this.m31 * var27;
      float var40 = this.m02 * var24 + this.m12 * var25 + this.m22 * var26 + this.m32 * var27;
      var9.m30 = this.m00 * var28 + this.m10 * var29 + this.m20 * var30 + this.m30 * var31;
      var9.m31 = this.m01 * var28 + this.m11 * var29 + this.m21 * var30 + this.m31 * var31;
      var9.m32 = this.m02 * var28 + this.m12 * var29 + this.m22 * var30 + this.m32 * var31;
      var9.m00 = var32;
      var9.m01 = var33;
      var9.m02 = var34;
      var9.m10 = var35;
      var9.m11 = var36;
      var9.m12 = var37;
      var9.m20 = var38;
      var9.m21 = var39;
      var9.m22 = var40;
      var9.properties = this.properties & -29;
      return var9;
   }

   public Matrix4x3f shadow(Vector4fc var1, Matrix4x3fc var2, Matrix4x3f var3) {
      float var4 = var2.m10();
      float var5 = var2.m11();
      float var6 = var2.m12();
      float var7 = -var4 * var2.m30() - var5 * var2.m31() - var6 * var2.m32();
      return this.shadow(var1.x(), var1.y(), var1.z(), var1.w(), var4, var5, var6, var7, var3);
   }

   public Matrix4x3f shadow(Vector4fc var1, Matrix4x3fc var2) {
      return this.shadow(var1, var2, this);
   }

   public Matrix4x3f shadow(float var1, float var2, float var3, float var4, Matrix4x3fc var5, Matrix4x3f var6) {
      float var7 = var5.m10();
      float var8 = var5.m11();
      float var9 = var5.m12();
      float var10 = -var7 * var5.m30() - var8 * var5.m31() - var9 * var5.m32();
      return this.shadow(var1, var2, var3, var4, var7, var8, var9, var10, var6);
   }

   public Matrix4x3f shadow(float var1, float var2, float var3, float var4, Matrix4x3f var5) {
      return this.shadow(var1, var2, var3, var4, var5, this);
   }

   public Matrix4x3f billboardCylindrical(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      float var4 = var2.x() - var1.x();
      float var5 = var2.y() - var1.y();
      float var6 = var2.z() - var1.z();
      float var7 = var3.y() * var6 - var3.z() * var5;
      float var8 = var3.z() * var4 - var3.x() * var6;
      float var9 = var3.x() * var5 - var3.y() * var4;
      float var10 = Math.invsqrt(var7 * var7 + var8 * var8 + var9 * var9);
      var7 *= var10;
      var8 *= var10;
      var9 *= var10;
      var4 = var8 * var3.z() - var9 * var3.y();
      var5 = var9 * var3.x() - var7 * var3.z();
      var6 = var7 * var3.y() - var8 * var3.x();
      float var11 = Math.invsqrt(var4 * var4 + var5 * var5 + var6 * var6);
      var4 *= var11;
      var5 *= var11;
      var6 *= var11;
      this.m00 = var7;
      this.m01 = var8;
      this.m02 = var9;
      this.m10 = var3.x();
      this.m11 = var3.y();
      this.m12 = var3.z();
      this.m20 = var4;
      this.m21 = var5;
      this.m22 = var6;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
   }

   public Matrix4x3f billboardSpherical(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      float var4 = var2.x() - var1.x();
      float var5 = var2.y() - var1.y();
      float var6 = var2.z() - var1.z();
      float var7 = Math.invsqrt(var4 * var4 + var5 * var5 + var6 * var6);
      var4 *= var7;
      var5 *= var7;
      var6 *= var7;
      float var8 = var3.y() * var6 - var3.z() * var5;
      float var9 = var3.z() * var4 - var3.x() * var6;
      float var10 = var3.x() * var5 - var3.y() * var4;
      float var11 = Math.invsqrt(var8 * var8 + var9 * var9 + var10 * var10);
      var8 *= var11;
      var9 *= var11;
      var10 *= var11;
      float var12 = var5 * var10 - var6 * var9;
      float var13 = var6 * var8 - var4 * var10;
      float var14 = var4 * var9 - var5 * var8;
      this.m00 = var8;
      this.m01 = var9;
      this.m02 = var10;
      this.m10 = var12;
      this.m11 = var13;
      this.m12 = var14;
      this.m20 = var4;
      this.m21 = var5;
      this.m22 = var6;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
   }

   public Matrix4x3f billboardSpherical(Vector3fc var1, Vector3fc var2) {
      float var3 = var2.x() - var1.x();
      float var4 = var2.y() - var1.y();
      float var5 = var2.z() - var1.z();
      float var6 = -var4;
      float var8 = Math.sqrt(var3 * var3 + var4 * var4 + var5 * var5) + var5;
      float var9 = Math.invsqrt(var6 * var6 + var3 * var3 + var8 * var8);
      var6 *= var9;
      float var7 = var3 * var9;
      var8 *= var9;
      float var10 = (var6 + var6) * var6;
      float var11 = (var7 + var7) * var7;
      float var12 = (var6 + var6) * var7;
      float var13 = (var6 + var6) * var8;
      float var14 = (var7 + var7) * var8;
      this.m00 = 1.0F - var11;
      this.m01 = var12;
      this.m02 = -var14;
      this.m10 = var12;
      this.m11 = 1.0F - var10;
      this.m12 = var13;
      this.m20 = var14;
      this.m21 = -var13;
      this.m22 = 1.0F - var11 - var10;
      this.m30 = var1.x();
      this.m31 = var1.y();
      this.m32 = var1.z();
      this.properties = 16;
      return this;
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
      var2 = 31 * var2 + Float.floatToIntBits(this.m30);
      var2 = 31 * var2 + Float.floatToIntBits(this.m31);
      var2 = 31 * var2 + Float.floatToIntBits(this.m32);
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4x3f)) {
         return false;
      } else {
         Matrix4x3f var2 = (Matrix4x3f)var1;
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
         } else if (Float.floatToIntBits(this.m22) != Float.floatToIntBits(var2.m22)) {
            return false;
         } else if (Float.floatToIntBits(this.m30) != Float.floatToIntBits(var2.m30)) {
            return false;
         } else if (Float.floatToIntBits(this.m31) != Float.floatToIntBits(var2.m31)) {
            return false;
         } else {
            return Float.floatToIntBits(this.m32) == Float.floatToIntBits(var2.m32);
         }
      }
   }

   public boolean equals(Matrix4x3fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4x3f)) {
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

   public Matrix4x3f pick(float var1, float var2, float var3, float var4, int[] var5, Matrix4x3f var6) {
      float var7 = (float)var5[2] / var3;
      float var8 = (float)var5[3] / var4;
      float var9 = ((float)var5[2] + 2.0F * ((float)var5[0] - var1)) / var3;
      float var10 = ((float)var5[3] + 2.0F * ((float)var5[1] - var2)) / var4;
      var6.m30 = this.m00 * var9 + this.m10 * var10 + this.m30;
      var6.m31 = this.m01 * var9 + this.m11 * var10 + this.m31;
      var6.m32 = this.m02 * var9 + this.m12 * var10 + this.m32;
      var6.m00 = this.m00 * var7;
      var6.m01 = this.m01 * var7;
      var6.m02 = this.m02 * var7;
      var6.m10 = this.m10 * var8;
      var6.m11 = this.m11 * var8;
      var6.m12 = this.m12 * var8;
      var6.properties = 0;
      return var6;
   }

   public Matrix4x3f pick(float var1, float var2, float var3, float var4, int[] var5) {
      return this.pick(var1, var2, var3, var4, var5, this);
   }

   public Matrix4x3f swap(Matrix4x3f var1) {
      MemUtil.INSTANCE.swap(this, var1);
      int var2 = this.properties;
      this.properties = var1.properties;
      var1.properties = var2;
      return this;
   }

   public Matrix4x3f arcball(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
      float var8 = this.m20 * -var1 + this.m30;
      float var9 = this.m21 * -var1 + this.m31;
      float var10 = this.m22 * -var1 + this.m32;
      float var11 = Math.sin(var5);
      float var12 = Math.cosFromSin(var11, var5);
      float var13 = this.m10 * var12 + this.m20 * var11;
      float var14 = this.m11 * var12 + this.m21 * var11;
      float var15 = this.m12 * var12 + this.m22 * var11;
      float var16 = this.m20 * var12 - this.m10 * var11;
      float var17 = this.m21 * var12 - this.m11 * var11;
      float var18 = this.m22 * var12 - this.m12 * var11;
      var11 = Math.sin(var6);
      var12 = Math.cosFromSin(var11, var6);
      float var19 = this.m00 * var12 - var16 * var11;
      float var20 = this.m01 * var12 - var17 * var11;
      float var21 = this.m02 * var12 - var18 * var11;
      float var22 = this.m00 * var11 + var16 * var12;
      float var23 = this.m01 * var11 + var17 * var12;
      float var24 = this.m02 * var11 + var18 * var12;
      var7.m30 = -var19 * var2 - var13 * var3 - var22 * var4 + var8;
      var7.m31 = -var20 * var2 - var14 * var3 - var23 * var4 + var9;
      var7.m32 = -var21 * var2 - var15 * var3 - var24 * var4 + var10;
      var7.m20 = var22;
      var7.m21 = var23;
      var7.m22 = var24;
      var7.m10 = var13;
      var7.m11 = var14;
      var7.m12 = var15;
      var7.m00 = var19;
      var7.m01 = var20;
      var7.m02 = var21;
      var7.properties = this.properties & -13;
      return var7;
   }

   public Matrix4x3f arcball(float var1, Vector3fc var2, float var3, float var4, Matrix4x3f var5) {
      return this.arcball(var1, var2.x(), var2.y(), var2.z(), var3, var4, var5);
   }

   public Matrix4x3f arcball(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.arcball(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4x3f arcball(float var1, Vector3fc var2, float var3, float var4) {
      return this.arcball(var1, var2.x(), var2.y(), var2.z(), var3, var4, this);
   }

   public Matrix4x3f transformAab(float var1, float var2, float var3, float var4, float var5, float var6, Vector3f var7, Vector3f var8) {
      float var9 = this.m00 * var1;
      float var10 = this.m01 * var1;
      float var11 = this.m02 * var1;
      float var12 = this.m00 * var4;
      float var13 = this.m01 * var4;
      float var14 = this.m02 * var4;
      float var15 = this.m10 * var2;
      float var16 = this.m11 * var2;
      float var17 = this.m12 * var2;
      float var18 = this.m10 * var5;
      float var19 = this.m11 * var5;
      float var20 = this.m12 * var5;
      float var21 = this.m20 * var3;
      float var22 = this.m21 * var3;
      float var23 = this.m22 * var3;
      float var24 = this.m20 * var6;
      float var25 = this.m21 * var6;
      float var26 = this.m22 * var6;
      float var27;
      float var36;
      if (var9 < var12) {
         var27 = var9;
         var36 = var12;
      } else {
         var27 = var12;
         var36 = var9;
      }

      float var28;
      float var37;
      if (var10 < var13) {
         var28 = var10;
         var37 = var13;
      } else {
         var28 = var13;
         var37 = var10;
      }

      float var29;
      float var38;
      if (var11 < var14) {
         var29 = var11;
         var38 = var14;
      } else {
         var29 = var14;
         var38 = var11;
      }

      float var30;
      float var39;
      if (var15 < var18) {
         var30 = var15;
         var39 = var18;
      } else {
         var30 = var18;
         var39 = var15;
      }

      float var31;
      float var40;
      if (var16 < var19) {
         var31 = var16;
         var40 = var19;
      } else {
         var31 = var19;
         var40 = var16;
      }

      float var32;
      float var41;
      if (var17 < var20) {
         var32 = var17;
         var41 = var20;
      } else {
         var32 = var20;
         var41 = var17;
      }

      float var33;
      float var42;
      if (var21 < var24) {
         var33 = var21;
         var42 = var24;
      } else {
         var33 = var24;
         var42 = var21;
      }

      float var34;
      float var43;
      if (var22 < var25) {
         var34 = var22;
         var43 = var25;
      } else {
         var34 = var25;
         var43 = var22;
      }

      float var35;
      float var44;
      if (var23 < var26) {
         var35 = var23;
         var44 = var26;
      } else {
         var35 = var26;
         var44 = var23;
      }

      var7.x = var27 + var30 + var33 + this.m30;
      var7.y = var28 + var31 + var34 + this.m31;
      var7.z = var29 + var32 + var35 + this.m32;
      var8.x = var36 + var39 + var42 + this.m30;
      var8.y = var37 + var40 + var43 + this.m31;
      var8.z = var38 + var41 + var44 + this.m32;
      return this;
   }

   public Matrix4x3f transformAab(Vector3fc var1, Vector3fc var2, Vector3f var3, Vector3f var4) {
      return this.transformAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3, var4);
   }

   public Matrix4x3f lerp(Matrix4x3fc var1, float var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix4x3f lerp(Matrix4x3fc var1, float var2, Matrix4x3f var3) {
      var3.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var3.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var3.m02 = Math.fma(var1.m02() - this.m02, var2, this.m02);
      var3.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var3.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      var3.m12 = Math.fma(var1.m12() - this.m12, var2, this.m12);
      var3.m20 = Math.fma(var1.m20() - this.m20, var2, this.m20);
      var3.m21 = Math.fma(var1.m21() - this.m21, var2, this.m21);
      var3.m22 = Math.fma(var1.m22() - this.m22, var2, this.m22);
      var3.m30 = Math.fma(var1.m30() - this.m30, var2, this.m30);
      var3.m31 = Math.fma(var1.m31() - this.m31, var2, this.m31);
      var3.m32 = Math.fma(var1.m32() - this.m32, var2, this.m32);
      var3.properties = this.properties & var1.properties();
      return var3;
   }

   public Matrix4x3f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix4x3f var3) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4x3f rotateTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4x3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.rotateTowards(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4x3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7) {
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
      var7.m30 = this.m30;
      var7.m31 = this.m31;
      var7.m32 = this.m32;
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
      var7.properties = this.properties & -13;
      return var7;
   }

   public Matrix4x3f rotationTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotationTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4x3f rotationTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
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
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = 0.0F;
      this.properties = 16;
      return this;
   }

   public Matrix4x3f translationRotateTowards(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.translationRotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4x3f translationRotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = Math.invsqrt(var4 * var4 + var5 * var5 + var6 * var6);
      float var11 = var4 * var10;
      float var12 = var5 * var10;
      float var13 = var6 * var10;
      float var14 = var8 * var13 - var9 * var12;
      float var15 = var9 * var11 - var7 * var13;
      float var16 = var7 * var12 - var8 * var11;
      float var17 = Math.invsqrt(var14 * var14 + var15 * var15 + var16 * var16);
      var14 *= var17;
      var15 *= var17;
      var16 *= var17;
      float var18 = var12 * var16 - var13 * var15;
      float var19 = var13 * var14 - var11 * var16;
      float var20 = var11 * var15 - var12 * var14;
      this.m00 = var14;
      this.m01 = var15;
      this.m02 = var16;
      this.m10 = var18;
      this.m11 = var19;
      this.m12 = var20;
      this.m20 = var11;
      this.m21 = var12;
      this.m22 = var13;
      this.m30 = var1;
      this.m31 = var2;
      this.m32 = var3;
      this.properties = 16;
      return this;
   }

   public Vector3f getEulerAnglesZYX(Vector3f var1) {
      var1.x = Math.atan2(this.m12, this.m22);
      var1.y = Math.atan2(-this.m02, Math.sqrt(this.m12 * this.m12 + this.m22 * this.m22));
      var1.z = Math.atan2(this.m01, this.m00);
      return var1;
   }

   public Matrix4x3f obliqueZ(float var1, float var2) {
      this.m20 += this.m00 * var1 + this.m10 * var2;
      this.m21 += this.m01 * var1 + this.m11 * var2;
      this.m22 += this.m02 * var1 + this.m12 * var2;
      this.properties = 0;
      return this;
   }

   public Matrix4x3f obliqueZ(float var1, float var2, Matrix4x3f var3) {
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m02 = this.m02;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      var3.m12 = this.m12;
      var3.m20 = this.m00 * var1 + this.m10 * var2 + this.m20;
      var3.m21 = this.m01 * var1 + this.m11 * var2 + this.m21;
      var3.m22 = this.m02 * var1 + this.m12 * var2 + this.m22;
      var3.m30 = this.m30;
      var3.m31 = this.m31;
      var3.m32 = this.m32;
      var3.properties = 0;
      return var3;
   }

   public Matrix4x3f withLookAtUp(Vector3fc var1) {
      return this.withLookAtUp(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix4x3f withLookAtUp(Vector3fc var1, Matrix4x3f var2) {
      return this.withLookAtUp(var1.x(), var1.y(), var1.z());
   }

   public Matrix4x3f withLookAtUp(float var1, float var2, float var3) {
      return this.withLookAtUp(var1, var2, var3, this);
   }

   public Matrix4x3f withLookAtUp(float var1, float var2, float var3, Matrix4x3f var4) {
      float var5 = (var2 * this.m21 - var3 * this.m11) * this.m02 + (var3 * this.m01 - var1 * this.m21) * this.m12 + (var1 * this.m11 - var2 * this.m01) * this.m22;
      float var6 = var1 * this.m01 + var2 * this.m11 + var3 * this.m21;
      if ((this.properties & 16) == 0) {
         var6 *= Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
      }

      float var7 = Math.invsqrt(var5 * var5 + var6 * var6);
      float var8 = var6 * var7;
      float var9 = var5 * var7;
      float var10 = var8 * this.m00 - var9 * this.m01;
      float var11 = var8 * this.m10 - var9 * this.m11;
      float var12 = var8 * this.m20 - var9 * this.m21;
      float var13 = var9 * this.m30 + var8 * this.m31;
      float var14 = var9 * this.m00 + var8 * this.m01;
      float var15 = var9 * this.m10 + var8 * this.m11;
      float var16 = var9 * this.m20 + var8 * this.m21;
      float var17 = var8 * this.m30 - var9 * this.m31;
      var4._m00(var10)._m10(var11)._m20(var12)._m30(var17)._m01(var14)._m11(var15)._m21(var16)._m31(var13);
      if (var4 != this) {
         var4._m02(this.m02)._m12(this.m12)._m22(this.m22)._m32(this.m32);
      }

      var4.properties = this.properties & -13;
      return var4;
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32);
   }
}
