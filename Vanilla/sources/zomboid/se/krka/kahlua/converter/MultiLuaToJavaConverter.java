package se.krka.kahlua.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiLuaToJavaConverter implements LuaToJavaConverter {
   private final List converters = new ArrayList();
   private final Class luaType;
   private final Class javaType;

   public MultiLuaToJavaConverter(Class var1, Class var2) {
      this.luaType = var1;
      this.javaType = var2;
   }

   public Class getLuaType() {
      return this.luaType;
   }

   public Class getJavaType() {
      return this.javaType;
   }

   public Object fromLuaToJava(Object var1, Class var2) {
      Iterator var3 = this.converters.iterator();

      Object var5;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         LuaToJavaConverter var4 = (LuaToJavaConverter)var3.next();
         var5 = var4.fromLuaToJava(var1, var2);
      } while(var5 == null);

      return var5;
   }

   public void add(LuaToJavaConverter var1) {
      this.converters.add(var1);
   }
}
