package se.krka.kahlua.converter;

public interface JavaToLuaConverter {
   Class getJavaType();

   Object fromJavaToLua(Object var1);
}
