package zombie.network;

import fmod.javafmod;
import fmod.fmod.FMODManager;
import fmod.fmod.FMOD_STUDIO_EVENT_DESCRIPTION;
import gnu.trove.map.hash.TShortObjectHashMap;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.AmbientStreamManager;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MapCollisionData;
import zombie.PersistentOutfits;
import zombie.SandboxOptions;
import zombie.SharedDescriptors;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.sadisticAIDirector.SleepingEvent;
import zombie.characters.Faction;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.NetworkTeleport;
import zombie.characters.NetworkZombieVariables;
import zombie.characters.SurvivorDesc;
import zombie.characters.SurvivorFactory;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.skills.PerkFactory;
import zombie.chat.ChatManager;
import zombie.core.BoxedStaticValues;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.ThreadGroups;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferReader;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.raknet.UdpEngine;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.core.utils.UpdateLimit;
import zombie.core.znet.SteamFriends;
import zombie.core.znet.SteamUser;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LogSeverity;
import zombie.erosion.ErosionConfig;
import zombie.erosion.ErosionMain;
import zombie.gameStates.ConnectToServerState;
import zombie.gameStates.MainScreenState;
import zombie.globalObjects.CGlobalObjectNetwork;
import zombie.inventory.CompressIdenticalItems;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.AlarmClock;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Radio;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridOcclusionData;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectSyncRequests;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.LosUtil;
import zombie.iso.ObjectsSyncRequests;
import zombie.iso.SliceY;
import zombie.iso.Vector2;
import zombie.iso.WorldStreamer;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.areas.SafeHouse;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.objects.BSFurnace;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoFire;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoLightSwitch;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTrap;
import zombie.iso.objects.IsoWaveSignal;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.weather.ClimateManager;
import zombie.network.packets.ActionPacket;
import zombie.network.packets.DeadPlayerPacket;
import zombie.network.packets.DeadZombiePacket;
import zombie.network.packets.EventPacket;
import zombie.network.packets.PlaySoundPacket;
import zombie.network.packets.PlayWorldSoundPacket;
import zombie.network.packets.PlayerPacket;
import zombie.network.packets.StopSoundPacket;
import zombie.network.packets.SyncClothingPacket;
import zombie.network.packets.SyncInjuriesPacket;
import zombie.network.packets.hit.HitCharacterPacket;
import zombie.network.packets.hit.PlayerHitPlayerPacket;
import zombie.network.packets.hit.PlayerHitSquarePacket;
import zombie.network.packets.hit.PlayerHitVehiclePacket;
import zombie.network.packets.hit.PlayerHitZombiePacket;
import zombie.network.packets.hit.VehicleHitPacket;
import zombie.network.packets.hit.VehicleHitPlayerPacket;
import zombie.network.packets.hit.VehicleHitZombiePacket;
import zombie.network.packets.hit.ZombieHitPlayerPacket;
import zombie.popman.MPDebugInfo;
import zombie.popman.NetworkZombieSimulator;
import zombie.popman.ZombieCountOptimiser;
import zombie.radio.ZomboidRadio;
import zombie.radio.devices.DeviceData;
import zombie.savefile.ClientPlayerDB;
import zombie.scripting.ScriptManager;
import zombie.ui.ServerPulseGraph;
import zombie.util.AddCoopPlayer;
import zombie.util.StringUtils;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclePart;
import zombie.world.moddata.GlobalModData;

public class GameClient {
   public static final GameClient instance = new GameClient();
   public static final int DEFAULT_PORT = 16361;
   public static boolean bClient = false;
   public static UdpConnection connection;
   public static int count = 0;
   public static String ip = "localhost";
   public static String localIP = "";
   public static String password = "testpass";
   public static boolean allChatMuted = false;
   public static String username = "lemmy101";
   public static String serverPassword = "";
   public UdpEngine udpEngine;
   public static String accessLevel = "";
   public byte ID = -1;
   public float timeSinceKeepAlive = 0.0F;
   UpdateLimit itemSendFrequency = new UpdateLimit(3000L);
   public static int port;
   public boolean bPlayerConnectSent = false;
   private boolean bClientStarted = false;
   private int ResetID = 0;
   private boolean bConnectionLost = false;
   public static String checksum;
   public static boolean checksumValid;
   public static List pingsList;
   public static String GameMap;
   public static boolean bFastForward;
   public static final ClientServerMap[] loadedCells;
   public int DEBUG_PING = 5;
   public IsoObjectSyncRequests objectSyncReq = new IsoObjectSyncRequests();
   public ObjectsSyncRequests worldObjectsSyncReq = new ObjectsSyncRequests(true);
   public static boolean bCoopInvite;
   private ArrayList connectedPlayers = new ArrayList();
   private static boolean isPaused;
   private final ArrayList players = new ArrayList();
   private boolean idMapDirty = true;
   private static final int sendZombieWithoutNeighbor = 4000;
   private static final int sendZombieWithNeighbor = 200;
   public final UpdateLimit sendZombieTimer = new UpdateLimit(4000L);
   public final UpdateLimit sendZombieRequestsTimer = new UpdateLimit(200L);
   public static Map positions;
   private int safehouseUpdateTimer = 0;
   private boolean delayPacket = false;
   private final long[] packetCountsFromAllClients = new long[256];
   private final long[] packetCountsFromServer = new long[256];
   private final KahluaTable packetCountsTable;
   private final ArrayList delayedDisconnect;
   private volatile GameClient.RequestState request;
   public KahluaTable ServerSpawnRegions;
   static final ConcurrentLinkedQueue MainLoopNetDataQ;
   static final ArrayList MainLoopNetData;
   static final ArrayList LoadingMainLoopNetData;
   static final ArrayList DelayedCoopNetData;
   public boolean bConnected;
   UpdateLimit PlayerUpdateReliableLimit;
   public int TimeSinceLastUpdate;
   ByteBuffer staticTest;
   ByteBufferWriter wr;
   long StartHeartMilli;
   long EndHeartMilli;
   public int ping;
   public static float ServerPredictedAhead;
   public static final HashMap IDToPlayerMap;
   public static final TShortObjectHashMap IDToZombieMap;
   public static boolean bIngame;
   public static boolean askPing;
   public final ArrayList ServerMods;
   public ErosionConfig erosionConfig;
   public static Calendar startAuth;
   public static String poisonousBerry;
   public static String poisonousMushroom;
   final ArrayList incomingNetData;
   private final HashMap itemsToSend;
   private final HashMap itemsToSendRemove;
   KahluaTable dbSchema;

   public GameClient() {
      this.packetCountsTable = LuaManager.platform.newTable();
      this.delayedDisconnect = new ArrayList();
      this.ServerSpawnRegions = null;
      this.bConnected = false;
      this.PlayerUpdateReliableLimit = new UpdateLimit(2000L);
      this.TimeSinceLastUpdate = 0;
      this.staticTest = ByteBuffer.allocate(20000);
      this.wr = new ByteBufferWriter(this.staticTest);
      this.StartHeartMilli = 0L;
      this.EndHeartMilli = 0L;
      this.ping = 0;
      this.ServerMods = new ArrayList();
      this.incomingNetData = new ArrayList();
      this.itemsToSend = new HashMap();
      this.itemsToSendRemove = new HashMap();
   }

   public IsoPlayer getPlayerByOnlineID(short var1) {
      return (IsoPlayer)IDToPlayerMap.get(var1);
   }

   public void init() {
      DebugLog.setLogEnabled(DebugType.Network, true);
      LoadingMainLoopNetData.clear();
      MainLoopNetDataQ.clear();
      MainLoopNetData.clear();
      DelayedCoopNetData.clear();
      bIngame = false;
      IDToPlayerMap.clear();
      IDToZombieMap.clear();
      pingsList.clear();
      IDToZombieMap.setAutoCompactionFactor(0.0F);
      this.bPlayerConnectSent = false;
      this.bConnectionLost = false;
      this.delayedDisconnect.clear();
      GameWindow.bServerDisconnected = false;
      this.ServerSpawnRegions = null;
      this.startClient();
   }

   public void startClient() {
      if (this.bClientStarted) {
         this.udpEngine.Connect(ip, port, serverPassword);
      } else {
         try {
            this.udpEngine = new UdpEngine(Rand.Next(10000) + 12345, 1, (String)null, false);
            this.udpEngine.Connect(ip, port, serverPassword);
            this.bClientStarted = true;
         } catch (Exception var2) {
            DebugLog.Network.printException(var2, "Exception thrown during GameClient.startClient.", LogSeverity.Error);
         }

      }
   }

   static void receiveStatistic(ByteBuffer var0, short var1) {
      try {
         long var2 = var0.getLong();
         ByteBufferWriter var4 = connection.startPacket();
         PacketTypes.PacketType.Statistic.doPacket(var4);
         var4.putLong(var2);
         MPStatisticClient.getInstance().send(var4);
         PacketTypes.PacketType.Statistic.send(connection);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   static void receiveStatisticRequest(ByteBuffer var0, short var1) {
      try {
         MPStatistic.getInstance().setStatisticTable(var0);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      LuaEventManager.triggerEvent("OnServerStatisticReceived");
   }

   static void receivePlayerUpdate(ByteBuffer var0, short var1) {
      PlayerPacket var2 = PlayerPacket.l_receive.playerPacket;
      var2.parse(var0);

      try {
         IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2.id);
         if (var3 == null) {
            DebugLog.General.error("receivePlayerUpdate: Client received position for unknown player (id:" + var2.id + "). Client will ignore this data.");
         } else {
            if (DebugOptions.instance.MultiplayerShowPosition.getValue()) {
               if (positions.containsKey(var3.getOnlineID())) {
                  ((Vector2)positions.get(var3.getOnlineID())).set(var2.realx, var2.realy);
               } else {
                  positions.put(var3.getOnlineID(), new Vector2(var2.realx, var2.realy));
               }
            }

            if (!var3.networkAI.isSetVehicleHit()) {
               var3.networkAI.parse(var2);
            }

            var3.bleedingLevel = var2.bleedingLevel;
            if (var3.getVehicle() == null && !var2.usePathFinder && (var3.networkAI.distance.getLength() > 7.0F || IsoUtils.DistanceTo(var2.x, var2.y, (float)var2.z, var3.x, var3.y, var3.z) > 1.0F && (int)var3.z != var2.z)) {
               NetworkTeleport.update(var3, var2);
               if (NetworkTeleport.teleport(var3, var2, 1.0F)) {
                  DebugLog.Multiplayer.warn(String.format("Player %d teleport from (%.2f, %.2f, %.2f) to (%.2f, %.2f %d)", var3.getOnlineID(), var3.x, var3.y, var3.z, var2.x, var2.y, var2.z));
               }
            }

            IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare((double)var2.x, (double)var2.y, (double)var2.z);
            if (var4 != null) {
               if (var3.isAlive() && !IsoWorld.instance.CurrentCell.getObjectList().contains(var3)) {
                  IsoWorld.instance.CurrentCell.getObjectList().add(var3);
                  var3.setCurrent(var4);
               }
            } else if (IsoWorld.instance.CurrentCell.getObjectList().contains(var3)) {
               var3.removeFromWorld();
               var3.removeFromSquare();
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   static void receiveZombieSimulation(ByteBuffer var0, short var1) {
      NetworkZombieSimulator.getInstance().clear();
      boolean var2 = var0.get() == 1;
      if (var2) {
         instance.sendZombieTimer.setUpdatePeriod(200L);
      } else {
         instance.sendZombieTimer.setUpdatePeriod(4000L);
      }

      short var3 = var0.getShort();

      short var4;
      short var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var0.getShort();
         IsoZombie var6 = (IsoZombie)IDToZombieMap.get(var5);
         if (var6 != null) {
            VirtualZombieManager.instance.removeZombieFromWorld(var6);
         }
      }

      var4 = var0.getShort();

      for(var5 = 0; var5 < var4; ++var5) {
         short var7 = var0.getShort();
         NetworkZombieSimulator.getInstance().add(var7);
      }

      NetworkZombieSimulator.getInstance().added();
      NetworkZombieSimulator.getInstance().receivePacket(var0);
   }

   static void receiveZombieControl(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      short var3 = var0.getShort();
      int var4 = var0.getInt();
      IsoZombie var5 = (IsoZombie)IDToZombieMap.get(var2);
      if (var5 != null) {
         NetworkZombieVariables.setInt(var5, var3, var4);
      }
   }

   public void Shutdown() {
      if (this.bClientStarted) {
         this.udpEngine.Shutdown();
      }

   }

   public void update() {
      ZombieCountOptimiser.startCount();
      if (this.safehouseUpdateTimer == 0 && ServerOptions.instance.DisableSafehouseWhenPlayerConnected.getValue()) {
         this.safehouseUpdateTimer = 3000;
         SafeHouse.updateSafehousePlayersConnected();
      }

      if (this.safehouseUpdateTimer > 0) {
         --this.safehouseUpdateTimer;
      }

      for(ZomboidNetData var1 = (ZomboidNetData)MainLoopNetDataQ.poll(); var1 != null; var1 = (ZomboidNetData)MainLoopNetDataQ.poll()) {
         MainLoopNetData.add(var1);
      }

      int var9;
      ZomboidNetData var10;
      if (this.bConnectionLost) {
         if (!this.bPlayerConnectSent) {
            for(var9 = 0; var9 < MainLoopNetData.size(); ++var9) {
               var10 = (ZomboidNetData)MainLoopNetData.get(var9);
               this.gameLoadingDealWithNetData(var10);
            }

            MainLoopNetData.clear();
         } else {
            for(var9 = 0; var9 < MainLoopNetData.size(); ++var9) {
               var10 = (ZomboidNetData)MainLoopNetData.get(var9);
               if (var10.type == PacketTypes.PacketType.Kicked.getId()) {
                  GameWindow.kickReason = GameWindow.ReadStringUTF(var10.buffer);
               }
            }

            MainLoopNetData.clear();
         }

         GameWindow.bServerDisconnected = true;
      } else {
         synchronized(this.delayedDisconnect) {
            while(!this.delayedDisconnect.isEmpty()) {
               int var2 = (Integer)this.delayedDisconnect.remove(0);
               switch(var2) {
               case 17:
                  LuaEventManager.triggerEvent("OnConnectFailed", (Object)null);
                  break;
               case 18:
                  LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_AlreadyConnected"));
               case 19:
               case 20:
               case 22:
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               case 30:
               case 31:
               default:
                  break;
               case 21:
                  LuaEventManager.triggerEvent("OnDisconnect");
                  break;
               case 23:
                  LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_Banned"));
                  break;
               case 24:
                  LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_InvalidServerPassword"));
                  break;
               case 32:
                  LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_ConnectionLost"));
               }
            }
         }

         if (!this.bPlayerConnectSent) {
            for(var9 = 0; var9 < MainLoopNetData.size(); ++var9) {
               var10 = (ZomboidNetData)MainLoopNetData.get(var9);
               if (!this.gameLoadingDealWithNetData(var10)) {
                  LoadingMainLoopNetData.add(var10);
               }
            }

            MainLoopNetData.clear();
            WorldStreamer.instance.updateMain();
         } else {
            if (!LoadingMainLoopNetData.isEmpty()) {
               DebugLog.log(DebugType.Network, "Processing delayed packets...");
               MainLoopNetData.addAll(0, LoadingMainLoopNetData);
               LoadingMainLoopNetData.clear();
            }

            if (!DelayedCoopNetData.isEmpty() && IsoWorld.instance.AddCoopPlayers.isEmpty()) {
               DebugLog.log(DebugType.Network, "Processing delayed coop packets...");
               MainLoopNetData.addAll(0, DelayedCoopNetData);
               DelayedCoopNetData.clear();
            }

            long var8 = System.currentTimeMillis();

            int var3;
            for(var3 = 0; var3 < MainLoopNetData.size(); ++var3) {
               ZomboidNetData var4 = (ZomboidNetData)MainLoopNetData.get(var3);
               if (var4.time + (long)this.DEBUG_PING <= var8) {
                  this.mainLoopDealWithNetData(var4);
                  MainLoopNetData.remove(var3--);
               }
            }

            for(var3 = 0; var3 < IsoWorld.instance.CurrentCell.getObjectList().size(); ++var3) {
               IsoMovingObject var11 = (IsoMovingObject)IsoWorld.instance.CurrentCell.getObjectList().get(var3);
               if (var11 instanceof IsoPlayer && !((IsoPlayer)var11).isLocalPlayer() && !this.getPlayers().contains(var11)) {
                  if (Core.bDebug) {
                     DebugLog.log("Disconnected/Distant player " + ((IsoPlayer)var11).username + " in CurrentCell.getObjectList() removed");
                  }

                  IsoWorld.instance.CurrentCell.getObjectList().remove(var3--);
               }
            }

            try {
               this.sendAddedRemovedItems(false);
            } catch (Exception var6) {
               var6.printStackTrace();
               ExceptionLogger.logException(var6);
            }

            try {
               VehicleManager.instance.clientUpdate();
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            this.objectSyncReq.sendRequests(connection);
            this.worldObjectsSyncReq.sendRequests(connection);
            WorldStreamer.instance.updateMain();
            MPStatisticClient.getInstance().update();
            this.timeSinceKeepAlive += GameTime.getInstance().getMultiplier();
         }
      }
   }

   public void smashWindow(IsoWindow var1, int var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.SmashWindow.doPacket(var3);
      var3.putInt(var1.square.getX());
      var3.putInt(var1.square.getY());
      var3.putInt(var1.square.getZ());
      var3.putByte((byte)var1.square.getObjects().indexOf(var1));
      var3.putByte((byte)var2);
      PacketTypes.PacketType.SmashWindow.send(connection);
   }

   public static void getCustomModData() {
      ByteBufferWriter var0 = connection.startPacket();
      PacketTypes.PacketType.getModData.doPacket(var0);
      PacketTypes.PacketType.getModData.send(connection);
   }

   static void receiveStitch(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         boolean var5 = var0.get() == 1;
         float var6 = var0.getFloat();
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setStitched(var5);
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setStitchTime(var6);
      }

   }

   static void receiveBandage(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         boolean var5 = var0.get() == 1;
         float var6 = var0.getFloat();
         boolean var7 = var0.get() == 1;
         String var8 = GameWindow.ReadStringUTF(var0);
         var3.getBodyDamage().SetBandaged(var4, var5, var6, var7, var8);
      }

   }

   static void receivePingFromClient(ByteBuffer var0, short var1) {
      MPStatistics.parse(var0);
   }

   static void receiveWoundInfection(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         boolean var5 = var0.get() == 1;
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setInfectedWound(var5);
      }

   }

   static void receiveDisinfect(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         float var5 = var0.getFloat();
         BodyPart var6 = var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4));
         var6.setAlcoholLevel(var6.getAlcoholLevel() + var5);
      }

   }

