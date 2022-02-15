package zombie.fileSystem;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import zombie.ZomboidFileSystem;
import zombie.core.textures.TexturePackPage;

public final class TexturePackDevice implements IFileDevice {
   String m_name;
   String m_filename;
   final ArrayList m_pages = new ArrayList();
   final HashMap m_pagemap = new HashMap();
   final HashMap m_submap = new HashMap();
   int m_textureFlags;

   public TexturePackDevice(String var1, int var2) {
      this.m_name = var1;
      this.m_filename = ZomboidFileSystem.instance.getString("media/texturepacks/" + var1 + ".pack");
      this.m_textureFlags = var2;
   }

   public IFile createFile(IFile var1) {
      return null;
   }

   public void destroyFile(IFile var1) {
   }

   public InputStream createStream(String var1, InputStream var2) throws IOException {
      this.initMetaData();
      return new TexturePackDevice.TexturePackInputStream(var1, this);
   }

   public void destroyStream(InputStream var1) {
      if (var1 instanceof TexturePackDevice.TexturePackInputStream) {
      }

   }

   public String name() {
      return this.m_name;
   }

   public void getSubTextureInfo(FileSystem.TexturePackTextures var1) throws IOException {
      this.initMetaData();
      Iterator var2 = this.m_submap.values().iterator();

      while(var2.hasNext()) {
         TexturePackDevice.SubTexture var3 = (TexturePackDevice.SubTexture)var2.next();
         FileSystem.SubTexture var4 = new FileSystem.SubTexture(this.name(), var3.m_page.m_name, var3.m_info);
         var1.put(var3.m_info.name, var4);
      }

   }

   private void initMetaData() throws IOException {
      if (this.m_pages.isEmpty()) {
         FileInputStream var1 = new FileInputStream(this.m_filename);

         try {
            BufferedInputStream var2 = new BufferedInputStream(var1);

            try {
               TexturePackDevice.PositionInputStream var3 = new TexturePackDevice.PositionInputStream(var2);

               try {
                  int var4 = TexturePackPage.readInt((InputStream)var3);

                  for(int var5 = 0; var5 < var4; ++var5) {
                     TexturePackDevice.Page var6 = this.readPage(var3);
                     this.m_pages.add(var6);
                     this.m_pagemap.put(var6.m_name, var6);
                     Iterator var7 = var6.m_sub.iterator();

                     while(var7.hasNext()) {
                        TexturePackPage.SubTextureInfo var8 = (TexturePackPage.SubTextureInfo)var7.next();
                        this.m_submap.put(var8.name, new TexturePackDevice.SubTexture(var6, var8));
                     }
                  }
               } catch (Throwable var12) {
                  try {
                     var3.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }

                  throw var12;
               }

               var3.close();
            } catch (Throwable var13) {
               try {
                  var2.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }

               throw var13;
            }

            var2.close();
         } catch (Throwable var14) {
            try {
               var1.close();
            } catch (Throwable var9) {
               var14.addSuppressed(var9);
            }

            throw var14;
         }

         var1.close();
      }
   }

   private TexturePackDevice.Page readPage(TexturePackDevice.PositionInputStream var1) throws IOException {
      TexturePackDevice.Page var2 = new TexturePackDevice.Page();
      String var3 = TexturePackPage.ReadString(var1);
      int var4 = TexturePackPage.readInt((InputStream)var1);
      boolean var5 = TexturePackPage.readInt((InputStream)var1) != 0;
      var2.m_name = var3;
      var2.m_has_alpha = var5;

      int var6;
      for(var6 = 0; var6 < var4; ++var6) {
         String var7 = TexturePackPage.ReadString(var1);
         int var8 = TexturePackPage.readInt((InputStream)var1);
         int var9 = TexturePackPage.readInt((InputStream)var1);
         int var10 = TexturePackPage.readInt((InputStream)var1);
         int var11 = TexturePackPage.readInt((InputStream)var1);
         int var12 = TexturePackPage.readInt((InputStream)var1);
         int var13 = TexturePackPage.readInt((InputStream)var1);
         int var14 = TexturePackPage.readInt((InputStream)var1);
         int var15 = TexturePackPage.readInt((InputStream)var1);
         var2.m_sub.add(new TexturePackPage.SubTextureInfo(var8, var9, var10, var11, var12, var13, var14, var15, var7));
      }

      var2.m_png_start = var1.getPosition();
      boolean var16 = false;

      do {
         var6 = TexturePackPage.readIntByte(var1);
      } while(var6 != -559038737);

      return var2;
   }

   public boolean isAlpha(String var1) {
      TexturePackDevice.Page var2 = (TexturePackDevice.Page)this.m_pagemap.get(var1);
      return var2.m_has_alpha;
   }

   public int getTextureFlags() {
      return this.m_textureFlags;
   }

   static class TexturePackInputStream extends FileInputStream {
      TexturePackDevice m_device;

      TexturePackInputStream(String var1, TexturePackDevice var2) throws IOException {
         super(var2.m_filename);
         this.m_device = var2;
         TexturePackDevice.Page var3 = (TexturePackDevice.Page)this.m_device.m_pagemap.get(var1);
         if (var3 == null) {
            throw new FileNotFoundException();
         } else {
            this.skip(var3.m_png_start);
         }
      }
   }

   static final class SubTexture {
      final TexturePackDevice.Page m_page;
      final TexturePackPage.SubTextureInfo m_info;

      SubTexture(TexturePackDevice.Page var1, TexturePackPage.SubTextureInfo var2) {
         this.m_page = var1;
         this.m_info = var2;
      }
   }

   static final class Page {
      String m_name;
      boolean m_has_alpha = false;
      long m_png_start = -1L;
      final ArrayList m_sub = new ArrayList();
   }

   public final class PositionInputStream extends FilterInputStream {
      private long pos = 0L;
      private long mark = 0L;

      public PositionInputStream(InputStream var2) {
         super(var2);
      }

      public synchronized long getPosition() {
         return this.pos;
      }

      public synchronized int read() throws IOException {
         int var1 = super.read();
         if (var1 >= 0) {
            ++this.pos;
         }

         return var1;
      }

      public synchronized int read(byte[] var1, int var2, int var3) throws IOException {
         int var4 = super.read(var1, var2, var3);
         if (var4 > 0) {
            this.pos += (long)var4;
         }

         return var4;
      }

      public synchronized long skip(long var1) throws IOException {
         long var3 = super.skip(var1);
         if (var3 > 0L) {
            this.pos += var3;
         }

         return var3;
      }

      public synchronized void mark(int var1) {
         super.mark(var1);
         this.mark = this.pos;
      }

      public synchronized void reset() throws IOException {
         if (!this.markSupported()) {
            throw new IOException("Mark not supported.");
         } else {
            super.reset();
            this.pos = this.mark;
         }
      }
   }
}
