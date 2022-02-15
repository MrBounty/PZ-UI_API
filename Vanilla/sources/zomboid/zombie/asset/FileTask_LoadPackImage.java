package zombie.asset;

import java.io.InputStream;
import zombie.core.textures.ImageData;
import zombie.core.textures.TextureIDAssetManager;
import zombie.fileSystem.DeviceList;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

public final class FileTask_LoadPackImage extends FileTask {
   String m_pack_name;
   String m_image_name;
   boolean bMask;
   int m_flags;

   public FileTask_LoadPackImage(String var1, String var2, FileSystem var3, IFileTaskCallback var4) {
      super(var3, var4);
      this.m_pack_name = var1;
      this.m_image_name = var2;
      this.bMask = var3.getTexturePackAlpha(var1, var2);
      this.m_flags = var3.getTexturePackFlags(var1);
   }

   public void done() {
   }

   public Object call() throws Exception {
      TextureIDAssetManager.instance.waitFileTask();
      DeviceList var1 = this.m_file_system.getTexturePackDevice(this.m_pack_name);
      InputStream var2 = this.m_file_system.openStream(var1, this.m_image_name);

      ImageData var4;
      try {
         ImageData var3 = new ImageData(var2, this.bMask);
         if ((this.m_flags & 64) != 0) {
            var3.initMipMaps();
         }

         var4 = var3;
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

      return var4;
   }
}
