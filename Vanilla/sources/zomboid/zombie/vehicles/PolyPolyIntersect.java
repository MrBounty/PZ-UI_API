package zombie.vehicles;

import org.joml.Vector2f;

public final class PolyPolyIntersect {
   private static Vector2f tempVector2f_1 = new Vector2f();
   private static Vector2f tempVector2f_2 = new Vector2f();
   private static Vector2f tempVector2f_3 = new Vector2f();

   public static boolean intersects(PolygonalMap2.VehiclePoly var0, PolygonalMap2.VehiclePoly var1) {
      for(int var2 = 0; var2 < 2; ++var2) {
         PolygonalMap2.VehiclePoly var3 = var2 == 0 ? var0 : var1;

         for(int var4 = 0; var4 < 4; ++var4) {
            int var5 = (var4 + 1) % 4;
            Vector2f var6 = getPoint(var3, var4, tempVector2f_1);
            Vector2f var7 = getPoint(var3, var5, tempVector2f_2);
            Vector2f var8 = tempVector2f_3.set(var7.y - var6.y, var6.x - var7.x);
            double var9 = Double.MAX_VALUE;
            double var11 = Double.NEGATIVE_INFINITY;

            double var15;
            for(int var13 = 0; var13 < 4; ++var13) {
               Vector2f var14 = getPoint(var0, var13, tempVector2f_1);
               var15 = (double)(var8.x * var14.x + var8.y * var14.y);
               if (var15 < var9) {
                  var9 = var15;
               }

               if (var15 > var11) {
                  var11 = var15;
               }
            }

            double var21 = Double.MAX_VALUE;
            var15 = Double.NEGATIVE_INFINITY;

            for(int var17 = 0; var17 < 4; ++var17) {
               Vector2f var18 = getPoint(var1, var17, tempVector2f_1);
               double var19 = (double)(var8.x * var18.x + var8.y * var18.y);
               if (var19 < var21) {
                  var21 = var19;
               }

               if (var19 > var15) {
                  var15 = var19;
               }
            }

            if (var11 < var21 || var15 < var9) {
               return false;
            }
         }
      }

      return true;
   }

   private static Vector2f getPoint(PolygonalMap2.VehiclePoly var0, int var1, Vector2f var2) {
      if (var1 == 0) {
         return var2.set(var0.x1, var0.y1);
      } else if (var1 == 1) {
         return var2.set(var0.x2, var0.y2);
      } else if (var1 == 2) {
         return var2.set(var0.x3, var0.y3);
      } else {
         return var1 == 3 ? var2.set(var0.x4, var0.y4) : null;
      }
   }
}
