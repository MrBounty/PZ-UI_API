package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;

public class Vector2f implements Externalizable, Vector2fc {
   private static final long serialVersionUID = 1L;
   public float x;
   public float y;

   public Vector2f() {
   }

   public Vector2f(float var1) {
      this.x = var1;
      this.y = var1;
   }

   public Vector2f(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public Vector2f(Vector2fc var1) {
      this.x = var1.x();
      this.y = var1.y();
   }

   public Vector2f(Vector2ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
   }

   public Vector2f(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Vector2f(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2f(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector2f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2f(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }

   public Vector2f set(float var1) {
      this.x = var1;
      this.y = var1;
      return this;
   }

   public Vector2f set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      return this;
   }

   public Vector2f set(double var1) {
      this.x = (float)var1;
      this.y = (float)var1;
      return this;
   }

   public Vector2f set(double var1, double var3) {
      this.x = (float)var1;
      this.y = (float)var3;
      return this;
   }

   public Vector2f set(Vector2fc var1) {
      this.x = var1.x();
      this.y = var1.y();
      return this;
   }

   public Vector2f set(Vector2ic var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      return this;
   }

   public Vector2f set(Vector2dc var1) {
      this.x = (float)var1.x();
      this.y = (float)var1.y();
      return this;
   }

   public Vector2f set(float[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      return this;
   }

   public Vector2f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2f set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2f set(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public float get(int var1) throws IllegalArgumentException {
      switch(var1) {
      case 0:
         return this.x;
      case 1:
         return this.y;
      default:
         throw new IllegalArgumentException();
      }
   }

   public Vector2i get(int var1, Vector2i var2) {
      var2.x = Math.roundUsing(this.x(), var1);
      var2.y = Math.roundUsing(this.y(), var1);
      return var2;
   }

   public Vector2f get(Vector2f var1) {
      var1.x = this.x();
      var1.y = this.y();
      return var1;
   }

   public Vector2d get(Vector2d var1) {
      var1.x = (double)this.x();
      var1.y = (double)this.y();
      return var1;
   }

   public Vector2f setComponent(int var1, float var2) throws IllegalArgumentException {
      switch(var1) {
      case 0:
         this.x = var2;
         break;
      case 1:
         this.y = var2;
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

   public FloatBuffer get(FloatBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public FloatBuffer get(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public Vector2fc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector2f perpendicular() {
      float var1 = this.y;
      this.y = this.x * -1.0F;
      this.x = var1;
      return this;
   }

   public Vector2f sub(Vector2fc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      return this;
   }

   public Vector2f sub(Vector2fc var1, Vector2f var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      return var2;
   }

   public Vector2f sub(float var1, float var2) {
      this.x -= var1;
      this.y -= var2;
      return this;
   }

   public Vector2f sub(float var1, float var2, Vector2f var3) {
      var3.x = this.x - var1;
      var3.y = this.y - var2;
      return var3;
   }

   public float dot(Vector2fc var1) {
      return this.x * var1.x() + this.y * var1.y();
   }

   public float angle(Vector2fc var1) {
      float var2 = this.x * var1.x() + this.y * var1.y();
      float var3 = this.x * var1.y() - this.y * var1.x();
      return Math.atan2(var3, var2);
   }

   public float lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public static float lengthSquared(float var0, float var1) {
      return var0 * var0 + var1 * var1;
   }

   public float length() {
      return Math.sqrt(this.x * this.x + this.y * this.y);
   }

   public static float length(float var0, float var1) {
      return Math.sqrt(var0 * var0 + var1 * var1);
   }

   public float distance(Vector2fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      return Math.sqrt(var2 * var2 + var3 * var3);
   }

   public float distanceSquared(Vector2fc var1) {
      float var2 = this.x - var1.x();
      float var3 = this.y - var1.y();
      return var2 * var2 + var3 * var3;
   }

   public float distance(float var1, float var2) {
      float var3 = this.x - var1;
      float var4 = this.y - var2;
      return Math.sqrt(var3 * var3 + var4 * var4);
   }

   public float distanceSquared(float var1, float var2) {
      float var3 = this.x - var1;
      float var4 = this.y - var2;
      return var3 * var3 + var4 * var4;
   }

   public static float distance(float var0, float var1, float var2, float var3) {
      float var4 = var0 - var2;
      float var5 = var1 - var3;
      return Math.sqrt(var4 * var4 + var5 * var5);
   }

   public static float distanceSquared(float var0, float var1, float var2, float var3) {
      float var4 = var0 - var2;
      float var5 = var1 - var3;
      return var4 * var4 + var5 * var5;
   }

   public Vector2f normalize() {
      float var1 = Math.invsqrt(this.x * this.x + this.y * this.y);
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public Vector2f normalize(Vector2f var1) {
      float var2 = Math.invsqrt(this.x * this.x + this.y * this.y);
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      return var1;
   }

   public Vector2f normalize(float var1) {
      float var2 = Math.invsqrt(this.x * this.x + this.y * this.y) * var1;
      this.x *= var2;
      this.y *= var2;
      return this;
   }

   public Vector2f normalize(float var1, Vector2f var2) {
      float var3 = Math.invsqrt(this.x * this.x + this.y * this.y) * var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      return var2;
   }

   public Vector2f add(Vector2fc var1) {
      this.x += var1.x();
      this.y += var1.y();
      return this;
   }

   public Vector2f add(Vector2fc var1, Vector2f var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      return var2;
   }

   public Vector2f add(float var1, float var2) {
      return this.add(var1, var2, this);
   }

   public Vector2f add(float var1, float var2, Vector2f var3) {
      var3.x = this.x + var1;
      var3.y = this.y + var2;
      return var3;
   }

   public Vector2f zero() {
      this.x = 0.0F;
      this.y = 0.0F;
      return this;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.x);
      var1.writeFloat(this.y);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readFloat();
      this.y = var1.readFloat();
   }

   public Vector2f negate() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public Vector2f negate(Vector2f var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      return var1;
   }

   public Vector2f mul(float var1) {
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public Vector2f mul(float var1, Vector2f var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      return var2;
   }

   public Vector2f mul(float var1, float var2) {
      this.x *= var1;
      this.y *= var2;
      return this;
   }

   public Vector2f mul(float var1, float var2, Vector2f var3) {
      var3.x = this.x * var1;
      var3.y = this.y * var2;
      return var3;
   }

   public Vector2f mul(Vector2fc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      return this;
   }

   public Vector2f mul(Vector2fc var1, Vector2f var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      return var2;
   }

   public Vector2f div(Vector2fc var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      return this;
   }

   public Vector2f div(Vector2fc var1, Vector2f var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      return var2;
   }

   public Vector2f div(float var1) {
      float var2 = 1.0F / var1;
      this.x *= var2;
      this.y *= var2;
      return this;
   }

   public Vector2f div(float var1, Vector2f var2) {
      float var3 = 1.0F / var1;
      var2.x = this.x * var3;
      var2.y = this.y * var3;
      return var2;
   }

   public Vector2f div(float var1, float var2) {
      this.x /= var1;
      this.y /= var2;
      return this;
   }

   public Vector2f div(float var1, float var2, Vector2f var3) {
      var3.x = this.x / var1;
      var3.y = this.y / var2;
      return var3;
   }

   public Vector2f mul(Matrix2fc var1) {
      float var2 = var1.m00() * this.x + var1.m10() * this.y;
      float var3 = var1.m01() * this.x + var1.m11() * this.y;
      this.x = var2;
      this.y = var3;
      return this;
   }

   public Vector2f mul(Matrix2fc var1, Vector2f var2) {
      float var3 = var1.m00() * this.x + var1.m10() * this.y;
      float var4 = var1.m01() * this.x + var1.m11() * this.y;
      var2.x = var3;
      var2.y = var4;
      return var2;
   }

   public Vector2f mul(Matrix2dc var1) {
      double var2 = var1.m00() * (double)this.x + var1.m10() * (double)this.y;
      double var4 = var1.m01() * (double)this.x + var1.m11() * (double)this.y;
      this.x = (float)var2;
      this.y = (float)var4;
      return this;
   }

   public Vector2f mul(Matrix2dc var1, Vector2f var2) {
      double var3 = var1.m00() * (double)this.x + var1.m10() * (double)this.y;
      double var5 = var1.m01() * (double)this.x + var1.m11() * (double)this.y;
      var2.x = (float)var3;
      var2.y = (float)var5;
      return var2;
   }

   public Vector2f mulTranspose(Matrix2fc var1) {
      float var2 = var1.m00() * this.x + var1.m01() * this.y;
      float var3 = var1.m10() * this.x + var1.m11() * this.y;
      this.x = var2;
      this.y = var3;
      return this;
   }

   public Vector2f mulTranspose(Matrix2fc var1, Vector2f var2) {
      float var3 = var1.m00() * this.x + var1.m01() * this.y;
      float var4 = var1.m10() * this.x + var1.m11() * this.y;
      var2.x = var3;
      var2.y = var4;
      return var2;
   }

   public Vector2f mulPosition(Matrix3x2fc var1) {
      this.x = var1.m00() * this.x + var1.m10() * this.y + var1.m20();
      this.y = var1.m01() * this.x + var1.m11() * this.y + var1.m21();
      return this;
   }

   public Vector2f mulPosition(Matrix3x2fc var1, Vector2f var2) {
      var2.x = var1.m00() * this.x + var1.m10() * this.y + var1.m20();
      var2.y = var1.m01() * this.x + var1.m11() * this.y + var1.m21();
      return var2;
   }

   public Vector2f mulDirection(Matrix3x2fc var1) {
      this.x = var1.m00() * this.x + var1.m10() * this.y;
      this.y = var1.m01() * this.x + var1.m11() * this.y;
      return this;
   }

   public Vector2f mulDirection(Matrix3x2fc var1, Vector2f var2) {
      var2.x = var1.m00() * this.x + var1.m10() * this.y;
      var2.y = var1.m01() * this.x + var1.m11() * this.y;
      return var2;
   }

   public Vector2f lerp(Vector2fc var1, float var2) {
      this.x += (var1.x() - this.x) * var2;
      this.y += (var1.y() - this.y) * var2;
      return this;
   }

   public Vector2f lerp(Vector2fc var1, float var2, Vector2f var3) {
      var3.x = this.x + (var1.x() - this.x) * var2;
      var3.y = this.y + (var1.y() - this.y) * var2;
      return var3;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.x);
      var2 = 31 * var2 + Float.floatToIntBits(this.y);
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
         Vector2f var2 = (Vector2f)var1;
         if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var2.x)) {
            return false;
         } else {
            return Float.floatToIntBits(this.y) == Float.floatToIntBits(var2.y);
         }
      }
   }

   public boolean equals(Vector2fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector2fc)) {
         return false;
      } else if (!Runtime.equals(this.x, var1.x(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.y, var1.y(), var2);
      }
   }

   public boolean equals(float var1, float var2) {
      if (Float.floatToIntBits(this.x) != Float.floatToIntBits(var1)) {
         return false;
      } else {
         return Float.floatToIntBits(this.y) == Float.floatToIntBits(var2);
      }
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format((double)this.x, var1);
      return "(" + var10000 + " " + Runtime.format((double)this.y, var1) + ")";
   }

   public Vector2f fma(Vector2fc var1, Vector2fc var2) {
      this.x += var1.x() * var2.x();
      this.y += var1.y() * var2.y();
      return this;
   }

   public Vector2f fma(float var1, Vector2fc var2) {
      this.x += var1 * var2.x();
      this.y += var1 * var2.y();
      return this;
   }

   public Vector2f fma(Vector2fc var1, Vector2fc var2, Vector2f var3) {
      var3.x = this.x + var1.x() * var2.x();
      var3.y = this.y + var1.y() * var2.y();
      return var3;
   }

   public Vector2f fma(float var1, Vector2fc var2, Vector2f var3) {
      var3.x = this.x + var1 * var2.x();
      var3.y = this.y + var1 * var2.y();
      return var3;
   }

   public Vector2f min(Vector2fc var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2f min(Vector2fc var1, Vector2f var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      return var2;
   }

   public Vector2f max(Vector2fc var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2f max(Vector2fc var1, Vector2f var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      return var2;
   }

   public int maxComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      return var1 >= var2 ? 0 : 1;
   }

   public int minComponent() {
      float var1 = Math.abs(this.x);
      float var2 = Math.abs(this.y);
      return var1 < var2 ? 0 : 1;
   }

   public Vector2f floor() {
      this.x = Math.floor(this.x);
      this.y = Math.floor(this.y);
      return this;
   }

   public Vector2f floor(Vector2f var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      return var1;
   }

   public Vector2f ceil() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      return this;
   }

   public Vector2f ceil(Vector2f var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      return var1;
   }

   public Vector2f round() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      return this;
   }

   public Vector2f round(Vector2f var1) {
      var1.x = (float)Math.round(this.x);
      var1.y = (float)Math.round(this.y);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y);
   }

   public Vector2f absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      return this;
   }

   public Vector2f absolute(Vector2f var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      return var1;
   }
}
