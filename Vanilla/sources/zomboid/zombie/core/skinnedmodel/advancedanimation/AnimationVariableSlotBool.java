package zombie.core.skinnedmodel.advancedanimation;

import zombie.util.StringUtils;

public final class AnimationVariableSlotBool extends AnimationVariableSlot {
   private boolean m_value;

   public AnimationVariableSlotBool(String var1) {
      super(var1);
   }

   public String getValueString() {
      return this.m_value ? "true" : "false";
   }

   public float getValueFloat() {
      return this.m_value ? 1.0F : 0.0F;
   }

   public boolean getValueBool() {
      return this.m_value;
   }

   public void setValue(String var1) {
      this.m_value = StringUtils.tryParseBoolean(var1);
   }

   public void setValue(float var1) {
      this.m_value = (double)var1 != 0.0D;
   }

   public void setValue(boolean var1) {
      this.m_value = var1;
   }

   public AnimationVariableType getType() {
      return AnimationVariableType.Boolean;
   }

   public boolean canConvertFrom(String var1) {
      return StringUtils.isBoolean(var1);
   }

   public void clear() {
      this.m_value = false;
   }
}
