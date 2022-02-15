package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Vector3f implements Externalizable, Vector3fc {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;

   public Vector3f() {
   }

   public Vector3f(float var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
   }

   public Vector3f(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector3f(Vector3fc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
   }

   public Vector3f(Vector3ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
   }

   public Vector3f(Vector2fc var1, float var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
   }

   public Vector3f(Vector2ic var1, float var2) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = var2;
   }

   public Vector3f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Vector3f(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3f(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector3f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3f(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
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

   public Vector3f set(Vector3fc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      return this;
   }

   public Vector3f set(Vector3dc var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      return this;
   }

   public Vector3f set(Vector3ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      return this;
   }

   public Vector3f set(Vector2fc var1, float var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      return this;
   }

   public Vector3f set(Vector2dc var1, float var2) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = var2;
      return this;
   }

   public Vector3f set(Vector2ic var1, float var2) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = var2;
      return this;
   }

   public Vector3f set(float var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      return this;
   }

   public Vector3f set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      return this;
   }

   public Vector3f set(double var1) {
      this.x = (float)var1;
      this.y = (float)var1;
      this.z = (float)var1;
      return this;
   }

   public Vector3f set(double var1, double var3, double var5) {
      this.x = (float)var1;
      this.y = (float)var3;
      this.z = (float)var5;
      return this;
   }

   public Vector3f set(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      return this;
   }

   public Vector3f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3f set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3f set(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Vector3f setComponent(int var1, float var2) throws IllegalArgumentException {
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

   public Vector3fc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector3f sub(Vector3fc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      return this;
   }

   public Vector3f sub(Vector3fc var1, Vector3f var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      return var2;
   }

   public Vector3f sub(float var1, float var2, float var3) {
      this.x -= var1;
      this.y -= var2;
      this.z -= var3;
      return this;
   }

   public Vector3f sub(float var1, float var2, float var3, Vector3f var4) {
      var4.x = this.x - var1;
      var4.y = this.y - var2;
      var4.z = this.z - var3;
      return var4;
   }

   public Vector3f add(Vector3fc var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      return this;
   }

   public Vector3f add(Vector3fc var1, Vector3f var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      return var2;
   }

   public Vector3f add(float var1, float var2, float var3) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      return this;
   }

   public Vector3f add(float var1, float var2, float var3, Vector3f var4) {
      var4.x = this.x + var1;
      var4.y = this.y + var2;
      var4.z = this.z + var3;
      return var4;
   }

   public Vector3f fma(Vector3fc var1, Vector3fc var2) {
      this.x = Math.fma(var1.x(), var2.x(), this.x);
      this.y = Math.fma(var1.y(), var2.y(), this.y);
      this.z = Math.fma(var1.z(), var2.z(), this.z);
      return this;
   }

   public Vector3f fma(float var1, Vector3fc var2) {
      this.x = Math.fma(var1, var2.x(), this.x);
      this.y = Math.fma(var1, var2.y(), this.y);
      this.z = Math.fma(var1, var2.z(), this.z);
      return this;
   }

   public Vector3f fma(Vector3fc var1, Vector3fc var2, Vector3f var3) {
      var3.x = Math.fma(var1.x(), var2.x(), this.x);
      var3.y = Math.fma(var1.y(), var2.y(), this.y);
      var3.z = Math.fma(var1.z(), var2.z(), this.z);
      return var3;
   }

   public Vector3f fma(float var1, Vector3fc var2, Vector3f var3) {
      var3.x = Math.fma(var1, var2.x(), this.x);
      var3.y = Math.fma(var1, var2.y(), this.y);
      var3.z = Math.fma(var1, var2.z(), this.z);
      return var3;
   }

   public Vector3f mulAdd(Vector3fc var1, Vector3fc var2) {
      this.x = Math.fma(this.x, var1.x(), var2.x());
      this.y = Math.fma(this.y, var1.y(), var2.y());
      this.z = Math.fma(this.z, var1.z(), var2.z());
      return this;
   }

   public Vector3f mulAdd(float var1, Vector3fc var2) {
      this.x = Math.fma(this.x, var1, var2.x());
      this.y = Math.fma(this.y, var1, var2.y());
      this.z = Math.fma(this.z, var1, var2.z());
      return this;
   }

   public Vector3f mulAdd(Vector3fc var1, Vector3fc var2, Vector3f var3) {
      var3.x = Math.fma(this.x, var1.x(), var2.x());
      var3.y = Math.fma(this.y, var1.y(), var2.y());
      var3.z = Math.fma(this.z, var1.z(), var2.z());
      return var3;
   }

   public Vector3f mulAdd(float var1, Vector3fc var2, Vector3f var3) {
      var3.x = Math.fma(this.x, var1, var2.x());
      var3.y = Math.fma(this.y, var1, var2.y());
      var3.z = Math.fma(this.z, var1, var2.z());
      return var3;
   }

   public Vector3f mul(Vector3fc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      return this;
   }

   public Vector3f mul(Vector3fc var1, Vector3f var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      return var2;
   }

   public Vector3f div(Vector3fc var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      this.z /= var1.z();
      return this;
   }

   public Vector3f div(Vector3fc var1, Vector3f var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      var2.z = this.z / var1.z();
      return var2;
   }

   public Vector3f mulProject(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = 1.0F / Math.fma(var1.m03(), var3, Math.fma(var1.m13(), var4, Math.fma(var1.m23(), var5, var1.m33())));
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30()))) * var6;
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31()))) * var6;
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32()))) * var6;
      return var2;
   }

   public Vector3f mulProject(Matrix4fc var1, float var2, Vector3f var3) {
      float var4 = this.x;
      float var5 = this.y;
      float var6 = this.z;
      float var7 = 1.0F / Math.fma(var1.m03(), var4, Math.fma(var1.m13(), var5, Math.fma(var1.m23(), var6, var1.m33() * var2)));
      var3.x = Math.fma(var1.m00(), var4, Math.fma(var1.m10(), var5, Math.fma(var1.m20(), var6, var1.m30() * var2))) * var7;
      var3.y = Math.fma(var1.m01(), var4, Math.fma(var1.m11(), var5, Math.fma(var1.m21(), var6, var1.m31() * var2))) * var7;
      var3.z = Math.fma(var1.m02(), var4, Math.fma(var1.m12(), var5, Math.fma(var1.m22(), var6, var1.m32() * var2))) * var7;
      return var3;
   }

   public Vector3f mulProject(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = 1.0F / Math.fma(var1.m03(), var2, Math.fma(var1.m13(), var3, Math.fma(var1.m23(), var4, var1.m33())));
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30()))) * var5;
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31()))) * var5;
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32()))) * var5;
      return this;
   }

   public Vector3f mul(Matrix3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, var1.m20() * var4));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, var1.m21() * var4));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, var1.m22() * var4));
      return this;
   }

   public Vector3f mul(Matrix3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, var1.m20() * var5));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, var1.m21() * var5));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, var1.m22() * var5));
      return var2;
   }

   public Vector3f mul(Matrix3dc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = (float)Math.fma(var1.m00(), (double)var2, Math.fma(var1.m10(), (double)var3, var1.m20() * (double)var4));
      this.y = (float)Math.fma(var1.m01(), (double)var2, Math.fma(var1.m11(), (double)var3, var1.m21() * (double)var4));
      this.z = (float)Math.fma(var1.m02(), (double)var2, Math.fma(var1.m12(), (double)var3, var1.m22() * (double)var4));
      return this;
   }

   public Vector3f mul(Matrix3dc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = (float)Math.fma(var1.m00(), (double)var3, Math.fma(var1.m10(), (double)var4, var1.m20() * (double)var5));
      var2.y = (float)Math.fma(var1.m01(), (double)var3, Math.fma(var1.m11(), (double)var4, var1.m21() * (double)var5));
      var2.z = (float)Math.fma(var1.m02(), (double)var3, Math.fma(var1.m12(), (double)var4, var1.m22() * (double)var5));
      return var2;
   }

   public Vector3f mul(Matrix3x2fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, var1.m20() * var4));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, var1.m21() * var4));
      this.z = var4;
      return this;
   }

   public Vector3f mul(Matrix3x2fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, var1.m20() * var5));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, var1.m21() * var5));
      var2.z = var5;
      return var2;
   }

   public Vector3f mulTranspose(Matrix3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m01(), var3, var1.m02() * var4));
      this.y = Math.fma(var1.m10(), var2, Math.fma(var1.m11(), var3, var1.m12() * var4));
      this.z = Math.fma(var1.m20(), var2, Math.fma(var1.m21(), var3, var1.m22() * var4));
      return this;
   }

   public Vector3f mulTranspose(Matrix3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var4, var1.m02() * var5));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var4, var1.m12() * var5));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var4, var1.m22() * var5));
      return var2;
   }

   public Vector3f mulPosition(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30())));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31())));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32())));
      return this;
   }

   public Vector3f mulPosition(Matrix4x3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30())));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31())));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32())));
      return this;
   }

   public Vector3f mulPosition(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30())));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31())));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32())));
      return var2;
   }

   public Vector3f mulPosition(Matrix4x3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30())));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31())));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32())));
      return var2;
   }

   public Vector3f mulTransposePosition(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m01(), var3, Math.fma(var1.m02(), var4, var1.m03())));
      this.y = Math.fma(var1.m10(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m12(), var4, var1.m13())));
      this.z = Math.fma(var1.m20(), var2, Math.fma(var1.m21(), var3, Math.fma(var1.m22(), var4, var1.m23())));
      return this;
   }

   public Vector3f mulTransposePosition(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var4, Math.fma(var1.m02(), var5, var1.m03())));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m12(), var5, var1.m13())));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var4, Math.fma(var1.m22(), var5, var1.m23())));
      return var2;
   }

   public float mulPositionW(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = Math.fma(var1.m03(), var2, Math.fma(var1.m13(), var3, Math.fma(var1.m23(), var4, var1.m33())));
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30())));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31())));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32())));
      return var5;
   }

   public float mulPositionW(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = Math.fma(var1.m03(), var3, Math.fma(var1.m13(), var4, Math.fma(var1.m23(), var5, var1.m33())));
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30())));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31())));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32())));
      return var6;
   }

   public Vector3f mulDirection(Matrix4dc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = (float)Math.fma(var1.m00(), (double)var2, Math.fma(var1.m10(), (double)var3, var1.m20() * (double)var4));
      this.y = (float)Math.fma(var1.m01(), (double)var2, Math.fma(var1.m11(), (double)var3, var1.m21() * (double)var4));
      this.z = (float)Math.fma(var1.m02(), (double)var2, Math.fma(var1.m12(), (double)var3, var1.m22() * (double)var4));
      return this;
   }

   public Vector3f mulDirection(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, var1.m20() * var4));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, var1.m21() * var4));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, var1.m22() * var4));
      return this;
   }

   public Vector3f mulDirection(Matrix4x3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, var1.m20() * var4));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, var1.m21() * var4));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, var1.m22() * var4));
      return this;
   }

   public Vector3f mulDirection(Matrix4dc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = (float)Math.fma(var1.m00(), (double)var3, Math.fma(var1.m10(), (double)var4, var1.m20() * (double)var5));
      var2.y = (float)Math.fma(var1.m01(), (double)var3, Math.fma(var1.m11(), (double)var4, var1.m21() * (double)var5));
      var2.z = (float)Math.fma(var1.m02(), (double)var3, Math.fma(var1.m12(), (double)var4, var1.m22() * (double)var5));
      return var2;
   }

   public Vector3f mulDirection(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, var1.m20() * var5));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, var1.m21() * var5));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, var1.m22() * var5));
      return var2;
   }

   public Vector3f mulDirection(Matrix4x3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, var1.m20() * var5));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, var1.m21() * var5));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, var1.m22() * var5));
      return var2;
   }

   public Vector3f mulTransposeDirection(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m01(), var3, var1.m02() * var4));
      this.y = Math.fma(var1.m10(), var2, Math.fma(var1.m11(), var3, var1.m12() * var4));
      this.z = Math.fma(var1.m20(), var2, Math.fma(var1.m21(), var3, var1.m22() * var4));
      return this;
   }

   public Vector3f mulTransposeDirection(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var4, var1.m02() * var5));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var4, var1.m12() * var5));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var4, var1.m22() * var5));
      return var2;
   }

   public Vector3f mul(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3f mul(float var1, Vector3f var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      var2.z = this.z * var1;
      return var2;
   }

   public Vector3f mul(float var1, float var2, float var3) {
      this.x *= var1;
      this.y *= var2;
      this.z *= var3;
      return this;
   }

   public Vector3f mul(float var1, float var2, float var3, Vector3f var4) {
      var4.x = this.x * var1;
      var4.y = this.y * var2;
      var4.z = this.z * var3;
      return var4;
   }

   public Vector3f div(float var1) {
      float var2 = 1.0F / var1;
      this.x *= var2;
      this.y *= var2;
      this.z *= var2;
      return this;
   }

   public Vector3f div(float var1, Vector3f var2) {
      float var3 = 1.0F / var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      var2.z = this.z * var3;
      return var2;
   }

   public Vector3f div(float var1, float var2, float var3) {
      this.x /= var1;
      this.y /= var2;
      this.z /= var3;
      return this;
   }

   public Vector3f div(float var1, float var2, float var3, Vector3f var4) {
      var4.x = this.x / var1;
      var4.y = this.y / var2;
      var4.z = this.z / var3;
      return var4;
   }

   public Vector3f rotate(Quaternionfc var1) {
      return var1.transform((Vector3fc)this, (Vector3f)this);
   }

   public Vector3f rotate(Quaternionfc var1, Vector3f var2) {
      return var1.transform((Vector3fc)this, (Vector3f)var2);
   }

   public Quaternionf rotationTo(Vector3fc var1, Quaternionf var2) {
      return var2.rotationTo(this, var1);
   }

   public Quaternionf rotationTo(float var1, float var2, float var3, Quaternionf var4) {
      return var4.rotationTo(this.x, this.y, this.z, var1, var2, var3);
   }

   public Vector3f rotateAxis(float var1, float var2, float var3, float var4) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, this);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, this);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, this) : this.rotateAxisInternal(var1, var2, var3, var4, this);
      }
   }

   public Vector3f rotateAxis(float var1, float var2, float var3, float var4, Vector3f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, var5) : this.rotateAxisInternal(var1, var2, var3, var4, var5);
      }
   }

   private Vector3f rotateAxisInternal(float var1, float var2, float var3, float var4, Vector3f var5) {
      float var6 = var1 * 0.5F;
      float var7 = Math.sin(var6);
      float var8 = var2 * var7;
      float var9 = var3 * var7;
      float var10 = var4 * var7;
      float var11 = Math.cosFromSin(var7, var6);
      float var12 = var11 * var11;
      float var13 = var8 * var8;
      float var14 = var9 * var9;
      float var15 = var10 * var10;
      float var16 = var10 * var11;
      float var17 = var8 * var9;
      float var18 = var8 * var10;
      float var19 = var9 * var11;
      float var20 = var9 * var10;
      float var21 = var8 * var11;
      float var22 = this.x;
      float var23 = this.y;
      float var24 = this.z;
      var5.x = (var12 + var13 - var15 - var14) * var22 + (-var16 + var17 - var16 + var17) * var23 + (var19 + var18 + var18 + var19) * var24;
      var5.y = (var17 + var16 + var16 + var17) * var22 + (var14 - var15 + var12 - var13) * var23 + (var20 + var20 - var21 - var21) * var24;
      var5.z = (var18 - var19 + var18 - var19) * var22 + (var20 + var20 + var21 + var21) * var23 + (var15 - var14 - var13 + var12) * var24;
      return var5;
   }

   public Vector3f rotateX(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.y * var3 - this.z * var2;
      float var5 = this.y * var2 + this.z * var3;
      this.y = var4;
      this.z = var5;
      return this;
   }

   public Vector3f rotateX(float var1, Vector3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.y * var4 - this.z * var3;
      float var6 = this.y * var3 + this.z * var4;
      var2.x = this.x;
      var2.y = var5;
      var2.z = var6;
      return var2;
   }

   public Vector3f rotateY(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.x * var3 + this.z * var2;
      float var5 = -this.x * var2 + this.z * var3;
      this.x = var4;
      this.z = var5;
      return this;
   }

   public Vector3f rotateY(float var1, Vector3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.x * var4 + this.z * var3;
      float var6 = -this.x * var3 + this.z * var4;
      var2.x = var5;
      var2.y = this.y;
      var2.z = var6;
      return var2;
   }

   public Vector3f rotateZ(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.x * var3 - this.y * var2;
      float var5 = this.x * var2 + this.y * var3;
      this.x = var4;
      this.y = var5;
      return this;
   }

   public Vector3f rotateZ(float var1, Vector3f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.x * var4 - this.y * var3;
      float var6 = this.x * var3 + this.y * var4;
      var2.x = var5;
      var2.y = var6;
      var2.z = this.z;
      return var2;
   }

   public float lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
   }

   public static float lengthSquared(float var0, float var1, float var2) {
      return Math.fma(var0, var0, Math.fma(var1, var1, var2 * var2));
   }

   public float length() {
      return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
   }

   public static float length(float var0, float var1, float var2) {
      return Math.sqrt(Math.fma(var0, var0, Math.fma(var1, var1, var2 * var2)));
   }

   public Vector3f normalize() {
      float var1 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3f normalize(Vector3f var1) {
      float var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      return var1;
   }

   public Vector3f normalize(float var1) {
      float var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * var1;
      this.x *= var2;
      this.y *= var2;
      this.z *= var2;
      return this;
   }

   public Vector3f normalize(float var1, Vector3f var2) {
      float var3 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      var2.z = this.z * var3;
      return var2;
   }

   public Vector3f cross(Vector3fc var1) {
      float var2 = Math.fma(this.y, var1.z(), -this.z * var1.y());
      float var3 = Math.fma(this.z, var1.x(), -this.x * var1.z());
      float var4 = Math.fma(this.x, var1.y(), -this.y * var1.x());
      this.x = var2;
      this.y = var3;
      this.z = var4;
      return this;
   }

   public Vector3f cross(float var1, float var2, float var3) {
      float var4 = Math.fma(this.y, var3, -this.z * var2);
      float var5 = Math.fma(this.z, var1, -this.x * var3);
      float var6 = Math.fma(this.x, var2, -this.y * var1);
      this.x = var4;
      this.y = var5;
      this.z = var6;
      return this;
   }

   public Vector3f cross(Vector3fc var1, Vector3f var2) {
      float var3 = Math.fma(this.y, var1.z(), -this.z * var1.y());
      float var4 = Math.fma(this.z, var1.x(), -this.x * var1.z());
      float var5 = Math.fma(this.x, var1.y(), -this.y * var1.x());
      var2.x = var3;
      var2.y = var4;
      var2.z = var5;
      return var2;
   }

   public Vector3f cross(float var1, float var2, float var3, Vector3f var4) {
      float var5 = Math.fma(this.y, var3, -this.z * var2);
      float var6 = Math.fma(this.z, var1, -this.x * var3);
      float var7 = Math.fma(this.x, var2, -this.y * var1);
      var4.x = var5;
      var4.y = var6;
      var4.z = var7;
      return var4;
   }

   public float distance(Vector3fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      float var4 = this.z - var1.z();
      return Math.sqrt(Math.fma(var2, var2, Math.fma(var3, var3, var4 * var4)));
   }

   public float distance(float var1, float var2, float var3) {
      float var4 = this.x - var1;
      float var5 = this.y - var2;
      float var6 = this.z - var3;
      return Math.sqrt(Math.fma(var4, var4, Math.fma(var5, var5, var6 * var6)));
   }

   public float distanceSquared(Vector3fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      float var4 = this.z - var1.z();
      return Math.fma(var2, var2, Math.fma(var3, var3, var4 * var4));
   }

   public float distanceSquared(float var1, float var2, float var3) {
      float var4 = this.x - var1;
      float var5 = this.y - var2;
      float var6 = this.z - var3;
      return Math.fma(var4, var4, Math.fma(var5, var5, var6 * var6));
   }

   public static float distance(float var0, float var1, float var2, float var3, float var4, float var5) {
      return Math.sqrt(distanceSquared(var0, var1, var2, var3, var4, var5));
   }

   public static float distanceSquared(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = var0 - var3;
      float var7 = var1 - var4;
      float var8 = var2 - var5;
      return Math.fma(var6, var6, Math.fma(var7, var7, var8 * var8));
   }

   public float dot(Vector3fc var1) {
      return Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), this.z * var1.z()));
   }

   public float dot(float var1, float var2, float var3) {
      return Math.fma(this.x, var1, Math.fma(this.y, var2, this.z * var3));
   }

   public float angleCos(Vector3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = Math.fma(var2, var2, Math.fma(var3, var3, var4 * var4));
      float var6 = Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), var1.z() * var1.z()));
      float var7 = Math.fma(var2, var1.x(), Math.fma(var3, var1.y(), var4 * var1.z()));
      return var7 / Math.sqrt(var5 * var6);
   }

   public float angle(Vector3fc var1) {
      float var2 = this.angleCos(var1);
      var2 = var2 < 1.0F ? var2 : 1.0F;
      var2 = var2 > -1.0F ? var2 : -1.0F;
      return Math.acos(var2);
   }

   public float angleSigned(Vector3fc var1, Vector3fc var2) {
      return this.angleSigned(var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z());
   }

   public float angleSigned(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = this.x;
      float var8 = this.y;
      float var9 = this.z;
      return Math.atan2((var8 * var3 - var9 * var2) * var4 + (var9 * var1 - var7 * var3) * var5 + (var7 * var2 - var8 * var1) * var6, var7 * var1 + var8 * var2 + var9 * var3);
   }

   public Vector3f min(Vector3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = var2 < var1.x() ? var2 : var1.x();
      this.y = var3 < var1.y() ? var3 : var1.y();
      this.z = var4 < var1.z() ? var4 : var1.z();
      return this;
   }

   public Vector3f min(Vector3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = var3 < var1.x() ? var3 : var1.x();
      var2.y = var4 < var1.y() ? var4 : var1.y();
      var2.z = var5 < var1.z() ? var5 : var1.z();
      return var2;
   }

   public Vector3f max(Vector3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      this.x = var2 > var1.x() ? var2 : var1.x();
      this.y = var3 > var1.y() ? var3 : var1.y();
      this.z = var4 > var1.z() ? var4 : var1.z();
      return this;
   }

   public Vector3f max(Vector3fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      var2.x = var3 > var1.x() ? var3 : var1.x();
      var2.y = var4 > var1.y() ? var4 : var1.y();
      var2.z = var5 > var1.z() ? var5 : var1.z();
      return var2;
   }

   public Vector3f zero() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      return this;
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format((double)this.x, var1);
      return "(" + var10000 + " " + Runtime.format((double)this.y, var1) + " " + Runtime.format((double)this.z, var1) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.x);
      var1.writeFloat(this.y);
      var1.writeFloat(this.z);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.set(var1.readFloat(), var1.readFloat(), var1.readFloat());
   }

   public Vector3f negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3f negate(Vector3f var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      return var1;
   }

   public Vector3f absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      return this;
   }

   public Vector3f absolute(Vector3f var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.x);
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
         Vector3f var2 = (Vector3f)var1;
         if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var2.x)) {
            return false;
         } else if (Float.floatToIntBits(this.y) != Float.floatToIntBits(var2.y)) {
            return false;
         } else {
            return Float.floatToIntBits(this.z) == Float.floatToIntBits(var2.z);
         }
      }
   }

   public boolean equals(Vector3fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector3fc)) {
         return false;
      } else if (!Runtime.equals(this.x, var1.x(), var2)) {
         return false;
      } else if (!Runtime.equals(this.y, var1.y(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.z, var1.z(), var2);
      }
   }

   public boolean equals(float var1, float var2, float var3) {
      if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var1)) {
         return false;
      } else if (Float.floatToIntBits(this.y) != Float.floatToIntBits(var2)) {
         return false;
      } else {
         return Float.floatToIntBits(this.z) == Float.floatToIntBits(var3);
      }
   }

   public Vector3f reflect(Vector3fc var1) {
      float var2 = var1.x();
      float var3 = var1.y();
      float var4 = var1.z();
      float var5 = Math.fma(this.x, var2, Math.fma(this.y, var3, this.z * var4));
      this.x -= (var5 + var5) * var2;
      this.y -= (var5 + var5) * var3;
      this.z -= (var5 + var5) * var4;
      return this;
   }

   public Vector3f reflect(float var1, float var2, float var3) {
      float var4 = Math.fma(this.x, var1, Math.fma(this.y, var2, this.z * var3));
      this.x -= (var4 + var4) * var1;
      this.y -= (var4 + var4) * var2;
      this.z -= (var4 + var4) * var3;
      return this;
   }

   public Vector3f reflect(Vector3fc var1, Vector3f var2) {
      return this.reflect(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f reflect(float var1, float var2, float var3, Vector3f var4) {
      float var5 = this.dot(var1, var2, var3);
      var4.x = this.x - (var5 + var5) * var1;
      var4.y = this.y - (var5 + var5) * var2;
      var4.z = this.z - (var5 + var5) * var3;
      return var4;
   }

   public Vector3f half(Vector3fc var1) {
      return this.set((Vector3fc)this).add(var1.x(), var1.y(), var1.z()).normalize();
   }

   public Vector3f half(float var1, float var2, float var3) {
      return this.half(var1, var2, var3, this);
   }

   public Vector3f half(Vector3fc var1, Vector3f var2) {
      return this.half(var1.x(), var1.y(), var1.z(), var2);
   }

   public Vector3f half(float var1, float var2, float var3, Vector3f var4) {
      return var4.set((Vector3fc)this).add(var1, var2, var3).normalize();
   }

   public Vector3f smoothStep(Vector3fc var1, float var2, Vector3f var3) {
      float var4 = this.x;
      float var5 = this.y;
      float var6 = this.z;
      float var7 = var2 * var2;
      float var8 = var7 * var2;
      var3.x = (var4 + var4 - var1.x() - var1.x()) * var8 + (3.0F * var1.x() - 3.0F * var4) * var7 + var4 * var2 + var4;
      var3.y = (var5 + var5 - var1.y() - var1.y()) * var8 + (3.0F * var1.y() - 3.0F * var5) * var7 + var5 * var2 + var5;
      var3.z = (var6 + var6 - var1.z() - var1.z()) * var8 + (3.0F * var1.z() - 3.0F * var6) * var7 + var6 * var2 + var6;
      return var3;
   }

   public Vector3f hermite(Vector3fc var1, Vector3fc var2, Vector3fc var3, float var4, Vector3f var5) {
      float var6 = this.x;
      float var7 = this.y;
      float var8 = this.z;
      float var9 = var4 * var4;
      float var10 = var9 * var4;
      var5.x = (var6 + var6 - var2.x() - var2.x() + var3.x() + var1.x()) * var10 + (3.0F * var2.x() - 3.0F * var6 - var1.x() - var1.x() - var3.x()) * var9 + var6 * var4 + var6;
      var5.y = (var7 + var7 - var2.y() - var2.y() + var3.y() + var1.y()) * var10 + (3.0F * var2.y() - 3.0F * var7 - var1.y() - var1.y() - var3.y()) * var9 + var7 * var4 + var7;
      var5.z = (var8 + var8 - var2.z() - var2.z() + var3.z() + var1.z()) * var10 + (3.0F * var2.z() - 3.0F * var8 - var1.z() - var1.z() - var3.z()) * var9 + var8 * var4 + var8;
      return var5;
   }

   public Vector3f lerp(Vector3fc var1, float var2) {
      return this.lerp(var1, var2, this);
   }

   public Vector3f lerp(Vector3fc var1, float var2, Vector3f var3) {
      var3.x = Math.fma(var1.x() - this.x, var2, this.x);
      var3.y = Math.fma(var1.y() - this.y, var2, this.y);
      var3.z = Math.fma(var1.z() - this.z, var2, this.z);
      return var3;
   }

   public float get(int var1) throws IllegalArgumentException {
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
      var1.x = this.x();
      var1.y = this.y();
      var1.z = this.z();
      return var1;
   }

   public Vector3d get(Vector3d var1) {
      var1.x = (double)this.x();
      var1.y = (double)this.y();
      var1.z = (double)this.z();
      return var1;
   }

   public int maxComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      float var3 = Math.abs(this.z);
      if (var1 >= var2 && var1 >= var3) {
         return 0;
      } else {
         return var2 >= var3 ? 1 : 2;
      }
   }

   public int minComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      float var3 = Math.abs(this.z);
      if (var1 < var2 && var1 < var3) {
         return 0;
      } else {
         return var2 < var3 ? 1 : 2;
      }
   }

   public Vector3f orthogonalize(Vector3fc var1, Vector3f var2) {
      float var3;
      float var4;
      float var5;
      if (Math.abs(var1.x()) > Math.abs(var1.z())) {
         var3 = -var1.y();
         var4 = var1.x();
         var5 = 0.0F;
      } else {
         var3 = 0.0F;
         var4 = -var1.z();
         var5 = var1.y();
      }

      float var6 = Math.invsqrt(var3 * var3 + var4 * var4 + var5 * var5);
      var2.x = var3 * var6;
      var2.y = var4 * var6;
      var2.z = var5 * var6;
      return var2;
   }

   public Vector3f orthogonalize(Vector3fc var1) {
      return this.orthogonalize(var1, this);
   }

   public Vector3f orthogonalizeUnit(Vector3fc var1, Vector3f var2) {
      return this.orthogonalize(var1, var2);
   }

   public Vector3f orthogonalizeUnit(Vector3fc var1) {
      return this.orthogonalizeUnit(var1, this);
   }

   public Vector3f floor() {
      return this.floor(this);
   }

   public Vector3f floor(Vector3f var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      var1.z = Math.floor(this.z);
      return var1;
   }

   public Vector3f ceil() {
      return this.ceil(this);
   }

   public Vector3f ceil(Vector3f var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      var1.z = Math.ceil(this.z);
      return var1;
   }

   public Vector3f round() {
      return this.round(this);
   }

   public Vector3f round(Vector3f var1) {
      var1.x = (float)Math.round(this.x);
      var1.y = (float)Math.round(this.y);
      var1.z = (float)Math.round(this.z);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z);
   }
}
