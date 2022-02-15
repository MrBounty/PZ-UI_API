package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix3x2f implements Matrix3x2fc, Externalizable {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m10;
   public float m11;
   public float m20;
   public float m21;

   public Matrix3x2f() {
      this.m00 = 1.0F;
      this.m11 = 1.0F;
   }

   public Matrix3x2f(Matrix3x2fc var1) {
      if (var1 instanceof Matrix3x2f) {
         MemUtil.INSTANCE.copy((Matrix3x2f)var1, this);
      } else {
         this.setMatrix3x2fc(var1);
      }

   }

   public Matrix3x2f(Matrix2fc var1) {
      if (var1 instanceof Matrix2f) {
         MemUtil.INSTANCE.copy((Matrix2f)var1, this);
      } else {
         this.setMatrix2fc(var1);
      }

   }

   public Matrix3x2f(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.m00 = var1;
      this.m01 = var2;
      this.m10 = var3;
      this.m11 = var4;
      this.m20 = var5;
      this.m21 = var6;
   }

   public Matrix3x2f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public float m00() {
      return this.m00;
   }

   public float m01() {
      return this.m01;
   }

   public float m10() {
      return this.m10;
   }

   public float m11() {
      return this.m11;
   }

   public float m20() {
      return this.m20;
   }

   public float m21() {
      return this.m21;
   }

   Matrix3x2f _m00(float var1) {
      this.m00 = var1;
      return this;
   }

   Matrix3x2f _m01(float var1) {
      this.m01 = var1;
      return this;
   }

   Matrix3x2f _m10(float var1) {
      this.m10 = var1;
      return this;
   }

   Matrix3x2f _m11(float var1) {
      this.m11 = var1;
      return this;
   }

   Matrix3x2f _m20(float var1) {
      this.m20 = var1;
      return this;
   }

   Matrix3x2f _m21(float var1) {
      this.m21 = var1;
      return this;
   }

   public Matrix3x2f set(Matrix3x2fc var1) {
      if (var1 instanceof Matrix3x2f) {
         MemUtil.INSTANCE.copy((Matrix3x2f)var1, this);
      } else {
         this.setMatrix3x2fc(var1);
      }

      return this;
   }

   private void setMatrix3x2fc(Matrix3x2fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
      this.m20 = var1.m20();
      this.m21 = var1.m21();
   }

   public Matrix3x2f set(Matrix2fc var1) {
      if (var1 instanceof Matrix2f) {
         MemUtil.INSTANCE.copy((Matrix2f)var1, this);
      } else {
         this.setMatrix2fc(var1);
      }

      return this;
   }

   private void setMatrix2fc(Matrix2fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
   }

   public Matrix3x2f mul(Matrix3x2fc var1) {
      return this.mul(var1, this);
   }

   public Matrix3x2f mul(Matrix3x2fc var1, Matrix3x2f var2) {
      float var3 = this.m00 * var1.m00() + this.m10 * var1.m01();
      float var4 = this.m01 * var1.m00() + this.m11 * var1.m01();
      float var5 = this.m00 * var1.m10() + this.m10 * var1.m11();
      float var6 = this.m01 * var1.m10() + this.m11 * var1.m11();
      float var7 = this.m00 * var1.m20() + this.m10 * var1.m21() + this.m20;
      float var8 = this.m01 * var1.m20() + this.m11 * var1.m21() + this.m21;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = var5;
      var2.m11 = var6;
      var2.m20 = var7;
      var2.m21 = var8;
      return var2;
   }

   public Matrix3x2f mulLocal(Matrix3x2fc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix3x2f mulLocal(Matrix3x2fc var1, Matrix3x2f var2) {
      float var3 = var1.m00() * this.m00 + var1.m10() * this.m01;
      float var4 = var1.m01() * this.m00 + var1.m11() * this.m01;
      float var5 = var1.m00() * this.m10 + var1.m10() * this.m11;
      float var6 = var1.m01() * this.m10 + var1.m11() * this.m11;
      float var7 = var1.m00() * this.m20 + var1.m10() * this.m21 + var1.m20();
      float var8 = var1.m01() * this.m20 + var1.m11() * this.m21 + var1.m21();
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = var5;
      var2.m11 = var6;
      var2.m20 = var7;
      var2.m21 = var8;
      return var2;
   }

   public Matrix3x2f set(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.m00 = var1;
      this.m01 = var2;
      this.m10 = var3;
      this.m11 = var4;
      this.m20 = var5;
      this.m21 = var6;
      return this;
   }

   public Matrix3x2f set(float[] var1) {
      MemUtil.INSTANCE.copy((float[])var1, 0, (Matrix3x2f)this);
      return this;
   }

   public float determinant() {
      return this.m00 * this.m11 - this.m01 * this.m10;
   }

   public Matrix3x2f invert() {
      return this.invert(this);
   }

   public Matrix3x2f invert(Matrix3x2f var1) {
      float var2 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10);
      float var3 = this.m11 * var2;
      float var4 = -this.m01 * var2;
      float var5 = -this.m10 * var2;
      float var6 = this.m00 * var2;
      float var7 = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      float var8 = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      var1.m00 = var3;
      var1.m01 = var4;
      var1.m10 = var5;
      var1.m11 = var6;
      var1.m20 = var7;
      var1.m21 = var8;
      return var1;
   }

   public Matrix3x2f translation(float var1, float var2) {
      this.m00 = 1.0F;
      this.m01 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 1.0F;
      this.m20 = var1;
      this.m21 = var2;
      return this;
   }

   public Matrix3x2f translation(Vector2fc var1) {
      return this.translation(var1.x(), var1.y());
   }

   public Matrix3x2f setTranslation(float var1, float var2) {
      this.m20 = var1;
      this.m21 = var2;
      return this;
   }

   public Matrix3x2f setTranslation(Vector2f var1) {
      return this.setTranslation(var1.x, var1.y);
   }

   public Matrix3x2f translate(float var1, float var2, Matrix3x2f var3) {
      var3.m20 = this.m00 * var1 + this.m10 * var2 + this.m20;
      var3.m21 = this.m01 * var1 + this.m11 * var2 + this.m21;
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      return var3;
   }

   public Matrix3x2f translate(float var1, float var2) {
      return this.translate(var1, var2, this);
   }

   public Matrix3x2f translate(Vector2fc var1, Matrix3x2f var2) {
      return this.translate(var1.x(), var1.y(), var2);
   }

   public Matrix3x2f translate(Vector2fc var1) {
      return this.translate(var1.x(), var1.y(), this);
   }

   public Matrix3x2f translateLocal(Vector2fc var1) {
      return this.translateLocal(var1.x(), var1.y());
   }

   public Matrix3x2f translateLocal(Vector2fc var1, Matrix3x2f var2) {
      return this.translateLocal(var1.x(), var1.y(), var2);
   }

   public Matrix3x2f translateLocal(float var1, float var2, Matrix3x2f var3) {
      var3.m00 = this.m00;
      var3.m01 = this.m01;
      var3.m10 = this.m10;
      var3.m11 = this.m11;
      var3.m20 = this.m20 + var1;
      var3.m21 = this.m21 + var2;
      return var3;
   }

   public Matrix3x2f translateLocal(float var1, float var2) {
      return this.translateLocal(var1, var2, this);
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
      return var10000 + " " + Runtime.format((double)this.m10, var1) + " " + Runtime.format((double)this.m20, var1) + "\n" + Runtime.format((double)this.m01, var1) + " " + Runtime.format((double)this.m11, var1) + " " + Runtime.format((double)this.m21, var1) + "\n";
   }

   public Matrix3x2f get(Matrix3x2f var1) {
      return var1.set((Matrix3x2fc)this);
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

   public FloatBuffer get3x3(FloatBuffer var1) {
      MemUtil.INSTANCE.put3x3((Matrix3x2f)this, 0, (FloatBuffer)var1);
      return var1;
   }

   public FloatBuffer get3x3(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put3x3(this, var1, var2);
      return var2;
   }

   public ByteBuffer get3x3(ByteBuffer var1) {
      MemUtil.INSTANCE.put3x3((Matrix3x2f)this, 0, (ByteBuffer)var1);
      return var1;
   }

   public ByteBuffer get3x3(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put3x3(this, var1, var2);
      return var2;
   }

   public FloatBuffer get4x4(FloatBuffer var1) {
      MemUtil.INSTANCE.put4x4((Matrix3x2f)this, 0, (FloatBuffer)var1);
      return var1;
   }

   public FloatBuffer get4x4(int var1, FloatBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public ByteBuffer get4x4(ByteBuffer var1) {
      MemUtil.INSTANCE.put4x4((Matrix3x2f)this, 0, (ByteBuffer)var1);
      return var1;
   }

   public ByteBuffer get4x4(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.put4x4(this, var1, var2);
      return var2;
   }

   public Matrix3x2fc getToAddress(long var1) {
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

   public float[] get3x3(float[] var1, int var2) {
      MemUtil.INSTANCE.copy3x3(this, var1, var2);
      return var1;
   }

   public float[] get3x3(float[] var1) {
      return this.get3x3(var1, 0);
   }

   public float[] get4x4(float[] var1, int var2) {
      MemUtil.INSTANCE.copy4x4(this, var1, var2);
      return var1;
   }

   public float[] get4x4(float[] var1) {
      return this.get4x4(var1, 0);
   }

   public Matrix3x2f set(FloatBuffer var1) {
      int var2 = var1.position();
      MemUtil.INSTANCE.get(this, var2, var1);
      return this;
   }

   public Matrix3x2f set(ByteBuffer var1) {
      int var2 = var1.position();
      MemUtil.INSTANCE.get(this, var2, var1);
      return this;
   }

   public Matrix3x2f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix3x2f zero() {
      MemUtil.INSTANCE.zero(this);
      return this;
   }

   public Matrix3x2f identity() {
      MemUtil.INSTANCE.identity(this);
      return this;
   }

   public Matrix3x2f scale(float var1, float var2, Matrix3x2f var3) {
      var3.m00 = this.m00 * var1;
      var3.m01 = this.m01 * var1;
      var3.m10 = this.m10 * var2;
      var3.m11 = this.m11 * var2;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      return var3;
   }

   public Matrix3x2f scale(float var1, float var2) {
      return this.scale(var1, var2, this);
   }

   public Matrix3x2f scale(Vector2fc var1) {
      return this.scale(var1.x(), var1.y(), this);
   }

   public Matrix3x2f scale(Vector2fc var1, Matrix3x2f var2) {
      return this.scale(var1.x(), var1.y(), var2);
   }

   public Matrix3x2f scale(float var1, Matrix3x2f var2) {
      return this.scale(var1, var1, var2);
   }

   public Matrix3x2f scale(float var1) {
      return this.scale(var1, var1);
   }

   public Matrix3x2f scaleLocal(float var1, float var2, Matrix3x2f var3) {
      var3.m00 = var1 * this.m00;
      var3.m01 = var2 * this.m01;
      var3.m10 = var1 * this.m10;
      var3.m11 = var2 * this.m11;
      var3.m20 = var1 * this.m20;
      var3.m21 = var2 * this.m21;
      return var3;
   }

   public Matrix3x2f scaleLocal(float var1, float var2) {
      return this.scaleLocal(var1, var2, this);
   }

   public Matrix3x2f scaleLocal(float var1, Matrix3x2f var2) {
      return this.scaleLocal(var1, var1, var2);
   }

   public Matrix3x2f scaleLocal(float var1) {
      return this.scaleLocal(var1, var1, this);
   }

   public Matrix3x2f scaleAround(float var1, float var2, float var3, float var4, Matrix3x2f var5) {
      float var6 = this.m00 * var3 + this.m10 * var4 + this.m20;
      float var7 = this.m01 * var3 + this.m11 * var4 + this.m21;
      var5.m00 = this.m00 * var1;
      var5.m01 = this.m01 * var1;
      var5.m10 = this.m10 * var2;
      var5.m11 = this.m11 * var2;
      var5.m20 = var5.m00 * -var3 + var5.m10 * -var4 + var6;
      var5.m21 = var5.m01 * -var3 + var5.m11 * -var4 + var7;
      return var5;
   }

   public Matrix3x2f scaleAround(float var1, float var2, float var3, float var4) {
      return this.scaleAround(var1, var2, var3, var4, this);
   }

   public Matrix3x2f scaleAround(float var1, float var2, float var3, Matrix3x2f var4) {
      return this.scaleAround(var1, var1, var2, var3, this);
   }

   public Matrix3x2f scaleAround(float var1, float var2, float var3) {
      return this.scaleAround(var1, var1, var2, var3, this);
   }

   public Matrix3x2f scaleAroundLocal(float var1, float var2, float var3, float var4, Matrix3x2f var5) {
      var5.m00 = var1 * this.m00;
      var5.m01 = var2 * this.m01;
      var5.m10 = var1 * this.m10;
      var5.m11 = var2 * this.m11;
      var5.m20 = var1 * this.m20 - var1 * var3 + var3;
      var5.m21 = var2 * this.m21 - var2 * var4 + var4;
      return var5;
   }

   public Matrix3x2f scaleAroundLocal(float var1, float var2, float var3, Matrix3x2f var4) {
      return this.scaleAroundLocal(var1, var1, var2, var3, var4);
   }

   public Matrix3x2f scaleAroundLocal(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.scaleAroundLocal(var1, var2, var4, var5, this);
   }

   public Matrix3x2f scaleAroundLocal(float var1, float var2, float var3) {
      return this.scaleAroundLocal(var1, var1, var2, var3, this);
   }

   public Matrix3x2f scaling(float var1) {
      return this.scaling(var1, var1);
   }

   public Matrix3x2f scaling(float var1, float var2) {
      this.m00 = var1;
      this.m01 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = var2;
      this.m20 = 0.0F;
      this.m21 = 0.0F;
      return this;
   }

   public Matrix3x2f rotation(float var1) {
      float var2 = Math.cos(var1);
      float var3 = Math.sin(var1);
      this.m00 = var2;
      this.m10 = -var3;
      this.m20 = 0.0F;
      this.m01 = var3;
      this.m11 = var2;
      this.m21 = 0.0F;
      return this;
   }

   public Vector3f transform(Vector3f var1) {
      return var1.mul((Matrix3x2fc)this);
   }

   public Vector3f transform(Vector3f var1, Vector3f var2) {
      return var1.mul((Matrix3x2fc)this, var2);
   }

   public Vector3f transform(float var1, float var2, float var3, Vector3f var4) {
      return var4.set(this.m00 * var1 + this.m10 * var2 + this.m20 * var3, this.m01 * var1 + this.m11 * var2 + this.m21 * var3, var3);
   }

   public Vector2f transformPosition(Vector2f var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y + this.m20, this.m01 * var1.x + this.m11 * var1.y + this.m21);
      return var1;
   }

   public Vector2f transformPosition(Vector2fc var1, Vector2f var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y() + this.m20, this.m01 * var1.x() + this.m11 * var1.y() + this.m21);
      return var2;
   }

   public Vector2f transformPosition(float var1, float var2, Vector2f var3) {
      return var3.set(this.m00 * var1 + this.m10 * var2 + this.m20, this.m01 * var1 + this.m11 * var2 + this.m21);
   }

   public Vector2f transformDirection(Vector2f var1) {
      var1.set(this.m00 * var1.x + this.m10 * var1.y, this.m01 * var1.x + this.m11 * var1.y);
      return var1;
   }

   public Vector2f transformDirection(Vector2fc var1, Vector2f var2) {
      var2.set(this.m00 * var1.x() + this.m10 * var1.y(), this.m01 * var1.x() + this.m11 * var1.y());
      return var2;
   }

   public Vector2f transformDirection(float var1, float var2, Vector2f var3) {
      return var3.set(this.m00 * var1 + this.m10 * var2, this.m01 * var1 + this.m11 * var2);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.m00);
      var1.writeFloat(this.m01);
      var1.writeFloat(this.m10);
      var1.writeFloat(this.m11);
      var1.writeFloat(this.m20);
      var1.writeFloat(this.m21);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readFloat();
      this.m01 = var1.readFloat();
      this.m10 = var1.readFloat();
      this.m11 = var1.readFloat();
      this.m20 = var1.readFloat();
      this.m21 = var1.readFloat();
   }

   public Matrix3x2f rotate(float var1) {
      return this.rotate(var1, this);
   }

   public Matrix3x2f rotate(float var1, Matrix3x2f var2) {
      float var3 = Math.cos(var1);
      float var4 = Math.sin(var1);
      float var7 = -var4;
      float var9 = this.m00 * var3 + this.m10 * var4;
      float var10 = this.m01 * var3 + this.m11 * var4;
      var2.m10 = this.m00 * var7 + this.m10 * var3;
      var2.m11 = this.m01 * var7 + this.m11 * var3;
      var2.m00 = var9;
      var2.m01 = var10;
      var2.m20 = this.m20;
      var2.m21 = this.m21;
      return var2;
   }

   public Matrix3x2f rotateLocal(float var1, Matrix3x2f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 - var3 * this.m01;
      float var6 = var3 * this.m00 + var4 * this.m01;
      float var7 = var4 * this.m10 - var3 * this.m11;
      float var8 = var3 * this.m10 + var4 * this.m11;
      float var9 = var4 * this.m20 - var3 * this.m21;
      float var10 = var3 * this.m20 + var4 * this.m21;
      var2.m00 = var5;
      var2.m01 = var6;
      var2.m10 = var7;
      var2.m11 = var8;
      var2.m20 = var9;
      var2.m21 = var10;
      return var2;
   }

   public Matrix3x2f rotateLocal(float var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix3x2f rotateAbout(float var1, float var2, float var3) {
      return this.rotateAbout(var1, var2, var3, this);
   }

   public Matrix3x2f rotateAbout(float var1, float var2, float var3, Matrix3x2f var4) {
      float var5 = this.m00 * var2 + this.m10 * var3 + this.m20;
      float var6 = this.m01 * var2 + this.m11 * var3 + this.m21;
      float var7 = Math.cos(var1);
      float var8 = Math.sin(var1);
      float var9 = this.m00 * var7 + this.m10 * var8;
      float var10 = this.m01 * var7 + this.m11 * var8;
      var4.m10 = this.m00 * -var8 + this.m10 * var7;
      var4.m11 = this.m01 * -var8 + this.m11 * var7;
      var4.m00 = var9;
      var4.m01 = var10;
      var4.m20 = var4.m00 * -var2 + var4.m10 * -var3 + var5;
      var4.m21 = var4.m01 * -var2 + var4.m11 * -var3 + var6;
      return var4;
   }

   public Matrix3x2f rotateTo(Vector2fc var1, Vector2fc var2, Matrix3x2f var3) {
      float var4 = var1.x() * var2.x() + var1.y() * var2.y();
      float var5 = var1.x() * var2.y() - var1.y() * var2.x();
      float var8 = -var5;
      float var10 = this.m00 * var4 + this.m10 * var5;
      float var11 = this.m01 * var4 + this.m11 * var5;
      var3.m10 = this.m00 * var8 + this.m10 * var4;
      var3.m11 = this.m01 * var8 + this.m11 * var4;
      var3.m00 = var10;
      var3.m01 = var11;
      var3.m20 = this.m20;
      var3.m21 = this.m21;
      return var3;
   }

   public Matrix3x2f rotateTo(Vector2fc var1, Vector2fc var2) {
      return this.rotateTo(var1, var2, this);
   }

   public Matrix3x2f view(float var1, float var2, float var3, float var4, Matrix3x2f var5) {
      float var6 = 2.0F / (var2 - var1);
      float var7 = 2.0F / (var4 - var3);
      float var8 = (var1 + var2) / (var1 - var2);
      float var9 = (var3 + var4) / (var3 - var4);
      var5.m20 = this.m00 * var8 + this.m10 * var9 + this.m20;
      var5.m21 = this.m01 * var8 + this.m11 * var9 + this.m21;
      var5.m00 = this.m00 * var6;
      var5.m01 = this.m01 * var6;
      var5.m10 = this.m10 * var7;
      var5.m11 = this.m11 * var7;
      return var5;
   }

   public Matrix3x2f view(float var1, float var2, float var3, float var4) {
      return this.view(var1, var2, var3, var4, this);
   }

   public Matrix3x2f setView(float var1, float var2, float var3, float var4) {
      this.m00 = 2.0F / (var2 - var1);
      this.m01 = 0.0F;
      this.m10 = 0.0F;
      this.m11 = 2.0F / (var4 - var3);
      this.m20 = (var1 + var2) / (var1 - var2);
      this.m21 = (var3 + var4) / (var3 - var4);
      return this;
   }

   public Vector2f origin(Vector2f var1) {
      float var2 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10);
      var1.x = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      var1.y = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      return var1;
   }

   public float[] viewArea(float[] var1) {
      float var2 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10);
      float var3 = this.m11 * var2;
      float var4 = -this.m01 * var2;
      float var5 = -this.m10 * var2;
      float var6 = this.m00 * var2;
      float var7 = (this.m10 * this.m21 - this.m20 * this.m11) * var2;
      float var8 = (this.m20 * this.m01 - this.m00 * this.m21) * var2;
      float var9 = -var3 - var5;
      float var10 = -var4 - var6;
      float var11 = var3 - var5;
      float var12 = var4 - var6;
      float var13 = -var3 + var5;
      float var14 = -var4 + var6;
      float var15 = var3 + var5;
      float var16 = var4 + var6;
      float var17 = var9 < var13 ? var9 : var13;
      var17 = var17 < var11 ? var17 : var11;
      var17 = var17 < var15 ? var17 : var15;
      float var18 = var10 < var14 ? var10 : var14;
      var18 = var18 < var12 ? var18 : var12;
      var18 = var18 < var16 ? var18 : var16;
      float var19 = var9 > var13 ? var9 : var13;
      var19 = var19 > var11 ? var19 : var11;
      var19 = var19 > var15 ? var19 : var15;
      float var20 = var10 > var14 ? var10 : var14;
      var20 = var20 > var12 ? var20 : var12;
      var20 = var20 > var16 ? var20 : var16;
      var1[0] = var17 + var7;
      var1[1] = var18 + var8;
      var1[2] = var19 + var7;
      var1[3] = var20 + var8;
      return var1;
   }

   public Vector2f positiveX(Vector2f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      var2 = 1.0F / var2;
      var1.x = this.m11 * var2;
      var1.y = -this.m01 * var2;
      return var1.normalize(var1);
   }

   public Vector2f normalizedPositiveX(Vector2f var1) {
      var1.x = this.m11;
      var1.y = -this.m01;
      return var1;
   }

   public Vector2f positiveY(Vector2f var1) {
      float var2 = this.m00 * this.m11 - this.m01 * this.m10;
      var2 = 1.0F / var2;
      var1.x = -this.m10 * var2;
      var1.y = this.m00 * var2;
      return var1.normalize(var1);
   }

   public Vector2f normalizedPositiveY(Vector2f var1) {
      var1.x = -this.m10;
      var1.y = this.m00;
      return var1;
   }

   public Vector2f unproject(float var1, float var2, int[] var3, Vector2f var4) {
      float var5 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10);
      float var6 = this.m11 * var5;
      float var7 = -this.m01 * var5;
      float var8 = -this.m10 * var5;
      float var9 = this.m00 * var5;
      float var10 = (this.m10 * this.m21 - this.m20 * this.m11) * var5;
      float var11 = (this.m20 * this.m01 - this.m00 * this.m21) * var5;
      float var12 = (var1 - (float)var3[0]) / (float)var3[2] * 2.0F - 1.0F;
      float var13 = (var2 - (float)var3[1]) / (float)var3[3] * 2.0F - 1.0F;
      var4.x = var6 * var12 + var8 * var13 + var10;
      var4.y = var7 * var12 + var9 * var13 + var11;
      return var4;
   }

   public Vector2f unprojectInv(float var1, float var2, int[] var3, Vector2f var4) {
      float var5 = (var1 - (float)var3[0]) / (float)var3[2] * 2.0F - 1.0F;
      float var6 = (var2 - (float)var3[1]) / (float)var3[3] * 2.0F - 1.0F;
      var4.x = this.m00 * var5 + this.m10 * var6 + this.m20;
      var4.y = this.m01 * var5 + this.m11 * var6 + this.m21;
      return var4;
   }

   public Matrix3x2f shearX(float var1) {
      return this.shearX(var1, this);
   }

   public Matrix3x2f shearX(float var1, Matrix3x2f var2) {
      float var3 = this.m00 * var1 + this.m10;
      float var4 = this.m01 * var1 + this.m11;
      var2.m00 = this.m00;
      var2.m01 = this.m01;
      var2.m10 = var3;
      var2.m11 = var4;
      var2.m20 = this.m20;
      var2.m21 = this.m21;
      return var2;
   }

   public Matrix3x2f shearY(float var1) {
      return this.shearY(var1, this);
   }

   public Matrix3x2f shearY(float var1, Matrix3x2f var2) {
      float var3 = this.m00 + this.m10 * var1;
      float var4 = this.m01 + this.m11 * var1;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = this.m10;
      var2.m11 = this.m11;
      var2.m20 = this.m20;
      var2.m21 = this.m21;
      return var2;
   }

   public Matrix3x2f span(Vector2f var1, Vector2f var2, Vector2f var3) {
      float var4 = 1.0F / (this.m00 * this.m11 - this.m01 * this.m10);
      float var5 = this.m11 * var4;
      float var6 = -this.m01 * var4;
      float var7 = -this.m10 * var4;
      float var8 = this.m00 * var4;
      var1.x = -var5 - var7 + (this.m10 * this.m21 - this.m20 * this.m11) * var4;
      var1.y = -var6 - var8 + (this.m20 * this.m01 - this.m00 * this.m21) * var4;
      var2.x = 2.0F * var5;
      var2.y = 2.0F * var6;
      var3.x = 2.0F * var7;
      var3.y = 2.0F * var8;
      return this;
   }

   public boolean testPoint(float var1, float var2) {
      float var3 = this.m00;
      float var4 = this.m10;
      float var5 = 1.0F + this.m20;
      float var6 = -this.m00;
      float var7 = -this.m10;
      float var8 = 1.0F - this.m20;
      float var9 = this.m01;
      float var10 = this.m11;
      float var11 = 1.0F + this.m21;
      float var12 = -this.m01;
      float var13 = -this.m11;
      float var14 = 1.0F - this.m21;
      return var3 * var1 + var4 * var2 + var5 >= 0.0F && var6 * var1 + var7 * var2 + var8 >= 0.0F && var9 * var1 + var10 * var2 + var11 >= 0.0F && var12 * var1 + var13 * var2 + var14 >= 0.0F;
   }

   public boolean testCircle(float var1, float var2, float var3) {
      float var5 = this.m00;
      float var6 = this.m10;
      float var7 = 1.0F + this.m20;
      float var4 = Math.invsqrt(var5 * var5 + var6 * var6);
      var5 *= var4;
      var6 *= var4;
      var7 *= var4;
      float var8 = -this.m00;
      float var9 = -this.m10;
      float var10 = 1.0F - this.m20;
      var4 = Math.invsqrt(var8 * var8 + var9 * var9);
      var8 *= var4;
      var9 *= var4;
      var10 *= var4;
      float var11 = this.m01;
      float var12 = this.m11;
      float var13 = 1.0F + this.m21;
      var4 = Math.invsqrt(var11 * var11 + var12 * var12);
      var11 *= var4;
      var12 *= var4;
      var13 *= var4;
      float var14 = -this.m01;
      float var15 = -this.m11;
      float var16 = 1.0F - this.m21;
      var4 = Math.invsqrt(var14 * var14 + var15 * var15);
      var14 *= var4;
      var15 *= var4;
      var16 *= var4;
      return var5 * var1 + var6 * var2 + var7 >= -var3 && var8 * var1 + var9 * var2 + var10 >= -var3 && var11 * var1 + var12 * var2 + var13 >= -var3 && var14 * var1 + var15 * var2 + var16 >= -var3;
   }

   public boolean testAar(float var1, float var2, float var3, float var4) {
      float var5 = this.m00;
      float var6 = this.m10;
      float var7 = 1.0F + this.m20;
      float var8 = -this.m00;
      float var9 = -this.m10;
      float var10 = 1.0F - this.m20;
      float var11 = this.m01;
      float var12 = this.m11;
      float var13 = 1.0F + this.m21;
      float var14 = -this.m01;
      float var15 = -this.m11;
      float var16 = 1.0F - this.m21;
      return var5 * (var5 < 0.0F ? var1 : var3) + var6 * (var6 < 0.0F ? var2 : var4) >= -var7 && var8 * (var8 < 0.0F ? var1 : var3) + var9 * (var9 < 0.0F ? var2 : var4) >= -var10 && var11 * (var11 < 0.0F ? var1 : var3) + var12 * (var12 < 0.0F ? var2 : var4) >= -var13 && var14 * (var14 < 0.0F ? var1 : var3) + var15 * (var15 < 0.0F ? var2 : var4) >= -var16;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + Float.floatToIntBits(this.m00);
      var2 = 31 * var2 + Float.floatToIntBits(this.m01);
      var2 = 31 * var2 + Float.floatToIntBits(this.m10);
      var2 = 31 * var2 + Float.floatToIntBits(this.m11);
      var2 = 31 * var2 + Float.floatToIntBits(this.m20);
      var2 = 31 * var2 + Float.floatToIntBits(this.m21);
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
         Matrix3x2f var2 = (Matrix3x2f)var1;
         if (Float.floatToIntBits(this.m00) != Float.floatToIntBits(var2.m00)) {
            return false;
         } else if (Float.floatToIntBits(this.m01) != Float.floatToIntBits(var2.m01)) {
            return false;
         } else if (Float.floatToIntBits(this.m10) != Float.floatToIntBits(var2.m10)) {
            return false;
         } else if (Float.floatToIntBits(this.m11) != Float.floatToIntBits(var2.m11)) {
            return false;
         } else if (Float.floatToIntBits(this.m20) != Float.floatToIntBits(var2.m20)) {
            return false;
         } else {
            return Float.floatToIntBits(this.m21) == Float.floatToIntBits(var2.m21);
         }
      }
   }

   public boolean equals(Matrix3x2fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix3x2f)) {
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
