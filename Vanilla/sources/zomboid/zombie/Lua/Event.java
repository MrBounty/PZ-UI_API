package zombie.Lua;

import java.util.ArrayList;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;

public final class Event {
   public static final int ADD = 0;
   public static final int NUM_FUNCTIONS = 1;
   private final Event.Add add;
   private final Event.Remove remove;
   public final ArrayList callbacks = new ArrayList();
   public String name;
   private int index = 0;

   public boolean trigger(KahluaTable var1, LuaCaller var2, Object[] var3) {
      if (this.callbacks.isEmpty()) {
         return false;
      } else {
         int var4;
         if (DebugOptions.instance.Checks.SlowLuaEvents.getValue()) {
            for(var4 = 0; var4 < this.callbacks.size(); ++var4) {
               try {
                  LuaClosure var5 = (LuaClosure)this.callbacks.get(var4);
                  long var6 = System.nanoTime();
                  var2.protectedCallVoid(LuaManager.thread, var5, (Object[])var3);
                  double var8 = (double)(System.nanoTime() - var6) / 1000000.0D;
                  if (var8 > 250.0D) {
                     DebugLog.Lua.warn("SLOW Lua event callback %s %s %dms", var5.prototype.file, var5, (int)var8);
                  }
               } catch (Exception var10) {
                  ExceptionLogger.logException(var10);
               }
            }

            return true;
         } else {
            for(var4 = 0; var4 < this.callbacks.size(); ++var4) {
               try {
                  var2.protectedCallVoid(LuaManager.thread, this.callbacks.get(var4), var3);
               } catch (Exception var11) {
                  ExceptionLogger.logException(var11);
               }
            }

            return true;
         }
      }
   }

   public Event(String var1, int var2) {
      this.index = var2;
      this.name = var1;
      this.add = new Event.Add(this);
      this.remove = new Event.Remove(this);
   }

   public void register(Platform var1, KahluaTable var2) {
      KahluaTable var3 = var1.newTable();
      var3.rawset("Add", this.add);
      var3.rawset("Remove", this.remove);
      var2.rawset(this.name, var3);
   }

   public static final class Add implements JavaFunction {
      Event e;

      public Add(Event var1) {
         this.e = var1;
      }

      public int call(LuaCallFrame var1, int var2) {
         if (LuaCompiler.rewriteEvents) {
            return 0;
         } else {
            Object var3 = var1.get(0);
            if (this.e.name.contains("CreateUI")) {
               boolean var4 = false;
            }

            if (var3 instanceof LuaClosure) {
               LuaClosure var5 = (LuaClosure)var3;
               this.e.callbacks.add(var5);
            }

            return 0;
         }
      }
   }

   public static final class Remove implements JavaFunction {
      Event e;

      public Remove(Event var1) {
         this.e = var1;
      }

      public int call(LuaCallFrame var1, int var2) {
         if (LuaCompiler.rewriteEvents) {
            return 0;
         } else {
            Object var3 = var1.get(0);
            if (var3 instanceof LuaClosure) {
               LuaClosure var4 = (LuaClosure)var3;
               this.e.callbacks.remove(var4);
            }

            return 0;
         }
      }
   }
}
