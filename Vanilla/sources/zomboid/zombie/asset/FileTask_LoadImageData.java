package zombie.asset;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import zombie.core.textures.ImageData;
import zombie.core.textures.TextureIDAssetManager;
import zombie.debug.DebugOptions;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

public final class FileTask_LoadImageData extends FileTask {
   String m_image_name;
   boolean bMask = false;

   public FileTask_LoadImageData(String var1, FileSystem var2, IFileTaskCallback var3) {
      super(var2, var3);
      this.m_image_name = var1;
   }

   public String getErrorMessage() {
      return this.m_image_name;
   }

   public void done() {
   }

   public Object call() throws Exception {
      TextureIDAssetManager.instance.waitFileTask();
      if (DebugOptions.instance.AssetSlowLoad.getValue()) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var9) {
         }
      }

      FileInputStream var1 = new FileInputStream(this.m_image_name);

      ImageData var3;
      try {
         BufferedInputStream var2 = new BufferedInputStream(var1);

         try {
            var3 = new ImageData(var2, this.bMask);
         } catch (Throwable var7) {
            try {
               var2.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         var2.close();
      } catch (Throwable var8) {
         try {
            var1.close();
         } catch (Throwable var5) {
            var8.addSuppressed(var5);
         }

         throw var8;
      }

      var1.close();
      return var3;
   }
}
