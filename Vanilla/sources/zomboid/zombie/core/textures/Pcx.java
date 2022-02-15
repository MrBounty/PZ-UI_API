package zombie.core.textures;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import zombie.GameWindow;
import zombie.debug.DebugLog;

public class Pcx {
   public static HashMap Cache = new HashMap();
   public byte[] imageData;
   public int imageWidth;
   public int imageHeight;
   public int[] palette;
   public int[] pic;

   public Pcx(String var1) {
   }

   public Pcx(URL var1) {
   }

   public Pcx(String var1, int[] var2) {
   }

   public Pcx(String var1, String var2) {
   }

   public Image getImage() {
      int[] var1 = new int[this.imageWidth * this.imageHeight];
      int var2 = 0;
      int var3 = 0;

      for(int var4 = 0; var4 < this.imageWidth; ++var4) {
         for(int var5 = 0; var5 < this.imageHeight; ++var5) {
            var1[var2++] = -16777216 | (this.imageData[var3++] & 255) << 16 | (this.imageData[var3++] & 255) << 8 | this.imageData[var3++] & 255;
         }
      }

      Toolkit var6 = Toolkit.getDefaultToolkit();
      return var6.createImage(new MemoryImageSource(this.imageWidth, this.imageHeight, var1, 0, this.imageWidth));
   }

