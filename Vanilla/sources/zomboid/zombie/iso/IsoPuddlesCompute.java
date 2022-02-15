package zombie.iso;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.popman.ObjectPool;

public final class IsoPuddlesCompute {
   private static final float Pi = 3.1415F;
   private static float puddlesDirNE;
   private static float puddlesDirNW;
   private static float puddlesDirAll;
   private static float puddlesDirNone;
   private static float puddlesSize;
   private static boolean hd_quality = true;
   private static final Vector2f add = new Vector2f(1.0F, 0.0F);
   private static final Vector3f add_xyy = new Vector3f(1.0F, 0.0F, 0.0F);
   private static final Vector3f add_xxy = new Vector3f(1.0F, 1.0F, 0.0F);
   private static final Vector3f add_xxx = new Vector3f(1.0F, 1.0F, 1.0F);
   private static final Vector3f add_xyx = new Vector3f(1.0F, 0.0F, 1.0F);
   private static final Vector3f add_yxy = new Vector3f(0.0F, 1.0F, 0.0F);
   private static final Vector3f add_yyx = new Vector3f(0.0F, 0.0F, 1.0F);
   private static final Vector3f add_yxx = new Vector3f(0.0F, 1.0F, 1.0F);
   private static final Vector3f HashVector31 = new Vector3f(17.1F, 31.7F, 32.6F);
   private static final Vector3f HashVector32 = new Vector3f(29.5F, 13.3F, 42.6F);
   private static final ObjectPool pool_vector3f = new ObjectPool(Vector3f::new);
   private static final ArrayList allocated_vector3f = new ArrayList();
   private static final Vector2f temp_vector2f = new Vector2f();

   private static Vector3f allocVector3f(float var0, float var1, float var2) {
      Vector3f var3 = ((Vector3f)pool_vector3f.alloc()).set(var0, var1, var2);
      allocated_vector3f.add(var3);
      return var3;
   }

   private static Vector3f allocVector3f(Vector3f var0) {
      return allocVector3f(var0.x, var0.y, var0.z);
   }

   private static Vector3f floor(Vector3f var0) {
      return allocVector3f((float)Math.floor((double)var0.x), (float)Math.floor((double)var0.y), (float)Math.floor((double)var0.z));
   }

   private static Vector3f fract(Vector3f var0) {
      return allocVector3f(fract(var0.x), fract(var0.y), fract(var0.z));
   }

   private static float fract(float var0) {
      return (float)((double)var0 - Math.floor((double)var0));
   }

   private static float mix(float var0, float var1, float var2) {
      return var0 * (1.0F - var2) + var1 * var2;
   }

   private static float FuncHash(Vector3f var0) {
      Vector3f var1 = allocVector3f(var0.dot(HashVector31), var0.dot(HashVector32), 0.0F);
      return fract((float)(Math.sin((double)var1.x * 2.1D + 1.1D) + Math.sin((double)var1.y * 2.5D + 1.5D)));
   }

   private static float FuncNoise(Vector3f var0) {
      Vector3f var1 = floor(var0);
      Vector3f var2 = fract(var0);
      Vector3f var3 = allocVector3f(var2.x * var2.x * (4.5F - 3.5F * var2.x), var2.y * var2.y * (4.5F - 3.5F * var2.y), var2.z * var2.z * (4.5F - 3.5F * var2.z));
      float var4 = mix(FuncHash(var1), FuncHash(allocVector3f(var1).add(add_xyy)), var3.x);
      float var5 = mix(FuncHash(allocVector3f(var1).add(add_yxy)), FuncHash(allocVector3f(var1).add(add_xxy)), var3.x);
      float var6 = mix(FuncHash(allocVector3f(var1).add(add_yyx)), FuncHash(allocVector3f(var1).add(add_xyx)), var3.x);
      float var7 = mix(FuncHash(allocVector3f(var1).add(add_yxx)), FuncHash(allocVector3f(var1).add(add_xxx)), var3.x);
      float var8 = mix(var4, var5, var3.y);
      float var9 = mix(var6, var7, var3.y);
      return mix(var8, var9, var3.z);
   }

   private static float PerlinNoise(Vector3f var0) {
      if (hd_quality) {
         var0.mul(0.5F);
         float var1 = 0.5F * FuncNoise(var0);
         var0.mul(3.0F);
         var1 = (float)((double)var1 + 0.25D * (double)FuncNoise(var0));
         var0.mul(3.0F);
         var1 = (float)((double)var1 + 0.125D * (double)FuncNoise(var0));
         var1 = (float)((double)var1 * Math.min(1.0D, 2.0D * (double)FuncNoise(allocVector3f(var0).mul(0.02F)) * Math.min(1.0D, 1.0D * (double)FuncNoise(allocVector3f(var0).mul(0.1F)))));
         return var1;
      } else {
         return FuncNoise(var0) * 0.5F;
      }
   }

