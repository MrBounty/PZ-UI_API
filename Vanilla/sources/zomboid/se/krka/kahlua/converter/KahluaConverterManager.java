package se.krka.kahlua.converter;

import java.util.HashMap;
import java.util.Map;

public class KahluaConverterManager {
   private static final Map PRIMITIVE_CLASS = new HashMap();
   private static final Map LUA_NULL_MAP;
   private final Map luaToJava = new HashMap();
   private final Map luatoJavaCache = new HashMap();
   private static final JavaToLuaConverter NULL_CONVERTER;
   private final Map javaToLua = new HashMap();
   private final Map javaToLuaCache = new HashMap();

   public void addLuaConverter(LuaToJavaConverter var1) {
      Map var2 = this.getOrCreate(this.luaToJava, var1.getLuaType());
      Class var3 = var1.getJavaType();
      LuaToJavaConverter var4 = (LuaToJavaConverter)var2.get(var3);
      if (var4 != null) {
         if (var4 instanceof MultiLuaToJavaConverter) {
            ((MultiLuaToJavaConverter)var4).add(var1);
         } else {
            MultiLuaToJavaConverter var5 = new MultiLuaToJavaConverter(var1.getLuaType(), var3);
            var5.add(var4);
            var5.add(var1);
            var2.put(var3, var5);
         }
      } else {
         var2.put(var3, var1);
      }

      this.luatoJavaCache.clear();
   }

   public void addJavaConverter(JavaToLuaConverter var1) {
      Class var2 = var1.getJavaType();
      JavaToLuaConverter var3 = (JavaToLuaConverter)this.javaToLua.get(var2);
      if (var3 != null) {
         if (var3 instanceof MultiJavaToLuaConverter) {
            ((MultiJavaToLuaConverter)var3).add(var1);
         } else {
            MultiJavaToLuaConverter var4 = new MultiJavaToLuaConverter(var2);
            var4.add(var3);
            var4.add(var1);
            this.javaToLua.put(var2, var4);
         }
      } else {
         this.javaToLua.put(var2, var1);
      }

      this.javaToLuaCache.clear();
   }

   private Map getOrCreate(Map var1, Class var2) {
      Object var3 = (Map)var1.get(var2);
      if (var3 == null) {
         var3 = new HashMap();
         var1.put(var2, var3);
      }

      return (Map)var3;
   }

   public Object fromLuaToJava(Object var1, Class var2) {
      if (var1 == null) {
         return null;
      } else {
         if (var2.isPrimitive()) {
            var2 = (Class)PRIMITIVE_CLASS.get(var2);
         }

         if (var2.isInstance(var1)) {
            return var1;
         } else {
            Class var3 = var1.getClass();
            Map var4 = this.getLuaCache(var3);

            for(Class var5 = var2; var5 != null; var5 = var5.getSuperclass()) {
               LuaToJavaConverter var6 = (LuaToJavaConverter)var4.get(var5);
               if (var6 != null) {
                  Object var7 = var6.fromLuaToJava(var1, var2);
                  if (var7 != null) {
                     return var7;
                  }
               }
            }

            return this.tryInterfaces(var4, var2, var1);
         }
      }
   }

   private Object tryInterfaces(Map var1, Class var2, Object var3) {
      if (var2 == null) {
         return null;
      } else {
         LuaToJavaConverter var4 = (LuaToJavaConverter)var1.get(var2);
         if (var4 != null) {
            Object var5 = var4.fromLuaToJava(var3, var2);
            if (var5 != null) {
               return var5;
            }
         }

         Class[] var10 = var2.getInterfaces();
         int var6 = var10.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Class var8 = var10[var7];
            Object var9 = this.tryInterfaces(var1, var8, var3);
            if (var9 != null) {
               return var9;
            }
         }

         return this.tryInterfaces(var1, var2.getSuperclass(), var3);
      }
   }

   private Map createLuaCache(Class var1) {
      HashMap var2 = new HashMap();
      this.luatoJavaCache.put(var1, var2);
      var2.putAll(this.getLuaCache(var1.getSuperclass()));
      Class[] var3 = var1.getInterfaces();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         var2.putAll(this.getLuaCache(var6));
      }

      Map var7 = (Map)this.luaToJava.get(var1);
      if (var7 != null) {
         var2.putAll(var7);
      }

      return var2;
   }

   private Map getLuaCache(Class var1) {
      if (var1 == null) {
         return LUA_NULL_MAP;
      } else {
         Map var2 = (Map)this.luatoJavaCache.get(var1);
         if (var2 == null) {
            var2 = this.createLuaCache(var1);
         }

         return var2;
      }
   }

   public Object fromJavaToLua(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Class var2 = var1.getClass();
         JavaToLuaConverter var3 = this.getJavaCache(var2);

         try {
            Object var4 = var3.fromJavaToLua(var1);
            return var4 == null ? var1 : var4;
         } catch (StackOverflowError var5) {
            throw new RuntimeException("Could not convert " + var1 + ": it contained recursive elements.");
         }
      }
   }

   private JavaToLuaConverter getJavaCache(Class var1) {
      if (var1 == null) {
         return NULL_CONVERTER;
      } else {
         JavaToLuaConverter var2 = (JavaToLuaConverter)this.javaToLuaCache.get(var1);
         if (var2 == null) {
            var2 = this.createJavaCache(var1);
            this.javaToLuaCache.put(var1, var2);
         }

         return var2;
      }
   }

   private JavaToLuaConverter createJavaCache(Class var1) {
      JavaToLuaConverter var2 = (JavaToLuaConverter)this.javaToLua.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Class[] var3 = var1.getInterfaces();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            var2 = this.getJavaCache(var6);
            if (var2 != NULL_CONVERTER) {
               return var2;
            }
         }

         return this.getJavaCache(var1.getSuperclass());
      }
   }

   static {
      PRIMITIVE_CLASS.put(Boolean.TYPE, Boolean.class);
      PRIMITIVE_CLASS.put(Byte.TYPE, Byte.class);
      PRIMITIVE_CLASS.put(Character.TYPE, Character.class);
      PRIMITIVE_CLASS.put(Short.TYPE, Short.TYPE);
      PRIMITIVE_CLASS.put(Integer.TYPE, Integer.class);
      PRIMITIVE_CLASS.put(Long.TYPE, Long.class);
      PRIMITIVE_CLASS.put(Float.TYPE, Float.class);
      PRIMITIVE_CLASS.put(Double.TYPE, Double.class);
      LUA_NULL_MAP = new HashMap();
      NULL_CONVERTER = new JavaToLuaConverter() {
         public Object fromJavaToLua(Object var1) {
            return null;
         }

         public Class getJavaType() {
            return Object.class;
         }
      };
   }
}
