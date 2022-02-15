package zombie.core.textures;

import com.evildevil.engines.bubble.texture.DDSLoader;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import zombie.ZomboidFileSystem;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.core.utils.BooleanGrid;
import zombie.core.utils.DirectBufferAllocator;
import zombie.core.utils.ImageUtils;
import zombie.core.utils.WrappedBuffer;
import zombie.core.znet.SteamFriends;
import zombie.debug.DebugOptions;
import zombie.util.list.PZArrayUtil;

public final class ImageData implements Serializable {
   private static final long serialVersionUID = -7893392091273534932L;
   public MipMapLevel data;
   private MipMapLevel[] mipMaps;
   private int height;
   private int heightHW;
   private boolean solid = true;
   private int width;
   private int widthHW;
   private int mipMapCount = -1;
   private boolean alphaPaddingDone = false;
   public BooleanGrid mask;
   private static final int BufferSize = 67108864;
   static final DDSLoader dds = new DDSLoader();
   public int id = -1;
   public static final int MIP_LEVEL_IDX_OFFSET = 0;
   private static final ThreadLocal TL_generateMipMaps = ThreadLocal.withInitial(ImageData.L_generateMipMaps::new);
   private static final ThreadLocal TL_performAlphaPadding = ThreadLocal.withInitial(ImageData.L_performAlphaPadding::new);

   public ImageData(TextureID var1, WrappedBuffer var2) {
      this.data = new MipMapLevel(var1.widthHW, var1.heightHW, var2);
      this.width = var1.width;
      this.widthHW = var1.widthHW;
      this.height = var1.height;
      this.heightHW = var1.heightHW;
      this.solid = var1.solid;
   }

