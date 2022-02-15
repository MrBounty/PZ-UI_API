package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoObject;

public final class PlayerStrafeState extends State {
   private static final PlayerStrafeState _instance = new PlayerStrafeState();

   public static PlayerStrafeState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      if (!"aim".equals(var1.getPreviousActionContextStateName())) {
         InventoryItem var2 = var1.getPrimaryHandItem();
         if (var2 != null && var2.getBringToBearSound() != null) {
            var1.getEmitter().playSoundImpl(var2.getBringToBearSound(), (IsoObject)null);
         }
      }

   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
   }
}
