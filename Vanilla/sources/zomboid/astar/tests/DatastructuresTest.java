package astar.tests;

import astar.datastructures.HashPriorityQueue;
import java.util.Comparator;
import org.junit.Assert;
import org.junit.Test;

public class DatastructuresTest {
   @Test
   public void hashPriorityQueueTest() {
      class InconsistentComparator implements Comparator {
         public int compare(Integer var1, Integer var2) {
            return 0;
         }
      }

      HashPriorityQueue var1 = new HashPriorityQueue(new InconsistentComparator());
      var1.add(0, 0);
      var1.add(1, 1);
      var1.add(2, 2);
      var1.add(3, 3);
      var1.remove(1, 1);
      Assert.assertEquals(true, var1.contains(0));
      Assert.assertEquals(false, var1.contains(1));
      Assert.assertEquals(true, var1.contains(2));
      Assert.assertEquals(true, var1.contains(3));
      var1.remove(0, 0);
      Assert.assertEquals(false, var1.contains(0));
      Assert.assertEquals(true, var1.contains(2));
      Assert.assertEquals(true, var1.contains(3));
      var1.remove(3, 3);
      Assert.assertEquals(true, var1.contains(2));
      Assert.assertEquals(false, var1.contains(3));
      var1.clear();
      var1.add(0, 0);
      var1.add(1, 1);
      var1.add(2, 2);
      var1.add(3, 3);
      int var2 = (Integer)var1.poll();
      Assert.assertEquals(0L, (long)var1.size());
   }
}
