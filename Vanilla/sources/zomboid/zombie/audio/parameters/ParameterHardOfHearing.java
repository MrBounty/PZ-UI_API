package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoPlayer;

public final class ParameterHardOfHearing extends FMODGlobalParameter {
   private int m_playerIndex = -1;

   public ParameterHardOfHearing() {
      super("HardOfHearing");
   }

   public float calculateCurrentValue() {
      IsoPlayer var1 = this.choosePlayer();
      if (var1 != null) {
         return var1.getCharacterTraits().HardOfHearing.isSet() ? 1.0F : 0.0F;
      } else {
         return 0.0F;
      }
   }

   private IsoPlayer choosePlayer() {
      if (this.m_playerIndex != -1) {
         IsoPlayer var1 = IsoPlayer.players[this.m_playerIndex];
         if (var1 == null) {
            this.m_playerIndex = -1;
         }
      }

      if (this.m_playerIndex != -1) {
         return IsoPlayer.players[this.m_playerIndex];
      } else {
         for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            IsoPlayer var2 = IsoPlayer.players[var3];
            if (var2 != null) {
               this.m_playerIndex = var3;
               return var2;
            }
         }

         return null;
      }
   }
}
