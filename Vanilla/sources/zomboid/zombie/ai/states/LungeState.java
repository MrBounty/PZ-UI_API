package zombie.ai.states;

import java.util.HashMap;
import zombie.GameTime;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.gameStates.IngameState;
import zombie.iso.Vector2;
import zombie.network.GameServer;
import zombie.util.Type;

public final class LungeState extends State {
   private static final LungeState _instance = new LungeState();
   private final Vector2 temp = new Vector2();
   private static final Integer PARAM_TICK_COUNT = 0;

   public static LungeState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      if (System.currentTimeMillis() - var2.LungeSoundTime > 5000L) {
         String var4 = "MaleZombieAttack";
         if (var2.isFemale()) {
            var4 = "FemaleZombieAttack";
         }

         if (GameServer.bServer) {
            GameServer.sendZombieSound(IsoZombie.ZombieSound.Lunge, var2);
         }

         var2.LungeSoundTime = System.currentTimeMillis();
      }

      var2.LungeTimer = 180.0F;
      var3.put(PARAM_TICK_COUNT, IngameState.instance.numberTicks);
   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      var1.setOnFloor(false);
      var1.setShootable(true);
      if (var2.bLunger) {
         var2.walkVariantUse = "ZombieWalk3";
      }

      var2.LungeTimer -= GameTime.getInstance().getMultiplier() / 1.6F;
      IsoPlayer var4 = (IsoPlayer)Type.tryCastTo(var2.getTarget(), IsoPlayer.class);
      if (var4 != null && var4.isGhostMode()) {
         var2.LungeTimer = 0.0F;
      }

      if (var2.LungeTimer < 0.0F) {
         var2.LungeTimer = 0.0F;
      }

      if (var2.LungeTimer <= 0.0F) {
         var2.AllowRepathDelay = 0.0F;
      }

      this.temp.x = var2.vectorToTarget.x;
      this.temp.y = var2.vectorToTarget.y;
      var2.getZombieLungeSpeed();
      this.temp.normalize();
      var2.setForwardDirection(this.temp);
      var2.DirectionFromVector(this.temp);
      var2.getVectorFromDirection(var2.getForwardDirection());
      var2.setForwardDirection(this.temp);
      if (!var2.isTargetLocationKnown() && var2.LastTargetSeenX != -1 && !var1.getPathFindBehavior2().isTargetLocation((float)var2.LastTargetSeenX + 0.5F, (float)var2.LastTargetSeenY + 0.5F, (float)var2.LastTargetSeenZ)) {
         var2.LungeTimer = 0.0F;
         var1.pathToLocation(var2.LastTargetSeenX, var2.LastTargetSeenY, var2.LastTargetSeenZ);
      }

      long var5 = (Long)var3.get(PARAM_TICK_COUNT);
      if (IngameState.instance.numberTicks - var5 == 2L) {
         ((IsoZombie)var1).parameterZombieState.setState(ParameterZombieState.State.LockTarget);
      }

   }

   public void exit(IsoGameCharacter var1) {
   }

   public boolean isMoving(IsoGameCharacter var1) {
      return true;
   }
}
