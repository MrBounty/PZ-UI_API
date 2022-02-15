package zombie.config;

import zombie.debug.DebugLog;

public class IntegerConfigOption extends ConfigOption {
   protected int value;
   protected int defaultValue;
   protected int min;
   protected int max;

   public IntegerConfigOption(String var1, int var2, int var3, int var4) {
      super(var1);
      if (var4 >= var2 && var4 <= var3) {
         this.value = var4;
         this.defaultValue = var4;
         this.min = var2;
         this.max = var3;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getType() {
      return "integer";
   }

   public void resetToDefault() {
      this.setValue(this.defaultValue);
   }

   public double getMin() {
      return (double)this.min;
   }

   public double getMax() {
      return (double)this.max;
   }

   public void setDefaultToCurrentValue() {
      this.defaultValue = this.value;
   }

   public void parse(String var1) {
      try {
         double var2 = Double.parseDouble(var1);
         this.setValue((int)var2);
      } catch (NumberFormatException var4) {
         DebugLog.log("ERROR IntegerConfigOption.parse() \"" + this.name + "\" string=\"" + var1 + "\"");
      }

   }

   public String getValueAsString() {
      return String.valueOf(this.value);
   }

   public void setValueFromObject(Object var1) {
      if (var1 instanceof Double) {
         this.setValue(((Double)var1).intValue());
      } else if (var1 instanceof String) {
         this.parse((String)var1);
      }

   }

   public Object getValueAsObject() {
      return (double)this.value;
   }

   public boolean isValidString(String var1) {
      try {
         int var2 = Integer.parseInt(var1);
         return var2 >= this.min && var2 <= this.max;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public void setValue(int var1) {
      if (var1 < this.min) {
         DebugLog.log("ERROR: IntegerConfigOption.setValue() \"" + this.name + "\" " + var1 + " is less than min=" + this.min);
      } else if (var1 > this.max) {
         DebugLog.log("ERROR: IntegerConfigOption.setValue() \"" + this.name + "\" " + var1 + " is greater than max=" + this.max);
      } else {
         this.value = var1;
      }
   }

   public int getValue() {
      return this.value;
   }

   public int getDefaultValue() {
      return this.defaultValue;
   }
}
