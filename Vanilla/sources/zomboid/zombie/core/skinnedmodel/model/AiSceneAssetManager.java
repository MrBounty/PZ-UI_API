package zombie.core.skinnedmodel.model;

import jassimp.AiScene;
import jassimp.Jassimp;
import java.util.EnumSet;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

/** @deprecated */
@Deprecated
public final class AiSceneAssetManager extends AssetManager {
   public static final AiSceneAssetManager instance = new AiSceneAssetManager();

   protected void startLoading(Asset var1) {
      FileSystem var2 = var1.getAssetManager().getOwner().getFileSystem();
      AiSceneAssetManager.FileTask_LoadAiScene var3 = new AiSceneAssetManager.FileTask_LoadAiScene(var1.getPath().getPath(), ((AiSceneAsset)var1).m_post_process_step_set, (var2x) -> {
         this.onFileTaskFinished((AiSceneAsset)var1, var2x);
      }, var2);
      AssetTask_RunFileTask var4 = new AssetTask_RunFileTask(var3, var1);
      this.setTask(var1, var4);
      var4.execute();
   }

   public void onFileTaskFinished(AiSceneAsset var1, Object var2) {
      if (var2 instanceof AiScene) {
         var1.m_scene = (AiScene)var2;
         this.onLoadingSucceeded(var1);
      } else {
         this.onLoadingFailed(var1);
      }

   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new AiSceneAsset(var1, this, (AiSceneAsset.AiSceneAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }

   static class FileTask_LoadAiScene extends FileTask {
      String m_filename;
      EnumSet m_post_process_step_set;

      public FileTask_LoadAiScene(String var1, EnumSet var2, IFileTaskCallback var3, FileSystem var4) {
         super(var4, var3);
         this.m_filename = var1;
         this.m_post_process_step_set = var2;
      }

      public String getErrorMessage() {
         return this.m_filename;
      }

      public void done() {
         this.m_filename = null;
         this.m_post_process_step_set = null;
      }

      public Object call() throws Exception {
         return Jassimp.importFile(this.m_filename, this.m_post_process_step_set);
      }
   }
}
