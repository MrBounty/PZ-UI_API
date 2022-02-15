package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Vector4d implements Externalizable, Vector4dc {
   private static final long serialVersionUID = 1L;
   public double x;
   public double y;
   public double z;
   public double w;

   public Vector4d() {
      this.w = 1.0D;
   }

   public Vector4d(Vector4dc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
   }

   public Vector4d(Vector4ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
   }

   public Vector4d(Vector3dc var1, double var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
   }

   public Vector4d(Vector3ic var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = var2;
   }

   public Vector4d(Vector2dc var1, double var2, double var4) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var4;
   }

   public Vector4d(Vector2ic var1, double var2, double var4) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      this.w = var4;
   }

   public Vector4d(double var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
   }

   public Vector4d(Vector4fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
   }

   public Vector4d(Vector3fc var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = var2;
   }

   public Vector4d(Vector2fc var1, double var2, double var4) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      this.w = var4;
   }

   public Vector4d(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public Vector4d(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
      this.z = (double)var1[2];
      this.w = (double)var1[3];
   }

   public Vector4d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Vector4d(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4d(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector4d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4d(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
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

   public Vector4d set(Vector4dc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
      return this;
   }

   public Vector4d set(Vector4fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
      return this;
   }

   public Vector4d set(Vector4ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = (double)var1.w();
      return this;
   }

   public Vector4d set(Vector3dc var1, double var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
      return this;
   }

   public Vector4d set(Vector3ic var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = var2;
      return this;
   }

   public Vector4d set(Vector3fc var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      this.w = var2;
      return this;
   }

   public Vector4d set(Vector2dc var1, double var2, double var4) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var4;
      return this;
   }

   public Vector4d set(Vector2ic var1, double var2, double var4) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      this.w = var4;
      return this;
   }

   public Vector4d set(double var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
      return this;
   }

   public Vector4d set(Vector2fc var1, double var2, double var4) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      this.w = var4;
      return this;
   }

   public Vector4d set(double var1, double var3, double var5, double var7) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
      return this;
   }

   public Vector4d set(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      return this;
   }

   public Vector4d set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[2];
      return this;
   }

   public Vector4d set(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
      this.z = (double)var1[2];
      this.w = (double)var1[2];
      return this;
   }

   public Vector4d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4d set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4d set(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Vector4d setComponent(int var1, double var2) throws IllegalArgumentException {
      switch(var1) {
      case 0:
         this.x = var2;
         break;
      case 1:
         this.y = var2;
         break;
      case 2:
         this.z = var2;
         break;
      case 3:
         this.w = var2;
         break;
      default:
         throw new IllegalArgumentException();
      }

      return this;
   }

   public ByteBuffer get(ByteBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer get(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public DoubleBuffer get(DoubleBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public DoubleBuffer get(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public ByteBuffer getf(ByteBuffer var1) {
      MemUtil.INSTANCE.putf(this, var1.position(), var1);
      return var1;
   }

   public ByteBuffer getf(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putf(this, var1, var2);
      return var2;
   }

   public FloatBuffer get(FloatBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public Vector4dc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector4d sub(Vector4dc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      this.w -= var1.w();
      return this;
   }

   public Vector4d sub(Vector4dc var1, Vector4d var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      var2.w = this.w - var1.w();
      return var2;
   }

   public Vector4d sub(Vector4fc var1) {
      this.x -= (double)var1.x();
      this.y -= (double)var1.y();
      this.z -= (double)var1.z();
      this.w -= (double)var1.w();
      return this;
   }

   public Vector4d sub(Vector4fc var1, Vector4d var2) {
      var2.x = this.x - (double)var1.x();
      var2.y = this.y - (double)var1.y();
      var2.z = this.z - (double)var1.z();
      var2.w = this.w - (double)var1.w();
      return var2;
   }

   public Vector4d sub(double var1, double var3, double var5, double var7) {
      this.x -= var1;
      this.y -= var3;
      this.z -= var5;
      this.w -= var7;
      return this;
   }

   public Vector4d sub(double var1, double var3, double var5, double var7, Vector4d var9) {
      var9.x = this.x - var1;
      var9.y = this.y - var3;
      var9.z = this.z - var5;
      var9.w = this.w - var7;
      return var9;
   }

   public Vector4d add(Vector4dc var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      this.w += var1.w();
      return this;
   }

   public Vector4d add(Vector4dc var1, Vector4d var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      var2.w = this.w + var1.w();
      return var2;
   }

   public Vector4d add(Vector4fc var1, Vector4d var2) {
      var2.x = this.x + (double)var1.x();
      var2.y = this.y + (double)var1.y();
      var2.z = this.z + (double)var1.z();
      var2.w = this.w + (double)var1.w();
      return var2;
   }

   public Vector4d add(double var1, double var3, double var5, double var7) {
      this.x += var1;
      this.y += var3;
      this.z += var5;
      this.w += var7;
      return this;
   }

   public Vector4d add(double var1, double var3, double var5, double var7, Vector4d var9) {
      var9.x = this.x + var1;
      var9.y = this.y + var3;
      var9.z = this.z + var5;
      var9.w = this.w + var7;
      return var9;
   }

   public Vector4d add(Vector4fc var1) {
      this.x += (double)var1.x();
      this.y += (double)var1.y();
      this.z += (double)var1.z();
      this.w += (double)var1.w();
      return this;
   }

   public Vector4d fma(Vector4dc var1, Vector4dc var2) {
      this.x = Math.fma(var1.x(), var2.x(), this.x);
      this.y = Math.fma(var1.y(), var2.y(), this.y);
      this.z = Math.fma(var1.z(), var2.z(), this.z);
      this.w = Math.fma(var1.w(), var2.w(), this.w);
      return this;
   }

   public Vector4d fma(double var1, Vector4dc var3) {
      this.x = Math.fma(var1, var3.x(), this.x);
      this.y = Math.fma(var1, var3.y(), this.y);
      this.z = Math.fma(var1, var3.z(), this.z);
      this.w = Math.fma(var1, var3.w(), this.w);
      return this;
   }

   public Vector4d fma(Vector4dc var1, Vector4dc var2, Vector4d var3) {
      var3.x = Math.fma(var1.x(), var2.x(), this.x);
      var3.y = Math.fma(var1.y(), var2.y(), this.y);
      var3.z = Math.fma(var1.z(), var2.z(), this.z);
      var3.w = Math.fma(var1.w(), var2.w(), this.w);
      return var3;
   }

   public Vector4d fma(double var1, Vector4dc var3, Vector4d var4) {
      var4.x = Math.fma(var1, var3.x(), this.x);
      var4.y = Math.fma(var1, var3.y(), this.y);
      var4.z = Math.fma(var1, var3.z(), this.z);
      var4.w = Math.fma(var1, var3.w(), this.w);
      return var4;
   }

   public Vector4d mulAdd(Vector4dc var1, Vector4dc var2) {
      this.x = Math.fma(this.x, var1.x(), var2.x());
      this.y = Math.fma(this.y, var1.y(), var2.y());
      this.z = Math.fma(this.z, var1.z(), var2.z());
      return this;
   }

   public Vector4d mulAdd(double var1, Vector4dc var3) {
      this.x = Math.fma(this.x, var1, var3.x());
      this.y = Math.fma(this.y, var1, var3.y());
      this.z = Math.fma(this.z, var1, var3.z());
      return this;
   }

   public Vector4d mulAdd(Vector4dc var1, Vector4dc var2, Vector4d var3) {
      var3.x = Math.fma(this.x, var1.x(), var2.x());
      var3.y = Math.fma(this.y, var1.y(), var2.y());
      var3.z = Math.fma(this.z, var1.z(), var2.z());
      return var3;
   }

   public Vector4d mulAdd(double var1, Vector4dc var3, Vector4d var4) {
      var4.x = Math.fma(this.x, var1, var3.x());
      var4.y = Math.fma(this.y, var1, var3.y());
      var4.z = Math.fma(this.z, var1, var3.z());
      return var4;
   }

   public Vector4d mul(Vector4dc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      this.w *= var1.w();
      return this;
   }

   public Vector4d mul(Vector4dc var1, Vector4d var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      var2.w = this.w * var1.w();
      return var2;
   }

   public Vector4d div(Vector4dc var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      this.z /= var1.z();
      this.w /= var1.w();
      return this;
   }

   public Vector4d div(Vector4dc var1, Vector4d var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      var2.z = this.z / var1.z();
      var2.w = this.w / var1.w();
      return var2;
   }

   public Vector4d mul(Vector4fc var1) {
      this.x *= (double)var1.x();
      this.y *= (double)var1.y();
      this.z *= (double)var1.z();
      this.w *= (double)var1.w();
      return this;
   }

   public Vector4d mul(Vector4fc var1, Vector4d var2) {
      var2.x = this.x * (double)var1.x();
      var2.y = this.y * (double)var1.y();
      var2.z = this.z * (double)var1.z();
      var2.w = this.w * (double)var1.w();
      return var2;
   }

   public Vector4d mul(Matrix4dc var1) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, this) : this.mulGeneric(var1, this);
   }

   public Vector4d mul(Matrix4dc var1, Vector4d var2) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, var2) : this.mulGeneric(var1, var2);
   }

   public Vector4d mulTranspose(Matrix4dc var1) {
      return (var1.properties() & 2) != 0 ? this.mulAffineTranspose(var1, this) : this.mulGenericTranspose(var1, this);
   }

   public Vector4d mulTranspose(Matrix4dc var1, Vector4d var2) {
      return (var1.properties() & 2) != 0 ? this.mulAffineTranspose(var1, var2) : this.mulGenericTranspose(var1, var2);
   }

   public Vector4d mulAffine(Matrix4dc var1, Vector4d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w)));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w)));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = this.w;
      return var2;
   }

   private Vector4d mulGeneric(Matrix4dc var1, Vector4d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w)));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w)));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w)));
      double var9 = Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = var9;
      return var2;
   }

   public Vector4d mulAffineTranspose(Matrix4dc var1, Vector4d var2) {
      double var3 = this.x;
      double var5 = this.y;
      double var7 = this.z;
      double var9 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var5, var1.m02() * var7));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var5, var1.m12() * var7));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var5, var1.m22() * var7));
      var2.w = Math.fma(var1.m30(), var3, Math.fma(var1.m31(), var5, var1.m32() * var7 + var9));
      return var2;
   }

   private Vector4d mulGenericTranspose(Matrix4dc var1, Vector4d var2) {
      double var3 = this.x;
      double var5 = this.y;
      double var7 = this.z;
      double var9 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var5, Math.fma(var1.m02(), var7, var1.m03() * var9)));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var5, Math.fma(var1.m12(), var7, var1.m13() * var9)));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var5, Math.fma(var1.m22(), var7, var1.m23() * var9)));
      var2.w = Math.fma(var1.m30(), var3, Math.fma(var1.m31(), var5, Math.fma(var1.m32(), var7, var1.m33() * var9)));
      return var2;
   }

   public Vector4d mul(Matrix4x3dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w)));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w)));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w)));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector4d mul(Matrix4x3dc var1, Vector4d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w)));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w)));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = this.w;
      return var2;
   }

   public Vector4d mul(Matrix4x3fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30() * this.w)));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31() * this.w)));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32() * this.w)));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector4d mul(Matrix4x3fc var1, Vector4d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30() * this.w)));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31() * this.w)));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = this.w;
      return var2;
   }

   public Vector4d mul(Matrix4fc var1) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, this) : this.mulGeneric(var1, this);
   }

   public Vector4d mul(Matrix4fc var1, Vector4d var2) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, var2) : this.mulGeneric(var1, var2);
   }

   private Vector4d mulAffine(Matrix4fc var1, Vector4d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30() * this.w)));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31() * this.w)));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = this.w;
      return var2;
   }

   private Vector4d mulGeneric(Matrix4fc var1, Vector4d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30() * this.w)));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31() * this.w)));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32() * this.w)));
      double var9 = Math.fma((double)var1.m03(), this.x, Math.fma((double)var1.m13(), this.y, Math.fma((double)var1.m23(), this.z, (double)var1.m33() * this.w)));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      var2.w = var9;
      return var2;
   }

   public Vector4d mulProject(Matrix4dc var1, Vector4d var2) {
      double var3 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33() * this.w)));
      double var5 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w))) * var3;
      double var7 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w))) * var3;
      double var9 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w))) * var3;
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      var2.w = 1.0D;
      return var2;
   }

   public Vector4d mulProject(Matrix4dc var1) {
      double var2 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33() * this.w)));
      double var4 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w))) * var2;
      double var6 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w))) * var2;
      double var8 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w))) * var2;
      this.x = var4;
      this.y = var6;
      this.z = var8;
      this.w = 1.0D;
      return this;
   }

   public Vector3d mulProject(Matrix4dc var1, Vector3d var2) {
      double var3 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33() * this.w)));
      double var5 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * this.w))) * var3;
      double var7 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * this.w))) * var3;
      double var9 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * this.w))) * var3;
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      return var2;
   }

   public Vector4d mul(double var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4d mul(double var1, Vector4d var3) {
      var3.x = this.x * var1;
      var3.y = this.y * var1;
      var3.z = this.z * var1;
      var3.w = this.w * var1;
      return var3;
   }

   public Vector4d div(double var1) {
      double var3 = 1.0D / var1;
      this.x *= var3;
      this.y *= var3;
      this.z *= var3;
      this.w *= var3;
      return this;
   }

   public Vector4d div(double var1, Vector4d var3) {
      double var4 = 1.0D / var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      var3.z = this.z * var4;
      var3.w = this.w * var4;
      return var3;
   }

   public Vector4d rotate(Quaterniondc var1) {
      var1.transform((Vector4dc)this, (Vector4d)this);
      return this;
   }

   public Vector4d rotate(Quaterniondc var1, Vector4d var2) {
      var1.transform((Vector4dc)this, (Vector4d)var2);
      return var2;
   }

   public Vector4d rotateAxis(double var1, double var3, double var5, double var7) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateX(var3 * var1, this);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateY(var5 * var1, this);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateZ(var7 * var1, this) : this.rotateAxisInternal(var1, var3, var5, var7, this);
      }
   }

   public Vector4d rotateAxis(double var1, double var3, double var5, double var7, Vector4d var9) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateX(var3 * var1, var9);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateY(var5 * var1, var9);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateZ(var7 * var1, var9) : this.rotateAxisInternal(var1, var3, var5, var7, var9);
      }
   }

   private Vector4d rotateAxisInternal(double var1, double var3, double var5, double var7, Vector4d var9) {
      double var10 = var1 * 0.5D;
      double var12 = Math.sin(var10);
      double var14 = var3 * var12;
      double var16 = var5 * var12;
      double var18 = var7 * var12;
      double var20 = Math.cosFromSin(var12, var10);
      double var22 = var20 * var20;
      double var24 = var14 * var14;
      double var26 = var16 * var16;
      double var28 = var18 * var18;
      double var30 = var18 * var20;
      double var32 = var14 * var16;
      double var34 = var14 * var18;
      double var36 = var16 * var20;
      double var38 = var16 * var18;
      double var40 = var14 * var20;
      double var42 = (var22 + var24 - var28 - var26) * this.x + (-var30 + var32 - var30 + var32) * this.y + (var36 + var34 + var34 + var36) * this.z;
      double var44 = (var32 + var30 + var30 + var32) * this.x + (var26 - var28 + var22 - var24) * this.y + (var38 + var38 - var40 - var40) * this.z;
      double var46 = (var34 - var36 + var34 - var36) * this.x + (var38 + var38 + var40 + var40) * this.y + (var28 - var26 - var24 + var22) * this.z;
      var9.x = var42;
      var9.y = var44;
      var9.z = var46;
      return var9;
   }

   public Vector4d rotateX(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.y * var5 - this.z * var3;
      double var9 = this.y * var3 + this.z * var5;
      this.y = var7;
      this.z = var9;
      return this;
   }

   public Vector4d rotateX(double var1, Vector4d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.y * var6 - this.z * var4;
      double var10 = this.y * var4 + this.z * var6;
      var3.x = this.x;
      var3.y = var8;
      var3.z = var10;
      var3.w = this.w;
      return var3;
   }

   public Vector4d rotateY(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.x * var5 + this.z * var3;
      double var9 = -this.x * var3 + this.z * var5;
      this.x = var7;
      this.z = var9;
      return this;
   }

   public Vector4d rotateY(double var1, Vector4d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.x * var6 + this.z * var4;
      double var10 = -this.x * var4 + this.z * var6;
      var3.x = var8;
      var3.y = this.y;
      var3.z = var10;
      var3.w = this.w;
      return var3;
   }

   public Vector4d rotateZ(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.x * var5 - this.y * var3;
      double var9 = this.x * var3 + this.y * var5;
      this.x = var7;
      this.y = var9;
      return this;
   }

   public Vector4d rotateZ(double var1, Vector4d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.x * var6 - this.y * var4;
      double var10 = this.x * var4 + this.y * var6;
      var3.x = var8;
      var3.y = var10;
      var3.z = this.z;
      var3.w = this.w;
      return var3;
   }

   public double lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
   }

   public static double lengthSquared(double var0, double var2, double var4, double var6) {
      return Math.fma(var0, var0, Math.fma(var2, var2, Math.fma(var4, var4, var6 * var6)));
   }

   public double length() {
      return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
   }

   public static double length(double var0, double var2, double var4, double var6) {
      return Math.sqrt(Math.fma(var0, var0, Math.fma(var2, var2, Math.fma(var4, var4, var6 * var6))));
   }

   public Vector4d normalize() {
      double var1 = 1.0D / this.length();
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4d normalize(Vector4d var1) {
      double var2 = 1.0D / this.length();
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Vector4d normalize(double var1) {
      double var3 = 1.0D / this.length() * var1;
      this.x *= var3;
      this.y *= var3;
      this.z *= var3;
      this.w *= var3;
      return this;
   }

   public Vector4d normalize(double var1, Vector4d var3) {
      double var4 = 1.0D / this.length() * var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      var3.z = this.z * var4;
      var3.w = this.w * var4;
      return var3;
   }

   public Vector4d normalize3() {
      double var1 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4d normalize3(Vector4d var1) {
      double var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public double distance(Vector4dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      double var6 = this.z - var1.z();
      double var8 = this.w - var1.w();
      return Math.sqrt(Math.fma(var2, var2, Math.fma(var4, var4, Math.fma(var6, var6, var8 * var8))));
   }

   public double distance(double var1, double var3, double var5, double var7) {
      double var9 = this.x - var1;
      double var11 = this.y - var3;
      double var13 = this.z - var5;
      double var15 = this.w - var7;
      return Math.sqrt(Math.fma(var9, var9, Math.fma(var11, var11, Math.fma(var13, var13, var15 * var15))));
   }

   public double distanceSquared(Vector4dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      double var6 = this.z - var1.z();
      double var8 = this.w - var1.w();
      return Math.fma(var2, var2, Math.fma(var4, var4, Math.fma(var6, var6, var8 * var8)));
   }

   public double distanceSquared(double var1, double var3, double var5, double var7) {
      double var9 = this.x - var1;
      double var11 = this.y - var3;
      double var13 = this.z - var5;
      double var15 = this.w - var7;
      return Math.fma(var9, var9, Math.fma(var11, var11, Math.fma(var13, var13, var15 * var15)));
   }

   public static double distance(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      double var16 = var0 - var8;
      double var18 = var2 - var10;
      double var20 = var4 - var12;
      double var22 = var6 - var14;
      return Math.sqrt(Math.fma(var16, var16, Math.fma(var18, var18, Math.fma(var20, var20, var22 * var22))));
   }

   public static double distanceSquared(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      double var16 = var0 - var8;
      double var18 = var2 - var10;
      double var20 = var4 - var12;
      double var22 = var6 - var14;
      return Math.fma(var16, var16, Math.fma(var18, var18, Math.fma(var20, var20, var22 * var22)));
   }

   public double dot(Vector4dc var1) {
      return Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
   }

   public double dot(double var1, double var3, double var5, double var7) {
      return Math.fma(this.x, var1, Math.fma(this.y, var3, Math.fma(this.z, var5, this.w * var7)));
   }

   public double angleCos(Vector4dc var1) {
      double var2 = Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
      double var4 = Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), Math.fma(var1.z(), var1.z(), var1.w() * var1.w())));
      double var6 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
      return var6 / Math.sqrt(var2 * var4);
   }

   public double angle(Vector4dc var1) {
      double var2 = this.angleCos(var1);
      var2 = var2 < 1.0D ? var2 : 1.0D;
      var2 = var2 > -1.0D ? var2 : -1.0D;
      return Math.acos(var2);
   }

   public Vector4d zero() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = 0.0D;
      return this;
   }

   public Vector4d negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
      return this;
   }

   public Vector4d negate(Vector4d var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      var1.w = -this.w;
      return var1;
   }

   public Vector4d min(Vector4dc var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      this.z = this.z < var1.z() ? this.z : var1.z();
      this.w = this.w < var1.w() ? this.w : var1.w();
      return this;
   }

   public Vector4d min(Vector4dc var1, Vector4d var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      var2.z = this.z < var1.z() ? this.z : var1.z();
      var2.w = this.w < var1.w() ? this.w : var1.w();
      return var2;
   }

   public Vector4d max(Vector4dc var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      this.z = this.z > var1.z() ? this.z : var1.z();
      this.w = this.w > var1.w() ? this.w : var1.w();
      return this;
   }

   public Vector4d max(Vector4dc var1, Vector4d var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      var2.z = this.z > var1.z() ? this.z : var1.z();
      var2.w = this.w > var1.w() ? this.w : var1.w();
      return var2;
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
         Vector4d var2 = (Vector4d)var1;
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

   public boolean equals(Vector4dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector4dc)) {
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

   public Vector4d smoothStep(Vector4dc var1, double var2, Vector4d var4) {
      double var5 = var2 * var2;
      double var7 = var5 * var2;
      var4.x = (this.x + this.x - var1.x() - var1.x()) * var7 + (3.0D * var1.x() - 3.0D * this.x) * var5 + this.x * var2 + this.x;
      var4.y = (this.y + this.y - var1.y() - var1.y()) * var7 + (3.0D * var1.y() - 3.0D * this.y) * var5 + this.y * var2 + this.y;
      var4.z = (this.z + this.z - var1.z() - var1.z()) * var7 + (3.0D * var1.z() - 3.0D * this.z) * var5 + this.z * var2 + this.z;
      var4.w = (this.w + this.w - var1.w() - var1.w()) * var7 + (3.0D * var1.w() - 3.0D * this.w) * var5 + this.w * var2 + this.w;
      return var4;
   }

   public Vector4d hermite(Vector4dc var1, Vector4dc var2, Vector4dc var3, double var4, Vector4d var6) {
      double var7 = var4 * var4;
      double var9 = var7 * var4;
      var6.x = (this.x + this.x - var2.x() - var2.x() + var3.x() + var1.x()) * var9 + (3.0D * var2.x() - 3.0D * this.x - var1.x() - var1.x() - var3.x()) * var7 + this.x * var4 + this.x;
      var6.y = (this.y + this.y - var2.y() - var2.y() + var3.y() + var1.y()) * var9 + (3.0D * var2.y() - 3.0D * this.y - var1.y() - var1.y() - var3.y()) * var7 + this.y * var4 + this.y;
      var6.z = (this.z + this.z - var2.z() - var2.z() + var3.z() + var1.z()) * var9 + (3.0D * var2.z() - 3.0D * this.z - var1.z() - var1.z() - var3.z()) * var7 + this.z * var4 + this.z;
      var6.w = (this.w + this.w - var2.w() - var2.w() + var3.w() + var1.w()) * var9 + (3.0D * var2.w() - 3.0D * this.w - var1.w() - var1.w() - var3.w()) * var7 + this.w * var4 + this.w;
      return var6;
   }

   public Vector4d lerp(Vector4dc var1, double var2) {
      this.x = Math.fma(var1.x() - this.x, var2, this.x);
      this.y = Math.fma(var1.y() - this.y, var2, this.y);
      this.z = Math.fma(var1.z() - this.z, var2, this.z);
      this.w = Math.fma(var1.w() - this.w, var2, this.w);
      return this;
   }

   public Vector4d lerp(Vector4dc var1, double var2, Vector4d var4) {
      var4.x = Math.fma(var1.x() - this.x, var2, this.x);
      var4.y = Math.fma(var1.y() - this.y, var2, this.y);
      var4.z = Math.fma(var1.z() - this.z, var2, this.z);
      var4.w = Math.fma(var1.w() - this.w, var2, this.w);
      return var4;
   }

   public double get(int var1) throws IllegalArgumentException {
      switch(var1) {
      case 0:
         return this.x;
      case 1:
         return this.y;
      case 2:
         return this.z;
      case 3:
         return this.w;
      default:
         throw new IllegalArgumentException();
      }
   }

   public Vector4i get(int var1, Vector4i var2) {
      var2.x = Math.roundUsing(this.x(), var1);
      var2.y = Math.roundUsing(this.y(), var1);
      var2.z = Math.roundUsing(this.z(), var1);
      var2.w = Math.roundUsing(this.w(), var1);
      return var2;
   }

   public Vector4f get(Vector4f var1) {
      var1.x = (float)this.x();
      var1.y = (float)this.y();
      var1.z = (float)this.z();
      var1.w = (float)this.w();
      return var1;
   }

   public Vector4d get(Vector4d var1) {
      var1.x = this.x();
      var1.y = this.y();
      var1.z = this.z();
      var1.w = this.w();
      return var1;
   }

   public int maxComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      double var5 = Math.abs(this.z);
      double var7 = Math.abs(this.w);
      if (var1 >= var3 && var1 >= var5 && var1 >= var7) {
         return 0;
      } else if (var3 >= var5 && var3 >= var7) {
         return 1;
      } else {
         return var5 >= var7 ? 2 : 3;
      }
   }

   public int minComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      double var5 = Math.abs(this.z);
      double var7 = Math.abs(this.w);
      if (var1 < var3 && var1 < var5 && var1 < var7) {
         return 0;
      } else if (var3 < var5 && var3 < var7) {
         return 1;
      } else {
         return var5 < var7 ? 2 : 3;
      }
   }

   public Vector4d floor() {
      this.x = Math.floor(this.x);
      this.y = Math.floor(this.y);
      this.z = Math.floor(this.z);
      this.w = Math.floor(this.w);
      return this;
   }

   public Vector4d floor(Vector4d var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      var1.z = Math.floor(this.z);
      var1.w = Math.floor(this.w);
      return var1;
   }

   public Vector4d ceil() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      this.z = Math.ceil(this.z);
      this.w = Math.ceil(this.w);
      return this;
   }

   public Vector4d ceil(Vector4d var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      var1.z = Math.ceil(this.z);
      var1.w = Math.ceil(this.w);
      return var1;
   }

   public Vector4d round() {
      this.x = (double)Math.round(this.x);
      this.y = (double)Math.round(this.y);
      this.z = (double)Math.round(this.z);
      this.w = (double)Math.round(this.w);
      return this;
   }

   public Vector4d round(Vector4d var1) {
      var1.x = (double)Math.round(this.x);
      var1.y = (double)Math.round(this.y);
      var1.z = (double)Math.round(this.z);
      var1.w = (double)Math.round(this.w);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
   }

   public Vector4d absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      this.w = Math.abs(this.w);
      return this;
   }

   public Vector4d absolute(Vector4d var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      var1.w = Math.abs(this.w);
      return var1;
   }
}
