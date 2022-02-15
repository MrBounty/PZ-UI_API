package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class ZombieGetDownState extends State {
   private static final ZombieGetDownState _instance = new ZombieGetDownState();
   static final Integer PARAM_PREV_STATE = 1;
   static final Integer PARAM_WAIT_TIME = 2;
   static final Integer PARAM_START_X = 3;
   static final Integer PARAM_START_Y = 4;

   public static ZombieGetDownState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var2.put(PARAM_PREV_STATE, var1.getStateMachine().getPrevious());
      var2.put(PARAM_START_X, var1.getX());
      var2.put(PARAM_START_Y, var1.getY());
      var1.setStateEventDelayTimer((Float)var2.get(PARAM_WAIT_TIME));
   }

   public void execute(IsoGameCharacter var1) {
      var1.getStateMachineParams(this);
   }

   public void exit(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoZombie var3 = (IsoZombie)var1;
      var3.setStateEventDelayTimer(0.0F);
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
      IsoZombie var3 = (IsoZombie)var1;
      if (var2.m_EventName.equalsIgnoreCase("StartCrawling") && !var3.isCrawling()) {
         var3.toggleCrawling();
      }

   }

   public boolean isNearStartXY(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      Float var3 = (Float)var2.get(PARAM_START_X);
      Float var4 = (Float)var2.get(PARAM_START_Y);
      if (var3 != null && var4 != null) {
         return var1.DistToSquared(var3, var4) <= 0.25F;
      } else {
         return false;
      }
   }

   public void setParams(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var2.put(PARAM_WAIT_TIME, Rand.Next(60.0F, 150.0F));
   }
}
