package zombie.Lua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;
import zombie.debug.DebugLog;

public final class LuaHookManager implements JavaFunction {
   public static final ArrayList OnTickCallbacks = new ArrayList();
   static Object[] a = new Object[1];
   static Object[] b = new Object[2];
   static Object[] c = new Object[3];
   static Object[] d = new Object[4];
   static Object[] f = new Object[5];
   static Object[] g = new Object[6];
   private static final ArrayList EventList = new ArrayList();
   private static final HashMap EventMap = new HashMap();

   public static boolean TriggerHook(String var0) {
      if (EventMap.containsKey(var0)) {
         Event var1 = (Event)EventMap.get(var0);
         a[0] = null;
         return var1.trigger(LuaManager.env, LuaManager.caller, a);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1) {
      if (EventMap.containsKey(var0)) {
         Event var2 = (Event)EventMap.get(var0);
         a[0] = var1;
         return var2.trigger(LuaManager.env, LuaManager.caller, a);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1, Object var2) {
      if (EventMap.containsKey(var0)) {
         Event var3 = (Event)EventMap.get(var0);
         b[0] = var1;
         b[1] = var2;
         return var3.trigger(LuaManager.env, LuaManager.caller, b);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1, Object var2, Object var3) {
      if (EventMap.containsKey(var0)) {
         Event var4 = (Event)EventMap.get(var0);
         c[0] = var1;
         c[1] = var2;
         c[2] = var3;
         return var4.trigger(LuaManager.env, LuaManager.caller, c);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1, Object var2, Object var3, Object var4) {
      if (EventMap.containsKey(var0)) {
         Event var5 = (Event)EventMap.get(var0);
         d[0] = var1;
         d[1] = var2;
         d[2] = var3;
         d[3] = var4;
         return var5.trigger(LuaManager.env, LuaManager.caller, d);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      if (EventMap.containsKey(var0)) {
         Event var6 = (Event)EventMap.get(var0);
         f[0] = var1;
         f[1] = var2;
         f[2] = var3;
         f[3] = var4;
         f[4] = var5;
         return var6.trigger(LuaManager.env, LuaManager.caller, f);
      } else {
         return false;
      }
   }

   public static boolean TriggerHook(String var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      if (EventMap.containsKey(var0)) {
         Event var7 = (Event)EventMap.get(var0);
         g[0] = var1;
         g[1] = var2;
         g[2] = var3;
         g[3] = var4;
         g[4] = var5;
         g[5] = var6;
         return var7.trigger(LuaManager.env, LuaManager.caller, g);
      } else {
         return false;
      }
   }

   public static void AddEvent(String var0) {
      if (!EventMap.containsKey(var0)) {
         Event var1 = new Event(var0, EventList.size());
         EventList.add(var1);
         EventMap.put(var0, var1);
         Object var2 = LuaManager.env.rawget("Hook");
         if (var2 instanceof KahluaTable) {
            KahluaTable var3 = (KahluaTable)var2;
            var1.register(LuaManager.platform, var3);
         } else {
            DebugLog.log("ERROR: 'Hook' table not found or not a table");
         }

      }
   }

   private static void AddEvents() {
      AddEvent("AutoDrink");
      AddEvent("UseItem");
      AddEvent("Attack");
      AddEvent("CalculateStats");
      AddEvent("WeaponHitCharacter");
      AddEvent("WeaponSwing");
      AddEvent("WeaponSwingHitPoint");
   }

   public static void clear() {
      a[0] = null;
      b[0] = null;
      b[1] = null;
      c[0] = null;
      c[1] = null;
      c[2] = null;
      d[0] = null;
      d[1] = null;
      d[2] = null;
      d[3] = null;
      f[0] = null;
      f[1] = null;
      f[2] = null;
      f[3] = null;
      f[4] = null;
      g[0] = null;
      g[1] = null;
      g[2] = null;
      g[3] = null;
      g[4] = null;
      g[5] = null;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();
      var1.rawset("Hook", var2);
      AddEvents();
   }

   public static void Reset() {
      Iterator var0 = EventList.iterator();

      while(var0.hasNext()) {
         Event var1 = (Event)var0.next();
         var1.callbacks.clear();
      }

      EventList.clear();
      EventMap.clear();
   }

   public int call(LuaCallFrame var1, int var2) {
      return 0;
   }

   private int OnTick(LuaCallFrame var1, int var2) {
      return 0;
   }
}
