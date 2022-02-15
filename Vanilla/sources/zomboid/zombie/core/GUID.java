package zombie.core;

import java.util.UUID;

public class GUID {
   public static String generateGUID() {
      UUID var0 = UUID.randomUUID();
      return var0.toString();
   }
}
