package zombie.iso;

import org.joml.Vector2i;
import org.junit.Assert;
import org.junit.Test;

public class test_DiamondMatrixIterator extends Assert {
   @Test
   public void test3a() {
      byte var1 = 3;
      DiamondMatrixIterator var2 = new DiamondMatrixIterator(var1);

      for(int var3 = 0; var3 <= var1 * var1; ++var3) {
         System.out.println("i=" + var3 + " i2line=" + var2.i2line(var3));
      }

      assertEquals(new Vector2i(0, 0), var2.i2line(0));
      assertEquals(new Vector2i(0, 1), var2.i2line(1));
      assertEquals(new Vector2i(1, 1), var2.i2line(2));
      assertEquals(new Vector2i(0, 2), var2.i2line(3));
      assertEquals(new Vector2i(1, 2), var2.i2line(4));
      assertEquals(new Vector2i(2, 2), var2.i2line(5));
      assertEquals(new Vector2i(0, 3), var2.i2line(6));
      assertEquals(new Vector2i(1, 3), var2.i2line(7));
      assertEquals(new Vector2i(0, 4), var2.i2line(8));
   }

   @Test
   public void test3() {
      byte var1 = 3;
      DiamondMatrixIterator var2 = new DiamondMatrixIterator(var1);

      for(int var3 = 0; var3 <= var1 * var1; ++var3) {
         System.out.println("i=" + var3 + " i2line=" + var2.i2line(var3) + " line2coord=" + var2.line2coord(var2.i2line(var3)));
      }

      assertEquals(new Vector2i(0, 0), var2.line2coord(var2.i2line(0)));
      assertEquals(new Vector2i(0, 1), var2.line2coord(var2.i2line(1)));
      assertEquals(new Vector2i(1, 0), var2.line2coord(var2.i2line(2)));
      assertEquals(new Vector2i(0, 2), var2.line2coord(var2.i2line(3)));
      assertEquals(new Vector2i(1, 1), var2.line2coord(var2.i2line(4)));
      assertEquals(new Vector2i(2, 0), var2.line2coord(var2.i2line(5)));
      assertEquals(new Vector2i(1, 2), var2.line2coord(var2.i2line(6)));
      assertEquals(new Vector2i(2, 1), var2.line2coord(var2.i2line(7)));
      assertEquals(new Vector2i(2, 2), var2.line2coord(var2.i2line(8)));
      assertEquals((Object)null, var2.line2coord(var2.i2line(9)));
   }

