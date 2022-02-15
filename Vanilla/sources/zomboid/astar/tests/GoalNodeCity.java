package astar.tests;

import astar.IGoalNode;
import astar.ISearchNode;

public class GoalNodeCity implements IGoalNode {
   private String name;

   public GoalNodeCity(String var1) {
      this.name = var1;
   }

   public boolean inGoal(ISearchNode var1) {
      if (var1 instanceof SearchNodeCity) {
         SearchNodeCity var2 = (SearchNodeCity)var1;
         return this.name == var2.getName();
      } else {
         return false;
      }
   }
}
