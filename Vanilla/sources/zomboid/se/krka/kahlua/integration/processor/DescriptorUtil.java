package se.krka.kahlua.integration.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.element.VariableElement;

public class DescriptorUtil {
   public static String getDescriptor(String var0, List var1) {
      String var2 = "";

      VariableElement var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = var2 + ":" + var4.asType().toString()) {
         var4 = (VariableElement)var3.next();
      }

      return var0 + var2;
   }

   public static String getDescriptor(Constructor var0) {
      String var1 = getParameters(var0.getParameterTypes());
      return "new" + var1;
   }

   public static String getDescriptor(Method var0) {
      String var1 = getParameters(var0.getParameterTypes());
      String var10000 = var0.getName();
      return var10000 + var1;
   }

   private static String getParameters(Class[] var0) {
      String var1 = "";
      Class[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         var1 = var1 + ":" + var5.getName();
      }

      return var1;
   }
}
