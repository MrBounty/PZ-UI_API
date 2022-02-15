package astar.datastructures;

import astar.ISearchNode;
import java.util.Comparator;

public class OpenSetHash implements IOpenSet {
   private HashPriorityQueue hashQ;
   private Comparator comp;

   public OpenSetHash(Comparator var1) {
      this.hashQ = new HashPriorityQueue(var1);
      this.comp = var1;
   }

   public void add(ISearchNode var1) {
      this.hashQ.add(var1.keyCode(), var1);
   }

   public void remove(ISearchNode var1) {
      this.hashQ.remove(var1.keyCode(), var1);
   }

   public ISearchNode poll() {
      return (ISearchNode)this.hashQ.poll();
   }

   public ISearchNode getNode(ISearchNode var1) {
      return (ISearchNode)this.hashQ.get(var1.keyCode());
   }

   public int size() {
      return this.hashQ.size();
   }

   public String toString() {
      return this.hashQ.getTreeMap().keySet().toString();
   }

   public void clear() {
      this.hashQ.clear();
   }
}
