package zombie.asset;

import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;

public final class AssetTask_RunFileTask extends AssetTask {
   protected final FileTask m_file_task;
   int m_async_op = -1;

   public AssetTask_RunFileTask(FileTask var1, Asset var2) {
      super(var2);
      this.m_file_task = var1;
   }

   public void execute() {
      FileSystem var1 = this.m_asset.getAssetManager().getOwner().getFileSystem();
      this.m_async_op = var1.runAsync(this.m_file_task);
   }

   public void cancel() {
      FileSystem var1 = this.m_asset.getAssetManager().getOwner().getFileSystem();
      var1.cancelAsync(this.m_async_op);
      this.m_async_op = -1;
   }
}
