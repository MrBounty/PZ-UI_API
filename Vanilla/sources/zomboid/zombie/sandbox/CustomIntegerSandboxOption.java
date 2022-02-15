package zombie.sandbox;

import zombie.scripting.ScriptParser;

public final class CustomIntegerSandboxOption extends CustomSandboxOption {
   public final int min;
   public final int max;
   public final int defaultValue;

   CustomIntegerSandboxOption(String var1, int var2, int var3, int var4) {
      super(var1);
      this.min = var2;
      this.max = var3;
      this.defaultValue = var4;
   }

   static CustomIntegerSandboxOption parse(ScriptParser.Block var0) {
      int var1 = getValueInt(var0, "min", Integer.MIN_VALUE);
      int var2 = getValueInt(var0, "max", Integer.MIN_VALUE);
      int var3 = getValueInt(var0, "default", Integer.MIN_VALUE);
      if (var1 != Integer.MIN_VALUE && var2 != Integer.MIN_VALUE && var3 != Integer.MIN_VALUE) {
         CustomIntegerSandboxOption var4 = new CustomIntegerSandboxOption(var0.id, var1, var2, var3);
         return !var4.parseCommon(var0) ? null : var4;
      } else {
         return null;
      }
   }
}
