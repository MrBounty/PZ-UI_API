package zombie.core.znet;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import zombie.Lua.LuaEventManager;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.RenderThread;
import zombie.debug.DebugLog;
import zombie.network.CoopSlave;
import zombie.network.GameServer;
import zombie.network.ServerWorldDatabase;

public class SteamUtils {
   private static boolean m_steamEnabled;
   private static boolean m_netEnabled;
   private static final BigInteger TWO_64;
   private static final BigInteger MAX_ULONG;
   private static List m_joinRequestCallbacks;

   private static void loadLibrary(String var0) {
      DebugLog.log("Loading " + var0 + "...");
      System.loadLibrary(var0);
   }

   public static void init() {
      m_steamEnabled = System.getProperty("zomboid.steam") != null && System.getProperty("zomboid.steam").equals("1");
      DebugLog.log("Loading networking libraries...");
      String var0 = "";
      if ("1".equals(System.getProperty("zomboid.debuglibs"))) {
         DebugLog.log("***** Loading debug versions of libraries");
         var0 = "d";
      }

      try {
         if (System.getProperty("os.name").contains("OS X")) {
            if (m_steamEnabled) {
               loadLibrary("steam_api");
               loadLibrary("RakNet");
               loadLibrary("ZNetJNI");
            } else {
               loadLibrary("RakNet");
               loadLibrary("ZNetNoSteam");
            }
         } else if (System.getProperty("os.name").startsWith("Win")) {
            if (System.getProperty("sun.arch.data.model").equals("64")) {
               if (m_steamEnabled) {
                  loadLibrary("steam_api64");
                  loadLibrary("RakNet64" + var0);
                  loadLibrary("ZNetJNI64" + var0);
               } else {
                  loadLibrary("RakNet64" + var0);
                  loadLibrary("ZNetNoSteam64" + var0);
               }
            } else if (m_steamEnabled) {
               loadLibrary("steam_api");
               loadLibrary("RakNet32" + var0);
               loadLibrary("ZNetJNI32" + var0);
            } else {
               loadLibrary("RakNet32" + var0);
               loadLibrary("ZNetNoSteam32" + var0);
            }
         } else if (System.getProperty("sun.arch.data.model").equals("64")) {
            if (m_steamEnabled) {
               loadLibrary("steam_api");
               loadLibrary("RakNet64");
               loadLibrary("ZNetJNI64");
            } else {
               loadLibrary("RakNet64");
               loadLibrary("ZNetNoSteam64");
            }
         } else if (m_steamEnabled) {
            loadLibrary("steam_api");
            loadLibrary("RakNet32");
            loadLibrary("ZNetJNI32");
         } else {
            loadLibrary("RakNet32");
            loadLibrary("ZNetNoSteam32");
         }

         m_netEnabled = true;
      } catch (UnsatisfiedLinkError var6) {
         m_steamEnabled = false;
         m_netEnabled = false;
         ExceptionLogger.logException(var6);
         if (System.getProperty("os.name").startsWith("Win")) {
            DebugLog.log("One of the game's DLLs could not be loaded.");
            DebugLog.log("  Your system may be missing a DLL needed by the game's DLL.");
            DebugLog.log("  You may need to install the Microsoft Visual C++ Redistributable 2013.");
            File var2 = new File("../_CommonRedist/vcredist/");
            if (var2.exists()) {
               DebugLog.log("  This file is provided in " + var2.getAbsolutePath());
            }
         }
      }

      String var1 = System.getProperty("zomboid.znetlog");
      if (m_netEnabled && var1 != null) {
         try {
            int var7 = Integer.parseInt(var1);
            ZNet.setLogLevel(var7);
         } catch (NumberFormatException var5) {
            ExceptionLogger.logException(var5);
         }
      }

      if (!m_netEnabled) {
         DebugLog.log("Failed to load networking libraries");
      } else {
         ZNet.init();
         synchronized(RenderThread.m_contextLock) {
            if (!m_steamEnabled) {
               DebugLog.log("SteamUtils started without Steam");
            } else if (n_Init(GameServer.bServer)) {
               DebugLog.log("SteamUtils initialised successfully");
            } else {
               DebugLog.log("Could not initialise SteamUtils");
               m_steamEnabled = false;
            }
         }
      }

      m_joinRequestCallbacks = new ArrayList();
   }

   public static void shutdown() {
      if (m_steamEnabled) {
         n_Shutdown();
      }

   }

   public static void runLoop() {
      if (m_steamEnabled) {
         n_RunLoop();
      }

   }

   public static boolean isSteamModeEnabled() {
      return m_steamEnabled;
   }

   public static boolean isOverlayEnabled() {
      return m_steamEnabled && n_IsOverlayEnabled();
   }

   public static String convertSteamIDToString(long var0) {
      BigInteger var2 = BigInteger.valueOf(var0);
      if (var2.signum() < 0) {
         var2.add(TWO_64);
      }

      return var2.toString();
   }

   public static boolean isValidSteamID(String var0) {
      try {
         BigInteger var1 = new BigInteger(var0);
         return var1.signum() >= 0 && var1.compareTo(MAX_ULONG) <= 0;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static long convertStringToSteamID(String var0) {
      try {
         BigInteger var1 = new BigInteger(var0);
         return var1.signum() >= 0 && var1.compareTo(MAX_ULONG) <= 0 ? var1.longValue() : -1L;
      } catch (NumberFormatException var2) {
         return -1L;
      }
   }

   public static void addJoinRequestCallback(IJoinRequestCallback var0) {
      m_joinRequestCallbacks.add(var0);
   }

   public static void removeJoinRequestCallback(IJoinRequestCallback var0) {
      m_joinRequestCallbacks.remove(var0);
   }

   private static native boolean n_Init(boolean var0);

   private static native void n_Shutdown();

   private static native void n_RunLoop();

   private static native boolean n_IsOverlayEnabled();

   private static void joinRequestCallback(long var0, String var2) {
      DebugLog.log("Got Join Request");
      Iterator var3 = m_joinRequestCallbacks.iterator();

      while(var3.hasNext()) {
         IJoinRequestCallback var4 = (IJoinRequestCallback)var3.next();
         var4.onJoinRequest(var0, var2);
      }

      if (var2.contains("+connect ")) {
         String var5 = var2.substring(9);
         System.setProperty("args.server.connect", var5);
         LuaEventManager.triggerEvent("OnSteamGameJoin");
      }

   }

   private static int clientInitiateConnectionCallback(long var0) {
      if (CoopSlave.instance == null) {
         ServerWorldDatabase.LogonResult var2 = ServerWorldDatabase.instance.authClient(var0);
         return var2.bAuthorized ? 0 : 1;
      } else {
         return !CoopSlave.instance.isHost(var0) && !CoopSlave.instance.isInvited(var0) ? 2 : 0;
      }
   }

   private static int validateOwnerCallback(long var0, long var2) {
      if (CoopSlave.instance != null) {
         return 0;
      } else {
         ServerWorldDatabase.LogonResult var4 = ServerWorldDatabase.instance.authOwner(var0, var2);
         return var4.bAuthorized ? 0 : 1;
      }
   }

   static {
      TWO_64 = BigInteger.ONE.shiftLeft(64);
      MAX_ULONG = new BigInteger("FFFFFFFFFFFFFFFF", 16);
   }
}
