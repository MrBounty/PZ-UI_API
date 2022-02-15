package zombie.worldMap;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import zombie.DebugFileWatcher;
import zombie.PredicatedFileWatcher;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetTask_RunFileTask;
import zombie.debug.DebugLog;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;

public final class WorldMapDataAssetManager extends AssetManager {
   public static final WorldMapDataAssetManager instance = new WorldMapDataAssetManager();

   protected void startLoading(Asset var1) {
      WorldMapData var2 = (WorldMapData)var1;
      FileSystem var3 = this.getOwner().getFileSystem();
      String var5 = var1.getPath().getPath();
      Object var4;
      if (Files.exists(Paths.get(var5 + ".bin"), new LinkOption[0])) {
         var4 = new FileTask_LoadWorldMapBinary(var2, var5 + ".bin", var3, (var2x) -> {
            this.loadCallback(var2, var2x);
         });
      } else {
         var4 = new FileTask_LoadWorldMapXML(var2, var5, var3, (var2x) -> {
            this.loadCallback(var2, var2x);
         });
      }

      ((FileTask)var4).setPriority(4);
      AssetTask_RunFileTask var6 = new AssetTask_RunFileTask((FileTask)var4, var1);
      this.setTask(var1, var6);
      var6.execute();
   }

   private void loadCallback(WorldMapData var1, Object var2) {
      if (var2 == Boolean.TRUE) {
         var1.onLoaded();
         this.onLoadingSucceeded(var1);
      } else {
         DebugLog.General.warn("Failed to load asset: " + var1.getPath());
         this.onLoadingFailed(var1);
      }

   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      WorldMapData var3 = new WorldMapData(var1, this, var2);
      DebugFileWatcher.instance.add(new PredicatedFileWatcher(var1.getPath(), (var3x) -> {
         this.reload(var3, var2);
      }));
      return var3;
   }

   protected void destroyAsset(Asset var1) {
   }
}
