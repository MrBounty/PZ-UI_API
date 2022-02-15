package zombie.util;

import java.util.HashMap;

public final class SharedStrings {
   private final HashMap strings = new HashMap();

   public String get(String var1) {
      String var2 = (String)this.strings.get(var1);
      if (var2 == null) {
         this.strings.put(var1, var1);
         var2 = var1;
      }

      return var2;
   }

   public void clear() {
      this.strings.clear();
   }
}
