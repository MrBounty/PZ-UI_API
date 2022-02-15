package zombie.network;

import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MovingObjectUpdateScheduler;
import zombie.VirtualZombieManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.raknet.UdpConnection;
import zombie.core.utils.UpdateLimit;
import zombie.core.znet.ZNetStatistics;
import zombie.debug.DebugOptions;
import zombie.iso.IsoWorld;
import zombie.popman.NetworkZombieManager;
import zombie.popman.NetworkZombieSimulator;

public class MPStatistics {
   private static final long REQUEST_TIMEOUT = 10000L;
   private static final long STATISTICS_INTERVAL = 2000L;
   private static final long PING_INTERVAL = 1000L;
   private static final long PING_PERIOD = 10000L;
   private static final KahluaTable pingTable;
   private static final KahluaTable statsTable;
   private static final UpdateLimit ulRequestTimeout;
   private static final UpdateLimit ulStatistics;
   private static final UpdateLimit ulPing;
   private static boolean serverStatisticsEnabled;
   private static int lastPing;
   private static int avgPing;
   private static int minPing;
   private static long serverMemTotal;
   private static long serverMemUsed;
   private static long serverMemFree;
   private static long serverRX;
   private static long serverTX;
   private static double serverLoss;
   private static float serverFPS;
   private static long serverNetworkingUpdates;
   private static long serverNetworkingFPS;
   private static String serverRevision;
   private static long clientMemTotal;
   private static long clientMemUsed;
   private static long clientMemFree;
   private static long clientRX;
   private static long clientTX;
   private static double clientLoss;
   private static float clientFPS;
   private static int serverZombiesTotal;
   private static int serverZombiesLoaded;
   private static int serverZombiesSimulated;
   private static int serverZombiesCulled;
   private static int serverZombiesAuthorized;
   private static int serverZombiesUnauthorized;
   private static int serverZombiesReusable;
   private static int serverZombiesUpdated;
   private static int clientZombiesTotal;
   private static int clientZombiesLoaded;
   private static int clientZombiesSimulated;
   private static int clientZombiesCulled;
   private static int clientZombiesAuthorized;
   private static int clientZombiesUnauthorized;
   private static int clientZombiesReusable;
   private static int clientZombiesUpdated;
   private static long zombieUpdates;
   private static long serverHandledMinPing;
   private static long serverHandledMaxPing;
   private static long serverHandledAvgPing;
   private static long serverHandledLastPing;
   private static long serverHandledLossPing;
   private static long serverHandledPingPeriodStart;
   private static int serverHandledPingPacketIndex;
   private static final ArrayList serverHandledPingHistory;
   private static final HashSet serverHandledLossPingHistory;

   private static void getClientZombieStatistics() {
      int var0 = (int)Math.max(MovingObjectUpdateScheduler.instance.getFrameCounter() - zombieUpdates, 1L);
      clientZombiesTotal = GameClient.IDToZombieMap.values().length;
      clientZombiesLoaded = IsoWorld.instance.getCell().getZombieList().size();
      clientZombiesSimulated = clientZombiesUpdated / var0;
      clientZombiesAuthorized = NetworkZombieSimulator.getInstance().getAuthorizedZombieCount();
      clientZombiesUnauthorized = NetworkZombieSimulator.getInstance().getUnauthorizedZombieCount();
      clientZombiesReusable = VirtualZombieManager.instance.reusableZombiesSize();
      clientZombiesCulled = 0;
      clientZombiesUpdated = 0;
      zombieUpdates = MovingObjectUpdateScheduler.instance.getFrameCounter();
      serverZombiesCulled = 0;
   }

