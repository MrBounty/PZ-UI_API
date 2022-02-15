package zombie.asset;

import java.util.zip.CRC32;

public final class AssetType {
   public static final AssetType INVALID_ASSET_TYPE = new AssetType("");
   public long type;

   public AssetType(String var1) {
      CRC32 var2 = new CRC32();
      var2.update(var1.getBytes());
      this.type = var2.getValue();
   }
}
