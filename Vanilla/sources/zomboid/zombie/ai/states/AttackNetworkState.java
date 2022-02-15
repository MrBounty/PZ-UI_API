package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoMovingObject;
import zombie.network.GameClient;
import zombie.util.StringUtils;

public class AttackNetworkState extends State {
   private static final AttackNetworkState s_instance = new AttackNetworkState();
   private String attackOutcome;

   public static AttackNetworkState instance() {
      return s_instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      var3.clear();
      var3.put(0, Boolean.FALSE);
      this.attackOutcome = var1.getVariableString("AttackOutcome");
      var1.setVariable("AttackOutcome", "start");
      var1.clearVariable("AttackDidDamage");
      var1.clearVariable("ZombieBiteDone");
      var2.setTargetSeenTime(1.0F);
      if (!var2.bCrawling) {
         var2.setVariable("AttackType", "bite");
      }

   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      IsoGameCharacter var4 = (IsoGameCharacter)var2.target;
      if (var4 == null || !"Chainsaw".equals(var4.getVariableString("ZombieHitReaction"))) {
         String var5 = var1.getVariableString("AttackOutcome");
         if ("success".equals(var5) && !var1.getVariableBoolean("bAttack") && (var4 == null || !var4.isGodMod()) && !var1.getVariableBoolean("AttackDidDamage") && var1.getVariableString("ZombieBiteDone") != "true") {
            var1.setVariable("AttackOutcome", "interrupted");
         }

         if (var4 == null || var4.isDead()) {
            var2.setTargetSeenTime(10.0F);
         }

         if (var4 != null && var3.get(0) == Boolean.FALSE && !"started".equals(var5) && !StringUtils.isNullOrEmpty(var1.getVariableString("PlayerHitReaction"))) {
            var3.put(0, Boolean.TRUE);
         }

         var2.setShootable(true);
         if (var2.target != null && !var2.bCrawling) {
            if (!"fail".equals(var5) && !"interrupted".equals(var5)) {
               var2.faceThisObject(var2.target);
            }

            var2.setOnFloor(false);
         }

         if (var2.target != null) {
            var2.target.setTimeSinceZombieAttack(0);
            var2.target.setLastTargettedBy(var2);
         }

         if (!var2.bCrawling) {
            var2.setVariable("AttackType", "bite");
         }

      }
   }

   public void exit(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      var1.clearVariable("AttackOutcome");
      var1.clearVariable("AttackType");
      var1.clearVariable("PlayerHitReaction");
      var1.setStateMachineLocked(false);
      if (var2.target != null && var2.target.isOnFloor()) {
         var2.setEatBodyTarget(var2.target, true);
         var2.setTarget((IsoMovingObject)null);
      }

      var2.AllowRepathDelay = 0.0F;
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      IsoZombie var3 = (IsoZombie)var1;
      if (GameClient.bClient && var3.isRemoteZombie()) {
         if (var2.m_EventName.equalsIgnoreCase("SetAttackOutcome")) {
            var3.setVariable("AttackOutcome", "fail".equals(this.attackOutcome) ? "fail" : "success");
         }

         if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck") && var3.target instanceof IsoPlayer) {
            IsoPlayer var4 = (IsoPlayer)var3.target;
            if (var3.scratch) {
               var3.getEmitter().playSoundImpl("ZombieScratch", var3);
            } else if (var3.laceration) {
               var3.getEmitter().playSoundImpl("ZombieScratch", var3);
            } else {
               var3.getEmitter().playSoundImpl("ZombieBite", var3);
               var4.splatBloodFloorBig();
               var4.splatBloodFloorBig();
               var4.splatBloodFloorBig();
            }
         }

         if (var2.m_EventName.equalsIgnoreCase("EatBody")) {
            var1.setVariable("EatingStarted", true);
            ((IsoZombie)var1).setEatBodyTarget(((IsoZombie)var1).target, true);
            ((IsoZombie)var1).setTarget((IsoMovingObject)null);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("SetState")) {
         var3.parameterZombieState.setState(ParameterZombieState.State.Attack);
      }

   }

   public boolean isAttacking(IsoGameCharacter var1) {
      return true;
   }
}
