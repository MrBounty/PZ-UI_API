package zombie.config;

import zombie.debug.DebugLog;

public class BooleanConfigOption extends ConfigOption {
   protected boolean value;
   protected boolean defaultValue;

   public BooleanConfigOption(String var1, boolean var2) {
      super(var1);
      this.value = var2;
      this.defaultValue = var2;
   }

   public String getType() {
      return "boolean";
   }

   public void resetToDefault() {
      this.setValue(this.defaultValue);
   }

   public void setDefaultToCurrentValue() {
      this.defaultValue = this.value;
   }

   public void parse(String var1) {
      if (this.isValidString(var1)) {
         this.setValue(var1.equalsIgnoreCase("true") || var1.equalsIgnoreCase("1"));
      } else {
         DebugLog.log("ERROR BooleanConfigOption.parse() \"" + this.name + "\" string=" + var1 + "\"");
      }

   }

   public String getValueAsString() {
      return String.valueOf(this.value);
   }

   public void setValueFromObject(Object var1) {
      if (var1 instanceof Boolean) {
         this.setValue((Boolean)var1);
      } else if (var1 instanceof Double) {
         this.setValue((Double)var1 != 0.0D);
      } else if (var1 instanceof String) {
         this.parse((String)var1);
      }

   }

   public Object getValueAsObject() {
      return this.value;
   }

   public boolean isValidString(String var1) {
      return var1 != null && (var1.equalsIgnoreCase("true") || var1.equalsIgnoreCase("false") || var1.equalsIgnoreCase("1") || var1.equalsIgnoreCase("0"));
   }

   public boolean getValue() {
      return this.value;
   }

   public void setValue(boolean var1) {
      this.value = var1;
   }

   public boolean getDefaultValue() {
      return this.defaultValue;
   }
}
