package zombie.sandbox;

import zombie.scripting.ScriptParser;

public final class CustomStringSandboxOption extends CustomSandboxOption {
   public final String defaultValue;

   CustomStringSandboxOption(String var1, String var2) {
      super(var1);
      this.defaultValue = var2;
   }

   static CustomStringSandboxOption parse(ScriptParser.Block var0) {
      ScriptParser.Value var1 = var0.getValue("default");
      if (var1 == null) {
         return null;
      } else {
         CustomStringSandboxOption var2 = new CustomStringSandboxOption(var0.id, var1.getValue().trim());
         return !var2.parseCommon(var0) ? null : var2;
      }
   }
}
