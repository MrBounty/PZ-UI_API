package zombie.asset;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.debug.DebugLog;
import zombie.fileSystem.IFile;

public abstract class AssetManager implements AssetStateObserver {
   private final AssetManager.AssetTable m_assets = new AssetManager.AssetTable();
   private AssetManagers m_owner;
   private boolean m_is_unload_enabled = false;

   public void create(AssetType var1, AssetManagers var2) {
      var2.add(var1, this);
      this.m_owner = var2;
   }

   public void destroy() {
      this.m_assets.forEachValue((var1) -> {
         if (!var1.isEmpty()) {
            DebugLog.Asset.println("Leaking asset " + var1.getPath());
         }

         this.destroyAsset(var1);
         return true;
      });
   }

   public void removeUnreferenced() {
      if (this.m_is_unload_enabled) {
         ArrayList var1 = new ArrayList();
         this.m_assets.forEachValue((var1x) -> {
            if (var1x.getRefCount() == 0) {
               var1.add(var1x);
            }

            return true;
         });
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Asset var3 = (Asset)var2.next();
            this.m_assets.remove(var3.getPath());
            this.destroyAsset(var3);
         }

      }
   }

   public Asset load(AssetPath var1) {
      return this.load(var1, (AssetManager.AssetParams)null);
   }

   public Asset load(AssetPath var1, AssetManager.AssetParams var2) {
      if (!var1.isValid()) {
         return null;
      } else {
         Asset var3 = this.get(var1);
         if (var3 == null) {
            var3 = this.createAsset(var1, var2);
            this.m_assets.put(var1.getPath(), var3);
         }

         if (var3.isEmpty() && var3.m_priv.m_desired_state == Asset.State.EMPTY) {
            this.doLoad(var3, var2);
         }

         var3.addRef();
         return var3;
      }
   }

   public void load(Asset var1) {
      if (var1.isEmpty() && var1.m_priv.m_desired_state == Asset.State.EMPTY) {
         this.doLoad(var1, (AssetManager.AssetParams)null);
      }

      var1.addRef();
   }

   public void unload(AssetPath var1) {
      Asset var2 = this.get(var1);
      if (var2 != null) {
         this.unload(var2);
      }

   }

   public void unload(Asset var1) {
      int var2 = var1.rmRef();

      assert var2 >= 0;

      if (var2 == 0 && this.m_is_unload_enabled) {
         this.doUnload(var1);
      }

   }

   public void reload(AssetPath var1) {
      Asset var2 = this.get(var1);
      if (var2 != null) {
         this.reload(var2);
      }

   }

   public void reload(Asset var1) {
      this.reload(var1, (AssetManager.AssetParams)null);
   }

   public void reload(Asset var1, AssetManager.AssetParams var2) {
      this.doUnload(var1);
      this.doLoad(var1, var2);
   }

   public void enableUnload(boolean var1) {
      this.m_is_unload_enabled = var1;
      if (var1) {
         this.m_assets.forEachValue((var1x) -> {
            if (var1x.getRefCount() == 0) {
               this.doUnload(var1x);
            }

            return true;
         });
      }
   }

   private void doLoad(Asset var1, AssetManager.AssetParams var2) {
      if (var1.m_priv.m_desired_state != Asset.State.READY) {
         var1.m_priv.m_desired_state = Asset.State.READY;
         var1.setAssetParams(var2);
         this.startLoading(var1);
      }
   }

   private void doUnload(Asset var1) {
      if (var1.m_priv.m_task != null) {
         var1.m_priv.m_task.cancel();
         var1.m_priv.m_task = null;
      }

      var1.m_priv.m_desired_state = Asset.State.EMPTY;
      this.unloadData(var1);

      assert var1.m_priv.m_empty_dep_count <= 1;

      var1.m_priv.m_empty_dep_count = 1;
      var1.m_priv.m_failed_dep_count = 0;
      var1.m_priv.checkState();
   }

   public void onStateChanged(Asset.State var1, Asset.State var2, Asset var3) {
   }

   protected void startLoading(Asset var1) {
      if (var1.m_priv.m_task == null) {
         var1.m_priv.m_task = new AssetTask_LoadFromFileAsync(var1, false);
         var1.m_priv.m_task.execute();
      }
   }

   protected final void onLoadingSucceeded(Asset var1) {
      var1.m_priv.onLoadingSucceeded();
   }

   protected final void onLoadingFailed(Asset var1) {
      var1.m_priv.onLoadingFailed();
   }

   protected final void setTask(Asset var1, AssetTask var2) {
      if (var1.m_priv.m_task != null) {
         if (var2 == null) {
            var1.m_priv.m_task = null;
         }

      } else {
         var1.m_priv.m_task = var2;
      }
   }

   protected boolean loadDataFromFile(Asset var1, IFile var2) {
      throw new RuntimeException("not implemented");
   }

   protected void unloadData(Asset var1) {
   }

   public AssetManager.AssetTable getAssetTable() {
      return this.m_assets;
   }

   public AssetManagers getOwner() {
      return this.m_owner;
   }

   protected abstract Asset createAsset(AssetPath var1, AssetManager.AssetParams var2);

   protected abstract void destroyAsset(Asset var1);

   protected Asset get(AssetPath var1) {
      return (Asset)this.m_assets.get(var1.getPath());
   }

   public static final class AssetTable extends THashMap {
   }

   public static class AssetParams {
   }
}
