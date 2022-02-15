package zombie.util.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.util.ICloner;
import zombie.util.Pool;
import zombie.util.StringUtils;

public class PZArrayUtil {
   public static final int[] emptyIntArray = new int[0];
   public static final float[] emptyFloatArray = new float[0];

   public static Object pickRandom(Object[] var0) {
      if (var0.length == 0) {
         return null;
      } else {
         int var1 = Rand.Next(var0.length);
         return var0[var1];
      }
   }

   public static Object pickRandom(List var0) {
      if (var0.isEmpty()) {
         return null;
      } else {
         int var1 = Rand.Next(var0.size());
         return var0.get(var1);
      }
   }

   public static Object pickRandom(Collection var0) {
      if (var0.isEmpty()) {
         return null;
      } else {
         int var1 = Rand.Next(var0.size());
         return getElementAt(var0, var1);
      }
   }

   public static Object pickRandom(Iterable var0) {
      int var1 = getSize(var0);
      if (var1 == 0) {
         return null;
      } else {
         int var2 = Rand.Next(var1);
         return getElementAt(var0, var2);
      }
   }

   public static int getSize(Iterable var0) {
      int var1 = 0;
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         ++var1;
         var2.next();
      }

      return var1;
   }

   public static Object getElementAt(Iterable var0, int var1) throws ArrayIndexOutOfBoundsException {
      Object var2 = null;
      Iterator var3 = var0.iterator();

      for(int var4 = 0; var4 <= var1; ++var4) {
         if (!var3.hasNext()) {
            throw new ArrayIndexOutOfBoundsException(var4);
         }

         if (var4 == var1) {
            var2 = var3.next();
         }
      }

      return var2;
   }

   public static void copy(ArrayList var0, ArrayList var1) {
      copy(var0, var1, (var0x) -> {
         return var0x;
      });
   }

   public static void copy(ArrayList var0, ArrayList var1, ICloner var2) {
      if (var0 != var1) {
         var0.clear();
         var0.ensureCapacity(var1.size());

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            Object var4 = var1.get(var3);
            var0.add(var2.clone(var4));
         }

      }
   }

   public static int indexOf(Object[] var0, Predicate var1) {
      try {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var3 = var0[var2];
            if (var1.test(var3)) {
               int var4 = var2;
               return var4;
            }
         }

         byte var8 = -1;
         return var8;
      } finally {
         Pool.tryRelease((Object)var1);
      }
   }

   public static int indexOf(List var0, Predicate var1) {
      try {
         int var2 = -1;
         int var3 = 0;

         while(true) {
            if (var3 < var0.size()) {
               Object var4 = var0.get(var3);
               if (!var1.test(var4)) {
                  ++var3;
                  continue;
               }

               var2 = var3;
            }

            var3 = var2;
            return var3;
         }
      } finally {
         Pool.tryRelease((Object)var1);
      }
   }

   public static boolean contains(Object[] var0, Predicate var1) {
      return indexOf(var0, var1) > -1;
   }

   public static boolean contains(List var0, Predicate var1) {
      return indexOf(var0, var1) > -1;
   }

   public static boolean contains(Collection var0, Predicate var1) {
      if (var0 instanceof List) {
         return contains((List)var0, var1);
      } else {
         try {
            boolean var2 = false;
            Iterator var3 = var0.iterator();

            while(true) {
               if (var3.hasNext()) {
                  Object var4 = var3.next();
                  if (!var1.test(var4)) {
                     continue;
                  }

                  var2 = true;
               }

               boolean var8 = var2;
               return var8;
            }
         } finally {
            Pool.tryRelease((Object)var1);
         }
      }
   }

   public static boolean contains(Iterable var0, Predicate var1) {
      if (var0 instanceof List) {
         return indexOf((List)var0, var1) > -1;
      } else {
         try {
            boolean var2 = false;
            Iterator var3 = var0.iterator();

            while(true) {
               if (var3.hasNext()) {
                  Object var4 = var3.next();
                  if (!var1.test(var4)) {
                     continue;
                  }

                  var2 = true;
               }

               boolean var8 = var2;
               return var8;
            }
         } finally {
            Pool.tryRelease((Object)var1);
         }
      }
   }

   public static Object find(List var0, Predicate var1) {
      int var2 = indexOf(var0, var1);
      return var2 > -1 ? var0.get(var2) : null;
   }

   public static Object find(Iterable var0, Predicate var1) {
      if (var0 instanceof List) {
         return find((List)var0, var1);
      } else {
         Iterator var2;
         try {
            var2 = var0.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               if (var1.test(var3)) {
                  Object var4 = var3;
                  return var4;
               }
            }

            var2 = null;
         } finally {
            Pool.tryRelease((Object)var1);
         }

         return var2;
      }
   }

   public static List listConvert(List var0, Function var1) {
      return (List)(var0.isEmpty() ? PZArrayList.emptyList() : new PZConvertList(var0, var1));
   }

   public static Iterable itConvert(Iterable var0, Function var1) {
      return new PZConvertIterable(var0, var1);
   }

   public static List listConvert(List var0, List var1, Function var2) {
      var1.clear();

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         var1.add(var2.apply(var0.get(var3)));
      }

      return var1;
   }

   public static List listConvert(List var0, List var1, Object var2, PZArrayUtil.IListConverter1Param var3) {
      var1.clear();

      for(int var4 = 0; var4 < var0.size(); ++var4) {
         var1.add(var3.convert(var0.get(var4), var2));
      }

      return var1;
   }

   private static List asList(Object[] var0) {
      return Arrays.asList(var0);
   }

   private static List asList(float[] var0) {
      return new PrimitiveFloatList(var0);
   }

   private static Iterable asSafeIterable(Object[] var0) {
      return (Iterable)(var0 != null ? asList(var0) : PZEmptyIterable.getInstance());
   }

   private static Iterable asSafeIterable(float[] var0) {
      return (Iterable)(var0 != null ? asList(var0) : PZEmptyIterable.getInstance());
   }

   public static String arrayToString(float[] var0) {
      return arrayToString(asSafeIterable(var0));
   }

   public static String arrayToString(float[] var0, String var1, String var2, String var3) {
      return arrayToString(asSafeIterable(var0), var1, var2, var3);
   }

   public static String arrayToString(Object[] var0) {
      return arrayToString(asSafeIterable(var0));
   }

   public static String arrayToString(Object[] var0, String var1, String var2, String var3) {
      return arrayToString(asSafeIterable(var0), var1, var2, var3);
   }

   public static String arrayToString(Iterable var0, Function var1) {
      return arrayToString(var0, var1, "{", "}", System.lineSeparator());
   }

   public static String arrayToString(Iterable var0) {
      return arrayToString(var0, String::valueOf, "{", "}", System.lineSeparator());
   }

   public static String arrayToString(Iterable var0, String var1, String var2, String var3) {
      return arrayToString(var0, String::valueOf, var1, var2, var3);
   }

   public static String arrayToString(Iterable var0, Function var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(var2);
      if (var0 != null) {
         boolean var6 = true;

         for(Iterator var7 = var0.iterator(); var7.hasNext(); var6 = false) {
            Object var8 = var7.next();
            if (!var6) {
               var5.append(var4);
            }

            String var9 = (String)var1.apply(var8);
            var5.append(var9);
         }
      }

      var5.append(var3);
      Pool.tryRelease((Object)var1);
      return var5.toString();
   }

   public static Object[] newInstance(Class var0, int var1) {
      return (Object[])Array.newInstance(var0, var1);
   }

   public static Object[] newInstance(Class var0, int var1, Supplier var2) {
      Object[] var3 = newInstance(var0, var1);
      int var4 = 0;

      for(int var5 = var3.length; var4 < var5; ++var4) {
         var3[var4] = var2.get();
      }

      return var3;
   }

   public static Object[] newInstance(Class var0, Object[] var1, int var2) {
      return newInstance(var0, var1, var2, false, () -> {
         return null;
      });
   }

   public static Object[] newInstance(Class var0, Object[] var1, int var2, boolean var3) {
      return newInstance(var0, var1, var2, var3, () -> {
         return null;
      });
   }

   public static Object[] newInstance(Class var0, Object[] var1, int var2, Supplier var3) {
      return newInstance(var0, var1, var2, false, var3);
   }

   public static Object[] newInstance(Class var0, Object[] var1, int var2, boolean var3, Supplier var4) {
      if (var1 == null) {
         return newInstance(var0, var2, var4);
      } else {
         int var5 = var1.length;
         if (var5 == var2) {
            return var1;
         } else if (var3 && var5 > var2) {
            return var1;
         } else {
            Object[] var6 = newInstance(var0, var2);
            arrayCopy((Object[])var6, (Object[])var1, 0, PZMath.min(var2, var5));
            int var7;
            if (var2 > var5) {
               for(var7 = var5; var7 < var2; ++var7) {
                  var6[var7] = var4.get();
               }
            }

            if (var2 < var5) {
               for(var7 = var2; var7 < var5; ++var7) {
                  var1[var7] = Pool.tryRelease(var1[var7]);
               }
            }

            return var6;
         }
      }
   }

   public static float[] add(float[] var0, float var1) {
      float[] var2 = new float[var0.length + 1];
      arrayCopy((float[])var2, (float[])var0, 0, var0.length);
      var2[var0.length] = var1;
      return var2;
   }

   public static Object[] add(Object[] var0, Object var1) {
      Object[] var2 = newInstance(var0.getClass().getComponentType(), var0.length + 1);
      arrayCopy((Object[])var2, (Object[])var0, 0, var0.length);
      var2[var0.length] = var1;
      return var2;
   }

   public static Object[] concat(Object[] var0, Object[] var1) {
      boolean var2 = var0 == null || var0.length == 0;
      boolean var3 = var1 == null || var1.length == 0;
      if (var2 && var3) {
         return null;
      } else if (var2) {
         return clone(var1);
      } else if (var3) {
         return var0;
      } else {
         Object[] var4 = newInstance(var0.getClass().getComponentType(), var0.length + var1.length);
         arrayCopy((Object[])var4, (Object[])var0, 0, var0.length);
         arrayCopy(var4, var1, var0.length, var4.length);
         return var4;
      }
   }

   public static Object[] arrayCopy(Object[] var0, Object[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         var0[var4] = var1[var4];
      }

      return var0;
   }

   public static float[] arrayCopy(float[] var0, float[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         var0[var4] = var1[var4];
      }

      return var0;
   }

   public static int[] arrayCopy(int[] var0, int[] var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         var0[var4] = var1[var4];
      }

      return var0;
   }

   public static List arrayCopy(List var0, List var1) {
      var0.clear();
      var0.addAll(var1);
      return var0;
   }

   public static Object[] arrayCopy(Object[] var0, List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         var0[var2] = var1.get(var2);
      }

      return var0;
   }

   public static Object[] arrayCopy(Object[] var0, Object[] var1) {
      System.arraycopy(var1, 0, var0, 0, var1.length);
      return var0;
   }

   public static List arrayConvert(List var0, List var1, Function var2) {
      var0.clear();
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         Object var5 = var1.get(var3);
         var0.add(var2.apply(var5));
      }

      return var0;
   }

   public static float[] clone(float[] var0) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         float[] var1 = new float[var0.length];
         arrayCopy((float[])var1, (float[])var0, 0, var0.length);
         return var1;
      }
   }

   public static Object[] clone(Object[] var0) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         Object[] var1 = newInstance(var0.getClass().getComponentType(), var0.length);
         arrayCopy((Object[])var1, (Object[])var0, 0, var0.length);
         return var1;
      }
   }

   public static boolean isNullOrEmpty(Object[] var0) {
      return var0 == null || var0.length == 0;
   }

   public static boolean isNullOrEmpty(int[] var0) {
      return var0 == null || var0.length == 0;
   }

   public static boolean isNullOrEmpty(float[] var0) {
      return var0 == null || var0.length == 0;
   }

   public static boolean isNullOrEmpty(List var0) {
      return var0 == null || var0.isEmpty();
   }

   public static boolean isNullOrEmpty(Iterable var0) {
      if (var0 instanceof List) {
         return isNullOrEmpty((List)var0);
      } else {
         boolean var1 = true;
         Iterator var2 = var0.iterator();
         if (var2.hasNext()) {
            Object var3 = var2.next();
            var1 = false;
         }

         return var1;
      }
   }

   public static Object getOrDefault(List var0, int var1) {
      return getOrDefault((List)var0, var1, (Object)null);
   }

   public static Object getOrDefault(List var0, int var1, Object var2) {
      return var1 >= 0 && var1 < var0.size() ? var0.get(var1) : var2;
   }

   public static Object getOrDefault(Object[] var0, int var1, Object var2) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : var2;
   }

   public static float getOrDefault(float[] var0, int var1, float var2) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : var2;
   }

   public static int[] arraySet(int[] var0, int var1) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         int var2 = 0;

         for(int var3 = var0.length; var2 < var3; ++var2) {
            var0[var2] = var1;
         }

         return var0;
      }
   }

   public static float[] arraySet(float[] var0, float var1) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         int var2 = 0;

         for(int var3 = var0.length; var2 < var3; ++var2) {
            var0[var2] = var1;
         }

         return var0;
      }
   }

   public static Object[] arraySet(Object[] var0, Object var1) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         int var2 = 0;

         for(int var3 = var0.length; var2 < var3; ++var2) {
            var0[var2] = var1;
         }

         return var0;
      }
   }

   public static Object[] arrayPopulate(Object[] var0, Supplier var1) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         int var2 = 0;

         for(int var3 = var0.length; var2 < var3; ++var2) {
            var0[var2] = var1.get();
         }

         return var0;
      }
   }

   public static void insertAt(int[] var0, int var1, int var2) {
      for(int var3 = var0.length - 1; var3 > var1; --var3) {
         var0[var3] = var0[var3 - 1];
      }

      var0[var1] = var2;
   }

   public static void insertAt(float[] var0, int var1, float var2) {
      for(int var3 = var0.length - 1; var3 > var1; --var3) {
         var0[var3] = var0[var3 - 1];
      }

      var0[var1] = var2;
   }

   public static Object[] toArray(List var0) {
      if (var0 != null && !var0.isEmpty()) {
         Object[] var1 = newInstance(var0.get(0).getClass(), var0.size());
         arrayCopy(var1, var0);
         return var1;
      } else {
         return null;
      }
   }

   public static int indexOf(Object[] var0, int var1, Object var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         if (var0[var3] == var2) {
            return var3;
         }
      }

      return -1;
   }

   public static int indexOf(int[] var0, int var1, int var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         if (var0[var3] == var2) {
            return var3;
         }
      }

      return -1;
   }

   public static boolean contains(int[] var0, int var1, int var2) {
      return indexOf(var0, var1, var2) != -1;
   }

   public static void forEach(List var0, Consumer var1) {
      try {
         if (var0 == null) {
            return;
         }

         int var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            Object var4 = var0.get(var2);
            var1.accept(var4);
         }
      } finally {
         Pool.tryRelease((Object)var1);
      }

   }

   public static void forEach(Iterable var0, Consumer var1) {
      if (var0 == null) {
         Pool.tryRelease((Object)var1);
      } else if (var0 instanceof List) {
         forEach((List)var0, var1);
      } else {
         try {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               Object var3 = var2.next();
               var1.accept(var3);
            }
         } finally {
            Pool.tryRelease((Object)var1);
         }

      }
   }

   public static void forEach(Object[] var0, Consumer var1) {
      if (!isNullOrEmpty(var0)) {
         int var2 = 0;

         for(int var3 = var0.length; var2 < var3; ++var2) {
            var1.accept(var0[var2]);
         }

      }
   }

   public static Object getOrCreate(HashMap var0, Object var1, Supplier var2) {
      Object var3 = var0.get(var1);
      if (var3 == null) {
         var3 = var2.get();
         var0.put(var1, var3);
      }

      return var3;
   }

   public static void sort(Stack var0, Comparator var1) {
      try {
         var0.sort(var1);
      } finally {
         Pool.tryRelease((Object)var1);
      }

   }

   public static boolean sequenceEqual(Object[] var0, List var1) {
      return sequenceEqual(var0, var1, PZArrayUtil.Comparators::objectsEqual);
   }

   public static boolean sequenceEqual(Object[] var0, List var1, Comparator var2) {
      return var0.length == var1.size() && sequenceEqual(asList(var0), var1, var2);
   }

   public static boolean sequenceEqual(List var0, List var1) {
      return sequenceEqual(var0, var1, PZArrayUtil.Comparators::objectsEqual);
   }

   public static boolean sequenceEqual(List var0, List var1, Comparator var2) {
      if (var0.size() != var1.size()) {
         return false;
      } else {
         boolean var3 = true;
         int var4 = 0;

         for(int var5 = var0.size(); var4 < var5; ++var4) {
            Object var6 = var0.get(var4);
            Object var7 = var1.get(var4);
            if (var2.compare(var6, var7) != 0) {
               var3 = false;
               break;
            }
         }

         return var3;
      }
   }

   public static int[] arrayAdd(int[] var0, int[] var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         var0[var2] += var1[var2];
      }

      return var0;
   }

   public interface IListConverter1Param {
      Object convert(Object var1, Object var2);
   }

   public static class Comparators {
      public static int referencesEqual(Object var0, Object var1) {
         return var0 == var1 ? 0 : 1;
      }

      public static int objectsEqual(Object var0, Object var1) {
         return var0 != null && var0.equals(var1) ? 0 : 1;
      }

      public static int equalsIgnoreCase(String var0, String var1) {
         return StringUtils.equals(var0, var1) ? 0 : 1;
      }
   }
}
