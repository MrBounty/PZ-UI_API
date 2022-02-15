package zombie.network;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.Sets.SetView;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.CRC32;
import org.json.JSONArray;
import org.json.JSONObject;
import zombie.characters.NetworkCharacter;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.ThreadGroups;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.RakNetPeerInterface;
import zombie.core.secure.PZcrypt;
import zombie.core.utils.UpdateLimit;
import zombie.core.znet.ZNet;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoDirections;
import zombie.iso.IsoUtils;
import zombie.iso.Vector2;
import zombie.network.packets.PlayerPacket;
import zombie.network.packets.SyncInjuriesPacket;
import zombie.network.packets.ZombiePacket;

public class FakeClientManager {
   private static final int SERVER_PORT = 16261;
   private static final int CLIENT_PORT = 17500;
   private static final String CLIENT_ADDRESS = "0.0.0.0";
   private static final String versionNumber = Core.getInstance().getVersionNumber();
   private static final DateFormat logDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
   private static final ThreadLocal stringUTF = ThreadLocal.withInitial(FakeClientManager.StringUTF::new);
   private static int logLevel = 0;
   private static long startTime = System.currentTimeMillis();

   public static String ReadStringUTF(ByteBuffer var0) {
      return ((FakeClientManager.StringUTF)stringUTF.get()).load(var0);
   }

   public static void WriteStringUTF(ByteBuffer var0, String var1) {
      ((FakeClientManager.StringUTF)stringUTF.get()).save(var0, var1);
   }

   private static void sleep(long var0) {
      try {
         Thread.sleep(var0);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   private static HashMap load(String var0) {
      HashMap var1 = new HashMap();

      try {
         String var2 = new String(Files.readAllBytes(Paths.get(var0)));
         JSONObject var3 = new JSONObject(var2);
         FakeClientManager.Movement.version = var3.getString("version");
         JSONObject var4 = var3.getJSONObject("config");
         JSONObject var5 = var4.getJSONObject("client");
         JSONObject var6 = var5.getJSONObject("connection");
         if (var6.has("serverHost")) {
            FakeClientManager.Client.connectionServerHost = var6.getString("serverHost");
         }

         FakeClientManager.Client.connectionInterval = var6.getLong("interval");
         FakeClientManager.Client.connectionTimeout = var6.getLong("timeout");
         FakeClientManager.Client.connectionDelay = var6.getLong("delay");
         JSONObject var7 = var5.getJSONObject("statistics");
         FakeClientManager.Client.statisticsPeriod = var7.getInt("period");
         FakeClientManager.Client.statisticsClientID = Math.max(var7.getInt("id"), -1);
         JSONObject var8;
         if (var5.has("checksum")) {
            var8 = var5.getJSONObject("checksum");
            FakeClientManager.Client.luaChecksum = var8.getString("lua");
            FakeClientManager.Client.scriptChecksum = var8.getString("script");
         }

         int var42;
         if (var4.has("zombies")) {
            var6 = var4.getJSONObject("zombies");
            FakeClientManager.ZombieSimulator.Behaviour var41 = FakeClientManager.ZombieSimulator.Behaviour.Normal;
            if (var6.has("behaviour")) {
               var41 = FakeClientManager.ZombieSimulator.Behaviour.valueOf(var6.getString("behaviour"));
            }

            FakeClientManager.ZombieSimulator.behaviour = var41;
            if (var6.has("maxZombiesPerUpdate")) {
               FakeClientManager.ZombieSimulator.maxZombiesPerUpdate = var6.getInt("maxZombiesPerUpdate");
            }

            if (var6.has("deleteZombieDistance")) {
               var42 = var6.getInt("deleteZombieDistance");
               FakeClientManager.ZombieSimulator.deleteZombieDistanceSquared = var42 * var42;
            }

            if (var6.has("forgotZombieDistance")) {
               var42 = var6.getInt("forgotZombieDistance");
               FakeClientManager.ZombieSimulator.forgotZombieDistanceSquared = var42 * var42;
            }

            if (var6.has("canSeeZombieDistance")) {
               var42 = var6.getInt("canSeeZombieDistance");
               FakeClientManager.ZombieSimulator.canSeeZombieDistanceSquared = var42 * var42;
            }

            if (var6.has("seeZombieDistance")) {
               var42 = var6.getInt("seeZombieDistance");
               FakeClientManager.ZombieSimulator.seeZombieDistanceSquared = var42 * var42;
            }

            if (var6.has("canChangeTarget")) {
               FakeClientManager.ZombieSimulator.canChangeTarget = var6.getBoolean("canChangeTarget");
            }
         }

         var6 = var4.getJSONObject("player");
         FakeClientManager.Player.fps = var6.getInt("fps");
         FakeClientManager.Player.predictInterval = var6.getInt("predict");
         if (var6.has("damage")) {
            FakeClientManager.Player.damage = (float)var6.getDouble("damage");
         }

         var7 = var4.getJSONObject("movement");
         FakeClientManager.Movement.defaultRadius = var7.getInt("radius");
         var8 = var7.getJSONObject("motion");
         FakeClientManager.Movement.aimSpeed = var8.getInt("aim");
         FakeClientManager.Movement.sneakSpeed = var8.getInt("sneak");
         FakeClientManager.Movement.sneakRunSpeed = var8.getInt("sneakrun");
         FakeClientManager.Movement.walkSpeed = var8.getInt("walk");
         FakeClientManager.Movement.runSpeed = var8.getInt("run");
         FakeClientManager.Movement.sprintSpeed = var8.getInt("sprint");
         JSONObject var9 = var8.getJSONObject("pedestrian");
         FakeClientManager.Movement.pedestrianSpeedMin = var9.getInt("min");
         FakeClientManager.Movement.pedestrianSpeedMax = var9.getInt("max");
         JSONObject var10 = var8.getJSONObject("vehicle");
         FakeClientManager.Movement.vehicleSpeedMin = var10.getInt("min");
         FakeClientManager.Movement.vehicleSpeedMax = var10.getInt("max");
         JSONArray var39 = var3.getJSONArray("movements");

         for(int var40 = 0; var40 < var39.length(); ++var40) {
            var7 = var39.getJSONObject(var40);
            var42 = var7.getInt("id");
            String var43 = null;
            if (var7.has("description")) {
               var43 = var7.getString("description");
            }

            int var44 = (int)Math.round(Math.random() * 6000.0D + 6000.0D);
            int var11 = (int)Math.round(Math.random() * 6000.0D + 6000.0D);
            if (var7.has("spawn")) {
               JSONObject var12 = var7.getJSONObject("spawn");
               var44 = var12.getInt("x");
               var11 = var12.getInt("y");
            }

            FakeClientManager.Movement.Motion var45 = Math.random() > 0.800000011920929D ? FakeClientManager.Movement.Motion.Vehicle : FakeClientManager.Movement.Motion.Pedestrian;
            if (var7.has("motion")) {
               var45 = FakeClientManager.Movement.Motion.valueOf(var7.getString("motion"));
            }

            int var13 = 0;
            if (var7.has("speed")) {
               var13 = var7.getInt("speed");
            } else {
               switch(var45) {
               case Aim:
                  var13 = FakeClientManager.Movement.aimSpeed;
                  break;
               case Sneak:
                  var13 = FakeClientManager.Movement.sneakSpeed;
                  break;
               case SneakRun:
                  var13 = FakeClientManager.Movement.sneakRunSpeed;
                  break;
               case Run:
                  var13 = FakeClientManager.Movement.runSpeed;
                  break;
               case Sprint:
                  var13 = FakeClientManager.Movement.sprintSpeed;
                  break;
               case Walk:
                  var13 = FakeClientManager.Movement.walkSpeed;
                  break;
               case Pedestrian:
                  var13 = (int)Math.round(Math.random() * (double)(FakeClientManager.Movement.pedestrianSpeedMax - FakeClientManager.Movement.pedestrianSpeedMin) + (double)FakeClientManager.Movement.pedestrianSpeedMin);
                  break;
               case Vehicle:
                  var13 = (int)Math.round(Math.random() * (double)(FakeClientManager.Movement.vehicleSpeedMax - FakeClientManager.Movement.vehicleSpeedMin) + (double)FakeClientManager.Movement.vehicleSpeedMin);
               }
            }

            FakeClientManager.Movement.Type var14 = FakeClientManager.Movement.Type.Line;
            if (var7.has("type")) {
               var14 = FakeClientManager.Movement.Type.valueOf(var7.getString("type"));
            }

            int var15 = FakeClientManager.Movement.defaultRadius;
            if (var7.has("radius")) {
               var15 = var7.getInt("radius");
            }

            IsoDirections var16 = IsoDirections.fromIndex((int)Math.round(Math.random() * 7.0D));
            if (var7.has("direction")) {
               var16 = IsoDirections.valueOf(var7.getString("direction"));
            }

            boolean var17 = false;
            if (var7.has("ghost")) {
               var17 = var7.getBoolean("ghost");
            }

            long var18 = (long)var42 * FakeClientManager.Client.connectionInterval;
            if (var7.has("connect")) {
               var18 = var7.getLong("connect");
            }

            long var20 = 0L;
            if (var7.has("disconnect")) {
               var20 = var7.getLong("disconnect");
            }

            long var22 = 0L;
            if (var7.has("reconnect")) {
               var22 = var7.getLong("reconnect");
            }

            long var24 = 0L;
            if (var7.has("teleport")) {
               var24 = var7.getLong("teleport");
            }

            int var26 = (int)Math.round(Math.random() * 6000.0D + 6000.0D);
            int var27 = (int)Math.round(Math.random() * 6000.0D + 6000.0D);
            if (var7.has("destination")) {
               JSONObject var28 = var7.getJSONObject("destination");
               var26 = var28.getInt("x");
               var27 = var28.getInt("y");
            }

            FakeClientManager.HordeCreator var46 = null;
            int var31;
            if (var7.has("createHorde")) {
               JSONObject var29 = var7.getJSONObject("createHorde");
               int var30 = var29.getInt("count");
               var31 = var29.getInt("radius");
               long var32 = var29.getLong("interval");
               if (var32 != 0L) {
                  var46 = new FakeClientManager.HordeCreator(var31, var30, var32);
               }
            }

            FakeClientManager.SoundMaker var47 = null;
            if (var7.has("makeSound")) {
               JSONObject var48 = var7.getJSONObject("makeSound");
               var31 = var48.getInt("interval");
               int var50 = var48.getInt("radius");
               String var33 = var48.getString("message");
               if (var31 != 0) {
                  var47 = new FakeClientManager.SoundMaker(var31, var50, var33);
               }
            }

            FakeClientManager.Movement var49 = new FakeClientManager.Movement(var42, var43, var44, var11, var45, var13, var14, var15, var26, var27, var16, var17, var18, var20, var22, var24, var46, var47);
            if (var1.containsKey(var42)) {
               error(var42, String.format("Client %d already exists", var49.id));
            } else {
               var1.put(var42, var49);
            }
         }

         return var1;
      } catch (Exception var37) {
         error(-1, "Scenarios file load failed");
         var37.printStackTrace();
         return var1;
      } finally {
         ;
      }
   }

   private static void error(int var0, String var1) {
      System.out.print(String.format("%5s : %s , [%2d] > %s\n", "ERROR", logDateFormat.format(Calendar.getInstance().getTime()), var0, var1));
   }

   private static void info(int var0, String var1) {
      if (logLevel >= 0) {
         System.out.print(String.format("%5s : %s , [%2d] > %s\n", "INFO", logDateFormat.format(Calendar.getInstance().getTime()), var0, var1));
      }

   }

   private static void log(int var0, String var1) {
      if (logLevel >= 1) {
         System.out.print(String.format("%5s : %s , [%2d] > %s\n", "LOG", logDateFormat.format(Calendar.getInstance().getTime()), var0, var1));
      }

   }

   private static void trace(int var0, String var1) {
      if (logLevel >= 2) {
         System.out.print(String.format("%5s : %s , [%2d] > %s\n", "TRACE", logDateFormat.format(Calendar.getInstance().getTime()), var0, var1));
      }

   }

   public static void main(String[] var0) {
      String var1 = null;
      int var2 = -1;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3].startsWith("-scenarios=")) {
            var1 = var0[var3].replace("-scenarios=", "").trim();
         } else if (var0[var3].startsWith("-id=")) {
            var2 = Integer.parseInt(var0[var3].replace("-id=", "").trim());
         }
      }

