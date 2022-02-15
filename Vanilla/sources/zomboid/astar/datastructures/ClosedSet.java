package astar.datastructures;

import astar.ISearchNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClosedSet implements IClosedSet {
   private ArrayList list = new ArrayList();
   private Comparator comp;

   public ClosedSet(Comparator var1) {
      this.comp = var1;
   }

   public boolean contains(ISearchNode var1) {
      return this.list.contains(var1);
   }

   public void add(ISearchNode var1) {
      this.list.add(var1);
   }

   public ISearchNode min() {
      return (ISearchNode)Collections.min(this.list, this.comp);
   }

   public void clear() {
      this.list.clear();
   }
}
