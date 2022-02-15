package zombie.sandbox;

import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class CustomEnumSandboxOption extends CustomSandboxOption {
   public final int numValues;
   public final int defaultValue;
   public String m_valueTranslation;

   CustomEnumSandboxOption(String var1, int var2, int var3) {
      super(var1);
      this.numValues = var2;
      this.defaultValue = var3;
   }

   static CustomEnumSandboxOption parse(ScriptParser.Block var0) {
      int var1 = getValueInt(var0, "numValues", -1);
      int var2 = getValueInt(var0, "default", -1);
      if (var1 > 0 && var2 > 0) {
         CustomEnumSandboxOption var3 = new CustomEnumSandboxOption(var0.id, var1, var2);
         if (!var3.parseCommon(var0)) {
            return null;
         } else {
            ScriptParser.Value var4 = var0.getValue("valueTranslation");
            if (var4 != null) {
               var3.m_valueTranslation = StringUtils.discardNullOrWhitespace(var4.getValue().trim());
            }

            return var3;
         }
      } else {
         return null;
      }
   }
}
