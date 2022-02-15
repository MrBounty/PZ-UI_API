package zombie.core.skinnedmodel.advancedanimation;

import zombie.debug.DebugLog;

public final class AnimationVariableGenericSlot extends AnimationVariableSlot {
   private AnimationVariableType m_type;
   private IAnimationVariableSlot m_valueSlot;

   public AnimationVariableGenericSlot(String var1) {
      super(var1);
      this.m_type = AnimationVariableType.Void;
   }

   public String getValueString() {
      return this.m_valueSlot != null ? this.m_valueSlot.getValueString() : null;
   }

   public float getValueFloat() {
      return this.m_valueSlot != null ? this.m_valueSlot.getValueFloat() : 0.0F;
   }

   public boolean getValueBool() {
      return this.m_valueSlot != null && this.m_valueSlot.getValueBool();
   }

   public void setValue(String var1) {
      if (this.m_valueSlot == null || !this.m_valueSlot.canConvertFrom(var1)) {
         this.m_valueSlot = new AnimationVariableSlotString(this.getKey());
         this.setType(this.m_valueSlot.getType());
      }

      this.m_valueSlot.setValue(var1);
   }

   public void setValue(float var1) {
      if (this.m_valueSlot == null || this.m_type != AnimationVariableType.Float) {
         this.m_valueSlot = new AnimationVariableSlotFloat(this.getKey());
         this.setType(this.m_valueSlot.getType());
      }

      this.m_valueSlot.setValue(var1);
   }

   public void setValue(boolean var1) {
      if (this.m_valueSlot == null || this.m_type != AnimationVariableType.Boolean) {
         this.m_valueSlot = new AnimationVariableSlotBool(this.getKey());
         this.setType(this.m_valueSlot.getType());
      }

      this.m_valueSlot.setValue(var1);
   }

   public AnimationVariableType getType() {
      return this.m_type;
   }

   private void setType(AnimationVariableType var1) {
      if (this.m_type != var1) {
         if (this.m_type != AnimationVariableType.Void) {
            DebugLog.General.printf("Variable %s converting from %s to %s\n", this.getKey(), this.m_type, var1);
         }

         this.m_type = var1;
      }
   }

   public boolean canConvertFrom(String var1) {
      return true;
   }

   public void clear() {
      this.m_type = AnimationVariableType.Void;
      this.m_valueSlot = null;
   }
}
