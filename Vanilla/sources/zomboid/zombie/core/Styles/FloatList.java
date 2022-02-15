package zombie.core.Styles;

import java.io.Serializable;

public class FloatList implements Serializable {
   private static final long serialVersionUID = 1L;
   private float[] value;
   private int count;
   private final boolean fastExpand;

   public FloatList() {
      this(0);
   }

   public FloatList(int var1) {
      this(true, var1);
   }

   public FloatList(boolean var1, int var2) {
      this.count = 0;
      this.fastExpand = var1;
      this.value = new float[var2];
   }

   public float add(float var1) {
      if (this.count == this.value.length) {
         float[] var2 = this.value;
         if (this.fastExpand) {
            this.value = new float[(var2.length << 1) + 1];
         } else {
            this.value = new float[var2.length + 1];
         }

         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }

      this.value[this.count] = var1;
      return (float)(this.count++);
   }

   public float remove(int var1) {
      if (var1 < this.count && var1 >= 0) {
         float var2 = this.value[var1];
         if (var1 < this.count - 1) {
            System.arraycopy(this.value, var1 + 1, this.value, var1, this.count - var1 - 1);
         }

         --this.count;
         return var2;
      } else {
         throw new IndexOutOfBoundsException("Referenced " + var1 + ", size=" + this.count);
      }
   }

   public void addAll(float[] var1) {
      this.ensureCapacity(this.count + var1.length);
      System.arraycopy(var1, 0, this.value, this.count, var1.length);
      this.count += var1.length;
   }

   public void addAll(FloatList var1) {
      this.ensureCapacity(this.count + var1.count);
      System.arraycopy(var1.value, 0, this.value, this.count, var1.count);
      this.count += var1.count;
   }

   public float[] array() {
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
         float[] var2 = this.value;
         this.value = new float[var1];
         System.arraycopy(var2, 0, this.value, 0, var2.length);
      }
   }

   public float get(int var1) {
      return this.value[var1];
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public int size() {
      return this.count;
   }

   public void toArray(Object[] var1) {
      System.arraycopy(this.value, 0, var1, 0, this.count);
   }

   public void trimToSize() {
      if (this.count != this.value.length) {
         float[] var1 = this.value;
         this.value = new float[this.count];
         System.arraycopy(var1, 0, this.value, 0, this.count);
      }
   }
}
