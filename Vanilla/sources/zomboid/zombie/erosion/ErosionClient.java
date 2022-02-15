package zombie.erosion;

import zombie.erosion.season.ErosionIceQueen;
import zombie.iso.sprite.IsoSpriteManager;

public class ErosionClient {
   public static ErosionClient instance;

   public ErosionClient(IsoSpriteManager var1, boolean var2) {
      instance = this;
      new ErosionIceQueen(var1);
      ErosionRegions.init();
   }

   public static void Reset() {
      instance = null;
   }
}
