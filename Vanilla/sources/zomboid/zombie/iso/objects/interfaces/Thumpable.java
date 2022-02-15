package zombie.iso.objects.interfaces;

import zombie.characters.IsoGameCharacter;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoMovingObject;

public interface Thumpable {
   boolean isDestroyed();

   void Thump(IsoMovingObject var1);

   void WeaponHit(IsoGameCharacter var1, HandWeapon var2);

   Thumpable getThumpableFor(IsoGameCharacter var1);

   float getThumpCondition();
}
