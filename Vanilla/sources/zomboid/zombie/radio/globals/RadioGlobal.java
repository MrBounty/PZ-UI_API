package zombie.radio.globals;

public abstract class RadioGlobal {
   protected String name;
   protected Object value;
   protected RadioGlobalType type;

   protected RadioGlobal(Object var1, RadioGlobalType var2) {
      this((String)null, var1, var2);
   }

   protected RadioGlobal(String var1, Object var2, RadioGlobalType var3) {
      this.type = RadioGlobalType.Invalid;
      this.name = var1;
      this.value = var2;
      this.type = var3;
   }

   public final RadioGlobalType getType() {
      return this.type;
   }

   public final String getName() {
      return this.name;
   }

   public abstract String getString();

   public abstract CompareResult compare(RadioGlobal var1, CompareMethod var2);

   public abstract boolean setValue(RadioGlobal var1, EditGlobalOps var2);
}
