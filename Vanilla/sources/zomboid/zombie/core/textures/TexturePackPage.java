package zombie.core.textures;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public final class TexturePackPage {
   public static HashMap FoundTextures = new HashMap();
   public static final HashMap subTextureMap = new HashMap();
   public static final HashMap subTextureMap2 = new HashMap();
   public static final HashMap texturePackPageMap = new HashMap();
   public static final HashMap TexturePackPageNameMap = new HashMap();
   public final HashMap subTextures = new HashMap();
   public Texture tex = null;
   static ByteBuffer SliceBuffer = null;
   static boolean bHasCache = false;
   static int percent = 0;
   public static int chl1 = 0;
   public static int chl2 = 0;
   public static int chl3 = 0;
   public static int chl4 = 0;
   static StringBuilder v = new StringBuilder(50);
   public static ArrayList TempSubTextureInfo = new ArrayList();
   public static ArrayList tempFilenameCheck = new ArrayList();
   public static boolean bIgnoreWorldItemTextures = true;

   public static void LoadDir(String var0) throws URISyntaxException {
   }

   public static void searchFolders(File var0) {
   }

   public static Texture getTexture(String var0) {
      if (var0.contains(".png")) {
         return Texture.getSharedTexture(var0);
      } else {
         return subTextureMap.containsKey(var0) ? (Texture)subTextureMap.get(var0) : null;
      }
   }

   public static int readInt(InputStream var0) throws EOFException, IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      chl1 = var1;
      chl2 = var2;
      chl3 = var3;
      chl4 = var4;
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   public static int readInt(ByteBuffer var0) throws EOFException, IOException {
      byte var1 = var0.get();
      byte var2 = var0.get();
      byte var3 = var0.get();
      byte var4 = var0.get();
      chl1 = var1;
      chl2 = var2;
      chl3 = var3;
      chl4 = var4;
      return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
   }

   public static int readIntByte(InputStream var0) throws EOFException, IOException {
      int var1 = chl2;
      int var2 = chl3;
      int var3 = chl4;
      int var4 = var0.read();
      chl1 = var1;
      chl2 = var2;
      chl3 = var3;
      chl4 = var4;
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   public static String ReadString(InputStream var0) throws IOException {
      v.setLength(0);
      int var1 = readInt(var0);

      for(int var2 = 0; var2 < var1; ++var2) {
         v.append((char)var0.read());
      }

      return v.toString();
   }

   public void loadFromPackFileDDS(BufferedInputStream var1) throws IOException {
      String var2 = ReadString(var1);
      tempFilenameCheck.add(var2);
      int var3 = readInt((InputStream)var1);
      boolean var4 = readInt((InputStream)var1) != 0;
      TempSubTextureInfo.clear();

      int var5;
      for(var5 = 0; var5 < var3; ++var5) {
         String var6 = ReadString(var1);
         int var7 = readInt((InputStream)var1);
         int var8 = readInt((InputStream)var1);
         int var9 = readInt((InputStream)var1);
         int var10 = readInt((InputStream)var1);
         int var11 = readInt((InputStream)var1);
         int var12 = readInt((InputStream)var1);
         int var13 = readInt((InputStream)var1);
         int var14 = readInt((InputStream)var1);
         if (var6.contains("ZombieWalk") && var6.contains("BobZ_")) {
            TempSubTextureInfo.add(new TexturePackPage.SubTextureInfo(var7, var8, var9, var10, var11, var12, var13, var14, var6));
         }
      }

      if (TempSubTextureInfo.isEmpty()) {
         boolean var16 = false;

         do {
            var5 = readIntByte(var1);
         } while(var5 != -559038737);

      } else {
         Texture var15 = new Texture(var2, var1, var4, Texture.PZFileformat.DDS);

         int var17;
         for(var17 = 0; var17 < TempSubTextureInfo.size(); ++var17) {
            TexturePackPage.SubTextureInfo var19 = (TexturePackPage.SubTextureInfo)TempSubTextureInfo.get(var17);
            Texture var20 = var15.split(var19.x, var19.y, var19.w, var19.h);
            var20.copyMaskRegion(var15, var19.x, var19.y, var19.w, var19.h);
            var20.setName(var19.name);
            this.subTextures.put(var19.name, var20);
            subTextureMap.put(var19.name, var20);
            var20.offsetX = (float)var19.ox;
            var20.offsetY = (float)var19.oy;
            var20.widthOrig = var19.fx;
            var20.heightOrig = var19.fy;
         }

         var15.mask = null;
         texturePackPageMap.put(var2, this);
         boolean var18 = false;

         do {
            var17 = readIntByte(var1);
         } while(var17 != -559038737);

      }
   }

   public void loadFromPackFile(BufferedInputStream var1) throws Exception {
      String var2 = ReadString(var1);
      tempFilenameCheck.add(var2);
      int var3 = readInt((InputStream)var1);
      boolean var4 = readInt((InputStream)var1) != 0;
      if (var4) {
         boolean var5 = false;
      }

      TempSubTextureInfo.clear();

      for(int var15 = 0; var15 < var3; ++var15) {
         String var6 = ReadString(var1);
         int var7 = readInt((InputStream)var1);
         int var8 = readInt((InputStream)var1);
         int var9 = readInt((InputStream)var1);
         int var10 = readInt((InputStream)var1);
         int var11 = readInt((InputStream)var1);
         int var12 = readInt((InputStream)var1);
         int var13 = readInt((InputStream)var1);
         int var14 = readInt((InputStream)var1);
         if (!bIgnoreWorldItemTextures || !var6.startsWith("WItem_")) {
            TempSubTextureInfo.add(new TexturePackPage.SubTextureInfo(var7, var8, var9, var10, var11, var12, var13, var14, var6));
         }
      }

      Texture var16 = new Texture(var2, var1, var4);

      int var17;
      for(var17 = 0; var17 < TempSubTextureInfo.size(); ++var17) {
         TexturePackPage.SubTextureInfo var19 = (TexturePackPage.SubTextureInfo)TempSubTextureInfo.get(var17);
         Texture var20 = var16.split(var19.x, var19.y, var19.w, var19.h);
         var20.copyMaskRegion(var16, var19.x, var19.y, var19.w, var19.h);
         var20.setName(var19.name);
         this.subTextures.put(var19.name, var20);
         subTextureMap.put(var19.name, var20);
         var20.offsetX = (float)var19.ox;
         var20.offsetY = (float)var19.oy;
         var20.widthOrig = var19.fx;
         var20.heightOrig = var19.fy;
      }

      var16.mask = null;
      texturePackPageMap.put(var2, this);
      boolean var18 = false;

      do {
         var17 = readIntByte(var1);
      } while(var17 != -559038737);

   }

   public static class SubTextureInfo {
      public int w;
      public int h;
      public int x;
      public int y;
      public int ox;
      public int oy;
      public int fx;
      public int fy;
      public String name;

      public SubTextureInfo(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, String var9) {
         this.x = var1;
         this.y = var2;
         this.w = var3;
         this.h = var4;
         this.ox = var5;
         this.oy = var6;
         this.fx = var7;
         this.fy = var8;
         this.name = var9;
      }
   }
}
