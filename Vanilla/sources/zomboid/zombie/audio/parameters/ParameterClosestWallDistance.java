package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.iso.NearestWalls;

public final class ParameterClosestWallDistance extends FMODGlobalParameter {
   public ParameterClosestWallDistance() {
      super("ClosestWallDistance");
   }

   public float calculateCurrentValue() {
      IsoGameCharacter var1 = this.getCharacter();
      return var1 == null ? 127.0F : (float)NearestWalls.ClosestWallDistance(var1.getCurrentSquare());
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
