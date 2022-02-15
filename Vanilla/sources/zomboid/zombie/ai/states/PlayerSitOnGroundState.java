package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoFireplace;
import zombie.util.StringUtils;
import zombie.util.Type;

public final class PlayerSitOnGroundState extends State {
   private static final PlayerSitOnGroundState _instance = new PlayerSitOnGroundState();
   private static final int RAND_EXT = 2500;
   private static final Integer PARAM_FIRE = 0;
   private static final Integer PARAM_SITGROUNDANIM = 1;
   private static final Integer PARAM_CHECK_FIRE = 2;
   private static final Integer PARAM_CHANGE_ANIM = 3;

   public static PlayerSitOnGroundState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var2.put(PARAM_FIRE, this.checkFire(var1));
      var2.put(PARAM_CHECK_FIRE, System.currentTimeMillis());
      var2.put(PARAM_CHANGE_ANIM, 0L);
      var1.setSitOnGround(true);
      if ((var1.getPrimaryHandItem() == null || !(var1.getPrimaryHandItem() instanceof HandWeapon)) && (var1.getSecondaryHandItem() == null || !(var1.getSecondaryHandItem() instanceof HandWeapon))) {
         var1.setHideWeaponModel(true);
      }

      if (var1.getStateMachine().getPrevious() == IdleState.instance()) {
         var1.clearVariable("SitGroundStarted");
         var1.clearVariable("forceGetUp");
         var1.clearVariable("SitGroundAnim");
      }

   }

   private boolean checkFire(IsoGameCharacter var1) {
      IsoGridSquare var2 = var1.getCurrentSquare();

      for(int var3 = -4; var3 < 4; ++var3) {
         for(int var4 = -4; var4 < 4; ++var4) {
            IsoGridSquare var5 = var2.getCell().getGridSquare(var2.x + var3, var2.y + var4, var2.z);
            if (var5 != null) {
               if (var5.haveFire()) {
                  return true;
               }

               for(int var6 = 0; var6 < var5.getObjects().size(); ++var6) {
                  IsoFireplace var7 = (IsoFireplace)Type.tryCastTo((IsoObject)var5.getObjects().get(var6), IsoFireplace.class);
                  if (var7 != null && var7.isLit()) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoPlayer var3 = (IsoPlayer)var1;
      if (var3.pressedMovement(false)) {
         var1.StopAllActionQueue();
         var1.setVariable("forceGetUp", true);
      }

      long var4 = System.currentTimeMillis();
      if (var4 > (Long)var2.get(PARAM_CHECK_FIRE) + 5000L) {
         var2.put(PARAM_FIRE, this.checkFire(var1));
         var2.put(PARAM_CHECK_FIRE, var4);
      }

      if (var1.hasTimedActions()) {
         var2.put(PARAM_FIRE, false);
         var1.setVariable("SitGroundAnim", "Idle");
      }

      boolean var6 = (Boolean)var2.get(PARAM_FIRE);
      if (var6) {
         boolean var7 = var4 > (Long)var2.get(PARAM_CHANGE_ANIM);
         if (var7) {
            if ("Idle".equals(var1.getVariableString("SitGroundAnim"))) {
               var1.setVariable("SitGroundAnim", "WarmHands");
            } else if ("WarmHands".equals(var1.getVariableString("SitGroundAnim"))) {
               var1.setVariable("SitGroundAnim", "Idle");
            }

            var2.put(PARAM_CHANGE_ANIM, var4 + (long)Rand.Next(30000, 90000));
         }
      } else if (var1.getVariableBoolean("SitGroundStarted")) {
         var1.clearVariable("FireNear");
         var1.setVariable("SitGroundAnim", "Idle");
      }

      if ("WarmHands".equals(var1.getVariableString("SitGroundAnim")) && Rand.Next(Rand.AdjustForFramerate(2500)) == 0) {
         var2.put(PARAM_SITGROUNDANIM, var1.getVariableString("SitGroundAnim"));
         var1.setVariable("SitGroundAnim", "rubhands");
      }

      var3.setInitiateAttack(false);
      var3.attackStarted = false;
      var3.setAttackType((String)null);
   }

   public void exit(IsoGameCharacter var1) {
      var1.setHideWeaponModel(false);
      if (StringUtils.isNullOrEmpty(var1.getVariableString("HitReaction"))) {
         var1.clearVariable("SitGroundStarted");
         var1.clearVariable("forceGetUp");
         var1.clearVariable("SitGroundAnim");
         var1.setIgnoreMovement(false);
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("SitGroundStarted")) {
         var1.setVariable("SitGroundStarted", true);
         boolean var3 = (Boolean)var1.getStateMachineParams(this).get(PARAM_FIRE);
         if (var3) {
            var1.setVariable("SitGroundAnim", "WarmHands");
         } else {
            var1.setVariable("SitGroundAnim", "Idle");
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("ResetSitOnGroundAnim")) {
         var1.setVariable("SitGroundAnim", (String)var1.getStateMachineParams(this).get(PARAM_SITGROUNDANIM));
      }

   }
}