   public ImageData(String var1) throws Exception {
      if (var1.contains(".txt")) {
         var1 = var1.replace(".txt", ".png");
      }

      var1 = Texture.processFilePath(var1);
      var1 = ZomboidFileSystem.instance.getString(var1);

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               PNGDecoder var4 = new PNGDecoder(var3, false);
               this.width = var4.getWidth();
               this.height = var4.getHeight();
               this.widthHW = ImageUtils.getNextPowerOfTwoHW(this.width);
               this.heightHW = ImageUtils.getNextPowerOfTwoHW(this.height);
               this.data = new MipMapLevel(this.widthHW, this.heightHW);
               ByteBuffer var5 = this.data.getBuffer();
               var5.rewind();
               int var6 = this.widthHW * 4;
               int var7;
               int var8;
               if (this.width != this.widthHW) {
                  for(var7 = this.width * 4; var7 < this.widthHW * 4; ++var7) {
                     for(var8 = 0; var8 < this.heightHW; ++var8) {
                        var5.put(var7 + var8 * var6, (byte)0);
                     }
                  }
               }

               if (this.height != this.heightHW) {
                  for(var7 = this.height; var7 < this.heightHW; ++var7) {
                     for(var8 = 0; var8 < this.width * 4; ++var8) {
                        var5.put(var8 + var7 * var6, (byte)0);
                     }
                  }
               }

               var4.decode(this.data.getBuffer(), var6, PNGDecoder.Format.RGBA);
            } catch (Throwable var11) {
               try {
                  var3.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            var3.close();
         } catch (Throwable var12) {
            try {
               var2.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         var2.close();
      } catch (Exception var13) {
         this.dispose();
         this.width = this.height = -1;
      }

   }

   public ImageData(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      this.widthHW = ImageUtils.getNextPowerOfTwoHW(var1);
      this.heightHW = ImageUtils.getNextPowerOfTwoHW(var2);
      this.data = new MipMapLevel(this.widthHW, this.heightHW);
   }

   public ImageData(int var1, int var2, WrappedBuffer var3) {
      this.width = var1;
      this.height = var2;
      this.widthHW = ImageUtils.getNextPowerOfTwoHW(var1);
      this.heightHW = ImageUtils.getNextPowerOfTwoHW(var2);
      this.data = new MipMapLevel(this.widthHW, this.heightHW, var3);
   }

   ImageData(String var1, String var2) {
      Pcx var3 = new Pcx(var1, var2);
      this.width = var3.imageWidth;
      this.height = var3.imageHeight;
      this.widthHW = ImageUtils.getNextPowerOfTwoHW(this.width);
      this.heightHW = ImageUtils.getNextPowerOfTwoHW(this.height);
      this.data = new MipMapLevel(this.widthHW, this.heightHW);
      this.setData(var3);
      this.makeTransp((byte)var3.palette[762], (byte)var3.palette[763], (byte)var3.palette[764], (byte)0);
   }

   ImageData(String var1, int[] var2) {
      Pcx var3 = new Pcx(var1, var2);
      this.width = var3.imageWidth;
      this.height = var3.imageHeight;
      this.widthHW = ImageUtils.getNextPowerOfTwoHW(this.width);
      this.heightHW = ImageUtils.getNextPowerOfTwoHW(this.height);
      this.data = new MipMapLevel(this.widthHW, this.heightHW);
      this.setData(var3);
      this.makeTransp((byte)var3.palette[762], (byte)var3.palette[763], (byte)var3.palette[764], (byte)0);
   }

   public ImageData(BufferedInputStream var1, boolean var2, Texture.PZFileformat var3) {
      if (var3 == Texture.PZFileformat.DDS) {
         RenderThread.invokeOnRenderContext(() -> {
            this.id = dds.loadDDSFile(var1);
         });
         this.width = DDSLoader.lastWid;
         this.height = DDSLoader.lastHei;
         this.widthHW = ImageUtils.getNextPowerOfTwoHW(this.width);
         this.heightHW = ImageUtils.getNextPowerOfTwoHW(this.height);
      }

   }

   public ImageData(InputStream var1, boolean var2) throws Exception {
      Object var3 = null;
      PNGDecoder var4 = new PNGDecoder(var1, var2);
      this.width = var4.getWidth();
      this.height = var4.getHeight();
      this.widthHW = ImageUtils.getNextPowerOfTwoHW(this.width);
      this.heightHW = ImageUtils.getNextPowerOfTwoHW(this.height);
      this.data = new MipMapLevel(this.widthHW, this.heightHW);
      this.data.rewind();
      var4.decode(this.data.getBuffer(), 4 * this.widthHW, PNGDecoder.Format.RGBA);
      if (var2) {
         this.mask = var4.mask;
      }

   }

   public static ImageData createSteamAvatar(long var0) {
      WrappedBuffer var2 = DirectBufferAllocator.allocate(65536);
      int var3 = SteamFriends.CreateSteamAvatar(var0, var2.getBuffer());
      if (var3 <= 0) {
         return null;
      } else {
         int var4 = var2.getBuffer().position() / (var3 * 4);
         var2.getBuffer().flip();
         ImageData var5 = new ImageData(var3, var4, var2);
         return var5;
      }
   }

   public MipMapLevel getData() {
      if (this.data == null) {
         this.data = new MipMapLevel(this.widthHW, this.heightHW, DirectBufferAllocator.allocate(67108864));
      }

      this.data.rewind();
      return this.data;
   }

   public void makeTransp(byte var1, byte var2, byte var3) {
      this.makeTransp(var1, var2, var3, (byte)0);
   }

   public void makeTransp(byte var1, byte var2, byte var3, byte var4) {
      this.solid = false;
      ByteBuffer var5 = this.data.getBuffer();
      var5.rewind();
      int var10 = this.widthHW * 4;

      for(int var11 = 0; var11 < this.heightHW; ++var11) {
         int var9 = var5.position();

         for(int var12 = 0; var12 < this.widthHW; ++var12) {
            byte var6 = var5.get();
            byte var7 = var5.get();
            byte var8 = var5.get();
            if (var6 == var1 && var7 == var2 && var8 == var3) {
               var5.put(var4);
            } else {
               var5.get();
            }

            if (var12 == this.width) {
               var5.position(var9 + var10);
               break;
            }
         }

         if (var11 == this.height) {
            break;
         }
      }

      var5.rewind();
   }

   public void setData(BufferedImage var1) {
      if (var1 != null) {
         this.setData(var1.getData());
      }

   }

   public void setData(Raster var1) {
      if (var1 == null) {
         (new Exception()).printStackTrace();
      } else {
         this.width = var1.getWidth();
         this.height = var1.getHeight();
         if (this.width <= this.widthHW && this.height <= this.heightHW) {
            int[] var2 = var1.getPixels(0, 0, this.width, this.height, (int[])null);
            ByteBuffer var3 = this.data.getBuffer();
            var3.rewind();
            int var4 = 0;
            int var5 = var3.position();
            int var6 = this.widthHW * 4;

            for(int var7 = 0; var7 < var2.length; ++var7) {
               ++var4;
               if (var4 > this.width) {
                  var3.position(var5 + var6);
                  var5 = var3.position();
                  var4 = 1;
               }

               var3.put((byte)var2[var7]);
               ++var7;
               var3.put((byte)var2[var7]);
               ++var7;
               var3.put((byte)var2[var7]);
               ++var7;
               var3.put((byte)var2[var7]);
            }

            var3.rewind();
            this.solid = false;
         } else {
            (new Exception()).printStackTrace();
         }
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.data = new MipMapLevel(this.widthHW, this.heightHW);
      ByteBuffer var2 = this.data.getBuffer();

      for(int var3 = 0; var3 < this.widthHW * this.heightHW; ++var3) {
         var2.put(var1.readByte()).put(var1.readByte()).put(var1.readByte()).put(var1.readByte());
      }

      var2.flip();
   }

   private void setData(Pcx var1) {
      this.width = var1.imageWidth;
      this.height = var1.imageHeight;
      if (this.width <= this.widthHW && this.height <= this.heightHW) {
         ByteBuffer var2 = this.data.getBuffer();
         var2.rewind();
         int var3 = 0;
         int var4 = var2.position();
         int var5 = this.widthHW * 4;

         for(int var6 = 0; var6 < this.heightHW * this.widthHW * 3; ++var6) {
            ++var3;
            if (var3 > this.width) {
               var4 = var2.position();
               var3 = 1;
            }

            var2.put(var1.imageData[var6]);
            ++var6;
            var2.put(var1.imageData[var6]);
            ++var6;
            var2.put(var1.imageData[var6]);
            var2.put((byte)-1);
         }

         var2.rewind();
         this.solid = false;
      } else {
         (new Exception()).printStackTrace();
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      ByteBuffer var2 = this.data.getBuffer();
      var2.rewind();

      for(int var3 = 0; var3 < this.widthHW * this.heightHW; ++var3) {
         var1.writeByte(var2.get());
         var1.writeByte(var2.get());
         var1.writeByte(var2.get());
         var1.writeByte(var2.get());
      }

   }

   public int getHeight() {
      return this.height;
   }

   public int getHeightHW() {
      return this.heightHW;
   }

   public boolean isSolid() {
      return this.solid;
   }

   public int getWidth() {
      return this.width;
   }

   public int getWidthHW() {
      return this.widthHW;
   }

   public int getMipMapCount() {
      if (this.data == null) {
         return 0;
      } else {
         if (this.mipMapCount < 0) {
            this.mipMapCount = calculateNumMips(this.widthHW, this.heightHW);
         }

         return this.mipMapCount;
      }
   }

   public MipMapLevel getMipMapData(int var1) {
      if (this.data != null && !this.alphaPaddingDone) {
         this.performAlphaPadding();
      }

      if (var1 == 0) {
         return this.getData();
      } else {
         if (this.mipMaps == null) {
            this.generateMipMaps();
         }

         int var2 = var1 - 1;
         MipMapLevel var3 = this.mipMaps[var2];
         var3.rewind();
         return var3;
      }
   }

   public void initMipMaps() {
      int var1 = this.getMipMapCount();
      int var2 = PZMath.min(0, var1 - 1);
      int var3 = var1;

      for(int var4 = var2; var4 < var3; ++var4) {
         this.getMipMapData(var4);
      }

   }

   public void dispose() {
      if (this.data != null) {
         this.data.dispose();
         this.data = null;
      }

      if (this.mipMaps != null) {
         for(int var1 = 0; var1 < this.mipMaps.length; ++var1) {
            this.mipMaps[var1].dispose();
            this.mipMaps[var1] = null;
         }

         this.mipMaps = null;
      }

   }

   private void generateMipMaps() {
      this.mipMapCount = calculateNumMips(this.widthHW, this.heightHW);
      int var1 = this.mipMapCount - 1;
      this.mipMaps = new MipMapLevel[var1];
      MipMapLevel var2 = this.getData();
      int var3 = this.widthHW;
      int var4 = this.heightHW;
      MipMapLevel var5 = var2;
      int var8 = getNextMipDimension(var3);
      int var9 = getNextMipDimension(var4);

      for(int var10 = 0; var10 < var1; ++var10) {
         MipMapLevel var11 = new MipMapLevel(var8, var9);
         if (var10 < 2) {
            this.scaleMipLevelMaxAlpha(var5, var11, var10);
         } else {
            this.scaleMipLevelAverage(var5, var11, var10);
         }

         this.performAlphaPadding(var11);
         this.mipMaps[var10] = var11;
         var5 = var11;
         var8 = getNextMipDimension(var8);
         var9 = getNextMipDimension(var9);
      }

   }

   private void scaleMipLevelMaxAlpha(MipMapLevel var1, MipMapLevel var2, int var3) {
      ImageData.L_generateMipMaps var4 = (ImageData.L_generateMipMaps)TL_generateMipMaps.get();
      ByteBuffer var5 = var2.getBuffer();
      var5.rewind();
      int var6 = var1.width;
      int var7 = var1.height;
      ByteBuffer var8 = var1.getBuffer();
      int var9 = var2.width;
      int var10 = var2.height;

      for(int var11 = 0; var11 < var10; ++var11) {
         for(int var12 = 0; var12 < var9; ++var12) {
            int[] var13 = var4.pixelBytes;
            int[] var14 = var4.originalPixel;
            int[] var15 = var4.resultPixelBytes;
            getPixelClamped(var8, var6, var7, var12 * 2, var11 * 2, var14);
            byte var16;
            if (var14[3] > 0) {
               PZArrayUtil.arrayCopy((int[])var15, (int[])var14, 0, 4);
               var16 = 1;
            } else {
               PZArrayUtil.arraySet(var15, 0);
               var16 = 0;
            }

            int var17 = var16 + this.sampleNeighborPixelDiscard(var8, var6, var7, var12 * 2 + 1, var11 * 2, var13, var15);
            var17 += this.sampleNeighborPixelDiscard(var8, var6, var7, var12 * 2, var11 * 2 + 1, var13, var15);
            var17 += this.sampleNeighborPixelDiscard(var8, var6, var7, var12 * 2 + 1, var11 * 2 + 1, var13, var15);
            if (var17 > 0) {
               var15[0] /= var17;
               var15[1] /= var17;
               var15[2] /= var17;
               var15[3] /= var17;
               if (DebugOptions.instance.IsoSprite.WorldMipmapColors.getValue()) {
                  setMipmapDebugColors(var3, var15);
               }
            }

            setPixel(var5, var9, var10, var12, var11, var15);
         }
      }

   }

   private void scaleMipLevelAverage(MipMapLevel var1, MipMapLevel var2, int var3) {
      ImageData.L_generateMipMaps var4 = (ImageData.L_generateMipMaps)TL_generateMipMaps.get();
      ByteBuffer var5 = var2.getBuffer();
      var5.rewind();
      int var6 = var1.width;
      int var7 = var1.height;
      ByteBuffer var8 = var1.getBuffer();
      int var9 = var2.width;
      int var10 = var2.height;

      for(int var11 = 0; var11 < var10; ++var11) {
         for(int var12 = 0; var12 < var9; ++var12) {
            int[] var13 = var4.resultPixelBytes;
            byte var14 = 1;
            getPixelClamped(var8, var6, var7, var12 * 2, var11 * 2, var13);
            int var15 = var14 + getPixelDiscard(var8, var6, var7, var12 * 2 + 1, var11 * 2, var13);
            var15 += getPixelDiscard(var8, var6, var7, var12 * 2, var11 * 2 + 1, var13);
            var15 += getPixelDiscard(var8, var6, var7, var12 * 2 + 1, var11 * 2 + 1, var13);
            var13[0] /= var15;
            var13[1] /= var15;
            var13[2] /= var15;
            var13[3] /= var15;
            if (var13[3] != 0 && DebugOptions.instance.IsoSprite.WorldMipmapColors.getValue()) {
               setMipmapDebugColors(var3, var13);
            }

            setPixel(var5, var9, var10, var12, var11, var13);
         }
      }

   }

   public static int calculateNumMips(int var0, int var1) {
      int var2 = calculateNumMips(var0);
      int var3 = calculateNumMips(var1);
      return PZMath.max(var2, var3);
   }

   private static int calculateNumMips(int var0) {
      int var1 = 0;

      for(int var2 = var0; var2 > 0; ++var1) {
         var2 >>= 1;
      }

      return var1;
   }

   private void performAlphaPadding() {
      MipMapLevel var1 = this.data;
      if (var1 != null && var1.data != null) {
         this.performAlphaPadding(var1);
         this.alphaPaddingDone = true;
      }
   }

   private void performAlphaPadding(MipMapLevel var1) {
      ImageData.L_performAlphaPadding var2 = (ImageData.L_performAlphaPadding)TL_performAlphaPadding.get();
      ByteBuffer var3 = var1.getBuffer();
      int var4 = var1.width;
      int var5 = var1.height;

      for(int var6 = 0; var6 < var5; ++var6) {
         for(int var7 = 0; var7 < var4; ++var7) {
            int var8 = (var6 * var4 + var7) * 4;
            int var9 = var3.get(var8 + 3) & 255;
            if (var9 != 255 && var9 == 0) {
               int[] var10 = getPixelClamped(var3, var4, var5, var7, var6, var2.pixelRGBA);
               int[] var11 = var2.newPixelRGBA;
               PZArrayUtil.arraySet(var11, 0);
               var11[3] = var10[3];
               byte var12 = 0;
               int var13 = var12 + this.sampleNeighborPixelDiscard(var3, var4, var5, var7 - 1, var6, var2.pixelRGBA_neighbor, var11);
               var13 += this.sampleNeighborPixelDiscard(var3, var4, var5, var7, var6 - 1, var2.pixelRGBA_neighbor, var11);
               var13 += this.sampleNeighborPixelDiscard(var3, var4, var5, var7 - 1, var6 - 1, var2.pixelRGBA_neighbor, var11);
               var13 += this.sampleNeighborPixelDiscard(var3, var4, var5, var7 + 1, var6, var2.pixelRGBA_neighbor, var11);
               var13 += this.sampleNeighborPixelDiscard(var3, var4, var5, var7, var6 + 1, var2.pixelRGBA_neighbor, var11);
               var13 += this.sampleNeighborPixelDiscard(var3, var4, var5, var7 + 1, var6 + 1, var2.pixelRGBA_neighbor, var11);
               if (var13 > 0) {
                  var11[0] /= var13;
                  var11[1] /= var13;
                  var11[2] /= var13;
                  var11[3] = var10[3];
                  setPixel(var3, var4, var5, var7, var6, var11);
               }
            }
         }
      }

   }

   private int sampleNeighborPixelDiscard(ByteBuffer var1, int var2, int var3, int var4, int var5, int[] var6, int[] var7) {
      if (var4 >= 0 && var4 < var2 && var5 >= 0 && var5 < var3) {
         getPixelClamped(var1, var2, var3, var4, var5, var6);
         if (var6[3] > 0) {
            var7[0] += var6[0];
            var7[1] += var6[1];
            var7[2] += var6[2];
            var7[3] += var6[3];
            return 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public static int getPixelDiscard(ByteBuffer var0, int var1, int var2, int var3, int var4, int[] var5) {
      if (var3 >= 0 && var3 < var1 && var4 >= 0 && var4 < var2) {
         int var6 = (var3 + var4 * var1) * 4;
         var5[0] += var0.get(var6) & 255;
         var5[1] += var0.get(var6 + 1) & 255;
         var5[2] += var0.get(var6 + 2) & 255;
         var5[3] += var0.get(var6 + 3) & 255;
         return 1;
      } else {
         return 0;
      }
   }

   public static int[] getPixelClamped(ByteBuffer var0, int var1, int var2, int var3, int var4, int[] var5) {
      var3 = PZMath.clamp(var3, 0, var1 - 1);
      var4 = PZMath.clamp(var4, 0, var2 - 1);
      int var6 = (var3 + var4 * var1) * 4;
      var5[0] = var0.get(var6) & 255;
      var5[1] = var0.get(var6 + 1) & 255;
      var5[2] = var0.get(var6 + 2) & 255;
      var5[3] = var0.get(var6 + 3) & 255;
      return var5;
   }

   public static void setPixel(ByteBuffer var0, int var1, int var2, int var3, int var4, int[] var5) {
      int var6 = (var3 + var4 * var1) * 4;
      var0.put(var6, (byte)(var5[0] & 255));
      var0.put(var6 + 1, (byte)(var5[1] & 255));
      var0.put(var6 + 2, (byte)(var5[2] & 255));
      var0.put(var6 + 3, (byte)(var5[3] & 255));
   }

   public static int getNextMipDimension(int var0) {
      if (var0 > 1) {
         var0 >>= 1;
      }

      return var0;
   }

   private static void setMipmapDebugColors(int var0, int[] var1) {
      switch(var0) {
      case 0:
         var1[0] = 255;
         var1[1] = 0;
         var1[2] = 0;
         break;
      case 1:
         var1[0] = 0;
         var1[1] = 255;
         var1[2] = 0;
         break;
      case 2:
         var1[0] = 0;
         var1[1] = 0;
         var1[2] = 255;
         break;
      case 3:
         var1[0] = 255;
         var1[1] = 255;
         var1[2] = 0;
         break;
      case 4:
         var1[0] = 255;
         var1[1] = 0;
         var1[2] = 255;
         break;
      case 5:
         var1[0] = 0;
         var1[1] = 0;
         var1[2] = 0;
         break;
      case 6:
         var1[0] = 255;
         var1[1] = 255;
         var1[2] = 255;
         break;
      case 7:
         var1[0] = 128;
         var1[1] = 128;
         var1[2] = 128;
      }

   }

   private static final class L_generateMipMaps {
      final int[] pixelBytes = new int[4];
      final int[] originalPixel = new int[4];
      final int[] resultPixelBytes = new int[4];
   }

   static final class L_performAlphaPadding {
      final int[] pixelRGBA = new int[4];
      final int[] newPixelRGBA = new int[4];
      final int[] pixelRGBA_neighbor = new int[4];
   }
}
