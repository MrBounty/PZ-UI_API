package zombie.characters;

import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;

public final class IsoDummyCameraCharacter extends IsoGameCharacter {
   public IsoDummyCameraCharacter(float var1, float var2, float var3) {
      super((IsoCell)null, var1, var2, var3);
      IsoCamera.CamCharacter = this;
   }

   public void update() {
   }
}
