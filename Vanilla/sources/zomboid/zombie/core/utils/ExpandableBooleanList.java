package zombie.core.utils;

import java.io.Serializable;
import java.util.Arrays;

public class ExpandableBooleanList implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   private int width;
   private int bitWidth;
   private int[] value;

   public ExpandableBooleanList(int var1) {
      this.bitWidth = var1;
      this.width = var1 / 32 + (var1 % 32 != 0 ? 1 : 0);
      this.value = new int[this.width];
   }

   public ExpandableBooleanList clone() throws CloneNotSupportedException {
      ExpandableBooleanList var1 = new ExpandableBooleanList(this.bitWidth);
      System.arraycopy(this.value, 0, var1.value, 0, this.value.length);
      return var1;
   }

   public void clear() {
      Arrays.fill(this.value, 0);
   }

   public void fill() {
      Arrays.fill(this.value, -1);
   }

   public boolean getValue(int var1) {
      if (var1 >= 0 && var1 < this.bitWidth) {
         int var2 = var1 >> 5;
         int var3 = 1 << (var1 & 31);
         int var4 = this.value[var2];
         return (var4 & var3) != 0;
      } else {
         return false;
      }
   }

   public void setValue(int var1, boolean var2) {
      if (var1 >= 0) {
         if (var1 >= this.bitWidth) {
            int[] var3 = this.value;
            this.bitWidth = Math.max(this.bitWidth * 2, var1 + 1);
            this.width = this.bitWidth / 32 + (this.width % 32 != 0 ? 1 : 0);
            this.value = new int[this.width];
            System.arraycopy(var3, 0, this.value, 0, var3.length);
         }

         int var5 = var1 >> 5;
         int var4 = 1 << (var1 & 31);
         int[] var10000;
         if (var2) {
            var10000 = this.value;
            var10000[var5] |= var4;
         } else {
            var10000 = this.value;
            var10000[var5] &= ~var4;
         }

      }
   }

   public final int getWidth() {
      return this.width;
   }
}
