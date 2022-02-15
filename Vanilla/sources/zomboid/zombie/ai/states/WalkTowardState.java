package zombie.ai.states;

import java.util.HashMap;
import org.joml.Vector3f;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.gameStates.IngameState;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.util.Type;
import zombie.vehicles.PolygonalMap2;

public final class WalkTowardState extends State {
   private static final WalkTowardState _instance = new WalkTowardState();
   private static final Integer PARAM_IGNORE_OFFSET = 0;
   private static final Integer PARAM_IGNORE_TIME = 1;
   private static final Integer PARAM_TICK_COUNT = 2;
   private final Vector2 temp = new Vector2();
   private final Vector3f worldPos = new Vector3f();

   public static WalkTowardState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (var2.get(PARAM_IGNORE_OFFSET) == null) {
         var2.put(PARAM_IGNORE_OFFSET, Boolean.FALSE);
         var2.put(PARAM_IGNORE_TIME, 0L);
      }

      if (var2.get(PARAM_IGNORE_OFFSET) == Boolean.TRUE && System.currentTimeMillis() - (Long)var2.get(PARAM_IGNORE_TIME) > 3000L) {
         var2.put(PARAM_IGNORE_OFFSET, Boolean.FALSE);
         var2.put(PARAM_IGNORE_TIME, 0L);
      }

      var2.put(PARAM_TICK_COUNT, IngameState.instance.numberTicks);
      if (((IsoZombie)var1).isUseless()) {
         var1.changeState(ZombieIdleState.instance());
      }

      var1.getPathFindBehavior2().walkingOnTheSpot.reset(var1.x, var1.y);
      ((IsoZombie)var1).networkAI.extraUpdate();
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoZombie var3 = (IsoZombie)var1;
      if (!var3.bCrawling) {
         var1.setOnFloor(false);
      }

      IsoGameCharacter var4 = (IsoGameCharacter)Type.tryCastTo(var3.target, IsoGameCharacter.class);
      if (var3.target != null) {
         if (var3.isTargetLocationKnown()) {
            if (var4 != null) {
               var3.getPathFindBehavior2().pathToCharacter(var4);
               if (var4.getVehicle() != null && var3.DistToSquared(var3.target) < 16.0F) {
                  Vector3f var5 = var4.getVehicle().chooseBestAttackPosition(var4, var3, this.worldPos);
                  if (var5 == null) {
                     var3.setVariable("bMoving", false);
                     return;
                  }

                  if (Math.abs(var1.x - var3.getPathFindBehavior2().getTargetX()) > 0.1F || Math.abs(var1.y - var3.getPathFindBehavior2().getTargetY()) > 0.1F) {
                     var3.setVariable("bPathfind", true);
                     var3.setVariable("bMoving", false);
                     return;
                  }
               }
            }
         } else if (var3.LastTargetSeenX != -1 && !var1.getPathFindBehavior2().isTargetLocation((float)var3.LastTargetSeenX + 0.5F, (float)var3.LastTargetSeenY + 0.5F, (float)var3.LastTargetSeenZ)) {
            var1.pathToLocation(var3.LastTargetSeenX, var3.LastTargetSeenY, var3.LastTargetSeenZ);
         }
      }

      if (var1.getPathTargetX() == (int)var1.getX() && var1.getPathTargetY() == (int)var1.getY()) {
         if (var3.target == null) {
            var3.setVariable("bPathfind", false);
            var3.setVariable("bMoving", false);
            return;
         }

         if ((int)var3.target.getZ() != (int)var1.getZ()) {
            var3.setVariable("bPathfind", true);
            var3.setVariable("bMoving", false);
            return;
         }
      }

      boolean var11 = var1.isCollidedWithVehicle();
      if (var4 != null && var4.getVehicle() != null && var4.getVehicle().isCharacterAdjacentTo(var1)) {
         var11 = false;
      }

      boolean var6 = var1.isCollidedThisFrame();
      float var7;
      float var8;
      float var9;
      if (var6 && var2.get(PARAM_IGNORE_OFFSET) == Boolean.FALSE) {
         var2.put(PARAM_IGNORE_OFFSET, Boolean.TRUE);
         var2.put(PARAM_IGNORE_TIME, System.currentTimeMillis());
         var7 = var3.getPathFindBehavior2().getTargetX();
         var8 = var3.getPathFindBehavior2().getTargetY();
         var9 = var3.z;
         var6 = !this.isPathClear(var1, var7, var8, var9);
      }

