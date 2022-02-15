package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.network.GameClient;
import zombie.util.StringUtils;

public final class ZombieGetUpState extends State {
   private static final ZombieGetUpState _instance = new ZombieGetUpState();
   static final Integer PARAM_STANDING = 1;
   static final Integer PARAM_PREV_STATE = 2;

   public static ZombieGetUpState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      var3.put(PARAM_STANDING, Boolean.FALSE);
      State var4 = var1.getStateMachine().getPrevious();
      if (var4 == ZombieGetUpFromCrawlState.instance()) {
         var4 = (State)var1.getStateMachineParams(ZombieGetUpFromCrawlState.instance()).get(1);
      }

      var3.put(PARAM_PREV_STATE, var4);
      var2.parameterZombieState.setState(ParameterZombieState.State.GettingUp);
      if (GameClient.bClient) {
         var1.setKnockedDown(false);
      }

   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      boolean var3 = var2.get(PARAM_STANDING) == Boolean.TRUE;
      var1.setOnFloor(!var3);
      ((IsoZombie)var1).setKnockedDown(!var3);
   }

   public void exit(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoZombie var3 = (IsoZombie)var1;
      var1.setCollidable(true);
      var1.clearVariable("SprinterTripped");
      var1.clearVariable("ShouldStandUp");
      if (StringUtils.isNullOrEmpty(var1.getHitReaction())) {
         var3.setSitAgainstWall(false);
      }

      var3.setKnockedDown(false);
      var3.AllowRepathDelay = 0.0F;
      if (var2.get(PARAM_PREV_STATE) == PathFindState.instance()) {
         if (var1.getPathFindBehavior2().getTargetChar() == null) {
            var1.setVariable("bPathfind", true);
            var1.setVariable("bMoving", false);
         } else if (var3.isTargetLocationKnown()) {
            var1.pathToCharacter(var1.getPathFindBehavior2().getTargetChar());
         } else if (var3.LastTargetSeenX != -1) {
            var1.pathToLocation(var3.LastTargetSeenX, var3.LastTargetSeenY, var3.LastTargetSeenZ);
         }
      } else if (var2.get(PARAM_PREV_STATE) == WalkTowardState.instance()) {
         var1.setVariable("bPathFind", false);
         var1.setVariable("bMoving", true);
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      IsoZombie var4 = (IsoZombie)var1;
      if (var2.m_EventName.equalsIgnoreCase("IsAlmostUp")) {
         var3.put(PARAM_STANDING, Boolean.TRUE);
      }

   }
}
