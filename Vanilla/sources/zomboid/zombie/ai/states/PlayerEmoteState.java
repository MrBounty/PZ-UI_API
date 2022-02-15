package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class PlayerEmoteState extends State {
   private static final PlayerEmoteState _instance = new PlayerEmoteState();

   public static PlayerEmoteState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setVariable("EmotePlaying", true);
      var1.resetModelNextFrame();
   }

   public void execute(IsoGameCharacter var1) {
      IsoPlayer var2 = (IsoPlayer)var1;
      if (var2.pressedCancelAction()) {
         var1.setVariable("EmotePlaying", false);
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("EmotePlaying");
      var1.resetModelNextFrame();
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if ("EmoteFinishing".equalsIgnoreCase(var2.m_EventName)) {
         var1.setVariable("EmotePlaying", false);
      }

      if ("EmoteLooped".equalsIgnoreCase(var2.m_EventName)) {
      }

   }

   public boolean isDoingActionThatCanBeCancelled() {
      return true;
   }
}
