package zombie.core.textures;

import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;

public final class TextureAssetManager extends AssetManager {
   public static final TextureAssetManager instance = new TextureAssetManager();

   protected void startLoading(Asset var1) {
   }

   protected Asset createAsset(AssetPath var1, AssetManager.AssetParams var2) {
      return new Texture(var1, this, (Texture.TextureAssetParams)var2);
   }

   protected void destroyAsset(Asset var1) {
   }
}
