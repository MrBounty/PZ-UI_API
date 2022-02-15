package astar.datastructures;

import astar.ISearchNode;

public interface IOpenSet {
   void add(ISearchNode var1);

   void remove(ISearchNode var1);

   ISearchNode poll();

   ISearchNode getNode(ISearchNode var1);

   int size();

   void clear();
}
