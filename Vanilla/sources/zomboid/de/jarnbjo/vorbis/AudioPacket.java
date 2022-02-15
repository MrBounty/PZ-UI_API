package de.jarnbjo.vorbis;

import de.jarnbjo.util.io.BitInputStream;
import java.io.IOException;

class AudioPacket {
   private int modeNumber;
   private Mode mode;
   private Mapping mapping;
   private int n;
   private boolean blockFlag;
   private boolean previousWindowFlag;
   private boolean nextWindowFlag;
   private int windowCenter;
   private int leftWindowStart;
   private int leftWindowEnd;
   private int leftN;
   private int rightWindowStart;
   private int rightWindowEnd;
   private int rightN;
   private float[] window;
   private float[][] pcm;
   private int[][] pcmInt;
   private Floor[] channelFloors;
   private boolean[] noResidues;
   private static final float[][] windows = new float[8][];

   protected AudioPacket(VorbisStream var1, BitInputStream var2) throws VorbisFormatException, IOException {
      SetupHeader var3 = var1.getSetupHeader();
      IdentificationHeader var4 = var1.getIdentificationHeader();
      Mode[] var5 = var3.getModes();
      Mapping[] var6 = var3.getMappings();
      Residue[] var7 = var3.getResidues();
      int var8 = var4.getChannels();
      if (var2.getInt(1) != 0) {
         throw new VorbisFormatException("Packet type mismatch when trying to create an audio packet.");
      } else {
         this.modeNumber = var2.getInt(Util.ilog(var5.length - 1));

         try {
            this.mode = var5[this.modeNumber];
         } catch (ArrayIndexOutOfBoundsException var25) {
            throw new VorbisFormatException("Reference to invalid mode in audio packet.");
         }

         this.mapping = var6[this.mode.getMapping()];
         int[] var9 = this.mapping.getMagnitudes();
         int[] var10 = this.mapping.getAngles();
         this.blockFlag = this.mode.getBlockFlag();
         int var11 = var4.getBlockSize0();
         int var12 = var4.getBlockSize1();
         this.n = this.blockFlag ? var12 : var11;
         if (this.blockFlag) {
            this.previousWindowFlag = var2.getBit();
            this.nextWindowFlag = var2.getBit();
         }

         this.windowCenter = this.n / 2;
         if (this.blockFlag && !this.previousWindowFlag) {
            this.leftWindowStart = this.n / 4 - var11 / 4;
            this.leftWindowEnd = this.n / 4 + var11 / 4;
            this.leftN = var11 / 2;
         } else {
            this.leftWindowStart = 0;
            this.leftWindowEnd = this.n / 2;
            this.leftN = this.windowCenter;
         }

         if (this.blockFlag && !this.nextWindowFlag) {
            this.rightWindowStart = this.n * 3 / 4 - var11 / 4;
            this.rightWindowEnd = this.n * 3 / 4 + var11 / 4;
            this.rightN = var11 / 2;
         } else {
            this.rightWindowStart = this.windowCenter;
            this.rightWindowEnd = this.n;
            this.rightN = this.n / 2;
         }

         this.window = this.getComputedWindow();
         this.channelFloors = new Floor[var8];
         this.noResidues = new boolean[var8];
         this.pcm = new float[var8][this.n];
         this.pcmInt = new int[var8][this.n];
         boolean var13 = true;

         int var14;
         int var15;
         int var16;
         for(var14 = 0; var14 < var8; ++var14) {
            var15 = this.mapping.getMux()[var14];
            var16 = this.mapping.getSubmapFloors()[var15];
            Floor var17 = var3.getFloors()[var16].decodeFloor(var1, var2);
            this.channelFloors[var14] = var17;
            this.noResidues[var14] = var17 == null;
            if (var17 != null) {
               var13 = false;
            }
         }

         if (!var13) {
            for(var14 = 0; var14 < var9.length; ++var14) {
               if (!this.noResidues[var9[var14]] || !this.noResidues[var10[var14]]) {
                  this.noResidues[var9[var14]] = false;
                  this.noResidues[var10[var14]] = false;
               }
            }

            Residue[] var26 = new Residue[this.mapping.getSubmaps()];

            for(var15 = 0; var15 < this.mapping.getSubmaps(); ++var15) {
               var16 = 0;
               boolean[] var28 = new boolean[var8];

               int var18;
               for(var18 = 0; var18 < var8; ++var18) {
                  if (this.mapping.getMux()[var18] == var15) {
                     var28[var16++] = this.noResidues[var18];
                  }
               }

               var18 = this.mapping.getSubmapResidues()[var15];
               Residue var19 = var7[var18];
               var19.decodeResidue(var1, var2, this.mode, var16, var28, this.pcm);
            }

            for(var15 = this.mapping.getCouplingSteps() - 1; var15 >= 0; --var15) {
               double var27 = 0.0D;
               double var30 = 0.0D;
               float[] var20 = this.pcm[var9[var15]];
               float[] var21 = this.pcm[var10[var15]];

               for(int var22 = 0; var22 < var20.length; ++var22) {
                  float var23 = var21[var22];
                  float var24 = var20[var22];
                  if (var23 > 0.0F) {
                     var21[var22] = var24 > 0.0F ? var24 - var23 : var24 + var23;
                  } else {
                     var20[var22] = var24 > 0.0F ? var24 + var23 : var24 - var23;
                     var21[var22] = var24;
                  }
               }
            }

            for(var15 = 0; var15 < var8; ++var15) {
               if (this.channelFloors[var15] != null) {
                  this.channelFloors[var15].computeFloor(this.pcm[var15]);
               }
            }

            for(var15 = 0; var15 < var8; ++var15) {
               MdctFloat var29 = this.blockFlag ? var4.getMdct1() : var4.getMdct0();
               var29.imdct(this.pcm[var15], this.window, this.pcmInt[var15]);
            }

         }
      }
   }

