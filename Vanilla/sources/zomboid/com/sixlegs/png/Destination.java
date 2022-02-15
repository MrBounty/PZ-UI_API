package com.sixlegs.png;

import java.awt.image.WritableRaster;

abstract class Destination {
   public abstract void setPixels(int var1, int var2, int var3, int[] var4);

   public abstract void setPixel(int var1, int var2, int[] var3);

   public abstract void getPixel(int var1, int var2, int[] var3);

   public abstract WritableRaster getRaster();

   public abstract int getSourceWidth();

   public abstract void done();
}
