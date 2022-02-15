package astar.datastructures;

import astar.ISearchNode;

public interface IClosedSet {
   boolean contains(ISearchNode var1);

   void add(ISearchNode var1);

   ISearchNode min();

   void clear();
}
