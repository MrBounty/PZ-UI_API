package zombie.radio.globals;

public final class RadioGlobalString extends RadioGlobal {
   public RadioGlobalString(String var1) {
      super(var1, RadioGlobalType.String);
   }

   public RadioGlobalString(String var1, String var2) {
      super(var1, var2, RadioGlobalType.String);
   }

   public String getValue() {
      return (String)this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public String getString() {
      return (String)this.value;
   }

   public CompareResult compare(RadioGlobal var1, CompareMethod var2) {
      if (var1 instanceof RadioGlobalString) {
         RadioGlobalString var3 = (RadioGlobalString)var1;
         switch(var2) {
         case equals:
            return ((String)this.value).equals(var3.getValue()) ? CompareResult.True : CompareResult.False;
         case notequals:
            return !((String)this.value).equals(var3.getValue()) ? CompareResult.True : CompareResult.False;
         default:
            return CompareResult.Invalid;
         }
      } else {
         return CompareResult.Invalid;
      }
   }

   public boolean setValue(RadioGlobal var1, EditGlobalOps var2) {
      if (var2.equals(EditGlobalOps.set) && var1 instanceof RadioGlobalString) {
         this.value = ((RadioGlobalString)var1).getValue();
         return true;
      } else {
         return false;
      }
   }
}
