package zombie.core.utils;

import zombie.GameTime;
import zombie.core.Rand;

public final class OnceEvery {
   private long initialDelayMillis;
   private long triggerIntervalMillis;
   private static float milliFraction = 0.0F;
   private static long currentMillis = 0L;
   private static long prevMillis = 0L;

   public OnceEvery(float var1) {
      this(var1, false);
   }

   public OnceEvery(float var1, boolean var2) {
      this.initialDelayMillis = 0L;
      this.triggerIntervalMillis = (long)(var1 * 1000.0F);
      this.initialDelayMillis = 0L;
      if (var2) {
         this.initialDelayMillis = Rand.Next(this.triggerIntervalMillis);
      }

   }

   public static long getElapsedMillis() {
      return currentMillis;
   }

   public boolean Check() {
      if (currentMillis < this.initialDelayMillis) {
         return false;
      } else if (this.triggerIntervalMillis == 0L) {
         return true;
      } else {
         long var1 = (prevMillis - this.initialDelayMillis) % this.triggerIntervalMillis;
         long var3 = (currentMillis - this.initialDelayMillis) % this.triggerIntervalMillis;
         if (var1 > var3) {
            return true;
         } else {
            long var5 = currentMillis - prevMillis;
            return this.triggerIntervalMillis < var5;
         }
      }
   }

   public static void update() {
      long var0 = currentMillis;
      float var2 = milliFraction;
      float var3 = GameTime.instance.getTimeDelta();
      float var4 = var3 * 1000.0F + var2;
      long var5 = (long)var4;
      float var7 = var4 - (float)var5;
      long var8 = var0 + var5;
      prevMillis = var0;
      currentMillis = var8;
      milliFraction = var7;
   }
}
