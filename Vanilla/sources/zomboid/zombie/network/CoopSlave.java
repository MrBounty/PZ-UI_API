package zombie.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.ZomboidFileSystem;
import zombie.core.znet.PortMapper;
import zombie.core.znet.SteamGameServer;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;

public class CoopSlave {
   private static PrintStream stdout;
   private static PrintStream stderr;
   private Pattern serverMessageParser = Pattern.compile("^([\\-\\w]+)(\\[(\\d+)\\])?@(.*)$");
   private long nextPing = -1L;
   private long lastPong = -1L;
   public static CoopSlave instance;
   public String hostUser = null;
   public long hostSteamID = 0L;
   private boolean masterLost = false;
   private HashSet invites = new HashSet();
   private Long serverSteamID = null;

   public static void init() throws FileNotFoundException {
      instance = new CoopSlave();
   }

   public static void initStreams() throws FileNotFoundException {
      String var10002 = ZomboidFileSystem.instance.getCacheDir();
      FileOutputStream var0 = new FileOutputStream(var10002 + File.separator + "coop-console.txt");
      stdout = System.out;
      stderr = System.err;
      System.setOut(new PrintStream(var0));
      System.setErr(System.out);
   }

   private CoopSlave() {
      this.notify("coop mode enabled");
      if (System.getProperty("hostUser") != null) {
         this.hostUser = System.getProperty("zomboid.hostUser").trim();
      }

   }

   public synchronized void notify(String var1) {
      this.sendMessage("info", (String)null, var1);
   }

   public synchronized void sendStatus(String var1) {
      this.sendMessage("status", (String)null, var1);
   }

   public static void status(String var0) {
      if (instance != null) {
         instance.sendStatus(var0);
      }

   }

   public synchronized void sendMessage(String var1) {
      this.sendMessage("message", (String)null, var1);
   }

   public synchronized void sendMessage(String var1, String var2, String var3) {
      if (var2 != null) {
         stdout.println(var1 + "[" + var2 + "]@" + var3);
      } else {
         stdout.println(var1 + "@" + var3);
      }

   }

   public void sendExternalIPAddress(String var1) {
      this.sendMessage("get-parameter", var1, PortMapper.getExternalAddress());
   }

   public synchronized void sendSteamID(String var1) {
      if (this.serverSteamID == null && SteamUtils.isSteamModeEnabled()) {
         this.serverSteamID = SteamGameServer.GetSteamID();
      }

      this.sendMessage("get-parameter", var1, this.serverSteamID.toString());
   }

   public boolean handleCommand(String var1) {
      Matcher var2 = this.serverMessageParser.matcher(var1);
      if (var2.find()) {
         String var3 = var2.group(1);
         String var4 = var2.group(3);
         String var5 = var2.group(4);
         if (Objects.equals(var3, "set-host-user")) {
            System.out.println("Set host user:" + var5);
            this.hostUser = var5;
         }

         if (Objects.equals(var3, "set-host-steamid")) {
            this.hostSteamID = SteamUtils.convertStringToSteamID(var5);
         }

         Long var6;
         if (Objects.equals(var3, "invite-add")) {
            var6 = SteamUtils.convertStringToSteamID(var5);
            if (var6 != -1L) {
               this.invites.add(var6);
            }
         }

         if (Objects.equals(var3, "invite-remove")) {
            var6 = SteamUtils.convertStringToSteamID(var5);
            if (var6 != -1L) {
               this.invites.remove(var6);
            }
         }

         if (Objects.equals(var3, "get-parameter")) {
            DebugLog.log("Got get-parameter command: tag = " + var3 + " payload = " + var5);
            if (Objects.equals(var5, "external-ip")) {
               this.sendExternalIPAddress(var4);
            } else if (Objects.equals(var5, "steam-id")) {
               this.sendSteamID(var4);
            }
         }

         if (Objects.equals(var3, "ping")) {
            this.lastPong = System.currentTimeMillis();
         }

         if (Objects.equals(var3, "process-status") && Objects.equals(var5, "eof")) {
            DebugLog.log("Master connection lost: EOF");
            this.masterLost = true;
         }

         return true;
      } else {
         DebugLog.log("Got malformed command: " + var1);
         return false;
      }
   }

   public String getHostUser() {
      return this.hostUser;
   }

   public void update() {
      long var1 = System.currentTimeMillis();
      if (var1 >= this.nextPing) {
         this.sendMessage("ping", (String)null, "ping");
         this.nextPing = var1 + 5000L;
      }

      long var3 = (long)(Math.max(ServerOptions.instance.CoopMasterPingTimeout.getValue(), 30) * 1000);
      if (this.lastPong == -1L) {
         this.lastPong = var1;
      }

      this.masterLost = this.masterLost || var1 - this.lastPong > var3;
   }

   public boolean masterLost() {
      return this.masterLost;
   }

   public boolean isHost(long var1) {
      return var1 == this.hostSteamID;
   }

   public boolean isInvited(long var1) {
      return this.invites.contains(var1);
   }
}
