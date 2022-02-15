package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;

public final class ParameterInside extends FMODGlobalParameter {
   public ParameterInside() {
      super("Inside");
   }

   public float calculateCurrentValue() {
      IsoGameCharacter var1 = this.getCharacter();
      if (var1 == null) {
         return 0.0F;
      } else if (var1.getVehicle() == null) {
         return var1.getCurrentBuilding() == null ? 0.0F : 1.0F;
      } else {
         return -1.0F;
      }
   }

   private IsoGameCharacter getCharacter() {
      IsoPlayer var1 = null;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         IsoPlayer var3 = IsoPlayer.players[var2];
         if (var3 != null && (var1 == null || var1.isDead() && var3.isAlive())) {
            var1 = var3;
         }
      }

      return var1;
   }
}