   private static void getServerZombieStatistics() {
      int var0 = (int)Math.max(MovingObjectUpdateScheduler.instance.getFrameCounter() - zombieUpdates, 1L);
      serverZombiesTotal = ServerMap.instance.ZombieMap.size();
      serverZombiesLoaded = IsoWorld.instance.getCell().getZombieList().size();
      serverZombiesSimulated = serverZombiesUpdated / var0;
      serverZombiesAuthorized = 0;
      serverZombiesUnauthorized = NetworkZombieManager.getInstance().getUnauthorizedZombieCount();
      serverZombiesReusable = VirtualZombieManager.instance.reusableZombiesSize();
      serverZombiesCulled = 0;
      serverZombiesUpdated = 0;
      zombieUpdates = MovingObjectUpdateScheduler.instance.getFrameCounter();
   }

   private static void resetStatistic() {
      if (GameClient.bClient) {
         GameClient.connection.netStatistics = null;
      } else {
         UdpConnection var1;
         if (GameServer.bServer) {
            for(Iterator var0 = GameServer.udpEngine.connections.iterator(); var0.hasNext(); var1.netStatistics = null) {
               var1 = (UdpConnection)var0.next();
            }
         }
      }

      serverRX = 0L;
      serverTX = 0L;
      serverLoss = 0.0D;
      serverFPS = 0.0F;
      serverNetworkingFPS = 0L;
      serverMemFree = 0L;
      serverMemTotal = 0L;
      serverMemUsed = 0L;
      clientRX = 0L;
      clientTX = 0L;
      clientLoss = 0.0D;
      clientFPS = 0.0F;
      clientMemFree = 0L;
      clientMemTotal = 0L;
      clientMemUsed = 0L;
      serverZombiesTotal = 0;
      serverZombiesLoaded = 0;
      serverZombiesSimulated = 0;
      serverZombiesCulled = 0;
      serverZombiesAuthorized = 0;
      serverZombiesUnauthorized = 0;
      serverZombiesReusable = 0;
      serverZombiesUpdated = 0;
      clientZombiesTotal = 0;
      clientZombiesLoaded = 0;
      clientZombiesSimulated = 0;
      clientZombiesCulled = 0;
      clientZombiesAuthorized = 0;
      clientZombiesUnauthorized = 0;
      clientZombiesReusable = 0;
      clientZombiesUpdated = 0;
   }

   private static void getClientStatistics() {
      try {
         clientRX = 0L;
         clientTX = 0L;
         clientLoss = 0.0D;
         ZNetStatistics var0 = GameClient.connection.getStatistics();
         if (var0 != null) {
            clientRX = var0.lastActualBytesReceived / 1000L;
            clientTX = var0.lastActualBytesSent / 1000L;
            clientLoss = var0.packetlossLastSecond / 1000.0D;
         }

         clientFPS = 60.0F / GameTime.instance.FPSMultiplier;
         clientMemFree = Runtime.getRuntime().freeMemory() / 1000L / 1000L;
         clientMemTotal = Runtime.getRuntime().totalMemory() / 1000L / 1000L;
         clientMemUsed = clientMemTotal - clientMemFree;
      } catch (Exception var1) {
      }

   }

   private static void getServerStatistics() {
      try {
         serverRX = 0L;
         serverTX = 0L;
         serverLoss = 0.0D;
         Iterator var0 = GameServer.udpEngine.connections.iterator();

         while(var0.hasNext()) {
            UdpConnection var1 = (UdpConnection)var0.next();
            ZNetStatistics var2 = var1.getStatistics();
            if (var2 != null) {
               serverRX += var1.netStatistics.lastActualBytesReceived;
               serverTX += var1.netStatistics.lastActualBytesSent;
               serverLoss += var1.netStatistics.packetlossLastSecond;
            }
         }

         serverRX /= 1000L;
         serverTX /= 1000L;
         serverLoss /= 1000.0D;
         serverFPS = 60.0F / GameTime.instance.FPSMultiplier;
         serverNetworkingFPS = 1000L * serverNetworkingUpdates / 2000L;
         serverNetworkingUpdates = 0L;
         serverMemFree = Runtime.getRuntime().freeMemory() / 1000L / 1000L;
         serverMemTotal = Runtime.getRuntime().totalMemory() / 1000L / 1000L;
         serverMemUsed = serverMemTotal - serverMemFree;
      } catch (Exception var3) {
      }

   }

