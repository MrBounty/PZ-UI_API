package zombie;

import java.util.HashMap;

public class CollisionMatrixPrototypes {
   public static CollisionMatrixPrototypes instance = new CollisionMatrixPrototypes();
   public HashMap Map = new HashMap();

   public int ToBitMatrix(boolean[][][] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < 3; ++var3) {
         for(int var4 = 0; var4 < 3; ++var4) {
            for(int var5 = 0; var5 < 3; ++var5) {
               if (var1[var3][var4][var5]) {
                  var2 = BitMatrix.Set(var2, var3 - 1, var4 - 1, var5 - 1, true);
               }
            }
         }
      }

      return var2;
   }

   public boolean[][][] Add(int var1) {
      if (this.Map.containsKey(var1)) {
         return (boolean[][][])this.Map.get(var1);
      } else {
         boolean[][][] var2 = new boolean[3][3][3];

         for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
               for(int var5 = 0; var5 < 3; ++var5) {
                  var2[var3][var4][var5] = BitMatrix.Is(var1, var3 - 1, var4 - 1, var5 - 1);
               }
            }
         }

         this.Map.put(var1, var2);
         return var2;
      }
   }
}
