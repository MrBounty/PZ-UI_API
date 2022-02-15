package zombie.globalObjects;

import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;

public final class SGlobalObject extends GlobalObject {
   private static KahluaTable tempTable;

   SGlobalObject(SGlobalObjectSystem var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      boolean var3 = var1.get() == 0;
      if (!var3) {
         this.modData.load(var1, var2);
      }

   }

   public void save(ByteBuffer var1) throws IOException {
      var1.putInt(this.x);
      var1.putInt(this.y);
      var1.put((byte)this.z);
      if (tempTable == null) {
         tempTable = LuaManager.platform.newTable();
      }

      tempTable.wipe();
      KahluaTableIterator var2 = this.modData.iterator();

      while(var2.advance()) {
         Object var3 = var2.getKey();
         if (((SGlobalObjectSystem)this.system).objectModDataKeys.contains(var3)) {
            tempTable.rawset(var3, this.modData.rawget(var3));
         }
      }

      if (tempTable.isEmpty()) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         tempTable.save(var1);
         tempTable.wipe();
      }

   }
}
