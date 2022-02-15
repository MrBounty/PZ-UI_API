package zombie.core.skinnedmodel.advancedanimation;

import zombie.core.math.PZMath;

public final class AnimationVariableSlotFloat extends AnimationVariableSlot {
   private float m_value = 0.0F;

   public AnimationVariableSlotFloat(String var1) {
      super(var1);
   }

   public String getValueString() {
      return String.valueOf(this.m_value);
   }

   public float getValueFloat() {
      return this.m_value;
   }

   public boolean getValueBool() {
      return this.m_value != 0.0F;
   }

   public void setValue(String var1) {
      this.m_value = PZMath.tryParseFloat(var1, 0.0F);
   }

   public void setValue(float var1) {
      this.m_value = var1;
   }

   public void setValue(boolean var1) {
      this.m_value = var1 ? 1.0F : 0.0F;
   }

   public AnimationVariableType getType() {
      return AnimationVariableType.Float;
   }

   public boolean canConvertFrom(String var1) {
      return PZMath.canParseFloat(var1);
   }

   public void clear() {
      this.m_value = 0.0F;
   }
}
