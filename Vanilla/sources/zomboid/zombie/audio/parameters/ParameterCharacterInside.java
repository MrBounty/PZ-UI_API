package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoGameCharacter;

public final class ParameterCharacterInside extends FMODLocalParameter {
   private final IsoGameCharacter character;

   public ParameterCharacterInside(IsoGameCharacter var1) {
      super("CharacterInside");
      this.character = var1;
   }

   public float calculateCurrentValue() {
      if (this.character.getVehicle() == null) {
         return this.character.getCurrentBuilding() == null ? 0.0F : 1.0F;
      } else {
         return 2.0F;
      }
   }
}
