package zombie.asset;

import zombie.fileSystem.FileSystem;
import zombie.fileSystem.IFile;
import zombie.fileSystem.IFileTask2Callback;

final class AssetTask_LoadFromFileAsync extends AssetTask implements IFileTask2Callback {
   int m_async_op = -1;
   boolean bStream;

   AssetTask_LoadFromFileAsync(Asset var1, boolean var2) {
      super(var1);
      this.bStream = var2;
   }

   public void execute() {
      FileSystem var1 = this.m_asset.getAssetManager().getOwner().getFileSystem();
      int var2 = 4 | (this.bStream ? 16 : 1);
      this.m_async_op = var1.openAsync(var1.getDefaultDevice(), this.m_asset.getPath().m_path, var2, this);
   }

   public void cancel() {
      FileSystem var1 = this.m_asset.getAssetManager().getOwner().getFileSystem();
      var1.cancelAsync(this.m_async_op);
      this.m_async_op = -1;
   }

   public void onFileTaskFinished(IFile var1, Object var2) {
      this.m_async_op = -1;
      if (this.m_asset.m_priv.m_desired_state == Asset.State.READY) {
         if (var2 != Boolean.TRUE) {
            this.m_asset.m_priv.onLoadingFailed();
         } else if (!this.m_asset.getAssetManager().loadDataFromFile(this.m_asset, var1)) {
            this.m_asset.m_priv.onLoadingFailed();
         } else {
            this.m_asset.m_priv.onLoadingSucceeded();
         }
      }
   }
}