   @Test
   public void test3i() {
      byte var1 = 3;
      Vector2i var2 = new Vector2i();
      DiamondMatrixIterator var3 = new DiamondMatrixIterator(var1);

      for(int var4 = 0; var4 <= var1 * var1; ++var4) {
         var3.next(var2);
         System.out.println("i=" + var4 + " v=" + var2);
      }

      var3.reset();
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 2), var2);
      assertEquals(false, var3.next(var2));
   }

   @Test
   public void test4() {
      byte var1 = 4;
      DiamondMatrixIterator var2 = new DiamondMatrixIterator(var1);

      for(int var3 = 0; var3 <= var1 * var1; ++var3) {
         System.out.println("i=" + var3 + " i2line=" + var2.i2line(var3) + " line2coord=" + var2.line2coord(var2.i2line(var3)));
      }

      assertEquals(new Vector2i(0, 0), var2.line2coord(var2.i2line(0)));
      assertEquals(new Vector2i(0, 1), var2.line2coord(var2.i2line(1)));
      assertEquals(new Vector2i(1, 0), var2.line2coord(var2.i2line(2)));
      assertEquals(new Vector2i(0, 2), var2.line2coord(var2.i2line(3)));
      assertEquals(new Vector2i(1, 1), var2.line2coord(var2.i2line(4)));
      assertEquals(new Vector2i(2, 0), var2.line2coord(var2.i2line(5)));
      assertEquals(new Vector2i(0, 3), var2.line2coord(var2.i2line(6)));
      assertEquals(new Vector2i(1, 2), var2.line2coord(var2.i2line(7)));
      assertEquals(new Vector2i(2, 1), var2.line2coord(var2.i2line(8)));
      assertEquals(new Vector2i(3, 0), var2.line2coord(var2.i2line(9)));
      assertEquals(new Vector2i(1, 3), var2.line2coord(var2.i2line(10)));
      assertEquals(new Vector2i(2, 2), var2.line2coord(var2.i2line(11)));
      assertEquals(new Vector2i(3, 1), var2.line2coord(var2.i2line(12)));
      assertEquals(new Vector2i(2, 3), var2.line2coord(var2.i2line(13)));
      assertEquals(new Vector2i(3, 2), var2.line2coord(var2.i2line(14)));
      assertEquals(new Vector2i(3, 3), var2.line2coord(var2.i2line(15)));
      assertEquals((Object)null, var2.line2coord(var2.i2line(16)));
   }

   @Test
   public void test4i() {
      byte var1 = 4;
      Vector2i var2 = new Vector2i();
      DiamondMatrixIterator var3 = new DiamondMatrixIterator(var1);

      for(int var4 = 0; var4 <= var1 * var1; ++var4) {
         var3.next(var2);
         System.out.println("i=" + var4 + " v=" + var2);
      }

      var3.reset();
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(0, 3), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(3, 0), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(1, 3), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(3, 1), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(2, 3), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(3, 2), var2);
      assertEquals(true, var3.next(var2));
      assertEquals(new Vector2i(3, 3), var2);
      assertEquals(false, var3.next(var2));
   }

   @Test
   public void test10() {
      byte var1 = 10;
      DiamondMatrixIterator var2 = new DiamondMatrixIterator(var1);

      for(int var3 = 0; var3 <= var1 * var1; ++var3) {
         System.out.println("i=" + var3 + " i2line=" + var2.i2line(var3) + " line2coord=" + var2.line2coord(var2.i2line(var3)));
      }

      Vector2i var4 = new Vector2i();

      for(var4.y = 0; var4.y < var1; ++var4.y) {
         for(var4.x = 0; var4.x <= var4.y; ++var4.x) {
            System.out.println("v=" + var4 + " line2coord=" + var2.line2coord(var4));
         }
      }

      for(var4.y = var1; var4.y <= var1 * 2; ++var4.y) {
         for(var4.x = 0; var4.x <= 18 - var4.y; ++var4.x) {
            System.out.println("v=" + var4 + " line2coord=" + var2.line2coord(var4));
         }
      }

      assertEquals(new Vector2i(0, 0), var2.line2coord(var2.i2line(0)));
      assertEquals(new Vector2i(0, 1), var2.line2coord(var2.i2line(1)));
      assertEquals(new Vector2i(1, 0), var2.line2coord(var2.i2line(2)));
      assertEquals(new Vector2i(0, 2), var2.line2coord(var2.i2line(3)));
      assertEquals(new Vector2i(1, 1), var2.line2coord(var2.i2line(4)));
      assertEquals(new Vector2i(2, 0), var2.line2coord(var2.i2line(5)));
      assertEquals(new Vector2i(0, 9), var2.line2coord(var2.i2line(45)));
      assertEquals(new Vector2i(4, 5), var2.line2coord(var2.i2line(49)));
      assertEquals(new Vector2i(5, 4), var2.line2coord(var2.i2line(50)));
      assertEquals(new Vector2i(9, 0), var2.line2coord(var2.i2line(54)));
      assertEquals(new Vector2i(8, 9), var2.line2coord(var2.i2line(97)));
      assertEquals(new Vector2i(9, 8), var2.line2coord(var2.i2line(98)));
      assertEquals(new Vector2i(9, 9), var2.line2coord(var2.i2line(99)));
      assertEquals((Object)null, var2.line2coord(var2.i2line(100)));
      assertEquals((Object)null, var2.line2coord(var2.i2line(34536)));
   }
}
