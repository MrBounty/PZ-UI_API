package com.sixlegs.png;

final class ProgressivePixelProcessor extends PixelProcessor {
   private final PixelProcessor pp;
   private final int imgWidth;
   private final int imgHeight;
   private final Destination dst;
   private final int samples;
   private final int[] pixels;

   public ProgressivePixelProcessor(Destination var1, PixelProcessor var2, int var3, int var4) {
      this.pp = var2;
      this.imgWidth = var3;
      this.imgHeight = var4;
      this.dst = var1;
      this.samples = var1.getRaster().getNumBands();
      this.pixels = new int[this.samples * 8];
   }

   public boolean process(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      this.pp.process(var1, var2, var3, var4, var5, var6);
      int var8 = var3 - var2;
      if (var8 > 1 || var3 > 1) {
         int var9 = Math.min(var5 + var3, this.imgHeight);
         int var10 = 0;

         for(int var11 = var2; var10 < var6; ++var10) {
            this.dst.getPixel(var11, var5, this.pixels);
            int var12 = Math.min(var11 + var8, this.imgWidth);
            int var13 = var12 - var11;
            int var14 = this.samples;

            for(int var15 = var13 * this.samples; var14 < var15; ++var14) {
               this.pixels[var14] = this.pixels[var14 - this.samples];
            }

            for(var14 = var5; var14 < var9; ++var14) {
               this.dst.setPixels(var11, var14, var13, this.pixels);
            }

            var11 += var3;
         }
      }

      return true;
   }
}
