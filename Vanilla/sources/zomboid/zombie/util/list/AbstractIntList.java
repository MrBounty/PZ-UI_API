package zombie.util.list;

import zombie.util.AbstractIntCollection;
import zombie.util.IntCollection;
import zombie.util.IntIterator;
import zombie.util.hash.DefaultIntHashFunction;
import zombie.util.util.Exceptions;

public abstract class AbstractIntList extends AbstractIntCollection implements IntList {
   protected AbstractIntList() {
   }

   public boolean add(int var1) {
      this.add(this.size(), var1);
      return true;
   }

   public void add(int var1, int var2) {
      Exceptions.unsupported("add");
   }

   public boolean addAll(int var1, IntCollection var2) {
      if (var1 < 0 || var1 > this.size()) {
         Exceptions.indexOutOfBounds(var1, 0, this.size());
      }

      IntIterator var3 = var2.iterator();

      boolean var4;
      for(var4 = var3.hasNext(); var3.hasNext(); ++var1) {
         this.add(var1, var3.next());
      }

      return var4;
   }

   public int indexOf(int var1) {
      return this.indexOf(0, var1);
   }

   public int indexOf(int var1, int var2) {
      IntListIterator var3 = this.listIterator(var1);

      do {
         if (!var3.hasNext()) {
            return -1;
         }
      } while(var3.next() != var2);

      return var3.previousIndex();
   }

   public IntIterator iterator() {
      return this.listIterator();
   }

   public int lastIndexOf(int var1) {
      IntListIterator var2 = this.listIterator(this.size());

      do {
         if (!var2.hasPrevious()) {
            return -1;
         }
      } while(var2.previous() != var1);

      return var2.nextIndex();
   }

   public int lastIndexOf(int var1, int var2) {
      IntListIterator var3 = this.listIterator(var1);

      do {
         if (!var3.hasPrevious()) {
            return -1;
         }
      } while(var3.previous() != var2);

      return var3.nextIndex();
   }

   public IntListIterator listIterator() {
      return this.listIterator(0);
   }

   public IntListIterator listIterator(final int var1) {
      if (var1 < 0 || var1 > this.size()) {
         Exceptions.indexOutOfBounds(var1, 0, this.size());
      }

      return new IntListIterator() {
         private int ptr = var1;
         private int lptr = -1;

         public boolean hasNext() {
            return this.ptr < AbstractIntList.this.size();
         }

         public int next() {
            if (this.ptr == AbstractIntList.this.size()) {
               Exceptions.endOfIterator();
            }

            this.lptr = this.ptr++;
            return AbstractIntList.this.get(this.lptr);
         }

         public void remove() {
            if (this.lptr == -1) {
               Exceptions.noElementToRemove();
            }

            AbstractIntList.this.removeElementAt(this.lptr);
            if (this.lptr < this.ptr) {
               --this.ptr;
            }

            this.lptr = -1;
         }

         public void add(int var1x) {
            AbstractIntList.this.add(this.ptr++, var1x);
            this.lptr = -1;
         }

         public boolean hasPrevious() {
            return this.ptr > 0;
         }

         public int nextIndex() {
            return this.ptr;
         }

         public int previous() {
            if (this.ptr == 0) {
               Exceptions.startOfIterator();
            }

            --this.ptr;
            this.lptr = this.ptr;
            return AbstractIntList.this.get(this.ptr);
         }

         public int previousIndex() {
            return this.ptr - 1;
         }

         public void set(int var1x) {
            if (this.lptr == -1) {
               Exceptions.noElementToSet();
            }

            AbstractIntList.this.set(this.lptr, var1x);
         }
      };
   }

   public int removeElementAt(int var1) {
      Exceptions.unsupported("removeElementAt");
      throw new RuntimeException();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof IntList)) {
         return false;
      } else {
         IntListIterator var2 = this.listIterator();
         IntListIterator var3 = ((IntList)var1).listIterator();

         while(var2.hasNext() && var3.hasNext()) {
            if (var2.next() != var3.next()) {
               return false;
            }
         }

         return !var2.hasNext() && !var3.hasNext();
      }
   }

   public int hashCode() {
      int var1 = 1;

      for(IntIterator var2 = this.iterator(); var2.hasNext(); var1 = 31 * var1 + DefaultIntHashFunction.INSTANCE.hash(var2.next())) {
      }

      return var1;
   }
}
