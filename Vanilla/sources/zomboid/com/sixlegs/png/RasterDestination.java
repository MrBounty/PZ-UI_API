package com.sixlegs.png;

import java.awt.image.WritableRaster;

class RasterDestination extends Destination {
   protected final WritableRaster raster;
   protected final int sourceWidth;

   public RasterDestination(WritableRaster var1, int var2) {
      this.raster = var1;
      this.sourceWidth = var2;
   }

   public void setPixels(int var1, int var2, int var3, int[] var4) {
      this.raster.setPixels(var1, var2, var3, 1, var4);
   }

   public void setPixel(int var1, int var2, int[] var3) {
      this.raster.setPixel(var1, var2, var3);
   }

   public void getPixel(int var1, int var2, int[] var3) {
      this.raster.getPixel(var1, var2, var3);
   }

   public WritableRaster getRaster() {
      return this.raster;
   }

   public int getSourceWidth() {
      return this.sourceWidth;
   }

   public void done() {
   }
}
