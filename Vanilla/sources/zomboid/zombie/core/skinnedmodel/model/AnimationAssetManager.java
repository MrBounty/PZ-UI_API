package zombie.core.skinnedmodel.model;

import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiScene;
import zombie.debug.DebugLog;
import zombie.fileSystem.FileSystem;

public final class AnimationAssetManager extends AssetManager {
   public static final AnimationAssetManager instance = new AnimationAssetManager();

   protected void startLoading(Asset var1) {
      AnimationAsset var2 = (AnimationAsset)var1;
      FileSystem var3 = this.getOwner().getFileSystem();
      FileTask_LoadAnimation var4 = new FileTask_LoadAnimation(var2, var3, (var2x) -> {
         this.loadCallback(var2, var2x);
      });
      var4.setPriority(4);
      String var5 = var1.getPath().getPath().toLowerCase();
      if (var5.endsWith("bob_idle") || var5.endsWith("bob_walk") || var5.endsWith("bob_run")) {
         var4.setPriority(6);
      }

      AssetTask_RunFileTask var6 = new AssetTask_RunFileTask(var4, var1);
      this.setTask(var1, var6);
      var6.execute();
   }

   private void loadCallback(AnimationAsset var1, Object var2) {
      if (var2 instanceof ProcessedAiScene) {
         var1.onLoadedX((ProcessedAiScene)var2);
         this.onLoadingSucceeded(var1);
         ModelManager.instance.animationAssetLoaded(var1);
      } else if (var2 instanceof ModelTxt) {
         var1.onLoadedTxt((ModelTxt)var2);
         this.onLoadingSucceeded(var1);
         ModelManager.instance.animationAssetLoaded(var1);
      } else {
         DebugLog.General.warn("Failed to load asset: " + var1.getPath());
         this.onLoadingFailed(var1);
      }

   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new AnimationAsset(var1, this, (AnimationAsset.AnimationAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }
}
