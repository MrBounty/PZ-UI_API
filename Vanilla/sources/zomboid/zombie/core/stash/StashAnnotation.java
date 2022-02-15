package zombie.core.stash;

import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import zombie.util.Type;

public final class StashAnnotation {
   public String symbol;
   public String text;
   public float x;
   public float y;
   public float r;
   public float g;
   public float b;

   public void fromLua(KahluaTable var1) {
      KahluaTableImpl var2 = (KahluaTableImpl)var1;
      this.symbol = (String)Type.tryCastTo(var1.rawget("symbol"), String.class);
      this.text = (String)Type.tryCastTo(var1.rawget("text"), String.class);
      this.x = var2.rawgetFloat("x");
      this.y = var2.rawgetFloat("y");
      this.r = var2.rawgetFloat("r");
      this.g = var2.rawgetFloat("g");
      this.b = var2.rawgetFloat("b");
   }
}
