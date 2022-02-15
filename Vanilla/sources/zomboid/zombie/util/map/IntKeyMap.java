package zombie.util.map;

import java.util.Collection;
import zombie.util.set.IntSet;

public interface IntKeyMap {
   void clear();

   boolean containsKey(int var1);

   boolean containsValue(Object var1);

   IntKeyMapIterator entries();

   boolean equals(Object var1);

   Object get(int var1);

   int hashCode();

   boolean isEmpty();

   IntSet keySet();

   Object put(int var1, Object var2);

   void putAll(IntKeyMap var1);

   Object remove(int var1);

   int size();

   Collection values();
}
