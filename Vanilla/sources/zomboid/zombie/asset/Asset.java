package zombie.asset;

import java.util.ArrayList;

public abstract class Asset {
   protected final AssetManager m_asset_manager;
   private AssetPath m_path;
   private int m_ref_count = 0;
   final Asset.PRIVATE m_priv = new Asset.PRIVATE();

   protected Asset(AssetPath var1, AssetManager var2) {
      this.m_path = var1;
      this.m_asset_manager = var2;
   }

   public abstract AssetType getType();

   public Asset.State getState() {
      return this.m_priv.m_current_state;
   }

   public boolean isEmpty() {
      return this.m_priv.m_current_state == Asset.State.EMPTY;
   }

   public boolean isReady() {
      return this.m_priv.m_current_state == Asset.State.READY;
   }

   public boolean isFailure() {
      return this.m_priv.m_current_state == Asset.State.FAILURE;
   }

   public void onCreated(Asset.State var1) {
      this.m_priv.onCreated(var1);
   }

   public int getRefCount() {
      return this.m_ref_count;
   }

   public Asset.ObserverCallback getObserverCb() {
      if (this.m_priv.m_cb == null) {
         this.m_priv.m_cb = new Asset.ObserverCallback();
      }

      return this.m_priv.m_cb;
   }

   public AssetPath getPath() {
      return this.m_path;
   }

   public AssetManager getAssetManager() {
      return this.m_asset_manager;
   }

   protected void onBeforeReady() {
   }

   protected void onBeforeEmpty() {
   }

   public void addDependency(Asset var1) {
      this.m_priv.addDependency(var1);
   }

   public void removeDependency(Asset var1) {
      this.m_priv.removeDependency(var1);
   }

   int addRef() {
      return ++this.m_ref_count;
   }

   int rmRef() {
      return --this.m_ref_count;
   }

   public void setAssetParams(AssetManager.AssetParams var1) {
   }

   final class PRIVATE implements AssetStateObserver {
      Asset.State m_current_state;
      Asset.State m_desired_state;
      int m_empty_dep_count;
      int m_failed_dep_count;
      Asset.ObserverCallback m_cb;
      AssetTask m_task;

      PRIVATE() {
         this.m_current_state = Asset.State.EMPTY;
         this.m_desired_state = Asset.State.EMPTY;
         this.m_empty_dep_count = 1;
         this.m_failed_dep_count = 0;
         this.m_task = null;
      }

      void onCreated(Asset.State var1) {
         assert this.m_empty_dep_count == 1;

         assert this.m_failed_dep_count == 0;

         this.m_current_state = var1;
         this.m_desired_state = Asset.State.READY;
         this.m_failed_dep_count = var1 == Asset.State.FAILURE ? 1 : 0;
         this.m_empty_dep_count = 0;
      }

      void addDependency(Asset var1) {
         assert this.m_desired_state != Asset.State.EMPTY;

         var1.getObserverCb().add(this);
         if (var1.isEmpty()) {
            ++this.m_empty_dep_count;
         }

         if (var1.isFailure()) {
            ++this.m_failed_dep_count;
         }

         this.checkState();
      }

      void removeDependency(Asset var1) {
         var1.getObserverCb().remove(this);
         if (var1.isEmpty()) {
            assert this.m_empty_dep_count > 0;

            --this.m_empty_dep_count;
         }

         if (var1.isFailure()) {
            assert this.m_failed_dep_count > 0;

            --this.m_failed_dep_count;
         }

         this.checkState();
      }

      public void onStateChanged(Asset.State var1, Asset.State var2, Asset var3) {
         assert var1 != var2;

         assert this.m_current_state != Asset.State.EMPTY || this.m_desired_state != Asset.State.EMPTY;

         if (var1 == Asset.State.EMPTY) {
            assert this.m_empty_dep_count > 0;

            --this.m_empty_dep_count;
         }

         if (var1 == Asset.State.FAILURE) {
            assert this.m_failed_dep_count > 0;

            --this.m_failed_dep_count;
         }

         if (var2 == Asset.State.EMPTY) {
            ++this.m_empty_dep_count;
         }

         if (var2 == Asset.State.FAILURE) {
            ++this.m_failed_dep_count;
         }

         this.checkState();
      }

      void onLoadingSucceeded() {
         assert this.m_current_state != Asset.State.READY;

         assert this.m_empty_dep_count == 1;

         --this.m_empty_dep_count;
         this.m_task = null;
         this.checkState();
      }

      void onLoadingFailed() {
         assert this.m_current_state != Asset.State.READY;

         assert this.m_empty_dep_count == 1;

         ++this.m_failed_dep_count;
         --this.m_empty_dep_count;
         this.m_task = null;
         this.checkState();
      }

      void checkState() {
         Asset.State var1 = this.m_current_state;
         if (this.m_failed_dep_count > 0 && this.m_current_state != Asset.State.FAILURE) {
            this.m_current_state = Asset.State.FAILURE;
            Asset.this.getAssetManager().onStateChanged(var1, this.m_current_state, Asset.this);
            if (this.m_cb != null) {
               this.m_cb.invoke(var1, this.m_current_state, Asset.this);
            }
         }

         if (this.m_failed_dep_count == 0) {
            if (this.m_empty_dep_count == 0 && this.m_current_state != Asset.State.READY && this.m_desired_state != Asset.State.EMPTY) {
               Asset.this.onBeforeReady();
               this.m_current_state = Asset.State.READY;
               Asset.this.getAssetManager().onStateChanged(var1, this.m_current_state, Asset.this);
               if (this.m_cb != null) {
                  this.m_cb.invoke(var1, this.m_current_state, Asset.this);
               }
            }

            if (this.m_empty_dep_count > 0 && this.m_current_state != Asset.State.EMPTY) {
               Asset.this.onBeforeEmpty();
               this.m_current_state = Asset.State.EMPTY;
               Asset.this.getAssetManager().onStateChanged(var1, this.m_current_state, Asset.this);
               if (this.m_cb != null) {
                  this.m_cb.invoke(var1, this.m_current_state, Asset.this);
               }
            }
         }

      }
   }

   public static enum State {
      EMPTY,
      READY,
      FAILURE;

      // $FF: synthetic method
      private static Asset.State[] $values() {
         return new Asset.State[]{EMPTY, READY, FAILURE};
      }
   }

   public static final class ObserverCallback extends ArrayList {
      public void invoke(Asset.State var1, Asset.State var2, Asset var3) {
         int var4 = this.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            ((AssetStateObserver)this.get(var5)).onStateChanged(var1, var2, var3);
         }

      }
   }
}
