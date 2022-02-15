package zombie.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;

public class MPStatistic {
   public static MPStatistic instance;
   private static final boolean doPrintStatistic = false;
   private static final boolean doCSVStatistic = true;
   private static int Period = 0;
   public MPStatistic.TasksStatistic LoaderThreadTasks = new MPStatistic.TasksStatistic();
   public MPStatistic.TasksStatistic RecalcThreadTasks = new MPStatistic.TasksStatistic();
   public MPStatistic.SaveTasksStatistic SaveTasks = new MPStatistic.SaveTasksStatistic();
   public MPStatistic.ServerCellStatistic ServerMapToLoad = new MPStatistic.ServerCellStatistic();
   public MPStatistic.ServerCellStatistic ServerMapLoadedCells = new MPStatistic.ServerCellStatistic();
   public MPStatistic.ServerCellStatistic ServerMapLoaded2 = new MPStatistic.ServerCellStatistic();
   private int countServerChunkThreadSaveNow = 0;
   public MPStatistic.MainThreadStatistic Main = new MPStatistic.MainThreadStatistic();
   public MPStatistic.ThreadStatistic ServerLOS = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic LoaderThread = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic RecalcAllThread = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic SaveThread = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic PolyPathThread = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic WorldReuser = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic PlayerDownloadServer = new MPStatistic.ThreadStatistic();
   public MPStatistic.ThreadStatistic MapCollisionThread = new MPStatistic.ThreadStatistic();
   public MPStatistic.ProbeStatistic ChunkChecksum = new MPStatistic.ProbeStatistic();
   public MPStatistic.ProbeStatistic Bullet = new MPStatistic.ProbeStatistic();
   public MPStatistic.ProbeStatistic AnimationPlayerUpdate = new MPStatistic.ProbeStatistic();
   public MPStatistic.ProbeStatistic ServerMapPreupdate = new MPStatistic.ProbeStatistic();
   public MPStatistic.ProbeStatistic ServerMapPostupdate = new MPStatistic.ProbeStatistic();
   public MPStatistic.ProbeStatistic IngameStateUpdate = new MPStatistic.ProbeStatistic();
   private long packetLength = 0L;
   private int countIncomePackets = 0;
   private int countOutcomePackets = 0;
   private int countIncomeBytes = 0;
   private int countOutcomeBytes = 0;
   private int maxIncomeBytesPerSecond = 0;
   private int maxOutcomeBytesPerSecond = 0;
   private int currentIncomeBytesPerSecond = 0;
   private int currentOutcomeBytesPerSecond = 0;
   private long lastCalculateBPS = 0L;
   private long lastReport = 0L;
   private long minUpdatePeriod = 9999L;
   private long maxUpdatePeriod = 0L;
   private long avgUpdatePeriod = 0L;
   private long currentAvgUpdatePeriod = 0L;
   private long teleports = 0L;
   private long counter1 = 0L;
   private long counter2 = 0L;
   private long counter3 = 0L;
   private long updatePeriods = 0L;
   private int loadCellFromDisk = 0;
   private int saveCellToDisk = 0;
   public static boolean clientStatisticEnable = false;
   private PrintStream csvStatisticFile = null;
   private PrintStream csvIncomePacketsFile = null;
   private PrintStream csvIncomeBytesFile = null;
   private PrintStream csvOutcomePacketsFile = null;
   private PrintStream csvOutcomeBytesFile = null;
   private PrintStream csvConnectionsFile = null;
   private ArrayList csvConnections = new ArrayList();
   private KahluaTable table = null;

   public MPStatistic() {
      if (GameServer.bServer) {
         this.openCSVStatistic();
      }

   }

   public static MPStatistic getInstance() {
      if (instance == null) {
         instance = new MPStatistic();
      }

      return instance;
   }

   public void IncrementServerChunkThreadSaveNow() {
      ++this.countServerChunkThreadSaveNow;
   }

   public void teleport() {
      ++this.teleports;
   }

   public void count1(long var1) {
      this.counter1 += var1;
   }

   public void count2(long var1) {
      this.counter2 += var1;
   }

   public void count3(long var1) {
      this.counter3 += var1;
   }

   public void write(ByteBufferWriter var1) {
      var1.putLong(this.minUpdatePeriod);
      var1.putLong(this.maxUpdatePeriod);
      var1.putLong(this.currentAvgUpdatePeriod / this.updatePeriods);
      var1.putLong(this.updatePeriods / (long)Period);
      var1.putLong(this.teleports);
      var1.putLong((long)GameServer.udpEngine.connections.size());
      var1.putLong(this.counter1 / this.updatePeriods);
      var1.putLong(this.counter2 / this.updatePeriods);
      var1.putLong(this.counter3 / this.updatePeriods);
   }

