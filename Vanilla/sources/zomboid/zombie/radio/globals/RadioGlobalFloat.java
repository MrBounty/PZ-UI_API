package zombie.radio.globals;

public final class RadioGlobalFloat extends RadioGlobal {
   public RadioGlobalFloat(float var1) {
      super(var1, RadioGlobalType.Float);
   }

   public RadioGlobalFloat(String var1, float var2) {
      super(var1, var2, RadioGlobalType.Float);
   }

   public float getValue() {
      return (Float)this.value;
   }

   public void setValue(float var1) {
      this.value = var1;
   }

   public String getString() {
      return ((Float)this.value).toString();
   }

   public CompareResult compare(RadioGlobal var1, CompareMethod var2) {
      if (var1 instanceof RadioGlobalFloat) {
         RadioGlobalFloat var3 = (RadioGlobalFloat)var1;
         switch(var2) {
         case equals:
            return (Float)this.value == var3.getValue() ? CompareResult.True : CompareResult.False;
         case notequals:
            return (Float)this.value != var3.getValue() ? CompareResult.True : CompareResult.False;
         case lessthan:
            return (Float)this.value < var3.getValue() ? CompareResult.True : CompareResult.False;
         case morethan:
            return (Float)this.value > var3.getValue() ? CompareResult.True : CompareResult.False;
         case lessthanorequals:
            return (Float)this.value <= var3.getValue() ? CompareResult.True : CompareResult.False;
         case morethanorequals:
            return (Float)this.value >= var3.getValue() ? CompareResult.True : CompareResult.False;
         default:
            return CompareResult.Invalid;
         }
      } else {
         return CompareResult.Invalid;
      }
   }

   public boolean setValue(RadioGlobal var1, EditGlobalOps var2) {
      if (var1 instanceof RadioGlobalFloat) {
         RadioGlobalFloat var3 = (RadioGlobalFloat)var1;
         switch(var2) {
         case set:
            this.value = var3.getValue();
            return true;
         case add:
            this.value = (Float)this.value + var3.getValue();
            return true;
         case sub:
            this.value = (Float)this.value - var3.getValue();
            return true;
         }
      }

      return false;
   }
}
