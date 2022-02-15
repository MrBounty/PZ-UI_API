package com.evildevil.engines.bubble.texture;

final class DDSCaps2 implements DDSurface {
   protected long caps1 = 0L;
   protected long caps2 = 0L;
   protected long reserved = 0L;
   protected boolean isVolumeTexture = false;

   public DDSCaps2() {
   }

   public void setCaps1(long var1) throws TextureFormatException {
      this.caps1 = var1;
      if ((var1 & 4096L) != 4096L) {
         throw new TextureFormatException("DDS file does not contain DDSCAPS_TEXTURE, but it must!");
      }
   }

   public void setCaps2(long var1) {
      this.caps2 = var1;
      if ((var1 & 2097152L) == 2097152L) {
         this.isVolumeTexture = true;
      }

   }
}
