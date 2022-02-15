package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.text.NumberFormat;

public class Vector2d implements Externalizable, Vector2dc {
   private static final long serialVersionUID = 1L;
   public double x;
   public double y;

   public Vector2d() {
   }

   public Vector2d(double var1) {
      this.x = var1;
      this.y = var1;
   }

   public Vector2d(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public Vector2d(Vector2dc var1) {
      this.x = var1.x();
      this.y = var1.y();
   }

   public Vector2d(Vector2fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
   }

   public Vector2d(Vector2ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
   }

   public Vector2d(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Vector2d(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
   }

   public Vector2d(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2d(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector2d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2d(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public double x() {
      return this.x;
   }

   public double y() {
      return this.y;
   }

   public Vector2d set(double var1) {
      this.x = var1;
      this.y = var1;
      return this;
   }

   public Vector2d set(double var1, double var3) {
      this.x = var1;
      this.y = var3;
      return this;
   }

   public Vector2d set(Vector2dc var1) {
      this.x = var1.x();
      this.y = var1.y();
      return this;
   }

   public Vector2d set(Vector2fc var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      return this;
   }

   public Vector2d set(Vector2ic var1) {
      this.x = (double)var1.x();
      this.y = (double)var1.y();
      return this;
   }

   public Vector2d set(double[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      return this;
   }

   public Vector2d set(float[] var1) {
      this.x = (double)var1[0];
      this.y = (double)var1[1];
      return this;
   }

   public Vector2d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2d set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2d set(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public double get(int var1) throws IllegalArgumentException {
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
      var1.x = (float)this.x();
      var1.y = (float)this.y();
      return var1;
   }

   public Vector2d get(Vector2d var1) {
      var1.x = this.x();
      var1.y = this.y();
      return var1;
   }

   public Vector2d setComponent(int var1, double var2) throws IllegalArgumentException {
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

   public DoubleBuffer get(DoubleBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public DoubleBuffer get(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public Vector2dc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector2d perpendicular() {
      double var1 = this.y;
      this.y = this.x * -1.0D;
      this.x = var1;
      return this;
   }

   public Vector2d sub(Vector2dc var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      return this;
   }

   public Vector2d sub(double var1, double var3) {
      this.x -= var1;
      this.y -= var3;
      return this;
   }

   public Vector2d sub(double var1, double var3, Vector2d var5) {
      var5.x = this.x - var1;
      var5.y = this.y - var3;
      return var5;
   }

   public Vector2d sub(Vector2fc var1) {
      this.x -= (double)var1.x();
      this.y -= (double)var1.y();
      return this;
   }

   public Vector2d sub(Vector2dc var1, Vector2d var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      return var2;
   }

   public Vector2d sub(Vector2fc var1, Vector2d var2) {
      var2.x = this.x - (double)var1.x();
      var2.y = this.y - (double)var1.y();
      return var2;
   }

   public Vector2d mul(double var1) {
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public Vector2d mul(double var1, Vector2d var3) {
      var3.x = this.x * var1;
      var3.y = this.y * var1;
      return var3;
   }

   public Vector2d mul(double var1, double var3) {
      this.x *= var1;
      this.y *= var3;
      return this;
   }

   public Vector2d mul(double var1, double var3, Vector2d var5) {
      var5.x = this.x * var1;
      var5.y = this.y * var3;
      return var5;
   }

   public Vector2d mul(Vector2dc var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      return this;
   }

   public Vector2d mul(Vector2dc var1, Vector2d var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      return var2;
   }

   public Vector2d div(double var1) {
      double var3 = 1.0D / var1;
      this.x *= var3;
      this.y *= var3;
      return this;
   }

   public Vector2d div(double var1, Vector2d var3) {
      double var4 = 1.0D / var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      return var3;
   }

   public Vector2d div(double var1, double var3) {
      this.x /= var1;
      this.y /= var3;
      return this;
   }

   public Vector2d div(double var1, double var3, Vector2d var5) {
      var5.x = this.x / var1;
      var5.y = this.y / var3;
      return var5;
   }

   public Vector2d div(Vector2d var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      return this;
   }

   public Vector2d div(Vector2fc var1) {
      this.x /= (double)var1.x();
      this.y /= (double)var1.y();
      return this;
   }

   public Vector2d div(Vector2fc var1, Vector2d var2) {
      var2.x = this.x / (double)var1.x();
      var2.y = this.y / (double)var1.y();
      return var2;
   }

   public Vector2d div(Vector2dc var1, Vector2d var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      return var2;
   }

   public Vector2d mul(Matrix2fc var1) {
      double var2 = (double)var1.m00() * this.x + (double)var1.m10() * this.y;
      double var4 = (double)var1.m01() * this.x + (double)var1.m11() * this.y;
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mul(Matrix2dc var1) {
      double var2 = var1.m00() * this.x + var1.m10() * this.y;
      double var4 = var1.m01() * this.x + var1.m11() * this.y;
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mul(Matrix2dc var1, Vector2d var2) {
      double var3 = var1.m00() * this.x + var1.m10() * this.y;
      double var5 = var1.m01() * this.x + var1.m11() * this.y;
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public Vector2d mul(Matrix2fc var1, Vector2d var2) {
      double var3 = (double)var1.m00() * this.x + (double)var1.m10() * this.y;
      double var5 = (double)var1.m01() * this.x + (double)var1.m11() * this.y;
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public Vector2d mulTranspose(Matrix2dc var1) {
      double var2 = var1.m00() * this.x + var1.m01() * this.y;
      double var4 = var1.m10() * this.x + var1.m11() * this.y;
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mulTranspose(Matrix2dc var1, Vector2d var2) {
      double var3 = var1.m00() * this.x + var1.m01() * this.y;
      double var5 = var1.m10() * this.x + var1.m11() * this.y;
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public Vector2d mulTranspose(Matrix2fc var1) {
      double var2 = (double)var1.m00() * this.x + (double)var1.m01() * this.y;
      double var4 = (double)var1.m10() * this.x + (double)var1.m11() * this.y;
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mulTranspose(Matrix2fc var1, Vector2d var2) {
      double var3 = (double)var1.m00() * this.x + (double)var1.m01() * this.y;
      double var5 = (double)var1.m10() * this.x + (double)var1.m11() * this.y;
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public Vector2d mulPosition(Matrix3x2dc var1) {
      double var2 = var1.m00() * this.x + var1.m10() * this.y + var1.m20();
      double var4 = var1.m01() * this.x + var1.m11() * this.y + var1.m21();
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mulPosition(Matrix3x2dc var1, Vector2d var2) {
      double var3 = var1.m00() * this.x + var1.m10() * this.y + var1.m20();
      double var5 = var1.m01() * this.x + var1.m11() * this.y + var1.m21();
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public Vector2d mulDirection(Matrix3x2dc var1) {
      double var2 = var1.m00() * this.x + var1.m10() * this.y;
      double var4 = var1.m01() * this.x + var1.m11() * this.y;
      this.x = var2;
      this.y = var4;
      return this;
   }

   public Vector2d mulDirection(Matrix3x2dc var1, Vector2d var2) {
      double var3 = var1.m00() * this.x + var1.m10() * this.y;
      double var5 = var1.m01() * this.x + var1.m11() * this.y;
      var2.x = var3;
      var2.y = var5;
      return var2;
   }

   public double dot(Vector2dc var1) {
      return this.x * var1.x() + this.y * var1.y();
   }

   public double angle(Vector2dc var1) {
      double var2 = this.x * var1.x() + this.y * var1.y();
      double var4 = this.x * var1.y() - this.y * var1.x();
      return Math.atan2(var4, var2);
   }

   public double lengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public static double lengthSquared(double var0, double var2) {
      return var0 * var0 + var2 * var2;
   }

   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y);
   }

   public static double length(double var0, double var2) {
      return Math.sqrt(var0 * var0 + var2 * var2);
   }

   public double distance(Vector2dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      return Math.sqrt(var2 * var2 + var4 * var4);
   }

   public double distanceSquared(Vector2dc var1) {
      double var2 = this.x - var1.x();
      double var4 = this.y - var1.y();
      return var2 * var2 + var4 * var4;
   }

   public double distance(Vector2fc var1) {
      double var2 = this.x - (double)var1.x();
      double var4 = this.y - (double)var1.y();
      return Math.sqrt(var2 * var2 + var4 * var4);
   }

   public double distanceSquared(Vector2fc var1) {
      double var2 = this.x - (double)var1.x();
      double var4 = this.y - (double)var1.y();
      return var2 * var2 + var4 * var4;
   }

   public double distance(double var1, double var3) {
      double var5 = this.x - var1;
      double var7 = this.y - var3;
      return Math.sqrt(var5 * var5 + var7 * var7);
   }

   public double distanceSquared(double var1, double var3) {
      double var5 = this.x - var1;
      double var7 = this.y - var3;
      return var5 * var5 + var7 * var7;
   }

   public static double distance(double var0, double var2, double var4, double var6) {
      double var8 = var0 - var4;
      double var10 = var2 - var6;
      return Math.sqrt(var8 * var8 + var10 * var10);
   }

   public static double distanceSquared(double var0, double var2, double var4, double var6) {
      double var8 = var0 - var4;
      double var10 = var2 - var6;
      return var8 * var8 + var10 * var10;
   }

   public Vector2d normalize() {
      double var1 = Math.invsqrt(this.x * this.x + this.y * this.y);
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public Vector2d normalize(Vector2d var1) {
      double var2 = Math.invsqrt(this.x * this.x + this.y * this.y);
      var1.x = this.x * var2;
      var1.y = this.y * var2;
      return var1;
   }

   public Vector2d normalize(double var1) {
      double var3 = Math.invsqrt(this.x * this.x + this.y * this.y) * var1;
      this.x *= var3;
      this.y *= var3;
      return this;
   }

   public Vector2d normalize(double var1, Vector2d var3) {
      double var4 = Math.invsqrt(this.x * this.x + this.y * this.y) * var1;
      var3.x = this.x * var4;
      var3.y = this.y * var4;
      return var3;
   }

   public Vector2d add(Vector2dc var1) {
      this.x += var1.x();
      this.y += var1.y();
      return this;
   }

   public Vector2d add(double var1, double var3) {
      this.x += var1;
      this.y += var3;
      return this;
   }

   public Vector2d add(double var1, double var3, Vector2d var5) {
      var5.x = this.x + var1;
      var5.y = this.y + var3;
      return var5;
   }

   public Vector2d add(Vector2fc var1) {
      this.x += (double)var1.x();
      this.y += (double)var1.y();
      return this;
   }

   public Vector2d add(Vector2dc var1, Vector2d var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      return var2;
   }

   public Vector2d add(Vector2fc var1, Vector2d var2) {
      var2.x = this.x + (double)var1.x();
      var2.y = this.y + (double)var1.y();
      return var2;
   }

   public Vector2d zero() {
      this.x = 0.0D;
      this.y = 0.0D;
      return this;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.x);
      var1.writeDouble(this.y);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readDouble();
      this.y = var1.readDouble();
   }

   public Vector2d negate() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public Vector2d negate(Vector2d var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      return var1;
   }

   public Vector2d lerp(Vector2dc var1, double var2) {
      this.x += (var1.x() - this.x) * var2;
      this.y += (var1.y() - this.y) * var2;
      return this;
   }

   public Vector2d lerp(Vector2dc var1, double var2, Vector2d var4) {
      var4.x = this.x + (var1.x() - this.x) * var2;
      var4.y = this.y + (var1.y() - this.y) * var2;
      return var4;
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.x);
      int var4 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.y);
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
         Vector2d var2 = (Vector2d)var1;
         if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var2.x)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.y) == Double.doubleToLongBits(var2.y);
         }
      }
   }

   public boolean equals(Vector2dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Vector2dc)) {
         return false;
      } else if (!Runtime.equals(this.x, var1.x(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.y, var1.y(), var2);
      }
   }

   public boolean equals(double var1, double var3) {
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var1)) {
         return false;
      } else {
         return Double.doubleToLongBits(this.y) == Double.doubleToLongBits(var3);
      }
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = Runtime.format(this.x, var1);
      return "(" + var10000 + " " + Runtime.format(this.y, var1) + ")";
   }

   public Vector2d fma(Vector2dc var1, Vector2dc var2) {
      this.x += var1.x() * var2.x();
      this.y += var1.y() * var2.y();
      return this;
   }

   public Vector2d fma(double var1, Vector2dc var3) {
      this.x += var1 * var3.x();
      this.y += var1 * var3.y();
      return this;
   }

   public Vector2d fma(Vector2dc var1, Vector2dc var2, Vector2d var3) {
      var3.x = this.x + var1.x() * var2.x();
      var3.y = this.y + var1.y() * var2.y();
      return var3;
   }

   public Vector2d fma(double var1, Vector2dc var3, Vector2d var4) {
      var4.x = this.x + var1 * var3.x();
      var4.y = this.y + var1 * var3.y();
      return var4;
   }

   public Vector2d min(Vector2dc var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2d min(Vector2dc var1, Vector2d var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      return var2;
   }

   public Vector2d max(Vector2dc var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2d max(Vector2dc var1, Vector2d var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      return var2;
   }

   public int maxComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      return var1 >= var3 ? 0 : 1;
   }

   public int minComponent() {
      double var1 = Math.abs(this.x);
      double var3 = Math.abs(this.y);
      return var1 < var3 ? 0 : 1;
   }

   public Vector2d floor() {
      this.x = Math.floor(this.x);
      this.y = Math.floor(this.y);
      return this;
   }

   public Vector2d floor(Vector2d var1) {
      var1.x = Math.floor(this.x);
      var1.y = Math.floor(this.y);
      return var1;
   }

   public Vector2d ceil() {
      this.x = Math.ceil(this.x);
      this.y = Math.ceil(this.y);
      return this;
   }

   public Vector2d ceil(Vector2d var1) {
      var1.x = Math.ceil(this.x);
      var1.y = Math.ceil(this.y);
      return var1;
   }

   public Vector2d round() {
      this.x = (double)Math.round(this.x);
      this.y = (double)Math.round(this.y);
      return this;
   }

   public Vector2d round(Vector2d var1) {
      var1.x = (double)Math.round(this.x);
      var1.y = (double)Math.round(this.y);
      return var1;
   }

   public boolean isFinite() {
      return Math.isFinite(this.x) && Math.isFinite(this.y);
   }

   public Vector2d absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      return this;
   }

   public Vector2d absolute(Vector2d var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      return var1;
   }
}
