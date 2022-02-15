package zombie.ai.states;

import java.util.HashMap;
import zombie.GameTime;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMovingObject;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.network.GameClient;

public final class ZombieHitReactionState extends State {
   private static final ZombieHitReactionState _instance = new ZombieHitReactionState();
   private static final int TURN_TO_PLAYER = 1;
   private static final int HIT_REACTION_TIMER = 2;

   public static ZombieHitReactionState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      var2.collideWhileHit = true;
      HashMap var3 = var1.getStateMachineParams(this);
      var3.put(1, Boolean.FALSE);
      var3.put(2, 0.0F);
      var1.clearVariable("onknees");
      if (var2.isSitAgainstWall()) {
         var1.setHitReaction((String)null);
      }

   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var1.setOnFloor(((IsoZombie)var1).isKnockedDown());
      var2.put(2, (Float)var2.get(2) + GameTime.getInstance().getMultiplier());
      if (var2.get(1) == Boolean.TRUE) {
         if (!var1.isHitFromBehind()) {
            var1.setDir(IsoDirections.reverse(IsoDirections.fromAngle(var1.getHitDir())));
         } else {
            var1.setDir(IsoDirections.fromAngle(var1.getHitDir()));
         }
      } else if (var1.hasAnimationPlayer()) {
         var1.getAnimationPlayer().setTargetToAngle();
      }

   }

   public void exit(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      var2.collideWhileHit = true;
      if (var2.target != null) {
         var2.AllowRepathDelay = 0.0F;
         var2.spotted(var2.target, true);
      }

      var2.setStaggerBack(false);
      var2.setHitReaction("");
      var2.setEatBodyTarget((IsoMovingObject)null, false);
      var2.setSitAgainstWall(false);
      var2.setShootable(true);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      IsoZombie var4 = (IsoZombie)var1;
      if (var2.m_EventName.equalsIgnoreCase("DoDeath") && Boolean.parseBoolean(var2.m_ParameterValue) && var1.isAlive()) {
         var1.Kill(var1.getAttackedBy());
         if (GameClient.bClient) {
            GameClient.sendKillZombie(var4);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("PlayDeathSound")) {
         var1.setDoDeathSound(false);
         var1.playDeadSound();
      }

      if (var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing")) {
      }

      if (var2.m_EventName.equalsIgnoreCase("Collide") && ((IsoZombie)var1).speedType == 1) {
         ((IsoZombie)var1).collideWhileHit = false;
      }

      boolean var5;
      if (var2.m_EventName.equalsIgnoreCase("ZombieTurnToPlayer")) {
         var5 = Boolean.parseBoolean(var2.m_ParameterValue);
         var3.put(1, var5 ? Boolean.TRUE : Boolean.FALSE);
      }

      if (var2.m_EventName.equalsIgnoreCase("CancelKnockDown")) {
         var5 = Boolean.parseBoolean(var2.m_ParameterValue);
         if (var5) {
            ((IsoZombie)var1).setKnockedDown(false);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("KnockDown")) {
         var1.setOnFloor(true);
         ((IsoZombie)var1).setKnockedDown(true);
      }

      if (var2.m_EventName.equalsIgnoreCase("SplatBlood")) {
         var4.addBlood((BloodBodyPartType)null, true, false, false);
         var4.addBlood((BloodBodyPartType)null, true, false, false);
         var4.addBlood((BloodBodyPartType)null, true, false, false);
         var4.playBloodSplatterSound();

         for(int var6 = 0; var6 < 10; ++var6) {
            var4.getCurrentSquare().getChunk().addBloodSplat(var4.x + Rand.Next(-0.5F, 0.5F), var4.y + Rand.Next(-0.5F, 0.5F), var4.z, Rand.Next(8));
            if (Rand.Next(5) == 0) {
               new IsoZombieGiblets(IsoZombieGiblets.GibletType.B, var4.getCell(), var4.getX(), var4.getY(), var4.getZ() + 0.3F, Rand.Next(-0.2F, 0.2F) * 1.5F, Rand.Next(-0.2F, 0.2F) * 1.5F);
            } else {
               new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var4.getCell(), var4.getX(), var4.getY(), var4.getZ() + 0.3F, Rand.Next(-0.2F, 0.2F) * 1.5F, Rand.Next(-0.2F, 0.2F) * 1.5F);
            }
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("SetState") && !var4.isDead()) {
         if (var4.getAttackedBy() != null && var4.getAttackedBy().getVehicle() != null && "Floor".equals(var4.getHitReaction())) {
            var4.parameterZombieState.setState(ParameterZombieState.State.RunOver);
            return;
         }

         var4.parameterZombieState.setState(ParameterZombieState.State.Hit);
      }

   }
}
