package zombie.core.skinnedmodel.advancedanimation;

import zombie.core.math.PZMath;
import zombie.util.StringUtils;

public final class AnimationVariableSlotString extends AnimationVariableSlot {
   private String m_value;

   public AnimationVariableSlotString(String var1) {
      super(var1);
   }

   public String getValueString() {
      return this.m_value;
   }

   public float getValueFloat() {
      return PZMath.tryParseFloat(this.m_value, 0.0F);
   }

   public boolean getValueBool() {
      return StringUtils.tryParseBoolean(this.m_value);
   }

   public void setValue(String var1) {
      this.m_value = var1;
   }

   public void setValue(float var1) {
      this.m_value = String.valueOf(var1);
   }

   public void setValue(boolean var1) {
      this.m_value = var1 ? "true" : "false";
   }

   public AnimationVariableType getType() {
      return AnimationVariableType.String;
   }

   public boolean canConvertFrom(String var1) {
      return true;
   }

   public void clear() {
      this.m_value = "";
   }
}
