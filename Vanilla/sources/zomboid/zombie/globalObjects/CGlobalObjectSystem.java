package zombie.globalObjects;

import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.BoxedStaticValues;

public final class CGlobalObjectSystem extends GlobalObjectSystem {
   public CGlobalObjectSystem(String var1) {
      super(var1);
   }

   protected GlobalObject makeObject(int var1, int var2, int var3) {
      return new CGlobalObject(this, var1, var2, var3);
   }

   public void sendCommand(String var1, IsoPlayer var2, KahluaTable var3) {
      CGlobalObjectNetwork.sendClientCommand(var2, this.name, var1, var3);
   }

   public void receiveServerCommand(String var1, KahluaTable var2) {
      Object var3 = this.modData.rawget("OnServerCommand");
      if (var3 == null) {
         throw new IllegalStateException("OnServerCommand method undefined for system '" + this.name + "'");
      } else {
         LuaManager.caller.pcallvoid(LuaManager.thread, var3, this.modData, var1, var2);
      }
   }

   public void receiveNewLuaObjectAt(int var1, int var2, int var3, KahluaTable var4) {
      Object var5 = this.modData.rawget("newLuaObjectAt");
      if (var5 == null) {
         throw new IllegalStateException("newLuaObjectAt method undefined for system '" + this.name + "'");
      } else {
         LuaManager.caller.pcall(LuaManager.thread, var5, this.modData, BoxedStaticValues.toDouble((double)var1), BoxedStaticValues.toDouble((double)var2), BoxedStaticValues.toDouble((double)var3));
         GlobalObject var6 = this.getObjectAt(var1, var2, var3);
         if (var6 != null) {
            KahluaTableIterator var7 = var4.iterator();

            while(var7.advance()) {
               var6.getModData().rawset(var7.getKey(), var7.getValue());
            }

         }
      }
   }

   public void receiveRemoveLuaObjectAt(int var1, int var2, int var3) {
      Object var4 = this.modData.rawget("removeLuaObjectAt");
      if (var4 == null) {
         throw new IllegalStateException("removeLuaObjectAt method undefined for system '" + this.name + "'");
      } else {
         LuaManager.caller.pcall(LuaManager.thread, var4, this.modData, BoxedStaticValues.toDouble((double)var1), BoxedStaticValues.toDouble((double)var2), BoxedStaticValues.toDouble((double)var3));
      }
   }

   public void receiveUpdateLuaObjectAt(int var1, int var2, int var3, KahluaTable var4) {
      GlobalObject var5 = this.getObjectAt(var1, var2, var3);
      if (var5 != null) {
         KahluaTableIterator var6 = var4.iterator();

         while(var6.advance()) {
            var5.getModData().rawset(var6.getKey(), var6.getValue());
         }

         Object var7 = this.modData.rawget("OnLuaObjectUpdated");
         if (var7 == null) {
            throw new IllegalStateException("OnLuaObjectUpdated method undefined for system '" + this.name + "'");
         } else {
            LuaManager.caller.pcall(LuaManager.thread, var7, this.modData, var5.getModData());
         }
      }
   }

   public void Reset() {
      super.Reset();
      this.modData.wipe();
   }
}
