package zombie.util;

public interface IntCollection {
   boolean add(int var1);

   boolean addAll(IntCollection var1);

   void clear();

   boolean contains(int var1);

   boolean containsAll(IntCollection var1);

   boolean equals(Object var1);

   int hashCode();

   boolean isEmpty();

   IntIterator iterator();

   boolean remove(int var1);

   boolean removeAll(IntCollection var1);

   boolean retainAll(IntCollection var1);

   int size();

   int[] toArray();

   int[] toArray(int[] var1);

   void trimToSize();
}
