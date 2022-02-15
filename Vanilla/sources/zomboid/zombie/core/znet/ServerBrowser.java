package zombie.core.znet;

import java.util.ArrayList;
import java.util.List;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.network.Server;

public class ServerBrowser {
   private static IServerBrowserCallback m_callbackInterface = null;

   public static boolean init() {
      boolean var0 = false;
      if (SteamUtils.isSteamModeEnabled()) {
         var0 = n_Init();
      }

      return var0;
   }

   public static void shutdown() {
      if (SteamUtils.isSteamModeEnabled()) {
         n_Shutdown();
      }

   }

   public static void RefreshInternetServers() {
      if (SteamUtils.isSteamModeEnabled()) {
         n_RefreshInternetServers();
      }

   }

   public static int GetServerCount() {
      int var0 = 0;
      if (SteamUtils.isSteamModeEnabled()) {
         var0 = n_GetServerCount();
      }

      return var0;
   }

   public static GameServerDetails GetServerDetails(int var0) {
      GameServerDetails var1 = null;
      if (SteamUtils.isSteamModeEnabled()) {
         var1 = n_GetServerDetails(var0);
      }

      return var1;
   }

   public static void Release() {
      if (SteamUtils.isSteamModeEnabled()) {
         n_Release();
      }

   }

   public static boolean IsRefreshing() {
      boolean var0 = false;
      if (SteamUtils.isSteamModeEnabled()) {
         var0 = n_IsRefreshing();
      }

      return var0;
   }

   public static boolean QueryServer(String var0, int var1) {
      boolean var2 = false;
      if (SteamUtils.isSteamModeEnabled()) {
         var2 = n_QueryServer(var0, var1);
      }

      return var2;
   }

   public static GameServerDetails GetServerDetails(String var0, int var1) {
      GameServerDetails var2 = null;
      if (SteamUtils.isSteamModeEnabled()) {
         var2 = n_GetServerDetails(var0, var1);
      }

      return var2;
   }

   public static void ReleaseServerQuery(String var0, int var1) {
      if (SteamUtils.isSteamModeEnabled()) {
         n_ReleaseServerQuery(var0, var1);
      }

   }

   public static List GetServerList() {
      ArrayList var0 = new ArrayList();
      if (SteamUtils.isSteamModeEnabled()) {
         while(true) {
            try {
               if (IsRefreshing()) {
                  Thread.sleep(100L);
                  SteamUtils.runLoop();
                  continue;
               }
            } catch (InterruptedException var3) {
               var3.printStackTrace();
            }

            for(int var1 = 0; var1 < GetServerCount(); ++var1) {
               GameServerDetails var2 = GetServerDetails(var1);
               if (var2.steamId != 0L) {
                  var0.add(var2);
               }
            }

            return var0;
         }
      } else {
         return var0;
      }
   }

   public static GameServerDetails GetServerDetailsSync(String var0, int var1) {
      GameServerDetails var2 = null;
      if (SteamUtils.isSteamModeEnabled()) {
         var2 = GetServerDetails(var0, var1);
         if (var2 == null) {
            QueryServer(var0, var1);

            try {
               while(var2 == null) {
                  Thread.sleep(100L);
                  SteamUtils.runLoop();
                  var2 = GetServerDetails(var0, var1);
               }
            } catch (InterruptedException var4) {
               var4.printStackTrace();
            }
         }
      }

      return var2;
   }

   public static boolean RequestServerRules(String var0, int var1) {
      return n_RequestServerRules(var0, var1);
   }

   public static void setCallbackInterface(IServerBrowserCallback var0) {
      m_callbackInterface = var0;
   }

   private static native boolean n_Init();

   private static native void n_Shutdown();

   private static native void n_RefreshInternetServers();

   private static native int n_GetServerCount();

   private static native GameServerDetails n_GetServerDetails(int var0);

   private static native void n_Release();

   private static native boolean n_IsRefreshing();

   private static native boolean n_QueryServer(String var0, int var1);

   private static native GameServerDetails n_GetServerDetails(String var0, int var1);

   private static native void n_ReleaseServerQuery(String var0, int var1);

   private static native boolean n_RequestServerRules(String var0, int var1);

   private static void onServerRespondedCallback(int var0) {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnServerResponded(var0);
      }

      LuaEventManager.triggerEvent("OnSteamServerResponded", var0);
   }

   private static void onServerFailedToRespondCallback(int var0) {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnServerFailedToRespond(var0);
      }

   }

   private static void onRefreshCompleteCallback() {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnRefreshComplete();
      }

      LuaEventManager.triggerEvent("OnSteamRefreshInternetServers");
   }

   private static void onServerRespondedCallback(String var0, int var1) {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnServerResponded(var0, var1);
      }

      GameServerDetails var2 = GetServerDetails(var0, var1);
      if (var2 != null) {
         Server var3 = new Server();
         var3.setName(var2.name);
         var3.setDescription("");
         var3.setSteamId(Long.toString(var2.steamId));
         var3.setPing(Integer.toString(var2.ping));
         var3.setPlayers(Integer.toString(var2.numPlayers));
         var3.setMaxPlayers(Integer.toString(var2.maxPlayers));
         var3.setOpen(true);
         if (var2.tags.contains("hidden")) {
            var3.setOpen(false);
         }

         var3.setIp(var2.address);
         var3.setPort(Integer.toString(var2.port));
         var3.setMods("");
         if (!var2.tags.replace("hidden", "").replace("hosted", "").replace(";", "").isEmpty()) {
            var3.setMods(var2.tags.replace(";hosted", "").replace("hidden", ""));
         }

         var3.setHosted(var2.tags.endsWith(";hosted"));
         var3.setVersion("");
         var3.setLastUpdate(1);
         var3.setPasswordProtected(var2.passwordProtected);
         ReleaseServerQuery(var0, var1);
         LuaEventManager.triggerEvent("OnSteamServerResponded2", var0, (double)var1, var3);
      }
   }

   private static void onServerFailedToRespondCallback(String var0, int var1) {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnServerFailedToRespond(var0, var1);
      }

      LuaEventManager.triggerEvent("OnSteamServerFailedToRespond2", var0, (double)var1);
   }

   private static void onRulesRefreshComplete(String var0, int var1, String[] var2) {
      if (m_callbackInterface != null) {
         m_callbackInterface.OnSteamRulesRefreshComplete(var0, var1);
      }

      KahluaTable var3 = LuaManager.platform.newTable();

      for(int var4 = 0; var4 < var2.length; var4 += 2) {
         var3.rawset(var2[var4], var2[var4 + 1]);
      }

      LuaEventManager.triggerEvent("OnSteamRulesRefreshComplete", var0, (double)var1, var3);
   }
}
