package de.jarnbjo.util.io;

import java.io.IOException;

public class ByteArrayBitInputStream implements BitInputStream {
   private byte[] source;
   private byte currentByte;
   private int endian;
   private int byteIndex;
   private int bitIndex;

   public ByteArrayBitInputStream(byte[] var1) {
      this(var1, 0);
   }

   public ByteArrayBitInputStream(byte[] var1, int var2) {
      this.byteIndex = 0;
      this.bitIndex = 0;
      this.endian = var2;
      this.source = var1;
      this.currentByte = var1[0];
      this.bitIndex = var2 == 0 ? 0 : 7;
   }

   public boolean getBit() throws IOException {
      if (this.endian == 0) {
         if (this.bitIndex > 7) {
            this.bitIndex = 0;
            this.currentByte = this.source[++this.byteIndex];
         }

         return (this.currentByte & 1 << this.bitIndex++) != 0;
      } else {
         if (this.bitIndex < 0) {
            this.bitIndex = 7;
            this.currentByte = this.source[++this.byteIndex];
         }

         return (this.currentByte & 1 << this.bitIndex--) != 0;
      }
   }

   public int getInt(int var1) throws IOException {
      if (var1 > 32) {
         throw new IllegalArgumentException("Argument \"bits\" must be <= 32");
      } else {
         int var2 = 0;
         int var3;
         if (this.endian == 0) {
            for(var3 = 0; var3 < var1; ++var3) {
               if (this.getBit()) {
                  var2 |= 1 << var3;
               }
            }
         } else {
            if (this.bitIndex < 0) {
               this.bitIndex = 7;
               this.currentByte = this.source[++this.byteIndex];
            }

            if (var1 <= this.bitIndex + 1) {
               var3 = this.currentByte & 255;
               int var4 = 1 + this.bitIndex - var1;
               int var5 = (1 << var1) - 1 << var4;
               var2 = (var3 & var5) >> var4;
               this.bitIndex -= var1;
            } else {
               var2 = (this.currentByte & 255 & (1 << this.bitIndex + 1) - 1) << var1 - this.bitIndex - 1;
               var1 -= this.bitIndex + 1;

               for(this.currentByte = this.source[++this.byteIndex]; var1 >= 8; this.currentByte = this.source[++this.byteIndex]) {
                  var1 -= 8;
                  var2 |= (this.source[this.byteIndex] & 255) << var1;
               }

               if (var1 > 0) {
                  var3 = this.source[this.byteIndex] & 255;
                  var2 |= var3 >> 8 - var1 & (1 << var1) - 1;
                  this.bitIndex = 7 - var1;
               } else {
                  this.currentByte = this.source[--this.byteIndex];
                  this.bitIndex = -1;
               }
            }
         }

         return var2;
      }
   }

   public int getSignedInt(int var1) throws IOException {
      int var2 = this.getInt(var1);
      if (var2 >= 1 << var1 - 1) {
         var2 -= 1 << var1;
      }

      return var2;
   }

   public int getInt(HuffmanNode var1) throws IOException {
      for(; var1.value == null; var1 = (this.currentByte & 1 << this.bitIndex++) != 0 ? var1.o1 : var1.o0) {
         if (this.bitIndex > 7) {
            this.bitIndex = 0;
            this.currentByte = this.source[++this.byteIndex];
         }
      }

      return var1.value;
   }

   public long getLong(int var1) throws IOException {
      if (var1 > 64) {
         throw new IllegalArgumentException("Argument \"bits\" must be <= 64");
      } else {
         long var2 = 0L;
         int var4;
         if (this.endian == 0) {
            for(var4 = 0; var4 < var1; ++var4) {
               if (this.getBit()) {
                  var2 |= 1L << var4;
               }
            }
         } else {
            for(var4 = var1 - 1; var4 >= 0; --var4) {
               if (this.getBit()) {
                  var2 |= 1L << var4;
               }
            }
         }

         return var2;
      }
   }

