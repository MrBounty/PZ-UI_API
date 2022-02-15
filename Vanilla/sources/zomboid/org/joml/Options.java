package org.joml;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public final class Options {
   public static final boolean DEBUG = hasOption(System.getProperty("joml.debug", "false"));
   public static final boolean NO_UNSAFE = hasOption(System.getProperty("joml.nounsafe", "false"));
   public static final boolean FORCE_UNSAFE = hasOption(System.getProperty("joml.forceUnsafe", "false"));
   public static final boolean FASTMATH = hasOption(System.getProperty("joml.fastmath", "false"));
   public static final boolean SIN_LOOKUP = hasOption(System.getProperty("joml.sinLookup", "false"));
   public static final int SIN_LOOKUP_BITS = Integer.parseInt(System.getProperty("joml.sinLookup.bits", "14"));
   public static final boolean useNumberFormat = hasOption(System.getProperty("joml.format", "true"));
   public static final boolean USE_MATH_FMA = hasOption(System.getProperty("joml.useMathFma", "false"));
   public static final int numberFormatDecimals = Integer.parseInt(System.getProperty("joml.format.decimals", "3"));
   public static final NumberFormat NUMBER_FORMAT = decimalFormat();

   private Options() {
   }

   private static NumberFormat decimalFormat() {
      Object var0;
      if (useNumberFormat) {
         char[] var1 = new char[numberFormatDecimals];
         Arrays.fill(var1, '0');
         String var10002 = new String(var1);
         var0 = new DecimalFormat(" 0." + var10002 + "E0;-");
      } else {
         var0 = NumberFormat.getNumberInstance(Locale.ENGLISH);
         ((NumberFormat)var0).setGroupingUsed(false);
      }

      return (NumberFormat)var0;
   }

   private static boolean hasOption(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.trim().length() == 0 ? true : Boolean.valueOf(var0);
      }
   }
}
