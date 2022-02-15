package com.sixlegs.png;

class SuggestedPaletteImpl implements SuggestedPalette {
   private final String name;
   private final int sampleDepth;
   private final byte[] bytes;
   private final int entrySize;
   private final int sampleCount;

   public SuggestedPaletteImpl(String var1, int var2, byte[] var3) {
      this.name = var1;
      this.sampleDepth = var2;
      this.bytes = var3;
      this.entrySize = var2 == 8 ? 6 : 10;
      this.sampleCount = var3.length / this.entrySize;
   }

   public String getName() {
      return this.name;
   }

   public int getSampleCount() {
      return this.sampleCount;
   }

   public int getSampleDepth() {
      return this.sampleDepth;
   }

   public void getSample(int var1, short[] var2) {
      int var3 = var1 * this.entrySize;
      int var4;
      int var5;
      if (this.sampleDepth == 8) {
         for(var4 = 0; var4 < 4; ++var4) {
            var5 = 255 & this.bytes[var3++];
            var2[var4] = (short)var5;
         }
      } else {
         for(var4 = 0; var4 < 4; ++var4) {
            var5 = 255 & this.bytes[var3++];
            int var6 = 255 & this.bytes[var3++];
            var2[var4] = (short)(var5 << 8 | var6);
         }
      }

   }

   public int getFrequency(int var1) {
      int var2 = (var1 + 1) * this.entrySize - 2;
      int var3 = 255 & this.bytes[var2];
      int var4 = 255 & this.bytes[var2 + 1];
      return var3 << 8 | var4;
   }
}
