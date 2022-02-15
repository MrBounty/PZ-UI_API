package zombie.Lua;

import se.krka.kahlua.converter.JavaToLuaConverter;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.converter.LuaToJavaConverter;

public final class KahluaNumberConverter {
   private KahluaNumberConverter() {
   }

   public static void install(KahluaConverterManager var0) {
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Long fromLuaToJava(Double var1, Class var2) {
            return var1.longValue();
         }

         public Class getJavaType() {
            return Long.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Integer fromLuaToJava(Double var1, Class var2) {
            return var1.intValue();
         }

         public Class getJavaType() {
            return Integer.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Float fromLuaToJava(Double var1, Class var2) {
            return new Float(var1.floatValue());
         }

         public Class getJavaType() {
            return Float.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Byte fromLuaToJava(Double var1, Class var2) {
            return var1.byteValue();
         }

         public Class getJavaType() {
            return Byte.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Character fromLuaToJava(Double var1, Class var2) {
            return (char)var1.intValue();
         }

         public Class getJavaType() {
            return Character.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addLuaConverter(new LuaToJavaConverter() {
         public Short fromLuaToJava(Double var1, Class var2) {
            return var1.shortValue();
         }

         public Class getJavaType() {
            return Short.class;
         }

         public Class getLuaType() {
            return Double.class;
         }
      });
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Double.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Float.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Integer.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Long.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Short.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Byte.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Character.class));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Double.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Float.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Integer.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Long.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Short.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Byte.TYPE));
      var0.addJavaConverter(new KahluaNumberConverter.NumberToLuaConverter(Character.TYPE));
      var0.addJavaConverter(new JavaToLuaConverter() {
         public Object fromJavaToLua(Boolean var1) {
            return var1;
         }

         public Class getJavaType() {
            return Boolean.class;
         }
      });
   }

   private static final class NumberToLuaConverter implements JavaToLuaConverter {
      private final Class clazz;

      public NumberToLuaConverter(Class var1) {
         this.clazz = var1;
      }

      public Object fromJavaToLua(Number var1) {
         return var1 instanceof Double ? var1 : KahluaNumberConverter.DoubleCache.valueOf(var1.doubleValue());
      }

      public Class getJavaType() {
         return this.clazz;
      }
   }

   private static final class DoubleCache {
      static final int low = -128;
      static final int high = 10000;
      static final Double[] cache = new Double[10129];

      public static Double valueOf(double var0) {
         return var0 == (double)((int)var0) && var0 >= -128.0D && var0 <= 10000.0D ? cache[(int)(var0 + 128.0D)] : new Double(var0);
      }

      static {
         int var0 = -128;

         for(int var1 = 0; var1 < cache.length; ++var1) {
            cache[var1] = new Double((double)(var0++));
         }

      }
   }
}
