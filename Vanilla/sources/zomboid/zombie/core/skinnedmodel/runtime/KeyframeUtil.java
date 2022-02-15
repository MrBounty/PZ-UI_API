package zombie.core.skinnedmodel.runtime;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.skinnedmodel.animation.Keyframe;

public final class KeyframeUtil {
   static final Quaternion end = new Quaternion();

   public static Vector3f GetKeyFramePosition(Keyframe[] var0, float var1, double var2) {
      Vector3f var4 = new Vector3f();
      if (var0.length == 0) {
         return var4;
      } else {
         int var5;
         for(var5 = 0; var5 < var0.length - 1 && !(var1 < var0[var5 + 1].Time); ++var5) {
         }

         int var6 = (var5 + 1) % var0.length;
         Keyframe var7 = var0[var5];
         Keyframe var8 = var0[var6];
         float var9 = var7.Time;
         float var10 = var8.Time;
         float var11 = var10 - var9;
         if (var11 < 0.0F) {
            var11 = (float)((double)var11 + var2);
         }

         if (var11 > 0.0F) {
            float var12 = var10 - var9;
            float var13 = var1 - var9;
            var13 /= var12;
            float var14 = var7.Position.x;
            float var15 = var8.Position.x;
            float var16 = var14 + var13 * (var15 - var14);
            float var17 = var7.Position.y;
            float var18 = var8.Position.y;
            float var19 = var17 + var13 * (var18 - var17);
            float var20 = var7.Position.z;
            float var21 = var8.Position.z;
            float var22 = var20 + var13 * (var21 - var20);
            var4.set(var16, var19, var22);
         } else {
            var4.set(var7.Position);
         }

         return var4;
      }
   }

   public static Quaternion GetKeyFrameRotation(Keyframe[] var0, float var1, double var2) {
      Quaternion var4 = new Quaternion();
      if (var0.length == 0) {
         return var4;
      } else {
         int var5;
         for(var5 = 0; var5 < var0.length - 1 && !(var1 < var0[var5 + 1].Time); ++var5) {
         }

         int var6 = (var5 + 1) % var0.length;
         Keyframe var7 = var0[var5];
         Keyframe var8 = var0[var6];
         float var9 = var7.Time;
         float var10 = var8.Time;
         float var11 = var10 - var9;
         if (var11 < 0.0F) {
            var11 = (float)((double)var11 + var2);
         }

         if (var11 > 0.0F) {
            float var12 = (var1 - var9) / var11;
            Quaternion var13 = var7.Rotation;
            Quaternion var14 = var8.Rotation;
            double var15 = (double)(var13.getX() * var14.getX() + var13.getY() * var14.getY() + var13.getZ() * var14.getZ() + var13.getW() * var14.getW());
            end.set(var14);
            if (var15 < 0.0D) {
               var15 *= -1.0D;
               end.setX(-end.getX());
               end.setY(-end.getY());
               end.setZ(-end.getZ());
               end.setW(-end.getW());
            }

            double var17;
            double var19;
            if (1.0D - var15 > 1.0E-4D) {
               double var21 = Math.acos(var15);
               double var23 = Math.sin(var21);
               var17 = Math.sin((1.0D - (double)var12) * var21) / var23;
               var19 = Math.sin((double)var12 * var21) / var23;
            } else {
               var17 = 1.0D - (double)var12;
               var19 = (double)var12;
            }

            var4.set((float)(var17 * (double)var13.getX() + var19 * (double)end.getX()), (float)(var17 * (double)var13.getY() + var19 * (double)end.getY()), (float)(var17 * (double)var13.getZ() + var19 * (double)end.getZ()), (float)(var17 * (double)var13.getW() + var19 * (double)end.getW()));
         } else {
            var4.set(var7.Rotation);
         }

         return var4;
      }
   }
}
