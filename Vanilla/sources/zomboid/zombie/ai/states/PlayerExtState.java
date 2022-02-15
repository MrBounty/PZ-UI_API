package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class PlayerExtState extends State {
   private static final PlayerExtState _instance = new PlayerExtState();

   public static PlayerExtState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setVariable("ExtPlaying", true);
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("ExtPlaying");
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if ("ExtFinishing".equalsIgnoreCase(var2.m_EventName)) {
         var1.setVariable("ExtPlaying", false);
      }

   }
}
