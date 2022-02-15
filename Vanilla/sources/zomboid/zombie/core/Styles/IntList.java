package zombie.core.Styles;

import java.io.Serializable;

public class IntList implements Serializable {
   private static final long serialVersionUID = 1L;
   private int[] value;
   private int count;
   private final boolean fastExpand;

   public IntList() {
      this(0);
   }

   public IntList(int var1) {
      this(true, var1);
   }

   public IntList(boolean var1, int var2) {
      this.count = 0;
      this.fastExpand = var1;
      this.value = new int[var2];
   }

   public int add(short var1) {
      if (this.count == this.value.length) {
         int[] var2 = this.value;
         if (this.fastExpand) {
            this.value = new int[(var2.length << 1) + 1];
         } else {
            this.value = new int[var2.length + 1];
         }

         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }

      this.value[this.count] = var1;
      return this.count++;
   }

   public int remove(int var1) {
      if (var1 < this.count && var1 >= 0) {
         int var2 = this.value[var1];
         if (var1 < this.count - 1) {
            System.arraycopy(this.value, var1 + 1, this.value, var1, this.count - var1 - 1);
         }

         --this.count;
         return var2;
      } else {
         throw new IndexOutOfBoundsException("Referenced " + var1 + ", size=" + this.count);
      }
   }

   public void addAll(short[] var1) {
      this.ensureCapacity(this.count + var1.length);
      System.arraycopy(var1, 0, this.value, this.count, var1.length);
      this.count += var1.length;
   }

   public void addAll(IntList var1) {
      this.ensureCapacity(this.count + var1.count);
      System.arraycopy(var1.value, 0, this.value, this.count, var1.count);
      this.count += var1.count;
   }

   public int[] array() {
      return this.value;
   }

   public int capacity() {
      return this.value.length;
   }

   public void clear() {
      this.count = 0;
   }

   public void ensureCapacity(int var1) {
      if (this.value.length < var1) {
         int[] var2 = this.value;
         this.value = new int[var1];
         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }
   }

   public int get(int var1) {
      return this.value[var1];
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public int size() {
      return this.count;
   }

   public short[] toArray(short[] var1) {
      if (var1 == null) {
         var1 = new short[this.count];
      }

      System.arraycopy(this.value, 0, var1, 0, this.count);
      return var1;
   }

   public void trimToSize() {
      if (this.count != this.value.length) {
         int[] var1 = this.value;
         this.value = new int[this.count];
         System.arraycopy(var1, 0, this.value, 0, this.count);
      }
   }
}
