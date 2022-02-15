package astar.tests;

import astar.AStar;
import astar.ISearchNode;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class AStarTest {
   @Test
   public void SearchNodeTest2D() {
      GoalNode2D var1 = new GoalNode2D(3, 3);
      SearchNode2D var2 = new SearchNode2D(1, 1, (SearchNode2D)null, var1);
      ArrayList var3 = (new AStar()).shortestPath(var2, var1);
      Assert.assertEquals((long)var3.size(), 5L);
   }

   @Test
   public void SearchNodeCityTest() {
      SearchNodeCity var1 = new SearchNodeCity("SaarbrÃ¼cken");
      ArrayList var2 = (new AStar()).shortestPath(var1, new GoalNodeCity("WÃ¼rzburg"));
      double var3 = 1.0E-5D;
      Assert.assertEquals(((ISearchNode)var2.get(0)).f(), 222.0D, var3);
      Assert.assertEquals(((ISearchNode)var2.get(1)).f(), 228.0D, var3);
      Assert.assertEquals(((ISearchNode)var2.get(2)).f(), 269.0D, var3);
      Assert.assertEquals(((ISearchNode)var2.get(3)).f(), 289.0D, var3);
      Assert.assertEquals(var2.toString(), "[SaarbrÃ¼cken,f:222.0, Kaiserslautern,f:228.0, Frankfurt,f:269.0, WÃ¼rzburg,f:289.0]");
   }
}
