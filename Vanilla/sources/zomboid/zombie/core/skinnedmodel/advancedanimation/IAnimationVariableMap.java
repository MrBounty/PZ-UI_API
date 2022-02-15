package zombie.core.skinnedmodel.advancedanimation;

public interface IAnimationVariableMap extends IAnimationVariableSource {
   IAnimationVariableSlot getOrCreateVariable(String var1);

   void setVariable(IAnimationVariableSlot var1);

   void setVariable(String var1, String var2);

   void setVariable(String var1, boolean var2);

   void setVariable(String var1, float var2);

   void clearVariable(String var1);

   void clearVariables();
}
