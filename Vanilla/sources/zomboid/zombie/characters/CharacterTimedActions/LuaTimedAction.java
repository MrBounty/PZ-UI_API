package zombie.characters.CharacterTimedActions;

import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;

public final class LuaTimedAction extends BaseAction {
   KahluaTable table;
   public static Object[] statObj = new Object[6];

   public LuaTimedAction(KahluaTable var1, IsoGameCharacter var2) {
      super(var2);
      this.table = var1;
      Object var3 = var1.rawget("maxTime");
      this.MaxTime = (Integer)LuaManager.converterManager.fromLuaToJava(var3, Integer.class);
      Object var4 = var1.rawget("stopOnWalk");
      Object var5 = var1.rawget("stopOnRun");
      Object var6 = var1.rawget("stopOnAim");
      Object var7 = var1.rawget("onUpdateFunc");
      if (var4 != null) {
         this.StopOnWalk = (Boolean)LuaManager.converterManager.fromLuaToJava(var4, Boolean.class);
      }

      if (var5 != null) {
         this.StopOnRun = (Boolean)LuaManager.converterManager.fromLuaToJava(var5, Boolean.class);
      }

      if (var6 != null) {
         this.StopOnAim = (Boolean)LuaManager.converterManager.fromLuaToJava(var6, Boolean.class);
      }

   }

   public void update() {
      statObj[0] = this.table.rawget("character");
      statObj[1] = this.table.rawget("param1");
      statObj[2] = this.table.rawget("param2");
      statObj[3] = this.table.rawget("param3");
      statObj[4] = this.table.rawget("param4");
      statObj[5] = this.table.rawget("param5");
      LuaManager.caller.pcallvoid(LuaManager.thread, this.table.rawget("onUpdateFunc"), statObj);
      super.update();
   }

   public boolean valid() {
      Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("isValidFunc"), this.table.rawget("character"), this.table.rawget("param1"), this.table.rawget("param2"), this.table.rawget("param3"), this.table.rawget("param4"), this.table.rawget("param5"));
      return var1.length > 0 && (Boolean)var1[0];
   }

   public void start() {
      super.start();
      this.CurrentTime = 0.0F;
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("startFunc"), this.table.rawget("character"), this.table.rawget("param1"), this.table.rawget("param2"), this.table.rawget("param3"), this.table.rawget("param4"), this.table.rawget("param5"));
   }

   public void stop() {
      super.stop();
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("onStopFunc"), this.table.rawget("character"), this.table.rawget("param1"), this.table.rawget("param2"), this.table.rawget("param3"), this.table.rawget("param4"), this.table.rawget("param5"));
   }

   public void perform() {
      super.perform();
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("performFunc"), this.table.rawget("character"), this.table.rawget("param1"), this.table.rawget("param2"), this.table.rawget("param3"), this.table.rawget("param4"), this.table.rawget("param5"));
   }
}
