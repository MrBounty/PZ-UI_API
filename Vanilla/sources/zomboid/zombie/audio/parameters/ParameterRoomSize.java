package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.iso.RoomDef;

public final class ParameterRoomSize extends FMODGlobalParameter {
   public ParameterRoomSize() {
      super("RoomSize");
   }

   public float calculateCurrentValue() {
      IsoGameCharacter var1 = this.getCharacter();
      if (var1 == null) {
         return 0.0F;
      } else {
         RoomDef var2 = var1.getCurrentRoomDef();
         return var2 == null ? 0.0F : (float)var2.getArea();
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
