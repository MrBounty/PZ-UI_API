package com.evildevil.engines.bubble.texture;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.core.textures.Texture;
import zombie.core.textures.TexturePackPage;
import zombie.debug.DebugLog;

public final class DDSLoader implements DDSurface {
   private final String DDS_IDENTIFIER = "DDS ";
   private final int DDS_HEADER_SIZE = 128;
   private final int DDS_DESC2_RESERVED_1 = 44;
   private final int DDS_DESC2_RESERVED_2 = 4;
   private final int DDS_CAPS2_RESERVED = 8;
   private final int DEFAULT_DXT_BLOCKSIZE = 16;
   private final int DXT1_BLOCKSIZE = 8;
   private final DDSurfaceDesc2 ddsDesc2 = new DDSurfaceDesc2();
   private static ByteBuffer ddsHeader = null;
   private BufferedInputStream ddsFileChannel = null;
   static ByteBuffer imageData = null;
   static ByteBuffer imageData2 = null;
   public static int lastWid = 0;
   public static int lastHei = 0;

   public int loadDDSFile(String var1) {
      File var2 = new File(var1);

      try {
         new FileInputStream(var2);
         if (this.ddsFileChannel == null) {
            throw new NullPointerException("ddsFileChannel couldn't be null!");
         }
      } catch (FileNotFoundException var4) {
         var4.printStackTrace();
      }

      if (ddsHeader == null) {
         ddsHeader = ByteBuffer.allocate(128);
      }

      this.readFileHeader();
      int var3 = this.readFileData();
      return var3;
   }

   public int loadDDSFile(BufferedInputStream var1) {
      this.ddsFileChannel = var1;
      if (this.ddsFileChannel == null) {
         throw new NullPointerException("ddsFileChannel couldn't be null!");
      } else {
         if (ddsHeader == null) {
            ddsHeader = ByteBuffer.allocate(128);
         }

         ddsHeader.rewind();
         this.readFileHeader();
         int var2 = this.readFileData();
         return var2;
      }
   }

   private void readFileHeader() {
      try {
         try {
            this.ddsFileChannel.read(ddsHeader.array());
            this.ddsDesc2.setIdentifier((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            this.ddsDesc2.setSize((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            this.ddsDesc2.setFlags((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            this.ddsDesc2.setHeight((long)TexturePackPage.readInt(ddsHeader));
            this.ddsDesc2.setWidth((long)TexturePackPage.readInt(ddsHeader));
            this.ddsDesc2.setPitchOrLinearSize((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            this.ddsDesc2.setDepth((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            this.ddsDesc2.setMipMapCount((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            ddsHeader.position(ddsHeader.position() + 44);
            DDPixelFormat var1 = this.ddsDesc2.getDDPixelformat();
            var1.setSize((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setFlags((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setFourCC((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setRGBBitCount((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setRBitMask((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setGBitMask((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setBBitMask((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var1.setRGBAlphaBitMask((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            DDSCaps2 var2 = this.ddsDesc2.getDDSCaps2();
            var2.setCaps1((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            var2.setCaps2((long)TexturePackPage.readInt(ddsHeader) & 4294967295L);
            ddsHeader.position(ddsHeader.position() + 8);
            ddsHeader.position(ddsHeader.position() + 4);
         } catch (BufferUnderflowException var8) {
            var8.printStackTrace();
         } catch (TextureFormatException var9) {
            var9.printStackTrace();
         } catch (IOException var10) {
            var10.printStackTrace();
         }

      } finally {
         ;
      }
   }

   private int readFileData() {
      DDPixelFormat var1 = this.ddsDesc2.getDDPixelformat();
      boolean var2 = false;
      char var3 = 0;
      int var6;
      if (var1.isCompressed && var1.getFourCCString().equalsIgnoreCase("DXT1")) {
         var6 = this.calculateSize(8);
         var3 = '菱';
      } else {
         var6 = this.calculateSize(16);
         if (var1.getFourCCString().equalsIgnoreCase("DXT3")) {
            var3 = '菲';
         } else if (var1.getFourCCString().equals("DXT5")) {
            var3 = '菳';
         }
      }

      if (imageData == null) {
         imageData = ByteBuffer.allocate(4194304);
      }

      imageData.rewind();

      try {
         this.ddsFileChannel.read(imageData.array(), 0, (int)this.ddsDesc2.pitchOrLinearSize);
         imageData.flip();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      if (imageData2 == null) {
         imageData2 = ByteBuffer.allocateDirect(4194304);
      }

      imageData2.clear();
      imageData2.put(imageData.array(), 0, (int)this.ddsDesc2.pitchOrLinearSize);
      imageData2.flip();
      lastWid = (int)this.ddsDesc2.width;
      lastHei = (int)this.ddsDesc2.height;
      int var4 = GL11.glGenTextures();
      Texture.lastTextureID = var4;
      GL11.glBindTexture(3553, var4);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10242, 10497);
      GL11.glTexParameteri(3553, 10243, 10497);
      GL13.glCompressedTexImage2D(3553, 0, var3, (int)this.ddsDesc2.width, (int)this.ddsDesc2.height, 0, imageData2);
      return var4;
   }

   private int calculateSize(int var1) {
      double var2 = Math.ceil((double)(this.ddsDesc2.width / 4L)) * Math.ceil((double)(this.ddsDesc2.height / 4L)) * (double)var1;
      return (int)var2;
   }

   public void debugInfo() {
      DDPixelFormat var1 = this.ddsDesc2.getDDPixelformat();
      DDSCaps2 var2 = this.ddsDesc2.getDDSCaps2();
      DebugLog.log("\nDDSURFACEDESC2:");
      DebugLog.log("----------------------------------------");
      DebugLog.log("SIZE: " + this.ddsDesc2.size);
      DebugLog.log("FLAGS: " + this.ddsDesc2.flags);
      DebugLog.log("HEIGHT: " + this.ddsDesc2.height);
      DebugLog.log("WIDTH: " + this.ddsDesc2.width);
      DebugLog.log("PITCH_OR_LINEAR_SIZE: " + this.ddsDesc2.pitchOrLinearSize);
      DebugLog.log("DEPTH: " + this.ddsDesc2.depth);
      DebugLog.log("MIP_MAP_COUNT: " + this.ddsDesc2.mipMapCount);
      DebugLog.log("\nDDPIXELFORMAT of DDSURFACEDESC2:");
      DebugLog.log("----------------------------------------");
      DebugLog.log("SIZE :" + var1.size);
      DebugLog.log("FLAGS: " + var1.flags);
      DebugLog.log("FOUR_CC: " + var1.getFourCCString());
      DebugLog.log("RGB_BIT_COUNT: " + var1.rgbBitCount);
      DebugLog.log("R_BIT_MASK: " + var1.rBitMask);
      DebugLog.log("G_BIT_MASK: " + var1.gBitMask);
      DebugLog.log("B_BIT_MASK: " + var1.bBitMask);
      DebugLog.log("RGB_ALPHA_BIT_MASK: " + var1.rgbAlphaBitMask);
      DebugLog.log("\nDDSCAPS of DDSURFACEDESC2");
      DebugLog.log("----------------------------------------");
      DebugLog.log("CAPS1: " + var2.caps1);
      DebugLog.log("CAPS2: " + var2.caps2);
   }
}
