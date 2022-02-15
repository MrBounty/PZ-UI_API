package zombie.util;

public class Type {
   public static Object tryCastTo(Object var0, Class var1) {
      return var1.isInstance(var0) ? var1.cast(var0) : null;
   }

   public static boolean asBoolean(Object var0) {
      return asBoolean(var0, false);
   }

   public static boolean asBoolean(Object var0, boolean var1) {
      if (var0 == null) {
         return var1;
      } else {
         Boolean var2 = (Boolean)tryCastTo(var0, Boolean.class);
         return var2 == null ? var1 : var2;
      }
   }
}
