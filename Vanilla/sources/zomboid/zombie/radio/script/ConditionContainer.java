package zombie.radio.script;

import java.util.ArrayList;
import java.util.List;
import zombie.radio.globals.CompareMethod;
import zombie.radio.globals.CompareResult;
import zombie.radio.globals.RadioGlobal;

public final class ConditionContainer implements ConditionIter {
   private List conditions;
   private OperatorType operatorType;

   public ConditionContainer() {
      this(OperatorType.NONE);
   }

   public ConditionContainer(OperatorType var1) {
      this.conditions = new ArrayList();
      this.operatorType = OperatorType.NONE;
      this.operatorType = var1;
   }

   public CompareResult Evaluate() {
      boolean var2 = false;

      for(int var3 = 0; var3 < this.conditions.size(); ++var3) {
         ConditionIter var4 = (ConditionIter)this.conditions.get(var3);
         CompareResult var1 = var4 != null ? var4.Evaluate() : CompareResult.Invalid;
         if (var1.equals(CompareResult.Invalid)) {
            return var1;
         }

         OperatorType var5 = var4.getNextOperator();
         if (var3 == this.conditions.size() - 1) {
            return !var5.equals(OperatorType.NONE) ? CompareResult.Invalid : (!var2 ? var1 : CompareResult.False);
         }

         if (var5.equals(OperatorType.OR)) {
            if (!var2 && var1.equals(CompareResult.True)) {
               return var1;
            }

            var2 = false;
         } else if (var5.equals(OperatorType.AND)) {
            var2 = var2 || var1.equals(CompareResult.False);
         } else if (var5.equals(OperatorType.NONE)) {
            return CompareResult.Invalid;
         }
      }

      return CompareResult.Invalid;
   }

   public OperatorType getNextOperator() {
      return this.operatorType;
   }

   public void setNextOperator(OperatorType var1) {
      this.operatorType = var1;
   }

   public void Add(ConditionContainer var1) {
      this.conditions.add(var1);
   }

   public void Add(RadioGlobal var1, RadioGlobal var2, CompareMethod var3, OperatorType var4) {
      ConditionContainer.Condition var5 = new ConditionContainer.Condition(var1, var2, var3, var4);
      this.conditions.add(var5);
   }

   private static final class Condition implements ConditionIter {
      private OperatorType operatorType;
      private CompareMethod compareMethod;
      private RadioGlobal valueA;
      private RadioGlobal valueB;

      public Condition(RadioGlobal var1, RadioGlobal var2, CompareMethod var3) {
         this(var1, var2, var3, OperatorType.NONE);
      }

      public Condition(RadioGlobal var1, RadioGlobal var2, CompareMethod var3, OperatorType var4) {
         this.operatorType = OperatorType.NONE;
         this.valueA = var1;
         this.valueB = var2;
         this.operatorType = var4;
         this.compareMethod = var3;
      }

      public CompareResult Evaluate() {
         return this.valueA != null && this.valueB != null ? this.valueA.compare(this.valueB, this.compareMethod) : CompareResult.Invalid;
      }

      public OperatorType getNextOperator() {
         return this.operatorType;
      }

      public void setNextOperator(OperatorType var1) {
         this.operatorType = var1;
      }
   }
}
