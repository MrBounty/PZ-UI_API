package zombie.iso.sprite.shapers;

import zombie.core.textures.TextureDraw;

public class FloorShaperDiamond extends FloorShaper {
   public static final FloorShaperDiamond instance = new FloorShaperDiamond();

   public void accept(TextureDraw var1) {
      super.accept(var1);
      DiamondShaper.instance.accept(var1);
   }
}
