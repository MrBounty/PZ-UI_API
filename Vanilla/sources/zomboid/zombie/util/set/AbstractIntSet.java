package zombie.util.set;

import zombie.util.AbstractIntCollection;
import zombie.util.IntIterator;
import zombie.util.hash.DefaultIntHashFunction;

public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet {
   protected AbstractIntSet() {
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof IntSet)) {
         return false;
      } else {
         IntSet var2 = (IntSet)var1;
         return var2.size() != this.size() ? false : this.containsAll(var2);
      }
   }

   public int hashCode() {
      int var1 = 0;

      for(IntIterator var2 = this.iterator(); var2.hasNext(); var1 += DefaultIntHashFunction.INSTANCE.hash(var2.next())) {
      }

      return var1;
   }
}
