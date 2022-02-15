package zombie.core.skinnedmodel.model;

import java.util.HashSet;
import zombie.DebugFileWatcher;
import zombie.PredicatedFileWatcher;
import zombie.ZomboidFileSystem;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiScene;
import zombie.debug.DebugLog;
import zombie.fileSystem.FileSystem;
import zombie.util.StringUtils;

public final class MeshAssetManager extends AssetManager {
   public static final MeshAssetManager instance = new MeshAssetManager();
   private final HashSet m_watchedFiles = new HashSet();
   private final PredicatedFileWatcher m_watcher = new PredicatedFileWatcher(MeshAssetManager::isWatched, MeshAssetManager::watchedFileChanged);

   private MeshAssetManager() {
      DebugFileWatcher.instance.add(this.m_watcher);
   }

   protected void startLoading(Asset var1) {
      ModelMesh var2 = (ModelMesh)var1;
      FileSystem var3 = this.getOwner().getFileSystem();
      FileTask_LoadMesh var4 = new FileTask_LoadMesh(var2, var3, (var2x) -> {
         this.loadCallback(var2, var2x);
      });
      var4.setPriority(6);
      AssetTask_RunFileTask var5 = new AssetTask_RunFileTask(var4, var1);
      this.setTask(var1, var5);
      var5.execute();
   }

   private void loadCallback(ModelMesh var1, Object var2) {
      if (var2 instanceof ProcessedAiScene) {
         var1.onLoadedX((ProcessedAiScene)var2);
         this.onLoadingSucceeded(var1);
      } else if (var2 instanceof ModelTxt) {
         var1.onLoadedTxt((ModelTxt)var2);
         this.onLoadingSucceeded(var1);
      } else {
         DebugLog.General.warn("Failed to load asset: " + var1.getPath());
         this.onLoadingFailed(var1);
      }

   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new ModelMesh(var1, this, (ModelMesh.MeshAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }

   private static boolean isWatched(String var0) {
      if (!StringUtils.endsWithIgnoreCase(var0, ".fbx") && !StringUtils.endsWithIgnoreCase(var0, ".x")) {
         return false;
      } else {
         String var1 = ZomboidFileSystem.instance.getString(var0);
         return instance.m_watchedFiles.contains(var1);
      }
   }

   private static void watchedFileChanged(String var0) {
      DebugLog.Asset.printf("%s changed\n", var0);
      String var1 = ZomboidFileSystem.instance.getString(var0);
      instance.getAssetTable().forEachValue((var1x) -> {
         ModelMesh var2 = (ModelMesh)var1x;
         if (!var2.isEmpty() && var1.equalsIgnoreCase(var2.m_fullPath)) {
            ModelMesh.MeshAssetParams var3 = new ModelMesh.MeshAssetParams();
            var3.animationsMesh = var2.m_animationsMesh;
            var3.bStatic = var2.bStatic;
            instance.reload(var1x, var3);
         }

         return true;
      });
   }

   public void addWatchedFile(String var1) {
      this.m_watchedFiles.add(var1);
   }
}
