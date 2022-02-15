package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

public class IdentificationHeader {
   private int version;
   private int channels;
   private int sampleRate;
   private int bitrateMaximum;
   private int bitrateNominal;
   private int bitrateMinimum;
   private int blockSize0;
   private int blockSize1;
   private boolean framingFlag;
   private MdctFloat[] mdct = new MdctFloat[2];
   private static final long HEADER = 126896460427126L;

   public IdentificationHeader(BitInputStream var1) throws VorbisFormatException, IOException {
      long var2 = var1.getLong(48);
      if (var2 != 126896460427126L) {
         throw new VorbisFormatException("The identification header has an illegal leading.");
      } else {
         this.version = var1.getInt(32);
         this.channels = var1.getInt(8);
         this.sampleRate = var1.getInt(32);
         this.bitrateMaximum = var1.getInt(32);
         this.bitrateNominal = var1.getInt(32);
         this.bitrateMinimum = var1.getInt(32);
         int var4 = var1.getInt(8);
         this.blockSize0 = 1 << (var4 & 15);
         this.blockSize1 = 1 << (var4 >> 4);
         this.mdct[0] = new MdctFloat(this.blockSize0);
         this.mdct[1] = new MdctFloat(this.blockSize1);
         this.framingFlag = var1.getInt(8) != 0;
      }
   }

   public int getSampleRate() {
      return this.sampleRate;
   }

   public int getMaximumBitrate() {
      return this.bitrateMaximum;
   }

   public int getNominalBitrate() {
      return this.bitrateNominal;
   }

   public int getMinimumBitrate() {
      return this.bitrateMinimum;
   }

   public int getChannels() {
      return this.channels;
   }

   public int getBlockSize0() {
      return this.blockSize0;
   }

   public int getBlockSize1() {
      return this.blockSize1;
   }

   protected MdctFloat getMdct0() {
      return this.mdct[0];
   }

   protected MdctFloat getMdct1() {
      return this.mdct[1];
   }

   public int getVersion() {
      return this.version;
   }
}
