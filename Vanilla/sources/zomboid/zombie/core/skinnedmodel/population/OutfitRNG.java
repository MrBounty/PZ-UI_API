package zombie.core.skinnedmodel.population;

import java.util.List;
import zombie.core.Color;
import zombie.core.ImmutableColor;
import zombie.util.LocationRNG;

public final class OutfitRNG {
   private static final ThreadLocal RNG = ThreadLocal.withInitial(LocationRNG::new);

   public static void setSeed(long var0) {
      ((LocationRNG)RNG.get()).setSeed(var0);
   }

   public static long getSeed() {
      return ((LocationRNG)RNG.get()).getSeed();
   }

   public static int Next(int var0) {
      return ((LocationRNG)RNG.get()).nextInt(var0);
   }

   public static int Next(int var0, int var1) {
      if (var1 == var0) {
         return var0;
      } else {
         int var2;
         if (var0 > var1) {
            var2 = var0;
            var0 = var1;
            var1 = var2;
         }

         var2 = ((LocationRNG)RNG.get()).nextInt(var1 - var0);
         return var2 + var0;
      }
   }

   public static float Next(float var0, float var1) {
      if (var1 == var0) {
         return var0;
      } else {
         if (var0 > var1) {
            float var2 = var0;
            var0 = var1;
            var1 = var2;
         }

         return var0 + ((LocationRNG)RNG.get()).nextFloat() * (var1 - var0);
      }
   }

   public static boolean NextBool(int var0) {
      return Next(var0) == 0;
   }

   public static Object pickRandom(List var0) {
      if (var0.isEmpty()) {
         return null;
      } else if (var0.size() == 1) {
         return var0.get(0);
      } else {
         int var1 = Next(var0.size());
         return var0.get(var1);
      }
   }

   public static ImmutableColor randomImmutableColor() {
      float var0 = Next(0.0F, 1.0F);
      float var1 = Next(0.0F, 0.6F);
      float var2 = Next(0.0F, 0.9F);
      Color var3 = Color.HSBtoRGB(var0, var1, var2);
      return new ImmutableColor(var3);
   }
}
