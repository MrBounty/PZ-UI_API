package com.sixlegs.png;

import java.awt.Rectangle;
import java.awt.image.WritableRaster;

final class SourceRegionDestination extends Destination {
   private final Destination dst;
   private final int xoff;
   private final int yoff;
   private final int xlen;
   private final int ylen;
   private final int samples;

   public SourceRegionDestination(Destination var1, Rectangle var2) {
      this.dst = var1;
      this.xoff = var2.x;
      this.yoff = var2.y;
      this.xlen = var2.width;
      this.ylen = var2.height;
      this.samples = var1.getRaster().getNumBands();
   }

   public void setPixels(int var1, int var2, int var3, int[] var4) {
      if (var2 >= this.yoff && var2 < this.yoff + this.ylen) {
         int var5 = Math.max(var1, this.xoff);
         int var6 = Math.min(var1 + var3, this.xoff + this.xlen) - var5;
         if (var6 > 0) {
            if (var5 > var1) {
               System.arraycopy(var4, var5 * this.samples, var4, 0, var6 * this.samples);
            }

            this.dst.setPixels(var5 - this.xoff, var2 - this.yoff, var6, var4);
         }
      }

   }

   public void setPixel(int var1, int var2, int[] var3) {
      var1 -= this.xoff;
      var2 -= this.yoff;
      if (var1 >= 0 && var2 >= 0 && var1 < this.xlen && var2 < this.ylen) {
         this.dst.setPixel(var1, var2, var3);
      }

   }

   public void getPixel(int var1, int var2, int[] var3) {
      throw new UnsupportedOperationException();
   }

   public WritableRaster getRaster() {
      return this.dst.getRaster();
   }

   public int getSourceWidth() {
      return this.dst.getSourceWidth();
   }

   public void done() {
      this.dst.done();
   }
}
