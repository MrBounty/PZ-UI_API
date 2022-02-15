package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class SetupHeader {
   private static final long HEADER = 126896460427126L;
   private CodeBook[] codeBooks;
   private Floor[] floors;
   private Residue[] residues;
   private Mapping[] mappings;
   private Mode[] modes;

   public SetupHeader(VorbisStream var1, BitInputStream var2) throws VorbisFormatException, IOException {
      if (var2.getLong(48) != 126896460427126L) {
         throw new VorbisFormatException("The setup header has an illegal leading.");
      } else {
         int var3 = var2.getInt(8) + 1;
         this.codeBooks = new CodeBook[var3];

         int var4;
         for(var4 = 0; var4 < this.codeBooks.length; ++var4) {
            this.codeBooks[var4] = new CodeBook(var2);
         }

         var4 = var2.getInt(6) + 1;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            if (var2.getInt(16) != 0) {
               throw new VorbisFormatException("Time domain transformation != 0");
            }
         }

         var5 = var2.getInt(6) + 1;
         this.floors = new Floor[var5];

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            this.floors[var6] = Floor.createInstance(var2, this);
         }

         var6 = var2.getInt(6) + 1;
         this.residues = new Residue[var6];

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            this.residues[var7] = Residue.createInstance(var2, this);
         }

         var7 = var2.getInt(6) + 1;
         this.mappings = new Mapping[var7];

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            this.mappings[var8] = Mapping.createInstance(var1, var2, this);
         }

         var8 = var2.getInt(6) + 1;
         this.modes = new Mode[var8];

         for(int var9 = 0; var9 < var8; ++var9) {
            this.modes[var9] = new Mode(var2, this);
         }

         if (!var2.getBit()) {
            throw new VorbisFormatException("The setup header framing bit is incorrect.");
         }
      }
   }

   public CodeBook[] getCodeBooks() {
      return this.codeBooks;
   }

   public Floor[] getFloors() {
      return this.floors;
   }

   public Residue[] getResidues() {
      return this.residues;
   }

   public Mapping[] getMappings() {
      return this.mappings;
   }

   public Mode[] getModes() {
      return this.modes;
   }
}
