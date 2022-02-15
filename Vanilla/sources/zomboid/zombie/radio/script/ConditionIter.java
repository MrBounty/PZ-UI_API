package zombie.radio.script;

import zombie.radio.globals.CompareResult;

public interface ConditionIter {
   CompareResult Evaluate();

   OperatorType getNextOperator();

   void setNextOperator(OperatorType var1);
}
