package zombie.core.skinnedmodel.advancedanimation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.w3c.dom.Element;
import zombie.DebugFileWatcher;
import zombie.GameProfiler;
import zombie.PredicatedFileWatcher;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.characters.CharacterActionAnims;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.gameStates.ChooseGameInfo;
import zombie.util.Lambda;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.list.PZArrayList;
import zombie.util.list.PZArrayUtil;

public final class AdvancedAnimator implements IAnimEventCallback {
   private IAnimatable character;
   public AnimationSet animSet;
   public final ArrayList animCallbackHandlers = new ArrayList();
   private AnimLayer m_rootLayer = null;
   private final List m_subLayers = new ArrayList();
   public static float s_MotionScale = 0.76F;
   public static float s_RotationScale = 0.76F;
   private AnimatorDebugMonitor debugMonitor;
   private static long animSetModificationTime = -1L;
   private static long actionGroupModificationTime = -1L;
   private AnimationPlayerRecorder m_recorder = null;

   public static void systemInit() {
      DebugFileWatcher.instance.add(new PredicatedFileWatcher("media/AnimSets", AdvancedAnimator::isAnimSetFilePath, AdvancedAnimator::onAnimSetsRefreshTriggered));
      DebugFileWatcher.instance.add(new PredicatedFileWatcher("media/actiongroups", AdvancedAnimator::isActionGroupFilePath, AdvancedAnimator::onActionGroupsRefreshTriggered));
      LoadDefaults();
   }

   private static boolean isAnimSetFilePath(String var0) {
      if (var0 == null) {
         return false;
      } else if (!var0.endsWith(".xml")) {
         return false;
      } else {
         ArrayList var1 = ZomboidFileSystem.instance.getModIDs();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            String var3 = (String)var1.get(var2);
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var3);
            if (var4 != null && var4.animSetsFile != null && var0.startsWith(var4.animSetsFile.getPath())) {
               return true;
            }
         }

