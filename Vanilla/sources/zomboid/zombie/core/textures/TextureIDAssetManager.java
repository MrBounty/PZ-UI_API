package zombie.core.textures;

import java.util.Objects;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.asset.FileTask_LoadImageData;
import zombie.asset.FileTask_LoadPackImage;
import zombie.core.opengl.RenderThread;
import zombie.core.utils.DirectBufferAllocator;
import zombie.fileSystem.FileSystem;

public final class TextureIDAssetManager extends AssetManager {
   public static final TextureIDAssetManager instance = new TextureIDAssetManager();

   protected void startLoading(Asset var1) {
      TextureID var2 = (TextureID)var1;
      FileSystem var3 = this.getOwner().getFileSystem();
      if (var2.assetParams != null && var2.assetParams.subTexture != null) {
         FileSystem.SubTexture var8 = var2.assetParams.subTexture;
         FileTask_LoadPackImage var7 = new FileTask_LoadPackImage(var8.m_pack_name, var8.m_page_name, var3, (var2x) -> {
            this.onFileTaskFinished(var1, var2x);
         });
         var7.setPriority(7);
         AssetTask_RunFileTask var6 = new AssetTask_RunFileTask(var7, var1);
         this.setTask(var1, var6);
         var6.execute();
      } else {
         FileTask_LoadImageData var4 = new FileTask_LoadImageData(var1.getPath().getPath(), var3, (var2x) -> {
            this.onFileTaskFinished(var1, var2x);
         });
         var4.setPriority(7);
         AssetTask_RunFileTask var5 = new AssetTask_RunFileTask(var4, var1);
         this.setTask(var1, var5);
         var5.execute();
      }

   }

   protected void unloadData(Asset var1) {
      TextureID var2 = (TextureID)var1;
      if (!var2.isDestroyed()) {
         Objects.requireNonNull(var2);
         RenderThread.invokeOnRenderContext(var2::destroy);
      }
   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new TextureID(var1, this, (TextureID.TextureIDAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }

   private void onFileTaskFinished(Asset var1, Object var2) {
      TextureID var3 = (TextureID)var1;
      if (var2 instanceof ImageData) {
         var3.setImageData((ImageData)var2);
         this.onLoadingSucceeded(var1);
      } else {
         this.onLoadingFailed(var1);
      }

   }

   public void waitFileTask() {
      while(DirectBufferAllocator.getBytesAllocated() > 52428800L) {
         try {
            Thread.sleep(20L);
         } catch (InterruptedException var2) {
         }
      }

   }
}
