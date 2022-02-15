package zombie.core.skinnedmodel.model;

import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;

public final class ModelAssetManager extends AssetManager {
   public static final ModelAssetManager instance = new ModelAssetManager();

   protected void startLoading(Asset var1) {
   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new Model(var1, this, (Model.ModelAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }
}
