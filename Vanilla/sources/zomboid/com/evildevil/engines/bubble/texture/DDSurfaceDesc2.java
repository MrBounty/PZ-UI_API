package com.evildevil.engines.bubble.texture;

import java.util.Objects;

final class DDSurfaceDesc2 implements DDSurface {
   private final String DDS_IDENTIFIER = "DDS ";
   protected long identifier = 0L;
   private String identifierString = "";
   protected long size = 0L;
   protected long flags = 0L;
   protected long height = 0L;
   protected long width = 0L;
   protected long pitchOrLinearSize = 0L;
   protected long depth = 0L;
   protected long mipMapCount = 0L;
   protected long reserved = 0L;
   private DDPixelFormat pixelFormat = null;
   private DDSCaps2 caps2 = null;
   protected int reserved2 = 0;

   public DDSurfaceDesc2() {
      this.pixelFormat = new DDPixelFormat();
      this.caps2 = new DDSCaps2();
   }

   public void setIdentifier(long var1) throws TextureFormatException {
      this.identifier = var1;
      this.createIdentifierString();
   }

   private void createIdentifierString() throws TextureFormatException {
      byte[] var1 = new byte[]{(byte)((int)this.identifier), (byte)((int)(this.identifier >> 8)), (byte)((int)(this.identifier >> 16)), (byte)((int)(this.identifier >> 24))};
      this.identifierString = new String(var1);
      String var10000 = this.identifierString;
      Objects.requireNonNull(this);
      if (!var10000.equalsIgnoreCase("DDS ")) {
         throw new TextureFormatException("The DDS Identifier is wrong. Have to be \"DDS \"!");
      }
   }

   public void setSize(long var1) throws TextureFormatException {
      if (var1 != 124L) {
         throw new TextureFormatException("Wrong DDSurfaceDesc2 size. DDSurfaceDesc2 size must be 124!");
      } else {
         this.size = var1;
      }
   }

   public void setFlags(long var1) throws TextureFormatException {
      this.flags = var1;
      if ((var1 & 1L) != 1L || (var1 & 4096L) != 4096L || (var1 & 4L) != 4L || (var1 & 2L) != 2L) {
         throw new TextureFormatException("One or more required flag bits are set wrong\nflags have to include \"DDSD_CAPS, DDSD_PIXELFORMAT, DDSD_WIDTH, DDSD_HEIGHT\"");
      }
   }

   public void setHeight(long var1) {
      this.height = Math.abs(var1);
   }

   public void setWidth(long var1) {
      this.width = var1;
   }

   public void setPitchOrLinearSize(long var1) {
      this.pitchOrLinearSize = var1;
      this.pitchOrLinearSize = (this.width + 3L) / 4L * ((this.height + 3L) / 4L) * 16L;
      if (this.pitchOrLinearSize > 1000000L) {
         this.pitchOrLinearSize = (this.width + 3L) / 4L * ((this.height + 3L) / 4L) * 16L;
      }

   }

   public void setDepth(long var1) {
      this.depth = var1;
   }

   public void setMipMapCount(long var1) {
      this.mipMapCount = var1;
   }

   public void setDDPixelFormat(DDPixelFormat var1) throws NullPointerException {
      if (var1 == null) {
         throw new NullPointerException("DDPixelFormat can't be null. DDSurfaceDesc2 needs a valid DDPixelFormat.");
      } else {
         this.pixelFormat = var1;
      }
   }

   public DDPixelFormat getDDPixelformat() {
      return this.pixelFormat;
   }

   public void setDDSCaps2(DDSCaps2 var1) throws NullPointerException {
      if (var1 == null) {
         throw new NullPointerException("DDSCaps can't be null. DDSurfaceDesc2 needs a valid DDSCaps2.");
      } else {
         this.caps2 = var1;
      }
   }

   public DDSCaps2 getDDSCaps2() {
      return this.caps2;
   }
}
