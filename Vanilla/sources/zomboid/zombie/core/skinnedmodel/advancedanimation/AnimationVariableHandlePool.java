package zombie.core.skinnedmodel.advancedanimation;

import java.util.HashMap;
import zombie.util.StringUtils;

public class AnimationVariableHandlePool {
   private static final Object s_threadLock = "AnimationVariableHandlePool.ThreadLock";
   private static HashMap s_handlePool = new HashMap();
   private static int s_globalIndexGenerator = 0;

   public static AnimationVariableHandle getOrCreate(String var0) {
      synchronized(s_threadLock) {
         return getOrCreateInternal(var0);
      }
   }

   private static AnimationVariableHandle getOrCreateInternal(String var0) {
      if (!isVariableNameValid(var0)) {
         return null;
      } else {
         AnimationVariableHandle var1 = (AnimationVariableHandle)s_handlePool.get(var0);
         if (var1 != null) {
            return var1;
         } else {
            AnimationVariableHandle var2 = new AnimationVariableHandle();
            var2.setVariableName(var0);
            var2.setVariableIndex(generateNewVariableIndex());
            s_handlePool.put(var0, var2);
            return var2;
         }
      }
   }

   private static boolean isVariableNameValid(String var0) {
      return !StringUtils.isNullOrWhitespace(var0);
   }

   private static int generateNewVariableIndex() {
      return s_globalIndexGenerator++;
   }
}
