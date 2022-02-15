package zombie;

import zombie.core.PerformanceSettings;

public final class FPSTracking {
   private final double[] lastFPS = new double[20];
   private int lastFPSCount = 0;
   private long timeAtLastUpdate;
   private final long[] last10 = new long[10];
   private int last10index = 0;

   public void init() {
      for(int var1 = 0; var1 < 20; ++var1) {
         this.lastFPS[var1] = (double)PerformanceSettings.getLockFPS();
      }

      this.timeAtLastUpdate = System.nanoTime();
   }

   public long frameStep() {
      long var1 = System.nanoTime();
      long var3 = var1 - this.timeAtLastUpdate;
      if (var3 > 0L) {
         float var5 = 0.0F;
         double var6 = (double)var3 / 1.0E9D;
         double var8 = 1.0D / var6;
         this.lastFPS[this.lastFPSCount] = var8;
         ++this.lastFPSCount;
         if (this.lastFPSCount >= 5) {
            this.lastFPSCount = 0;
         }

         for(int var10 = 0; var10 < 5; ++var10) {
            var5 = (float)((double)var5 + this.lastFPS[var10]);
         }

         var5 /= 5.0F;
         GameWindow.averageFPS = var5;
         GameTime.instance.FPSMultiplier = (float)(60.0D / var8);
         if (GameTime.instance.FPSMultiplier > 5.0F) {
            GameTime.instance.FPSMultiplier = 5.0F;
         }
      }

      this.timeAtLastUpdate = var1;
      this.updateFPS(var3);
      return var3;
   }

   public void updateFPS(long var1) {
      this.last10[this.last10index++] = var1;
      if (this.last10index >= this.last10.length) {
         this.last10index = 0;
      }

      float var3 = 11110.0F;
      float var4 = -11110.0F;
      long[] var5 = this.last10;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         long var8 = var5[var7];
         if (var8 != 0L) {
            if ((float)var8 < var3) {
               var3 = (float)var8;
            }

            if ((float)var8 > var4) {
               var4 = (float)var8;
            }
         }
      }

   }
}
