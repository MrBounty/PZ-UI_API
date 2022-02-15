package com.sixlegs.png;

import java.awt.Rectangle;

public final class PngConfig {
   public static final int READ_ALL = 0;
   public static final int READ_HEADER = 1;
   public static final int READ_UNTIL_DATA = 2;
   public static final int READ_EXCEPT_DATA = 3;
   public static final int READ_EXCEPT_METADATA = 4;
   final int readLimit;
   final float defaultGamma;
   final float displayExponent;
   final boolean warningsFatal;
   final boolean progressive;
   final boolean reduce16;
   final boolean gammaCorrect;
   final Rectangle sourceRegion;
   final int[] subsampling;
   final boolean convertIndexed;

   PngConfig(PngConfig.Builder var1) {
      this.readLimit = var1.readLimit;
      this.defaultGamma = var1.defaultGamma;
      this.displayExponent = var1.displayExponent;
      this.warningsFatal = var1.warningsFatal;
      this.progressive = var1.progressive;
      this.reduce16 = var1.reduce16;
      this.gammaCorrect = var1.gammaCorrect;
      this.sourceRegion = var1.sourceRegion;
      this.subsampling = var1.subsampling;
      this.convertIndexed = var1.convertIndexed;
      boolean var2 = this.getSourceXSubsampling() != 1 || this.getSourceYSubsampling() != 1;
      if (this.progressive && (var2 || this.getSourceRegion() != null)) {
         throw new IllegalStateException("Progressive rendering cannot be used with source regions or subsampling");
      }
   }

   public boolean getConvertIndexed() {
      return this.convertIndexed;
   }

   public boolean getReduce16() {
      return this.reduce16;
   }

   public float getDefaultGamma() {
      return this.defaultGamma;
   }

   public boolean getGammaCorrect() {
      return this.gammaCorrect;
   }

   public boolean getProgressive() {
      return this.progressive;
   }

   public float getDisplayExponent() {
      return this.displayExponent;
   }

   public int getReadLimit() {
      return this.readLimit;
   }

   public boolean getWarningsFatal() {
      return this.warningsFatal;
   }

   public Rectangle getSourceRegion() {
      return this.sourceRegion != null ? new Rectangle(this.sourceRegion) : null;
   }

   public int getSourceXSubsampling() {
      return this.subsampling[0];
   }

   public int getSourceYSubsampling() {
      return this.subsampling[1];
   }

   public int getSubsamplingXOffset() {
      return this.subsampling[2];
   }

   public int getSubsamplingYOffset() {
      return this.subsampling[3];
   }

   public static final class Builder {
      private static final int[] DEFAULT_SUBSAMPLING = new int[]{1, 1, 0, 0};
      int readLimit = 0;
      float defaultGamma = 0.45455F;
      float displayExponent = 2.2F;
      boolean warningsFatal;
      boolean progressive;
      boolean reduce16 = true;
      boolean gammaCorrect = true;
      Rectangle sourceRegion;
      int[] subsampling;
      boolean convertIndexed;

      public Builder() {
         this.subsampling = DEFAULT_SUBSAMPLING;
      }

      public Builder(PngConfig var1) {
         this.subsampling = DEFAULT_SUBSAMPLING;
         this.readLimit = var1.readLimit;
         this.defaultGamma = var1.defaultGamma;
         this.displayExponent = var1.displayExponent;
         this.warningsFatal = var1.warningsFatal;
         this.progressive = var1.progressive;
         this.reduce16 = var1.reduce16;
         this.gammaCorrect = var1.gammaCorrect;
         this.subsampling = var1.subsampling;
      }

      public PngConfig build() {
         return new PngConfig(this);
      }

      public PngConfig.Builder reduce16(boolean var1) {
         this.reduce16 = var1;
         return this;
      }

      public PngConfig.Builder defaultGamma(float var1) {
         this.defaultGamma = var1;
         return this;
      }

      public PngConfig.Builder displayExponent(float var1) {
         this.displayExponent = var1;
         return this;
      }

      public PngConfig.Builder gammaCorrect(boolean var1) {
         this.gammaCorrect = var1;
         return this;
      }

      public PngConfig.Builder progressive(boolean var1) {
         this.progressive = var1;
         return this;
      }

      public PngConfig.Builder readLimit(int var1) {
         this.readLimit = var1;
         return this;
      }

      public PngConfig.Builder warningsFatal(boolean var1) {
         this.warningsFatal = var1;
         return this;
      }

      public PngConfig.Builder sourceRegion(Rectangle var1) {
         if (var1 != null) {
            if (var1.x < 0 || var1.y < 0 || var1.width <= 0 || var1.height <= 0) {
               throw new IllegalArgumentException("invalid source region: " + var1);
            }

            this.sourceRegion = new Rectangle(var1);
         } else {
            this.sourceRegion = null;
         }

         return this;
      }

      public PngConfig.Builder sourceSubsampling(int var1, int var2, int var3, int var4) {
         if (var1 > 0 && var2 > 0 && var3 >= 0 && var3 < var1 && var4 >= 0 && var4 < var2) {
            this.subsampling = new int[]{var1, var2, var3, var4};
            return this;
         } else {
            throw new IllegalArgumentException("invalid subsampling values");
         }
      }

      public PngConfig.Builder convertIndexed(boolean var1) {
         this.convertIndexed = var1;
         return this;
      }
   }
}
