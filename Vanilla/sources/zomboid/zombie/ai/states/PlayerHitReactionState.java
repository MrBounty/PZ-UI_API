package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.debug.DebugLog;
import zombie.inventory.types.HandWeapon;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class PlayerHitReactionState extends State {
   private static final PlayerHitReactionState _instance = new PlayerHitReactionState();

   public static PlayerHitReactionState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      if (!var1.getCharacterActions().isEmpty()) {
         ((BaseAction)var1.getCharacterActions().get(0)).forceStop();
      }

      var1.setIsAiming(false);
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.setHitReaction("");
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var1.getAttackedBy() != null && var1.getAttackedBy() instanceof IsoZombie) {
         if (var2.m_EventName.equalsIgnoreCase("PushAwayZombie")) {
            var1.getAttackedBy().setHitForce(0.03F);
            ((IsoZombie)var1.getAttackedBy()).setPlayerAttackPosition((String)null);
            ((IsoZombie)var1.getAttackedBy()).setStaggerBack(true);
         }

         if (var2.m_EventName.equalsIgnoreCase("Defend")) {
            var1.getAttackedBy().setHitReaction("BiteDefended");
            if (GameClient.bClient) {
               GameClient.sendHitCharacter(var1.getAttackedBy(), var1, (HandWeapon)null, 0.0F, false, 1.0F, false, false, false);
            }
         }

         if (var2.m_EventName.equalsIgnoreCase("DeathSound")) {
            if (var1.isPlayingDeathSound()) {
               return;
            }

            var1.setPlayingDeathSound(true);
            String var3 = "Male";
            if (var1.isFemale()) {
               var3 = "Female";
            }

            var3 = var3 + "BeingEatenDeath";
            var1.playSound(var3);
         }

         if (var2.m_EventName.equalsIgnoreCase("Death")) {
            var1.setOnFloor(true);
            if (!GameServer.bServer) {
               var1.Kill(var1.getAttackedBy());
            }
         }

      } else {
         DebugLog.log("PlayerHitReactionState.animEvent (" + var2.m_EventName + ") zombie is null");
      }
   }
}
