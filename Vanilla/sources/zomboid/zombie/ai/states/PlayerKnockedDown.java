package zombie.ai.states;

import zombie.GameTime;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class PlayerKnockedDown extends State {
   private static final PlayerKnockedDown _instance = new PlayerKnockedDown();

   public static PlayerKnockedDown instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      ((IsoPlayer)var1).setBlockMovement(true);
      var1.setHitReaction("");
   }

   public void execute(IsoGameCharacter var1) {
      if (var1.isDead()) {
         if (!GameServer.bServer && !GameClient.bClient) {
            var1.Kill((IsoGameCharacter)null);
         }
      } else {
         var1.setReanimateTimer(var1.getReanimateTimer() - GameTime.getInstance().getMultiplier() / 1.6F);
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("FallOnBack")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("setSitOnGround")) {
         var1.setSitOnGround(Boolean.parseBoolean(var2.m_ParameterValue));
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      ((IsoPlayer)var1).setBlockMovement(false);
      ((IsoPlayer)var1).setKnockedDown(false);
      var1.setOnFloor(true);
   }
}
