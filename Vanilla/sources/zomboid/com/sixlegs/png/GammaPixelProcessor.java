package com.sixlegs.png;

final class GammaPixelProcessor extends BasicPixelProcessor {
   private final short[] gammaTable;
   private final int shift;
   private final int samplesNoAlpha;
   private final boolean hasAlpha;
   private final boolean shiftAlpha;

   public GammaPixelProcessor(Destination var1, short[] var2, int var3) {
      super(var1, var1.getRaster().getNumBands());
      this.gammaTable = var2;
      this.shift = var3;
      this.hasAlpha = this.samples % 2 == 0;
      this.samplesNoAlpha = this.hasAlpha ? this.samples - 1 : this.samples;
      this.shiftAlpha = this.hasAlpha && var3 > 0;
   }

   public boolean process(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = this.samples * var6;

      int var8;
      for(var8 = 0; var8 < this.samplesNoAlpha; ++var8) {
         for(int var9 = var8; var9 < var7; var9 += this.samples) {
            var1[var9] = '\uffff' & this.gammaTable[var1[var9] >> this.shift];
         }
      }

      if (this.shiftAlpha) {
         for(var8 = this.samplesNoAlpha; var8 < var7; var8 += this.samples) {
            var1[var8] >>= this.shift;
         }
      }

      return super.process(var1, var2, var3, var4, var5, var6);
   }
}
