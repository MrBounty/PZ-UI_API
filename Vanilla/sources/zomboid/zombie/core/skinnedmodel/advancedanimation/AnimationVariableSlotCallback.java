package zombie.core.skinnedmodel.advancedanimation;

import zombie.debug.DebugLog;

public abstract class AnimationVariableSlotCallback extends AnimationVariableSlot {
   private final AnimationVariableSlotCallback.CallbackGet m_callbackGet;
   private final AnimationVariableSlotCallback.CallbackSet m_callbackSet;

   protected AnimationVariableSlotCallback(String var1, AnimationVariableSlotCallback.CallbackGet var2) {
      this(var1, var2, (AnimationVariableSlotCallback.CallbackSet)null);
   }

   protected AnimationVariableSlotCallback(String var1, AnimationVariableSlotCallback.CallbackGet var2, AnimationVariableSlotCallback.CallbackSet var3) {
      super(var1);
      this.m_callbackGet = var2;
      this.m_callbackSet = var3;
   }

   public Object getValue() {
      return this.m_callbackGet.call();
   }

   public abstract Object getDefaultValue();

   public boolean trySetValue(Object var1) {
      if (this.isReadOnly()) {
         DebugLog.General.warn("Trying to set read-only variable \"%s\"", super.getKey());
         return false;
      } else {
         this.m_callbackSet.call(var1);
         return true;
      }
   }

   public boolean isReadOnly() {
      return this.m_callbackSet == null;
   }

   public void clear() {
      if (!this.isReadOnly()) {
         this.trySetValue(this.getDefaultValue());
      }

   }

   public interface CallbackGet {
      Object call();
   }

   public interface CallbackSet {
      void call(Object var1);
   }
}
