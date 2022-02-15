package zombie.core.skinnedmodel.advancedanimation;

import zombie.core.math.PZMath;
import zombie.util.StringUtils;

public final class AnimationVariableSlotCallbackString extends AnimationVariableSlotCallback {
   private String m_defaultValue = "";

   public AnimationVariableSlotCallbackString(String var1, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var2) {
      super(var1, var2);
   }

   public AnimationVariableSlotCallbackString(String var1, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackString.CallbackSetStrongTyped var3) {
      super(var1, var2, var3);
   }

   public AnimationVariableSlotCallbackString(String var1, String var2, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var3) {
      super(var1, var3);
      this.m_defaultValue = var2;
   }

   public AnimationVariableSlotCallbackString(String var1, String var2, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackString.CallbackSetStrongTyped var4) {
      super(var1, var3, var4);
      this.m_defaultValue = var2;
   }

   public String getDefaultValue() {
      return this.m_defaultValue;
   }

   public String getValueString() {
      return (String)this.getValue();
   }

   public float getValueFloat() {
      return PZMath.tryParseFloat((String)this.getValue(), 0.0F);
   }

   public boolean getValueBool() {
      return StringUtils.tryParseBoolean((String)this.getValue());
   }

   public void setValue(String var1) {
      this.trySetValue(var1);
   }

   public void setValue(float var1) {
      this.trySetValue(String.valueOf(var1));
   }

   public void setValue(boolean var1) {
      this.trySetValue(var1 ? "true" : "false");
   }

   public AnimationVariableType getType() {
      return AnimationVariableType.String;
   }

   public boolean canConvertFrom(String var1) {
      return true;
   }

   public interface CallbackSetStrongTyped extends AnimationVariableSlotCallback.CallbackSet {
   }

   public interface CallbackGetStrongTyped extends AnimationVariableSlotCallback.CallbackGet {
   }
}