         String var5 = ZomboidFileSystem.instance.getAnimSetsPath();
         if (!var0.startsWith(var5)) {
            return false;
         } else {
            return true;
         }
      }
   }

   private static boolean isActionGroupFilePath(String var0) {
      if (var0 == null) {
         return false;
      } else if (!var0.endsWith(".xml")) {
         return false;
      } else {
         ArrayList var1 = ZomboidFileSystem.instance.getModIDs();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            String var3 = (String)var1.get(var2);
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var3);
            if (var4 != null && var4.actionGroupsFile != null && var0.startsWith(var4.actionGroupsFile.getPath())) {
               return true;
            }
         }

         String var5 = ZomboidFileSystem.instance.getActionGroupsPath();
         if (!var0.startsWith(var5)) {
            return false;
         } else {
            return true;
         }
      }
   }

   private static void onActionGroupsRefreshTriggered(String var0) {
      DebugLog.General.println("DebugFileWatcher Hit. ActionGroups: " + var0);
      actionGroupModificationTime = System.currentTimeMillis() + 1000L;
   }

   private static void onAnimSetsRefreshTriggered(String var0) {
      DebugLog.General.println("DebugFileWatcher Hit. AnimSets: " + var0);
      animSetModificationTime = System.currentTimeMillis() + 1000L;
   }

   public static void checkModifiedFiles() {
      if (animSetModificationTime != -1L && animSetModificationTime < System.currentTimeMillis()) {
         DebugLog.General.println("Refreshing AnimSets.");
         animSetModificationTime = -1L;
         LoadDefaults();
         LuaManager.GlobalObject.refreshAnimSets(true);
      }

      if (actionGroupModificationTime != -1L && actionGroupModificationTime < System.currentTimeMillis()) {
         DebugLog.General.println("Refreshing action groups.");
         actionGroupModificationTime = -1L;
         LuaManager.GlobalObject.reloadActionGroups();
      }

   }

   private static void LoadDefaults() {
      try {
         Element var0 = PZXmlUtil.parseXml("media/AnimSets/Defaults.xml");
         String var1 = var0.getElementsByTagName("MotionScale").item(0).getTextContent();
         s_MotionScale = Float.parseFloat(var1);
         String var2 = var0.getElementsByTagName("RotationScale").item(0).getTextContent();
         s_RotationScale = Float.parseFloat(var2);
      } catch (PZXmlParserException var3) {
         DebugLog.General.error("Exception thrown: " + var3);
         var3.printStackTrace();
      }

   }

   public String GetDebug() {
      StringBuilder var1 = new StringBuilder();
      var1.append("GameState: ");
      if (this.character instanceof IsoGameCharacter) {
         IsoGameCharacter var2 = (IsoGameCharacter)this.character;
         var1.append(var2.getCurrentState() == null ? "null" : var2.getCurrentState().getClass().getSimpleName()).append("\n");
      }

      if (this.m_rootLayer != null) {
         var1.append("Layer: ").append(0).append("\n");
         var1.append(this.m_rootLayer.GetDebugString()).append("\n");
      }

      var1.append("Variables:\n");
      var1.append("Weapon: ").append(this.character.getVariableString("weapon")).append("\n");
      var1.append("Aim: ").append(this.character.getVariableString("aim")).append("\n");
      Iterator var4 = this.character.getGameVariables().iterator();

      while(var4.hasNext()) {
         IAnimationVariableSlot var3 = (IAnimationVariableSlot)var4.next();
         var1.append("  ").append(var3.getKey()).append(" : ").append(var3.getValueString()).append("\n");
      }

      return var1.toString();
   }

   public void OnAnimDataChanged(boolean var1) {
      if (var1 && this.character instanceof IsoGameCharacter) {
         IsoGameCharacter var2 = (IsoGameCharacter)this.character;
         ++var2.getStateMachine().activeStateChanged;
         var2.setDefaultState();
         if (var2 instanceof IsoZombie) {
            var2.setOnFloor(false);
         }

         --var2.getStateMachine().activeStateChanged;
      }

      this.SetAnimSet(AnimationSet.GetAnimationSet(this.character.GetAnimSetName(), false));
      if (this.character.getAnimationPlayer() != null) {
         this.character.getAnimationPlayer().reset();
      }

      if (this.m_rootLayer != null) {
         this.m_rootLayer.Reset();
      }

      for(int var4 = 0; var4 < this.m_subLayers.size(); ++var4) {
         AdvancedAnimator.SubLayerSlot var3 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var4);
         var3.animLayer.Reset();
      }

   }

   public void Reload() {
   }

   public void init(IAnimatable var1) {
      this.character = var1;
      this.m_rootLayer = new AnimLayer(var1, this);
   }

   public void SetAnimSet(AnimationSet var1) {
      this.animSet = var1;
   }

   public void OnAnimEvent(AnimLayer var1, AnimEvent var2) {
      for(int var3 = 0; var3 < this.animCallbackHandlers.size(); ++var3) {
         IAnimEventCallback var4 = (IAnimEventCallback)this.animCallbackHandlers.get(var3);
         var4.OnAnimEvent(var1, var2);
      }

   }

   public String getCurrentStateName() {
      return this.m_rootLayer == null ? null : this.m_rootLayer.getCurrentStateName();
   }

   public boolean containsState(String var1) {
      return this.animSet != null && this.animSet.containsState(var1);
   }

   public void SetState(String var1) {
      this.SetState(var1, PZArrayList.emptyList());
   }

   public void SetState(String var1, List var2) {
      if (this.animSet == null) {
         DebugLog.Animation.error("(" + var1 + ") Cannot set state. AnimSet is null.");
      } else {
         if (!this.animSet.containsState(var1)) {
            DebugLog.Animation.error("State not found: " + var1);
         }

         this.m_rootLayer.TransitionTo(this.animSet.GetState(var1), false);
         PZArrayUtil.forEach(this.m_subLayers, (var0) -> {
            var0.shouldBeActive = false;
         });
         Lambda.forEachFrom(PZArrayUtil::forEach, (List)var2, this, (var0, var1x) -> {
            AdvancedAnimator.SubLayerSlot var2 = var1x.getOrCreateSlot(var0);
            var2.transitionTo(var1x.animSet.GetState(var0), false);
         });
         PZArrayUtil.forEach(this.m_subLayers, AdvancedAnimator.SubLayerSlot::applyTransition);
      }
   }

   protected AdvancedAnimator.SubLayerSlot getOrCreateSlot(String var1) {
      AdvancedAnimator.SubLayerSlot var2 = null;
      int var3 = 0;

      int var4;
      AdvancedAnimator.SubLayerSlot var5;
      for(var4 = this.m_subLayers.size(); var3 < var4; ++var3) {
         var5 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var3);
         if (var5.animLayer.isCurrentState(var1)) {
            var2 = var5;
            break;
         }
      }

      if (var2 != null) {
         return var2;
      } else {
         var3 = 0;

         for(var4 = this.m_subLayers.size(); var3 < var4; ++var3) {
            var5 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var3);
            if (var5.animLayer.isStateless()) {
               var2 = var5;
               break;
            }
         }

         if (var2 != null) {
            return var2;
         } else {
            AdvancedAnimator.SubLayerSlot var6 = new AdvancedAnimator.SubLayerSlot(this.m_rootLayer, this.character, this);
            this.m_subLayers.add(var6);
            return var6;
         }
      }
   }

   public void update() {
      GameProfiler.getInstance().invokeAndMeasure("AdvancedAnimator.Update", this, AdvancedAnimator::updateInternal);
   }

   private void updateInternal() {
      if (this.character.getAnimationPlayer() != null) {
         if (this.character.getAnimationPlayer().isReady()) {
            if (this.animSet != null) {
               if (!this.m_rootLayer.hasState()) {
                  this.m_rootLayer.TransitionTo(this.animSet.GetState("Idle"), true);
               }

               this.m_rootLayer.Update();

               int var1;
               for(var1 = 0; var1 < this.m_subLayers.size(); ++var1) {
                  AdvancedAnimator.SubLayerSlot var2 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var1);
                  var2.update();
               }

               if (this.debugMonitor != null && this.character instanceof IsoGameCharacter) {
                  var1 = 1 + this.getActiveSubLayerCount();
                  AnimLayer[] var5 = new AnimLayer[var1];
                  var5[0] = this.m_rootLayer;
                  var1 = 0;

                  for(int var3 = 0; var3 < this.m_subLayers.size(); ++var3) {
                     AdvancedAnimator.SubLayerSlot var4 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var3);
                     if (var4.shouldBeActive) {
                        var5[1 + var1] = var4.animLayer;
                        ++var1;
                     }
                  }

                  this.debugMonitor.update((IsoGameCharacter)this.character, var5);
               }

            }
         }
      }
   }

   public void render() {
      if (this.character.getAnimationPlayer() != null) {
         if (this.character.getAnimationPlayer().isReady()) {
            if (this.animSet != null) {
               if (this.m_rootLayer.hasState()) {
                  this.m_rootLayer.render();
               }
            }
         }
      }
   }

   public void printDebugCharacterActions(String var1) {
      if (this.animSet != null) {
         AnimState var2 = this.animSet.GetState("actions");
         if (var2 != null) {
            boolean var4 = false;
            boolean var5 = false;
            CharacterActionAnims[] var7 = CharacterActionAnims.values();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               CharacterActionAnims var10 = var7[var9];
               var4 = false;
               String var6;
               if (var10 == CharacterActionAnims.None) {
                  var6 = var1;
                  var4 = true;
               } else {
                  var6 = var10.toString();
               }

               boolean var3 = false;
               Iterator var11 = var2.m_Nodes.iterator();

               while(var11.hasNext()) {
                  AnimNode var12 = (AnimNode)var11.next();
                  Iterator var13 = var12.m_Conditions.iterator();

                  while(var13.hasNext()) {
                     AnimCondition var14 = (AnimCondition)var13.next();
                     if (var14.m_Type == AnimCondition.Type.STRING && var14.m_Name.toLowerCase().equals("performingaction") && var14.m_StringValue.equalsIgnoreCase(var6)) {
                        var3 = true;
                        break;
                     }
                  }

                  if (var3) {
                     break;
                  }
               }

               if (var3) {
                  if (var4) {
                     var5 = true;
                  }
               } else {
                  DebugLog.log("WARNING: did not find node with condition 'PerformingAction = " + var6 + "' in player/actions/");
               }
            }

            if (var5) {
               if (DebugLog.isEnabled(DebugType.Animation)) {
                  DebugLog.Animation.debugln("SUCCESS - Current 'actions' TargetNode: '" + var1 + "' was found.");
               }
            } else if (DebugLog.isEnabled(DebugType.Animation)) {
               DebugLog.Animation.debugln("FAIL - Current 'actions' TargetNode: '" + var1 + "' not found.");
            }
         }
      }

   }

   public ArrayList debugGetVariables() {
      ArrayList var1 = new ArrayList();
      if (this.animSet != null) {
         Iterator var2 = this.animSet.states.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            AnimState var4 = (AnimState)var3.getValue();
            Iterator var5 = var4.m_Nodes.iterator();

            while(var5.hasNext()) {
               AnimNode var6 = (AnimNode)var5.next();
               Iterator var7 = var6.m_Conditions.iterator();

               while(var7.hasNext()) {
                  AnimCondition var8 = (AnimCondition)var7.next();
                  if (var8.m_Name != null && !var1.contains(var8.m_Name.toLowerCase())) {
                     var1.add(var8.m_Name.toLowerCase());
                  }
               }
            }
         }
      }

      return var1;
   }

   public AnimatorDebugMonitor getDebugMonitor() {
      return this.debugMonitor;
   }

   public void setDebugMonitor(AnimatorDebugMonitor var1) {
      this.debugMonitor = var1;
   }

   public IAnimatable getCharacter() {
      return this.character;
   }

   public void updateSpeedScale(String var1, float var2) {
      if (this.m_rootLayer != null) {
         List var3 = this.m_rootLayer.getLiveAnimNodes();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            LiveAnimNode var5 = (LiveAnimNode)var3.get(var4);
            if (var5.isActive() && var5.getSourceNode() != null && var1.equals(var5.getSourceNode().m_SpeedScaleVariable)) {
               var5.getSourceNode().m_SpeedScale = var2.makeConcatWithConstants<invokedynamic>(var2);

               for(int var6 = 0; var6 < var5.m_AnimationTracks.size(); ++var6) {
                  ((AnimationTrack)var5.m_AnimationTracks.get(var6)).SpeedDelta = var2;
               }
            }
         }
      }

   }

   public boolean containsAnyIdleNodes() {
      if (this.m_rootLayer == null) {
         return false;
      } else {
         boolean var1 = false;
         List var2 = this.m_rootLayer.getLiveAnimNodes();

         int var3;
         for(var3 = 0; var3 < var2.size() && !var1; ++var3) {
            var1 = ((LiveAnimNode)var2.get(var3)).isIdleAnimActive();
         }

         for(var3 = 0; var3 < this.getSubLayerCount(); ++var3) {
            AnimLayer var4 = this.getSubLayerAt(var3);
            var2 = var4.getLiveAnimNodes();

            for(int var5 = 0; var5 < var2.size(); ++var5) {
               var1 = ((LiveAnimNode)var2.get(var5)).isIdleAnimActive();
               if (!var1) {
                  break;
               }
            }
         }

         return var1;
      }
   }

   public AnimLayer getRootLayer() {
      return this.m_rootLayer;
   }

   public int getSubLayerCount() {
      return this.m_subLayers.size();
   }

   public AnimLayer getSubLayerAt(int var1) {
      return ((AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var1)).animLayer;
   }

   public int getActiveSubLayerCount() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.m_subLayers.size(); ++var2) {
         AdvancedAnimator.SubLayerSlot var3 = (AdvancedAnimator.SubLayerSlot)this.m_subLayers.get(var2);
         if (var3.shouldBeActive) {
            ++var1;
         }
      }

      return var1;
   }

   public void setRecorder(AnimationPlayerRecorder var1) {
      this.m_recorder = var1;
   }

   public boolean isRecording() {
      return this.m_recorder != null && this.m_recorder.isRecording();
   }

   public static class SubLayerSlot {
      public boolean shouldBeActive = false;
      public final AnimLayer animLayer;

      public SubLayerSlot(AnimLayer var1, IAnimatable var2, IAnimEventCallback var3) {
         this.animLayer = new AnimLayer(var1, var2, var3);
      }

      public void update() {
         this.animLayer.Update();
      }

      public void transitionTo(AnimState var1, boolean var2) {
         this.animLayer.TransitionTo(var1, var2);
         this.shouldBeActive = var1 != null;
      }

      public void applyTransition() {
         if (!this.shouldBeActive) {
            this.transitionTo((AnimState)null, false);
         }

      }
   }
}
