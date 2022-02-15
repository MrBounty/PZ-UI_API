package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix2d implements Externalizable, Matrix2dc {
   private static final long serialVersionUID = 1L;
   public double m00;
   public double m01;
   public double m10;
   public double m11;

   public Matrix2d() {
      this.m00 = 1.0D;
      this.m11 = 1.0D;
   }

   public Matrix2d(Matrix2dc var1) {
      if (var1 instanceof Matrix2d) {
         MemUtil.INSTANCE.copy((Matrix2d)var1, this);
      } else {
         this.setMatrix2dc(var1);
      }

   }

   public Matrix2d(Matrix2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
   }

   public Matrix2d(Matrix3dc var1) {
      if (var1 instanceof Matrix3d) {
         MemUtil.INSTANCE.copy((Matrix3d)var1, this);
      } else {
         this.setMatrix3dc(var1);
      }

   }

   public Matrix2d(Matrix3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
   }

   public Matrix2d(double var1, double var3, double var5, double var7) {
      this.m00 = var1;
      this.m01 = var3;
      this.m10 = var5;
      this.m11 = var7;
   }

   public Matrix2d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Matrix2d(Vector2dc var1, Vector2dc var2) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m10 = var2.x();
      this.m11 = var2.y();
   }

   public double m00() {
      return this.m00;
   }

   public double m01() {
      return this.m01;
   }

   public double m10() {
      return this.m10;
   }

   public double m11() {
      return this.m11;
   }

   public Matrix2d m00(double var1) {
      this.m00 = var1;
      return this;
   }

   public Matrix2d m01(double var1) {
      this.m01 = var1;
      return this;
   }

   public Matrix2d m10(double var1) {
      this.m10 = var1;
      return this;
   }

   public Matrix2d m11(double var1) {
      this.m11 = var1;
      return this;
   }

   Matrix2d _m00(double var1) {
      this.m00 = var1;
      return this;
   }

   Matrix2d _m01(double var1) {
      this.m01 = var1;
      return this;
   }

   Matrix2d _m10(double var1) {
      this.m10 = var1;
      return this;
   }

   Matrix2d _m11(double var1) {
      this.m11 = var1;
      return this;
   }

   public Matrix2d set(Matrix2dc var1) {
      if (var1 instanceof Matrix2d) {
         MemUtil.INSTANCE.copy((Matrix2d)var1, this);
      } else {
         this.setMatrix2dc(var1);
      }

      return this;
   }

   private void setMatrix2dc(Matrix2dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
   }

   public Matrix2d set(Matrix2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      return this;
   }

   public Matrix2d set(Matrix3x2dc var1) {
      if (var1 instanceof Matrix3x2d) {
         MemUtil.INSTANCE.copy((Matrix3x2d)var1, this);
      } else {
         this.setMatrix3x2dc(var1);
      }

      return this;
   }

   private void setMatrix3x2dc(Matrix3x2dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
   }

   public Matrix2d set(Matrix3x2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      return this;
   }

   public Matrix2d set(Matrix3dc var1) {
      if (var1 instanceof Matrix3d) {
         MemUtil.INSTANCE.copy((Matrix3d)var1, this);
      } else {
         this.setMatrix3dc(var1);
      }

      return this;
   }

   private void setMatrix3dc(Matrix3dc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
   }

   public Matrix2d set(Matrix3fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      return this;
   }

   public Matrix2d mul(Matrix2dc var1) {
      return this.mul(var1, this);
   }

   public Matrix2d mul(Matrix2dc var1, Matrix2d var2) {
      double var3 = this.m00 * var1.m00() + this.m10 * var1.m01();
      double var5 = this.m01 * var1.m00() + this.m11 * var1.m01();
      double var7 = this.m00 * var1.m10() + this.m10 * var1.m11();
      double var9 = this.m01 * var1.m10() + this.m11 * var1.m11();
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m10 = var7;
      var2.m11 = var9;
      return var2;
   }

   public Matrix2d mul(Matrix2fc var1) {
      return this.mul(var1, this);
   }

   public Matrix2d mul(Matrix2fc var1, Matrix2d var2) {
      double var3 = this.m00 * (double)var1.m00() + this.m10 * (double)var1.m01();
      double var5 = this.m01 * (double)var1.m00() + this.m11 * (double)var1.m01();
      double var7 = this.m00 * (double)var1.m10() + this.m10 * (double)var1.m11();
      double var9 = this.m01 * (double)var1.m10() + this.m11 * (double)var1.m11();
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m10 = var7;
      var2.m11 = var9;
      return var2;
   }

   public Matrix2d mulLocal(Matrix2dc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix2d mulLocal(Matrix2dc var1, Matrix2d var2) {
      double var3 = var1.m00() * this.m00 + var1.m10() * this.m01;
      double var5 = var1.m01() * this.m00 + var1.m11() * this.m01;
      double var7 = var1.m00() * this.m10 + var1.m10() * this.m11;
      double var9 = var1.m01() * this.m10 + var1.m11() * this.m11;
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m10 = var7;
      var2.m11 = var9;
      return var2;
   }

   public Matrix2d set(double var1, double var3, double var5, double var7) {
      this.m00 = var1;
      this.m01 = var3;
      this.m10 = var5;
      this.m11 = var7;
      return this;
   }

   public Matrix2d set(double[] var1) {
      MemUtil.INSTANCE.copy((double[])var1, 0, (Matrix2d)this);
      return this;
   }

   public Matrix2d set(Vector2dc var1, Vector2dc var2) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m10 = var2.x();
      this.m11 = var2.y();
      return this;
   }

   public double determinant() {
      return this.m00 * this.m11 - this.m10 * this.m01;
   }

   public Matrix2d invert() {
      return this.invert(this);
   }

   public Matrix2d invert(Matrix2d var1) {
      double var2 = 1.0D / this.determinant();
      double var4 = this.m11 * var2;
      double var6 = -this.m01 * var2;
      double var8 = -this.m10 * var2;
      double var10 = this.m00 * var2;
      var1.m00 = var4;
      var1.m01 = var6;
      var1.m10 = var8;
      var1.m11 = var10;
      return var1;
   }

   public Matrix2d transpose() {
      return this.transpose(this);
   }

   public Matrix2d transpose(Matrix2d var1) {
      var1.set(this.m00, this.m10, this.m01, this.m11);
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
      return var10000 + " " + Runtime.format(this.m10, var1) + "\n" + Runtime.format(this.m01, var1) + " " + Runtime.format(this.m11, var1) + "\n";
   }

   public Matrix2d get(Matrix2d var1) {
      return var1.set((Matrix2dc)this);
   }

   public Matrix3x2d get(Matrix3x2d var1) {
      return var1.set((Matrix2dc)this);
   }

   public Matrix3d get(Matrix3d var1) {
      return var1.set((Matrix2dc)this);
   }

   public double getRotation() {
      return Math.atan2(this.m01, this.m11);
   }

   public DoubleBuffer get(DoubleBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public DoubleBuffer get(int var1, DoubleBuffer var2) {
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

   public DoubleBuffer getTransposed(DoubleBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public DoubleBuffer getTransposed(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public ByteBuffer getTransposed(ByteBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public ByteBuffer getTransposed(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.putTransposed(this, var1, var2);
      return var2;
   }

   public Matrix2dc getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public double[] get(double[] var1, int var2) {
      MemUtil.INSTANCE.copy(this, var1, var2);
      return var1;
   }

   public double[] get(double[] var1) {
      return this.get(var1, 0);
   }

   public Matrix2d set(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix2d set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix2d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix2d zero() {
      MemUtil.INSTANCE.zero(this);
      return this;
   }

   public Matrix2d identity() {
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      return this;
   }

   public Matrix2d scale(Vector2dc var1, Matrix2d var2) {
      return this.scale(var1.x(), var1.y(), var2);
   }

   public Matrix2d scale(Vector2dc var1) {
      return this.scale(var1.x(), var1.y(), this);
   }

   public Matrix2d scale(double var1, double var3, Matrix2d var5) {
      var5.m00 = this.m00 * var1;
      var5.m01 = this.m01 * var1;
      var5.m10 = this.m10 * var3;
      var5.m11 = this.m11 * var3;
      return var5;
   }

   public Matrix2d scale(double var1, double var3) {
      return this.scale(var1, var3, this);
   }

   public Matrix2d scale(double var1, Matrix2d var3) {
      return this.scale(var1, var1, var3);
   }

   public Matrix2d scale(double var1) {
      return this.scale(var1, var1);
   }

   public Matrix2d scaleLocal(double var1, double var3, Matrix2d var5) {
      var5.m00 = var1 * this.m00;
      var5.m01 = var3 * this.m01;
      var5.m10 = var1 * this.m10;
      var5.m11 = var3 * this.m11;
      return var5;
   }

   public Matrix2d scaleLocal(double var1, double var3) {
      return this.scaleLocal(var1, var3, this);
   }

   public Matrix2d scaling(double var1) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var1;
      return this;
   }

   public Matrix2d scaling(double var1, double var3) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var3;
      return this;
   }

   public Matrix2d scaling(Vector2dc var1) {
      return this.scaling(var1.x(), var1.y());
   }

   public Matrix2d rotation(double var1) {
      double var3 = Math.sin(var1);
      double var5 = Math.cosFromSin(var3, var1);
      this.m00 = var5;
      this.m01 = var3;
      this.m10 = -var3;
      this.m11 = var5;
      return this;
   }

   public Vector2d transform(Vector2d var1) {
      return var1.mul((Matrix2dc)this);
   }

   public Vector2d transform(Vector2dc var1, Vector2d var2) {
      var1.mul((Matrix2dc)this, var2);
      return var2;
   }

   public Vector2d transform(double var1, double var3, Vector2d var5) {
      var5.set(this.m00 * var1 + this.m10 * var3, this.m01 * var1 + this.m11 * var3);
      return var5;
   }

   public Vector2d transformTranspose(Vector2d var1) {
      return var1.mulTranspose((Matrix2dc)this);
   }

   public Vector2d transformTranspose(Vector2dc var1, Vector2d var2) {
      var1.mulTranspose((Matrix2dc)this, var2);
      return var2;
   }

   public Vector2d transformTranspose(double var1, double var3, Vector2d var5) {
      var5.set(this.m00 * var1 + this.m01 * var3, this.m10 * var1 + this.m11 * var3);
      return var5;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.m00);
      var1.writeDouble(this.m01);
      var1.writeDouble(this.m10);
      var1.writeDouble(this.m11);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readDouble();
      this.m01 = var1.readDouble();
      this.m10 = var1.readDouble();
      this.m11 = var1.readDouble();
   }

   public Matrix2d rotate(double var1) {
      return this.rotate(var1, this);
   }

   public Matrix2d rotate(double var1, Matrix2d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = this.m00 * var6 + this.m10 * var4;
      double var10 = this.m01 * var6 + this.m11 * var4;
      double var12 = this.m10 * var6 - this.m00 * var4;
      double var14 = this.m11 * var6 - this.m01 * var4;
      var3.m00 = var8;
      var3.m01 = var10;
      var3.m10 = var12;
      var3.m11 = var14;
      return var3;
   }

   public Matrix2d rotateLocal(double var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix2d rotateLocal(double var1, Matrix2d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 - var4 * this.m01;
      double var10 = var4 * this.m00 + var6 * this.m01;
      double var12 = var6 * this.m10 - var4 * this.m11;
      double var14 = var4 * this.m10 + var6 * this.m11;
      var3.m00 = var8;
      var3.m01 = var10;
      var3.m10 = var12;
      var3.m11 = var14;
      return var3;
   }

   public Vector2d getRow(int var1, Vector2d var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         var2.x = this.m00;
         var2.y = this.m10;
         break;
      case 1:
         var2.x = this.m01;
         var2.y = this.m11;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return var2;
   }

   public Matrix2d setRow(int var1, Vector2dc var2) throws IndexOutOfBoundsException {
      return this.setRow(var1, var2.x(), var2.y());
   }

   public Matrix2d setRow(int var1, double var2, double var4) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var4;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var4;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public Vector2d getColumn(int var1, Vector2d var2) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         var2.x = this.m00;
         var2.y = this.m01;
         break;
      case 1:
         var2.x = this.m10;
         var2.y = this.m11;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return var2;
   }

   public Matrix2d setColumn(int var1, Vector2dc var2) throws IndexOutOfBoundsException {
      return this.setColumn(var1, var2.x(), var2.y());
   }

   public Matrix2d setColumn(int var1, double var2, double var4) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var4;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var4;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public double get(int var1, int var2) {
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
            return this.m00;
         case 1:
            return this.m01;
         default:
            throw new IndexOutOfBoundsException();
         }
      case 1:
         switch(var2) {
         case 0:
            return this.m10;
         case 1:
            return this.m11;
         }
      }

      throw new IndexOutOfBoundsException();
   }

   public Matrix2d set(int var1, int var2, double var3) {
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
            this.m00 = var3;
            return this;
         case 1:
            this.m01 = var3;
            return this;
         default:
            throw new IndexOutOfBoundsException();
         }
      case 1:
         switch(var2) {
         case 0:
            this.m10 = var3;
            return this;
         case 1:
            this.m11 = var3;
            return this;
         }
      }

      throw new IndexOutOfBoundsException();
   }

   public Matrix2d normal() {
      return this.normal(this);
   }

   public Matrix2d normal(Matrix2d var1) {
      double var2 = this.m00 * this.m11 - this.m10 * this.m01;
      double var4 = 1.0D / var2;
      double var6 = this.m11 * var4;
      double var8 = -this.m10 * var4;
      double var10 = -this.m01 * var4;
      double var12 = this.m00 * var4;
      var1.m00 = var6;
      var1.m01 = var8;
      var1.m10 = var10;
      var1.m11 = var12;
      return var1;
   }

   public Vector2d getScale(Vector2d var1) {
      var1.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01);
      var1.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11);
      return var1;
   }

   public Vector2d positiveX(Vector2d var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = -this.m11;
         var1.y = this.m01;
      } else {
         var1.x = this.m11;
         var1.y = -this.m01;
      }

      return var1.normalize(var1);
   }

   public Vector2d normalizedPositiveX(Vector2d var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = -this.m11;
         var1.y = this.m01;
      } else {
         var1.x = this.m11;
         var1.y = -this.m01;
      }

      return var1;
   }

   public Vector2d positiveY(Vector2d var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = this.m10;
         var1.y = -this.m00;
      } else {
         var1.x = -this.m10;
         var1.y = this.m00;
      }

      return var1.normalize(var1);
   }

   public Vector2d normalizedPositiveY(Vector2d var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = this.m10;
         var1.y = -this.m00;
      } else {
         var1.x = -this.m10;
         var1.y = this.m00;
      }

      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.m00);
      int var4 = 31 * var1 + (int)(var2 >>> 32 ^ var2);
      var2 = Double.doubleToLongBits(this.m01);
      var4 = 31 * var4 + (int)(var2 >>> 32 ^ var2);
      var2 = Double.doubleToLongBits(this.m10);
      var4 = 31 * var4 + (int)(var2 >>> 32 ^ var2);
      var2 = Double.doubleToLongBits(this.m11);
      var4 = 31 * var4 + (int)(var2 >>> 32 ^ var2);
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
         Matrix2d var2 = (Matrix2d)var1;
         if (Double.doubleToLongBits(this.m00) != Double.doubleToLongBits(var2.m00)) {
            return false;
         } else if (Double.doubleToLongBits(this.m01) != Double.doubleToLongBits(var2.m01)) {
            return false;
         } else if (Double.doubleToLongBits(this.m10) != Double.doubleToLongBits(var2.m10)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(var2.m11);
         }
      }
   }

   public boolean equals(Matrix2dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix2d)) {
         return false;
      } else if (!Runtime.equals(this.m00, var1.m00(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m01, var1.m01(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m10, var1.m10(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.m11, var1.m11(), var2);
      }
   }

   public Matrix2d swap(Matrix2d var1) {
      MemUtil.INSTANCE.swap(this, var1);
      return this;
   }

   public Matrix2d add(Matrix2dc var1) {
      return this.add(var1, this);
   }

   public Matrix2d add(Matrix2dc var1, Matrix2d var2) {
      var2.m00 = this.m00 + var1.m00();
      var2.m01 = this.m01 + var1.m01();
      var2.m10 = this.m10 + var1.m10();
      var2.m11 = this.m11 + var1.m11();
      return var2;
   }

   public Matrix2d sub(Matrix2dc var1) {
      return this.sub(var1, this);
   }

   public Matrix2d sub(Matrix2dc var1, Matrix2d var2) {
      var2.m00 = this.m00 - var1.m00();
      var2.m01 = this.m01 - var1.m01();
      var2.m10 = this.m10 - var1.m10();
      var2.m11 = this.m11 - var1.m11();
      return var2;
   }

   public Matrix2d mulComponentWise(Matrix2dc var1) {
      return this.sub(var1, this);
   }

   public Matrix2d mulComponentWise(Matrix2dc var1, Matrix2d var2) {
      var2.m00 = this.m00 * var1.m00();
      var2.m01 = this.m01 * var1.m01();
      var2.m10 = this.m10 * var1.m10();
      var2.m11 = this.m11 * var1.m11();
      return var2;
   }

   public Matrix2d lerp(Matrix2dc var1, double var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix2d lerp(Matrix2dc var1, double var2, Matrix2d var4) {
      var4.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var4.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var4.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var4.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      return var4;
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11);
   }
}
