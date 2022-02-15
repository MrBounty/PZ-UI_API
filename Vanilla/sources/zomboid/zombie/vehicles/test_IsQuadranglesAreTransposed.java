package zombie.vehicles;

import org.junit.Assert;
import org.junit.Test;
import zombie.iso.Vector2;

public class test_IsQuadranglesAreTransposed extends Assert {
   @Test
   public void testIsQuadranglesAreTransposed_1() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_2() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.5F, 2.0F), new Vector2(3.5F, 2.0F), new Vector2(3.5F, 4.0F), new Vector2(2.5F, 4.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_3() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.5F), new Vector2(3.0F, 2.5F), new Vector2(3.0F, 4.5F), new Vector2(2.0F, 4.5F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_4() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.5F, 2.5F), new Vector2(3.5F, 2.5F), new Vector2(3.5F, 4.5F), new Vector2(2.5F, 4.5F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_5() {
      Vector2[] var1 = new Vector2[]{new Vector2(1.0F, 1.0F), new Vector2(6.0F, 1.0F), new Vector2(6.0F, 6.0F), new Vector2(1.0F, 6.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_6() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 5.0F), new Vector2(2.0F, 5.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(1.0F, 3.0F), new Vector2(5.0F, 3.0F), new Vector2(5.0F, 4.0F), new Vector2(1.0F, 4.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_7() {
      Vector2[] var1 = new Vector2[]{new Vector2(10.0F, 10.0F), new Vector2(15.0F, 15.0F), new Vector2(18.0F, 12.0F), new Vector2(13.0F, 7.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(18.0F, 9.0F), new Vector2(14.0F, 16.0F), new Vector2(11.0F, 14.0F), new Vector2(17.0F, 7.0F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_8() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(4.0F, 2.0F), new Vector2(5.0F, 2.0F), new Vector2(5.0F, 4.0F), new Vector2(4.0F, 4.0F)};
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_9() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(2.0F, 5.0F), new Vector2(3.0F, 5.0F), new Vector2(3.0F, 10.0F), new Vector2(2.0F, 10.0F)};
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_10() {
      Vector2[] var1 = new Vector2[]{new Vector2(2.0F, 2.0F), new Vector2(3.0F, 2.0F), new Vector2(3.0F, 4.0F), new Vector2(2.0F, 4.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(5.0F, 5.0F), new Vector2(6.0F, 5.0F), new Vector2(6.0F, 7.0F), new Vector2(5.0F, 7.0F)};
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_11() {
      Vector2[] var1 = new Vector2[]{new Vector2(10.0F, 10.0F), new Vector2(15.0F, 15.0F), new Vector2(18.0F, 12.0F), new Vector2(13.0F, 7.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(28.0F, 9.0F), new Vector2(24.0F, 16.0F), new Vector2(21.0F, 14.0F), new Vector2(27.0F, 7.0F)};
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_12() {
      Vector2[] var1 = new Vector2[]{new Vector2(10860.373F, 9928.479F), new Vector2(10860.373F, 9926.521F), new Vector2(10865.627F, 9926.521F), new Vector2(10865.627F, 9928.479F)};
      Vector2[] var2 = new Vector2[]{new Vector2(10864.479F, 9929.127F), new Vector2(10862.521F, 9929.127F), new Vector2(10862.521F, 9923.873F), new Vector2(10864.479F, 9923.873F)};
      assertEquals(true, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_13() {
      Vector2[] var1 = new Vector2[]{new Vector2(10.0F, 10.0F), new Vector2(15.0F, 15.0F), new Vector2(18.0F, 12.0F)};
      Vector2[] var2 = new Vector2[]{new Vector2(28.0F, 9.0F), new Vector2(24.0F, 16.0F), new Vector2(21.0F, 14.0F)};
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected(var1, var2));
   }

   @Test
   public void testIsQuadranglesAreTransposed_14() {
      assertEquals(false, QuadranglesIntersection.IsQuadranglesAreIntersected((Vector2[])null, (Vector2[])null));
   }
}
