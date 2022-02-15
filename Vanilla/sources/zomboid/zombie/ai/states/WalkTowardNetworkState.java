package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.gameStates.IngameState;
import zombie.iso.IsoChunk;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;
import zombie.network.NetworkVariables;
import zombie.network.ServerMap;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;

public class WalkTowardNetworkState extends State {
   static WalkTowardNetworkState _instance = new WalkTowardNetworkState();
   private static final Integer PARAM_TICK_COUNT = 2;

   public static WalkTowardNetworkState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var2.put(PARAM_TICK_COUNT, IngameState.instance.numberTicks);
      var1.setVariable("bMoving", true);
      var1.setVariable("bPathfind", false);
   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      PathFindBehavior2 var3 = var2.getPathFindBehavior2();
      var2.vectorToTarget.x = var2.networkAI.targetX - var2.x;
      var2.vectorToTarget.y = var2.networkAI.targetY - var2.y;
      var3.walkingOnTheSpot.reset(var2.x, var2.y);
      if (var2.z != (float)var2.networkAI.targetZ || var2.networkAI.predictionType != NetworkVariables.PredictionTypes.Thump && var2.networkAI.predictionType != NetworkVariables.PredictionTypes.Climb) {
         if (var2.z == (float)var2.networkAI.targetZ && !PolygonalMap2.instance.lineClearCollide(var2.x, var2.y, var2.networkAI.targetX, var2.networkAI.targetY, var2.networkAI.targetZ, (IsoMovingObject)null)) {
            if (var2.networkAI.usePathFind) {
               var3.reset();
               var2.setPath2((PolygonalMap2.Path)null);
               var2.networkAI.usePathFind = false;
            }

            var3.moveToPoint(var2.networkAI.targetX, var2.networkAI.targetY, 1.0F);
            var2.setVariable("bMoving", IsoUtils.DistanceManhatten(var2.networkAI.targetX, var2.networkAI.targetY, var2.nx, var2.ny) > 0.5F);
         } else {
            if (!var2.networkAI.usePathFind) {
               var3.pathToLocationF(var2.networkAI.targetX, var2.networkAI.targetY, (float)var2.networkAI.targetZ);
               var3.walkingOnTheSpot.reset(var2.x, var2.y);
               var2.networkAI.usePathFind = true;
            }

            PathFindBehavior2.BehaviorResult var4 = var3.update();
            if (var4 == PathFindBehavior2.BehaviorResult.Failed) {
               var2.setPathFindIndex(-1);
               return;
            }

            if (var4 == PathFindBehavior2.BehaviorResult.Succeeded) {
               int var5 = (int)var2.getPathFindBehavior2().getTargetX();
               int var6 = (int)var2.getPathFindBehavior2().getTargetY();
               IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var5 / 10, var6 / 10) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var5, var6, 0);
               if (var7 == null) {
                  var2.setVariable("bMoving", true);
                  return;
               }

               var2.setPath2((PolygonalMap2.Path)null);
               var2.setVariable("bMoving", true);
               return;
            }
         }
      } else {
         if (var2.networkAI.usePathFind) {
            var3.reset();
            var2.setPath2((PolygonalMap2.Path)null);
            var2.networkAI.usePathFind = false;
         }

         var3.moveToPoint(var2.networkAI.targetX, var2.networkAI.targetY, 1.0F);
         var2.setVariable("bMoving", IsoUtils.DistanceManhatten(var2.networkAI.targetX, var2.networkAI.targetY, var2.nx, var2.ny) > 0.5F);
      }

      if (!((IsoZombie)var1).bCrawling) {
         var1.setOnFloor(false);
      }

      boolean var8 = var1.isCollidedWithVehicle();
      if (var2.target instanceof IsoGameCharacter && ((IsoGameCharacter)var2.target).getVehicle() != null && ((IsoGameCharacter)var2.target).getVehicle().isCharacterAdjacentTo(var1)) {
         var8 = false;
      }

      if (var1.isCollidedThisFrame() || var8) {
         var2.AllowRepathDelay = 0.0F;
         var2.pathToLocation(var1.getPathTargetX(), var1.getPathTargetY(), var1.getPathTargetZ());
         if (!"true".equals(var2.getVariableString("bPathfind"))) {
            var2.setVariable("bPathfind", true);
            var2.setVariable("bMoving", false);
         }
      }

      HashMap var9 = var1.getStateMachineParams(this);
      long var10 = (Long)var9.get(PARAM_TICK_COUNT);
      if (IngameState.instance.numberTicks - var10 == 2L) {
         var2.parameterZombieState.setState(ParameterZombieState.State.Idle);
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.setVariable("bMoving", false);
   }
}
