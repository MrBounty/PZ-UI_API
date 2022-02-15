package zombie.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;
import zombie.GameWindow;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.core.ThreadGroups;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;

public class CoopMaster {
   private Process serverProcess;
   private Thread serverThread;
   private PrintStream serverCommandStream;
   private final List incomingMessages = new LinkedList();
   private Pattern serverMessageParser = Pattern.compile("^([\\-\\w]+)(\\[(\\d+)\\])?@(.*)$");
   private CoopMaster.TerminationReason serverTerminationReason;
   private Thread timeoutWatchThread;
   private boolean serverResponded;
   public static final CoopMaster instance = new CoopMaster();
   private String adminUsername = null;
   private String adminPassword = null;
   private String serverName = null;
   private Long serverSteamID = null;
   private String serverIP = null;
   private Integer serverPort = null;
   private int autoCookie = 0;
   private static final int autoCookieOffset = 1000000;
   private static final int maxAutoCookie = 1000000;
   private final List listeners = new LinkedList();

   private CoopMaster() {
      this.adminPassword = UUID.randomUUID().toString();
   }

   public void launchServer(String var1, String var2, int var3) throws IOException {
      this.launchServer(var1, var2, var3, false);
   }

   public void softreset(String var1, String var2, int var3) throws IOException {
      this.launchServer(var1, var2, var3, true);
   }

   private void launchServer(String var1, String var2, int var3, boolean var4) throws IOException {
      String var5 = Paths.get(System.getProperty("java.home"), "bin", "java").toAbsolutePath().toString();
      if (SteamUtils.isSteamModeEnabled()) {
         var2 = "admin";
      }

      ArrayList var6 = new ArrayList();
      var6.add(var5);
      var6.add("-Xms" + var3 + "m");
      var6.add("-Xmx" + var3 + "m");
      String var10001 = System.getProperty("java.library.path");
      var6.add("-Djava.library.path=" + var10001);
      var10001 = System.getProperty("java.class.path");
      var6.add("-Djava.class.path=" + var10001);
      var10001 = System.getProperty("user.dir");
      var6.add("-Duser.dir=" + var10001);
      var10001 = System.getProperty("user.home");
      var6.add("-Duser.home=" + var10001);
      var6.add("-Dzomboid.znetlog=1");
      var6.add("-Dzomboid.steam=" + (SteamUtils.isSteamModeEnabled() ? "1" : "0"));
      var6.add("-Djava.awt.headless=true");
      var6.add("-XX:-OmitStackTraceInFastThrow");
      String var7 = this.getGarbageCollector();
      if (var7 != null) {
         var6.add(var7);
      }

      if (var4) {
         var6.add("-Dsoftreset");
      }

      if (Core.bDebug) {
         var6.add("-Ddebug");
      }

      var6.add("zombie.network.GameServer");
      var6.add("-coop");
      var6.add("-servername");
      var6.add(this.serverName = var1);
      var6.add("-adminusername");
      var6.add(this.adminUsername = var2);
      var6.add("-adminpassword");
      var6.add(this.adminPassword);
      var6.add("-cachedir=" + ZomboidFileSystem.instance.getCacheDir());
      ProcessBuilder var8 = new ProcessBuilder(var6);
      this.serverTerminationReason = CoopMaster.TerminationReason.NormalTermination;
      this.serverResponded = false;
      this.serverProcess = var8.start();
      this.serverCommandStream = new PrintStream(this.serverProcess.getOutputStream());
      this.serverThread = new Thread(ThreadGroups.Workers, this::readServer);
      this.serverThread.setUncaughtExceptionHandler(GameWindow::uncaughtException);
      this.serverThread.start();
      this.timeoutWatchThread = new Thread(ThreadGroups.Workers, this::watchServer);
      this.timeoutWatchThread.setUncaughtExceptionHandler(GameWindow::uncaughtException);
      this.timeoutWatchThread.start();
   }

   private String getGarbageCollector() {
      try {
         RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
         List var2 = var1.getInputArguments();
         boolean var3 = false;
         boolean var4 = false;
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if ("-XX:+UseZGC".equals(var6)) {
               var3 = true;
            }

            if ("-XX:-UseZGC".equals(var6)) {
               var3 = false;
            }

            if ("-XX:+UseG1GC".equals(var6)) {
               var4 = true;
            }

            if ("-XX:-UseG1GC".equals(var6)) {
               var4 = false;
            }
         }

         if (var3) {
            return "-XX:+UseZGC";
         }

         if (var4) {
            return "-XX:+UseG1GC";
         }
      } catch (Throwable var7) {
      }

