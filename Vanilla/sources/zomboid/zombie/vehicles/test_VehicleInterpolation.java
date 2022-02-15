package zombie.vehicles;

import java.nio.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;
import zombie.GameTime;

public class test_VehicleInterpolation extends Assert {
   @Test
   public void normalTest() {
      System.out.print("START: normalTest\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(500);

      for(int var4 = 1; var4 < 30; ++var4) {
         System.out.print("Interation " + var4 + "\n");
         var1.position(0);
         var1.putLong(System.nanoTime());
         var1.position(0);
         var3.interpolationDataAdd(var1, (float)var4, (float)var4, 0.0F);
         boolean var5 = var3.interpolationDataGet(var2);
         if (var4 < 6) {
            assertEquals(false, var5);
         } else {
            assertEquals(true, var5);
            float var6 = var2[0];
            assertEquals((float)(var4 - 6 + 1), var6, 0.2F);
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

   }

   @Test
   public void normalZeroTest() {
      System.out.print("START: normalZeroTest\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(0);

      for(int var4 = 1; var4 < 30; ++var4) {
         System.out.print("Interation " + var4 + "\n");
         var1.position(0);
         var1.putLong(System.nanoTime());
         var1.position(0);
         var3.interpolationDataAdd(var1, (float)var4, (float)var4, 0.0F);
         boolean var5 = var3.interpolationDataGet(var2);
         assertEquals(true, var5);
         float var6 = var2[0];
         assertEquals((float)(var4 - 1 + 1), var6, 0.2F);

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var8) {
            var8.printStackTrace();
         }
      }

   }

   @Test
   public void interpolationTest() {
      System.out.print("START: interpolationTest\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(500);

      for(int var4 = 1; var4 < 30; ++var4) {
         System.out.print("Interation " + var4 + "\n");
         var1.position(0);
         if (var4 % 2 == 1) {
            var1.putLong(System.nanoTime());
            var1.position(0);
            var3.interpolationDataAdd(var1, (float)var4, (float)var4, 0.0F);
         }

         boolean var5 = var3.interpolationDataGet(var2);
         if (var4 < 7) {
            assertEquals(false, var5);
         } else {
            assertEquals(true, var5);
            float var6 = var2[0];
            assertEquals((float)(var4 - 6 + 1), var6, 0.2F);
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

   }

   @Test
   public void testBufferRestoring() {
      System.out.print("START: normalTestBufferRestoring\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(400);

      for(int var4 = 1; var4 < 30; ++var4) {
         System.out.print("Interation " + var4 + "\n");
         var1.position(0);
         var1.putLong(System.nanoTime());
         var1.position(0);
         var3.interpolationDataAdd(var1, (float)var4, (float)var4, 0.0F);
         boolean var5 = var3.interpolationDataGet(var2);
         if (var4 >= 5 && (var4 < 11 || var4 >= 15)) {
            assertEquals(true, var5);
            float var6 = var2[0];
            assertEquals((float)(var4 - 5 + 1), var6, 0.2F);
         } else {
            assertEquals(false, var5);
         }

         try {
            if (var4 == 10) {
               Thread.sleep(800L);
            }

            Thread.sleep(100L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

   }

   @Test
   public void normalTestBufferRestoring2() {
      System.out.print("START: normalTestBufferRestoring2\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(400);

      try {
         for(int var4 = 1; var4 < 40; ++var4) {
            System.out.print("Interation " + var4 + "\n");
            var1.position(0);
            var1.putLong(System.nanoTime());
            var1.position(0);
            if (var4 < 15 || var4 > 20) {
               var3.interpolationDataAdd(var1, (float)var4, 0.0F, 0.0F);
            }

            boolean var5 = var3.interpolationDataGet(var2);
            if (var4 >= 5 && (var4 < 18 || var4 >= 25)) {
               assertEquals(true, var5);
               float var6 = var2[0];
               assertEquals((float)(var4 - 5 + 1), var6, 0.1F);
            } else {
               assertEquals(false, var5);
            }

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var10) {
               var10.printStackTrace();
            }
         }
      } finally {
         ;
      }

   }

   @Test
   public void normalTestPR() {
      System.out.print("START: normalTestPR\n");
      GameTime.setServerTimeShift(0L);
      ByteBuffer var1 = ByteBuffer.allocateDirect(255);
      float[] var2 = new float[27];
      VehicleInterpolation var3 = new VehicleInterpolation(500);

      for(int var4 = 1; var4 < 30; ++var4) {
         System.out.print("Interation " + var4 + "\n");
         var1.position(0);
         var1.putLong(System.nanoTime());
         var1.position(0);
         var3.interpolationDataAdd(var1, (float)var4, (float)var4, 0.0F);
         boolean var5 = var3.interpolationDataGetPR(var2);
         if (var4 < 6) {
            assertEquals(false, var5);
         } else {
            assertEquals(true, var5);
            float var6 = var2[0];
            assertEquals((float)(var4 - 6 + 1), var6, 1.0F);
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      }

   }
}
