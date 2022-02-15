package zombie.core.utils;

import java.util.Arrays;

public class IntHyperCube2 {
   private static final long serialVersionUID = 1L;
   private final int width;
   private final int height;
   private final int depth;
   private final int quanta;
   private final int wxh;
   private final int wxhxd;
   private final int[][][][] value;

   public IntHyperCube2(int var1, int var2, int var3, int var4) {
      this.width = var1;
      this.height = var2;
      this.depth = var3;
      this.quanta = var4;
      this.wxh = var1 * var2;
      this.wxhxd = this.wxh * var3;
      this.value = new int[var1][var2][var3][var4];
   }

   public void clear() {
      Arrays.fill(this.value, 0);
   }

   public void fill(int var1) {
      Arrays.fill(this.value, var1);
   }

   private int getIndex(int var1, int var2, int var3, int var4) {
      return var1 >= 0 && var2 >= 0 && var3 >= 0 && var4 >= 0 && var1 < this.width && var2 < this.height && var3 < this.depth && var4 < this.quanta ? var1 + var2 * this.width + var3 * this.wxh + var4 * this.wxhxd : -1;
   }

   public int getValue(int var1, int var2, int var3, int var4) {
      return var1 >= 0 && var2 >= 0 && var3 >= 0 && var4 >= 0 && var1 < this.width && var2 < this.height && var3 < this.depth && var4 < this.quanta ? this.value[var1][var2][var3][var4] : 0;
   }

   public void setValue(int var1, int var2, int var3, int var4, int var5) {
      this.value[var1][var2][var3][var4] = var5;
   }

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public final int getDepth() {
      return this.depth;
   }

   public final int getQuanta() {
      return this.quanta;
   }
}
