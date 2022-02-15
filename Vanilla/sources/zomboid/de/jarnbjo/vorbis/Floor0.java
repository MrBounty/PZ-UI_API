package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class Floor0 extends Floor {
   private int order;
   private int rate;
   private int barkMapSize;
   private int amplitudeBits;
   private int amplitudeOffset;
   private int[] bookList;

   protected Floor0(BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      this.order = var1.getInt(8);
      this.rate = var1.getInt(16);
      this.barkMapSize = var1.getInt(16);
      this.amplitudeBits = var1.getInt(6);
      this.amplitudeOffset = var1.getInt(8);
      int var3 = var1.getInt(4) + 1;
      this.bookList = new int[var3];

      for(int var4 = 0; var4 < this.bookList.length; ++var4) {
         this.bookList[var4] = var1.getInt(8);
         if (this.bookList[var4] > var2.getCodeBooks().length) {
            throw new VorbisFormatException("A floor0_book_list entry is higher than the code book count.");
         }
      }

   }

   protected int getType() {
      return 0;
   }

   protected Floor decodeFloor(VorbisStream var1, BitInputStream var2) throws VorbisFormatException, IOException {
      throw new UnsupportedOperationException();
   }

   protected void computeFloor(float[] var1) {
      throw new UnsupportedOperationException();
   }
}
