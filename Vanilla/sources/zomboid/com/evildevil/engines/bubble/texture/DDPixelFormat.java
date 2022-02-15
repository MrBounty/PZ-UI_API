package com.evildevil.engines.bubble.texture;

final class DDPixelFormat implements DDSurface {
   protected long size = 0L;
   protected long flags = 0L;
   protected long fourCC = 0L;
   private String fourCCString = "";
   protected long rgbBitCount = 0L;
   protected long rBitMask = 0L;
   protected long gBitMask = 0L;
   protected long bBitMask = 0L;
   protected long rgbAlphaBitMask = 0L;
   protected boolean isCompressed = true;

   public DDPixelFormat() {
   }

   public void setSize(long var1) throws TextureFormatException {
      if (var1 != 32L) {
         throw new TextureFormatException("Wrong DDPixelFormat size. DDPixelFormat size must be 32!");
      } else {
         this.size = var1;
      }
   }

   public void setFlags(long var1) {
      this.flags = var1;
      if ((var1 & 64L) == 64L) {
         this.isCompressed = false;
      } else if ((var1 & 4L) == 4L) {
         this.isCompressed = true;
      }

   }

   public void setFourCC(long var1) {
      this.fourCC = var1;
      if (this.isCompressed) {
         this.createFourCCString();
      }

   }

   private void createFourCCString() {
      byte[] var1 = new byte[]{(byte)((int)this.fourCC), (byte)((int)(this.fourCC >> 8)), (byte)((int)(this.fourCC >> 16)), (byte)((int)(this.fourCC >> 24))};
      this.fourCCString = new String(var1);
   }

   public String getFourCCString() {
      return this.fourCCString;
   }

   public void setRGBBitCount(long var1) {
      this.rgbAlphaBitMask = var1;
   }

   public void setRBitMask(long var1) {
      this.rBitMask = var1;
   }

   public void setGBitMask(long var1) {
      this.gBitMask = var1;
   }

   public void setBBitMask(long var1) {
      this.bBitMask = var1;
   }

   public void setRGBAlphaBitMask(long var1) {
      this.rgbAlphaBitMask = var1;
   }
}
