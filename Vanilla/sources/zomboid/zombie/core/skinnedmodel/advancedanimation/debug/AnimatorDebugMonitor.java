package zombie.core.skinnedmodel.advancedanimation.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Map.Entry;
import zombie.characters.IsoGameCharacter;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.skinnedmodel.advancedanimation.AnimLayer;
import zombie.core.skinnedmodel.advancedanimation.IAnimationVariableSlot;
import zombie.core.skinnedmodel.advancedanimation.LiveAnimNode;
import zombie.core.skinnedmodel.animation.AnimationTrack;

public final class AnimatorDebugMonitor {
   private static final ArrayList knownVariables = new ArrayList();
   private static boolean knownVarsDirty = false;
   private String currentState = "null";
   private AnimatorDebugMonitor.MonitoredLayer[] monitoredLayers;
   private final HashMap monitoredVariables = new HashMap();
   private final ArrayList customVariables = new ArrayList();
   private final LinkedList logLines = new LinkedList();
   private final Queue logLineQueue = new LinkedList();
   private boolean floatsListDirty = false;
   private boolean hasFilterChanges = false;
   private boolean hasLogUpdates = false;
   private String logString = "";
   private static final int maxLogSize = 1028;
   private static final int maxOutputLines = 128;
   private static final int maxFloatCache = 1024;
   private final ArrayList floatsOut = new ArrayList();
   private AnimatorDebugMonitor.MonitoredVar selectedVariable;
   private int tickCount = 0;
   private boolean doTickStamps = false;
   private static final int tickStampLength = 10;
   private static final Color col_curstate;
   private static final Color col_layer_nodename;
   private static final Color col_layer_activated;
   private static final Color col_layer_deactivated;
   private static final Color col_track_activated;
   private static final Color col_track_deactivated;
   private static final Color col_node_activated;
   private static final Color col_node_deactivated;
   private static final Color col_var_activated;
   private static final Color col_var_changed;
   private static final Color col_var_deactivated;
   private static final String TAG_VAR = "[variable]";
   private static final String TAG_LAYER = "[layer]";
   private static final String TAG_NODE = "[active_nodes]";
   private static final String TAG_TRACK = "[anim_tracks]";
   private boolean[] logFlags;

