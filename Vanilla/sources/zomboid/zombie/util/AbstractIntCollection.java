package zombie.util;

import zombie.util.util.Display;
import zombie.util.util.Exceptions;

public abstract class AbstractIntCollection implements IntCollection {
   protected AbstractIntCollection() {
   }

   public boolean add(int var1) {
      Exceptions.unsupported("add");
      return false;
   }

   public boolean addAll(IntCollection var1) {
      IntIterator var2 = var1.iterator();

      boolean var3;
      for(var3 = false; var2.hasNext(); var3 |= this.add(var2.next())) {
      }

      return var3;
   }

   public void clear() {
      IntIterator var1 = this.iterator();

      while(var1.hasNext()) {
         var1.next();
         var1.remove();
      }

   }

   public boolean contains(int var1) {
      IntIterator var2 = this.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(var2.next() != var1);

      return true;
   }

   public boolean containsAll(IntCollection var1) {
      IntIterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(this.contains(var2.next()));

      return false;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean remove(int var1) {
      IntIterator var2 = this.iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         if (var2.next() == var1) {
            var2.remove();
            var3 = true;
            break;
         }
      }

      return var3;
   }

   public boolean removeAll(IntCollection var1) {
      if (var1 == null) {
         Exceptions.nullArgument("collection");
      }

      IntIterator var2 = this.iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         if (var1.contains(var2.next())) {
            var2.remove();
            var3 = true;
         }
      }

      return var3;
   }

   public boolean retainAll(IntCollection var1) {
      if (var1 == null) {
         Exceptions.nullArgument("collection");
      }

      IntIterator var2 = this.iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         if (!var1.contains(var2.next())) {
            var2.remove();
            var3 = true;
         }
      }

      return var3;
   }

   public int size() {
      IntIterator var1 = this.iterator();

      int var2;
      for(var2 = 0; var1.hasNext(); ++var2) {
         var1.next();
      }

      return var2;
   }

   public int[] toArray() {
      return this.toArray((int[])null);
   }

   public int[] toArray(int[] var1) {
      int var2 = this.size();
      if (var1 == null || var1.length < var2) {
         var1 = new int[var2];
      }

      IntIterator var3 = this.iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         var1[var4] = var3.next();
      }

      return var1;
   }

   public void trimToSize() {
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[');

      for(IntIterator var2 = this.iterator(); var2.hasNext(); var1.append(Display.display(var2.next()))) {
         if (var1.length() > 1) {
            var1.append(',');
         }
      }

      var1.append(']');
      return var1.toString();
   }
}
