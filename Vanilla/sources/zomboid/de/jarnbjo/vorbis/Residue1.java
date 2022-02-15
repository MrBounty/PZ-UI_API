package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class Residue1 extends Residue {
   protected Residue1(BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      super(var1, var2);
   }

   protected int getType() {
      return 1;
   }

   protected void decodeResidue(VorbisStream var1, BitInputStream var2, Mode var3, int var4, boolean[] var5, float[][] var6) throws VorbisFormatException, IOException {
      throw new UnsupportedOperationException();
   }
}
