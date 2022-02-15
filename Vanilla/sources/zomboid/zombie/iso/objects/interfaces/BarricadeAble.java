package zombie.iso.objects.interfaces;

import zombie.characters.IsoGameCharacter;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoBarricade;

public interface BarricadeAble {
   boolean isBarricaded();

   boolean isBarricadeAllowed();

   IsoBarricade getBarricadeOnSameSquare();

   IsoBarricade getBarricadeOnOppositeSquare();

   IsoBarricade getBarricadeForCharacter(IsoGameCharacter var1);

   IsoBarricade getBarricadeOppositeCharacter(IsoGameCharacter var1);

   IsoGridSquare getSquare();

   IsoGridSquare getOppositeSquare();

   boolean getNorth();
}
