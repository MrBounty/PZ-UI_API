package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.network.GameClient;
import zombie.util.StringUtils;

public final class PlayerActionsState extends State {
   private static final PlayerActionsState _instance = new PlayerActionsState();

   public static PlayerActionsState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      InventoryItem var2 = var1.getPrimaryHandItem();
      InventoryItem var3 = var1.getSecondaryHandItem();
      if (!(var2 instanceof HandWeapon) && !(var3 instanceof HandWeapon)) {
         var1.setHideWeaponModel(true);
      }

      if (GameClient.bClient && var1 instanceof IsoPlayer && var1.isLocal() && !var1.getCharacterActions().isEmpty() && var1.getNetworkCharacterAI().getAction() == null) {
         var1.getNetworkCharacterAI().setAction((BaseAction)var1.getCharacterActions().get(0));
         GameClient.sendAction(var1.getNetworkCharacterAI().getAction(), true);
      }

   }

   public void execute(IsoGameCharacter var1) {
      if (GameClient.bClient && var1 instanceof IsoPlayer && var1.isLocal() && !var1.getCharacterActions().isEmpty() && var1.getNetworkCharacterAI().getAction() != var1.getCharacterActions().get(0)) {
         GameClient.sendAction(var1.getNetworkCharacterAI().getAction(), false);
         var1.getNetworkCharacterAI().setAction((BaseAction)var1.getCharacterActions().get(0));
         GameClient.sendAction(var1.getNetworkCharacterAI().getAction(), true);
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.setHideWeaponModel(false);
      if (GameClient.bClient && var1 instanceof IsoPlayer && var1.isLocal() && var1.getNetworkCharacterAI().getAction() != null) {
         GameClient.sendAction(var1.getNetworkCharacterAI().getAction(), false);
         var1.getNetworkCharacterAI().setAction((BaseAction)null);
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (GameClient.bClient && var2 != null && var1 instanceof IsoPlayer && var1.getNetworkCharacterAI().getAction() != null && !var1.isLocal() && "changeWeaponSprite".equalsIgnoreCase(var2.m_EventName) && !StringUtils.isNullOrEmpty(var2.m_ParameterValue)) {
         if ("original".equals(var2.m_ParameterValue)) {
            var1.getNetworkCharacterAI().setOverride(false, (String)null, (String)null);
         } else {
            var1.getNetworkCharacterAI().setOverride(true, var2.m_ParameterValue, (String)null);
         }
      }

   }
}
