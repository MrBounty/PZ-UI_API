package zombie.iso;

import java.util.ArrayList;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import zombie.debug.DebugLog;
import zombie.iso.SpriteDetails.IsoFlagType;

public final class IsoWaterFlow {
   private static final ArrayList points = new ArrayList();
   private static final ArrayList zones = new ArrayList();

   public static void addFlow(float var0, float var1, float var2, float var3) {
      int var4 = (360 - (int)var2 - 45) % 360;
      if (var4 < 0) {
         var4 += 360;
      }

      var2 = (float)Math.toRadians((double)var4);
      points.add(new Vector4f(var0, var1, var2, var3));
   }

   public static void addZone(float var0, float var1, float var2, float var3, float var4, float var5) {
      if (var0 > var2 || var1 > var3 || (double)var4 > 1.0D) {
         DebugLog.log("ERROR IsoWaterFlow: Invalid waterzone (" + var0 + ", " + var1 + ", " + var2 + ", " + var3 + ")");
      }

      zones.add(new Matrix3f(var0, var1, var2, var3, var4, var5, 0.0F, 0.0F, 0.0F));
   }

   public static int getShore(int var0, int var1) {
      for(int var2 = 0; var2 < zones.size(); ++var2) {
         Matrix3f var3 = (Matrix3f)zones.get(var2);
         if (var3.m00 <= (float)var0 && var3.m02 >= (float)var0 && var3.m01 <= (float)var1 && var3.m10 >= (float)var1) {
            return (int)var3.m11;
         }
      }

      return 1;
   }

   public static Vector2f getFlow(IsoGridSquare var0, int var1, int var2, Vector2f var3) {
      float var4 = 0.0F;
      float var5 = 0.0F;
      Vector4f var6 = null;
      float var7 = Float.MAX_VALUE;
      Vector4f var8 = null;
      float var9 = Float.MAX_VALUE;
      Vector4f var10 = null;
      float var11 = Float.MAX_VALUE;
      if (points.size() == 0) {
         return var3.set(0.0F, 0.0F);
      } else {
         int var12;
         Vector4f var13;
         double var14;
         for(var12 = 0; var12 < points.size(); ++var12) {
            var13 = (Vector4f)points.get(var12);
            var14 = Math.pow((double)(var13.x - (float)(var0.x + var1)), 2.0D) + Math.pow((double)(var13.y - (float)(var0.y + var2)), 2.0D);
            if (var14 < (double)var7) {
               var7 = (float)var14;
               var6 = var13;
            }
         }

         for(var12 = 0; var12 < points.size(); ++var12) {
            var13 = (Vector4f)points.get(var12);
            var14 = Math.pow((double)(var13.x - (float)(var0.x + var1)), 2.0D) + Math.pow((double)(var13.y - (float)(var0.y + var2)), 2.0D);
            if (var14 < (double)var9 && var13 != var6) {
               var9 = (float)var14;
               var8 = var13;
            }
         }

         var7 = Math.max((float)Math.sqrt((double)var7), 0.1F);
         var9 = Math.max((float)Math.sqrt((double)var9), 0.1F);
         float var17;
         if (var7 > var9 * 10.0F) {
            var4 = var6.z;
            var5 = var6.w;
         } else {
            for(var12 = 0; var12 < points.size(); ++var12) {
               var13 = (Vector4f)points.get(var12);
               var14 = Math.pow((double)(var13.x - (float)(var0.x + var1)), 2.0D) + Math.pow((double)(var13.y - (float)(var0.y + var2)), 2.0D);
               if (var14 < (double)var11 && var13 != var6 && var13 != var8) {
                  var11 = (float)var14;
                  var10 = var13;
               }
            }

            var11 = Math.max((float)Math.sqrt((double)var11), 0.1F);
            var17 = var8.z * (1.0F - var9 / (var9 + var11)) + var10.z * (1.0F - var11 / (var9 + var11));
            float var18 = var8.w * (1.0F - var9 / (var9 + var11)) + var10.w * (1.0F - var11 / (var9 + var11));
            float var20 = var9 * (1.0F - var9 / (var9 + var11)) + var11 * (1.0F - var11 / (var9 + var11));
            var4 = var6.z * (1.0F - var7 / (var7 + var20)) + var17 * (1.0F - var20 / (var7 + var20));
            var5 = var6.w * (1.0F - var7 / (var7 + var20)) + var18 * (1.0F - var20 / (var7 + var20));
         }

         var17 = 1.0F;
         IsoCell var19 = var0.getCell();

         for(int var21 = -5; var21 < 5; ++var21) {
            for(int var15 = -5; var15 < 5; ++var15) {
               IsoGridSquare var16 = var19.getGridSquare(var0.x + var1 + var21, var0.y + var2 + var15, 0);
               if (var16 == null || !var16.getProperties().Is(IsoFlagType.water)) {
                  var17 = (float)Math.min((double)var17, Math.max(0.0D, Math.sqrt((double)(var21 * var21 + var15 * var15))) / 4.0D);
               }
            }
         }

         var5 *= var17;
         return var3.set(var4, var5);
      }
   }

   public static void Reset() {
      points.clear();
      zones.clear();
   }
}
