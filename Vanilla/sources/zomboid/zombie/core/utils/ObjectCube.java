package zombie.core.utils;

import java.util.Arrays;
import zombie.debug.DebugLog;

public class ObjectCube implements Cloneable {
   private final int width;
   private final int height;
   private final int depth;
   private final Object[] value;

   public ObjectCube(int var1, int var2, int var3) {
      DebugLog.log("Created object cube of size " + var1 + "x" + var2 + "x" + var3 + " (" + var1 * var2 * var3 * 4 + " bytes)");
      this.width = var1;
      this.height = var2;
      this.depth = var3;
      this.value = new Object[var1 * var2 * var3];
   }

   public ObjectCube clone() throws CloneNotSupportedException {
      ObjectCube var1 = new ObjectCube(this.width, this.height, this.depth);
      System.arraycopy(this.value, 0, var1.value, 0, this.value.length);
      return var1;
   }

   public void clear() {
      Arrays.fill(this.value, (Object)null);
   }

   public void fill(Object var1) {
      Arrays.fill(this.value, var1);
   }

   private int getIndex(int var1, int var2, int var3) {
      return var1 >= 0 && var2 >= 0 && var3 >= 0 && var1 < this.width && var2 < this.height && var3 < this.depth ? var1 + var2 * this.width + var3 * this.width * this.height : -1;
   }

   public Object getValue(int var1, int var2, int var3) {
      int var4 = this.getIndex(var1, var2, var3);
      return var4 == -1 ? null : this.value[var4];
   }

   public void setValue(int var1, int var2, int var3, Object var4) {
      int var5 = this.getIndex(var1, var2, var3);
      if (var5 != -1) {
         this.value[var5] = var4;
      }
   }

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public int getDepth() {
      return this.depth;
   }
}
