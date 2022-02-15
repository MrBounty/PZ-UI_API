package astar.datastructures;

import astar.ISearchNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class OpenSet implements IOpenSet {
   private PriorityQueue Q;
   private ArrayList QA;

   public OpenSet(Comparator var1) {
      this.Q = new PriorityQueue(1000, var1);
      this.QA = new ArrayList(1000);
   }

   public void add(ISearchNode var1) {
      this.Q.add(var1);
      this.QA.add(var1);
   }

   public void remove(ISearchNode var1) {
      this.Q.remove(var1);
      this.QA.remove(var1);
   }

   public ISearchNode poll() {
      return (ISearchNode)this.Q.poll();
   }

   public ISearchNode getNode(ISearchNode var1) {
      ArrayList var2 = this.QA;

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         ISearchNode var4 = (ISearchNode)var2.get(var3);
         if (var4.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public int size() {
      return this.Q.size();
   }

   public void clear() {
      this.Q.clear();
      this.QA.clear();
   }
}
