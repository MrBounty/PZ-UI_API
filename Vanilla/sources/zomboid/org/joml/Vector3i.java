package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;

public class Vector3i implements Externalizable, Vector3ic {
   private static final long serialVersionUID = 1L;
   public int x;
   public int y;
   public int z;

   public Vector3i() {
   }

   public Vector3i(int var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
   }

   public Vector3i(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector3i(Vector3ic var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
   }

   public Vector3i(Vector2ic var1, int var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
   }

   public Vector3i(float var1, float var2, float var3, int var4) {
      this.x = Math.roundUsing(var1, var4);
      this.y = Math.roundUsing(var2, var4);
      this.z = Math.roundUsing(var3, var4);
   }

   public Vector3i(double var1, double var3, double var5, int var7) {
      this.x = Math.roundUsing(var1, var7);
      this.y = Math.roundUsing(var3, var7);
      this.z = Math.roundUsing(var5, var7);
   }

   public Vector3i(Vector2fc var1, float var2, int var3) {
      this.x = Math.roundUsing(var1.x(), var3);
      this.y = Math.roundUsing(var1.y(), var3);
      this.z = Math.roundUsing(var2, var3);
   }

   public Vector3i(Vector3fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
   }

   public Vector3i(Vector2dc var1, float var2, int var3) {
      this.x = Math.roundUsing(var1.x(), var3);
      this.y = Math.roundUsing(var1.y(), var3);
      this.z = Math.roundUsing(var2, var3);
   }

   public Vector3i(Vector3dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
   }

   public Vector3i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
   }

