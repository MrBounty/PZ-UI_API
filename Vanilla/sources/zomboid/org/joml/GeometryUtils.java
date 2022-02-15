package org.joml;

public class GeometryUtils {
   public static void perpendicular(float var0, float var1, float var2, Vector3f var3, Vector3f var4) {
      float var5 = var2 * var2 + var1 * var1;
      float var6 = var2 * var2 + var0 * var0;
      float var7 = var1 * var1 + var0 * var0;
      float var8;
      if (var5 > var6 && var5 > var7) {
         var3.x = 0.0F;
         var3.y = var2;
         var3.z = -var1;
         var8 = var5;
      } else if (var6 > var7) {
         var3.x = -var2;
         var3.y = 0.0F;
         var3.z = var0;
         var8 = var6;
      } else {
         var3.x = var1;
         var3.y = -var0;
         var3.z = 0.0F;
         var8 = var7;
      }

      float var9 = Math.invsqrt(var8);
      var3.x *= var9;
      var3.y *= var9;
      var3.z *= var9;
      var4.x = var1 * var3.z - var2 * var3.y;
      var4.y = var2 * var3.x - var0 * var3.z;
      var4.z = var0 * var3.y - var1 * var3.x;
   }

   public static void perpendicular(Vector3fc var0, Vector3f var1, Vector3f var2) {
      perpendicular(var0.x(), var0.y(), var0.z(), var1, var2);
   }

   public static void normal(Vector3fc var0, Vector3fc var1, Vector3fc var2, Vector3f var3) {
      normal(var0.x(), var0.y(), var0.z(), var1.x(), var1.y(), var1.z(), var2.x(), var2.y(), var2.z(), var3);
   }

   public static void normal(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Vector3f var9) {
      var9.x = (var4 - var1) * (var8 - var2) - (var5 - var2) * (var7 - var1);
      var9.y = (var5 - var2) * (var6 - var0) - (var3 - var0) * (var8 - var2);
      var9.z = (var3 - var0) * (var7 - var1) - (var4 - var1) * (var6 - var0);
      var9.normalize();
   }

   public static void tangent(Vector3fc var0, Vector2fc var1, Vector3fc var2, Vector2fc var3, Vector3fc var4, Vector2fc var5, Vector3f var6) {
      float var7 = var3.y() - var1.y();
      float var8 = var5.y() - var1.y();
      float var9 = 1.0F / ((var3.x() - var1.x()) * var8 - (var5.x() - var1.x()) * var7);
      var6.x = var9 * (var8 * (var2.x() - var0.x()) - var7 * (var4.x() - var0.x()));
      var6.y = var9 * (var8 * (var2.y() - var0.y()) - var7 * (var4.y() - var0.y()));
      var6.z = var9 * (var8 * (var2.z() - var0.z()) - var7 * (var4.z() - var0.z()));
      var6.normalize();
   }

   public static void bitangent(Vector3fc var0, Vector2fc var1, Vector3fc var2, Vector2fc var3, Vector3fc var4, Vector2fc var5, Vector3f var6) {
      float var7 = var3.x() - var1.x();
      float var8 = var5.x() - var1.x();
      float var9 = 1.0F / (var7 * (var5.y() - var1.y()) - var8 * (var3.y() - var1.y()));
      var6.x = var9 * (-var8 * (var2.x() - var0.x()) + var7 * (var4.x() - var0.x()));
      var6.y = var9 * (-var8 * (var2.y() - var0.y()) + var7 * (var4.y() - var0.y()));
      var6.z = var9 * (-var8 * (var2.z() - var0.z()) + var7 * (var4.z() - var0.z()));
      var6.normalize();
   }

   public static void tangentBitangent(Vector3fc var0, Vector2fc var1, Vector3fc var2, Vector2fc var3, Vector3fc var4, Vector2fc var5, Vector3f var6, Vector3f var7) {
      float var8 = var3.y() - var1.y();
      float var9 = var5.y() - var1.y();
      float var10 = var3.x() - var1.x();
      float var11 = var5.x() - var1.x();
      float var12 = 1.0F / (var10 * var9 - var11 * var8);
      var6.x = var12 * (var9 * (var2.x() - var0.x()) - var8 * (var4.x() - var0.x()));
      var6.y = var12 * (var9 * (var2.y() - var0.y()) - var8 * (var4.y() - var0.y()));
      var6.z = var12 * (var9 * (var2.z() - var0.z()) - var8 * (var4.z() - var0.z()));
      var6.normalize();
      var7.x = var12 * (-var11 * (var2.x() - var0.x()) + var10 * (var4.x() - var0.x()));
      var7.y = var12 * (-var11 * (var2.y() - var0.y()) + var10 * (var4.y() - var0.y()));
      var7.z = var12 * (-var11 * (var2.z() - var0.z()) + var10 * (var4.z() - var0.z()));
      var7.normalize();
   }
}
