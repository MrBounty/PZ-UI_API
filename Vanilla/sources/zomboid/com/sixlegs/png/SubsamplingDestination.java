package com.sixlegs.png;

import java.awt.image.WritableRaster;

final class SubsamplingDestination extends RasterDestination {
   private final int xsub;
   private final int ysub;
   private final int xoff;
   private final int yoff;
   private final int[] singlePixel;

   public SubsamplingDestination(WritableRaster var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2);
      this.xsub = var3;
      this.ysub = var4;
      this.xoff = var5;
      this.yoff = var6;
      this.singlePixel = new int[var1.getNumBands()];
   }

   public void setPixels(int var1, int var2, int var3, int[] var4) {
      if ((var2 - this.yoff) % this.ysub == 0) {
         int var5 = (var1 - this.xoff) / this.xsub;
         int var6 = (var2 - this.yoff) / this.ysub;
         int var7 = var5 * this.xsub + this.xoff;
         if (var7 < var1) {
            ++var5;
            var7 += this.xsub;
         }

         int var8 = this.raster.getNumBands();
         int var9 = var7 - var1;

         for(int var10 = var1 + var3; var9 < var10; var9 += this.xsub) {
            System.arraycopy(var4, var9 * var8, this.singlePixel, 0, var8);
            super.setPixel(var5++, var6, this.singlePixel);
         }
      }

   }

   public void setPixel(int var1, int var2, int[] var3) {
      var1 -= this.xoff;
      var2 -= this.yoff;
      if (var1 % this.xsub == 0 && var2 % this.ysub == 0) {
         super.setPixel(var1 / this.xsub, var2 / this.ysub, var3);
      }

   }

   public void getPixel(int var1, int var2, int[] var3) {
      throw new UnsupportedOperationException();
   }
}
