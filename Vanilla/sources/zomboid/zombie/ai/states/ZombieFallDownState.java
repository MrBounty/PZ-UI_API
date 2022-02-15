package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class ZombieFallDownState extends State {
   private static final ZombieFallDownState _instance = new ZombieFallDownState();

   public static ZombieFallDownState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.blockTurning = true;
      var1.setHitReaction("");
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.blockTurning = false;
      var1.setOnFloor(true);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("PlayDeathSound")) {
         var1.setDoDeathSound(false);
         var1.playDeadSound();
      }

   }
}
