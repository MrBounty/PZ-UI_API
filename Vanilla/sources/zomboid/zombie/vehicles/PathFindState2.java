package zombie.vehicles;

import java.util.HashMap;
import zombie.ai.State;
import zombie.ai.astar.AStarPathFinder;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.gameStates.IngameState;
import zombie.iso.IsoChunk;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public final class PathFindState2 extends State {
   private static final Integer PARAM_TICK_COUNT = 0;

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var1.setVariable("bPathfind", true);
      var1.setVariable("bMoving", false);
      ((IsoZombie)var1).networkAI.extraUpdate();
      var2.put(PARAM_TICK_COUNT, IngameState.instance.numberTicks);
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      PathFindBehavior2.BehaviorResult var3 = var1.getPathFindBehavior2().update();
      if (var3 == PathFindBehavior2.BehaviorResult.Failed) {
         var1.setPathFindIndex(-1);
         var1.setVariable("bPathfind", false);
         var1.setVariable("bMoving", false);
      } else if (var3 == PathFindBehavior2.BehaviorResult.Succeeded) {
         int var7 = (int)var1.getPathFindBehavior2().getTargetX();
         int var5 = (int)var1.getPathFindBehavior2().getTargetY();
         IsoChunk var6 = GameServer.bServer ? ServerMap.instance.getChunk(var7 / 10, var5 / 10) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var7, var5, 0);
         if (var6 == null) {
            var1.setVariable("bPathfind", false);
            var1.setVariable("bMoving", true);
         } else {
            var1.setVariable("bPathfind", false);
            var1.setVariable("bMoving", false);
            var1.setPath2((PolygonalMap2.Path)null);
         }
      } else {
         if (var1 instanceof IsoZombie) {
            long var4 = (Long)var2.get(PARAM_TICK_COUNT);
            if (IngameState.instance.numberTicks - var4 == 2L) {
               ((IsoZombie)var1).parameterZombieState.setState(ParameterZombieState.State.Idle);
            }
         }

      }
   }

   public void exit(IsoGameCharacter var1) {
      if (var1 instanceof IsoZombie) {
         ((IsoZombie)var1).networkAI.extraUpdate();
         ((IsoZombie)var1).AllowRepathDelay = 0.0F;
      }

      var1.setVariable("bPathfind", false);
      var1.setVariable("bMoving", false);
      var1.setVariable("ShouldBeCrawling", false);
      PolygonalMap2.instance.cancelRequest(var1);
      var1.getFinder().progress = AStarPathFinder.PathFindProgress.notrunning;
      var1.setPath2((PolygonalMap2.Path)null);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
   }

   public boolean isMoving(IsoGameCharacter var1) {
      return var1.isMoving();
   }
}
