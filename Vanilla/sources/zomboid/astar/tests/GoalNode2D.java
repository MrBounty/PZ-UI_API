package astar.tests;

import astar.IGoalNode;
import astar.ISearchNode;

public class GoalNode2D implements IGoalNode {
   private int x;
   private int y;

   public GoalNode2D(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public boolean inGoal(ISearchNode var1) {
      if (!(var1 instanceof SearchNode2D)) {
         return false;
      } else {
         SearchNode2D var2 = (SearchNode2D)var1;
         return this.x == var2.getX() && this.y == var2.getY();
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
