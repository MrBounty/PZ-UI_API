package se.krka.kahlua.integration.expose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import se.krka.kahlua.integration.annotations.Desc;
import se.krka.kahlua.integration.annotations.LuaConstructor;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.integration.processor.ClassParameterInformation;
import se.krka.kahlua.integration.processor.DescriptorUtil;
import se.krka.kahlua.integration.processor.MethodParameterInformation;

public class ClassDebugInformation {
   private final Map methods = new HashMap();

   public ClassDebugInformation(Class var1, ClassParameterInformation var2) {
      this.addContent(var1, var2);
      this.addConstructors(var1, var2);
   }

   private void addContent(Class var1, ClassParameterInformation var2) {
      if (var1 != null) {
         this.addContent(var1.getSuperclass(), var2);
         Class[] var3 = var1.getInterfaces();
         int var4 = var3.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            this.addContent(var6, var2);
         }

         Method[] var15 = var1.getDeclaredMethods();
         var4 = var15.length;

         for(var5 = 0; var5 < var4; ++var5) {
            Method var16 = var15[var5];
            LuaMethod var7 = (LuaMethod)AnnotationUtil.getAnnotation(var16, LuaMethod.class);
            String var8 = var16.getName();
            int var9 = var16.getModifiers();
            Type[] var10 = var16.getGenericParameterTypes();
            String var11 = DescriptorUtil.getDescriptor(var16);
            Type var12 = var16.getGenericReturnType();
            Annotation[][] var13 = var16.getParameterAnnotations();
            Desc var14 = (Desc)AnnotationUtil.getAnnotation(var16, Desc.class);
            this.addMethod(var2, var10, var11, var12, var13, getName(var7, var8), !isGlobal(var7, isStatic(var9)), var14);
         }

      }
   }

   private void addConstructors(Class var1, ClassParameterInformation var2) {
      Constructor[] var3 = var1.getConstructors();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Constructor var6 = var3[var5];
         LuaConstructor var7 = (LuaConstructor)var6.getAnnotation(LuaConstructor.class);
         String var8 = "new";
         Type[] var9 = var6.getGenericParameterTypes();
         String var10 = DescriptorUtil.getDescriptor(var6);
         Annotation[][] var12 = var6.getParameterAnnotations();
         Desc var13 = (Desc)var6.getAnnotation(Desc.class);
         this.addMethod(var2, var9, var10, var1, var12, getName(var7, var8), true, var13);
      }

   }

   private void addMethod(ClassParameterInformation var1, Type[] var2, String var3, Type var4, Annotation[][] var5, String var6, boolean var7, Desc var8) {
      MethodParameterInformation var9 = (MethodParameterInformation)var1.methods.get(var3);
      if (!this.methods.containsKey(var3)) {
         if (var9 != null) {
            ArrayList var10 = new ArrayList();

            for(int var11 = 0; var11 < var2.length; ++var11) {
               Type var12 = var2[var11];
               String var13 = var9.getName(var11);
               String var14 = TypeUtil.getClassName(var12);
               String var15 = this.getDescription(var5[var11]);
               var10.add(new MethodParameter(var13, var14, var15));
            }

            String var16 = TypeUtil.getClassName(var4);
            String var17 = getDescription(var8);
            MethodDebugInformation var18 = new MethodDebugInformation(var6, var7, var10, var16, var17);
            this.methods.put(var3, var18);
         }
      }
   }

   private String getDescription(Annotation[] var1) {
      Annotation[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Annotation var5 = var2[var4];
         if (var5 != null && var5 instanceof Desc) {
            return getDescription((Desc)var5);
         }
      }

      return null;
   }

   private static String getDescription(Desc var0) {
      return var0 != null ? var0.value() : null;
   }

   private static boolean isStatic(int var0) {
      return (var0 & 8) != 0;
   }

   private static boolean isGlobal(LuaMethod var0, boolean var1) {
      return var0 != null ? var0.global() : var1;
   }

   private static String getName(LuaMethod var0, String var1) {
      if (var0 != null) {
         String var2 = var0.name();
         if (var2 != null && var2.length() > 0) {
            return var2;
         }
      }

      return var1;
   }

   private static String getName(LuaConstructor var0, String var1) {
      if (var0 != null) {
         String var2 = var0.name();
         if (var2 != null && var2.length() > 0) {
            return var2;
         }
      }

      return var1;
   }

   public Map getMethods() {
      return this.methods;
   }
}
