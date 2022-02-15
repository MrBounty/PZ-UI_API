package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import de.jarnbjo.util.io.HuffmanNode;
import java.io.IOException;
import java.util.Arrays;

class CodeBook {
   private HuffmanNode huffmanRoot;
   private int dimensions;
   private int entries;
   private int[] entryLengths;
   private float[][] valueVector;
   private static long totalTime = 0L;

   protected CodeBook(BitInputStream var1) throws VorbisFormatException, IOException {
      if (var1.getInt(24) != 5653314) {
         throw new VorbisFormatException("The code book sync pattern is not correct.");
      } else {
         this.dimensions = var1.getInt(16);
         this.entries = var1.getInt(24);
         this.entryLengths = new int[this.entries];
         boolean var2 = var1.getBit();
         int var3;
         int var4;
         if (var2) {
            var3 = var1.getInt(5) + 1;

            int var5;
            for(var4 = 0; var4 < this.entryLengths.length; var4 += var5) {
               var5 = var1.getInt(Util.ilog(this.entryLengths.length - var4));
               if (var4 + var5 > this.entryLengths.length) {
                  throw new VorbisFormatException("The codebook entry length list is longer than the actual number of entry lengths.");
               }

               Arrays.fill(this.entryLengths, var4, var4 + var5, var3);
               ++var3;
            }
         } else {
            boolean var15 = var1.getBit();
            if (var15) {
               for(var4 = 0; var4 < this.entryLengths.length; ++var4) {
                  if (var1.getBit()) {
                     this.entryLengths[var4] = var1.getInt(5) + 1;
                  } else {
                     this.entryLengths[var4] = -1;
                  }
               }
            } else {
               for(var4 = 0; var4 < this.entryLengths.length; ++var4) {
                  this.entryLengths[var4] = var1.getInt(5) + 1;
               }
            }
         }

         if (!this.createHuffmanTree(this.entryLengths)) {
            throw new VorbisFormatException("An exception was thrown when building the codebook Huffman tree.");
         } else {
            var3 = var1.getInt(4);
            switch(var3) {
            case 1:
            case 2:
               float var17 = Util.float32unpack(var1.getInt(32));
               float var16 = Util.float32unpack(var1.getInt(32));
               int var6 = var1.getInt(4) + 1;
               boolean var7 = var1.getBit();
               boolean var8 = false;
               int var18;
               if (var3 == 1) {
                  var18 = Util.lookup1Values(this.entries, this.dimensions);
               } else {
                  var18 = this.entries * this.dimensions;
               }

               int[] var9 = new int[var18];

               int var10;
               for(var10 = 0; var10 < var9.length; ++var10) {
                  var9[var10] = var1.getInt(var6);
               }

               this.valueVector = new float[this.entries][this.dimensions];
               if (var3 != 1) {
                  throw new UnsupportedOperationException();
               } else {
                  for(var10 = 0; var10 < this.entries; ++var10) {
                     float var11 = 0.0F;
                     int var12 = 1;

                     for(int var13 = 0; var13 < this.dimensions; ++var13) {
                        int var14 = var10 / var12 % var18;
                        this.valueVector[var10][var13] = (float)var9[var14] * var16 + var17 + var11;
                        if (var7) {
                           var11 = this.valueVector[var10][var13];
                        }

                        var12 *= var18;
                     }
                  }
               }
            case 0:
               return;
            default:
               throw new VorbisFormatException("Unsupported codebook lookup type: " + var3);
            }
         }
      }
   }

   private boolean createHuffmanTree(int[] var1) {
      this.huffmanRoot = new HuffmanNode();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         int var3 = var1[var2];
         if (var3 > 0 && !this.huffmanRoot.setNewValue(var3, var2)) {
            return false;
         }
      }

      return true;
   }

   protected int getDimensions() {
      return this.dimensions;
   }

   protected int getEntries() {
      return this.entries;
   }

   protected HuffmanNode getHuffmanRoot() {
      return this.huffmanRoot;
   }

   protected int readInt(BitInputStream var1) throws IOException {
      return var1.getInt(this.huffmanRoot);
   }

   protected void readVvAdd(float[][] var1, BitInputStream var2, int var3, int var4) throws VorbisFormatException, IOException {
      int var7 = 0;
      int var8 = var1.length;
      if (var8 != 0) {
         int var9 = (var3 + var4) / var8;
         int var5 = var3 / var8;

         while(var5 < var9) {
            float[] var10 = this.valueVector[var2.getInt(this.huffmanRoot)];

            for(int var6 = 0; var6 < this.dimensions; ++var6) {
               int var10001 = var7++;
               var1[var10001][var5] += var10[var6];
               if (var7 == var8) {
                  var7 = 0;
                  ++var5;
               }
            }
         }

      }
   }
}
