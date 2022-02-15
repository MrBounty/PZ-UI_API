package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class ZombieGetUpFromCrawlState extends State {
   private static final ZombieGetUpFromCrawlState _instance = new ZombieGetUpFromCrawlState();

   public static ZombieGetUpFromCrawlState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoZombie var3 = (IsoZombie)var1;
      var2.put(1, var1.getStateMachine().getPrevious());
      if (var3.isCrawling()) {
         var3.toggleCrawling();
         var3.setOnFloor(true);
      }

   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoZombie var3 = (IsoZombie)var1;
      var3.AllowRepathDelay = 0.0F;
      if (var2.get(1) == PathFindState.instance()) {
         if (var1.getPathFindBehavior2().getTargetChar() == null) {
            var1.setVariable("bPathfind", true);
            var1.setVariable("bMoving", false);
         } else if (var3.isTargetLocationKnown()) {
            var1.pathToCharacter(var1.getPathFindBehavior2().getTargetChar());
         } else if (var3.LastTargetSeenX != -1) {
            var1.pathToLocation(var3.LastTargetSeenX, var3.LastTargetSeenY, var3.LastTargetSeenZ);
         }
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
   }
}
