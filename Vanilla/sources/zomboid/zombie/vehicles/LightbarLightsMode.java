package zombie.vehicles;

public final class LightbarLightsMode {
   private long startTime = 0L;
   private int light = 0;
   private final int modeMax = 3;
   private int mode = 0;

   public int get() {
      return this.mode;
   }

   public void set(int var1) {
      if (var1 > 3) {
         this.mode = 3;
      } else if (var1 < 0) {
         this.mode = 0;
      } else {
         this.mode = var1;
         if (this.mode != 0) {
            this.start();
         }

      }
   }

   public void start() {
      this.startTime = System.currentTimeMillis();
   }

   public void update() {
      long var1 = System.currentTimeMillis() - this.startTime;
      switch(this.mode) {
      case 1:
         var1 %= 1000L;
         if (var1 < 50L) {
            this.light = 0;
         } else if (var1 < 450L) {
            this.light = 1;
         } else if (var1 < 550L) {
            this.light = 0;
         } else if (var1 < 950L) {
            this.light = 2;
         } else {
            this.light = 0;
         }
         break;
      case 2:
         var1 %= 1000L;
         if (var1 < 50L) {
            this.light = 0;
         } else if (var1 < 250L) {
            this.light = 1;
         } else if (var1 < 300L) {
            this.light = 0;
         } else if (var1 < 500L) {
            this.light = 1;
         } else if (var1 < 550L) {
            this.light = 0;
         } else if (var1 < 750L) {
            this.light = 2;
         } else if (var1 < 800L) {
            this.light = 0;
         } else {
            this.light = 2;
         }
         break;
      case 3:
         var1 %= 300L;
         if (var1 < 25L) {
            this.light = 0;
         } else if (var1 < 125L) {
            this.light = 1;
         } else if (var1 < 175L) {
            this.light = 0;
         } else if (var1 < 275L) {
            this.light = 2;
         } else {
            this.light = 0;
         }
         break;
      default:
         this.light = 0;
      }

   }

   public int getLightTexIndex() {
      return this.light;
   }

   public boolean isEnable() {
      return this.mode != 0;
   }
}