   private static void resetPingCounters() {
      lastPing = -1;
      avgPing = -1;
      minPing = -1;
   }

   private static void getPing(UdpConnection var0) {
      try {
         if (var0 != null) {
            lastPing = var0.getLastPing();
            avgPing = var0.getAveragePing();
            minPing = var0.getLowestPing();
         }
      } catch (Exception var2) {
      }

   }

   private static void resetServerHandledPingCounters() {
      serverHandledMinPing = 0L;
      serverHandledMaxPing = 0L;
      serverHandledAvgPing = 0L;
      serverHandledLastPing = 0L;
      serverHandledLossPing = 0L;
      serverHandledPingPeriodStart = 0L;
      serverHandledPingPacketIndex = 0;
      serverHandledPingHistory.clear();
      serverHandledLossPingHistory.clear();
   }

   private static void getServerHandledPing() {
      long var0 = System.currentTimeMillis();
      if ((long)serverHandledPingPacketIndex == 10L) {
         serverHandledMinPing = serverHandledPingHistory.stream().mapToLong((var0x) -> {
            return var0x;
         }).min().orElse(0L);
         serverHandledMaxPing = serverHandledPingHistory.stream().mapToLong((var0x) -> {
            return var0x;
         }).max().orElse(0L);
         serverHandledAvgPing = (long)serverHandledPingHistory.stream().mapToLong((var0x) -> {
            return var0x;
         }).average().orElse(0.0D);
         serverHandledPingHistory.clear();
         serverHandledPingPacketIndex = 0;
         int var2 = serverHandledLossPingHistory.size();
         serverHandledLossPingHistory.removeIf((var2x) -> {
            return var0 > var2x + 10000L;
         });
         serverHandledLossPing += (long)(var2 - serverHandledLossPingHistory.size());
         serverHandledPingPeriodStart = var0;
      }

      GameClient.sendServerPing(var0);
      if (serverHandledLossPingHistory.size() > 1000) {
         serverHandledLossPingHistory.clear();
      }

      serverHandledLossPingHistory.add(var0);
      ++serverHandledPingPacketIndex;
   }

   public static void countServerNetworkingFPS() {
      ++serverNetworkingUpdates;
   }

   public static void Reset() {
      resetPingCounters();
      resetServerHandledPingCounters();
      resetStatistic();
   }

   public static void Update() {
      if (GameClient.bClient) {
         if (ulPing.Check()) {
            if (!IsoPlayer.getInstance().isShowMPInfos() && !DebugOptions.instance.MultiplayerPing.getValue()) {
               resetPingCounters();
               resetServerHandledPingCounters();
            } else {
               getPing(GameClient.connection);
               if (IsoPlayer.getInstance().isShowMPInfos()) {
                  getServerHandledPing();
               } else {
                  resetServerHandledPingCounters();
               }
            }
         }

         if (IsoPlayer.getInstance().isShowMPInfos()) {
            if (ulStatistics.Check()) {
               getClientStatistics();
               getClientZombieStatistics();
            }
         } else {
            resetStatistic();
         }
      } else if (GameServer.bServer) {
         if (ulRequestTimeout.Check()) {
            serverStatisticsEnabled = false;
         }

         if (serverStatisticsEnabled) {
            if (ulStatistics.Check()) {
               getServerStatistics();
               getServerZombieStatistics();
            }
         } else {
            resetStatistic();
         }
      }

   }

   public static void requested() {
      serverStatisticsEnabled = true;
      ulRequestTimeout.Reset(10000L);
   }

   public static void clientZombieCulled() {
      ++clientZombiesCulled;
   }

   public static void serverZombieCulled() {
      ++serverZombiesCulled;
   }

   public static void clientZombieUpdated() {
      ++clientZombiesUpdated;
   }

   public static void serverZombieUpdated() {
      ++serverZombiesUpdated;
   }

