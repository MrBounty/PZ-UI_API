package zombie.core.Collections;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

class UtilUnsafe {
   private UtilUnsafe() {
   }

   public static Unsafe getUnsafe() {
      if (UtilUnsafe.class.getClassLoader() == null) {
         return Unsafe.getUnsafe();
      } else {
         try {
            Field var0 = Unsafe.class.getDeclaredField("theUnsafe");
            var0.setAccessible(true);
            return (Unsafe)var0.get(UtilUnsafe.class);
         } catch (Exception var1) {
            throw new RuntimeException("Could not obtain access to sun.misc.Unsafe", var1);
         }
      }
   }
}
