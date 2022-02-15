package se.krka.kahlua.integration.expose;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.debug.DebugLog;

public class TypeUtil {
   private static final Pattern pattern = Pattern.compile("([\\.a-z0-9]*)\\.([A-Za-z][A-Za-z0-9_]*)");

   public static String removePackages(String var0) {
      Matcher var1 = pattern.matcher(var0);
      return var1.replaceAll("$2");
   }

   public static String getClassName(Type var0) {
      if (var0 instanceof Class) {
         Class var9 = (Class)var0;
         return var9.isArray() ? getClassName(var9.getComponentType()) + "[]" : var9.getName();
      } else {
         Type[] var2;
         if (var0 instanceof WildcardType) {
            WildcardType var8 = (WildcardType)var0;
            var2 = var8.getUpperBounds();
            Type[] var10 = var8.getLowerBounds();
            return handleBounds("?", var2, var10);
         } else if (var0 instanceof ParameterizedType) {
            ParameterizedType var7 = (ParameterizedType)var0;
            var2 = var7.getActualTypeArguments();
            String var3 = getClassName(var7.getRawType());
            if (var2.length == 0) {
               return var3;
            } else {
               StringBuilder var4 = new StringBuilder(var3);
               var4.append("<");

               for(int var5 = 0; var5 < var2.length; ++var5) {
                  if (var5 > 0) {
                     var4.append(", ");
                  }

                  var4.append(getClassName(var2[var5]));
               }

               var4.append(">");
               return var4.toString();
            }
         } else if (var0 instanceof TypeVariable) {
            TypeVariable var6 = (TypeVariable)var0;
            return var6.getName();
         } else if (var0 instanceof GenericArrayType) {
            GenericArrayType var1 = (GenericArrayType)var0;
            return getClassName(var1.getGenericComponentType()) + "[]";
         } else {
            DebugLog.log("got unknown: " + var0 + ", " + var0.getClass());
            return "unknown";
         }
      }
   }

   static String handleBounds(String var0, Type[] var1, Type[] var2) {
      StringBuilder var3;
      boolean var4;
      Type[] var5;
      int var6;
      int var7;
      Type var8;
      if (var1 != null) {
         if (var1.length == 1 && var1[0] == Object.class) {
            return var0;
         }

         if (var1.length >= 1) {
            var3 = new StringBuilder();
            var4 = true;
            var5 = var1;
            var6 = var1.length;

            for(var7 = 0; var7 < var6; ++var7) {
               var8 = var5[var7];
               if (var4) {
                  var4 = false;
               } else {
                  var3.append(", ");
               }

               var3.append(getClassName(var8));
            }

            return var0 + " extends " + var3.toString();
         }
      }

      if (var2 != null && var2.length > 0) {
         var3 = new StringBuilder();
         var4 = true;
         var5 = var2;
         var6 = var2.length;

         for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            if (var4) {
               var4 = false;
            } else {
               var3.append(", ");
            }

            var3.append(getClassName(var8));
         }

         return var0 + " super " + var3.toString();
      } else {
         return "unknown type";
      }
   }
}