   public int readSignedRice(int var1) throws IOException {
      int var2 = -1;
      boolean var3 = false;
      boolean var4 = false;
      if (this.endian == 0) {
         throw new UnsupportedOperationException("ByteArrayBitInputStream.readSignedRice() is only supported in big endian mode");
      } else {
         byte var5 = this.source[this.byteIndex];

         do {
            ++var2;
            if (this.bitIndex < 0) {
               this.bitIndex = 7;
               ++this.byteIndex;
               var5 = this.source[this.byteIndex];
            }
         } while((var5 & 1 << this.bitIndex--) == 0);

         if (this.bitIndex < 0) {
            this.bitIndex = 7;
            ++this.byteIndex;
         }

         int var7;
         int var10;
         if (var1 <= this.bitIndex + 1) {
            var7 = this.source[this.byteIndex] & 255;
            int var8 = 1 + this.bitIndex - var1;
            int var9 = (1 << var1) - 1 << var8;
            var10 = (var7 & var9) >> var8;
            this.bitIndex -= var1;
         } else {
            var10 = (this.source[this.byteIndex] & 255 & (1 << this.bitIndex + 1) - 1) << var1 - this.bitIndex - 1;
            int var6 = var1 - (this.bitIndex + 1);
            ++this.byteIndex;

            while(var6 >= 8) {
               var6 -= 8;
               var10 |= (this.source[this.byteIndex] & 255) << var6;
               ++this.byteIndex;
            }

            if (var6 > 0) {
               var7 = this.source[this.byteIndex] & 255;
               var10 |= var7 >> 8 - var6 & (1 << var6) - 1;
               this.bitIndex = 7 - var6;
            } else {
               --this.byteIndex;
               this.bitIndex = -1;
            }
         }

         int var11 = var2 << var1 | var10;
         return (var11 & 1) == 1 ? -(var11 >> 1) - 1 : var11 >> 1;
      }
   }

   public void readSignedRice(int var1, int[] var2, int var3, int var4) throws IOException {
      if (this.endian == 0) {
         throw new UnsupportedOperationException("ByteArrayBitInputStream.readSignedRice() is only supported in big endian mode");
      } else {
         for(int var5 = var3; var5 < var3 + var4; ++var5) {
            int var6 = -1;
            boolean var7 = false;
            byte var8 = this.source[this.byteIndex];

            do {
               ++var6;
               if (this.bitIndex < 0) {
                  this.bitIndex = 7;
                  ++this.byteIndex;
                  var8 = this.source[this.byteIndex];
               }
            } while((var8 & 1 << this.bitIndex--) == 0);

            if (this.bitIndex < 0) {
               this.bitIndex = 7;
               ++this.byteIndex;
            }

            int var10;
            int var13;
            if (var1 <= this.bitIndex + 1) {
               var10 = this.source[this.byteIndex] & 255;
               int var11 = 1 + this.bitIndex - var1;
               int var12 = (1 << var1) - 1 << var11;
               var13 = (var10 & var12) >> var11;
               this.bitIndex -= var1;
            } else {
               var13 = (this.source[this.byteIndex] & 255 & (1 << this.bitIndex + 1) - 1) << var1 - this.bitIndex - 1;
               int var9 = var1 - (this.bitIndex + 1);
               ++this.byteIndex;

               while(var9 >= 8) {
                  var9 -= 8;
                  var13 |= (this.source[this.byteIndex] & 255) << var9;
                  ++this.byteIndex;
               }

               if (var9 > 0) {
                  var10 = this.source[this.byteIndex] & 255;
                  var13 |= var10 >> 8 - var9 & (1 << var9) - 1;
                  this.bitIndex = 7 - var9;
               } else {
                  --this.byteIndex;
                  this.bitIndex = -1;
               }
            }

            var10 = var6 << var1 | var13;
            var2[var5] = (var10 & 1) == 1 ? -(var10 >> 1) - 1 : var10 >> 1;
         }

      }
   }

   public void align() {
      if (this.endian == 1 && this.bitIndex >= 0) {
         this.bitIndex = 7;
         ++this.byteIndex;
      } else if (this.endian == 0 && this.bitIndex <= 7) {
         this.bitIndex = 0;
         ++this.byteIndex;
      }

   }

   public void setEndian(int var1) {
      if (this.endian == 1 && var1 == 0) {
         this.bitIndex = 0;
         ++this.byteIndex;
      } else if (this.endian == 0 && var1 == 1) {
         this.bitIndex = 7;
         ++this.byteIndex;
      }

      this.endian = var1;
   }

   public byte[] getSource() {
      return this.source;
   }
}
