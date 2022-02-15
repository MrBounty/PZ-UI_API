package astar.datastructures;

import astar.ISearchNode;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Comparator;

public class ClosedSetHash implements IClosedSet {
   private TIntObjectHashMap hashMap = new TIntObjectHashMap();
   private Comparator comp;
   private static final ClosedSetHash.MinNodeProc minNodeProc = new ClosedSetHash.MinNodeProc();

   public ClosedSetHash(Comparator var1) {
      this.comp = var1;
   }

   public boolean contains(ISearchNode var1) {
      return this.hashMap.containsKey(var1.keyCode());
   }

   public void add(ISearchNode var1) {
      this.hashMap.put(var1.keyCode(), var1);
   }

   public ISearchNode min() {
      minNodeProc.comp = this.comp;
      minNodeProc.candidate = null;
      this.hashMap.forEachValue(minNodeProc);
      return minNodeProc.candidate;
   }

   public void clear() {
      this.hashMap.clear();
   }

   private static final class MinNodeProc implements TObjectProcedure {
      Comparator comp;
      ISearchNode candidate;

      public boolean execute(ISearchNode var1) {
         if (this.candidate == null) {
            this.candidate = var1;
            return true;
         } else {
            if (this.comp.compare(var1, this.candidate) < 0) {
               this.candidate = var1;
            }

            return true;
         }
      }
   }
}
