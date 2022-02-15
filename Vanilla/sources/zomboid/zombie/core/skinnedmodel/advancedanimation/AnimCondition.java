package zombie.core.skinnedmodel.advancedanimation;

import java.util.List;

public final class AnimCondition {
   public String m_Name = "";
   public AnimCondition.Type m_Type;
   public float m_FloatValue;
   public boolean m_BoolValue;
   public String m_StringValue;
   private AnimationVariableHandle m_variableHandle;

   public AnimCondition() {
      this.m_Type = AnimCondition.Type.STRING;
      this.m_FloatValue = 0.0F;
      this.m_BoolValue = false;
      this.m_StringValue = "";
   }

   public String toString() {
      return String.format("AnimCondition{name:%s type:%s value:%s }", this.m_Name, this.m_Type.toString(), this.getValueString());
   }

   public String getConditionString() {
      return this.m_Type == AnimCondition.Type.OR ? "OR" : String.format("( %s %s %s )", this.m_Name, this.m_Type.toString(), this.getValueString());
   }

   public String getValueString() {
      switch(this.m_Type) {
      case EQU:
      case NEQ:
      case LESS:
      case GTR:
         return String.valueOf(this.m_FloatValue);
      case BOOL:
         return this.m_BoolValue ? "true" : "false";
      case STRING:
      case STRNEQ:
         return this.m_StringValue;
      case OR:
         return " -- OR -- ";
      default:
         throw new RuntimeException("Unexpected internal type:" + this.m_Type);
      }
   }

   public boolean check(IAnimationVariableSource var1) {
      return this.checkInternal(var1);
   }

   private boolean checkInternal(IAnimationVariableSource var1) {
      AnimCondition.Type var2 = this.m_Type;
      if (var2 == AnimCondition.Type.OR) {
         return false;
      } else {
         if (this.m_variableHandle == null) {
            this.m_variableHandle = AnimationVariableHandle.alloc(this.m_Name);
         }

         IAnimationVariableSlot var3 = var1.getVariable(this.m_variableHandle);
         switch(var2) {
         case EQU:
            return var3 != null && this.m_FloatValue == var3.getValueFloat();
         case NEQ:
            return var3 != null && this.m_FloatValue != var3.getValueFloat();
         case LESS:
            return var3 != null && var3.getValueFloat() < this.m_FloatValue;
         case GTR:
            return var3 != null && var3.getValueFloat() > this.m_FloatValue;
         case BOOL:
            return (var3 != null && var3.getValueBool()) == this.m_BoolValue;
         case STRING:
            return this.m_StringValue.equalsIgnoreCase(var3 != null ? var3.getValueString() : "");
         case STRNEQ:
            return !this.m_StringValue.equalsIgnoreCase(var3 != null ? var3.getValueString() : "");
         case OR:
            return false;
         default:
            throw new RuntimeException("Unexpected internal type:" + this.m_Type);
         }
      }
   }

   public static boolean pass(IAnimationVariableSource var0, List var1) {
      boolean var2 = true;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         AnimCondition var4 = (AnimCondition)var1.get(var3);
         if (var4.m_Type == AnimCondition.Type.OR) {
            if (var2) {
               break;
            }

            var2 = true;
         } else {
            var2 = var2 && var4.check(var0);
         }
      }

      return var2;
   }

   public static enum Type {
      STRING,
      STRNEQ,
      BOOL,
      EQU,
      NEQ,
      LESS,
      GTR,
      OR;

      // $FF: synthetic method
      private static AnimCondition.Type[] $values() {
         return new AnimCondition.Type[]{STRING, STRNEQ, BOOL, EQU, NEQ, LESS, GTR, OR};
      }
   }
}
