package zombie.config;

import zombie.debug.DebugLog;

public class DoubleConfigOption extends ConfigOption {
   protected double value;
   protected double defaultValue;
   protected double min;
   protected double max;

   public DoubleConfigOption(String var1, double var2, double var4, double var6) {
      super(var1);
      if (!(var6 < var2) && !(var6 > var4)) {
         this.value = var6;
         this.defaultValue = var6;
         this.min = var2;
         this.max = var4;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getType() {
      return "double";
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public void resetToDefault() {
      this.setValue(this.defaultValue);
   }

   public void setDefaultToCurrentValue() {
      this.defaultValue = this.value;
   }

   public void parse(String var1) {
      try {
         double var2 = Double.parseDouble(var1);
         this.setValue(var2);
      } catch (NumberFormatException var4) {
         DebugLog.log("ERROR DoubleConfigOption.parse() \"" + this.name + "\" string=" + var1 + "\"");
      }

   }

   public String getValueAsString() {
      return String.valueOf(this.value);
   }

   public void setValueFromObject(Object var1) {
      if (var1 instanceof Double) {
         this.setValue((Double)var1);
      } else if (var1 instanceof String) {
         this.parse((String)var1);
      }

   }

   public Object getValueAsObject() {
      return this.value;
   }

   public boolean isValidString(String var1) {
      try {
         double var2 = Double.parseDouble(var1);
         return var2 >= this.min && var2 <= this.max;
      } catch (NumberFormatException var4) {
         return false;
      }
   }

   public void setValue(double var1) {
      if (var1 < this.min) {
         DebugLog.log("ERROR: DoubleConfigOption.setValue() \"" + this.name + "\" " + var1 + " is less than min=" + this.min);
      } else if (var1 > this.max) {
         DebugLog.log("ERROR: DoubleConfigOption.setValue() \"" + this.name + "\" " + var1 + " is greater than max=" + this.max);
      } else {
         this.value = var1;
      }
   }

   public double getValue() {
      return this.value;
   }

   public double getDefaultValue() {
      return this.defaultValue;
   }
}
