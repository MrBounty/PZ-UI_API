package zombie.globalObjects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.iso.IsoObject;
import zombie.iso.SliceY;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.TableNetworkUtils;

public final class SGlobalObjects {
   protected static final ArrayList systems = new ArrayList();

   public static void noise(String var0) {
      if (Core.bDebug) {
         DebugLog.log("SGlobalObjects: " + var0);
      }

   }

   public static SGlobalObjectSystem registerSystem(String var0) {
      SGlobalObjectSystem var1 = getSystemByName(var0);
      if (var1 == null) {
         var1 = newSystem(var0);
         var1.load();
      }

      return var1;
   }

   public static SGlobalObjectSystem newSystem(String var0) throws IllegalStateException {
      if (getSystemByName(var0) != null) {
         throw new IllegalStateException("system with that name already exists");
      } else {
         noise("newSystem " + var0);
         SGlobalObjectSystem var1 = new SGlobalObjectSystem(var0);
         systems.add(var1);
         return var1;
      }
   }

   public static int getSystemCount() {
      return systems.size();
   }

   public static SGlobalObjectSystem getSystemByIndex(int var0) {
      return var0 >= 0 && var0 < systems.size() ? (SGlobalObjectSystem)systems.get(var0) : null;
   }

   public static SGlobalObjectSystem getSystemByName(String var0) {
      for(int var1 = 0; var1 < systems.size(); ++var1) {
         SGlobalObjectSystem var2 = (SGlobalObjectSystem)systems.get(var1);
         if (var2.name.equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static void update() {
      for(int var0 = 0; var0 < systems.size(); ++var0) {
         SGlobalObjectSystem var1 = (SGlobalObjectSystem)systems.get(var0);
         var1.update();
      }

   }

   public static void chunkLoaded(int var0, int var1) {
      for(int var2 = 0; var2 < systems.size(); ++var2) {
         SGlobalObjectSystem var3 = (SGlobalObjectSystem)systems.get(var2);
         var3.chunkLoaded(var0, var1);
      }

   }

   public static void initSystems() {
      if (!GameClient.bClient) {
         LuaEventManager.triggerEvent("OnSGlobalObjectSystemInit");
         if (!GameServer.bServer) {
            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  saveInitialStateForClient(SliceY.SliceBuffer);
                  SliceY.SliceBuffer.flip();
                  CGlobalObjects.loadInitialState(SliceY.SliceBuffer);
               }
            } catch (Throwable var3) {
               ExceptionLogger.logException(var3);
            }

         }
      }
   }

   public static void saveInitialStateForClient(ByteBuffer var0) throws IOException {
      var0.put((byte)systems.size());

      for(int var1 = 0; var1 < systems.size(); ++var1) {
         SGlobalObjectSystem var2 = (SGlobalObjectSystem)systems.get(var1);
         GameWindow.WriteStringUTF(var0, var2.name);
         KahluaTable var3 = var2.getInitialStateForClient();
         if (var3 == null) {
            var3 = LuaManager.platform.newTable();
         }

         KahluaTable var4 = LuaManager.platform.newTable();
         var3.rawset("_objects", var4);

         for(int var5 = 0; var5 < var2.getObjectCount(); ++var5) {
            GlobalObject var6 = var2.getObjectByIndex(var5);
            KahluaTable var7 = LuaManager.platform.newTable();
            var7.rawset("x", BoxedStaticValues.toDouble((double)var6.getX()));
            var7.rawset("y", BoxedStaticValues.toDouble((double)var6.getY()));
            var7.rawset("z", BoxedStaticValues.toDouble((double)var6.getZ()));
            Iterator var8 = var2.objectSyncKeys.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               var7.rawset(var9, var6.getModData().rawget(var9));
            }

            var4.rawset(var5 + 1, var7);
         }

         if (var3 != null && !var3.isEmpty()) {
            var0.put((byte)1);
            TableNetworkUtils.save(var3, var0);
         } else {
            var0.put((byte)0);
         }
      }

   }

   public static boolean receiveClientCommand(String var0, String var1, IsoPlayer var2, KahluaTable var3) {
      noise("receiveClientCommand " + var0 + " " + var1 + " OnlineID=" + var2.getOnlineID());
      SGlobalObjectSystem var4 = getSystemByName(var0);
      if (var4 == null) {
         throw new IllegalStateException("system '" + var0 + "' not found");
      } else {
         var4.receiveClientCommand(var1, var2, var3);
         return true;
      }
   }

   public static void load() {
   }

   public static void save() {
      for(int var0 = 0; var0 < systems.size(); ++var0) {
         SGlobalObjectSystem var1 = (SGlobalObjectSystem)systems.get(var0);
         var1.save();
      }

   }

   public static void OnIsoObjectChangedItself(String var0, IsoObject var1) {
      if (!GameClient.bClient) {
         SGlobalObjectSystem var2 = getSystemByName(var0);
         if (var2 != null) {
            var2.OnIsoObjectChangedItself(var1);
         }
      }
   }

   public static void Reset() {
      for(int var0 = 0; var0 < systems.size(); ++var0) {
         SGlobalObjectSystem var1 = (SGlobalObjectSystem)systems.get(var0);
         var1.Reset();
      }

      systems.clear();
      GlobalObjectLookup.Reset();
   }
}