   public Vector3i(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3i(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector3i(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector3i(int var1, IntBuffer var2) {
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

   public Vector3i set(Vector3ic var1) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var1.z();
      return this;
   }

   public Vector3i set(Vector3dc var1) {
      this.x = (int)var1.x();
      this.y = (int)var1.y();
      this.z = (int)var1.z();
      return this;
   }

   public Vector3i set(Vector3dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      return this;
   }

   public Vector3i set(Vector3fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      this.z = Math.roundUsing(var1.z(), var2);
      return this;
   }

   public Vector3i set(Vector2ic var1, int var2) {
      this.x = var1.x();
      this.y = var1.y();
      this.z = var2;
      return this;
   }

   public Vector3i set(int var1) {
      this.x = var1;
      this.y = var1;
      this.z = var1;
      return this;
   }

   public Vector3i set(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      return this;
   }

   public Vector3i set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      this.z = var1[2];
      return this;
   }

   public Vector3i set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3i set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3i set(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector3i set(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector3i setFromAddress(long var1) {
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
      default:
         throw new IllegalArgumentException();
      }
   }

   public Vector3i setComponent(int var1, int var2) throws IllegalArgumentException {
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

   public Vector3ic getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector3i sub(Vector3ic var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      this.z -= var1.z();
      return this;
   }

   public Vector3i sub(Vector3ic var1, Vector3i var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      var2.z = this.z - var1.z();
      return var2;
   }

   public Vector3i sub(int var1, int var2, int var3) {
      this.x -= var1;
      this.y -= var2;
      this.z -= var3;
      return this;
   }

   public Vector3i sub(int var1, int var2, int var3, Vector3i var4) {
      var4.x = this.x - var1;
      var4.y = this.y - var2;
      var4.z = this.z - var3;
      return var4;
   }

   public Vector3i add(Vector3ic var1) {
      this.x += var1.x();
      this.y += var1.y();
      this.z += var1.z();
      return this;
   }

   public Vector3i add(Vector3ic var1, Vector3i var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      var2.z = this.z + var1.z();
      return var2;
   }

   public Vector3i add(int var1, int var2, int var3) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      return this;
   }

   public Vector3i add(int var1, int var2, int var3, Vector3i var4) {
      var4.x = this.x + var1;
      var4.y = this.y + var2;
      var4.z = this.z + var3;
      return var4;
   }

   public Vector3i mul(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public Vector3i mul(int var1, Vector3i var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      var2.z = this.z * var1;
      return var2;
   }

   public Vector3i mul(Vector3ic var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      this.z *= var1.z();
      return this;
   }

   public Vector3i mul(Vector3ic var1, Vector3i var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      var2.z = this.z * var1.z();
      return var2;
   }

   public Vector3i mul(int var1, int var2, int var3) {
      this.x *= var1;
      this.y *= var2;
      this.z *= var3;
      return this;
   }

   public Vector3i mul(int var1, int var2, int var3, Vector3i var4) {
      var4.x = this.x * var1;
      var4.y = this.y * var2;
      var4.z = this.z * var3;
      return var4;
   }

   public Vector3i div(float var1) {
      float var2 = 1.0F / var1;
      this.x = (int)((float)this.x * var2);
      this.y = (int)((float)this.y * var2);
      this.z = (int)((float)this.z * var2);
      return this;
   }

   public Vector3i div(float var1, Vector3i var2) {
      float var3 = 1.0F / var1;
      var2.x = (int)((float)this.x * var3);
      var2.y = (int)((float)this.y * var3);
      var2.z = (int)((float)this.z * var3);
      return var2;
   }

   public Vector3i div(int var1) {
      this.x /= var1;
      this.y /= var1;
      this.z /= var1;
      return this;
   }

   public Vector3i div(int var1, Vector3i var2) {
      var2.x = this.x / var1;
      var2.y = this.y / var1;
      var2.z = this.z / var1;
      return var2;
   }

   public long lengthSquared() {
      return (long)(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public static long lengthSquared(int var0, int var1, int var2) {
      return (long)(var0 * var0 + var1 * var1 + var2 * var2);
   }

   public double length() {
      return (double)Math.sqrt((float)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public static double length(int var0, int var1, int var2) {
      return (double)Math.sqrt((float)(var0 * var0 + var1 * var1 + var2 * var2));
   }

   public double distance(Vector3ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      int var4 = this.z - var1.z();
      return (double)Math.sqrt((float)(var2 * var2 + var3 * var3 + var4 * var4));
   }

   public double distance(int var1, int var2, int var3) {
      int var4 = this.x - var1;
      int var5 = this.y - var2;
      int var6 = this.z - var3;
      return (double)Math.sqrt((float)(var4 * var4 + var5 * var5 + var6 * var6));
   }

   public long gridDistance(Vector3ic var1) {
      return (long)(Math.abs(var1.x() - this.x()) + Math.abs(var1.y() - this.y()) + Math.abs(var1.z() - this.z()));
   }

   public long gridDistance(int var1, int var2, int var3) {
      return (long)(Math.abs(var1 - this.x()) + Math.abs(var2 - this.y()) + Math.abs(var3 - this.z()));
   }

   public long distanceSquared(Vector3ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      int var4 = this.z - var1.z();
      return (long)(var2 * var2 + var3 * var3 + var4 * var4);
   }

   public long distanceSquared(int var1, int var2, int var3) {
      int var4 = this.x - var1;
      int var5 = this.y - var2;
      int var6 = this.z - var3;
      return (long)(var4 * var4 + var5 * var5 + var6 * var6);
   }

   public static double distance(int var0, int var1, int var2, int var3, int var4, int var5) {
      return (double)Math.sqrt((float)distanceSquared(var0, var1, var2, var3, var4, var5));
   }

   public static long distanceSquared(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = var0 - var3;
      int var7 = var1 - var4;
      int var8 = var2 - var5;
      return (long)(var6 * var6 + var7 * var7 + var8 * var8);
   }

   public Vector3i zero() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
      return this;
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = var1.format((long)this.x);
      return "(" + var10000 + " " + var1.format((long)this.y) + " " + var1.format((long)this.z) + ")";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.x);
      var1.writeInt(this.y);
      var1.writeInt(this.z);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readInt();
      this.y = var1.readInt();
      this.z = var1.readInt();
   }

   public Vector3i negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3i negate(Vector3i var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      var1.z = -this.z;
      return var1;
   }

   public Vector3i min(Vector3ic var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      this.z = this.z < var1.z() ? this.z : var1.z();
      return this;
   }

   public Vector3i min(Vector3ic var1, Vector3i var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      var2.z = this.z < var1.z() ? this.z : var1.z();
      return var2;
   }

   public Vector3i max(Vector3ic var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      this.z = this.z > var1.z() ? this.z : var1.z();
      return this;
   }

   public Vector3i max(Vector3ic var1, Vector3i var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      var2.z = this.z > var1.z() ? this.z : var1.z();
      return var2;
   }

   public int maxComponent() {
      float var1 = (float)Math.abs(this.x);
      float var2 = (float)Math.abs(this.y);
      float var3 = (float)Math.abs(this.z);
      if (var1 >= var2 && var1 >= var3) {
         return 0;
      } else {
         return var2 >= var3 ? 1 : 2;
      }
   }

   public int minComponent() {
      float var1 = (float)Math.abs(this.x);
      float var2 = (float)Math.abs(this.y);
      float var3 = (float)Math.abs(this.z);
      if (var1 < var2 && var1 < var3) {
         return 0;
      } else {
         return var2 < var3 ? 1 : 2;
      }
   }

   public Vector3i absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
      return this;
   }

   public Vector3i absolute(Vector3i var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      var1.z = Math.abs(this.z);
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + this.x;
      var2 = 31 * var2 + this.y;
      var2 = 31 * var2 + this.z;
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
         Vector3i var2 = (Vector3i)var1;
         if (this.x != var2.x) {
            return false;
         } else if (this.y != var2.y) {
            return false;
         } else {
            return this.z == var2.z;
         }
      }
   }

   public boolean equals(int var1, int var2, int var3) {
      if (this.x != var1) {
         return false;
      } else if (this.y != var2) {
         return false;
      } else {
         return this.z == var3;
      }
   }
}
