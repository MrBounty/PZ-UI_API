package zombie.core.skinnedmodel.advancedanimation;

public class AnimationVariableHandle {
   private String m_name = null;
   private int m_varIndex = -1;

   AnimationVariableHandle() {
   }

   public static AnimationVariableHandle alloc(String var0) {
      return AnimationVariableHandlePool.getOrCreate(var0);
   }

   public String getVariableName() {
      return this.m_name;
   }

   public int getVariableIndex() {
      return this.m_varIndex;
   }

   void setVariableName(String var1) {
      this.m_name = var1;
   }

   void setVariableIndex(int var1) {
      this.m_varIndex = var1;
   }
}
