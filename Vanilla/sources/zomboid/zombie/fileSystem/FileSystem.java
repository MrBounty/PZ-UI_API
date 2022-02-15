package zombie.fileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import zombie.core.textures.TexturePackPage;

public abstract class FileSystem {
   public static final int INVALID_ASYNC = -1;

   public abstract boolean mount(IFileDevice var1);

   public abstract boolean unMount(IFileDevice var1);

   public abstract IFile open(DeviceList var1, String var2, int var3);

   public abstract void close(IFile var1);

   public abstract int openAsync(DeviceList var1, String var2, int var3, IFileTask2Callback var4);

   public abstract void closeAsync(IFile var1, IFileTask2Callback var2);

   public abstract void cancelAsync(int var1);

   public abstract InputStream openStream(DeviceList var1, String var2) throws IOException;

   public abstract void closeStream(InputStream var1);

   public abstract int runAsync(FileTask var1);

   public abstract void updateAsyncTransactions();

   public abstract boolean hasWork();

   public abstract DeviceList getDefaultDevice();

   public abstract void mountTexturePack(String var1, FileSystem.TexturePackTextures var2, int var3);

   public abstract DeviceList getTexturePackDevice(String var1);

   public abstract int getTexturePackFlags(String var1);

   public abstract boolean getTexturePackAlpha(String var1, String var2);

   public static final class TexturePackTextures extends HashMap {
   }

   public static final class SubTexture {
      public String m_pack_name;
      public String m_page_name;
      public TexturePackPage.SubTextureInfo m_info;

      public SubTexture(String var1, String var2, TexturePackPage.SubTextureInfo var3) {
         this.m_pack_name = var1;
         this.m_page_name = var2;
         this.m_info = var3;
      }
   }
}
