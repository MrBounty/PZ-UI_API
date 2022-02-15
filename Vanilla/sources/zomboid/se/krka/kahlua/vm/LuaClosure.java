package se.krka.kahlua.vm;

import se.krka.kahlua.luaj.compiler.LuaCompiler;
import zombie.Lua.LuaEventManager;
import zombie.Lua.MapObjects;

public final class LuaClosure {
   public Prototype prototype;
   public KahluaTable env;
   public UpValue[] upvalues;
   public String debugName;

   public LuaClosure(Prototype var1, KahluaTable var2) {
      this.prototype = var1;
      if (LuaCompiler.rewriteEvents) {
         LuaEventManager.reroute(var1, this);
         MapObjects.reroute(var1, this);
      }

      this.env = var2;
      this.upvalues = new UpValue[var1.numUpvalues];
   }

   public String toString() {
      if (this.prototype.lines.length > 0) {
         String var1 = this.prototype.toString();
         return "function " + var1 + ":" + this.prototype.lines[0];
      } else {
         int var10000 = this.hashCode();
         return "function[" + Integer.toString(var10000, 36) + "]";
      }
   }

   public String toString2(int var1) {
      if (this.prototype.lines.length > 0) {
         if (var1 == 0) {
            var1 = 1;
         }

         return "function: " + this.prototype.name + " -- file: " + this.prototype.file + " line # " + this.prototype.lines[var1 - 1];
      } else {
         int var10000 = this.hashCode();
         return "function[" + Integer.toString(var10000, 36) + "]";
      }
   }
}
