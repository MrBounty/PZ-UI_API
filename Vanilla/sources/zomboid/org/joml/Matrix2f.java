package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix2f implements Externalizable, Matrix2fc {
   private static final long serialVersionUID = 1L;
   public float m00;
   public float m01;
   public float m10;
   public float m11;

   public Matrix2f() {
      this.m00 = 1.0F;
      this.m11 = 1.0F;
   }

   public Matrix2f(Matrix2fc var1) {
      if (var1 instanceof Matrix2f) {
         MemUtil.INSTANCE.copy((Matrix2f)var1, this);
      } else {
         this.setMatrix2fc(var1);
      }

   }

   public Matrix2f(Matrix3fc var1) {
      if (var1 instanceof Matrix3f) {
         MemUtil.INSTANCE.copy((Matrix3f)var1, this);
      } else {
         this.setMatrix3fc(var1);
      }

   }

   public Matrix2f(float var1, float var2, float var3, float var4) {
      this.m00 = var1;
      this.m01 = var2;
      this.m10 = var3;
      this.m11 = var4;
   }

   public Matrix2f(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Matrix2f(Vector2fc var1, Vector2fc var2) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m10 = var2.x();
      this.m11 = var2.y();
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

   public Matrix2f m00(float var1) {
      this.m00 = var1;
      return this;
   }

   public Matrix2f m01(float var1) {
      this.m01 = var1;
      return this;
   }

   public Matrix2f m10(float var1) {
      this.m10 = var1;
      return this;
   }

   public Matrix2f m11(float var1) {
      this.m11 = var1;
      return this;
   }

   Matrix2f _m00(float var1) {
      this.m00 = var1;
      return this;
   }

   Matrix2f _m01(float var1) {
      this.m01 = var1;
      return this;
   }

   Matrix2f _m10(float var1) {
      this.m10 = var1;
      return this;
   }

   Matrix2f _m11(float var1) {
      this.m11 = var1;
      return this;
   }

   public Matrix2f set(Matrix2fc var1) {
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

   public Matrix2f set(Matrix3x2fc var1) {
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
   }

   public Matrix2f set(Matrix3fc var1) {
      if (var1 instanceof Matrix3f) {
         MemUtil.INSTANCE.copy((Matrix3f)var1, this);
      } else {
         this.setMatrix3fc(var1);
      }

      return this;
   }

   private void setMatrix3fc(Matrix3fc var1) {
      this.m00 = var1.m00();
      this.m01 = var1.m01();
      this.m10 = var1.m10();
      this.m11 = var1.m11();
   }

   public Matrix2f mul(Matrix2fc var1) {
      return this.mul(var1, this);
   }

   public Matrix2f mul(Matrix2fc var1, Matrix2f var2) {
      float var3 = this.m00 * var1.m00() + this.m10 * var1.m01();
      float var4 = this.m01 * var1.m00() + this.m11 * var1.m01();
      float var5 = this.m00 * var1.m10() + this.m10 * var1.m11();
      float var6 = this.m01 * var1.m10() + this.m11 * var1.m11();
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = var5;
      var2.m11 = var6;
      return var2;
   }

   public Matrix2f mulLocal(Matrix2fc var1) {
      return this.mulLocal(var1, this);
   }

   public Matrix2f mulLocal(Matrix2fc var1, Matrix2f var2) {
      float var3 = var1.m00() * this.m00 + var1.m10() * this.m01;
      float var4 = var1.m01() * this.m00 + var1.m11() * this.m01;
      float var5 = var1.m00() * this.m10 + var1.m10() * this.m11;
      float var6 = var1.m01() * this.m10 + var1.m11() * this.m11;
      var2.m00 = var3;
      var2.m01 = var4;
      var2.m10 = var5;
      var2.m11 = var6;
      return var2;
   }

   public Matrix2f set(float var1, float var2, float var3, float var4) {
      this.m00 = var1;
      this.m01 = var2;
      this.m10 = var3;
      this.m11 = var4;
      return this;
   }

   public Matrix2f set(float[] var1) {
      MemUtil.INSTANCE.copy((float[])var1, 0, (Matrix2f)this);
      return this;
   }

   public Matrix2f set(Vector2fc var1, Vector2fc var2) {
      this.m00 = var1.x();
      this.m01 = var1.y();
      this.m10 = var2.x();
      this.m11 = var2.y();
      return this;
   }

   public float determinant() {
      return this.m00 * this.m11 - this.m10 * this.m01;
   }

   public Matrix2f invert() {
      return this.invert(this);
   }

   public Matrix2f invert(Matrix2f var1) {
      float var2 = 1.0F / this.determinant();
      float var3 = this.m11 * var2;
      float var4 = -this.m01 * var2;
      float var5 = -this.m10 * var2;
      float var6 = this.m00 * var2;
      var1.m00 = var3;
      var1.m01 = var4;
      var1.m10 = var5;
      var1.m11 = var6;
      return var1;
   }

   public Matrix2f transpose() {
      return this.transpose(this);
   }

   public Matrix2f transpose(Matrix2f var1) {
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
      String var10000 = Runtime.format((double)this.m00, var1);
      return var10000 + " " + Runtime.format((double)this.m10, var1) + "\n" + Runtime.format((double)this.m01, var1) + " " + Runtime.format((double)this.m11, var1) + "\n";
   }

   public Matrix2f get(Matrix2f var1) {
      return var1.set((Matrix2fc)this);
   }

   public Matrix3x2f get(Matrix3x2f var1) {
      return var1.set((Matrix2fc)this);
   }

   public Matrix3f get(Matrix3f var1) {
      return var1.set((Matrix2fc)this);
   }

   public float getRotation() {
      return Math.atan2(this.m01, this.m11);
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

   public FloatBuffer getTransposed(FloatBuffer var1) {
      return this.get(var1.position(), var1);
   }

   public FloatBuffer getTransposed(int var1, FloatBuffer var2) {
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

   public Matrix2fc getToAddress(long var1) {
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

   public Matrix2f set(FloatBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix2f set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Matrix2f setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public Matrix2f zero() {
      MemUtil.INSTANCE.zero(this);
      return this;
   }

   public Matrix2f identity() {
      MemUtil.INSTANCE.identity(this);
      return this;
   }

   public Matrix2f scale(Vector2fc var1, Matrix2f var2) {
      return this.scale(var1.x(), var1.y(), var2);
   }

   public Matrix2f scale(Vector2fc var1) {
      return this.scale(var1.x(), var1.y(), this);
   }

   public Matrix2f scale(float var1, float var2, Matrix2f var3) {
      var3.m00 = this.m00 * var1;
      var3.m01 = this.m01 * var1;
      var3.m10 = this.m10 * var2;
      var3.m11 = this.m11 * var2;
      return var3;
   }

   public Matrix2f scale(float var1, float var2) {
      return this.scale(var1, var2, this);
   }

   public Matrix2f scale(float var1, Matrix2f var2) {
      return this.scale(var1, var1, var2);
   }

   public Matrix2f scale(float var1) {
      return this.scale(var1, var1);
   }

   public Matrix2f scaleLocal(float var1, float var2, Matrix2f var3) {
      var3.m00 = var1 * this.m00;
      var3.m01 = var2 * this.m01;
      var3.m10 = var1 * this.m10;
      var3.m11 = var2 * this.m11;
      return var3;
   }

   public Matrix2f scaleLocal(float var1, float var2) {
      return this.scaleLocal(var1, var2, this);
   }

   public Matrix2f scaling(float var1) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var1;
      return this;
   }

   public Matrix2f scaling(float var1, float var2) {
      MemUtil.INSTANCE.zero(this);
      this.m00 = var1;
      this.m11 = var2;
      return this;
   }

   public Matrix2f scaling(Vector2fc var1) {
      return this.scaling(var1.x(), var1.y());
   }

   public Matrix2f rotation(float var1) {
      float var2 = Math.sin(var1);
      float var3 = Math.cosFromSin(var2, var1);
      this.m00 = var3;
      this.m01 = var2;
      this.m10 = -var2;
      this.m11 = var3;
      return this;
   }

   public Vector2f transform(Vector2f var1) {
      return var1.mul((Matrix2fc)this);
   }

   public Vector2f transform(Vector2fc var1, Vector2f var2) {
      var1.mul((Matrix2fc)this, var2);
      return var2;
   }

   public Vector2f transform(float var1, float var2, Vector2f var3) {
      var3.set(this.m00 * var1 + this.m10 * var2, this.m01 * var1 + this.m11 * var2);
      return var3;
   }

   public Vector2f transformTranspose(Vector2f var1) {
      return var1.mulTranspose(this);
   }

   public Vector2f transformTranspose(Vector2fc var1, Vector2f var2) {
      var1.mulTranspose(this, var2);
      return var2;
   }

   public Vector2f transformTranspose(float var1, float var2, Vector2f var3) {
      var3.set(this.m00 * var1 + this.m01 * var2, this.m10 * var1 + this.m11 * var2);
      return var3;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeFloat(this.m00);
      var1.writeFloat(this.m01);
      var1.writeFloat(this.m10);
      var1.writeFloat(this.m11);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.m00 = var1.readFloat();
      this.m01 = var1.readFloat();
      this.m10 = var1.readFloat();
      this.m11 = var1.readFloat();
   }

   public Matrix2f rotate(float var1) {
      return this.rotate(var1, this);
   }

   public Matrix2f rotate(float var1, Matrix2f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = this.m00 * var4 + this.m10 * var3;
      float var6 = this.m01 * var4 + this.m11 * var3;
      float var7 = this.m10 * var4 - this.m00 * var3;
      float var8 = this.m11 * var4 - this.m01 * var3;
      var2.m00 = var5;
      var2.m01 = var6;
      var2.m10 = var7;
      var2.m11 = var8;
      return var2;
   }

   public Matrix2f rotateLocal(float var1) {
      return this.rotateLocal(var1, this);
   }

   public Matrix2f rotateLocal(float var1, Matrix2f var2) {
      float var3 = Math.sin(var1);
      float var4 = Math.cosFromSin(var3, var1);
      float var5 = var4 * this.m00 - var3 * this.m01;
      float var6 = var3 * this.m00 + var4 * this.m01;
      float var7 = var4 * this.m10 - var3 * this.m11;
      float var8 = var3 * this.m10 + var4 * this.m11;
      var2.m00 = var5;
      var2.m01 = var6;
      var2.m10 = var7;
      var2.m11 = var8;
      return var2;
   }

   public Vector2f getRow(int var1, Vector2f var2) throws IndexOutOfBoundsException {
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

   public Matrix2f setRow(int var1, Vector2fc var2) throws IndexOutOfBoundsException {
      return this.setRow(var1, var2.x(), var2.y());
   }

   public Matrix2f setRow(int var1, float var2, float var3) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m10 = var3;
         break;
      case 1:
         this.m01 = var2;
         this.m11 = var3;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public Vector2f getColumn(int var1, Vector2f var2) throws IndexOutOfBoundsException {
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

   public Matrix2f setColumn(int var1, Vector2fc var2) throws IndexOutOfBoundsException {
      return this.setColumn(var1, var2.x(), var2.y());
   }

   public Matrix2f setColumn(int var1, float var2, float var3) throws IndexOutOfBoundsException {
      switch(var1) {
      case 0:
         this.m00 = var2;
         this.m01 = var3;
         break;
      case 1:
         this.m10 = var2;
         this.m11 = var3;
         break;
      default:
         throw new IndexOutOfBoundsException();
      }

      return this;
   }

   public float get(int var1, int var2) {
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

   public Matrix2f set(int var1, int var2, float var3) {
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

   public Matrix2f normal() {
      return this.normal(this);
   }

   public Matrix2f normal(Matrix2f var1) {
      float var2 = this.m00 * this.m11 - this.m10 * this.m01;
      float var3 = 1.0F / var2;
      float var4 = this.m11 * var3;
      float var5 = -this.m10 * var3;
      float var6 = -this.m01 * var3;
      float var7 = this.m00 * var3;
      var1.m00 = var4;
      var1.m01 = var5;
      var1.m10 = var6;
      var1.m11 = var7;
      return var1;
   }

   public Vector2f getScale(Vector2f var1) {
      var1.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01);
      var1.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11);
      return var1;
   }

   public Vector2f positiveX(Vector2f var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = -this.m11;
         var1.y = this.m01;
      } else {
         var1.x = this.m11;
         var1.y = -this.m01;
      }

      return var1.normalize(var1);
   }

   public Vector2f normalizedPositiveX(Vector2f var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = -this.m11;
         var1.y = this.m01;
      } else {
         var1.x = this.m11;
         var1.y = -this.m01;
      }

      return var1;
   }

   public Vector2f positiveY(Vector2f var1) {
      if (this.m00 * this.m11 < this.m01 * this.m10) {
         var1.x = this.m10;
         var1.y = -this.m00;
      } else {
         var1.x = -this.m10;
         var1.y = this.m00;
      }

      return var1.normalize(var1);
   }

   public Vector2f normalizedPositiveY(Vector2f var1) {
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
      int var2 = 31 * var1 + Float.floatToIntBits(this.m00);
      var2 = 31 * var2 + Float.floatToIntBits(this.m01);
      var2 = 31 * var2 + Float.floatToIntBits(this.m10);
      var2 = 31 * var2 + Float.floatToIntBits(this.m11);
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
         Matrix2f var2 = (Matrix2f)var1;
         if (Float.floatToIntBits(this.m00) != Float.floatToIntBits(var2.m00)) {
            return false;
         } else if (Float.floatToIntBits(this.m01) != Float.floatToIntBits(var2.m01)) {
            return false;
         } else if (Float.floatToIntBits(this.m10) != Float.floatToIntBits(var2.m10)) {
            return false;
         } else {
            return Float.floatToIntBits(this.m11) == Float.floatToIntBits(var2.m11);
         }
      }
   }

   public boolean equals(Matrix2fc var1, float var2) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!(var1 instanceof Matrix2f)) {
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

   public Matrix2f swap(Matrix2f var1) {
      MemUtil.INSTANCE.swap(this, var1);
      return this;
   }

   public Matrix2f add(Matrix2fc var1) {
      return this.add(var1, this);
   }

   public Matrix2f add(Matrix2fc var1, Matrix2f var2) {
      var2.m00 = this.m00 + var1.m00();
      var2.m01 = this.m01 + var1.m01();
      var2.m10 = this.m10 + var1.m10();
      var2.m11 = this.m11 + var1.m11();
      return var2;
   }

   public Matrix2f sub(Matrix2fc var1) {
      return this.sub(var1, this);
   }

   public Matrix2f sub(Matrix2fc var1, Matrix2f var2) {
      var2.m00 = this.m00 - var1.m00();
      var2.m01 = this.m01 - var1.m01();
      var2.m10 = this.m10 - var1.m10();
      var2.m11 = this.m11 - var1.m11();
      return var2;
   }

   public Matrix2f mulComponentWise(Matrix2fc var1) {
      return this.sub(var1, this);
   }

   public Matrix2f mulComponentWise(Matrix2fc var1, Matrix2f var2) {
      var2.m00 = this.m00 * var1.m00();
      var2.m01 = this.m01 * var1.m01();
      var2.m10 = this.m10 * var1.m10();
      var2.m11 = this.m11 * var1.m11();
      return var2;
   }

   public Matrix2f lerp(Matrix2fc var1, float var2) {
      return this.lerp(var1, var2, this);
   }

   public Matrix2f lerp(Matrix2fc var1, float var2, Matrix2f var3) {
      var3.m00 = Math.fma(var1.m00() - this.m00, var2, this.m00);
      var3.m01 = Math.fma(var1.m01() - this.m01, var2, this.m01);
      var3.m10 = Math.fma(var1.m10() - this.m10, var2, this.m10);
      var3.m11 = Math.fma(var1.m11() - this.m11, var2, this.m11);
      return var3;
   }

   public boolean isFinite() {
      return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m10) && Math.isFinite(this.m11);
   }
}
