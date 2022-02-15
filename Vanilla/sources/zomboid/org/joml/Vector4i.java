package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;

public class Vector4i implements Externalizable, Vector4ic {
   private static final long serialVersionUID = 1L;
   public int x;
   public int y;
   public int z;
   public int w;

   public Vector4i() {
      this.w = 1;
   }

   public Vector4i(Vector4ic var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
   }

   public Vector4i(Vector3ic var1, int var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
   }

   public Vector4i(Vector2ic var1, int var2, int var3) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var3;
   }

   public Vector4i(Vector3fc var1, float var2, int var3) {
      this.x = Math.roundUsing(var1.x(), var3);
      this.y = Math.roundUsing(var1.y(), var3);
      this.z = Math.roundUsing(var1.z(), var3);
      var2 = (float)Math.roundUsing(var2, var3);
   }

   public Vector4i(Vector4fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      this.w = Math.roundUsing(var1.w(), var2);
   }

   public Vector4i(Vector4dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      this.w = Math.roundUsing(var1.w(), var2);
   }

   public Vector4i(int var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
   }

   public Vector4i(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Vector4i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[3];
   }

   public Vector4i(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4i(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector4i(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector4i(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public int z() {
      return this.z;
   }

   public int w() {
      return this.w;
   }

   public Vector4i set(Vector4ic var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var1.w();
      return this;
   }

   public Vector4i set(Vector4dc var1) {
      this.x = (int)var1.x();
      this.y = (int)var1.y();
      this.z = (int)var1.z();
      this.w = (int)var1.w();
      return this;
   }

   public Vector4i set(Vector4dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      this.w = Math.roundUsing(var1.w(), var2);
      return this;
   }

   public Vector4i set(Vector4fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      this.w = Math.roundUsing(var1.w(), var2);
      return this;
   }

   public Vector4i set(Vector3ic var1, int var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      this.w = var2;
      return this;
   }

   public Vector4i set(Vector2ic var1, int var2, int var3) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      this.w = var3;
      return this;
   }

   public Vector4i set(int var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      this.w = var1;
      return this;
   }

   public Vector4i set(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
      return this;
   }

   public Vector4i set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      this.w = var1[2];
      return this;
   }

   public Vector4i set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4i set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4i set(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector4i set(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector4i setFromAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.get(this, var1);
         return this;
      }
   }

   public int get(int var1) throws IllegalArgumentException {
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

   public int maxComponent() {
      int var1 = Math.abs(this.x);
      int var2 = Math.abs(this.y);
      int var3 = Math.abs(this.z);
      int var4 = Math.abs(this.w);
      if (var1 >= var2 && var1 >= var3 && var1 >= var4) {
         return 0;
      } else if (var2 >= var3 && var2 >= var4) {
         return 1;
      } else {
         return var3 >= var4 ? 2 : 3;
      }
   }

   public int minComponent() {
      int var1 = Math.abs(this.x);
      int var2 = Math.abs(this.y);
      int var3 = Math.abs(this.z);
      int var4 = Math.abs(this.w);
      if (var1 < var2 && var1 < var3 && var1 < var4) {
         return 0;
      } else if (var2 < var3 && var2 < var4) {
         return 1;
      } else {
         return var3 < var4 ? 2 : 3;
      }
   }

   public Vector4i setComponent(int var1, int var2) throws IllegalArgumentException {
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

   public IntBuffer get(IntBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public IntBuffer get(int var1, IntBuffer var2) {
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

   public Vector4ic getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector4i sub(Vector4ic var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      this.w -= var1.w();
      return this;
   }

   public Vector4i sub(int var1, int var2, int var3, int var4) {
      this.x -= var1;
      this.y -= var2;
      this.z -= var3;
      this.w -= var4;
      return this;
   }

   public Vector4i sub(Vector4ic var1, Vector4i var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      var2.w = this.w - var1.w();
      return var2;
   }

   public Vector4i sub(int var1, int var2, int var3, int var4, Vector4i var5) {
      var5.x = this.x - var1;
      var5.y = this.y - var2;
      var5.z = this.z - var3;
      var5.w = this.w - var4;
      return var5;
   }

   public Vector4i add(Vector4ic var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      this.w += var1.w();
      return this;
   }

   public Vector4i add(Vector4ic var1, Vector4i var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      var2.w = this.w + var1.w();
      return var2;
   }

   public Vector4i add(int var1, int var2, int var3, int var4) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      this.w += var4;
      return this;
   }

   public Vector4i add(int var1, int var2, int var3, int var4, Vector4i var5) {
      var5.x = this.x + var1;
      var5.y = this.y + var2;
      var5.z = this.z + var3;
      var5.w = this.w + var4;
      return var5;
   }

   public Vector4i mul(Vector4ic var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      this.w *= var1.w();
      return this;
   }

   public Vector4i mul(Vector4ic var1, Vector4i var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      var2.w = this.w * var1.w();
      return var2;
   }

   public Vector4i div(Vector4ic var1) {
      this.x /= var1.x();
      this.y /= var1.y();
      this.z /= var1.z();
      this.w /= var1.w();
      return this;
   }

   public Vector4i div(Vector4ic var1, Vector4i var2) {
      var2.x = this.x / var1.x();
      var2.y = this.y / var1.y();
      var2.z = this.z / var1.z();
      var2.w = this.w / var1.w();
      return var2;
   }

   public Vector4i mul(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      this.w *= var1;
      return this;
   }

   public Vector4i mul(int var1, Vector4i var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      var2.z = this.z * var1;
      var2.w = this.w * var1;
      return var2;
   }

   public Vector4i div(float var1) {
      float var2 = 1.0F / var1;
      this.x = (int)((float)this.x * var2);
      this.y = (int)((float)this.y * var2);
      this.z = (int)((float)this.z * var2);
      this.w = (int)((float)this.w * var2);
      return this;
   }

   public Vector4i div(float var1, Vector4i var2) {
      float var3 = 1.0F / var1;
      var2.x = (int)((float)this.x * var3);
      var2.y = (int)((float)this.y * var3);
      var2.z = (int)((float)this.z * var3);
      var2.w = (int)((float)this.w * var3);
      return var2;
   }

   public Vector4i div(int var1) {
      this.x /= var1;
      this.y /= var1;
      this.z /= var1;
      this.w /= var1;
      return this;
   }

   public Vector4i div(int var1, Vector4i var2) {
      var2.x = this.x / var1;
      var2.y = this.y / var1;
      var2.z = this.z / var1;
      var2.w = this.w / var1;
      return var2;
   }

   public long lengthSquared() {
      return (long)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
   }

   public static long lengthSquared(int var0, int var1, int var2, int var3) {
      return (long)(var0 * var0 + var1 * var1 + var2 * var2 + var3 * var3);
   }

   public double length() {
      return (double)Math.sqrt((float)(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w));
   }

   public static double length(int var0, int var1, int var2, int var3) {
      return (double)Math.sqrt((float)(var0 * var0 + var1 * var1 + var2 * var2 + var3 * var3));
   }

   public double distance(Vector4ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      int var4 = this.z - var1.z();
      int var5 = this.w - var1.w();
      return (double)Math.sqrt(Math.fma((float)var2, (float)var2, Math.fma((float)var3, (float)var3, Math.fma((float)var4, (float)var4, (float)(var5 * var5)))));
   }

   public double distance(int var1, int var2, int var3, int var4) {
      int var5 = this.x - var1;
      int var6 = this.y - var2;
      int var7 = this.z - var3;
      int var8 = this.w - var4;
      return (double)Math.sqrt(Math.fma((float)var5, (float)var5, Math.fma((float)var6, (float)var6, Math.fma((float)var7, (float)var7, (float)(var8 * var8)))));
   }

   public long gridDistance(Vector4ic var1) {
      return (long)(Math.abs(var1.x() - this.x()) + Math.abs(var1.y() - this.y()) + Math.abs(var1.z() - this.z()) + Math.abs(var1.w() - this.w()));
   }

   public long gridDistance(int var1, int var2, int var3, int var4) {
      return (long)(Math.abs(var1 - this.x()) + Math.abs(var2 - this.y()) + Math.abs(var3 - this.z()) + Math.abs(var4 - this.w()));
   }

   public int distanceSquared(Vector4ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      int var4 = this.z - var1.z();
      int var5 = this.w - var1.w();
      return var2 * var2 + var3 * var3 + var4 * var4 + var5 * var5;
   }

   public int distanceSquared(int var1, int var2, int var3, int var4) {
      int var5 = this.x - var1;
      int var6 = this.y - var2;
      int var7 = this.z - var3;
      int var8 = this.w - var4;
      return var5 * var5 + var6 * var6 + var7 * var7 + var8 * var8;
   }

   public static double distance(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var0 - var4;
      int var9 = var1 - var5;
      int var10 = var2 - var6;
      int var11 = var3 - var7;
      return (double)Math.sqrt((float)(var8 * var8 + var9 * var9 + var10 * var10 + var11 * var11));
   }

   public static long distanceSquared(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var0 - var4;
      int var9 = var1 - var5;
      int var10 = var2 - var6;
      int var11 = var3 - var7;
      return (long)(var8 * var8 + var9 * var9 + var10 * var10 + var11 * var11);
   }

   public int dot(Vector4ic var1) {
      return this.x * var1.x() + this.y * var1.y() + this.z * var1.z() + this.w * var1.w();
   }

   public Vector4i zero() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
      this.w = 0;
      return this;
   }

   public Vector4i negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      this.w = -this.w;
      return this;
   }

   public Vector4i negate(Vector4i var1) {
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
      String var10000 = var1.format((long)this.x);
      return "(" + var10000 + " " + var1.format((long)this.y) + " " + var1.format((long)this.z) + " " + var1.format((long)this.w) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.x);
      var1.writeInt(this.y);
      var1.writeInt(this.z);
      var1.writeInt(this.w);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readInt();
      this.y = var1.readInt();
      this.z = var1.readInt();
      this.w = var1.readInt();
   }

   public Vector4i min(Vector4ic var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      this.z = this.z < var1.z() ? this.z : var1.z();
      this.w = this.w < var1.w() ? this.w : var1.w();
      return this;
   }

   public Vector4i min(Vector4ic var1, Vector4i var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      var2.z = this.z < var1.z() ? this.z : var1.z();
      var2.w = this.w < var1.w() ? this.w : var1.w();
      return var2;
   }

   public Vector4i max(Vector4ic var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      this.z = this.z > var1.z() ? this.z : var1.z();
      this.w = this.w > var1.w() ? this.w : var1.w();
      return this;
   }

   public Vector4i max(Vector4ic var1, Vector4i var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      var2.z = this.z > var1.z() ? this.z : var1.z();
      var2.w = this.w > var1.w() ? this.w : var1.w();
      return var2;
   }

   public Vector4i absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      this.w = Math.abs(this.w);
      return this;
   }

   public Vector4i absolute(Vector4i var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      var1.w = Math.abs(this.w);
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + this.x;
      var2 = 31 * var2 + this.y;
      var2 = 31 * var2 + this.z;
      var2 = 31 * var2 + this.w;
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
         Vector4i var2 = (Vector4i)var1;
         if (this.x != var2.x) {
            return false;
         } else if (this.y != var2.y) {
            return false;
         } else if (this.z != var2.z) {
            return false;
         } else {
            return this.w == var2.w;
         }
      }
   }

   public boolean equals(int var1, int var2, int var3, int var4) {
      if (this.x != var1) {
         return false;
      } else if (this.y != var2) {
         return false;
      } else if (this.z != var3) {
         return false;
      } else {
         return this.w == var4;
      }
   }
}
