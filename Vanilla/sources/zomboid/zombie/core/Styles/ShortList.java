package zombie.core.Styles;

import java.io.Serializable;

public class ShortList implements Serializable {
   private static final long serialVersionUID = 1L;
   private short[] value;
   private short count;
   private final boolean fastExpand;

   public ShortList() {
      this(0);
   }

   public ShortList(int var1) {
      this(true, var1);
   }

   public ShortList(boolean var1, int var2) {
      this.count = 0;
      this.fastExpand = var1;
      this.value = new short[var2];
   }

   public short add(short var1) {
      if (this.count == this.value.length) {
         short[] var2 = this.value;
         if (this.fastExpand) {
            this.value = new short[(var2.length << 1) + 1];
         } else {
            this.value = new short[var2.length + 1];
         }

         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }

      this.value[this.count] = var1;
      short var10002 = this.count;
      this.count = (short)(var10002 + 1);
      return var10002;
   }

   public short remove(int var1) {
      if (var1 < this.count && var1 >= 0) {
         short var2 = this.value[var1];
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
      this.count = (short)(this.count + var1.length);
   }

   public void addAll(ShortList var1) {
      this.ensureCapacity(this.count + var1.count);
      System.arraycopy(var1.value, 0, this.value, this.count, var1.count);
      this.count += var1.count;
   }

   public short[] array() {
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
         short[] var2 = this.value;
         this.value = new short[var1];
         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }
   }

   public short get(int var1) {
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
         short[] var1 = this.value;
         this.value = new short[this.count];
         System.arraycopy(var1, 0, this.value, 0, this.count);
      }
   }
}
