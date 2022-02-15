package zombie.core.skinnedmodel.advancedanimation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import zombie.ZomboidFileSystem;
import zombie.asset.AssetPath;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.util.StringUtils;

public final class AnimState {
   public String m_Name = "";
   public final List m_Nodes = new ArrayList();
   public int m_DefaultIndex = 0;
   public AnimationSet m_Set = null;
   private static final boolean s_bDebugLog_NodeConditions = false;

   public List getAnimNodes(IAnimationVariableSource var1, List var2) {
      var2.clear();
      if (this.m_Nodes.size() <= 0) {
         return var2;
      } else {
         int var4;
         int var5;
         AnimNode var6;
         if (DebugOptions.instance.Animation.AnimLayer.AllowAnimNodeOverride.getValue() && var1.getVariableBoolean("dbgForceAnim") && var1.isVariable("dbgForceAnimStateName", this.m_Name)) {
            String var7 = var1.getVariableString("dbgForceAnimNodeName");
            var4 = 0;

            for(var5 = this.m_Nodes.size(); var4 < var5; ++var4) {
               var6 = (AnimNode)this.m_Nodes.get(var4);
               if (StringUtils.equalsIgnoreCase(var6.m_Name, var7)) {
                  var2.add(var6);
                  break;
               }
            }

            return var2;
         } else {
            int var3 = -1;
            var4 = 0;

            for(var5 = this.m_Nodes.size(); var4 < var5; ++var4) {
               var6 = (AnimNode)this.m_Nodes.get(var4);
               if (!var6.isAbstract() && var6.m_Conditions.size() >= var3 && var6.checkConditions(var1)) {
                  if (var3 < var6.m_Conditions.size()) {
                     var2.clear();
                     var3 = var6.m_Conditions.size();
                  }

                  var2.add(var6);
               }
            }

            if (!var2.isEmpty()) {
            }

            return var2;
         }
      }
   }

   public static AnimState Parse(String var0, String var1) {
      boolean var2 = DebugLog.isEnabled(DebugType.Animation);
      AnimState var3 = new AnimState();
      var3.m_Name = var0;
      if (var2) {
         DebugLog.Animation.println("Loading AnimState: " + var0);
      }

      String[] var4 = ZomboidFileSystem.instance.resolveAllFiles(var1, (var0x) -> {
         return var0x.getName().endsWith(".xml");
      }, true);
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         File var9 = new File(var8);
         String var10 = var9.getName().split(".xml")[0].toLowerCase();
         if (var2) {
            DebugLog.Animation.println(var0 + " -> AnimNode: " + var10);
         }

         String var11 = ZomboidFileSystem.instance.resolveFileOrGUID(var8);
         AnimNodeAsset var12 = (AnimNodeAsset)AnimNodeAssetManager.instance.load(new AssetPath(var11));
         if (var12.isReady()) {
            AnimNode var13 = var12.m_animNode;
            var13.m_State = var3;
            var3.m_Nodes.add(var13);
         }
      }

      return var3;
   }

   public String toString() {
      String var10000 = this.m_Name;
      return "AnimState{" + var10000 + ", NodeCount:" + this.m_Nodes.size() + ", DefaultIndex:" + this.m_DefaultIndex + "}";
   }

   public static String getStateName(AnimState var0) {
      return var0 != null ? var0.m_Name : null;
   }

   protected void clear() {
      this.m_Nodes.clear();
      this.m_Set = null;
   }

   // $FF: synthetic method
   private static String lambda$getAnimNodes$0(AnimNode var0) {
      return String.format("%s: %s", var0.m_Name, var0.getConditionsString());
   }
}
