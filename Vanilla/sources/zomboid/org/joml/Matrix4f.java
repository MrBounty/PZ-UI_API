package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix4f implements Externalizable, Matrix4fc {
   private static final long serialVersionUID = 1L;
   float m00;
   float m01;
   float m02;
   float m03;
   float m10;
   float m11;
   float m12;
   float m13;
   float m20;
   float m21;
   float m22;
   float m23;
   float m30;
   float m31;
   float m32;
   float m33;
   int properties;

   public Matrix4f() {
      this._m00(1.0F)._m11(1.0F)._m22(1.0F)._m33(1.0F)._properties(30);
   }

   public Matrix4f(Matrix3fc var1) {
      this.set(var1);
   }

   public Matrix4f(Matrix4fc var1) {
      this.set(var1);
   }

   public Matrix4f(Matrix4x3fc var1) {
      this.set(var1);
   }

   public Matrix4f(Matrix4dc var1) {
      this.set(var1);
   }

   public Matrix4f(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      this._m00(var1)._m01(var2)._m02(var3)._m03(var4)._m10(var5)._m11(var6)._m12(var7)._m13(var8)._m20(var9)._m21(var10)._m22(var11)._m23(var12)._m30(var13)._m31(var14)._m32(var15)._m33(var16).determineProperties();
   }

   public Matrix4f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      this.determineProperties();
   }

   public Matrix4f(Vector4fc var1, Vector4fc var2, Vector4fc var3, Vector4fc var4) {
      this.set(var1, var2, var3, var4);
   }

   Matrix4f _properties(int var1) {
      this.properties = var1;
      return this;
   }

   public Matrix4f assume(int var1) {
      this._properties(var1);
      return this;
   }

   public Matrix4f determineProperties() {
      int var1 = 0;
      if (this.m03 == 0.0F && this.m13 == 0.0F) {
         if (this.m23 == 0.0F && this.m33 == 1.0F) {
            var1 |= 2;
            if (this.m00 == 1.0F && this.m01 == 0.0F && this.m02 == 0.0F && this.m10 == 0.0F && this.m11 == 1.0F && this.m12 == 0.0F && this.m20 == 0.0F && this.m21 == 0.0F && this.m22 == 1.0F) {
               var1 |= 24;
               if (this.m30 == 0.0F && this.m31 == 0.0F && this.m32 == 0.0F) {
                  var1 |= 4;
               }
            }
         } else if (this.m01 == 0.0F && this.m02 == 0.0F && this.m10 == 0.0F && this.m12 == 0.0F && this.m20 == 0.0F && this.m21 == 0.0F && this.m30 == 0.0F && this.m31 == 0.0F && this.m33 == 0.0F) {
            var1 |= 1;
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

   public float m03() {
      return this.m03;
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

   public float m13() {
      return this.m13;
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

   public float m23() {
      return this.m23;
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

   public float m33() {
      return this.m33;
   }

   public Matrix4f m00(float var1) {
      this.m00 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4f m01(float var1) {
      this.m01 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m02(float var1) {
      this.m02 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m03(float var1) {
      this.m03 = var1;
      if (var1 != 0.0F) {
         this.properties = 0;
      }

      return this;
   }

   public Matrix4f m10(float var1) {
      this.m10 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m11(float var1) {
      this.m11 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4f m12(float var1) {
      this.m12 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m13(float var1) {
      this.m13 = var1;
      if (var1 != 0.0F) {
         this.properties = 0;
      }

      return this;
   }

   public Matrix4f m20(float var1) {
      this.m20 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m21(float var1) {
      this.m21 = var1;
      this.properties &= -17;
      if (var1 != 0.0F) {
         this.properties &= -14;
      }

      return this;
   }

   public Matrix4f m22(float var1) {
      this.m22 = var1;
      this.properties &= -17;
      if (var1 != 1.0F) {
         this.properties &= -13;
      }

      return this;
   }

   public Matrix4f m23(float var1) {
      this.m23 = var1;
      if (var1 != 0.0F) {
         this.properties &= -31;
      }

      return this;
   }

   public Matrix4f m30(float var1) {
      this.m30 = var1;
      if (var1 != 0.0F) {
         this.properties &= -6;
      }

      return this;
   }

   public Matrix4f m31(float var1) {
      this.m31 = var1;
      if (var1 != 0.0F) {
         this.properties &= -6;
      }

      return this;
   }

   public Matrix4f m32(float var1) {
      this.m32 = var1;
      if (var1 != 0.0F) {
         this.properties &= -6;
      }

      return this;
   }

   public Matrix4f m33(float var1) {
      this.m33 = var1;
      if (var1 != 0.0F) {
         this.properties &= -2;
      }

      if (var1 != 1.0F) {
         this.properties &= -31;
      }

      return this;
   }

   Matrix4f _m00(float var1) {
      this.m00 = var1;
      return this;
   }

   Matrix4f _m01(float var1) {
      this.m01 = var1;
      return this;
   }

   Matrix4f _m02(float var1) {
      this.m02 = var1;
      return this;
   }

   Matrix4f _m03(float var1) {
      this.m03 = var1;
      return this;
   }

   Matrix4f _m10(float var1) {
      this.m10 = var1;
      return this;
   }

   Matrix4f _m11(float var1) {
      this.m11 = var1;
      return this;
   }

   Matrix4f _m12(float var1) {
      this.m12 = var1;
      return this;
   }

   Matrix4f _m13(float var1) {
      this.m13 = var1;
      return this;
   }

   Matrix4f _m20(float var1) {
      this.m20 = var1;
      return this;
   }

   Matrix4f _m21(float var1) {
      this.m21 = var1;
      return this;
   }

   Matrix4f _m22(float var1) {
      this.m22 = var1;
      return this;
   }

   Matrix4f _m23(float var1) {
      this.m23 = var1;
      return this;
   }

   Matrix4f _m30(float var1) {
      this.m30 = var1;
      return this;
   }

   Matrix4f _m31(float var1) {
      this.m31 = var1;
      return this;
   }

   Matrix4f _m32(float var1) {
      this.m32 = var1;
      return this;
   }

   Matrix4f _m33(float var1) {
      this.m33 = var1;
      return this;
   }

   public Matrix4f identity() {
      return (this.properties & 4) != 0 ? this : this._m00(1.0F)._m01(0.0F)._m02(0.0F)._m03(0.0F)._m10(0.0F)._m11(1.0F)._m12(0.0F)._m13(0.0F)._m20(0.0F)._m21(0.0F)._m22(1.0F)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(30);
   }

   public Matrix4f set(Matrix4fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(var1.m03())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(var1.m13())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(var1.m23())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32())._m33(var1.m33())._properties(var1.properties());
   }

   public Matrix4f setTransposed(Matrix4fc var1) {
      return (var1.properties() & 4) != 0 ? this.identity() : this.setTransposedInternal(var1);
   }

   private Matrix4f setTransposedInternal(Matrix4fc var1) {
      float var2 = var1.m01();
      float var3 = var1.m21();
      float var4 = var1.m31();
      float var5 = var1.m02();
      float var6 = var1.m12();
      float var7 = var1.m03();
      float var8 = var1.m13();
      float var9 = var1.m23();
      return this._m00(var1.m00())._m01(var1.m10())._m02(var1.m20())._m03(var1.m30())._m10(var2)._m11(var1.m11())._m12(var3)._m13(var4)._m20(var5)._m21(var6)._m22(var1.m22())._m23(var1.m32())._m30(var7)._m31(var8)._m32(var9)._m33(var1.m33())._properties(var1.properties() & 4);
   }

   public Matrix4f set(Matrix4x3fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(0.0F)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(0.0F)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(0.0F)._m30(var1.m30())._m31(var1.m31())._m32(var1.m32())._m33(1.0F)._properties(var1.properties() | 2);
   }

   public Matrix4f set(Matrix4dc var1) {
      return this._m00((float)var1.m00())._m01((float)var1.m01())._m02((float)var1.m02())._m03((float)var1.m03())._m10((float)var1.m10())._m11((float)var1.m11())._m12((float)var1.m12())._m13((float)var1.m13())._m20((float)var1.m20())._m21((float)var1.m21())._m22((float)var1.m22())._m23((float)var1.m23())._m30((float)var1.m30())._m31((float)var1.m31())._m32((float)var1.m32())._m33((float)var1.m33())._properties(var1.properties());
   }

   public Matrix4f set(Matrix3fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(0.0F)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(0.0F)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(2);
   }

   public Matrix4f set(AxisAngle4f var1) {
      float var2 = var1.x;
      float var3 = var1.y;
      float var4 = var1.z;
      float var5 = var1.angle;
      double var6 = (double)Math.sqrt(var2 * var2 + var3 * var3 + var4 * var4);
      var6 = 1.0D / var6;
      var2 = (float)((double)var2 * var6);
      var3 = (float)((double)var3 * var6);
      var4 = (float)((double)var4 * var6);
      float var8 = Math.sin(var5);
      float var9 = Math.cosFromSin(var8, var5);
      float var10 = 1.0F - var9;
      this._m00(var9 + var2 * var2 * var10)._m11(var9 + var3 * var3 * var10)._m22(var9 + var4 * var4 * var10);
      float var11 = var2 * var3 * var10;
      float var12 = var4 * var8;
      this._m10(var11 - var12)._m01(var11 + var12);
      var11 = var2 * var4 * var10;
      var12 = var3 * var8;
      this._m20(var11 + var12)._m02(var11 - var12);
      var11 = var3 * var4 * var10;
      var12 = var2 * var8;
      return this._m21(var11 - var12)._m12(var11 + var12)._m03(0.0F)._m13(0.0F)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
   }

   public Matrix4f set(AxisAngle4d var1) {
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
      this._m00((float)(var14 + var2 * var2 * var16))._m11((float)(var14 + var4 * var4 * var16))._m22((float)(var14 + var6 * var6 * var16));
      double var18 = var2 * var4 * var16;
      double var20 = var6 * var12;
      this._m10((float)(var18 - var20))._m01((float)(var18 + var20));
      var18 = var2 * var6 * var16;
      var20 = var4 * var12;
      this._m20((float)(var18 + var20))._m02((float)(var18 - var20));
      var18 = var4 * var6 * var16;
      var20 = var2 * var12;
      return this._m21((float)(var18 - var20))._m12((float)(var18 + var20))._m03(0.0F)._m13(0.0F)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
   }

   public Matrix4f set(Quaternionfc var1) {
      return this.rotation(var1);
   }

   public Matrix4f set(Quaterniondc var1) {
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
      return this._m00((float)(var2 + var4 - var8 - var6))._m01((float)(var12 + var10 + var10 + var12))._m02((float)(var14 - var16 + var14 - var16))._m03(0.0F)._m10((float)(-var10 + var12 - var10 + var12))._m11((float)(var6 - var8 + var2 - var4))._m12((float)(var18 + var18 + var20 + var20))._m13(0.0F)._m20((float)(var16 + var14 + var14 + var16))._m21((float)(var18 + var18 - var20 - var20))._m22((float)(var8 - var6 - var4 + var2))._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
   }

   public Matrix4f set3x3(Matrix4f var1) {
      MemUtil.INSTANCE.copy3x3(var1, this);
      return this._properties(this.properties & var1.properties & -2);
   }

   public Matrix4f set4x3(Matrix4x3fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32())._properties(this.properties & var1.properties() & -2);
   }

   public Matrix4f set4x3(Matrix4f var1) {
      MemUtil.INSTANCE.copy4x3(var1, this);
      return this._properties(this.properties & var1.properties & -2);
   }

   public Matrix4f mul(Matrix4fc var1) {
      return this.mul(var1, this);
   }

   public Matrix4f mul(Matrix4fc var1, Matrix4f var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4fc)this);
      } else if ((this.properties & 8) != 0 && (var1.properties() & 2) != 0) {
         return this.mulTranslationAffine(var1, var2);
      } else if ((this.properties & 2) != 0 && (var1.properties() & 2) != 0) {
         return this.mulAffine(var1, var2);
      } else if ((this.properties & 1) != 0 && (var1.properties() & 2) != 0) {
         return this.mulPerspectiveAffine(var1, var2);
      } else {
         return (var1.properties() & 2) != 0 ? this.mulAffineR(var1, var2) : this.mul0(var1, var2);
      }
   }

   public Matrix4f mul0(Matrix4fc var1) {
      return this.mul0(var1, this);
   }

   public Matrix4f mul0(Matrix4fc var1, Matrix4f var2) {
      float var3 = Math.fma(this.m00, var1.m00(), Math.fma(this.m10, var1.m01(), Math.fma(this.m20, var1.m02(), this.m30 * var1.m03())));
      float var4 = Math.fma(this.m01, var1.m00(), Math.fma(this.m11, var1.m01(), Math.fma(this.m21, var1.m02(), this.m31 * var1.m03())));
      float var5 = Math.fma(this.m02, var1.m00(), Math.fma(this.m12, var1.m01(), Math.fma(this.m22, var1.m02(), this.m32 * var1.m03())));
      float var6 = Math.fma(this.m03, var1.m00(), Math.fma(this.m13, var1.m01(), Math.fma(this.m23, var1.m02(), this.m33 * var1.m03())));
      float var7 = Math.fma(this.m00, var1.m10(), Math.fma(this.m10, var1.m11(), Math.fma(this.m20, var1.m12(), this.m30 * var1.m13())));
      float var8 = Math.fma(this.m01, var1.m10(), Math.fma(this.m11, var1.m11(), Math.fma(this.m21, var1.m12(), this.m31 * var1.m13())));
      float var9 = Math.fma(this.m02, var1.m10(), Math.fma(this.m12, var1.m11(), Math.fma(this.m22, var1.m12(), this.m32 * var1.m13())));
      float var10 = Math.fma(this.m03, var1.m10(), Math.fma(this.m13, var1.m11(), Math.fma(this.m23, var1.m12(), this.m33 * var1.m13())));
      float var11 = Math.fma(this.m00, var1.m20(), Math.fma(this.m10, var1.m21(), Math.fma(this.m20, var1.m22(), this.m30 * var1.m23())));
      float var12 = Math.fma(this.m01, var1.m20(), Math.fma(this.m11, var1.m21(), Math.fma(this.m21, var1.m22(), this.m31 * var1.m23())));
      float var13 = Math.fma(this.m02, var1.m20(), Math.fma(this.m12, var1.m21(), Math.fma(this.m22, var1.m22(), this.m32 * var1.m23())));
      float var14 = Math.fma(this.m03, var1.m20(), Math.fma(this.m13, var1.m21(), Math.fma(this.m23, var1.m22(), this.m33 * var1.m23())));
      float var15 = Math.fma(this.m00, var1.m30(), Math.fma(this.m10, var1.m31(), Math.fma(this.m20, var1.m32(), this.m30 * var1.m33())));
      float var16 = Math.fma(this.m01, var1.m30(), Math.fma(this.m11, var1.m31(), Math.fma(this.m21, var1.m32(), this.m31 * var1.m33())));
      float var17 = Math.fma(this.m02, var1.m30(), Math.fma(this.m12, var1.m31(), Math.fma(this.m22, var1.m32(), this.m32 * var1.m33())));
      float var18 = Math.fma(this.m03, var1.m30(), Math.fma(this.m13, var1.m31(), Math.fma(this.m23, var1.m32(), this.m33 * var1.m33())));
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(0);
   }

   public Matrix4f mul(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      return this.mul(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, this);
   }

   public Matrix4f mul(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, Matrix4f var17) {
      if ((this.properties & 4) != 0) {
         return var17.set(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
      } else {
         return (this.properties & 2) != 0 ? this.mulAffineL(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17) : this.mulGeneric(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      }
   }

   private Matrix4f mulAffineL(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, Matrix4f var17) {
      float var18 = Math.fma(this.m00, var1, Math.fma(this.m10, var2, Math.fma(this.m20, var3, this.m30 * var4)));
      float var19 = Math.fma(this.m01, var1, Math.fma(this.m11, var2, Math.fma(this.m21, var3, this.m31 * var4)));
      float var20 = Math.fma(this.m02, var1, Math.fma(this.m12, var2, Math.fma(this.m22, var3, this.m32 * var4)));
      float var22 = Math.fma(this.m00, var5, Math.fma(this.m10, var6, Math.fma(this.m20, var7, this.m30 * var8)));
      float var23 = Math.fma(this.m01, var5, Math.fma(this.m11, var6, Math.fma(this.m21, var7, this.m31 * var8)));
      float var24 = Math.fma(this.m02, var5, Math.fma(this.m12, var6, Math.fma(this.m22, var7, this.m32 * var8)));
      float var26 = Math.fma(this.m00, var9, Math.fma(this.m10, var10, Math.fma(this.m20, var11, this.m30 * var12)));
      float var27 = Math.fma(this.m01, var9, Math.fma(this.m11, var10, Math.fma(this.m21, var11, this.m31 * var12)));
      float var28 = Math.fma(this.m02, var9, Math.fma(this.m12, var10, Math.fma(this.m22, var11, this.m32 * var12)));
      float var30 = Math.fma(this.m00, var13, Math.fma(this.m10, var14, Math.fma(this.m20, var15, this.m30 * var16)));
      float var31 = Math.fma(this.m01, var13, Math.fma(this.m11, var14, Math.fma(this.m21, var15, this.m31 * var16)));
      float var32 = Math.fma(this.m02, var13, Math.fma(this.m12, var14, Math.fma(this.m22, var15, this.m32 * var16)));
      return var17._m00(var18)._m01(var19)._m02(var20)._m03(var4)._m10(var22)._m11(var23)._m12(var24)._m13(var8)._m20(var26)._m21(var27)._m22(var28)._m23(var12)._m30(var30)._m31(var31)._m32(var32)._m33(var16)._properties(2);
   }

   private Matrix4f mulGeneric(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, Matrix4f var17) {
      float var18 = Math.fma(this.m00, var1, Math.fma(this.m10, var2, Math.fma(this.m20, var3, this.m30 * var4)));
      float var19 = Math.fma(this.m01, var1, Math.fma(this.m11, var2, Math.fma(this.m21, var3, this.m31 * var4)));
      float var20 = Math.fma(this.m02, var1, Math.fma(this.m12, var2, Math.fma(this.m22, var3, this.m32 * var4)));
      float var21 = Math.fma(this.m03, var1, Math.fma(this.m13, var2, Math.fma(this.m23, var3, this.m33 * var4)));
      float var22 = Math.fma(this.m00, var5, Math.fma(this.m10, var6, Math.fma(this.m20, var7, this.m30 * var8)));
      float var23 = Math.fma(this.m01, var5, Math.fma(this.m11, var6, Math.fma(this.m21, var7, this.m31 * var8)));
      float var24 = Math.fma(this.m02, var5, Math.fma(this.m12, var6, Math.fma(this.m22, var7, this.m32 * var8)));
      float var25 = Math.fma(this.m03, var5, Math.fma(this.m13, var6, Math.fma(this.m23, var7, this.m33 * var8)));
      float var26 = Math.fma(this.m00, var9, Math.fma(this.m10, var10, Math.fma(this.m20, var11, this.m30 * var12)));
      float var27 = Math.fma(this.m01, var9, Math.fma(this.m11, var10, Math.fma(this.m21, var11, this.m31 * var12)));
      float var28 = Math.fma(this.m02, var9, Math.fma(this.m12, var10, Math.fma(this.m22, var11, this.m32 * var12)));
      float var29 = Math.fma(this.m03, var9, Math.fma(this.m13, var10, Math.fma(this.m23, var11, this.m33 * var12)));
      float var30 = Math.fma(this.m00, var13, Math.fma(this.m10, var14, Math.fma(this.m20, var15, this.m30 * var16)));
      float var31 = Math.fma(this.m01, var13, Math.fma(this.m11, var14, Math.fma(this.m21, var15, this.m31 * var16)));
      float var32 = Math.fma(this.m02, var13, Math.fma(this.m12, var14, Math.fma(this.m22, var15, this.m32 * var16)));
      float var33 = Math.fma(this.m03, var13, Math.fma(this.m13, var14, Math.fma(this.m23, var15, this.m33 * var16)));
      return var17._m00(var18)._m01(var19)._m02(var20)._m03(var21)._m10(var22)._m11(var23)._m12(var24)._m13(var25)._m20(var26)._m21(var27)._m22(var28)._m23(var29)._m30(var30)._m31(var31)._m32(var32)._m33(var33)._properties(0);
   }

   public Matrix4f mul3x3(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      return this.mul3x3(var1, var2, var3, var4, var5, var6, var7, var8, var9, this);
   }

   public Matrix4f mul3x3(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
      return (this.properties & 4) != 0 ? var10.set(var1, var2, var3, 0.0F, var4, var5, var6, 0.0F, var7, var8, var9, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F) : this.mulGeneric3x3(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   private Matrix4f mulGeneric3x3(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
      float var11 = Math.fma(this.m00, var1, Math.fma(this.m10, var2, this.m20 * var3));
      float var12 = Math.fma(this.m01, var1, Math.fma(this.m11, var2, this.m21 * var3));
      float var13 = Math.fma(this.m02, var1, Math.fma(this.m12, var2, this.m22 * var3));
      float var14 = Math.fma(this.m03, var1, Math.fma(this.m13, var2, this.m23 * var3));
      float var15 = Math.fma(this.m00, var4, Math.fma(this.m10, var5, this.m20 * var6));
      float var16 = Math.fma(this.m01, var4, Math.fma(this.m11, var5, this.m21 * var6));
      float var17 = Math.fma(this.m02, var4, Math.fma(this.m12, var5, this.m22 * var6));
      float var18 = Math.fma(this.m03, var4, Math.fma(this.m13, var5, this.m23 * var6));
      float var19 = Math.fma(this.m00, var7, Math.fma(this.m10, var8, this.m20 * var9));
      float var20 = Math.fma(this.m01, var7, Math.fma(this.m11, var8, this.m21 * var9));
      float var21 = Math.fma(this.m02, var7, Math.fma(this.m12, var8, this.m22 * var9));
      float var22 = Math.fma(this.m03, var7, Math.fma(this.m13, var8, this.m23 * var9));
      return var10._m00(var11)._m01(var12)._m02(var13)._m03(var14)._m10(var15)._m11(var16)._m12(var17)._m13(var18)._m20(var19)._m21(var20)._m22(var21)._m23(var22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 2);
   }

   public Matrix4f mulLocal(Matrix4fc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix4f mulLocal(Matrix4fc var1, Matrix4f var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4fc)this);
      } else {
         return (this.properties & 2) != 0 && (var1.properties() & 2) != 0 ? this.mulLocalAffine(var1, var2) : this.mulLocalGeneric(var1, var2);
      }
   }

   private Matrix4f mulLocalGeneric(Matrix4fc var1, Matrix4f var2) {
      float var3 = Math.fma(var1.m00(), this.m00, Math.fma(var1.m10(), this.m01, Math.fma(var1.m20(), this.m02, var1.m30() * this.m03)));
      float var4 = Math.fma(var1.m01(), this.m00, Math.fma(var1.m11(), this.m01, Math.fma(var1.m21(), this.m02, var1.m31() * this.m03)));
      float var5 = Math.fma(var1.m02(), this.m00, Math.fma(var1.m12(), this.m01, Math.fma(var1.m22(), this.m02, var1.m32() * this.m03)));
      float var6 = Math.fma(var1.m03(), this.m00, Math.fma(var1.m13(), this.m01, Math.fma(var1.m23(), this.m02, var1.m33() * this.m03)));
      float var7 = Math.fma(var1.m00(), this.m10, Math.fma(var1.m10(), this.m11, Math.fma(var1.m20(), this.m12, var1.m30() * this.m13)));
      float var8 = Math.fma(var1.m01(), this.m10, Math.fma(var1.m11(), this.m11, Math.fma(var1.m21(), this.m12, var1.m31() * this.m13)));
      float var9 = Math.fma(var1.m02(), this.m10, Math.fma(var1.m12(), this.m11, Math.fma(var1.m22(), this.m12, var1.m32() * this.m13)));
      float var10 = Math.fma(var1.m03(), this.m10, Math.fma(var1.m13(), this.m11, Math.fma(var1.m23(), this.m12, var1.m33() * this.m13)));
      float var11 = Math.fma(var1.m00(), this.m20, Math.fma(var1.m10(), this.m21, Math.fma(var1.m20(), this.m22, var1.m30() * this.m23)));
      float var12 = Math.fma(var1.m01(), this.m20, Math.fma(var1.m11(), this.m21, Math.fma(var1.m21(), this.m22, var1.m31() * this.m23)));
      float var13 = Math.fma(var1.m02(), this.m20, Math.fma(var1.m12(), this.m21, Math.fma(var1.m22(), this.m22, var1.m32() * this.m23)));
      float var14 = Math.fma(var1.m03(), this.m20, Math.fma(var1.m13(), this.m21, Math.fma(var1.m23(), this.m22, var1.m33() * this.m23)));
      float var15 = Math.fma(var1.m00(), this.m30, Math.fma(var1.m10(), this.m31, Math.fma(var1.m20(), this.m32, var1.m30() * this.m33)));
      float var16 = Math.fma(var1.m01(), this.m30, Math.fma(var1.m11(), this.m31, Math.fma(var1.m21(), this.m32, var1.m31() * this.m33)));
      float var17 = Math.fma(var1.m02(), this.m30, Math.fma(var1.m12(), this.m31, Math.fma(var1.m22(), this.m32, var1.m32() * this.m33)));
      float var18 = Math.fma(var1.m03(), this.m30, Math.fma(var1.m13(), this.m31, Math.fma(var1.m23(), this.m32, var1.m33() * this.m33)));
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(0);
   }

   public Matrix4f mulLocalAffine(Matrix4fc var1) {
      return this.mulLocalAffine(var1, this);
   }

   public Matrix4f mulLocalAffine(Matrix4fc var1, Matrix4f var2) {
      float var3 = var1.m00() * this.m00 + var1.m10() * this.m01 + var1.m20() * this.m02;
      float var4 = var1.m01() * this.m00 + var1.m11() * this.m01 + var1.m21() * this.m02;
      float var5 = var1.m02() * this.m00 + var1.m12() * this.m01 + var1.m22() * this.m02;
      float var6 = var1.m03();
      float var7 = var1.m00() * this.m10 + var1.m10() * this.m11 + var1.m20() * this.m12;
      float var8 = var1.m01() * this.m10 + var1.m11() * this.m11 + var1.m21() * this.m12;
      float var9 = var1.m02() * this.m10 + var1.m12() * this.m11 + var1.m22() * this.m12;
      float var10 = var1.m13();
      float var11 = var1.m00() * this.m20 + var1.m10() * this.m21 + var1.m20() * this.m22;
      float var12 = var1.m01() * this.m20 + var1.m11() * this.m21 + var1.m21() * this.m22;
      float var13 = var1.m02() * this.m20 + var1.m12() * this.m21 + var1.m22() * this.m22;
      float var14 = var1.m23();
      float var15 = var1.m00() * this.m30 + var1.m10() * this.m31 + var1.m20() * this.m32 + var1.m30();
      float var16 = var1.m01() * this.m30 + var1.m11() * this.m31 + var1.m21() * this.m32 + var1.m31();
      float var17 = var1.m02() * this.m30 + var1.m12() * this.m31 + var1.m22() * this.m32 + var1.m32();
      float var18 = var1.m33();
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(2 | this.properties() & var1.properties() & 16);
   }

   public Matrix4f mul(Matrix4x3fc var1) {
      return this.mul(var1, this);
   }

   public Matrix4f mul(Matrix4x3fc var1, Matrix4f var2) {
      if ((this.properties & 4) != 0) {
         return var2.set(var1);
      } else if ((var1.properties() & 4) != 0) {
         return var2.set((Matrix4fc)this);
      } else if ((this.properties & 8) != 0) {
         return this.mulTranslation(var1, var2);
      } else if ((this.properties & 2) != 0) {
         return this.mulAffine(var1, var2);
      } else {
         return (this.properties & 1) != 0 ? this.mulPerspectiveAffine(var1, var2) : this.mulGeneric(var1, var2);
      }
   }

   private Matrix4f mulTranslation(Matrix4x3fc var1, Matrix4f var2) {
      return var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(this.m03)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(this.m13)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(this.m23)._m30(var1.m30() + this.m30)._m31(var1.m31() + this.m31)._m32(var1.m32() + this.m32)._m33(this.m33)._properties(2 | var1.properties() & 16);
   }

   private Matrix4f mulAffine(Matrix4x3fc var1, Matrix4f var2) {
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
      return var2._m00(Math.fma(var3, var12, Math.fma(var6, var13, var9 * var14)))._m01(Math.fma(var4, var12, Math.fma(var7, var13, var10 * var14)))._m02(Math.fma(var5, var12, Math.fma(var8, var13, var11 * var14)))._m03(this.m03)._m10(Math.fma(var3, var15, Math.fma(var6, var16, var9 * var17)))._m11(Math.fma(var4, var15, Math.fma(var7, var16, var10 * var17)))._m12(Math.fma(var5, var15, Math.fma(var8, var16, var11 * var17)))._m13(this.m13)._m20(Math.fma(var3, var18, Math.fma(var6, var19, var9 * var20)))._m21(Math.fma(var4, var18, Math.fma(var7, var19, var10 * var20)))._m22(Math.fma(var5, var18, Math.fma(var8, var19, var11 * var20)))._m23(this.m23)._m30(Math.fma(var3, var21, Math.fma(var6, var22, Math.fma(var9, var23, this.m30))))._m31(Math.fma(var4, var21, Math.fma(var7, var22, Math.fma(var10, var23, this.m31))))._m32(Math.fma(var5, var21, Math.fma(var8, var22, Math.fma(var11, var23, this.m32))))._m33(this.m33)._properties(2 | this.properties & var1.properties() & 16);
   }

   private Matrix4f mulGeneric(Matrix4x3fc var1, Matrix4f var2) {
      float var3 = Math.fma(this.m00, var1.m00(), Math.fma(this.m10, var1.m01(), this.m20 * var1.m02()));
      float var4 = Math.fma(this.m01, var1.m00(), Math.fma(this.m11, var1.m01(), this.m21 * var1.m02()));
      float var5 = Math.fma(this.m02, var1.m00(), Math.fma(this.m12, var1.m01(), this.m22 * var1.m02()));
      float var6 = Math.fma(this.m03, var1.m00(), Math.fma(this.m13, var1.m01(), this.m23 * var1.m02()));
      float var7 = Math.fma(this.m00, var1.m10(), Math.fma(this.m10, var1.m11(), this.m20 * var1.m12()));
      float var8 = Math.fma(this.m01, var1.m10(), Math.fma(this.m11, var1.m11(), this.m21 * var1.m12()));
      float var9 = Math.fma(this.m02, var1.m10(), Math.fma(this.m12, var1.m11(), this.m22 * var1.m12()));
      float var10 = Math.fma(this.m03, var1.m10(), Math.fma(this.m13, var1.m11(), this.m23 * var1.m12()));
      float var11 = Math.fma(this.m00, var1.m20(), Math.fma(this.m10, var1.m21(), this.m20 * var1.m22()));
      float var12 = Math.fma(this.m01, var1.m20(), Math.fma(this.m11, var1.m21(), this.m21 * var1.m22()));
      float var13 = Math.fma(this.m02, var1.m20(), Math.fma(this.m12, var1.m21(), this.m22 * var1.m22()));
      float var14 = Math.fma(this.m03, var1.m20(), Math.fma(this.m13, var1.m21(), this.m23 * var1.m22()));
      float var15 = Math.fma(this.m00, var1.m30(), Math.fma(this.m10, var1.m31(), Math.fma(this.m20, var1.m32(), this.m30)));
      float var16 = Math.fma(this.m01, var1.m30(), Math.fma(this.m11, var1.m31(), Math.fma(this.m21, var1.m32(), this.m31)));
      float var17 = Math.fma(this.m02, var1.m30(), Math.fma(this.m12, var1.m31(), Math.fma(this.m22, var1.m32(), this.m32)));
      float var18 = Math.fma(this.m03, var1.m30(), Math.fma(this.m13, var1.m31(), Math.fma(this.m23, var1.m32(), this.m33)));
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(this.properties & -30);
   }

   public Matrix4f mul(Matrix3x2fc var1) {
      return this.mul(var1, this);
   }

   public Matrix4f mul(Matrix3x2fc var1, Matrix4f var2) {
      float var3 = this.m00 * var1.m00() + this.m10 * var1.m01();
      float var4 = this.m01 * var1.m00() + this.m11 * var1.m01();
      float var5 = this.m02 * var1.m00() + this.m12 * var1.m01();
      float var6 = this.m03 * var1.m00() + this.m13 * var1.m01();
      float var7 = this.m00 * var1.m10() + this.m10 * var1.m11();
      float var8 = this.m01 * var1.m10() + this.m11 * var1.m11();
      float var9 = this.m02 * var1.m10() + this.m12 * var1.m11();
      float var10 = this.m03 * var1.m10() + this.m13 * var1.m11();
      float var11 = this.m00 * var1.m20() + this.m10 * var1.m21() + this.m30;
      float var12 = this.m01 * var1.m20() + this.m11 * var1.m21() + this.m31;
      float var13 = this.m02 * var1.m20() + this.m12 * var1.m21() + this.m32;
      float var14 = this.m03 * var1.m20() + this.m13 * var1.m21() + this.m33;
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(var11)._m31(var12)._m32(var13)._m33(var14)._properties(this.properties & -30);
   }

   public Matrix4f mulPerspectiveAffine(Matrix4fc var1) {
      return this.mulPerspectiveAffine(var1, this);
   }

   public Matrix4f mulPerspectiveAffine(Matrix4fc var1, Matrix4f var2) {
      float var3 = this.m00 * var1.m00();
      float var4 = this.m11 * var1.m01();
      float var5 = this.m22 * var1.m02();
      float var6 = this.m23 * var1.m02();
      float var7 = this.m00 * var1.m10();
      float var8 = this.m11 * var1.m11();
      float var9 = this.m22 * var1.m12();
      float var10 = this.m23 * var1.m12();
      float var11 = this.m00 * var1.m20();
      float var12 = this.m11 * var1.m21();
      float var13 = this.m22 * var1.m22();
      float var14 = this.m23 * var1.m22();
      float var15 = this.m00 * var1.m30();
      float var16 = this.m11 * var1.m31();
      float var17 = this.m22 * var1.m32() + this.m32;
      float var18 = this.m23 * var1.m32();
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(0);
   }

   public Matrix4f mulPerspectiveAffine(Matrix4x3fc var1) {
      return this.mulPerspectiveAffine(var1, this);
   }

   public Matrix4f mulPerspectiveAffine(Matrix4x3fc var1, Matrix4f var2) {
      float var3 = this.m00;
      float var4 = this.m11;
      float var5 = this.m22;
      float var6 = this.m23;
      return var2._m00(var3 * var1.m00())._m01(var4 * var1.m01())._m02(var5 * var1.m02())._m03(var6 * var1.m02())._m10(var3 * var1.m10())._m11(var4 * var1.m11())._m12(var5 * var1.m12())._m13(var6 * var1.m12())._m20(var3 * var1.m20())._m21(var4 * var1.m21())._m22(var5 * var1.m22())._m23(var6 * var1.m22())._m30(var3 * var1.m30())._m31(var4 * var1.m31())._m32(var5 * var1.m32() + this.m32)._m33(var6 * var1.m32())._properties(0);
   }

   public Matrix4f mulAffineR(Matrix4fc var1) {
      return this.mulAffineR(var1, this);
   }

   public Matrix4f mulAffineR(Matrix4fc var1, Matrix4f var2) {
      float var3 = Math.fma(this.m00, var1.m00(), Math.fma(this.m10, var1.m01(), this.m20 * var1.m02()));
      float var4 = Math.fma(this.m01, var1.m00(), Math.fma(this.m11, var1.m01(), this.m21 * var1.m02()));
      float var5 = Math.fma(this.m02, var1.m00(), Math.fma(this.m12, var1.m01(), this.m22 * var1.m02()));
      float var6 = Math.fma(this.m03, var1.m00(), Math.fma(this.m13, var1.m01(), this.m23 * var1.m02()));
      float var7 = Math.fma(this.m00, var1.m10(), Math.fma(this.m10, var1.m11(), this.m20 * var1.m12()));
      float var8 = Math.fma(this.m01, var1.m10(), Math.fma(this.m11, var1.m11(), this.m21 * var1.m12()));
      float var9 = Math.fma(this.m02, var1.m10(), Math.fma(this.m12, var1.m11(), this.m22 * var1.m12()));
      float var10 = Math.fma(this.m03, var1.m10(), Math.fma(this.m13, var1.m11(), this.m23 * var1.m12()));
      float var11 = Math.fma(this.m00, var1.m20(), Math.fma(this.m10, var1.m21(), this.m20 * var1.m22()));
      float var12 = Math.fma(this.m01, var1.m20(), Math.fma(this.m11, var1.m21(), this.m21 * var1.m22()));
      float var13 = Math.fma(this.m02, var1.m20(), Math.fma(this.m12, var1.m21(), this.m22 * var1.m22()));
      float var14 = Math.fma(this.m03, var1.m20(), Math.fma(this.m13, var1.m21(), this.m23 * var1.m22()));
      float var15 = Math.fma(this.m00, var1.m30(), Math.fma(this.m10, var1.m31(), Math.fma(this.m20, var1.m32(), this.m30)));
      float var16 = Math.fma(this.m01, var1.m30(), Math.fma(this.m11, var1.m31(), Math.fma(this.m21, var1.m32(), this.m31)));
      float var17 = Math.fma(this.m02, var1.m30(), Math.fma(this.m12, var1.m31(), Math.fma(this.m22, var1.m32(), this.m32)));
      float var18 = Math.fma(this.m03, var1.m30(), Math.fma(this.m13, var1.m31(), Math.fma(this.m23, var1.m32(), this.m33)));
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(var6)._m10(var7)._m11(var8)._m12(var9)._m13(var10)._m20(var11)._m21(var12)._m22(var13)._m23(var14)._m30(var15)._m31(var16)._m32(var17)._m33(var18)._properties(this.properties & -30);
   }

   public Matrix4f mulAffine(Matrix4fc var1) {
      return this.mulAffine(var1, this);
   }

   public Matrix4f mulAffine(Matrix4fc var1, Matrix4f var2) {
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
      return var2._m00(Math.fma(var3, var12, Math.fma(var6, var13, var9 * var14)))._m01(Math.fma(var4, var12, Math.fma(var7, var13, var10 * var14)))._m02(Math.fma(var5, var12, Math.fma(var8, var13, var11 * var14)))._m03(this.m03)._m10(Math.fma(var3, var15, Math.fma(var6, var16, var9 * var17)))._m11(Math.fma(var4, var15, Math.fma(var7, var16, var10 * var17)))._m12(Math.fma(var5, var15, Math.fma(var8, var16, var11 * var17)))._m13(this.m13)._m20(Math.fma(var3, var18, Math.fma(var6, var19, var9 * var20)))._m21(Math.fma(var4, var18, Math.fma(var7, var19, var10 * var20)))._m22(Math.fma(var5, var18, Math.fma(var8, var19, var11 * var20)))._m23(this.m23)._m30(Math.fma(var3, var21, Math.fma(var6, var22, Math.fma(var9, var23, this.m30))))._m31(Math.fma(var4, var21, Math.fma(var7, var22, Math.fma(var10, var23, this.m31))))._m32(Math.fma(var5, var21, Math.fma(var8, var22, Math.fma(var11, var23, this.m32))))._m33(this.m33)._properties(2 | this.properties & var1.properties() & 16);
   }

   public Matrix4f mulTranslationAffine(Matrix4fc var1, Matrix4f var2) {
      return var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(this.m03)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(this.m13)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(this.m23)._m30(var1.m30() + this.m30)._m31(var1.m31() + this.m31)._m32(var1.m32() + this.m32)._m33(this.m33)._properties(2 | var1.properties() & 16);
   }

   public Matrix4f mulOrthoAffine(Matrix4fc var1) {
      return this.mulOrthoAffine(var1, this);
   }

   public Matrix4f mulOrthoAffine(Matrix4fc var1, Matrix4f var2) {
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
      return var2._m00(var3)._m01(var4)._m02(var5)._m03(0.0F)._m10(var6)._m11(var7)._m12(var8)._m13(0.0F)._m20(var9)._m21(var10)._m22(var11)._m23(0.0F)._m30(var12)._m31(var13)._m32(var14)._m33(1.0F)._properties(2);
   }

   public Matrix4f fma4x3(Matrix4fc var1, float var2) {
      return this.fma4x3(var1, var2, this);
   }

   public Matrix4f fma4x3(Matrix4fc var1, float var2, Matrix4f var3) {
      var3._m00(Math.fma(var1.m00(), var2, this.m00))._m01(Math.fma(var1.m01(), var2, this.m01))._m02(Math.fma(var1.m02(), var2, this.m02))._m03(this.m03)._m10(Math.fma(var1.m10(), var2, this.m10))._m11(Math.fma(var1.m11(), var2, this.m11))._m12(Math.fma(var1.m12(), var2, this.m12))._m13(this.m13)._m20(Math.fma(var1.m20(), var2, this.m20))._m21(Math.fma(var1.m21(), var2, this.m21))._m22(Math.fma(var1.m22(), var2, this.m22))._m23(this.m23)._m30(Math.fma(var1.m30(), var2, this.m30))._m31(Math.fma(var1.m31(), var2, this.m31))._m32(Math.fma(var1.m32(), var2, this.m32))._m33(this.m33)._properties(0);
      return var3;
   }

   public Matrix4f add(Matrix4fc var1) {
      return this.add(var1, this);
   }

   public Matrix4f add(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 + var1.m00())._m01(this.m01 + var1.m01())._m02(this.m02 + var1.m02())._m03(this.m03 + var1.m03())._m10(this.m10 + var1.m10())._m11(this.m11 + var1.m11())._m12(this.m12 + var1.m12())._m13(this.m13 + var1.m13())._m20(this.m20 + var1.m20())._m21(this.m21 + var1.m21())._m22(this.m22 + var1.m22())._m23(this.m23 + var1.m23())._m30(this.m30 + var1.m30())._m31(this.m31 + var1.m31())._m32(this.m32 + var1.m32())._m33(this.m33 + var1.m33())._properties(0);
      return var2;
   }

   public Matrix4f sub(Matrix4fc var1) {
      return this.sub(var1, this);
   }

   public Matrix4f sub(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 - var1.m00())._m01(this.m01 - var1.m01())._m02(this.m02 - var1.m02())._m03(this.m03 - var1.m03())._m10(this.m10 - var1.m10())._m11(this.m11 - var1.m11())._m12(this.m12 - var1.m12())._m13(this.m13 - var1.m13())._m20(this.m20 - var1.m20())._m21(this.m21 - var1.m21())._m22(this.m22 - var1.m22())._m23(this.m23 - var1.m23())._m30(this.m30 - var1.m30())._m31(this.m31 - var1.m31())._m32(this.m32 - var1.m32())._m33(this.m33 - var1.m33())._properties(0);
      return var2;
   }

   public Matrix4f mulComponentWise(Matrix4fc var1) {
      return this.mulComponentWise(var1, this);
   }

   public Matrix4f mulComponentWise(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 * var1.m00())._m01(this.m01 * var1.m01())._m02(this.m02 * var1.m02())._m03(this.m03 * var1.m03())._m10(this.m10 * var1.m10())._m11(this.m11 * var1.m11())._m12(this.m12 * var1.m12())._m13(this.m13 * var1.m13())._m20(this.m20 * var1.m20())._m21(this.m21 * var1.m21())._m22(this.m22 * var1.m22())._m23(this.m23 * var1.m23())._m30(this.m30 * var1.m30())._m31(this.m31 * var1.m31())._m32(this.m32 * var1.m32())._m33(this.m33 * var1.m33())._properties(0);
      return var2;
   }

   public Matrix4f add4x3(Matrix4fc var1) {
      return this.add4x3(var1, this);
   }

   public Matrix4f add4x3(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 + var1.m00())._m01(this.m01 + var1.m01())._m02(this.m02 + var1.m02())._m03(this.m03)._m10(this.m10 + var1.m10())._m11(this.m11 + var1.m11())._m12(this.m12 + var1.m12())._m13(this.m13)._m20(this.m20 + var1.m20())._m21(this.m21 + var1.m21())._m22(this.m22 + var1.m22())._m23(this.m23)._m30(this.m30 + var1.m30())._m31(this.m31 + var1.m31())._m32(this.m32 + var1.m32())._m33(this.m33)._properties(0);
      return var2;
   }

   public Matrix4f sub4x3(Matrix4f var1) {
      return this.sub4x3(var1, this);
   }

   public Matrix4f sub4x3(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 - var1.m00())._m01(this.m01 - var1.m01())._m02(this.m02 - var1.m02())._m03(this.m03)._m10(this.m10 - var1.m10())._m11(this.m11 - var1.m11())._m12(this.m12 - var1.m12())._m13(this.m13)._m20(this.m20 - var1.m20())._m21(this.m21 - var1.m21())._m22(this.m22 - var1.m22())._m23(this.m23)._m30(this.m30 - var1.m30())._m31(this.m31 - var1.m31())._m32(this.m32 - var1.m32())._m33(this.m33)._properties(0);
      return var2;
   }

   public Matrix4f mul4x3ComponentWise(Matrix4fc var1) {
      return this.mul4x3ComponentWise(var1, this);
   }

   public Matrix4f mul4x3ComponentWise(Matrix4fc var1, Matrix4f var2) {
      var2._m00(this.m00 * var1.m00())._m01(this.m01 * var1.m01())._m02(this.m02 * var1.m02())._m03(this.m03)._m10(this.m10 * var1.m10())._m11(this.m11 * var1.m11())._m12(this.m12 * var1.m12())._m13(this.m13)._m20(this.m20 * var1.m20())._m21(this.m21 * var1.m21())._m22(this.m22 * var1.m22())._m23(this.m23)._m30(this.m30 * var1.m30())._m31(this.m31 * var1.m31())._m32(this.m32 * var1.m32())._m33(this.m33)._properties(0);
      return var2;
   }

   public Matrix4f set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      return this._m00(var1)._m10(var5)._m20(var9)._m30(var13)._m01(var2)._m11(var6)._m21(var10)._m31(var14)._m02(var3)._m12(var7)._m22(var11)._m32(var15)._m03(var4)._m13(var8)._m23(var12)._m33(var16).determineProperties();
   }

   public Matrix4f set(float[] var1, int var2) {
      MemUtil.INSTANCE.copy(var1, var2, this);
      return this.determineProperties();
   }

   public Matrix4f set(float[] var1) {
      return this.set(var1, 0);
   }

   public Matrix4f setTransposed(float[] var1, int var2) {
      MemUtil.INSTANCE.copyTransposed(var1, var2, this);
      return this.determineProperties();
   }

   public Matrix4f setTransposed(float[] var1) {
      return this.setTransposed(var1, 0);
   }

   public Matrix4f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4f setTransposed(FloatBuffer var1) {
      MemUtil.INSTANCE.getTransposed(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4f setTransposed(ByteBuffer var1) {
      MemUtil.INSTANCE.getTransposed(this, var1.position(), var1);
      return this.determineProperties();
   }

   public Matrix4f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this.determineProperties();
      }
   }

   public Matrix4f setTransposedFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.getTransposed(this, var1);
         return this.determineProperties();
      }
   }

   public Matrix4f set(Vector4fc var1, Vector4fc var2, Vector4fc var3, Vector4fc var4) {
      return this._m00(var1.x())._m01(var1.y())._m02(var1.z())._m03(var1.w())._m10(var2.x())._m11(var2.y())._m12(var2.z())._m13(var2.w())._m20(var3.x())._m21(var3.y())._m22(var3.z())._m23(var3.w())._m30(var4.x())._m31(var4.y())._m32(var4.z())._m33(var4.w()).determineProperties();
   }

   public float determinant() {
      return (this.properties & 2) != 0 ? this.determinantAffine() : (this.m00 * this.m11 - this.m01 * this.m10) * (this.m22 * this.m33 - this.m23 * this.m32) + (this.m02 * this.m10 - this.m00 * this.m12) * (this.m21 * this.m33 - this.m23 * this.m31) + (this.m00 * this.m13 - this.m03 * this.m10) * (this.m21 * this.m32 - this.m22 * this.m31) + (this.m01 * this.m12 - this.m02 * this.m11) * (this.m20 * this.m33 - this.m23 * this.m30) + (this.m03 * this.m11 - this.m01 * this.m13) * (this.m20 * this.m32 - this.m22 * this.m30) + (this.m02 * this.m13 - this.m03 * this.m12) * (this.m20 * this.m31 - this.m21 * this.m30);
   }

   public float determinant3x3() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public float determinantAffine() {
      return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
   }

   public Matrix4f invert(Matrix4f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else if ((this.properties & 8) != 0) {
         return this.invertTranslation(var1);
      } else if ((this.properties & 16) != 0) {
         return this.invertOrthonormal(var1);
      } else if ((this.properties & 2) != 0) {
         return this.invertAffine(var1);
      } else {
         return (this.properties & 1) != 0 ? this.invertPerspective(var1) : this.invertGeneric(var1);
      }
   }

   private Matrix4f invertTranslation(Matrix4f var1) {
      if (var1 != this) {
         var1.set((Matrix4fc)this);
      }

      return var1._m30(-this.m30)._m31(-this.m31)._m32(-this.m32)._properties(26);
   }

   private Matrix4f invertOrthonormal(Matrix4f var1) {
      float var2 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
      float var3 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
      float var4 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
      float var5 = this.m01;
      float var6 = this.m02;
      float var7 = this.m12;
      return var1._m00(this.m00)._m01(this.m10)._m02(this.m20)._m03(0.0F)._m10(var5)._m11(this.m11)._m12(this.m21)._m13(0.0F)._m20(var6)._m21(var7)._m22(this.m22)._m23(0.0F)._m30(var2)._m31(var3)._m32(var4)._m33(1.0F)._properties(18);
   }

   private Matrix4f invertGeneric(Matrix4f var1) {
      return this != var1 ? this.invertGenericNonThis(var1) : this.invertGenericThis(var1);
   }

   private Matrix4f invertGenericNonThis(Matrix4f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      float var3 = this.m00 * this.m12 - this.m02 * this.m10;
      float var4 = this.m00 * this.m13 - this.m03 * this.m10;
      float var5 = this.m01 * this.m12 - this.m02 * this.m11;
      float var6 = this.m01 * this.m13 - this.m03 * this.m11;
      float var7 = this.m02 * this.m13 - this.m03 * this.m12;
      float var8 = this.m20 * this.m31 - this.m21 * this.m30;
      float var9 = this.m20 * this.m32 - this.m22 * this.m30;
      float var10 = this.m20 * this.m33 - this.m23 * this.m30;
      float var11 = this.m21 * this.m32 - this.m22 * this.m31;
      float var12 = this.m21 * this.m33 - this.m23 * this.m31;
      float var13 = this.m22 * this.m33 - this.m23 * this.m32;
      float var14 = var2 * var13 - var3 * var12 + var4 * var11 + var5 * var10 - var6 * var9 + var7 * var8;
      var14 = 1.0F / var14;
      return var1._m00(Math.fma(this.m11, var13, Math.fma(-this.m12, var12, this.m13 * var11)) * var14)._m01(Math.fma(-this.m01, var13, Math.fma(this.m02, var12, -this.m03 * var11)) * var14)._m02(Math.fma(this.m31, var7, Math.fma(-this.m32, var6, this.m33 * var5)) * var14)._m03(Math.fma(-this.m21, var7, Math.fma(this.m22, var6, -this.m23 * var5)) * var14)._m10(Math.fma(-this.m10, var13, Math.fma(this.m12, var10, -this.m13 * var9)) * var14)._m11(Math.fma(this.m00, var13, Math.fma(-this.m02, var10, this.m03 * var9)) * var14)._m12(Math.fma(-this.m30, var7, Math.fma(this.m32, var4, -this.m33 * var3)) * var14)._m13(Math.fma(this.m20, var7, Math.fma(-this.m22, var4, this.m23 * var3)) * var14)._m20(Math.fma(this.m10, var12, Math.fma(-this.m11, var10, this.m13 * var8)) * var14)._m21(Math.fma(-this.m00, var12, Math.fma(this.m01, var10, -this.m03 * var8)) * var14)._m22(Math.fma(this.m30, var6, Math.fma(-this.m31, var4, this.m33 * var2)) * var14)._m23(Math.fma(-this.m20, var6, Math.fma(this.m21, var4, -this.m23 * var2)) * var14)._m30(Math.fma(-this.m10, var11, Math.fma(this.m11, var9, -this.m12 * var8)) * var14)._m31(Math.fma(this.m00, var11, Math.fma(-this.m01, var9, this.m02 * var8)) * var14)._m32(Math.fma(-this.m30, var5, Math.fma(this.m31, var3, -this.m32 * var2)) * var14)._m33(Math.fma(this.m20, var5, Math.fma(-this.m21, var3, this.m22 * var2)) * var14)._properties(0);
   }

   private Matrix4f invertGenericThis(Matrix4f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      float var3 = this.m00 * this.m12 - this.m02 * this.m10;
      float var4 = this.m00 * this.m13 - this.m03 * this.m10;
      float var5 = this.m01 * this.m12 - this.m02 * this.m11;
      float var6 = this.m01 * this.m13 - this.m03 * this.m11;
      float var7 = this.m02 * this.m13 - this.m03 * this.m12;
      float var8 = this.m20 * this.m31 - this.m21 * this.m30;
      float var9 = this.m20 * this.m32 - this.m22 * this.m30;
      float var10 = this.m20 * this.m33 - this.m23 * this.m30;
      float var11 = this.m21 * this.m32 - this.m22 * this.m31;
      float var12 = this.m21 * this.m33 - this.m23 * this.m31;
      float var13 = this.m22 * this.m33 - this.m23 * this.m32;
      float var14 = var2 * var13 - var3 * var12 + var4 * var11 + var5 * var10 - var6 * var9 + var7 * var8;
      var14 = 1.0F / var14;
      float var15 = Math.fma(this.m11, var13, Math.fma(-this.m12, var12, this.m13 * var11)) * var14;
      float var16 = Math.fma(-this.m01, var13, Math.fma(this.m02, var12, -this.m03 * var11)) * var14;
      float var17 = Math.fma(this.m31, var7, Math.fma(-this.m32, var6, this.m33 * var5)) * var14;
      float var18 = Math.fma(-this.m21, var7, Math.fma(this.m22, var6, -this.m23 * var5)) * var14;
      float var19 = Math.fma(-this.m10, var13, Math.fma(this.m12, var10, -this.m13 * var9)) * var14;
      float var20 = Math.fma(this.m00, var13, Math.fma(-this.m02, var10, this.m03 * var9)) * var14;
      float var21 = Math.fma(-this.m30, var7, Math.fma(this.m32, var4, -this.m33 * var3)) * var14;
      float var22 = Math.fma(this.m20, var7, Math.fma(-this.m22, var4, this.m23 * var3)) * var14;
      float var23 = Math.fma(this.m10, var12, Math.fma(-this.m11, var10, this.m13 * var8)) * var14;
      float var24 = Math.fma(-this.m00, var12, Math.fma(this.m01, var10, -this.m03 * var8)) * var14;
      float var25 = Math.fma(this.m30, var6, Math.fma(-this.m31, var4, this.m33 * var2)) * var14;
      float var26 = Math.fma(-this.m20, var6, Math.fma(this.m21, var4, -this.m23 * var2)) * var14;
      float var27 = Math.fma(-this.m10, var11, Math.fma(this.m11, var9, -this.m12 * var8)) * var14;
      float var28 = Math.fma(this.m00, var11, Math.fma(-this.m01, var9, this.m02 * var8)) * var14;
      float var29 = Math.fma(-this.m30, var5, Math.fma(this.m31, var3, -this.m32 * var2)) * var14;
      float var30 = Math.fma(this.m20, var5, Math.fma(-this.m21, var3, this.m22 * var2)) * var14;
      return var1._m00(var15)._m01(var16)._m02(var17)._m03(var18)._m10(var19)._m11(var20)._m12(var21)._m13(var22)._m20(var23)._m21(var24)._m22(var25)._m23(var26)._m30(var27)._m31(var28)._m32(var29)._m33(var30)._properties(0);
   }

   public Matrix4f invert() {
      return this.invert(this);
   }

   public Matrix4f invertPerspective(Matrix4f var1) {
      float var2 = 1.0F / (this.m00 * this.m11);
      float var3 = -1.0F / (this.m23 * this.m32);
      return var1.set(this.m11 * var2, 0.0F, 0.0F, 0.0F, 0.0F, this.m00 * var2, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -this.m23 * var3, 0.0F, 0.0F, -this.m32 * var3, this.m22 * var3)._properties(0);
   }

   public Matrix4f invertPerspective() {
      return this.invertPerspective(this);
   }

   public Matrix4f invertFrustum(Matrix4f var1) {
      float var2 = 1.0F / this.m00;
      float var3 = 1.0F / this.m11;
      float var4 = 1.0F / this.m23;
      float var5 = 1.0F / this.m32;
      return var1.set(var2, 0.0F, 0.0F, 0.0F, 0.0F, var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, var5, -this.m20 * var2 * var4, -this.m21 * var3 * var4, var4, -this.m22 * var4 * var5);
   }

   public Matrix4f invertFrustum() {
      return this.invertFrustum(this);
   }

   public Matrix4f invertOrtho(Matrix4f var1) {
      float var2 = 1.0F / this.m00;
      float var3 = 1.0F / this.m11;
      float var4 = 1.0F / this.m22;
      return var1.set(var2, 0.0F, 0.0F, 0.0F, 0.0F, var3, 0.0F, 0.0F, 0.0F, 0.0F, var4, 0.0F, -this.m30 * var2, -this.m31 * var3, -this.m32 * var4, 1.0F)._properties(2 | this.properties & 16);
   }

   public Matrix4f invertOrtho() {
      return this.invertOrtho(this);
   }

   public Matrix4f invertPerspectiveView(Matrix4fc var1, Matrix4f var2) {
      float var3 = 1.0F / (this.m00 * this.m11);
      float var4 = -1.0F / (this.m23 * this.m32);
      float var5 = this.m11 * var3;
      float var6 = this.m00 * var3;
      float var7 = -this.m23 * var4;
      float var8 = -this.m32 * var4;
      float var9 = this.m22 * var4;
      float var10 = -var1.m00() * var1.m30() - var1.m01() * var1.m31() - var1.m02() * var1.m32();
      float var11 = -var1.m10() * var1.m30() - var1.m11() * var1.m31() - var1.m12() * var1.m32();
      float var12 = -var1.m20() * var1.m30() - var1.m21() * var1.m31() - var1.m22() * var1.m32();
      float var13 = var1.m01() * var6;
      float var14 = var1.m02() * var8 + var10 * var9;
      float var15 = var1.m12() * var8 + var11 * var9;
      float var16 = var1.m22() * var8 + var12 * var9;
      return var2._m00(var1.m00() * var5)._m01(var1.m10() * var5)._m02(var1.m20() * var5)._m03(0.0F)._m10(var13)._m11(var1.m11() * var6)._m12(var1.m21() * var6)._m13(0.0F)._m20(var10 * var7)._m21(var11 * var7)._m22(var12 * var7)._m23(var7)._m30(var14)._m31(var15)._m32(var16)._m33(var9)._properties(0);
   }

   public Matrix4f invertPerspectiveView(Matrix4x3fc var1, Matrix4f var2) {
      float var3 = 1.0F / (this.m00 * this.m11);
      float var4 = -1.0F / (this.m23 * this.m32);
      float var5 = this.m11 * var3;
      float var6 = this.m00 * var3;
      float var7 = -this.m23 * var4;
      float var8 = -this.m32 * var4;
      float var9 = this.m22 * var4;
      float var10 = -var1.m00() * var1.m30() - var1.m01() * var1.m31() - var1.m02() * var1.m32();
      float var11 = -var1.m10() * var1.m30() - var1.m11() * var1.m31() - var1.m12() * var1.m32();
      float var12 = -var1.m20() * var1.m30() - var1.m21() * var1.m31() - var1.m22() * var1.m32();
      return var2._m00(var1.m00() * var5)._m01(var1.m10() * var5)._m02(var1.m20() * var5)._m03(0.0F)._m10(var1.m01() * var6)._m11(var1.m11() * var6)._m12(var1.m21() * var6)._m13(0.0F)._m20(var10 * var7)._m21(var11 * var7)._m22(var12 * var7)._m23(var7)._m30(var1.m02() * var8 + var10 * var9)._m31(var1.m12() * var8 + var11 * var9)._m32(var1.m22() * var8 + var12 * var9)._m33(var9)._properties(0);
   }

   public Matrix4f invertAffine(Matrix4f var1) {
      float var2 = this.m00 * this.m11;
      float var3 = this.m01 * this.m10;
      float var4 = this.m02 * this.m10;
      float var5 = this.m00 * this.m12;
      float var6 = this.m01 * this.m12;
      float var7 = this.m02 * this.m11;
      float var8 = (var2 - var3) * this.m22 + (var4 - var5) * this.m21 + (var6 - var7) * this.m20;
      float var9 = 1.0F / var8;
      float var10 = this.m10 * this.m22;
      float var11 = this.m10 * this.m21;
      float var12 = this.m11 * this.m22;
      float var13 = this.m11 * this.m20;
      float var14 = this.m12 * this.m21;
      float var15 = this.m12 * this.m20;
      float var16 = this.m20 * this.m02;
      float var17 = this.m20 * this.m01;
      float var18 = this.m21 * this.m02;
      float var19 = this.m21 * this.m00;
      float var20 = this.m22 * this.m01;
      float var21 = this.m22 * this.m00;
      float var22 = (var16 * this.m31 - var17 * this.m32 + var19 * this.m32 - var18 * this.m30 + var20 * this.m30 - var21 * this.m31) * var9;
      float var23 = (var7 * this.m30 - var6 * this.m30 + var5 * this.m31 - var4 * this.m31 + var3 * this.m32 - var2 * this.m32) * var9;
      return var1._m00((var12 - var14) * var9)._m01((var18 - var20) * var9)._m02((var6 - var7) * var9)._m03(0.0F)._m10((var15 - var10) * var9)._m11((var21 - var16) * var9)._m12((var4 - var5) * var9)._m13(0.0F)._m20((var11 - var13) * var9)._m21((var17 - var19) * var9)._m22((var2 - var3) * var9)._m23(0.0F)._m30((var10 * this.m31 - var11 * this.m32 + var13 * this.m32 - var12 * this.m30 + var14 * this.m30 - var15 * this.m31) * var9)._m31(var22)._m32(var23)._m33(1.0F)._properties(2);
   }

   public Matrix4f invertAffine() {
      return this.invertAffine(this);
   }

   public Matrix4f transpose(Matrix4f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return this != var1 ? this.transposeNonThisGeneric(var1) : this.transposeThisGeneric(var1);
      }
   }

   private Matrix4f transposeNonThisGeneric(Matrix4f var1) {
      return var1._m00(this.m00)._m01(this.m10)._m02(this.m20)._m03(this.m30)._m10(this.m01)._m11(this.m11)._m12(this.m21)._m13(this.m31)._m20(this.m02)._m21(this.m12)._m22(this.m22)._m23(this.m32)._m30(this.m03)._m31(this.m13)._m32(this.m23)._m33(this.m33)._properties(0);
   }

   private Matrix4f transposeThisGeneric(Matrix4f var1) {
      float var2 = this.m01;
      float var3 = this.m02;
      float var4 = this.m12;
      float var5 = this.m03;
      float var6 = this.m13;
      float var7 = this.m23;
      return var1._m01(this.m10)._m02(this.m20)._m03(this.m30)._m10(var2)._m12(this.m21)._m13(this.m31)._m20(var3)._m21(var4)._m23(this.m32)._m30(var5)._m31(var6)._m32(var7)._properties(0);
   }

   public Matrix4f transpose3x3() {
      return this.transpose3x3(this);
   }

   public Matrix4f transpose3x3(Matrix4f var1) {
      float var2 = this.m01;
      float var3 = this.m02;
      float var4 = this.m12;
      return var1._m00(this.m00)._m01(this.m10)._m02(this.m20)._m10(var2)._m11(this.m11)._m12(this.m21)._m20(var3)._m21(var4)._m22(this.m22)._properties(this.properties & 30);
   }

   public Matrix3f transpose3x3(Matrix3f var1) {
      return var1._m00(this.m00)._m01(this.m10)._m02(this.m20)._m10(this.m01)._m11(this.m11)._m12(this.m21)._m20(this.m02)._m21(this.m12)._m22(this.m22);
   }

   public Matrix4f transpose() {
      return this.transpose(this);
   }

   public Matrix4f translation(float var1, float var2, float var3) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      return this._m30(var1)._m31(var2)._m32(var3)._properties(26);
   }

   public Matrix4f translation(Vector3fc var1) {
      return this.translation(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f setTranslation(float var1, float var2, float var3) {
      return this._m30(var1)._m31(var2)._m32(var3)._properties(this.properties & -6);
   }

   public Matrix4f setTranslation(Vector3fc var1) {
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
      return var10000 + " " + Runtime.format((double)this.m10, var1) + " " + Runtime.format((double)this.m20, var1) + " " + Runtime.format((double)this.m30, var1) + "\n" + Runtime.format((double)this.m01, var1) + " " + Runtime.format((double)this.m11, var1) + " " + Runtime.format((double)this.m21, var1) + " " + Runtime.format((double)this.m31, var1) + "\n" + Runtime.format((double)this.m02, var1) + " " + Runtime.format((double)this.m12, var1) + " " + Runtime.format((double)this.m22, var1) + " " + Runtime.format((double)this.m32, var1) + "\n" + Runtime.format((double)this.m03, var1) + " " + Runtime.format((double)this.m13, var1) + " " + Runtime.format((double)this.m23, var1) + " " + Runtime.format((double)this.m33, var1) + "\n";
   }

   public Matrix4f get(Matrix4f var1) {
      return var1.set((Matrix4fc)this);
   }

   public Matrix4x3f get4x3(Matrix4x3f var1) {
      return var1.set((Matrix4fc)this);
   }

   public Matrix4d get(Matrix4d var1) {
      return var1.set((Matrix4fc)this);
   }

   public Matrix3f get3x3(Matrix3f var1) {
      return var1.set((Matrix4fc)this);
   }

   public Matrix3d get3x3(Matrix3d var1) {
      return var1.set((Matrix4fc)this);
   }

   public AxisAngle4f getRotation(AxisAngle4f var1) {
      return var1.set((Matrix4fc)this);
   }

   public AxisAngle4d getRotation(AxisAngle4d var1) {
      return var1.set((Matrix4fc)this);
   }

   public Quaternionf getUnnormalizedRotation(Quaternionf var1) {
      return var1.setFromUnnormalized((Matrix4fc)this);
   }

   public Quaternionf getNormalizedRotation(Quaternionf var1) {
      return var1.setFromNormalized((Matrix4fc)this);
   }

   public Quaterniond getUnnormalizedRotation(Quaterniond var1) {
      return var1.setFromUnnormalized((Matrix4fc)this);
   }

   public Quaterniond getNormalizedRotation(Quaterniond var1) {
      return var1.setFromNormalized((Matrix4fc)this);
   }

   public FloatBuffer get(FloatBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public ByteBuffer get(ByteBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer get(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public FloatBuffer get4x3(FloatBuffer var1) {
      MemUtil.INSTANCE.put4x3(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get4x3(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put4x3(this, var1, var2);
      return var2;
   }

   public ByteBuffer get4x3(ByteBuffer var1) {
      MemUtil.INSTANCE.put4x3(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer get4x3(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put4x3(this, var1, var2);
      return var2;
   }

   public FloatBuffer get3x4(FloatBuffer var1) {
      MemUtil.INSTANCE.put3x4(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get3x4(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put3x4(this, var1, var2);
      return var2;
   }

   public ByteBuffer get3x4(ByteBuffer var1) {
      MemUtil.INSTANCE.put3x4(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer get3x4(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put3x4(this, var1, var2);
      return var2;
   }

   public FloatBuffer getTransposed(FloatBuffer var1) {
      MemUtil.INSTANCE.putTransposed(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer getTransposed(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public ByteBuffer getTransposed(ByteBuffer var1) {
      MemUtil.INSTANCE.putTransposed(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer getTransposed(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public FloatBuffer get4x3Transposed(FloatBuffer var1) {
      MemUtil.INSTANCE.put4x3Transposed(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get4x3Transposed(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put4x3Transposed(this, var1, var2);
      return var2;
   }

   public ByteBuffer get4x3Transposed(ByteBuffer var1) {
      MemUtil.INSTANCE.put4x3Transposed(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer get4x3Transposed(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put4x3Transposed(this, var1, var2);
      return var2;
   }

   public Matrix4fc getToAddress(long var1) {
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
      MemUtil.INSTANCE.copy((Matrix4f)this, (float[])var1, 0);
      return var1;
   }

   public Matrix4f zero() {
      MemUtil.INSTANCE.zero(this);
      return this._properties(0);
   }

   public Matrix4f scaling(float var1) {
      return this.scaling(var1, var1, var1);
   }

   public Matrix4f scaling(float var1, float var2, float var3) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      boolean var4 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      return this._m00(var1)._m11(var2)._m22(var3)._properties(2 | (var4 ? 16 : 0));
   }

   public Matrix4f scaling(Vector3fc var1) {
      return this.scaling(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f rotation(float var1, Vector3fc var2) {
      return this.rotation(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix4f rotation(AxisAngle4f var1) {
      return this.rotation(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4f rotation(float var1, float var2, float var3, float var4) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotationX(var2 * var1);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotationY(var3 * var1);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotationZ(var4 * var1) : this.rotationInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4f rotationInternal(float var1, float var2, float var3, float var4) {
      float var5 = Math.sin(var1);
      float var6 = Math.cosFromSin(var5, var1);
      float var7 = 1.0F - var6;
      float var8 = var2 * var3;
      float var9 = var2 * var4;
      float var10 = var3 * var4;
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      return this._m00(var6 + var2 * var2 * var7)._m10(var8 * var7 - var4 * var5)._m20(var9 * var7 + var3 * var5)._m01(var8 * var7 + var4 * var5)._m11(var6 + var3 * var3 * var7)._m21(var10 * var7 - var2 * var5)._m02(var9 * var7 - var3 * var5)._m12(var10 * var7 + var2 * var5)._m22(var6 + var4 * var4 * var7)._properties(18);
   }

   public Matrix4f rotationX(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m11(var3)._m12(var2)._m21(-var2)._m22(var3)._properties(18);
      return this;
   }

   public Matrix4f rotationY(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(var3)._m02(-var2)._m20(var2)._m22(var3)._properties(18);
      return this;
   }

   public Matrix4f rotationZ(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      return this._m00(var3)._m01(var2)._m10(-var2)._m11(var3)._properties(18);
   }

   public Matrix4f rotationTowardsXY(float var1, float var2) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      return this._m00(var2)._m01(var1)._m10(-var1)._m11(var2)._properties(18);
   }

   public Matrix4f rotationXYZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var1);
      float var5 = Math.cosFromSin(var4, var1);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      float var10 = -var4 * -var6;
      float var11 = var5 * -var6;
      return this._m20(var6)._m21(-var4 * var7)._m22(var5 * var7)._m00(var7 * var9)._m01(var10 * var9 + var5 * var8)._m02(var11 * var9 + var4 * var8)._m10(var7 * -var8)._m11(var10 * -var8 + var5 * var9)._m12(var11 * -var8 + var4 * var9)._properties(18);
   }

   public Matrix4f rotationZYX(float var1, float var2, float var3) {
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var1);
      float var9 = Math.cosFromSin(var8, var1);
      float var10 = var9 * var6;
      float var11 = var8 * var6;
      return this._m00(var9 * var7)._m01(var8 * var7)._m02(-var6)._m03(0.0F)._m10(-var8 * var5 + var10 * var4)._m11(var9 * var5 + var11 * var4)._m12(var7 * var4)._m13(0.0F)._m20(-var8 * -var4 + var10 * var5)._m21(var9 * -var4 + var11 * var5)._m22(var7 * var5)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
   }

   public Matrix4f rotationYXZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var2);
      float var5 = Math.cosFromSin(var4, var2);
      float var6 = Math.sin(var1);
      float var7 = Math.cosFromSin(var6, var1);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      float var10 = var6 * var4;
      float var11 = var7 * var4;
      return this._m20(var6 * var5)._m21(-var4)._m22(var7 * var5)._m23(0.0F)._m00(var7 * var9 + var10 * var8)._m01(var5 * var8)._m02(-var6 * var9 + var11 * var8)._m03(0.0F)._m10(var7 * -var8 + var10 * var9)._m11(var5 * var9)._m12(-var6 * -var8 + var11 * var9)._m13(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
   }

   public Matrix4f setRotationXYZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var1);
      float var5 = Math.cosFromSin(var4, var1);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      float var10 = -var4 * -var6;
      float var11 = var5 * -var6;
      return this._m20(var6)._m21(-var4 * var7)._m22(var5 * var7)._m00(var7 * var9)._m01(var10 * var9 + var5 * var8)._m02(var11 * var9 + var4 * var8)._m10(var7 * -var8)._m11(var10 * -var8 + var5 * var9)._m12(var11 * -var8 + var4 * var9)._properties(this.properties & -14);
   }

   public Matrix4f setRotationZYX(float var1, float var2, float var3) {
      float var4 = Math.sin(var3);
      float var5 = Math.cosFromSin(var4, var3);
      float var6 = Math.sin(var2);
      float var7 = Math.cosFromSin(var6, var2);
      float var8 = Math.sin(var1);
      float var9 = Math.cosFromSin(var8, var1);
      float var10 = var9 * var6;
      float var11 = var8 * var6;
      return this._m00(var9 * var7)._m01(var8 * var7)._m02(-var6)._m10(-var8 * var5 + var10 * var4)._m11(var9 * var5 + var11 * var4)._m12(var7 * var4)._m20(-var8 * -var4 + var10 * var5)._m21(var9 * -var4 + var11 * var5)._m22(var7 * var5)._properties(this.properties & -14);
   }

   public Matrix4f setRotationYXZ(float var1, float var2, float var3) {
      float var4 = Math.sin(var2);
      float var5 = Math.cosFromSin(var4, var2);
      float var6 = Math.sin(var1);
      float var7 = Math.cosFromSin(var6, var1);
      float var8 = Math.sin(var3);
      float var9 = Math.cosFromSin(var8, var3);
      float var10 = var6 * var4;
      float var11 = var7 * var4;
      return this._m20(var6 * var5)._m21(-var4)._m22(var7 * var5)._m00(var7 * var9 + var10 * var8)._m01(var5 * var8)._m02(-var6 * var9 + var11 * var8)._m10(var7 * -var8 + var10 * var9)._m11(var5 * var9)._m12(-var6 * -var8 + var11 * var9)._properties(this.properties & -14);
   }

   public Matrix4f rotation(Quaternionfc var1) {
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
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      return this._m00(var2 + var3 - var5 - var4)._m01(var9 + var7)._m02(var11 - var13)._m10(-var7 + var9)._m11(var4 - var5 + var2 - var3)._m12(var15 + var17)._m20(var13 + var11)._m21(var15 - var17)._m22(var5 - var4 - var3 + var2)._properties(18);
   }

   public Matrix4f translationRotateScale(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
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
      boolean var23 = Math.absEqualsOne(var8) && Math.absEqualsOne(var9) && Math.absEqualsOne(var10);
      return this._m00(var8 - (var15 + var16) * var8)._m01((var17 + var22) * var8)._m02((var18 - var21) * var8)._m03(0.0F)._m10((var17 - var22) * var9)._m11(var9 - (var16 + var14) * var9)._m12((var20 + var19) * var9)._m13(0.0F)._m20((var18 + var21) * var10)._m21((var20 - var19) * var10)._m22(var10 - (var15 + var14) * var10)._m23(0.0F)._m30(var1)._m31(var2)._m32(var3)._m33(1.0F)._properties(2 | (var23 ? 16 : 0));
   }

   public Matrix4f translationRotateScale(Vector3fc var1, Quaternionfc var2, Vector3fc var3) {
      return this.translationRotateScale(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4f translationRotateScale(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.translationRotateScale(var1, var2, var3, var4, var5, var6, var7, var8, var8, var8);
   }

   public Matrix4f translationRotateScale(Vector3fc var1, Quaternionfc var2, float var3) {
      return this.translationRotateScale(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3, var3, var3);
   }

   public Matrix4f translationRotateScaleInvert(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      boolean var11 = Math.absEqualsOne(var8) && Math.absEqualsOne(var9) && Math.absEqualsOne(var10);
      if (var11) {
         return this.translationRotateScale(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10).invertOrthonormal(this);
      } else {
         float var12 = -var4;
         float var13 = -var5;
         float var14 = -var6;
         float var15 = var12 + var12;
         float var16 = var13 + var13;
         float var17 = var14 + var14;
         float var18 = var15 * var12;
         float var19 = var16 * var13;
         float var20 = var17 * var14;
         float var21 = var15 * var13;
         float var22 = var15 * var14;
         float var23 = var15 * var7;
         float var24 = var16 * var14;
         float var25 = var16 * var7;
         float var26 = var17 * var7;
         float var27 = 1.0F / var8;
         float var28 = 1.0F / var9;
         float var29 = 1.0F / var10;
         return this._m00(var27 * (1.0F - var19 - var20))._m01(var28 * (var21 + var26))._m02(var29 * (var22 - var25))._m03(0.0F)._m10(var27 * (var21 - var26))._m11(var28 * (1.0F - var20 - var18))._m12(var29 * (var24 + var23))._m13(0.0F)._m20(var27 * (var22 + var25))._m21(var28 * (var24 - var23))._m22(var29 * (1.0F - var19 - var18))._m23(0.0F)._m30(-this.m00 * var1 - this.m10 * var2 - this.m20 * var3)._m31(-this.m01 * var1 - this.m11 * var2 - this.m21 * var3)._m32(-this.m02 * var1 - this.m12 * var2 - this.m22 * var3)._m33(1.0F)._properties(2);
      }
   }

   public Matrix4f translationRotateScaleInvert(Vector3fc var1, Quaternionfc var2, Vector3fc var3) {
      return this.translationRotateScaleInvert(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4f translationRotateScaleInvert(Vector3fc var1, Quaternionfc var2, float var3) {
      return this.translationRotateScaleInvert(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3, var3, var3);
   }

   public Matrix4f translationRotateScaleMulAffine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, Matrix4f var11) {
      float var12 = var7 * var7;
      float var13 = var4 * var4;
      float var14 = var5 * var5;
      float var15 = var6 * var6;
      float var16 = var6 * var7;
      float var17 = var4 * var5;
      float var18 = var4 * var6;
      float var19 = var5 * var7;
      float var20 = var5 * var6;
      float var21 = var4 * var7;
      float var22 = var12 + var13 - var15 - var14;
      float var23 = var17 + var16 + var16 + var17;
      float var24 = var18 - var19 + var18 - var19;
      float var25 = -var16 + var17 - var16 + var17;
      float var26 = var14 - var15 + var12 - var13;
      float var27 = var20 + var20 + var21 + var21;
      float var28 = var19 + var18 + var18 + var19;
      float var29 = var20 + var20 - var21 - var21;
      float var30 = var15 - var14 - var13 + var12;
      float var31 = var22 * var11.m00 + var25 * var11.m01 + var28 * var11.m02;
      float var32 = var23 * var11.m00 + var26 * var11.m01 + var29 * var11.m02;
      this._m02(var24 * var11.m00 + var27 * var11.m01 + var30 * var11.m02)._m00(var31)._m01(var32)._m03(0.0F);
      float var33 = var22 * var11.m10 + var25 * var11.m11 + var28 * var11.m12;
      float var34 = var23 * var11.m10 + var26 * var11.m11 + var29 * var11.m12;
      this._m12(var24 * var11.m10 + var27 * var11.m11 + var30 * var11.m12)._m10(var33)._m11(var34)._m13(0.0F);
      float var35 = var22 * var11.m20 + var25 * var11.m21 + var28 * var11.m22;
      float var36 = var23 * var11.m20 + var26 * var11.m21 + var29 * var11.m22;
      this._m22(var24 * var11.m20 + var27 * var11.m21 + var30 * var11.m22)._m20(var35)._m21(var36)._m23(0.0F);
      float var37 = var22 * var11.m30 + var25 * var11.m31 + var28 * var11.m32 + var1;
      float var38 = var23 * var11.m30 + var26 * var11.m31 + var29 * var11.m32 + var2;
      this._m32(var24 * var11.m30 + var27 * var11.m31 + var30 * var11.m32 + var3)._m30(var37)._m31(var38)._m33(1.0F);
      boolean var39 = Math.absEqualsOne(var8) && Math.absEqualsOne(var9) && Math.absEqualsOne(var10);
      return this._properties(2 | (var39 && (var11.properties & 16) != 0 ? 16 : 0));
   }

   public Matrix4f translationRotateScaleMulAffine(Vector3fc var1, Quaternionfc var2, Vector3fc var3, Matrix4f var4) {
      return this.translationRotateScaleMulAffine(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var2.w(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4f translationRotate(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var7 * var7;
      float var9 = var4 * var4;
      float var10 = var5 * var5;
      float var11 = var6 * var6;
      float var12 = var6 * var7;
      float var13 = var4 * var5;
      float var14 = var4 * var6;
      float var15 = var5 * var7;
      float var16 = var5 * var6;
      float var17 = var4 * var7;
      return this._m00(var8 + var9 - var11 - var10)._m01(var13 + var12 + var12 + var13)._m02(var14 - var15 + var14 - var15)._m10(-var12 + var13 - var12 + var13)._m11(var10 - var11 + var8 - var9)._m12(var16 + var16 + var17 + var17)._m20(var15 + var14 + var14 + var15)._m21(var16 + var16 - var17 - var17)._m22(var11 - var10 - var9 + var8)._m30(var1)._m31(var2)._m32(var3)._m33(1.0F)._properties(18);
   }

   public Matrix4f translationRotate(float var1, float var2, float var3, Quaternionfc var4) {
      return this.translationRotate(var1, var2, var3, var4.x(), var4.y(), var4.z(), var4.w());
   }

   public Matrix4f set3x3(Matrix3fc var1) {
      return this.set3x3Matrix3fc(var1)._properties(this.properties & -30);
   }

   private Matrix4f set3x3Matrix3fc(Matrix3fc var1) {
      return this._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
   }

   public Vector4f transform(Vector4f var1) {
      return var1.mul((Matrix4fc)this);
   }

   public Vector4f transform(Vector4fc var1, Vector4f var2) {
      return var1.mul((Matrix4fc)this, var2);
   }

   public Vector4f transform(float var1, float var2, float var3, float var4, Vector4f var5) {
      return var5.set(var1, var2, var3, var4).mul((Matrix4fc)this);
   }

   public Vector4f transformTranspose(Vector4f var1) {
      return var1.mulTranspose(this);
   }

   public Vector4f transformTranspose(Vector4fc var1, Vector4f var2) {
      return var1.mulTranspose(this, var2);
   }

   public Vector4f transformTranspose(float var1, float var2, float var3, float var4, Vector4f var5) {
      return var5.set(var1, var2, var3, var4).mulTranspose(this);
   }

   public Vector4f transformProject(Vector4f var1) {
      return var1.mulProject(this);
   }

   public Vector4f transformProject(Vector4fc var1, Vector4f var2) {
      return var1.mulProject(this, (Vector4f)var2);
   }

   public Vector4f transformProject(float var1, float var2, float var3, float var4, Vector4f var5) {
      return var5.set(var1, var2, var3, var4).mulProject(this);
   }

   public Vector3f transformProject(Vector4fc var1, Vector3f var2) {
      return var1.mulProject(this, (Vector3f)var2);
   }

   public Vector3f transformProject(float var1, float var2, float var3, float var4, Vector3f var5) {
      return var5.set(var1, var2, var3).mulProject(this, var4, var5);
   }

   public Vector3f transformProject(Vector3f var1) {
      return var1.mulProject(this);
   }

   public Vector3f transformProject(Vector3fc var1, Vector3f var2) {
      return var1.mulProject(this, var2);
   }

   public Vector3f transformProject(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(var1, var2, var3).mulProject(this);
   }

   public Vector3f transformPosition(Vector3f var1) {
      return var1.mulPosition((Matrix4fc)this);
   }

   public Vector3f transformPosition(Vector3fc var1, Vector3f var2) {
      return this.transformPosition(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transformPosition(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(var1, var2, var3).mulPosition((Matrix4fc)this);
   }

   public Vector3f transformDirection(Vector3f var1) {
      return this.transformDirection(var1.x, var1.y, var1.z, var1);
   }

   public Vector3f transformDirection(Vector3fc var1, Vector3f var2) {
      return this.transformDirection(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f transformDirection(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(var1, var2, var3).mulDirection((Matrix4fc)this);
   }

   public Vector4f transformAffine(Vector4f var1) {
      return var1.mulAffine(this, var1);
   }

   public Vector4f transformAffine(Vector4fc var1, Vector4f var2) {
      return this.transformAffine(var1.x(), var1.y(), var1.z(), var1.w(), var2);
   }

   public Vector4f transformAffine(float var1, float var2, float var3, float var4, Vector4f var5) {
      return var5.set(var1, var2, var3, var4).mulAffine(this, var5);
   }

   public Matrix4f scale(Vector3fc var1, Matrix4f var2) {
      return this.scale(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4f scale(Vector3fc var1) {
      return this.scale(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix4f scale(float var1, Matrix4f var2) {
      return this.scale(var1, var1, var1, var2);
   }

   public Matrix4f scale(float var1) {
      return this.scale(var1, var1, var1);
   }

   public Matrix4f scaleXY(float var1, float var2, Matrix4f var3) {
      return this.scale(var1, var2, 1.0F, var3);
   }

   public Matrix4f scaleXY(float var1, float var2) {
      return this.scale(var1, var2, 1.0F);
   }

   public Matrix4f scale(float var1, float var2, float var3, Matrix4f var4) {
      return (this.properties & 4) != 0 ? var4.scaling(var1, var2, var3) : this.scaleGeneric(var1, var2, var3, var4);
   }

   private Matrix4f scaleGeneric(float var1, float var2, float var3, Matrix4f var4) {
      boolean var5 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      return var4._m00(this.m00 * var1)._m01(this.m01 * var1)._m02(this.m02 * var1)._m03(this.m03 * var1)._m10(this.m10 * var2)._m11(this.m11 * var2)._m12(this.m12 * var2)._m13(this.m13 * var2)._m20(this.m20 * var3)._m21(this.m21 * var3)._m22(this.m22 * var3)._m23(this.m23 * var3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & ~(13 | (var5 ? 0 : 16)));
   }

   public Matrix4f scale(float var1, float var2, float var3) {
      return this.scale(var1, var2, var3, this);
   }

   public Matrix4f scaleAround(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      float var8 = this.m00 * var4 + this.m10 * var5 + this.m20 * var6 + this.m30;
      float var9 = this.m01 * var4 + this.m11 * var5 + this.m21 * var6 + this.m31;
      float var10 = this.m02 * var4 + this.m12 * var5 + this.m22 * var6 + this.m32;
      float var11 = this.m03 * var4 + this.m13 * var5 + this.m23 * var6 + this.m33;
      boolean var12 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      return var7._m00(this.m00 * var1)._m01(this.m01 * var1)._m02(this.m02 * var1)._m03(this.m03 * var1)._m10(this.m10 * var2)._m11(this.m11 * var2)._m12(this.m12 * var2)._m13(this.m13 * var2)._m20(this.m20 * var3)._m21(this.m21 * var3)._m22(this.m22 * var3)._m23(this.m23 * var3)._m30(-this.m00 * var4 - this.m10 * var5 - this.m20 * var6 + var8)._m31(-this.m01 * var4 - this.m11 * var5 - this.m21 * var6 + var9)._m32(-this.m02 * var4 - this.m12 * var5 - this.m22 * var6 + var10)._m33(-this.m03 * var4 - this.m13 * var5 - this.m23 * var6 + var11)._properties(this.properties & ~(13 | (var12 ? 0 : 16)));
   }

   public Matrix4f scaleAround(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.scaleAround(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f scaleAround(float var1, float var2, float var3, float var4) {
      return this.scaleAround(var1, var1, var1, var2, var3, var4, this);
   }

   public Matrix4f scaleAround(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.scaleAround(var1, var1, var1, var2, var3, var4, var5);
   }

   public Matrix4f scaleLocal(float var1, float var2, float var3, Matrix4f var4) {
      return (this.properties & 4) != 0 ? var4.scaling(var1, var2, var3) : this.scaleLocalGeneric(var1, var2, var3, var4);
   }

   private Matrix4f scaleLocalGeneric(float var1, float var2, float var3, Matrix4f var4) {
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
      boolean var17 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      return var4._m00(var5)._m01(var6)._m02(var7)._m03(this.m03)._m10(var8)._m11(var9)._m12(var10)._m13(this.m13)._m20(var11)._m21(var12)._m22(var13)._m23(this.m23)._m30(var14)._m31(var15)._m32(var16)._m33(this.m33)._properties(this.properties & ~(13 | (var17 ? 0 : 16)));
   }

   public Matrix4f scaleLocal(float var1, Matrix4f var2) {
      return this.scaleLocal(var1, var1, var1, var2);
   }

   public Matrix4f scaleLocal(float var1) {
      return this.scaleLocal(var1, this);
   }

   public Matrix4f scaleLocal(float var1, float var2, float var3) {
      return this.scaleLocal(var1, var2, var3, this);
   }

   public Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      boolean var8 = Math.absEqualsOne(var1) && Math.absEqualsOne(var2) && Math.absEqualsOne(var3);
      return var7._m00(var1 * (this.m00 - var4 * this.m03) + var4 * this.m03)._m01(var2 * (this.m01 - var5 * this.m03) + var5 * this.m03)._m02(var3 * (this.m02 - var6 * this.m03) + var6 * this.m03)._m03(this.m03)._m10(var1 * (this.m10 - var4 * this.m13) + var4 * this.m13)._m11(var2 * (this.m11 - var5 * this.m13) + var5 * this.m13)._m12(var3 * (this.m12 - var6 * this.m13) + var6 * this.m13)._m13(this.m13)._m20(var1 * (this.m20 - var4 * this.m23) + var4 * this.m23)._m21(var2 * (this.m21 - var5 * this.m23) + var5 * this.m23)._m22(var3 * (this.m22 - var6 * this.m23) + var6 * this.m23)._m23(this.m23)._m30(var1 * (this.m30 - var4 * this.m33) + var4 * this.m33)._m31(var2 * (this.m31 - var5 * this.m33) + var5 * this.m33)._m32(var3 * (this.m32 - var6 * this.m33) + var6 * this.m33)._m33(this.m33)._properties(this.properties & ~(13 | (var8 ? 0 : 16)));
   }

   public Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.scaleAroundLocal(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4) {
      return this.scaleAroundLocal(var1, var1, var1, var2, var3, var4, this);
   }

   public Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.scaleAroundLocal(var1, var1, var1, var2, var3, var4, var5);
   }

   public Matrix4f rotateX(float var1, Matrix4f var2) {
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

   private Matrix4f rotateXInternal(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.m10;
      float var6 = this.m11;
      float var7 = this.m12;
      float var8 = this.m13;
      float var9 = this.m20;
      float var10 = this.m21;
      float var11 = this.m22;
      float var12 = this.m23;
      return var2._m20(var5 * -var3 + var9 * var4)._m21(var6 * -var3 + var10 * var4)._m22(var7 * -var3 + var11 * var4)._m23(var8 * -var3 + var12 * var4)._m10(var5 * var4 + var9 * var3)._m11(var6 * var4 + var10 * var3)._m12(var7 * var4 + var11 * var3)._m13(var8 * var4 + var12 * var3)._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateX(float var1) {
      return this.rotateX(var1, this);
   }

   public Matrix4f rotateY(float var1, Matrix4f var2) {
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

   private Matrix4f rotateYInternal(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.m00 * var4 + this.m20 * -var3;
      float var6 = this.m01 * var4 + this.m21 * -var3;
      float var7 = this.m02 * var4 + this.m22 * -var3;
      float var8 = this.m03 * var4 + this.m23 * -var3;
      return var2._m20(this.m00 * var3 + this.m20 * var4)._m21(this.m01 * var3 + this.m21 * var4)._m22(this.m02 * var3 + this.m22 * var4)._m23(this.m03 * var3 + this.m23 * var4)._m00(var5)._m01(var6)._m02(var7)._m03(var8)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateY(float var1) {
      return this.rotateY(var1, this);
   }

   public Matrix4f rotateZ(float var1, Matrix4f var2) {
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

   private Matrix4f rotateZInternal(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      return this.rotateTowardsXY(var3, var4, var2);
   }

   public Matrix4f rotateZ(float var1) {
      return this.rotateZ(var1, this);
   }

   public Matrix4f rotateTowardsXY(float var1, float var2) {
      return this.rotateTowardsXY(var1, var2, this);
   }

   public Matrix4f rotateTowardsXY(float var1, float var2, Matrix4f var3) {
      if ((this.properties & 4) != 0) {
         return var3.rotationTowardsXY(var1, var2);
      } else {
         float var4 = this.m00 * var2 + this.m10 * var1;
         float var5 = this.m01 * var2 + this.m11 * var1;
         float var6 = this.m02 * var2 + this.m12 * var1;
         float var7 = this.m03 * var2 + this.m13 * var1;
         return var3._m10(this.m00 * -var1 + this.m10 * var2)._m11(this.m01 * -var1 + this.m11 * var2)._m12(this.m02 * -var1 + this.m12 * var2)._m13(this.m03 * -var1 + this.m13 * var2)._m00(var4)._m01(var5)._m02(var6)._m03(var7)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
      }
   }

   public Matrix4f rotateXYZ(Vector3fc var1) {
      return this.rotateXYZ(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f rotateXYZ(float var1, float var2, float var3) {
      return this.rotateXYZ(var1, var2, var3, this);
   }

   public Matrix4f rotateXYZ(float var1, float var2, float var3, Matrix4f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationXYZ(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationXYZ(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return (this.properties & 2) != 0 ? var4.rotateAffineXYZ(var1, var2, var3) : this.rotateXYZInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4f rotateXYZInternal(float var1, float var2, float var3, Matrix4f var4) {
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
      float var17 = this.m13 * var6 + this.m23 * var5;
      float var18 = this.m10 * var11 + this.m20 * var6;
      float var19 = this.m11 * var11 + this.m21 * var6;
      float var20 = this.m12 * var11 + this.m22 * var6;
      float var21 = this.m13 * var11 + this.m23 * var6;
      float var22 = this.m00 * var8 + var18 * var12;
      float var23 = this.m01 * var8 + var19 * var12;
      float var24 = this.m02 * var8 + var20 * var12;
      float var25 = this.m03 * var8 + var21 * var12;
      return var4._m20(this.m00 * var7 + var18 * var8)._m21(this.m01 * var7 + var19 * var8)._m22(this.m02 * var7 + var20 * var8)._m23(this.m03 * var7 + var21 * var8)._m00(var22 * var10 + var14 * var9)._m01(var23 * var10 + var15 * var9)._m02(var24 * var10 + var16 * var9)._m03(var25 * var10 + var17 * var9)._m10(var22 * var13 + var14 * var10)._m11(var23 * var13 + var15 * var10)._m12(var24 * var13 + var16 * var10)._m13(var25 * var13 + var17 * var10)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffineXYZ(float var1, float var2, float var3) {
      return this.rotateAffineXYZ(var1, var2, var3, this);
   }

   public Matrix4f rotateAffineXYZ(float var1, float var2, float var3, Matrix4f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationXYZ(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationXYZ(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return this.rotateAffineXYZInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4f rotateAffineXYZInternal(float var1, float var2, float var3, Matrix4f var4) {
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
      return var4._m20(this.m00 * var7 + var17 * var8)._m21(this.m01 * var7 + var18 * var8)._m22(this.m02 * var7 + var19 * var8)._m23(0.0F)._m00(var20 * var10 + var14 * var9)._m01(var21 * var10 + var15 * var9)._m02(var22 * var10 + var16 * var9)._m03(0.0F)._m10(var20 * var13 + var14 * var10)._m11(var21 * var13 + var15 * var10)._m12(var22 * var13 + var16 * var10)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateZYX(Vector3f var1) {
      return this.rotateZYX(var1.z, var1.y, var1.x);
   }

   public Matrix4f rotateZYX(float var1, float var2, float var3) {
      return this.rotateZYX(var1, var2, var3, this);
   }

   public Matrix4f rotateZYX(float var1, float var2, float var3, Matrix4f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationZYX(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationZYX(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return (this.properties & 2) != 0 ? var4.rotateAffineZYX(var1, var2, var3) : this.rotateZYXInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4f rotateZYXInternal(float var1, float var2, float var3, Matrix4f var4) {
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
      float var17 = this.m03 * var10 + this.m13 * var9;
      float var18 = this.m00 * var11 + this.m10 * var10;
      float var19 = this.m01 * var11 + this.m11 * var10;
      float var20 = this.m02 * var11 + this.m12 * var10;
      float var21 = this.m03 * var11 + this.m13 * var10;
      float var22 = var14 * var7 + this.m20 * var8;
      float var23 = var15 * var7 + this.m21 * var8;
      float var24 = var16 * var7 + this.m22 * var8;
      float var25 = var17 * var7 + this.m23 * var8;
      return var4._m00(var14 * var8 + this.m20 * var12)._m01(var15 * var8 + this.m21 * var12)._m02(var16 * var8 + this.m22 * var12)._m03(var17 * var8 + this.m23 * var12)._m10(var18 * var6 + var22 * var5)._m11(var19 * var6 + var23 * var5)._m12(var20 * var6 + var24 * var5)._m13(var21 * var6 + var25 * var5)._m20(var18 * var13 + var22 * var6)._m21(var19 * var13 + var23 * var6)._m22(var20 * var13 + var24 * var6)._m23(var21 * var13 + var25 * var6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffineZYX(float var1, float var2, float var3) {
      return this.rotateAffineZYX(var1, var2, var3, this);
   }

   public Matrix4f rotateAffineZYX(float var1, float var2, float var3, Matrix4f var4) {
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
      return var4._m00(var14 * var8 + this.m20 * var12)._m01(var15 * var8 + this.m21 * var12)._m02(var16 * var8 + this.m22 * var12)._m03(0.0F)._m10(var17 * var6 + var20 * var5)._m11(var18 * var6 + var21 * var5)._m12(var19 * var6 + var22 * var5)._m13(0.0F)._m20(var17 * var13 + var20 * var6)._m21(var18 * var13 + var21 * var6)._m22(var19 * var13 + var22 * var6)._m23(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateYXZ(Vector3f var1) {
      return this.rotateYXZ(var1.y, var1.x, var1.z);
   }

   public Matrix4f rotateYXZ(float var1, float var2, float var3) {
      return this.rotateYXZ(var1, var2, var3, this);
   }

   public Matrix4f rotateYXZ(float var1, float var2, float var3, Matrix4f var4) {
      if ((this.properties & 4) != 0) {
         return var4.rotationYXZ(var1, var2, var3);
      } else if ((this.properties & 8) != 0) {
         float var5 = this.m30;
         float var6 = this.m31;
         float var7 = this.m32;
         return var4.rotationYXZ(var1, var2, var3).setTranslation(var5, var6, var7);
      } else {
         return (this.properties & 2) != 0 ? var4.rotateAffineYXZ(var1, var2, var3) : this.rotateYXZInternal(var1, var2, var3, var4);
      }
   }

   private Matrix4f rotateYXZInternal(float var1, float var2, float var3, Matrix4f var4) {
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
      float var17 = this.m03 * var7 + this.m23 * var8;
      float var18 = this.m00 * var8 + this.m20 * var11;
      float var19 = this.m01 * var8 + this.m21 * var11;
      float var20 = this.m02 * var8 + this.m22 * var11;
      float var21 = this.m03 * var8 + this.m23 * var11;
      float var22 = this.m10 * var6 + var14 * var5;
      float var23 = this.m11 * var6 + var15 * var5;
      float var24 = this.m12 * var6 + var16 * var5;
      float var25 = this.m13 * var6 + var17 * var5;
      return var4._m20(this.m10 * var12 + var14 * var6)._m21(this.m11 * var12 + var15 * var6)._m22(this.m12 * var12 + var16 * var6)._m23(this.m13 * var12 + var17 * var6)._m00(var18 * var10 + var22 * var9)._m01(var19 * var10 + var23 * var9)._m02(var20 * var10 + var24 * var9)._m03(var21 * var10 + var25 * var9)._m10(var18 * var13 + var22 * var10)._m11(var19 * var13 + var23 * var10)._m12(var20 * var13 + var24 * var10)._m13(var21 * var13 + var25 * var10)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffineYXZ(float var1, float var2, float var3) {
      return this.rotateAffineYXZ(var1, var2, var3, this);
   }

   public Matrix4f rotateAffineYXZ(float var1, float var2, float var3, Matrix4f var4) {
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
      return var4._m20(this.m10 * var12 + var14 * var6)._m21(this.m11 * var12 + var15 * var6)._m22(this.m12 * var12 + var16 * var6)._m23(0.0F)._m00(var17 * var10 + var20 * var9)._m01(var18 * var10 + var21 * var9)._m02(var19 * var10 + var22 * var9)._m03(0.0F)._m10(var17 * var13 + var20 * var10)._m11(var18 * var13 + var21 * var10)._m12(var19 * var13 + var22 * var10)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotate(float var1, float var2, float var3, float var4, Matrix4f var5) {
      if ((this.properties & 4) != 0) {
         return var5.rotation(var1, var2, var3, var4);
      } else if ((this.properties & 8) != 0) {
         return this.rotateTranslation(var1, var2, var3, var4, var5);
      } else {
         return (this.properties & 2) != 0 ? this.rotateAffine(var1, var2, var3, var4, var5) : this.rotateGeneric(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f rotateGeneric(float var1, float var2, float var3, float var4, Matrix4f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, var5) : this.rotateGenericInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f rotateGenericInternal(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      float var27 = this.m03 * var15 + this.m13 * var16 + this.m23 * var17;
      float var28 = this.m00 * var18 + this.m10 * var19 + this.m20 * var20;
      float var29 = this.m01 * var18 + this.m11 * var19 + this.m21 * var20;
      float var30 = this.m02 * var18 + this.m12 * var19 + this.m22 * var20;
      float var31 = this.m03 * var18 + this.m13 * var19 + this.m23 * var20;
      return var5._m20(this.m00 * var21 + this.m10 * var22 + this.m20 * var23)._m21(this.m01 * var21 + this.m11 * var22 + this.m21 * var23)._m22(this.m02 * var21 + this.m12 * var22 + this.m22 * var23)._m23(this.m03 * var21 + this.m13 * var22 + this.m23 * var23)._m00(var24)._m01(var25)._m02(var26)._m03(var27)._m10(var28)._m11(var29)._m12(var30)._m13(var31)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotate(float var1, float var2, float var3, float var4) {
      return this.rotate(var1, var2, var3, var4, this);
   }

   public Matrix4f rotateTranslation(float var1, float var2, float var3, float var4, Matrix4f var5) {
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

   private Matrix4f rotateTranslationInternal(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      return var5._m20(var21)._m21(var22)._m22(var23)._m00(var15)._m01(var16)._m02(var17)._m03(0.0F)._m10(var18)._m11(var19)._m12(var20)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(1.0F)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffine(float var1, float var2, float var3, float var4, Matrix4f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, var5) : this.rotateAffineInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f rotateAffineInternal(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      return var5._m20(this.m00 * var21 + this.m10 * var22 + this.m20 * var23)._m21(this.m01 * var21 + this.m11 * var22 + this.m21 * var23)._m22(this.m02 * var21 + this.m12 * var22 + this.m22 * var23)._m23(0.0F)._m00(var24)._m01(var25)._m02(var26)._m03(0.0F)._m10(var27)._m11(var28)._m12(var29)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(1.0F)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffine(float var1, float var2, float var3, float var4) {
      return this.rotateAffine(var1, var2, var3, var4, this);
   }

   public Matrix4f rotateLocal(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return (this.properties & 4) != 0 ? var5.rotation(var1, var2, var3, var4) : this.rotateLocalGeneric(var1, var2, var3, var4, var5);
   }

   private Matrix4f rotateLocalGeneric(float var1, float var2, float var3, float var4, Matrix4f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateLocalX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateLocalY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateLocalZ(var4 * var1, var5) : this.rotateLocalGenericInternal(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f rotateLocalGenericInternal(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      return var5._m00(var24)._m01(var25)._m02(var26)._m03(this.m03)._m10(var27)._m11(var28)._m12(var29)._m13(this.m13)._m20(var30)._m21(var31)._m22(var32)._m23(this.m23)._m30(var33)._m31(var34)._m32(var35)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateLocal(float var1, float var2, float var3, float var4) {
      return this.rotateLocal(var1, var2, var3, var4, this);
   }

   public Matrix4f rotateLocalX(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var3 * this.m01 + var4 * this.m02;
      float var6 = var3 * this.m11 + var4 * this.m12;
      float var7 = var3 * this.m21 + var4 * this.m22;
      float var8 = var3 * this.m31 + var4 * this.m32;
      return var2._m00(this.m00)._m01(var4 * this.m01 - var3 * this.m02)._m02(var5)._m03(this.m03)._m10(this.m10)._m11(var4 * this.m11 - var3 * this.m12)._m12(var6)._m13(this.m13)._m20(this.m20)._m21(var4 * this.m21 - var3 * this.m22)._m22(var7)._m23(this.m23)._m30(this.m30)._m31(var4 * this.m31 - var3 * this.m32)._m32(var8)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateLocalX(float var1) {
      return this.rotateLocalX(var1, this);
   }

   public Matrix4f rotateLocalY(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = -var3 * this.m00 + var4 * this.m02;
      float var6 = -var3 * this.m10 + var4 * this.m12;
      float var7 = -var3 * this.m20 + var4 * this.m22;
      float var8 = -var3 * this.m30 + var4 * this.m32;
      return var2._m00(var4 * this.m00 + var3 * this.m02)._m01(this.m01)._m02(var5)._m03(this.m03)._m10(var4 * this.m10 + var3 * this.m12)._m11(this.m11)._m12(var6)._m13(this.m13)._m20(var4 * this.m20 + var3 * this.m22)._m21(this.m21)._m22(var7)._m23(this.m23)._m30(var4 * this.m30 + var3 * this.m32)._m31(this.m31)._m32(var8)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateLocalY(float var1) {
      return this.rotateLocalY(var1, this);
   }

   public Matrix4f rotateLocalZ(float var1, Matrix4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var3 * this.m00 + var4 * this.m01;
      float var6 = var3 * this.m10 + var4 * this.m11;
      float var7 = var3 * this.m20 + var4 * this.m21;
      float var8 = var3 * this.m30 + var4 * this.m31;
      return var2._m00(var4 * this.m00 - var3 * this.m01)._m01(var5)._m02(this.m02)._m03(this.m03)._m10(var4 * this.m10 - var3 * this.m11)._m11(var6)._m12(this.m12)._m13(this.m13)._m20(var4 * this.m20 - var3 * this.m21)._m21(var7)._m22(this.m22)._m23(this.m23)._m30(var4 * this.m30 - var3 * this.m31)._m31(var8)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateLocalZ(float var1) {
      return this.rotateLocalZ(var1, this);
   }

   public Matrix4f translate(Vector3fc var1) {
      return this.translate(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f translate(Vector3fc var1, Matrix4f var2) {
      return this.translate(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4f translate(float var1, float var2, float var3, Matrix4f var4) {
      return (this.properties & 4) != 0 ? var4.translation(var1, var2, var3) : this.translateGeneric(var1, var2, var3, var4);
   }

   private Matrix4f translateGeneric(float var1, float var2, float var3, Matrix4f var4) {
      MemUtil.INSTANCE.copy(this, var4);
      return var4._m30(Math.fma(this.m00, var1, Math.fma(this.m10, var2, Math.fma(this.m20, var3, this.m30))))._m31(Math.fma(this.m01, var1, Math.fma(this.m11, var2, Math.fma(this.m21, var3, this.m31))))._m32(Math.fma(this.m02, var1, Math.fma(this.m12, var2, Math.fma(this.m22, var3, this.m32))))._m33(Math.fma(this.m03, var1, Math.fma(this.m13, var2, Math.fma(this.m23, var3, this.m33))))._properties(this.properties & -6);
   }

   public Matrix4f translate(float var1, float var2, float var3) {
      return (this.properties & 4) != 0 ? this.translation(var1, var2, var3) : this.translateGeneric(var1, var2, var3);
   }

   private Matrix4f translateGeneric(float var1, float var2, float var3) {
      return this._m30(Math.fma(this.m00, var1, Math.fma(this.m10, var2, Math.fma(this.m20, var3, this.m30))))._m31(Math.fma(this.m01, var1, Math.fma(this.m11, var2, Math.fma(this.m21, var3, this.m31))))._m32(Math.fma(this.m02, var1, Math.fma(this.m12, var2, Math.fma(this.m22, var3, this.m32))))._m33(Math.fma(this.m03, var1, Math.fma(this.m13, var2, Math.fma(this.m23, var3, this.m33))))._properties(this.properties & -6);
   }

   public Matrix4f translateLocal(Vector3fc var1) {
      return this.translateLocal(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f translateLocal(Vector3fc var1, Matrix4f var2) {
      return this.translateLocal(var1.x(), var1.y(), var1.z(), var2);
   }

   public Matrix4f translateLocal(float var1, float var2, float var3, Matrix4f var4) {
      return (this.properties & 4) != 0 ? var4.translation(var1, var2, var3) : this.translateLocalGeneric(var1, var2, var3, var4);
   }

   private Matrix4f translateLocalGeneric(float var1, float var2, float var3, Matrix4f var4) {
      float var5 = this.m00 + var1 * this.m03;
      float var6 = this.m01 + var2 * this.m03;
      float var7 = this.m02 + var3 * this.m03;
      float var8 = this.m10 + var1 * this.m13;
      float var9 = this.m11 + var2 * this.m13;
      float var10 = this.m12 + var3 * this.m13;
      float var11 = this.m20 + var1 * this.m23;
      float var12 = this.m21 + var2 * this.m23;
      float var13 = this.m22 + var3 * this.m23;
      float var14 = this.m30 + var1 * this.m33;
      float var15 = this.m31 + var2 * this.m33;
      float var16 = this.m32 + var3 * this.m33;
      return var4._m00(var5)._m01(var6)._m02(var7)._m03(this.m03)._m10(var8)._m11(var9)._m12(var10)._m13(this.m13)._m20(var11)._m21(var12)._m22(var13)._m23(this.m23)._m30(var14)._m31(var15)._m32(var16)._m33(this.m33)._properties(this.properties & -6);
   }

   public Matrix4f translateLocal(float var1, float var2, float var3) {
      return this.translateLocal(var1, var2, var3, this);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.m00);
      var1.writeFloat(this.m01);
      var1.writeFloat(this.m02);
      var1.writeFloat(this.m03);
      var1.writeFloat(this.m10);
      var1.writeFloat(this.m11);
      var1.writeFloat(this.m12);
      var1.writeFloat(this.m13);
      var1.writeFloat(this.m20);
      var1.writeFloat(this.m21);
      var1.writeFloat(this.m22);
      var1.writeFloat(this.m23);
      var1.writeFloat(this.m30);
      var1.writeFloat(this.m31);
      var1.writeFloat(this.m32);
      var1.writeFloat(this.m33);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this._m00(var1.readFloat())._m01(var1.readFloat())._m02(var1.readFloat())._m03(var1.readFloat())._m10(var1.readFloat())._m11(var1.readFloat())._m12(var1.readFloat())._m13(var1.readFloat())._m20(var1.readFloat())._m21(var1.readFloat())._m22(var1.readFloat())._m23(var1.readFloat())._m30(var1.readFloat())._m31(var1.readFloat())._m32(var1.readFloat())._m33(var1.readFloat()).determineProperties();
   }

   public Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      return (this.properties & 4) != 0 ? var8.setOrtho(var1, var2, var3, var4, var5, var6, var7) : this.orthoGeneric(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private Matrix4f orthoGeneric(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      float var9 = 2.0F / (var2 - var1);
      float var10 = 2.0F / (var4 - var3);
      float var11 = (var7 ? 1.0F : 2.0F) / (var5 - var6);
      float var12 = (var1 + var2) / (var1 - var2);
      float var13 = (var4 + var3) / (var3 - var4);
      float var14 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      var8._m30(this.m00 * var12 + this.m10 * var13 + this.m20 * var14 + this.m30)._m31(this.m01 * var12 + this.m11 * var13 + this.m21 * var14 + this.m31)._m32(this.m02 * var12 + this.m12 * var13 + this.m22 * var14 + this.m32)._m33(this.m03 * var12 + this.m13 * var13 + this.m23 * var14 + this.m33)._m00(this.m00 * var9)._m01(this.m01 * var9)._m02(this.m02 * var9)._m03(this.m03 * var9)._m10(this.m10 * var10)._m11(this.m11 * var10)._m12(this.m12 * var10)._m13(this.m13 * var10)._m20(this.m20 * var11)._m21(this.m21 * var11)._m22(this.m22 * var11)._m23(this.m23 * var11)._properties(this.properties & -30);
      return var8;
   }

   public Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return this.ortho(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.ortho(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.ortho(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      return (this.properties & 4) != 0 ? var8.setOrthoLH(var1, var2, var3, var4, var5, var6, var7) : this.orthoLHGeneric(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private Matrix4f orthoLHGeneric(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      float var9 = 2.0F / (var2 - var1);
      float var10 = 2.0F / (var4 - var3);
      float var11 = (var7 ? 1.0F : 2.0F) / (var6 - var5);
      float var12 = (var1 + var2) / (var1 - var2);
      float var13 = (var4 + var3) / (var3 - var4);
      float var14 = (var7 ? var5 : var6 + var5) / (var5 - var6);
      var8._m30(this.m00 * var12 + this.m10 * var13 + this.m20 * var14 + this.m30)._m31(this.m01 * var12 + this.m11 * var13 + this.m21 * var14 + this.m31)._m32(this.m02 * var12 + this.m12 * var13 + this.m22 * var14 + this.m32)._m33(this.m03 * var12 + this.m13 * var13 + this.m23 * var14 + this.m33)._m00(this.m00 * var9)._m01(this.m01 * var9)._m02(this.m02 * var9)._m03(this.m03 * var9)._m10(this.m10 * var10)._m11(this.m11 * var10)._m12(this.m12 * var10)._m13(this.m13 * var10)._m20(this.m20 * var11)._m21(this.m21 * var11)._m22(this.m22 * var11)._m23(this.m23 * var11)._properties(this.properties & -30);
      return var8;
   }

   public Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.orthoLH(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f setOrtho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / (var2 - var1))._m11(2.0F / (var4 - var3))._m22((var7 ? 1.0F : 2.0F) / (var5 - var6))._m30((var2 + var1) / (var1 - var2))._m31((var4 + var3) / (var3 - var4))._m32((var7 ? var5 : var6 + var5) / (var5 - var6))._properties(2);
      return this;
   }

   public Matrix4f setOrtho(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setOrtho(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f setOrthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / (var2 - var1))._m11(2.0F / (var4 - var3))._m22((var7 ? 1.0F : 2.0F) / (var6 - var5))._m30((var2 + var1) / (var1 - var2))._m31((var4 + var3) / (var3 - var4))._m32((var7 ? var5 : var6 + var5) / (var5 - var6))._properties(2);
      return this;
   }

   public Matrix4f setOrthoLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setOrthoLH(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      return (this.properties & 4) != 0 ? var6.setOrthoSymmetric(var1, var2, var3, var4, var5) : this.orthoSymmetricGeneric(var1, var2, var3, var4, var5, var6);
   }

   private Matrix4f orthoSymmetricGeneric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      float var7 = 2.0F / var1;
      float var8 = 2.0F / var2;
      float var9 = (var5 ? 1.0F : 2.0F) / (var3 - var4);
      float var10 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      var6._m30(this.m20 * var10 + this.m30)._m31(this.m21 * var10 + this.m31)._m32(this.m22 * var10 + this.m32)._m33(this.m23 * var10 + this.m33)._m00(this.m00 * var7)._m01(this.m01 * var7)._m02(this.m02 * var7)._m03(this.m03 * var7)._m10(this.m10 * var8)._m11(this.m11 * var8)._m12(this.m12 * var8)._m13(this.m13 * var8)._m20(this.m20 * var9)._m21(this.m21 * var9)._m22(this.m22 * var9)._m23(this.m23 * var9)._properties(this.properties & -30);
      return var6;
   }

   public Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.orthoSymmetric(var1, var2, var3, var4, false, var5);
   }

   public Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5) {
      return this.orthoSymmetric(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4) {
      return this.orthoSymmetric(var1, var2, var3, var4, false, this);
   }

   public Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      return (this.properties & 4) != 0 ? var6.setOrthoSymmetricLH(var1, var2, var3, var4, var5) : this.orthoSymmetricLHGeneric(var1, var2, var3, var4, var5, var6);
   }

   private Matrix4f orthoSymmetricLHGeneric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      float var7 = 2.0F / var1;
      float var8 = 2.0F / var2;
      float var9 = (var5 ? 1.0F : 2.0F) / (var4 - var3);
      float var10 = (var5 ? var3 : var4 + var3) / (var3 - var4);
      var6._m30(this.m20 * var10 + this.m30)._m31(this.m21 * var10 + this.m31)._m32(this.m22 * var10 + this.m32)._m33(this.m23 * var10 + this.m33)._m00(this.m00 * var7)._m01(this.m01 * var7)._m02(this.m02 * var7)._m03(this.m03 * var7)._m10(this.m10 * var8)._m11(this.m11 * var8)._m12(this.m12 * var8)._m13(this.m13 * var8)._m20(this.m20 * var9)._m21(this.m21 * var9)._m22(this.m22 * var9)._m23(this.m23 * var9)._properties(this.properties & -30);
      return var6;
   }

   public Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, false, var5);
   }

   public Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4) {
      return this.orthoSymmetricLH(var1, var2, var3, var4, false, this);
   }

   public Matrix4f setOrthoSymmetric(float var1, float var2, float var3, float var4, boolean var5) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / var1)._m11(2.0F / var2)._m22((var5 ? 1.0F : 2.0F) / (var3 - var4))._m32((var5 ? var3 : var4 + var3) / (var3 - var4))._properties(2);
      return this;
   }

   public Matrix4f setOrthoSymmetric(float var1, float var2, float var3, float var4) {
      return this.setOrthoSymmetric(var1, var2, var3, var4, false);
   }

   public Matrix4f setOrthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / var1)._m11(2.0F / var2)._m22((var5 ? 1.0F : 2.0F) / (var4 - var3))._m32((var5 ? var3 : var4 + var3) / (var3 - var4))._properties(2);
      return this;
   }

   public Matrix4f setOrthoSymmetricLH(float var1, float var2, float var3, float var4) {
      return this.setOrthoSymmetricLH(var1, var2, var3, var4, false);
   }

   public Matrix4f ortho2D(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return (this.properties & 4) != 0 ? var5.setOrtho2D(var1, var2, var3, var4) : this.ortho2DGeneric(var1, var2, var3, var4, var5);
   }

   private Matrix4f ortho2DGeneric(float var1, float var2, float var3, float var4, Matrix4f var5) {
      float var6 = 2.0F / (var2 - var1);
      float var7 = 2.0F / (var4 - var3);
      float var8 = (var2 + var1) / (var1 - var2);
      float var9 = (var4 + var3) / (var3 - var4);
      var5._m30(this.m00 * var8 + this.m10 * var9 + this.m30)._m31(this.m01 * var8 + this.m11 * var9 + this.m31)._m32(this.m02 * var8 + this.m12 * var9 + this.m32)._m33(this.m03 * var8 + this.m13 * var9 + this.m33)._m00(this.m00 * var6)._m01(this.m01 * var6)._m02(this.m02 * var6)._m03(this.m03 * var6)._m10(this.m10 * var7)._m11(this.m11 * var7)._m12(this.m12 * var7)._m13(this.m13 * var7)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(-this.m23)._properties(this.properties & -30);
      return var5;
   }

   public Matrix4f ortho2D(float var1, float var2, float var3, float var4) {
      return this.ortho2D(var1, var2, var3, var4, this);
   }

   public Matrix4f ortho2DLH(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return (this.properties & 4) != 0 ? var5.setOrtho2DLH(var1, var2, var3, var4) : this.ortho2DLHGeneric(var1, var2, var3, var4, var5);
   }

   private Matrix4f ortho2DLHGeneric(float var1, float var2, float var3, float var4, Matrix4f var5) {
      float var6 = 2.0F / (var2 - var1);
      float var7 = 2.0F / (var4 - var3);
      float var8 = (var2 + var1) / (var1 - var2);
      float var9 = (var4 + var3) / (var3 - var4);
      var5._m30(this.m00 * var8 + this.m10 * var9 + this.m30)._m31(this.m01 * var8 + this.m11 * var9 + this.m31)._m32(this.m02 * var8 + this.m12 * var9 + this.m32)._m33(this.m03 * var8 + this.m13 * var9 + this.m33)._m00(this.m00 * var6)._m01(this.m01 * var6)._m02(this.m02 * var6)._m03(this.m03 * var6)._m10(this.m10 * var7)._m11(this.m11 * var7)._m12(this.m12 * var7)._m13(this.m13 * var7)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._properties(this.properties & -30);
      return var5;
   }

   public Matrix4f ortho2DLH(float var1, float var2, float var3, float var4) {
      return this.ortho2DLH(var1, var2, var3, var4, this);
   }

   public Matrix4f setOrtho2D(float var1, float var2, float var3, float var4) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / (var2 - var1))._m11(2.0F / (var4 - var3))._m22(-1.0F)._m30((var2 + var1) / (var1 - var2))._m31((var4 + var3) / (var3 - var4))._properties(2);
      return this;
   }

   public Matrix4f setOrtho2DLH(float var1, float var2, float var3, float var4) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(2.0F / (var2 - var1))._m11(2.0F / (var4 - var3))._m30((var2 + var1) / (var1 - var2))._m31((var4 + var3) / (var3 - var4))._properties(2);
      return this;
   }

   public Matrix4f lookAlong(Vector3fc var1, Vector3fc var2) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4f lookAlong(Vector3fc var1, Vector3fc var2, Matrix4f var3) {
      return this.lookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return (this.properties & 4) != 0 ? var7.setLookAlong(var1, var2, var3, var4, var5, var6) : this.lookAlongGeneric(var1, var2, var3, var4, var5, var6, var7);
   }

   private Matrix4f lookAlongGeneric(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
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
      float var16 = this.m00 * var9 + this.m10 * var13 + this.m20 * var1;
      float var17 = this.m01 * var9 + this.m11 * var13 + this.m21 * var1;
      float var18 = this.m02 * var9 + this.m12 * var13 + this.m22 * var1;
      float var19 = this.m03 * var9 + this.m13 * var13 + this.m23 * var1;
      float var20 = this.m00 * var10 + this.m10 * var14 + this.m20 * var2;
      float var21 = this.m01 * var10 + this.m11 * var14 + this.m21 * var2;
      float var22 = this.m02 * var10 + this.m12 * var14 + this.m22 * var2;
      float var23 = this.m03 * var10 + this.m13 * var14 + this.m23 * var2;
      return var7._m20(this.m00 * var11 + this.m10 * var15 + this.m20 * var3)._m21(this.m01 * var11 + this.m11 * var15 + this.m21 * var3)._m22(this.m02 * var11 + this.m12 * var15 + this.m22 * var3)._m23(this.m03 * var11 + this.m13 * var15 + this.m23 * var3)._m00(var16)._m01(var17)._m02(var18)._m03(var19)._m10(var20)._m11(var21)._m12(var22)._m13(var23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.lookAlong(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f setLookAlong(Vector3fc var1, Vector3fc var2) {
      return this.setLookAlong(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4f setLookAlong(float var1, float var2, float var3, float var4, float var5, float var6) {
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
      this._m00(var8)._m01(var2 * var10 - var3 * var9)._m02(var1)._m03(0.0F)._m10(var9)._m11(var3 * var8 - var1 * var10)._m12(var2)._m13(0.0F)._m20(var10)._m21(var1 * var9 - var2 * var8)._m22(var3)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f setLookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.setLookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4f setLookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
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
      return this._m00(var14)._m01(var18)._m02(var10)._m03(0.0F)._m10(var15)._m11(var19)._m12(var11)._m13(0.0F)._m20(var16)._m21(var20)._m22(var12)._m23(0.0F)._m30(-(var14 * var1 + var15 * var2 + var16 * var3))._m31(-(var18 * var1 + var19 * var2 + var20 * var3))._m32(-(var10 * var1 + var11 * var2 + var12 * var3))._m33(1.0F)._properties(18);
   }

   public Matrix4f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4f var4) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.lookAt(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
      if ((this.properties & 4) != 0) {
         return var10.setLookAt(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      } else {
         return (this.properties & 1) != 0 ? this.lookAtPerspective(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10) : this.lookAtGeneric(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }
   }

   private Matrix4f lookAtGeneric(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
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
      float var22 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var23 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var24 = -(var11 * var1 + var12 * var2 + var13 * var3);
      float var25 = this.m00 * var15 + this.m10 * var19 + this.m20 * var11;
      float var26 = this.m01 * var15 + this.m11 * var19 + this.m21 * var11;
      float var27 = this.m02 * var15 + this.m12 * var19 + this.m22 * var11;
      float var28 = this.m03 * var15 + this.m13 * var19 + this.m23 * var11;
      float var29 = this.m00 * var16 + this.m10 * var20 + this.m20 * var12;
      float var30 = this.m01 * var16 + this.m11 * var20 + this.m21 * var12;
      float var31 = this.m02 * var16 + this.m12 * var20 + this.m22 * var12;
      float var32 = this.m03 * var16 + this.m13 * var20 + this.m23 * var12;
      return var10._m30(this.m00 * var22 + this.m10 * var23 + this.m20 * var24 + this.m30)._m31(this.m01 * var22 + this.m11 * var23 + this.m21 * var24 + this.m31)._m32(this.m02 * var22 + this.m12 * var23 + this.m22 * var24 + this.m32)._m33(this.m03 * var22 + this.m13 * var23 + this.m23 * var24 + this.m33)._m20(this.m00 * var17 + this.m10 * var21 + this.m20 * var13)._m21(this.m01 * var17 + this.m11 * var21 + this.m21 * var13)._m22(this.m02 * var17 + this.m12 * var21 + this.m22 * var13)._m23(this.m03 * var17 + this.m13 * var21 + this.m23 * var13)._m00(var25)._m01(var26)._m02(var27)._m03(var28)._m10(var29)._m11(var30)._m12(var31)._m13(var32)._properties(this.properties & -14);
   }

   public Matrix4f lookAtPerspective(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
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
      float var22 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var23 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var24 = -(var11 * var1 + var12 * var2 + var13 * var3);
      float var25 = this.m00 * var16;
      float var26 = this.m00 * var17;
      float var27 = this.m11 * var21;
      float var28 = this.m00 * var22;
      float var29 = this.m11 * var23;
      float var30 = this.m22 * var24 + this.m32;
      float var31 = this.m23 * var24;
      return var10._m00(this.m00 * var15)._m01(this.m11 * var19)._m02(this.m22 * var11)._m03(this.m23 * var11)._m10(var25)._m11(this.m11 * var20)._m12(this.m22 * var12)._m13(this.m23 * var12)._m20(var26)._m21(var27)._m22(this.m22 * var13)._m23(this.m23 * var13)._m30(var28)._m31(var29)._m32(var30)._m33(var31)._properties(0);
   }

   public Matrix4f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      return this.lookAt(var1, var2, var3, var4, var5, var6, var7, var8, var9, this);
   }

   public Matrix4f setLookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.setLookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4f setLookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
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
      this._m00(var14)._m01(var18)._m02(var10)._m03(0.0F)._m10(var15)._m11(var19)._m12(var11)._m13(0.0F)._m20(var16)._m21(var20)._m22(var12)._m23(0.0F)._m30(-(var14 * var1 + var15 * var2 + var16 * var3))._m31(-(var18 * var1 + var19 * var2 + var20 * var3))._m32(-(var10 * var1 + var11 * var2 + var12 * var3))._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4f var4) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), var4);
   }

   public Matrix4f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.lookAtLH(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z(), this);
   }

   public Matrix4f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
      if ((this.properties & 4) != 0) {
         return var10.setLookAtLH(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      } else {
         return (this.properties & 1) != 0 ? this.lookAtPerspectiveLH(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10) : this.lookAtLHGeneric(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }
   }

   private Matrix4f lookAtLHGeneric(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
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
      float var22 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var23 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var24 = -(var11 * var1 + var12 * var2 + var13 * var3);
      float var25 = this.m00 * var15 + this.m10 * var19 + this.m20 * var11;
      float var26 = this.m01 * var15 + this.m11 * var19 + this.m21 * var11;
      float var27 = this.m02 * var15 + this.m12 * var19 + this.m22 * var11;
      float var28 = this.m03 * var15 + this.m13 * var19 + this.m23 * var11;
      float var29 = this.m00 * var16 + this.m10 * var20 + this.m20 * var12;
      float var30 = this.m01 * var16 + this.m11 * var20 + this.m21 * var12;
      float var31 = this.m02 * var16 + this.m12 * var20 + this.m22 * var12;
      float var32 = this.m03 * var16 + this.m13 * var20 + this.m23 * var12;
      return var10._m30(this.m00 * var22 + this.m10 * var23 + this.m20 * var24 + this.m30)._m31(this.m01 * var22 + this.m11 * var23 + this.m21 * var24 + this.m31)._m32(this.m02 * var22 + this.m12 * var23 + this.m22 * var24 + this.m32)._m33(this.m03 * var22 + this.m13 * var23 + this.m23 * var24 + this.m33)._m20(this.m00 * var17 + this.m10 * var21 + this.m20 * var13)._m21(this.m01 * var17 + this.m11 * var21 + this.m21 * var13)._m22(this.m02 * var17 + this.m12 * var21 + this.m22 * var13)._m23(this.m03 * var17 + this.m13 * var21 + this.m23 * var13)._m00(var25)._m01(var26)._m02(var27)._m03(var28)._m10(var29)._m11(var30)._m12(var31)._m13(var32)._properties(this.properties & -14);
   }

   public Matrix4f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      return this.lookAtLH(var1, var2, var3, var4, var5, var6, var7, var8, var9, this);
   }

   public Matrix4f lookAtPerspectiveLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10) {
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
      float var22 = -(var15 * var1 + var16 * var2 + var17 * var3);
      float var23 = -(var19 * var1 + var20 * var2 + var21 * var3);
      float var24 = -(var11 * var1 + var12 * var2 + var13 * var3);
      float var25 = this.m00 * var15;
      float var26 = this.m11 * var19;
      float var27 = this.m22 * var11;
      float var28 = this.m23 * var11;
      float var29 = this.m00 * var16;
      float var30 = this.m11 * var20;
      float var31 = this.m22 * var12;
      float var32 = this.m23 * var12;
      float var33 = this.m00 * var17;
      float var34 = this.m11 * var21;
      float var35 = this.m22 * var13;
      float var36 = this.m23 * var13;
      float var37 = this.m00 * var22;
      float var38 = this.m11 * var23;
      float var39 = this.m22 * var24 + this.m32;
      float var40 = this.m23 * var24;
      return var10._m00(var25)._m01(var26)._m02(var27)._m03(var28)._m10(var29)._m11(var30)._m12(var31)._m13(var32)._m20(var33)._m21(var34)._m22(var35)._m23(var36)._m30(var37)._m31(var38)._m32(var39)._m33(var40)._properties(0);
   }

   public Matrix4f perspective(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      return (this.properties & 4) != 0 ? var6.setPerspective(var1, var2, var3, var4, var5) : this.perspectiveGeneric(var1, var2, var3, var4, var5, var6);
   }

   private Matrix4f perspectiveGeneric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      float var7 = Math.tan(var1 * 0.5F);
      float var8 = 1.0F / (var7 * var2);
      float var9 = 1.0F / var7;
      boolean var12 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var13 = var3 > 0.0F && Float.isInfinite(var3);
      float var10;
      float var11;
      float var14;
      if (var12) {
         var14 = 1.0E-6F;
         var10 = var14 - 1.0F;
         var11 = (var14 - (var5 ? 1.0F : 2.0F)) * var3;
      } else if (var13) {
         var14 = 1.0E-6F;
         var10 = (var5 ? 0.0F : 1.0F) - var14;
         var11 = ((var5 ? 1.0F : 2.0F) - var14) * var4;
      } else {
         var10 = (var5 ? var4 : var4 + var3) / (var3 - var4);
         var11 = (var5 ? var4 : var4 + var4) * var3 / (var3 - var4);
      }

      var14 = this.m20 * var10 - this.m30;
      float var15 = this.m21 * var10 - this.m31;
      float var16 = this.m22 * var10 - this.m32;
      float var17 = this.m23 * var10 - this.m33;
      var6._m00(this.m00 * var8)._m01(this.m01 * var8)._m02(this.m02 * var8)._m03(this.m03 * var8)._m10(this.m10 * var9)._m11(this.m11 * var9)._m12(this.m12 * var9)._m13(this.m13 * var9)._m30(this.m20 * var11)._m31(this.m21 * var11)._m32(this.m22 * var11)._m33(this.m23 * var11)._m20(var14)._m21(var15)._m22(var16)._m23(var17)._properties(this.properties & -31);
      return var6;
   }

   public Matrix4f perspective(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.perspective(var1, var2, var3, var4, false, var5);
   }

   public Matrix4f perspective(float var1, float var2, float var3, float var4, boolean var5) {
      return this.perspective(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f perspective(float var1, float var2, float var3, float var4) {
      return this.perspective(var1, var2, var3, var4, this);
   }

   public Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      return (this.properties & 4) != 0 ? var6.setPerspectiveRect(var1, var2, var3, var4, var5) : this.perspectiveRectGeneric(var1, var2, var3, var4, var5, var6);
   }

   private Matrix4f perspectiveRectGeneric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      float var7 = (var3 + var3) / var1;
      float var8 = (var3 + var3) / var2;
      boolean var11 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var12 = var3 > 0.0F && Float.isInfinite(var3);
      float var9;
      float var10;
      float var13;
      if (var11) {
         var13 = 1.0E-6F;
         var9 = var13 - 1.0F;
         var10 = (var13 - (var5 ? 1.0F : 2.0F)) * var3;
      } else if (var12) {
         var13 = 1.0E-6F;
         var9 = (var5 ? 0.0F : 1.0F) - var13;
         var10 = ((var5 ? 1.0F : 2.0F) - var13) * var4;
      } else {
         var9 = (var5 ? var4 : var4 + var3) / (var3 - var4);
         var10 = (var5 ? var4 : var4 + var4) * var3 / (var3 - var4);
      }

      var13 = this.m20 * var9 - this.m30;
      float var14 = this.m21 * var9 - this.m31;
      float var15 = this.m22 * var9 - this.m32;
      float var16 = this.m23 * var9 - this.m33;
      var6._m00(this.m00 * var7)._m01(this.m01 * var7)._m02(this.m02 * var7)._m03(this.m03 * var7)._m10(this.m10 * var8)._m11(this.m11 * var8)._m12(this.m12 * var8)._m13(this.m13 * var8)._m30(this.m20 * var10)._m31(this.m21 * var10)._m32(this.m22 * var10)._m33(this.m23 * var10)._m20(var13)._m21(var14)._m22(var15)._m23(var16)._properties(this.properties & -31);
      return var6;
   }

   public Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.perspectiveRect(var1, var2, var3, var4, false, var5);
   }

   public Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, boolean var5) {
      return this.perspectiveRect(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f perspectiveRect(float var1, float var2, float var3, float var4) {
      return this.perspectiveRect(var1, var2, var3, var4, this);
   }

   public Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      return (this.properties & 4) != 0 ? var8.setPerspectiveOffCenter(var1, var2, var3, var4, var5, var6, var7) : this.perspectiveOffCenterGeneric(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private Matrix4f perspectiveOffCenterGeneric(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      float var9 = Math.tan(var1 * 0.5F);
      float var10 = 1.0F / (var9 * var4);
      float var11 = 1.0F / var9;
      float var12 = Math.tan(var2);
      float var13 = Math.tan(var3);
      float var14 = var12 * var10;
      float var15 = var13 * var11;
      boolean var18 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var19 = var5 > 0.0F && Float.isInfinite(var5);
      float var16;
      float var17;
      float var20;
      if (var18) {
         var20 = 1.0E-6F;
         var16 = var20 - 1.0F;
         var17 = (var20 - (var7 ? 1.0F : 2.0F)) * var5;
      } else if (var19) {
         var20 = 1.0E-6F;
         var16 = (var7 ? 0.0F : 1.0F) - var20;
         var17 = ((var7 ? 1.0F : 2.0F) - var20) * var6;
      } else {
         var16 = (var7 ? var6 : var6 + var5) / (var5 - var6);
         var17 = (var7 ? var6 : var6 + var6) * var5 / (var5 - var6);
      }

      var20 = this.m00 * var14 + this.m10 * var15 + this.m20 * var16 - this.m30;
      float var21 = this.m01 * var14 + this.m11 * var15 + this.m21 * var16 - this.m31;
      float var22 = this.m02 * var14 + this.m12 * var15 + this.m22 * var16 - this.m32;
      float var23 = this.m03 * var14 + this.m13 * var15 + this.m23 * var16 - this.m33;
      var8._m00(this.m00 * var10)._m01(this.m01 * var10)._m02(this.m02 * var10)._m03(this.m03 * var10)._m10(this.m10 * var11)._m11(this.m11 * var11)._m12(this.m12 * var11)._m13(this.m13 * var11)._m30(this.m20 * var17)._m31(this.m21 * var17)._m32(this.m22 * var17)._m33(this.m23 * var17)._m20(var20)._m21(var21)._m22(var22)._m23(var23)._properties(this.properties & ~(30 | (var14 == 0.0F && var15 == 0.0F ? 0 : 1)));
      return var8;
   }

   public Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return this.perspectiveOffCenter(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.perspectiveOffCenter(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.perspectiveOffCenter(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f setPerspective(float var1, float var2, float var3, float var4, boolean var5) {
      MemUtil.INSTANCE.zero(this);
      float var6 = Math.tan(var1 * 0.5F);
      this._m00(1.0F / (var6 * var2))._m11(1.0F / var6);
      boolean var7 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var8 = var3 > 0.0F && Float.isInfinite(var3);
      float var9;
      if (var7) {
         var9 = 1.0E-6F;
         this._m22(var9 - 1.0F)._m32((var9 - (var5 ? 1.0F : 2.0F)) * var3);
      } else if (var8) {
         var9 = 1.0E-6F;
         this._m22((var5 ? 0.0F : 1.0F) - var9)._m32(((var5 ? 1.0F : 2.0F) - var9) * var4);
      } else {
         this._m22((var5 ? var4 : var4 + var3) / (var3 - var4))._m32((var5 ? var4 : var4 + var4) * var3 / (var3 - var4));
      }

      return this._m23(-1.0F)._properties(1);
   }

   public Matrix4f setPerspective(float var1, float var2, float var3, float var4) {
      return this.setPerspective(var1, var2, var3, var4, false);
   }

   public Matrix4f setPerspectiveRect(float var1, float var2, float var3, float var4, boolean var5) {
      MemUtil.INSTANCE.zero(this);
      this._m00((var3 + var3) / var1)._m11((var3 + var3) / var2);
      boolean var6 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var7 = var3 > 0.0F && Float.isInfinite(var3);
      float var8;
      if (var6) {
         var8 = 1.0E-6F;
         this._m22(var8 - 1.0F)._m32((var8 - (var5 ? 1.0F : 2.0F)) * var3);
      } else if (var7) {
         var8 = 1.0E-6F;
         this._m22((var5 ? 0.0F : 1.0F) - var8)._m32(((var5 ? 1.0F : 2.0F) - var8) * var4);
      } else {
         this._m22((var5 ? var4 : var4 + var3) / (var3 - var4))._m32((var5 ? var4 : var4 + var4) * var3 / (var3 - var4));
      }

      this._m23(-1.0F)._properties(1);
      return this;
   }

   public Matrix4f setPerspectiveRect(float var1, float var2, float var3, float var4) {
      return this.setPerspectiveRect(var1, var2, var3, var4, false);
   }

   public Matrix4f setPerspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setPerspectiveOffCenter(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f setPerspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      MemUtil.INSTANCE.zero(this);
      float var8 = Math.tan(var1 * 0.5F);
      float var9 = 1.0F / (var8 * var4);
      float var10 = 1.0F / var8;
      float var11 = Math.tan(var2);
      float var12 = Math.tan(var3);
      this._m00(var9)._m11(var10)._m20(var11 * var9)._m21(var12 * var10);
      boolean var13 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var14 = var5 > 0.0F && Float.isInfinite(var5);
      float var15;
      if (var13) {
         var15 = 1.0E-6F;
         this._m22(var15 - 1.0F)._m32((var15 - (var7 ? 1.0F : 2.0F)) * var5);
      } else if (var14) {
         var15 = 1.0E-6F;
         this._m22((var7 ? 0.0F : 1.0F) - var15)._m32(((var7 ? 1.0F : 2.0F) - var15) * var6);
      } else {
         this._m22((var7 ? var6 : var6 + var5) / (var5 - var6))._m32((var7 ? var6 : var6 + var6) * var5 / (var5 - var6));
      }

      this._m23(-1.0F)._properties(var2 == 0.0F && var3 == 0.0F ? 1 : 0);
      return this;
   }

   public Matrix4f perspectiveLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      return (this.properties & 4) != 0 ? var6.setPerspectiveLH(var1, var2, var3, var4, var5) : this.perspectiveLHGeneric(var1, var2, var3, var4, var5, var6);
   }

   private Matrix4f perspectiveLHGeneric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6) {
      float var7 = Math.tan(var1 * 0.5F);
      float var8 = 1.0F / (var7 * var2);
      float var9 = 1.0F / var7;
      boolean var12 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var13 = var3 > 0.0F && Float.isInfinite(var3);
      float var10;
      float var11;
      float var14;
      if (var12) {
         var14 = 1.0E-6F;
         var10 = 1.0F - var14;
         var11 = (var14 - (var5 ? 1.0F : 2.0F)) * var3;
      } else if (var13) {
         var14 = 1.0E-6F;
         var10 = (var5 ? 0.0F : 1.0F) - var14;
         var11 = ((var5 ? 1.0F : 2.0F) - var14) * var4;
      } else {
         var10 = (var5 ? var4 : var4 + var3) / (var4 - var3);
         var11 = (var5 ? var4 : var4 + var4) * var3 / (var3 - var4);
      }

      var14 = this.m20 * var10 + this.m30;
      float var15 = this.m21 * var10 + this.m31;
      float var16 = this.m22 * var10 + this.m32;
      float var17 = this.m23 * var10 + this.m33;
      var6._m00(this.m00 * var8)._m01(this.m01 * var8)._m02(this.m02 * var8)._m03(this.m03 * var8)._m10(this.m10 * var9)._m11(this.m11 * var9)._m12(this.m12 * var9)._m13(this.m13 * var9)._m30(this.m20 * var11)._m31(this.m21 * var11)._m32(this.m22 * var11)._m33(this.m23 * var11)._m20(var14)._m21(var15)._m22(var16)._m23(var17)._properties(this.properties & -31);
      return var6;
   }

   public Matrix4f perspectiveLH(float var1, float var2, float var3, float var4, boolean var5) {
      return this.perspectiveLH(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f perspectiveLH(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.perspectiveLH(var1, var2, var3, var4, false, var5);
   }

   public Matrix4f perspectiveLH(float var1, float var2, float var3, float var4) {
      return this.perspectiveLH(var1, var2, var3, var4, this);
   }

   public Matrix4f setPerspectiveLH(float var1, float var2, float var3, float var4, boolean var5) {
      MemUtil.INSTANCE.zero(this);
      float var6 = Math.tan(var1 * 0.5F);
      this._m00(1.0F / (var6 * var2))._m11(1.0F / var6);
      boolean var7 = var4 > 0.0F && Float.isInfinite(var4);
      boolean var8 = var3 > 0.0F && Float.isInfinite(var3);
      float var9;
      if (var7) {
         var9 = 1.0E-6F;
         this._m22(1.0F - var9)._m32((var9 - (var5 ? 1.0F : 2.0F)) * var3);
      } else if (var8) {
         var9 = 1.0E-6F;
         this._m22((var5 ? 0.0F : 1.0F) - var9)._m32(((var5 ? 1.0F : 2.0F) - var9) * var4);
      } else {
         this._m22((var5 ? var4 : var4 + var3) / (var4 - var3))._m32((var5 ? var4 : var4 + var4) * var3 / (var3 - var4));
      }

      this._m23(1.0F)._properties(1);
      return this;
   }

   public Matrix4f setPerspectiveLH(float var1, float var2, float var3, float var4) {
      return this.setPerspectiveLH(var1, var2, var3, var4, false);
   }

   public Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      return (this.properties & 4) != 0 ? var8.setFrustum(var1, var2, var3, var4, var5, var6, var7) : this.frustumGeneric(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private Matrix4f frustumGeneric(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      float var9 = (var5 + var5) / (var2 - var1);
      float var10 = (var5 + var5) / (var4 - var3);
      float var11 = (var2 + var1) / (var2 - var1);
      float var12 = (var4 + var3) / (var4 - var3);
      boolean var15 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var16 = var5 > 0.0F && Float.isInfinite(var5);
      float var13;
      float var14;
      float var17;
      if (var15) {
         var17 = 1.0E-6F;
         var13 = var17 - 1.0F;
         var14 = (var17 - (var7 ? 1.0F : 2.0F)) * var5;
      } else if (var16) {
         var17 = 1.0E-6F;
         var13 = (var7 ? 0.0F : 1.0F) - var17;
         var14 = ((var7 ? 1.0F : 2.0F) - var17) * var6;
      } else {
         var13 = (var7 ? var6 : var6 + var5) / (var5 - var6);
         var14 = (var7 ? var6 : var6 + var6) * var5 / (var5 - var6);
      }

      var17 = this.m00 * var11 + this.m10 * var12 + this.m20 * var13 - this.m30;
      float var18 = this.m01 * var11 + this.m11 * var12 + this.m21 * var13 - this.m31;
      float var19 = this.m02 * var11 + this.m12 * var12 + this.m22 * var13 - this.m32;
      float var20 = this.m03 * var11 + this.m13 * var12 + this.m23 * var13 - this.m33;
      var8._m00(this.m00 * var9)._m01(this.m01 * var9)._m02(this.m02 * var9)._m03(this.m03 * var9)._m10(this.m10 * var10)._m11(this.m11 * var10)._m12(this.m12 * var10)._m13(this.m13 * var10)._m30(this.m20 * var14)._m31(this.m21 * var14)._m32(this.m22 * var14)._m33(this.m23 * var14)._m20(var17)._m21(var18)._m22(var19)._m23(var20)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(0);
      return var8;
   }

   public Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return this.frustum(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.frustum(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.frustum(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f setFrustum(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00((var5 + var5) / (var2 - var1))._m11((var5 + var5) / (var4 - var3))._m20((var2 + var1) / (var2 - var1))._m21((var4 + var3) / (var4 - var3));
      boolean var8 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var9 = var5 > 0.0F && Float.isInfinite(var5);
      float var10;
      if (var8) {
         var10 = 1.0E-6F;
         this._m22(var10 - 1.0F)._m32((var10 - (var7 ? 1.0F : 2.0F)) * var5);
      } else if (var9) {
         var10 = 1.0E-6F;
         this._m22((var7 ? 0.0F : 1.0F) - var10)._m32(((var7 ? 1.0F : 2.0F) - var10) * var6);
      } else {
         this._m22((var7 ? var6 : var6 + var5) / (var5 - var6))._m32((var7 ? var6 : var6 + var6) * var5 / (var5 - var6));
      }

      this._m23(-1.0F)._m33(0.0F)._properties(this.m20 == 0.0F && this.m21 == 0.0F ? 1 : 0);
      return this;
   }

   public Matrix4f setFrustum(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setFrustum(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      return (this.properties & 4) != 0 ? var8.setFrustumLH(var1, var2, var3, var4, var5, var6, var7) : this.frustumLHGeneric(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private Matrix4f frustumLHGeneric(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8) {
      float var9 = (var5 + var5) / (var2 - var1);
      float var10 = (var5 + var5) / (var4 - var3);
      float var11 = (var2 + var1) / (var2 - var1);
      float var12 = (var4 + var3) / (var4 - var3);
      boolean var15 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var16 = var5 > 0.0F && Float.isInfinite(var5);
      float var13;
      float var14;
      float var17;
      if (var15) {
         var17 = 1.0E-6F;
         var13 = 1.0F - var17;
         var14 = (var17 - (var7 ? 1.0F : 2.0F)) * var5;
      } else if (var16) {
         var17 = 1.0E-6F;
         var13 = (var7 ? 0.0F : 1.0F) - var17;
         var14 = ((var7 ? 1.0F : 2.0F) - var17) * var6;
      } else {
         var13 = (var7 ? var6 : var6 + var5) / (var6 - var5);
         var14 = (var7 ? var6 : var6 + var6) * var5 / (var5 - var6);
      }

      var17 = this.m00 * var11 + this.m10 * var12 + this.m20 * var13 + this.m30;
      float var18 = this.m01 * var11 + this.m11 * var12 + this.m21 * var13 + this.m31;
      float var19 = this.m02 * var11 + this.m12 * var12 + this.m22 * var13 + this.m32;
      float var20 = this.m03 * var11 + this.m13 * var12 + this.m23 * var13 + this.m33;
      var8._m00(this.m00 * var9)._m01(this.m01 * var9)._m02(this.m02 * var9)._m03(this.m03 * var9)._m10(this.m10 * var10)._m11(this.m11 * var10)._m12(this.m12 * var10)._m13(this.m13 * var10)._m30(this.m20 * var14)._m31(this.m21 * var14)._m32(this.m22 * var14)._m33(this.m23 * var14)._m20(var17)._m21(var18)._m22(var19)._m23(var20)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(0);
      return var8;
   }

   public Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      return this.frustumLH(var1, var2, var3, var4, var5, var6, var7, this);
   }

   public Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      return this.frustumLH(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.frustumLH(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f setFrustumLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00((var5 + var5) / (var2 - var1))._m11((var5 + var5) / (var4 - var3))._m20((var2 + var1) / (var2 - var1))._m21((var4 + var3) / (var4 - var3));
      boolean var8 = var6 > 0.0F && Float.isInfinite(var6);
      boolean var9 = var5 > 0.0F && Float.isInfinite(var5);
      float var10;
      if (var8) {
         var10 = 1.0E-6F;
         this._m22(1.0F - var10)._m32((var10 - (var7 ? 1.0F : 2.0F)) * var5);
      } else if (var9) {
         var10 = 1.0E-6F;
         this._m22((var7 ? 0.0F : 1.0F) - var10)._m32(((var7 ? 1.0F : 2.0F) - var10) * var6);
      } else {
         this._m22((var7 ? var6 : var6 + var5) / (var6 - var5))._m32((var7 ? var6 : var6 + var6) * var5 / (var5 - var6));
      }

      return this._m23(1.0F)._m33(0.0F)._properties(0);
   }

   public Matrix4f setFrustumLH(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.setFrustumLH(var1, var2, var3, var4, var5, var6, false);
   }

   public Matrix4f setFromIntrinsic(float var1, float var2, float var3, float var4, float var5, int var6, int var7, float var8, float var9) {
      float var10 = 2.0F / (float)var6;
      float var11 = 2.0F / (float)var7;
      float var12 = 2.0F / (var8 - var9);
      this.m00 = var10 * var1;
      this.m01 = 0.0F;
      this.m02 = 0.0F;
      this.m03 = 0.0F;
      this.m10 = var10 * var3;
      this.m11 = var11 * var2;
      this.m12 = 0.0F;
      this.m13 = 0.0F;
      this.m20 = var10 * var4 - 1.0F;
      this.m21 = var11 * var5 - 1.0F;
      this.m22 = var12 * -(var8 + var9) + (var9 + var8) / (var8 - var9);
      this.m23 = -1.0F;
      this.m30 = 0.0F;
      this.m31 = 0.0F;
      this.m32 = var12 * -var8 * var9;
      this.m33 = 0.0F;
      this.properties = 1;
      return this;
   }

   public Matrix4f rotate(Quaternionfc var1, Matrix4f var2) {
      if ((this.properties & 4) != 0) {
         return var2.rotation(var1);
      } else if ((this.properties & 8) != 0) {
         return this.rotateTranslation(var1, var2);
      } else {
         return (this.properties & 2) != 0 ? this.rotateAffine(var1, var2) : this.rotateGeneric(var1, var2);
      }
   }

   private Matrix4f rotateGeneric(Quaternionfc var1, Matrix4f var2) {
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
      float var22 = -var8 + var10;
      float var23 = var5 - var6 + var3 - var4;
      float var24 = var16 + var18;
      float var25 = var14 + var12;
      float var26 = var16 - var18;
      float var27 = var6 - var5 - var4 + var3;
      float var28 = this.m00 * var19 + this.m10 * var20 + this.m20 * var21;
      float var29 = this.m01 * var19 + this.m11 * var20 + this.m21 * var21;
      float var30 = this.m02 * var19 + this.m12 * var20 + this.m22 * var21;
      float var31 = this.m03 * var19 + this.m13 * var20 + this.m23 * var21;
      float var32 = this.m00 * var22 + this.m10 * var23 + this.m20 * var24;
      float var33 = this.m01 * var22 + this.m11 * var23 + this.m21 * var24;
      float var34 = this.m02 * var22 + this.m12 * var23 + this.m22 * var24;
      float var35 = this.m03 * var22 + this.m13 * var23 + this.m23 * var24;
      return var2._m20(this.m00 * var25 + this.m10 * var26 + this.m20 * var27)._m21(this.m01 * var25 + this.m11 * var26 + this.m21 * var27)._m22(this.m02 * var25 + this.m12 * var26 + this.m22 * var27)._m23(this.m03 * var25 + this.m13 * var26 + this.m23 * var27)._m00(var28)._m01(var29)._m02(var30)._m03(var31)._m10(var32)._m11(var33)._m12(var34)._m13(var35)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotate(Quaternionfc var1) {
      return this.rotate(var1, this);
   }

   public Matrix4f rotateAffine(Quaternionfc var1, Matrix4f var2) {
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
      float var22 = -var8 + var10;
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
      return var2._m20(this.m00 * var25 + this.m10 * var26 + this.m20 * var27)._m21(this.m01 * var25 + this.m11 * var26 + this.m21 * var27)._m22(this.m02 * var25 + this.m12 * var26 + this.m22 * var27)._m23(0.0F)._m00(var28)._m01(var29)._m02(var30)._m03(0.0F)._m10(var31)._m11(var32)._m12(var33)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateAffine(Quaternionfc var1) {
      return this.rotateAffine(var1, this);
   }

   public Matrix4f rotateTranslation(Quaternionfc var1, Matrix4f var2) {
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
      float var22 = -var8 + var10;
      float var23 = var5 - var6 + var3 - var4;
      float var24 = var16 + var18;
      float var25 = var14 + var12;
      float var26 = var16 - var18;
      float var27 = var6 - var5 - var4 + var3;
      return var2._m20(var25)._m21(var26)._m22(var27)._m23(0.0F)._m00(var19)._m01(var20)._m02(var21)._m03(0.0F)._m10(var22)._m11(var23)._m12(var24)._m13(0.0F)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateAround(Quaternionfc var1, float var2, float var3, float var4) {
      return this.rotateAround(var1, var2, var3, var4, this);
   }

   public Matrix4f rotateAroundAffine(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5) {
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
      float var25 = -var11 + var13;
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
      var5._m20(this.m00 * var28 + this.m10 * var29 + this.m20 * var30)._m21(this.m01 * var28 + this.m11 * var29 + this.m21 * var30)._m22(this.m02 * var28 + this.m12 * var29 + this.m22 * var30)._m23(0.0F)._m00(var34)._m01(var35)._m02(var36)._m03(0.0F)._m10(var37)._m11(var38)._m12(var39)._m13(0.0F)._m30(-var34 * var2 - var37 * var3 - this.m20 * var4 + var31)._m31(-var35 * var2 - var38 * var3 - this.m21 * var4 + var32)._m32(-var36 * var2 - var39 * var3 - this.m22 * var4 + var33)._m33(1.0F)._properties(this.properties & -14);
      return var5;
   }

   public Matrix4f rotateAround(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5) {
      if ((this.properties & 4) != 0) {
         return this.rotationAround(var1, var2, var3, var4);
      } else {
         return (this.properties & 2) != 0 ? this.rotateAroundAffine(var1, var2, var3, var4, var5) : this.rotateAroundGeneric(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f rotateAroundGeneric(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5) {
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
      float var25 = -var11 + var13;
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
      float var37 = this.m03 * var22 + this.m13 * var23 + this.m23 * var24;
      float var38 = this.m00 * var25 + this.m10 * var26 + this.m20 * var27;
      float var39 = this.m01 * var25 + this.m11 * var26 + this.m21 * var27;
      float var40 = this.m02 * var25 + this.m12 * var26 + this.m22 * var27;
      float var41 = this.m03 * var25 + this.m13 * var26 + this.m23 * var27;
      var5._m20(this.m00 * var28 + this.m10 * var29 + this.m20 * var30)._m21(this.m01 * var28 + this.m11 * var29 + this.m21 * var30)._m22(this.m02 * var28 + this.m12 * var29 + this.m22 * var30)._m23(this.m03 * var28 + this.m13 * var29 + this.m23 * var30)._m00(var34)._m01(var35)._m02(var36)._m03(var37)._m10(var38)._m11(var39)._m12(var40)._m13(var41)._m30(-var34 * var2 - var38 * var3 - this.m20 * var4 + var31)._m31(-var35 * var2 - var39 * var3 - this.m21 * var4 + var32)._m32(-var36 * var2 - var40 * var3 - this.m22 * var4 + var33)._m33(this.m33)._properties(this.properties & -14);
      return var5;
   }

   public Matrix4f rotationAround(Quaternionfc var1, float var2, float var3, float var4) {
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
      this._m20(var16 + var14)._m21(var18 - var20)._m22(var8 - var7 - var6 + var5)._m23(0.0F)._m00(var5 + var6 - var8 - var7)._m01(var12 + var10)._m02(var14 - var16)._m03(0.0F)._m10(-var10 + var12)._m11(var7 - var8 + var5 - var6)._m12(var18 + var20)._m13(0.0F)._m30(-this.m00 * var2 - this.m10 * var3 - this.m20 * var4 + var2)._m31(-this.m01 * var2 - this.m11 * var3 - this.m21 * var4 + var3)._m32(-this.m02 * var2 - this.m12 * var3 - this.m22 * var4 + var4)._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f rotateLocal(Quaternionfc var1, Matrix4f var2) {
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
      float var22 = -var8 + var10;
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
      float var37 = var19 * this.m30 + var22 * this.m31 + var25 * this.m32;
      float var38 = var20 * this.m30 + var23 * this.m31 + var26 * this.m32;
      float var39 = var21 * this.m30 + var24 * this.m31 + var27 * this.m32;
      return var2._m00(var28)._m01(var29)._m02(var30)._m03(this.m03)._m10(var31)._m11(var32)._m12(var33)._m13(this.m13)._m20(var34)._m21(var35)._m22(var36)._m23(this.m23)._m30(var37)._m31(var38)._m32(var39)._m33(this.m33)._properties(this.properties & -14);
   }

   public Matrix4f rotateLocal(Quaternionfc var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix4f rotateAroundLocal(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5) {
      float var6 = var1.w() * var1.w();
      float var7 = var1.x() * var1.x();
      float var8 = var1.y() * var1.y();
      float var9 = var1.z() * var1.z();
      float var10 = var1.z() * var1.w();
      float var11 = var1.x() * var1.y();
      float var12 = var1.x() * var1.z();
      float var13 = var1.y() * var1.w();
      float var14 = var1.y() * var1.z();
      float var15 = var1.x() * var1.w();
      float var16 = var6 + var7 - var9 - var8;
      float var17 = var11 + var10 + var10 + var11;
      float var18 = var12 - var13 + var12 - var13;
      float var19 = -var10 + var11 - var10 + var11;
      float var20 = var8 - var9 + var6 - var7;
      float var21 = var14 + var14 + var15 + var15;
      float var22 = var13 + var12 + var12 + var13;
      float var23 = var14 + var14 - var15 - var15;
      float var24 = var9 - var8 - var7 + var6;
      float var25 = this.m00 - var2 * this.m03;
      float var26 = this.m01 - var3 * this.m03;
      float var27 = this.m02 - var4 * this.m03;
      float var28 = this.m10 - var2 * this.m13;
      float var29 = this.m11 - var3 * this.m13;
      float var30 = this.m12 - var4 * this.m13;
      float var31 = this.m20 - var2 * this.m23;
      float var32 = this.m21 - var3 * this.m23;
      float var33 = this.m22 - var4 * this.m23;
      float var34 = this.m30 - var2 * this.m33;
      float var35 = this.m31 - var3 * this.m33;
      float var36 = this.m32 - var4 * this.m33;
      var5._m00(var16 * var25 + var19 * var26 + var22 * var27 + var2 * this.m03)._m01(var17 * var25 + var20 * var26 + var23 * var27 + var3 * this.m03)._m02(var18 * var25 + var21 * var26 + var24 * var27 + var4 * this.m03)._m03(this.m03)._m10(var16 * var28 + var19 * var29 + var22 * var30 + var2 * this.m13)._m11(var17 * var28 + var20 * var29 + var23 * var30 + var3 * this.m13)._m12(var18 * var28 + var21 * var29 + var24 * var30 + var4 * this.m13)._m13(this.m13)._m20(var16 * var31 + var19 * var32 + var22 * var33 + var2 * this.m23)._m21(var17 * var31 + var20 * var32 + var23 * var33 + var3 * this.m23)._m22(var18 * var31 + var21 * var32 + var24 * var33 + var4 * this.m23)._m23(this.m23)._m30(var16 * var34 + var19 * var35 + var22 * var36 + var2 * this.m33)._m31(var17 * var34 + var20 * var35 + var23 * var36 + var3 * this.m33)._m32(var18 * var34 + var21 * var35 + var24 * var36 + var4 * this.m33)._m33(this.m33)._properties(this.properties & -14);
      return var5;
   }

   public Matrix4f rotateAroundLocal(Quaternionfc var1, float var2, float var3, float var4) {
      return this.rotateAroundLocal(var1, var2, var3, var4, this);
   }

   public Matrix4f rotate(AxisAngle4f var1) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z);
   }

   public Matrix4f rotate(AxisAngle4f var1, Matrix4f var2) {
      return this.rotate(var1.angle, var1.x, var1.y, var1.z, var2);
   }

   public Matrix4f rotate(float var1, Vector3fc var2) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z());
   }

   public Matrix4f rotate(float var1, Vector3fc var2, Matrix4f var3) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z(), var3);
   }

   public Vector4f unproject(float var1, float var2, float var3, int[] var4, Vector4f var5) {
      float var6 = this.m00 * this.m11 - this.m01 * this.m10;
      float var7 = this.m00 * this.m12 - this.m02 * this.m10;
      float var8 = this.m00 * this.m13 - this.m03 * this.m10;
      float var9 = this.m01 * this.m12 - this.m02 * this.m11;
      float var10 = this.m01 * this.m13 - this.m03 * this.m11;
      float var11 = this.m02 * this.m13 - this.m03 * this.m12;
      float var12 = this.m20 * this.m31 - this.m21 * this.m30;
      float var13 = this.m20 * this.m32 - this.m22 * this.m30;
      float var14 = this.m20 * this.m33 - this.m23 * this.m30;
      float var15 = this.m21 * this.m32 - this.m22 * this.m31;
      float var16 = this.m21 * this.m33 - this.m23 * this.m31;
      float var17 = this.m22 * this.m33 - this.m23 * this.m32;
      float var18 = var6 * var17 - var7 * var16 + var8 * var15 + var9 * var14 - var10 * var13 + var11 * var12;
      var18 = 1.0F / var18;
      float var19 = (this.m11 * var17 - this.m12 * var16 + this.m13 * var15) * var18;
      float var20 = (-this.m01 * var17 + this.m02 * var16 - this.m03 * var15) * var18;
      float var21 = (this.m31 * var11 - this.m32 * var10 + this.m33 * var9) * var18;
      float var22 = (-this.m21 * var11 + this.m22 * var10 - this.m23 * var9) * var18;
      float var23 = (-this.m10 * var17 + this.m12 * var14 - this.m13 * var13) * var18;
      float var24 = (this.m00 * var17 - this.m02 * var14 + this.m03 * var13) * var18;
      float var25 = (-this.m30 * var11 + this.m32 * var8 - this.m33 * var7) * var18;
      float var26 = (this.m20 * var11 - this.m22 * var8 + this.m23 * var7) * var18;
      float var27 = (this.m10 * var16 - this.m11 * var14 + this.m13 * var12) * var18;
      float var28 = (-this.m00 * var16 + this.m01 * var14 - this.m03 * var12) * var18;
      float var29 = (this.m30 * var10 - this.m31 * var8 + this.m33 * var6) * var18;
      float var30 = (-this.m20 * var10 + this.m21 * var8 - this.m23 * var6) * var18;
      float var31 = (-this.m10 * var15 + this.m11 * var13 - this.m12 * var12) * var18;
      float var32 = (this.m00 * var15 - this.m01 * var13 + this.m02 * var12) * var18;
      float var33 = (-this.m30 * var9 + this.m31 * var7 - this.m32 * var6) * var18;
      float var34 = (this.m20 * var9 - this.m21 * var7 + this.m22 * var6) * var18;
      float var35 = (var1 - (float)var4[0]) / (float)var4[2] * 2.0F - 1.0F;
      float var36 = (var2 - (float)var4[1]) / (float)var4[3] * 2.0F - 1.0F;
      float var37 = var3 + var3 - 1.0F;
      float var38 = 1.0F / (var22 * var35 + var26 * var36 + var30 * var37 + var34);
      return var5.set((var19 * var35 + var23 * var36 + var27 * var37 + var31) * var38, (var20 * var35 + var24 * var36 + var28 * var37 + var32) * var38, (var21 * var35 + var25 * var36 + var29 * var37 + var33) * var38, 1.0F);
   }

   public Vector3f unproject(float var1, float var2, float var3, int[] var4, Vector3f var5) {
      float var6 = this.m00 * this.m11 - this.m01 * this.m10;
      float var7 = this.m00 * this.m12 - this.m02 * this.m10;
      float var8 = this.m00 * this.m13 - this.m03 * this.m10;
      float var9 = this.m01 * this.m12 - this.m02 * this.m11;
      float var10 = this.m01 * this.m13 - this.m03 * this.m11;
      float var11 = this.m02 * this.m13 - this.m03 * this.m12;
      float var12 = this.m20 * this.m31 - this.m21 * this.m30;
      float var13 = this.m20 * this.m32 - this.m22 * this.m30;
      float var14 = this.m20 * this.m33 - this.m23 * this.m30;
      float var15 = this.m21 * this.m32 - this.m22 * this.m31;
      float var16 = this.m21 * this.m33 - this.m23 * this.m31;
      float var17 = this.m22 * this.m33 - this.m23 * this.m32;
      float var18 = var6 * var17 - var7 * var16 + var8 * var15 + var9 * var14 - var10 * var13 + var11 * var12;
      var18 = 1.0F / var18;
      float var19 = (this.m11 * var17 - this.m12 * var16 + this.m13 * var15) * var18;
      float var20 = (-this.m01 * var17 + this.m02 * var16 - this.m03 * var15) * var18;
      float var21 = (this.m31 * var11 - this.m32 * var10 + this.m33 * var9) * var18;
      float var22 = (-this.m21 * var11 + this.m22 * var10 - this.m23 * var9) * var18;
      float var23 = (-this.m10 * var17 + this.m12 * var14 - this.m13 * var13) * var18;
      float var24 = (this.m00 * var17 - this.m02 * var14 + this.m03 * var13) * var18;
      float var25 = (-this.m30 * var11 + this.m32 * var8 - this.m33 * var7) * var18;
      float var26 = (this.m20 * var11 - this.m22 * var8 + this.m23 * var7) * var18;
      float var27 = (this.m10 * var16 - this.m11 * var14 + this.m13 * var12) * var18;
      float var28 = (-this.m00 * var16 + this.m01 * var14 - this.m03 * var12) * var18;
      float var29 = (this.m30 * var10 - this.m31 * var8 + this.m33 * var6) * var18;
      float var30 = (-this.m20 * var10 + this.m21 * var8 - this.m23 * var6) * var18;
      float var31 = (-this.m10 * var15 + this.m11 * var13 - this.m12 * var12) * var18;
      float var32 = (this.m00 * var15 - this.m01 * var13 + this.m02 * var12) * var18;
      float var33 = (-this.m30 * var9 + this.m31 * var7 - this.m32 * var6) * var18;
      float var34 = (this.m20 * var9 - this.m21 * var7 + this.m22 * var6) * var18;
      float var35 = (var1 - (float)var4[0]) / (float)var4[2] * 2.0F - 1.0F;
      float var36 = (var2 - (float)var4[1]) / (float)var4[3] * 2.0F - 1.0F;
      float var37 = var3 + var3 - 1.0F;
      float var38 = 1.0F / (var22 * var35 + var26 * var36 + var30 * var37 + var34);
      return var5.set((var19 * var35 + var23 * var36 + var27 * var37 + var31) * var38, (var20 * var35 + var24 * var36 + var28 * var37 + var32) * var38, (var21 * var35 + var25 * var36 + var29 * var37 + var33) * var38);
   }

   public Vector4f unproject(Vector3fc var1, int[] var2, Vector4f var3) {
      return this.unproject(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Vector3f unproject(Vector3fc var1, int[] var2, Vector3f var3) {
      return this.unproject(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Matrix4f unprojectRay(float var1, float var2, int[] var3, Vector3f var4, Vector3f var5) {
      float var6 = this.m00 * this.m11 - this.m01 * this.m10;
      float var7 = this.m00 * this.m12 - this.m02 * this.m10;
      float var8 = this.m00 * this.m13 - this.m03 * this.m10;
      float var9 = this.m01 * this.m12 - this.m02 * this.m11;
      float var10 = this.m01 * this.m13 - this.m03 * this.m11;
      float var11 = this.m02 * this.m13 - this.m03 * this.m12;
      float var12 = this.m20 * this.m31 - this.m21 * this.m30;
      float var13 = this.m20 * this.m32 - this.m22 * this.m30;
      float var14 = this.m20 * this.m33 - this.m23 * this.m30;
      float var15 = this.m21 * this.m32 - this.m22 * this.m31;
      float var16 = this.m21 * this.m33 - this.m23 * this.m31;
      float var17 = this.m22 * this.m33 - this.m23 * this.m32;
      float var18 = var6 * var17 - var7 * var16 + var8 * var15 + var9 * var14 - var10 * var13 + var11 * var12;
      var18 = 1.0F / var18;
      float var19 = (this.m11 * var17 - this.m12 * var16 + this.m13 * var15) * var18;
      float var20 = (-this.m01 * var17 + this.m02 * var16 - this.m03 * var15) * var18;
      float var21 = (this.m31 * var11 - this.m32 * var10 + this.m33 * var9) * var18;
      float var22 = (-this.m21 * var11 + this.m22 * var10 - this.m23 * var9) * var18;
      float var23 = (-this.m10 * var17 + this.m12 * var14 - this.m13 * var13) * var18;
      float var24 = (this.m00 * var17 - this.m02 * var14 + this.m03 * var13) * var18;
      float var25 = (-this.m30 * var11 + this.m32 * var8 - this.m33 * var7) * var18;
      float var26 = (this.m20 * var11 - this.m22 * var8 + this.m23 * var7) * var18;
      float var27 = (this.m10 * var16 - this.m11 * var14 + this.m13 * var12) * var18;
      float var28 = (-this.m00 * var16 + this.m01 * var14 - this.m03 * var12) * var18;
      float var29 = (this.m30 * var10 - this.m31 * var8 + this.m33 * var6) * var18;
      float var30 = (-this.m20 * var10 + this.m21 * var8 - this.m23 * var6) * var18;
      float var31 = (-this.m10 * var15 + this.m11 * var13 - this.m12 * var12) * var18;
      float var32 = (this.m00 * var15 - this.m01 * var13 + this.m02 * var12) * var18;
      float var33 = (-this.m30 * var9 + this.m31 * var7 - this.m32 * var6) * var18;
      float var34 = (this.m20 * var9 - this.m21 * var7 + this.m22 * var6) * var18;
      float var35 = (var1 - (float)var3[0]) / (float)var3[2] * 2.0F - 1.0F;
      float var36 = (var2 - (float)var3[1]) / (float)var3[3] * 2.0F - 1.0F;
      float var37 = var19 * var35 + var23 * var36 + var31;
      float var38 = var20 * var35 + var24 * var36 + var32;
      float var39 = var21 * var35 + var25 * var36 + var33;
      float var40 = 1.0F / (var22 * var35 + var26 * var36 - var30 + var34);
      float var41 = (var37 - var27) * var40;
      float var42 = (var38 - var28) * var40;
      float var43 = (var39 - var29) * var40;
      float var44 = 1.0F / (var22 * var35 + var26 * var36 + var34);
      float var45 = var37 * var44;
      float var46 = var38 * var44;
      float var47 = var39 * var44;
      var4.x = var41;
      var4.y = var42;
      var4.z = var43;
      var5.x = var45 - var41;
      var5.y = var46 - var42;
      var5.z = var47 - var43;
      return this;
   }

   public Matrix4f unprojectRay(Vector2fc var1, int[] var2, Vector3f var3, Vector3f var4) {
      return this.unprojectRay(var1.x(), var1.y(), var2, var3, var4);
   }

   public Vector4f unprojectInv(Vector3fc var1, int[] var2, Vector4f var3) {
      return this.unprojectInv(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Vector4f unprojectInv(float var1, float var2, float var3, int[] var4, Vector4f var5) {
      float var6 = (var1 - (float)var4[0]) / (float)var4[2] * 2.0F - 1.0F;
      float var7 = (var2 - (float)var4[1]) / (float)var4[3] * 2.0F - 1.0F;
      float var8 = var3 + var3 - 1.0F;
      float var9 = 1.0F / (this.m03 * var6 + this.m13 * var7 + this.m23 * var8 + this.m33);
      return var5.set((this.m00 * var6 + this.m10 * var7 + this.m20 * var8 + this.m30) * var9, (this.m01 * var6 + this.m11 * var7 + this.m21 * var8 + this.m31) * var9, (this.m02 * var6 + this.m12 * var7 + this.m22 * var8 + this.m32) * var9, 1.0F);
   }

   public Matrix4f unprojectInvRay(Vector2fc var1, int[] var2, Vector3f var3, Vector3f var4) {
      return this.unprojectInvRay(var1.x(), var1.y(), var2, var3, var4);
   }

   public Matrix4f unprojectInvRay(float var1, float var2, int[] var3, Vector3f var4, Vector3f var5) {
      float var6 = (var1 - (float)var3[0]) / (float)var3[2] * 2.0F - 1.0F;
      float var7 = (var2 - (float)var3[1]) / (float)var3[3] * 2.0F - 1.0F;
      float var8 = this.m00 * var6 + this.m10 * var7 + this.m30;
      float var9 = this.m01 * var6 + this.m11 * var7 + this.m31;
      float var10 = this.m02 * var6 + this.m12 * var7 + this.m32;
      float var11 = 1.0F / (this.m03 * var6 + this.m13 * var7 - this.m23 + this.m33);
      float var12 = (var8 - this.m20) * var11;
      float var13 = (var9 - this.m21) * var11;
      float var14 = (var10 - this.m22) * var11;
      float var15 = 1.0F / (this.m03 * var6 + this.m13 * var7 + this.m33);
      float var16 = var8 * var15;
      float var17 = var9 * var15;
      float var18 = var10 * var15;
      var4.x = var12;
      var4.y = var13;
      var4.z = var14;
      var5.x = var16 - var12;
      var5.y = var17 - var13;
      var5.z = var18 - var14;
      return this;
   }

   public Vector3f unprojectInv(Vector3fc var1, int[] var2, Vector3f var3) {
      return this.unprojectInv(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Vector3f unprojectInv(float var1, float var2, float var3, int[] var4, Vector3f var5) {
      float var6 = (var1 - (float)var4[0]) / (float)var4[2] * 2.0F - 1.0F;
      float var7 = (var2 - (float)var4[1]) / (float)var4[3] * 2.0F - 1.0F;
      float var8 = var3 + var3 - 1.0F;
      float var9 = 1.0F / (this.m03 * var6 + this.m13 * var7 + this.m23 * var8 + this.m33);
      return var5.set((this.m00 * var6 + this.m10 * var7 + this.m20 * var8 + this.m30) * var9, (this.m01 * var6 + this.m11 * var7 + this.m21 * var8 + this.m31) * var9, (this.m02 * var6 + this.m12 * var7 + this.m22 * var8 + this.m32) * var9);
   }

   public Vector4f project(float var1, float var2, float var3, int[] var4, Vector4f var5) {
      float var6 = 1.0F / (this.m03 * var1 + this.m13 * var2 + this.m23 * var3 + this.m33);
      float var7 = (this.m00 * var1 + this.m10 * var2 + this.m20 * var3 + this.m30) * var6;
      float var8 = (this.m01 * var1 + this.m11 * var2 + this.m21 * var3 + this.m31) * var6;
      float var9 = (this.m02 * var1 + this.m12 * var2 + this.m22 * var3 + this.m32) * var6;
      return var5.set((var7 * 0.5F + 0.5F) * (float)var4[2] + (float)var4[0], (var8 * 0.5F + 0.5F) * (float)var4[3] + (float)var4[1], (1.0F + var9) * 0.5F, 1.0F);
   }

   public Vector3f project(float var1, float var2, float var3, int[] var4, Vector3f var5) {
      float var6 = 1.0F / (this.m03 * var1 + this.m13 * var2 + this.m23 * var3 + this.m33);
      float var7 = (this.m00 * var1 + this.m10 * var2 + this.m20 * var3 + this.m30) * var6;
      float var8 = (this.m01 * var1 + this.m11 * var2 + this.m21 * var3 + this.m31) * var6;
      float var9 = (this.m02 * var1 + this.m12 * var2 + this.m22 * var3 + this.m32) * var6;
      var5.x = (var7 * 0.5F + 0.5F) * (float)var4[2] + (float)var4[0];
      var5.y = (var8 * 0.5F + 0.5F) * (float)var4[3] + (float)var4[1];
      var5.z = (1.0F + var9) * 0.5F;
      return var5;
   }

   public Vector4f project(Vector3fc var1, int[] var2, Vector4f var3) {
      return this.project(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Vector3f project(Vector3fc var1, int[] var2, Vector3f var3) {
      return this.project(var1.x(), var1.y(), var1.z(), var2, var3);
   }

   public Matrix4f reflect(float var1, float var2, float var3, float var4, Matrix4f var5) {
      if ((this.properties & 4) != 0) {
         return var5.reflection(var1, var2, var3, var4);
      } else {
         return (this.properties & 2) != 0 ? this.reflectAffine(var1, var2, var3, var4, var5) : this.reflectGeneric(var1, var2, var3, var4, var5);
      }
   }

   private Matrix4f reflectAffine(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      var5._m30(this.m00 * var19 + this.m10 * var20 + this.m20 * var21 + this.m30)._m31(this.m01 * var19 + this.m11 * var20 + this.m21 * var21 + this.m31)._m32(this.m02 * var19 + this.m12 * var20 + this.m22 * var21 + this.m32)._m33(this.m33);
      float var22 = this.m00 * var10 + this.m10 * var11 + this.m20 * var12;
      float var23 = this.m01 * var10 + this.m11 * var11 + this.m21 * var12;
      float var24 = this.m02 * var10 + this.m12 * var11 + this.m22 * var12;
      float var25 = this.m00 * var13 + this.m10 * var14 + this.m20 * var15;
      float var26 = this.m01 * var13 + this.m11 * var14 + this.m21 * var15;
      float var27 = this.m02 * var13 + this.m12 * var14 + this.m22 * var15;
      var5._m20(this.m00 * var16 + this.m10 * var17 + this.m20 * var18)._m21(this.m01 * var16 + this.m11 * var17 + this.m21 * var18)._m22(this.m02 * var16 + this.m12 * var17 + this.m22 * var18)._m23(0.0F)._m00(var22)._m01(var23)._m02(var24)._m03(0.0F)._m10(var25)._m11(var26)._m12(var27)._m13(0.0F)._properties(this.properties & -14);
      return var5;
   }

   private Matrix4f reflectGeneric(float var1, float var2, float var3, float var4, Matrix4f var5) {
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
      var5._m30(this.m00 * var19 + this.m10 * var20 + this.m20 * var21 + this.m30)._m31(this.m01 * var19 + this.m11 * var20 + this.m21 * var21 + this.m31)._m32(this.m02 * var19 + this.m12 * var20 + this.m22 * var21 + this.m32)._m33(this.m03 * var19 + this.m13 * var20 + this.m23 * var21 + this.m33);
      float var22 = this.m00 * var10 + this.m10 * var11 + this.m20 * var12;
      float var23 = this.m01 * var10 + this.m11 * var11 + this.m21 * var12;
      float var24 = this.m02 * var10 + this.m12 * var11 + this.m22 * var12;
      float var25 = this.m03 * var10 + this.m13 * var11 + this.m23 * var12;
      float var26 = this.m00 * var13 + this.m10 * var14 + this.m20 * var15;
      float var27 = this.m01 * var13 + this.m11 * var14 + this.m21 * var15;
      float var28 = this.m02 * var13 + this.m12 * var14 + this.m22 * var15;
      float var29 = this.m03 * var13 + this.m13 * var14 + this.m23 * var15;
      var5._m20(this.m00 * var16 + this.m10 * var17 + this.m20 * var18)._m21(this.m01 * var16 + this.m11 * var17 + this.m21 * var18)._m22(this.m02 * var16 + this.m12 * var17 + this.m22 * var18)._m23(this.m03 * var16 + this.m13 * var17 + this.m23 * var18)._m00(var22)._m01(var23)._m02(var24)._m03(var25)._m10(var26)._m11(var27)._m12(var28)._m13(var29)._properties(this.properties & -14);
      return var5;
   }

   public Matrix4f reflect(float var1, float var2, float var3, float var4) {
      return this.reflect(var1, var2, var3, var4, this);
   }

   public Matrix4f reflect(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.reflect(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f reflect(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      float var8 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var9 = var1 * var8;
      float var10 = var2 * var8;
      float var11 = var3 * var8;
      return this.reflect(var9, var10, var11, -var9 * var4 - var10 * var5 - var11 * var6, var7);
   }

   public Matrix4f reflect(Vector3fc var1, Vector3fc var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4f reflect(Quaternionfc var1, Vector3fc var2) {
      return this.reflect(var1, var2, this);
   }

   public Matrix4f reflect(Quaternionfc var1, Vector3fc var2, Matrix4f var3) {
      double var4 = (double)(var1.x() + var1.x());
      double var6 = (double)(var1.y() + var1.y());
      double var8 = (double)(var1.z() + var1.z());
      float var10 = (float)((double)var1.x() * var8 + (double)var1.w() * var6);
      float var11 = (float)((double)var1.y() * var8 - (double)var1.w() * var4);
      float var12 = (float)(1.0D - ((double)var1.x() * var4 + (double)var1.y() * var6));
      return this.reflect(var10, var11, var12, var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4f reflect(Vector3fc var1, Vector3fc var2, Matrix4f var3) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4f reflection(float var1, float var2, float var3, float var4) {
      float var5 = var1 + var1;
      float var6 = var2 + var2;
      float var7 = var3 + var3;
      float var8 = var4 + var4;
      this._m00(1.0F - var5 * var1)._m01(-var5 * var2)._m02(-var5 * var3)._m03(0.0F)._m10(-var6 * var1)._m11(1.0F - var6 * var2)._m12(-var6 * var3)._m13(0.0F)._m20(-var7 * var1)._m21(-var7 * var2)._m22(1.0F - var7 * var3)._m23(0.0F)._m30(-var8 * var1)._m31(-var8 * var2)._m32(-var8 * var3)._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f reflection(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var8 = var1 * var7;
      float var9 = var2 * var7;
      float var10 = var3 * var7;
      return this.reflection(var8, var9, var10, -var8 * var4 - var9 * var5 - var10 * var6);
   }

   public Matrix4f reflection(Vector3fc var1, Vector3fc var2) {
      return this.reflection(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4f reflection(Quaternionfc var1, Vector3fc var2) {
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
         return var2.set(this.m00, this.m10, this.m20, this.m30);
      case 1:
         return var2.set(this.m01, this.m11, this.m21, this.m31);
      case 2:
         return var2.set(this.m02, this.m12, this.m22, this.m32);
      case 3:
         return var2.set(this.m03, this.m13, this.m23, this.m33);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Vector3f getRow(int var1, Vector3f var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         return var2.set(this.m00, this.m10, this.m20);
      case 1:
         return var2.set(this.m01, this.m11, this.m21);
      case 2:
         return var2.set(this.m02, this.m12, this.m22);
      case 3:
         return var2.set(this.m03, this.m13, this.m23);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Matrix4f setRow(int var1, Vector4fc var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         return this._m00(var2.x())._m10(var2.y())._m20(var2.z())._m30(var2.w())._properties(0);
      case 1:
         return this._m01(var2.x())._m11(var2.y())._m21(var2.z())._m31(var2.w())._properties(0);
      case 2:
         return this._m02(var2.x())._m12(var2.y())._m22(var2.z())._m32(var2.w())._properties(0);
      case 3:
         return this._m03(var2.x())._m13(var2.y())._m23(var2.z())._m33(var2.w())._properties(0);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Vector4f getColumn(int var1, Vector4f var2) throws IndexOutOfBoundsException {
      return MemUtil.INSTANCE.getColumn(this, var1, var2);
   }

   public Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         return var2.set(this.m00, this.m01, this.m02);
      case 1:
         return var2.set(this.m10, this.m11, this.m12);
      case 2:
         return var2.set(this.m20, this.m21, this.m22);
      case 3:
         return var2.set(this.m30, this.m31, this.m32);
      default:
         throw new IndexOutOfBoundsException();
      }
   }

   public Matrix4f setColumn(int var1, Vector4fc var2) throws IndexOutOfBoundsException {
      return var2 instanceof Vector4f ? MemUtil.INSTANCE.setColumn((Vector4f)var2, var1, this)._properties(0) : MemUtil.INSTANCE.setColumn(var2, var1, this)._properties(0);
   }

   public float get(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Matrix4f set(int var1, int var2, float var3) {
      return MemUtil.INSTANCE.set(this, var1, var2, var3);
   }

   public float getRowColumn(int var1, int var2) {
      return MemUtil.INSTANCE.get(this, var2, var1);
   }

   public Matrix4f setRowColumn(int var1, int var2, float var3) {
      return MemUtil.INSTANCE.set(this, var2, var1, var3);
   }

   public Matrix4f normal() {
      return this.normal(this);
   }

   public Matrix4f normal(Matrix4f var1) {
      if ((this.properties & 4) != 0) {
         return var1.identity();
      } else {
         return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
      }
   }

   private Matrix4f normalOrthonormal(Matrix4f var1) {
      if (var1 != this) {
         var1.set((Matrix4fc)this);
      }

      return var1._properties(18);
   }

   private Matrix4f normalGeneric(Matrix4f var1) {
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
      return var1._m00(var10)._m01(var11)._m02(var12)._m03(0.0F)._m10(var13)._m11(var14)._m12(var15)._m13(0.0F)._m20(var16)._m21(var17)._m22(var18)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties((this.properties | 2) & -10);
   }

   public Matrix3f normal(Matrix3f var1) {
      return (this.properties & 16) != 0 ? this.normalOrthonormal(var1) : this.normalGeneric(var1);
   }

   private Matrix3f normalOrthonormal(Matrix3f var1) {
      var1.set((Matrix4fc)this);
      return var1;
   }

   private Matrix3f normalGeneric(Matrix3f var1) {
      float var2 = (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
      float var3 = 1.0F / var2;
      return var1._m00((this.m11 * this.m22 - this.m21 * this.m12) * var3)._m01((this.m20 * this.m12 - this.m10 * this.m22) * var3)._m02((this.m10 * this.m21 - this.m20 * this.m11) * var3)._m10((this.m21 * this.m02 - this.m01 * this.m22) * var3)._m11((this.m00 * this.m22 - this.m20 * this.m02) * var3)._m12((this.m20 * this.m01 - this.m00 * this.m21) * var3)._m20((this.m01 * this.m12 - this.m02 * this.m11) * var3)._m21((this.m02 * this.m10 - this.m00 * this.m12) * var3)._m22((this.m00 * this.m11 - this.m01 * this.m10) * var3);
   }

   public Matrix4f cofactor3x3() {
      return this.cofactor3x3(this);
   }

   public Matrix3f cofactor3x3(Matrix3f var1) {
      return var1._m00(this.m11 * this.m22 - this.m21 * this.m12)._m01(this.m20 * this.m12 - this.m10 * this.m22)._m02(this.m10 * this.m21 - this.m20 * this.m11)._m10(this.m21 * this.m02 - this.m01 * this.m22)._m11(this.m00 * this.m22 - this.m20 * this.m02)._m12(this.m20 * this.m01 - this.m00 * this.m21)._m20(this.m01 * this.m12 - this.m02 * this.m11)._m21(this.m02 * this.m10 - this.m00 * this.m12)._m22(this.m00 * this.m11 - this.m01 * this.m10);
   }

   public Matrix4f cofactor3x3(Matrix4f var1) {
      float var2 = this.m21 * this.m02 - this.m01 * this.m22;
      float var3 = this.m00 * this.m22 - this.m20 * this.m02;
      float var4 = this.m20 * this.m01 - this.m00 * this.m21;
      float var5 = this.m01 * this.m12 - this.m11 * this.m02;
      float var6 = this.m02 * this.m10 - this.m12 * this.m00;
      float var7 = this.m00 * this.m11 - this.m10 * this.m01;
      return var1._m00(this.m11 * this.m22 - this.m21 * this.m12)._m01(this.m20 * this.m12 - this.m10 * this.m22)._m02(this.m10 * this.m21 - this.m20 * this.m11)._m03(0.0F)._m10(var2)._m11(var3)._m12(var4)._m13(0.0F)._m20(var5)._m21(var6)._m22(var7)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F)._properties((this.properties | 2) & -10);
   }

   public Matrix4f normalize3x3() {
      return this.normalize3x3(this);
   }

   public Matrix4f normalize3x3(Matrix4f var1) {
      float var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      float var3 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      float var4 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      return var1._m00(this.m00 * var2)._m01(this.m01 * var2)._m02(this.m02 * var2)._m10(this.m10 * var3)._m11(this.m11 * var3)._m12(this.m12 * var3)._m20(this.m20 * var4)._m21(this.m21 * var4)._m22(this.m22 * var4)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties);
   }

   public Matrix3f normalize3x3(Matrix3f var1) {
      float var2 = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
      float var3 = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
      float var4 = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
      return var1._m00(this.m00 * var2)._m01(this.m01 * var2)._m02(this.m02 * var2)._m10(this.m10 * var3)._m11(this.m11 * var3)._m12(this.m12 * var3)._m20(this.m20 * var4)._m21(this.m21 * var4)._m22(this.m22 * var4);
   }

   public Vector4f frustumPlane(int var1, Vector4f var2) {
      switch(var1) {
      case 0:
         var2.set(this.m03 + this.m00, this.m13 + this.m10, this.m23 + this.m20, this.m33 + this.m30).normalize3();
         break;
      case 1:
         var2.set(this.m03 - this.m00, this.m13 - this.m10, this.m23 - this.m20, this.m33 - this.m30).normalize3();
         break;
      case 2:
         var2.set(this.m03 + this.m01, this.m13 + this.m11, this.m23 + this.m21, this.m33 + this.m31).normalize3();
         break;
      case 3:
         var2.set(this.m03 - this.m01, this.m13 - this.m11, this.m23 - this.m21, this.m33 - this.m31).normalize3();
         break;
      case 4:
         var2.set(this.m03 + this.m02, this.m13 + this.m12, this.m23 + this.m22, this.m33 + this.m32).normalize3();
         break;
      case 5:
         var2.set(this.m03 - this.m02, this.m13 - this.m12, this.m23 - this.m22, this.m33 - this.m32).normalize3();
         break;
      default:
         throw new IllegalArgumentException("dest");
      }

      return var2;
   }

   public Vector3f frustumCorner(int var1, Vector3f var2) {
      float var3;
      float var4;
      float var5;
      float var6;
      float var7;
      float var8;
      float var9;
      float var10;
      float var11;
      float var12;
      float var13;
      float var14;
      switch(var1) {
      case 0:
         var6 = this.m03 + this.m00;
         var7 = this.m13 + this.m10;
         var8 = this.m23 + this.m20;
         var3 = this.m33 + this.m30;
         var9 = this.m03 + this.m01;
         var10 = this.m13 + this.m11;
         var11 = this.m23 + this.m21;
         var4 = this.m33 + this.m31;
         var12 = this.m03 + this.m02;
         var13 = this.m13 + this.m12;
         var14 = this.m23 + this.m22;
         var5 = this.m33 + this.m32;
         break;
      case 1:
         var6 = this.m03 - this.m00;
         var7 = this.m13 - this.m10;
         var8 = this.m23 - this.m20;
         var3 = this.m33 - this.m30;
         var9 = this.m03 + this.m01;
         var10 = this.m13 + this.m11;
         var11 = this.m23 + this.m21;
         var4 = this.m33 + this.m31;
         var12 = this.m03 + this.m02;
         var13 = this.m13 + this.m12;
         var14 = this.m23 + this.m22;
         var5 = this.m33 + this.m32;
         break;
      case 2:
         var6 = this.m03 - this.m00;
         var7 = this.m13 - this.m10;
         var8 = this.m23 - this.m20;
         var3 = this.m33 - this.m30;
         var9 = this.m03 - this.m01;
         var10 = this.m13 - this.m11;
         var11 = this.m23 - this.m21;
         var4 = this.m33 - this.m31;
         var12 = this.m03 + this.m02;
         var13 = this.m13 + this.m12;
         var14 = this.m23 + this.m22;
         var5 = this.m33 + this.m32;
         break;
      case 3:
         var6 = this.m03 + this.m00;
         var7 = this.m13 + this.m10;
         var8 = this.m23 + this.m20;
         var3 = this.m33 + this.m30;
         var9 = this.m03 - this.m01;
         var10 = this.m13 - this.m11;
         var11 = this.m23 - this.m21;
         var4 = this.m33 - this.m31;
         var12 = this.m03 + this.m02;
         var13 = this.m13 + this.m12;
         var14 = this.m23 + this.m22;
         var5 = this.m33 + this.m32;
         break;
      case 4:
         var6 = this.m03 - this.m00;
         var7 = this.m13 - this.m10;
         var8 = this.m23 - this.m20;
         var3 = this.m33 - this.m30;
         var9 = this.m03 + this.m01;
         var10 = this.m13 + this.m11;
         var11 = this.m23 + this.m21;
         var4 = this.m33 + this.m31;
         var12 = this.m03 - this.m02;
         var13 = this.m13 - this.m12;
         var14 = this.m23 - this.m22;
         var5 = this.m33 - this.m32;
         break;
      case 5:
         var6 = this.m03 + this.m00;
         var7 = this.m13 + this.m10;
         var8 = this.m23 + this.m20;
         var3 = this.m33 + this.m30;
         var9 = this.m03 + this.m01;
         var10 = this.m13 + this.m11;
         var11 = this.m23 + this.m21;
         var4 = this.m33 + this.m31;
         var12 = this.m03 - this.m02;
         var13 = this.m13 - this.m12;
         var14 = this.m23 - this.m22;
         var5 = this.m33 - this.m32;
         break;
      case 6:
         var6 = this.m03 + this.m00;
         var7 = this.m13 + this.m10;
         var8 = this.m23 + this.m20;
         var3 = this.m33 + this.m30;
         var9 = this.m03 - this.m01;
         var10 = this.m13 - this.m11;
         var11 = this.m23 - this.m21;
         var4 = this.m33 - this.m31;
         var12 = this.m03 - this.m02;
         var13 = this.m13 - this.m12;
         var14 = this.m23 - this.m22;
         var5 = this.m33 - this.m32;
         break;
      case 7:
         var6 = this.m03 - this.m00;
         var7 = this.m13 - this.m10;
         var8 = this.m23 - this.m20;
         var3 = this.m33 - this.m30;
         var9 = this.m03 - this.m01;
         var10 = this.m13 - this.m11;
         var11 = this.m23 - this.m21;
         var4 = this.m33 - this.m31;
         var12 = this.m03 - this.m02;
         var13 = this.m13 - this.m12;
         var14 = this.m23 - this.m22;
         var5 = this.m33 - this.m32;
         break;
      default:
         throw new IllegalArgumentException("corner");
      }

      float var15 = var10 * var14 - var11 * var13;
      float var16 = var11 * var12 - var9 * var14;
      float var17 = var9 * var13 - var10 * var12;
      float var18 = var13 * var8 - var14 * var7;
      float var19 = var14 * var6 - var12 * var8;
      float var20 = var12 * var7 - var13 * var6;
      float var21 = var7 * var11 - var8 * var10;
      float var22 = var8 * var9 - var6 * var11;
      float var23 = var6 * var10 - var7 * var9;
      float var24 = 1.0F / (var6 * var15 + var7 * var16 + var8 * var17);
      var2.x = (-var15 * var3 - var18 * var4 - var21 * var5) * var24;
      var2.y = (-var16 * var3 - var19 * var4 - var22 * var5) * var24;
      var2.z = (-var17 * var3 - var20 * var4 - var23 * var5) * var24;
      return var2;
   }

   public Vector3f perspectiveOrigin(Vector3f var1) {
      float var5 = this.m03 + this.m00;
      float var6 = this.m13 + this.m10;
      float var7 = this.m23 + this.m20;
      float var2 = this.m33 + this.m30;
      float var8 = this.m03 - this.m00;
      float var9 = this.m13 - this.m10;
      float var10 = this.m23 - this.m20;
      float var3 = this.m33 - this.m30;
      float var11 = this.m03 - this.m01;
      float var12 = this.m13 - this.m11;
      float var13 = this.m23 - this.m21;
      float var4 = this.m33 - this.m31;
      float var14 = var9 * var13 - var10 * var12;
      float var15 = var10 * var11 - var8 * var13;
      float var16 = var8 * var12 - var9 * var11;
      float var17 = var12 * var7 - var13 * var6;
      float var18 = var13 * var5 - var11 * var7;
      float var19 = var11 * var6 - var12 * var5;
      float var20 = var6 * var10 - var7 * var9;
      float var21 = var7 * var8 - var5 * var10;
      float var22 = var5 * var9 - var6 * var8;
      float var23 = 1.0F / (var5 * var14 + var6 * var15 + var7 * var16);
      var1.x = (-var14 * var2 - var17 * var3 - var20 * var4) * var23;
      var1.y = (-var15 * var2 - var18 * var3 - var21 * var4) * var23;
      var1.z = (-var16 * var2 - var19 * var3 - var22 * var4) * var23;
      return var1;
   }

   public Vector3f perspectiveInvOrigin(Vector3f var1) {
      float var2 = 1.0F / this.m23;
      var1.x = this.m20 * var2;
      var1.y = this.m21 * var2;
      var1.z = this.m22 * var2;
      return var1;
   }

   public float perspectiveFov() {
      float var1 = this.m03 + this.m01;
      float var2 = this.m13 + this.m11;
      float var3 = this.m23 + this.m21;
      float var4 = this.m01 - this.m03;
      float var5 = this.m11 - this.m13;
      float var6 = this.m21 - this.m23;
      float var7 = Math.sqrt(var1 * var1 + var2 * var2 + var3 * var3);
      float var8 = Math.sqrt(var4 * var4 + var5 * var5 + var6 * var6);
      return Math.acos((var1 * var4 + var2 * var5 + var3 * var6) / (var7 * var8));
   }

   public float perspectiveNear() {
      return this.m32 / (this.m23 + this.m22);
   }

   public float perspectiveFar() {
      return this.m32 / (this.m22 - this.m23);
   }

   public Vector3f frustumRayDir(float var1, float var2, Vector3f var3) {
      float var4 = this.m10 * this.m23;
      float var5 = this.m13 * this.m21;
      float var6 = this.m10 * this.m21;
      float var7 = this.m11 * this.m23;
      float var8 = this.m13 * this.m20;
      float var9 = this.m11 * this.m20;
      float var10 = this.m03 * this.m20;
      float var11 = this.m01 * this.m23;
      float var12 = this.m01 * this.m20;
      float var13 = this.m03 * this.m21;
      float var14 = this.m00 * this.m23;
      float var15 = this.m00 * this.m21;
      float var16 = this.m00 * this.m13;
      float var17 = this.m03 * this.m11;
      float var18 = this.m00 * this.m11;
      float var19 = this.m01 * this.m13;
      float var20 = this.m03 * this.m10;
      float var21 = this.m01 * this.m10;
      float var22 = (var7 + var8 + var9 - var4 - var5 - var6) * (1.0F - var2) + (var4 - var5 - var6 + var7 - var8 + var9) * var2;
      float var23 = (var13 + var14 + var15 - var10 - var11 - var12) * (1.0F - var2) + (var10 - var11 - var12 + var13 - var14 + var15) * var2;
      float var24 = (var19 + var20 + var21 - var16 - var17 - var18) * (1.0F - var2) + (var16 - var17 - var18 + var19 - var20 + var21) * var2;
      float var25 = (var5 - var6 - var7 + var8 + var9 - var4) * (1.0F - var2) + (var4 + var5 - var6 - var7 - var8 + var9) * var2;
      float var26 = (var11 - var12 - var13 + var14 + var15 - var10) * (1.0F - var2) + (var10 + var11 - var12 - var13 - var14 + var15) * var2;
      float var27 = (var17 - var18 - var19 + var20 + var21 - var16) * (1.0F - var2) + (var16 + var17 - var18 - var19 - var20 + var21) * var2;
      var3.x = var22 + (var25 - var22) * var1;
      var3.y = var23 + (var26 - var23) * var1;
      var3.z = var24 + (var27 - var24) * var1;
      return var3.normalize(var3);
   }

   public Vector3f positiveZ(Vector3f var1) {
      return (this.properties & 16) != 0 ? this.normalizedPositiveZ(var1) : this.positiveZGeneric(var1);
   }

   private Vector3f positiveZGeneric(Vector3f var1) {
      return var1.set(this.m10 * this.m21 - this.m11 * this.m20, this.m20 * this.m01 - this.m21 * this.m00, this.m00 * this.m11 - this.m01 * this.m10).normalize();
   }

   public Vector3f normalizedPositiveZ(Vector3f var1) {
      return var1.set(this.m02, this.m12, this.m22);
   }

   public Vector3f positiveX(Vector3f var1) {
      return (this.properties & 16) != 0 ? this.normalizedPositiveX(var1) : this.positiveXGeneric(var1);
   }

   private Vector3f positiveXGeneric(Vector3f var1) {
      return var1.set(this.m11 * this.m22 - this.m12 * this.m21, this.m02 * this.m21 - this.m01 * this.m22, this.m01 * this.m12 - this.m02 * this.m11).normalize();
   }

   public Vector3f normalizedPositiveX(Vector3f var1) {
      return var1.set(this.m00, this.m10, this.m20);
   }

   public Vector3f positiveY(Vector3f var1) {
      return (this.properties & 16) != 0 ? this.normalizedPositiveY(var1) : this.positiveYGeneric(var1);
   }

   private Vector3f positiveYGeneric(Vector3f var1) {
      return var1.set(this.m12 * this.m20 - this.m10 * this.m22, this.m00 * this.m22 - this.m02 * this.m20, this.m02 * this.m10 - this.m00 * this.m12).normalize();
   }

   public Vector3f normalizedPositiveY(Vector3f var1) {
      return var1.set(this.m01, this.m11, this.m21);
   }

   public Vector3f originAffine(Vector3f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      float var3 = this.m00 * this.m12 - this.m02 * this.m10;
      float var4 = this.m01 * this.m12 - this.m02 * this.m11;
      float var5 = this.m20 * this.m31 - this.m21 * this.m30;
      float var6 = this.m20 * this.m32 - this.m22 * this.m30;
      float var7 = this.m21 * this.m32 - this.m22 * this.m31;
      return var1.set(-this.m10 * var7 + this.m11 * var6 - this.m12 * var5, this.m00 * var7 - this.m01 * var6 + this.m02 * var5, -this.m30 * var4 + this.m31 * var3 - this.m32 * var2);
   }

   public Vector3f origin(Vector3f var1) {
      return (this.properties & 2) != 0 ? this.originAffine(var1) : this.originGeneric(var1);
   }

   private Vector3f originGeneric(Vector3f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      float var3 = this.m00 * this.m12 - this.m02 * this.m10;
      float var4 = this.m00 * this.m13 - this.m03 * this.m10;
      float var5 = this.m01 * this.m12 - this.m02 * this.m11;
      float var6 = this.m01 * this.m13 - this.m03 * this.m11;
      float var7 = this.m02 * this.m13 - this.m03 * this.m12;
      float var8 = this.m20 * this.m31 - this.m21 * this.m30;
      float var9 = this.m20 * this.m32 - this.m22 * this.m30;
      float var10 = this.m20 * this.m33 - this.m23 * this.m30;
      float var11 = this.m21 * this.m32 - this.m22 * this.m31;
      float var12 = this.m21 * this.m33 - this.m23 * this.m31;
      float var13 = this.m22 * this.m33 - this.m23 * this.m32;
      float var14 = var2 * var13 - var3 * var12 + var4 * var11 + var5 * var10 - var6 * var9 + var7 * var8;
      float var15 = 1.0F / var14;
      float var16 = (-this.m10 * var11 + this.m11 * var9 - this.m12 * var8) * var15;
      float var17 = (this.m00 * var11 - this.m01 * var9 + this.m02 * var8) * var15;
      float var18 = (-this.m30 * var5 + this.m31 * var3 - this.m32 * var2) * var15;
      float var19 = var14 / (this.m20 * var5 - this.m21 * var3 + this.m22 * var2);
      return var1.set(var16 * var19, var17 * var19, var18 * var19);
   }

   public Matrix4f shadow(Vector4f var1, float var2, float var3, float var4, float var5) {
      return this.shadow(var1.x, var1.y, var1.z, var1.w, var2, var3, var4, var5, this);
   }

   public Matrix4f shadow(Vector4f var1, float var2, float var3, float var4, float var5, Matrix4f var6) {
      return this.shadow(var1.x, var1.y, var1.z, var1.w, var2, var3, var4, var5, var6);
   }

   public Matrix4f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return this.shadow(var1, var2, var3, var4, var5, var6, var7, var8, this);
   }

   public Matrix4f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Matrix4f var9) {
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
      float var35 = this.m03 * var16 + this.m13 * var17 + this.m23 * var18 + this.m33 * var19;
      float var36 = this.m00 * var20 + this.m10 * var21 + this.m20 * var22 + this.m30 * var23;
      float var37 = this.m01 * var20 + this.m11 * var21 + this.m21 * var22 + this.m31 * var23;
      float var38 = this.m02 * var20 + this.m12 * var21 + this.m22 * var22 + this.m32 * var23;
      float var39 = this.m03 * var20 + this.m13 * var21 + this.m23 * var22 + this.m33 * var23;
      float var40 = this.m00 * var24 + this.m10 * var25 + this.m20 * var26 + this.m30 * var27;
      float var41 = this.m01 * var24 + this.m11 * var25 + this.m21 * var26 + this.m31 * var27;
      float var42 = this.m02 * var24 + this.m12 * var25 + this.m22 * var26 + this.m32 * var27;
      float var43 = this.m03 * var24 + this.m13 * var25 + this.m23 * var26 + this.m33 * var27;
      var9._m30(this.m00 * var28 + this.m10 * var29 + this.m20 * var30 + this.m30 * var31)._m31(this.m01 * var28 + this.m11 * var29 + this.m21 * var30 + this.m31 * var31)._m32(this.m02 * var28 + this.m12 * var29 + this.m22 * var30 + this.m32 * var31)._m33(this.m03 * var28 + this.m13 * var29 + this.m23 * var30 + this.m33 * var31)._m00(var32)._m01(var33)._m02(var34)._m03(var35)._m10(var36)._m11(var37)._m12(var38)._m13(var39)._m20(var40)._m21(var41)._m22(var42)._m23(var43)._properties(this.properties & -30);
      return var9;
   }

   public Matrix4f shadow(Vector4f var1, Matrix4fc var2, Matrix4f var3) {
      float var4 = var2.m10();
      float var5 = var2.m11();
      float var6 = var2.m12();
      float var7 = -var4 * var2.m30() - var5 * var2.m31() - var6 * var2.m32();
      return this.shadow(var1.x, var1.y, var1.z, var1.w, var4, var5, var6, var7, var3);
   }

   public Matrix4f shadow(Vector4f var1, Matrix4f var2) {
      return this.shadow(var1, var2, this);
   }

   public Matrix4f shadow(float var1, float var2, float var3, float var4, Matrix4fc var5, Matrix4f var6) {
      float var7 = var5.m10();
      float var8 = var5.m11();
      float var9 = var5.m12();
      float var10 = -var7 * var5.m30() - var8 * var5.m31() - var9 * var5.m32();
      return this.shadow(var1, var2, var3, var4, var7, var8, var9, var10, var6);
   }

   public Matrix4f shadow(float var1, float var2, float var3, float var4, Matrix4f var5) {
      return this.shadow(var1, var2, var3, var4, var5, this);
   }

   public Matrix4f billboardCylindrical(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
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
      this._m00(var7)._m01(var8)._m02(var9)._m03(0.0F)._m10(var3.x())._m11(var3.y())._m12(var3.z())._m13(0.0F)._m20(var4)._m21(var5)._m22(var6)._m23(0.0F)._m30(var1.x())._m31(var1.y())._m32(var1.z())._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f billboardSpherical(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
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
      this._m00(var8)._m01(var9)._m02(var10)._m03(0.0F)._m10(var12)._m11(var13)._m12(var14)._m13(0.0F)._m20(var4)._m21(var5)._m22(var6)._m23(0.0F)._m30(var1.x())._m31(var1.y())._m32(var1.z())._m33(1.0F)._properties(18);
      return this;
   }

   public Matrix4f billboardSpherical(Vector3fc var1, Vector3fc var2) {
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
      this._m00(1.0F - var11)._m01(var12)._m02(-var14)._m03(0.0F)._m10(var12)._m11(1.0F - var10)._m12(var13)._m13(0.0F)._m20(var14)._m21(-var13)._m22(1.0F - var11 - var10)._m23(0.0F)._m30(var1.x())._m31(var1.y())._m32(var1.z())._m33(1.0F)._properties(18);
      return this;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.m00);
      var2 = 31 * var2 + Float.floatToIntBits(this.m01);
      var2 = 31 * var2 + Float.floatToIntBits(this.m02);
      var2 = 31 * var2 + Float.floatToIntBits(this.m03);
      var2 = 31 * var2 + Float.floatToIntBits(this.m10);
      var2 = 31 * var2 + Float.floatToIntBits(this.m11);
      var2 = 31 * var2 + Float.floatToIntBits(this.m12);
      var2 = 31 * var2 + Float.floatToIntBits(this.m13);
      var2 = 31 * var2 + Float.floatToIntBits(this.m20);
      var2 = 31 * var2 + Float.floatToIntBits(this.m21);
      var2 = 31 * var2 + Float.floatToIntBits(this.m22);
      var2 = 31 * var2 + Float.floatToIntBits(this.m23);
      var2 = 31 * var2 + Float.floatToIntBits(this.m30);
      var2 = 31 * var2 + Float.floatToIntBits(this.m31);
      var2 = 31 * var2 + Float.floatToIntBits(this.m32);
      var2 = 31 * var2 + Float.floatToIntBits(this.m33);
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4f)) {
         return false;
      } else {
         Matrix4fc var2 = (Matrix4fc)var1;
         if (Float.floatToIntBits(this.m00) != Float.floatToIntBits(var2.m00())) {
            return false;
         } else if (Float.floatToIntBits(this.m01) != Float.floatToIntBits(var2.m01())) {
            return false;
         } else if (Float.floatToIntBits(this.m02) != Float.floatToIntBits(var2.m02())) {
            return false;
         } else if (Float.floatToIntBits(this.m03) != Float.floatToIntBits(var2.m03())) {
            return false;
         } else if (Float.floatToIntBits(this.m10) != Float.floatToIntBits(var2.m10())) {
            return false;
         } else if (Float.floatToIntBits(this.m11) != Float.floatToIntBits(var2.m11())) {
            return false;
         } else if (Float.floatToIntBits(this.m12) != Float.floatToIntBits(var2.m12())) {
            return false;
         } else if (Float.floatToIntBits(this.m13) != Float.floatToIntBits(var2.m13())) {
            return false;
         } else if (Float.floatToIntBits(this.m20) != Float.floatToIntBits(var2.m20())) {
            return false;
         } else if (Float.floatToIntBits(this.m21) != Float.floatToIntBits(var2.m21())) {
            return false;
         } else if (Float.floatToIntBits(this.m22) != Float.floatToIntBits(var2.m22())) {
            return false;
         } else if (Float.floatToIntBits(this.m23) != Float.floatToIntBits(var2.m23())) {
            return false;
         } else if (Float.floatToIntBits(this.m30) != Float.floatToIntBits(var2.m30())) {
            return false;
         } else if (Float.floatToIntBits(this.m31) != Float.floatToIntBits(var2.m31())) {
            return false;
         } else if (Float.floatToIntBits(this.m32) != Float.floatToIntBits(var2.m32())) {
            return false;
         } else {
            return Float.floatToIntBits(this.m33) == Float.floatToIntBits(var2.m33());
         }
      }
   }

   public boolean equals(Matrix4fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix4f)) {
         return false;
      } else if (!Runtime.equals(this.m00, var1.m00(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m01, var1.m01(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m02, var1.m02(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m03, var1.m03(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m10, var1.m10(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m11, var1.m11(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m12, var1.m12(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m13, var1.m13(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m20, var1.m20(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m21, var1.m21(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m22, var1.m22(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m23, var1.m23(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m30, var1.m30(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m31, var1.m31(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m32, var1.m32(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.m33, var1.m33(), var2);
      }
   }

   public Matrix4f pick(float var1, float var2, float var3, float var4, int[] var5, Matrix4f var6) {
      float var7 = (float)var5[2] / var3;
      float var8 = (float)var5[3] / var4;
      float var9 = ((float)var5[2] + 2.0F * ((float)var5[0] - var1)) / var3;
      float var10 = ((float)var5[3] + 2.0F * ((float)var5[1] - var2)) / var4;
      var6._m30(this.m00 * var9 + this.m10 * var10 + this.m30)._m31(this.m01 * var9 + this.m11 * var10 + this.m31)._m32(this.m02 * var9 + this.m12 * var10 + this.m32)._m33(this.m03 * var9 + this.m13 * var10 + this.m33)._m00(this.m00 * var7)._m01(this.m01 * var7)._m02(this.m02 * var7)._m03(this.m03 * var7)._m10(this.m10 * var8)._m11(this.m11 * var8)._m12(this.m12 * var8)._m13(this.m13 * var8)._properties(0);
      return var6;
   }

   public Matrix4f pick(float var1, float var2, float var3, float var4, int[] var5) {
      return this.pick(var1, var2, var3, var4, var5, this);
   }

   public boolean isAffine() {
      return this.m03 == 0.0F && this.m13 == 0.0F && this.m23 == 0.0F && this.m33 == 1.0F;
   }

   public Matrix4f swap(Matrix4f var1) {
      MemUtil.INSTANCE.swap(this, var1);
      int var2 = this.properties;
      this.properties = var1.properties();
      var1.properties = var2;
      return this;
   }

   public Matrix4f arcball(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
      float var8 = this.m20 * -var1 + this.m30;
      float var9 = this.m21 * -var1 + this.m31;
      float var10 = this.m22 * -var1 + this.m32;
      float var11 = this.m23 * -var1 + this.m33;
      float var12 = Math.sin(var5);
      float var13 = Math.cosFromSin(var12, var5);
      float var14 = this.m10 * var13 + this.m20 * var12;
      float var15 = this.m11 * var13 + this.m21 * var12;
      float var16 = this.m12 * var13 + this.m22 * var12;
      float var17 = this.m13 * var13 + this.m23 * var12;
      float var18 = this.m20 * var13 - this.m10 * var12;
      float var19 = this.m21 * var13 - this.m11 * var12;
      float var20 = this.m22 * var13 - this.m12 * var12;
      float var21 = this.m23 * var13 - this.m13 * var12;
      var12 = Math.sin(var6);
      var13 = Math.cosFromSin(var12, var6);
      float var22 = this.m00 * var13 - var18 * var12;
      float var23 = this.m01 * var13 - var19 * var12;
      float var24 = this.m02 * var13 - var20 * var12;
      float var25 = this.m03 * var13 - var21 * var12;
      float var26 = this.m00 * var12 + var18 * var13;
      float var27 = this.m01 * var12 + var19 * var13;
      float var28 = this.m02 * var12 + var20 * var13;
      float var29 = this.m03 * var12 + var21 * var13;
      var7._m30(-var22 * var2 - var14 * var3 - var26 * var4 + var8)._m31(-var23 * var2 - var15 * var3 - var27 * var4 + var9)._m32(-var24 * var2 - var16 * var3 - var28 * var4 + var10)._m33(-var25 * var2 - var17 * var3 - var29 * var4 + var11)._m20(var26)._m21(var27)._m22(var28)._m23(var29)._m10(var14)._m11(var15)._m12(var16)._m13(var17)._m00(var22)._m01(var23)._m02(var24)._m03(var25)._properties(this.properties & -14);
      return var7;
   }

   public Matrix4f arcball(float var1, Vector3fc var2, float var3, float var4, Matrix4f var5) {
      return this.arcball(var1, var2.x(), var2.y(), var2.z(), var3, var4, var5);
   }

   public Matrix4f arcball(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.arcball(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f arcball(float var1, Vector3fc var2, float var3, float var4) {
      return this.arcball(var1, var2.x(), var2.y(), var2.z(), var3, var4, this);
   }

   public Matrix4f frustumAabb(Vector3f var1, Vector3f var2) {
      float var3 = Float.POSITIVE_INFINITY;
      float var4 = Float.POSITIVE_INFINITY;
      float var5 = Float.POSITIVE_INFINITY;
      float var6 = Float.NEGATIVE_INFINITY;
      float var7 = Float.NEGATIVE_INFINITY;
      float var8 = Float.NEGATIVE_INFINITY;

      for(int var9 = 0; var9 < 8; ++var9) {
         float var10 = (float)((var9 & 1) << 1) - 1.0F;
         float var11 = (float)((var9 >>> 1 & 1) << 1) - 1.0F;
         float var12 = (float)((var9 >>> 2 & 1) << 1) - 1.0F;
         float var13 = 1.0F / (this.m03 * var10 + this.m13 * var11 + this.m23 * var12 + this.m33);
         float var14 = (this.m00 * var10 + this.m10 * var11 + this.m20 * var12 + this.m30) * var13;
         float var15 = (this.m01 * var10 + this.m11 * var11 + this.m21 * var12 + this.m31) * var13;
         float var16 = (this.m02 * var10 + this.m12 * var11 + this.m22 * var12 + this.m32) * var13;
         var3 = var3 < var14 ? var3 : var14;
         var4 = var4 < var15 ? var4 : var15;
         var5 = var5 < var16 ? var5 : var16;
         var6 = var6 > var14 ? var6 : var14;
         var7 = var7 > var15 ? var7 : var15;
         var8 = var8 > var16 ? var8 : var16;
      }

      var1.x = var3;
      var1.y = var4;
      var1.z = var5;
      var2.x = var6;
      var2.y = var7;
      var2.z = var8;
      return this;
   }

   public Matrix4f projectedGridRange(Matrix4fc var1, float var2, float var3, Matrix4f var4) {
      float var5 = Float.POSITIVE_INFINITY;
      float var6 = Float.POSITIVE_INFINITY;
      float var7 = Float.NEGATIVE_INFINITY;
      float var8 = Float.NEGATIVE_INFINITY;
      boolean var9 = false;

      for(int var10 = 0; var10 < 12; ++var10) {
         float var11;
         float var12;
         float var13;
         float var14;
         float var15;
         float var16;
         if (var10 < 4) {
            var11 = -1.0F;
            var14 = 1.0F;
            var12 = var15 = (float)((var10 & 1) << 1) - 1.0F;
            var13 = var16 = (float)((var10 >>> 1 & 1) << 1) - 1.0F;
         } else if (var10 < 8) {
            var12 = -1.0F;
            var15 = 1.0F;
            var11 = var14 = (float)((var10 & 1) << 1) - 1.0F;
            var13 = var16 = (float)((var10 >>> 1 & 1) << 1) - 1.0F;
         } else {
            var13 = -1.0F;
            var16 = 1.0F;
            var11 = var14 = (float)((var10 & 1) << 1) - 1.0F;
            var12 = var15 = (float)((var10 >>> 1 & 1) << 1) - 1.0F;
         }

         float var17 = 1.0F / (this.m03 * var11 + this.m13 * var12 + this.m23 * var13 + this.m33);
         float var18 = (this.m00 * var11 + this.m10 * var12 + this.m20 * var13 + this.m30) * var17;
         float var19 = (this.m01 * var11 + this.m11 * var12 + this.m21 * var13 + this.m31) * var17;
         float var20 = (this.m02 * var11 + this.m12 * var12 + this.m22 * var13 + this.m32) * var17;
         var17 = 1.0F / (this.m03 * var14 + this.m13 * var15 + this.m23 * var16 + this.m33);
         float var21 = (this.m00 * var14 + this.m10 * var15 + this.m20 * var16 + this.m30) * var17;
         float var22 = (this.m01 * var14 + this.m11 * var15 + this.m21 * var16 + this.m31) * var17;
         float var23 = (this.m02 * var14 + this.m12 * var15 + this.m22 * var16 + this.m32) * var17;
         float var24 = var21 - var18;
         float var25 = var22 - var19;
         float var26 = var23 - var20;
         float var27 = 1.0F / var25;

         for(int var28 = 0; var28 < 2; ++var28) {
            float var29 = -(var19 + (var28 == 0 ? var2 : var3)) * var27;
            if (var29 >= 0.0F && var29 <= 1.0F) {
               var9 = true;
               float var30 = var18 + var29 * var24;
               float var31 = var20 + var29 * var26;
               var17 = 1.0F / (var1.m03() * var30 + var1.m23() * var31 + var1.m33());
               float var32 = (var1.m00() * var30 + var1.m20() * var31 + var1.m30()) * var17;
               float var33 = (var1.m01() * var30 + var1.m21() * var31 + var1.m31()) * var17;
               var5 = var5 < var32 ? var5 : var32;
               var6 = var6 < var33 ? var6 : var33;
               var7 = var7 > var32 ? var7 : var32;
               var8 = var8 > var33 ? var8 : var33;
            }
         }
      }

      if (!var9) {
         return null;
      } else {
         var4.set(var7 - var5, 0.0F, 0.0F, 0.0F, 0.0F, var8 - var6, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, var5, var6, 0.0F, 1.0F);
         var4._properties(2);
         return var4;
      }
   }

   public Matrix4f perspectiveFrustumSlice(float var1, float var2, Matrix4f var3) {
      float var4 = (this.m23 + this.m22) / this.m32;
      float var5 = 1.0F / (var1 - var2);
      var3._m00(this.m00 * var4 * var1)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11 * var4 * var1)._m12(this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22((var2 + var1) * var5)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32((var2 + var2) * var1 * var5)._m33(this.m33)._properties(this.properties & -29);
      return var3;
   }

   public Matrix4f orthoCrop(Matrix4fc var1, Matrix4f var2) {
      float var3 = Float.POSITIVE_INFINITY;
      float var4 = Float.NEGATIVE_INFINITY;
      float var5 = Float.POSITIVE_INFINITY;
      float var6 = Float.NEGATIVE_INFINITY;
      float var7 = Float.POSITIVE_INFINITY;
      float var8 = Float.NEGATIVE_INFINITY;

      for(int var9 = 0; var9 < 8; ++var9) {
         float var10 = (float)((var9 & 1) << 1) - 1.0F;
         float var11 = (float)((var9 >>> 1 & 1) << 1) - 1.0F;
         float var12 = (float)((var9 >>> 2 & 1) << 1) - 1.0F;
         float var13 = 1.0F / (this.m03 * var10 + this.m13 * var11 + this.m23 * var12 + this.m33);
         float var14 = (this.m00 * var10 + this.m10 * var11 + this.m20 * var12 + this.m30) * var13;
         float var15 = (this.m01 * var10 + this.m11 * var11 + this.m21 * var12 + this.m31) * var13;
         float var16 = (this.m02 * var10 + this.m12 * var11 + this.m22 * var12 + this.m32) * var13;
         var13 = 1.0F / (var1.m03() * var14 + var1.m13() * var15 + var1.m23() * var16 + var1.m33());
         float var17 = var1.m00() * var14 + var1.m10() * var15 + var1.m20() * var16 + var1.m30();
         float var18 = var1.m01() * var14 + var1.m11() * var15 + var1.m21() * var16 + var1.m31();
         float var19 = (var1.m02() * var14 + var1.m12() * var15 + var1.m22() * var16 + var1.m32()) * var13;
         var3 = var3 < var17 ? var3 : var17;
         var4 = var4 > var17 ? var4 : var17;
         var5 = var5 < var18 ? var5 : var18;
         var6 = var6 > var18 ? var6 : var18;
         var7 = var7 < var19 ? var7 : var19;
         var8 = var8 > var19 ? var8 : var19;
      }

      return var2.setOrtho(var3, var4, var5, var6, -var8, -var7);
   }

   public Matrix4f trapezoidCrop(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = var4 - var2;
      float var10 = var1 - var3;
      float var12 = -var9;
      float var13 = var9 * var2 - var10 * var1;
      float var16 = -(var9 * var1 + var10 * var2);
      float var17 = var10 * var7 + var12 * var8 + var13;
      float var18 = var9 * var7 + var10 * var8 + var16;
      float var19 = -var17 / var18;
      float var11 = var10 + var19 * var9;
      var12 += var19 * var10;
      var13 += var19 * var16;
      float var20 = var11 * var3 + var12 * var4 + var13;
      float var21 = var11 * var5 + var12 * var6 + var13;
      float var22 = var20 * var18 / (var21 - var20);
      var16 += var22;
      float var23 = 2.0F / var21;
      float var24 = 1.0F / (var18 + var22);
      float var25 = (var24 + var24) * var22 / (1.0F - var24 * var22);
      float var26 = var9 * var24;
      float var27 = var10 * var24;
      float var28 = var16 * var24;
      float var14 = (var25 + 1.0F) * var26;
      float var15 = (var25 + 1.0F) * var27;
      var16 = (var25 + 1.0F) * var28 - var25;
      var11 = var23 * var11 - var26;
      var12 = var23 * var12 - var27;
      var13 = var23 * var13 - var28;
      this.set(var11, var14, 0.0F, var26, var12, var15, 0.0F, var27, 0.0F, 0.0F, 1.0F, 0.0F, var13, var16, 0.0F, var28);
      this._properties(0);
      return this;
   }

   public Matrix4f transformAab(float var1, float var2, float var3, float var4, float var5, float var6, Vector3f var7, Vector3f var8) {
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

   public Matrix4f transformAab(Vector3fc var1, Vector3fc var2, Vector3f var3, Vector3f var4) {
      return this.transformAab(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3, var4);
   }

   public Matrix4f lerp(Matrix4fc var1, float var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix4f lerp(Matrix4fc var1, float var2, Matrix4f var3) {
      var3._m00(Math.fma(var1.m00() - this.m00, var2, this.m00))._m01(Math.fma(var1.m01() - this.m01, var2, this.m01))._m02(Math.fma(var1.m02() - this.m02, var2, this.m02))._m03(Math.fma(var1.m03() - this.m03, var2, this.m03))._m10(Math.fma(var1.m10() - this.m10, var2, this.m10))._m11(Math.fma(var1.m11() - this.m11, var2, this.m11))._m12(Math.fma(var1.m12() - this.m12, var2, this.m12))._m13(Math.fma(var1.m13() - this.m13, var2, this.m13))._m20(Math.fma(var1.m20() - this.m20, var2, this.m20))._m21(Math.fma(var1.m21() - this.m21, var2, this.m21))._m22(Math.fma(var1.m22() - this.m22, var2, this.m22))._m23(Math.fma(var1.m23() - this.m23, var2, this.m23))._m30(Math.fma(var1.m30() - this.m30, var2, this.m30))._m31(Math.fma(var1.m31() - this.m31, var2, this.m31))._m32(Math.fma(var1.m32() - this.m32, var2, this.m32))._m33(Math.fma(var1.m33() - this.m33, var2, this.m33))._properties(this.properties & var1.properties());
      return var3;
   }

   public Matrix4f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix4f var3) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public Matrix4f rotateTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), this);
   }

   public Matrix4f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.rotateTowards(var1, var2, var3, var4, var5, var6, this);
   }

   public Matrix4f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7) {
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
      var7._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33);
      float var28 = this.m00 * var12 + this.m10 * var13 + this.m20 * var14;
      float var29 = this.m01 * var12 + this.m11 * var13 + this.m21 * var14;
      float var30 = this.m02 * var12 + this.m12 * var13 + this.m22 * var14;
      float var31 = this.m03 * var12 + this.m13 * var13 + this.m23 * var14;
      float var32 = this.m00 * var16 + this.m10 * var17 + this.m20 * var18;
      float var33 = this.m01 * var16 + this.m11 * var17 + this.m21 * var18;
      float var34 = this.m02 * var16 + this.m12 * var17 + this.m22 * var18;
      float var35 = this.m03 * var16 + this.m13 * var17 + this.m23 * var18;
      var7._m20(this.m00 * var9 + this.m10 * var10 + this.m20 * var11)._m21(this.m01 * var9 + this.m11 * var10 + this.m21 * var11)._m22(this.m02 * var9 + this.m12 * var10 + this.m22 * var11)._m23(this.m03 * var9 + this.m13 * var10 + this.m23 * var11)._m00(var28)._m01(var29)._m02(var30)._m03(var31)._m10(var32)._m11(var33)._m12(var34)._m13(var35)._properties(this.properties & -14);
      return var7;
   }

   public Matrix4f rotationTowards(Vector3fc var1, Vector3fc var2) {
      return this.rotationTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public Matrix4f rotationTowards(float var1, float var2, float var3, float var4, float var5, float var6) {
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
      if ((this.properties & 4) == 0) {
         MemUtil.INSTANCE.identity(this);
      }

      this._m00(var11)._m01(var12)._m02(var13)._m10(var15)._m11(var16)._m12(var17)._m20(var8)._m21(var9)._m22(var10)._properties(18);
      return this;
   }

   public Matrix4f translationRotateTowards(Vector3fc var1, Vector3fc var2, Vector3fc var3) {
      return this.translationRotateTowards(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3.x(), var3.y(), var3.z());
   }

   public Matrix4f translationRotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
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
      this._m00(var14)._m01(var15)._m02(var16)._m03(0.0F)._m10(var18)._m11(var19)._m12(var20)._m13(0.0F)._m20(var11)._m21(var12)._m22(var13)._m23(0.0F)._m30(var1)._m31(var2)._m32(var3)._m33(1.0F)._properties(18);
      return this;
   }

   public Vector3f getEulerAnglesZYX(Vector3f var1) {
      var1.x = Math.atan2(this.m12, this.m22);
      var1.y = Math.atan2(-this.m02, Math.sqrt(this.m12 * this.m12 + this.m22 * this.m22));
      var1.z = Math.atan2(this.m01, this.m00);
      return var1;
   }

   public Matrix4f affineSpan(Vector3f var1, Vector3f var2, Vector3f var3, Vector3f var4) {
      float var5 = this.m10 * this.m22;
      float var6 = this.m10 * this.m21;
      float var7 = this.m10 * this.m02;
      float var8 = this.m10 * this.m01;
      float var9 = this.m11 * this.m22;
      float var10 = this.m11 * this.m20;
      float var11 = this.m11 * this.m02;
      float var12 = this.m11 * this.m00;
      float var13 = this.m12 * this.m21;
      float var14 = this.m12 * this.m20;
      float var15 = this.m12 * this.m01;
      float var16 = this.m12 * this.m00;
      float var17 = this.m20 * this.m02;
      float var18 = this.m20 * this.m01;
      float var19 = this.m21 * this.m02;
      float var20 = this.m21 * this.m00;
      float var21 = this.m22 * this.m01;
      float var22 = this.m22 * this.m00;
      float var23 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
      float var24 = (var9 - var13) * var23;
      float var25 = (var19 - var21) * var23;
      float var26 = (var15 - var11) * var23;
      float var27 = (var14 - var5) * var23;
      float var28 = (var22 - var17) * var23;
      float var29 = (var7 - var16) * var23;
      float var30 = (var6 - var10) * var23;
      float var31 = (var18 - var20) * var23;
      float var32 = (var12 - var8) * var23;
      var1.x = -var24 - var27 - var30 + (var5 * this.m31 - var6 * this.m32 + var10 * this.m32 - var9 * this.m30 + var13 * this.m30 - var14 * this.m31) * var23;
      var1.y = -var25 - var28 - var31 + (var17 * this.m31 - var18 * this.m32 + var20 * this.m32 - var19 * this.m30 + var21 * this.m30 - var22 * this.m31) * var23;
      var1.z = -var26 - var29 - var32 + (var11 * this.m30 - var15 * this.m30 + var16 * this.m31 - var7 * this.m31 + var8 * this.m32 - var12 * this.m32) * var23;
      var2.x = 2.0F * var24;
      var2.y = 2.0F * var25;
      var2.z = 2.0F * var26;
      var3.x = 2.0F * var27;
      var3.y = 2.0F * var28;
      var3.z = 2.0F * var29;
      var4.x = 2.0F * var30;
      var4.y = 2.0F * var31;
      var4.z = 2.0F * var32;
      return this;
   }

   public boolean testPoint(float var1, float var2, float var3) {
      float var4 = this.m03 + this.m00;
      float var5 = this.m13 + this.m10;
      float var6 = this.m23 + this.m20;
      float var7 = this.m33 + this.m30;
      float var8 = this.m03 - this.m00;
      float var9 = this.m13 - this.m10;
      float var10 = this.m23 - this.m20;
      float var11 = this.m33 - this.m30;
      float var12 = this.m03 + this.m01;
      float var13 = this.m13 + this.m11;
      float var14 = this.m23 + this.m21;
      float var15 = this.m33 + this.m31;
      float var16 = this.m03 - this.m01;
      float var17 = this.m13 - this.m11;
      float var18 = this.m23 - this.m21;
      float var19 = this.m33 - this.m31;
      float var20 = this.m03 + this.m02;
      float var21 = this.m13 + this.m12;
      float var22 = this.m23 + this.m22;
      float var23 = this.m33 + this.m32;
      float var24 = this.m03 - this.m02;
      float var25 = this.m13 - this.m12;
      float var26 = this.m23 - this.m22;
      float var27 = this.m33 - this.m32;
      return var4 * var1 + var5 * var2 + var6 * var3 + var7 >= 0.0F && var8 * var1 + var9 * var2 + var10 * var3 + var11 >= 0.0F && var12 * var1 + var13 * var2 + var14 * var3 + var15 >= 0.0F && var16 * var1 + var17 * var2 + var18 * var3 + var19 >= 0.0F && var20 * var1 + var21 * var2 + var22 * var3 + var23 >= 0.0F && var24 * var1 + var25 * var2 + var26 * var3 + var27 >= 0.0F;
   }

   public boolean testSphere(float var1, float var2, float var3, float var4) {
      float var6 = this.m03 + this.m00;
      float var7 = this.m13 + this.m10;
      float var8 = this.m23 + this.m20;
      float var9 = this.m33 + this.m30;
      float var5 = Math.invsqrt(var6 * var6 + var7 * var7 + var8 * var8);
      var6 *= var5;
      var7 *= var5;
      var8 *= var5;
      var9 *= var5;
      float var10 = this.m03 - this.m00;
      float var11 = this.m13 - this.m10;
      float var12 = this.m23 - this.m20;
      float var13 = this.m33 - this.m30;
      var5 = Math.invsqrt(var10 * var10 + var11 * var11 + var12 * var12);
      var10 *= var5;
      var11 *= var5;
      var12 *= var5;
      var13 *= var5;
      float var14 = this.m03 + this.m01;
      float var15 = this.m13 + this.m11;
      float var16 = this.m23 + this.m21;
      float var17 = this.m33 + this.m31;
      var5 = Math.invsqrt(var14 * var14 + var15 * var15 + var16 * var16);
      var14 *= var5;
      var15 *= var5;
      var16 *= var5;
      var17 *= var5;
      float var18 = this.m03 - this.m01;
      float var19 = this.m13 - this.m11;
      float var20 = this.m23 - this.m21;
      float var21 = this.m33 - this.m31;
      var5 = Math.invsqrt(var18 * var18 + var19 * var19 + var20 * var20);
      var18 *= var5;
      var19 *= var5;
      var20 *= var5;
      var21 *= var5;
      float var22 = this.m03 + this.m02;
      float var23 = this.m13 + this.m12;
      float var24 = this.m23 + this.m22;
      float var25 = this.m33 + this.m32;
      var5 = Math.invsqrt(var22 * var22 + var23 * var23 + var24 * var24);
      var22 *= var5;
      var23 *= var5;
      var24 *= var5;
      var25 *= var5;
      float var26 = this.m03 - this.m02;
      float var27 = this.m13 - this.m12;
      float var28 = this.m23 - this.m22;
      float var29 = this.m33 - this.m32;
      var5 = Math.invsqrt(var26 * var26 + var27 * var27 + var28 * var28);
      var26 *= var5;
      var27 *= var5;
      var28 *= var5;
      var29 *= var5;
      return var6 * var1 + var7 * var2 + var8 * var3 + var9 >= -var4 && var10 * var1 + var11 * var2 + var12 * var3 + var13 >= -var4 && var14 * var1 + var15 * var2 + var16 * var3 + var17 >= -var4 && var18 * var1 + var19 * var2 + var20 * var3 + var21 >= -var4 && var22 * var1 + var23 * var2 + var24 * var3 + var25 >= -var4 && var26 * var1 + var27 * var2 + var28 * var3 + var29 >= -var4;
   }

   public boolean testAab(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = this.m03 + this.m00;
      float var8 = this.m13 + this.m10;
      float var9 = this.m23 + this.m20;
      float var10 = this.m33 + this.m30;
      float var11 = this.m03 - this.m00;
      float var12 = this.m13 - this.m10;
      float var13 = this.m23 - this.m20;
      float var14 = this.m33 - this.m30;
      float var15 = this.m03 + this.m01;
      float var16 = this.m13 + this.m11;
      float var17 = this.m23 + this.m21;
      float var18 = this.m33 + this.m31;
      float var19 = this.m03 - this.m01;
      float var20 = this.m13 - this.m11;
      float var21 = this.m23 - this.m21;
      float var22 = this.m33 - this.m31;
      float var23 = this.m03 + this.m02;
      float var24 = this.m13 + this.m12;
      float var25 = this.m23 + this.m22;
      float var26 = this.m33 + this.m32;
      float var27 = this.m03 - this.m02;
      float var28 = this.m13 - this.m12;
      float var29 = this.m23 - this.m22;
      float var30 = this.m33 - this.m32;
      return var7 * (var7 < 0.0F ? var1 : var4) + var8 * (var8 < 0.0F ? var2 : var5) + var9 * (var9 < 0.0F ? var3 : var6) >= -var10 && var11 * (var11 < 0.0F ? var1 : var4) + var12 * (var12 < 0.0F ? var2 : var5) + var13 * (var13 < 0.0F ? var3 : var6) >= -var14 && var15 * (var15 < 0.0F ? var1 : var4) + var16 * (var16 < 0.0F ? var2 : var5) + var17 * (var17 < 0.0F ? var3 : var6) >= -var18 && var19 * (var19 < 0.0F ? var1 : var4) + var20 * (var20 < 0.0F ? var2 : var5) + var21 * (var21 < 0.0F ? var3 : var6) >= -var22 && var23 * (var23 < 0.0F ? var1 : var4) + var24 * (var24 < 0.0F ? var2 : var5) + var25 * (var25 < 0.0F ? var3 : var6) >= -var26 && var27 * (var27 < 0.0F ? var1 : var4) + var28 * (var28 < 0.0F ? var2 : var5) + var29 * (var29 < 0.0F ? var3 : var6) >= -var30;
   }

   public Matrix4f obliqueZ(float var1, float var2) {
      this.m20 += this.m00 * var1 + this.m10 * var2;
      this.m21 += this.m01 * var1 + this.m11 * var2;
      this.m22 += this.m02 * var1 + this.m12 * var2;
      this._properties(this.properties & 2);
      return this;
   }

   public Matrix4f obliqueZ(float var1, float var2, Matrix4f var3) {
      var3._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(this.m00 * var1 + this.m10 * var2 + this.m20)._m21(this.m01 * var1 + this.m11 * var2 + this.m21)._m22(this.m02 * var1 + this.m12 * var2 + this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 2);
      return var3;
   }

   public static void projViewFromRectangle(Vector3f var0, Vector3f var1, Vector3f var2, Vector3f var3, float var4, boolean var5, Matrix4f var6, Matrix4f var7) {
      float var8 = var3.y * var2.z - var3.z * var2.y;
      float var9 = var3.z * var2.x - var3.x * var2.z;
      float var10 = var3.x * var2.y - var3.y * var2.x;
      float var11 = var8 * (var1.x - var0.x) + var9 * (var1.y - var0.y) + var10 * (var1.z - var0.z);
      float var12 = var11 >= 0.0F ? 1.0F : -1.0F;
      var8 *= var12;
      var9 *= var12;
      var10 *= var12;
      var11 *= var12;
      var7.setLookAt(var0.x, var0.y, var0.z, var0.x + var8, var0.y + var9, var0.z + var10, var3.x, var3.y, var3.z);
      float var13 = var7.m00 * var1.x + var7.m10 * var1.y + var7.m20 * var1.z + var7.m30;
      float var14 = var7.m01 * var1.x + var7.m11 * var1.y + var7.m21 * var1.z + var7.m31;
      float var15 = var7.m00 * var2.x + var7.m10 * var2.y + var7.m20 * var2.z;
      float var16 = var7.m01 * var3.x + var7.m11 * var3.y + var7.m21 * var3.z;
      float var17 = Math.sqrt(var8 * var8 + var9 * var9 + var10 * var10);
      float var18 = var11 / var17;
      float var19;
      if (Float.isInfinite(var4) && var4 < 0.0F) {
         var19 = var18;
         var18 = Float.POSITIVE_INFINITY;
      } else if (Float.isInfinite(var4) && var4 > 0.0F) {
         var19 = Float.POSITIVE_INFINITY;
      } else if (var4 < 0.0F) {
         var19 = var18;
         var18 += var4;
      } else {
         var19 = var18 + var4;
      }

      var6.setFrustum(var13, var13 + var15, var14, var14 + var16, var18, var19, var5);
   }

   public Matrix4f withLookAtUp(Vector3fc var1) {
      return this.withLookAtUp(var1.x(), var1.y(), var1.z(), this);
   }

   public Matrix4f withLookAtUp(Vector3fc var1, Matrix4f var2) {
      return this.withLookAtUp(var1.x(), var1.y(), var1.z());
   }

   public Matrix4f withLookAtUp(float var1, float var2, float var3) {
      return this.withLookAtUp(var1, var2, var3, this);
   }

   public Matrix4f withLookAtUp(float var1, float var2, float var3, Matrix4f var4) {
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
         var4._m02(this.m02)._m12(this.m12)._m22(this.m22)._m32(this.m32)._m03(this.m03)._m13(this.m13)._m23(this.m23)._m33(this.m33);
      }

      var4._properties(this.properties & -14);
      return var4;
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m03) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m13) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m23) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32) && Math.isFinite(this.m33);
   }
}
