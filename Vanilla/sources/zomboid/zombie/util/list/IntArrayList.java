package zombie.util.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import zombie.util.IntCollection;
import zombie.util.hash.DefaultIntHashFunction;
import zombie.util.util.Exceptions;

public class IntArrayList extends AbstractIntList implements Cloneable, Serializable {
   private static final long serialVersionUID = 1L;
   private static final int GROWTH_POLICY_RELATIVE = 0;
   private static final int GROWTH_POLICY_ABSOLUTE = 1;
   private static final int DEFAULT_GROWTH_POLICY = 0;
   public static final double DEFAULT_GROWTH_FACTOR = 1.0D;
   public static final int DEFAULT_GROWTH_CHUNK = 10;
   public static final int DEFAULT_CAPACITY = 10;
   private transient int[] data;
   private int size;
   private int growthPolicy;
   private double growthFactor;
   private int growthChunk;

   private IntArrayList(int var1, int var2, double var3, int var5) {
      if (var1 < 0) {
         Exceptions.negativeArgument("capacity", String.valueOf(var1));
      }

      if (var3 < 0.0D) {
         Exceptions.negativeArgument("growthFactor", String.valueOf(var3));
      }

      if (var5 < 0) {
         Exceptions.negativeArgument("growthChunk", String.valueOf(var5));
      }

      this.data = new int[var1];
      this.size = 0;
      this.growthPolicy = var2;
      this.growthFactor = var3;
      this.growthChunk = var5;
   }

   public IntArrayList() {
      this(10);
   }

   public IntArrayList(IntCollection var1) {
      this(var1.size());
      this.addAll(var1);
   }

   public IntArrayList(int[] var1) {
      this(var1.length);
      System.arraycopy(var1, 0, this.data, 0, var1.length);
      this.size = var1.length;
   }

   public IntArrayList(int var1) {
      this(var1, 1.0D);
   }

   public IntArrayList(int var1, double var2) {
      this(var1, 0, var2, 10);
   }

   public IntArrayList(int var1, int var2) {
      this(var1, 1, 1.0D, var2);
   }

   private int computeCapacity(int var1) {
      int var2;
      if (this.growthPolicy == 0) {
         var2 = (int)((double)this.data.length * (1.0D + this.growthFactor));
      } else {
         var2 = this.data.length + this.growthChunk;
      }

      if (var2 < var1) {
         var2 = var1;
      }

      return var2;
   }

   public int ensureCapacity(int var1) {
      if (var1 > this.data.length) {
         int[] var2 = new int[var1 = this.computeCapacity(var1)];
         System.arraycopy(this.data, 0, var2, 0, this.size);
         this.data = var2;
      }

      return var1;
   }

   public int capacity() {
      return this.data.length;
   }

   public void add(int var1, int var2) {
      if (var1 < 0 || var1 > this.size) {
         Exceptions.indexOutOfBounds(var1, 0, this.size);
      }

      this.ensureCapacity(this.size + 1);
      int var3 = this.size - var1;
      if (var3 > 0) {
         System.arraycopy(this.data, var1, this.data, var1 + 1, var3);
      }

      this.data[var1] = var2;
      ++this.size;
   }

   public int get(int var1) {
      if (var1 < 0 || var1 >= this.size) {
         Exceptions.indexOutOfBounds(var1, 0, this.size - 1);
      }

      return this.data[var1];
   }

   public int set(int var1, int var2) {
      if (var1 < 0 || var1 >= this.size) {
         Exceptions.indexOutOfBounds(var1, 0, this.size - 1);
      }

      int var3 = this.data[var1];
      this.data[var1] = var2;
      return var3;
   }

   public int removeElementAt(int var1) {
      if (var1 < 0 || var1 >= this.size) {
         Exceptions.indexOutOfBounds(var1, 0, this.size - 1);
      }

      int var2 = this.data[var1];
      int var3 = this.size - (var1 + 1);
      if (var3 > 0) {
         System.arraycopy(this.data, var1 + 1, this.data, var1, var3);
      }

      --this.size;
      return var2;
   }

   public void trimToSize() {
      if (this.data.length > this.size) {
         int[] var1 = new int[this.size];
         System.arraycopy(this.data, 0, var1, 0, this.size);
         this.data = var1;
      }

   }

   public Object clone() {
      try {
         IntArrayList var1 = (IntArrayList)super.clone();
         var1.data = new int[this.data.length];
         System.arraycopy(this.data, 0, var1.data, 0, this.size);
         return var1;
      } catch (CloneNotSupportedException var2) {
         Exceptions.cloning();
         return null;
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      this.size = 0;
   }

   public boolean contains(int var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.data[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public int indexOf(int var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.data[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public int indexOf(int var1, int var2) {
      if (var1 < 0 || var1 > this.size) {
         Exceptions.indexOutOfBounds(var1, 0, this.size);
      }

      for(int var3 = var1; var3 < this.size; ++var3) {
         if (this.data[var3] == var2) {
            return var3;
         }
      }

      return -1;
   }

   public int lastIndexOf(int var1) {
      for(int var2 = this.size - 1; var2 >= 0; --var2) {
         if (this.data[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public boolean remove(int var1) {
      int var2 = this.indexOf(var1);
      if (var2 != -1) {
         this.removeElementAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public int[] toArray() {
      int[] var1 = new int[this.size];
      System.arraycopy(this.data, 0, var1, 0, this.size);
      return var1;
   }

   public int[] toArray(int[] var1) {
      if (var1 == null || var1.length < this.size) {
         var1 = new int[this.size];
      }

      System.arraycopy(this.data, 0, var1, 0, this.size);
      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof IntList)) {
         return false;
      } else {
         int var2 = 0;
         IntListIterator var3 = ((IntList)var1).listIterator();

         while(var2 < this.size && var3.hasNext()) {
            if (this.data[var2++] != var3.next()) {
               return false;
            }
         }

         return var2 >= this.size && !var3.hasNext();
      }
   }

   public int hashCode() {
      int var1 = 1;

      for(int var2 = 0; var2 < this.size; ++var2) {
         var1 = 31 * var1 + DefaultIntHashFunction.INSTANCE.hash(this.data[var2]);
      }

      return var1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.data.length);

      for(int var2 = 0; var2 < this.size; ++var2) {
         var1.writeInt(this.data[var2]);
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.data = new int[var1.readInt()];

      for(int var2 = 0; var2 < this.size; ++var2) {
         this.data[var2] = var1.readInt();
      }

   }
}
