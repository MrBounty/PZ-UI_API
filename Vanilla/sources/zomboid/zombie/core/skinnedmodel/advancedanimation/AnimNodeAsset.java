package zombie.core.skinnedmodel.advancedanimation;

import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;

public class AnimNodeAsset extends Asset {
   public static final AssetType ASSET_TYPE = new AssetType("AnimNode");
   public AnimNode m_animNode;

   protected AnimNodeAsset(AssetPath var1, AssetManager var2) {
      super(var1, var2);
   }

   public AssetType getType() {
      return ASSET_TYPE;
   }
}
