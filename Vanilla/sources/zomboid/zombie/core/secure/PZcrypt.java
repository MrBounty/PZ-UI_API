package zombie.core.secure;

import org.mindrot.jbcrypt.BCrypt;

public class PZcrypt {
   static String salt = "$2a$12$O/BFHoDFPrfFaNPAACmWpu";

   public static String hash(String var0, boolean var1) {
      return var1 && var0.isEmpty() ? var0 : BCrypt.hashpw(var0, salt);
   }

   public static String hash(String var0) {
      return hash(var0, true);
   }

   public static String hashSalt(String var0) {
      return BCrypt.hashpw(var0, BCrypt.gensalt(12));
   }

   public static boolean checkHashSalt(String var0, String var1) {
      return BCrypt.checkpw(var1, var0);
   }
}