   private static float getPuddles(Vector2f var0) {
      float var1 = puddlesDirNE;
      float var2 = puddlesDirNW;
      float var3 = puddlesDirAll;
      var0.mul(10.0F);
      float var4 = 1.02F * puddlesSize;
      var4 = (float)((double)var4 + (double)var1 * Math.sin(((double)var0.x * 1.0D + (double)var0.y * 2.0D) * 3.1414999961853027D * 1.0D) * Math.cos(((double)var0.x * 1.0D + (double)var0.y * 2.0D) * 3.1414999961853027D * 1.0D) * 2.0D);
      var4 = (float)((double)var4 + (double)var2 * Math.sin(((double)var0.x * 1.0D - (double)var0.y * 2.0D) * 3.1414999961853027D * 1.0D) * Math.cos(((double)var0.x * 1.0D - (double)var0.y * 2.0D) * 3.1414999961853027D * 1.0D) * 2.0D);
      var4 = (float)((double)var4 + (double)var3 * 0.3D);
      float var5 = PerlinNoise(allocVector3f(var0.x * 1.0F, 0.0F, var0.y * 2.0F));
      float var6 = Math.min(0.7F, var4 * var5);
      var5 = Math.min(0.7F, PerlinNoise(allocVector3f(var0.x * 0.7F, 1.0F, var0.y * 0.7F)));
      return var6 + var5;
   }

   public static float computePuddle(IsoGridSquare var0) {
      pool_vector3f.release((List)allocated_vector3f);
      allocated_vector3f.clear();
      hd_quality = PerformanceSettings.PuddlesQuality == 0;
      if (!Core.getInstance().getUseShaders()) {
         return -0.1F;
      } else if (Core.getInstance().getPerfPuddlesOnLoad() != 3 && Core.getInstance().getPerfPuddles() != 3) {
         if (Core.getInstance().getPerfPuddles() > 0 && var0.z > 0) {
            return -0.1F;
         } else {
            IsoPuddles var1 = IsoPuddles.getInstance();
            puddlesSize = var1.getPuddlesSize();
            if (puddlesSize <= 0.0F) {
               return -0.1F;
            } else {
               Vector4f var2 = var1.getShaderOffsetMain();
               var2.x -= 90000.0F;
               var2.y -= 640000.0F;
               int var3 = (int)IsoCamera.frameState.OffX;
               int var4 = (int)IsoCamera.frameState.OffY;
               float var5 = IsoUtils.XToScreen((float)var0.x + 0.5F - (float)var0.z * 3.0F, (float)var0.y + 0.5F - (float)var0.z * 3.0F, 0.0F, 0) - (float)var3;
               float var6 = IsoUtils.YToScreen((float)var0.x + 0.5F - (float)var0.z * 3.0F, (float)var0.y + 0.5F - (float)var0.z * 3.0F, 0.0F, 0) - (float)var4;
               var5 /= (float)IsoCamera.frameState.OffscreenWidth;
               var6 /= (float)IsoCamera.frameState.OffscreenHeight;
               if (Core.getInstance().getPerfPuddles() <= 1) {
                  var0.getPuddles().recalcIfNeeded();
                  puddlesDirNE = (var0.getPuddles().pdne[0] + var0.getPuddles().pdne[2]) * 0.5F;
                  puddlesDirNW = (var0.getPuddles().pdnw[0] + var0.getPuddles().pdnw[2]) * 0.5F;
                  puddlesDirAll = (var0.getPuddles().pda[0] + var0.getPuddles().pda[2]) * 0.5F;
                  puddlesDirNone = (var0.getPuddles().pnon[0] + var0.getPuddles().pnon[2]) * 0.5F;
               } else {
                  puddlesDirNE = 0.0F;
                  puddlesDirNW = 0.0F;
                  puddlesDirAll = 1.0F;
                  puddlesDirNone = 0.0F;
               }

               Vector2f var9 = temp_vector2f.set((var5 * var2.z + var2.x) * 8.0E-4F + (float)var0.z * 7.0F, (var6 * var2.w + var2.y) * 8.0E-4F + (float)var0.z * 7.0F);
               float var10 = (float)Math.pow((double)getPuddles(var9), 2.0D);
               float var11 = (float)Math.min(Math.pow((double)var10, 0.3D), 1.0D) + var10;
               return var11 * puddlesSize - 0.34F;
            }
         }
      } else {
         return -0.1F;
      }
   }
}
