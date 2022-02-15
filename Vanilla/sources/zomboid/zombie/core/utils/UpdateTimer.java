package zombie.core.utils;

public class UpdateTimer {
   private long time = 0L;

   public UpdateTimer() {
      this.time = System.currentTimeMillis() + 3800L;
   }

   public void reset(long var1) {
      this.time = System.currentTimeMillis() + var1;
   }

   public boolean check() {
      return this.time != 0L && System.currentTimeMillis() + 200L >= this.time;
   }

   public long getTime() {
      return this.time;
   }
}
