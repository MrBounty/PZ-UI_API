package zombie.util.list;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public final class PZArrayList extends AbstractList implements List, RandomAccess {
   private Object[] elements;
   private int numElements;
   private static final PZArrayList instance = new PZArrayList(Object.class, 0);

   public PZArrayList(Class var1, int var2) {
      this.elements = (Object[])Array.newInstance(var1, var2);
   }

   public Object get(int var1) {
      if (var1 >= 0 && var1 < this.numElements) {
         return this.elements[var1];
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + " Size: " + this.numElements);
      }
   }

   public int size() {
      return this.numElements;
   }

   public int indexOf(Object var1) {
      for(int var2 = 0; var2 < this.numElements; ++var2) {
         if (var1 == null && this.elements[var2] == null || var1 != null && var1.equals(this.elements[var2])) {
            return var2;
         }
      }

      return -1;
   }

   public boolean isEmpty() {
      return this.numElements == 0;
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) >= 0;
   }

   public Iterator iterator() {
      throw new UnsupportedOperationException();
   }

   public ListIterator listIterator() {
      throw new UnsupportedOperationException();
   }

   public ListIterator listIterator(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean add(Object var1) {
      if (this.numElements == this.elements.length) {
         int var2 = this.elements.length + (this.elements.length >> 1);
         if (var2 < this.numElements + 1) {
            var2 = this.numElements + 1;
         }

         this.elements = Arrays.copyOf(this.elements, var2);
      }

      this.elements[this.numElements] = var1;
      ++this.numElements;
      return true;
   }

   public void add(int var1, Object var2) {
      if (var1 >= 0 && var1 <= this.numElements) {
         if (this.numElements == this.elements.length) {
            int var3 = this.elements.length + this.elements.length >> 1;
            if (var3 < this.numElements + 1) {
               var3 = this.numElements + 1;
            }

            this.elements = Arrays.copyOf(this.elements, var3);
         }

         System.arraycopy(this.elements, var1, this.elements, var1 + 1, this.numElements - var1);
         this.elements[var1] = var2;
         ++this.numElements;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + " Size: " + this.numElements);
      }
   }

   public Object remove(int var1) {
      if (var1 >= 0 && var1 < this.numElements) {
         Object var2 = this.elements[var1];
         int var3 = this.numElements - var1 - 1;
         if (var3 > 0) {
            System.arraycopy(this.elements, var1 + 1, this.elements, var1, var3);
         }

         this.elements[this.numElements - 1] = null;
         --this.numElements;
         return var2;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + " Size: " + this.numElements);
      }
   }

   public boolean remove(Object var1) {
      for(int var2 = 0; var2 < this.numElements; ++var2) {
         if (var1 == null && this.elements[var2] == null || var1 != null && var1.equals(this.elements[var2])) {
            int var3 = this.numElements - var2 - 1;
            if (var3 > 0) {
               System.arraycopy(this.elements, var2 + 1, this.elements, var2, var3);
            }

            this.elements[this.numElements - 1] = null;
            --this.numElements;
            return true;
         }
      }

      return false;
   }

   public Object set(int var1, Object var2) {
      if (var1 >= 0 && var1 < this.numElements) {
         Object var3 = this.elements[var1];
         this.elements[var1] = var2;
         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + " Size: " + this.numElements);
      }
   }

   public void clear() {
      for(int var1 = 0; var1 < this.numElements; ++var1) {
         this.elements[var1] = null;
      }

      this.numElements = 0;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "[]";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append('[');

         for(int var2 = 0; var2 < this.numElements; ++var2) {
            Object var3 = this.elements[var2];
            var1.append(var3 == this ? "(self)" : var3.toString());
            if (var2 == this.numElements - 1) {
               break;
            }

            var1.append(',');
            var1.append(' ');
         }

         return var1.append(']').toString();
      }
   }

   public Object[] getElements() {
      return this.elements;
   }

   public static AbstractList emptyList() {
      return instance;
   }
}