   public AnimatorDebugMonitor(IsoGameCharacter var1) {
      this.logFlags = new boolean[AnimatorDebugMonitor.LogType.MAX.value()];
      this.logFlags[AnimatorDebugMonitor.LogType.DEFAULT.value()] = true;

      int var2;
      for(var2 = 0; var2 < this.logFlags.length; ++var2) {
         this.logFlags[var2] = true;
      }

      for(var2 = 0; var2 < 1024; ++var2) {
         this.floatsOut.add(0.0F);
      }

      this.initCustomVars();
      if (var1 != null && var1.advancedAnimator != null) {
         ArrayList var5 = var1.advancedAnimator.debugGetVariables();
         Iterator var3 = var5.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            registerVariable(var4);
         }
      }

   }

   private void initCustomVars() {
      this.addCustomVariable("aim");
      this.addCustomVariable("bdead");
      this.addCustomVariable("bfalling");
      this.addCustomVariable("baimatfloor");
      this.addCustomVariable("battackfrombehind");
      this.addCustomVariable("attacktype");
      this.addCustomVariable("bundervehicle");
      this.addCustomVariable("reanimatetimer");
      this.addCustomVariable("isattacking");
      this.addCustomVariable("canclimbdownrope");
      this.addCustomVariable("frombehind");
      this.addCustomVariable("fallonfront");
      this.addCustomVariable("hashitreaction");
      this.addCustomVariable("hitreaction");
      this.addCustomVariable("collided");
      this.addCustomVariable("collidetype");
      this.addCustomVariable("intrees");
   }

   public void addCustomVariable(String var1) {
      String var2 = var1.toLowerCase();
      if (!this.customVariables.contains(var2)) {
         this.customVariables.add(var2);
      }

      registerVariable(var1);
   }

   public void removeCustomVariable(String var1) {
      String var2 = var1.toLowerCase();
      this.customVariables.remove(var2);
   }

   public void setFilter(int var1, boolean var2) {
      if (var1 >= 0 && var1 < AnimatorDebugMonitor.LogType.MAX.value()) {
         this.logFlags[var1] = var2;
         this.hasFilterChanges = true;
      }

   }

   public boolean getFilter(int var1) {
      return var1 >= 0 && var1 < AnimatorDebugMonitor.LogType.MAX.value() ? this.logFlags[var1] : false;
   }

   public boolean isDoTickStamps() {
      return this.doTickStamps;
   }

   public void setDoTickStamps(boolean var1) {
      if (this.doTickStamps != var1) {
         this.doTickStamps = var1;
         this.hasFilterChanges = true;
      }

   }

   private void queueLogLine(String var1) {
      this.addLogLine(AnimatorDebugMonitor.LogType.DEFAULT, var1, (Color)null, true);
   }

   private void queueLogLine(String var1, Color var2) {
      this.addLogLine(AnimatorDebugMonitor.LogType.DEFAULT, var1, var2, true);
   }

   private void queueLogLine(AnimatorDebugMonitor.LogType var1, String var2, Color var3) {
      this.addLogLine(var1, var2, var3, true);
   }

   private void addLogLine(String var1) {
      this.addLogLine(AnimatorDebugMonitor.LogType.DEFAULT, var1, (Color)null, false);
   }

   private void addLogLine(String var1, Color var2) {
      this.addLogLine(AnimatorDebugMonitor.LogType.DEFAULT, var1, var2, false);
   }

   private void addLogLine(String var1, Color var2, boolean var3) {
      this.addLogLine(AnimatorDebugMonitor.LogType.DEFAULT, var1, var2, var3);
   }

   private void addLogLine(AnimatorDebugMonitor.LogType var1, String var2, Color var3) {
      this.addLogLine(var1, var2, var3, false);
   }

   private void addLogLine(AnimatorDebugMonitor.LogType var1, String var2, Color var3, boolean var4) {
      AnimatorDebugMonitor.MonitorLogLine var5 = new AnimatorDebugMonitor.MonitorLogLine();
      var5.line = var2;
      var5.color = var3;
      var5.type = var1;
      var5.tick = this.tickCount;
      if (var4) {
         this.logLineQueue.add(var5);
      } else {
         this.log(var5);
      }

   }

   private void log(AnimatorDebugMonitor.MonitorLogLine var1) {
      this.logLines.addFirst(var1);
      if (this.logLines.size() > 1028) {
         this.logLines.removeLast();
      }

      this.hasLogUpdates = true;
   }

   private void processQueue() {
      while(this.logLineQueue.size() > 0) {
         AnimatorDebugMonitor.MonitorLogLine var1 = (AnimatorDebugMonitor.MonitorLogLine)this.logLineQueue.poll();
         this.log(var1);
      }

   }

   private void preUpdate() {
      Entry var2;
      for(Iterator var1 = this.monitoredVariables.entrySet().iterator(); var1.hasNext(); ((AnimatorDebugMonitor.MonitoredVar)var2.getValue()).updated = false) {
         var2 = (Entry)var1.next();
      }

      for(int var5 = 0; var5 < this.monitoredLayers.length; ++var5) {
         AnimatorDebugMonitor.MonitoredLayer var6 = this.monitoredLayers[var5];
         var6.updated = false;

         Iterator var3;
         Entry var4;
         for(var3 = var6.activeNodes.entrySet().iterator(); var3.hasNext(); ((AnimatorDebugMonitor.MonitoredNode)var4.getValue()).updated = false) {
            var4 = (Entry)var3.next();
         }

         for(var3 = var6.animTracks.entrySet().iterator(); var3.hasNext(); ((AnimatorDebugMonitor.MonitoredTrack)var4.getValue()).updated = false) {
            var4 = (Entry)var3.next();
         }
      }

   }

   private void postUpdate() {
      Iterator var1 = this.monitoredVariables.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         if (((AnimatorDebugMonitor.MonitoredVar)var2.getValue()).active && !((AnimatorDebugMonitor.MonitoredVar)var2.getValue()).updated) {
            this.addLogLine(AnimatorDebugMonitor.LogType.VAR, "[variable] : removed -> '" + (String)var2.getKey() + "', last value: '" + ((AnimatorDebugMonitor.MonitoredVar)var2.getValue()).value + "'.", col_var_deactivated);
            ((AnimatorDebugMonitor.MonitoredVar)var2.getValue()).active = false;
         }
      }

      for(int var5 = 0; var5 < this.monitoredLayers.length; ++var5) {
         AnimatorDebugMonitor.MonitoredLayer var6 = this.monitoredLayers[var5];
         Iterator var3 = var6.activeNodes.entrySet().iterator();

         Entry var4;
         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            if (((AnimatorDebugMonitor.MonitoredNode)var4.getValue()).active && !((AnimatorDebugMonitor.MonitoredNode)var4.getValue()).updated) {
               this.addLogLine(AnimatorDebugMonitor.LogType.NODE, "[layer][" + var6.index + "] [active_nodes] : deactivated -> '" + ((AnimatorDebugMonitor.MonitoredNode)var4.getValue()).name + "'.", col_node_deactivated);
               ((AnimatorDebugMonitor.MonitoredNode)var4.getValue()).active = false;
            }
         }

         var3 = var6.animTracks.entrySet().iterator();

         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            if (((AnimatorDebugMonitor.MonitoredTrack)var4.getValue()).active && !((AnimatorDebugMonitor.MonitoredTrack)var4.getValue()).updated) {
               this.addLogLine(AnimatorDebugMonitor.LogType.TRACK, "[layer][" + var6.index + "] [anim_tracks] : deactivated -> '" + ((AnimatorDebugMonitor.MonitoredTrack)var4.getValue()).name + "'.", col_track_deactivated);
               ((AnimatorDebugMonitor.MonitoredTrack)var4.getValue()).active = false;
            }
         }

         if (var6.active && !var6.updated) {
            this.addLogLine(AnimatorDebugMonitor.LogType.LAYER, "[layer][" + var5 + "] : deactivated (last animstate: '" + var6.nodeName + "').", col_layer_deactivated);
            var6.active = false;
         }
      }

   }

   public void update(IsoGameCharacter var1, AnimLayer[] var2) {
      if (var1 != null) {
         this.ensureLayers(var2);
         this.preUpdate();
         Iterator var3 = var1.getGameVariables().iterator();

         while(var3.hasNext()) {
            IAnimationVariableSlot var4 = (IAnimationVariableSlot)var3.next();
            this.updateVariable(var4.getKey(), var4.getValueString());
         }

         Iterator var7 = this.customVariables.iterator();

         while(var7.hasNext()) {
            String var5 = (String)var7.next();
            String var6 = var1.getVariableString(var5);
            if (var6 != null) {
               this.updateVariable(var5, var6);
            }
         }

         this.updateCurrentState(var1.getCurrentState() == null ? "null" : var1.getCurrentState().getClass().getSimpleName());

         for(int var8 = 0; var8 < var2.length; ++var8) {
            if (var2[var8] != null) {
               this.updateLayer(var8, var2[var8]);
            }
         }

         this.postUpdate();
         this.processQueue();
         ++this.tickCount;
      }
   }

   private void updateCurrentState(String var1) {
      if (!this.currentState.equals(var1)) {
         this.queueLogLine("Character.currentState changed from '" + this.currentState + "' to: '" + var1 + "'.", col_curstate);
         this.currentState = var1;
      }

   }

   private void updateLayer(int var1, AnimLayer var2) {
      AnimatorDebugMonitor.MonitoredLayer var3 = this.monitoredLayers[var1];
      String var4 = var2.getDebugNodeName();
      if (!var3.active) {
         var3.active = true;
         this.queueLogLine(AnimatorDebugMonitor.LogType.LAYER, "[layer][" + var1 + "] activated -> animstate: '" + var4 + "'.", col_layer_activated);
      }

      if (!var3.nodeName.equals(var4)) {
         this.queueLogLine(AnimatorDebugMonitor.LogType.LAYER, "[layer][" + var1 + "] changed -> animstate from '" + var3.nodeName + "' to: '" + var4 + "'.", col_layer_nodename);
         var3.nodeName = var4;
      }

      Iterator var5 = var2.getLiveAnimNodes().iterator();

      while(var5.hasNext()) {
         LiveAnimNode var6 = (LiveAnimNode)var5.next();
         this.updateActiveNode(var3, var6.getSourceNode().m_Name);
      }

      if (var2.getAnimationTrack() != null) {
         var5 = var2.getAnimationTrack().getTracks().iterator();

         while(var5.hasNext()) {
            AnimationTrack var7 = (AnimationTrack)var5.next();
            if (var7.getLayerIdx() == var1) {
               this.updateAnimTrack(var3, var7.name, var7.BlendDelta);
            }
         }
      }

      var3.updated = true;
   }

   private void updateActiveNode(AnimatorDebugMonitor.MonitoredLayer var1, String var2) {
      AnimatorDebugMonitor.MonitoredNode var3 = (AnimatorDebugMonitor.MonitoredNode)var1.activeNodes.get(var2);
      if (var3 == null) {
         var3 = new AnimatorDebugMonitor.MonitoredNode();
         var3.name = var2;
         var1.activeNodes.put(var2, var3);
      }

      if (!var3.active) {
         var3.active = true;
         this.queueLogLine(AnimatorDebugMonitor.LogType.NODE, "[layer][" + var1.index + "] [active_nodes] : activated -> '" + var2 + "'.", col_node_activated);
      }

      var3.updated = true;
   }

   private void updateAnimTrack(AnimatorDebugMonitor.MonitoredLayer var1, String var2, float var3) {
      AnimatorDebugMonitor.MonitoredTrack var4 = (AnimatorDebugMonitor.MonitoredTrack)var1.animTracks.get(var2);
      if (var4 == null) {
         var4 = new AnimatorDebugMonitor.MonitoredTrack();
         var4.name = var2;
         var4.blendDelta = var3;
         var1.animTracks.put(var2, var4);
      }

      if (!var4.active) {
         var4.active = true;
         this.queueLogLine(AnimatorDebugMonitor.LogType.TRACK, "[layer][" + var1.index + "] [anim_tracks] : activated -> '" + var2 + "'.", col_track_activated);
      }

      if (var4.blendDelta != var3) {
         var4.blendDelta = var3;
      }

      var4.updated = true;
   }

   private void updateVariable(String var1, String var2) {
      AnimatorDebugMonitor.MonitoredVar var3 = (AnimatorDebugMonitor.MonitoredVar)this.monitoredVariables.get(var1);
      boolean var4 = false;
      if (var3 == null) {
         var3 = new AnimatorDebugMonitor.MonitoredVar();
         this.monitoredVariables.put(var1, var3);
         var4 = true;
      }

      if (!var3.active) {
         var3.active = true;
         var3.key = var1;
         var3.value = var2;
         this.queueLogLine(AnimatorDebugMonitor.LogType.VAR, "[variable] : added -> '" + var1 + "', value: '" + var2 + "'.", col_var_activated);
         if (var4) {
            registerVariable(var1);
         }
      } else if (var2 == null) {
         if (var3.isFloat) {
            var3.isFloat = false;
            this.floatsListDirty = true;
         }

         var3.value = null;
      } else if (var3.value == null || !var3.value.equals(var2)) {
         try {
            float var5 = Float.parseFloat(var2);
            var3.logFloat(var5);
            if (!var3.isFloat) {
               var3.isFloat = true;
               this.floatsListDirty = true;
            }
         } catch (NumberFormatException var6) {
            if (var3.isFloat) {
               var3.isFloat = false;
               this.floatsListDirty = true;
            }
         }

         if (!var3.isFloat) {
            this.queueLogLine(AnimatorDebugMonitor.LogType.VAR, "[variable] : updated -> '" + var1 + "' changed from '" + var3.value + "' to: '" + var2 + "'.", col_var_changed);
         }

         var3.value = var2;
      }

      var3.updated = true;
   }

   private void buildLogString() {
      ListIterator var1 = this.logLines.listIterator(0);
      int var2 = 0;
      int var3 = 0;

      while(var1.hasNext()) {
         AnimatorDebugMonitor.MonitorLogLine var4 = (AnimatorDebugMonitor.MonitorLogLine)var1.next();
         ++var3;
         if (this.logFlags[var4.type.value()]) {
            ++var2;
            if (var2 >= 128) {
               break;
            }
         }
      }

      if (var3 == 0) {
         this.logString = "";
      } else {
         var1 = this.logLines.listIterator(var3);
         StringBuilder var6 = new StringBuilder();

         while(var1.hasPrevious()) {
            AnimatorDebugMonitor.MonitorLogLine var5 = (AnimatorDebugMonitor.MonitorLogLine)var1.previous();
            if (this.logFlags[var5.type.value()]) {
               var6.append(" <TEXT> ");
               if (this.doTickStamps) {
                  var6.append("[");
                  var6.append(String.format("%010d", var5.tick));
                  var6.append("]");
               }

               if (var5.color != null) {
                  var6.append(" <RGB:");
                  var6.append(var5.color.r);
                  var6.append(",");
                  var6.append(var5.color.g);
                  var6.append(",");
                  var6.append(var5.color.b);
                  var6.append("> ");
               }

               var6.append(var5.line);
               var6.append(" <LINE> ");
            }
         }

         this.logString = var6.toString();
         this.hasLogUpdates = false;
         this.hasFilterChanges = false;
      }
   }

   public boolean IsDirty() {
      return this.hasLogUpdates || this.hasFilterChanges;
   }

   public String getLogString() {
      if (this.hasLogUpdates || this.hasFilterChanges) {
         this.buildLogString();
      }

      return this.logString;
   }

   public boolean IsDirtyFloatList() {
      return this.floatsListDirty;
   }

   public ArrayList getFloatNames() {
      this.floatsListDirty = false;
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.monitoredVariables.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((AnimatorDebugMonitor.MonitoredVar)var3.getValue()).isFloat) {
            var1.add(((AnimatorDebugMonitor.MonitoredVar)var3.getValue()).key);
         }
      }

      Collections.sort(var1);
      return var1;
   }

   public static boolean isKnownVarsDirty() {
      return knownVarsDirty;
   }

   public static List getKnownVariables() {
      knownVarsDirty = false;
      Collections.sort(knownVariables);
      return knownVariables;
   }

   public void setSelectedVariable(String var1) {
      if (var1 == null) {
         this.selectedVariable = null;
      } else {
         this.selectedVariable = (AnimatorDebugMonitor.MonitoredVar)this.monitoredVariables.get(var1);
      }

   }

   public String getSelectedVariable() {
      return this.selectedVariable != null ? this.selectedVariable.key : null;
   }

   public float getSelectedVariableFloat() {
      return this.selectedVariable != null ? this.selectedVariable.valFloat : 0.0F;
   }

   public String getSelectedVarMinFloat() {
      return this.selectedVariable != null && this.selectedVariable.isFloat && this.selectedVariable.f_min != -1.0F ? this.selectedVariable.f_min.makeConcatWithConstants<invokedynamic>(this.selectedVariable.f_min) : "-1.0";
   }

   public String getSelectedVarMaxFloat() {
      return this.selectedVariable != null && this.selectedVariable.isFloat && this.selectedVariable.f_max != -1.0F ? this.selectedVariable.f_max.makeConcatWithConstants<invokedynamic>(this.selectedVariable.f_max) : "1.0";
   }

   public ArrayList getSelectedVarFloatList() {
      if (this.selectedVariable != null && this.selectedVariable.isFloat) {
         AnimatorDebugMonitor.MonitoredVar var1 = this.selectedVariable;
         int var2 = var1.f_index - 1;
         if (var2 < 0) {
            var2 = 0;
         }

         float var3 = var1.f_max - var1.f_min;

         for(int var5 = 0; var5 < 1024; ++var5) {
            float var4 = (var1.f_floats[var2--] - var1.f_min) / var3;
            this.floatsOut.set(var5, var4);
            if (var2 < 0) {
               var2 = var1.f_floats.length - 1;
            }
         }

         return this.floatsOut;
      } else {
         return null;
      }
   }

   public static void registerVariable(String var0) {
      if (var0 != null) {
         var0 = var0.toLowerCase();
         if (!knownVariables.contains(var0)) {
            knownVariables.add(var0);
            knownVarsDirty = true;
         }

      }
   }

   private void ensureLayers(AnimLayer[] var1) {
      int var2 = var1.length;
      if (this.monitoredLayers == null || this.monitoredLayers.length != var2) {
         this.monitoredLayers = new AnimatorDebugMonitor.MonitoredLayer[var2];

         for(int var3 = 0; var3 < var2; ++var3) {
            this.monitoredLayers[var3] = new AnimatorDebugMonitor.MonitoredLayer(var3);
         }
      }

   }

   static {
      col_curstate = Colors.Cyan;
      col_layer_nodename = Colors.CornFlowerBlue;
      col_layer_activated = Colors.DarkTurquoise;
      col_layer_deactivated = Colors.Orange;
      col_track_activated = Colors.SandyBrown;
      col_track_deactivated = Colors.Salmon;
      col_node_activated = Colors.Pink;
      col_node_deactivated = Colors.Plum;
      col_var_activated = Colors.Chartreuse;
      col_var_changed = Colors.LimeGreen;
      col_var_deactivated = Colors.Gold;
   }

   private static enum LogType {
      DEFAULT(0),
      LAYER(1),
      NODE(2),
      TRACK(3),
      VAR(4),
      MAX(5);

      private final int val;

      private LogType(int var3) {
         this.val = var3;
      }

      public int value() {
         return this.val;
      }

      // $FF: synthetic method
      private static AnimatorDebugMonitor.LogType[] $values() {
         return new AnimatorDebugMonitor.LogType[]{DEFAULT, LAYER, NODE, TRACK, VAR, MAX};
      }
   }

   private class MonitorLogLine {
      String line;
      Color color = null;
      AnimatorDebugMonitor.LogType type;
      int tick;

      private MonitorLogLine() {
         this.type = AnimatorDebugMonitor.LogType.DEFAULT;
      }
   }

   private class MonitoredVar {
      String key = "";
      String value = "";
      boolean isFloat = false;
      float valFloat;
      boolean active = false;
      boolean updated = false;
      float[] f_floats;
      int f_index = 0;
      float f_min = -1.0F;
      float f_max = 1.0F;

      public void logFloat(float var1) {
         if (this.f_floats == null) {
            this.f_floats = new float[1024];
         }

         if (var1 != this.valFloat) {
            this.valFloat = var1;
            this.f_floats[this.f_index++] = var1;
            if (var1 < this.f_min) {
               this.f_min = var1;
            }

            if (var1 > this.f_max) {
               this.f_max = var1;
            }

            if (this.f_index >= 1024) {
               this.f_index = 0;
            }

         }
      }
   }

   private class MonitoredLayer {
      int index;
      String nodeName = "";
      HashMap activeNodes = new HashMap();
      HashMap animTracks = new HashMap();
      boolean active = false;
      boolean updated = false;

      public MonitoredLayer(int var2) {
         this.index = var2;
      }
   }

   private class MonitoredNode {
      String name = "";
      boolean active = false;
      boolean updated = false;
   }

   private class MonitoredTrack {
      String name = "";
      float blendDelta;
      boolean active = false;
      boolean updated = false;
   }
}
