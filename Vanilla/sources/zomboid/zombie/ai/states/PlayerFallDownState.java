package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class PlayerFallDownState extends State {
   private static final PlayerFallDownState _instance = new PlayerFallDownState();

   public static PlayerFallDownState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.clearVariable("bKnockedDown");
      if (var1.isDead() && !GameServer.bServer && !GameClient.bClient) {
         var1.Kill((IsoGameCharacter)null);
      }

   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.setOnFloor(true);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (GameClient.bClient && var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

   }
}