   private float[] getComputedWindow() {
      int var1 = (this.blockFlag ? 4 : 0) + (this.previousWindowFlag ? 2 : 0) + (this.nextWindowFlag ? 1 : 0);
      float[] var2 = windows[var1];
      if (var2 == null) {
         var2 = new float[this.n];

         int var3;
         float var4;
         for(var3 = 0; var3 < this.leftN; ++var3) {
            var4 = (float)(((double)var3 + 0.5D) / (double)this.leftN * 3.141592653589793D / 2.0D);
            var4 = (float)Math.sin((double)var4);
            var4 *= var4;
            var4 = (float)((double)var4 * 1.5707963705062866D);
            var4 = (float)Math.sin((double)var4);
            var2[var3 + this.leftWindowStart] = var4;
         }

         for(var3 = this.leftWindowEnd; var3 < this.rightWindowStart; var2[var3++] = 1.0F) {
         }

         for(var3 = 0; var3 < this.rightN; ++var3) {
            var4 = (float)(((double)(this.rightN - var3) - 0.5D) / (double)this.rightN * 3.141592653589793D / 2.0D);
            var4 = (float)Math.sin((double)var4);
            var4 *= var4;
            var4 = (float)((double)var4 * 1.5707963705062866D);
            var4 = (float)Math.sin((double)var4);
            var2[var3 + this.rightWindowStart] = var4;
         }

         windows[var1] = var2;
      }

      return var2;
   }

   protected int getNumberOfSamples() {
      return this.rightWindowStart - this.leftWindowStart;
   }

   protected int getPcm(AudioPacket var1, int[][] var2) {
      int var3 = this.pcm.length;

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         int var6 = 0;
         int var7 = var1.rightWindowStart;
         int[] var8 = var1.pcmInt[var5];
         int[] var9 = this.pcmInt[var5];
         int[] var10 = var2[var5];

         for(int var11 = this.leftWindowStart; var11 < this.leftWindowEnd; ++var11) {
            int var4 = var8[var7++] + var9[var11];
            if (var4 > 32767) {
               var4 = 32767;
            }

            if (var4 < -32768) {
               var4 = -32768;
            }

            var10[var6++] = var4;
         }
      }

      if (this.leftWindowEnd + 1 < this.rightWindowStart) {
         for(var5 = 0; var5 < var3; ++var5) {
            System.arraycopy(this.pcmInt[var5], this.leftWindowEnd, var2[var5], this.leftWindowEnd - this.leftWindowStart, this.rightWindowStart - this.leftWindowEnd);
         }
      }

      return this.rightWindowStart - this.leftWindowStart;
   }

   protected void getPcm(AudioPacket var1, byte[] var2) {
      int var3 = this.pcm.length;

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = 0;
         int var7 = var1.rightWindowStart;
         int[] var8 = var1.pcmInt[var5];
         int[] var9 = this.pcmInt[var5];

         int var4;
         int var10;
         for(var10 = this.leftWindowStart; var10 < this.leftWindowEnd; ++var10) {
            var4 = var8[var7++] + var9[var10];
            if (var4 > 32767) {
               var4 = 32767;
            }

            if (var4 < -32768) {
               var4 = -32768;
            }

            var2[var6 + var5 * 2 + 1] = (byte)(var4 & 255);
            var2[var6 + var5 * 2] = (byte)(var4 >> 8 & 255);
            var6 += var3 * 2;
         }

         var6 = (this.leftWindowEnd - this.leftWindowStart) * var3 * 2;

         for(var10 = this.leftWindowEnd; var10 < this.rightWindowStart; ++var10) {
            var4 = var9[var10];
            if (var4 > 32767) {
               var4 = 32767;
            }

            if (var4 < -32768) {
               var4 = -32768;
            }

            var2[var6 + var5 * 2 + 1] = (byte)(var4 & 255);
            var2[var6 + var5 * 2] = (byte)(var4 >> 8 & 255);
            var6 += var3 * 2;
         }
      }

   }

   protected float[] getWindow() {
      return this.window;
   }

   protected int getLeftWindowStart() {
      return this.leftWindowStart;
   }

   protected int getLeftWindowEnd() {
      return this.leftWindowEnd;
   }

   protected int getRightWindowStart() {
      return this.rightWindowStart;
   }

   protected int getRightWindowEnd() {
      return this.rightWindowEnd;
   }

   public int[][] getPcm() {
      return this.pcmInt;
   }

   public float[][] getFreqencyDomain() {
      return this.pcm;
   }
}
