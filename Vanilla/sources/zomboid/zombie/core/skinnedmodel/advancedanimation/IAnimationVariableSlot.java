package zombie.core.skinnedmodel.advancedanimation;

public interface IAnimationVariableSlot {
   String getKey();

   String getValueString();

   float getValueFloat();

   boolean getValueBool();

   void setValue(String var1);

   void setValue(float var1);

   void setValue(boolean var1);

   AnimationVariableType getType();

   boolean canConvertFrom(String var1);

   void clear();

   default boolean isReadOnly() {
      return false;
   }
}
