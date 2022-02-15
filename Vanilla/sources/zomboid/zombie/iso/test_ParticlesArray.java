package zombie.iso;

import java.io.PrintStream;
import java.util.Comparator;
import org.junit.Assert;
import org.junit.Test;
import zombie.core.Rand;

public class test_ParticlesArray extends Assert {
   @Test
   public void test_ParticlesArray_functional() {
      ParticlesArray var1 = new ParticlesArray();
      var1.addParticle(new Integer(1));
      var1.addParticle(new Integer(2));
      var1.addParticle(new Integer(3));
      var1.addParticle(new Integer(4));
      var1.addParticle(new Integer(5));
      var1.addParticle(new Integer(6));
      var1.addParticle(new Integer(7));
      var1.addParticle(new Integer(8));
      var1.addParticle(new Integer(9));
      assertEquals(9L, (long)var1.size());
      assertEquals(9L, (long)var1.getCount());

      for(int var2 = 0; var2 < 9; ++var2) {
         assertEquals((long)(var2 + 1), (long)(Integer)var1.get(var2));
      }

      var1.deleteParticle(0);
      var1.deleteParticle(1);
      var1.deleteParticle(4);
      var1.deleteParticle(7);
      var1.deleteParticle(8);
      assertEquals(9L, (long)var1.size());
      assertEquals(4L, (long)var1.getCount());
      assertEquals((Object)null, var1.get(0));
      assertEquals((Object)null, var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals((Object)null, var1.get(4));
      assertEquals(6L, (long)(Integer)var1.get(5));
      assertEquals(7L, (long)(Integer)var1.get(6));
      assertEquals((Object)null, var1.get(7));
      assertEquals((Object)null, var1.get(8));
      var1.defragmentParticle();
      assertEquals(9L, (long)var1.size());
      assertEquals(4L, (long)var1.getCount());
      assertEquals(7L, (long)(Integer)var1.get(0));
      assertEquals(6L, (long)(Integer)var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals((Object)null, var1.get(4));
      assertEquals((Object)null, var1.get(5));
      assertEquals((Object)null, var1.get(6));
      assertEquals((Object)null, var1.get(7));
      assertEquals((Object)null, var1.get(8));
      var1.addParticle(new Integer(11));
      var1.addParticle(new Integer(12));
      var1.addParticle(new Integer(13));
      var1.addParticle(new Integer(14));
      var1.addParticle(new Integer(15));
      var1.addParticle(new Integer(16));
      assertEquals(10L, (long)var1.size());
      assertEquals(10L, (long)var1.getCount());
      assertEquals(7L, (long)(Integer)var1.get(0));
      assertEquals(6L, (long)(Integer)var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals(11L, (long)(Integer)var1.get(4));
      assertEquals(12L, (long)(Integer)var1.get(5));
      assertEquals(13L, (long)(Integer)var1.get(6));
      assertEquals(14L, (long)(Integer)var1.get(7));
      assertEquals(15L, (long)(Integer)var1.get(8));
      assertEquals(16L, (long)(Integer)var1.get(9));
      var1.deleteParticle(0);
      var1.deleteParticle(1);
      var1.deleteParticle(4);
      var1.deleteParticle(7);
      var1.deleteParticle(8);
      var1.deleteParticle(9);
      assertEquals(10L, (long)var1.size());
      assertEquals(4L, (long)var1.getCount());
      assertEquals((Object)null, var1.get(0));
      assertEquals((Object)null, var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals((Object)null, var1.get(4));
      assertEquals(12L, (long)(Integer)var1.get(5));
      assertEquals(13L, (long)(Integer)var1.get(6));
      assertEquals((Object)null, var1.get(7));
      assertEquals((Object)null, var1.get(8));
      assertEquals((Object)null, var1.get(9));
      var1.defragmentParticle();
      assertEquals(10L, (long)var1.size());
      assertEquals(4L, (long)var1.getCount());
      assertEquals(13L, (long)(Integer)var1.get(0));
      assertEquals(12L, (long)(Integer)var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals((Object)null, var1.get(4));
      assertEquals((Object)null, var1.get(5));
      assertEquals((Object)null, var1.get(6));
      assertEquals((Object)null, var1.get(7));
      assertEquals((Object)null, var1.get(8));
      assertEquals((Object)null, var1.get(9));
      var1.addParticle(new Integer(21));
      var1.addParticle(new Integer(22));
      assertEquals(10L, (long)var1.size());
      assertEquals(6L, (long)var1.getCount());
      assertEquals(13L, (long)(Integer)var1.get(0));
      assertEquals(12L, (long)(Integer)var1.get(1));
      assertEquals(3L, (long)(Integer)var1.get(2));
      assertEquals(4L, (long)(Integer)var1.get(3));
      assertEquals(21L, (long)(Integer)var1.get(4));
      assertEquals(22L, (long)(Integer)var1.get(5));
      assertEquals((Object)null, var1.get(6));
      assertEquals((Object)null, var1.get(7));
      assertEquals((Object)null, var1.get(8));
      assertEquals((Object)null, var1.get(9));
      assertEquals(6L, (long)var1.addParticle(new Integer(31)));
      assertEquals(7L, (long)var1.addParticle(new Integer(32)));
      assertEquals(8L, (long)var1.addParticle(new Integer(33)));
      assertEquals(9L, (long)var1.addParticle(new Integer(34)));
      assertEquals(10L, (long)var1.addParticle(new Integer(35)));
      assertEquals(11L, (long)var1.size());
      assertEquals(11L, (long)var1.getCount());
      var1.deleteParticle(4);
      assertEquals(11L, (long)var1.size());
      assertEquals(10L, (long)var1.getCount());
      assertEquals(4L, (long)var1.addParticle(new Integer(36)));
   }

   @Test
   public void test_ParticlesArray_Failure() {
      ParticlesArray var1 = new ParticlesArray();
      var1.addParticle(new Integer(1));
      var1.addParticle(new Integer(2));
      var1.addParticle(new Integer(3));
      var1.addParticle(new Integer(4));
      var1.addParticle(new Integer(5));
      var1.addParticle(new Integer(6));
      var1.addParticle(new Integer(7));
      var1.addParticle(new Integer(8));
      var1.addParticle(new Integer(9));
      assertEquals(9L, (long)var1.size());
      assertEquals(9L, (long)var1.getCount());

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         assertEquals((long)(var2 + 1), (long)(Integer)var1.get(var2));
      }

      var1.deleteParticle(-1);
      var1.deleteParticle(100);
      var1.addParticle((Object)null);
      assertEquals(9L, (long)var1.size());
      assertEquals(9L, (long)var1.getCount());

      for(var2 = 0; var2 < 9; ++var2) {
         assertEquals((long)(var2 + 1), (long)(Integer)var1.get(var2));
      }

      var1.deleteParticle(3);
      var1.deleteParticle(3);
      var1.deleteParticle(3);
   }

   @Test
   public void test_ParticlesArray_time() {
      ParticlesArray var2 = new ParticlesArray();
      long var3 = System.currentTimeMillis();

      for(int var5 = 0; var5 < 1000000; ++var5) {
         var2.addParticle(new Integer(var5));
      }

      long var11 = System.currentTimeMillis();
      System.out.println("Add 1000000 elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      int var1 = 0;
      var3 = System.currentTimeMillis();

      int var7;
      for(var7 = 0; var7 < 1000000; ++var7) {
         if (var2.deleteParticle(var7)) {
            ++var1;
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Delete " + var1 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();

      for(var7 = 0; var7 < 1000000; ++var7) {
         var2.addParticle(new Integer(var7));
      }

      var11 = System.currentTimeMillis();
      System.out.println("Add 1000000 elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      Rand.init();
      var1 = 0;
      var3 = System.currentTimeMillis();

      for(var7 = 0; var7 < 500000; ++var7) {
         if (var2.deleteParticle(Rand.Next(1000000))) {
            ++var1;
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Delete random " + var1 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();

      for(var7 = 0; var7 < 1000000; ++var7) {
         var2.addParticle(new Integer(var7));
      }

      var11 = System.currentTimeMillis();
      System.out.println("Add 1000000 elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      Comparator var12 = (var0, var1x) -> {
         return var0.compareTo(var1x);
      };
      var3 = System.currentTimeMillis();
      var2.sort(var12);
      var11 = System.currentTimeMillis();
      PrintStream var10000 = System.out;
      int var10001 = var2.size();
      var10000.println("Sort " + var10001 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var1 = 0;
      var3 = System.currentTimeMillis();

      int var8;
      for(var8 = 0; var8 < 500000; ++var8) {
         if (var2.deleteParticle(Rand.Next(1000000))) {
            ++var1;
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Delete random " + var1 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();
      var2.defragmentParticle();
      var11 = System.currentTimeMillis();
      var10000 = System.out;
      var10001 = var2.size();
      var10000.println("Defragment " + var10001 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();

      for(var8 = 0; var8 < 1000000; ++var8) {
         var2.addParticle(new Integer(var8));
      }

      var11 = System.currentTimeMillis();
      System.out.println("Add 1000000 elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var1 = 0;
      var3 = System.currentTimeMillis();

      for(var8 = 0; var8 < 500000; ++var8) {
         if (var2.deleteParticle(Rand.Next(1000000))) {
            ++var1;
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Delete random " + var1 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();

      for(var8 = 0; var8 < 1000000; ++var8) {
         var2.addParticle(new Integer(var8));
      }

      var11 = System.currentTimeMillis();
      System.out.println("Add 1000000 elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var1 = 0;
      var3 = System.currentTimeMillis();

      for(var8 = 0; var8 < 1000000; ++var8) {
         if (var2.deleteParticle(Rand.Next(1000000))) {
            ++var1;
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Delete random " + var1 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
      var3 = System.currentTimeMillis();
      var8 = 0;

      for(int var9 = 0; var9 < 100000; ++var9) {
         for(int var10 = 0; var10 < var2.size(); ++var10) {
            if (var2.get(var10) == null) {
               var2.set(var10, new Integer(var9));
               ++var8;
               break;
            }
         }
      }

      var11 = System.currentTimeMillis();
      System.out.println("Simple add " + var8 + " elements = " + (var11 - var3) + " ms (size=" + var2.size() + ", count=" + var2.getCount() + ")");
   }
}
