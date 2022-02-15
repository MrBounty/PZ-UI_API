package com.sixlegs.png;

import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class Defilterer {
   private final InputStream in;
   private final int width;
   private final int bitDepth;
   private final int samples;
   private final PixelProcessor pp;
   private final int bpp;
   private final int[] row;
   private static int[][] bandOffsets = new int[][]{null, {0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}};

   public Defilterer(InputStream var1, int var2, int var3, int var4, PixelProcessor var5) {
      this.in = var1;
      this.bitDepth = var2;
      this.samples = var3;
      this.width = var4;
      this.pp = var5;
      this.bpp = Math.max(1, var2 * var3 >> 3);
      this.row = new int[var3 * var4];
   }

   public boolean defilter(int var1, int var2, int var3, int var4, int var5, int var6) throws IOException {
      if (var5 != 0 && var6 != 0) {
         int var7 = (this.bitDepth * this.samples * var5 + 7) / 8;
         boolean var8 = this.bitDepth == 16;
         WritableRaster var9 = createInputRaster(this.bitDepth, this.samples, this.width);
         DataBuffer var10 = var9.getDataBuffer();
         byte[] var11 = var8 ? null : ((DataBufferByte)var10).getData();
         short[] var12 = var8 ? ((DataBufferUShort)var10).getData() : null;
         int var13 = var7 + this.bpp;
         byte[] var14 = new byte[var13];
         byte[] var15 = new byte[var13];
         int var16 = 0;

         for(int var17 = var2; var16 < var6; var17 += var4) {
            int var18 = this.in.read();
            if (var18 == -1) {
               throw new EOFException("Unexpected end of image data");
            }

            readFully(this.in, var15, this.bpp, var7);
            defilter(var15, var14, this.bpp, var18);
            if (var8) {
               int var19 = 0;

               for(int var20 = this.bpp; var20 < var13; var20 += 2) {
                  var12[var19] = (short)(var15[var20] << 8 | 255 & var15[var20 + 1]);
                  ++var19;
               }
            } else {
               System.arraycopy(var15, this.bpp, var11, 0, var7);
            }

            var9.getPixels(0, 0, var5, 1, this.row);
            if (!this.pp.process(this.row, var1, var3, var4, var17, var5)) {
               return false;
            }

            byte[] var21 = var15;
            var15 = var14;
            var14 = var21;
            ++var16;
         }

         return true;
      } else {
         return true;
      }
   }

   private static void defilter(byte[] var0, byte[] var1, int var2, int var3) throws PngException {
      int var4 = var0.length;
      int var5;
      int var6;
      switch(var3) {
      case 0:
         return;
      case 1:
         var5 = var2;

         for(var6 = 0; var5 < var4; ++var6) {
            var0[var5] += var0[var6];
            ++var5;
         }

         return;
      case 2:
         for(var5 = var2; var5 < var4; ++var5) {
            var0[var5] += var1[var5];
         }

         return;
      case 3:
         var5 = var2;

         for(var6 = 0; var5 < var4; ++var6) {
            var0[var5] = (byte)(var0[var5] + ((255 & var0[var6]) + (255 & var1[var5])) / 2);
            ++var5;
         }

         return;
      case 4:
         var5 = var2;

         for(var6 = 0; var5 < var4; ++var6) {
            byte var7 = var0[var6];
            byte var8 = var1[var5];
            byte var9 = var1[var6];
            int var10 = 255 & var7;
            int var11 = 255 & var8;
            int var12 = 255 & var9;
            int var13 = var10 + var11 - var12;
            int var14 = var13 - var10;
            if (var14 < 0) {
               var14 = -var14;
            }

            int var15 = var13 - var11;
            if (var15 < 0) {
               var15 = -var15;
            }

            int var16 = var13 - var12;
            if (var16 < 0) {
               var16 = -var16;
            }

            int var17;
            if (var14 <= var15 && var14 <= var16) {
               var17 = var10;
            } else if (var15 <= var16) {
               var17 = var11;
            } else {
               var17 = var12;
            }

            var0[var5] = (byte)(var0[var5] + var17);
            ++var5;
         }

         return;
      default:
         throw new PngException("Unrecognized filter type " + var3, true);
      }
   }

   private static WritableRaster createInputRaster(int var0, int var1, int var2) {
      int var3 = (var0 * var1 * var2 + 7) / 8;
      Point var4 = new Point(0, 0);
      DataBufferByte var6;
      if (var0 < 8 && var1 == 1) {
         var6 = new DataBufferByte(var3);
         return Raster.createPackedRaster(var6, var2, 1, var0, var4);
      } else if (var0 <= 8) {
         var6 = new DataBufferByte(var3);
         return Raster.createInterleavedRaster(var6, var2, 1, var3, var1, bandOffsets[var1], var4);
      } else {
         DataBufferUShort var5 = new DataBufferUShort(var3 / 2);
         return Raster.createInterleavedRaster(var5, var2, 1, var3 / 2, var1, bandOffsets[var1], var4);
      }
   }

   private static void readFully(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      int var5;
      for(int var4 = 0; var4 < var3; var4 += var5) {
         var5 = var0.read(var1, var2 + var4, var3 - var4);
         if (var5 == -1) {
            throw new EOFException("Unexpected end of image data");
         }
      }

   }
}
