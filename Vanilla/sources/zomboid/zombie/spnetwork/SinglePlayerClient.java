package zombie.spnetwork;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.globalObjects.CGlobalObjectNetwork;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.network.TableNetworkUtils;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleManager;

public final class SinglePlayerClient {
   private static final ArrayList MainLoopNetData = new ArrayList();
   public static final UdpEngine udpEngine = new SinglePlayerClient.UdpEngineClient();
   public static final UdpConnection connection;

   public static void addIncoming(short var0, ByteBuffer var1) {
      ZomboidNetData var2;
      if (var1.remaining() > 2048) {
         var2 = ZomboidNetDataPool.instance.getLong(var1.remaining());
      } else {
         var2 = ZomboidNetDataPool.instance.get();
      }

      var2.read(var0, var1, connection);
      synchronized(MainLoopNetData) {
         MainLoopNetData.add(var2);
      }
   }

   public static void update() throws Exception {
      if (!GameClient.bClient) {
         for(short var0 = 0; var0 < IsoPlayer.numPlayers; ++var0) {
            if (IsoPlayer.players[var0] != null) {
               IsoPlayer.players[var0].setOnlineID(var0);
            }
         }

         synchronized(MainLoopNetData) {
            for(int var1 = 0; var1 < MainLoopNetData.size(); ++var1) {
               ZomboidNetData var2 = (ZomboidNetData)MainLoopNetData.get(var1);

               try {
                  mainLoopDealWithNetData(var2);
               } finally {
                  MainLoopNetData.remove(var1--);
               }
            }

         }
      }
   }

   private static void mainLoopDealWithNetData(ZomboidNetData var0) throws Exception {
      ByteBuffer var1 = var0.buffer;

      try {
         PacketTypes.PacketType var2 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var0.type);
         switch(var2) {
         case ClientCommand:
            receiveServerCommand(var1);
            break;
         case GlobalObjects:
            CGlobalObjectNetwork.receive(var1);
            break;
         case ObjectChange:
            receiveObjectChange(var1);
            break;
         default:
            throw new IllegalStateException("Unexpected value: " + var2);
         }
      } finally {
         ZomboidNetDataPool.instance.discard(var0);
      }

   }

   private static void delayPacket(int var0, int var1, int var2) {
   }

   private static IsoPlayer getPlayerByID(int var0) {
      return IsoPlayer.players[var0];
   }

   private static void receiveObjectChange(ByteBuffer var0) {
      byte var1 = var0.get();
      short var2;
      String var3;
      if (var1 == 1) {
         var2 = var0.getShort();
         var3 = GameWindow.ReadString(var0);
         if (Core.bDebug) {
            DebugLog.log("receiveObjectChange " + var3);
         }

         IsoPlayer var4 = getPlayerByID(var2);
         if (var4 != null) {
            var4.loadChange(var3, var0);
         }
      } else if (var1 == 2) {
         var2 = var0.getShort();
         var3 = GameWindow.ReadString(var0);
         if (Core.bDebug) {
            DebugLog.log("receiveObjectChange " + var3);
         }

         BaseVehicle var12 = VehicleManager.instance.getVehicleByID(var2);
         if (var12 != null) {
            var12.loadChange(var3, var0);
         } else if (Core.bDebug) {
            DebugLog.log("receiveObjectChange: unknown vehicle id=" + var2);
         }
      } else {
         int var5;
         String var6;
         IsoGridSquare var7;
         int var10;
         int var11;
         int var13;
         if (var1 == 3) {
            var10 = var0.getInt();
            var11 = var0.getInt();
            var13 = var0.getInt();
            var5 = var0.getInt();
            var6 = GameWindow.ReadString(var0);
            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange " + var6);
            }

            var7 = IsoWorld.instance.CurrentCell.getGridSquare(var10, var11, var13);
            if (var7 == null) {
               delayPacket(var10, var11, var13);
               return;
            }

            for(int var8 = 0; var8 < var7.getWorldObjects().size(); ++var8) {
               IsoWorldInventoryObject var9 = (IsoWorldInventoryObject)var7.getWorldObjects().get(var8);
               if (var9.getItem() != null && var9.getItem().getID() == var5) {
                  var9.loadChange(var6, var0);
                  return;
               }
            }

            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange: itemID=" + var5 + " is invalid x,y,z=" + var10 + "," + var11 + "," + var13);
            }
         } else {
            var10 = var0.getInt();
            var11 = var0.getInt();
            var13 = var0.getInt();
            var5 = var0.getInt();
            var6 = GameWindow.ReadString(var0);
            if (Core.bDebug) {
               DebugLog.log("receiveObjectChange " + var6);
            }

            var7 = IsoWorld.instance.CurrentCell.getGridSquare(var10, var11, var13);
            if (var7 == null) {
               delayPacket(var10, var11, var13);
               return;
            }

            if (var5 >= 0 && var5 < var7.getObjects().size()) {
               IsoObject var14 = (IsoObject)var7.getObjects().get(var5);
               var14.loadChange(var6, var0);
            } else if (Core.bDebug) {
               DebugLog.log("receiveObjectChange: index=" + var5 + " is invalid x,y,z=" + var10 + "," + var11 + "," + var13);
            }
         }
      }

   }

   public static void sendClientCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
      ByteBufferWriter var4 = connection.startPacket();
      PacketTypes.PacketType.ClientCommand.doPacket(var4);
      var4.putByte((byte)(var0 != null ? var0.PlayerIndex : -1));
      var4.putUTF(var1);
      var4.putUTF(var2);
      if (var3 != null && !var3.isEmpty()) {
         var4.putByte((byte)1);

         try {
            KahluaTableIterator var5 = var3.iterator();

            while(var5.advance()) {
               if (!TableNetworkUtils.canSave(var5.getKey(), var5.getValue())) {
                  Object var10000 = var5.getKey();
                  DebugLog.log("ERROR: sendClientCommand: can't save key,value=" + var10000 + "," + var5.getValue());
               }
            }

            TableNetworkUtils.save(var3, var4.bb);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      } else {
         var4.putByte((byte)0);
      }

      connection.endPacketImmediate();
   }

   private static void receiveServerCommand(ByteBuffer var0) {
      String var1 = GameWindow.ReadString(var0);
      String var2 = GameWindow.ReadString(var0);
      boolean var3 = var0.get() == 1;
      KahluaTable var4 = null;
      if (var3) {
         var4 = LuaManager.platform.newTable();

         try {
            TableNetworkUtils.load(var4, var0);
         } catch (Exception var6) {
            var6.printStackTrace();
            return;
         }
      }

      LuaEventManager.triggerEvent("OnServerCommand", var1, var2, var4);
   }

   public static void Reset() {
      Iterator var0 = MainLoopNetData.iterator();

      while(var0.hasNext()) {
         ZomboidNetData var1 = (ZomboidNetData)var0.next();
         ZomboidNetDataPool.instance.discard(var1);
      }

      MainLoopNetData.clear();
   }

   static {
      connection = new UdpConnection(udpEngine);
   }

   private static final class UdpEngineClient extends UdpEngine {
      public void Send(ByteBuffer var1) {
         SinglePlayerServer.udpEngine.Receive(var1);
      }

      public void Receive(ByteBuffer var1) {
         int var2 = var1.get() & 255;
         short var3 = var1.getShort();
         SinglePlayerClient.addIncoming(var3, var1);
      }
   }
}
