package zombie.radio.globals;

public final class EditGlobal {
   private RadioGlobal global;
   private RadioGlobal value;
   private EditGlobalOps operator;

   public EditGlobal(RadioGlobal var1, EditGlobalOps var2, RadioGlobal var3) {
      this.global = var1;
      this.operator = var2;
      this.value = var3;
   }

   public RadioGlobal getGlobal() {
      return this.global;
   }

   public EditGlobalOps getOperator() {
      return this.operator;
   }

   public RadioGlobal getValue() {
      return this.value;
   }
}
