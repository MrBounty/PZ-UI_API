package zombie.config;

public class EnumConfigOption extends IntegerConfigOption {
   public EnumConfigOption(String var1, int var2, int var3) {
      super(var1, 1, var2, var3);
   }

   public String getType() {
      return "enum";
   }

   public int getNumValues() {
      return this.max;
   }
}
