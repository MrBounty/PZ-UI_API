package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.Moodles.MoodleType;

public final class ParameterMoodlePanic extends FMODGlobalParameter {
   public ParameterMoodlePanic() {
      super("MoodlePanic");
   }

   public float calculateCurrentValue() {
      IsoGameCharacter var1 = this.getCharacter();
      return var1 == null ? 0.0F : (float)var1.getMoodles().getMoodleLevel(MoodleType.Panic) / 4.0F;
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
