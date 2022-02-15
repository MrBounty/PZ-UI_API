package zombie.sandbox;

import zombie.scripting.ScriptParser;

public final class CustomDoubleSandboxOption extends CustomSandboxOption {
   public final double min;
   public final double max;
   public final double defaultValue;

   CustomDoubleSandboxOption(String var1, double var2, double var4, double var6) {
      super(var1);
      this.min = var2;
      this.max = var4;
      this.defaultValue = var6;
   }

   static CustomDoubleSandboxOption parse(ScriptParser.Block var0) {
      float var1 = getValueFloat(var0, "min", Float.NaN);
      float var2 = getValueFloat(var0, "max", Float.NaN);
      float var3 = getValueFloat(var0, "default", Float.NaN);
      if (!Float.isNaN(var1) && !Float.isNaN(var2) && !Float.isNaN(var3)) {
         CustomDoubleSandboxOption var4 = new CustomDoubleSandboxOption(var0.id, (double)var1, (double)var2, (double)var3);
         return !var4.parseCommon(var0) ? null : var4;
      } else {
         return null;
      }
   }
}
