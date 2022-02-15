package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;
import java.util.HashMap;

abstract class Residue {
   protected int begin;
   protected int end;
   protected int partitionSize;
   protected int classifications;
   protected int classBook;
   protected int[] cascade;
   protected int[][] books;
   protected HashMap looks = new HashMap();

   protected Residue() {
   }

   protected Residue(BitInputStream var1, SetupHeader var2) throws VorbisFormatException, IOException {
      this.begin = var1.getInt(24);
      this.end = var1.getInt(24);
      this.partitionSize = var1.getInt(24) + 1;
      this.classifications = var1.getInt(6) + 1;
      this.classBook = var1.getInt(8);
      this.cascade = new int[this.classifications];
      int var3 = 0;

      int var4;
      int var5;
      for(var4 = 0; var4 < this.classifications; ++var4) {
         var5 = 0;
         boolean var6 = false;
         int var7 = var1.getInt(3);
         if (var1.getBit()) {
            var5 = var1.getInt(5);
         }

         this.cascade[var4] = var5 << 3 | var7;
         var3 += Util.icount(this.cascade[var4]);
      }

      this.books = new int[this.classifications][8];

      for(var4 = 0; var4 < this.classifications; ++var4) {
         for(var5 = 0; var5 < 8; ++var5) {
            if ((this.cascade[var4] & 1 << var5) != 0) {
               this.books[var4][var5] = var1.getInt(8);
               if (this.books[var4][var5] > var2.getCodeBooks().length) {
                  throw new VorbisFormatException("Reference to invalid codebook entry in residue header.");
               }
            }
         }
      }

   }

   protected static Residue createInstance(BitInputStream var0, SetupHeader var1) throws VorbisFormatException, IOException {
      int var2 = var0.getInt(16);
      switch(var2) {
      case 0:
         return new Residue0(var0, var1);
      case 1:
         return new Residue2(var0, var1);
      case 2:
         return new Residue2(var0, var1);
      default:
         throw new VorbisFormatException("Residue type " + var2 + " is not supported.");
      }
   }

   protected abstract int getType();

   protected abstract void decodeResidue(VorbisStream var1, BitInputStream var2, Mode var3, int var4, boolean[] var5, float[][] var6) throws VorbisFormatException, IOException;

   protected int getBegin() {
      return this.begin;
   }

   protected int getEnd() {
      return this.end;
   }

   protected int getPartitionSize() {
      return this.partitionSize;
   }

   protected int getClassifications() {
      return this.classifications;
   }

   protected int getClassBook() {
      return this.classBook;
   }

   protected int[] getCascade() {
      return this.cascade;
   }

   protected int[][] getBooks() {
      return this.books;
   }

   protected final void fill(Residue var1) {
      var1.begin = this.begin;
      var1.books = this.books;
      var1.cascade = this.cascade;
      var1.classBook = this.classBook;
      var1.classifications = this.classifications;
      var1.end = this.end;
      var1.partitionSize = this.partitionSize;
   }

   protected Residue.Look getLook(VorbisStream var1, Mode var2) {
      Residue.Look var3 = (Residue.Look)this.looks.get(var2);
      if (var3 == null) {
         var3 = new Residue.Look(var1, var2);
         this.looks.put(var2, var3);
      }

      return var3;
   }

   class Look {
      int map;
      int parts;
      int stages;
      CodeBook[] fullbooks;
      CodeBook phrasebook;
      int[][] partbooks;
      int partvals;
      int[][] decodemap;
      int postbits;
      int phrasebits;
      int frames;

      protected Look(VorbisStream var2, Mode var3) {
         boolean var4 = false;
         boolean var5 = false;
         int var6 = 0;
         this.map = var3.getMapping();
         this.parts = Residue.this.getClassifications();
         this.fullbooks = var2.getSetupHeader().getCodeBooks();
         this.phrasebook = this.fullbooks[Residue.this.getClassBook()];
         int var12 = this.phrasebook.getDimensions();
         this.partbooks = new int[this.parts][];

         int var7;
         int var8;
         int var9;
         for(var7 = 0; var7 < this.parts; ++var7) {
            var8 = Util.ilog(Residue.this.getCascade()[var7]);
            if (var8 != 0) {
               if (var8 > var6) {
                  var6 = var8;
               }

               this.partbooks[var7] = new int[var8];

               for(var9 = 0; var9 < var8; ++var9) {
                  if ((Residue.this.getCascade()[var7] & 1 << var9) != 0) {
                     this.partbooks[var7][var9] = Residue.this.getBooks()[var7][var9];
                  }
               }
            }
         }

         this.partvals = (int)Math.rint(Math.pow((double)this.parts, (double)var12));
         this.stages = var6;
         this.decodemap = new int[this.partvals][];

         for(var7 = 0; var7 < this.partvals; ++var7) {
            var8 = var7;
            var9 = this.partvals / this.parts;
            this.decodemap[var7] = new int[var12];

            for(int var10 = 0; var10 < var12; ++var10) {
               int var11 = var8 / var9;
               var8 -= var11 * var9;
               var9 /= this.parts;
               this.decodemap[var7][var10] = var11;
            }
         }

      }

      protected int[][] getDecodeMap() {
         return this.decodemap;
      }

      protected int getFrames() {
         return this.frames;
      }

      protected int getMap() {
         return this.map;
      }

      protected int[][] getPartBooks() {
         return this.partbooks;
      }

      protected int getParts() {
         return this.parts;
      }

      protected int getPartVals() {
         return this.partvals;
      }

      protected int getPhraseBits() {
         return this.phrasebits;
      }

      protected CodeBook getPhraseBook() {
         return this.phrasebook;
      }

      protected int getPostBits() {
         return this.postbits;
      }

      protected int getStages() {
         return this.stages;
      }
   }
}
