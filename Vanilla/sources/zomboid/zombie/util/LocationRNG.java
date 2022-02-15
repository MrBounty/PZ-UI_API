package zombie.util;

public final class LocationRNG {
   public static final LocationRNG instance = new LocationRNG();
   private static final float INT_TO_FLOAT = Float.intBitsToFloat(864026624);
   private long _s0;
   private long _s1;
   private long state;

   public void setSeed(long var1) {
      this.state = var1;
      this._s0 = this.nextSplitMix64();
      this._s1 = this.nextSplitMix64();
   }

   public long getSeed() {
      return this.state;
   }

   private long nextSplitMix64() {
      long var1 = this.state += -7046029254386353131L;
      var1 = (var1 ^ var1 >>> 30) * -4658895280553007687L;
      var1 = (var1 ^ var1 >>> 27) * -7723592293110705685L;
      return var1 ^ var1 >>> 31;
   }

   public float nextFloat() {
      return (float)(this.nextInt() >>> 8) * INT_TO_FLOAT;
   }

   private int nextInt() {
      long var1 = this._s0;
      long var3 = this._s1;
      long var5 = var1 + var3;
      var3 ^= var1;
      this._s0 = Long.rotateLeft(var1, 55) ^ var3 ^ var3 << 14;
      this._s1 = Long.rotateLeft(var3, 36);
      return (int)(var5 & -1L);
   }

   public int nextInt(int var1) {
      long var2 = (long)(this.nextInt() >>> 1);
      var2 = var2 * (long)var1 >> 31;
      return (int)var2;
   }

   public int nextInt(int var1, int var2, int var3, int var4) {
      this.setSeed((long)var4 << 16 | (long)var3 << 32 | (long)var2);
      return this.nextInt(var1);
   }
}
