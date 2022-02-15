package zombie.radio.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import zombie.characters.IsoPlayer;

public final class LineEffectMemory {
   private final Map memory = new HashMap();

   public void addLine(IsoPlayer var1, String var2) {
      int var3 = var1.getDescriptor().getID();
      ArrayList var4;
      if (!this.memory.containsKey(var3)) {
         var4 = new ArrayList();
         this.memory.put(var3, var4);
      } else {
         var4 = (ArrayList)this.memory.get(var3);
      }

      if (!var4.contains(var2)) {
         var4.add(var2);
      }

   }

   public boolean contains(IsoPlayer var1, String var2) {
      int var3 = var1.getDescriptor().getID();
      return !this.memory.containsKey(var3) ? false : ((ArrayList)this.memory.get(var3)).contains(var2);
   }
}
