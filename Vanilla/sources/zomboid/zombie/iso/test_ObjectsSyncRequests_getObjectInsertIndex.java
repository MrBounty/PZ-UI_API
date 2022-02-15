package zombie.iso;

import org.junit.Assert;
import org.junit.Test;

public class test_ObjectsSyncRequests_getObjectInsertIndex extends Assert {
   @Test
   public void test_getInsertIndex() {
      long[] var1 = new long[]{13L, 88L, 51L};
      long[] var2 = new long[]{8L, 13L, 52L, 21L, 88L, 36L, 51L, 15L};
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 8L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 13L));
      assertEquals(1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 52L));
      assertEquals(1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 21L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 88L));
      assertEquals(2L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 36L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 51L));
      assertEquals(3L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 15L));
   }

   @Test
   public void test_getInsertIndex2() {
      long[] var1 = new long[0];
      long[] var2 = new long[]{81L, 45L, 72L};
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 8L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 13L));
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 81L));
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 45L));
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 72L));
   }

   @Test
   public void test_getInsertIndex3() {
      long[] var1 = new long[]{71L, 66L, 381L};
      long[] var2 = new long[]{55L, 81L, 71L, 41L, 66L, 381L, 68L};
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 55L));
      assertEquals(0L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 81L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 71L));
      assertEquals(1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 41L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 66L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 381L));
      assertEquals(3L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 68L));
      assertEquals(-1L, (long)ObjectsSyncRequests.getObjectInsertIndex(var1, var2, 33L));
   }
}
