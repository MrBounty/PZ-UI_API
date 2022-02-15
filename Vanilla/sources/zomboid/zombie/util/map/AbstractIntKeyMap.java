package zombie.util.map;

import zombie.util.hash.DefaultIntHashFunction;

public abstract class AbstractIntKeyMap implements IntKeyMap {
   protected AbstractIntKeyMap() {
   }

   public void clear() {
      IntKeyMapIterator var1 = this.entries();

      while(var1.hasNext()) {
         var1.next();
         var1.remove();
      }

   }

   public Object remove(int var1) {
      IntKeyMapIterator var2 = this.entries();

      do {
         if (!var2.hasNext()) {
            return null;
         }

         var2.next();
      } while(var2.getKey() != var1);

      Object var3 = var2.getValue();
      var2.remove();
      return var3;
   }

   public void putAll(IntKeyMap var1) {
      IntKeyMapIterator var2 = var1.entries();

      while(var2.hasNext()) {
         var2.next();
         this.put(var2.getKey(), var2.getValue());
      }

   }

   public boolean containsKey(int var1) {
      IntKeyMapIterator var2 = this.entries();

      do {
         if (!var2.hasNext()) {
            return false;
         }

         var2.next();
      } while(var2.getKey() != var1);

      return true;
   }

   public Object get(int var1) {
      IntKeyMapIterator var2 = this.entries();

      do {
         if (!var2.hasNext()) {
            return null;
         }

         var2.next();
      } while(var2.getKey() != var1);

      return var2.getValue();
   }

   public boolean containsValue(Object var1) {
      IntKeyMapIterator var2 = this.entries();
      if (var1 == null) {
         while(var2.hasNext()) {
            var2.next();
            if (var1 == null) {
               return true;
            }
         }
      } else {
         while(var2.hasNext()) {
            var2.next();
            if (var1.equals(var2.getValue())) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof IntKeyMap)) {
         return false;
      } else {
         IntKeyMap var2 = (IntKeyMap)var1;
         if (this.size() != var2.size()) {
            return false;
         } else {
            IntKeyMapIterator var3 = this.entries();

            while(var3.hasNext()) {
               var3.next();
               int var4 = var3.getKey();
               Object var5 = var3.getValue();
               if (var5 == null) {
                  if (var2.get(var4) != null) {
                     return false;
                  }

                  if (!var2.containsKey(var4)) {
                     return false;
                  }
               } else if (!var5.equals(var2.get(var4))) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int var1 = 0;

      for(IntKeyMapIterator var2 = this.entries(); var2.hasNext(); var1 += DefaultIntHashFunction.INSTANCE.hash(var2.getKey()) ^ var2.getValue().hashCode()) {
         var2.next();
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public int size() {
      int var1 = 0;

      for(IntKeyMapIterator var2 = this.entries(); var2.hasNext(); ++var1) {
         var2.next();
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[');
      IntKeyMapIterator var2 = this.entries();

      while(var2.hasNext()) {
         if (var1.length() > 1) {
            var1.append(',');
         }

         var2.next();
         var1.append(String.valueOf(var2.getKey()));
         var1.append("->");
         var1.append(String.valueOf(var2.getValue()));
      }

      var1.append(']');
      return var1.toString();
   }

   public void trimToSize() {
   }
}
