package se.krka.kahlua.integration.expose;

import java.util.Iterator;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;

public class IterableExposer {
   @LuaMethod(
      global = true
   )
   public Object iter(Iterable var1) {
      final Iterator var2 = var1.iterator();
      return new JavaFunction() {
         public int call(LuaCallFrame var1, int var2x) {
            return !var2.hasNext() ? 0 : var1.push(var2.next());
         }
      };
   }
}