   public static void write(UdpConnection var0, ByteBuffer var1) {
      var1.putLong(serverMemFree);
      var1.putLong(serverMemTotal);
      var1.putLong(serverMemUsed);
      var1.putLong(serverRX);
      var1.putLong(serverTX);
      var1.putDouble(serverLoss);
      var1.putFloat(serverFPS);
      var1.putLong(serverNetworkingFPS);
      var1.putInt(serverZombiesTotal);
      var1.putInt(serverZombiesLoaded);
      var1.putInt(serverZombiesSimulated);
      var1.putInt(serverZombiesCulled);
      var1.putInt(NetworkZombieManager.getInstance().getAuthorizedZombieCount(var0));
      var1.putInt(serverZombiesUnauthorized);
      var1.putInt(serverZombiesReusable);
      GameWindow.WriteString(var1, "");
   }

   public static void parse(ByteBuffer var0) {
      long var1 = System.currentTimeMillis();
      long var3 = var0.getLong();
      serverMemFree = var0.getLong();
      serverMemTotal = var0.getLong();
      serverMemUsed = var0.getLong();
      serverRX = var0.getLong();
      serverTX = var0.getLong();
      serverLoss = var0.getDouble();
      serverFPS = var0.getFloat();
      serverNetworkingFPS = var0.getLong();
      serverZombiesTotal = var0.getInt();
      serverZombiesLoaded = var0.getInt();
      serverZombiesSimulated = var0.getInt();
      serverZombiesCulled += var0.getInt();
      serverZombiesAuthorized = var0.getInt();
      serverZombiesUnauthorized = var0.getInt();
      serverZombiesReusable = var0.getInt();
      serverRevision = GameWindow.ReadString(var0);
      serverHandledLossPingHistory.remove(var3);
      if (var3 >= serverHandledPingPeriodStart) {
         serverHandledLastPing = var1 - var3;
         serverHandledPingHistory.add(serverHandledLastPing);
      }

   }

   public static KahluaTable getLuaPing() {
      pingTable.wipe();
      if (GameClient.bClient) {
         pingTable.rawset("enabled", DebugOptions.instance.MultiplayerPing.getValue());
         pingTable.rawset("lastPing", String.valueOf(lastPing));
         pingTable.rawset("avgPing", String.valueOf(avgPing));
         pingTable.rawset("minPing", String.valueOf(minPing));
      }

      return pingTable;
   }

