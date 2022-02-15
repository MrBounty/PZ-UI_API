package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoPlayer;
import zombie.core.math.PZMath;

public final class ParameterMusicZombiesTargeting extends FMODGlobalParameter {
   private int m_playerIndex = -1;

   public ParameterMusicZombiesTargeting() {
      super("MusicZombiesTargeting");
   }

   public float calculateCurrentValue() {
      IsoPlayer var1 = this.choosePlayer();
      return var1 != null ? (float)PZMath.clamp(var1.getStats().MusicZombiesTargeting, 0, 50) : 0.0F;
   }

   private IsoPlayer choosePlayer() {
      if (this.m_playerIndex != -1) {
         IsoPlayer var1 = IsoPlayer.players[this.m_playerIndex];
         if (var1 == null || var1.isDead()) {
            this.m_playerIndex = -1;
         }
      }

      if (this.m_playerIndex != -1) {
         return IsoPlayer.players[this.m_playerIndex];
      } else {
         for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            IsoPlayer var2 = IsoPlayer.players[var3];
            if (var2 != null && !var2.isDead()) {
               this.m_playerIndex = var3;
               return var2;
            }
         }

         return null;
      }
   }
}
