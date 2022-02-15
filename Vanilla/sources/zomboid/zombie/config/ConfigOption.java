package zombie.config;

public abstract class ConfigOption {
   protected final String name;

   public ConfigOption(String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.contains("=")) {
         this.name = var1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getName() {
      return this.name;
   }

   public abstract String getType();

   public abstract void resetToDefault();

   public abstract void setDefaultToCurrentValue();

   public abstract void parse(String var1);

   public abstract String getValueAsString();

   public String getValueAsLuaString() {
      return this.getValueAsString();
   }

   public abstract void setValueFromObject(Object var1);

   public abstract Object getValueAsObject();

   public abstract boolean isValidString(String var1);
}
