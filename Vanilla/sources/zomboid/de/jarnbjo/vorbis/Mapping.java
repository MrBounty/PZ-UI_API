package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

abstract class Mapping {
   protected static Mapping createInstance(VorbisStream var0, BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      int var3 = var1.getInt(16);
      switch(var3) {
      case 0:
         return new Mapping0(var0, var1, var2);
      default:
         throw new VorbisFormatException("Mapping type " + var3 + " is not supported.");
      }
   }

   protected abstract int getType();

   protected abstract int[] getAngles();

   protected abstract int[] getMagnitudes();

   protected abstract int[] getMux();

   protected abstract int[] getSubmapFloors();

   protected abstract int[] getSubmapResidues();

   protected abstract int getCouplingSteps();

   protected abstract int getSubmaps();
}
