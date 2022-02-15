package zombie.sandbox;

import zombie.core.math.PZMath;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public class CustomSandboxOption {
   public final String m_id;
   public String m_page;
   public String m_translation;

   CustomSandboxOption(String var1) {
      this.m_id = var1;
   }

   static float getValueFloat(ScriptParser.Block var0, String var1, float var2) {
      ScriptParser.Value var3 = var0.getValue(var1);
      return var3 == null ? var2 : PZMath.tryParseFloat(var3.getValue().trim(), var2);
   }

   static int getValueInt(ScriptParser.Block var0, String var1, int var2) {
      ScriptParser.Value var3 = var0.getValue(var1);
      return var3 == null ? var2 : PZMath.tryParseInt(var3.getValue().trim(), var2);
   }

   boolean parseCommon(ScriptParser.Block var1) {
      ScriptParser.Value var2 = var1.getValue("page");
      if (var2 != null) {
         this.m_page = StringUtils.discardNullOrWhitespace(var2.getValue().trim());
      }

      ScriptParser.Value var3 = var1.getValue("translation");
      if (var3 != null) {
         this.m_translation = StringUtils.discardNullOrWhitespace(var3.getValue().trim());
      }

      return true;
   }
}
