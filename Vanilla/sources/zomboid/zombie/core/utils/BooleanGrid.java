package zombie.core.utils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BooleanGrid implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   private final int width;
   private final int height;
   private final int bitWidth;
   private final int[] value;

   public BooleanGrid(int var1, int var2) {
      this.bitWidth = var1;
      this.width = var1 / 32 + (var1 % 32 != 0 ? 1 : 0);
      this.height = var2;
      this.value = new int[this.width * this.height];
   }

   public BooleanGrid clone() throws CloneNotSupportedException {
      BooleanGrid var1 = new BooleanGrid(this.bitWidth, this.height);
      System.arraycopy(this.value, 0, var1.value, 0, this.value.length);
      return var1;
   }

   public void copy(BooleanGrid var1) {
      if (var1.bitWidth == this.bitWidth && var1.height == this.height) {
         System.arraycopy(var1.value, 0, this.value, 0, var1.value.length);
      } else {
         throw new IllegalArgumentException("src must be same size as this: " + var1 + " cannot be copied into " + this);
      }
   }

   public void clear() {
      Arrays.fill(this.value, 0);
   }

   public void fill() {
      Arrays.fill(this.value, -1);
   }

   private int getIndex(int var1, int var2) {
      return var1 >= 0 && var2 >= 0 && var1 < this.width && var2 < this.height ? var1 + var2 * this.width : -1;
   }

   public boolean getValue(int var1, int var2) {
      if (var1 < this.bitWidth && var1 >= 0 && var2 < this.height && var2 >= 0) {
         int var3 = var1 / 32;
         int var4 = 1 << (var1 & 31);
         int var5 = this.getIndex(var3, var2);
         if (var5 == -1) {
            return false;
         } else {
            int var6 = this.value[var5];
            return (var6 & var4) != 0;
         }
      } else {
         return false;
      }
   }

   public void setValue(int var1, int var2, boolean var3) {
      if (var1 < this.bitWidth && var1 >= 0 && var2 < this.height && var2 >= 0) {
         int var4 = var1 / 32;
         int var5 = 1 << (var1 & 31);
         int var6 = this.getIndex(var4, var2);
         if (var6 != -1) {
            int[] var10000;
            if (var3) {
               var10000 = this.value;
               var10000[var6] |= var5;
            } else {
               var10000 = this.value;
               var10000[var6] &= ~var5;
            }

         }
      }
   }

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public String toString() {
      return "BooleanGrid [width=" + this.width + ", height=" + this.height + ", bitWidth=" + this.bitWidth + "]";
   }

   public void LoadFromByteBuffer(ByteBuffer var1) {
      int var2 = this.width * this.height;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.value[var3] = var1.getInt();
      }

   }

   public void PutToByteBuffer(ByteBuffer var1) {
      int var2 = this.width * this.height;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.putInt(this.value[var3]);
      }

   }
}
