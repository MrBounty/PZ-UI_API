package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;

public class Vector2i implements Externalizable, Vector2ic {
   private static final long serialVersionUID = 1L;
   public int x;
   public int y;

   public Vector2i() {
   }

   public Vector2i(int var1) {
      this.x = var1;
      this.y = var1;
   }

   public Vector2i(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public Vector2i(float var1, float var2, int var3) {
      this.x = Math.roundUsing(var1, var3);
      this.y = Math.roundUsing(var2, var3);
   }

   public Vector2i(double var1, double var3, int var5) {
      this.x = Math.roundUsing(var1, var5);
      this.y = Math.roundUsing(var3, var5);
   }

   public Vector2i(Vector2ic var1) {
      this.x = var1.x();
      this.y = var1.y();
   }

   public Vector2i(Vector2fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
   }

   public Vector2i(Vector2dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
   }

   public Vector2i(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
   }

   public Vector2i(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2i(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public Vector2i(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
   }

   public Vector2i(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public Vector2i set(int var1) {
      this.x = var1;
      this.y = var1;
      return this;
   }

   public Vector2i set(int var1, int var2) {
      this.x = var1;
      this.y = var2;
      return this;
   }

   public Vector2i set(Vector2ic var1) {
      this.x = var1.x();
      this.y = var1.y();
      return this;
   }

   public Vector2i set(Vector2dc var1) {
      this.x = (int)var1.x();
      this.y = (int)var1.y();
      return this;
   }

   public Vector2i set(Vector2dc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      return this;
   }

   public Vector2i set(Vector2fc var1, int var2) {
      this.x = Math.roundUsing(var1.x(), var2);
      this.y = Math.roundUsing(var1.y(), var2);
      return this;
   }

   public Vector2i set(int[] var1) {
      this.x = var1[0];
      this.y = var1[1];
      return this;
   }

   public Vector2i set(ByteBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2i set(int var1, ByteBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2i set(IntBuffer var1) {
      MemUtil.INSTANCE.get(this, var1.position(), var1);
      return this;
   }

   public Vector2i set(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.get(this, var1, var2);
      return this;
   }

   public Vector2i setFromAddress(long var1) {
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
      default:
         throw new IllegalArgumentException();
      }
   }

   public Vector2i setComponent(int var1, int var2) throws IllegalArgumentException {
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

   public IntBuffer get(IntBuffer var1) {
      MemUtil.INSTANCE.put(this, var1.position(), var1);
      return var1;
   }

   public IntBuffer get(int var1, IntBuffer var2) {
      MemUtil.INSTANCE.put(this, var1, var2);
      return var2;
   }

   public Vector2ic getToAddress(long var1) {
      if (Options.NO_UNSAFE) {
         throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
      } else {
         MemUtil.MemUtilUnsafe.put(this, var1);
         return this;
      }
   }

   public Vector2i sub(Vector2ic var1) {
      this.x -= var1.x();
      this.y -= var1.y();
      return this;
   }

   public Vector2i sub(Vector2ic var1, Vector2i var2) {
      var2.x = this.x - var1.x();
      var2.y = this.y - var1.y();
      return var2;
   }

   public Vector2i sub(int var1, int var2) {
      this.x -= var1;
      this.y -= var2;
      return this;
   }

   public Vector2i sub(int var1, int var2, Vector2i var3) {
      var3.x = this.x - var1;
      var3.y = this.y - var2;
      return var3;
   }

   public long lengthSquared() {
      return (long)(this.x * this.x + this.y * this.y);
   }

   public static long lengthSquared(int var0, int var1) {
      return (long)(var0 * var0 + var1 * var1);
   }

   public double length() {
      return (double)Math.sqrt((float)(this.x * this.x + this.y * this.y));
   }

   public static double length(int var0, int var1) {
      return (double)Math.sqrt((float)(var0 * var0 + var1 * var1));
   }

   public double distance(Vector2ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      return (double)Math.sqrt((float)(var2 * var2 + var3 * var3));
   }

   public double distance(int var1, int var2) {
      int var3 = this.x - var1;
      int var4 = this.y - var2;
      return (double)Math.sqrt((float)(var3 * var3 + var4 * var4));
   }

   public long distanceSquared(Vector2ic var1) {
      int var2 = this.x - var1.x();
      int var3 = this.y - var1.y();
      return (long)(var2 * var2 + var3 * var3);
   }

   public long distanceSquared(int var1, int var2) {
      int var3 = this.x - var1;
      int var4 = this.y - var2;
      return (long)(var3 * var3 + var4 * var4);
   }

   public long gridDistance(Vector2ic var1) {
      return (long)(Math.abs(var1.x() - this.x()) + Math.abs(var1.y() - this.y()));
   }

   public long gridDistance(int var1, int var2) {
      return (long)(Math.abs(var1 - this.x()) + Math.abs(var2 - this.y()));
   }

   public static double distance(int var0, int var1, int var2, int var3) {
      int var4 = var0 - var2;
      int var5 = var1 - var3;
      return (double)Math.sqrt((float)(var4 * var4 + var5 * var5));
   }

   public static long distanceSquared(int var0, int var1, int var2, int var3) {
      int var4 = var0 - var2;
      int var5 = var1 - var3;
      return (long)(var4 * var4 + var5 * var5);
   }

   public Vector2i add(Vector2ic var1) {
      this.x += var1.x();
      this.y += var1.y();
      return this;
   }

   public Vector2i add(Vector2ic var1, Vector2i var2) {
      var2.x = this.x + var1.x();
      var2.y = this.y + var1.y();
      return var2;
   }

   public Vector2i add(int var1, int var2) {
      this.x += var1;
      this.y += var2;
      return this;
   }

   public Vector2i add(int var1, int var2, Vector2i var3) {
      var3.x = this.x + var1;
      var3.y = this.y + var2;
      return var3;
   }

   public Vector2i mul(int var1) {
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public Vector2i mul(int var1, Vector2i var2) {
      var2.x = this.x * var1;
      var2.y = this.y * var1;
      return var2;
   }

   public Vector2i mul(Vector2ic var1) {
      this.x *= var1.x();
      this.y *= var1.y();
      return this;
   }

   public Vector2i mul(Vector2ic var1, Vector2i var2) {
      var2.x = this.x * var1.x();
      var2.y = this.y * var1.y();
      return var2;
   }

   public Vector2i mul(int var1, int var2) {
      this.x *= var1;
      this.y *= var2;
      return this;
   }

   public Vector2i mul(int var1, int var2, Vector2i var3) {
      var3.x = this.x * var1;
      var3.y = this.y * var2;
      return var3;
   }

   public Vector2i div(float var1) {
      float var2 = 1.0F / var1;
      this.x = (int)((float)this.x * var2);
      this.y = (int)((float)this.y * var2);
      return this;
   }

   public Vector2i div(float var1, Vector2i var2) {
      float var3 = 1.0F / var1;
      var2.x = (int)((float)this.x * var3);
      var2.y = (int)((float)this.y * var3);
      return var2;
   }

   public Vector2i div(int var1) {
      this.x /= var1;
      this.y /= var1;
      return this;
   }

   public Vector2i div(int var1, Vector2i var2) {
      var2.x = this.x / var1;
      var2.y = this.y / var1;
      return var2;
   }

   public Vector2i zero() {
      this.x = 0;
      this.y = 0;
      return this;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.x);
      var1.writeInt(this.y);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.x = var1.readInt();
      this.y = var1.readInt();
   }

   public Vector2i negate() {
      this.x = -this.x;
      this.y = -this.y;
      return this;
   }

   public Vector2i negate(Vector2i var1) {
      var1.x = -this.x;
      var1.y = -this.y;
      return var1;
   }

   public Vector2i min(Vector2ic var1) {
      this.x = this.x < var1.x() ? this.x : var1.x();
      this.y = this.y < var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2i min(Vector2ic var1, Vector2i var2) {
      var2.x = this.x < var1.x() ? this.x : var1.x();
      var2.y = this.y < var1.y() ? this.y : var1.y();
      return var2;
   }

   public Vector2i max(Vector2ic var1) {
      this.x = this.x > var1.x() ? this.x : var1.x();
      this.y = this.y > var1.y() ? this.y : var1.y();
      return this;
   }

   public Vector2i max(Vector2ic var1, Vector2i var2) {
      var2.x = this.x > var1.x() ? this.x : var1.x();
      var2.y = this.y > var1.y() ? this.y : var1.y();
      return var2;
   }

   public int maxComponent() {
      int var1 = Math.abs(this.x);
      int var2 = Math.abs(this.y);
      return var1 >= var2 ? 0 : 1;
   }

   public int minComponent() {
      int var1 = Math.abs(this.x);
      int var2 = Math.abs(this.y);
      return var1 < var2 ? 0 : 1;
   }

   public Vector2i absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      return this;
   }

   public Vector2i absolute(Vector2i var1) {
      var1.x = Math.abs(this.x);
      var1.y = Math.abs(this.y);
      return var1;
   }

   public int hashCode() {
      byte var1 = 1;
      int var2 = 31 * var1 + this.x;
      var2 = 31 * var2 + this.y;
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
         Vector2i var2 = (Vector2i)var1;
         if (this.x != var2.x) {
            return false;
         } else {
            return this.y == var2.y;
         }
      }
   }

   public boolean equals(int var1, int var2) {
      if (this.x != var1) {
         return false;
      } else {
         return this.y == var2;
      }
   }

   public String toString() {
      return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
   }

   public String toString(NumberFormat var1) {
      String var10000 = var1.format((long)this.x);
      return "(" + var10000 + " " + var1.format((long)this.y) + ")";
   }
}
