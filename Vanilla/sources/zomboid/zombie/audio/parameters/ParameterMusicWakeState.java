package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoPlayer;

public final class ParameterMusicWakeState extends FMODGlobalParameter {
   private int m_playerIndex = -1;
   private ParameterMusicWakeState.State m_state;

   public ParameterMusicWakeState() {
      super("MusicWakeState");
      this.m_state = ParameterMusicWakeState.State.Awake;
   }

   public float calculateCurrentValue() {
      IsoPlayer var1 = this.choosePlayer();
      if (var1 != null && this.m_state == ParameterMusicWakeState.State.Awake && var1.isAsleep()) {
         this.m_state = ParameterMusicWakeState.State.Sleeping;
      }

      return (float)this.m_state.label;
   }

   public void setState(IsoPlayer var1, ParameterMusicWakeState.State var2) {
      if (var1 == this.choosePlayer()) {
         this.m_state = var2;
      }

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
               this.m_state = var2.isAsleep() ? ParameterMusicWakeState.State.Sleeping : ParameterMusicWakeState.State.Awake;
               return var2;
            }
         }

         return null;
      }
   }

   public static enum State {
      Awake(0),
      Sleeping(1),
      WakeNormal(2),
      WakeNightmare(3),
      WakeZombies(4);

      final int label;

      private State(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterMusicWakeState.State[] $values() {
         return new ParameterMusicWakeState.State[]{Awake, Sleeping, WakeNormal, WakeNightmare, WakeZombies};
      }
   }
}
