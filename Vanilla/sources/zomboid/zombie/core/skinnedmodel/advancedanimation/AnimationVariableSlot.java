package zombie.core.skinnedmodel.advancedanimation;

public abstract class AnimationVariableSlot implements IAnimationVariableSlot {
   private final String m_key;

   protected AnimationVariableSlot(String var1) {
      this.m_key = var1.toLowerCase().trim();
   }

   public String getKey() {
      return this.m_key;
   }
}