   public void setPacketsLength(long var1) {
      this.packetLength = var1;
   }

   public void addIncomePacket(short var1, int var2) {
      PacketTypes.PacketType var3 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var1);
      if (var3 != null) {
         ++var3.incomePackets;
         ++this.countIncomePackets;
         var3.incomeBytes += var2;
         this.countIncomeBytes += var2;
         this.currentIncomeBytesPerSecond += var2;
         this.calculateMaxBPS();
      }

   }

   public void addOutcomePacket(short var1, int var2) {
      PacketTypes.PacketType var3 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var1);
      if (var3 != null) {
         ++var3.outcomePackets;
         ++this.countOutcomePackets;
         var3.outcomeBytes += var2;
         this.countOutcomeBytes += var2;
         this.currentOutcomeBytesPerSecond += var2;
         this.calculateMaxBPS();
      }

   }

   void calculateMaxBPS() {
      if (System.currentTimeMillis() - this.lastCalculateBPS > 1000L) {
         this.lastCalculateBPS = System.currentTimeMillis();
         if (this.currentIncomeBytesPerSecond > this.maxIncomeBytesPerSecond) {
            this.maxIncomeBytesPerSecond = this.currentIncomeBytesPerSecond;
         }

         if (this.currentOutcomeBytesPerSecond > this.maxOutcomeBytesPerSecond) {
            this.maxOutcomeBytesPerSecond = this.currentOutcomeBytesPerSecond;
         }

         this.currentIncomeBytesPerSecond = 0;
         this.currentOutcomeBytesPerSecond = 0;
      }

   }

   public void IncrementLoadCellFromDisk() {
      ++this.loadCellFromDisk;
   }

   public void IncrementSaveCellToDisk() {
      ++this.saveCellToDisk;
   }

   public void process(long var1) {
      if (var1 > this.maxUpdatePeriod) {
         this.maxUpdatePeriod = var1;
      }

      if (var1 < this.minUpdatePeriod) {
         this.minUpdatePeriod = var1;
      }

      this.avgUpdatePeriod = (long)((float)this.avgUpdatePeriod + (float)(var1 - this.avgUpdatePeriod) * 0.05F);
      this.currentAvgUpdatePeriod += var1;
      ++this.updatePeriods;
      if (Period != 0 && System.currentTimeMillis() - this.lastReport >= (long)(Period * 1000)) {
         this.lastReport = System.currentTimeMillis();
         this.printStatistic();
         this.printCSVStatistic();
         GameServer.sendShortStatistic();
         this.table = LuaManager.platform.newTable();
         this.table.rawset("lastReport", (double)this.lastReport);
         this.table.rawset("period", (double)Period);
         this.table.rawset("minUpdatePeriod", (double)this.minUpdatePeriod);
         this.table.rawset("maxUpdatePeriod", (double)this.maxUpdatePeriod);
         this.table.rawset("avgUpdatePeriod", (double)this.avgUpdatePeriod);
         this.maxUpdatePeriod = 0L;
         this.minUpdatePeriod = 9999L;
         this.currentAvgUpdatePeriod = 0L;
         this.updatePeriods = 0L;
         this.teleports = 0L;
         this.counter1 = 0L;
         this.counter2 = 0L;
         this.counter3 = 0L;
         this.table.rawset("loadCellFromDisk", (double)this.loadCellFromDisk);
         this.table.rawset("saveCellToDisk", (double)this.saveCellToDisk);
         this.loadCellFromDisk = 0;
         this.saveCellToDisk = 0;
         this.table.rawset("usedMemory", (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
         this.table.rawset("totalMemory", (double)Runtime.getRuntime().totalMemory());
         this.table.rawset("freeMemory", (double)Runtime.getRuntime().freeMemory());
         this.table.rawset("countConnections", (double)GameServer.udpEngine.connections.size());
         KahluaTable var3 = LuaManager.platform.newTable();

         KahluaTable var9;
         KahluaTable var18;
         for(int var4 = 0; var4 < GameServer.udpEngine.connections.size(); ++var4) {
            KahluaTable var5 = LuaManager.platform.newTable();
            UdpConnection var6 = (UdpConnection)GameServer.udpEngine.connections.get(var4);
            var5.rawset("ip", var6.ip);
            var5.rawset("username", var6.username);
            var5.rawset("accessLevel", var6.accessLevel);
            KahluaTable var7 = LuaManager.platform.newTable();

            for(int var8 = 0; var8 < var6.players.length; ++var8) {
               if (var6.players[var8] != null) {
                  var9 = LuaManager.platform.newTable();
                  var9.rawset("username", var6.players[var8].username);
                  var9.rawset("x", (double)var6.players[var8].x);
                  var9.rawset("y", (double)var6.players[var8].y);
                  var9.rawset("z", (double)var6.players[var8].z);
                  var7.rawset(var8, var9);
               }
            }

            var5.rawset("users", var7);
            var5.rawset("diff", (double)var6.statistic.diff);
            var5.rawset("pingAVG", (double)var6.statistic.pingAVG);
            var5.rawset("remotePlayersCount", (double)var6.statistic.remotePlayersCount);
            var5.rawset("remotePlayersDesyncAVG", (double)var6.statistic.remotePlayersDesyncAVG);
            var5.rawset("remotePlayersDesyncMax", (double)var6.statistic.remotePlayersDesyncMax);
            var5.rawset("remotePlayersTeleports", (double)var6.statistic.remotePlayersTeleports);
            var5.rawset("zombiesCount", (double)var6.statistic.zombiesCount);
            var5.rawset("zombiesLocalOwnership", (double)var6.statistic.zombiesLocalOwnership);
            var5.rawset("zombiesDesyncAVG", (double)var6.statistic.zombiesDesyncAVG);
            var5.rawset("zombiesDesyncMax", (double)var6.statistic.zombiesDesyncMax);
            var5.rawset("zombiesTeleports", (double)var6.statistic.zombiesTeleports);
            var5.rawset("FPS", (double)var6.statistic.FPS);
            var5.rawset("FPSMin", (double)var6.statistic.FPSMin);
            var5.rawset("FPSAvg", (double)var6.statistic.FPSAvg);
            var5.rawset("FPSMax", (double)var6.statistic.FPSMax);
            var18 = LuaManager.platform.newTable();
            short var19 = 0;

            for(int var10 = 0; var10 < 32; ++var10) {
               var18.rawset(var10, (double)var6.statistic.FPSHistogramm[var10]);
               if (var19 < var6.statistic.FPSHistogramm[var10]) {
                  var19 = var6.statistic.FPSHistogramm[var10];
               }
            }

            var5.rawset("FPSHistogram", var18);
            var5.rawset("FPSHistogramMax", (double)var19);
            var3.rawset(var4, var5);
         }

         this.table.rawset("connections", var3);
         this.table.rawset("packetLength", (double)this.packetLength);
         this.table.rawset("countIncomePackets", (double)this.countIncomePackets);
         this.table.rawset("countIncomeBytes", (double)this.countIncomeBytes);
         this.table.rawset("maxIncomeBytesPerSecound", (double)this.maxIncomeBytesPerSecond);
         KahluaTable var11 = LuaManager.platform.newTable();
         byte var12 = -1;

         PacketTypes.PacketType var16;
         for(Iterator var14 = PacketTypes.packetTypes.values().iterator(); var14.hasNext(); var16.incomeBytes = 0) {
            var16 = (PacketTypes.PacketType)var14.next();
            if (var16.incomePackets > 0) {
               var18 = LuaManager.platform.newTable();
               var18.rawset("name", var16.name());
               var18.rawset("count", (double)var16.incomePackets);
               var18.rawset("bytes", (double)var16.incomeBytes);
               var11.rawset(var12, var18);
            }

            var16.incomePackets = 0;
         }

         this.table.rawset("incomePacketsTable", var11);
         this.countIncomePackets = 0;
         this.countIncomeBytes = 0;
         this.maxIncomeBytesPerSecond = 0;
         this.table.rawset("countOutcomePackets", (double)this.countOutcomePackets);
         this.table.rawset("countOutcomeBytes", (double)this.countOutcomeBytes);
         this.table.rawset("maxOutcomeBytesPerSecound", (double)this.maxOutcomeBytesPerSecond);
         KahluaTable var15 = LuaManager.platform.newTable();
         int var13 = -1;

         PacketTypes.PacketType var20;
         for(Iterator var17 = PacketTypes.packetTypes.values().iterator(); var17.hasNext(); var20.outcomeBytes = 0) {
            var20 = (PacketTypes.PacketType)var17.next();
            if (var20.outcomePackets > 0) {
               var9 = LuaManager.platform.newTable();
               var9.rawset("name", var20.name());
               var9.rawset("count", (double)var20.outcomePackets);
               var9.rawset("bytes", (double)var20.outcomeBytes);
               var15.rawset(var13++, var9);
            }

            var20.outcomePackets = 0;
         }

         this.table.rawset("outcomePacketsTable", var15);
         this.countOutcomePackets = 0;
         this.countOutcomeBytes = 0;
         this.maxOutcomeBytesPerSecond = 0;
         this.LoaderThreadTasks.Clear();
         this.RecalcThreadTasks.Clear();
         this.SaveTasks.Clear();
         this.ServerMapToLoad.Clear();
         this.ServerMapLoadedCells.Clear();
         this.ServerMapLoaded2.Clear();
         this.countServerChunkThreadSaveNow = 0;
         this.Main.Clear();
         this.ServerLOS.Clear();
         this.LoaderThread.Clear();
         this.RecalcAllThread.Clear();
         this.SaveThread.Clear();
         this.PolyPathThread.Clear();
         this.WorldReuser.Clear();
         this.PlayerDownloadServer.Clear();
         this.MapCollisionThread.Clear();
         this.ChunkChecksum.Clear();
         this.Bullet.Clear();
         this.AnimationPlayerUpdate.Clear();
         this.ServerMapPreupdate.Clear();
         this.ServerMapPostupdate.Clear();
         this.IngameStateUpdate.Clear();
         GameServer.getStatisticFromClients();
         GameServer.sendStatistic();
      }
   }

   private void printStatistic() {
   }

   public static String getStatisticDir() {
      String var0 = ZomboidFileSystem.instance.getCacheDirSub("Statistic");
      ZomboidFileSystem.ensureFolderExists(var0);
      File var1 = new File(var0);
      return var1.getAbsolutePath();
   }

   private void openCSVStatistic() {
      String var1 = getStatisticDir();

      try {
         File var2 = new File(var1 + File.separator + "Statistic.csv");

         try {
            this.csvStatisticFile = new PrintStream(var2);
         } catch (FileNotFoundException var10) {
            try {
               var2 = new File(var1 + File.separator + "Statistic_new.csv");
               this.csvStatisticFile = new PrintStream(var2);
            } catch (FileNotFoundException var9) {
               DebugLog.Statistic.error("The Statistic.csv was not open");
               var9.printStackTrace();
            }
         }

         PrintStream var10000 = this.csvStatisticFile;
         String var10001 = this.Main.PrintTitle("MainThread");
         var10000.println("lastReport; minUpdatePeriod; maxUpdatePeriod; avgUpdatePeriod; loadCellFromDisk; saveCellToDisk; countLoaderThreadTasksAdded; countLoaderThreadTasksProcessed; countRecalcThreadTasksAdded; countRecalcThreadTasksProcessed; countSaveUnloadedTasksAdded; countSaveLoadedTasksAdded; countSaveGameTimeTasksAdded; countQuitThreadTasksAdded; countSaveThreadTasksProcessed; countServerMapToLoadAdded; countServerMapToLoadCanceled; countServerMapLoadedCellsAdded; countServerMapLoadedCellsCanceled; countServerMapLoaded2Added; countServerMapLoaded2Canceled; countServerChunkThreadSaveNow; " + var10001 + this.ServerLOS.PrintTitle("ServerLOS") + this.LoaderThread.PrintTitle("LoaderThread") + this.RecalcAllThread.PrintTitle("RecalcAllThread") + this.SaveThread.PrintTitle("SaveThread") + this.PolyPathThread.PrintTitle("PolyPathThread") + this.WorldReuser.PrintTitle("WorldReuser") + this.PlayerDownloadServer.PrintTitle("WorldReuser") + this.MapCollisionThread.PrintTitle("MapCollisionThread") + this.ChunkChecksum.PrintTitle("ChunkChecksum") + this.Bullet.PrintTitle("Bullet") + this.AnimationPlayerUpdate.PrintTitle("AnimationPlayerUpdate") + this.ServerMapPreupdate.PrintTitle("ServerMapPreupdate") + this.ServerMapPostupdate.PrintTitle("ServerMapPostupdate") + this.IngameStateUpdate.PrintTitle("IngameStateUpdate") + " totalMemory; freeMemory; countConnections; packetLength; countIncomePackets; countIncomeBytes; maxIncomeBytesPerSecound; countOutcomePackets; countOutcomeBytes; maxOutcomeBytesPerSecound");
         File var3 = new File(var1 + File.separator + "IncomePackets.csv");
         this.csvIncomePacketsFile = new PrintStream(var3);
         Iterator var4 = PacketTypes.packetTypes.values().iterator();

         while(var4.hasNext()) {
            PacketTypes.PacketType var5 = (PacketTypes.PacketType)var4.next();
            var10000 = this.csvIncomePacketsFile;
            var10001 = var5.name();
            var10000.print(var10001 + "(" + var5.getId() + "); ");
         }

         this.csvIncomePacketsFile.println();
         File var12 = new File(var1 + File.separator + "IncomeBytes.csv");
         this.csvIncomeBytesFile = new PrintStream(var12);
         Iterator var13 = PacketTypes.packetTypes.values().iterator();

         while(var13.hasNext()) {
            PacketTypes.PacketType var6 = (PacketTypes.PacketType)var13.next();
            var10000 = this.csvIncomeBytesFile;
            var10001 = var6.name();
            var10000.print(var10001 + "(" + var6.getId() + "); ");
         }

         this.csvIncomeBytesFile.println();
         File var14 = new File(var1 + File.separator + "OutcomePackets.csv");
         this.csvOutcomePacketsFile = new PrintStream(var14);
         Iterator var15 = PacketTypes.packetTypes.values().iterator();

         while(var15.hasNext()) {
            PacketTypes.PacketType var7 = (PacketTypes.PacketType)var15.next();
            var10000 = this.csvOutcomePacketsFile;
            var10001 = var7.name();
            var10000.print(var10001 + "(" + var7.getId() + "); ");
         }

         this.csvOutcomePacketsFile.println();
         File var16 = new File(var1 + File.separator + "OutcomeBytes.csv");
         this.csvOutcomeBytesFile = new PrintStream(var16);
         Iterator var17 = PacketTypes.packetTypes.values().iterator();

         while(var17.hasNext()) {
            PacketTypes.PacketType var8 = (PacketTypes.PacketType)var17.next();
            var10000 = this.csvOutcomeBytesFile;
            var10001 = var8.name();
            var10000.print(var10001 + "(" + var8.getId() + "); ");
         }

         this.csvOutcomeBytesFile.println();
         File var18 = new File(var1 + File.separator + "Connections.csv");
         this.csvConnectionsFile = new PrintStream(var18);
         this.csvConnectionsFile.print("ip; ");
         this.csvConnectionsFile.print("username; ");
         this.csvConnectionsFile.print("accessLevel; ");
         this.csvConnectionsFile.print("players.length; ");
         this.csvConnectionsFile.print("ping; ");
         this.csvConnectionsFile.print("pingAVG; ");
         this.csvConnectionsFile.print("remotePlayersCount; ");
         this.csvConnectionsFile.print("remotePlayersDesyncAVG; ");
         this.csvConnectionsFile.print("remotePlayersDesyncMax; ");
         this.csvConnectionsFile.print("remotePlayersTeleports; ");
         this.csvConnectionsFile.print("zombiesCount; ");
         this.csvConnectionsFile.print("zombiesLocalOwnership; ");
         this.csvConnectionsFile.print("zombiesDesyncAVG; ");
         this.csvConnectionsFile.print("zombiesDesyncMax; ");
         this.csvConnectionsFile.println("zombiesTeleports; ");
         this.csvConnectionsFile.print("FPS; ");
         this.csvConnectionsFile.print("FPSMin; ");
         this.csvConnectionsFile.print("FPSAvg; ");
         this.csvConnectionsFile.print("FPSMax; ");

         for(int var19 = 0; var19 < 32; ++var19) {
            this.csvConnectionsFile.print("FPSHistogramm[" + var19 + "]; ");
         }

         this.csvConnectionsFile.println();
      } catch (FileNotFoundException var11) {
         var11.printStackTrace();
         this.csvStatisticFile = null;
      }

   }

   private void printCSVStatistic() {
      try {
         if (this.csvStatisticFile != null) {
            this.csvStatisticFile.print(System.currentTimeMillis() + ";");
            this.csvStatisticFile.print(this.minUpdatePeriod + ";");
            this.csvStatisticFile.print(this.maxUpdatePeriod + ";");
            this.csvStatisticFile.print(this.avgUpdatePeriod + ";");
            this.csvStatisticFile.print(this.loadCellFromDisk + ";");
            this.csvStatisticFile.print(this.saveCellToDisk + ";");
            this.csvStatisticFile.print(this.LoaderThreadTasks.Print());
            this.csvStatisticFile.print(this.RecalcThreadTasks.Print());
            this.csvStatisticFile.print(this.SaveTasks.Print());
            this.csvStatisticFile.print(this.ServerMapToLoad.Print());
            this.csvStatisticFile.print(this.ServerMapLoadedCells.Print());
            this.csvStatisticFile.print(this.ServerMapLoaded2.Print());
            this.csvStatisticFile.print(this.countServerChunkThreadSaveNow + ";");
            this.csvStatisticFile.print(this.Main.Print());
            this.csvStatisticFile.print(this.ServerLOS.Print());
            this.csvStatisticFile.print(this.LoaderThread.Print());
            this.csvStatisticFile.print(this.RecalcAllThread.Print());
            this.csvStatisticFile.print(this.SaveThread.Print());
            this.csvStatisticFile.print(this.PolyPathThread.Print());
            this.csvStatisticFile.print(this.WorldReuser.Print());
            this.csvStatisticFile.print(this.PlayerDownloadServer.Print());
            this.csvStatisticFile.print(this.MapCollisionThread.Print());
            this.csvStatisticFile.print(this.ChunkChecksum.Print());
            this.csvStatisticFile.print(this.Bullet.Print());
            this.csvStatisticFile.print(this.AnimationPlayerUpdate.Print());
            this.csvStatisticFile.print(this.ServerMapPreupdate.Print());
            this.csvStatisticFile.print(this.ServerMapPostupdate.Print());
            this.csvStatisticFile.print(this.IngameStateUpdate.Print());
            this.csvStatisticFile.print(Runtime.getRuntime().totalMemory() + ";");
            this.csvStatisticFile.print(Runtime.getRuntime().freeMemory() + ";");
            this.csvStatisticFile.print(GameServer.udpEngine.connections.size() + ";");
            this.csvStatisticFile.print(this.packetLength + ";");
            this.csvStatisticFile.print(this.countIncomePackets + ";");
            this.csvStatisticFile.print(this.countIncomeBytes + ";");
            this.csvStatisticFile.print(this.maxIncomeBytesPerSecond + ";");
            this.csvStatisticFile.print(this.countOutcomePackets + ";");
            this.csvStatisticFile.print(this.countOutcomeBytes + ";");
            this.csvStatisticFile.println(this.maxOutcomeBytesPerSecond + ";");

            int var1;
            UdpConnection var2;
            for(var1 = 0; var1 < GameServer.udpEngine.connections.size(); ++var1) {
               var2 = (UdpConnection)GameServer.udpEngine.connections.get(var1);

               try {
                  if (var2 != null && var2.username != null && !this.csvConnections.contains(var2.username.hashCode())) {
                     this.csvConnections.add(var2.username.hashCode());
                  }
               } catch (NullPointerException var5) {
                  var5.printStackTrace();
                  return;
               }
            }

            for(var1 = 0; var1 < this.csvConnections.size(); ++var1) {
               var2 = null;

               int var3;
               for(var3 = 0; var3 < GameServer.udpEngine.connections.size(); ++var3) {
                  UdpConnection var4 = (UdpConnection)GameServer.udpEngine.connections.get(var3);
                  if (var4 != null && var4.username != null && var4.username.hashCode() == (Integer)this.csvConnections.get(var1)) {
                     var2 = var4;
                  }
               }

               if (var2 != null) {
                  this.csvConnectionsFile.print(var2.ip + "; ");
                  this.csvConnectionsFile.print(var2.username + "; ");
                  this.csvConnectionsFile.print(var2.accessLevel + "; ");
                  this.csvConnectionsFile.print(var2.players.length + "; ");
                  this.csvConnectionsFile.print(var2.statistic.diff / 2 + "; ");
                  this.csvConnectionsFile.print(var2.statistic.pingAVG + "; ");
                  this.csvConnectionsFile.print(var2.statistic.remotePlayersCount + "; ");
                  this.csvConnectionsFile.print(var2.statistic.remotePlayersDesyncAVG + "; ");
                  this.csvConnectionsFile.print(var2.statistic.remotePlayersDesyncMax + "; ");
                  this.csvConnectionsFile.print(var2.statistic.remotePlayersTeleports + "; ");
                  this.csvConnectionsFile.print(var2.statistic.zombiesCount + "; ");
                  this.csvConnectionsFile.print(var2.statistic.zombiesLocalOwnership + "; ");
                  this.csvConnectionsFile.print(var2.statistic.zombiesDesyncAVG + "; ");
                  this.csvConnectionsFile.print(var2.statistic.zombiesDesyncMax + "; ");
                  this.csvConnectionsFile.println(var2.statistic.zombiesTeleports + "; ");
                  this.csvConnectionsFile.print(var2.statistic.zombiesTeleports + "; ");
                  this.csvConnectionsFile.print(var2.statistic.FPS + "; ");
                  this.csvConnectionsFile.print(var2.statistic.FPSMin + "; ");
                  this.csvConnectionsFile.print(var2.statistic.FPSAvg + "; ");
                  this.csvConnectionsFile.println(var2.statistic.FPSMax + "; ");

                  for(var3 = 0; var3 < 32; ++var3) {
                     short var10001 = var2.statistic.FPSHistogramm[var3];
                     this.csvConnectionsFile.println(var10001 + "; ");
                  }
               } else {
                  for(var3 = 0; var3 < 51; ++var3) {
                     this.csvConnectionsFile.print("; ");
                  }

                  this.csvConnectionsFile.println();
               }
            }

            Iterator var7 = PacketTypes.packetTypes.values().iterator();

            while(var7.hasNext()) {
               PacketTypes.PacketType var8 = (PacketTypes.PacketType)var7.next();
               this.csvIncomePacketsFile.print(var8.incomePackets + ";");
               this.csvIncomeBytesFile.print(var8.incomeBytes + ";");
               this.csvOutcomePacketsFile.print(var8.outcomePackets + ";");
               this.csvOutcomeBytesFile.print(var8.outcomeBytes + ";");
            }

            this.csvIncomePacketsFile.println();
            this.csvIncomeBytesFile.println();
            this.csvOutcomePacketsFile.println();
            this.csvOutcomeBytesFile.println();
            this.csvStatisticFile.flush();
            this.csvConnectionsFile.flush();
            this.csvIncomePacketsFile.flush();
            this.csvIncomeBytesFile.flush();
            this.csvOutcomePacketsFile.flush();
            this.csvOutcomeBytesFile.flush();
         }

      } catch (NullPointerException var6) {
         var6.printStackTrace();
      }
   }

   public void getStatisticTable(ByteBuffer var1) throws IOException {
      if (this.table != null) {
         this.table.save(var1);
      }

   }

   public void setStatisticTable(ByteBuffer var1) throws IOException {
      if (var1.remaining() != 0) {
         this.table = LuaManager.platform.newTable();

         try {
            this.table.load((ByteBuffer)var1, 186);
            this.table.rawset("lastReportTime", (double)System.currentTimeMillis());
         } catch (Exception var3) {
            this.table = null;
            ExceptionLogger.logException(var3);
         }

      }
   }

   public KahluaTable getStatisticTableForLua() {
      return this.table;
   }

   public void setPeriod(int var1) {
      Period = var1;
      if (this.table != null) {
         this.table.rawset("period", (double)Period);
      }

   }

   public class TasksStatistic {
      protected long added = 0L;
      protected long processed = 0L;

      public void Clear() {
         this.added = 0L;
         this.processed = 0L;
      }

      public String PrintTitle(String var1) {
         return var1 + "Added; " + var1 + "Processed; ";
      }

      public String Print() {
         String var1 = this.added + "; " + this.processed + "; ";
         return var1;
      }

      public void Added() {
         ++this.added;
      }

      public void Processed() {
         ++this.processed;
      }
   }

   public class SaveTasksStatistic extends MPStatistic.TasksStatistic {
      private int SaveUnloadedTasksAdded = 0;
      private int SaveLoadedTasksAdded = 0;
      private int SaveGameTimeTasksAdded = 0;
      private int QuitThreadTasksAdded = 0;

      public SaveTasksStatistic() {
         super();
      }

      public void Clear() {
         super.Clear();
         this.SaveUnloadedTasksAdded = 0;
         this.SaveLoadedTasksAdded = 0;
         this.SaveGameTimeTasksAdded = 0;
         this.QuitThreadTasksAdded = 0;
      }

      public String PrintTitle(String var1) {
         return var1 + "SaveUnloadedAdded; " + var1 + "SaveLoadedAdded; " + var1 + "SaveGameTimeAdded; " + var1 + "QuitThreadAdded; " + var1 + "Processed; ";
      }

      public String Print() {
         String var1 = this.SaveUnloadedTasksAdded + "; " + this.SaveLoadedTasksAdded + "; " + this.SaveGameTimeTasksAdded + "; " + this.QuitThreadTasksAdded + "; " + this.processed + "; ";
         return var1;
      }

      public void SaveUnloadedTasksAdded() {
         ++this.SaveUnloadedTasksAdded;
      }

      public void SaveLoadedTasksAdded() {
         ++this.SaveLoadedTasksAdded;
      }

      public void SaveGameTimeTasksAdded() {
         ++this.SaveGameTimeTasksAdded;
      }

      public void QuitThreadTasksAdded() {
         ++this.QuitThreadTasksAdded;
      }
   }

   public class ServerCellStatistic {
      protected long added = 0L;
      protected long canceled = 0L;

      public void Clear() {
         this.added = 0L;
         this.canceled = 0L;
      }

      public String PrintTitle(String var1) {
         return var1 + "Added; " + var1 + "Canceled; ";
      }

      public String Print() {
         String var1 = this.added + "; " + this.canceled + "; ";
         return var1;
      }

      public void Added() {
         ++this.added;
      }

      public void Added(int var1) {
         this.added += (long)var1;
      }

      public void Canceled() {
         ++this.canceled;
      }
   }

   public class MainThreadStatistic extends MPStatistic.ThreadStatistic {
      private long timeStartSleep = 0L;

      public MainThreadStatistic() {
         super();
      }

      public void Start() {
         if (this.timeStart == 0L) {
            this.timeStart = System.currentTimeMillis();
         } else {
            long var1 = System.currentTimeMillis() - this.timeStart;
            this.timeStart = System.currentTimeMillis();
            this.timeWork += var1;
            if (this.timeMax < var1) {
               this.timeMax = var1;
            }

            ++this.timeCount;
         }
      }

      public void End() {
      }

      public void StartSleep() {
         this.timeStartSleep = System.currentTimeMillis();
      }

      public void EndSleep() {
         long var1 = System.currentTimeMillis() - this.timeStartSleep;
         this.timeSleep += var1;
         this.timeStart += var1;
      }
   }

   public class ThreadStatistic {
      protected boolean started = false;
      protected long timeStart = 0L;
      protected long timeWork = 0L;
      protected long timeMax = 0L;
      protected long timeSleep = 0L;
      protected long timeCount = 0L;

      public void Clear() {
         this.timeWork = 0L;
         this.timeMax = 0L;
         this.timeSleep = 0L;
         this.timeCount = 0L;
      }

      public String PrintTitle(String var1) {
         return var1 + "Work; " + var1 + "Max; " + var1 + "Sleep; " + var1 + "Count;";
      }

      public String Print() {
         String var1 = this.timeWork + "; " + this.timeMax + "; " + this.timeSleep + "; " + this.timeCount + "; ";
         return var1;
      }

      public void Start() {
         if (this.started) {
            this.End();
         }

         if (this.timeStart != 0L) {
            this.timeSleep += System.currentTimeMillis() - this.timeStart;
         }

         this.timeStart = System.currentTimeMillis();
         ++this.timeCount;
         this.started = true;
      }

      public void End() {
         if (this.timeStart != 0L && this.started) {
            long var1 = System.currentTimeMillis() - this.timeStart;
            this.timeStart = System.currentTimeMillis();
            this.timeWork += var1;
            if (this.timeMax < var1) {
               this.timeMax = var1;
            }

            this.started = false;
         }
      }
   }

   public class ProbeStatistic {
      protected boolean started = false;
      protected long timeStart = 0L;
      protected long timeWork = 0L;
      protected long timeMax = 0L;
      protected long timeCount = 0L;

      public void Clear() {
         this.timeWork = 0L;
         this.timeMax = 0L;
         this.timeCount = 0L;
      }

      public String PrintTitle(String var1) {
         return var1 + "Work; " + var1 + "Max; " + var1 + "Count;";
      }

      public String Print() {
         long var10000 = this.timeWork / 1000000L;
         String var1 = var10000 + "; " + this.timeMax / 1000000L + "; " + this.timeCount + "; ";
         return var1;
      }

      public void Start() {
         this.timeStart = System.nanoTime();
         ++this.timeCount;
         this.started = true;
      }

      public void End() {
         if (this.started) {
            long var1 = System.nanoTime() - this.timeStart;
            this.timeWork += var1;
            if (this.timeMax < var1) {
               this.timeMax = var1;
            }

            this.started = false;
         }
      }
   }
}
