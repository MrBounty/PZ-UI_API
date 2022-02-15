package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix3x2d implements Matrix3x2dc, Externalizable {
   private static final long serialVersionUID = 1L;
   public double m00;
   public double m01;
   public double m10;
   public double m11;
   public double m20;
   public double m21;

   public Matrix3x2d() {
      this.m00 = 1.0D;
      this.m11 = 1.0D;
   }

   public Matrix3x2d(Matrix2dc var1) {
      if (var1 instanceof Matrix2d) {
         MemUtil.INSTANCE.copy((Matrix2d)var1, this);
      } else {
         this.setMatrix2dc(var1);
      }

   }

   public Matrix3x2d(Matrix2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
   }

   public Matrix3x2d(Matrix3x2dc var1) {
      if (var1 instanceof Matrix3x2d) {
         MemUtil.INSTANCE.copy((Matrix3x2d)var1, this);
      } else {
         this.setMatrix3x2dc(var1);
      }

   }

   public Matrix3x2d(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.m00 = var1;
      this.m01 = var3;
      this.m10 = var5;
      this.m11 = var7;
      this.m20 = var9;
      this.m21 = var11;
   }

   public Matrix3x2d(DoubleBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
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

   public double m20() {
      return this.m20;
   }

   public double m21() {
      return this.m21;
   }

   Matrix3x2d _m00(double var1) {
      this.m00 = var1;
      return this;
   }

   Matrix3x2d _m01(double var1) {
      this.m01 = var1;
      return this;
   }

   Matrix3x2d _m10(double var1) {
      this.m10 = var1;
      return this;
   }

   Matrix3x2d _m11(double var1) {
      this.m11 = var1;
      return this;
   }

   Matrix3x2d _m20(double var1) {
      this.m20 = var1;
      return this;
   }

   Matrix3x2d _m21(double var1) {
      this.m21 = var1;
      return this;
   }

   public Matrix3x2d set(Matrix3x2dc var1) {
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
      this.m20 = var1.m20();
      this.m21 = var1.m21();
   }

   public Matrix3x2d set(Matrix2dc var1) {
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

   public Matrix3x2d set(Matrix2fc var1) {
      this.m00 = (double)var1.m00();
      this.m01 = (double)var1.m01();
      this.m10 = (double)var1.m10();
      this.m11 = (double)var1.m11();
      return this;
   }

   public Matrix3x2d mul(Matrix3x2dc var1) {
      return this.mul(var1, this);
   }

   public Matrix3x2d mul(Matrix3x2dc var1, Matrix3x2d var2) {
      double var3 = this.m00 * var1.m00() + this.m10 * var1.m01();
      double var5 = this.m01 * var1.m00() + this.m11 * var1.m01();
      double var7 = this.m00 * var1.m10() + this.m10 * var1.m11();
      double var9 = this.m01 * var1.m10() + this.m11 * var1.m11();
      double var11 = this.m00 * var1.m20() + this.m10 * var1.m21() + this.m20;
      double var13 = this.m01 * var1.m20() + this.m11 * var1.m21() + this.m21;
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m10 = var7;
      var2.m11 = var9;
      var2.m20 = var11;
      var2.m21 = var13;
      return var2;
   }

   public Matrix3x2d mulLocal(Matrix3x2dc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix3x2d mulLocal(Matrix3x2dc var1, Matrix3x2d var2) {
      double var3 = var1.m00() * this.m00 + var1.m10() * this.m01;
      double var5 = var1.m01() * this.m00 + var1.m11() * this.m01;
      double var7 = var1.m00() * this.m10 + var1.m10() * this.m11;
      double var9 = var1.m01() * this.m10 + var1.m11() * this.m11;
      double var11 = var1.m00() * this.m20 + var1.m10() * this.m21 + var1.m20();
      double var13 = var1.m01() * this.m20 + var1.m11() * this.m21 + var1.m21();
      var2.m00 = var3;
      var2.m01 = var5;
      var2.m10 = var7;
      var2.m11 = var9;
      var2.m20 = var11;
      var2.m21 = var13;
      return var2;
   }

   public Matrix3x2d set(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.m00 = var1;
      this.m01 = var3;
      this.m10 = var5;
      this.m11 = var7;
      this.m20 = var9;
      this.m21 = var11;
      return this;
   }

   public Matrix3x2d set(double[] var1) {
      MemUtil.INSTANCE.copy((double[])var1, 0, (Matrix3x2d)this);
      return this;
   }

   public double determinant() {
      return this.m00 * this.m11 - this.m01 * this.m10;
   }

   public Matrix3x2d invert() {
      return this.invert(this);
   }

   public Matrix3x2d invert(Matrix3x2d var1) {
      double var2 = 1.0D / (this.m00 * this.m11 - this.m01 * this.m10);
      double var4 = this.m11 * var2;
      double var6 = -this.m01 * var2;
      double var8 = -this.m10 * var2;
      double var10 = this.m00 * var2;
      double var12 = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      double var14 = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      var1.m00 = var4;
      var1.m01 = var6;
      var1.m10 = var8;
      var1.m11 = var10;
      var1.m20 = var12;
      var1.m21 = var14;
      return var1;
   }

   public Matrix3x2d translation(double var1, double var3) {
      this.m00 = 1.0D;
      this.m01 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 1.0D;
      this.m20 = var1;
      this.m21 = var3;
      return this;
   }

   public Matrix3x2d translation(Vector2dc var1) {
      return this.translation(var1.x(), var1.y());
   }

   public Matrix3x2d setTranslation(double var1, double var3) {
      this.m20 = var1;
      this.m21 = var3;
      return this;
   }

   public Matrix3x2d setTranslation(Vector2dc var1) {
      return this.setTranslation(var1.x(), var1.y());
   }

   public Matrix3x2d translate(double var1, double var3, Matrix3x2d var5) {
      var5.m20 = this.m00 * var1 + this.m10 * var3 + this.m20;
      var5.m21 = this.m01 * var1 + this.m11 * var3 + this.m21;
      var5.m00 = this.m00;
      var5.m01 = this.m01;
      var5.m10 = this.m10;
      var5.m11 = this.m11;
      return var5;
   }

   public Matrix3x2d translate(double var1, double var3) {
      return this.translate(var1, var3, this);
   }

   public Matrix3x2d translate(Vector2dc var1, Matrix3x2d var2) {
      return this.translate(var1.x(), var1.y(), var2);
   }

   public Matrix3x2d translate(Vector2dc var1) {
      return this.translate(var1.x(), var1.y(), this);
   }

   public Matrix3x2d translateLocal(Vector2dc var1) {
      return this.translateLocal(var1.x(), var1.y());
   }

   public Matrix3x2d translateLocal(Vector2dc var1, Matrix3x2d var2) {
      return this.translateLocal(var1.x(), var1.y(), var2);
   }

   public Matrix3x2d translateLocal(double var1, double var3, Matrix3x2d var5) {
      var5.m00 = this.m00;
      var5.m01 = this.m01;
      var5.m10 = this.m10;
      var5.m11 = this.m11;
      var5.m20 = this.m20 + var1;
      var5.m21 = this.m21 + var3;
      return var5;
   }

   public Matrix3x2d translateLocal(double var1, double var3) {
      return this.translateLocal(var1, var3, this);
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
      return var10000 + " " + Runtime.format(this.m10, var1) + " " + Runtime.format(this.m20, var1) + "\n" + Runtime.format(this.m01, var1) + " " + Runtime.format(this.m11, var1) + " " + Runtime.format(this.m21, var1) + "\n";
   }

   public Matrix3x2d get(Matrix3x2d var1) {
      return var1.set((Matrix3x2dc)this);
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

   public DoubleBuffer get3x3(DoubleBuffer var1) {
      MemUtil.INSTANCE.put3x3((Matrix3x2d)this, 0, (DoubleBuffer)var1);
      return var1;
   }

   public DoubleBuffer get3x3(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put3x3(this, var1, var2);
      return var2;
   }

   public ByteBuffer get3x3(ByteBuffer var1) {
      MemUtil.INSTANCE.put3x3((Matrix3x2d)this, 0, (ByteBuffer)var1);
      return var1;
   }

   public ByteBuffer get3x3(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put3x3(this, var1, var2);
      return var2;
   }

   public DoubleBuffer get4x4(DoubleBuffer var1) {
      MemUtil.INSTANCE.put4x4((Matrix3x2d)this, 0, (DoubleBuffer)var1);
      return var1;
   }

   public DoubleBuffer get4x4(int var1, DoubleBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public ByteBuffer get4x4(ByteBuffer var1) {
      MemUtil.INSTANCE.put4x4((Matrix3x2d)this, 0, (ByteBuffer)var1);
      return var1;
   }

   public ByteBuffer get4x4(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public Matrix3x2dc getToAddress(long var1) {
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

   public double[] get3x3(double[] var1, int var2) {
      MemUtil.INSTANCE.copy3x3(this, var1, var2);
      return var1;
   }

   public double[] get3x3(double[] var1) {
      return this.get3x3(var1, 0);
   }

   public double[] get4x4(double[] var1, int var2) {
      MemUtil.INSTANCE.copy4x4(this, var1, var2);
      return var1;
   }

   public double[] get4x4(double[] var1) {
      return this.get4x4(var1, 0);
   }

   public Matrix3x2d set(DoubleBuffer var1) {
      int var2 = var1.position();
      MemUtil.INSTANCE.get(this, var2, var1);
      return this;
   }

   public Matrix3x2d set(ByteBuffer var1) {
      int var2 = var1.position();
      MemUtil.INSTANCE.get(this, var2, var1);
      return this;
   }

   public Matrix3x2d setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix3x2d zero() {
      MemUtil.INSTANCE.zero(this);
      return this;
   }

   public Matrix3x2d identity() {
      MemUtil.INSTANCE.identity(this);
      return this;
   }

   public Matrix3x2d scale(double var1, double var3, Matrix3x2d var5) {
      var5.m00 = this.m00 * var1;
      var5.m01 = this.m01 * var1;
      var5.m10 = this.m10 * var3;
      var5.m11 = this.m11 * var3;
      var5.m20 = this.m20;
      var5.m21 = this.m21;
      return var5;
   }

   public Matrix3x2d scale(double var1, double var3) {
      return this.scale(var1, var3, this);
   }

   public Matrix3x2d scale(Vector2dc var1) {
      return this.scale(var1.x(), var1.y(), this);
   }

   public Matrix3x2d scale(Vector2dc var1, Matrix3x2d var2) {
      return this.scale(var1.x(), var1.y(), var2);
   }

   public Matrix3x2d scale(Vector2fc var1) {
      return this.scale((double)var1.x(), (double)var1.y(), this);
   }

   public Matrix3x2d scale(Vector2fc var1, Matrix3x2d var2) {
      return this.scale((double)var1.x(), (double)var1.y(), var2);
   }

   public Matrix3x2d scale(double var1, Matrix3x2d var3) {
      return this.scale(var1, var1, var3);
   }

   public Matrix3x2d scale(double var1) {
      return this.scale(var1, var1);
   }

   public Matrix3x2d scaleLocal(double var1, double var3, Matrix3x2d var5) {
      var5.m00 = var1 * this.m00;
      var5.m01 = var3 * this.m01;
      var5.m10 = var1 * this.m10;
      var5.m11 = var3 * this.m11;
      var5.m20 = var1 * this.m20;
      var5.m21 = var3 * this.m21;
      return var5;
   }

   public Matrix3x2d scaleLocal(double var1, double var3) {
      return this.scaleLocal(var1, var3, this);
   }

   public Matrix3x2d scaleLocal(double var1, Matrix3x2d var3) {
      return this.scaleLocal(var1, var1, var3);
   }

   public Matrix3x2d scaleLocal(double var1) {
      return this.scaleLocal(var1, var1, this);
   }

   public Matrix3x2d scaleAround(double var1, double var3, double var5, double var7, Matrix3x2d var9) {
      double var10 = this.m00 * var5 + this.m10 * var7 + this.m20;
      double var12 = this.m01 * var5 + this.m11 * var7 + this.m21;
      var9.m00 = this.m00 * var1;
      var9.m01 = this.m01 * var1;
      var9.m10 = this.m10 * var3;
      var9.m11 = this.m11 * var3;
      var9.m20 = var9.m00 * -var5 + var9.m10 * -var7 + var10;
      var9.m21 = var9.m01 * -var5 + var9.m11 * -var7 + var12;
      return var9;
   }

   public Matrix3x2d scaleAround(double var1, double var3, double var5, double var7) {
      return this.scaleAround(var1, var3, var5, var7, this);
   }

   public Matrix3x2d scaleAround(double var1, double var3, double var5, Matrix3x2d var7) {
      return this.scaleAround(var1, var1, var3, var5, this);
   }

   public Matrix3x2d scaleAround(double var1, double var3, double var5) {
      return this.scaleAround(var1, var1, var3, var5, this);
   }

   public Matrix3x2d scaleAroundLocal(double var1, double var3, double var5, double var7, Matrix3x2d var9) {
      var9.m00 = var1 * this.m00;
      var9.m01 = var3 * this.m01;
      var9.m10 = var1 * this.m10;
      var9.m11 = var3 * this.m11;
      var9.m20 = var1 * this.m20 - var1 * var5 + var5;
      var9.m21 = var3 * this.m21 - var3 * var7 + var7;
      return var9;
   }

   public Matrix3x2d scaleAroundLocal(double var1, double var3, double var5, Matrix3x2d var7) {
      return this.scaleAroundLocal(var1, var1, var3, var5, var7);
   }

   public Matrix3x2d scaleAroundLocal(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.scaleAroundLocal(var1, var3, var7, var9, this);
   }

   public Matrix3x2d scaleAroundLocal(double var1, double var3, double var5) {
      return this.scaleAroundLocal(var1, var1, var3, var5, this);
   }

   public Matrix3x2d scaling(double var1) {
      return this.scaling(var1, var1);
   }

   public Matrix3x2d scaling(double var1, double var3) {
      this.m00 = var1;
      this.m01 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = var3;
      this.m20 = 0.0D;
      this.m21 = 0.0D;
      return this;
   }

   public Matrix3x2d rotation(double var1) {
      double var3 = Math.cos(var1);
      double var5 = Math.sin(var1);
      this.m00 = var3;
      this.m10 = -var5;
      this.m20 = 0.0D;
      this.m01 = var5;
      this.m11 = var3;
      this.m21 = 0.0D;
      return this;
   }

   public Vector3d transform(Vector3d var1) {
      return var1.mul((Matrix3x2dc)this);
   }

   public Vector3d transform(Vector3dc var1, Vector3d var2) {
      return var1.mul((Matrix3x2dc)this, (Vector3d)var2);
   }

   public Vector3d transform(double var1, double var3, double var5, Vector3d var7) {
      return var7.set(this.m00 * var1 + this.m10 * var3 + this.m20 * var5, this.m01 * var1 + this.m11 * var3 + this.m21 * var5, var5);
   }

   public Vector2d transformPosition(Vector2d var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20, this.m01 * var1.x + this.m11 * var1.y + this.m21);
      return var1;
   }

   public Vector2d transformPosition(Vector2dc var1, Vector2d var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20, this.m01 * var1.x() + this.m11 * var1.y() + this.m21);
      return var2;
   }

   public Vector2d transformPosition(double var1, double var3, Vector2d var5) {
      return var5.set(this.m00 * var1 + this.m10 * var3 + this.m20, this.m01 * var1 + this.m11 * var3 + this.m21);
   }

   public Vector2d transformDirection(Vector2d var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y, this.m01 * var1.x + this.m11 * var1.y);
      return var1;
   }

   public Vector2d transformDirection(Vector2dc var1, Vector2d var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y(), this.m01 * var1.x() + this.m11 * var1.y());
      return var2;
   }

   public Vector2d transformDirection(double var1, double var3, Vector2d var5) {
      return var5.set(this.m00 * var1 + this.m10 * var3, this.m01 * var1 + this.m11 * var3);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeDouble(this.m00);
      var1.writeDouble(this.m01);
      var1.writeDouble(this.m10);
      var1.writeDouble(this.m11);
      var1.writeDouble(this.m20);
      var1.writeDouble(this.m21);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readDouble();
      this.m01 = var1.readDouble();
      this.m10 = var1.readDouble();
      this.m11 = var1.readDouble();
      this.m20 = var1.readDouble();
      this.m21 = var1.readDouble();
   }

   public Matrix3x2d rotate(double var1) {
      return this.rotate(var1, this);
   }

   public Matrix3x2d rotate(double var1, Matrix3x2d var3) {
      double var4 = Math.cos(var1);
      double var6 = Math.sin(var1);
      double var12 = -var6;
      double var16 = this.m00 * var4 + this.m10 * var6;
      double var18 = this.m01 * var4 + this.m11 * var6;
      var3.m10 = this.m00 * var12 + this.m10 * var4;
      var3.m11 = this.m01 * var12 + this.m11 * var4;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      return var3;
   }

   public Matrix3x2d rotateLocal(double var1, Matrix3x2d var3) {
      double var4 = Math.sin(var1);
      double var6 = Math.cosFromSin(var4, var1);
      double var8 = var6 * this.m00 - var4 * this.m01;
      double var10 = var4 * this.m00 + var6 * this.m01;
      double var12 = var6 * this.m10 - var4 * this.m11;
      double var14 = var4 * this.m10 + var6 * this.m11;
      double var16 = var6 * this.m20 - var4 * this.m21;
      double var18 = var4 * this.m20 + var6 * this.m21;
      var3.m00 = var8;
      var3.m01 = var10;
      var3.m10 = var12;
      var3.m11 = var14;
      var3.m20 = var16;
      var3.m21 = var18;
      return var3;
   }

   public Matrix3x2d rotateLocal(double var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix3x2d rotateAbout(double var1, double var3, double var5) {
      return this.rotateAbout(var1, var3, var5, this);
   }

   public Matrix3x2d rotateAbout(double var1, double var3, double var5, Matrix3x2d var7) {
      double var8 = this.m00 * var3 + this.m10 * var5 + this.m20;
      double var10 = this.m01 * var3 + this.m11 * var5 + this.m21;
      double var12 = Math.cos(var1);
      double var14 = Math.sin(var1);
      double var16 = this.m00 * var12 + this.m10 * var14;
      double var18 = this.m01 * var12 + this.m11 * var14;
      var7.m10 = this.m00 * -var14 + this.m10 * var12;
      var7.m11 = this.m01 * -var14 + this.m11 * var12;
      var7.m00 = var16;
      var7.m01 = var18;
      var7.m20 = var7.m00 * -var3 + var7.m10 * -var5 + var8;
      var7.m21 = var7.m01 * -var3 + var7.m11 * -var5 + var10;
      return var7;
   }

   public Matrix3x2d rotateTo(Vector2dc var1, Vector2dc var2, Matrix3x2d var3) {
      double var4 = var1.x() * var2.x() + var1.y() * var2.y();
      double var6 = var1.x() * var2.y() - var1.y() * var2.x();
      double var12 = -var6;
      double var16 = this.m00 * var4 + this.m10 * var6;
      double var18 = this.m01 * var4 + this.m11 * var6;
      var3.m10 = this.m00 * var12 + this.m10 * var4;
      var3.m11 = this.m01 * var12 + this.m11 * var4;
      var3.m00 = var16;
      var3.m01 = var18;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      return var3;
   }

   public Matrix3x2d rotateTo(Vector2dc var1, Vector2dc var2) {
      return this.rotateTo(var1, var2, this);
   }

   public Matrix3x2d view(double var1, double var3, double var5, double var7, Matrix3x2d var9) {
      double var10 = 2.0D / (var3 - var1);
      double var12 = 2.0D / (var7 - var5);
      double var14 = (var1 + var3) / (var1 - var3);
      double var16 = (var5 + var7) / (var5 - var7);
      var9.m20 = this.m00 * var14 + this.m10 * var16 + this.m20;
      var9.m21 = this.m01 * var14 + this.m11 * var16 + this.m21;
      var9.m00 = this.m00 * var10;
      var9.m01 = this.m01 * var10;
      var9.m10 = this.m10 * var12;
      var9.m11 = this.m11 * var12;
      return var9;
   }

   public Matrix3x2d view(double var1, double var3, double var5, double var7) {
      return this.view(var1, var3, var5, var7, this);
   }

   public Matrix3x2d setView(double var1, double var3, double var5, double var7) {
      this.m00 = 2.0D / (var3 - var1);
      this.m01 = 0.0D;
      this.m10 = 0.0D;
      this.m11 = 2.0D / (var7 - var5);
      this.m20 = (var1 + var3) / (var1 - var3);
      this.m21 = (var5 + var7) / (var5 - var7);
      return this;
   }

   public Vector2d origin(Vector2d var1) {
      double var2 = 1.0D / (this.m00 * this.m11 - this.m01 * this.m10);
      var1.x = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      var1.y = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      return var1;
   }

   public double[] viewArea(double[] var1) {
      double var2 = 1.0D / (this.m00 * this.m11 - this.m01 * this.m10);
      double var4 = this.m11 * var2;
      double var6 = -this.m01 * var2;
      double var8 = -this.m10 * var2;
      double var10 = this.m00 * var2;
      double var12 = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      double var14 = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      double var16 = -var4 - var8;
      double var18 = -var6 - var10;
      double var20 = var4 - var8;
      double var22 = var6 - var10;
      double var24 = -var4 + var8;
      double var26 = -var6 + var10;
      double var28 = var4 + var8;
      double var30 = var6 + var10;
      double var32 = var16 < var24 ? var16 : var24;
      var32 = var32 < var20 ? var32 : var20;
      var32 = var32 < var28 ? var32 : var28;
      double var34 = var18 < var26 ? var18 : var26;
      var34 = var34 < var22 ? var34 : var22;
      var34 = var34 < var30 ? var34 : var30;
      double var36 = var16 > var24 ? var16 : var24;
      var36 = var36 > var20 ? var36 : var20;
      var36 = var36 > var28 ? var36 : var28;
      double var38 = var18 > var26 ? var18 : var26;
      var38 = var38 > var22 ? var38 : var22;
      var38 = var38 > var30 ? var38 : var30;
      var1[0] = var32 + var12;
      var1[1] = var34 + var14;
      var1[2] = var36 + var12;
      var1[3] = var38 + var14;
      return var1;
   }

   public Vector2d positiveX(Vector2d var1) {
      double var2 = this.m00 * this.m11 - this.m01 * this.m10;
      var2 = 1.0D / var2;
      var1.x = this.m11 * var2;
      var1.y = -this.m01 * var2;
      return var1.normalize(var1);
   }

   public Vector2d normalizedPositiveX(Vector2d var1) {
      var1.x = this.m11;
      var1.y = -this.m01;
      return var1;
   }

   public Vector2d positiveY(Vector2d var1) {
      double var2 = this.m00 * this.m11 - this.m01 * this.m10;
      var2 = 1.0D / var2;
      var1.x = -this.m10 * var2;
      var1.y = this.m00 * var2;
      return var1.normalize(var1);
   }

   public Vector2d normalizedPositiveY(Vector2d var1) {
      var1.x = -this.m10;
      var1.y = this.m00;
      return var1;
   }

   public Vector2d unproject(double var1, double var3, int[] var5, Vector2d var6) {
      double var7 = 1.0D / (this.m00 * this.m11 - this.m01 * this.m10);
      double var9 = this.m11 * var7;
      double var11 = -this.m01 * var7;
      double var13 = -this.m10 * var7;
      double var15 = this.m00 * var7;
      double var17 = (this.m10 * this.m21 - this.m20 * this.m11) * var7;
      double var19 = (this.m20 * this.m01 - this.m00 * this.m21) * var7;
      double var21 = (var1 - (double)var5[0]) / (double)var5[2] * 2.0D - 1.0D;
      double var23 = (var3 - (double)var5[1]) / (double)var5[3] * 2.0D - 1.0D;
      var6.x = var9 * var21 + var13 * var23 + var17;
      var6.y = var11 * var21 + var15 * var23 + var19;
      return var6;
   }

   public Vector2d unprojectInv(double var1, double var3, int[] var5, Vector2d var6) {
      double var7 = (var1 - (double)var5[0]) / (double)var5[2] * 2.0D - 1.0D;
      double var9 = (var3 - (double)var5[1]) / (double)var5[3] * 2.0D - 1.0D;
      var6.x = this.m00 * var7 + this.m10 * var9 + this.m20;
      var6.y = this.m01 * var7 + this.m11 * var9 + this.m21;
      return var6;
   }

   public Matrix3x2d span(Vector2d var1, Vector2d var2, Vector2d var3) {
      double var4 = 1.0D / (this.m00 * this.m11 - this.m01 * this.m10);
      double var6 = this.m11 * var4;
      double var8 = -this.m01 * var4;
      double var10 = -this.m10 * var4;
      double var12 = this.m00 * var4;
      var1.x = -var6 - var10 + (this.m10 * this.m21 - this.m20 * this.m11) * var4;
      var1.y = -var8 - var12 + (this.m20 * this.m01 - this.m00 * this.m21) * var4;
      var2.x = 2.0D * var6;
      var2.y = 2.0D * var8;
      var3.x = 2.0D * var10;
      var3.y = 2.0D * var12;
      return this;
   }

   public boolean testPoint(double var1, double var3) {
      double var5 = this.m00;
      double var7 = this.m10;
      double var9 = 1.0D + this.m20;
      double var11 = -this.m00;
      double var13 = -this.m10;
      double var15 = 1.0D - this.m20;
      double var17 = this.m01;
      double var19 = this.m11;
      double var21 = 1.0D + this.m21;
      double var23 = -this.m01;
      double var25 = -this.m11;
      double var27 = 1.0D - this.m21;
      return var5 * var1 + var7 * var3 + var9 >= 0.0D && var11 * var1 + var13 * var3 + var15 >= 0.0D && var17 * var1 + var19 * var3 + var21 >= 0.0D && var23 * var1 + var25 * var3 + var27 >= 0.0D;
   }

   public boolean testCircle(double var1, double var3, double var5) {
      double var9 = this.m00;
      double var11 = this.m10;
      double var13 = 1.0D + this.m20;
      double var7 = Math.invsqrt(var9 * var9 + var11 * var11);
      var9 *= var7;
      var11 *= var7;
      var13 *= var7;
      double var15 = -this.m00;
      double var17 = -this.m10;
      double var19 = 1.0D - this.m20;
      var7 = Math.invsqrt(var15 * var15 + var17 * var17);
      var15 *= var7;
      var17 *= var7;
      var19 *= var7;
      double var21 = this.m01;
      double var23 = this.m11;
      double var25 = 1.0D + this.m21;
      var7 = Math.invsqrt(var21 * var21 + var23 * var23);
      var21 *= var7;
      var23 *= var7;
      var25 *= var7;
      double var27 = -this.m01;
      double var29 = -this.m11;
      double var31 = 1.0D - this.m21;
      var7 = Math.invsqrt(var27 * var27 + var29 * var29);
      var27 *= var7;
      var29 *= var7;
      var31 *= var7;
      return var9 * var1 + var11 * var3 + var13 >= -var5 && var15 * var1 + var17 * var3 + var19 >= -var5 && var21 * var1 + var23 * var3 + var25 >= -var5 && var27 * var1 + var29 * var3 + var31 >= -var5;
   }

   public boolean testAar(double var1, double var3, double var5, double var7) {
      double var9 = this.m00;
      double var11 = this.m10;
      double var13 = 1.0D + this.m20;
      double var15 = -this.m00;
      double var17 = -this.m10;
      double var19 = 1.0D - this.m20;
      double var21 = this.m01;
      double var23 = this.m11;
      double var25 = 1.0D + this.m21;
      double var27 = -this.m01;
      double var29 = -this.m11;
      double var31 = 1.0D - this.m21;
      return var9 * (var9 < 0.0D ? var1 : var5) + var11 * (var11 < 0.0D ? var3 : var7) >= -var13 && var15 * (var15 < 0.0D ? var1 : var5) + var17 * (var17 < 0.0D ? var3 : var7) >= -var19 && var21 * (var21 < 0.0D ? var1 : var5) + var23 * (var23 < 0.0D ? var3 : var7) >= -var25 && var27 * (var27 < 0.0D ? var1 : var5) + var29 * (var29 < 0.0D ? var3 : var7) >= -var31;
   }

   public int hashCode() {
      byte var1 = 1;
      long var2 = Double.doubleToLongBits(this.m00);
      int var4 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m01);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m10);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m11);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m20);
      var4 = 31 * var4 + (int)(var2 ^ var2 >>> 32);
      var2 = Double.doubleToLongBits(this.m21);
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
         Matrix3x2d var2 = (Matrix3x2d)var1;
         if (Double.doubleToLongBits(this.m00) != Double.doubleToLongBits(var2.m00)) {
            return false;
         } else if (Double.doubleToLongBits(this.m01) != Double.doubleToLongBits(var2.m01)) {
            return false;
         } else if (Double.doubleToLongBits(this.m10) != Double.doubleToLongBits(var2.m10)) {
            return false;
         } else if (Double.doubleToLongBits(this.m11) != Double.doubleToLongBits(var2.m11)) {
            return false;
         } else if (Double.doubleToLongBits(this.m20) != Double.doubleToLongBits(var2.m20)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.m21) == Double.doubleToLongBits(var2.m21);
         }
      }
   }

   public boolean equals(Matrix3x2dc var1, double var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix3x2d)) {
         return false;
      } else if (!Runtime.equals(this.m00, var1.m00(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m01, var1.m01(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m10, var1.m10(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m11, var1.m11(), var2)) {
         return false;
      } else if (!Runtime.equals(this.m20, var1.m20(), var2)) {
         return false;
      } else {
         return Runtime.equals(this.m21, var1.m21(), var2);
      }
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m20) && Math.isFinite(this.m21);
   }
}
