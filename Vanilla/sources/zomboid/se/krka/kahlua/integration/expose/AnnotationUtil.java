package se.krka.kahlua.integration.expose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtil {
   public static Annotation getAnnotation(Method var0, Class var1) {
      return getAnnotation(var0.getDeclaringClass(), var0.getName(), var0.getParameterTypes(), var1);
   }

   private static Annotation getAnnotation(Class var0, String var1, Class[] var2, Class var3) {
      if (var0 == null) {
         return null;
      } else {
         try {
            Method var4 = var0.getMethod(var1, var2);
            Annotation var5 = var4.getAnnotation(var3);
            if (var5 != null) {
               return var5;
            } else {
               Class[] var6 = var0.getInterfaces();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  Class var9 = var6[var8];
                  var5 = getAnnotation(var9, var1, var2, var3);
                  if (var5 != null) {
                     return var5;
                  }
               }

               return getAnnotation(var0.getSuperclass(), var1, var2, var3);
            }
         } catch (NoSuchMethodException var10) {
            return null;
         }
      }
   }
}