      if (!var6 && !var11) {
         this.temp.x = var3.getPathFindBehavior2().getTargetX();
         this.temp.y = var3.getPathFindBehavior2().getTargetY();
         Vector2 var10000 = this.temp;
         var10000.x -= var3.getX();
         var10000 = this.temp;
         var10000.y -= var3.getY();
         var7 = this.temp.getLength();
         if (var7 < 0.25F) {
            var1.x = var3.getPathFindBehavior2().getTargetX();
            var1.y = var3.getPathFindBehavior2().getTargetY();
            var1.nx = var1.x;
            var1.ny = var1.y;
            var7 = 0.0F;
         }

         if (var7 < 0.025F) {
            var3.setVariable("bPathfind", false);
            var3.setVariable("bMoving", false);
         } else {
            if (!GameServer.bServer && !var3.bCrawling && var2.get(PARAM_IGNORE_OFFSET) == Boolean.FALSE) {
               var8 = Math.min(var7 / 2.0F, 4.0F);
               var9 = (float)((var1.getID() + var3.ZombieID) % 20) / 10.0F - 1.0F;
               float var10 = (float)((var3.getID() + var3.ZombieID) % 20) / 10.0F - 1.0F;
               var10000 = this.temp;
               var10000.x += var3.getX();
               var10000 = this.temp;
               var10000.y += var3.getY();
               var10000 = this.temp;
               var10000.x += var9 * var8;
               var10000 = this.temp;
               var10000.y += var10 * var8;
               var10000 = this.temp;
               var10000.x -= var3.getX();
               var10000 = this.temp;
               var10000.y -= var3.getY();
            }

            var3.bRunning = false;
            this.temp.normalize();
            if (var3.bCrawling) {
               if (var3.getVariableString("TurnDirection").isEmpty()) {
                  var3.setForwardDirection(this.temp);
               }
            } else {
               var3.setDir(IsoDirections.fromAngle(this.temp));
               var3.setForwardDirection(this.temp);
            }

            if (var1.getPathFindBehavior2().walkingOnTheSpot.check(var1.x, var1.y)) {
               var1.setVariable("bMoving", false);
            }

            long var12 = (Long)var2.get(PARAM_TICK_COUNT);
            if (IngameState.instance.numberTicks - var12 == 2L) {
               var3.parameterZombieState.setState(ParameterZombieState.State.Idle);
            }

         }
      } else {
         var3.AllowRepathDelay = 0.0F;
         var3.pathToLocation(var1.getPathTargetX(), var1.getPathTargetY(), var1.getPathTargetZ());
         if (!var3.getVariableBoolean("bPathfind")) {
            var3.setVariable("bPathfind", true);
            var3.setVariable("bMoving", false);
         }

      }
   }

   public void exit(IsoGameCharacter var1) {
      var1.setVariable("bMoving", false);
      ((IsoZombie)var1).networkAI.extraUpdate();
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
   }

   public boolean isMoving(IsoGameCharacter var1) {
      return true;
   }

   private boolean isPathClear(IsoGameCharacter var1, float var2, float var3, float var4) {
      int var5 = (int)var2 / 10;
      int var6 = (int)var3 / 10;
      IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var5, var6) : IsoWorld.instance.CurrentCell.getChunkForGridSquare((int)var2, (int)var3, (int)var4);
      if (var7 != null) {
         byte var8 = 1;
         int var9 = var8 | 2;
         return !PolygonalMap2.instance.lineClearCollide(var1.getX(), var1.getY(), var2, var3, (int)var4, var1.getPathFindBehavior2().getTargetChar(), var9);
      } else {
         return false;
      }
   }

   public boolean calculateTargetLocation(IsoZombie var1, Vector2 var2) {
      assert var1.isCurrentState(this);

      HashMap var3 = var1.getStateMachineParams(this);
      var2.x = var1.getPathFindBehavior2().getTargetX();
      var2.y = var1.getPathFindBehavior2().getTargetY();
      this.temp.set(var2);
      Vector2 var10000 = this.temp;
      var10000.x -= var1.getX();
      var10000 = this.temp;
      var10000.y -= var1.getY();
      float var4 = this.temp.getLength();
      if (var4 < 0.025F) {
         return false;
      } else if (!GameServer.bServer && !var1.bCrawling && var3.get(PARAM_IGNORE_OFFSET) == Boolean.FALSE) {
         float var5 = Math.min(var4 / 2.0F, 4.0F);
         float var6 = (float)((var1.getID() + var1.ZombieID) % 20) / 10.0F - 1.0F;
         float var7 = (float)((var1.getID() + var1.ZombieID) % 20) / 10.0F - 1.0F;
         var2.x += var6 * var5;
         var2.y += var7 * var5;
         return true;
      } else {
         return false;
      }
   }
}
