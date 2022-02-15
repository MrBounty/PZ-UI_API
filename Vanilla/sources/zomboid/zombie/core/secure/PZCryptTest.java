package zombie.core.secure;

import org.junit.Assert;
import org.junit.Test;

public class PZCryptTest extends Assert {
   @Test
   public void hash() {
      String var1 = PZcrypt.hash("123456");
      assertEquals("$2a$12$O/BFHoDFPrfFaNPAACmWpuPkOtwkznuRQ7saS6/ouHjTT9KuVcKfq", var1);
   }

   @Test
   public void hashSalt() {
      String var1 = PZcrypt.hashSalt("1234567");
      String var2 = PZcrypt.hashSalt("1234567");
      assertNotEquals(var1, var2);
      boolean var3 = PZcrypt.checkHashSalt(var1, "1234567");
      assertEquals(true, var3);
      var3 = PZcrypt.checkHashSalt(var1, "1238567");
      assertEquals(false, var3);
      var3 = PZcrypt.checkHashSalt(var2, "1234567");
      assertEquals(true, var3);
      var3 = PZcrypt.checkHashSalt(var2, "dnfgdf;godf;ogdogi;");
      assertEquals(false, var3);
   }
}
