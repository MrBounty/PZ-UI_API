package se.krka.kahlua.require;

import java.io.Reader;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;

public class Loadfile implements JavaFunction {
   private final LuaSourceProvider luaSourceProvider;

   public void install(KahluaTable var1) {
      var1.rawset("loadfile", this);
   }

   public Loadfile(LuaSourceProvider var1) {
      this.luaSourceProvider = var1;
   }

   public int call(LuaCallFrame var1, int var2) {
      String var3 = KahluaUtil.getStringArg(var1, 1, "loadfile");
      Reader var4 = this.luaSourceProvider.getLuaSource(var3);
      if (var4 == null) {
         var1.pushNil();
         var1.push("Does not exist: " + var3);
         return 2;
      } else {
         var1.setTop(2);
         var1.set(0, var4);
         var1.set(1, var3);
         return LuaCompiler.loadstream(var1, 2);
      }
   }
}
