package astar;

public abstract class ASearchNode implements ISearchNode {
   private double g = 0.0D;
   private int depth;

   public double f() {
      return this.g() + this.h();
   }

   public double g() {
      return this.g;
   }

   public void setG(double var1) {
      this.g = var1;
   }

   public int getDepth() {
      return this.depth;
   }

   public void setDepth(int var1) {
      this.depth = var1;
   }
}
