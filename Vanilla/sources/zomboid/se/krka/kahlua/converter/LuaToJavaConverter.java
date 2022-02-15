package se.krka.kahlua.converter;

public interface LuaToJavaConverter {
   Class getLuaType();

   Class getJavaType();

   Object fromLuaToJava(Object var1, Class var2);
}
