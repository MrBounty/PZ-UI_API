package zombie.globalObjects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.network.TableNetworkUtils;
import zombie.util.Type;

public final class CGlobalObjects {
   protected static final ArrayList systems = new ArrayList();
   protected static final HashMap initialState = new HashMap();

   public static void noise(String var0) {
      if (Core.bDebug) {
         DebugLog.log("CGlobalObjects: " + var0);
      }

   }

   public static CGlobalObjectSystem registerSystem(String var0) {
      CGlobalObjectSystem var1 = getSystemByName(var0);
      if (var1 == null) {
         var1 = newSystem(var0);
         KahluaTable var2 = (KahluaTable)initialState.get(var0);
         if (var2 != null) {
            KahluaTableIterator var3 = var2.iterator();

            while(true) {
               while(var3.advance()) {
                  Object var4 = var3.getKey();
                  Object var5 = var3.getValue();
                  if ("_objects".equals(var4)) {
                     KahluaTable var6 = (KahluaTable)Type.tryCastTo(var5, KahluaTable.class);
                     int var7 = 1;

                     for(int var8 = var6.len(); var7 <= var8; ++var7) {
                        KahluaTable var9 = (KahluaTable)Type.tryCastTo(var6.rawget(var7), KahluaTable.class);
                        int var10 = ((Double)var9.rawget("x")).intValue();
                        int var11 = ((Double)var9.rawget("y")).intValue();
                        int var12 = ((Double)var9.rawget("z")).intValue();
                        var9.rawset("x", (Object)null);
                        var9.rawset("y", (Object)null);
                        var9.rawset("z", (Object)null);
                        CGlobalObject var13 = (CGlobalObject)Type.tryCastTo(var1.newObject(var10, var11, var12), CGlobalObject.class);
                        KahluaTableIterator var14 = var9.iterator();

                        while(var14.advance()) {
                           var13.getModData().rawset(var14.getKey(), var14.getValue());
                        }
                     }

                     var6.wipe();
                  } else {
                     var1.modData.rawset(var4, var5);
                  }
               }

               return var1;
            }
         }
      }

      return var1;
   }

   public static CGlobalObjectSystem newSystem(String var0) throws IllegalStateException {
      if (getSystemByName(var0) != null) {
         throw new IllegalStateException("system with that name already exists");
      } else {
         noise("newSystem " + var0);
         CGlobalObjectSystem var1 = new CGlobalObjectSystem(var0);
         systems.add(var1);
         return var1;
      }
   }

   public static int getSystemCount() {
      return systems.size();
   }

   public static CGlobalObjectSystem getSystemByIndex(int var0) {
      return var0 >= 0 && var0 < systems.size() ? (CGlobalObjectSystem)systems.get(var0) : null;
   }

   public static CGlobalObjectSystem getSystemByName(String var0) {
      for(int var1 = 0; var1 < systems.size(); ++var1) {
         CGlobalObjectSystem var2 = (CGlobalObjectSystem)systems.get(var1);
         if (var2.name.equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static void initSystems() {
      LuaEventManager.triggerEvent("OnCGlobalObjectSystemInit");
   }

   public static void loadInitialState(ByteBuffer var0) throws IOException {
      byte var1 = var0.get();

      for(int var2 = 0; var2 < var1; ++var2) {
         String var3 = GameWindow.ReadStringUTF(var0);
         if (var0.get() != 0) {
            KahluaTable var4 = LuaManager.platform.newTable();
            initialState.put(var3, var4);
            TableNetworkUtils.load(var4, var0);
         }
      }

   }

   public static boolean receiveServerCommand(String var0, String var1, KahluaTable var2) {
      CGlobalObjectSystem var3 = getSystemByName(var0);
      if (var3 == null) {
         throw new IllegalStateException("system '" + var0 + "' not found");
      } else {
         var3.receiveServerCommand(var1, var2);
         return true;
      }
   }

   public static void Reset() {
      for(int var0 = 0; var0 < systems.size(); ++var0) {
         CGlobalObjectSystem var1 = (CGlobalObjectSystem)systems.get(var0);
         var1.Reset();
      }

      systems.clear();
      initialState.clear();
      CGlobalObjectNetwork.Reset();
   }
}
