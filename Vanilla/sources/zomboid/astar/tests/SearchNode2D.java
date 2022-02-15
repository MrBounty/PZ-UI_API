package astar.tests;

import astar.ASearchNode;
import astar.ISearchNode;
import java.util.ArrayList;

public class SearchNode2D extends ASearchNode {
   private int x;
   private int y;
   private SearchNode2D parent;
   private GoalNode2D goal;

   public SearchNode2D(int var1, int var2, SearchNode2D var3, GoalNode2D var4) {
      this.x = var1;
      this.y = var2;
      this.parent = var3;
      this.goal = var4;
   }

   public SearchNode2D getParent() {
      return this.parent;
   }

   public ArrayList getSuccessors() {
      ArrayList var1 = new ArrayList();
      var1.add(new SearchNode2D(this.x - 1, this.y, this, this.goal));
      var1.add(new SearchNode2D(this.x + 1, this.y, this, this.goal));
      var1.add(new SearchNode2D(this.x, this.y + 1, this, this.goal));
      var1.add(new SearchNode2D(this.x, this.y - 1, this, this.goal));
      return var1;
   }

   public double h() {
      return this.dist(this.goal.getX(), this.goal.getY());
   }

   public double c(ISearchNode var1) {
      this.castToSearchNode2D(var1);
      return 1.0D;
   }

   public void getSuccessors(ArrayList var1) {
      var1.add(new SearchNode2D(this.x - 1, this.y, this, this.goal));
      var1.add(new SearchNode2D(this.x + 1, this.y, this, this.goal));
      var1.add(new SearchNode2D(this.x, this.y + 1, this, this.goal));
      var1.add(new SearchNode2D(this.x, this.y - 1, this, this.goal));
   }

   public void setParent(ISearchNode var1) {
      this.parent = this.castToSearchNode2D(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SearchNode2D)) {
         return false;
      } else {
         SearchNode2D var2 = (SearchNode2D)var1;
         return this.x == var2.getX() && this.y == var2.getY();
      }
   }

   public int hashCode() {
      return 41 * (41 + this.x + this.y);
   }

   public double dist(int var1, int var2) {
      return Math.sqrt(Math.pow((double)(this.x - var1), 2.0D) + Math.pow((double)(this.y - var2), 2.0D));
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public String toString() {
      String var10000 = Integer.toString(this.x);
      return "(" + var10000 + ";" + Integer.toString(this.y) + ";h:" + Double.toString(this.h()) + ";g:" + Double.toString(this.g()) + ")";
   }

   private SearchNode2D castToSearchNode2D(ISearchNode var1) {
      return (SearchNode2D)var1;
   }

   public Integer keyCode() {
      return null;
   }
}
