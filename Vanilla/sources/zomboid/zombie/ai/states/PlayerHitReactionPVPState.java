package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.network.GameServer;

public final class PlayerHitReactionPVPState extends State {
   private static final PlayerHitReactionPVPState _instance = new PlayerHitReactionPVPState();

   public static PlayerHitReactionPVPState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      if (!var1.getCharacterActions().isEmpty()) {
         ((BaseAction)var1.getCharacterActions().get(0)).forceStop();
      }

      var1.setSitOnGround(false);
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.setHitReaction("");
      var1.setVariable("hitpvp", false);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("PushAwayZombie")) {
         var1.getAttackedBy().setHitForce(0.03F);
         if (var1.getAttackedBy() instanceof IsoZombie) {
            ((IsoZombie)var1.getAttackedBy()).setPlayerAttackPosition((String)null);
            ((IsoZombie)var1.getAttackedBy()).setStaggerBack(true);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("Defend")) {
         var1.getAttackedBy().setHitReaction("BiteDefended");
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

   }
}
