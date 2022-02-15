package zombie;

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.Stack;

public class IntArrayCache {
   public static IntArrayCache instance = new IntArrayCache();
   TIntObjectHashMap Map = new TIntObjectHashMap();

   public void Init() {
      for(int var1 = 0; var1 < 100; ++var1) {
         Stack var2 = new Stack();

         for(int var3 = 0; var3 < 1000; ++var3) {
            var2.push(new Integer[var1]);
         }
      }

   }

   public void put(Integer[] var1) {
      if (this.Map.containsKey(var1.length)) {
         ((Stack)this.Map.get(var1.length)).push(var1);
      } else {
         Stack var2 = new Stack();
         var2.push(var1);
         this.Map.put(var1.length, var2);
      }

   }

   public Integer[] get(int var1) {
      if (this.Map.containsKey(var1)) {
         Stack var2 = (Stack)this.Map.get(var1);
         if (!var2.isEmpty()) {
            return (Integer[])var2.pop();
         }
      }

      return new Integer[var1];
   }
}
