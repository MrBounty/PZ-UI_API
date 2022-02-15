package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Vector4f implements Externalizable, Vector4fc {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;
   public float z;
   public float w;

   public Vector4f() {
      this.w = 1.0F;
   }

   public Vector4f(Vector4fc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
   }

   public Vector4f(Vector4ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = (float)var1.w();
   }

   public Vector4f(Vector3fc var1, float var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
   }

   public Vector4f(Vector3ic var1, float var2) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = var2;
   }

   public Vector4f(Vector2fc var1, float var2, float var3) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var3;
   }

   public Vector4f(Vector2ic var1, float var2, float var3) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = var2;
      this.w = var3;
   }

   public Vector4f(float var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
   }

   public Vector4f(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Vector4f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Vector4f(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4f(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector4f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4f(int var1, FloatBuffer var2) {
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

   public float w() {
      return this.w;
   }

   public Vector4f set(Vector4fc var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
      return this;
   }

   public Vector4f set(Vector4ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = (float)var1.w();
      return this;
   }

   public Vector4f set(Vector4dc var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = (float)var1.w();
      return this;
   }

   public Vector4f set(Vector3fc var1, float var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
      return this;
   }

   public Vector4f set(Vector3ic var1, float var2) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = (float)var1.z();
      this.w = var2;
      return this;
   }

   public Vector4f set(Vector2fc var1, float var2, float var3) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var3;
      return this;
   }

   public Vector4f set(Vector2ic var1, float var2, float var3) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      this.z = var2;
      this.w = var3;
      return this;
   }

   public Vector4f set(float var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
      return this;
   }

   public Vector4f set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
      return this;
   }

   public Vector4f set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      return this;
   }

   public Vector4f set(double var1) {
      this.x = (float)var1;
      this.y = (float)var1;
      this.z = (float)var1;
      this.w = (float)var1;
      return this;
   }

   public Vector4f set(double var1, double var3, double var5, double var7) {
      this.x = (float)var1;
      this.y = (float)var3;
      this.z = (float)var5;
      this.w = (float)var7;
      return this;
   }

   public Vector4f set(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[2];
      return this;
   }

   public Vector4f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4f set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4f set(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Vector4f setComponent(int var1, float var2) throws IllegalArgumentException {
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

   public Vector4fc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector4f sub(Vector4fc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      this.w -= var1.w();
      return this;
   }

   public Vector4f sub(float var1, float var2, float var3, float var4) {
      this.x -= var1;
      this.y -= var2;
      this.z -= var3;
      this.w -= var4;
      return this;
   }

   public Vector4f sub(Vector4fc var1, Vector4f var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      var2.w = this.w - var1.w();
      return var2;
   }

   public Vector4f sub(float var1, float var2, float var3, float var4, Vector4f var5) {
      var5.x = this.x - var1;
      var5.y = this.y - var2;
      var5.z = this.z - var3;
      var5.w = this.w - var4;
      return var5;
   }

   public Vector4f add(Vector4fc var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      this.w += var1.w();
      return this;
   }

   public Vector4f add(Vector4fc var1, Vector4f var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      var2.w = this.w + var1.w();
      return var2;
   }

   public Vector4f add(float var1, float var2, float var3, float var4) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      this.w += var4;
      return this;
   }

   public Vector4f add(float var1, float var2, float var3, float var4, Vector4f var5) {
      var5.x = this.x + var1;
      var5.y = this.y + var2;
      var5.z = this.z + var3;
      var5.w = this.w + var4;
      return var5;
   }

   public Vector4f fma(Vector4fc var1, Vector4fc var2) {
      this.x = Math.fma(var1.x(), var2.x(), this.x);
      this.y = Math.fma(var1.y(), var2.y(), this.y);
      this.z = Math.fma(var1.z(), var2.z(), this.z);
      this.w = Math.fma(var1.w(), var2.w(), this.w);
      return this;
   }

   public Vector4f fma(float var1, Vector4fc var2) {
      this.x = Math.fma(var1, var2.x(), this.x);
      this.y = Math.fma(var1, var2.y(), this.y);
      this.z = Math.fma(var1, var2.z(), this.z);
      this.w = Math.fma(var1, var2.w(), this.w);
      return this;
   }

   public Vector4f fma(Vector4fc var1, Vector4fc var2, Vector4f var3) {
      var3.x = Math.fma(var1.x(), var2.x(), this.x);
      var3.y = Math.fma(var1.y(), var2.y(), this.y);
      var3.z = Math.fma(var1.z(), var2.z(), this.z);
      var3.w = Math.fma(var1.w(), var2.w(), this.w);
      return var3;
   }

   public Vector4f fma(float var1, Vector4fc var2, Vector4f var3) {
      var3.x = Math.fma(var1, var2.x(), this.x);
      var3.y = Math.fma(var1, var2.y(), this.y);
      var3.z = Math.fma(var1, var2.z(), this.z);
      var3.w = Math.fma(var1, var2.w(), this.w);
      return var3;
   }

   public Vector4f mulAdd(Vector4fc var1, Vector4fc var2) {
      this.x = Math.fma(this.x, var1.x(), var2.x());
      this.y = Math.fma(this.y, var1.y(), var2.y());
      this.z = Math.fma(this.z, var1.z(), var2.z());
      return this;
   }

   public Vector4f mulAdd(float var1, Vector4fc var2) {
      this.x = Math.fma(this.x, var1, var2.x());
      this.y = Math.fma(this.y, var1, var2.y());
      this.z = Math.fma(this.z, var1, var2.z());
      return this;
   }

   public Vector4f mulAdd(Vector4fc var1, Vector4fc var2, Vector4f var3) {
      var3.x = Math.fma(this.x, var1.x(), var2.x());
      var3.y = Math.fma(this.y, var1.y(), var2.y());
      var3.z = Math.fma(this.z, var1.z(), var2.z());
      return var3;
   }

   public Vector4f mulAdd(float var1, Vector4fc var2, Vector4f var3) {
      var3.x = Math.fma(this.x, var1, var2.x());
      var3.y = Math.fma(this.y, var1, var2.y());
      var3.z = Math.fma(this.z, var1, var2.z());
      return var3;
   }

   public Vector4f mul(Vector4fc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      this.w *= var1.w();
      return this;
   }

   public Vector4f mul(Vector4fc var1, Vector4f var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      var2.w = this.w * var1.w();
      return var2;
   }

   public Vector4f div(Vector4fc var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      this.z /= var1.z();
      this.w /= var1.w();
      return this;
   }

   public Vector4f div(Vector4fc var1, Vector4f var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      var2.z = this.z / var1.z();
      var2.w = this.w / var1.w();
      return var2;
   }

   public Vector4f mul(Matrix4fc var1) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, this) : this.mulGeneric(var1, this);
   }

   public Vector4f mul(Matrix4fc var1, Vector4f var2) {
      return (var1.properties() & 2) != 0 ? this.mulAffine(var1, var2) : this.mulGeneric(var1, var2);
   }

   public Vector4f mulTranspose(Matrix4fc var1) {
      return (var1.properties() & 2) != 0 ? this.mulAffineTranspose(var1, this) : this.mulGenericTranspose(var1, this);
   }

   public Vector4f mulTranspose(Matrix4fc var1, Vector4f var2) {
      return (var1.properties() & 2) != 0 ? this.mulAffineTranspose(var1, var2) : this.mulGenericTranspose(var1, var2);
   }

   public Vector4f mulAffine(Matrix4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30() * var6)));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31() * var6)));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32() * var6)));
      var2.w = var6;
      return var2;
   }

   private Vector4f mulGeneric(Matrix4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30() * var6)));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31() * var6)));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32() * var6)));
      var2.w = Math.fma(var1.m03(), var3, Math.fma(var1.m13(), var4, Math.fma(var1.m23(), var5, var1.m33() * var6)));
      return var2;
   }

   public Vector4f mulAffineTranspose(Matrix4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var4, var1.m02() * var5));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var4, var1.m12() * var5));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var4, var1.m22() * var5));
      var2.w = Math.fma(var1.m30(), var3, Math.fma(var1.m31(), var4, var1.m32() * var5 + var6));
      return var2;
   }

   private Vector4f mulGenericTranspose(Matrix4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m01(), var4, Math.fma(var1.m02(), var5, var1.m03() * var6)));
      var2.y = Math.fma(var1.m10(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m12(), var5, var1.m13() * var6)));
      var2.z = Math.fma(var1.m20(), var3, Math.fma(var1.m21(), var4, Math.fma(var1.m22(), var5, var1.m23() * var6)));
      var2.w = Math.fma(var1.m30(), var3, Math.fma(var1.m31(), var4, Math.fma(var1.m32(), var5, var1.m33() * var6)));
      return var2;
   }

   public Vector4f mul(Matrix4x3fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30() * var5)));
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31() * var5)));
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32() * var5)));
      this.w = var5;
      return this;
   }

   public Vector4f mul(Matrix4x3fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30() * var6)));
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31() * var6)));
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32() * var6)));
      var2.w = var6;
      return var2;
   }

   public Vector4f mulProject(Matrix4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      float var7 = 1.0F / Math.fma(var1.m03(), var3, Math.fma(var1.m13(), var4, Math.fma(var1.m23(), var5, var1.m33() * var6)));
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30() * var6))) * var7;
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31() * var6))) * var7;
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32() * var6))) * var7;
      var2.w = 1.0F;
      return var2;
   }

   public Vector4f mulProject(Matrix4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      float var6 = 1.0F / Math.fma(var1.m03(), var2, Math.fma(var1.m13(), var3, Math.fma(var1.m23(), var4, var1.m33() * var5)));
      this.x = Math.fma(var1.m00(), var2, Math.fma(var1.m10(), var3, Math.fma(var1.m20(), var4, var1.m30() * var5))) * var6;
      this.y = Math.fma(var1.m01(), var2, Math.fma(var1.m11(), var3, Math.fma(var1.m21(), var4, var1.m31() * var5))) * var6;
      this.z = Math.fma(var1.m02(), var2, Math.fma(var1.m12(), var3, Math.fma(var1.m22(), var4, var1.m32() * var5))) * var6;
      this.w = 1.0F;
      return this;
   }

   public Vector3f mulProject(Matrix4fc var1, Vector3f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      float var7 = 1.0F / Math.fma(var1.m03(), var3, Math.fma(var1.m13(), var4, Math.fma(var1.m23(), var5, var1.m33() * var6)));
      var2.x = Math.fma(var1.m00(), var3, Math.fma(var1.m10(), var4, Math.fma(var1.m20(), var5, var1.m30() * var6))) * var7;
      var2.y = Math.fma(var1.m01(), var3, Math.fma(var1.m11(), var4, Math.fma(var1.m21(), var5, var1.m31() * var6))) * var7;
      var2.z = Math.fma(var1.m02(), var3, Math.fma(var1.m12(), var4, Math.fma(var1.m22(), var5, var1.m32() * var6))) * var7;
      return var2;
   }

   public Vector4f mul(float var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4f mul(float var1, Vector4f var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      var2.z = this.z * var1;
      var2.w = this.w * var1;
      return var2;
   }

   public Vector4f mul(float var1, float var2, float var3, float var4) {
      this.x *= var1;
      this.y *= var2;
      this.z *= var3;
      this.w *= var4;
      return this;
   }

   public Vector4f mul(float var1, float var2, float var3, float var4, Vector4f var5) {
      var5.x = this.x * var1;
      var5.y = this.y * var2;
      var5.z = this.z * var3;
      var5.w = this.w * var4;
      return var5;
   }

   public Vector4f div(float var1) {
      float var2 = 1.0F / var1;
      this.x *= var2;
      this.y *= var2;
      this.z *= var2;
      this.w *= var2;
      return this;
   }

   public Vector4f div(float var1, Vector4f var2) {
      float var3 = 1.0F / var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      var2.z = this.z * var3;
      var2.w = this.w * var3;
      return var2;
   }

   public Vector4f div(float var1, float var2, float var3, float var4) {
      this.x /= var1;
      this.y /= var2;
      this.z /= var3;
      this.w /= var4;
      return this;
   }

   public Vector4f div(float var1, float var2, float var3, float var4, Vector4f var5) {
      var5.x = this.x / var1;
      var5.y = this.y / var2;
      var5.z = this.z / var3;
      var5.w = this.w / var4;
      return var5;
   }

   public Vector4f rotate(Quaternionfc var1) {
      return var1.transform((Vector4fc)this, (Vector4f)this);
   }

   public Vector4f rotate(Quaternionfc var1, Vector4f var2) {
      return var1.transform((Vector4fc)this, (Vector4f)var2);
   }

   public Vector4f rotateAbout(float var1, float var2, float var3, float var4) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, this);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, this);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, this) : this.rotateAxisInternal(var1, var2, var3, var4, this);
      }
   }

   public Vector4f rotateAxis(float var1, float var2, float var3, float var4, Vector4f var5) {
      if (var3 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var2)) {
         return this.rotateX(var2 * var1, var5);
      } else if (var2 == 0.0F && var4 == 0.0F && Math.absEqualsOne(var3)) {
         return this.rotateY(var3 * var1, var5);
      } else {
         return var2 == 0.0F && var3 == 0.0F && Math.absEqualsOne(var4) ? this.rotateZ(var4 * var1, var5) : this.rotateAxisInternal(var1, var2, var3, var4, var5);
      }
   }

   private Vector4f rotateAxisInternal(float var1, float var2, float var3, float var4, Vector4f var5) {
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

   public Vector4f rotateX(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.y * var3 - this.z * var2;
      float var5 = this.y * var2 + this.z * var3;
      this.y = var4;
      this.z = var5;
      return this;
   }

   public Vector4f rotateX(float var1, Vector4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.y * var4 - this.z * var3;
      float var6 = this.y * var3 + this.z * var4;
      var2.x = this.x;
      var2.y = var5;
      var2.z = var6;
      var2.w = this.w;
      return var2;
   }

   public Vector4f rotateY(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.x * var3 + this.z * var2;
      float var5 = -this.x * var2 + this.z * var3;
      this.x = var4;
      this.z = var5;
      return this;
   }

   public Vector4f rotateY(float var1, Vector4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.x * var4 + this.z * var3;
      float var6 = -this.x * var3 + this.z * var4;
      var2.x = var5;
      var2.y = this.y;
      var2.z = var6;
      var2.w = this.w;
      return var2;
   }

   public Vector4f rotateZ(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      float var4 = this.x * var3 - this.y * var2;
      float var5 = this.x * var2 + this.y * var3;
      this.x = var4;
      this.y = var5;
      return this;
   }

   public Vector4f rotateZ(float var1, Vector4f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.x * var4 - this.y * var3;
      float var6 = this.x * var3 + this.y * var4;
      var2.x = var5;
      var2.y = var6;
      var2.z = this.z;
      var2.w = this.w;
      return var2;
   }

   public float lengthSquared() {
      return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
   }

   public static float lengthSquared(float var0, float var1, float var2, float var3) {
      return Math.fma(var0, var0, Math.fma(var1, var1, Math.fma(var2, var2, var3 * var3)));
   }

   public static float lengthSquared(int var0, int var1, int var2, int var3) {
      return Math.fma((float)var0, (float)var0, Math.fma((float)var1, (float)var1, Math.fma((float)var2, (float)var2, (float)(var3 * var3))));
   }

   public float length() {
      return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
   }

   public static float length(float var0, float var1, float var2, float var3) {
      return Math.sqrt(Math.fma(var0, var0, Math.fma(var1, var1, Math.fma(var2, var2, var3 * var3))));
   }

   public Vector4f normalize() {
      float var1 = 1.0F / this.length();
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4f normalize(Vector4f var1) {
      float var2 = 1.0F / this.length();
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public Vector4f normalize(float var1) {
      float var2 = 1.0F / this.length() * var1;
      this.x *= var2;
      this.y *= var2;
      this.z *= var2;
      this.w *= var2;
      return this;
   }

   public Vector4f normalize(float var1, Vector4f var2) {
      float var3 = 1.0F / this.length() * var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      var2.z = this.z * var3;
      var2.w = this.w * var3;
      return var2;
   }

   public Vector4f normalize3() {
      float var1 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4f normalize3(Vector4f var1) {
      float var2 = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      var1.z = this.z * var2;
      var1.w = this.w * var2;
      return var1;
   }

   public float distance(Vector4fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      float var4 = this.z - var1.z();
      float var5 = this.w - var1.w();
      return Math.sqrt(Math.fma(var2, var2, Math.fma(var3, var3, Math.fma(var4, var4, var5 * var5))));
   }

   public float distance(float var1, float var2, float var3, float var4) {
      float var5 = this.x - var1;
      float var6 = this.y - var2;
      float var7 = this.z - var3;
      float var8 = this.w - var4;
      return Math.sqrt(Math.fma(var5, var5, Math.fma(var6, var6, Math.fma(var7, var7, var8 * var8))));
   }

   public float distanceSquared(Vector4fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      float var4 = this.z - var1.z();
      float var5 = this.w - var1.w();
      return Math.fma(var2, var2, Math.fma(var3, var3, Math.fma(var4, var4, var5 * var5)));
   }

   public float distanceSquared(float var1, float var2, float var3, float var4) {
      float var5 = this.x - var1;
      float var6 = this.y - var2;
      float var7 = this.z - var3;
      float var8 = this.w - var4;
      return Math.fma(var5, var5, Math.fma(var6, var6, Math.fma(var7, var7, var8 * var8)));
   }

   public static float distance(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var0 - var4;
      float var9 = var1 - var5;
      float var10 = var2 - var6;
      float var11 = var3 - var7;
      return Math.sqrt(Math.fma(var8, var8, Math.fma(var9, var9, Math.fma(var10, var10, var11 * var11))));
   }

   public static float distanceSquared(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var0 - var4;
      float var9 = var1 - var5;
      float var10 = var2 - var6;
      float var11 = var3 - var7;
      return Math.fma(var8, var8, Math.fma(var9, var9, Math.fma(var10, var10, var11 * var11)));
   }

   public float dot(Vector4fc var1) {
      return Math.fma(this.x, var1.x(), Math.fma(this.y, var1.y(), Math.fma(this.z, var1.z(), this.w * var1.w())));
   }

   public float dot(float var1, float var2, float var3, float var4) {
      return Math.fma(this.x, var1, Math.fma(this.y, var2, Math.fma(this.z, var3, this.w * var4)));
   }

   public float angleCos(Vector4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      float var6 = Math.fma(var2, var2, Math.fma(var3, var3, Math.fma(var4, var4, var5 * var5)));
      float var7 = Math.fma(var1.x(), var1.x(), Math.fma(var1.y(), var1.y(), Math.fma(var1.z(), var1.z(), var1.w() * var1.w())));
      float var8 = Math.fma(var2, var1.x(), Math.fma(var3, var1.y(), Math.fma(var4, var1.z(), var5 * var1.w())));
      return var8 / Math.sqrt(var6 * var7);
   }

   public float angle(Vector4fc var1) {
      float var2 = this.angleCos(var1);
      var2 = var2 < 1.0F ? var2 : 1.0F;
      var2 = var2 > -1.0F ? var2 : -1.0F;
      return Math.acos(var2);
   }

   public Vector4f zero() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
      this.w = 0.0F;
      return this;
   }

   public Vector4f negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
      return this;
   }

   public Vector4f negate(Vector4f var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      var1.w = -this.w;
      return var1;
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
      this.set(var1.readFloat(), var1.readFloat(), var1.readFloat(), var1.readFloat());
   }

   public Vector4f min(Vector4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      this.x = var2 < var1.x() ? var2 : var1.x();
      this.y = var3 < var1.y() ? var3 : var1.y();
      this.z = var4 < var1.z() ? var4 : var1.z();
      this.w = var5 < var1.w() ? var5 : var1.w();
      return this;
   }

   public Vector4f min(Vector4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = var3 < var1.x() ? var3 : var1.x();
      var2.y = var4 < var1.y() ? var4 : var1.y();
      var2.z = var5 < var1.z() ? var5 : var1.z();
      var2.w = var6 < var1.w() ? var6 : var1.w();
      return var2;
   }

   public Vector4f max(Vector4fc var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = this.z;
      float var5 = this.w;
      this.x = var2 > var1.x() ? var2 : var1.x();
      this.y = var3 > var1.y() ? var3 : var1.y();
      this.z = var4 > var1.z() ? var4 : var1.z();
      this.w = var5 > var1.w() ? var5 : var1.w();
      return this;
   }

   public Vector4f max(Vector4fc var1, Vector4f var2) {
      float var3 = this.x;
      float var4 = this.y;
      float var5 = this.z;
      float var6 = this.w;
      var2.x = var3 > var1.x() ? var3 : var1.x();
      var2.y = var4 > var1.y() ? var4 : var1.y();
      var2.z = var5 > var1.z() ? var5 : var1.z();
      var2.w = var6 > var1.w() ? var6 : var1.w();
      return var2;
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
         Vector4f var2 = (Vector4f)var1;
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

   public boolean equals(Vector4fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector4fc)) {
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

   public Vector4f smoothStep(Vector4fc var1, float var2, Vector4f var3) {
      float var4 = var2 * var2;
      float var5 = var4 * var2;
      float var6 = this.x;
      float var7 = this.y;
      float var8 = this.z;
      float var9 = this.w;
      var3.x = (var6 + var6 - var1.x() - var1.x()) * var5 + (3.0F * var1.x() - 3.0F * var6) * var4 + var6 * var2 + var6;
      var3.y = (var7 + var7 - var1.y() - var1.y()) * var5 + (3.0F * var1.y() - 3.0F * var7) * var4 + var7 * var2 + var7;
      var3.z = (var8 + var8 - var1.z() - var1.z()) * var5 + (3.0F * var1.z() - 3.0F * var8) * var4 + var8 * var2 + var8;
      var3.w = (var9 + var9 - var1.w() - var1.w()) * var5 + (3.0F * var1.w() - 3.0F * var9) * var4 + var9 * var2 + var9;
      return var3;
   }

   public Vector4f hermite(Vector4fc var1, Vector4fc var2, Vector4fc var3, float var4, Vector4f var5) {
      float var6 = var4 * var4;
      float var7 = var6 * var4;
      float var8 = this.x;
      float var9 = this.y;
      float var10 = this.z;
      float var11 = this.w;
      var5.x = (var8 + var8 - var2.x() - var2.x() + var3.x() + var1.x()) * var7 + (3.0F * var2.x() - 3.0F * var8 - var1.x() - var1.x() - var3.x()) * var6 + var8 * var4 + var8;
      var5.y = (var9 + var9 - var2.y() - var2.y() + var3.y() + var1.y()) * var7 + (3.0F * var2.y() - 3.0F * var9 - var1.y() - var1.y() - var3.y()) * var6 + var9 * var4 + var9;
      var5.z = (var10 + var10 - var2.z() - var2.z() + var3.z() + var1.z()) * var7 + (3.0F * var2.z() - 3.0F * var10 - var1.z() - var1.z() - var3.z()) * var6 + var10 * var4 + var10;
      var5.w = (var11 + var11 - var2.w() - var2.w() + var3.w() + var1.w()) * var7 + (3.0F * var2.w() - 3.0F * var11 - var1.w() - var1.w() - var3.w()) * var6 + var11 * var4 + var11;
      return var5;
   }

   public Vector4f lerp(Vector4fc var1, float var2) {
      this.x = Math.fma(var1.x() - this.x, var2, this.x);
      this.y = Math.fma(var1.y() - this.y, var2, this.y);
      this.z = Math.fma(var1.z() - this.z, var2, this.z);
      this.w = Math.fma(var1.w() - this.w, var2, this.w);
      return this;
   }

   public Vector4f lerp(Vector4fc var1, float var2, Vector4f var3) {
      var3.x = Math.fma(var1.x() - this.x, var2, this.x);
      var3.y = Math.fma(var1.y() - this.y, var2, this.y);
      var3.z = Math.fma(var1.z() - this.z, var2, this.z);
      var3.w = Math.fma(var1.w() - this.w, var2, this.w);
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
      var1.x = this.x();
      var1.y = this.y();
      var1.z = this.z();
      var1.w = this.w();
      return var1;
   }

   public Vector4d get(Vector4d var1) {
      var1.x = (double)this.x();
      var1.y = (double)this.y();
      var1.z = (double)this.z();
      var1.w = (double)this.w();
      return var1;
   }

   public int maxComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      float var3 = Math.abs(this.z);
      float var4 = Math.abs(this.w);
      if (var1 >= var2 && var1 >= var3 && var1 >= var4) {
         return 0;
      } else if (var2 >= var3 && var2 >= var4) {
         return 1;
      } else {
         return var3 >= var4 ? 2 : 3;
      }
   }

   public int minComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      float var3 = Math.abs(this.z);
      float var4 = Math.abs(this.w);
      if (var1 < var2 && var1 < var3 && var1 < var4) {
         return 0;
      } else if (var2 < var3 && var2 < var4) {
         return 1;
      } else {
         return var3 < var4 ? 2 : 3;
      }
   }

   public Vector4f floor() {
      this.x = Math.floor(this.x);
      this.y = Math.floor(this.y);
      this.z = Math.floor(this.z);
      this.w = Math.floor(this.w);
      return this;
   }

   public Vector4f floor(Vector4f var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      var1.z = Math.floor(this.z);
      var1.w = Math.floor(this.w);
      return var1;
   }

   public Vector4f ceil() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      this.z = Math.ceil(this.z);
      this.w = Math.ceil(this.w);
      return this;
   }

   public Vector4f ceil(Vector4f var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      var1.z = Math.ceil(this.z);
      var1.w = Math.ceil(this.w);
      return var1;
   }

   public Vector4f round() {
      this.x = (float)Math.round(this.x);
      this.y = (float)Math.round(this.y);
      this.z = (float)Math.round(this.z);
      this.w = (float)Math.round(this.w);
      return this;
   }

   public Vector4f round(Vector4f var1) {
      var1.x = (float)Math.round(this.x);
      var1.y = (float)Math.round(this.y);
      var1.z = (float)Math.round(this.z);
      var1.w = (float)Math.round(this.w);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
   }

   public Vector4f absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      this.w = Math.abs(this.w);
      return this;
   }

   public Vector4f absolute(Vector4f var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      var1.w = Math.abs(this.w);
      return var1;
   }
}
