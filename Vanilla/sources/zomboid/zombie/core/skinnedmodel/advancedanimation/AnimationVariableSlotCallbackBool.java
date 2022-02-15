package zombie.core.skinnedmodel.advancedanimation;

import zombie.util.StringUtils;

public final class AnimationVariableSlotCallbackBool extends AnimationVariableSlotCallback {
   private boolean m_defaultValue = false;

   public AnimationVariableSlotCallbackBool(String var1, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var2) {
      super(var1, var2);
   }

   public AnimationVariableSlotCallbackBool(String var1, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackBool.CallbackSetStrongTyped var3) {
      super(var1, var2, var3);
   }

   public AnimationVariableSlotCallbackBool(String var1, boolean var2, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var3) {
      super(var1, var3);
      this.m_defaultValue = var2;
   }

   public AnimationVariableSlotCallbackBool(String var1, boolean var2, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackBool.CallbackSetStrongTyped var4) {
      super(var1, var3, var4);
      this.m_defaultValue = var2;
   }

   public Boolean getDefaultValue() {
      return this.m_defaultValue;
   }

   public String getValueString() {
      return (Boolean)this.getValue() ? "true" : "false";
   }

   public float getValueFloat() {
      return (Boolean)this.getValue() ? 1.0F : 0.0F;
   }

   public boolean getValueBool() {
      return (Boolean)this.getValue();
   }

   public void setValue(String var1) {
      this.trySetValue(StringUtils.tryParseBoolean(var1));
   }

   public void setValue(float var1) {
      this.trySetValue((double)var1 != 0.0D);
   }

   public void setValue(boolean var1) {
      this.trySetValue(var1);
   }

   public AnimationVariableType getType() {
      return AnimationVariableType.Boolean;
   }

   public boolean canConvertFrom(String var1) {
      return StringUtils.tryParseBoolean(var1);
   }

   public interface CallbackSetStrongTyped extends AnimationVariableSlotCallback.CallbackSet {
   }

   public interface CallbackGetStrongTyped extends AnimationVariableSlotCallback.CallbackGet {
   }
}
