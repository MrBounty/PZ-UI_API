package zombie.core.utils;

public final class UpdateLimit {
   private long delay;
   private long last;
   private long lastPeriod;

   public UpdateLimit(long var1) {
      this.delay = var1;
      this.last = System.currentTimeMillis();
      this.lastPeriod = this.last;
   }

   public UpdateLimit(long var1, long var3) {
      this.delay = var1;
      this.last = System.currentTimeMillis() - var3;
      this.lastPeriod = this.last;
   }

   public void BlockCheck() {
      this.last = System.currentTimeMillis() + this.delay;
   }

   public void Reset(long var1) {
      this.delay = var1;
      this.Reset();
   }

   public void Reset() {
      this.last = System.currentTimeMillis();
      this.lastPeriod = System.currentTimeMillis();
   }

   public void setUpdatePeriod(long var1) {
      this.delay = var1;
   }

   public boolean Check() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.last > this.delay) {
         if (var1 - this.last > 3L * this.delay) {
            this.last = var1;
         } else {
            this.last += this.delay;
         }

         return true;
      } else {
         return false;
      }
   }

   public long getLast() {
      return this.last;
   }

   public void updateTimePeriod() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.last > this.delay) {
         if (var1 - this.last > 3L * this.delay) {
            this.last = var1;
         } else {
            this.last += this.delay;
         }
      }

      this.lastPeriod = var1;
   }

   public double getTimePeriod() {
      return Math.min(((double)System.currentTimeMillis() - (double)this.lastPeriod) / (double)this.delay, 1.0D);
   }
}
