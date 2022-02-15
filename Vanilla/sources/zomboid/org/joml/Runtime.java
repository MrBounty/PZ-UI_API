package org.joml;

import java.text.NumberFormat;

public final class Runtime {
   public static final boolean HAS_floatToRawIntBits = hasFloatToRawIntBits();
   public static final boolean HAS_doubleToRawLongBits = hasDoubleToRawLongBits();
   public static final boolean HAS_Long_rotateLeft = hasLongRotateLeft();
   public static final boolean HAS_Math_fma;

   private static boolean hasMathFma() {
      try {
         java.lang.Math.class.getDeclaredMethod("fma", Float.TYPE, Float.TYPE, Float.TYPE);
         return true;
      } catch (NoSuchMethodException var1) {
         return false;
      }
   }

   private Runtime() {
   }

   private static boolean hasFloatToRawIntBits() {
      try {
         Float.class.getDeclaredMethod("floatToRawIntBits", Float.TYPE);
         return true;
      } catch (NoSuchMethodException var1) {
         return false;
      }
   }

   private static boolean hasDoubleToRawLongBits() {
      try {
         Double.class.getDeclaredMethod("doubleToRawLongBits", Double.TYPE);
         return true;
      } catch (NoSuchMethodException var1) {
         return false;
      }
   }

   private static boolean hasLongRotateLeft() {
      try {
         Long.class.getDeclaredMethod("rotateLeft", Long.TYPE, Integer.TYPE);
         return true;
      } catch (NoSuchMethodException var1) {
         return false;
      }
   }

   public static int floatToIntBits(float var0) {
      return HAS_floatToRawIntBits ? floatToIntBits1_3(var0) : floatToIntBits1_2(var0);
   }

   private static int floatToIntBits1_3(float var0) {
      return Float.floatToRawIntBits(var0);
   }

   private static int floatToIntBits1_2(float var0) {
      return Float.floatToIntBits(var0);
   }

   public static long doubleToLongBits(double var0) {
      return HAS_doubleToRawLongBits ? doubleToLongBits1_3(var0) : doubleToLongBits1_2(var0);
   }

   private static long doubleToLongBits1_3(double var0) {
      return Double.doubleToRawLongBits(var0);
   }

   private static long doubleToLongBits1_2(double var0) {
      return Double.doubleToLongBits(var0);
   }

   public static String formatNumbers(String var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = Integer.MIN_VALUE;

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 == 'E') {
            var2 = var3;
         } else {
            if (var4 == ' ' && var2 == var3 - 1) {
               var1.append('+');
               continue;
            }

            if (Character.isDigit(var4) && var2 == var3 - 1) {
               var1.append('+');
            }
         }

         var1.append(var4);
      }

      return var1.toString();
   }

   public static String format(double var0, NumberFormat var2) {
      if (Double.isNaN(var0)) {
         return padLeft(var2, " NaN");
      } else {
         return Double.isInfinite(var0) ? padLeft(var2, var0 > 0.0D ? " +Inf" : " -Inf") : var2.format(var0);
      }
   }

   private static String padLeft(NumberFormat var0, String var1) {
      int var2 = var0.format(0.0D).length();
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var2 - var1.length() + 1; ++var4) {
         var3.append(" ");
      }

      return var3.append(var1).toString();
   }

   public static boolean equals(float var0, float var1, float var2) {
      return Float.floatToIntBits(var0) == Float.floatToIntBits(var1) || Math.abs(var0 - var1) <= var2;
   }

   public static boolean equals(double var0, double var2, double var4) {
      return Double.doubleToLongBits(var0) == Double.doubleToLongBits(var2) || Math.abs(var0 - var2) <= var4;
   }

   static {
      HAS_Math_fma = Options.USE_MATH_FMA && hasMathFma();
   }
}
