package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.util.StringUtils;

public final class IdleState extends State {
   private static final IdleState _instance = new IdleState();

   public static IdleState instance() {
      return _instance;
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("PlaySound") && !StringUtils.isNullOrEmpty(var2.m_ParameterValue)) {
         var1.getSquare().playSound(var2.m_ParameterValue);
      }

   }
}
