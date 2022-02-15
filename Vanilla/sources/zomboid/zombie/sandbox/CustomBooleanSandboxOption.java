package zombie.sandbox;

import zombie.scripting.ScriptParser;

public final class CustomBooleanSandboxOption extends CustomSandboxOption {
   public final boolean defaultValue;

   CustomBooleanSandboxOption(String var1, boolean var2) {
      super(var1);
      this.defaultValue = var2;
   }

   static CustomBooleanSandboxOption parse(ScriptParser.Block var0) {
      ScriptParser.Value var1 = var0.getValue("default");
      if (var1 == null) {
         return null;
      } else {
         boolean var2 = Boolean.parseBoolean(var1.getValue().trim());
         CustomBooleanSandboxOption var3 = new CustomBooleanSandboxOption(var0.id, var2);
         return !var3.parseCommon(var0) ? null : var3;
      }
   }
}
