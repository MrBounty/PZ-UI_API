package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Vector3d implements Externalizable, Vector3dc {
   private static final long serialVersionUID = 1L;
   public double x;
   public double y;
   public double z;

   public Vector3d() {
   }

   public Vector3d(double var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
   }

   public Vector3d(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public Vector3d(Vector3fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
   }

   public Vector3d(Vector3ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
   }

   public Vector3d(Vector2fc var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
   }

   public Vector3d(Vector2ic var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
   }

   public Vector3d(Vector3dc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
   }

   public Vector3d(Vector2dc var1, double var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
   }

   public Vector3d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Vector3d(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
      this.z = (double)var1[2];
   }

   public Vector3d(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3d(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector3d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3d(int var1, DoubleBuffer var2) {
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

   public Vector3d set(Vector3dc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      return this;
   }

   public Vector3d set(Vector3ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      return this;
   }

   public Vector3d set(Vector2dc var1, double var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      return this;
   }

   public Vector3d set(Vector2ic var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      return this;
   }

   public Vector3d set(Vector3fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = (double)var1.z();
      return this;
   }

   public Vector3d set(Vector2fc var1, double var2) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      this.z = var2;
      return this;
   }

   public Vector3d set(double var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      return this;
   }

   public Vector3d set(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      return this;
   }

   public Vector3d set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      return this;
   }

   public Vector3d set(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
      this.z = (double)var1[2];
      return this;
   }

   public Vector3d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3d set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3d set(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Vector3d setComponent(int var1, double var2) throws IllegalArgumentException {
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

   public Vector3dc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector3d sub(Vector3dc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      return this;
   }

   public Vector3d sub(Vector3dc var1, Vector3d var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      return var2;
   }

   public Vector3d sub(Vector3fc var1) {
      this.x -= (double)var1.x();
      this.y -= (double)var1.y();
      this.z -= (double)var1.z();
      return this;
   }

   public Vector3d sub(Vector3fc var1, Vector3d var2) {
      var2.x = this.x - (double)var1.x();
      var2.y = this.y - (double)var1.y();
      var2.z = this.z - (double)var1.z();
      return var2;
   }

   public Vector3d sub(double var1, double var3, double var5) {
      this.x -= var1;
      this.y -= var3;
      this.z -= var5;
      return this;
   }

   public Vector3d sub(double var1, double var3, double var5, Vector3d var7) {
      var7.x = this.x - var1;
      var7.y = this.y - var3;
      var7.z = this.z - var5;
      return var7;
   }

   public Vector3d add(Vector3dc var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      return this;
   }

   public Vector3d add(Vector3dc var1, Vector3d var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      return var2;
   }

   public Vector3d add(Vector3fc var1) {
      this.x += (double)var1.x();
      this.y += (double)var1.y();
      this.z += (double)var1.z();
      return this;
   }

   public Vector3d add(Vector3fc var1, Vector3d var2) {
      var2.x = this.x + (double)var1.x();
      var2.y = this.y + (double)var1.y();
      var2.z = this.z + (double)var1.z();
      return var2;
   }

   public Vector3d add(double var1, double var3, double var5) {
      this.x += var1;
      this.y += var3;
      this.z += var5;
      return this;
   }

   public Vector3d add(double var1, double var3, double var5, Vector3d var7) {
      var7.x = this.x + var1;
      var7.y = this.y + var3;
      var7.z = this.z + var5;
      return var7;
   }

   public Vector3d fma(Vector3dc var1, Vector3dc var2) {
      this.x = Math.fma(var1.x(), var2.x(), this.x);
      this.y = Math.fma(var1.y(), var2.y(), this.y);
      this.z = Math.fma(var1.z(), var2.z(), this.z);
      return this;
   }

   public Vector3d fma(double var1, Vector3dc var3) {
      this.x = Math.fma(var1, var3.x(), this.x);
      this.y = Math.fma(var1, var3.y(), this.y);
      this.z = Math.fma(var1, var3.z(), this.z);
      return this;
   }

   public Vector3d fma(Vector3fc var1, Vector3fc var2) {
      this.x = Math.fma((double)var1.x(), (double)var2.x(), this.x);
      this.y = Math.fma((double)var1.y(), (double)var2.y(), this.y);
      this.z = Math.fma((double)var1.z(), (double)var2.z(), this.z);
      return this;
   }

   public Vector3d fma(Vector3fc var1, Vector3fc var2, Vector3d var3) {
      var3.x = Math.fma((double)var1.x(), (double)var2.x(), this.x);
      var3.y = Math.fma((double)var1.y(), (double)var2.y(), this.y);
      var3.z = Math.fma((double)var1.z(), (double)var2.z(), this.z);
      return var3;
   }

   public Vector3d fma(double var1, Vector3fc var3) {
      this.x = Math.fma(var1, (double)var3.x(), this.x);
      this.y = Math.fma(var1, (double)var3.y(), this.y);
      this.z = Math.fma(var1, (double)var3.z(), this.z);
      return this;
   }

   public Vector3d fma(Vector3dc var1, Vector3dc var2, Vector3d var3) {
      var3.x = Math.fma(var1.x(), var2.x(), this.x);
      var3.y = Math.fma(var1.y(), var2.y(), this.y);
      var3.z = Math.fma(var1.z(), var2.z(), this.z);
      return var3;
   }

   public Vector3d fma(double var1, Vector3dc var3, Vector3d var4) {
      var4.x = Math.fma(var1, var3.x(), this.x);
      var4.y = Math.fma(var1, var3.y(), this.y);
      var4.z = Math.fma(var1, var3.z(), this.z);
      return var4;
   }

   public Vector3d fma(Vector3dc var1, Vector3fc var2, Vector3d var3) {
      var3.x = Math.fma(var1.x(), (double)var2.x(), this.x);
      var3.y = Math.fma(var1.y(), (double)var2.y(), this.y);
      var3.z = Math.fma(var1.z(), (double)var2.z(), this.z);
      return var3;
   }

   public Vector3d fma(double var1, Vector3fc var3, Vector3d var4) {
      var4.x = Math.fma(var1, (double)var3.x(), this.x);
      var4.y = Math.fma(var1, (double)var3.y(), this.y);
      var4.z = Math.fma(var1, (double)var3.z(), this.z);
      return var4;
   }

   public Vector3d mulAdd(Vector3dc var1, Vector3dc var2) {
      this.x = Math.fma(this.x, var1.x(), var2.x());
      this.y = Math.fma(this.y, var1.y(), var2.y());
      this.z = Math.fma(this.z, var1.z(), var2.z());
      return this;
   }

   public Vector3d mulAdd(double var1, Vector3dc var3) {
      this.x = Math.fma(this.x, var1, var3.x());
      this.y = Math.fma(this.y, var1, var3.y());
      this.z = Math.fma(this.z, var1, var3.z());
      return this;
   }

   public Vector3d mulAdd(Vector3dc var1, Vector3dc var2, Vector3d var3) {
      var3.x = Math.fma(this.x, var1.x(), var2.x());
      var3.y = Math.fma(this.y, var1.y(), var2.y());
      var3.z = Math.fma(this.z, var1.z(), var2.z());
      return var3;
   }

   public Vector3d mulAdd(double var1, Vector3dc var3, Vector3d var4) {
      var4.x = Math.fma(this.x, var1, var3.x());
      var4.y = Math.fma(this.y, var1, var3.y());
      var4.z = Math.fma(this.z, var1, var3.z());
      return var4;
   }

   public Vector3d mulAdd(Vector3fc var1, Vector3dc var2, Vector3d var3) {
      var3.x = Math.fma(this.x, (double)var1.x(), var2.x());
      var3.y = Math.fma(this.y, (double)var1.y(), var2.y());
      var3.z = Math.fma(this.z, (double)var1.z(), var2.z());
      return var3;
   }

   public Vector3d mul(Vector3dc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      return this;
   }

   public Vector3d mul(Vector3fc var1) {
      this.x *= (double)var1.x();
      this.y *= (double)var1.y();
      this.z *= (double)var1.z();
      return this;
   }

   public Vector3d mul(Vector3fc var1, Vector3d var2) {
      var2.x = this.x * (double)var1.x();
      var2.y = this.y * (double)var1.y();
      var2.z = this.z * (double)var1.z();
      return var2;
   }

   public Vector3d mul(Vector3dc var1, Vector3d var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      return var2;
   }

   public Vector3d div(Vector3d var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      this.z /= var1.z();
      return this;
   }

   public Vector3d div(Vector3fc var1) {
      this.x /= (double)var1.x();
      this.y /= (double)var1.y();
      this.z /= (double)var1.z();
      return this;
   }

   public Vector3d div(Vector3fc var1, Vector3d var2) {
      var2.x = this.x / (double)var1.x();
      var2.y = this.y / (double)var1.y();
      var2.z = this.z / (double)var1.z();
      return var2;
   }

   public Vector3d div(Vector3dc var1, Vector3d var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      var2.z = this.z / var1.z();
      return var2;
   }

   public Vector3d mulProject(Matrix4dc var1, double var2, Vector3d var4) {
      double var5 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33() * var2)));
      double var7 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30() * var2))) * var5;
      double var9 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31() * var2))) * var5;
      double var11 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32() * var2))) * var5;
      var4.x = var7;
      var4.y = var9;
      var4.z = var11;
      return var4;
   }

   public Vector3d mulProject(Matrix4dc var1, Vector3d var2) {
      double var3 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33())));
      double var5 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30()))) * var3;
      double var7 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31()))) * var3;
      double var9 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32()))) * var3;
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      return var2;
   }

   public Vector3d mulProject(Matrix4dc var1) {
      double var2 = 1.0D / Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33())));
      double var4 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30()))) * var2;
      double var6 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31()))) * var2;
      double var8 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32()))) * var2;
      this.x = var4;
      this.y = var6;
      this.z = var8;
      return this;
   }

   public Vector3d mulProject(Matrix4fc var1, Vector3d var2) {
      double var3 = 1.0D / Math.fma((double)var1.m03(), this.x, Math.fma((double)var1.m13(), this.y, Math.fma((double)var1.m23(), this.z, (double)var1.m33())));
      double var5 = ((double)var1.m00() * this.x + (double)var1.m10() * this.y + (double)var1.m20() * this.z + (double)var1.m30()) * var3;
      double var7 = ((double)var1.m01() * this.x + (double)var1.m11() * this.y + (double)var1.m21() * this.z + (double)var1.m31()) * var3;
      double var9 = ((double)var1.m02() * this.x + (double)var1.m12() * this.y + (double)var1.m22() * this.z + (double)var1.m32()) * var3;
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      return var2;
   }

   public Vector3d mulProject(Matrix4fc var1) {
      double var2 = 1.0D / Math.fma((double)var1.m03(), this.x, Math.fma((double)var1.m13(), this.y, Math.fma((double)var1.m23(), this.z, (double)var1.m33())));
      double var4 = ((double)var1.m00() * this.x + (double)var1.m10() * this.y + (double)var1.m20() * this.z + (double)var1.m30()) * var2;
      double var6 = ((double)var1.m01() * this.x + (double)var1.m11() * this.y + (double)var1.m21() * this.z + (double)var1.m31()) * var2;
      double var8 = ((double)var1.m02() * this.x + (double)var1.m12() * this.y + (double)var1.m22() * this.z + (double)var1.m32()) * var2;
      this.x = var4;
      this.y = var6;
      this.z = var8;
      return this;
   }

   public Vector3d mul(Matrix3fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mul(Matrix3dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mul(Matrix3dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3f mul(Matrix3dc var1, Vector3f var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      var2.x = (float)var3;
      var2.y = (float)var5;
      var2.z = (float)var7;
      return var2;
   }

   public Vector3d mul(Matrix3fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mul(Matrix3x2dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector3d mul(Matrix3x2dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = this.z;
      return var2;
   }

   public Vector3d mul(Matrix3x2fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector3d mul(Matrix3x2fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = this.z;
      return var2;
   }

   public Vector3d mulTranspose(Matrix3dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, var1.m02() * this.z));
      double var4 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, var1.m12() * this.z));
      double var6 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTranspose(Matrix3dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, var1.m02() * this.z));
      double var5 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, var1.m12() * this.z));
      double var7 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulTranspose(Matrix3fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, (double)var1.m02() * this.z));
      double var4 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m12() * this.z));
      double var6 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, (double)var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTranspose(Matrix3fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, (double)var1.m02() * this.z));
      double var5 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m12() * this.z));
      double var7 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, (double)var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulPosition(Matrix4fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulPosition(Matrix4dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulPosition(Matrix4x3dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulPosition(Matrix4x3fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulPosition(Matrix4dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulPosition(Matrix4fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulPosition(Matrix4x3dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulPosition(Matrix4x3fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulTransposePosition(Matrix4dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, Math.fma(var1.m02(), this.z, var1.m03())));
      double var4 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m12(), this.z, var1.m13())));
      double var6 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, Math.fma(var1.m22(), this.z, var1.m23())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTransposePosition(Matrix4dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, Math.fma(var1.m02(), this.z, var1.m03())));
      double var5 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m12(), this.z, var1.m13())));
      double var7 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, Math.fma(var1.m22(), this.z, var1.m23())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulTransposePosition(Matrix4fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, Math.fma((double)var1.m02(), this.z, (double)var1.m03())));
      double var4 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m12(), this.z, (double)var1.m13())));
      double var6 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m23())));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTransposePosition(Matrix4fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, Math.fma((double)var1.m02(), this.z, (double)var1.m03())));
      double var5 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m12(), this.z, (double)var1.m13())));
      double var7 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m23())));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public double mulPositionW(Matrix4fc var1) {
      double var2 = Math.fma((double)var1.m03(), this.x, Math.fma((double)var1.m13(), this.y, Math.fma((double)var1.m23(), this.z, (double)var1.m33())));
      double var4 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var6 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var8 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      this.x = var4;
      this.y = var6;
      this.z = var8;
      return var2;
   }

   public double mulPositionW(Matrix4fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m03(), this.x, Math.fma((double)var1.m13(), this.y, Math.fma((double)var1.m23(), this.z, (double)var1.m33())));
      double var5 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, Math.fma((double)var1.m20(), this.z, (double)var1.m30())));
      double var7 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, Math.fma((double)var1.m21(), this.z, (double)var1.m31())));
      double var9 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, Math.fma((double)var1.m22(), this.z, (double)var1.m32())));
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      return var3;
   }

   public double mulPositionW(Matrix4dc var1) {
      double var2 = Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33())));
      double var4 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var6 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var8 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      this.x = var4;
      this.y = var6;
      this.z = var8;
      return var2;
   }

   public double mulPositionW(Matrix4dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m03(), this.x, Math.fma(var1.m13(), this.y, Math.fma(var1.m23(), this.z, var1.m33())));
      double var5 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, Math.fma(var1.m20(), this.z, var1.m30())));
      double var7 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, Math.fma(var1.m21(), this.z, var1.m31())));
      double var9 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, Math.fma(var1.m22(), this.z, var1.m32())));
      var2.x = var5;
      var2.y = var7;
      var2.z = var9;
      return var3;
   }

   public Vector3d mulDirection(Matrix4fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulDirection(Matrix4dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulDirection(Matrix4x3dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var4 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var6 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulDirection(Matrix4x3fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var4 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var6 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulDirection(Matrix4dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulDirection(Matrix4fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulDirection(Matrix4x3dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m10(), this.y, var1.m20() * this.z));
      double var5 = Math.fma(var1.m01(), this.x, Math.fma(var1.m11(), this.y, var1.m21() * this.z));
      double var7 = Math.fma(var1.m02(), this.x, Math.fma(var1.m12(), this.y, var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulDirection(Matrix4x3fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m10(), this.y, (double)var1.m20() * this.z));
      double var5 = Math.fma((double)var1.m01(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m21() * this.z));
      double var7 = Math.fma((double)var1.m02(), this.x, Math.fma((double)var1.m12(), this.y, (double)var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulTransposeDirection(Matrix4dc var1) {
      double var2 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, var1.m02() * this.z));
      double var4 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, var1.m12() * this.z));
      double var6 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTransposeDirection(Matrix4dc var1, Vector3d var2) {
      double var3 = Math.fma(var1.m00(), this.x, Math.fma(var1.m01(), this.y, var1.m02() * this.z));
      double var5 = Math.fma(var1.m10(), this.x, Math.fma(var1.m11(), this.y, var1.m12() * this.z));
      double var7 = Math.fma(var1.m20(), this.x, Math.fma(var1.m21(), this.y, var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mulTransposeDirection(Matrix4fc var1) {
      double var2 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, (double)var1.m02() * this.z));
      double var4 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m12() * this.z));
      double var6 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, (double)var1.m22() * this.z));
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d mulTransposeDirection(Matrix4fc var1, Vector3d var2) {
      double var3 = Math.fma((double)var1.m00(), this.x, Math.fma((double)var1.m01(), this.y, (double)var1.m02() * this.z));
      double var5 = Math.fma((double)var1.m10(), this.x, Math.fma((double)var1.m11(), this.y, (double)var1.m12() * this.z));
      double var7 = Math.fma((double)var1.m20(), this.x, Math.fma((double)var1.m21(), this.y, (double)var1.m22() * this.z));
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d mul(double var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3d mul(double var1, Vector3d var3) {
      var3.x = this.x * var1;
      var3.y = this.y * var1;
      var3.z = this.z * var1;
      return var3;
   }

   public Vector3d mul(double var1, double var3, double var5) {
      this.x *= var1;
      this.y *= var3;
      this.z *= var5;
      return this;
   }

   public Vector3d mul(double var1, double var3, double var5, Vector3d var7) {
      var7.x = this.x * var1;
      var7.y = this.y * var3;
      var7.z = this.z * var5;
      return var7;
   }

   public Vector3d rotate(Quaterniondc var1) {
      return var1.transform((Vector3dc)this, (Vector3d)this);
   }

   public Vector3d rotate(Quaterniondc var1, Vector3d var2) {
      return var1.transform((Vector3dc)this, (Vector3d)var2);
   }

   public Quaterniond rotationTo(Vector3dc var1, Quaterniond var2) {
      return var2.rotationTo(this, var1);
   }

   public Quaterniond rotationTo(double var1, double var3, double var5, Quaterniond var7) {
      return var7.rotationTo(this.x, this.y, this.z, var1, var3, var5);
   }

   public Vector3d rotateAxis(double var1, double var3, double var5, double var7) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateX(var3 * var1, this);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateY(var5 * var1, this);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateZ(var7 * var1, this) : this.rotateAxisInternal(var1, var3, var5, var7, this);
      }
   }

   public Vector3d rotateAxis(double var1, double var3, double var5, double var7, Vector3d var9) {
      if (var5 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var3)) {
         return this.rotateX(var3 * var1, var9);
      } else if (var3 == 0.0D && var7 == 0.0D && Math.absEqualsOne(var5)) {
         return this.rotateY(var5 * var1, var9);
      } else {
         return var3 == 0.0D && var5 == 0.0D && Math.absEqualsOne(var7) ? this.rotateZ(var7 * var1, var9) : this.rotateAxisInternal(var1, var3, var5, var7, var9);
      }
   }

   private Vector3d rotateAxisInternal(double var1, double var3, double var5, double var7, Vector3d var9) {
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

   public Vector3d rotateX(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.y * var5 - this.z * var3;
      double var9 = this.y * var3 + this.z * var5;
      this.y = var7;
      this.z = var9;
      return this;
   }

   public Vector3d rotateX(double var1, Vector3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.y * var6 - this.z * var4;
      double var10 = this.y * var4 + this.z * var6;
      var3.x = this.x;
      var3.y = var8;
      var3.z = var10;
      return var3;
   }

   public Vector3d rotateY(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.x * var5 + this.z * var3;
      double var9 = -this.x * var3 + this.z * var5;
      this.x = var7;
      this.z = var9;
      return this;
   }

   public Vector3d rotateY(double var1, Vector3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.x * var6 + this.z * var4;
      double var10 = -this.x * var4 + this.z * var6;
      var3.x = var8;
      var3.y = this.y;
      var3.z = var10;
      return var3;
   }

   public Vector3d rotateZ(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      double var7 = this.x * var5 - this.y * var3;
      double var9 = this.x * var3 + this.y * var5;
      this.x = var7;
      this.y = var9;
      return this;
   }

   public Vector3d rotateZ(double var1, Vector3d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.x * var6 - this.y * var4;
      double var10 = this.x * var4 + this.y * var6;
      var3.x = var8;
      var3.y = var10;
      var3.z = this.z;
      return var3;
   }

   public Vector3d div(double var1) {
      double var3 = 1.0D / var1;
      this.x *= var3;
      this.y *= var3;
      this.z *= var3;
      return this;
   }

   public Vector3d div(double var1, Vector3d var3) {
      double var4 = 1.0D / var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      var3.z = this.z * var4;
      return var3;
   }

   public Vector3d div(double var1, double var3, double var5) {
      this.x /= var1;
      this.y /= var3;
      this.z /= var5;
      return this;
   }

   public Vector3d div(double var1, double var3, double var5, Vector3d var7) {
      var7.x = this.x / var1;
      var7.y = this.y / var3;
      var7.z = this.z / var5;
      return var7;
   }

   public double lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
   }

   public static double lengthSquared(double var0, double var2, double var4) {
      return Math.fma(var0, var0, Math.fma(var2, var2, var4 * var4));
   }

   public double length() {
      return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
   }

   public static double length(double var0, double var2, double var4) {
      return Math.sqrt(Math.fma(var0, var0, Math.fma(var2, var2, var4 * var4)));
   }

   public Vector3d normalize() {
      double var1 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3d normalize(Vector3d var1) {
      double var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      return var1;
   }

   public Vector3d normalize(double var1) {
      double var3 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * var1;
      this.x *= var3;
      this.y *= var3;
      this.z *= var3;
      return this;
   }

   public Vector3d normalize(double var1, Vector3d var3) {
      double var4 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      var3.z = this.z * var4;
      return var3;
   }

   public Vector3d cross(Vector3dc var1) {
      double var2 = Math.fma(this.y, var1.z(), -this.z * var1.y());
      double var4 = Math.fma(this.z, var1.x(), -this.x * var1.z());
      double var6 = Math.fma(this.x, var1.y(), -this.y * var1.x());
      this.x = var2;
      this.y = var4;
      this.z = var6;
      return this;
   }

   public Vector3d cross(double var1, double var3, double var5) {
      double var7 = Math.fma(this.y, var5, -this.z * var3);
      double var9 = Math.fma(this.z, var1, -this.x * var5);
      double var11 = Math.fma(this.x, var3, -this.y * var1);
      this.x = var7;
      this.y = var9;
      this.z = var11;
      return this;
   }

   public Vector3d cross(Vector3dc var1, Vector3d var2) {
      double var3 = Math.fma(this.y, var1.z(), -this.z * var1.y());
      double var5 = Math.fma(this.z, var1.x(), -this.x * var1.z());
      double var7 = Math.fma(this.x, var1.y(), -this.y * var1.x());
      var2.x = var3;
      var2.y = var5;
      var2.z = var7;
      return var2;
   }

   public Vector3d cross(double var1, double var3, double var5, Vector3d var7) {
      double var8 = Math.fma(this.y, var5, -this.z * var3);
      double var10 = Math.fma(this.z, var1, -this.x * var5);
      double var12 = Math.fma(this.x, var3, -this.y * var1);
      var7.x = var8;
      var7.y = var10;
      var7.z = var12;
      return var7;
   }

   public double distance(Vector3dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      double var6 = this.z - var1.z();
      return Math.sqrt(Math.fma(var2, var2, Math.fma(var4, var4, var6 * var6)));
   }

   public double distance(double var1, double var3, double var5) {
      double var7 = this.x - var1;
      double var9 = this.y - var3;
      double var11 = this.z - var5;
      return Math.sqrt(Math.fma(var7, var7, Math.fma(var9, var9, var11 * var11)));
   }

   public double distanceSquared(Vector3dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      double var6 = this.z - var1.z();
      return Math.fma(var2, var2, Math.fma(var4, var4, var6 * var6));
   }

   public double distanceSquared(double var1, double var3, double var5) {
      double var7 = this.x - var1;
      double var9 = this.y - var3;
      double var11 = this.z - var5;
      return Math.fma(var7, var7, Math.fma(var9, var9, var11 * var11));
   }

   public static double distance(double var0, double var2, double var4, double var6, double var8, double var10) {
      return Math.sqrt(distanceSquared(var0, var2, var4, var6, var8, var10));
   }

   public static double distanceSquared(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return Math.fma(var12, var12, Math.fma(var14, var14, var16 * var16));
   }

   public double dot(Vector3dc var1) {
      return Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), this.z * var1.z()));
   }

   public double dot(double var1, double var3, double var5) {
      return Math.fma(this.x, var1, Math.fma(this.y, var3, this.z * var5));
   }

   public double angleCos(Vector3dc var1) {
      double var2 = Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
      double var4 = Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), var1.z() * var1.z()));
      double var6 = Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), this.z * var1.z()));
      return var6 / Math.sqrt(var2 * var4);
   }

   public double angle(Vector3dc var1) {
      double var2 = this.angleCos(var1);
      var2 = var2 < 1.0D ? var2 : 1.0D;
      var2 = var2 > -1.0D ? var2 : -1.0D;
      return Math.acos(var2);
   }

   public double angleSigned(Vector3dc var1, Vector3dc var2) {
      double var3 = var1.x();
      double var5 = var1.y();
      double var7 = var1.z();
      return Math.atan2((this.y * var7 - this.z * var5) * var2.x() + (this.z * var3 - this.x * var7) * var2.y() + (this.x * var5 - this.y * var3) * var2.z(), this.x * var3 + this.y * var5 + this.z * var7);
   }

   public double angleSigned(double var1, double var3, double var5, double var7, double var9, double var11) {
      return Math.atan2((this.y * var5 - this.z * var3) * var7 + (this.z * var1 - this.x * var5) * var9 + (this.x * var3 - this.y * var1) * var11, this.x * var1 + this.y * var3 + this.z * var5);
   }

   public Vector3d min(Vector3dc var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      this.z = this.z < var1.z() ? this.z : var1.z();
      return this;
   }

   public Vector3d min(Vector3dc var1, Vector3d var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      var2.z = this.z < var1.z() ? this.z : var1.z();
      return var2;
   }

   public Vector3d max(Vector3dc var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      this.z = this.z > var1.z() ? this.z : var1.z();
      return this;
   }

   public Vector3d max(Vector3dc var1, Vector3d var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      var2.z = this.z > var1.z() ? this.z : var1.z();
      return var2;
   }

   public Vector3d zero() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      return this;
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format(this.x, var1);
      return "(" + var10000 + " " + Runtime.format(this.y, var1) + " " + Runtime.format(this.z, var1) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.x);
      var1.writeDouble(this.y);
      var1.writeDouble(this.z);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readDouble();
      this.y = var1.readDouble();
      this.z = var1.readDouble();
   }

   public Vector3d negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3d negate(Vector3d var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      return var1;
   }

   public Vector3d absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      return this;
   }

   public Vector3d absolute(Vector3d var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.x);
      int var4 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
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
         Vector3d var2 = (Vector3d)var1;
         if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var2.x)) {
            return false;
         } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(var2.y)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(var2.z);
         }
      }
   }

   public boolean equals(Vector3dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector3dc)) {
         return false;
      } else if (!Runtime.equals(this.x, var1.x(), var2)) {
         return false;
      } else if (!Runtime.equals(this.y, var1.y(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.z, var1.z(), var2);
      }
   }

   public boolean equals(double var1, double var3, double var5) {
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var1)) {
         return false;
      } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(var3)) {
         return false;
      } else {
         return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(var5);
      }
   }

   public Vector3d reflect(Vector3dc var1) {
      double var2 = var1.x();
      double var4 = var1.y();
      double var6 = var1.z();
      double var8 = Math.fma(this.x, var2, Math.fma(this.y, var4, this.z * var6));
      this.x -= (var8 + var8) * var2;
      this.y -= (var8 + var8) * var4;
      this.z -= (var8 + var8) * var6;
      return this;
   }

   public Vector3d reflect(double var1, double var3, double var5) {
      double var7 = Math.fma(this.x, var1, Math.fma(this.y, var3, this.z * var5));
      this.x -= (var7 + var7) * var1;
      this.y -= (var7 + var7) * var3;
      this.z -= (var7 + var7) * var5;
      return this;
   }

   public Vector3d reflect(Vector3dc var1, Vector3d var2) {
      double var3 = var1.x();
      double var5 = var1.y();
      double var7 = var1.z();
      double var9 = Math.fma(this.x, var3, Math.fma(this.y, var5, this.z * var7));
      var2.x = this.x - (var9 + var9) * var3;
      var2.y = this.y - (var9 + var9) * var5;
      var2.z = this.z - (var9 + var9) * var7;
      return var2;
   }

   public Vector3d reflect(double var1, double var3, double var5, Vector3d var7) {
      double var8 = Math.fma(this.x, var1, Math.fma(this.y, var3, this.z * var5));
      var7.x = this.x - (var8 + var8) * var1;
      var7.y = this.y - (var8 + var8) * var3;
      var7.z = this.z - (var8 + var8) * var5;
      return var7;
   }

   public Vector3d half(Vector3dc var1) {
      return this.set((Vector3dc)this).add(var1.x(), var1.y(), var1.z()).normalize();
   }

   public Vector3d half(double var1, double var3, double var5) {
      return this.set((Vector3dc)this).add(var1, var3, var5).normalize();
   }

   public Vector3d half(Vector3dc var1, Vector3d var2) {
      return var2.set((Vector3dc)this).add(var1.x(), var1.y(), var1.z()).normalize();
   }

   public Vector3d half(double var1, double var3, double var5, Vector3d var7) {
      return var7.set((Vector3dc)this).add(var1, var3, var5).normalize();
   }

   public Vector3d smoothStep(Vector3dc var1, double var2, Vector3d var4) {
      double var5 = var2 * var2;
      double var7 = var5 * var2;
      var4.x = (this.x + this.x - var1.x() - var1.x()) * var7 + (3.0D * var1.x() - 3.0D * this.x) * var5 + this.x * var2 + this.x;
      var4.y = (this.y + this.y - var1.y() - var1.y()) * var7 + (3.0D * var1.y() - 3.0D * this.y) * var5 + this.y * var2 + this.y;
      var4.z = (this.z + this.z - var1.z() - var1.z()) * var7 + (3.0D * var1.z() - 3.0D * this.z) * var5 + this.z * var2 + this.z;
      return var4;
   }

   public Vector3d hermite(Vector3dc var1, Vector3dc var2, Vector3dc var3, double var4, Vector3d var6) {
      double var7 = var4 * var4;
      double var9 = var7 * var4;
      var6.x = (this.x + this.x - var2.x() - var2.x() + var3.x() + var1.x()) * var9 + (3.0D * var2.x() - 3.0D * this.x - var1.x() - var1.x() - var3.x()) * var7 + this.x * var4 + this.x;
      var6.y = (this.y + this.y - var2.y() - var2.y() + var3.y() + var1.y()) * var9 + (3.0D * var2.y() - 3.0D * this.y - var1.y() - var1.y() - var3.y()) * var7 + this.y * var4 + this.y;
      var6.z = (this.z + this.z - var2.z() - var2.z() + var3.z() + var1.z()) * var9 + (3.0D * var2.z() - 3.0D * this.z - var1.z() - var1.z() - var3.z()) * var7 + this.z * var4 + this.z;
      return var6;
   }

   public Vector3d lerp(Vector3dc var1, double var2) {
      this.x = Math.fma(var1.x() - this.x, var2, this.x);
      this.y = Math.fma(var1.y() - this.y, var2, this.y);
      this.z = Math.fma(var1.z() - this.z, var2, this.z);
      return this;
   }

   public Vector3d lerp(Vector3dc var1, double var2, Vector3d var4) {
      var4.x = Math.fma(var1.x() - this.x, var2, this.x);
      var4.y = Math.fma(var1.y() - this.y, var2, this.y);
      var4.z = Math.fma(var1.z() - this.z, var2, this.z);
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
      default:
         throw new IllegalArgumentException();
      }
   }

   public Vector3i get(int var1, Vector3i var2) {
      var2.x = Math.roundUsing(this.x(), var1);
      var2.y = Math.roundUsing(this.y(), var1);
      var2.z = Math.roundUsing(this.z(), var1);
      return var2;
   }

   public Vector3f get(Vector3f var1) {
      var1.x = (float)this.x();
      var1.y = (float)this.y();
      var1.z = (float)this.z();
      return var1;
   }

   public Vector3d get(Vector3d var1) {
      var1.x = this.x();
      var1.y = this.y();
      var1.z = this.z();
      return var1;
   }

   public int maxComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      double var5 = Math.abs(this.z);
      if (var1 >= var3 && var1 >= var5) {
         return 0;
      } else {
         return var3 >= var5 ? 1 : 2;
      }
   }

   public int minComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      double var5 = Math.abs(this.z);
      if (var1 < var3 && var1 < var5) {
         return 0;
      } else {
         return var3 < var5 ? 1 : 2;
      }
   }

   public Vector3d orthogonalize(Vector3dc var1, Vector3d var2) {
      double var3;
      double var5;
      double var7;
      if (Math.abs(var1.x()) > Math.abs(var1.z())) {
         var3 = -var1.y();
         var5 = var1.x();
         var7 = 0.0D;
      } else {
         var3 = 0.0D;
         var5 = -var1.z();
         var7 = var1.y();
      }

      double var9 = Math.invsqrt(var3 * var3 + var5 * var5 + var7 * var7);
      var2.x = var3 * var9;
      var2.y = var5 * var9;
      var2.z = var7 * var9;
      return var2;
   }

   public Vector3d orthogonalize(Vector3dc var1) {
      return this.orthogonalize(var1, this);
   }

   public Vector3d orthogonalizeUnit(Vector3dc var1, Vector3d var2) {
      return this.orthogonalize(var1, var2);
   }

   public Vector3d orthogonalizeUnit(Vector3dc var1) {
      return this.orthogonalizeUnit(var1, this);
   }

   public Vector3d floor() {
      this.x = Math.floor(this.x);
      this.y = Math.floor(this.y);
      this.z = Math.floor(this.z);
      return this;
   }

   public Vector3d floor(Vector3d var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      var1.z = Math.floor(this.z);
      return var1;
   }

   public Vector3d ceil() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      this.z = Math.ceil(this.z);
      return this;
   }

   public Vector3d ceil(Vector3d var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      var1.z = Math.ceil(this.z);
      return var1;
   }

   public Vector3d round() {
      this.x = (double)Math.round(this.x);
      this.y = (double)Math.round(this.y);
      this.z = (double)Math.round(this.z);
      return this;
   }

   public Vector3d round(Vector3d var1) {
      var1.x = (double)Math.round(this.x);
      var1.y = (double)Math.round(this.y);
      var1.z = (double)Math.round(this.z);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z);
   }
}
