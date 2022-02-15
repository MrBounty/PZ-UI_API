package zombie.characters.CharacterTimedActions;

import java.util.ArrayList;
import java.util.Arrays;
import zombie.GameTime;
import zombie.ai.states.PlayerActionsState;
import zombie.characters.CharacterActionAnims;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.EventPacket;
import zombie.ui.UIManager;
import zombie.util.StringUtils;
import zombie.util.Type;

public class BaseAction {
   public long SoundEffect = -1L;
   public float CurrentTime = -2.0F;
   public float LastTime = -1.0F;
   public int MaxTime = 60;
   public float PrevLastTime = 0.0F;
   public boolean UseProgressBar = true;
   public boolean ForceProgressBar = false;
   public IsoGameCharacter chr;
   public boolean StopOnWalk = true;
   public boolean StopOnRun = true;
   public boolean StopOnAim = false;
   public float caloriesModifier = 1.0F;
   public float delta = 0.0F;
   public boolean blockMovementEtc;
   public boolean overrideAnimation;
   public final ArrayList animVariables = new ArrayList();
   public boolean loopAction = false;
   public boolean bStarted = false;
   public boolean forceStop = false;
   public boolean forceComplete = false;
   private static final ArrayList specificNetworkAnim = new ArrayList(Arrays.asList("Reload", "Bandage", "Loot", "AttachItem", "Drink", "Eat", "Pour", "Read", "fill_container_tap", "drink_tap", "WearClothing"));
   private InventoryItem primaryHandItem = null;
   private InventoryItem secondaryHandItem = null;
   private String primaryHandMdl;
   private String secondaryHandMdl;
   public boolean overrideHandModels = false;

   public BaseAction(IsoGameCharacter var1) {
      this.chr = var1;
   }

   public void forceStop() {
      this.forceStop = true;
   }

   public void forceComplete() {
      this.forceComplete = true;
   }

   public void PlayLoopedSoundTillComplete(String var1, int var2, float var3) {
      this.SoundEffect = this.chr.getEmitter().playSound(var1);
   }

   public boolean hasStalled() {
      if (!this.bStarted) {
         return false;
      } else {
         return this.LastTime == this.CurrentTime && this.LastTime == this.PrevLastTime && this.LastTime < 0.0F || this.CurrentTime < 0.0F;
      }
   }

   public float getJobDelta() {
      return this.delta;
   }

   public void resetJobDelta() {
      this.delta = 0.0F;
      this.CurrentTime = 0.0F;
   }

   public void waitToStart() {
      if (!this.chr.shouldWaitToStartTimedAction()) {
         this.bStarted = true;
         this.start();
      }
   }

   public void update() {
      this.PrevLastTime = this.LastTime;
      this.LastTime = this.CurrentTime;
      this.CurrentTime += GameTime.instance.getMultiplier();
      if (this.CurrentTime < 0.0F) {
         this.CurrentTime = 0.0F;
      }

      if (this.MaxTime == 0) {
         this.delta = 0.0F;
      } else if (this.MaxTime != -1) {
         this.delta = Math.min(this.CurrentTime / (float)this.MaxTime, 1.0F);
      }

      if ((Core.getInstance().isOptionProgressBar() || this.ForceProgressBar) && this.UseProgressBar && this.chr instanceof IsoPlayer && ((IsoPlayer)this.chr).isLocalPlayer() && this.MaxTime != -1) {
         UIManager.getProgressBar((double)((IsoPlayer)this.chr).getPlayerNum()).setValue(this.delta);
      }

   }

   public void start() {
      this.forceComplete = false;
      this.forceStop = false;
      if (this.chr.isCurrentState(PlayerActionsState.instance())) {
         InventoryItem var1 = this.chr.getPrimaryHandItem();
         InventoryItem var2 = this.chr.getSecondaryHandItem();
         this.chr.setHideWeaponModel(!(var1 instanceof HandWeapon) && !(var2 instanceof HandWeapon));
      }

   }

   public void reset() {
      this.CurrentTime = 0.0F;
      this.forceComplete = false;
      this.forceStop = false;
   }

