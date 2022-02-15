package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class Mapping0 extends Mapping {
   private int[] magnitudes;
   private int[] angles;
   private int[] mux;
   private int[] submapFloors;
   private int[] submapResidues;

   protected Mapping0(VorbisStream var1, BitInputStream var2, SetupHeader var3) throws VorbisFormatException, IOException {
      int var4 = 1;
      if (var2.getBit()) {
         var4 = var2.getInt(4) + 1;
      }

      int var5 = var1.getIdentificationHeader().getChannels();
      int var6 = Util.ilog(var5 - 1);
      int var7;
      int var8;
      if (var2.getBit()) {
         var7 = var2.getInt(8) + 1;
         this.magnitudes = new int[var7];
         this.angles = new int[var7];

         for(var8 = 0; var8 < var7; ++var8) {
            this.magnitudes[var8] = var2.getInt(var6);
            this.angles[var8] = var2.getInt(var6);
            if (this.magnitudes[var8] == this.angles[var8] || this.magnitudes[var8] >= var5 || this.angles[var8] >= var5) {
               System.err.println(this.magnitudes[var8]);
               System.err.println(this.angles[var8]);
               throw new VorbisFormatException("The channel magnitude and/or angle mismatch.");
            }
         }
      } else {
         this.magnitudes = new int[0];
         this.angles = new int[0];
      }

      if (var2.getInt(2) != 0) {
         throw new VorbisFormatException("A reserved mapping field has an invalid value.");
      } else {
         this.mux = new int[var5];
         if (var4 > 1) {
            for(var7 = 0; var7 < var5; ++var7) {
               this.mux[var7] = var2.getInt(4);
               if (this.mux[var7] > var4) {
                  throw new VorbisFormatException("A mapping mux value is higher than the number of submaps");
               }
            }
         } else {
            for(var7 = 0; var7 < var5; ++var7) {
               this.mux[var7] = 0;
            }
         }

         this.submapFloors = new int[var4];
         this.submapResidues = new int[var4];
         var7 = var3.getFloors().length;
         var8 = var3.getResidues().length;

         for(int var9 = 0; var9 < var4; ++var9) {
            var2.getInt(8);
            this.submapFloors[var9] = var2.getInt(8);
            this.submapResidues[var9] = var2.getInt(8);
            if (this.submapFloors[var9] > var7) {
               throw new VorbisFormatException("A mapping floor value is higher than the number of floors.");
            }

            if (this.submapResidues[var9] > var8) {
               throw new VorbisFormatException("A mapping residue value is higher than the number of residues.");
            }
         }

      }
   }

   protected int getType() {
      return 0;
   }

   protected int[] getAngles() {
      return this.angles;
   }

   protected int[] getMagnitudes() {
      return this.magnitudes;
   }

   protected int[] getMux() {
      return this.mux;
   }

   protected int[] getSubmapFloors() {
      return this.submapFloors;
   }

   protected int[] getSubmapResidues() {
      return this.submapResidues;
   }

   protected int getCouplingSteps() {
      return this.angles.length;
   }

   protected int getSubmaps() {
      return this.submapFloors.length;
   }
}
