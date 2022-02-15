package zombie.radio.globals;

public final class RadioGlobalBool extends RadioGlobal {
   public RadioGlobalBool(boolean var1) {
      super(var1, RadioGlobalType.Boolean);
   }

   public RadioGlobalBool(String var1, boolean var2) {
      super(var1, var2, RadioGlobalType.Boolean);
   }

   public boolean getValue() {
      return (Boolean)this.value;
   }

   public void setValue(boolean var1) {
      this.value = var1;
   }

   public String getString() {
      return ((Boolean)this.value).toString();
   }

   public CompareResult compare(RadioGlobal var1, CompareMethod var2) {
      if (var1 instanceof RadioGlobalBool) {
         RadioGlobalBool var3 = (RadioGlobalBool)var1;
         switch(var2) {
         case equals:
            return ((Boolean)this.value).equals(var3.getValue()) ? CompareResult.True : CompareResult.False;
         case notequals:
            return !((Boolean)this.value).equals(var3.getValue()) ? CompareResult.True : CompareResult.False;
         default:
            return CompareResult.Invalid;
         }
      } else {
         return CompareResult.Invalid;
      }
   }

   public boolean setValue(RadioGlobal var1, EditGlobalOps var2) {
      if (var2.equals(EditGlobalOps.set) && var1 instanceof RadioGlobalBool) {
         this.value = ((RadioGlobalBool)var1).getValue();
         return true;
      } else {
         return false;
      }
   }
}