   int loadPCX(URL var1) {
      try {
         InputStream var12 = var1.openStream();
         int var7 = var12.available();
         byte[] var13 = new byte[var7 + 1];
         var13[var7] = 0;

         int var14;
         for(var14 = 0; var14 < var7; ++var14) {
            var13[var14] = (byte)var12.read();
         }

         var12.close();
         if (var7 == -1) {
            return -1;
         } else {
            Pcx.pcx_t var4 = new Pcx.pcx_t(var13);
            byte[] var3 = var4.data;
            if (var4.manufacturer == '\n' && var4.version == 5 && var4.encoding == 1 && var4.bits_per_pixel == '\b' && var4.xmax < 640 && var4.ymax < 480) {
               this.palette = new int[768];

               for(var14 = 0; var14 < 768; ++var14) {
                  if (var7 - 128 - 768 + var14 < var4.data.length) {
                     this.palette[var14] = var4.data[var7 - 128 - 768 + var14] & 255;
                  }
               }

               this.imageWidth = var4.xmax + 1;
               this.imageHeight = var4.ymax + 1;
               int[] var10 = new int[(var4.ymax + 1) * (var4.xmax + 1)];
               this.pic = var10;
               int[] var11 = var10;
               var14 = 0;
               int var15 = 0;

               for(int var6 = 0; var6 <= var4.ymax; var14 += var4.xmax + 1) {
                  int var5 = 0;

                  while(var5 <= var4.xmax) {
                     byte var8 = var3[var15++];
                     int var9;
                     if ((var8 & 192) == 192) {
                        var9 = var8 & 63;
                        var8 = var3[var15++];
                     } else {
                        var9 = 1;
                     }

                     while(var9-- > 0) {
                        var11[var14 + var5++] = var8 & 255;
                     }
                  }

                  ++var6;
               }

               if (this.pic != null && this.palette != null) {
                  this.imageData = new byte[(this.imageWidth + 1) * (this.imageHeight + 1) * 3];

                  for(int var2 = 0; var2 < this.imageWidth * this.imageHeight; ++var2) {
                     this.imageData[var2 * 3] = (byte)this.palette[this.pic[var2] * 3];
                     this.imageData[var2 * 3 + 1] = (byte)this.palette[this.pic[var2] * 3 + 1];
                     this.imageData[var2 * 3 + 2] = (byte)this.palette[this.pic[var2] * 3 + 2];
                  }

                  return 1;
               } else {
                  return -1;
               }
            } else {
               DebugLog.log("Bad pcx file " + var1);
               return -1;
            }
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         return 1;
      }
   }

   int loadPCXminusPal(String var1) {
      try {
         if (Cache.containsKey(var1)) {
            Pcx var17 = (Pcx)Cache.get(var1);
            this.imageWidth = var17.imageWidth;
            this.imageHeight = var17.imageHeight;
            this.imageData = new byte[(var17.imageWidth + 1) * (var17.imageHeight + 1) * 3];

            for(int var18 = 0; var18 < var17.imageWidth * var17.imageHeight; ++var18) {
               this.imageData[var18 * 3] = (byte)this.palette[var17.pic[var18] * 3];
               this.imageData[var18 * 3 + 1] = (byte)this.palette[var17.pic[var18] * 3 + 1];
               this.imageData[var18 * 3 + 2] = (byte)this.palette[var17.pic[var18] * 3 + 2];
            }

            return 1;
         } else {
            InputStream var12 = GameWindow.class.getClassLoader().getResourceAsStream(var1);
            if (var12 == null) {
               return 0;
            } else {
               int var7 = var12.available();
               byte[] var13 = new byte[var7 + 1];
               var13[var7] = 0;

               int var14;
               for(var14 = 0; var14 < var7; ++var14) {
                  var13[var14] = (byte)var12.read();
               }

               var12.close();
               if (var7 == -1) {
                  return -1;
               } else {
                  Pcx.pcx_t var4 = new Pcx.pcx_t(var13);
                  byte[] var3 = var4.data;
                  if (var4.manufacturer == '\n' && var4.version == 5 && var4.encoding == 1 && var4.bits_per_pixel == '\b' && var4.xmax < 640 && var4.ymax < 480) {
                     this.imageWidth = var4.xmax + 1;
                     this.imageHeight = var4.ymax + 1;
                     int[] var10 = new int[(var4.ymax + 1) * (var4.xmax + 1)];
                     this.pic = var10;
                     int[] var11 = var10;
                     var14 = 0;
                     int var15 = 0;

                     for(int var6 = 0; var6 <= var4.ymax; var14 += var4.xmax + 1) {
                        int var5 = 0;

                        while(var5 <= var4.xmax) {
                           byte var8 = var3[var15++];
                           int var9;
                           if ((var8 & 192) == 192) {
                              var9 = var8 & 63;
                              var8 = var3[var15++];
                           } else {
                              var9 = 1;
                           }

                           while(var9-- > 0) {
                              var11[var14 + var5++] = var8 & 255;
                           }
                        }

                        ++var6;
                     }

                     if (this.pic != null && this.palette != null) {
                        this.imageData = new byte[(this.imageWidth + 1) * (this.imageHeight + 1) * 3];

                        for(int var2 = 0; var2 < this.imageWidth * this.imageHeight; ++var2) {
                           this.imageData[var2 * 3] = (byte)this.palette[this.pic[var2] * 3];
                           this.imageData[var2 * 3 + 1] = (byte)this.palette[this.pic[var2] * 3 + 1];
                           this.imageData[var2 * 3 + 2] = (byte)this.palette[this.pic[var2] * 3 + 2];
                        }

                        Cache.put(var1, this);
                        return 1;
                     } else {
                        return -1;
                     }
                  } else {
                     DebugLog.log("Bad pcx file " + var1);
                     return -1;
                  }
               }
            }
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         return 1;
      }
   }

   int loadPCXpal(String var1) {
      try {
         InputStream var12 = GameWindow.class.getClassLoader().getResourceAsStream(var1);
         if (var12 == null) {
            return 1;
         } else {
            int var7 = var12.available();
            byte[] var13 = new byte[var7 + 1];
            var13[var7] = 0;

            int var14;
            for(var14 = 0; var14 < var7; ++var14) {
               var13[var14] = (byte)var12.read();
            }

            var12.close();
            if (var7 == -1) {
               return -1;
            } else {
               Pcx.pcx_t var4 = new Pcx.pcx_t(var13);
               byte[] var3 = var4.data;
               if (var4.manufacturer == '\n' && var4.version == 5 && var4.encoding == 1 && var4.bits_per_pixel == '\b' && var4.xmax < 640 && var4.ymax < 480) {
                  this.palette = new int[768];

                  for(var14 = 0; var14 < 768; ++var14) {
                     if (var7 - 128 - 768 + var14 < var4.data.length) {
                        this.palette[var14] = var4.data[var7 - 128 - 768 + var14] & 255;
                     }
                  }

                  this.imageWidth = var4.xmax + 1;
                  this.imageHeight = var4.ymax + 1;
                  int[] var10 = new int[(var4.ymax + 1) * (var4.xmax + 1)];
                  this.pic = var10;
                  boolean var17 = false;
                  boolean var15 = false;
                  if (this.pic != null && this.palette != null) {
                     return 1;
                  } else {
                     return -1;
                  }
               } else {
                  DebugLog.log("Bad pcx file " + var1);
                  return -1;
               }
            }
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         return 1;
      }
   }

   private void loadPCXpal(int[] var1) {
      this.palette = var1;
   }

   class pcx_t {
      char bits_per_pixel;
      short bytes_per_line;
      char color_planes;
      byte[] data;
      char encoding;
      byte[] filler = new byte[58];
      short hres;
      short vres;
      char manufacturer;
      int[] palette = new int[48];
      short palette_type;
      char reserved;
      char version;
      short xmin;
      short ymin;
      short xmax;
      short ymax;

      pcx_t(byte[] var2) {
         this.manufacturer = (char)var2[0];
         this.version = (char)var2[1];
         this.encoding = (char)var2[2];
         this.bits_per_pixel = (char)var2[3];
         this.xmin = (short)(var2[4] + (var2[5] << 8) & 255);
         this.ymin = (short)(var2[6] + (var2[7] << 8) & 255);
         this.xmax = (short)(var2[8] + (var2[9] << 8) & 255);
         this.ymax = (short)(var2[10] + (var2[11] << 8) & 255);
         this.hres = (short)(var2[12] + (var2[13] << 8) & 255);
         this.vres = (short)(var2[14] + (var2[15] << 8) & 255);

         int var3;
         for(var3 = 0; var3 < 48; ++var3) {
            this.palette[var3] = var2[16 + var3] & 255;
         }

         this.reserved = (char)var2[64];
         this.color_planes = (char)var2[65];
         this.bytes_per_line = (short)(var2[66] + (var2[67] << 8) & 255);
         this.palette_type = (short)(var2[68] + (var2[69] << 8) & 255);

         for(var3 = 0; var3 < 58; ++var3) {
            this.filler[var3] = var2[70 + var3];
         }

         this.data = new byte[var2.length - 128];

         for(var3 = 0; var3 < var2.length - 128; ++var3) {
            this.data[var3] = var2[128 + var3];
         }

      }
   }
}
