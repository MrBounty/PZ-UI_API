package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;

public final class ZombieTurnAlerted extends State {
   private static final ZombieTurnAlerted _instance = new ZombieTurnAlerted();
   public static final Integer PARAM_TARGET_ANGLE = 0;

   public static ZombieTurnAlerted instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      float var3 = (Float)var2.get(PARAM_TARGET_ANGLE);
      var1.getAnimationPlayer().setTargetAngle(var3);
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.pathToSound(var1.getPathTargetX(), var1.getPathTargetY(), var1.getPathTargetZ());
      ((IsoZombie)var1).alerted = false;
   }

   public void setParams(IsoGameCharacter var1, float var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      var3.clear();
      var3.put(PARAM_TARGET_ANGLE, var2);
   }
}
