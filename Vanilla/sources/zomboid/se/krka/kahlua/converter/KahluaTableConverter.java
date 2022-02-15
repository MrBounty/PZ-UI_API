package se.krka.kahlua.converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.Platform;

public class KahluaTableConverter {
   private final Platform platform;

   public KahluaTableConverter(Platform var1) {
      this.platform = var1;
   }

   public void install(final KahluaConverterManager var1) {
      var1.addJavaConverter(new KahluaTableConverter.CollectionToLuaConverter(var1, Collection.class));
      var1.addLuaConverter(new KahluaTableConverter.CollectionToJavaConverter(Collection.class));
      var1.addJavaConverter(new JavaToLuaConverter() {
         public Object fromJavaToLua(Map var1x) {
            KahluaTable var3 = KahluaTableConverter.this.platform.newTable();
            Iterator var4 = var1x.entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               Object var6 = var1.fromJavaToLua(var5.getKey());
               Object var7 = var1.fromJavaToLua(var5.getValue());
               var3.rawset(var6, var7);
            }

            return var3;
         }

         public Class getJavaType() {
            return Map.class;
         }
      });
      var1.addLuaConverter(new LuaToJavaConverter() {
         public Map fromLuaToJava(KahluaTable var1, Class var2) throws IllegalArgumentException {
            KahluaTableIterator var3 = var1.iterator();
            HashMap var4 = new HashMap();

            while(var3.advance()) {
               Object var5 = var3.getKey();
               Object var6 = var3.getValue();
               var4.put(var5, var6);
            }

            return var4;
         }

         public Class getJavaType() {
            return Map.class;
         }

         public Class getLuaType() {
            return KahluaTable.class;
         }
      });
      var1.addJavaConverter(new JavaToLuaConverter() {
         public Object fromJavaToLua(Object var1x) {
            if (!var1x.getClass().isArray()) {
               return null;
            } else {
               KahluaTable var2 = KahluaTableConverter.this.platform.newTable();
               int var3 = Array.getLength(var1x);

               for(int var4 = 0; var4 < var3; ++var4) {
                  Object var5 = Array.get(var1x, var4);
                  var2.rawset(var4 + 1, var1.fromJavaToLua(var5));
               }

               return var2;
            }
         }

         public Class getJavaType() {
            return Object.class;
         }
      });
      var1.addLuaConverter(new LuaToJavaConverter() {
         public Object fromLuaToJava(KahluaTable var1x, Class var2) throws IllegalArgumentException {
            if (var2.isArray()) {
               List var3 = (List)var1.fromLuaToJava(var1x, List.class);
               return var3.toArray();
            } else {
               return null;
            }
         }

         public Class getJavaType() {
            return Object.class;
         }

         public Class getLuaType() {
            return KahluaTable.class;
         }
      });
   }

   private class CollectionToLuaConverter implements JavaToLuaConverter {
      private final Class clazz;
      private final KahluaConverterManager manager;

      public CollectionToLuaConverter(KahluaConverterManager var2, Class var3) {
         this.manager = var2;
         this.clazz = var3;
      }

      public Object fromJavaToLua(Iterable var1) {
         KahluaTable var2 = KahluaTableConverter.this.platform.newTable();
         int var3 = 0;
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            ++var3;
            var2.rawset(var3, this.manager.fromJavaToLua(var5));
         }

         return var2;
      }

      public Class getJavaType() {
         return this.clazz;
      }
   }

   private static class CollectionToJavaConverter implements LuaToJavaConverter {
      private final Class javaClass;

      private CollectionToJavaConverter(Class var1) {
         this.javaClass = var1;
      }

      public Object fromLuaToJava(KahluaTable var1, Class var2) throws IllegalArgumentException {
         int var3 = var1.len();
         ArrayList var4 = new ArrayList(var3);

         for(int var5 = 1; var5 <= var3; ++var5) {
            Object var6 = var1.rawget(var5);
            var4.add(var6);
         }

         return var4;
      }

      public Class getJavaType() {
         return this.javaClass;
      }

      public Class getLuaType() {
         return KahluaTable.class;
      }
   }
}
