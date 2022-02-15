package zombie.characters;

import zombie.iso.IsoCell;

public final class IsoLuaCharacter extends IsoGameCharacter {
   public IsoLuaCharacter(float var1, float var2, float var3) {
      super((IsoCell)null, var1, var2, var3);
      this.descriptor = SurvivorFactory.CreateSurvivor();
      this.descriptor.setInstance(this);
      SurvivorDesc var4 = this.descriptor;
      this.InitSpriteParts(var4);
   }

   public void update() {
   }
}