   public static KahluaTable getLuaStatistics() {
      statsTable.wipe();
      if (GameClient.bClient) {
         statsTable.rawset("clientTime", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
         statsTable.rawset("serverTime", NumberFormat.getNumberInstance().format(TimeUnit.NANOSECONDS.toSeconds(GameTime.getServerTime())));
         statsTable.rawset("clientRevision", String.valueOf(serverRevision));
         statsTable.rawset("serverRevision", String.valueOf(""));
         statsTable.rawset("clientRX", String.valueOf(clientRX));
         statsTable.rawset("clientTX", String.valueOf(clientTX));
         statsTable.rawset("clientLoss", String.valueOf((int)clientLoss));
         statsTable.rawset("serverRX", String.valueOf(serverRX));
         statsTable.rawset("serverTX", String.valueOf(serverTX));
         statsTable.rawset("serverLoss", String.valueOf((int)serverLoss));
         statsTable.rawset("serverPingLast", String.valueOf(serverHandledLastPing));
         statsTable.rawset("serverPingMin", String.valueOf(serverHandledMinPing));
         statsTable.rawset("serverPingAvg", String.valueOf(serverHandledAvgPing));
         statsTable.rawset("serverPingMax", String.valueOf(serverHandledMaxPing));
         statsTable.rawset("serverPingLoss", String.valueOf(serverHandledLossPing));
         statsTable.rawset("clientMemTotal", String.valueOf(clientMemTotal));
         statsTable.rawset("clientMemUsed", String.valueOf(clientMemUsed));
         statsTable.rawset("clientMemFree", String.valueOf(clientMemFree));
         statsTable.rawset("serverMemTotal", String.valueOf(serverMemTotal));
         statsTable.rawset("serverMemUsed", String.valueOf(serverMemUsed));
         statsTable.rawset("serverMemFree", String.valueOf(serverMemFree));
         statsTable.rawset("serverNetworkingFPS", String.valueOf((int)serverNetworkingFPS));
         statsTable.rawset("serverFPS", String.valueOf((int)serverFPS));
         statsTable.rawset("clientFPS", String.valueOf((int)clientFPS));
         statsTable.rawset("serverZombiesTotal", String.valueOf(serverZombiesTotal));
         statsTable.rawset("serverZombiesLoaded", String.valueOf(serverZombiesLoaded));
         statsTable.rawset("serverZombiesSimulated", String.valueOf(serverZombiesSimulated));
         statsTable.rawset("serverZombiesCulled", String.valueOf(serverZombiesCulled));
         statsTable.rawset("serverZombiesAuthorized", String.valueOf(serverZombiesAuthorized));
         statsTable.rawset("serverZombiesUnauthorized", String.valueOf(serverZombiesUnauthorized));
         statsTable.rawset("serverZombiesReusable", String.valueOf(serverZombiesReusable));
         statsTable.rawset("clientZombiesTotal", String.valueOf(clientZombiesTotal));
         statsTable.rawset("clientZombiesLoaded", String.valueOf(clientZombiesLoaded));
         statsTable.rawset("clientZombiesSimulated", String.valueOf(clientZombiesSimulated));
         statsTable.rawset("clientZombiesCulled", String.valueOf(clientZombiesCulled));
         statsTable.rawset("clientZombiesAuthorized", String.valueOf(clientZombiesAuthorized));
         statsTable.rawset("clientZombiesUnauthorized", String.valueOf(clientZombiesUnauthorized));
         statsTable.rawset("clientZombiesReusable", String.valueOf(clientZombiesReusable));
      }

      return statsTable;
   }

   static {
      pingTable = LuaManager.platform.newTable();
      statsTable = LuaManager.platform.newTable();
      ulRequestTimeout = new UpdateLimit(10000L);
      ulStatistics = new UpdateLimit(2000L);
      ulPing = new UpdateLimit(1000L);
      serverStatisticsEnabled = false;
      lastPing = -1;
      avgPing = -1;
      minPing = -1;
      serverMemTotal = 0L;
      serverMemUsed = 0L;
      serverMemFree = 0L;
      serverRX = 0L;
      serverTX = 0L;
      serverLoss = 0.0D;
      serverFPS = 0.0F;
      serverNetworkingUpdates = 0L;
      serverNetworkingFPS = 0L;
      serverRevision = "";
      clientMemTotal = 0L;
      clientMemUsed = 0L;
      clientMemFree = 0L;
      clientRX = 0L;
      clientTX = 0L;
      clientLoss = 0.0D;
      clientFPS = 0.0F;
      serverZombiesTotal = 0;
      serverZombiesLoaded = 0;
      serverZombiesSimulated = 0;
      serverZombiesCulled = 0;
      serverZombiesAuthorized = 0;
      serverZombiesUnauthorized = 0;
      serverZombiesReusable = 0;
      serverZombiesUpdated = 0;
      clientZombiesTotal = 0;
      clientZombiesLoaded = 0;
      clientZombiesSimulated = 0;
      clientZombiesCulled = 0;
      clientZombiesAuthorized = 0;
      clientZombiesUnauthorized = 0;
      clientZombiesReusable = 0;
      clientZombiesUpdated = 0;
      zombieUpdates = 0L;
      serverHandledMinPing = 0L;
      serverHandledMaxPing = 0L;
      serverHandledAvgPing = 0L;
      serverHandledLastPing = 0L;
      serverHandledLossPing = 0L;
      serverHandledPingPeriodStart = 0L;
      serverHandledPingPacketIndex = 0;
      serverHandledPingHistory = new ArrayList();
      serverHandledLossPingHistory = new HashSet();
   }
}
