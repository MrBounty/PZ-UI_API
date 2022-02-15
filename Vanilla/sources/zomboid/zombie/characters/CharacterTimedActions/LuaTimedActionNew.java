package zombie.characters.CharacterTimedActions;

import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.ai.astar.IPathfinder;
import zombie.ai.astar.Mover;
import zombie.ai.astar.Path;
import zombie.characters.IsoGameCharacter;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class LuaTimedActionNew extends BaseAction implements IPathfinder {
   KahluaTable table;

   public LuaTimedActionNew(KahluaTable var1, IsoGameCharacter var2) {
      super(var2);
      this.table = var1;
      Object var3 = var1.rawget("maxTime");
      this.MaxTime = (Integer)LuaManager.converterManager.fromLuaToJava(var3, Integer.class);
      Object var4 = var1.rawget("stopOnWalk");
      Object var5 = var1.rawget("stopOnRun");
      Object var6 = var1.rawget("stopOnAim");
      Object var7 = var1.rawget("caloriesModifier");
      Object var8 = var1.rawget("useProgressBar");
      Object var9 = var1.rawget("forceProgressBar");
      Object var10 = var1.rawget("loopedAction");
      if (var4 != null) {
         this.StopOnWalk = (Boolean)LuaManager.converterManager.fromLuaToJava(var4, Boolean.class);
      }

      if (var5 != null) {
         this.StopOnRun = (Boolean)LuaManager.converterManager.fromLuaToJava(var5, Boolean.class);
      }

      if (var6 != null) {
         this.StopOnAim = (Boolean)LuaManager.converterManager.fromLuaToJava(var6, Boolean.class);
      }

      if (var7 != null) {
         this.caloriesModifier = (Float)LuaManager.converterManager.fromLuaToJava(var7, Float.class);
      }

      if (var8 != null) {
         this.UseProgressBar = (Boolean)LuaManager.converterManager.fromLuaToJava(var8, Boolean.class);
      }

      if (var9 != null) {
         this.ForceProgressBar = (Boolean)LuaManager.converterManager.fromLuaToJava(var9, Boolean.class);
      }

      if (var10 != null) {
         this.loopAction = (Boolean)LuaManager.converterManager.fromLuaToJava(var10, Boolean.class);
      }

   }

   public void waitToStart() {
      Boolean var1 = LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.table.rawget("waitToStart"), (Object)this.table);
      if (var1 == Boolean.FALSE) {
         super.waitToStart();
      }

   }

   public void update() {
      super.update();
      LuaManager.caller.pcallvoid(LuaManager.thread, this.table.rawget("update"), (Object)this.table);
   }

   public boolean valid() {
      Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("isValid"), (Object)this.table);
      return var1.length > 1 && var1[1] instanceof Boolean && (Boolean)var1[1];
   }

   public void start() {
      super.start();
      this.CurrentTime = 0.0F;
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("start"), (Object)this.table);
   }

   public void stop() {
      super.stop();
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("stop"), (Object)this.table);
   }

   public void perform() {
      super.perform();
      LuaManager.caller.pcall(LuaManager.thread, this.table.rawget("perform"), (Object)this.table);
   }

   public void Failed(Mover var1) {
      this.table.rawset("path", (Object)null);
      LuaManager.caller.pcallvoid(LuaManager.thread, this.table.rawget("failedPathfind"), (Object)this.table);
   }

   public void Succeeded(Path var1, Mover var2) {
      this.table.rawset("path", var1);
      LuaManager.caller.pcallvoid(LuaManager.thread, this.table.rawget("succeededPathfind"), (Object)this.table);
   }

   public void Pathfind(IsoGameCharacter var1, int var2, int var3, int var4) {
   }

   public String getName() {
      return "timedActionPathfind";
   }

   public void setCurrentTime(float var1) {
      this.CurrentTime = PZMath.clamp(var1, 0.0F, (float)this.MaxTime);
   }

   public void setTime(int var1) {
      this.MaxTime = var1;
   }

   public void OnAnimEvent(AnimEvent var1) {
      Object var2 = this.table.rawget("animEvent");
      if (var2 != null) {
         LuaManager.caller.pcallvoid(LuaManager.thread, var2, this.table, var1.m_EventName, var1.m_ParameterValue);
      }

   }

   public String getMetaType() {
      return this.table != null && this.table.getMetatable() != null ? this.table.getMetatable().getString("Type") : "";
   }
}
