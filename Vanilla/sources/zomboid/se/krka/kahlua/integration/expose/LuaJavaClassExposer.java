package se.krka.kahlua.integration.expose;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.annotations.Desc;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.integration.expose.caller.ConstructorCaller;
import se.krka.kahlua.integration.expose.caller.MethodCaller;
import se.krka.kahlua.integration.processor.ClassParameterInformation;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public class LuaJavaClassExposer {
   private static final Object DEBUGINFO_KEY = new Object();
   private final KahluaConverterManager manager;
   private final Platform platform;
   private final KahluaTable environment;
   private final KahluaTable classMetatables;
   private final Set visitedTypes;
   private final KahluaTable autoExposeBase;
   private final Map shouldExposeCache;
   public final HashMap TypeMap;

   public LuaJavaClassExposer(KahluaConverterManager var1, Platform var2, KahluaTable var3) {
      this(var1, var2, var3, (KahluaTable)null);
   }

   public LuaJavaClassExposer(KahluaConverterManager var1, Platform var2, KahluaTable var3, KahluaTable var4) {
      this.visitedTypes = new HashSet();
      this.shouldExposeCache = new HashMap();
      this.TypeMap = new HashMap();
      this.manager = var1;
      this.platform = var2;
      this.environment = var3;
      this.autoExposeBase = var4;
      this.classMetatables = KahluaUtil.getClassMetatables(var2, this.environment);
      if (this.classMetatables.getMetatable() == null) {
         KahluaTable var5 = var2.newTable();
         var5.rawset("__index", new JavaFunction() {
            public int call(LuaCallFrame var1, int var2) {
               Object var3 = var1.get(0);
               Object var4 = var1.get(1);
               if (var3 != LuaJavaClassExposer.this.classMetatables) {
                  throw new IllegalArgumentException("Expected classmetatables as the first argument to __index");
               } else if (var4 != null && var4 instanceof Class) {
                  Class var5 = (Class)var4;
                  if (!LuaJavaClassExposer.this.isExposed(var5) && LuaJavaClassExposer.this.shouldExpose(var5)) {
                     LuaJavaClassExposer.this.exposeLikeJavaRecursively(var5, LuaJavaClassExposer.this.environment);
                     return var1.push(LuaJavaClassExposer.this.classMetatables.rawget(var5));
                  } else {
                     return var1.pushNil();
                  }
               } else {
                  return var1.pushNil();
               }
            }
         });
         this.classMetatables.setMetatable(var5);
      }

   }

   public Map getClassDebugInformation() {
      Object var1 = this.environment.rawget(DEBUGINFO_KEY);
      if (var1 == null || !(var1 instanceof Map)) {
         var1 = new HashMap();
         this.environment.rawset(DEBUGINFO_KEY, var1);
      }

      return (Map)var1;
   }

   private KahluaTable getMetaTable(Class var1) {
      return (KahluaTable)this.classMetatables.rawget(var1);
   }

   private KahluaTable getIndexTable(KahluaTable var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = var1.rawget("__index");
         if (var2 == null) {
            return null;
         } else {
            return var2 instanceof KahluaTable ? (KahluaTable)var2 : null;
         }
      }
   }

   public void exposeGlobalObjectFunction(KahluaTable var1, Object var2, Method var3) {
      this.exposeGlobalObjectFunction(var1, var2, var3, var3.getName());
   }

   public void exposeGlobalObjectFunction(KahluaTable var1, Object var2, Method var3, String var4) {
      Class var5 = var2.getClass();
      this.readDebugData(var5);
      LuaJavaInvoker var6 = this.getMethodInvoker(var5, var3, var4, var2, false);
      this.addInvoker(var1, var4, var6);
   }

   public void exposeGlobalClassFunction(KahluaTable var1, Class var2, Constructor var3, String var4) {
      this.readDebugData(var2);
      LuaJavaInvoker var5 = this.getConstructorInvoker(var2, var3, var4);
      this.addInvoker(var1, var4, var5);
   }

   private LuaJavaInvoker getMethodInvoker(Class var1, Method var2, String var3, Object var4, boolean var5) {
      return new LuaJavaInvoker(this, this.manager, var1, var3, new MethodCaller(var2, var4, var5));
   }

   private LuaJavaInvoker getConstructorInvoker(Class var1, Constructor var2, String var3) {
      return new LuaJavaInvoker(this, this.manager, var1, var3, new ConstructorCaller(var2));
   }

   private LuaJavaInvoker getMethodInvoker(Class var1, Method var2, String var3) {
      return this.getMethodInvoker(var1, var2, var3, (Object)null, true);
   }

   private LuaJavaInvoker getGlobalInvoker(Class var1, Method var2, String var3) {
      return this.getMethodInvoker(var1, var2, var3, (Object)null, false);
   }

   public void exposeGlobalClassFunction(KahluaTable var1, Class var2, Method var3, String var4) {
      this.readDebugData(var2);
      if (Modifier.isStatic(var3.getModifiers())) {
         this.addInvoker(var1, var4, this.getGlobalInvoker(var2, var3, var4));
      }

   }

   public void exposeMethod(Class var1, Method var2, KahluaTable var3) {
      this.exposeMethod(var1, var2, var2.getName(), var3);
   }

   public void exposeMethod(Class var1, Method var2, String var3, KahluaTable var4) {
      this.readDebugData(var1);
      if (!this.isExposed(var1)) {
         this.setupMetaTables(var1, var4);
      }

      KahluaTable var5 = this.getMetaTable(var1);
      KahluaTable var6 = this.getIndexTable(var5);
      LuaJavaInvoker var7 = this.getMethodInvoker(var1, var2, var3);
      this.addInvoker(var6, var3, var7);
   }

   private void addInvoker(KahluaTable var1, String var2, LuaJavaInvoker var3) {
      if (var2.equals("setDir")) {
         boolean var4 = false;
      }

      Object var6 = var1.rawget(var2);
      if (var6 != null) {
         if (var6 instanceof LuaJavaInvoker) {
            if (var6.equals(var3)) {
               return;
            }

            MultiLuaJavaInvoker var5 = new MultiLuaJavaInvoker();
            var5.addInvoker((LuaJavaInvoker)var6);
            var5.addInvoker(var3);
            var1.rawset(var2, var5);
         } else if (var6 instanceof MultiLuaJavaInvoker) {
            ((MultiLuaJavaInvoker)var6).addInvoker(var3);
         }
      } else {
         var1.rawset(var2, var3);
      }

   }

   public boolean shouldExpose(Class var1) {
      if (var1 == null) {
         return false;
      } else {
         Boolean var2 = (Boolean)this.shouldExposeCache.get(var1);
         if (var2 != null) {
            return var2;
         } else if (this.autoExposeBase != null) {
            this.exposeLikeJavaRecursively(var1, this.autoExposeBase);
            return true;
         } else if (this.isExposed(var1)) {
            this.shouldExposeCache.put(var1, Boolean.TRUE);
            return true;
         } else if (this.shouldExpose(var1.getSuperclass())) {
            this.shouldExposeCache.put(var1, Boolean.TRUE);
            return true;
         } else {
            Class[] var3 = var1.getInterfaces();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Class var6 = var3[var5];
               if (this.shouldExpose(var6)) {
                  this.shouldExposeCache.put(var1, Boolean.TRUE);
                  return true;
               }
            }

            this.shouldExposeCache.put(var1, Boolean.FALSE);
            return false;
         }
      }
   }

   private void setupMetaTables(Class var1, KahluaTable var2) {
      Class var3 = var1.getSuperclass();
      this.exposeLikeJavaRecursively(var3, var2);
      KahluaTable var4 = this.getMetaTable(var3);
      KahluaTable var5 = this.platform.newTable();
      KahluaTable var6 = this.platform.newTable();
      var5.rawset("__index", var6);
      if (var4 != null) {
         var5.rawset("__newindex", var4.rawget("__newindex"));
      }

      var6.setMetatable(var4);
      this.classMetatables.rawset(var1, var5);
   }

   private void addJavaEquals(KahluaTable var1) {
      var1.rawset("__eq", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            boolean var3 = var1.get(0).equals(var1.get(1));
            var1.push(var3);
            return 1;
         }
      });
   }

   public void exposeGlobalFunctions(Object var1) {
      Class var2 = var1.getClass();
      this.readDebugData(var2);
      Method[] var3 = var2.getMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         LuaMethod var7 = (LuaMethod)AnnotationUtil.getAnnotation(var6, LuaMethod.class);
         if (var7 != null) {
            String var8;
            if (var7.name().equals("")) {
               var8 = var6.getName();
            } else {
               var8 = var7.name();
            }

            if (var7.global()) {
               this.exposeGlobalObjectFunction(this.environment, var1, var6, var8);
            }
         }
      }

   }

   public void exposeLikeJava(Class var1) {
      this.exposeLikeJava(var1, this.autoExposeBase);
   }

   public void exposeLikeJava(Class var1, KahluaTable var2) {
      if (var1 != null && !this.isExposed(var1) && this.shouldExpose(var1)) {
         this.setupMetaTables(var1, var2);
         this.exposeMethods(var1, var2);
         if (!var1.isSynthetic() && !var1.isAnonymousClass() && !var1.isPrimitive() && !Proxy.isProxyClass(var1) && !var1.getSimpleName().startsWith("$")) {
            this.exposeStatics(var1, var2);
         }

      }
   }

   private void exposeStatics(Class var1, KahluaTable var2) {
      String[] var3 = var1.getName().split("\\.");
      KahluaTable var4 = this.createTableStructure(var2, var3);
      var4.rawset("class", var1);
      if (var2.rawget(var1.getSimpleName()) == null) {
         var2.rawset(var1.getSimpleName(), var4);
      }

      Method[] var5 = var1.getMethods();
      int var6 = var5.length;

      int var7;
      String var9;
      for(var7 = 0; var7 < var6; ++var7) {
         Method var8 = var5[var7];
         var9 = var8.getName();
         if (Modifier.isPublic(var8.getModifiers()) && Modifier.isStatic(var8.getModifiers())) {
            this.exposeGlobalClassFunction(var4, var1, var8, var9);
         }
      }

      Field[] var12 = var1.getFields();
      var6 = var12.length;

      for(var7 = 0; var7 < var6; ++var7) {
         Field var14 = var12[var7];
         var9 = var14.getName();
         if (Modifier.isPublic(var14.getModifiers()) && Modifier.isStatic(var14.getModifiers())) {
            try {
               var4.rawset(var9, var14.get(var1));
            } catch (IllegalAccessException var11) {
            }
         }
      }

      Constructor[] var13 = var1.getConstructors();
      var6 = var13.length;

      for(var7 = 0; var7 < var6; ++var7) {
         Constructor var15 = var13[var7];
         int var16 = var15.getModifiers();
         if (!Modifier.isInterface(var16) && !Modifier.isAbstract(var16) && Modifier.isPublic(var16)) {
            this.addInvoker(var4, "new", this.getConstructorInvoker(var1, var15, "new"));
         }
      }

   }

   private void exposeMethods(Class var1, KahluaTable var2) {
      Method[] var3 = var1.getMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         String var7 = var6.getName();
         if (Modifier.isPublic(var6.getModifiers()) && !Modifier.isStatic(var6.getModifiers())) {
            this.exposeMethod(var1, var6, var7, var2);
         }
      }

   }

   private KahluaTable createTableStructure(KahluaTable var1, String[] var2) {
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var1 = KahluaUtil.getOrCreateTable(this.platform, var1, var6);
      }

      return var1;
   }

   public boolean isExposed(Class var1) {
      return var1 != null && this.getMetaTable(var1) != null;
   }

   ClassDebugInformation getDebugdata(Class var1) {
      this.readDebugDataD(var1);
      return (ClassDebugInformation)this.getClassDebugInformation().get(var1);
   }

   ClassDebugInformation getDebugdataA(Class var1) {
      return (ClassDebugInformation)this.getClassDebugInformation().get(var1);
   }

   private void readDebugDataD(Class var1) {
      if (this.getDebugdataA(var1) == null) {
         ClassParameterInformation var2 = null;

         try {
            var2 = ClassParameterInformation.getFromStream(var1);
         } catch (Exception var5) {
         }

         if (var2 == null) {
            var2 = new ClassParameterInformation(var1);
         }

         ClassDebugInformation var3 = new ClassDebugInformation(var1, var2);
         Map var4 = this.getClassDebugInformation();
         var4.put(var1, var3);
      }

   }

   private void readDebugData(Class var1) {
   }

   @LuaMethod(
      global = true,
      name = "definition"
   )
   @Desc("returns a string that describes the object")
   public String getDefinition(Object var1) {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof LuaJavaInvoker) {
         MethodDebugInformation var5 = ((LuaJavaInvoker)var1).getMethodDebugData();
         return var5.toString();
      } else if (!(var1 instanceof MultiLuaJavaInvoker)) {
         return KahluaUtil.tostring(var1, KahluaUtil.getWorkerThread(this.platform, this.environment));
      } else {
         StringBuilder var2 = new StringBuilder();
         Iterator var3 = ((MultiLuaJavaInvoker)var1).getInvokers().iterator();

         while(var3.hasNext()) {
            LuaJavaInvoker var4 = (LuaJavaInvoker)var3.next();
            var2.append(var4.getMethodDebugData().toString());
         }

         return var2.toString();
      }
   }

   public void exposeLikeJavaRecursively(Type var1) {
      this.exposeLikeJavaRecursively(var1, this.autoExposeBase);
   }

   public void exposeLikeJavaRecursively(Type var1, KahluaTable var2) {
      this.exposeLikeJava(var2, this.visitedTypes, var1);
   }

   private void exposeLikeJava(KahluaTable var1, Set var2, Type var3) {
      if (var3 != null) {
         if (!var2.contains(var3)) {
            var2.add(var3);
            if (var3 instanceof Class) {
               if (!this.shouldExpose((Class)var3)) {
                  return;
               }

               this.exposeLikeJavaByClass(var1, var2, (Class)var3);
            } else if (var3 instanceof WildcardType) {
               WildcardType var4 = (WildcardType)var3;
               this.exposeList(var1, var2, var4.getLowerBounds());
               this.exposeList(var1, var2, var4.getUpperBounds());
            } else if (var3 instanceof ParameterizedType) {
               ParameterizedType var5 = (ParameterizedType)var3;
               this.exposeLikeJava(var1, var2, var5.getRawType());
               this.exposeLikeJava(var1, var2, var5.getOwnerType());
               this.exposeList(var1, var2, var5.getActualTypeArguments());
            } else if (var3 instanceof TypeVariable) {
               TypeVariable var6 = (TypeVariable)var3;
               this.exposeList(var1, var2, var6.getBounds());
            } else if (var3 instanceof GenericArrayType) {
               GenericArrayType var7 = (GenericArrayType)var3;
               this.exposeLikeJava(var1, var2, var7.getGenericComponentType());
            }

         }
      }
   }

   private void exposeList(KahluaTable var1, Set var2, Type[] var3) {
      Type[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Type var7 = var4[var6];
         this.exposeLikeJava(var1, var2, var7);
      }

   }

   private void exposeLikeJavaByClass(KahluaTable var1, Set var2, Class var3) {
      String var4 = var3.toString();
      var4 = var4.substring(var4.lastIndexOf(".") + 1);
      this.TypeMap.put(var4, var3);
      this.exposeList(var1, var2, var3.getInterfaces());
      this.exposeLikeJava(var1, var2, var3.getGenericSuperclass());
      if (var3.isArray()) {
         this.exposeLikeJavaByClass(var1, var2, var3.getComponentType());
      } else {
         this.exposeLikeJava(var3, var1);
      }

      Method[] var5 = var3.getDeclaredMethods();
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         Method var8 = var5[var7];
         this.exposeList(var1, var2, var8.getGenericParameterTypes());
         this.exposeList(var1, var2, var8.getGenericExceptionTypes());
         this.exposeLikeJava(var1, var2, var8.getGenericReturnType());
      }

      Field[] var9 = var3.getDeclaredFields();
      var6 = var9.length;

      for(var7 = 0; var7 < var6; ++var7) {
         Field var11 = var9[var7];
         this.exposeLikeJava(var1, var2, var11.getGenericType());
      }

      Constructor[] var10 = var3.getConstructors();
      var6 = var10.length;

      for(var7 = 0; var7 < var6; ++var7) {
         Constructor var12 = var10[var7];
         this.exposeList(var1, var2, var12.getParameterTypes());
         this.exposeList(var1, var2, var12.getExceptionTypes());
      }

   }

   public void destroy() {
      this.shouldExposeCache.clear();
      this.TypeMap.clear();
      this.visitedTypes.clear();
   }
}
