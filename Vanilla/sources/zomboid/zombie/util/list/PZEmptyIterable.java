package zombie.util.list;

import java.util.Iterator;

public final class PZEmptyIterable implements Iterable {
   private static final PZEmptyIterable instance = new PZEmptyIterable();
   private final Iterator s_it = new Iterator() {
      public boolean hasNext() {
         return false;
      }

      public Object next() {
         throw new ArrayIndexOutOfBoundsException("Empty Iterator. Has no data.");
      }
   };

   private PZEmptyIterable() {
   }

   public static PZEmptyIterable getInstance() {
      return instance;
   }

   public Iterator iterator() {
      return this.s_it;
   }
}
