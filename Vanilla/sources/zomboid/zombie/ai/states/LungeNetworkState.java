package zombie.ai.states;

import java.util.HashMap;
import org.joml.Vector3f;
import zombie.GameTime;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.gameStates.IngameState;
import zombie.iso.Vector2;
import zombie.util.Type;

public class LungeNetworkState extends State {
   static LungeNetworkState _instance = new LungeNetworkState();
   private Vector2 temp = new Vector2();
   private final Vector3f worldPos = new Vector3f();
   private static final Integer PARAM_TICK_COUNT = 0;

   public static LungeNetworkState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      WalkTowardNetworkState.instance().enter(var1);
      IsoZombie var3 = (IsoZombie)var1;
      var3.LungeTimer = 180.0F;
      var2.put(PARAM_TICK_COUNT, IngameState.instance.numberTicks);
   }

   public void execute(IsoGameCharacter var1) {
      WalkTowardNetworkState.instance().execute(var1);
      IsoZombie var2 = (IsoZombie)var1;
      var1.setOnFloor(false);
      var1.setShootable(true);
      if (var2.bLunger) {
         var2.walkVariantUse = "ZombieWalk3";
      }

      var2.LungeTimer -= GameTime.getInstance().getMultiplier() / 1.6F;
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var2.getTarget(), IsoPlayer.class);
      if (var3 != null && var3.isGhostMode()) {
         var2.LungeTimer = 0.0F;
      }

      if (var2.LungeTimer < 0.0F) {
         var2.LungeTimer = 0.0F;
      }

      if (var2.LungeTimer <= 0.0F) {
         var2.AllowRepathDelay = 0.0F;
      }

      HashMap var4 = var1.getStateMachineParams(this);
      long var5 = (Long)var4.get(PARAM_TICK_COUNT);
      if (IngameState.instance.numberTicks - var5 == 2L) {
         ((IsoZombie)var1).parameterZombieState.setState(ParameterZombieState.State.LockTarget);
      }

   }

   public void exit(IsoGameCharacter var1) {
      WalkTowardNetworkState.instance().exit(var1);
   }

   public boolean isMoving(IsoGameCharacter var1) {
      return true;
   }
}
