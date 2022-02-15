package com.sixlegs.png;

class BasicPixelProcessor extends PixelProcessor {
   protected final Destination dst;
   protected final int samples;

   public BasicPixelProcessor(Destination var1, int var2) {
      this.dst = var1;
      this.samples = var2;
   }

   public boolean process(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 == 1) {
         this.dst.setPixels(var2, var5, var6, var1);
      } else {
         int var7 = var2;
         int var8 = 0;

         for(int var9 = this.samples * var6; var8 < var9; var8 += this.samples) {
            for(int var10 = 0; var10 < this.samples; ++var10) {
               var1[var10] = var1[var8 + var10];
            }

            this.dst.setPixel(var7, var5, var1);
            var7 += var3;
         }
      }

      return true;
   }
}
