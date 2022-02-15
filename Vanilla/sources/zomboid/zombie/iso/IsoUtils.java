package zombie.iso;

import org.joml.Vector2f;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;

public final class IsoUtils {
   public static float clamp(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   public static float smoothstep(float var0, float var1, float var2) {
      float var3 = clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (3.0F - 2.0F * var3);
   }

   public static float DistanceTo(float var0, float var1, float var2, float var3) {
      return (float)Math.sqrt(Math.pow((double)(var2 - var0), 2.0D) + Math.pow((double)(var3 - var1), 2.0D));
   }

   public static float DistanceTo2D(float var0, float var1, float var2, float var3) {
      return (float)Math.sqrt(Math.pow((double)(var2 - var0), 2.0D) + Math.pow((double)(var3 - var1), 2.0D));
   }

   public static float DistanceTo(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (float)Math.sqrt(Math.pow((double)(var3 - var0), 2.0D) + Math.pow((double)(var4 - var1), 2.0D) + Math.pow((double)(var5 - var2), 2.0D));
   }

   public static float DistanceToSquared(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (float)(Math.pow((double)(var3 - var0), 2.0D) + Math.pow((double)(var4 - var1), 2.0D) + Math.pow((double)(var5 - var2), 2.0D));
   }

   public static float DistanceToSquared(float var0, float var1, float var2, float var3) {
      return (float)(Math.pow((double)(var2 - var0), 2.0D) + Math.pow((double)(var3 - var1), 2.0D));
   }

   public static float DistanceManhatten(float var0, float var1, float var2, float var3) {
      return Math.abs(var2 - var0) + Math.abs(var3 - var1);
   }

   public static float DistanceManhatten(float var0, float var1, float var2, float var3, float var4, float var5) {
      return Math.abs(var2 - var0) + Math.abs(var3 - var1) + Math.abs(var5 - var4) * 2.0F;
   }

   public static float DistanceManhattenSquare(float var0, float var1, float var2, float var3) {
      return Math.max(Math.abs(var2 - var0), Math.abs(var3 - var1));
   }

   public static float XToIso(float var0, float var1, float var2) {
      float var3 = var0 + IsoCamera.getOffX();
      float var4 = var1 + IsoCamera.getOffY();
      float var5 = (var3 + 2.0F * var4) / (64.0F * (float)Core.TileScale);
      float var6 = (var3 - 2.0F * var4) / (-64.0F * (float)Core.TileScale);
      var5 += 3.0F * var2;
      float var10000 = var6 + 3.0F * var2;
      return var5;
   }

   public static float XToIsoTrue(float var0, float var1, int var2) {
      float var3 = var0 + (float)((int)IsoCamera.cameras[IsoPlayer.getPlayerIndex()].OffX);
      float var4 = var1 + (float)((int)IsoCamera.cameras[IsoPlayer.getPlayerIndex()].OffY);
      float var5 = (var3 + 2.0F * var4) / (64.0F * (float)Core.TileScale);
      float var6 = (var3 - 2.0F * var4) / (-64.0F * (float)Core.TileScale);
      var5 += (float)(3 * var2);
      float var10000 = var6 + (float)(3 * var2);
      return var5;
   }

   public static float XToScreen(float var0, float var1, float var2, int var3) {
      float var4 = 0.0F;
      var4 += var0 * (float)(32 * Core.TileScale);
      var4 -= var1 * (float)(32 * Core.TileScale);
      return var4;
   }

   public static float XToScreenInt(int var0, int var1, int var2, int var3) {
      return XToScreen((float)var0, (float)var1, (float)var2, var3);
   }

   public static float YToScreenExact(float var0, float var1, float var2, int var3) {
      float var4 = YToScreen(var0, var1, var2, var3);
      var4 -= IsoCamera.getOffY();
      return var4;
   }

   public static float XToScreenExact(float var0, float var1, float var2, int var3) {
      float var4 = XToScreen(var0, var1, var2, var3);
      var4 -= IsoCamera.getOffX();
      return var4;
   }

   public static float YToIso(float var0, float var1, float var2) {
      float var3 = var0 + IsoCamera.getOffX();
      float var4 = var1 + IsoCamera.getOffY();
      float var5 = (var3 + 2.0F * var4) / (64.0F * (float)Core.TileScale);
      float var6 = (var3 - 2.0F * var4) / (-64.0F * (float)Core.TileScale);
      float var10000 = var5 + 3.0F * var2;
      var6 += 3.0F * var2;
      return var6;
   }

   public static float YToScreen(float var0, float var1, float var2, int var3) {
      float var4 = 0.0F;
      var4 += var1 * (float)(16 * Core.TileScale);
      var4 += var0 * (float)(16 * Core.TileScale);
      var4 += ((float)var3 - var2) * (float)(96 * Core.TileScale);
      return var4;
   }

   public static float YToScreenInt(int var0, int var1, int var2, int var3) {
      return YToScreen((float)var0, (float)var1, (float)var2, var3);
   }

   public static boolean isSimilarDirection(IsoGameCharacter var0, float var1, float var2, float var3, float var4, float var5) {
      Vector2f var6 = new Vector2f(var1 - var0.x, var2 - var0.y);
      var6.normalize();
      Vector2f var7 = new Vector2f(var0.x - var3, var0.y - var4);
      var7.normalize();
      var6.add(var7);
      return var6.length() < var5;
   }
}
