package zombie.config;

public class StringConfigOption extends ConfigOption {
   protected String value;
   protected String defaultValue;

   public StringConfigOption(String var1, String var2) {
      super(var1);
      if (var2 == null) {
         var2 = "";
      }

      this.value = var2;
      this.defaultValue = var2;
   }

   public String getType() {
      return "string";
   }

   public void resetToDefault() {
      this.value = this.defaultValue;
   }

   public void setDefaultToCurrentValue() {
      this.defaultValue = this.value;
   }

   public void parse(String var1) {
      this.setValueFromObject(var1);
   }

   public String getValueAsString() {
      return this.value;
   }

   public String getValueAsLuaString() {
      return String.format("\"%s\"", this.value.replace("\\", "\\\\").replace("\"", "\\\""));
   }

   public void setValueFromObject(Object var1) {
      if (var1 == null) {
         this.value = "";
      } else if (var1 instanceof String) {
         this.value = (String)var1;
      } else {
         this.value = var1.toString();
      }

   }

   public Object getValueAsObject() {
      return this.value;
   }

   public boolean isValidString(String var1) {
      return true;
   }

   public void setValue(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.value = var1;
   }

   public String getValue() {
      return this.value;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }
}
