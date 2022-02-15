package zombie.core.skinnedmodel.advancedanimation;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import zombie.ZomboidFileSystem;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;

public final class AnimationSet {
   protected static final HashMap setMap = new HashMap();
   public final HashMap states = new HashMap();
   public String m_Name = "";

   public static AnimationSet GetAnimationSet(String var0, boolean var1) {
      AnimationSet var2 = (AnimationSet)setMap.get(var0);
      if (var2 != null && !var1) {
         return var2;
      } else {
         var2 = new AnimationSet();
         var2.Load(var0);
         setMap.put(var0, var2);
         return var2;
      }
   }

   public static void Reset() {
      Iterator var0 = setMap.values().iterator();

      while(var0.hasNext()) {
         AnimationSet var1 = (AnimationSet)var0.next();
         var1.clear();
      }

      setMap.clear();
   }

   public AnimState GetState(String var1) {
      AnimState var2 = (AnimState)this.states.get(var1.toLowerCase(Locale.ENGLISH));
      if (var2 != null) {
         return var2;
      } else {
         DebugLog.Animation.warn("AnimState not found: " + var1);
         var2 = new AnimState();
         return var2;
      }
   }

   public boolean containsState(String var1) {
      return this.states.containsKey(var1.toLowerCase(Locale.ENGLISH));
   }

   public boolean Load(String var1) {
      if (DebugLog.isEnabled(DebugType.Animation)) {
         DebugLog.Animation.println("Loading AnimSet: " + var1);
      }

      this.m_Name = var1;
      String[] var2 = ZomboidFileSystem.instance.resolveAllDirectories("media/AnimSets/" + var1, (var0) -> {
         return true;
      }, false);
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         String var7 = (new File(var6)).getName();
         AnimState var8 = AnimState.Parse(var7, var6);
         var8.m_Set = this;
         this.states.put(var7, var8);
      }

      return true;
   }

   private void clear() {
      Iterator var1 = this.states.values().iterator();

      while(var1.hasNext()) {
         AnimState var2 = (AnimState)var1.next();
         var2.clear();
      }

      this.states.clear();
   }
}