   public float getCurrentTime() {
      return this.CurrentTime;
   }

   public void stop() {
      if (this.SoundEffect > -1L) {
         this.chr.getEmitter().stopSound(this.SoundEffect);
         this.SoundEffect = -1L;
      }

      this.stopTimedActionAnim();
   }

   public boolean valid() {
      return true;
   }

   public boolean finished() {
      return this.CurrentTime >= (float)this.MaxTime && this.MaxTime != -1;
   }

   public void perform() {
      if (!this.loopAction) {
         this.stopTimedActionAnim();
      }

   }

   public void setUseProgressBar(boolean var1) {
      this.UseProgressBar = var1;
   }

   public void setBlockMovementEtc(boolean var1) {
      this.blockMovementEtc = var1;
   }

   public void setOverrideAnimation(boolean var1) {
      this.overrideAnimation = var1;
   }

   public void stopTimedActionAnim() {
      for(int var1 = 0; var1 < this.animVariables.size(); ++var1) {
         String var2 = (String)this.animVariables.get(var1);
         this.chr.clearVariable(var2);
      }

      this.chr.setVariable("IsPerformingAnAction", false);
      if (this.overrideHandModels) {
         this.overrideHandModels = false;
         this.chr.resetEquippedHandsModels();
      }

   }

   public void setAnimVariable(String var1, String var2) {
      if (!this.animVariables.contains(var1)) {
         this.animVariables.add(var1);
      }

      this.chr.setVariable(var1, var2);
   }

   public void setAnimVariable(String var1, boolean var2) {
      if (!this.animVariables.contains(var1)) {
         this.animVariables.add(var1);
      }

      this.chr.setVariable(var1, String.valueOf(var2));
   }

   public String getPrimaryHandMdl() {
      return this.primaryHandMdl;
   }

   public String getSecondaryHandMdl() {
      return this.secondaryHandMdl;
   }

   public InventoryItem getPrimaryHandItem() {
      return this.primaryHandItem;
   }

   public InventoryItem getSecondaryHandItem() {
      return this.secondaryHandItem;
   }

   public void setActionAnim(CharacterActionAnims var1) {
      this.setActionAnim(var1.toString());
   }

   public void setActionAnim(String var1) {
      this.setAnimVariable("PerformingAction", var1);
      this.chr.setVariable("IsPerformingAnAction", true);
      if (Core.bDebug) {
         this.chr.advancedAnimator.printDebugCharacterActions(var1);
      }

   }

   public void setOverrideHandModels(InventoryItem var1, InventoryItem var2) {
      this.setOverrideHandModels(var1, var2, true);
   }

   public void setOverrideHandModels(InventoryItem var1, InventoryItem var2, boolean var3) {
      this.setOverrideHandModelsObject(var1, var2, var3);
   }

   public void setOverrideHandModelsString(String var1, String var2) {
      this.setOverrideHandModelsString(var1, var2, true);
   }

   public void setOverrideHandModelsString(String var1, String var2, boolean var3) {
      this.setOverrideHandModelsObject(var1, var2, var3);
   }

   public void setOverrideHandModelsObject(Object var1, Object var2, boolean var3) {
      this.overrideHandModels = true;
      this.primaryHandItem = (InventoryItem)Type.tryCastTo(var1, InventoryItem.class);
      this.secondaryHandItem = (InventoryItem)Type.tryCastTo(var2, InventoryItem.class);
      this.primaryHandMdl = StringUtils.discardNullOrWhitespace((String)Type.tryCastTo(var1, String.class));
      this.secondaryHandMdl = StringUtils.discardNullOrWhitespace((String)Type.tryCastTo(var2, String.class));
      if (var3) {
         this.chr.resetEquippedHandsModels();
      }

      if (this.primaryHandItem != null || this.secondaryHandItem != null) {
         this.chr.reportEvent(EventPacket.EventType.EventOverrideItem.name());
      }

   }

   public void OnAnimEvent(AnimEvent var1) {
   }

   public void setLoopedAction(boolean var1) {
      this.loopAction = var1;
   }
}
