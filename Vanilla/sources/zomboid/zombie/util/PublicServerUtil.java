package zombie.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import zombie.core.Core;
import zombie.core.ThreadGroups;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.network.GameServer;
import zombie.network.ServerOptions;

public final class PublicServerUtil {
   public static String webSite = "https://www.projectzomboid.com/server_browser/";
   private static long timestampForUpdate = 0L;
   private static long timestampForPlayerUpdate = 0L;
   private static long updateTick = 600000L;
   private static long updatePlayerTick = 300000L;
   private static int sentPlayerCount = 0;
   private static boolean isEnabled = false;

   public static void init() {
      isEnabled = false;
      if (DebugOptions.instance.Network.PublicServerUtil.Enabled.getValue()) {
         try {
            if (GameServer.bServer) {
               ServerOptions.instance.changeOption("PublicName", checkHacking(ServerOptions.instance.getOption("PublicName")));
               ServerOptions.instance.changeOption("PublicDescription", checkHacking(ServerOptions.instance.getOption("PublicDescription")));
            }

            if (GameServer.bServer && !isPublic()) {
               return;
            }

            DebugLog.log("connecting to public server list");
            URL var0 = new URL(webSite + "serverVar.php");
            URLConnection var1 = var0.openConnection();
            var1.setConnectTimeout(3000);
            var1.connect();
            InputStreamReader var2 = new InputStreamReader(var1.getInputStream());
            BufferedReader var3 = new BufferedReader(var2);
            String var4 = null;
            StringBuilder var5 = new StringBuilder();

            while((var4 = var3.readLine()) != null) {
               var5.append(var4).append('\n');
            }

            var3.close();
            String[] var6 = var5.toString().split("<br>");
            String[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               if (var10.contains("allowed") && var10.contains("true")) {
                  isEnabled = true;
               }

               if (var10.contains("updateTick")) {
                  updateTick = Long.parseLong(var10.split("=")[1].trim());
               }

               if (var10.contains("updatePlayerTick")) {
                  updatePlayerTick = Long.parseLong(var10.split("=")[1].trim());
               }

               if (var10.contains("ip")) {
                  GameServer.ip = var10.split("=")[1].trim();
               }
            }
         } catch (SocketTimeoutException var11) {
            isEnabled = false;
            DebugLog.log("timeout trying to connect to public server list");
         } catch (Exception var12) {
            isEnabled = false;
            var12.printStackTrace();
         }

      }
   }

   private static String checkHacking(String var0) {
      return var0 == null ? "" : var0.replaceAll("--", "").replaceAll("->", "").replaceAll("(?i)select union", "").replaceAll("(?i)select join", "").replaceAll("1=1", "").replaceAll("(?i)delete from", "");
   }

   public static void insertOrUpdate() {
      if (isEnabled) {
         if (isPublic()) {
            try {
               insertDatas();
            } catch (Exception var1) {
               System.out.println("Can't reach PZ.com");
            }
         }

      }
   }

   private static boolean isPublic() {
      String var0 = checkHacking(ServerOptions.instance.PublicName.getValue());
      return ServerOptions.instance.Public.getValue() && !var0.isEmpty();
   }

   public static void update() {
      if (System.currentTimeMillis() - timestampForUpdate > updateTick) {
         timestampForUpdate = System.currentTimeMillis();
         init();
         if (!isEnabled) {
            return;
         }

         if (isPublic()) {
            try {
               insertDatas();
            } catch (Exception var1) {
               System.out.println("Can't reach PZ.com");
            }
         }
      }

   }

   private static void insertDatas() throws Exception {
      if (isEnabled) {
         String var0 = "";
         String var10000;
         if (!ServerOptions.instance.PublicDescription.getValue().isEmpty()) {
            var10000 = ServerOptions.instance.PublicDescription.getValue();
            var0 = "&desc=" + var10000.replaceAll(" ", "%20");
         }

         String var1 = "";

         String var3;
         for(Iterator var2 = GameServer.ServerMods.iterator(); var2.hasNext(); var1 = var1 + var3 + ",") {
            var3 = (String)var2.next();
         }

         if (!"".equals(var1)) {
            var1 = var1.substring(0, var1.length() - 1);
            var1 = "&mods=" + var1.replaceAll(" ", "%20");
         }

         String var4 = GameServer.ip;
         if (!ServerOptions.instance.server_browser_announced_ip.getValue().isEmpty()) {
            var4 = ServerOptions.instance.server_browser_announced_ip.getValue();
         }

         timestampForUpdate = System.currentTimeMillis();
         int var5 = GameServer.getPlayerCount();
         var10000 = webSite;
         callUrl(var10000 + "write.php?name=" + ServerOptions.instance.PublicName.getValue().replaceAll(" ", "%20") + var0 + "&port=" + ServerOptions.instance.DefaultPort.getValue() + "&players=" + var5 + "&ip=" + var4 + "&open=" + (ServerOptions.instance.Open.getValue() ? "1" : "0") + "&password=" + ("".equals(ServerOptions.instance.Password.getValue()) ? "0" : "1") + "&maxPlayers=" + ServerOptions.getInstance().getMaxPlayers() + "&version=" + Core.getInstance().getVersionNumber().replaceAll(" ", "%20") + var1 + "&mac=" + getMacAddress());
         sentPlayerCount = var5;
      }
   }

   public static void updatePlayers() {
      if (System.currentTimeMillis() - timestampForPlayerUpdate > updatePlayerTick) {
         timestampForPlayerUpdate = System.currentTimeMillis();
         if (!isEnabled) {
            return;
         }

         try {
            String var0 = GameServer.ip;
            if (!ServerOptions.instance.server_browser_announced_ip.getValue().isEmpty()) {
               var0 = ServerOptions.instance.server_browser_announced_ip.getValue();
            }

            int var1 = GameServer.getPlayerCount();
            String var10000 = webSite;
            callUrl(var10000 + "updatePlayers.php?port=" + ServerOptions.instance.DefaultPort.getValue() + "&players=" + var1 + "&ip=" + var0);
            sentPlayerCount = GameServer.getPlayerCount();
         } catch (Exception var2) {
            System.out.println("Can't reach PZ.com");
         }
      }

   }

   public static void updatePlayerCountIfChanged() {
      if (isEnabled && sentPlayerCount != GameServer.getPlayerCount()) {
         updatePlayers();
      }

   }

   public static boolean isEnabled() {
      return isEnabled;
   }

   private static String getMacAddress() {
      try {
         InetAddress var0 = InetAddress.getLocalHost();
         NetworkInterface var1 = NetworkInterface.getByInetAddress(var0);
         if (var1 != null) {
            byte[] var2 = var1.getHardwareAddress();
            StringBuilder var3 = new StringBuilder();

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3.append(String.format("%02X%s", var2[var4], var4 < var2.length - 1 ? "-" : ""));
            }

            return var3.toString();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return "";
   }

   private static void callUrl(String var0) {
      (new Thread(ThreadGroups.Workers, Lambda.invoker(var0, (var0x) -> {
         try {
            URL var1 = new URL(var0x);
            URLConnection var2 = var1.openConnection();
            var2.getInputStream();
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }), "openUrl")).start();
   }
}
