package com.sixlegs.png;

final class TransGammaPixelProcessor extends BasicPixelProcessor {
   private final short[] gammaTable;
   private final int[] trans;
   private final int shift;
   private final int max;
   private final int samplesNoAlpha;
   private final int[] temp;

   public TransGammaPixelProcessor(Destination var1, short[] var2, int[] var3, int var4) {
      super(var1, var1.getRaster().getNumBands());
      this.gammaTable = var2;
      this.trans = var3;
      this.shift = var4;
      this.max = var2.length - 1;
      this.samplesNoAlpha = this.samples - 1;
      this.temp = new int[this.samples * var1.getSourceWidth()];
   }

   public boolean process(int[] var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = var6 * this.samplesNoAlpha;
      int var8 = 0;

      for(int var9 = 0; var8 < var7; var9 += this.samples) {
         boolean var10 = false;

         for(int var11 = 0; var11 < this.samplesNoAlpha; ++var11) {
            int var12 = var1[var8 + var11];
            var10 = var10 || var12 != this.trans[var11];
            this.temp[var9 + var11] = '\uffff' & this.gammaTable[var12 >> this.shift];
         }

         this.temp[var9 + this.samplesNoAlpha] = var10 ? this.max : 0;
         var8 += this.samplesNoAlpha;
      }

      return super.process(this.temp, var2, var3, var4, var5, var6);
   }
}
