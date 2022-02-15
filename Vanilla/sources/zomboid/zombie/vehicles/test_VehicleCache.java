package zombie.vehicles;

import org.junit.Assert;
import org.junit.Test;

public class test_VehicleCache extends Assert {
   @Test
   public void test_getInsertIndex() {
      VehicleCache.vehicleUpdate((short)1, 10.0F, 20.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)2, 10.0F, 21.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)3, 15.0F, 22.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)4, 1010.0F, 1020.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)5, 2000.0F, 2020.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)6, 3010.0F, 3000.0F, 0.0F);
      assertNotEquals((Object)null, VehicleCache.vehicleGet(1, 2));
      assertEquals((Object)null, VehicleCache.vehicleGet(1, 3));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(10.0F, 20.0F));
      assertEquals((Object)null, VehicleCache.vehicleGet(10.0F, 30.0F));
      assertEquals(3L, (long)VehicleCache.vehicleGet(10.0F, 20.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(1010.0F, 1020.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(2000.0F, 2020.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(3010.0F, 3000.0F).size());
      VehicleCache.vehicleUpdate((short)1, 12.0F, 200.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)2, 10.0F, 210.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)3, 10.0F, 25.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)4, 1020.0F, 1020.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)5, 2000.0F, 2030.0F, 0.0F);
      VehicleCache.vehicleUpdate((short)6, 3010.3F, 3000.1F, 0.0F);
      assertNotEquals((Object)null, VehicleCache.vehicleGet(10.0F, 20.0F));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(10.0F, 200.0F));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(10.0F, 210.0F));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(1020.0F, 1020.0F));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(2000.0F, 2030.0F));
      assertNotEquals((Object)null, VehicleCache.vehicleGet(3010.0F, 3000.0F));
      assertEquals(1L, (long)VehicleCache.vehicleGet(10.0F, 20.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(10.0F, 200.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(10.0F, 210.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(1020.0F, 1020.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(2000.0F, 2030.0F).size());
      assertEquals(1L, (long)VehicleCache.vehicleGet(3010.0F, 3000.0F).size());
      assertEquals(0L, (long)VehicleCache.vehicleGet(1010.0F, 1020.0F).size());
      assertEquals(0L, (long)VehicleCache.vehicleGet(2000.0F, 2020.0F).size());
      assertEquals(3L, (long)((VehicleCache)VehicleCache.vehicleGet(10.0F, 20.0F).get(0)).id);
      assertEquals(1L, (long)((VehicleCache)VehicleCache.vehicleGet(10.0F, 200.0F).get(0)).id);
      assertEquals(2L, (long)((VehicleCache)VehicleCache.vehicleGet(10.0F, 210.0F).get(0)).id);
      assertEquals(4L, (long)((VehicleCache)VehicleCache.vehicleGet(1020.0F, 1020.0F).get(0)).id);
      assertEquals(5L, (long)((VehicleCache)VehicleCache.vehicleGet(2000.0F, 2030.0F).get(0)).id);
      assertEquals(6L, (long)((VehicleCache)VehicleCache.vehicleGet(3010.0F, 3000.0F).get(0)).id);
   }
}
