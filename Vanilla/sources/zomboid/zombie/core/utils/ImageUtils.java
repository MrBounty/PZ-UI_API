package zombie.core.utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import zombie.core.Core;
import zombie.core.textures.Texture;

public class ImageUtils {
   public static boolean USE_MIPMAP = true;

   private ImageUtils() {
   }

   public static void depureTexture(Texture var0, float var1) {
      WrappedBuffer var2 = var0.getData();
      ByteBuffer var3 = var2.getBuffer();
      var3.rewind();
      int var6 = (int)(var1 * 255.0F);
      long var7 = (long)(var0.getWidthHW() * var0.getHeightHW());

      for(int var9 = 0; (long)var9 < var7; ++var9) {
         var3.mark();
         var3.get();
         var3.get();
         var3.get();
         byte var4 = var3.get();
         int var5;
         if (var4 < 0) {
            var5 = 256 + var4;
         } else {
            var5 = var4;
         }

         if (var5 < var6) {
            var3.reset();
            var3.put((byte)0);
            var3.put((byte)0);
            var3.put((byte)0);
            var3.put((byte)0);
         }
      }

      var3.flip();
      var0.setData(var3);
      var2.dispose();
   }

   public static int getNextPowerOfTwo(int var0) {
      int var1;
      for(var1 = 2; var1 < var0; var1 += var1) {
      }

      return var1;
   }

   public static int getNextPowerOfTwoHW(int var0) {
      int var1;
      for(var1 = 2; var1 < var0; var1 += var1) {
      }

      return var1;
   }

   public static Texture getScreenShot() {
      Texture var0 = new Texture(Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight(), 0);
      IntBuffer var1 = org.lwjglx.BufferUtils.createIntBuffer(4);
      var0.bind();
      var1.rewind();
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glCopyTexImage2D(3553, 0, 6408, 0, 0, var0.getWidthHW(), var0.getHeightHW(), 0);
      return var0;
   }

   public static ByteBuffer makeTransp(ByteBuffer var0, int var1, int var2, int var3, int var4, int var5) {
      return makeTransp(var0, var1, var2, var3, 0, var4, var5);
   }

   public static ByteBuffer makeTransp(ByteBuffer var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var0.rewind();

      for(int var10 = 0; var10 < var6; ++var10) {
         for(int var11 = 0; var11 < var5; ++var11) {
            byte var7 = var0.get();
            byte var8 = var0.get();
            byte var9 = var0.get();
            if (var7 == (byte)var1 && var8 == (byte)var2 && var9 == (byte)var3) {
               var0.put((byte)var4);
            } else {
               var0.get();
            }
         }
      }

      var0.rewind();
      return var0;
   }

   public static void saveBmpImage(Texture var0, String var1) {
      saveImage(var0, var1, "bmp");
   }

   public static void saveImage(Texture var0, String var1, String var2) {
      BufferedImage var3 = new BufferedImage(var0.getWidth(), var0.getHeight(), 1);
      WritableRaster var4 = var3.getRaster();
      WrappedBuffer var5 = var0.getData();
      ByteBuffer var6 = var5.getBuffer();
      var6.rewind();

      for(int var7 = 0; var7 < var0.getHeightHW() && var7 < var0.getHeight(); ++var7) {
         for(int var8 = 0; var8 < var0.getWidthHW(); ++var8) {
            if (var8 >= var0.getWidth()) {
               var6.get();
               var6.get();
               var6.get();
               var6.get();
            } else {
               var4.setPixel(var8, var0.getHeight() - 1 - var7, new int[]{var6.get(), var6.get(), var6.get()});
               var6.get();
            }
         }
      }

      var5.dispose();

      try {
         ImageIO.write(var3, "png", new File(var1));
      } catch (IOException var9) {
      }

   }

   public static void saveJpgImage(Texture var0, String var1) {
      saveImage(var0, var1, "jpg");
   }

   public static void savePngImage(Texture var0, String var1) {
      saveImage(var0, var1, "png");
   }
}
