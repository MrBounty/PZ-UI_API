package zombie.core;

import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.SecureRandomSeedGenerator;
import org.uncommons.maths.random.SeedException;
import org.uncommons.maths.random.SeedGenerator;
import zombie.network.GameServer;

public final class Rand {
   public static CellularAutomatonRNG rand;
   public static CellularAutomatonRNG randlua;
   public static int id = 0;

   public static void init() {
      try {
         rand = new CellularAutomatonRNG(new Rand.PZSeedGenerator());
         randlua = new CellularAutomatonRNG(new Rand.PZSeedGenerator());
      } catch (SeedException var1) {
         var1.printStackTrace();
      }

   }

   private static int Next(int var0, CellularAutomatonRNG var1) {
      if (var0 <= 0) {
         return 0;
      } else {
         ++id;
         if (id >= 10000) {
            id = 0;
         }

         return var1.nextInt(var0);
      }
   }

   public static int Next(int var0) {
      return Next(var0, rand);
   }

   public static long Next(long var0, CellularAutomatonRNG var2) {
      return (long)Next((int)var0, var2);
   }

   public static long Next(long var0) {
      return Next(var0, rand);
   }

   public static int Next(int var0, int var1, CellularAutomatonRNG var2) {
      if (var1 == var0) {
         return var0;
      } else {
         int var3;
         if (var0 > var1) {
            var3 = var0;
            var0 = var1;
            var1 = var3;
         }

         ++id;
         if (id >= 10000) {
            id = 0;
         }

         var3 = var2.nextInt(var1 - var0);
         return var3 + var0;
      }
   }

   public static int Next(int var0, int var1) {
      return Next(var0, var1, rand);
   }

   public static long Next(long var0, long var2, CellularAutomatonRNG var4) {
      if (var2 == var0) {
         return var0;
      } else {
         if (var0 > var2) {
            long var5 = var0;
            var0 = var2;
            var2 = var5;
         }

         ++id;
         if (id >= 10000) {
            id = 0;
         }

         int var7 = var4.nextInt((int)(var2 - var0));
         return (long)var7 + var0;
      }
   }

   public static long Next(long var0, long var2) {
      return Next(var0, var2, rand);
   }

   public static float Next(float var0, float var1, CellularAutomatonRNG var2) {
      if (var1 == var0) {
         return var0;
      } else {
         if (var0 > var1) {
            float var3 = var0;
            var0 = var1;
            var1 = var3;
         }

         ++id;
         if (id >= 10000) {
            id = 0;
         }

         return var0 + var2.nextFloat() * (var1 - var0);
      }
   }

   public static float Next(float var0, float var1) {
      return Next(var0, var1, rand);
   }

   public static boolean NextBool(int var0) {
      return Next(var0) == 0;
   }

   public static int AdjustForFramerate(int var0) {
      if (GameServer.bServer) {
         var0 = (int)((float)var0 * 0.33333334F);
      } else {
         var0 = (int)((float)var0 * ((float)PerformanceSettings.getLockFPS() / 30.0F));
      }

      return var0;
   }

   public static final class PZSeedGenerator implements SeedGenerator {
      private static final SeedGenerator[] GENERATORS = new SeedGenerator[]{new SecureRandomSeedGenerator()};

      private PZSeedGenerator() {
      }

      public byte[] generateSeed(int var1) {
         SeedGenerator[] var2 = GENERATORS;
         int var3 = var2.length;
         int var4 = 0;

         while(var4 < var3) {
            SeedGenerator var5 = var2[var4];

            try {
               byte[] var6 = var5.generateSeed(var1);
               return var6;
            } catch (SeedException var7) {
               ++var4;
            }
         }

         throw new IllegalStateException("All available seed generation strategies failed.");
      }
   }
}
