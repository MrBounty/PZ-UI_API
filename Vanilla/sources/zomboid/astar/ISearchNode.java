package astar;

import java.util.ArrayList;

public interface ISearchNode {
   double f();

   double g();

   void setG(double var1);

   double h();

   double c(ISearchNode var1);

   void getSuccessors(ArrayList var1);

   ISearchNode getParent();

   void setParent(ISearchNode var1);

   Integer keyCode();

   boolean equals(Object var1);

   int hashCode();

   int getDepth();

   void setDepth(int var1);
}
