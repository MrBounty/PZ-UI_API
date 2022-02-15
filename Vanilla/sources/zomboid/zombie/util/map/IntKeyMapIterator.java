package zombie.util.map;

public interface IntKeyMapIterator {
   boolean hasNext();

   void next();

   void remove();

   int getKey();

   Object getValue();
}