      if (var1 == null || var1.isBlank()) {
         error(-1, "Invalid scenarios file name");
         System.exit(0);
      }

      Rand.init();
      System.loadLibrary("RakNet64");
      System.loadLibrary("ZNetNoSteam64");

      try {
         String var11 = System.getProperty("zomboid.znetlog");
         if (var11 != null) {
            logLevel = Integer.parseInt(var11);
            ZNet.init();
            ZNet.setLogLevel(logLevel);
         }
      } catch (NumberFormatException var10) {
         error(-1, "Invalid log arguments");
      }

      DebugLog.disableLog(DebugType.General);
      HashMap var12 = load(var1);
      FakeClientManager.Network var4;
      int var5;
      if (var2 != -1) {
         var5 = 17500 + var2;
         var4 = new FakeClientManager.Network(var12.size(), var5);
      } else {
         var5 = 17500;
         var4 = new FakeClientManager.Network(var12.size(), var5);
      }

      if (var4.isStarted()) {
         HashSet var6 = new HashSet();
         int var7 = 0;
         if (var2 != -1) {
            FakeClientManager.Movement var13 = (FakeClientManager.Movement)var12.get(var2);
            if (var13 != null) {
               var6.add(new FakeClientManager.Player(var13, var4, var7, var5));
            } else {
               error(var2, "Client movement not found");
            }
         } else {
            Iterator var8 = var12.values().iterator();

            while(var8.hasNext()) {
               FakeClientManager.Movement var9 = (FakeClientManager.Movement)var8.next();
               var6.add(new FakeClientManager.Player(var9, var4, var7++, var5));
            }
         }

         while(!var6.isEmpty()) {
            sleep(1000L);
         }
      }

   }

   private static class StringUTF {
      private char[] chars;
      private ByteBuffer byteBuffer;
      private CharBuffer charBuffer;
      private CharsetEncoder ce;
      private CharsetDecoder cd;

      private int encode(String var1) {
         int var2;
         if (this.chars == null || this.chars.length < var1.length()) {
            var2 = (var1.length() + 128 - 1) / 128 * 128;
            this.chars = new char[var2];
            this.charBuffer = CharBuffer.wrap(this.chars);
         }

         var1.getChars(0, var1.length(), this.chars, 0);
         this.charBuffer.limit(var1.length());
         this.charBuffer.position(0);
         if (this.ce == null) {
            this.ce = StandardCharsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
         }

         this.ce.reset();
         var2 = (int)((double)var1.length() * (double)this.ce.maxBytesPerChar());
         var2 = (var2 + 128 - 1) / 128 * 128;
         if (this.byteBuffer == null || this.byteBuffer.capacity() < var2) {
            this.byteBuffer = ByteBuffer.allocate(var2);
         }

         this.byteBuffer.clear();
         CoderResult var3 = this.ce.encode(this.charBuffer, this.byteBuffer, true);
         return this.byteBuffer.position();
      }

      private String decode(int var1) {
         if (this.cd == null) {
            this.cd = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
         }

         this.cd.reset();
         int var2 = (int)((double)var1 * (double)this.cd.maxCharsPerByte());
         if (this.chars == null || this.chars.length < var2) {
            int var3 = (var2 + 128 - 1) / 128 * 128;
            this.chars = new char[var3];
            this.charBuffer = CharBuffer.wrap(this.chars);
         }

         this.charBuffer.clear();
         CoderResult var4 = this.cd.decode(this.byteBuffer, this.charBuffer, true);
         return new String(this.chars, 0, this.charBuffer.position());
      }

      void save(ByteBuffer var1, String var2) {
         if (var2 != null && !var2.isEmpty()) {
            int var3 = this.encode(var2);
            var1.putShort((short)var3);
            this.byteBuffer.flip();
            var1.put(this.byteBuffer);
         } else {
            var1.putShort((short)0);
         }
      }

      String load(ByteBuffer var1) {
         short var2 = var1.getShort();
         if (var2 <= 0) {
            return "";
         } else {
            int var3 = (var2 + 128 - 1) / 128 * 128;
            if (this.byteBuffer == null || this.byteBuffer.capacity() < var3) {
               this.byteBuffer = ByteBuffer.allocate(var3);
            }

            this.byteBuffer.clear();
            if (var1.remaining() < var2) {
               DebugLog.General.error("GameWindow.StringUTF.load> numBytes:" + var2 + " is higher than the remaining bytes in the buffer:" + var1.remaining());
            }

            int var4 = var1.limit();
            var1.limit(var1.position() + var2);
            this.byteBuffer.put(var1);
            var1.limit(var4);
            this.byteBuffer.flip();
            return this.decode(var2);
         }
      }
   }

   private static class Movement {
      static String version;
      static int defaultRadius = 150;
      static int aimSpeed = 4;
      static int sneakSpeed = 6;
      static int walkSpeed = 7;
      static int sneakRunSpeed = 10;
      static int runSpeed = 13;
      static int sprintSpeed = 19;
      static int pedestrianSpeedMin = 5;
      static int pedestrianSpeedMax = 20;
      static int vehicleSpeedMin = 40;
      static int vehicleSpeedMax = 80;
      static final float zombieLungeDistanceSquared = 100.0F;
      static final float zombieWalkSpeed = 3.0F;
      static final float zombieLungeSpeed = 6.0F;
      final int id;
      final String description;
      final Vector2 spawn;
      FakeClientManager.Movement.Motion motion;
      float speed;
      final FakeClientManager.Movement.Type type;
      final int radius;
      final IsoDirections direction;
      final Vector2 destination;
      final boolean ghost;
      final long connectDelay;
      final long disconnectDelay;
      final long reconnectDelay;
      final long teleportDelay;
      final FakeClientManager.HordeCreator hordeCreator;
      FakeClientManager.SoundMaker soundMaker;
      long timestamp;

      public Movement(int var1, String var2, int var3, int var4, FakeClientManager.Movement.Motion var5, int var6, FakeClientManager.Movement.Type var7, int var8, int var9, int var10, IsoDirections var11, boolean var12, long var13, long var15, long var17, long var19, FakeClientManager.HordeCreator var21, FakeClientManager.SoundMaker var22) {
         this.id = var1;
         this.description = var2;
         this.spawn = new Vector2((float)var3, (float)var4);
         this.motion = var5;
         this.speed = (float)var6;
         this.type = var7;
         this.radius = var8;
         this.direction = var11;
         this.destination = new Vector2((float)var9, (float)var10);
         this.ghost = var12;
         this.connectDelay = var13;
         this.disconnectDelay = var15;
         this.reconnectDelay = var17;
         this.teleportDelay = var19;
         this.hordeCreator = var21;
         this.soundMaker = var22;
      }

      public void connect(int var1) {
         long var2 = System.currentTimeMillis();
         if (this.disconnectDelay != 0L) {
            FakeClientManager.info(this.id, String.format("Player %3d connect in %.3fs, disconnect in %.3fs", var1, (float)(var2 - this.timestamp) / 1000.0F, (float)this.disconnectDelay / 1000.0F));
         } else {
            FakeClientManager.info(this.id, String.format("Player %3d connect in %.3fs", var1, (float)(var2 - this.timestamp) / 1000.0F));
         }

         this.timestamp = var2;
      }

      public void disconnect(int var1) {
         long var2 = System.currentTimeMillis();
         if (this.reconnectDelay != 0L) {
            FakeClientManager.info(this.id, String.format("Player %3d disconnect in %.3fs, reconnect in %.3fs", var1, (float)(var2 - this.timestamp) / 1000.0F, (float)this.reconnectDelay / 1000.0F));
         } else {
            FakeClientManager.info(this.id, String.format("Player %3d disconnect in %.3fs", var1, (float)(var2 - this.timestamp) / 1000.0F));
         }

         this.timestamp = var2;
      }

      public boolean doTeleport() {
         return this.teleportDelay != 0L;
      }

      public boolean doDisconnect() {
         return this.disconnectDelay != 0L;
      }

      public boolean checkDisconnect() {
         return System.currentTimeMillis() - this.timestamp > this.disconnectDelay;
      }

      public boolean doReconnect() {
         return this.reconnectDelay != 0L;
      }

      public boolean checkReconnect() {
         return System.currentTimeMillis() - this.timestamp > this.reconnectDelay;
      }

      private static enum Motion {
         Aim,
         Sneak,
         Walk,
         SneakRun,
         Run,
         Sprint,
         Pedestrian,
         Vehicle;

         // $FF: synthetic method
         private static FakeClientManager.Movement.Motion[] $values() {
            return new FakeClientManager.Movement.Motion[]{Aim, Sneak, Walk, SneakRun, Run, Sprint, Pedestrian, Vehicle};
         }
      }

      private static enum Type {
         Stay,
         Line,
         Circle,
         AIAttackZombies,
         AIRunAwayFromZombies,
         AIRunToAnotherPlayers,
         AINormal;

         // $FF: synthetic method
         private static FakeClientManager.Movement.Type[] $values() {
            return new FakeClientManager.Movement.Type[]{Stay, Line, Circle, AIAttackZombies, AIRunAwayFromZombies, AIRunToAnotherPlayers, AINormal};
         }
      }
   }

   private static class Client {
      private static String connectionServerHost = "127.0.0.1";
      private static long connectionInterval = 1500L;
      private static long connectionTimeout = 10000L;
      private static long connectionDelay = 15000L;
      private static int statisticsClientID = -1;
      private static int statisticsPeriod = 1;
      private static long serverTimeShift = 0L;
      private static boolean serverTimeShiftIsSet = false;
      private final HashMap requests = new HashMap();
      private final FakeClientManager.Player player;
      private final FakeClientManager.Network network;
      private final int connectionIndex;
      private final int port;
      private long connectionGUID = -1L;
      private int requestId = 0;
      private long stateTime;
      private FakeClientManager.Client.State state;
      private String host;
      public static String luaChecksum = "";
      public static String scriptChecksum = "";

      private Client(FakeClientManager.Player var1, FakeClientManager.Network var2, int var3, int var4) {
         this.connectionIndex = var3;
         this.network = var2;
         this.player = var1;
         this.port = var4;

         try {
            this.host = InetAddress.getByName(connectionServerHost).getHostAddress();
            this.state = FakeClientManager.Client.State.CONNECT;
            Thread var5 = new Thread(ThreadGroups.Workers, this::updateThread, this.player.username);
            var5.setDaemon(true);
            var5.start();
         } catch (UnknownHostException var6) {
            this.state = FakeClientManager.Client.State.QUIT;
            var6.printStackTrace();
         }

      }

      private void updateThread() {
         FakeClientManager.info(this.player.movement.id, String.format("Start client (%d) %s:%d => %s:%d / \"%s\"", this.connectionIndex, "0.0.0.0", this.port, this.host, 16261, this.player.movement.description));
         FakeClientManager.sleep(this.player.movement.connectDelay);
         switch(this.player.movement.type) {
         case Circle:
            this.player.circleMovement();
            break;
         case Line:
            this.player.lineMovement();
            break;
         case AIAttackZombies:
            this.player.aiAttackZombiesMovement();
            break;
         case AIRunAwayFromZombies:
            this.player.aiRunAwayFromZombiesMovement();
            break;
         case AIRunToAnotherPlayers:
            this.player.aiRunToAnotherPlayersMovement();
            break;
         case AINormal:
            this.player.aiNormalMovement();
         }

         while(this.state != FakeClientManager.Client.State.QUIT) {
            this.update();
            FakeClientManager.sleep(1L);
         }

         FakeClientManager.info(this.player.movement.id, String.format("Stop client (%d) %s:%d => %s:%d / \"%s\"", this.connectionIndex, "0.0.0.0", this.port, this.host, 16261, this.player.movement.description));
      }

      private void updateTime() {
         this.stateTime = System.currentTimeMillis();
      }

      private long getServerTime() {
         return serverTimeShiftIsSet ? System.nanoTime() + serverTimeShift : 0L;
      }

      private boolean checkConnectionTimeout() {
         return System.currentTimeMillis() - this.stateTime > connectionTimeout;
      }

      private boolean checkConnectionDelay() {
         return System.currentTimeMillis() - this.stateTime > connectionDelay;
      }

      private void changeState(FakeClientManager.Client.State var1) {
         this.updateTime();
         FakeClientManager.log(this.player.movement.id, String.format("%s >> %s", this.state, var1));
         if (FakeClientManager.Client.State.RUN.equals(var1)) {
            this.player.movement.connect(this.player.OnlineID);
            if (this.player.teleportLimiter == null) {
               this.player.teleportLimiter = new UpdateLimit(this.player.movement.teleportDelay);
            }

            if (this.player.movement.id == statisticsClientID) {
               this.sendTimeSync();
               this.sendInjuries();
               this.sendStatisticsEnable(statisticsPeriod);
            }
         } else if (FakeClientManager.Client.State.DISCONNECT.equals(var1) && !FakeClientManager.Client.State.DISCONNECT.equals(this.state)) {
            this.player.movement.disconnect(this.player.OnlineID);
         }

         this.state = var1;
      }

      private void update() {
         switch(this.state) {
         case CONNECT:
            this.player.movement.timestamp = System.currentTimeMillis();
            this.network.connect(this.player.movement.id, this.host);
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case LOGIN:
            this.sendPlayerLogin();
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case PLAYER_CONNECT:
            this.sendPlayerConnect();
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case CHECKSUM:
            this.sendChecksum();
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case PLAYER_EXTRA_INFO:
            this.sendPlayerExtraInfo(this.player.movement.ghost, this.player.movement.hordeCreator != null);
            this.sendEquip();
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case LOAD:
            this.requestId = 0;
            this.requests.clear();
            this.requestFullUpdate();
            this.requestLargeAreaZip();
            this.changeState(FakeClientManager.Client.State.WAIT);
            break;
         case RUN:
            if (this.player.movement.doDisconnect() && this.player.movement.checkDisconnect()) {
               this.changeState(FakeClientManager.Client.State.DISCONNECT);
            } else {
               this.player.run();
            }
            break;
         case WAIT:
            if (this.checkConnectionTimeout()) {
               this.changeState(FakeClientManager.Client.State.DISCONNECT);
            }
            break;
         case DISCONNECT:
            if (this.network.isConnected()) {
               this.player.movement.timestamp = System.currentTimeMillis();
               this.network.disconnect(this.connectionGUID, this.player.movement.id, this.host);
            }

            if (this.player.movement.doReconnect() && this.player.movement.checkReconnect() || !this.player.movement.doReconnect() && this.checkConnectionDelay()) {
               this.changeState(FakeClientManager.Client.State.CONNECT);
            }
         case QUIT:
         }

      }

      private void receive(short var1, ByteBuffer var2) {
         PacketTypes.PacketType var3 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var1);
         FakeClientManager.Network.logUserPacket(this.player.movement.id, var1);
         switch(var3) {
         case PlayerConnect:
            if (this.receivePlayerConnect(var2)) {
               if (luaChecksum.isEmpty()) {
                  this.changeState(FakeClientManager.Client.State.PLAYER_EXTRA_INFO);
               } else {
                  this.changeState(FakeClientManager.Client.State.CHECKSUM);
               }
            }
            break;
         case ConnectionDetails:
            this.changeState(FakeClientManager.Client.State.LOAD);
            break;
         case ExtraInfo:
            if (this.receivePlayerExtraInfo(var2)) {
               this.changeState(FakeClientManager.Client.State.RUN);
            }
            break;
         case SentChunk:
            if (this.state == FakeClientManager.Client.State.WAIT && this.receiveChunkPart(var2)) {
               this.updateTime();
               if (this.allChunkPartsReceived()) {
                  this.changeState(FakeClientManager.Client.State.PLAYER_CONNECT);
               }
            }
            break;
         case NotRequiredInZip:
            if (this.state == FakeClientManager.Client.State.WAIT && this.receiveNotRequired(var2)) {
               this.updateTime();
               if (this.allChunkPartsReceived()) {
                  this.changeState(FakeClientManager.Client.State.PLAYER_CONNECT);
               }
            }
         case HitCharacter:
         default:
            break;
         case StatisticRequest:
            this.receiveStatistics(var2);
            break;
         case TimeSync:
            this.receiveTimeSync(var2);
            break;
         case SyncClock:
            this.receiveSyncClock(var2);
            break;
         case ZombieSimulation:
         case ZombieSimulationReliable:
            this.receiveZombieSimulation(var2);
            break;
         case PlayerUpdate:
         case PlayerUpdateReliable:
            this.player.playerManager.parsePlayer(var2);
            break;
         case PlayerTimeout:
            this.player.playerManager.parsePlayerTimeout(var2);
            break;
         case Kicked:
            this.receiveKicked(var2);
            break;
         case Checksum:
            this.receiveChecksum(var2);
            break;
         case KillZombie:
            this.receiveKillZombie(var2);
            break;
         case Teleport:
            this.receiveTeleport(var2);
         }

         var2.clear();
      }

      private void doPacket(short var1, ByteBuffer var2) {
         var2.put((byte)-122);
         var2.putShort(var1);
      }

      private void putUTF(ByteBuffer var1, String var2) {
         if (var2 == null) {
            var1.putShort((short)0);
         } else {
            byte[] var3 = var2.getBytes();
            var1.putShort((short)var3.length);
            var1.put(var3);
         }

      }

      private void putBoolean(ByteBuffer var1, boolean var2) {
         var1.put((byte)(var2 ? 1 : 0));
      }

      private void sendPlayerLogin() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.Login.getId(), var1);
         this.putUTF(var1, this.player.username);
         this.putUTF(var1, this.player.username);
         this.putUTF(var1, FakeClientManager.versionNumber);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendPlayerConnect() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.PlayerConnect.getId(), var1);
         this.writePlayerConnectData(var1);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void writePlayerConnectData(ByteBuffer var1) {
         var1.put((byte)0);
         var1.put((byte)13);
         var1.putFloat(this.player.x);
         var1.putFloat(this.player.y);
         var1.putFloat(this.player.z);
         var1.putInt(0);
         this.putUTF(var1, this.player.username);
         this.putUTF(var1, this.player.username);
         this.putUTF(var1, this.player.isFemale == 0 ? "Kate" : "Male");
         var1.putInt(this.player.isFemale);
         this.putUTF(var1, "fireofficer");
         var1.putInt(0);
         var1.putInt(4);
         this.putUTF(var1, "Sprinting");
         var1.putInt(1);
         this.putUTF(var1, "Fitness");
         var1.putInt(6);
         this.putUTF(var1, "Strength");
         var1.putInt(6);
         this.putUTF(var1, "Axe");
         var1.putInt(1);
         var1.put((byte)0);
         var1.put((byte)0);
         var1.put((byte)((int)Math.round(Math.random() * 5.0D)));
         var1.put((byte)0);
         var1.put((byte)0);
         var1.put((byte)0);
         var1.put((byte)0);
         int var2 = this.player.clothes.size();
         var1.put((byte)var2);
         Iterator var3 = this.player.clothes.iterator();

         while(var3.hasNext()) {
            FakeClientManager.Player.Clothes var4 = (FakeClientManager.Player.Clothes)var3.next();
            var1.put(var4.flags);
            this.putUTF(var1, "Base." + var4.name);
            this.putUTF(var1, (String)null);
            this.putUTF(var1, var4.name);
            var1.put((byte)-1);
            var1.put((byte)-1);
            var1.put((byte)-1);
            var1.put(var4.text);
            var1.putFloat(0.0F);
            var1.put((byte)0);
            var1.put((byte)0);
            var1.put((byte)0);
            var1.put((byte)0);
            var1.put((byte)0);
            var1.put((byte)0);
         }

         this.putUTF(var1, "fake_str");
         var1.putShort((short)0);
         var1.putInt(2);
         this.putUTF(var1, "Fit");
         this.putUTF(var1, "Stout");
         var1.putFloat(0.0F);
         var1.putInt(0);
         var1.putInt(0);
         var1.putInt(4);
         this.putUTF(var1, "Sprinting");
         var1.putFloat(75.0F);
         this.putUTF(var1, "Fitness");
         var1.putFloat(67500.0F);
         this.putUTF(var1, "Strength");
         var1.putFloat(67500.0F);
         this.putUTF(var1, "Axe");
         var1.putFloat(75.0F);
         var1.putInt(4);
         this.putUTF(var1, "Sprinting");
         var1.putInt(1);
         this.putUTF(var1, "Fitness");
         var1.putInt(6);
         this.putUTF(var1, "Strength");
         var1.putInt(6);
         this.putUTF(var1, "Axe");
         var1.putInt(1);
         var1.putInt(0);
         this.putBoolean(var1, true);
         this.putUTF(var1, "fake");
         var1.putFloat(this.player.tagColor.r);
         var1.putFloat(this.player.tagColor.g);
         var1.putFloat(this.player.tagColor.b);
         var1.putInt(0);
         var1.putDouble(0.0D);
         var1.putInt(0);
         this.putUTF(var1, this.player.username);
         var1.putFloat(this.player.speakColor.r);
         var1.putFloat(this.player.speakColor.g);
         var1.putFloat(this.player.speakColor.b);
         this.putBoolean(var1, true);
         this.putBoolean(var1, false);
         var1.put((byte)0);
         var1.put((byte)0);
         var1.putInt(0);
         var1.putInt(0);
      }

      private void sendPlayerExtraInfo(boolean var1, boolean var2) {
         ByteBuffer var3 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.ExtraInfo.getId(), var3);
         var3.putShort(this.player.OnlineID);
         this.putUTF(var3, var2 ? "admin" : "");
         var3.put((byte)0);
         var3.put((byte)(var1 ? 1 : 0));
         var3.put((byte)0);
         var3.put((byte)0);
         var3.put((byte)0);
         var3.put((byte)0);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendEquip() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.Equip.getId(), var1);
         var1.put((byte)0);
         var1.put((byte)0);
         var1.put((byte)1);
         var1.putInt(16);
         var1.putShort(this.player.registry_id);
         var1.put((byte)1);
         var1.putInt(this.player.weapon_id);
         var1.put((byte)0);
         var1.putInt(0);
         var1.putInt(0);
         var1.put((byte)0);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendChatMessage(String var1) {
         ByteBuffer var2 = this.network.startPacket();
         var2.putShort(this.player.OnlineID);
         var2.putInt(2);
         this.putUTF(var2, this.player.username);
         this.putUTF(var2, var1);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private int getBooleanVariables() {
         int var1 = 0;
         if (this.player.movement.speed > 0.0F) {
            switch(this.player.movement.motion) {
            case Aim:
               var1 |= 64;
               break;
            case Sneak:
               var1 |= 1;
               break;
            case SneakRun:
               var1 |= 17;
               break;
            case Run:
               var1 |= 16;
               break;
            case Sprint:
               var1 |= 32;
            }

            var1 |= 17408;
         }

         return var1;
      }

      private void sendPlayer(NetworkCharacter.Transform var1, int var2, Vector2 var3) {
         PlayerPacket var4 = new PlayerPacket();
         var4.id = this.player.OnlineID;
         var4.x = var1.position.x;
         var4.y = var1.position.y;
         var4.z = (byte)((int)this.player.z);
         var4.direction = var3.getDirection();
         var4.usePathFinder = false;
         var4.moveType = NetworkVariables.PredictionTypes.None;
         var4.VehicleID = -1;
         var4.VehicleSeat = -1;
         var4.booleanVariables = this.getBooleanVariables();
         var4.footstepSoundRadius = 0;
         var4.bleedingLevel = 0;
         var4.realx = this.player.x;
         var4.realy = this.player.y;
         var4.realz = (byte)((int)this.player.z);
         var4.realdir = (byte)IsoDirections.fromAngleActual(this.player.direction).index();
         var4.realt = var2;
         var4.collidePointX = -1.0F;
         var4.collidePointY = -1.0F;
         ByteBuffer var5 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.PlayerUpdateReliable.getId(), var5);
         ByteBufferWriter var6 = new ByteBufferWriter(var5);
         var4.write(var6);
         this.network.endPacket(this.connectionGUID);
      }

      private boolean receivePlayerConnect(ByteBuffer var1) {
         short var2 = var1.getShort();
         if (var2 == -1) {
            byte var3 = var1.get();
            var2 = var1.getShort();
            this.player.OnlineID = var2;
            return true;
         } else {
            return false;
         }
      }

      private boolean receivePlayerExtraInfo(ByteBuffer var1) {
         short var2 = var1.getShort();
         return var2 == this.player.OnlineID;
      }

      private boolean receiveChunkPart(ByteBuffer var1) {
         boolean var2 = false;
         int var3 = var1.getInt();
         int var4 = var1.getInt();
         int var5 = var1.getInt();
         int var6 = var1.getInt();
         int var7 = var1.getInt();
         int var8 = var1.getInt();
         if (this.requests.remove(var3) != null) {
            var2 = true;
         }

         return var2;
      }

      private boolean receiveNotRequired(ByteBuffer var1) {
         boolean var2 = false;
         int var3 = var1.getInt();

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = var1.getInt();
            boolean var6 = var1.get() == 1;
            if (this.requests.remove(var5) != null) {
               var2 = true;
            }
         }

         return var2;
      }

      private boolean allChunkPartsReceived() {
         return this.requests.size() == 0;
      }

      private void addChunkRequest(int var1, int var2, int var3, int var4) {
         FakeClientManager.Client.Request var5 = new FakeClientManager.Client.Request(var1, var2, this.requestId);
         ++this.requestId;
         this.requests.put(var5.id, var5);
      }

      private void requestZipList() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.RequestZipList.getId(), var1);
         var1.putInt(this.requests.size());
         Iterator var2 = this.requests.values().iterator();

         while(var2.hasNext()) {
            FakeClientManager.Client.Request var3 = (FakeClientManager.Client.Request)var2.next();
            var1.putInt(var3.id);
            var1.putInt(var3.wx);
            var1.putInt(var3.wy);
            var1.putLong(var3.crc);
         }

         this.network.endPacket(this.connectionGUID);
      }

      private void requestLargeAreaZip() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.RequestLargeAreaZip.getId(), var1);
         var1.putInt(this.player.WorldX);
         var1.putInt(this.player.WorldY);
         var1.putInt(13);
         this.network.endPacketImmediate(this.connectionGUID);
         int var2 = this.player.WorldX - 6 + 2;
         int var3 = this.player.WorldY - 6 + 2;
         int var4 = this.player.WorldX + 6 + 2;
         int var5 = this.player.WorldY + 6 + 2;

         for(int var6 = var3; var6 <= var5; ++var6) {
            for(int var7 = var2; var7 <= var4; ++var7) {
               FakeClientManager.Client.Request var8 = new FakeClientManager.Client.Request(var7, var6, this.requestId);
               ++this.requestId;
               this.requests.put(var8.id, var8);
            }
         }

         this.requestZipList();
      }

      private void requestFullUpdate() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.IsoRegionClientRequestFullUpdate.getId(), var1);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void requestChunkObjectState() {
         Iterator var1 = this.requests.values().iterator();

         while(var1.hasNext()) {
            FakeClientManager.Client.Request var2 = (FakeClientManager.Client.Request)var1.next();
            ByteBuffer var3 = this.network.startPacket();
            this.doPacket(PacketTypes.PacketType.ChunkObjectState.getId(), var3);
            var3.putShort((short)var2.wx);
            var3.putShort((short)var2.wy);
            this.network.endPacket(this.connectionGUID);
         }

      }

      private void requestChunks() {
         if (!this.requests.isEmpty()) {
            this.requestZipList();
            this.requestChunkObjectState();
            this.requests.clear();
         }

      }

      private void sendStatisticsEnable(int var1) {
         ByteBuffer var2 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.StatisticRequest.getId(), var2);
         var2.put((byte)3);
         var2.putInt(var1);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void receiveStatistics(ByteBuffer var1) {
         long var2 = var1.getLong();
         long var4 = var1.getLong();
         long var6 = var1.getLong();
         long var8 = var1.getLong();
         long var10 = var1.getLong();
         long var12 = var1.getLong();
         long var14 = var1.getLong();
         long var16 = var1.getLong();
         long var18 = var1.getLong();
         FakeClientManager.info(this.player.movement.id, String.format("ServerStats: con=[%2d] fps=[%2d] tps=[%2d] upt=[%4d-%4d/%4d], c1=[%d] c2=[%d] c3=[%d]", var12, var8, var10, var2, var4, var6, var14, var16, var18));
      }

      private void sendTimeSync() {
         ByteBuffer var1 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.TimeSync.getId(), var1);
         long var2 = System.nanoTime();
         var1.putLong(var2);
         var1.putLong(0L);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void receiveTimeSync(ByteBuffer var1) {
         long var2 = var1.getLong();
         long var4 = var1.getLong();
         long var6 = System.nanoTime();
         long var8 = var6 - var2;
         long var10 = var4 - var6 + var8 / 2L;
         long var12 = serverTimeShift;
         if (!serverTimeShiftIsSet) {
            serverTimeShift = var10;
         } else {
            serverTimeShift = (long)((float)serverTimeShift + (float)(var10 - serverTimeShift) * 0.05F);
         }

         long var14 = 10000000L;
         if (Math.abs(serverTimeShift - var12) > var14) {
            this.sendTimeSync();
         } else {
            serverTimeShiftIsSet = true;
         }

      }

      private void receiveSyncClock(ByteBuffer var1) {
         FakeClientManager.trace(this.player.movement.id, String.format("Player %3d sync clock", this.player.OnlineID));
      }

      private void receiveKicked(ByteBuffer var1) {
         String var2 = FakeClientManager.ReadStringUTF(var1);
         FakeClientManager.info(this.player.movement.id, String.format("Client kicked. Reason: %s", var2));
      }

      private void receiveChecksum(ByteBuffer var1) {
         FakeClientManager.trace(this.player.movement.id, String.format("Player %3d receive Checksum", this.player.OnlineID));
         short var2 = var1.getShort();
         boolean var3 = var1.get() == 1;
         boolean var4 = var1.get() == 1;
         if (var2 != 1 || !var3 || !var4) {
            FakeClientManager.info(this.player.movement.id, String.format("checksum lua: %b, script: %b", var3, var4));
         }

         this.changeState(FakeClientManager.Client.State.PLAYER_EXTRA_INFO);
      }

      private void receiveKillZombie(ByteBuffer var1) {
         FakeClientManager.trace(this.player.movement.id, String.format("Player %3d receive KillZombie", this.player.OnlineID));
         short var2 = var1.getShort();
         FakeClientManager.Zombie var3 = (FakeClientManager.Zombie)this.player.simulator.zombies.get(Integer.valueOf(var2));
         if (var3 != null) {
            this.player.simulator.zombies4Delete.add(var3);
         }

      }

      private void receiveTeleport(ByteBuffer var1) {
         byte var2 = var1.get();
         float var3 = var1.getFloat();
         float var4 = var1.getFloat();
         float var5 = var1.getFloat();
         FakeClientManager.info(this.player.movement.id, String.format("Player %3d teleport to (%d, %d)", this.player.OnlineID, (int)var3, (int)var4));
         this.player.x = var3;
         this.player.y = var4;
      }

      private void receiveZombieSimulation(ByteBuffer var1) {
         this.player.simulator.clear();
         boolean var2 = var1.get() == 1;
         short var3 = var1.getShort();

         short var4;
         short var5;
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var1.getShort();
            FakeClientManager.Zombie var6 = (FakeClientManager.Zombie)this.player.simulator.zombies.get(Integer.valueOf(var5));
            this.player.simulator.zombies4Delete.add(var6);
         }

         var4 = var1.getShort();

         for(var5 = 0; var5 < var4; ++var5) {
            short var7 = var1.getShort();
            this.player.simulator.add(var7);
         }

         this.player.simulator.receivePacket(var1);
         this.player.simulator.process();
      }

      private void sendInjuries() {
         SyncInjuriesPacket var1 = new SyncInjuriesPacket();
         var1.id = this.player.OnlineID;
         var1.strafeSpeed = 1.0F;
         var1.walkSpeed = 1.0F;
         var1.walkInjury = 0.0F;
         ByteBuffer var2 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.SyncInjuries.getId(), var2);
         ByteBufferWriter var3 = new ByteBufferWriter(var2);
         var1.write(var3);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendChecksum() {
         if (!luaChecksum.isEmpty()) {
            FakeClientManager.trace(this.player.movement.id, String.format("Player %3d sendChecksum", this.player.OnlineID));
            ByteBuffer var1 = this.network.startPacket();
            this.doPacket(PacketTypes.PacketType.Checksum.getId(), var1);
            var1.putShort((short)1);
            this.putUTF(var1, luaChecksum);
            this.putUTF(var1, scriptChecksum);
            this.network.endPacketImmediate(this.connectionGUID);
         }
      }

      public void sendCommand(String var1) {
         ByteBuffer var2 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.ReceiveCommand.getId(), var2);
         FakeClientManager.WriteStringUTF(var2, var1);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendEventPacket(short var1, int var2, int var3, int var4, byte var5, String var6) {
         ByteBuffer var7 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.EventPacket.getId(), var7);
         var7.putShort(var1);
         var7.putFloat((float)var2);
         var7.putFloat((float)var3);
         var7.putFloat((float)var4);
         var7.put(var5);
         FakeClientManager.WriteStringUTF(var7, var6);
         FakeClientManager.WriteStringUTF(var7, "");
         FakeClientManager.WriteStringUTF(var7, "");
         FakeClientManager.WriteStringUTF(var7, "");
         var7.putFloat(1.0F);
         var7.putFloat(1.0F);
         var7.putFloat(0.0F);
         var7.putInt(0);
         var7.putShort((short)0);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private void sendWorldSound4Player(int var1, int var2, int var3, int var4, int var5) {
         ByteBuffer var6 = this.network.startPacket();
         this.doPacket(PacketTypes.PacketType.WorldSound.getId(), var6);
         var6.putInt(var1);
         var6.putInt(var2);
         var6.putInt(var3);
         var6.putInt(var4);
         var6.putInt(var5);
         var6.put((byte)0);
         var6.putFloat(0.0F);
         var6.putFloat(1.0F);
         var6.put((byte)0);
         this.network.endPacketImmediate(this.connectionGUID);
      }

      private static enum State {
         CONNECT,
         LOGIN,
         CHECKSUM,
         PLAYER_CONNECT,
         PLAYER_EXTRA_INFO,
         LOAD,
         RUN,
         WAIT,
         DISCONNECT,
         QUIT;

         // $FF: synthetic method
         private static FakeClientManager.Client.State[] $values() {
            return new FakeClientManager.Client.State[]{CONNECT, LOGIN, CHECKSUM, PLAYER_CONNECT, PLAYER_EXTRA_INFO, LOAD, RUN, WAIT, DISCONNECT, QUIT};
         }
      }

      private static final class Request {
         private final int id;
         private final int wx;
         private final int wy;
         private final long crc;

         private Request(int var1, int var2, int var3) {
            this.id = var3;
            this.wx = var1;
            this.wy = var2;
            CRC32 var4 = new CRC32();
            var4.reset();
            var4.update(String.format("map_%d_%d.bin", var1, var2).getBytes());
            this.crc = var4.getValue();
         }
      }
   }

   private static class ZombieSimulator {
      public static FakeClientManager.ZombieSimulator.Behaviour behaviour;
      public static int deleteZombieDistanceSquared;
      public static int forgotZombieDistanceSquared;
      public static int canSeeZombieDistanceSquared;
      public static int seeZombieDistanceSquared;
      private static boolean canChangeTarget;
      private static int updatePeriod;
      private static int attackPeriod;
      public static int maxZombiesPerUpdate;
      private final ByteBuffer bb = ByteBuffer.allocate(1000000);
      private UpdateLimit updateLimiter;
      private UpdateLimit attackLimiter;
      private FakeClientManager.Player player;
      private final ZombiePacket zombiePacket;
      private HashSet authoriseZombiesCurrent;
      private HashSet authoriseZombiesLast;
      private final ArrayList unknownZombies;
      private final HashMap zombies;
      private final ArrayDeque zombies4Add;
      private final ArrayDeque zombies4Delete;
      private final HashSet authoriseZombies;
      private final ArrayDeque SendQueue;
      private static Vector2 tmpDir;

      public ZombieSimulator(FakeClientManager.Player var1) {
         this.updateLimiter = new UpdateLimit((long)updatePeriod);
         this.attackLimiter = new UpdateLimit((long)attackPeriod);
         this.player = null;
         this.zombiePacket = new ZombiePacket();
         this.authoriseZombiesCurrent = new HashSet();
         this.authoriseZombiesLast = new HashSet();
         this.unknownZombies = new ArrayList();
         this.zombies = new HashMap();
         this.zombies4Add = new ArrayDeque();
         this.zombies4Delete = new ArrayDeque();
         this.authoriseZombies = new HashSet();
         this.SendQueue = new ArrayDeque();
         this.player = var1;
      }

      public void becomeLocal(FakeClientManager.Zombie var1) {
         var1.localOwnership = true;
      }

      public void becomeRemote(FakeClientManager.Zombie var1) {
         var1.localOwnership = false;
      }

      public void clear() {
         HashSet var1 = this.authoriseZombiesCurrent;
         this.authoriseZombiesCurrent = this.authoriseZombiesLast;
         this.authoriseZombiesLast = var1;
         this.authoriseZombiesLast.removeIf((var1x) -> {
            return this.zombies.get(Integer.valueOf(var1x)) == null;
         });
         this.authoriseZombiesCurrent.clear();
      }

      public void add(short var1) {
         this.authoriseZombiesCurrent.add(var1);
      }

      public void receivePacket(ByteBuffer var1) {
         short var2 = var1.getShort();

         for(short var3 = 0; var3 < var2; ++var3) {
            this.parseZombie(var1);
         }

      }

      private void parseZombie(ByteBuffer var1) {
         ZombiePacket var2 = this.zombiePacket;
         var2.parse(var1);
         FakeClientManager.Zombie var3 = (FakeClientManager.Zombie)this.zombies.get(Integer.valueOf(var2.id));
         if (!this.authoriseZombies.contains(var2.id) || var3 == null) {
            if (var3 == null) {
               var3 = new FakeClientManager.Zombie(var2.id);
               this.zombies4Add.add(var3);
               FakeClientManager.trace(this.player.movement.id, String.format("New zombie %s", var3.OnlineID));
            }

            var3.lastUpdate = System.currentTimeMillis();
            var3.zombiePacket.copy(var2);
            var3.x = var2.realX;
            var3.y = var2.realY;
            var3.z = (float)var2.realZ;
         }
      }

      public void process() {
         SetView var1 = Sets.difference(this.authoriseZombiesCurrent, this.authoriseZombiesLast);
         UnmodifiableIterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Short var3 = (Short)var2.next();
            FakeClientManager.Zombie var4 = (FakeClientManager.Zombie)this.zombies.get(Integer.valueOf(var3));
            if (var4 != null) {
               this.becomeLocal(var4);
            } else if (!this.unknownZombies.contains(var3)) {
               this.unknownZombies.add(var3);
            }
         }

         SetView var8 = Sets.difference(this.authoriseZombiesLast, this.authoriseZombiesCurrent);
         UnmodifiableIterator var9 = var8.iterator();

         while(var9.hasNext()) {
            Short var10 = (Short)var9.next();
            FakeClientManager.Zombie var5 = (FakeClientManager.Zombie)this.zombies.get(Integer.valueOf(var10));
            if (var5 != null) {
               this.becomeRemote(var5);
            }
         }

         synchronized(this.authoriseZombies) {
            this.authoriseZombies.clear();
            this.authoriseZombies.addAll(this.authoriseZombiesCurrent);
         }
      }

      public void send() {
         if (this.authoriseZombies.size() != 0 || this.unknownZombies.size() != 0) {
            FakeClientManager.Zombie var4;
            if (this.SendQueue.isEmpty()) {
               synchronized(this.authoriseZombies) {
                  Iterator var2 = this.authoriseZombies.iterator();

                  while(var2.hasNext()) {
                     Short var3 = (Short)var2.next();
                     var4 = (FakeClientManager.Zombie)this.zombies.get(Integer.valueOf(var3));
                     if (var4 != null && var4.OnlineID != -1) {
                        this.SendQueue.add(var4);
                     }
                  }
               }
            }

            this.bb.clear();
            this.bb.putShort((short)0);
            int var1 = this.unknownZombies.size();
            this.bb.putShort((short)var1);

            int var7;
            for(var7 = 0; var7 < this.unknownZombies.size(); ++var7) {
               if (this.unknownZombies.get(var7) == null) {
                  return;
               }

               this.bb.putShort((Short)this.unknownZombies.get(var7));
            }

            this.unknownZombies.clear();
            var7 = this.bb.position();
            this.bb.putShort((short)maxZombiesPerUpdate);
            int var8 = 0;

            while(!this.SendQueue.isEmpty()) {
               var4 = (FakeClientManager.Zombie)this.SendQueue.poll();
               if (var4.OnlineID != -1) {
                  var4.zombiePacket.write(this.bb);
                  ++var8;
                  if (var8 >= maxZombiesPerUpdate) {
                     break;
                  }
               }
            }

            if (var8 < maxZombiesPerUpdate) {
               int var9 = this.bb.position();
               this.bb.position(var7);
               this.bb.putShort((short)var8);
               this.bb.position(var9);
            }

            if (var8 > 0 || var1 > 0) {
               ByteBuffer var10 = this.player.client.network.startPacket();
               this.player.client.doPacket(PacketTypes.PacketType.ZombieSimulation.getId(), var10);
               var10.put(this.bb.array(), 0, this.bb.position());
               this.player.client.network.endPacketSuperHighUnreliable(this.player.client.connectionGUID);
            }

         }
      }

      private void simulate(Integer var1, FakeClientManager.Zombie var2) {
         float var3 = IsoUtils.DistanceToSquared(this.player.x, this.player.y, var2.x, var2.y);
         if (!(var3 > (float)deleteZombieDistanceSquared) && (var2.localOwnership || var2.lastUpdate + 5000L >= System.currentTimeMillis())) {
            tmpDir.set(-var2.x + this.player.x, -var2.y + this.player.y);
            float var4;
            if (var2.isMoving) {
               var4 = 0.2F;
               var2.x = PZMath.lerp(var2.x, var2.zombiePacket.x, var4);
               var2.y = PZMath.lerp(var2.y, var2.zombiePacket.y, var4);
               var2.z = 0.0F;
               var2.dir = IsoDirections.fromAngle(tmpDir);
            }

            if (canChangeTarget) {
               synchronized(this.player.playerManager.players) {
                  Iterator var5 = this.player.playerManager.players.values().iterator();

                  while(var5.hasNext()) {
                     FakeClientManager.PlayerManager.RemotePlayer var6 = (FakeClientManager.PlayerManager.RemotePlayer)var5.next();
                     float var7 = IsoUtils.DistanceToSquared(var6.x, var6.y, var2.x, var2.y);
                     if (var7 < (float)seeZombieDistanceSquared) {
                        var2.zombiePacket.target = var6.OnlineID;
                        break;
                     }
                  }
               }
            } else {
               var2.zombiePacket.target = this.player.OnlineID;
            }

            if (behaviour == FakeClientManager.ZombieSimulator.Behaviour.Stay) {
               var2.isMoving = false;
            } else if (behaviour == FakeClientManager.ZombieSimulator.Behaviour.Normal) {
               if (var3 > (float)forgotZombieDistanceSquared) {
                  var2.isMoving = false;
               }

               if (var3 < (float)canSeeZombieDistanceSquared && (Rand.Next(100) < 1 || var2.dir == IsoDirections.fromAngle(tmpDir))) {
                  var2.isMoving = true;
               }

               if (var3 < (float)seeZombieDistanceSquared) {
                  var2.isMoving = true;
               }
            } else {
               var2.isMoving = true;
            }

            var4 = 0.0F;
            if (var2.isMoving) {
               Vector2 var10 = var2.dir.ToVector();
               var4 = 3.0F;
               if (var3 < 100.0F) {
                  var4 = 6.0F;
               }

               long var11 = System.currentTimeMillis() - var2.lastUpdate;
               var2.zombiePacket.x = var2.x + var10.x * (float)var11 * 0.001F * var4;
               var2.zombiePacket.y = var2.y + var10.y * (float)var11 * 0.001F * var4;
               var2.zombiePacket.z = (byte)((int)var2.z);
               var2.zombiePacket.moveType = NetworkVariables.PredictionTypes.Moving;
            } else {
               var2.zombiePacket.x = var2.x;
               var2.zombiePacket.y = var2.y;
               var2.zombiePacket.z = (byte)((int)var2.z);
               var2.zombiePacket.moveType = NetworkVariables.PredictionTypes.Static;
            }

            var2.zombiePacket.booleanVariables = 0;
            if (var3 < 100.0F) {
               ZombiePacket var10000 = var2.zombiePacket;
               var10000.booleanVariables = (short)(var10000.booleanVariables | 2);
            }

            var2.zombiePacket.timeSinceSeenFlesh = var2.isMoving ? 0 : 100000;
            var2.zombiePacket.smParamTargetAngle = 0;
            var2.zombiePacket.speedMod = 1000;
            var2.zombiePacket.walkType = NetworkVariables.WalkType.values()[var2.walkType];
            var2.zombiePacket.realX = var2.x;
            var2.zombiePacket.realY = var2.y;
            var2.zombiePacket.realZ = (byte)((int)var2.z);
            var2.zombiePacket.realHealth = (short)((int)(var2.health * 1000.0F));
            var2.zombiePacket.realState = NetworkVariables.ZombieState.fromString("fakezombie-" + behaviour.toString().toLowerCase());
            if (var2.isMoving) {
               var2.zombiePacket.pfbType = 1;
               var2.zombiePacket.pfbTarget = this.player.OnlineID;
            } else {
               var2.zombiePacket.pfbType = 0;
            }

            if (var3 < 2.0F && this.attackLimiter.Check()) {
               var2.health -= FakeClientManager.Player.damage;
               this.sendHitCharacter(var2, FakeClientManager.Player.damage);
               if (var2.health <= 0.0F) {
                  this.player.client.sendChatMessage("DIE!!");
                  this.zombies4Delete.add(var2);
               }
            }

            var2.lastUpdate = System.currentTimeMillis();
         } else {
            this.zombies4Delete.add(var2);
         }
      }

      private void writeHitInfoToZombie(ByteBuffer var1, short var2, float var3, float var4, float var5) {
         var1.put((byte)2);
         var1.putShort(var2);
         var1.put((byte)0);
         var1.putFloat(var3);
         var1.putFloat(var4);
         var1.putFloat(0.0F);
         var1.putFloat(var5);
         var1.putFloat(1.0F);
         var1.putInt(100);
      }

      private void sendHitCharacter(FakeClientManager.Zombie var1, float var2) {
         boolean var3 = false;
         ByteBuffer var4 = this.player.client.network.startPacket();
         this.player.client.doPacket(PacketTypes.PacketType.HitCharacter.getId(), var4);
         var4.put((byte)3);
         var4.putShort(this.player.OnlineID);
         var4.putShort((short)0);
         var4.putFloat(this.player.x);
         var4.putFloat(this.player.y);
         var4.putFloat(this.player.z);
         var4.putFloat(this.player.direction.x);
         var4.putFloat(this.player.direction.y);
         FakeClientManager.WriteStringUTF(var4, "");
         FakeClientManager.WriteStringUTF(var4, "");
         FakeClientManager.WriteStringUTF(var4, "");
         var4.putShort((short)((this.player.weapon_isBareHeads ? 2 : 0) + (var3 ? 8 : 0)));
         var4.putFloat(1.0F);
         var4.putFloat(1.0F);
         var4.putFloat(1.0F);
         FakeClientManager.WriteStringUTF(var4, "default");
         byte var5 = 0;
         byte var8 = (byte)(var5 | (byte)(this.player.weapon_isBareHeads ? 9 : 0));
         var4.put(var8);
         var4.put((byte)0);
         var4.putShort((short)0);
         var4.putFloat(1.0F);
         var4.putInt(0);
         byte var6 = 1;
         var4.put(var6);

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            this.writeHitInfoToZombie(var4, var1.OnlineID, var1.x, var1.y, var2);
         }

         var6 = 0;
         var4.put(var6);
         var6 = 1;
         var4.put(var6);

         for(var7 = 0; var7 < var6; ++var7) {
            this.writeHitInfoToZombie(var4, var1.OnlineID, var1.x, var1.y, var2);
         }

         if (!this.player.weapon_isBareHeads) {
            var4.put((byte)0);
         } else {
            var4.put((byte)1);
            var4.putShort(this.player.registry_id);
            var4.put((byte)1);
            var4.putInt(this.player.weapon_id);
            var4.put((byte)0);
            var4.putInt(0);
            var4.putInt(0);
         }

         var4.putShort(var1.OnlineID);
         var4.putShort((short)(var2 >= var1.health ? 3 : 0));
         var4.putFloat(var1.x);
         var4.putFloat(var1.y);
         var4.putFloat(var1.z);
         var4.putFloat(var1.dir.ToVector().x);
         var4.putFloat(var1.dir.ToVector().y);
         FakeClientManager.WriteStringUTF(var4, "");
         FakeClientManager.WriteStringUTF(var4, "");
         FakeClientManager.WriteStringUTF(var4, "");
         var4.putShort((short)0);
         FakeClientManager.WriteStringUTF(var4, "");
         FakeClientManager.WriteStringUTF(var4, "FRONT");
         var4.put((byte)0);
         var4.putFloat(var2);
         var4.putFloat(1.0F);
         var4.putFloat(this.player.direction.x);
         var4.putFloat(this.player.direction.y);
         var4.putFloat(1.0F);
         var4.put((byte)0);
         if (tmpDir.getLength() > 0.0F) {
            var1.dropPositionX = var1.x + tmpDir.x / tmpDir.getLength();
            var1.dropPositionY = var1.y + tmpDir.y / tmpDir.getLength();
         } else {
            var1.dropPositionX = var1.x;
            var1.dropPositionY = var1.y;
         }

         var4.putFloat(var1.dropPositionX);
         var4.putFloat(var1.dropPositionY);
         var4.put((byte)((int)var1.z));
         var4.putFloat(var1.dir.toAngle());
         this.player.client.network.endPacketImmediate(this.player.client.connectionGUID);
      }

      private void sendSendDeadZombie(FakeClientManager.Zombie var1) {
         ByteBuffer var2 = this.player.client.network.startPacket();
         this.player.client.doPacket(PacketTypes.PacketType.ZombieDeath.getId(), var2);
         var2.putShort(var1.OnlineID);
         var2.putFloat(var1.x);
         var2.putFloat(var1.y);
         var2.putFloat(var1.z);
         var2.putFloat(var1.dir.toAngle());
         var2.put((byte)var1.dir.index());
         var2.put((byte)0);
         var2.put((byte)0);
         var2.put((byte)0);
         this.player.client.network.endPacketImmediate(this.player.client.connectionGUID);
      }

      public void simulateAll() {
         FakeClientManager.Zombie var1;
         while(!this.zombies4Add.isEmpty()) {
            var1 = (FakeClientManager.Zombie)this.zombies4Add.poll();
            this.zombies.put(Integer.valueOf(var1.OnlineID), var1);
         }

         this.zombies.forEach(this::simulate);

         while(!this.zombies4Delete.isEmpty()) {
            var1 = (FakeClientManager.Zombie)this.zombies4Delete.poll();
            this.zombies.remove(Integer.valueOf(var1.OnlineID));
         }

      }

      public void update() {
         if (this.updateLimiter.Check()) {
            this.simulateAll();
            this.send();
         }

      }

      static {
         behaviour = FakeClientManager.ZombieSimulator.Behaviour.Stay;
         deleteZombieDistanceSquared = 10000;
         forgotZombieDistanceSquared = 225;
         canSeeZombieDistanceSquared = 100;
         seeZombieDistanceSquared = 25;
         canChangeTarget = true;
         updatePeriod = 100;
         attackPeriod = 1000;
         maxZombiesPerUpdate = 300;
         tmpDir = new Vector2();
      }

      private static enum Behaviour {
         Stay,
         Normal,
         Attack;

         // $FF: synthetic method
         private static FakeClientManager.ZombieSimulator.Behaviour[] $values() {
            return new FakeClientManager.ZombieSimulator.Behaviour[]{Stay, Normal, Attack};
         }
      }
   }

   private static class Player {
      private static final int cellSize = 50;
      private static final int spawnMinX = 3550;
      private static final int spawnMaxX = 14450;
      private static final int spawnMinY = 5050;
      private static final int spawnMaxY = 12950;
      private static final int ChunkGridWidth = 13;
      private static final int ChunksPerWidth = 10;
      private static int fps = 60;
      private static int predictInterval = 1000;
      private static float damage = 1.0F;
      private final NetworkCharacter networkCharacter;
      private final UpdateLimit updateLimiter;
      private final UpdateLimit predictLimiter;
      private final UpdateLimit timeSyncLimiter;
      private final FakeClientManager.Client client;
      private final FakeClientManager.Movement movement;
      private final ArrayList clothes;
      private final String username;
      private final int isFemale;
      private final Color tagColor;
      private final Color speakColor;
      private UpdateLimit teleportLimiter;
      private short OnlineID;
      private float x;
      private float y;
      private final float z;
      private Vector2 direction;
      private int WorldX;
      private int WorldY;
      private float angle;
      private FakeClientManager.ZombieSimulator simulator;
      private FakeClientManager.PlayerManager playerManager;
      private boolean weapon_isBareHeads = false;
      private int weapon_id = 837602032;
      private short registry_id = 1202;
      static float distance = 0.0F;
      private int lastPlayerForHello = -1;

      private Player(FakeClientManager.Movement var1, FakeClientManager.Network var2, int var3, int var4) {
         this.username = String.format("Client%d", var1.id);
         this.tagColor = Colors.SkyBlue;
         this.speakColor = Colors.GetRandomColor();
         this.isFemale = (int)Math.round(Math.random());
         this.OnlineID = -1;
         this.clothes = new ArrayList();
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)11, (byte)0, "Shirt_FormalWhite"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)13, (byte)3, "Tie_Full"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)11, (byte)0, "Socks_Ankle"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)13, (byte)0, "Trousers_Suit"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)13, (byte)0, "Suit_Jacket"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)11, (byte)0, "Shoes_Black"));
         this.clothes.add(new FakeClientManager.Player.Clothes((byte)11, (byte)0, "Glasses_Sun"));
         this.WorldX = (int)this.x / 10;
         this.WorldY = (int)this.y / 10;
         this.movement = var1;
         this.z = 0.0F;
         this.angle = 0.0F;
         this.x = var1.spawn.x;
         this.y = var1.spawn.y;
         this.direction = var1.direction.ToVector();
         this.networkCharacter = new NetworkCharacter();
         this.simulator = new FakeClientManager.ZombieSimulator(this);
         this.playerManager = new FakeClientManager.PlayerManager(this);
         this.client = new FakeClientManager.Client(this, var2, var3, var4);
         var2.createdClients.put(var3, this.client);
         this.updateLimiter = new UpdateLimit((long)(1000 / fps));
         this.predictLimiter = new UpdateLimit((long)((float)predictInterval * 0.6F));
         this.timeSyncLimiter = new UpdateLimit(10000L);
      }

      private float getDistance(float var1) {
         return var1 / 3.6F / (float)fps;
      }

      private void teleportMovement() {
         float var1 = this.movement.destination.x;
         float var2 = this.movement.destination.y;
         FakeClientManager.info(this.movement.id, String.format("Player %3d teleport (%9.3f,%9.3f) => (%9.3f,%9.3f) / %9.3f, next in %.3fs", this.OnlineID, this.x, this.y, var1, var2, Math.sqrt(Math.pow((double)(var1 - this.x), 2.0D) + Math.pow((double)(var2 - this.y), 2.0D)), (float)this.movement.teleportDelay / 1000.0F));
         this.x = var1;
         this.y = var2;
         this.angle = 0.0F;
         this.teleportLimiter.Reset(this.movement.teleportDelay);
      }

      private void lineMovement() {
         distance = this.getDistance(this.movement.speed);
         this.direction.set(this.movement.destination.x - this.x, this.movement.destination.y - this.y);
         this.direction.normalize();
         float var1 = this.x + distance * this.direction.x;
         float var2 = this.y + distance * this.direction.y;
         if (this.x < this.movement.destination.x && var1 > this.movement.destination.x || this.x > this.movement.destination.x && var1 < this.movement.destination.x || this.y < this.movement.destination.y && var2 > this.movement.destination.y || this.y > this.movement.destination.y && var2 < this.movement.destination.y) {
            var1 = this.movement.destination.x;
            var2 = this.movement.destination.y;
         }

         this.x = var1;
         this.y = var2;
      }

      private void circleMovement() {
         this.angle = (this.angle + (float)(2.0D * Math.asin((double)(this.getDistance(this.movement.speed) / 2.0F / (float)this.movement.radius)))) % 360.0F;
         float var1 = this.movement.spawn.x + (float)((double)this.movement.radius * Math.sin((double)this.angle));
         float var2 = this.movement.spawn.y + (float)((double)this.movement.radius * Math.cos((double)this.angle));
         this.x = var1;
         this.y = var2;
      }

      private FakeClientManager.Zombie getNearestZombie() {
         FakeClientManager.Zombie var1 = null;
         float var2 = Float.POSITIVE_INFINITY;
         Iterator var3 = this.simulator.zombies.values().iterator();

         while(var3.hasNext()) {
            FakeClientManager.Zombie var4 = (FakeClientManager.Zombie)var3.next();
            float var5 = IsoUtils.DistanceToSquared(this.x, this.y, var4.x, var4.y);
            if (var5 < var2) {
               var1 = var4;
               var2 = var5;
            }
         }

         return var1;
      }

      private FakeClientManager.Zombie getNearestZombie(FakeClientManager.PlayerManager.RemotePlayer var1) {
         FakeClientManager.Zombie var2 = null;
         float var3 = Float.POSITIVE_INFINITY;
         Iterator var4 = this.simulator.zombies.values().iterator();

         while(var4.hasNext()) {
            FakeClientManager.Zombie var5 = (FakeClientManager.Zombie)var4.next();
            float var6 = IsoUtils.DistanceToSquared(var1.x, var1.y, var5.x, var5.y);
            if (var6 < var3) {
               var2 = var5;
               var3 = var6;
            }
         }

         return var2;
      }

      private FakeClientManager.PlayerManager.RemotePlayer getNearestPlayer() {
         FakeClientManager.PlayerManager.RemotePlayer var1 = null;
         float var2 = Float.POSITIVE_INFINITY;
         synchronized(this.playerManager.players) {
            Iterator var4 = this.playerManager.players.values().iterator();

            while(var4.hasNext()) {
               FakeClientManager.PlayerManager.RemotePlayer var5 = (FakeClientManager.PlayerManager.RemotePlayer)var4.next();
               float var6 = IsoUtils.DistanceToSquared(this.x, this.y, var5.x, var5.y);
               if (var6 < var2) {
                  var1 = var5;
                  var2 = var6;
               }
            }

            return var1;
         }
      }

      private void aiAttackZombiesMovement() {
         FakeClientManager.Zombie var1 = this.getNearestZombie();
         float var2 = this.getDistance(this.movement.speed);
         if (var1 != null) {
            this.direction.set(var1.x - this.x, var1.y - this.y);
            this.direction.normalize();
         }

         float var3 = this.x + var2 * this.direction.x;
         float var4 = this.y + var2 * this.direction.y;
         this.x = var3;
         this.y = var4;
      }

      private void aiRunAwayFromZombiesMovement() {
         FakeClientManager.Zombie var1 = this.getNearestZombie();
         float var2 = this.getDistance(this.movement.speed);
         if (var1 != null) {
            this.direction.set(this.x - var1.x, this.y - var1.y);
            this.direction.normalize();
         }

         float var3 = this.x + var2 * this.direction.x;
         float var4 = this.y + var2 * this.direction.y;
         this.x = var3;
         this.y = var4;
      }

      private void aiRunToAnotherPlayersMovement() {
         FakeClientManager.PlayerManager.RemotePlayer var1 = this.getNearestPlayer();
         float var2 = this.getDistance(this.movement.speed);
         float var3 = this.x + var2 * this.direction.x;
         float var4 = this.y + var2 * this.direction.y;
         if (var1 != null) {
            this.direction.set(var1.x - this.x, var1.y - this.y);
            float var5 = this.direction.normalize();
            if (var5 > 2.0F) {
               this.x = var3;
               this.y = var4;
            } else if (this.lastPlayerForHello != var1.OnlineID) {
               this.lastPlayerForHello = var1.OnlineID;
            }
         }

      }

      private void aiNormalMovement() {
         float var1 = this.getDistance(this.movement.speed);
         FakeClientManager.PlayerManager.RemotePlayer var2 = this.getNearestPlayer();
         if (var2 == null) {
            this.aiRunAwayFromZombiesMovement();
         } else {
            float var3 = IsoUtils.DistanceToSquared(this.x, this.y, var2.x, var2.y);
            if (var3 > 36.0F) {
               this.movement.speed = 13.0F;
               this.movement.motion = FakeClientManager.Movement.Motion.Run;
            } else {
               this.movement.speed = 4.0F;
               this.movement.motion = FakeClientManager.Movement.Motion.Walk;
            }

            FakeClientManager.Zombie var4 = this.getNearestZombie();
            float var5 = Float.POSITIVE_INFINITY;
            if (var4 != null) {
               var5 = IsoUtils.DistanceToSquared(this.x, this.y, var4.x, var4.y);
            }

            FakeClientManager.Zombie var6 = this.getNearestZombie(var2);
            float var7 = Float.POSITIVE_INFINITY;
            if (var6 != null) {
               var7 = IsoUtils.DistanceToSquared(var2.x, var2.y, var6.x, var6.y);
            }

            if (var7 < 25.0F) {
               var4 = var6;
               var5 = var7;
            }

            if (!(var3 > 25.0F) && var4 != null) {
               if (var5 < 25.0F) {
                  this.direction.set(var4.x - this.x, var4.y - this.y);
                  this.direction.normalize();
                  this.x += var1 * this.direction.x;
                  this.y += var1 * this.direction.y;
               }
            } else {
               this.direction.set(var2.x - this.x, var2.y - this.y);
               float var8 = this.direction.normalize();
               if (var8 > 4.0F) {
                  float var9 = this.x + var1 * this.direction.x;
                  float var10 = this.y + var1 * this.direction.y;
                  this.x = var9;
                  this.y = var10;
               } else if (this.lastPlayerForHello != var2.OnlineID) {
                  this.lastPlayerForHello = var2.OnlineID;
               }
            }

         }
      }

      private void checkRequestChunks() {
         int var1 = (int)this.x / 10;
         int var2 = (int)this.y / 10;
         int var3;
         if (Math.abs(var1 - this.WorldX) < 13 && Math.abs(var2 - this.WorldY) < 13) {
            if (var1 != this.WorldX) {
               if (var1 < this.WorldX) {
                  for(var3 = -6; var3 <= 6; ++var3) {
                     this.client.addChunkRequest(this.WorldX - 6, this.WorldY + var3, 0, var3 + 6);
                  }
               } else {
                  for(var3 = -6; var3 <= 6; ++var3) {
                     this.client.addChunkRequest(this.WorldX + 6, this.WorldY + var3, 12, var3 + 6);
                  }
               }
            } else if (var2 != this.WorldY) {
               if (var2 < this.WorldY) {
                  for(var3 = -6; var3 <= 6; ++var3) {
                     this.client.addChunkRequest(this.WorldX + var3, this.WorldY - 6, var3 + 6, 0);
                  }
               } else {
                  for(var3 = -6; var3 <= 6; ++var3) {
                     this.client.addChunkRequest(this.WorldX + var3, this.WorldY + 6, var3 + 6, 12);
                  }
               }
            }
         } else {
            var3 = this.WorldX - 6;
            int var4 = this.WorldY - 6;
            int var5 = this.WorldX + 6;
            int var6 = this.WorldY + 6;

            for(int var7 = var3; var7 <= var5; ++var7) {
               for(int var8 = var4; var8 <= var6; ++var8) {
                  this.client.addChunkRequest(var7, var8, var7 - var3, var8 - var4);
               }
            }
         }

         this.client.requestChunks();
         this.WorldX = var1;
         this.WorldY = var2;
      }

      private void hit() {
         FakeClientManager.info(this.movement.id, String.format("Player %3d hit", this.OnlineID));
      }

      private void run() {
         this.simulator.update();
         if (this.updateLimiter.Check()) {
            if (this.movement.doTeleport() && this.teleportLimiter.Check()) {
               this.teleportMovement();
            }

            switch(this.movement.type) {
            case Circle:
               this.circleMovement();
               break;
            case Line:
               this.lineMovement();
               break;
            case AIAttackZombies:
               this.aiAttackZombiesMovement();
               break;
            case AIRunAwayFromZombies:
               this.aiRunAwayFromZombiesMovement();
               break;
            case AIRunToAnotherPlayers:
               this.aiRunToAnotherPlayersMovement();
               break;
            case AINormal:
               this.aiNormalMovement();
            }

            this.checkRequestChunks();
            if (this.predictLimiter.Check()) {
               int var1 = (int)(this.client.getServerTime() / 1000000L);
               this.networkCharacter.checkResetPlayer(var1);
               NetworkCharacter.Transform var2 = this.networkCharacter.predict(predictInterval, var1, this.x, this.y, this.direction.x, this.direction.y);
               this.client.sendPlayer(var2, var1, this.direction);
            }

            if (this.timeSyncLimiter.Check()) {
               this.client.sendTimeSync();
            }

            if (this.movement.hordeCreator != null && this.movement.hordeCreator.hordeCreatorLimiter.Check()) {
               this.client.sendCommand(this.movement.hordeCreator.getCommand((int)this.x, (int)this.y, (int)this.z));
            }

            if (this.movement.soundMaker != null && this.movement.soundMaker.soundMakerLimiter.Check()) {
               this.client.sendWorldSound4Player((int)this.x, (int)this.y, (int)this.z, this.movement.soundMaker.radius, this.movement.soundMaker.radius);
               this.client.sendChatMessage(this.movement.soundMaker.message);
               this.client.sendEventPacket(this.OnlineID, (int)this.x, (int)this.y, (int)this.z, (byte)4, "shout");
            }
         }

      }

      private static class Clothes {
         private final byte flags;
         private final byte text;
         private final String name;

         Clothes(byte var1, byte var2, String var3) {
            this.flags = var1;
            this.text = var2;
            this.name = var3;
         }
      }
   }

   private static class HordeCreator {
      private final int radius;
      private final int count;
      private final long interval;
      private final UpdateLimit hordeCreatorLimiter;

      public HordeCreator(int var1, int var2, long var3) {
         this.radius = var1;
         this.count = var2;
         this.interval = var3;
         this.hordeCreatorLimiter = new UpdateLimit(var3);
      }

      public String getCommand(int var1, int var2, int var3) {
         return String.format("/createhorde2 -x %d -y %d -z %d -count %d -radius %d -crawler false -isFallOnFront false -isFakeDead false -knockedDown false -health 1 -outfit", var1, var2, var3, this.count, this.radius);
      }
   }

   private static class SoundMaker {
      private final int radius;
      private final int interval;
      private final String message;
      private final UpdateLimit soundMakerLimiter;

      public SoundMaker(int var1, int var2, String var3) {
         this.radius = var2;
         this.message = var3;
         this.interval = var1;
         this.soundMakerLimiter = new UpdateLimit((long)var1);
      }
   }

   private static class Network {
      private final HashMap createdClients = new HashMap();
      private final HashMap connectedClients = new HashMap();
      private final ByteBuffer rb = ByteBuffer.allocate(1000000);
      private final ByteBuffer wb = ByteBuffer.allocate(1000000);
      private final RakNetPeerInterface peer = new RakNetPeerInterface();
      private final int started;
      private int connected = -1;
      private static final HashMap systemPacketTypeNames = new HashMap();
      private static final HashMap userPacketTypeNames = new HashMap();

      boolean isConnected() {
         return this.connected == 0;
      }

      boolean isStarted() {
         return this.started == 0;
      }

      private Network(int var1, int var2) {
         this.peer.Init(false);
         this.peer.SetMaximumIncomingConnections(0);
         this.peer.SetClientPort(var2);
         this.peer.SetOccasionalPing(true);
         this.started = this.peer.Startup(var1);
         if (this.started == 0) {
            Thread var3 = new Thread(ThreadGroups.Network, this::receiveThread, "PeerInterfaceReceive");
            var3.setDaemon(true);
            var3.start();
            FakeClientManager.log(-1, "Network start ok");
         } else {
            FakeClientManager.error(-1, String.format("Network start failed: %d", this.started));
         }

      }

      private void connect(int var1, String var2) {
         this.connected = this.peer.Connect(var2, 16261, PZcrypt.hash("", true));
         if (this.connected == 0) {
            FakeClientManager.log(var1, String.format("Client connected to %s:%d", var2, 16261));
         } else {
            FakeClientManager.error(var1, String.format("Client connection to %s:%d failed: %d", var2, 16261, this.connected));
         }

      }

      private void disconnect(long var1, int var3, String var4) {
         if (var1 != 0L) {
            this.peer.disconnect(var1);
            this.connected = -1;
         }

         if (this.connected == -1) {
            FakeClientManager.log(var3, String.format("Client disconnected from %s:%d", var4, 16261));
         } else {
            FakeClientManager.log(var3, String.format("Client disconnection from %s:%d failed: %d", var4, 16261, var1));
         }

      }

      private ByteBuffer startPacket() {
         this.wb.clear();
         return this.wb;
      }

      private void cancelPacket() {
         this.wb.clear();
      }

      private void endPacket(long var1) {
         this.wb.flip();
         this.peer.Send(this.wb, 1, 3, (byte)0, var1, false);
      }

      private void endPacketImmediate(long var1) {
         this.wb.flip();
         this.peer.Send(this.wb, 0, 3, (byte)0, var1, false);
      }

      private void endPacketSuperHighUnreliable(long var1) {
         this.wb.flip();
         this.peer.Send(this.wb, 0, 1, (byte)0, var1, false);
      }

      private void receiveThread() {
         while(true) {
            if (this.peer.Receive(this.rb)) {
               this.decode(this.rb);
            } else {
               FakeClientManager.sleep(1L);
            }
         }
      }

      private static void logUserPacket(int var0, short var1) {
         String var2 = (String)userPacketTypeNames.getOrDefault(var1, "unknown user packet");
         FakeClientManager.trace(var0, String.format("## %s", var2));
      }

      private static void logSystemPacket(int var0, int var1) {
         String var2 = (String)systemPacketTypeNames.getOrDefault(var1, "unknown system packet");
         FakeClientManager.trace(var0, String.format("# %s", var2));
      }

      private void decode(ByteBuffer var1) {
         int var2 = var1.get() & 255;
         int var3 = -1;
         long var4 = -1L;
         FakeClientManager.Client var6;
         switch(var2) {
         case 0:
         case 1:
         case 20:
         case 25:
         case 31:
         case 33:
         default:
            break;
         case 16:
            var3 = var1.get() & 255;
            var4 = this.peer.getGuidOfPacket();
            var6 = (FakeClientManager.Client)this.createdClients.get(var3);
            if (var6 != null) {
               var6.connectionGUID = var4;
               this.connectedClients.put(var4, var6);
               var6.changeState(FakeClientManager.Client.State.LOGIN);
            }

            FakeClientManager.log(-1, String.format("Connected clients: %d (connection index %d)", this.connectedClients.size(), var3));
            break;
         case 17:
         case 18:
         case 23:
         case 24:
         case 32:
            FakeClientManager.error(-1, "Connection failed: " + var2);
            break;
         case 19:
            var3 = var1.get() & 255;
         case 44:
         case 45:
            var4 = this.peer.getGuidOfPacket();
            break;
         case 21:
            var3 = var1.get() & 255;
            var4 = this.peer.getGuidOfPacket();
            var6 = (FakeClientManager.Client)this.connectedClients.get(var4);
            if (var6 != null) {
               this.connectedClients.remove(var4);
               var6.changeState(FakeClientManager.Client.State.DISCONNECT);
            }

            FakeClientManager.log(-1, String.format("Connected clients: %d (connection index %d)", this.connectedClients.size(), var3));
            break;
         case 22:
            var3 = var1.get() & 255;
            var6 = (FakeClientManager.Client)this.createdClients.get(var3);
            if (var6 != null) {
               var6.changeState(FakeClientManager.Client.State.DISCONNECT);
            }
            break;
         case 134:
            short var7 = var1.getShort();
            var4 = this.peer.getGuidOfPacket();
            var6 = (FakeClientManager.Client)this.connectedClients.get(var4);
            if (var6 != null) {
               var6.receive((short)var7, var1);
               var3 = var6.connectionIndex;
            }
         }

         logSystemPacket(var3, var2);
      }

      static {
         systemPacketTypeNames.put(22, "connection lost");
         systemPacketTypeNames.put(21, "disconnected");
         systemPacketTypeNames.put(23, "connection banned");
         systemPacketTypeNames.put(17, "connection failed");
         systemPacketTypeNames.put(20, "no free connections");
         systemPacketTypeNames.put(16, "connection accepted");
         systemPacketTypeNames.put(18, "already connected");
         systemPacketTypeNames.put(44, "voice request");
         systemPacketTypeNames.put(45, "voice reply");
         systemPacketTypeNames.put(25, "wrong protocol version");
         systemPacketTypeNames.put(0, "connected ping");
         systemPacketTypeNames.put(1, "unconnected ping");
         systemPacketTypeNames.put(33, "new remote connection");
         systemPacketTypeNames.put(31, "remote disconnection");
         systemPacketTypeNames.put(32, "remote connection lost");
         systemPacketTypeNames.put(24, "invalid password");
         systemPacketTypeNames.put(19, "new connection");
         systemPacketTypeNames.put(134, "user packet");
         Field[] var0 = PacketTypes.class.getFields();
         Field[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field var4 = var1[var3];
            if (var4.getType().equals(Short.TYPE) && Modifier.isStatic(var4.getModifiers())) {
               try {
                  userPacketTypeNames.put(var4.getShort((Object)null), var4.getName());
               } catch (IllegalAccessException var6) {
                  var6.printStackTrace();
               }
            }
         }

      }
   }

   private static class PlayerManager {
      private FakeClientManager.Player player = null;
      private final PlayerPacket playerPacket = new PlayerPacket();
      public final HashMap players = new HashMap();

      public PlayerManager(FakeClientManager.Player var1) {
         this.player = var1;
      }

      private void parsePlayer(ByteBuffer var1) {
         PlayerPacket var2 = this.playerPacket;
         var2.parse(var1);
         synchronized(this.players) {
            FakeClientManager.PlayerManager.RemotePlayer var4 = (FakeClientManager.PlayerManager.RemotePlayer)this.players.get(var2.id);
            if (var4 == null) {
               var4 = new FakeClientManager.PlayerManager.RemotePlayer(var2.id);
               this.players.put(Integer.valueOf(var2.id), var4);
               FakeClientManager.trace(this.player.movement.id, String.format("New player %s", var4.OnlineID));
            }

            var4.playerPacket.copy(var2);
            var4.x = var2.realx;
            var4.y = var2.realy;
            var4.z = (float)var2.realz;
         }
      }

      private void parsePlayerTimeout(ByteBuffer var1) {
         short var2 = var1.getShort();
         synchronized(this.players) {
            this.players.remove(var2);
         }

         FakeClientManager.trace(this.player.movement.id, String.format("Remove player %s", var2));
      }

      private class RemotePlayer {
         public float x;
         public float y;
         public float z;
         public short OnlineID;
         public PlayerPacket playerPacket = null;

         public RemotePlayer(short var2) {
            this.playerPacket = new PlayerPacket();
            this.playerPacket.id = var2;
            this.OnlineID = var2;
         }
      }
   }

   private static class Zombie {
      public long lastUpdate;
      public float x;
      public float y;
      public float z;
      public short OnlineID;
      public boolean localOwnership = false;
      public ZombiePacket zombiePacket = null;
      public IsoDirections dir;
      public float health;
      public byte walkType;
      public float dropPositionX;
      public float dropPositionY;
      public boolean isMoving;

      public Zombie(short var1) {
         this.dir = IsoDirections.N;
         this.health = 1.0F;
         this.walkType = (byte)Rand.Next(NetworkVariables.WalkType.values().length);
         this.isMoving = false;
         this.zombiePacket = new ZombiePacket();
         this.zombiePacket.id = var1;
         this.OnlineID = var1;
         this.localOwnership = false;
      }
   }
}
