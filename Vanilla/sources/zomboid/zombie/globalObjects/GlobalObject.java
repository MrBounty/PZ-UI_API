package zombie.globalObjects;

import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;

public abstract class GlobalObject {
   protected GlobalObjectSystem system;
   protected int x;
   protected int y;
   protected int z;
   protected final KahluaTable modData;

   GlobalObject(GlobalObjectSystem var1, int var2, int var3, int var4) {
      this.system = var1;
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.modData = LuaManager.platform.newTable();
   }

   public GlobalObjectSystem getSystem() {
      return this.system;
   }

   public void setLocation(int var1, int var2, int var3) {
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public KahluaTable getModData() {
      return this.modData;
   }

   public void Reset() {
      this.system = null;
      this.modData.wipe();
   }
}
