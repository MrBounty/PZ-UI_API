package se.krka.kahlua.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiJavaToLuaConverter implements JavaToLuaConverter {
   private final List converters = new ArrayList();
   private final Class clazz;

   public MultiJavaToLuaConverter(Class var1) {
      this.clazz = var1;
   }

   public Class getJavaType() {
      return this.clazz;
   }

   public Object fromJavaToLua(Object var1) {
      Iterator var2 = this.converters.iterator();

      Object var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         JavaToLuaConverter var3 = (JavaToLuaConverter)var2.next();
         var4 = var3.fromJavaToLua(var1);
      } while(var4 == null);

      return var4;
   }

   public void add(JavaToLuaConverter var1) {
      this.converters.add(var1);
   }
}
