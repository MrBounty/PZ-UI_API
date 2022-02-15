package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;

public final class PlayerFallingState extends State {
   private static final PlayerFallingState _instance = new PlayerFallingState();

   public static PlayerFallingState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setVariable("bHardFall", false);
      var1.clearVariable("bLandAnimFinished");
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("bHardFall");
      var1.clearVariable("bLandAnimFinished");
   }
}
