package com.sixlegs.png;

import java.awt.image.BufferedImage;

final class ProgressUpdater extends PixelProcessor {
   private static final int STEP_PERCENT = 5;
   private final PngImage png;
   private final BufferedImage image;
   private final PixelProcessor pp;
   private final int total;
   private final int step;
   private int count;
   private int mod;

   public ProgressUpdater(PngImage var1, BufferedImage var2, PixelProcessor var3) {
      this.png = var1;
      this.image = var2;
      this.pp = var3;
      this.total = var1.getWidth() * var1.getHeight();
      this.step = Math.max(1, this.total * 5 / 100);
   }

   public boolean process(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      boolean var7 = this.pp.process(var1, var2, var3, var4, var5, var6);
      this.mod += var6;
      this.count += var6;
      if (this.mod > this.step) {
         this.mod %= this.step;
         var7 = var7 && this.png.handleProgress(this.image, 100.0F * (float)this.count / (float)this.total);
      }

      return var7;
   }
}
