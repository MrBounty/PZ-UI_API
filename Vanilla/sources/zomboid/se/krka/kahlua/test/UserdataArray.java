package se.krka.kahlua.test;

import java.util.Vector;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public class UserdataArray implements JavaFunction {
   private static final int LENGTH = 0;
   private static final int INDEX = 1;
   private static final int NEWINDEX = 2;
   private static final int NEW = 3;
   private static final int PUSH = 4;
   private static final Class VECTOR_CLASS = (new Vector()).getClass();
   private static KahluaTable metatable;
   private int index;

   public static synchronized void register(Platform var0, KahluaTable var1) {
      if (metatable == null) {
         metatable = var0.newTable();
         metatable.rawset("__metatable", "restricted");
         metatable.rawset("__len", new UserdataArray(0));
         metatable.rawset("__index", new UserdataArray(1));
         metatable.rawset("__newindex", new UserdataArray(2));
         metatable.rawset("new", new UserdataArray(3));
         metatable.rawset("push", new UserdataArray(4));
      }

      KahluaTable var2 = KahluaUtil.getClassMetatables(var0, var1);
      var2.rawset(VECTOR_CLASS, metatable);
      var1.rawset("array", metatable);
   }

   private UserdataArray(int var1) {
      this.index = var1;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return this.length(var1, var2);
      case 1:
         return this.index(var1, var2);
      case 2:
         return this.newindex(var1, var2);
      case 3:
         return this.newVector(var1, var2);
      case 4:
         return this.push(var1, var2);
      default:
         return 0;
      }
   }

   private int push(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "not enough parameters");
      Vector var3 = (Vector)var1.get(0);
      Object var4 = var1.get(1);
      var3.addElement(var4);
      var1.push(var3);
      return 1;
   }

   private int newVector(LuaCallFrame var1, int var2) {
      var1.push(new Vector());
      return 1;
   }

   private int newindex(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 3, "not enough parameters");
      Vector var3 = (Vector)var1.get(0);
      Object var4 = var1.get(1);
      Object var5 = var1.get(2);
      var3.setElementAt(var5, (int)KahluaUtil.fromDouble(var4));
      return 0;
   }

   private int index(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "not enough parameters");
      Object var3 = var1.get(0);
      if (var3 != null && var3 instanceof Vector) {
         Vector var4 = (Vector)var3;
         Object var5 = var1.get(1);
         Object var6;
         if (var5 instanceof Double) {
            var6 = var4.elementAt((int)KahluaUtil.fromDouble(var5));
         } else {
            var6 = metatable.rawget(var5);
         }

         var1.push(var6);
         return 1;
      } else {
         return 0;
      }
   }

   private int length(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "not enough parameters");
      Vector var3 = (Vector)var1.get(0);
      double var4 = (double)var3.size();
      var1.push(KahluaUtil.toDouble(var4));
      return 1;
   }
}
