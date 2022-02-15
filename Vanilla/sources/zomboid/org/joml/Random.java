package org.joml;

public class Random {
   private final Random.Xorshiro128 rnd;
   private static long seedHalf = 8020463840L;

   public static long newSeed() {
      Class var4 = Random.class;
      synchronized(Random.class) {
         long var0 = seedHalf;
         long var2 = var0 * 3512401965023503517L;
         seedHalf = var2;
         return var2;
      }
   }

   public Random() {
      this(newSeed() ^ System.nanoTime());
   }

   public Random(long var1) {
      this.rnd = new Random.Xorshiro128(var1);
   }

   public float nextFloat() {
      return this.rnd.nextFloat();
   }

   public int nextInt(int var1) {
      return this.rnd.nextInt(var1);
   }

   private static final class Xorshiro128 {
      private static final float INT_TO_FLOAT = Float.intBitsToFloat(864026624);
      private long _s0;
      private long _s1;
      private long state;

      Xorshiro128(long var1) {
         this.state = var1;
         this._s0 = this.nextSplitMix64();
         this._s1 = this.nextSplitMix64();
      }

      private long nextSplitMix64() {
         long var1 = this.state += -7046029254386353131L;
         var1 = (var1 ^ var1 >>> 30) * -4658895280553007687L;
         var1 = (var1 ^ var1 >>> 27) * -7723592293110705685L;
         return var1 ^ var1 >>> 31;
      }

      final float nextFloat() {
         return (float)(this.nextInt() >>> 8) * INT_TO_FLOAT;
      }

      private int nextInt() {
         long var1 = this._s0;
         long var3 = this._s1;
         long var5 = var1 + var3;
         var3 ^= var1;
         this.rotateLeft(var1, var3);
         return (int)(var5 & -1L);
      }

      private static long rotl_JDK4(long var0, int var2) {
         return var0 << var2 | var0 >>> 64 - var2;
      }

      private static long rotl_JDK5(long var0, int var2) {
         return Long.rotateLeft(var0, var2);
      }

      private static long rotl(long var0, int var2) {
         return Runtime.HAS_Long_rotateLeft ? rotl_JDK5(var0, var2) : rotl_JDK4(var0, var2);
      }

      private void rotateLeft(long var1, long var3) {
         this._s0 = rotl(var1, 55) ^ var3 ^ var3 << 14;
         this._s1 = rotl(var3, 36);
      }

      final int nextInt(int var1) {
         long var2 = (long)(this.nextInt() >>> 1);
         var2 = var2 * (long)var1 >> 31;
         return (int)var2;
      }
   }
}