   static void receiveSplint(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         boolean var5 = var0.get() == 1;
         String var6 = var5 ? GameWindow.ReadStringUTF(var0) : null;
         float var7 = var5 ? var0.getFloat() : 0.0F;
         BodyPart var8 = var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4));
         var8.setSplint(var5, var7);
         var8.setSplintItem(var6);
      }

   }

   static void receiveRemoveGlass(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setHaveGlass(false);
      }

   }

   static void receiveRemoveBullet(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setHaveBullet(false, var5);
      }

   }

   static void receiveCleanBurn(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setNeedBurnWash(false);
      }

   }

   static void receiveAdditionalPain(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         float var5 = var0.getFloat();
         BodyPart var6 = var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4));
         var6.setAdditionalPain(var6.getAdditionalPain() + var5);
      }

   }

   private void delayPacket(int var1, int var2, int var3) {
      if (IsoWorld.instance != null) {
         for(int var4 = 0; var4 < IsoWorld.instance.AddCoopPlayers.size(); ++var4) {
            AddCoopPlayer var5 = (AddCoopPlayer)IsoWorld.instance.AddCoopPlayers.get(var4);
            if (var5.isLoadingThisSquare(var1, var2)) {
               this.delayPacket = true;
               return;
            }
         }

      }
   }

   private void mainLoopDealWithNetData(ZomboidNetData var1) {
      ByteBuffer var2 = var1.buffer;
      int var3 = var2.position();
      this.delayPacket = false;
      if (var1.type >= 0 && var1.type < 256) {
         int var10002 = this.packetCountsFromServer[var1.type]++;
      }

      try {
         this.mainLoopHandlePacketInternal(var1, var2);
         if (this.delayPacket) {
            var2.position(var3);
            DelayedCoopNetData.add(var1);
            return;
         }
      } catch (Exception var5) {
         DebugLog.Network.printException(var5, "Error with packet of type: " + var1.type, LogSeverity.Error);
      }

      ZomboidNetDataPool.instance.discard(var1);
   }

   private void mainLoopHandlePacketInternal(ZomboidNetData var1, ByteBuffer var2) throws IOException {
      if (DebugOptions.instance.Network.Client.MainLoop.getValue()) {
         PacketTypes.PacketType var3 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var1.type);
         var3.onMainLoopHandlePacketInternal(var2, var1.type);
      }
   }

   static void receiveAddBrokenGlass(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 != null) {
         var5.addBrokenGlass();
      }

   }

   static void receivePlayerDamageFromCarCrash(ByteBuffer var0, short var1) {
      float var2 = var0.getFloat();
      if (IsoPlayer.getInstance().getVehicle() == null) {
         DebugLog.Multiplayer.error("Receive damage from car crash, can't find vehicle");
      } else {
         IsoPlayer.getInstance().getVehicle().addRandomDamageFromCrash(IsoPlayer.getInstance(), var2);
      }
   }

   static void receivePacketCounts(ByteBuffer var0, short var1) {
      for(int var2 = 0; var2 < 256; ++var2) {
         instance.packetCountsFromAllClients[var2] = var0.getLong();
      }

   }

   public void requestPacketCounts() {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.PacketCounts.doPacket(var1);
      PacketTypes.PacketType.PacketCounts.send(connection);
   }

   public KahluaTable getPacketCounts(int var1) {
      long[] var2 = var1 == 1 ? this.packetCountsFromAllClients : this.packetCountsFromServer;

      for(int var3 = 0; var3 < 256; ++var3) {
         this.packetCountsTable.rawset(var3 + 1, BoxedStaticValues.toDouble((double)var2[var3]));
      }

      return this.packetCountsTable;
   }

   public static boolean IsClientPaused() {
      return isPaused;
   }

   static void receiveStartPause(ByteBuffer var0, short var1) {
      isPaused = true;
      LuaEventManager.triggerEvent("OnServerStartSaving");
   }

   static void receiveStopPause(ByteBuffer var0, short var1) {
      isPaused = false;
      LuaEventManager.triggerEvent("OnServerFinishSaving");
   }

   static void receiveChatMessageToPlayer(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processChatMessagePacket(var0);
   }

   static void receivePlayerConnectedToChat(ByteBuffer var0, short var1) {
      ChatManager.getInstance().setFullyConnected();
   }

   static void receivePlayerJoinChat(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processJoinChatPacket(var0);
   }

   static void receiveInvMngRemoveItem(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      InventoryItem var3 = IsoPlayer.getInstance().getInventory().getItemWithIDRecursiv(var2);
      if (var3 == null) {
         DebugLog.log("ERROR: invMngRemoveItem can not find " + var2 + " item.");
      } else {
         IsoPlayer.getInstance().removeWornItem(var3);
         if (var3.getCategory().equals("Clothing")) {
            LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
         }

         if (var3 == IsoPlayer.getInstance().getPrimaryHandItem()) {
            IsoPlayer.getInstance().setPrimaryHandItem((InventoryItem)null);
            LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
         } else if (var3 == IsoPlayer.getInstance().getSecondaryHandItem()) {
            IsoPlayer.getInstance().setSecondaryHandItem((InventoryItem)null);
            LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
         }

         boolean var4 = IsoPlayer.getInstance().getInventory().removeItemWithIDRecurse(var2);
         if (!var4) {
            DebugLog.log("ERROR: GameClient.invMngRemoveItem can not remove item " + var2);
         }

      }
   }

   static void receiveInvMngGetItem(ByteBuffer var0, short var1) throws IOException {
      int var2 = var0.getInt();
      InventoryItem var3 = null;

      try {
         var3 = InventoryItem.loadItem(var0, 186);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      if (var3 != null) {
         IsoPlayer.getInstance().getInventory().addItem(var3);
      }

   }

   static void receiveInvMngReqItem(ByteBuffer var0, short var1) throws IOException {
      int var2 = 0;
      String var3 = null;
      if (var0.get() == 1) {
         var3 = GameWindow.ReadString(var0);
      } else {
         var2 = var0.getInt();
      }

      short var4 = var0.getShort();
      InventoryItem var5 = null;
      if (var3 == null) {
         var5 = IsoPlayer.getInstance().getInventory().getItemWithIDRecursiv(var2);
         if (var5 == null) {
            DebugLog.log("ERROR: invMngRemoveItem can not find " + var2 + " item.");
            return;
         }
      } else {
         var5 = InventoryItemFactory.CreateItem(var3);
      }

      if (var5 != null) {
         if (var3 == null) {
            IsoPlayer.getInstance().removeWornItem(var5);
            if (var5.getCategory().equals("Clothing")) {
               LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
            }

            if (var5 == IsoPlayer.getInstance().getPrimaryHandItem()) {
               IsoPlayer.getInstance().setPrimaryHandItem((InventoryItem)null);
               LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
            } else if (var5 == IsoPlayer.getInstance().getSecondaryHandItem()) {
               IsoPlayer.getInstance().setSecondaryHandItem((InventoryItem)null);
               LuaEventManager.triggerEvent("OnClothingUpdated", IsoPlayer.getInstance());
            }

            IsoPlayer.getInstance().getInventory().removeItemWithIDRecurse(var5.getID());
         } else {
            IsoPlayer.getInstance().getInventory().RemoveOneOf(var3.split("\\.")[1]);
         }

         ByteBufferWriter var6 = connection.startPacket();
         PacketTypes.PacketType.InvMngGetItem.doPacket(var6);
         var6.putShort(var4);
         var5.saveWithSize(var6.bb, false);
         PacketTypes.PacketType.InvMngGetItem.send(connection);
      }

   }

   public static void invMngRequestItem(long var0, String var2, IsoPlayer var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.InvMngReqItem.doPacket(var4);
      if (var2 != null) {
         var4.putByte((byte)1);
         var4.putUTF(var2);
      } else {
         var4.putByte((byte)0);
         var4.putLong(var0);
      }

      var4.putShort(IsoPlayer.getInstance().getOnlineID());
      var4.putShort(var3.getOnlineID());
      PacketTypes.PacketType.InvMngReqItem.send(connection);
   }

   public static void invMngRequestRemoveItem(long var0, IsoPlayer var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.InvMngRemoveItem.doPacket(var3);
      var3.putLong(var0);
      var3.putShort(var2.getOnlineID());
      PacketTypes.PacketType.InvMngRemoveItem.send(connection);
   }

   static void receiveSyncFaction(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      int var4 = var0.getInt();
      Faction var5 = Faction.getFaction(var2);
      if (var5 == null) {
         var5 = new Faction(var2, var3);
         Faction.getFactions().add(var5);
      }

      var5.getPlayers().clear();
      if (var0.get() == 1) {
         var5.setTag(GameWindow.ReadString(var0));
         var5.setTagColor(new ColorInfo(var0.getFloat(), var0.getFloat(), var0.getFloat(), 1.0F));
      }

      for(int var6 = 0; var6 < var4; ++var6) {
         var5.getPlayers().add(GameWindow.ReadString(var0));
      }

      var5.setOwner(var3);
      boolean var7 = var0.get() == 1;
      if (var7) {
         Faction.getFactions().remove(var5);
         DebugLog.log("faction: removed " + var2 + " owner=" + var5.getOwner());
      }

      LuaEventManager.triggerEvent("SyncFaction", var2);
   }

   static void receiveSyncNonPvpZone(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      String var6 = GameWindow.ReadString(var0);
      NonPvpZone var7 = NonPvpZone.getZoneByTitle(var6);
      if (var7 == null) {
         var7 = NonPvpZone.addNonPvpZone(var6, var2, var3, var4, var5);
      }

      if (var7 != null) {
         boolean var8 = var0.get() == 1;
         if (var8) {
            NonPvpZone.removeNonPvpZone(var6, true);
         }

      }
   }

   static void receiveChangeTextColor(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         float var4 = var0.getFloat();
         float var5 = var0.getFloat();
         float var6 = var0.getFloat();
         var3.setSpeakColourInfo(new ColorInfo(var4, var5, var6, 1.0F));
      }
   }

   static void receivePlaySoundEveryPlayer(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      DebugLog.log(DebugType.Sound, "sound: received " + var2 + " at " + var3 + "," + var4 + "," + var5);
      if (!Core.SoundDisabled) {
         FMOD_STUDIO_EVENT_DESCRIPTION var6 = FMODManager.instance.getEventDescription(var2);
         if (var6 == null) {
            return;
         }

         long var7 = javafmod.FMOD_Studio_System_CreateEventInstance(var6.address);
         if (var7 <= 0L) {
            return;
         }

         javafmod.FMOD_Studio_EventInstance_SetVolume(var7, (float)Core.getInstance().getOptionAmbientVolume() / 20.0F);
         javafmod.FMOD_Studio_EventInstance3D(var7, (float)var3, (float)var4, (float)var5);
         javafmod.FMOD_Studio_StartEvent(var7);
         javafmod.FMOD_Studio_ReleaseEventInstance(var7);
      }

   }

   static void receiveCataplasm(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         int var4 = var0.getInt();
         float var5 = var0.getFloat();
         float var6 = var0.getFloat();
         float var7 = var0.getFloat();
         if (var5 > 0.0F) {
            var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setPlantainFactor(var5);
         }

         if (var6 > 0.0F) {
            var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setComfreyFactor(var6);
         }

         if (var7 > 0.0F) {
            var3.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var4)).setGarlicFactor(var7);
         }
      }

   }

   static void receiveStopFire(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 != null) {
         var5.stopFire();
      }
   }

   static void receiveAddAlarm(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      DebugLog.log(DebugType.Multiplayer, "ReceiveAlarm at [ " + var2 + " , " + var3 + " ]");
      IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, 0);
      if (var4 != null && var4.getBuilding() != null && var4.getBuilding().getDef() != null) {
         var4.getBuilding().getDef().bAlarmed = true;
         AmbientStreamManager.instance.doAlarm(var4.room.def);
      }
   }

   static void receiveAddExplosiveTrap(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 != null) {
         InventoryItem var6 = null;

         try {
            var6 = InventoryItem.loadItem(var0, 186);
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         HandWeapon var7 = var6 != null ? (HandWeapon)var6 : null;
         IsoTrap var8 = new IsoTrap(var7, var5.getCell(), var5);
         if (!var7.isInstantExplosion()) {
            var5.AddTileObject(var8);
         } else {
            var8.triggerExplosion(var7.getSensorRange() > 0);
         }
      }

   }

   static void receiveTeleport(ByteBuffer var0, short var1) {
      byte var2 = var0.get();
      IsoPlayer var3 = IsoPlayer.players[var2];
      if (var3 != null && !var3.isDead()) {
         if (var3.getVehicle() != null) {
            var3.getVehicle().exit(var3);
            LuaEventManager.triggerEvent("OnExitVehicle", var3);
         }

         var3.setX(var0.getFloat());
         var3.setY(var0.getFloat());
         var3.setZ(var0.getFloat());
         var3.setLx(var3.getX());
         var3.setLy(var3.getY());
         var3.setLz(var3.getZ());
      }
   }

   static void receiveRemoveBlood(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      boolean var5 = var0.get() == 1;
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 != null) {
         var6.removeBlood(true, var5);
      }

   }

   static void receiveSyncThumpable(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      byte var5 = var0.get();
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var5 >= 0 && var5 < var6.getObjects().size()) {
            IsoObject var7 = (IsoObject)var6.getObjects().get(var5);
            if (var7 instanceof IsoThumpable) {
               IsoThumpable var8 = (IsoThumpable)var7;
               var8.lockedByCode = var0.getInt();
               var8.lockedByPadlock = var0.get() == 1;
               var8.keyId = var0.getInt();
            } else {
               DebugLog.log("syncThumpable: expected IsoThumpable index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
            }
         } else {
            DebugLog.log("syncThumpable: index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
         }

      }
   }

   static void receiveSyncDoorKey(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      byte var5 = var0.get();
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var5 >= 0 && var5 < var6.getObjects().size()) {
            IsoObject var7 = (IsoObject)var6.getObjects().get(var5);
            if (var7 instanceof IsoDoor) {
               IsoDoor var8 = (IsoDoor)var7;
               var8.keyId = var0.getInt();
            } else {
               DebugLog.log("SyncDoorKey: expected IsoDoor index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
            }
         } else {
            DebugLog.log("SyncDoorKey: index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
         }

      }
   }

   static void receiveConstructedZone(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoMetaGrid.Zone var5 = IsoWorld.instance.MetaGrid.getZoneAt(var2, var3, var4);
      if (var5 != null) {
         var5.setHaveConstruction(true);
      }

   }

   static void receiveAddCoopPlayer(ByteBuffer var0, short var1) {
      boolean var2 = var0.get() == 1;
      byte var3 = var0.get();
      if (var2) {
         for(int var4 = 0; var4 < IsoWorld.instance.AddCoopPlayers.size(); ++var4) {
            ((AddCoopPlayer)IsoWorld.instance.AddCoopPlayers.get(var4)).accessGranted(var3);
         }
      } else {
         String var6 = GameWindow.ReadStringUTF(var0);

         for(int var5 = 0; var5 < IsoWorld.instance.AddCoopPlayers.size(); ++var5) {
            ((AddCoopPlayer)IsoWorld.instance.AddCoopPlayers.get(var5)).accessDenied(var3, var6);
         }
      }

   }

   static void receiveZombieDescriptors(ByteBuffer var0, short var1) {
      try {
         SharedDescriptors.Descriptor var2 = new SharedDescriptors.Descriptor();
         var2.load(var0, 186);
         SharedDescriptors.registerPlayerZombieDescriptor(var2);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void checksumServer() {
      ByteBufferWriter var0 = connection.startPacket();
      PacketTypes.PacketType.Checksum.doPacket(var0);
      String var10001 = checksum;
      var0.putUTF(var10001 + ScriptManager.instance.getChecksum());
      PacketTypes.PacketType.Checksum.send(connection);
   }

   static void receiveRegisterZone(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      int var6 = var0.getInt();
      int var7 = var0.getInt();
      int var8 = var0.getInt();
      int var9 = var0.getInt();
      ArrayList var10 = IsoWorld.instance.getMetaGrid().getZonesAt(var4, var5, var6);
      boolean var11 = false;
      Iterator var12 = var10.iterator();

      while(var12.hasNext()) {
         IsoMetaGrid.Zone var13 = (IsoMetaGrid.Zone)var12.next();
         if (var3.equals(var13.getType())) {
            var11 = true;
            var13.setName(var2);
            var13.setLastActionTimestamp(var9);
         }
      }

      if (!var11) {
         IsoWorld.instance.getMetaGrid().registerZone(var2, var3, var4, var5, var6, var7, var8);
      }

   }

   static void receiveAddXP(ByteBuffer var0, short var1) {
      byte var2 = var0.get();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoPlayer var5 = IsoPlayer.players[var2];
      if (var5 != null && !var5.isDead()) {
         PerkFactory.Perk var6 = PerkFactory.Perks.fromIndex(var3);
         var5.getXp().AddXP(var6, (float)var4);
      }
   }

   static void receiveAddXpCommand(ByteBuffer var0, short var1) {
      IsoPlayer var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      PerkFactory.Perk var3 = PerkFactory.Perks.fromIndex(var0.getInt());
      if (var2 != null && !var2.isDead()) {
         var2.getXp().AddXP(var3, (float)var0.getInt());
      }

   }

   public void sendAddXpFromPlayerStatsUI(IsoPlayer var1, PerkFactory.Perk var2, int var3, boolean var4, boolean var5) {
      if (canModifyPlayerStats()) {
         ByteBufferWriter var6 = connection.startPacket();
         PacketTypes.PacketType.AddXpFromPlayerStatsUI.doPacket(var6);
         var6.putShort(var1.getOnlineID());
         if (!var5) {
            var6.putInt(0);
            var6.putInt(var2.index());
            var6.putInt(var3);
            var6.putByte((byte)(var4 ? 1 : 0));
         }

         PacketTypes.PacketType.AddXpFromPlayerStatsUI.send(connection);
      }
   }

   static void receiveSyncXP(ByteBuffer var0, short var1) {
      IsoPlayer var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      if (var2 != null && !var2.isDead()) {
         try {
            var2.getXp().load(var0, 186);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

   }

   public void sendSyncXp(IsoPlayer var1) {
      if (canModifyPlayerStats()) {
         ByteBufferWriter var2 = connection.startPacket();
         PacketTypes.PacketType.SyncXP.doPacket(var2);
         var2.putShort(var1.getOnlineID());

         try {
            var1.getXp().save(var2.bb);
         } catch (IOException var4) {
            var4.printStackTrace();
         }

         PacketTypes.PacketType.SyncXP.send(connection);
      }
   }

   public void sendTransactionID(IsoPlayer var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SendTransactionID.doPacket(var2);
      var2.putShort(var1.getOnlineID());
      var2.putInt(var1.getTransactionID());
      PacketTypes.PacketType.SendTransactionID.send(connection);
   }

   static void receiveUserlog(ByteBuffer var0, short var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var0.getInt();
      String var4 = GameWindow.ReadString(var0);

      for(int var5 = 0; var5 < var3; ++var5) {
         var2.add(new Userlog(var4, Userlog.UserlogType.fromIndex(var0.getInt()).toString(), GameWindow.ReadString(var0), GameWindow.ReadString(var0), var0.getInt()));
      }

      LuaEventManager.triggerEvent("OnReceiveUserlog", var4, var2);
   }

   static void receiveAddXpFromPlayerStatsUI(ByteBuffer var0, short var1) {
      IsoPlayer var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      int var3 = var0.getInt();
      if (var2 != null && !var2.isDead() && var3 == 0) {
         PerkFactory.Perk var4 = PerkFactory.Perks.fromIndex(var0.getInt());
         var2.getXp().AddXP(var4, (float)var0.getInt(), false, var0.get() == 1, false, true);
      }

   }

   static void receivePing(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      int var10000 = var0.getInt() - 1;
      String var3 = var10000 + "/" + var0.getInt();
      LuaEventManager.triggerEvent("ServerPinged", var2, var3);
      connection.forceDisconnect();
      askPing = false;
   }

   static void receiveChecksumLoading(ByteBuffer var0, short var1) {
      NetChecksum.comparer.clientPacket(var0);
   }

   static void receiveKickedLoading(ByteBuffer var0, short var1) {
      GameWindow.kickReason = GameWindow.ReadStringUTF(var0);
      GameWindow.bServerDisconnected = true;
   }

   static void receiveServerMapLoading(ByteBuffer var0, short var1) {
      ClientServerMap.receivePacket(var0);
   }

   static void receiveChangeSafety(ByteBuffer var0, short var1) {
      IsoPlayer var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      if (var2 != null) {
         var2.setSafety(var0.get() == 1);
      }

   }

   static void receiveAddItemInInventory(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      String var3 = GameWindow.ReadString(var0);
      int var4 = var0.getInt();
      IsoPlayer var5 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var5 != null && !var5.isDead()) {
         var5.getInventory().AddItems(var3, var4);
      }

   }

   static void receiveKicked(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      if (var2 != null && !var2.equals("")) {
         ChatManager.getInstance().showServerChatMessage(var2);
      }

      connection.username = null;
      GameWindow.savePlayer();
      GameWindow.bServerDisconnected = true;
      GameWindow.kickReason = var2;
      connection.forceDisconnect();
      connection.close();
   }

   public void addDisconnectPacket(int var1) {
      synchronized(this.delayedDisconnect) {
         this.delayedDisconnect.add(var1);
      }
   }

   public void connectionLost() {
      this.bConnectionLost = true;
      positions.clear();
   }

   public static void SendCommandToServer(String var0) {
      if (ServerOptions.clientOptionsList == null) {
         ServerOptions.initClientCommandsHelp();
      }

      if (var0.startsWith("/roll")) {
         try {
            int var1 = Integer.parseInt(var0.split(" ")[1]);
            if (var1 > 100) {
               ChatManager.getInstance().showServerChatMessage((String)ServerOptions.clientOptionsList.get("roll"));
               return;
            }
         } catch (Exception var2) {
            ChatManager.getInstance().showServerChatMessage((String)ServerOptions.clientOptionsList.get("roll"));
            return;
         }

         if (!IsoPlayer.getInstance().getInventory().contains("Dice") && accessLevel.equals("")) {
            ChatManager.getInstance().showServerChatMessage((String)ServerOptions.clientOptionsList.get("roll"));
            return;
         }
      }

      if (var0.startsWith("/card") && !IsoPlayer.getInstance().getInventory().contains("CardDeck") && accessLevel.equals("")) {
         ChatManager.getInstance().showServerChatMessage((String)ServerOptions.clientOptionsList.get("card"));
      } else {
         ByteBufferWriter var3 = connection.startPacket();
         PacketTypes.PacketType.ReceiveCommand.doPacket(var3);
         var3.putUTF(var0);
         PacketTypes.PacketType.ReceiveCommand.send(connection);
      }
   }

   public static void sendServerPing(long var0) {
      if (connection != null) {
         ByteBufferWriter var2 = connection.startPacket();
         PacketTypes.PacketType.PingFromClient.doPacket(var2);
         var2.putLong(var0);
         PacketTypes.PacketType.PingFromClient.send(connection);
      }

   }

   private boolean gameLoadingDealWithNetData(ZomboidNetData var1) {
      ByteBuffer var2 = var1.buffer;

      try {
         PacketTypes.PacketType var3 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var1.type);
         return var3.onGameLoadingDealWithNetData(var2, var1.type);
      } catch (Exception var4) {
         DebugLog.log(DebugType.Network, "Error with packet of type: " + var1.type);
         var4.printStackTrace();
         ZomboidNetDataPool.instance.discard(var1);
         return true;
      }
   }

   static void receiveWorldMessage(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadStringUTF(var0);
      String var3 = GameWindow.ReadString(var0);
      var3 = var3.replaceAll("<", "&lt;");
      var3 = var3.replaceAll(">", "&gt;");
      ChatManager.getInstance().addMessage(var2, var3);
   }

   static void receiveReloadOptions(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         ServerOptions.instance.putOption(GameWindow.ReadString(var0), GameWindow.ReadString(var0));
      }

   }

   static void receiveStartRain(ByteBuffer var0, short var1) {
      RainManager.setRandRainMin(var0.getInt());
      RainManager.setRandRainMax(var0.getInt());
      RainManager.startRaining();
      RainManager.RainDesiredIntensity = var0.getFloat();
   }

   static void receiveStopRain(ByteBuffer var0, short var1) {
      RainManager.stopRaining();
   }

   static void receiveWeather(ByteBuffer var0, short var1) {
      GameTime var2 = GameTime.getInstance();
      var2.setDawn(var0.get() & 255);
      var2.setDusk(var0.get() & 255);
      var2.setThunderDay(var0.get() == 1);
      var2.setMoon(var0.getFloat());
      var2.setAmbientMin(var0.getFloat());
      var2.setAmbientMax(var0.getFloat());
      var2.setViewDistMin(var0.getFloat());
      var2.setViewDistMax(var0.getFloat());
      IsoWorld.instance.setGlobalTemperature(var0.getFloat());
      IsoWorld.instance.setWeather(GameWindow.ReadStringUTF(var0));
      ErosionMain.getInstance().receiveState(var0);
   }

   static void receiveSyncClock(ByteBuffer var0, short var1) {
      GameTime var2 = GameTime.getInstance();
      boolean var3 = bFastForward;
      bFastForward = var0.get() == 1;
      float var4 = var0.getFloat();
      float var5 = var2.getTimeOfDay() - var2.getLastTimeOfDay();
      var2.setTimeOfDay(var4);
      var2.setLastTimeOfDay(var4 - var5);
      if (var2.getLastTimeOfDay() < 0.0F) {
         var2.setLastTimeOfDay(var4 - var5 + 24.0F);
      }

      var2.ServerLastTimeOfDay = var2.ServerTimeOfDay;
      var2.ServerTimeOfDay = var4;
      if (var2.ServerLastTimeOfDay <= 7.0F && var2.ServerTimeOfDay > 7.0F) {
         var2.setNightsSurvived(var2.getNightsSurvived() + 1);
      }

      if (var2.ServerLastTimeOfDay > var2.ServerTimeOfDay) {
         ++var2.ServerNewDays;
      }

   }

   static void receiveClientCommand(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      boolean var4 = var0.get() == 1;
      KahluaTable var5 = null;
      if (var4) {
         var5 = LuaManager.platform.newTable();

         try {
            TableNetworkUtils.load(var5, var0);
         } catch (Exception var7) {
            var7.printStackTrace();
            return;
         }
      }

      LuaEventManager.triggerEvent("OnServerCommand", var2, var3, var5);
   }

   static void receiveGlobalObjects(ByteBuffer var0, short var1) throws IOException {
      CGlobalObjectNetwork.receive(var0);
   }

   private boolean receiveLargeFilePart(ByteBuffer var1, String var2) {
      int var3 = var1.getInt();
      int var4 = var1.getInt();
      int var5 = var1.getInt();
      File var6 = new File(var2);

      try {
         FileOutputStream var7 = new FileOutputStream(var6, var4 > 0);

         try {
            BufferedOutputStream var8 = new BufferedOutputStream(var7);

            try {
               var8.write(var1.array(), var1.position(), var1.limit() - var1.position());
            } catch (Throwable var13) {
               try {
                  var8.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            var8.close();
         } catch (Throwable var14) {
            try {
               var7.close();
            } catch (Throwable var11) {
               var14.addSuppressed(var11);
            }

            throw var14;
         }

         var7.close();
      } catch (IOException var15) {
         var15.printStackTrace();
      }

      return var4 + var5 >= var3;
   }

   static void receiveRequestData(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadStringUTF(var0);
      if ("descriptors.bin".equals(var2)) {
         try {
            instance.receiveDataZombieDescriptors(var0);
         } catch (IOException var5) {
            var5.printStackTrace();
         }

         instance.request = GameClient.RequestState.ReceivedDescriptors;
      }

      if ("playerzombiedesc".equals(var2)) {
         try {
            instance.receivePlayerZombieDescriptors(var0);
         } catch (IOException var4) {
            var4.printStackTrace();
         }

         instance.request = GameClient.RequestState.ReceivedPlayerZombieDescriptors;
      }

      boolean var3;
      if ("map_meta.bin".equals(var2)) {
         var3 = instance.receiveLargeFilePart(var0, ZomboidFileSystem.instance.getFileNameInCurrentSave("map_meta.bin"));
         if (var3) {
            instance.request = GameClient.RequestState.ReceivedMetaGrid;
         }
      }

      if ("map_zone.bin".equals(var2)) {
         var3 = instance.receiveLargeFilePart(var0, ZomboidFileSystem.instance.getFileNameInCurrentSave("map_zone.bin"));
         if (var3) {
            instance.request = GameClient.RequestState.ReceivedMapZone;
         }
      }

   }

   public void GameLoadingRequestData() {
      this.request = GameClient.RequestState.Start;

      while(this.request != GameClient.RequestState.Complete) {
         ByteBufferWriter var1;
         switch(this.request) {
         case Start:
            var1 = connection.startPacket();
            PacketTypes.PacketType.RequestData.doPacket(var1);
            var1.putUTF("descriptors.bin");
            PacketTypes.PacketType.RequestData.send(connection);
            this.request = GameClient.RequestState.RequestDescriptors;
         case RequestDescriptors:
         case RequestMetaGrid:
         case RequestMapZone:
         case RequestPlayerZombieDescriptors:
         case Complete:
         default:
            break;
         case ReceivedDescriptors:
            var1 = connection.startPacket();
            PacketTypes.PacketType.RequestData.doPacket(var1);
            var1.putUTF("map_meta.bin");
            PacketTypes.PacketType.RequestData.send(connection);
            this.request = GameClient.RequestState.RequestMetaGrid;
            break;
         case ReceivedMetaGrid:
            var1 = connection.startPacket();
            PacketTypes.PacketType.RequestData.doPacket(var1);
            var1.putUTF("map_zone.bin");
            PacketTypes.PacketType.RequestData.send(connection);
            this.request = GameClient.RequestState.RequestMapZone;
            break;
         case ReceivedMapZone:
            var1 = connection.startPacket();
            PacketTypes.PacketType.RequestData.doPacket(var1);
            var1.putUTF("playerzombiedesc");
            PacketTypes.PacketType.RequestData.send(connection);
            this.request = GameClient.RequestState.RequestPlayerZombieDescriptors;
            break;
         case ReceivedPlayerZombieDescriptors:
            this.request = GameClient.RequestState.Complete;
         }

         try {
            Thread.sleep(30L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

   }

   static void receiveMetaGrid(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      short var3 = var0.getShort();
      short var4 = var0.getShort();
      IsoMetaGrid var5 = IsoWorld.instance.MetaGrid;
      if (var2 >= var5.getMinX() && var2 <= var5.getMaxX() && var3 >= var5.getMinY() && var3 <= var5.getMaxY()) {
         IsoMetaCell var6 = var5.getCellData(var2, var3);
         if (var6.info != null && var4 >= 0 && var4 < var6.info.RoomList.size()) {
            var6.info.getRoom(var4).def.bLightsActive = var0.get() == 1;
         }
      }
   }

   static void receiveSendCustomColor(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var6 != null && var5 < var6.getObjects().size()) {
            IsoObject var7 = (IsoObject)var6.getObjects().get(var5);
            if (var7 != null) {
               var7.setCustomColor(new ColorInfo(var0.getFloat(), var0.getFloat(), var0.getFloat(), var0.getFloat()));
            }
         }

      }
   }

   static void receiveServerPulse(ByteBuffer var0, short var1) {
      if (ServerPulseGraph.instance != null) {
         ServerPulseGraph.instance.add(var0.getLong());
      }

   }

   static void receiveUpdateItemSprite(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      String var3 = GameWindow.ReadStringUTF(var0);
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      int var6 = var0.getInt();
      int var7 = var0.getInt();
      IsoGridSquare var8 = IsoWorld.instance.CurrentCell.getGridSquare(var4, var5, var6);
      if (var8 == null) {
         instance.delayPacket(var4, var5, var6);
      } else {
         if (var8 != null && var7 < var8.getObjects().size()) {
            try {
               IsoObject var9 = (IsoObject)var8.getObjects().get(var7);
               if (var9 != null) {
                  boolean var10 = var9.sprite != null && var9.sprite.getProperties().Is("HitByCar") && var9.sprite.getProperties().Val("DamagedSprite") != null && !var9.sprite.getProperties().Val("DamagedSprite").isEmpty();
                  var9.sprite = IsoSpriteManager.instance.getSprite(var2);
                  if (var9.sprite == null && !var3.isEmpty()) {
                     var9.setSprite(var3);
                  }

                  var9.RemoveAttachedAnims();
                  int var11 = var0.get() & 255;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     int var13 = var0.getInt();
                     IsoSprite var14 = IsoSpriteManager.instance.getSprite(var13);
                     if (var14 != null) {
                        var9.AttachExistingAnim(var14, 0, 0, false, 0, false, 0.0F);
                     }
                  }

                  if (var9 instanceof IsoThumpable && var10 && (var9.sprite == null || !var9.sprite.getProperties().Is("HitByCar"))) {
                     ((IsoThumpable)var9).setBlockAllTheSquare(false);
                  }

                  var8.RecalcAllWithNeighbours(true);
               }
            } catch (Exception var15) {
            }
         }

      }
   }

   static void receiveUpdateOverlaySprite(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadStringUTF(var0);
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      float var6 = var0.getFloat();
      float var7 = var0.getFloat();
      float var8 = var0.getFloat();
      float var9 = var0.getFloat();
      int var10 = var0.getInt();
      IsoGridSquare var11 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
      if (var11 == null) {
         instance.delayPacket(var3, var4, var5);
      } else {
         if (var11 != null && var10 < var11.getObjects().size()) {
            try {
               IsoObject var12 = (IsoObject)var11.getObjects().get(var10);
               if (var12 != null) {
                  var12.setOverlaySprite(var2, var6, var7, var8, var9, false);
               }
            } catch (Exception var13) {
            }
         }

      }
   }

   private KahluaTable copyTable(KahluaTable var1) {
      KahluaTable var2 = LuaManager.platform.newTable();
      KahluaTableIterator var3 = var1.iterator();

      while(var3.advance()) {
         Object var4 = var3.getKey();
         Object var5 = var3.getValue();
         if (var5 instanceof KahluaTable) {
            var2.rawset(var4, this.copyTable((KahluaTable)var5));
         } else {
            var2.rawset(var4, var5);
         }
      }

      return var2;
   }

   public KahluaTable getServerSpawnRegions() {
      return this.copyTable(this.ServerSpawnRegions);
   }

   public static void toggleSafety(IsoPlayer var0) {
      if (var0 != null) {
         ByteBufferWriter var1 = connection.startPacket();
         PacketTypes.PacketType.ChangeSafety.doPacket(var1);
         var1.putByte((byte)var0.PlayerIndex);
         PacketTypes.PacketType.ChangeSafety.send(connection);
      }
   }

   static void receiveStartFire(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      boolean var6 = var0.get() == 1;
      int var7 = var0.getInt();
      int var8 = var0.getInt();
      int var9 = var0.getInt();
      boolean var10 = var0.get() == 1;
      IsoGridSquare var11 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var11 == null) {
         instance.delayPacket(var2, var3, var4);
      } else if (!IsoFire.CanAddFire(var11, var6, var10)) {
         DebugLog.log("not adding fire that is on the server " + var2 + "," + var3);
      } else {
         IsoFire var12 = var10 ? new IsoFire(IsoWorld.instance.CurrentCell, var11, var6, var5, var8, true) : new IsoFire(IsoWorld.instance.CurrentCell, var11, var6, var5, var8);
         var12.SpreadDelay = var7;
         var12.numFlameParticles = var9;
         IsoFireManager.Add(var12);
         var11.getObjects().add(var12);
      }
   }

   static void receiveAddCorpseToMap(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoObject var5 = WorldItemTypes.createFromBuffer(var0);
      var5.loadFromRemoteBuffer(var0, false);
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var6 != null) {
            var6.addCorpse((IsoDeadBody)var5, true);
         }

      }
   }

   static void receiveReceiveModData(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 == null && IsoWorld.instance.isValidSquare(var2, var3, var4) && IsoWorld.instance.CurrentCell.getChunkForGridSquare(var2, var3, var4) != null) {
         var5 = IsoGridSquare.getNew(IsoWorld.instance.getCell(), (SliceY)null, var2, var3, var4);
      }

      if (var5 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         try {
            var5.getModData().load((ByteBuffer)var0, 186);
         } catch (IOException var7) {
            var7.printStackTrace();
         }

         LuaEventManager.triggerEvent("onLoadModDataFromServer", var5);
      }
   }

   static void receiveObjectModData(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      boolean var6 = var0.get() == 1;
      IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var7 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var7 != null && var5 >= 0 && var5 < var7.getObjects().size()) {
            IsoObject var8 = (IsoObject)var7.getObjects().get(var5);
            if (var6) {
               try {
                  var8.getModData().load((ByteBuffer)var0, 186);
               } catch (IOException var10) {
                  var10.printStackTrace();
               }
            } else {
               var8.getModData().wipe();
            }
         } else if (var7 != null) {
            DebugLog.log("receiveObjectModData: index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
         } else if (Core.bDebug) {
            DebugLog.log("receiveObjectModData: sq is null x,y,z=" + var2 + "," + var3 + "," + var4);
         }

      }
   }

   static void receiveObjectChange(ByteBuffer var0, short var1) {
      byte var2 = var0.get();
      short var3;
      String var4;
      if (var2 == 1) {
         var3 = var0.getShort();
         var4 = GameWindow.ReadString(var0);
         if (Core.bDebug) {
            DebugLog.log("receiveObjectChange " + var4);
         }

         IsoPlayer var5 = (IsoPlayer)IDToPlayerMap.get(var3);
         if (var5 != null) {
            var5.loadChange(var4, var0);
         }
      } else if (var2 == 2) {
         var3 = var0.getShort();
         var4 = GameWindow.ReadString(var0);
         if (Core.bDebug) {
            DebugLog.log("receiveObjectChange " + var4);
         }

         BaseVehicle var13 = VehicleManager.instance.getVehicleByID(var3);
         if (var13 != null) {
            var13.loadChange(var4, var0);
         } else if (Core.bDebug) {
            DebugLog.log("receiveObjectChange: unknown vehicle id=" + var3);
         }
      } else {
         int var6;
         String var7;
         IsoGridSquare var8;
         int var11;
         int var12;
         int var14;
         if (var2 == 3) {
            var11 = var0.getInt();
            var12 = var0.getInt();
            var14 = var0.getInt();
            var6 = var0.getInt();
            var7 = GameWindow.ReadString(var0);
            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange " + var7);
            }

            var8 = IsoWorld.instance.CurrentCell.getGridSquare(var11, var12, var14);
            if (var8 == null) {
               instance.delayPacket(var11, var12, var14);
               return;
            }

            for(int var9 = 0; var9 < var8.getWorldObjects().size(); ++var9) {
               IsoWorldInventoryObject var10 = (IsoWorldInventoryObject)var8.getWorldObjects().get(var9);
               if (var10.getItem() != null && var10.getItem().getID() == var6) {
                  var10.loadChange(var7, var0);
                  return;
               }
            }

            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange: itemID=" + var6 + " is invalid x,y,z=" + var11 + "," + var12 + "," + var14);
            }
         } else {
            var11 = var0.getInt();
            var12 = var0.getInt();
            var14 = var0.getInt();
            var6 = var0.getInt();
            var7 = GameWindow.ReadString(var0);
            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange " + var7);
            }

            var8 = IsoWorld.instance.CurrentCell.getGridSquare(var11, var12, var14);
            if (var8 == null) {
               instance.delayPacket(var11, var12, var14);
               return;
            }

            if (var8 != null && var6 >= 0 && var6 < var8.getObjects().size()) {
               IsoObject var15 = (IsoObject)var8.getObjects().get(var6);
               var15.loadChange(var7, var0);
            } else if (var8 != null) {
               if (Core.bDebug) {
                  DebugLog.log("receiveObjectChange: index=" + var6 + " is invalid x,y,z=" + var11 + "," + var12 + "," + var14);
               }
            } else if (Core.bDebug) {
               DebugLog.log("receiveObjectChange: sq is null x,y,z=" + var11 + "," + var12 + "," + var14);
            }
         }
      }

   }

   static void receiveKeepAlive(ByteBuffer var0, short var1) {
      MPDebugInfo.instance.clientPacket(var0);
   }

   static void receiveSmashWindow(ByteBuffer var0, short var1) {
      IsoObject var2 = instance.getIsoObjectRefFromByteBuffer(var0);
      if (var2 instanceof IsoWindow) {
         byte var3 = var0.get();
         if (var3 == 1) {
            ((IsoWindow)var2).smashWindow(true);
         } else if (var3 == 2) {
            ((IsoWindow)var2).setGlassRemoved(true);
         }
      } else if (Core.bDebug) {
         DebugLog.log("SmashWindow not a window!");
      }

   }

   static void receiveRemoveContestedItemsFromInventory(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var0.getInt();

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null && !var6.isDead()) {
               var6.getInventory().removeItemWithIDRecurse(var4);
            }
         }
      }

   }

   static void receiveServerQuit(ByteBuffer var0, short var1) {
      GameWindow.savePlayer();
      GameWindow.kickReason = "Server shut down safely. Players and map data saved.";
      GameWindow.bServerDisconnected = true;
   }

   static void receiveHitCharacter(ByteBuffer var0, short var1) {
      try {
         HitCharacterPacket var2 = HitCharacterPacket.process(var0);
         if (var2 != null) {
            var2.parse(var0);
            if (Core.bDebug) {
               if (!DebugLog.isEnabled(DebugType.Damage)) {
                  DebugLog.log(DebugType.Multiplayer, "ReceiveHitCharacter: " + var2.getHitDescription());
               }

               DebugLog.log(DebugType.Damage, "ReceiveHitCharacter: " + var2.getDescription());
            }

            var2.tryProcess();
         }
      } catch (Exception var3) {
         DebugLog.Multiplayer.printException(var3, "ReceiveHitCharacter: failed", LogSeverity.Error);
      }

   }

   public static boolean sendHitCharacter(IsoGameCharacter var0, IsoMovingObject var1, HandWeapon var2, float var3, boolean var4, float var5, boolean var6, boolean var7, boolean var8) {
      boolean var9 = false;
      ByteBufferWriter var10 = connection.startPacket();
      PacketTypes.PacketType.HitCharacter.doPacket(var10);

      try {
         Object var11 = null;
         if (var0 instanceof IsoZombie) {
            if (var1 instanceof IsoPlayer) {
               ZombieHitPlayerPacket var12 = new ZombieHitPlayerPacket();
               var12.set((IsoZombie)var0, (IsoPlayer)var1);
               var11 = var12;
            } else {
               DebugLog.Multiplayer.warn(String.format("SendHitCharacter: unknown target type (wielder=%s, target=%s)", var0.getClass().getName(), var1.getClass().getName()));
            }
         } else if (var0 instanceof IsoPlayer) {
            if (var1 == null) {
               PlayerHitSquarePacket var14 = new PlayerHitSquarePacket();
               var14.set((IsoPlayer)var0, var2, var6);
               var11 = var14;
            } else if (var1 instanceof IsoPlayer) {
               PlayerHitPlayerPacket var15 = new PlayerHitPlayerPacket();
               var15.set((IsoPlayer)var0, (IsoPlayer)var1, var2, var3, var4, var5, var6, var8);
               var11 = var15;
            } else if (var1 instanceof IsoZombie) {
               PlayerHitZombiePacket var16 = new PlayerHitZombiePacket();
               var16.set((IsoPlayer)var0, (IsoZombie)var1, var2, var3, var4, var5, var6, var7, var8);
               var11 = var16;
            } else if (var1 instanceof BaseVehicle) {
               PlayerHitVehiclePacket var17 = new PlayerHitVehiclePacket();
               var17.set((IsoPlayer)var0, (BaseVehicle)var1, var2, var6);
               var11 = var17;
            } else {
               DebugLog.Multiplayer.warn(String.format("SendHitCharacter: unknown target type (wielder=%s, target=%s)", var0.getClass().getName(), var1.getClass().getName()));
            }
         } else {
            DebugLog.Multiplayer.warn(String.format("SendHitCharacter: unknown wielder type (wielder=%s, target=%s)", var0.getClass().getName(), var1.getClass().getName()));
         }

         if (var11 != null) {
            ((HitCharacterPacket)var11).write(var10);
            PacketTypes.PacketType.HitCharacter.send(connection);
            if (Core.bDebug) {
               if (!DebugLog.isEnabled(DebugType.Damage)) {
                  DebugLog.log(DebugType.Multiplayer, "SendHitCharacter: " + ((HitCharacterPacket)var11).getHitDescription());
               }

               DebugLog.log(DebugType.Damage, "SendHitCharacter: " + ((HitCharacterPacket)var11).getDescription());
            }

            var9 = true;
         }
      } catch (Exception var13) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var13, "SendHitCharacter: failed", LogSeverity.Error);
      }

      return var9;
   }

   public static void sendHitVehicle(IsoPlayer var0, IsoGameCharacter var1, BaseVehicle var2, float var3, boolean var4, int var5, float var6, boolean var7) {
      ByteBufferWriter var8 = connection.startPacket();
      PacketTypes.PacketType.HitCharacter.doPacket(var8);

      try {
         Object var9 = null;
         if (var1 instanceof IsoPlayer) {
            VehicleHitPlayerPacket var10 = new VehicleHitPlayerPacket();
            var10.set(var0, (IsoPlayer)var1, var2, var3, var4, var5, var6, var7);
            var9 = var10;
         } else if (var1 instanceof IsoZombie) {
            VehicleHitZombiePacket var12 = new VehicleHitZombiePacket();
            var12.set(var0, (IsoZombie)var1, var2, var3, var4, var5, var6, var7);
            var9 = var12;
         } else {
            DebugLog.Multiplayer.warn(String.format("SendHitVehicle: unknown target type (wielder=%s, target=%s)", var0.getClass().getName(), var1.getClass().getName()));
         }

         if (var9 != null) {
            ((VehicleHitPacket)var9).write(var8);
            PacketTypes.PacketType.HitCharacter.send(connection);
            if (Core.bDebug) {
               if (!DebugLog.isEnabled(DebugType.Damage)) {
                  DebugLog.log(DebugType.Multiplayer, "SendHitVehicle: " + ((VehicleHitPacket)var9).getHitDescription());
               }

               DebugLog.log(DebugType.Damage, "SendHitVehicle: " + ((VehicleHitPacket)var9).getDescription());
            }
         }
      } catch (Exception var11) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var11, "SendHitVehicle: failed", LogSeverity.Error);
      }

   }

   static void receiveZombieDeath(ByteBuffer var0, short var1) {
      try {
         DeadZombiePacket var2 = new DeadZombiePacket();
         var2.parse(var0);
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Death)) {
               DebugLog.log(DebugType.Multiplayer, "ReceiveZombieDeath: " + var2.getDeathDescription());
            }

            DebugLog.log(DebugType.Death, "ReceiveZombieDeath: " + var2.getDescription());
         }
      } catch (Exception var3) {
         DebugLog.Multiplayer.printException(var3, "ReceiveZombieDeath: failed", LogSeverity.Error);
      }

   }

   public static void sendZombieDeath(IsoZombie var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.ZombieDeath.doPacket(var1);

      try {
         DeadZombiePacket var2 = new DeadZombiePacket();
         var2.set(var0);
         var2.write(var1);
         PacketTypes.PacketType.ZombieDeath.send(connection);
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Death)) {
               DebugLog.log(DebugType.Multiplayer, "SendZombieDeath: " + var2.getDeathDescription());
            }

            DebugLog.log(DebugType.Death, "SendZombieDeath: " + var2.getDescription());
         }
      } catch (Exception var3) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var3, "SendZombieDeath: failed", LogSeverity.Error);
      }

   }

   static void receivePlayerDeath(ByteBuffer var0, short var1) {
      try {
         DeadPlayerPacket var2 = new DeadPlayerPacket();
         var2.parse(var0);
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Death)) {
               DebugLog.log(DebugType.Multiplayer, "ReceivePlayerDeath: " + var2.getDeathDescription());
            }

            DebugLog.log(DebugType.Death, "ReceivePlayerDeath: " + var2.getDescription());
         }
      } catch (Exception var3) {
         DebugLog.Multiplayer.printException(var3, "ReceivePlayerDeath: failed", LogSeverity.Error);
      }

   }

   public void sendPlayerDeath(IsoPlayer var1) {
      var1.setTransactionID(0);
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.PlayerDeath.doPacket(var2);

      try {
         DeadPlayerPacket var3 = new DeadPlayerPacket();
         var3.set(var1);
         var3.write(var2);
         PacketTypes.PacketType.PlayerDeath.send(connection);
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Death)) {
               DebugLog.log(DebugType.Multiplayer, "SendPlayerDeath: " + var3.getDeathDescription());
            }

            DebugLog.log(DebugType.Death, "SendPlayerDeath: " + var3.getDescription());
         }
      } catch (Exception var4) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var4, "SendPlayerDeath: failed", LogSeverity.Error);
      }

   }

   static void receivePlayerDamage(ByteBuffer var0, short var1) {
      try {
         short var2 = var0.getShort();
         float var3 = var0.getFloat();
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Damage)) {
               DebugLog.log(DebugType.Multiplayer, "ReceivePlayerDamage: " + var2);
            }

            DebugLog.log(DebugType.Damage, "ReceivePlayerDamage: " + var2);
         }

         IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
         if (var4 != null) {
            var4.getBodyDamage().load(var0, IsoWorld.getWorldVersion());
            var4.getStats().setPain(var3);
            var4.getBodyDamage().Update();
            if (ServerOptions.instance.PlayerSaveOnDamage.getValue()) {
               GameWindow.savePlayer();
            }
         }
      } catch (Exception var5) {
         DebugLog.Multiplayer.printException(var5, "ReceivePlayerDamage: failed", LogSeverity.Error);
      }

   }

   public static void sendPlayerDamage(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.PlayerDamage.doPacket(var1);

      try {
         var1.putShort(var0.getOnlineID());
         var1.putFloat(var0.getStats().getPain());
         var0.getBodyDamage().save(var1.bb);
         PacketTypes.PacketType.PlayerDamage.send(connection);
         if (Core.bDebug) {
            if (!DebugLog.isEnabled(DebugType.Damage)) {
               DebugLog.log(DebugType.Multiplayer, "SendPlayerDamage: " + var0.getOnlineID());
            }

            DebugLog.log(DebugType.Damage, "SendPlayerDamage: " + var0.getOnlineID());
         }
      } catch (Exception var3) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var3, "SendPlayerDamage: failed", LogSeverity.Error);
      }

   }

   static void receiveSyncInjuries(ByteBuffer var0, short var1) {
      try {
         SyncInjuriesPacket var2 = new SyncInjuriesPacket();
         var2.parse(var0);
         if (Core.bDebug) {
            String var3 = String.format("Receive: %s", var2.getDescription());
            if (!DebugLog.isEnabled(DebugType.Damage)) {
               DebugLog.log(DebugType.Multiplayer, var3);
            }

            DebugLog.log(DebugType.Damage, var3);
         }

         IsoPlayer var5 = (IsoPlayer)IDToPlayerMap.get(var2.id);
         if (var5 != null && !var5.isLocalPlayer()) {
            var2.process(var5);
         }
      } catch (Exception var4) {
         DebugLog.Multiplayer.printException(var4, "ReceivePlayerInjuries: failed", LogSeverity.Error);
      }

   }

   public static void sendPlayerInjuries(IsoPlayer var0) {
      SyncInjuriesPacket var1 = new SyncInjuriesPacket();
      var1.set(var0);
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SyncInjuries.doPacket(var2);

      try {
         var1.write(var2);
         PacketTypes.PacketType.SyncInjuries.send(connection);
         if (Core.bDebug) {
            String var3 = String.format("Send: %s", var1.getDescription());
            if (!DebugLog.isEnabled(DebugType.Damage)) {
               DebugLog.log(DebugType.Multiplayer, var3);
            }

            DebugLog.log(DebugType.Damage, var3);
         }
      } catch (Exception var4) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var4, "SendPlayerInjuries: failed", LogSeverity.Error);
      }

   }

   static void receiveRemoveCorpseFromMap(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      short var6 = var0.getShort();
      if (Core.bDebug) {
         String var7 = String.format("ReceiveRemoveCorpse: id=%d, index=%d, pos=( %d ; %d ; %d )", var6, var5, var2, var3, var4);
         if (!DebugLog.isEnabled(DebugType.Death)) {
            DebugLog.log(DebugType.Multiplayer, var7);
         }

         DebugLog.log(DebugType.Death, var7);
      }

      IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var9 == null) {
         instance.delayPacket(var2, var3, var4);
         DebugLog.Multiplayer.error("ReceiveRemoveCorpse: incorrect square");
      } else {
         if (var5 >= 0 && var5 < var9.getStaticMovingObjects().size()) {
            IsoDeadBody var8 = (IsoDeadBody)var9.getStaticMovingObjects().get(var5);
            var9.removeCorpse(var8, true);
         } else {
            DebugLog.Multiplayer.error("ReceiveRemoveCorpse: no corpse on square");
         }

      }
   }

   public static void sendRemoveCorpseFromMap(IsoDeadBody var0) {
      int var1 = var0.getSquare().getX();
      int var2 = var0.getSquare().getY();
      int var3 = var0.getSquare().getZ();
      int var4 = var0.getSquare().getStaticMovingObjects().indexOf(var0);
      short var5 = var0.getOnlineID();
      if (Core.bDebug) {
         String var6 = String.format("SendRemoveCorpse: id=%d, index=%d, pos=( %d ; %d ; %d )", var5, var4, var1, var2, var3);
         if (!DebugLog.isEnabled(DebugType.Death)) {
            DebugLog.log(DebugType.Multiplayer, var6);
         }

         DebugLog.log(DebugType.Death, var6);
      }

      ByteBufferWriter var7 = connection.startPacket();
      PacketTypes.PacketType.RemoveCorpseFromMap.doPacket(var7);
      var7.putInt(var1);
      var7.putInt(var2);
      var7.putInt(var3);
      var7.putInt(var4);
      var7.putShort(var5);
      PacketTypes.PacketType.RemoveCorpseFromMap.send(connection);
   }

   public static void sendEvent(IsoPlayer var0, String var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.EventPacket.doPacket(var2);

      try {
         EventPacket var3 = new EventPacket();
         if (var3.set(var0, var1)) {
            var3.write(var2);
            PacketTypes.PacketType.EventPacket.send(connection);
            if (Core.bDebug) {
               DebugLog.log(DebugType.Multiplayer, "SendEvent: " + var3.getDescription());
            }
         } else {
            connection.cancelPacket();
         }
      } catch (Exception var4) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var4, "SendEvent: failed", LogSeverity.Error);
      }

   }

   static void receiveEventPacket(ByteBuffer var0, short var1) {
      try {
         EventPacket var2 = new EventPacket();
         var2.parse(var0);
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "ReceiveEvent: " + var2.getDescription());
         }

         var2.tryProcess();
      } catch (Exception var3) {
         DebugLog.Multiplayer.printException(var3, "ReceiveEvent: failed", LogSeverity.Error);
      }

   }

   public static void sendKillZombie(IsoZombie var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.KillZombie.doPacket(var1);

      try {
         var1.putShort(var0.getOnlineID());
         var1.putBoolean(var0.isFallOnFront());
         PacketTypes.PacketType.KillZombie.send(connection);
         if (Core.bDebug) {
            String var2 = String.format("SendKillZombie: id=%d, isFallOnFront=%b", var0.getOnlineID(), var0.isFallOnFront());
            if (!DebugLog.isEnabled(DebugType.Death)) {
               DebugLog.log(DebugType.Multiplayer, var2);
            }

            DebugLog.log(DebugType.Death, var2);
         }
      } catch (Exception var3) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var3, "SendKillZombie: failed", LogSeverity.Error);
      }

   }

   static void receiveKillZombie(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoZombie var3 = (IsoZombie)IDToZombieMap.get(var2);
      if (var3 != null) {
         if (var3.getCurrentSquare() == null) {
            if (Core.bDebug) {
               DebugLog.log(DebugType.Death, String.format("ReceiveKillZombie %d: no current square", var2));
            }

            return;
         }

         if (!var3.shouldBecomeCorpse()) {
            if (Core.bDebug) {
               DebugLog.log(DebugType.Multiplayer, String.format("ReceiveKillZombie %d: wait for death", var2));
            }

            return;
         }

         if (Core.bDebug) {
            DebugLog.Multiplayer.warn(String.format("ReceiveKillZombie %d: create corpse", var2));
         }

         new IsoDeadBody(var3);
         if (IDToZombieMap.containsKey(var2)) {
            DebugLog.Multiplayer.error(String.format("ReceiveKillZombie %d: zombie is still present", var2));
         }
      } else if (Core.bDebug) {
         DebugLog.log(DebugType.Death, String.format("ReceiveKillZombie %d: already killed", var2));
      }

   }

   public static void sendAction(BaseAction var0, boolean var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.ActionPacket.doPacket(var2);

      try {
         ActionPacket var3 = new ActionPacket();
         var3.set(var1, var0);
         var3.write(var2);
         PacketTypes.PacketType.ActionPacket.send(connection);
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "SendAction: " + var3.getDescription());
         }
      } catch (Exception var4) {
         connection.cancelPacket();
         DebugLog.Multiplayer.printException(var4, "SendAction: failed", LogSeverity.Error);
      }

   }

   static void receiveActionPacket(ByteBuffer var0, short var1) {
      try {
         ActionPacket var2 = new ActionPacket();
         var2.parse(var0);
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "ReceiveAction: " + var2.getDescription());
         }

         var2.process();
      } catch (Exception var3) {
         DebugLog.Multiplayer.printException(var3, "ReceiveAction: failed", LogSeverity.Error);
      }

   }

   public static void sendEatBody(IsoZombie var0, IsoMovingObject var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.EatBody.doPacket(var2);

      try {
         var2.putShort(var0.getOnlineID());
         if (var1 instanceof IsoDeadBody) {
            IsoDeadBody var3 = (IsoDeadBody)var1;
            var2.putByte((byte)1);
            var2.putBoolean(var0.getVariableBoolean("onknees"));
            var2.putFloat(var0.getEatSpeed());
            var2.putFloat(var0.getStateEventDelayTimer());
            var2.putInt(var3.getStaticMovingObjectIndex());
            var2.putFloat((float)var3.getSquare().getX());
            var2.putFloat((float)var3.getSquare().getY());
            var2.putFloat((float)var3.getSquare().getZ());
         } else if (var1 instanceof IsoPlayer) {
            var2.putByte((byte)2);
            var2.putBoolean(var0.getVariableBoolean("onknees"));
            var2.putFloat(var0.getEatSpeed());
            var2.putFloat(var0.getStateEventDelayTimer());
            var2.putShort(((IsoPlayer)var1).getOnlineID());
         } else {
            var2.putByte((byte)0);
         }

         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "SendEatBody");
         }

         PacketTypes.PacketType.EatBody.send(connection);
      } catch (Exception var4) {
         DebugLog.Multiplayer.printException(var4, "SendEatBody: failed", LogSeverity.Error);
         connection.cancelPacket();
      }

   }

   public static void receiveEatBody(ByteBuffer var0, short var1) {
      try {
         short var2 = var0.getShort();
         byte var3 = var0.get();
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, String.format("ReceiveEatBody: zombie=%d type=%d", var2, var3));
         }

         IsoZombie var4 = (IsoZombie)IDToZombieMap.get(var2);
         if (var4 == null) {
            DebugLog.Multiplayer.error("ReceiveEatBody: zombie " + var2 + " not found");
            return;
         }

         boolean var5;
         float var6;
         float var7;
         if (var3 == 1) {
            var5 = var0.get() != 0;
            var6 = var0.getFloat();
            var7 = var0.getFloat();
            int var8 = var0.getInt();
            float var9 = var0.getFloat();
            float var10 = var0.getFloat();
            float var11 = var0.getFloat();
            IsoGridSquare var12 = IsoWorld.instance.CurrentCell.getGridSquare((double)var9, (double)var10, (double)var11);
            if (var12 == null) {
               DebugLog.Multiplayer.error("ReceiveEatBody: incorrect square");
               return;
            }

            if (var8 >= 0 && var8 < var12.getStaticMovingObjects().size()) {
               IsoDeadBody var13 = (IsoDeadBody)var12.getStaticMovingObjects().get(var8);
               if (var13 != null) {
                  var4.setTarget((IsoMovingObject)null);
                  var4.setEatBodyTarget(var13, true, var6);
                  var4.setVariable("onknees", var5);
                  var4.setStateEventDelayTimer(var7);
               } else {
                  DebugLog.Multiplayer.error("ReceiveEatBody: no corpse with index " + var8 + " on square");
               }
            } else {
               DebugLog.Multiplayer.error("ReceiveEatBody: no corpse on square");
            }
         } else if (var3 == 2) {
            var5 = var0.get() != 0;
            var6 = var0.getFloat();
            var7 = var0.getFloat();
            short var15 = var0.getShort();
            IsoPlayer var16 = (IsoPlayer)IDToPlayerMap.get(var15);
            if (var16 == null) {
               DebugLog.Multiplayer.error("ReceiveEatBody: player " + var15 + " not found");
               return;
            }

            var4.setTarget((IsoMovingObject)null);
            var4.setEatBodyTarget(var16, true, var6);
            var4.setVariable("onknees", var5);
            var4.setStateEventDelayTimer(var7);
         } else {
            var4.setEatBodyTarget((IsoMovingObject)null, false);
         }
      } catch (Exception var14) {
         DebugLog.Multiplayer.printException(var14, "ReceiveEatBody: failed", LogSeverity.Error);
      }

   }

   public static void sendThump(IsoGameCharacter var0, Thumpable var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.Thump.doPacket(var2);

      try {
         short var3 = var0.getOnlineID();
         String var4 = var0.getVariableString("ThumpType");
         var2.putShort(var3);
         var2.putByte((byte)NetworkVariables.ThumpType.fromString(var4).ordinal());
         if (var1 instanceof IsoObject) {
            IsoObject var5 = (IsoObject)var1;
            var2.putInt(var5.getObjectIndex());
            var2.putFloat((float)var5.getSquare().getX());
            var2.putFloat((float)var5.getSquare().getY());
            var2.putFloat((float)var5.getSquare().getZ());
         } else {
            var2.putInt(-1);
         }

         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, String.format("SendThump: zombie=%d type=%s target=%s", var3, var4, var1 == null ? "null" : var1.getClass().getSimpleName()));
         }

         PacketTypes.PacketType.Thump.send(connection);
      } catch (Exception var6) {
         DebugLog.Multiplayer.printException(var6, "SendThump: failed", LogSeverity.Error);
         connection.cancelPacket();
      }

   }

   public static void receiveThump(ByteBuffer var0, short var1) {
      try {
         short var2 = var0.getShort();
         String var3 = NetworkVariables.ThumpType.fromByte(var0.get()).toString();
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, String.format("ReceiveThump: zombie=%d type=%s", var2, var3));
         }

         IsoZombie var4 = (IsoZombie)IDToZombieMap.get(var2);
         if (var4 == null) {
            DebugLog.Multiplayer.error("ReceiveThump: zombie " + var2 + " not found");
            return;
         }

         var4.setVariable("ThumpType", var3);
         int var5 = var0.getInt();
         if (var5 == -1) {
            var4.setThumpTarget((Thumpable)null);
            return;
         }

         float var6 = var0.getFloat();
         float var7 = var0.getFloat();
         float var8 = var0.getFloat();
         IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare((double)var6, (double)var7, (double)var8);
         if (var9 == null) {
            DebugLog.Multiplayer.error("ReceiveThump: incorrect square");
            return;
         }

         IsoObject var10 = (IsoObject)var9.getObjects().get(var5);
         if (var10 instanceof Thumpable) {
            var4.setThumpTarget(var10);
         } else {
            DebugLog.Multiplayer.error("ReceiveThump: no thumpable with index " + var5 + " on square");
         }
      } catch (Exception var11) {
         DebugLog.Multiplayer.printException(var11, "ReceiveThump: failed", LogSeverity.Error);
      }

   }

   public void sendWorldSound(WorldSoundManager.WorldSound var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.WorldSound.doPacket(var2);
      var2.putInt(var1.x);
      var2.putInt(var1.y);
      var2.putInt(var1.z);
      var2.putInt(var1.radius);
      var2.putInt(var1.volume);
      var2.putByte((byte)(var1.stresshumans ? 1 : 0));
      var2.putFloat(var1.zombieIgnoreDist);
      var2.putFloat(var1.stressMod);
      var2.putByte((byte)(var1.sourceIsZombie ? 1 : 0));
      PacketTypes.PacketType.WorldSound.send(connection);
   }

   static void receiveRemoveItemFromSquare(ByteBuffer var0, short var1) {
      if (IsoWorld.instance.CurrentCell != null) {
         int var2 = var0.getInt();
         int var3 = var0.getInt();
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
         if (var6 == null) {
            instance.delayPacket(var2, var3, var4);
         } else {
            if (var6 != null && var5 >= 0 && var5 < var6.getObjects().size()) {
               IsoObject var7 = (IsoObject)var6.getObjects().get(var5);
               var6.RemoveTileObject(var7);
               if (var7 instanceof IsoWorldInventoryObject || var7.getContainer() != null) {
                  LuaEventManager.triggerEvent("OnContainerUpdate", var7);
               }
            } else if (Core.bDebug) {
               DebugLog.log("RemoveItemFromMap: sq is null or index is invalid");
            }

         }
      }
   }

   static void receiveLoadPlayerProfile(ByteBuffer var0, short var1) {
      ClientPlayerDB.getInstance().clientLoadNetworkCharacter(var0, connection);
   }

   static void receiveRemoveInventoryItemFromContainer(ByteBuffer var0, short var1) {
      if (IsoWorld.instance.CurrentCell != null) {
         ByteBufferReader var2 = new ByteBufferReader(var0);
         short var3 = var0.getShort();
         int var4 = var2.getInt();
         int var5 = var2.getInt();
         int var6 = var2.getInt();
         IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var4, var5, var6);
         if (var7 != null) {
            byte var16;
            int var18;
            int var22;
            int var25;
            if (var3 == 0) {
               var16 = var2.getByte();
               var18 = var0.getInt();
               if (var16 < 0 || var16 >= var7.getStaticMovingObjects().size()) {
                  DebugLog.log("ERROR: removeItemFromContainer: invalid corpse index");
                  return;
               }

               IsoObject var21 = (IsoObject)var7.getStaticMovingObjects().get(var16);
               if (var21 != null && var21.getContainer() != null) {
                  for(var22 = 0; var22 < var18; ++var22) {
                     var25 = var2.getInt();
                     var21.getContainer().removeItemWithID(var25);
                     var21.getContainer().setExplored(true);
                  }
               }
            } else if (var3 == 1) {
               int var17 = var2.getInt();
               var18 = var0.getInt();
               ItemContainer var19 = null;

               for(var22 = 0; var22 < var7.getWorldObjects().size(); ++var22) {
                  IsoWorldInventoryObject var24 = (IsoWorldInventoryObject)var7.getWorldObjects().get(var22);
                  if (var24 != null && var24.getItem() instanceof InventoryContainer && var24.getItem().id == var17) {
                     var19 = ((InventoryContainer)var24.getItem()).getInventory();
                     break;
                  }
               }

               if (var19 == null) {
                  DebugLog.log("ERROR removeItemFromContainer can't find world item with id=" + var17);
                  return;
               }

               for(var22 = 0; var22 < var18; ++var22) {
                  var25 = var2.getInt();
                  var19.removeItemWithID(var25);
                  var19.setExplored(true);
               }
            } else {
               byte var9;
               int var10;
               int var14;
               if (var3 == 2) {
                  var16 = var2.getByte();
                  var9 = var2.getByte();
                  var10 = var0.getInt();
                  if (var16 < 0 || var16 >= var7.getObjects().size()) {
                     DebugLog.log("ERROR: removeItemFromContainer: invalid object index");
                     return;
                  }

                  IsoObject var20 = (IsoObject)var7.getObjects().get(var16);
                  ItemContainer var23 = var20 != null ? var20.getContainerByIndex(var9) : null;
                  if (var23 != null) {
                     for(int var26 = 0; var26 < var10; ++var26) {
                        var14 = var2.getInt();
                        var23.removeItemWithID(var14);
                        var23.setExplored(true);
                     }
                  }
               } else if (var3 == 3) {
                  short var8 = var2.getShort();
                  var9 = var2.getByte();
                  var10 = var0.getInt();
                  BaseVehicle var11 = VehicleManager.instance.getVehicleByID(var8);
                  if (var11 == null) {
                     DebugLog.log("ERROR: removeItemFromContainer: invalid vehicle id");
                     return;
                  }

                  VehiclePart var12 = var11.getPartByIndex(var9);
                  if (var12 == null) {
                     DebugLog.log("ERROR: removeItemFromContainer: invalid part index");
                     return;
                  }

                  ItemContainer var13 = var12.getItemContainer();
                  if (var13 == null) {
                     DebugLog.log("ERROR: removeItemFromContainer: part " + var12.getId() + " has no container");
                     return;
                  }

                  if (var13 != null) {
                     for(var14 = 0; var14 < var10; ++var14) {
                        int var15 = var2.getInt();
                        var13.removeItemWithID(var15);
                        var13.setExplored(true);
                     }

                     var12.setContainerContentAmount(var13.getCapacityWeight());
                  }
               } else {
                  DebugLog.log("ERROR: removeItemFromContainer: invalid object index");
               }
            }
         } else {
            instance.delayPacket(var4, var5, var6);
         }

      }
   }

   static void receiveAddInventoryItemToContainer(ByteBuffer var0, short var1) {
      if (IsoWorld.instance.CurrentCell != null) {
         ByteBufferReader var2 = new ByteBufferReader(var0);
         short var3 = var0.getShort();
         int var4 = var2.getInt();
         int var5 = var2.getInt();
         int var6 = var2.getInt();
         IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var4, var5, var6);
         if (var7 == null) {
            instance.delayPacket(var4, var5, var6);
         } else {
            ItemContainer var8 = null;
            VehiclePart var9 = null;
            byte var14;
            int var17;
            if (var3 == 0) {
               var14 = var2.getByte();
               if (var14 < 0 || var14 >= var7.getStaticMovingObjects().size()) {
                  DebugLog.log("ERROR: sendItemsToContainer: invalid corpse index");
                  return;
               }

               IsoObject var18 = (IsoObject)var7.getStaticMovingObjects().get(var14);
               if (var18 != null && var18.getContainer() != null) {
                  var8 = var18.getContainer();
               }
            } else if (var3 == 1) {
               int var15 = var2.getInt();

               for(var17 = 0; var17 < var7.getWorldObjects().size(); ++var17) {
                  IsoWorldInventoryObject var20 = (IsoWorldInventoryObject)var7.getWorldObjects().get(var17);
                  if (var20 != null && var20.getItem() instanceof InventoryContainer && var20.getItem().id == var15) {
                     var8 = ((InventoryContainer)var20.getItem()).getInventory();
                     break;
                  }
               }

               if (var8 == null) {
                  DebugLog.log("ERROR: sendItemsToContainer: can't find world item with id=" + var15);
                  return;
               }
            } else {
               byte var11;
               if (var3 == 2) {
                  var14 = var2.getByte();
                  var11 = var2.getByte();
                  if (var14 < 0 || var14 >= var7.getObjects().size()) {
                     DebugLog.log("ERROR: sendItemsToContainer: invalid object index");
                     return;
                  }

                  IsoObject var19 = (IsoObject)var7.getObjects().get(var14);
                  var8 = var19 != null ? var19.getContainerByIndex(var11) : null;
               } else if (var3 == 3) {
                  short var10 = var2.getShort();
                  var11 = var2.getByte();
                  BaseVehicle var12 = VehicleManager.instance.getVehicleByID(var10);
                  if (var12 == null) {
                     DebugLog.log("ERROR: sendItemsToContainer: invalid vehicle id");
                     return;
                  }

                  var9 = var12.getPartByIndex(var11);
                  if (var9 == null) {
                     DebugLog.log("ERROR: sendItemsToContainer: invalid part index");
                     return;
                  }

                  var8 = var9.getItemContainer();
                  if (var8 == null) {
                     DebugLog.log("ERROR: sendItemsToContainer: part " + var9.getId() + " has no container");
                     return;
                  }
               } else {
                  DebugLog.log("ERROR: sendItemsToContainer: unknown container type");
               }
            }

            if (var8 != null) {
               try {
                  ArrayList var16 = CompressIdenticalItems.load(var2.bb, 186, (ArrayList)null, (ArrayList)null);

                  for(var17 = 0; var17 < var16.size(); ++var17) {
                     InventoryItem var21 = (InventoryItem)var16.get(var17);
                     if (var21 != null) {
                        if (var8.containsID(var21.id)) {
                           if (var3 != 0) {
                              System.out.println("Error: Dupe item ID. id = " + var21.id);
                           }
                        } else {
                           var8.addItem(var21);
                           var8.setExplored(true);
                           if (var8.getParent() instanceof IsoMannequin) {
                              ((IsoMannequin)var8.getParent()).wearItem(var21, (IsoGameCharacter)null);
                           }
                        }
                     }
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }

               if (var9 != null) {
                  var9.setContainerContentAmount(var8.getCapacityWeight());
               }
            }
         }

      }
   }

   private void readItemStats(ByteBuffer var1, InventoryItem var2) {
      int var3 = var1.getInt();
      float var4 = var1.getFloat();
      boolean var5 = var1.get() == 1;
      var2.setUses(var3);
      if (var2 instanceof DrainableComboItem) {
         ((DrainableComboItem)var2).setDelta(var4);
         ((DrainableComboItem)var2).updateWeight();
      }

      if (var5 && var2 instanceof Food) {
         Food var6 = (Food)var2;
         var6.setHungChange(var1.getFloat());
         var6.setCalories(var1.getFloat());
         var6.setCarbohydrates(var1.getFloat());
         var6.setLipids(var1.getFloat());
         var6.setProteins(var1.getFloat());
         var6.setThirstChange(var1.getFloat());
         var6.setFluReduction(var1.getInt());
         var6.setPainReduction(var1.getFloat());
         var6.setEndChange(var1.getFloat());
         var6.setReduceFoodSickness(var1.getInt());
         var6.setStressChange(var1.getFloat());
         var6.setFatigueChange(var1.getFloat());
      }

   }

   static void receiveItemStats(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
      byte var8;
      int var9;
      byte var14;
      int var16;
      ItemContainer var20;
      InventoryItem var22;
      switch(var2) {
      case 0:
         var14 = var0.get();
         var16 = var0.getInt();
         if (var6 != null && var14 >= 0 && var14 < var6.getStaticMovingObjects().size()) {
            IsoMovingObject var18 = (IsoMovingObject)var6.getStaticMovingObjects().get(var14);
            var20 = var18.getContainer();
            if (var20 != null) {
               var22 = var20.getItemWithID(var16);
               if (var22 != null) {
                  instance.readItemStats(var0, var22);
               }
            }
         }
         break;
      case 1:
         int var15 = var0.getInt();
         if (var6 != null) {
            for(var16 = 0; var16 < var6.getWorldObjects().size(); ++var16) {
               IsoWorldInventoryObject var17 = (IsoWorldInventoryObject)var6.getWorldObjects().get(var16);
               if (var17.getItem() != null && var17.getItem().id == var15) {
                  instance.readItemStats(var0, var17.getItem());
                  break;
               }

               if (var17.getItem() instanceof InventoryContainer) {
                  var20 = ((InventoryContainer)var17.getItem()).getInventory();
                  var22 = var20.getItemWithID(var15);
                  if (var22 != null) {
                     instance.readItemStats(var0, var22);
                     break;
                  }
               }
            }
         }
         break;
      case 2:
         var14 = var0.get();
         var8 = var0.get();
         var9 = var0.getInt();
         if (var6 != null && var14 >= 0 && var14 < var6.getObjects().size()) {
            IsoObject var19 = (IsoObject)var6.getObjects().get(var14);
            ItemContainer var21 = var19.getContainerByIndex(var8);
            if (var21 != null) {
               InventoryItem var23 = var21.getItemWithID(var9);
               if (var23 != null) {
                  instance.readItemStats(var0, var23);
               }
            }
         }
         break;
      case 3:
         short var7 = var0.getShort();
         var8 = var0.get();
         var9 = var0.getInt();
         BaseVehicle var10 = VehicleManager.instance.getVehicleByID(var7);
         if (var10 != null) {
            VehiclePart var11 = var10.getPartByIndex(var8);
            if (var11 != null) {
               ItemContainer var12 = var11.getItemContainer();
               if (var12 != null) {
                  InventoryItem var13 = var12.getItemWithID(var9);
                  if (var13 != null) {
                     instance.readItemStats(var0, var13);
                  }
               }
            }
         }
      }

   }

   public static boolean canSeePlayerStats() {
      return !accessLevel.equals("");
   }

   public static boolean canModifyPlayerStats() {
      return accessLevel.equals("admin") || accessLevel.equals("moderator") || accessLevel.equals("overseer");
   }

   public void sendPersonalColor(IsoPlayer var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.ChangeTextColor.doPacket(var2);
      var2.putShort(var1.getOnlineID());
      var2.putFloat(Core.getInstance().getMpTextColor().r);
      var2.putFloat(Core.getInstance().getMpTextColor().g);
      var2.putFloat(Core.getInstance().getMpTextColor().b);
      PacketTypes.PacketType.ChangeTextColor.send(connection);
   }

   public void sendChangedPlayerStats(IsoPlayer var1) {
      ByteBufferWriter var2 = connection.startPacket();
      var1.createPlayerStats(var2, username);
      PacketTypes.PacketType.ChangePlayerStats.send(connection);
   }

   static void receiveChangePlayerStats(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         String var4 = GameWindow.ReadString(var0);
         var3.setPlayerStats(var0, var4);
         allChatMuted = var3.isAllChatMuted();
      }
   }

   public void writePlayerConnectData(ByteBufferWriter var1, IsoPlayer var2) {
      var1.putByte((byte)var2.PlayerIndex);
      var1.putByte((byte)IsoChunkMap.ChunkGridWidth);
      var1.putFloat(var2.x);
      var1.putFloat(var2.y);
      var1.putFloat(var2.z);

      try {
         var2.getDescriptor().save(var1.bb);
         var2.getHumanVisual().save(var1.bb);
         ItemVisuals var3 = new ItemVisuals();
         var2.getItemVisuals(var3);
         var3.save(var1.bb);
         var2.getXp().save(var1.bb);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      var1.putBoolean(var2.isAllChatMuted());
      var1.putUTF(var2.getTagPrefix());
      var1.putFloat(var2.getTagColor().r);
      var1.putFloat(var2.getTagColor().g);
      var1.putFloat(var2.getTagColor().b);
      var1.putInt(var2.getTransactionID());
      var1.putDouble(var2.getHoursSurvived());
      var1.putInt(var2.getZombieKills());
      var1.putUTF(var2.getDisplayName());
      var1.putFloat(var2.getSpeakColour().r);
      var1.putFloat(var2.getSpeakColour().g);
      var1.putFloat(var2.getSpeakColour().b);
      var1.putBoolean(var2.showTag);
      var1.putBoolean(var2.factionPvp);
      if (SteamUtils.isSteamModeEnabled()) {
         var1.putUTF(SteamFriends.GetFriendPersonaName(SteamUser.GetSteamID()));
      }

      InventoryItem var9 = var2.getPrimaryHandItem();
      if (var9 == null) {
         var1.putByte((byte)0);
      } else {
         var1.putByte((byte)1);

         try {
            var9.saveWithSize(var1.bb, false);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      InventoryItem var4 = var2.getSecondaryHandItem();
      if (var4 == null) {
         var1.putByte((byte)0);
      } else if (var4 == var9) {
         var1.putByte((byte)2);
      } else {
         var1.putByte((byte)1);

         try {
            var4.saveWithSize(var1.bb, false);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

      var1.putInt(var2.getAttachedItems().size());

      for(int var5 = 0; var5 < var2.getAttachedItems().size(); ++var5) {
         var1.putUTF(var2.getAttachedItems().get(var5).getLocation());
         var1.putUTF(var2.getAttachedItems().get(var5).getItem().getFullType());
      }

      var1.putInt(var2.getPerkLevel(PerkFactory.Perks.Sneak));
      connection.username = var2.username;
   }

   public void sendPlayerConnect(IsoPlayer var1) {
      var1.setOnlineID((short)-1);
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.PlayerConnect.doPacket(var2);
      this.writePlayerConnectData(var2, var1);
      PacketTypes.PacketType.PlayerConnect.send(connection);
      allChatMuted = var1.isAllChatMuted();
      sendPerks(var1);
      var1.updateEquippedRadioFreq();
      this.bPlayerConnectSent = true;
   }

   public void sendPlayerSave(IsoPlayer var1) {
      if (connection != null) {
         ByteBufferWriter var2 = connection.startPacket();
         PacketTypes.PacketType.PlayerSave.doPacket(var2);
         var2.putByte((byte)var1.PlayerIndex);
         var2.putShort(var1.OnlineID);
         var2.putFloat(var1.x);
         var2.putFloat(var1.y);
         var2.putFloat(var1.z);
         PacketTypes.PacketType.PlayerSave.send(connection);
      }
   }

   public void sendPlayer2(IsoPlayer var1) {
      if (bClient && var1.isLocalPlayer() && var1.networkAI.isNeedToUpdate()) {
         if (PlayerPacket.l_send.playerPacket.set(var1)) {
            ByteBufferWriter var2 = connection.startPacket();
            PacketTypes.PacketType var3;
            if (this.PlayerUpdateReliableLimit.Check()) {
               var3 = PacketTypes.PacketType.PlayerUpdateReliable;
            } else {
               var3 = PacketTypes.PacketType.PlayerUpdate;
            }

            var3.doPacket(var2);
            PlayerPacket.l_send.playerPacket.write(var2);
            var3.send(connection);
         }

      }
   }

   public void sendPlayer(IsoPlayer var1) {
      var1.networkAI.needToUpdate();
   }

   public void sendSteamProfileName(long var1) {
      if (SteamUtils.isSteamModeEnabled()) {
         for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            IsoPlayer var4 = IsoPlayer.players[var3];
            if (var4 != null && var4.getSteamID() == var1) {
               ByteBufferWriter var5 = connection.startPacket();
               PacketTypes.PacketType.SteamGeneric.doPacket(var5);
               var5.putShort((short)0);
               var5.putByte((byte)var4.getPlayerNum());
               var5.putUTF(SteamFriends.GetFriendPersonaName(var1));
               PacketTypes.PacketType.SteamGeneric.send(connection);
               return;
            }
         }

      }
   }

   public void heartBeat() {
      ++count;
   }

   public static IsoZombie getZombie(short var0) {
      return (IsoZombie)IDToZombieMap.get(var0);
   }

   public static void sendPlayerExtraInfo(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.ExtraInfo.doPacket(var1);
      var1.putShort(var0.OnlineID);
      var1.putUTF(var0.accessLevel);
      var1.putByte((byte)(var0.isGodMod() ? 1 : 0));
      var1.putByte((byte)(var0.isGhostMode() ? 1 : 0));
      var1.putByte((byte)(var0.isInvisible() ? 1 : 0));
      var1.putByte((byte)(var0.isNoClip() ? 1 : 0));
      var1.putByte((byte)(var0.isShowAdminTag() ? 1 : 0));
      var1.putByte((byte)(var0.isCanHearAll() ? 1 : 0));
      PacketTypes.PacketType.ExtraInfo.send(connection);
   }

   static void receiveExtraInfo(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      String var3 = GameWindow.ReadString(var0);
      boolean var4 = var0.get() == 1;
      boolean var5 = var0.get() == 1;
      boolean var6 = var0.get() == 1;
      boolean var7 = var0.get() == 1;
      boolean var8 = var0.get() == 1;
      IsoPlayer var9 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var9 != null) {
         var9.accessLevel = var3;
         var9.setGodMod(var4);
         var9.setInvisible(var6);
         var9.setGhostMode(var5);
         var9.setNoClip(var7);
         var9.setShowAdminTag(var8);
         if (!var9.bRemote) {
            accessLevel = var3;

            for(int var10 = 0; var10 < IsoPlayer.numPlayers; ++var10) {
               IsoPlayer var11 = IsoPlayer.players[var10];
               if (var11 != null && !var11.accessLevel.equals("")) {
                  accessLevel = var11.accessLevel;
                  break;
               }
            }
         }
      }

   }

   static void receiveConnectionDetails(ByteBuffer var0, short var1) {
      Calendar var2 = Calendar.getInstance();
      PrintStream var10000 = System.out;
      long var10001 = var2.getTimeInMillis();
      var10000.println("LOGGED INTO : " + (var10001 - startAuth.getTimeInMillis()) + " millisecond");
      ConnectToServerState var3 = new ConnectToServerState(var0);
      var3.enter();
      MainScreenState.getInstance().setConnectToServerState(var3);
   }

   public void setResetID(int var1) {
      this.loadResetID();
      if (this.ResetID != var1) {
         boolean var2 = true;
         ArrayList var3 = IsoPlayer.getAllFileNames();
         var3.add("map_p.bin");
         String var10002;
         int var4;
         File var5;
         File var6;
         if (var2) {
            for(var4 = 0; var4 < var3.size(); ++var4) {
               try {
                  var5 = ZomboidFileSystem.instance.getFileInCurrentSave((String)var3.get(var4));
                  if (var5.exists()) {
                     var10002 = ZomboidFileSystem.instance.getCacheDir();
                     var6 = new File(var10002 + File.separator + (String)var3.get(var4));
                     if (var6.exists()) {
                        var6.delete();
                     }

                     var5.renameTo(var6);
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }

         DebugLog.log("server was reset, deleting " + Core.GameSaveWorld);
         LuaManager.GlobalObject.deleteSave(Core.GameSaveWorld);
         LuaManager.GlobalObject.createWorld(Core.GameSaveWorld);
         if (var2) {
            for(var4 = 0; var4 < var3.size(); ++var4) {
               try {
                  var5 = ZomboidFileSystem.instance.getFileInCurrentSave((String)var3.get(var4));
                  var10002 = ZomboidFileSystem.instance.getCacheDir();
                  var6 = new File(var10002 + File.separator + (String)var3.get(var4));
                  if (var6 != null) {
                     var6.renameTo(var5);
                  }
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }
         }
      }

      this.ResetID = var1;
      this.saveResetID();
   }

   public void loadResetID() {
      File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("serverid.dat");
      if (var1.exists()) {
         FileInputStream var2 = null;

         try {
            var2 = new FileInputStream(var1);
         } catch (FileNotFoundException var7) {
            var7.printStackTrace();
         }

         DataInputStream var3 = new DataInputStream(var2);

         try {
            this.ResetID = var3.readInt();
         } catch (IOException var6) {
            var6.printStackTrace();
         }

         try {
            var2.close();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

   }

   private void saveResetID() {
      File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("serverid.dat");
      FileOutputStream var2 = null;

      try {
         var2 = new FileOutputStream(var1);
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
      }

      DataOutputStream var3 = new DataOutputStream(var2);

      try {
         var3.writeInt(this.ResetID);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      try {
         var2.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   static void receivePlayerConnect(ByteBuffer var0, short var1) {
      boolean var2 = false;
      short var3 = var0.getShort();
      byte var4 = -1;
      if (var3 == -1) {
         var2 = true;
         var4 = var0.get();
         var3 = var0.getShort();

         try {
            GameTime.getInstance().load(var0);
            GameTime.getInstance().ServerTimeOfDay = GameTime.getInstance().getTimeOfDay();
            GameTime.getInstance().ServerNewDays = 0;
            GameTime.getInstance().setMinutesPerDay((float)SandboxOptions.instance.getDayLengthMinutes());
            LuaEventManager.triggerEvent("OnGameTimeLoaded");
         } catch (IOException var17) {
            var17.printStackTrace();
         }
      } else if (IDToPlayerMap.containsKey(var3)) {
         return;
      }

      float var5 = var0.getFloat();
      float var6 = var0.getFloat();
      float var7 = var0.getFloat();
      IsoPlayer var8 = null;
      String var9;
      int var10;
      if (var2) {
         var9 = GameWindow.ReadString(var0);

         for(var10 = 0; var10 < IsoWorld.instance.AddCoopPlayers.size(); ++var10) {
            ((AddCoopPlayer)IsoWorld.instance.AddCoopPlayers.get(var10)).receivePlayerConnect(var4);
         }

         var8 = IsoPlayer.players[var4];
         var8.username = var9;
         var8.setOnlineID(var3);
      } else {
         var9 = GameWindow.ReadString(var0);
         SurvivorDesc var19 = SurvivorFactory.CreateSurvivor();

         try {
            var19.load(var0, 186, (IsoGameCharacter)null);
         } catch (IOException var16) {
            var16.printStackTrace();
         }

         try {
            var8 = new IsoPlayer(IsoWorld.instance.CurrentCell, var19, (int)var5, (int)var6, (int)var7);
            var8.bRemote = true;
            var8.getHumanVisual().load(var0, 186);
            var8.getItemVisuals().load(var0, 186);
            var8.username = var9;
            var8.updateUsername();
            var8.setSceneCulled(false);
         } catch (Exception var15) {
            var15.printStackTrace();
         }

         var8.setX(var5);
         var8.setY(var6);
         var8.setZ(var7);
         var8.networkAI.targetX = var5;
         var8.networkAI.targetY = var6;
         var8.networkAI.targetZ = (int)var7;
      }

      var8.setOnlineID(var3);
      if (SteamUtils.isSteamModeEnabled()) {
         var8.setSteamID(var0.getLong());
      }

      var8.setGodMod(var0.get() == 1);
      var8.setGhostMode(var0.get() == 1);
      var8.setSafety(var0.get() == 1);
      var8.accessLevel = GameWindow.ReadString(var0);
      var8.setInvisible(var0.get() == 1);
      if (!var2) {
         try {
            var8.getXp().load(var0, 186);
         } catch (IOException var14) {
            var14.printStackTrace();
         }
      }

      var8.setTagPrefix(GameWindow.ReadString(var0));
      var8.setTagColor(new ColorInfo(var0.getFloat(), var0.getFloat(), var0.getFloat(), 1.0F));
      var8.setHoursSurvived(var0.getDouble());
      var8.setZombieKills(var0.getInt());
      var8.setDisplayName(GameWindow.ReadString(var0));
      var8.setSpeakColour(new Color(var0.getFloat(), var0.getFloat(), var0.getFloat(), 1.0F));
      var8.showTag = var0.get() == 1;
      var8.factionPvp = var0.get() == 1;
      int var18 = var0.getInt();

      for(var10 = 0; var10 < var18; ++var10) {
         String var11 = GameWindow.ReadString(var0);
         InventoryItem var12 = InventoryItemFactory.CreateItem(GameWindow.ReadString(var0));
         if (var12 != null) {
            var8.setAttachedItem(var11, var12);
         }
      }

      var10 = var0.getInt();
      int var20 = var0.getInt();
      int var21 = var0.getInt();
      var8.remoteSneakLvl = var10;
      var8.remoteStrLvl = var20;
      var8.remoteFitLvl = var21;
      if (Core.bDebug) {
         DebugLog.log(DebugType.Network, "Player Connect received for player " + username + " id " + var3 + (var2 ? " (local)" : " (remote)"));
      }

      if (DebugOptions.instance.MultiplayerShowPosition.getValue() && !var2) {
         if (positions.containsKey(var8.getOnlineID())) {
            ((Vector2)positions.get(var8.getOnlineID())).set(var5, var6);
         } else {
            positions.put(var8.getOnlineID(), new Vector2(var5, var6));
         }
      }

      IDToPlayerMap.put(var3, var8);
      instance.idMapDirty = true;
      LuaEventManager.triggerEvent("OnMiniScoreboardUpdate");
      if (var2) {
         getCustomModData();
      }

      if (!var2 && ServerOptions.instance.DisableSafehouseWhenPlayerConnected.getValue()) {
         SafeHouse var13 = SafeHouse.hasSafehouse(var8);
         if (var13 != null) {
            var13.setPlayerConnected(var13.getPlayerConnected() + 1);
         }
      }

      String var22 = ServerOptions.getInstance().getOption("ServerWelcomeMessage");
      if (var2 && var22 != null && !var22.equals("")) {
         ChatManager.getInstance().showServerChatMessage(var22);
      }

   }

   static void receiveScoreboardUpdate(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      instance.connectedPlayers = new ArrayList();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for(int var6 = 0; var6 < var2; ++var6) {
         String var7 = GameWindow.ReadString(var0);
         String var8 = GameWindow.ReadString(var0);
         var3.add(var7);
         var4.add(var8);
         instance.connectedPlayers.add(instance.getPlayerFromUsername(var7));
         if (SteamUtils.isSteamModeEnabled()) {
            String var9 = SteamUtils.convertSteamIDToString(var0.getLong());
            var5.add(var9);
         }
      }

      LuaEventManager.triggerEvent("OnScoreboardUpdate", var3, var4, var5);
   }

   public boolean receivePlayerConnectWhileLoading(ByteBuffer var1) {
      boolean var2 = false;
      short var3 = var1.getShort();
      byte var4 = -1;
      if (var3 != -1) {
         return false;
      } else {
         if (var3 == -1) {
            var2 = true;
            var4 = var1.get();
            var3 = var1.getShort();

            try {
               GameTime.getInstance().load(var1);
               LuaEventManager.triggerEvent("OnGameTimeLoaded");
            } catch (IOException var14) {
               var14.printStackTrace();
            }
         }

         float var5 = var1.getFloat();
         float var6 = var1.getFloat();
         float var7 = var1.getFloat();
         IsoPlayer var8 = null;
         String var9;
         if (var2) {
            var9 = GameWindow.ReadString(var1);
            var8 = IsoPlayer.players[var4];
            var8.username = var9;
            var8.setOnlineID(var3);
         } else {
            var9 = GameWindow.ReadString(var1);
            SurvivorDesc var10 = SurvivorFactory.CreateSurvivor();

            try {
               var10.load(var1, 186, (IsoGameCharacter)null);
            } catch (IOException var13) {
               var13.printStackTrace();
            }

            try {
               var8 = new IsoPlayer(IsoWorld.instance.CurrentCell, var10, (int)var5, (int)var6, (int)var7);
               var8.getHumanVisual().load(var1, 186);
               var8.getItemVisuals().load(var1, 186);
               var8.username = var9;
               var8.updateUsername();
               var8.setSceneCulled(false);
            } catch (Exception var12) {
               var12.printStackTrace();
            }

            var8.bRemote = true;
            var8.setX(var5);
            var8.setY(var6);
            var8.setZ(var7);
         }

         var8.setOnlineID(var3);
         if (Core.bDebug) {
            DebugLog.log(DebugType.Network, "Player Connect received for player " + username + " id " + var3 + (var2 ? " (me)" : " (not me)"));
         }

         int var15 = var1.getInt();

         for(int var16 = 0; var16 < var15; ++var16) {
            ServerOptions.instance.putOption(GameWindow.ReadString(var1), GameWindow.ReadString(var1));
         }

         var8.setGodMod(var1.get() == 1);
         var8.setGhostMode(var1.get() == 1);
         var8.setSafety(var1.get() == 1);
         var8.accessLevel = GameWindow.ReadString(var1);
         var8.setInvisible(var1.get() == 1);
         IDToPlayerMap.put(var3, var8);
         this.idMapDirty = true;
         getCustomModData();
         String var17 = ServerOptions.getInstance().getOption("ServerWelcomeMessage");
         if (var2 && var17 != null && !var17.equals("")) {
            ChatManager.getInstance().showServerChatMessage(var17);
         }

         return true;
      }
   }

   public ArrayList getPlayers() {
      if (!this.idMapDirty) {
         return this.players;
      } else {
         this.players.clear();
         this.players.addAll(IDToPlayerMap.values());
         this.idMapDirty = false;
         return this.players;
      }
   }

   private IsoObject getIsoObjectRefFromByteBuffer(ByteBuffer var1) {
      int var2 = var1.getInt();
      int var3 = var1.getInt();
      int var4 = var1.getInt();
      byte var5 = var1.get();
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var6 == null) {
         this.delayPacket(var2, var3, var4);
         return null;
      } else {
         return var5 >= 0 && var5 < var6.getObjects().size() ? (IsoObject)var6.getObjects().get(var5) : null;
      }
   }

   public void sendWeaponHit(IsoPlayer var1, HandWeapon var2, IsoObject var3) {
      if (var1 != null && var3 != null && var1.isLocalPlayer()) {
         ByteBufferWriter var4 = connection.startPacket();
         PacketTypes.PacketType.WeaponHit.doPacket(var4);
         var4.putInt(var3.square.x);
         var4.putInt(var3.square.y);
         var4.putInt(var3.square.z);
         var4.putByte((byte)var3.getObjectIndex());
         var4.putShort(var1.OnlineID);
         var4.putUTF(var2 != null ? var2.getFullType() : "");
         PacketTypes.PacketType.WeaponHit.send(connection);
      }
   }

   public static void SyncCustomLightSwitchSettings(ByteBuffer var0) {
      int var1 = var0.getInt();
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      byte var4 = var0.get();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
      if (var5 != null && var4 >= 0 && var4 < var5.getObjects().size()) {
         if (var5.getObjects().get(var4) instanceof IsoLightSwitch) {
            ((IsoLightSwitch)var5.getObjects().get(var4)).receiveSyncCustomizedSettings(var0, (UdpConnection)null);
         } else {
            DebugLog.log("Sync Lightswitch custom settings: found object not a instance of IsoLightSwitch, x,y,z=" + var1 + "," + var2 + "," + var3);
         }
      } else if (var5 != null) {
         DebugLog.log("Sync Lightswitch custom settings: index=" + var4 + " is invalid x,y,z=" + var1 + "," + var2 + "," + var3);
      } else if (Core.bDebug) {
         DebugLog.log("Sync Lightswitch custom settings: sq is null x,y,z=" + var1 + "," + var2 + "," + var3);
      }

   }

   static void receiveSyncIsoObjectReq(ByteBuffer var0, short var1) {
      if (SystemDisabler.doObjectStateSyncEnable) {
         short var2 = var0.getShort();

         for(int var3 = 0; var3 < var2; ++var3) {
            GameClient var10000 = instance;
            receiveSyncIsoObject(var0, var1);
         }

      }
   }

   static void receiveSyncWorldObjectsReq(ByteBuffer var0, short var1) {
      DebugLog.log("SyncWorldObjectsReq client : ");
      short var2 = var0.getShort();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         instance.worldObjectsSyncReq.receiveSyncIsoChunk(var4, var5);
         short var6 = var0.getShort();
         DebugLog.log("[" + var4 + "," + var5 + "]:" + var6 + " ");
         IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var4 * 10, var5 * 10, 0);
         if (var7 == null) {
            return;
         }

         IsoChunk var8 = var7.getChunk();
         ++var8.ObjectsSyncCount;
         var8.recalcHashCodeObjects();
      }

      DebugLog.log(";\n");
   }

   static void receiveSyncObjects(ByteBuffer var0, short var1) {
      if (SystemDisabler.doWorldSyncEnable) {
         short var2 = var0.getShort();
         if (var2 == 2) {
            instance.worldObjectsSyncReq.receiveGridSquareHashes(var0);
         }

         if (var2 == 4) {
            instance.worldObjectsSyncReq.receiveGridSquareObjectHashes(var0);
         }

         if (var2 == 6) {
            instance.worldObjectsSyncReq.receiveObject(var0);
         }

      }
   }

   static void receiveSyncIsoObject(ByteBuffer var0, short var1) {
      if (DebugOptions.instance.Network.Client.SyncIsoObject.getValue()) {
         int var2 = var0.getInt();
         int var3 = var0.getInt();
         int var4 = var0.getInt();
         byte var5 = var0.get();
         byte var6 = var0.get();
         byte var7 = var0.get();
         if (var6 != 2) {
            instance.objectSyncReq.receiveIsoSync(var2, var3, var4, var5);
         }

         if (var6 == 1) {
            IsoGridSquare var8 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
            if (var8 == null) {
               return;
            }

            if (var5 >= 0 && var5 < var8.getObjects().size()) {
               ((IsoObject)var8.getObjects().get(var5)).syncIsoObject(true, var7, (UdpConnection)null, var0);
            } else {
               DebugLog.Network.warn("SyncIsoObject: index=" + var5 + " is invalid x,y,z=" + var2 + "," + var3 + "," + var4);
            }
         }

      }
   }

   static void receiveSyncAlarmClock(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var8;
      if (var2 == AlarmClock.PacketPlayer) {
         short var15 = var0.getShort();
         long var16 = var0.getLong();
         boolean var17 = var0.get() == 1;
         int var18 = var17 ? 0 : var0.getInt();
         var8 = var17 ? 0 : var0.getInt();
         byte var19 = var17 ? 0 : var0.get();
         IsoPlayer var20 = (IsoPlayer)IDToPlayerMap.get(var15);
         if (var20 != null) {
            for(int var21 = 0; var21 < var20.getInventory().getItems().size(); ++var21) {
               InventoryItem var22 = (InventoryItem)var20.getInventory().getItems().get(var21);
               if (var22 instanceof AlarmClock && (long)var22.getID() == var16) {
                  if (var17) {
                     ((AlarmClock)var22).stopRinging();
                  } else {
                     ((AlarmClock)var22).setAlarmSet(var19 == 1);
                     ((AlarmClock)var22).setHour(var18);
                     ((AlarmClock)var22).setMinute(var8);
                  }
                  break;
               }
            }

         }
      } else if (var2 == AlarmClock.PacketWorld) {
         int var3 = var0.getInt();
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         int var6 = var0.getInt();
         boolean var7 = var0.get() == 1;
         var8 = var7 ? 0 : var0.getInt();
         int var9 = var7 ? 0 : var0.getInt();
         byte var10 = var7 ? 0 : var0.get();
         IsoGridSquare var11 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
         if (var11 != null) {
            for(int var12 = 0; var12 < var11.getWorldObjects().size(); ++var12) {
               IsoWorldInventoryObject var13 = (IsoWorldInventoryObject)var11.getWorldObjects().get(var12);
               if (var13 != null && var13.getItem() instanceof AlarmClock && var13.getItem().id == var6) {
                  AlarmClock var14 = (AlarmClock)var13.getItem();
                  if (var7) {
                     var14.stopRinging();
                  } else {
                     var14.setAlarmSet(var10 == 1);
                     var14.setHour(var8);
                     var14.setMinute(var9);
                  }
                  break;
               }
            }

         }
      }
   }

   static void receiveAddItemToMap(ByteBuffer var0, short var1) {
      if (IsoWorld.instance.CurrentCell != null) {
         IsoObject var2 = WorldItemTypes.createFromBuffer(var0);
         var2.loadFromRemoteBuffer(var0);
         if (var2.square != null) {
            if (var2 instanceof IsoLightSwitch) {
               ((IsoLightSwitch)var2).addLightSourceFromSprite();
            }

            var2.addToWorld();
            IsoWorld.instance.CurrentCell.checkHaveRoof(var2.square.getX(), var2.square.getY());
            if (!(var2 instanceof IsoWorldInventoryObject)) {
               for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
                  LosUtil.cachecleared[var3] = true;
               }

               IsoGridSquare.setRecalcLightTime(-1);
               GameTime.instance.lightSourceUpdate = 100.0F;
               MapCollisionData.instance.squareChanged(var2.square);
               PolygonalMap2.instance.squareChanged(var2.square);
               if (var2 == var2.square.getPlayerBuiltFloor()) {
                  IsoGridOcclusionData.SquareChanged();
               }
            }

            if (var2 instanceof IsoWorldInventoryObject || var2.getContainer() != null) {
               LuaEventManager.triggerEvent("OnContainerUpdate", var2);
            }
         }

      }
   }

   static void skipPacket(ByteBuffer var0, short var1) {
   }

   static void receiveAccessDenied(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String[] var3 = var2.split("##");
      LuaEventManager.triggerEvent("OnConnectFailed", var3.length > 0 ? Translator.getText("UI_OnConnectFailed_" + var3[0], var3.length > 1 ? var3[1] : null, var3.length > 2 ? var3[2] : null, var3.length > 3 ? var3[3] : null) : null);
   }

   static void receivePlayerTimeout(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      if (DebugOptions.instance.MultiplayerShowPosition.getValue()) {
         positions.remove(var2);
      }

      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null) {
         DebugLog.log("Received timeout for player " + var3.username + " id " + var3.OnlineID);
         NetworkZombieSimulator.getInstance().clearTargetAuth(var3);
         if (var3.getVehicle() != null) {
            int var4 = var3.getVehicle().getSeat(var3);
            if (var4 != -1) {
               var3.getVehicle().clearPassenger(var4);
            }

            VehicleManager.instance.sendReqestGetPosition(var3.getVehicle().VehicleID);
         }

         var3.removeFromWorld();
         var3.removeFromSquare();
         IDToPlayerMap.remove(var3.OnlineID);
         instance.idMapDirty = true;
         LuaEventManager.triggerEvent("OnMiniScoreboardUpdate");
      }
   }

   public void disconnect() {
      this.bConnected = false;
      if (IsoPlayer.getInstance() != null) {
         IsoPlayer.getInstance().setOnlineID((short)-1);
      }

   }

   public void addIncoming(short var1, ByteBuffer var2) {
      if (connection != null) {
         if (var1 == PacketTypes.PacketType.SentChunk.getId()) {
            WorldStreamer.instance.receiveChunkPart(var2);
         } else if (var1 == PacketTypes.PacketType.NotRequiredInZip.getId()) {
            WorldStreamer.instance.receiveNotRequired(var2);
         } else if (var1 == PacketTypes.PacketType.LoadPlayerProfile.getId()) {
            ClientPlayerDB.getInstance().clientLoadNetworkCharacter(var2, connection);
         } else {
            ZomboidNetData var3 = null;
            if (var2.remaining() > 2048) {
               var3 = ZomboidNetDataPool.instance.getLong(var2.remaining());
            } else {
               var3 = ZomboidNetDataPool.instance.get();
            }

            var3.read(var1, var2, connection);
            var3.time = System.currentTimeMillis();
            MainLoopNetDataQ.add(var3);
         }
      }
   }

   public void doDisconnect(String var1) {
      if (this.bConnected && connection != null) {
         connection.forceDisconnect();
         this.bConnected = false;
         connection = null;
         bClient = false;
      }

   }

   public void removeZombieFromCache(IsoZombie var1) {
      if (IDToZombieMap.containsKey(var1.OnlineID)) {
         IDToZombieMap.remove(var1.OnlineID);
      }

   }

   static void receiveEquip(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      byte var3 = var0.get();
      byte var4 = var0.get();
      IsoPlayer var5 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var5 != IsoPlayer.getInstance()) {
         InventoryItem var6 = null;
         if (var4 == 1) {
            try {
               var6 = InventoryItem.loadItem(var0, 186);
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }

         if (var5 != null && var3 == 1 && var4 == 2) {
            var6 = var5.getPrimaryHandItem();
         }

         if (var5 != null) {
            if (var3 == 0) {
               var5.setPrimaryHandItem(var6);
            } else {
               var5.setSecondaryHandItem(var6);
            }

            try {
               if (var6 != null) {
                  var6.setContainer(var5.getInventory());
                  if (var4 == 1 && var0.get() == 1) {
                     var6.getVisual().load(var0, 186);
                  }
               }
            } catch (IOException var8) {
               var8.printStackTrace();
            }
         }

      }
   }

   public void equip(IsoPlayer var1, int var2) {
      InventoryItem var3 = null;
      if (var2 == 0) {
         var3 = var1.getPrimaryHandItem();
      } else {
         var3 = var1.getSecondaryHandItem();
      }

      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.Equip.doPacket(var4);
      var4.putByte((byte)var1.PlayerIndex);
      var4.putByte((byte)var2);
      if (var3 == null) {
         var4.putByte((byte)0);
      } else if (var2 == 1 && var1.getPrimaryHandItem() == var1.getSecondaryHandItem()) {
         var4.putByte((byte)2);
      } else {
         var4.putByte((byte)1);

         try {
            var3.saveWithSize(var4.bb, false);
            if (var3.getVisual() != null) {
               var4.bb.put((byte)1);
               var3.getVisual().save(var4.bb);
            } else {
               var4.bb.put((byte)0);
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

      PacketTypes.PacketType.Equip.send(connection);
   }

   public void sendWorldMessage(String var1) {
      ChatManager.getInstance().showInfoMessage(var1);
   }

   private void convertGameSaveWorldDirectory(String var1, String var2) {
      File var3 = new File(var1);
      if (var3.isDirectory()) {
         File var4 = new File(var2);
         boolean var5 = var3.renameTo(var4);
         if (var5) {
            DebugLog.log("CONVERT: The GameSaveWorld directory was renamed from " + var1 + " to " + var2);
         } else {
            DebugLog.log("ERROR: The GameSaveWorld directory cannot rename from " + var1 + " to " + var2);
         }

      }
   }

   public void doConnect(String var1, String var2, String var3, String var4, String var5, String var6) {
      username = var1.trim();
      password = var2.trim();
      ip = var3.trim();
      localIP = var4.trim();
      port = Integer.parseInt(var5.trim());
      serverPassword = var6.trim();
      instance.init();
      String var10000 = ip;
      Core.GameSaveWorld = var10000 + "_" + port + "_" + ServerWorldDatabase.encrypt(var1);
      String var10001 = ZomboidFileSystem.instance.getGameModeCacheDir() + File.separator + ip + "_" + port + "_" + var1;
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      this.convertGameSaveWorldDirectory(var10001, var10002 + File.separator + Core.GameSaveWorld);
      if (CoopMaster.instance != null && CoopMaster.instance.isRunning()) {
         Core.GameSaveWorld = CoopMaster.instance.getPlayerSaveFolder(CoopMaster.instance.getServerName());
      }

   }

   public void doConnectCoop(String var1) {
      username = SteamFriends.GetPersonaName();
      password = "";
      ip = var1;
      localIP = "";
      port = 0;
      serverPassword = "";
      this.init();
      if (CoopMaster.instance != null && CoopMaster.instance.isRunning()) {
         Core.GameSaveWorld = CoopMaster.instance.getPlayerSaveFolder(CoopMaster.instance.getServerName());
      }

   }

   public void scoreboardUpdate() {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.ScoreboardUpdate.doPacket(var1);
      PacketTypes.PacketType.ScoreboardUpdate.send(connection);
   }

   public void sendWorldSound(Object var1, int var2, int var3, int var4, int var5, int var6, boolean var7, float var8, float var9) {
      ByteBufferWriter var10 = connection.startPacket();
      PacketTypes.PacketType.WorldSound.doPacket(var10);
      var10.putInt(var2);
      var10.putInt(var3);
      var10.putInt(var4);
      var10.putInt(var5);
      var10.putInt(var6);
      var10.putByte((byte)(var7 ? 1 : 0));
      var10.putFloat(var8);
      var10.putFloat(var9);
      var10.putByte((byte)(var1 instanceof IsoZombie ? 1 : 0));
      PacketTypes.PacketType.WorldSound.send(connection);
   }

   static void receivePlayWorldSound(ByteBuffer var0, short var1) {
      PlayWorldSoundPacket var2 = new PlayWorldSoundPacket();
      var2.parse(var0);
      var2.process();
      DebugLog.log(DebugType.Sound, var2.getDescription());
   }

   static void receivePlaySound(ByteBuffer var0, short var1) {
      PlaySoundPacket var2 = new PlaySoundPacket();
      var2.parse(var0);
      var2.process();
      DebugLog.log(DebugType.Sound, var2.getDescription());
   }

   static void receiveStopSound(ByteBuffer var0, short var1) {
      StopSoundPacket var2 = new StopSoundPacket();
      var2.parse(var0);
      var2.process();
      DebugLog.log(DebugType.Sound, var2.getDescription());
   }

   static void receiveWorldSound(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      int var6 = var0.getInt();
      boolean var7 = var0.get() == 1;
      float var8 = var0.getFloat();
      float var9 = var0.getFloat();
      boolean var10 = var0.get() == 1;
      WorldSoundManager.instance.addSound((Object)null, var2, var3, var4, var5, var6, var7, var8, var9, var10, false, true);
   }

   private void receiveDataZombieDescriptors(ByteBuffer var1) throws IOException {
      DebugLog.log(DebugType.NetworkFileDebug, "received zombie descriptors");
      PersistentOutfits.instance.load(var1);
   }

   private void receivePlayerZombieDescriptors(ByteBuffer var1) throws IOException {
      short var2 = var1.getShort();
      DebugLog.log(DebugType.NetworkFileDebug, "received " + var2 + " player-zombie descriptors");

      for(short var3 = 0; var3 < var2; ++var3) {
         SharedDescriptors.Descriptor var4 = new SharedDescriptors.Descriptor();
         var4.load(var1, 186);
         SharedDescriptors.registerPlayerZombieDescriptor(var4);
      }

   }

   static void receiveAddAmbient(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      float var6 = var0.getFloat();
      DebugLog.log(DebugType.Sound, "ambient: received " + var2 + " at " + var3 + "," + var4 + " radius=" + var5);
      AmbientStreamManager.instance.addAmbient(var2, var3, var4, var5, var6);
   }

   public void sendClientCommand(IsoPlayer var1, String var2, String var3, KahluaTable var4) {
      ByteBufferWriter var5 = connection.startPacket();
      PacketTypes.PacketType.ClientCommand.doPacket(var5);
      var5.putByte((byte)(var1 != null ? var1.PlayerIndex : -1));
      var5.putUTF(var2);
      var5.putUTF(var3);
      if (var4 != null && !var4.isEmpty()) {
         var5.putByte((byte)1);

         try {
            KahluaTableIterator var6 = var4.iterator();

            while(var6.advance()) {
               if (!TableNetworkUtils.canSave(var6.getKey(), var6.getValue())) {
                  Object var10000 = var6.getKey();
                  DebugLog.log("ERROR: sendClientCommand: can't save key,value=" + var10000 + "," + var6.getValue());
               }
            }

            TableNetworkUtils.save(var4, var5.bb);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      } else {
         var5.putByte((byte)0);
      }

      PacketTypes.PacketType.ClientCommand.send(connection);
   }

   public void sendClientCommandV(IsoPlayer var1, String var2, String var3, Object... var4) {
      if (var4.length == 0) {
         this.sendClientCommand(var1, var2, var3, (KahluaTable)null);
      } else if (var4.length % 2 != 0) {
         DebugLog.log("ERROR: sendClientCommand called with wrong number of arguments (" + var2 + " " + var3 + ")");
      } else {
         KahluaTable var5 = LuaManager.platform.newTable();

         for(int var6 = 0; var6 < var4.length; var6 += 2) {
            Object var7 = var4[var6 + 1];
            if (var7 instanceof Float) {
               var5.rawset(var4[var6], ((Float)var7).doubleValue());
            } else if (var7 instanceof Integer) {
               var5.rawset(var4[var6], ((Integer)var7).doubleValue());
            } else if (var7 instanceof Short) {
               var5.rawset(var4[var6], ((Short)var7).doubleValue());
            } else {
               var5.rawset(var4[var6], var7);
            }
         }

         this.sendClientCommand(var1, var2, var3, var5);
      }
   }

   public void sendClothing(IsoPlayer var1, String var2, InventoryItem var3) {
      if (var1 != null && var1.OnlineID != -1) {
         SyncClothingPacket var4 = new SyncClothingPacket();
         var4.set(var1, var2, var3);
         ByteBufferWriter var5 = connection.startPacket();
         PacketTypes.PacketType.SyncClothing.doPacket(var5);
         var4.write(var5);
         PacketTypes.PacketType.SyncClothing.send(connection);
      }
   }

   static void receiveSyncClothing(ByteBuffer var0, short var1) {
      SyncClothingPacket var2 = new SyncClothingPacket();
      var2.parse(var0);
   }

   public void sendAttachedItem(IsoPlayer var1, String var2, InventoryItem var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.PlayerAttachedItem.doPacket(var4);

      try {
         var4.putByte((byte)var1.PlayerIndex);
         GameWindow.WriteString(var4.bb, var2);
         if (var3 != null) {
            var4.putByte((byte)1);
            GameWindow.WriteString(var4.bb, var3.getFullType());
         } else {
            var4.putByte((byte)0);
         }

         PacketTypes.PacketType.PlayerAttachedItem.send(connection);
      } catch (Throwable var6) {
         connection.cancelPacket();
         ExceptionLogger.logException(var6);
      }

   }

   static void receivePlayerAttachedItem(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null && !var3.isLocalPlayer()) {
         String var4 = GameWindow.ReadString(var0);
         boolean var5 = var0.get() == 1;
         if (var5) {
            String var6 = GameWindow.ReadString(var0);
            InventoryItem var7 = InventoryItemFactory.CreateItem(var6);
            if (var7 == null) {
               return;
            }

            var3.setAttachedItem(var4, var7);
         } else {
            var3.setAttachedItem(var4, (InventoryItem)null);
         }

      }
   }

   public void sendVisual(IsoPlayer var1) {
      if (var1 != null && var1.OnlineID != -1) {
         ByteBufferWriter var2 = connection.startPacket();
         PacketTypes.PacketType.HumanVisual.doPacket(var2);

         try {
            var2.putByte((byte)var1.PlayerIndex);
            var1.getHumanVisual().save(var2.bb);
            PacketTypes.PacketType.HumanVisual.send(connection);
         } catch (Throwable var4) {
            connection.cancelPacket();
            ExceptionLogger.logException(var4);
         }

      }
   }

   static void receiveHumanVisual(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoPlayer var3 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var3 != null && !var3.isLocalPlayer()) {
         try {
            var3.getHumanVisual().load(var0, 186);
            var3.resetModelNextFrame();
         } catch (Throwable var5) {
            ExceptionLogger.logException(var5);
         }

      }
   }

   static void receiveBloodSplatter(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      float var3 = var0.getFloat();
      float var4 = var0.getFloat();
      float var5 = var0.getFloat();
      float var6 = var0.getFloat();
      float var7 = var0.getFloat();
      boolean var8 = var0.get() == 1;
      boolean var9 = var0.get() == 1;
      byte var10 = var0.get();
      IsoCell var11 = IsoWorld.instance.CurrentCell;
      IsoGridSquare var12 = var11.getGridSquare((double)var3, (double)var4, (double)var5);
      if (var12 == null) {
         instance.delayPacket((int)var3, (int)var4, (int)var5);
      } else {
         int var13;
         if (var9 && SandboxOptions.instance.BloodLevel.getValue() > 1) {
            for(var13 = -1; var13 <= 1; ++var13) {
               for(int var18 = -1; var18 <= 1; ++var18) {
                  if (var13 != 0 || var18 != 0) {
                     new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, (float)var13 * Rand.Next(0.25F, 0.5F), (float)var18 * Rand.Next(0.25F, 0.5F));
                  }
               }
            }

            new IsoZombieGiblets(IsoZombieGiblets.GibletType.Eye, var11, var3, var4, var5, var6 * 0.8F, var7 * 0.8F);
         } else {
            if (SandboxOptions.instance.BloodLevel.getValue() > 1) {
               for(var13 = 0; var13 < var10; ++var13) {
                  var12.splatBlood(3, 0.3F);
               }

               var12.getChunk().addBloodSplat(var3, var4, (float)((int)var5), Rand.Next(20));
               new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.5F, var7 * 1.5F);
            }

            byte var17 = 3;
            byte var14 = 0;
            byte var15 = 1;
            switch(SandboxOptions.instance.BloodLevel.getValue()) {
            case 1:
               var15 = 0;
               break;
            case 2:
               var15 = 1;
               var17 = 5;
               var14 = 2;
            case 3:
            default:
               break;
            case 4:
               var15 = 3;
               var17 = 2;
               break;
            case 5:
               var15 = 10;
               var17 = 0;
            }

            for(int var16 = 0; var16 < var15; ++var16) {
               if (Rand.Next(var8 ? 8 : var17) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.5F, var7 * 1.5F);
               }

               if (Rand.Next(var8 ? 8 : var17) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.5F, var7 * 1.5F);
               }

               if (Rand.Next(var8 ? 8 : var17) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.8F, var7 * 1.8F);
               }

               if (Rand.Next(var8 ? 8 : var17) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.9F, var7 * 1.9F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 3.5F, var7 * 3.5F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 3.8F, var7 * 3.8F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 3.9F, var7 * 3.9F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 1.5F, var7 * 1.5F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 3.8F, var7 * 3.8F);
               }

               if (Rand.Next(var8 ? 4 : var14) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var11, var3, var4, var5, var6 * 3.9F, var7 * 3.9F);
               }

               if (Rand.Next(var8 ? 9 : 6) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.Eye, var11, var3, var4, var5, var6 * 0.8F, var7 * 0.8F);
               }
            }

         }
      }
   }

   static void receiveZombieSound(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      byte var3 = var0.get();
      IsoZombie.ZombieSound var4 = IsoZombie.ZombieSound.fromIndex(var3);
      DebugLog.log(DebugType.Sound, "sound: received " + var3 + " for zombie " + var2);
      IsoZombie var5 = (IsoZombie)IDToZombieMap.get(var2);
      if (var5 != null && var5.getCurrentSquare() != null) {
         float var6 = (float)var4.radius();
         String var7;
         switch(var4) {
         case Burned:
            var7 = var5.isFemale() ? "FemaleZombieDeath" : "MaleZombieDeath";
            var5.getEmitter().playVocals(var7);
            break;
         case DeadCloseKilled:
            var5.getEmitter().playSoundImpl("HeadStab", (IsoObject)null);
            var7 = var5.isFemale() ? "FemaleZombieDeath" : "MaleZombieDeath";
            var5.getEmitter().playVocals(var7);
            var5.getEmitter().tick();
            break;
         case DeadNotCloseKilled:
            var5.getEmitter().playSoundImpl("HeadSmash", (IsoObject)null);
            var7 = var5.isFemale() ? "FemaleZombieDeath" : "MaleZombieDeath";
            var5.getEmitter().playVocals(var7);
            var5.getEmitter().tick();
            break;
         case Hurt:
            var5.playHurtSound();
            break;
         case Idle:
            var7 = var5.isFemale() ? "FemaleZombieIdle" : "MaleZombieIdle";
            var5.getEmitter().playVocals(var7);
            break;
         case Lunge:
            var7 = var5.isFemale() ? "FemaleZombieAttack" : "MaleZombieAttack";
            var5.getEmitter().playVocals(var7);
            break;
         default:
            DebugLog.log("unhandled zombie sound " + var4);
         }
      }

   }

   static void receiveSlowFactor(ByteBuffer var0, short var1) {
      byte var2 = var0.get();
      float var3 = var0.getFloat();
      float var4 = var0.getFloat();
      IsoPlayer var5 = IsoPlayer.players[var2];
      if (var5 != null && !var5.isDead()) {
         var5.setSlowTimer(var3);
         var5.setSlowFactor(var4);
         DebugLog.log(DebugType.Combat, "slowTimer=" + var3 + " slowFactor=" + var4);
      }
   }

   public void sendCustomColor(IsoObject var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SendCustomColor.doPacket(var2);
      var2.putInt(var1.getSquare().getX());
      var2.putInt(var1.getSquare().getY());
      var2.putInt(var1.getSquare().getZ());
      var2.putInt(var1.getSquare().getObjects().indexOf(var1));
      var2.putFloat(var1.getCustomColor().r);
      var2.putFloat(var1.getCustomColor().g);
      var2.putFloat(var1.getCustomColor().b);
      var2.putFloat(var1.getCustomColor().a);
      PacketTypes.PacketType.SendCustomColor.send(connection);
   }

   public void sendBandage(int var1, int var2, boolean var3, float var4, boolean var5, String var6) {
      ByteBufferWriter var7 = connection.startPacket();
      PacketTypes.PacketType.Bandage.doPacket(var7);
      var7.putShort((short)var1);
      var7.putInt(var2);
      var7.putBoolean(var3);
      var7.putFloat(var4);
      var7.putBoolean(var5);
      GameWindow.WriteStringUTF(var7.bb, var6);
      PacketTypes.PacketType.Bandage.send(connection);
   }

   public void sendStitch(int var1, int var2, boolean var3, float var4) {
      ByteBufferWriter var5 = connection.startPacket();
      PacketTypes.PacketType.Stitch.doPacket(var5);
      var5.putShort((short)var1);
      var5.putInt(var2);
      var5.putBoolean(var3);
      var5.putFloat(var4);
      PacketTypes.PacketType.Stitch.send(connection);
   }

   public void sendWoundInfection(int var1, int var2, boolean var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.WoundInfection.doPacket(var4);
      var4.putShort((short)var1);
      var4.putInt(var2);
      var4.putBoolean(var3);
      PacketTypes.PacketType.WoundInfection.send(connection);
   }

   public void sendDisinfect(int var1, int var2, float var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.Disinfect.doPacket(var4);
      var4.putShort((short)var1);
      var4.putInt(var2);
      var4.putFloat(var3);
      PacketTypes.PacketType.Disinfect.send(connection);
   }

   public void sendSplint(int var1, int var2, boolean var3, float var4, String var5) {
      ByteBufferWriter var6 = connection.startPacket();
      PacketTypes.PacketType.Splint.doPacket(var6);
      var6.putShort((short)var1);
      var6.putInt(var2);
      var6.putBoolean(var3);
      if (var3) {
         if (var5 == null) {
            var5 = "";
         }

         var6.putUTF(var5);
         var6.putFloat(var4);
      }

      PacketTypes.PacketType.Splint.send(connection);
   }

   public void sendAdditionalPain(int var1, int var2, float var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.AdditionalPain.doPacket(var4);
      var4.putShort((short)var1);
      var4.putInt(var2);
      var4.putFloat(var3);
      PacketTypes.PacketType.AdditionalPain.send(connection);
   }

   public void sendRemoveGlass(int var1, int var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.RemoveGlass.doPacket(var3);
      var3.putShort((short)var1);
      var3.putInt(var2);
      PacketTypes.PacketType.RemoveGlass.send(connection);
   }

   public void sendRemoveBullet(int var1, int var2, int var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.RemoveBullet.doPacket(var4);
      var4.putShort((short)((byte)var1));
      var4.putInt(var2);
      var4.putInt(var3);
      PacketTypes.PacketType.RemoveBullet.send(connection);
   }

   public void sendCleanBurn(int var1, int var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.CleanBurn.doPacket(var3);
      var3.putShort((short)((byte)var1));
      var3.putInt(var2);
      PacketTypes.PacketType.CleanBurn.send(connection);
   }

   public void eatFood(IsoPlayer var1, Food var2, float var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.EatFood.doPacket(var4);

      try {
         var4.putByte((byte)var1.PlayerIndex);
         var4.putFloat(var3);
         var2.saveWithSize(var4.bb, false);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      PacketTypes.PacketType.EatFood.send(connection);
   }

   public void drink(IsoPlayer var1, float var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.Drink.doPacket(var3);
      var3.putByte((byte)var1.PlayerIndex);
      var3.putFloat(var2);
      PacketTypes.PacketType.Drink.send(connection);
   }

   public void addToItemSendBuffer(IsoObject var1, ItemContainer var2, InventoryItem var3) {
      if (var1 instanceof IsoWorldInventoryObject) {
         InventoryItem var4 = ((IsoWorldInventoryObject)var1).getItem();
         if (var3 == null || var4 == null || !(var4 instanceof InventoryContainer) || var2 != ((InventoryContainer)var4).getInventory()) {
            DebugLog.log("ERROR: addToItemSendBuffer parent=" + var1 + " item=" + var3);
            if (Core.bDebug) {
               throw new IllegalStateException();
            } else {
               return;
            }
         }
      } else if (var1 instanceof BaseVehicle) {
         if (var2.vehiclePart == null || var2.vehiclePart.getItemContainer() != var2 || var2.vehiclePart.getVehicle() != var1) {
            DebugLog.log("ERROR: addToItemSendBuffer parent=" + var1 + " item=" + var3);
            if (Core.bDebug) {
               throw new IllegalStateException();
            }

            return;
         }
      } else if (var1 == null || var3 == null || var1.getContainerIndex(var2) == -1) {
         DebugLog.log("ERROR: addToItemSendBuffer parent=" + var1 + " item=" + var3);
         if (Core.bDebug) {
            throw new IllegalStateException();
         }

         return;
      }

      ArrayList var5;
      if (this.itemsToSendRemove.containsKey(var2)) {
         var5 = (ArrayList)this.itemsToSendRemove.get(var2);
         if (var5.remove(var3)) {
            if (var5.isEmpty()) {
               this.itemsToSendRemove.remove(var2);
            }

            return;
         }
      }

      if (this.itemsToSend.containsKey(var2)) {
         ((ArrayList)this.itemsToSend.get(var2)).add(var3);
      } else {
         var5 = new ArrayList();
         this.itemsToSend.put(var2, var5);
         var5.add(var3);
      }

   }

   public void addToItemRemoveSendBuffer(IsoObject var1, ItemContainer var2, InventoryItem var3) {
      if (var1 instanceof IsoWorldInventoryObject) {
         InventoryItem var4 = ((IsoWorldInventoryObject)var1).getItem();
         if (var3 == null || var4 == null || !(var4 instanceof InventoryContainer) || var2 != ((InventoryContainer)var4).getInventory()) {
            DebugLog.log("ERROR: addToItemRemoveSendBuffer parent=" + var1 + " item=" + var3);
            if (Core.bDebug) {
               throw new IllegalStateException();
            } else {
               return;
            }
         }
      } else if (var1 instanceof BaseVehicle) {
         if (var2.vehiclePart == null || var2.vehiclePart.getItemContainer() != var2 || var2.vehiclePart.getVehicle() != var1) {
            DebugLog.log("ERROR: addToItemRemoveSendBuffer parent=" + var1 + " item=" + var3);
            if (Core.bDebug) {
               throw new IllegalStateException();
            }

            return;
         }
      } else if (var1 instanceof IsoDeadBody) {
         if (var3 == null || var2 != var1.getContainer()) {
            DebugLog.log("ERROR: addToItemRemoveSendBuffer parent=" + var1 + " item=" + var3);
            if (Core.bDebug) {
               throw new IllegalStateException();
            }

            return;
         }
      } else if (var1 == null || var3 == null || var1.getContainerIndex(var2) == -1) {
         DebugLog.log("ERROR: addToItemRemoveSendBuffer parent=" + var1 + " item=" + var3);
         if (Core.bDebug) {
            throw new IllegalStateException();
         }

         return;
      }

      if (!SystemDisabler.doWorldSyncEnable) {
         ArrayList var8;
         if (this.itemsToSend.containsKey(var2)) {
            var8 = (ArrayList)this.itemsToSend.get(var2);
            if (var8.remove(var3)) {
               if (var8.isEmpty()) {
                  this.itemsToSend.remove(var2);
               }

               return;
            }
         }

         if (this.itemsToSendRemove.containsKey(var2)) {
            ((ArrayList)this.itemsToSendRemove.get(var2)).add(var3);
         } else {
            var8 = new ArrayList();
            var8.add(var3);
            this.itemsToSendRemove.put(var2, var8);
         }

      } else {
         Object var7 = var2.getParent();
         if (var2.getContainingItem() != null && var2.getContainingItem().getWorldItem() != null) {
            var7 = var2.getContainingItem().getWorldItem();
         }

         GameClient var10000 = instance;
         UdpConnection var5 = connection;
         ByteBufferWriter var6 = var5.startPacket();
         PacketTypes.PacketType.RemoveInventoryItemFromContainer.doPacket(var6);
         if (var7 instanceof IsoDeadBody) {
            var6.putShort((short)0);
            var6.putInt(((IsoObject)var7).square.getX());
            var6.putInt(((IsoObject)var7).square.getY());
            var6.putInt(((IsoObject)var7).square.getZ());
            var6.putByte((byte)((IsoObject)var7).getStaticMovingObjectIndex());
            var6.putInt(1);
            var6.putInt(var3.id);
         } else if (var7 instanceof IsoWorldInventoryObject) {
            var6.putShort((short)1);
            var6.putInt(((IsoObject)var7).square.getX());
            var6.putInt(((IsoObject)var7).square.getY());
            var6.putInt(((IsoObject)var7).square.getZ());
            var6.putInt(((IsoWorldInventoryObject)var7).getItem().id);
            var6.putInt(1);
            var6.putInt(var3.id);
         } else if (var7 instanceof BaseVehicle) {
            var6.putShort((short)3);
            var6.putInt(((IsoObject)var7).square.getX());
            var6.putInt(((IsoObject)var7).square.getY());
            var6.putInt(((IsoObject)var7).square.getZ());
            var6.putShort(((BaseVehicle)var7).VehicleID);
            var6.putByte((byte)var2.vehiclePart.getIndex());
            var6.putInt(1);
            var6.putInt(var3.id);
         } else {
            var6.putShort((short)2);
            var6.putInt(((IsoObject)var7).square.getX());
            var6.putInt(((IsoObject)var7).square.getY());
            var6.putInt(((IsoObject)var7).square.getZ());
            var6.putByte((byte)((IsoObject)var7).square.getObjects().indexOf(var7));
            var6.putByte((byte)((IsoObject)var7).getContainerIndex(var2));
            var6.putInt(1);
            var6.putInt(var3.id);
         }

         PacketTypes.PacketType.RemoveInventoryItemFromContainer.send(var5);
      }
   }

   public void sendAddedRemovedItems(boolean var1) {
      boolean var2 = var1 || this.itemSendFrequency.Check();
      Iterator var3;
      Entry var4;
      ItemContainer var5;
      ArrayList var6;
      Object var7;
      ByteBufferWriter var8;
      int var9;
      Object var10000;
      if (!SystemDisabler.doWorldSyncEnable && !this.itemsToSendRemove.isEmpty() && var2) {
         var3 = this.itemsToSendRemove.entrySet().iterator();

         label166:
         while(true) {
            do {
               if (!var3.hasNext()) {
                  this.itemsToSendRemove.clear();
                  break label166;
               }

               var4 = (Entry)var3.next();
               var5 = (ItemContainer)var4.getKey();
               var6 = (ArrayList)var4.getValue();
               var7 = var5.getParent();
               if (var5.getContainingItem() != null && var5.getContainingItem().getWorldItem() != null) {
                  var7 = var5.getContainingItem().getWorldItem();
               }
            } while(((IsoObject)var7).square == null);

            try {
               var8 = connection.startPacket();
               PacketTypes.PacketType.RemoveInventoryItemFromContainer.doPacket(var8);
               if (var7 instanceof IsoDeadBody) {
                  var8.putShort((short)0);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putByte((byte)((IsoObject)var7).getStaticMovingObjectIndex());
               } else if (var7 instanceof IsoWorldInventoryObject) {
                  var8.putShort((short)1);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putInt(((IsoWorldInventoryObject)var7).getItem().id);
               } else if (var7 instanceof BaseVehicle) {
                  var8.putShort((short)3);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putShort(((BaseVehicle)var7).VehicleID);
                  var8.putByte((byte)var5.vehiclePart.getIndex());
               } else {
                  var8.putShort((short)2);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putByte((byte)((IsoObject)var7).square.getObjects().indexOf(var7));
                  var8.putByte((byte)((IsoObject)var7).getContainerIndex(var5));
               }

               var8.putInt(var6.size());

               for(var9 = 0; var9 < var6.size(); ++var9) {
                  InventoryItem var10 = (InventoryItem)var6.get(var9);
                  var8.putInt(var10.id);
               }

               PacketTypes.PacketType.RemoveInventoryItemFromContainer.send(connection);
            } catch (Exception var16) {
               DebugLog.log("sendAddedRemovedItems: itemsToSendRemove container:" + var5 + "." + var7 + " items:" + var6);
               if (var6 != null) {
                  for(var9 = 0; var9 < var6.size(); ++var9) {
                     if (var6.get(var9) == null) {
                        DebugLog.log("item:null");
                     } else {
                        var10000 = var6.get(var9);
                        DebugLog.log("item:" + ((InventoryItem)var10000).getName());
                     }
                  }

                  DebugLog.log("itemSize:" + var6.size());
               }

               var16.printStackTrace();
               connection.cancelPacket();
            }
         }
      }

      if (!this.itemsToSend.isEmpty() && var2) {
         var3 = this.itemsToSend.entrySet().iterator();

         while(true) {
            do {
               if (!var3.hasNext()) {
                  this.itemsToSend.clear();
                  return;
               }

               var4 = (Entry)var3.next();
               var5 = (ItemContainer)var4.getKey();
               var6 = (ArrayList)var4.getValue();
               var7 = var5.getParent();
               if (var5.getContainingItem() != null && var5.getContainingItem().getWorldItem() != null) {
                  var7 = var5.getContainingItem().getWorldItem();
               }
            } while(((IsoObject)var7).square == null);

            try {
               var8 = connection.startPacket();
               PacketTypes.PacketType.AddInventoryItemToContainer.doPacket(var8);
               if (var7 instanceof IsoDeadBody) {
                  var8.putShort((short)0);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putByte((byte)((IsoObject)var7).getStaticMovingObjectIndex());

                  try {
                     CompressIdenticalItems.save(var8.bb, var6, (IsoGameCharacter)null);
                  } catch (Exception var14) {
                     var14.printStackTrace();
                  }
               } else if (var7 instanceof IsoWorldInventoryObject) {
                  var8.putShort((short)1);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putInt(((IsoWorldInventoryObject)var7).getItem().id);

                  try {
                     CompressIdenticalItems.save(var8.bb, var6, (IsoGameCharacter)null);
                  } catch (Exception var13) {
                     var13.printStackTrace();
                  }
               } else if (var7 instanceof BaseVehicle) {
                  var8.putShort((short)3);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putShort(((BaseVehicle)var7).VehicleID);
                  var8.putByte((byte)var5.vehiclePart.getIndex());

                  try {
                     CompressIdenticalItems.save(var8.bb, var6, (IsoGameCharacter)null);
                  } catch (Exception var12) {
                     var12.printStackTrace();
                  }
               } else {
                  var8.putShort((short)2);
                  var8.putInt(((IsoObject)var7).square.getX());
                  var8.putInt(((IsoObject)var7).square.getY());
                  var8.putInt(((IsoObject)var7).square.getZ());
                  var8.putByte((byte)((IsoObject)var7).square.getObjects().indexOf(var7));
                  var8.putByte((byte)((IsoObject)var7).getContainerIndex(var5));

                  try {
                     CompressIdenticalItems.save(var8.bb, var6, (IsoGameCharacter)null);
                  } catch (Exception var11) {
                     var11.printStackTrace();
                  }
               }

               PacketTypes.PacketType.AddInventoryItemToContainer.send(connection);
            } catch (Exception var15) {
               DebugLog.log("sendAddedRemovedItems: itemsToSend container:" + var5 + "." + var7 + " items:" + var6);
               if (var6 != null) {
                  for(var9 = 0; var9 < var6.size(); ++var9) {
                     if (var6.get(var9) == null) {
                        DebugLog.log("item:null");
                     } else {
                        var10000 = var6.get(var9);
                        DebugLog.log("item:" + ((InventoryItem)var10000).getName());
                     }
                  }

                  DebugLog.log("itemSize:" + var6.size());
               }

               var15.printStackTrace();
               connection.cancelPacket();
            }
         }
      }
   }

   public void checkAddedRemovedItems(IsoObject var1) {
      if (var1 != null) {
         if (!this.itemsToSend.isEmpty() || !this.itemsToSendRemove.isEmpty()) {
            if (var1 instanceof IsoDeadBody) {
               if (this.itemsToSend.containsKey(var1.getContainer()) || this.itemsToSendRemove.containsKey(var1.getContainer())) {
                  this.sendAddedRemovedItems(true);
               }

            } else {
               ItemContainer var3;
               if (var1 instanceof IsoWorldInventoryObject) {
                  InventoryItem var4 = ((IsoWorldInventoryObject)var1).getItem();
                  if (var4 instanceof InventoryContainer) {
                     var3 = ((InventoryContainer)var4).getInventory();
                     if (this.itemsToSend.containsKey(var3) || this.itemsToSendRemove.containsKey(var3)) {
                        this.sendAddedRemovedItems(true);
                     }
                  }

               } else if (!(var1 instanceof BaseVehicle)) {
                  for(int var2 = 0; var2 < var1.getContainerCount(); ++var2) {
                     var3 = var1.getContainerByIndex(var2);
                     if (this.itemsToSend.containsKey(var3) || this.itemsToSendRemove.containsKey(var3)) {
                        this.sendAddedRemovedItems(true);
                        return;
                     }
                  }

               }
            }
         }
      }
   }

   private void writeItemStats(ByteBufferWriter var1, InventoryItem var2) {
      var1.putInt(var2.id);
      var1.putInt(var2.getUses());
      var1.putFloat(var2 instanceof DrainableComboItem ? ((DrainableComboItem)var2).getUsedDelta() : 0.0F);
      if (var2 instanceof Food) {
         Food var3 = (Food)var2;
         var1.putBoolean(true);
         var1.putFloat(var3.getHungChange());
         var1.putFloat(var3.getCalories());
         var1.putFloat(var3.getCarbohydrates());
         var1.putFloat(var3.getLipids());
         var1.putFloat(var3.getProteins());
         var1.putFloat(var3.getThirstChange());
         var1.putInt(var3.getFluReduction());
         var1.putFloat(var3.getPainReduction());
         var1.putFloat(var3.getEndChange());
         var1.putInt(var3.getReduceFoodSickness());
         var1.putFloat(var3.getStressChange());
         var1.putFloat(var3.getFatigueChange());
      } else {
         var1.putBoolean(false);
      }

   }

   public void sendItemStats(InventoryItem var1) {
      if (var1 != null) {
         if (var1.getWorldItem() != null && var1.getWorldItem().getWorldObjectIndex() != -1) {
            IsoWorldInventoryObject var6 = var1.getWorldItem();
            ByteBufferWriter var7 = connection.startPacket();
            PacketTypes.PacketType.ItemStats.doPacket(var7);
            var7.putShort((short)1);
            var7.putInt(var6.square.getX());
            var7.putInt(var6.square.getY());
            var7.putInt(var6.square.getZ());
            this.writeItemStats(var7, var1);
            PacketTypes.PacketType.ItemStats.send(connection);
         } else if (var1.getContainer() == null) {
            DebugLog.log("ERROR: sendItemStats(): item is neither in a container nor on the ground");
            if (Core.bDebug) {
               throw new IllegalStateException();
            }
         } else {
            ItemContainer var2 = var1.getContainer();
            Object var3 = var2.getParent();
            if (var2.getContainingItem() != null && var2.getContainingItem().getWorldItem() != null) {
               var3 = var2.getContainingItem().getWorldItem();
            }

            if (var3 instanceof IsoWorldInventoryObject) {
               InventoryItem var5 = ((IsoWorldInventoryObject)var3).getItem();
               if (!(var5 instanceof InventoryContainer) || var2 != ((InventoryContainer)var5).getInventory()) {
                  DebugLog.log("ERROR: sendItemStats() parent=" + var3 + " item=" + var1);
                  if (Core.bDebug) {
                     throw new IllegalStateException();
                  } else {
                     return;
                  }
               }
            } else if (var3 instanceof BaseVehicle) {
               if (var2.vehiclePart == null || var2.vehiclePart.getItemContainer() != var2 || var2.vehiclePart.getVehicle() != var3) {
                  DebugLog.log("ERROR: sendItemStats() parent=" + var3 + " item=" + var1);
                  if (Core.bDebug) {
                     throw new IllegalStateException();
                  }

                  return;
               }
            } else if (var3 instanceof IsoDeadBody) {
               if (var2 != ((IsoObject)var3).getContainer()) {
                  DebugLog.log("ERROR: sendItemStats() parent=" + var3 + " item=" + var1);
                  if (Core.bDebug) {
                     throw new IllegalStateException();
                  }

                  return;
               }
            } else if (var3 == null || ((IsoObject)var3).getContainerIndex(var2) == -1) {
               DebugLog.log("ERROR: sendItemStats() parent=" + var3 + " item=" + var1);
               if (Core.bDebug) {
                  throw new IllegalStateException();
               }

               return;
            }

            ByteBufferWriter var8 = connection.startPacket();
            PacketTypes.PacketType.ItemStats.doPacket(var8);
            if (var3 instanceof IsoDeadBody) {
               var8.putShort((short)0);
               var8.putInt(((IsoObject)var3).square.getX());
               var8.putInt(((IsoObject)var3).square.getY());
               var8.putInt(((IsoObject)var3).square.getZ());
               var8.putByte((byte)((IsoObject)var3).getStaticMovingObjectIndex());
            } else if (var3 instanceof IsoWorldInventoryObject) {
               var8.putShort((short)1);
               var8.putInt(((IsoObject)var3).square.getX());
               var8.putInt(((IsoObject)var3).square.getY());
               var8.putInt(((IsoObject)var3).square.getZ());
            } else if (var3 instanceof BaseVehicle) {
               var8.putShort((short)3);
               var8.putInt(((IsoObject)var3).square.getX());
               var8.putInt(((IsoObject)var3).square.getY());
               var8.putInt(((IsoObject)var3).square.getZ());
               var8.putShort(((BaseVehicle)var3).VehicleID);
               var8.putByte((byte)var2.vehiclePart.getIndex());
            } else {
               var8.putShort((short)2);
               var8.putInt(((IsoObject)var3).square.getX());
               var8.putInt(((IsoObject)var3).square.getY());
               var8.putInt(((IsoObject)var3).square.getZ());
               var8.putByte((byte)((IsoObject)var3).getObjectIndex());
               var8.putByte((byte)((IsoObject)var3).getContainerIndex(var2));
            }

            this.writeItemStats(var8, var1);
            PacketTypes.PacketType.ItemStats.send(connection);
         }
      }
   }

   public void PlayWorldSound(String var1, int var2, int var3, byte var4) {
      PlayWorldSoundPacket var5 = new PlayWorldSoundPacket();
      var5.set(var1, var2, var3, var4);
      ByteBufferWriter var6 = connection.startPacket();
      PacketTypes.PacketType.PlayWorldSound.doPacket(var6);
      var5.write(var6);
      PacketTypes.PacketType.PlayWorldSound.send(connection);
   }

   public void PlaySound(String var1, boolean var2, IsoMovingObject var3) {
      PlaySoundPacket var4 = new PlaySoundPacket();
      var4.set(var1, var2, var3);
      ByteBufferWriter var5 = connection.startPacket();
      PacketTypes.PacketType.PlaySound.doPacket(var5);
      var4.write(var5);
      PacketTypes.PacketType.PlaySound.send(connection);
   }

   public void StopSound(IsoMovingObject var1, String var2, boolean var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.StopSound.doPacket(var4);
      StopSoundPacket var5 = new StopSoundPacket();
      var5.set(var1, var2, var3);
      var5.write(var4);
      PacketTypes.PacketType.StopSound.send(connection);
   }

   public void startLocalServer() throws Exception {
      bClient = true;
      ip = "127.0.0.1";
      Thread var1 = new Thread(ThreadGroups.Workers, () -> {
         String var0 = System.getProperty("file.separator");
         String var1 = System.getProperty("java.class.path");
         String var10000 = System.getProperty("java.home");
         String var2 = var10000 + var0 + "bin" + var0 + "java";
         ProcessBuilder var3 = new ProcessBuilder(new String[]{var2, "-Xms2048m", "-Xmx2048m", "-Djava.library.path=../natives/", "-cp", "lwjgl.jar;lwjgl_util.jar;sqlitejdbc-v056.jar;../bin/", "zombie.network.GameServer"});
         var3.redirectErrorStream(true);
         Process var4 = null;

         try {
            var4 = var3.start();
         } catch (IOException var10) {
            var10.printStackTrace();
         }

         InputStreamReader var5 = new InputStreamReader(var4.getInputStream());
         boolean var6 = false;

         try {
            while(!var5.ready()) {
               int var7;
               try {
                  while((var7 = var5.read()) != -1) {
                     System.out.print((char)var7);
                  }
               } catch (IOException var11) {
                  var11.printStackTrace();
               }

               try {
                  var5.close();
               } catch (IOException var9) {
                  var9.printStackTrace();
               }
            }
         } catch (IOException var12) {
            var12.printStackTrace();
         }

      });
      var1.setUncaughtExceptionHandler(GameWindow::uncaughtException);
      var1.start();
   }

   public static void sendPing() {
      if (bClient) {
         ByteBufferWriter var0 = connection.startPingPacket();
         PacketTypes.doPingPacket(var0);
         var0.putLong(System.currentTimeMillis());
         var0.putLong(0L);
         connection.endPingPacket();
      }

   }

   public static void registerZone(IsoMetaGrid.Zone var0, boolean var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.RegisterZone.doPacket(var2);
      var2.putUTF(var0.name);
      var2.putUTF(var0.type);
      var2.putInt(var0.x);
      var2.putInt(var0.y);
      var2.putInt(var0.z);
      var2.putInt(var0.w);
      var2.putInt(var0.h);
      var2.putInt(var0.getLastActionTimestamp());
      var2.putBoolean(var1);
      PacketTypes.PacketType.SyncAlarmClock.send(connection);
   }

   static void receiveHelicopter(ByteBuffer var0, short var1) {
      float var2 = var0.getFloat();
      float var3 = var0.getFloat();
      boolean var4 = var0.get() == 1;
      if (IsoWorld.instance != null && IsoWorld.instance.helicopter != null) {
         IsoWorld.instance.helicopter.clientSync(var2, var3, var4);
      }

   }

   static void receiveVehicles(ByteBuffer var0, short var1) {
      VehicleManager.instance.clientPacket(var0);
   }

   static void receiveVehiclesLoading(ByteBuffer var0, short var1) throws IOException {
      VehicleManager.loadingClientPacket(var0);
      throw new IOException();
   }

   static void receiveTimeSync(ByteBuffer var0, short var1) {
      GameTime.receiveTimeSync(var0, connection);
   }

   public static void sendSafehouse(SafeHouse var0, boolean var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SyncSafehouse.doPacket(var2);
      var2.putInt(var0.getX());
      var2.putInt(var0.getY());
      var2.putInt(var0.getW());
      var2.putInt(var0.getH());
      var2.putUTF(var0.getOwner());
      var2.putInt(var0.getPlayers().size());
      Iterator var3 = var0.getPlayers().iterator();

      String var4;
      while(var3.hasNext()) {
         var4 = (String)var3.next();
         var2.putUTF(var4);
      }

      var2.putInt(var0.playersRespawn.size());
      var3 = var0.playersRespawn.iterator();

      while(var3.hasNext()) {
         var4 = (String)var3.next();
         var2.putUTF(var4);
      }

      var2.putBoolean(var1);
      var2.putUTF(var0.getTitle());
      PacketTypes.PacketType.SyncSafehouse.send(connection);
   }

   static void receiveSyncSafehouse(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      String var6 = GameWindow.ReadString(var0);
      int var7 = var0.getInt();
      SafeHouse var8 = SafeHouse.getSafeHouse(var2, var3, var4, var5);
      if (var8 == null) {
         var8 = SafeHouse.addSafeHouse(var2, var3, var4, var5, var6, true);
      }

      if (var8 != null) {
         var8.getPlayers().clear();

         int var9;
         for(var9 = 0; var9 < var7; ++var9) {
            var8.getPlayers().add(GameWindow.ReadString(var0));
         }

         var9 = var0.getInt();
         var8.playersRespawn.clear();

         for(int var10 = 0; var10 < var9; ++var10) {
            var8.playersRespawn.add(GameWindow.ReadString(var0));
         }

         if (var0.get() == 1) {
            SafeHouse.getSafehouseList().remove(var8);
         }

         var8.setTitle(GameWindow.ReadString(var0));
         var8.setOwner(var6);
         LuaEventManager.triggerEvent("OnSafehousesChanged");
      }
   }

   public IsoPlayer getPlayerFromUsername(String var1) {
      ArrayList var2 = this.getPlayers();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoPlayer var4 = (IsoPlayer)var2.get(var3);
         if (var4.getUsername().equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public static void destroy(IsoObject var0) {
      if (ServerOptions.instance.AllowDestructionBySledgehammer.getValue()) {
         ByteBufferWriter var1 = connection.startPacket();
         PacketTypes.PacketType.SledgehammerDestroy.doPacket(var1);
         IsoGridSquare var2 = var0.getSquare();
         var1.putInt(var2.getX());
         var1.putInt(var2.getY());
         var1.putInt(var2.getZ());
         var1.putInt(var2.getObjects().indexOf(var0));
         PacketTypes.PacketType.SledgehammerDestroy.send(connection);
         var2.RemoveTileObject(var0);
      }

   }

   public static void sendTeleport(IsoPlayer var0, float var1, float var2, float var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.Teleport.doPacket(var4);
      GameWindow.WriteString(var4.bb, var0.getUsername());
      var4.putFloat(var1);
      var4.putFloat(var2);
      var4.putFloat(var3);
      PacketTypes.PacketType.Teleport.send(connection);
   }

   public static void sendStopFire(IsoGridSquare var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.StopFire.doPacket(var1);
      var1.putByte((byte)0);
      var1.putInt(var0.getX());
      var1.putInt(var0.getY());
      var1.putInt(var0.getZ());
      PacketTypes.PacketType.StopFire.send(connection);
   }

   public static void sendStopFire(IsoGameCharacter var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.StopFire.doPacket(var1);
      if (var0 instanceof IsoPlayer) {
         var1.putByte((byte)1);
         var1.putShort(var0.getOnlineID());
      }

      if (var0 instanceof IsoZombie) {
         var1.putByte((byte)2);
         var1.putShort(((IsoZombie)var0).OnlineID);
      }

      PacketTypes.PacketType.StopFire.send(connection);
   }

   public void sendCataplasm(int var1, int var2, float var3, float var4, float var5) {
      ByteBufferWriter var6 = connection.startPacket();
      PacketTypes.PacketType.Cataplasm.doPacket(var6);
      var6.putShort((short)var1);
      var6.putInt(var2);
      var6.putFloat(var3);
      var6.putFloat(var4);
      var6.putFloat(var5);
      PacketTypes.PacketType.Cataplasm.send(connection);
   }

   static void receiveBodyDamageUpdate(ByteBuffer var0, short var1) {
      BodyDamageSync.instance.clientPacket(var0);
   }

   static void receivePacketTypeShort(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      switch(var2) {
      case 1000:
         receiveWaveSignal(var0);
         break;
      case 1002:
         receiveRadioServerData(var0);
         break;
      case 1004:
         receiveRadioDeviceDataState(var0);
         break;
      case 1200:
         SyncCustomLightSwitchSettings(var0);
      }

   }

   public static void receiveRadioDeviceDataState(ByteBuffer var0) {
      byte var1 = var0.get();
      if (var1 == 1) {
         int var2 = var0.getInt();
         int var3 = var0.getInt();
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
         if (var6 != null && var5 >= 0 && var5 < var6.getObjects().size()) {
            IsoObject var7 = (IsoObject)var6.getObjects().get(var5);
            if (var7 instanceof IsoWaveSignal) {
               DeviceData var8 = ((IsoWaveSignal)var7).getDeviceData();
               if (var8 != null) {
                  try {
                     var8.receiveDeviceDataStatePacket(var0, (UdpConnection)null);
                  } catch (Exception var12) {
                     System.out.print(var12.getMessage());
                  }
               }
            }
         }
      } else {
         short var13;
         if (var1 == 0) {
            var13 = var0.getShort();
            IsoPlayer var14 = (IsoPlayer)IDToPlayerMap.get(var13);
            byte var16 = var0.get();
            if (var14 != null) {
               Radio var18 = null;
               if (var16 == 1 && var14.getPrimaryHandItem() instanceof Radio) {
                  var18 = (Radio)var14.getPrimaryHandItem();
               }

               if (var16 == 2 && var14.getSecondaryHandItem() instanceof Radio) {
                  var18 = (Radio)var14.getSecondaryHandItem();
               }

               if (var18 != null && var18.getDeviceData() != null) {
                  try {
                     var18.getDeviceData().receiveDeviceDataStatePacket(var0, connection);
                  } catch (Exception var11) {
                     System.out.print(var11.getMessage());
                  }
               }
            }
         } else if (var1 == 2) {
            var13 = var0.getShort();
            short var15 = var0.getShort();
            BaseVehicle var17 = VehicleManager.instance.getVehicleByID(var13);
            if (var17 != null) {
               VehiclePart var20 = var17.getPartByIndex(var15);
               if (var20 != null) {
                  DeviceData var19 = var20.getDeviceData();
                  if (var19 != null) {
                     try {
                        var19.receiveDeviceDataStatePacket(var0, (UdpConnection)null);
                     } catch (Exception var10) {
                        System.out.print(var10.getMessage());
                     }
                  }
               }
            }
         }
      }

   }

   public static void sendRadioServerDataRequest() {
      ByteBufferWriter var0 = connection.startPacket();
      PacketTypesShort.doPacket((short)1002, var0);
      PacketTypes.PacketType.PacketTypeShort.send(connection);
   }

   private static void receiveRadioServerData(ByteBuffer var0) {
      ZomboidRadio var1 = ZomboidRadio.getInstance();
      int var2 = var0.getInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = GameWindow.ReadString(var0);
         int var5 = var0.getInt();

         for(int var6 = 0; var6 < var5; ++var6) {
            int var7 = var0.getInt();
            String var8 = GameWindow.ReadString(var0);
            var1.addChannelName(var8, var7, var4);
         }
      }

      var1.setHasRecievedServerData(true);
   }

   public static void sendIsoWaveSignal(int var0, int var1, int var2, String var3, String var4, float var5, float var6, float var7, int var8, boolean var9) {
      ByteBufferWriter var10 = connection.startPacket();
      PacketTypesShort.doPacket((short)1000, var10);
      var10.putInt(var0);
      var10.putInt(var1);
      var10.putInt(var2);
      var10.putBoolean(var3 != null);
      if (var3 != null) {
         GameWindow.WriteString(var10.bb, var3);
      }

      var10.putByte((byte)(var4 != null ? 1 : 0));
      if (var4 != null) {
         var10.putUTF(var4);
      }

      var10.putFloat(var5);
      var10.putFloat(var6);
      var10.putFloat(var7);
      var10.putInt(var8);
      var10.putByte((byte)(var9 ? 1 : 0));
      PacketTypes.PacketType.PacketTypeShort.send(connection);
   }

   private static void receiveWaveSignal(ByteBuffer var0) {
      if (ChatManager.getInstance().isWorking()) {
         int var1 = var0.getInt();
         int var2 = var0.getInt();
         int var3 = var0.getInt();
         String var4 = null;
         byte var5 = var0.get();
         if (var5 == 1) {
            var4 = GameWindow.ReadString(var0);
         }

         String var6 = null;
         if (var0.get() == 1) {
            var6 = GameWindow.ReadString(var0);
         }

         float var7 = var0.getFloat();
         float var8 = var0.getFloat();
         float var9 = var0.getFloat();
         int var10 = var0.getInt();
         boolean var11 = var0.get() == 1;
         ZomboidRadio.getInstance().ReceiveTransmission(var1, var2, var3, var4, var6, var7, var8, var9, var10, var11);
      }
   }

   public static void sendPlayerListensChannel(int var0, boolean var1, boolean var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypesShort.doPacket((short)1001, var3);
      var3.putInt(var0);
      var3.putByte((byte)(var1 ? 1 : 0));
      var3.putByte((byte)(var2 ? 1 : 0));
      PacketTypes.PacketType.PacketTypeShort.send(connection);
   }

   static void receiveSyncFurnace(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 == null) {
         instance.delayPacket(var2, var3, var4);
      } else {
         if (var5 != null) {
            BSFurnace var6 = null;

            for(int var7 = 0; var7 < var5.getObjects().size(); ++var7) {
               if (var5.getObjects().get(var7) instanceof BSFurnace) {
                  var6 = (BSFurnace)var5.getObjects().get(var7);
                  break;
               }
            }

            if (var6 == null) {
               DebugLog.log("receiveFurnaceChange: furnace is null x,y,z=" + var2 + "," + var3 + "," + var4);
               return;
            }

            var6.fireStarted = var0.get() == 1;
            var6.fuelAmount = var0.getFloat();
            var6.fuelDecrease = var0.getFloat();
            var6.heat = var0.getFloat();
            var6.sSprite = GameWindow.ReadString(var0);
            var6.sLitSprite = GameWindow.ReadString(var0);
            var6.updateLight();
         }

      }
   }

   public static void sendFurnaceChange(BSFurnace var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.SyncFurnace.doPacket(var1);
      var1.putInt(var0.getSquare().getX());
      var1.putInt(var0.getSquare().getY());
      var1.putInt(var0.getSquare().getZ());
      var1.putByte((byte)(var0.isFireStarted() ? 1 : 0));
      var1.putFloat(var0.getFuelAmount());
      var1.putFloat(var0.getFuelDecrease());
      var1.putFloat(var0.getHeat());
      GameWindow.WriteString(var1.bb, var0.sSprite);
      GameWindow.WriteString(var1.bb, var0.sLitSprite);
      PacketTypes.PacketType.SyncFurnace.send(connection);
   }

   public static void sendCompost(IsoCompost var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.SyncCompost.doPacket(var1);
      var1.putInt(var0.getSquare().getX());
      var1.putInt(var0.getSquare().getY());
      var1.putInt(var0.getSquare().getZ());
      var1.putFloat(var0.getCompost());
      PacketTypes.PacketType.SyncCompost.send(connection);
   }

   static void receiveSyncCompost(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 != null) {
         IsoCompost var6 = var5.getCompost();
         if (var6 == null) {
            var6 = new IsoCompost(var5.getCell(), var5);
            var5.AddSpecialObject(var6);
         }

         var6.setCompost(var0.getFloat());
         var6.updateSprite();
      }

   }

   public void requestUserlog(String var1) {
      if (canSeePlayerStats()) {
         ByteBufferWriter var2 = connection.startPacket();
         PacketTypes.PacketType.Userlog.doPacket(var2);
         GameWindow.WriteString(var2.bb, var1);
         PacketTypes.PacketType.Userlog.send(connection);
      }
   }

   public void addUserlog(String var1, String var2, String var3) {
      if (canSeePlayerStats()) {
         ByteBufferWriter var4 = connection.startPacket();
         PacketTypes.PacketType.AddUserlog.doPacket(var4);
         GameWindow.WriteString(var4.bb, var1);
         GameWindow.WriteString(var4.bb, var2);
         GameWindow.WriteString(var4.bb, var3);
         PacketTypes.PacketType.AddUserlog.send(connection);
      }
   }

   public void removeUserlog(String var1, String var2, String var3) {
      if (canModifyPlayerStats()) {
         ByteBufferWriter var4 = connection.startPacket();
         PacketTypes.PacketType.RemoveUserlog.doPacket(var4);
         GameWindow.WriteString(var4.bb, var1);
         GameWindow.WriteString(var4.bb, var2);
         GameWindow.WriteString(var4.bb, var3);
         PacketTypes.PacketType.RemoveUserlog.send(connection);
      }
   }

   public void addWarningPoint(String var1, String var2, int var3) {
      if (canModifyPlayerStats()) {
         ByteBufferWriter var4 = connection.startPacket();
         PacketTypes.PacketType.AddWarningPoint.doPacket(var4);
         GameWindow.WriteString(var4.bb, var1);
         GameWindow.WriteString(var4.bb, var2);
         var4.putInt(var3);
         PacketTypes.PacketType.AddWarningPoint.send(connection);
      }
   }

   static void receiveMessageForAdmin(ByteBuffer var0, short var1) {
      if (canSeePlayerStats()) {
         String var2 = GameWindow.ReadString(var0);
         int var3 = var0.getInt();
         int var4 = var0.getInt();
         int var5 = var0.getInt();
         LuaEventManager.triggerEvent("OnAdminMessage", var2, var3, var4, var5);
      }

   }

   public void wakeUpPlayer(IsoPlayer var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.WakeUpPlayer.doPacket(var2);
      var2.putShort(var1.OnlineID);
      PacketTypes.PacketType.WakeUpPlayer.send(connection);
   }

   static void receiveWakeUpPlayer(ByteBuffer var0, short var1) {
      IsoPlayer var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      if (var2 != null) {
         SleepingEvent.instance.wakeUp(var2, true);
      }

   }

   public void getDBSchema() {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.GetDBSchema.doPacket(var1);
      PacketTypes.PacketType.GetDBSchema.send(connection);
   }

   static void receiveGetDBSchema(ByteBuffer var0, short var1) {
      if (!accessLevel.equals("Observer") && !accessLevel.equals("")) {
         instance.dbSchema = LuaManager.platform.newTable();
         int var2 = var0.getInt();

         for(int var3 = 0; var3 < var2; ++var3) {
            KahluaTable var4 = LuaManager.platform.newTable();
            String var5 = GameWindow.ReadString(var0);
            int var6 = var0.getInt();

            for(int var7 = 0; var7 < var6; ++var7) {
               KahluaTable var8 = LuaManager.platform.newTable();
               String var9 = GameWindow.ReadString(var0);
               String var10 = GameWindow.ReadString(var0);
               var8.rawset("name", var9);
               var8.rawset("type", var10);
               var4.rawset(var7, var8);
            }

            instance.dbSchema.rawset(var5, var4);
         }

         LuaEventManager.triggerEvent("OnGetDBSchema", instance.dbSchema);
      }
   }

   public void getTableResult(String var1, int var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.GetTableResult.doPacket(var3);
      var3.putInt(var2);
      var3.putUTF(var1);
      PacketTypes.PacketType.GetTableResult.send(connection);
   }

   static void receiveGetTableResult(ByteBuffer var0, short var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var0.getInt();
      String var4 = GameWindow.ReadString(var0);
      int var5 = var0.getInt();
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < var5; ++var7) {
         DBResult var8 = new DBResult();
         var8.setTableName(var4);
         int var9 = var0.getInt();

         for(int var10 = 0; var10 < var9; ++var10) {
            String var11 = GameWindow.ReadString(var0);
            String var12 = GameWindow.ReadString(var0);
            var8.getValues().put(var11, var12);
            if (var7 == 0) {
               var6.add(var11);
            }
         }

         var8.setColumns(var6);
         var2.add(var8);
      }

      LuaEventManager.triggerEvent("OnGetTableResult", var2, var3, var4);
   }

   public void executeQuery(String var1, KahluaTable var2) {
      if (accessLevel.equals("admin")) {
         ByteBufferWriter var3 = connection.startPacket();
         PacketTypes.PacketType.ExecuteQuery.doPacket(var3);

         try {
            var3.putUTF(var1);
            var2.save(var3.bb);
         } catch (Throwable var8) {
            ExceptionLogger.logException(var8);
         } finally {
            PacketTypes.PacketType.ExecuteQuery.send(connection);
         }

      }
   }

   public ArrayList getConnectedPlayers() {
      return this.connectedPlayers;
   }

   public static void sendNonPvpZone(NonPvpZone var0, boolean var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SyncNonPvpZone.doPacket(var2);
      var2.putInt(var0.getX());
      var2.putInt(var0.getY());
      var2.putInt(var0.getX2());
      var2.putInt(var0.getY2());
      var2.putUTF(var0.getTitle());
      var2.putBoolean(var1);
      PacketTypes.PacketType.SyncNonPvpZone.send(connection);
   }

   public static void sendFaction(Faction var0, boolean var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SyncFaction.doPacket(var2);
      var0.writeToBuffer(var2, var1);
      PacketTypes.PacketType.SyncFaction.send(connection);
   }

   public static void sendFactionInvite(Faction var0, IsoPlayer var1, String var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.SendFactionInvite.doPacket(var3);
      var3.putUTF(var0.getName());
      var3.putUTF(var1.getUsername());
      var3.putUTF(var2);
      PacketTypes.PacketType.SendFactionInvite.send(connection);
   }

   static void receiveSendFactionInvite(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      LuaEventManager.triggerEvent("ReceiveFactionInvite", var2, var3);
   }

   public static void acceptFactionInvite(Faction var0, String var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.AcceptedFactionInvite.doPacket(var2);
      var2.putUTF(var0.getName());
      var2.putUTF(var1);
      PacketTypes.PacketType.AcceptedFactionInvite.send(connection);
   }

   static void receiveAcceptedFactionInvite(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      Faction var4 = Faction.getFaction(var2);
      if (var4 != null) {
         var4.addPlayer(var3);
      }

      LuaEventManager.triggerEvent("AcceptedFactionInvite", var2, var3);
   }

   public static void addTicket(String var0, String var1, int var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.AddTicket.doPacket(var3);
      var3.putUTF(var0);
      var3.putUTF(var1);
      var3.putInt(var2);
      PacketTypes.PacketType.AddTicket.send(connection);
   }

   public static void getTickets(String var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.ViewTickets.doPacket(var1);
      var1.putUTF(var0);
      PacketTypes.PacketType.ViewTickets.send(connection);
   }

   static void receiveViewTickets(ByteBuffer var0, short var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var0.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         DBTicket var5 = new DBTicket(GameWindow.ReadString(var0), GameWindow.ReadString(var0), var0.getInt());
         var2.add(var5);
         if (var0.get() == 1) {
            DBTicket var6 = new DBTicket(GameWindow.ReadString(var0), GameWindow.ReadString(var0), var0.getInt());
            var6.setIsAnswer(true);
            var5.setAnswer(var6);
         }
      }

      LuaEventManager.triggerEvent("ViewTickets", var2);
   }

   static void receiveChecksum(ByteBuffer var0, short var1) {
      NetChecksum.comparer.clientPacket(var0);
   }

   public static void removeTicket(int var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.RemoveTicket.doPacket(var1);
      var1.putInt(var0);
      PacketTypes.PacketType.RemoveTicket.send(connection);
   }

   public static boolean sendItemListNet(IsoPlayer var0, ArrayList var1, IsoPlayer var2, String var3, String var4) {
      ByteBufferWriter var5 = connection.startPacket();
      PacketTypes.PacketType.SendItemListNet.doPacket(var5);
      var5.putByte((byte)(var2 != null ? 1 : 0));
      if (var2 != null) {
         var5.putShort(var2.getOnlineID());
      }

      var5.putByte((byte)(var0 != null ? 1 : 0));
      if (var0 != null) {
         var5.putShort(var0.getOnlineID());
      }

      GameWindow.WriteString(var5.bb, var3);
      var5.putByte((byte)(var4 != null ? 1 : 0));
      if (var4 != null) {
         GameWindow.WriteString(var5.bb, var4);
      }

      try {
         CompressIdenticalItems.save(var5.bb, var1, (IsoGameCharacter)null);
      } catch (Exception var7) {
         var7.printStackTrace();
         connection.cancelPacket();
         return false;
      }

      PacketTypes.PacketType.SendItemListNet.send(connection);
      return true;
   }

   static void receiveSendItemListNet(ByteBuffer var0, short var1) {
      IsoPlayer var2 = null;
      if (var0.get() != 1) {
         var2 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      }

      IsoPlayer var3 = null;
      if (var0.get() == 1) {
         var3 = (IsoPlayer)IDToPlayerMap.get(var0.getShort());
      }

      String var4 = GameWindow.ReadString(var0);
      String var5 = null;
      if (var0.get() == 1) {
         var5 = GameWindow.ReadString(var0);
      }

      short var6 = var0.getShort();
      ArrayList var7 = new ArrayList(var6);

      try {
         for(int var8 = 0; var8 < var6; ++var8) {
            InventoryItem var9 = InventoryItem.loadItem(var0, 186);
            if (var9 != null) {
               var7.add(var9);
            }
         }
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      LuaEventManager.triggerEvent("OnReceiveItemListNet", var3, var7, var2, var4, var5);
   }

   public void requestTrading(IsoPlayer var1, IsoPlayer var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.RequestTrading.doPacket(var3);
      var3.putShort(var1.OnlineID);
      var3.putShort(var2.OnlineID);
      var3.putByte((byte)0);
      PacketTypes.PacketType.RequestTrading.send(connection);
   }

   public void acceptTrading(IsoPlayer var1, IsoPlayer var2, boolean var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.RequestTrading.doPacket(var4);
      var4.putShort(var2.OnlineID);
      var4.putShort(var1.OnlineID);
      var4.putByte((byte)(var3 ? 1 : 2));
      PacketTypes.PacketType.RequestTrading.send(connection);
   }

   static void receiveRequestTrading(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      byte var3 = var0.get();
      IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var4 != null) {
         if (var3 == 0) {
            LuaEventManager.triggerEvent("RequestTrade", var4);
         } else {
            LuaEventManager.triggerEvent("AcceptedTrade", var3 == 1);
         }
      }

   }

   public void tradingUISendAddItem(IsoPlayer var1, IsoPlayer var2, InventoryItem var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.TradingUIAddItem.doPacket(var4);
      var4.putShort(var1.OnlineID);
      var4.putShort(var2.OnlineID);

      try {
         var3.saveWithSize(var4.bb, false);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      PacketTypes.PacketType.TradingUIAddItem.send(connection);
   }

   static void receiveTradingUIAddItem(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      InventoryItem var3 = null;

      try {
         var3 = InventoryItem.loadItem(var0, 186);
      } catch (Exception var5) {
         var5.printStackTrace();
         return;
      }

      if (var3 != null) {
         IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
         if (var4 != null) {
            LuaEventManager.triggerEvent("TradingUIAddItem", var4, var3);
         }

      }
   }

   public void tradingUISendRemoveItem(IsoPlayer var1, IsoPlayer var2, int var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.TradingUIRemoveItem.doPacket(var4);
      var4.putShort(var1.OnlineID);
      var4.putShort(var2.OnlineID);
      var4.putInt(var3);
      PacketTypes.PacketType.TradingUIRemoveItem.send(connection);
   }

   static void receiveTradingUIRemoveItem(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var3 = var0.getInt();
      IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var4 != null) {
         LuaEventManager.triggerEvent("TradingUIRemoveItem", var4, var3);
      }

   }

   public void tradingUISendUpdateState(IsoPlayer var1, IsoPlayer var2, int var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.TradingUIUpdateState.doPacket(var4);
      var4.putShort(var1.OnlineID);
      var4.putShort(var2.OnlineID);
      var4.putInt(var3);
      PacketTypes.PacketType.TradingUIUpdateState.send(connection);
   }

   static void receiveTradingUIUpdateState(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var3 = var0.getInt();
      IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var4 != null) {
         LuaEventManager.triggerEvent("TradingUIUpdateState", var4, var3);
      }

   }

   public static void sendBuildingStashToDo(String var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.ReadAnnotedMap.doPacket(var1);
      var1.putUTF(var0);
      PacketTypes.PacketType.ReadAnnotedMap.send(connection);
   }

   public static void setServerStatisticEnable(boolean var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.StatisticRequest.doPacket(var1);
      var1.putBoolean(var0);
      PacketTypes.PacketType.StatisticRequest.send(connection);
      MPStatistic.clientStatisticEnable = var0;
   }

   public static boolean getServerStatisticEnable() {
      return MPStatistic.clientStatisticEnable;
   }

   public static void sendRequestInventory(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.RequestInventory.doPacket(var1);
      var1.putShort(IsoPlayer.getInstance().getOnlineID());
      var1.putShort(var0.getOnlineID());
      PacketTypes.PacketType.RequestInventory.send(connection);
   }

   private int sendInventoryPutItems(ByteBufferWriter var1, LinkedHashMap var2, long var3) {
      int var5 = var2.size();
      Iterator var6 = var2.keySet().iterator();

      while(var6.hasNext()) {
         InventoryItem var7 = (InventoryItem)var2.get(var6.next());
         var1.putUTF(var7.getModule());
         var1.putUTF(var7.getType());
         var1.putLong((long)var7.getID());
         var1.putLong(var3);
         var1.putBoolean(IsoPlayer.getInstance().isEquipped(var7));
         if (var7 instanceof DrainableComboItem) {
            var1.putFloat(((DrainableComboItem)var7).getUsedDelta());
         } else {
            var1.putFloat((float)var7.getCondition());
         }

         var1.putInt(var7.getCount());
         if (var7 instanceof DrainableComboItem) {
            var1.putUTF(Translator.getText("IGUI_ItemCat_Drainable"));
         } else {
            var1.putUTF(var7.getCategory());
         }

         var1.putUTF(var7.getContainer().getType());
         var1.putBoolean(var7.getWorker() != null && var7.getWorker().equals("inInv"));
         if (var7 instanceof InventoryContainer && ((InventoryContainer)var7).getItemContainer() != null && !((InventoryContainer)var7).getItemContainer().getItems().isEmpty()) {
            LinkedHashMap var8 = ((InventoryContainer)var7).getItemContainer().getItems4Admin();
            var5 += var8.size();
            this.sendInventoryPutItems(var1, var8, (long)var7.getID());
         }
      }

      return var5;
   }

   static void receiveRequestInventory(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.SendInventory.doPacket(var3);
      var3.putShort(var2);
      int var4 = var3.bb.position();
      var3.putInt(0);
      LinkedHashMap var5 = IsoPlayer.getInstance().getInventory().getItems4Admin();
      int var6 = instance.sendInventoryPutItems(var3, var5, -1L);
      int var7 = var3.bb.position();
      var3.bb.position(var4);
      var3.putInt(var6);
      var3.bb.position(var7);
      PacketTypes.PacketType.SendInventory.send(connection);
   }

   static void receiveSendInventory(ByteBuffer var0, short var1) {
      int var2 = var0.getInt();
      KahluaTable var3 = LuaManager.platform.newTable();

      for(int var4 = 0; var4 < var2; ++var4) {
         KahluaTable var5 = LuaManager.platform.newTable();
         String var10000 = GameWindow.ReadStringUTF(var0);
         String var6 = var10000 + "." + GameWindow.ReadStringUTF(var0);
         long var7 = var0.getLong();
         long var9 = var0.getLong();
         boolean var11 = var0.get() == 1;
         float var12 = var0.getFloat();
         int var13 = var0.getInt();
         String var14 = GameWindow.ReadStringUTF(var0);
         String var15 = GameWindow.ReadStringUTF(var0);
         boolean var16 = var0.get() == 1;
         var5.rawset("fullType", var6);
         var5.rawset("itemId", var7);
         var5.rawset("isEquip", var11);
         var5.rawset("var", (double)Math.round((double)var12 * 100.0D) / 100.0D);
         var5.rawset("count", var13.makeConcatWithConstants<invokedynamic>(var13));
         var5.rawset("cat", var14);
         var5.rawset("parrentId", var9);
         var5.rawset("hasParrent", var9 != -1L);
         var5.rawset("container", var15);
         var5.rawset("inInv", var16);
         var3.rawset(var3.size() + 1, var5);
      }

      LuaEventManager.triggerEvent("MngInvReceiveItems", var3);
   }

   public static void sendGetItemInvMng(long var0) {
   }

   static void receiveSpawnRegion(ByteBuffer var0, short var1) {
      if (instance.ServerSpawnRegions == null) {
         instance.ServerSpawnRegions = LuaManager.platform.newTable();
      }

      int var2 = var0.getInt();
      KahluaTable var3 = LuaManager.platform.newTable();

      try {
         var3.load((ByteBuffer)var0, 186);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      instance.ServerSpawnRegions.rawset(var2, var3);
   }

   static void receivePlayerConnectLoading(ByteBuffer var0) throws IOException {
      int var1 = var0.position();
      if (!instance.receivePlayerConnectWhileLoading(var0)) {
         var0.position(var1);
         throw new IOException();
      }
   }

   static void receiveClimateManagerPacket(ByteBuffer var0, short var1) {
      ClimateManager var2 = ClimateManager.getInstance();
      if (var2 != null) {
         try {
            var2.receiveClimatePacket(var0, (UdpConnection)null);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   static void receiveServerMap(ByteBuffer var0, short var1) {
      ClientServerMap.receivePacket(var0);
   }

   static void receivePassengerMap(ByteBuffer var0, short var1) {
      PassengerMap.clientReceivePacket(var0);
   }

   static void receiveIsoRegionServerPacket(ByteBuffer var0, short var1) {
      IsoRegions.receiveServerUpdatePacket(var0);
   }

   public static void sendIsoRegionDataRequest() {
      ByteBufferWriter var0 = connection.startPacket();
      PacketTypes.PacketType.IsoRegionClientRequestFullUpdate.doPacket(var0);
      PacketTypes.PacketType.IsoRegionClientRequestFullUpdate.send(connection);
   }

   public void sendSandboxOptionsToServer(SandboxOptions var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.SandboxOptions.doPacket(var2);

      try {
         var1.save(var2.bb);
      } catch (IOException var7) {
         ExceptionLogger.logException(var7);
      } finally {
         PacketTypes.PacketType.SandboxOptions.send(connection);
      }

   }

   static void receiveSandboxOptions(ByteBuffer var0, short var1) {
      try {
         SandboxOptions.instance.load(var0);
         SandboxOptions.instance.applySettings();
         SandboxOptions.instance.toLua();
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }

   }

   static void receiveChunkObjectState(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      short var3 = var0.getShort();
      IsoChunk var4 = IsoWorld.instance.CurrentCell.getChunk(var2, var3);
      if (var4 != null) {
         try {
            var4.loadObjectState(var0);
         } catch (Throwable var6) {
            ExceptionLogger.logException(var6);
         }

      }
   }

   static void receivePlayerLeaveChat(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processLeaveChatPacket(var0);
   }

   static void receiveInitPlayerChat(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processInitPlayerChatPacket(var0);
   }

   static void receiveAddChatTab(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processAddTabPacket(var0);
   }

   static void receiveRemoveChatTab(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processRemoveTabPacket(var0);
   }

   static void receivePlayerNotFound(ByteBuffer var0, short var1) {
      ChatManager.getInstance().processPlayerNotFound();
   }

   public static void sendZombieHelmetFall(IsoPlayer var0, IsoGameCharacter var1, InventoryItem var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.ZombieHelmetFalling.doPacket(var3);
      var3.putByte((byte)var0.PlayerIndex);
      var3.putShort(var1.getOnlineID());
      var3.putUTF(var2.getType());
      PacketTypes.PacketType.ZombieHelmetFalling.send(connection);
   }

   static void receiveZombieHelmetFalling(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      IsoZombie var3 = (IsoZombie)IDToZombieMap.get(var2);
      String var4 = GameWindow.ReadString(var0);
      if (var3 != null && !StringUtils.isNullOrEmpty(var4)) {
         var3.helmetFall(true, var4);
      }
   }

   public static void sendPerks(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.SyncPerks.doPacket(var1);
      var1.putByte((byte)var0.PlayerIndex);
      var1.putInt(var0.getPerkLevel(PerkFactory.Perks.Sneak));
      var1.putInt(var0.getPerkLevel(PerkFactory.Perks.Strength));
      var1.putInt(var0.getPerkLevel(PerkFactory.Perks.Fitness));
      PacketTypes.PacketType.SyncPerks.send(connection);
   }

   static void receiveSyncPerks(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      IsoPlayer var6 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var6 != null && !var6.isLocalPlayer()) {
         var6.remoteSneakLvl = var3;
         var6.remoteStrLvl = var4;
         var6.remoteFitLvl = var5;
      }
   }

   public static void sendWeight(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.SyncWeight.doPacket(var1);
      var1.putByte((byte)var0.PlayerIndex);
      var1.putFloat(var0.getNutrition().getWeight());
      PacketTypes.PacketType.SyncWeight.send(connection);
   }

   static void receiveSyncWeight(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      float var3 = var0.getFloat();
      IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var4 != null && !var4.isLocalPlayer()) {
         var4.getNutrition().setWeight(var3);
      }
   }

   static void receiveGlobalModData(ByteBuffer var0, short var1) {
      GlobalModData.instance.receive(var0);
   }

   public static void sendSafehouseInvite(SafeHouse var0, IsoPlayer var1, String var2) {
      ByteBufferWriter var3 = connection.startPacket();
      PacketTypes.PacketType.SendSafehouseInvite.doPacket(var3);
      var3.putUTF(var0.getTitle());
      var3.putUTF(var1.getUsername());
      var3.putUTF(var2);
      var3.putInt(var0.getX());
      var3.putInt(var0.getY());
      var3.putInt(var0.getW());
      var3.putInt(var0.getH());
      PacketTypes.PacketType.SendSafehouseInvite.send(connection);
   }

   static void receiveSendSafehouseInvite(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      int var4 = var0.getInt();
      int var5 = var0.getInt();
      int var6 = var0.getInt();
      int var7 = var0.getInt();
      SafeHouse var8 = SafeHouse.getSafeHouse(var4, var5, var6, var7);
      LuaEventManager.triggerEvent("ReceiveSafehouseInvite", var8, var3);
   }

   public static void acceptSafehouseInvite(SafeHouse var0, String var1) {
      ByteBufferWriter var2 = connection.startPacket();
      PacketTypes.PacketType.AcceptedSafehouseInvite.doPacket(var2);
      var2.putUTF(var0.getTitle());
      var2.putUTF(var1);
      var2.putUTF(username);
      var2.putInt(var0.getX());
      var2.putInt(var0.getY());
      var2.putInt(var0.getW());
      var2.putInt(var0.getH());
      PacketTypes.PacketType.AcceptedSafehouseInvite.send(connection);
   }

   static void receiveAcceptedSafehouseInvite(ByteBuffer var0, short var1) {
      String var2 = GameWindow.ReadString(var0);
      String var3 = GameWindow.ReadString(var0);
      String var4 = GameWindow.ReadString(var0);
      int var5 = var0.getInt();
      int var6 = var0.getInt();
      int var7 = var0.getInt();
      int var8 = var0.getInt();
      SafeHouse var9 = SafeHouse.getSafeHouse(var5, var6, var7, var8);
      if (var9 != null) {
         var9.addPlayer(var4);
      }

      LuaEventManager.triggerEvent("AcceptedSafehouseInvite", var9.getTitle(), var3);
   }

   public static void sendEquippedRadioFreq(IsoPlayer var0) {
      ByteBufferWriter var1 = connection.startPacket();
      PacketTypes.PacketType.SyncEquippedRadioFreq.doPacket(var1);
      var1.putByte((byte)var0.PlayerIndex);
      var1.putInt(var0.invRadioFreq.size());

      for(int var2 = 0; var2 < var0.invRadioFreq.size(); ++var2) {
         var1.putInt((Integer)var0.invRadioFreq.get(var2));
      }

      PacketTypes.PacketType.SyncEquippedRadioFreq.send(connection);
   }

   static void receiveSyncEquippedRadioFreq(ByteBuffer var0, short var1) {
      short var2 = var0.getShort();
      int var3 = var0.getInt();
      IsoPlayer var4 = (IsoPlayer)IDToPlayerMap.get(var2);
      if (var4 != null) {
         var4.invRadioFreq.clear();

         int var5;
         for(var5 = 0; var5 < var3; ++var5) {
            var4.invRadioFreq.add(var0.getInt());
         }

         System.out.println("SYNC EQUIPPED RADIO FOR PLAYER: " + var4.getDisplayName());

         for(var5 = 0; var5 < var4.invRadioFreq.size(); ++var5) {
            System.out.println(var4.invRadioFreq.get(var5));
         }
      }

   }

   static {
      port = GameServer.DEFAULT_PORT;
      checksum = "";
      checksumValid = false;
      pingsList = new ArrayList();
      loadedCells = new ClientServerMap[4];
      isPaused = false;
      positions = new HashMap(ServerOptions.getInstance().getMaxPlayers());
      MainLoopNetDataQ = new ConcurrentLinkedQueue();
      MainLoopNetData = new ArrayList();
      LoadingMainLoopNetData = new ArrayList();
      DelayedCoopNetData = new ArrayList();
      ServerPredictedAhead = 0.0F;
      IDToPlayerMap = new HashMap();
      IDToZombieMap = new TShortObjectHashMap();
      askPing = false;
      startAuth = null;
      poisonousBerry = null;
      poisonousMushroom = null;
   }

   private static enum RequestState {
      Start,
      RequestDescriptors,
      ReceivedDescriptors,
      RequestMetaGrid,
      ReceivedMetaGrid,
      RequestMapZone,
      ReceivedMapZone,
      RequestPlayerZombieDescriptors,
      ReceivedPlayerZombieDescriptors,
      Complete;

      // $FF: synthetic method
      private static GameClient.RequestState[] $values() {
         return new GameClient.RequestState[]{Start, RequestDescriptors, ReceivedDescriptors, RequestMetaGrid, ReceivedMetaGrid, RequestMapZone, ReceivedMapZone, RequestPlayerZombieDescriptors, ReceivedPlayerZombieDescriptors, Complete};
      }
   }
}
