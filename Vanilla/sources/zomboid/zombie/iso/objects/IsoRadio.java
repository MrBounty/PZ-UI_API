package zombie.iso.objects;

import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.sprite.IsoSprite;

public class IsoRadio extends IsoWaveSignal {
   public IsoRadio(IsoCell var1) {
      super(var1);
   }

   public IsoRadio(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
   }

   public String getObjectName() {
      return "Radio";
   }

   protected void init(boolean var1) {
      super.init(var1);
   }
}