      return null;
   }

   private void readServer() {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(this.serverProcess.getInputStream()));

      while(true) {
         try {
            int var6 = this.serverProcess.exitValue();
            break;
         } catch (IllegalThreadStateException var5) {
            String var2 = null;

            try {
               var2 = var1.readLine();
            } catch (IOException var4) {
               var4.printStackTrace();
            }

            if (var2 != null) {
               this.storeMessage(var2);
               this.serverResponded = true;
            }
         }
      }

      this.storeMessage("process-status@terminated");
   }

   public void abortServer() {
      this.serverProcess.destroy();
   }

   private void watchServer() {
      int var1 = Math.max(ServerOptions.instance.CoopServerLaunchTimeout.getValue(), 5);

      try {
         Thread.sleep((long)(1000 * var1));
         if (!this.serverResponded) {
            this.serverTerminationReason = CoopMaster.TerminationReason.Timeout;
            this.abortServer();
         }
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   public boolean isRunning() {
      return this.serverThread != null && this.serverThread.isAlive();
   }

   public CoopMaster.TerminationReason terminationReason() {
      return this.serverTerminationReason;
   }

   private void storeMessage(String var1) {
      synchronized(this.incomingMessages) {
         this.incomingMessages.add(var1);
      }
   }

   public synchronized void sendMessage(String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var1);
      if (var2 == null) {
         var4.append("@");
      } else {
         var4.append("[");
         var4.append(var2);
         var4.append("]@");
      }

      var4.append(var3);
      String var5 = var4.toString();
      if (this.serverCommandStream != null) {
         this.serverCommandStream.println(var5);
         this.serverCommandStream.flush();
      }

   }

   public void sendMessage(String var1, String var2) {
      this.sendMessage(var1, (String)null, var2);
   }

   public synchronized void invokeServer(String var1, String var2, ICoopServerMessageListener var3) {
      this.autoCookie = (this.autoCookie + 1) % 1000000;
      String var4 = Integer.toString(1000000 + this.autoCookie);
      this.addListener(var3, new CoopMaster.ListenerOptions(var1, var4, true));
      this.sendMessage(var1, var4, var2);
   }

   public String getMessage() {
      String var1 = null;
      synchronized(this.incomingMessages) {
         if (this.incomingMessages.size() != 0) {
            var1 = (String)this.incomingMessages.get(0);
            this.incomingMessages.remove(0);
            if (!"ping@ping".equals(var1)) {
               System.out.println("SERVER: " + var1);
            }
         }

         return var1;
      }
   }

   public void update() {
      String var1;
      while((var1 = this.getMessage()) != null) {
         Matcher var2 = this.serverMessageParser.matcher(var1);
         if (var2.find()) {
            String var3 = var2.group(1);
            String var4 = var2.group(3);
            String var5 = var2.group(4);
            LuaEventManager.triggerEvent("OnCoopServerMessage", var3, var4, var5);
            this.handleMessage(var3, var4, var5);
         } else {
            DebugLog.log(DebugType.Network, "[CoopMaster] Unknown message incoming from the slave server: " + var1);
         }
      }

   }

   private void handleMessage(String var1, String var2, String var3) {
      if (Objects.equals(var1, "ping")) {
         this.sendMessage("ping", var2, "pong");
      } else if (Objects.equals(var1, "steam-id")) {
         if (Objects.equals(var3, "null")) {
            this.serverSteamID = null;
         } else {
            this.serverSteamID = SteamUtils.convertStringToSteamID(var3);
         }
      } else if (Objects.equals(var1, "server-address")) {
         DebugLog.log("Got server-address: " + var3);
         String var4 = "^(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)$";
         Pattern var5 = Pattern.compile(var4);
         Matcher var6 = var5.matcher(var3);
         if (var6.find()) {
            String var7 = var6.group(1);
            String var8 = var6.group(2);
            this.serverIP = var7;
            this.serverPort = Integer.valueOf(var8);
            DebugLog.log("Successfully parsed: address = " + this.serverIP + ", port = " + this.serverPort);
         } else {
            DebugLog.log("Failed to parse server address");
         }
      }

      this.invokeListeners(var1, var2, var3);
   }

   public void register(Platform var1, KahluaTable var2) {
      KahluaTable var3 = var1.newTable();
      var3.rawset("launch", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            boolean var3 = false;
            if (var2 == 4) {
               Object var4 = var1.get(1);
               Object var5 = var1.get(2);
               Object var6 = var1.get(3);
               if (!(var4 instanceof String) || !(var5 instanceof String) || !(var6 instanceof Double)) {
                  return 0;
               }

               try {
                  CoopMaster.this.launchServer((String)var4, (String)var5, ((Double)var6).intValue());
                  var3 = true;
               } catch (IOException var8) {
                  var8.printStackTrace();
               }
            } else {
               DebugLog.log(DebugType.Network, "[CoopMaster] wrong number of arguments: " + var2);
            }

            var1.push(var3);
            return 1;
         }
      });
      var3.rawset("softreset", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            boolean var3 = false;
            if (var2 == 4) {
               Object var4 = var1.get(1);
               Object var5 = var1.get(2);
               Object var6 = var1.get(3);
               if (!(var4 instanceof String) || !(var5 instanceof String) || !(var6 instanceof Double)) {
                  return 0;
               }

               try {
                  CoopMaster.this.softreset((String)var4, (String)var5, ((Double)var6).intValue());
                  var3 = true;
               } catch (IOException var8) {
                  var8.printStackTrace();
               }
            } else {
               DebugLog.log(DebugType.Network, "[CoopMaster] wrong number of arguments: " + var2);
            }

            var1.push(var3);
            return 1;
         }
      });
      var3.rawset("isRunning", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            var1.push(CoopMaster.this.isRunning());
            return 1;
         }
      });
      var3.rawset("sendMessage", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            Object var3;
            Object var4;
            if (var2 == 4) {
               var3 = var1.get(1);
               var4 = var1.get(2);
               Object var5 = var1.get(3);
               if (var3 instanceof String && var4 instanceof String && var5 instanceof String) {
                  CoopMaster.this.sendMessage((String)var3, (String)var4, (String)var5);
               }
            } else if (var2 == 3) {
               var3 = var1.get(1);
               var4 = var1.get(2);
               if (var3 instanceof String && var4 instanceof String) {
                  CoopMaster.this.sendMessage((String)var3, (String)var4);
               }
            }

            return 0;
         }
      });
      var3.rawset("getAdminPassword", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            var1.push(CoopMaster.this.adminPassword);
            return 1;
         }
      });
      var3.rawset("getTerminationReason", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            var1.push(CoopMaster.this.serverTerminationReason.toString());
            return 1;
         }
      });
      var3.rawset("getSteamID", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            if (CoopMaster.this.serverSteamID != null) {
               var1.push(SteamUtils.convertSteamIDToString(CoopMaster.this.serverSteamID));
               return 1;
            } else {
               return 0;
            }
         }
      });
      var3.rawset("getAddress", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            var1.push(CoopMaster.this.serverIP);
            return 1;
         }
      });
      var3.rawset("getPort", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            var1.push(CoopMaster.this.serverPort);
            return 1;
         }
      });
      var3.rawset("abort", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            CoopMaster.this.abortServer();
            return 0;
         }
      });
      var3.rawset("getServerSaveFolder", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            Object var3 = var1.get(1);
            var1.push(CoopMaster.this.getServerSaveFolder((String)var3));
            return 1;
         }
      });
      var3.rawset("getPlayerSaveFolder", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            Object var3 = var1.get(1);
            var1.push(CoopMaster.this.getPlayerSaveFolder((String)var3));
            return 1;
         }
      });
      var2.rawset("CoopServer", var3);
   }

   public void addListener(ICoopServerMessageListener var1, CoopMaster.ListenerOptions var2) {
      synchronized(this.listeners) {
         this.listeners.add(new CoopMaster.Pair(var1, var2));
      }
   }

   public void addListener(ICoopServerMessageListener var1) {
      this.addListener(var1, (CoopMaster.ListenerOptions)null);
   }

   public void removeListener(ICoopServerMessageListener var1) {
      synchronized(this.listeners) {
         int var3;
         for(var3 = 0; var3 < this.listeners.size() && ((CoopMaster.Pair)this.listeners.get(var3)).first != var1; ++var3) {
         }

         if (var3 < this.listeners.size()) {
            this.listeners.remove(var3);
         }

      }
   }

   private void invokeListeners(String var1, String var2, String var3) {
      synchronized(this.listeners) {
         Iterator var5 = this.listeners.iterator();

         while(true) {
            while(true) {
               ICoopServerMessageListener var7;
               CoopMaster.ListenerOptions var8;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  CoopMaster.Pair var6 = (CoopMaster.Pair)var5.next();
                  var7 = (ICoopServerMessageListener)var6.first;
                  var8 = (CoopMaster.ListenerOptions)var6.second;
               } while(var7 == null);

               if (var8 == null) {
                  var7.OnCoopServerMessage(var1, var2, var3);
               } else if ((var8.tag == null || var8.tag.equals(var1)) && (var8.cookie == null || var8.cookie.equals(var2))) {
                  if (var8.autoRemove) {
                     var5.remove();
                  }

                  var7.OnCoopServerMessage(var1, var2, var3);
               }
            }
         }
      }
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getServerSaveFolder(String var1) {
      return LuaManager.GlobalObject.sanitizeWorldName(var1);
   }

   public String getPlayerSaveFolder(String var1) {
      return LuaManager.GlobalObject.sanitizeWorldName(var1 + "_player");
   }

   public static enum TerminationReason {
      NormalTermination,
      Timeout;

      // $FF: synthetic method
      private static CoopMaster.TerminationReason[] $values() {
         return new CoopMaster.TerminationReason[]{NormalTermination, Timeout};
      }
   }

   public class ListenerOptions {
      public String tag;
      public String cookie;
      public boolean autoRemove;

      public ListenerOptions(String var2, String var3, boolean var4) {
         this.tag = null;
         this.cookie = null;
         this.autoRemove = false;
         this.tag = var2;
         this.cookie = var3;
         this.autoRemove = var4;
      }

      public ListenerOptions(String var2, String var3) {
         this(var2, var3, false);
      }

      public ListenerOptions(String var2) {
         this(var2, (String)null, false);
      }
   }

   private class Pair {
      private final Object first;
      private final Object second;

      public Pair(Object var2, Object var3) {
         this.first = var2;
         this.second = var3;
      }

      public Object getFirst() {
         return this.first;
      }

      public Object getSecond() {
         return this.second;
      }
   }
}
