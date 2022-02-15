package zombie.util;

import java.util.function.BiFunction;

public class StringUtils {
   public static final String s_emptyString = "";
   public static final char UTF8_BOM = '\ufeff';

   public static boolean isNullOrEmpty(String var0) {
      return var0 == null || var0.length() == 0;
   }

   public static boolean isNullOrWhitespace(String var0) {
      return isNullOrEmpty(var0) || isWhitespace(var0);
   }

   private static boolean isWhitespace(String var0) {
      int var1 = var0.length();
      if (var1 <= 0) {
         return false;
      } else {
         int var2 = 0;
         int var3 = var1 / 2;

         for(int var4 = var1 - 1; var2 <= var3; --var4) {
            if (!Character.isWhitespace(var0.charAt(var2)) || !Character.isWhitespace(var0.charAt(var4))) {
               return false;
            }

            ++var2;
         }

         return true;
      }
   }

   public static String discardNullOrWhitespace(String var0) {
      return isNullOrWhitespace(var0) ? null : var0;
   }

   public static String trimPrefix(String var0, String var1) {
      return var0.startsWith(var1) ? var0.substring(var1.length()) : var0;
   }

   public static String trimSuffix(String var0, String var1) {
      return var0.endsWith(var1) ? var0.substring(0, var0.length() - var1.length()) : var0;
   }

   public static boolean equals(String var0, String var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var0.equals(var1);
      }
   }

   public static boolean startsWithIgnoreCase(String var0, String var1) {
      return var0.regionMatches(true, 0, var1, 0, var1.length());
   }

   public static boolean endsWithIgnoreCase(String var0, String var1) {
      int var2 = var1.length();
      return var0.regionMatches(true, var0.length() - var2, var1, 0, var2);
   }

   public static boolean containsIgnoreCase(String var0, String var1) {
      for(int var2 = var0.length() - var1.length(); var2 >= 0; --var2) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return true;
         }
      }

      return false;
   }

   public static boolean equalsIgnoreCase(String var0, String var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var0.equalsIgnoreCase(var1);
      }
   }

   public static boolean tryParseBoolean(String var0) {
      if (isNullOrWhitespace(var0)) {
         return false;
      } else {
         String var1 = var0.trim();
         return var1.equalsIgnoreCase("true") || var1.equals("1") || var1.equals("1.0");
      }
   }

   public static boolean isBoolean(String var0) {
      String var1 = var0.trim();
      if (!var1.equalsIgnoreCase("true") && !var1.equals("1") && !var1.equals("1.0")) {
         return var1.equalsIgnoreCase("false") || var1.equals("0") || var1.equals("0.0");
      } else {
         return true;
      }
   }

   public static boolean contains(String[] var0, String var1, BiFunction var2) {
      return indexOf(var0, var1, var2) > -1;
   }

   public static int indexOf(String[] var0, String var1, BiFunction var2) {
      int var3 = -1;

      for(int var4 = 0; var4 < var0.length; ++var4) {
         if ((Boolean)var2.apply(var0[var4], var1)) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   public static String indent(String var0) {
      return indent(var0, "", "\t");
   }

   private static String indent(String var0, String var1, String var2) {
      String var3 = System.lineSeparator();
      return indent(var0, var3, var1, var2);
   }

   private static String indent(String var0, String var1, String var2, String var3) {
      if (isNullOrEmpty(var0)) {
         return var0;
      } else {
         int var4 = var0.length();
         StringBuilder var5 = new StringBuilder(var4);
         StringBuilder var6 = new StringBuilder(var4);
         int var7 = 0;

         for(int var8 = 0; var8 < var4; ++var8) {
            char var9 = var0.charAt(var8);
            switch(var9) {
            case '\n':
               var5.append(var6);
               var5.append(var1);
               var6.setLength(0);
               ++var7;
            case '\r':
               break;
            default:
               if (var6.length() == 0) {
                  if (var7 == 0) {
                     var6.append(var2);
                  } else {
                     var6.append(var3);
                  }
               }

               var6.append(var9);
            }
         }

         var5.append(var6);
         var6.setLength(0);
         return var5.toString();
      }
   }

   public static String leftJustify(String var0, int var1) {
      if (var0 == null) {
         return leftJustify("", var1);
      } else {
         int var2 = var0.length();
         if (var2 >= var1) {
            return var0;
         } else {
            int var3 = var1 - var2;
            char[] var4 = new char[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
               var4[var5] = ' ';
            }

            String var6 = new String(var4);
            return var0 + var6;
         }
      }
   }

   public static String moduleDotType(String var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         return var1.contains(".") ? var1 : var0 + "." + var1;
      }
   }

   public static String stripBOM(String var0) {
      return var0 != null && var0.length() > 0 && var0.charAt(0) == '\ufeff' ? var0.substring(1) : var0;
   }
}
