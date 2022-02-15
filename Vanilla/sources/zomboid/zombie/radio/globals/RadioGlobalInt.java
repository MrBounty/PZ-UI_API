package zombie.radio.globals;

public final class RadioGlobalInt extends RadioGlobal {
   public RadioGlobalInt(int var1) {
      super(var1, RadioGlobalType.Integer);
   }

   public RadioGlobalInt(String var1, int var2) {
      super(var1, var2, RadioGlobalType.Integer);
   }

   public int getValue() {
      return (Integer)this.value;
   }

   public void setValue(int var1) {
      this.value = var1;
   }

   public String getString() {
      return ((Integer)this.value).toString();
   }

   public CompareResult compare(RadioGlobal var1, CompareMethod var2) {
      if (var1 instanceof RadioGlobalInt) {
         RadioGlobalInt var3 = (RadioGlobalInt)var1;
         switch(var2) {
         case equals:
            return (Integer)this.value == var3.getValue() ? CompareResult.True : CompareResult.False;
         case notequals:
            return (Integer)this.value != var3.getValue() ? CompareResult.True : CompareResult.False;
         case lessthan:
            return (Integer)this.value < var3.getValue() ? CompareResult.True : CompareResult.False;
         case morethan:
            return (Integer)this.value > var3.getValue() ? CompareResult.True : CompareResult.False;
         case lessthanorequals:
            return (Integer)this.value <= var3.getValue() ? CompareResult.True : CompareResult.False;
         case morethanorequals:
            return (Integer)this.value >= var3.getValue() ? CompareResult.True : CompareResult.False;
         default:
            return CompareResult.Invalid;
         }
      } else {
         return CompareResult.Invalid;
      }
   }

   public boolean setValue(RadioGlobal var1, EditGlobalOps var2) {
      if (var1 instanceof RadioGlobalInt) {
         RadioGlobalInt var3 = (RadioGlobalInt)var1;
         switch(var2) {
         case set:
            this.value = var3.getValue();
            return true;
         case add:
            this.value = (Integer)this.value + var3.getValue();
            return true;
         case sub:
            this.value = (Integer)this.value - var3.getValue();
            return true;
         }
      }

      return false;
   }
}
