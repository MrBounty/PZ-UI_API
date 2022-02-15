package com.sixlegs.png;

import java.awt.image.ComponentColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

class ConvertIndexedDestination extends Destination {
   private final Destination dst;
   private final IndexColorModel srcColorModel;
   private final int srcSamples;
   private final int dstSamples;
   private final int sampleDiff;
   private final int[] row;

   public ConvertIndexedDestination(Destination var1, int var2, IndexColorModel var3, ComponentColorModel var4) {
      this.dst = var1;
      this.srcColorModel = var3;
      this.srcSamples = var3.getNumComponents();
      this.dstSamples = var4.getNumComponents();
      this.sampleDiff = this.srcSamples - this.dstSamples;
      this.row = new int[var2 * this.dstSamples + this.sampleDiff];
   }

   public void setPixels(int var1, int var2, int var3, int[] var4) {
      int var5 = var3 - 1;

      for(int var6 = this.dstSamples * var5; var5 >= 0; var6 -= this.dstSamples) {
         this.srcColorModel.getComponents(var4[var5], this.row, var6);
         --var5;
      }

      if (this.sampleDiff != 0) {
         System.arraycopy(this.row, this.sampleDiff, this.row, 0, this.dstSamples * var3);
      }

      this.dst.setPixels(var1, var2, var3, this.row);
   }

   public void setPixel(int var1, int var2, int[] var3) {
      this.setPixels(var1, var2, 1, var3);
   }

   public void getPixel(int var1, int var2, int[] var3) {
      throw new UnsupportedOperationException("implement me");
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
