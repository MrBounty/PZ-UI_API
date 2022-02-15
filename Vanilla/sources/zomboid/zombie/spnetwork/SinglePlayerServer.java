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
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.globalObjects.SGlobalObjectNetwork;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.network.TableNetworkUtils;
import zombie.vehicles.BaseVehicle;

public final class SinglePlayerServer {
   private static final ArrayList MainLoopNetData = new ArrayList();
   public static final SinglePlayerServer.UdpEngineServer udpEngine = new SinglePlayerServer.UdpEngineServer();

   public static void addIncoming(short var0, ByteBuffer var1, UdpConnection var2) {
      ZomboidNetData var3;
      if (var1.remaining() > 2048) {
         var3 = ZomboidNetDataPool.instance.getLong(var1.remaining());
      } else {
         var3 = ZomboidNetDataPool.instance.get();
      }

      var3.read(var0, var1, var2);
      synchronized(MainLoopNetData) {
         MainLoopNetData.add(var3);
      }
   }

   private static void sendObjectChange(IsoObject var0, String var1, KahluaTable var2, UdpConnection var3) {
      if (var0.getSquare() != null) {
         ByteBufferWriter var4 = var3.startPacket();
         PacketTypes.PacketType.ObjectChange.doPacket(var4);
         if (var0 instanceof IsoPlayer) {
            var4.putByte((byte)1);
            var4.putShort(((IsoPlayer)var0).OnlineID);
         } else if (var0 instanceof BaseVehicle) {
            var4.putByte((byte)2);
            var4.putShort(((BaseVehicle)var0).getId());
         } else if (var0 instanceof IsoWorldInventoryObject) {
            var4.putByte((byte)3);
            var4.putInt(var0.getSquare().getX());
            var4.putInt(var0.getSquare().getY());
            var4.putInt(var0.getSquare().getZ());
            var4.putInt(((IsoWorldInventoryObject)var0).getItem().getID());
         } else {
            var4.putByte((byte)0);
            var4.putInt(var0.getSquare().getX());
            var4.putInt(var0.getSquare().getY());
            var4.putInt(var0.getSquare().getZ());
            var4.putInt(var0.getSquare().getObjects().indexOf(var0));
         }

         var4.putUTF(var1);
         var0.saveChange(var1, var2, var4.bb);
         var3.endPacketImmediate();
      }
   }

   public static void sendObjectChange(IsoObject var0, String var1, KahluaTable var2) {
      if (var0 != null) {
         for(int var3 = 0; var3 < udpEngine.connections.size(); ++var3) {
            UdpConnection var4 = (UdpConnection)udpEngine.connections.get(var3);
            if (var4.ReleventTo(var0.getX(), var0.getY())) {
               sendObjectChange(var0, var1, var2, var4);
            }
         }

      }
   }

   public static void sendObjectChange(IsoObject var0, String var1, Object... var2) {
      if (var2.length == 0) {
         sendObjectChange(var0, var1, (KahluaTable)null);
      } else if (var2.length % 2 == 0) {
         KahluaTable var3 = LuaManager.platform.newTable();

         for(int var4 = 0; var4 < var2.length; var4 += 2) {
            Object var5 = var2[var4 + 1];
            if (var5 instanceof Float) {
               var3.rawset(var2[var4], ((Float)var5).doubleValue());
            } else if (var5 instanceof Integer) {
               var3.rawset(var2[var4], ((Integer)var5).doubleValue());
            } else if (var5 instanceof Short) {
               var3.rawset(var2[var4], ((Short)var5).doubleValue());
            } else {
               var3.rawset(var2[var4], var5);
            }
         }

         sendObjectChange(var0, var1, var3);
      }
   }

   public static void sendServerCommand(String var0, String var1, KahluaTable var2, UdpConnection var3) {
      ByteBufferWriter var4 = var3.startPacket();
      PacketTypes.PacketType.ClientCommand.doPacket(var4);
      var4.putUTF(var0);
      var4.putUTF(var1);
      if (var2 != null && !var2.isEmpty()) {
         var4.putByte((byte)1);

         try {
            KahluaTableIterator var5 = var2.iterator();

            while(var5.advance()) {
               if (!TableNetworkUtils.canSave(var5.getKey(), var5.getValue())) {
                  Object var10000 = var5.getKey();
                  DebugLog.log("ERROR: sendServerCommand: can't save key,value=" + var10000 + "," + var5.getValue());
               }
            }

            TableNetworkUtils.save(var2, var4.bb);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      } else {
         var4.putByte((byte)0);
      }

      var3.endPacketImmediate();
   }

   public static void sendServerCommand(String var0, String var1, KahluaTable var2) {
      for(int var3 = 0; var3 < udpEngine.connections.size(); ++var3) {
         UdpConnection var4 = (UdpConnection)udpEngine.connections.get(var3);
         sendServerCommand(var0, var1, var2, var4);
      }

   }

   public static void update() {
      if (!GameClient.bClient) {
         for(short var0 = 0; var0 < IsoPlayer.numPlayers; ++var0) {
            if (IsoPlayer.players[var0] != null) {
               IsoPlayer.players[var0].setOnlineID(var0);
            }
         }

         synchronized(MainLoopNetData) {
            for(int var1 = 0; var1 < MainLoopNetData.size(); ++var1) {
               ZomboidNetData var2 = (ZomboidNetData)MainLoopNetData.get(var1);
               mainLoopDealWithNetData(var2);
               MainLoopNetData.remove(var1--);
            }

         }
      }
   }

   private static void mainLoopDealWithNetData(ZomboidNetData var0) {
      ByteBuffer var1 = var0.buffer;

      try {
         PacketTypes.PacketType var2 = (PacketTypes.PacketType)PacketTypes.packetTypes.get(var0.type);
         switch(var2) {
         case ClientCommand:
            receiveClientCommand(var1, var0.connection);
            break;
         case GlobalObjects:
            receiveGlobalObjects(var1, var0.connection);
         }
      } finally {
         ZomboidNetDataPool.instance.discard(var0);
      }

   }

   private static IsoPlayer getAnyPlayerFromConnection(UdpConnection var0) {
      for(int var1 = 0; var1 < 4; ++var1) {
         if (var0.players[var1] != null) {
            return var0.players[var1];
         }
      }

      return null;
   }

   private static IsoPlayer getPlayerFromConnection(UdpConnection var0, int var1) {
      return var1 >= 0 && var1 < 4 ? var0.players[var1] : null;
   }

   private static void receiveClientCommand(ByteBuffer var0, UdpConnection var1) {
      byte var2 = var0.get();
      String var3 = GameWindow.ReadString(var0);
      String var4 = GameWindow.ReadString(var0);
      boolean var5 = var0.get() == 1;
      KahluaTable var6 = null;
      if (var5) {
         var6 = LuaManager.platform.newTable();

         try {
            TableNetworkUtils.load(var6, var0);
         } catch (Exception var8) {
            var8.printStackTrace();
            return;
         }
      }

      IsoPlayer var7 = getPlayerFromConnection(var1, var2);
      if (var2 == -1) {
         var7 = getAnyPlayerFromConnection(var1);
      }

      if (var7 == null) {
         DebugLog.log("receiveClientCommand: player is null");
      } else {
         LuaEventManager.triggerEvent("OnClientCommand", var3, var4, var7, var6);
      }
   }

   private static void receiveGlobalObjects(ByteBuffer var0, UdpConnection var1) {
      byte var2 = var0.get();
      IsoPlayer var3 = getPlayerFromConnection(var1, var2);
      if (var2 == -1) {
         var3 = getAnyPlayerFromConnection(var1);
      }

      if (var3 == null) {
         DebugLog.log("receiveGlobalObjects: player is null");
      } else {
         SGlobalObjectNetwork.receive(var0, var3);
      }
   }

   public static void Reset() {
      Iterator var0 = MainLoopNetData.iterator();

      while(var0.hasNext()) {
         ZomboidNetData var1 = (ZomboidNetData)var0.next();
         ZomboidNetDataPool.instance.discard(var1);
      }

      MainLoopNetData.clear();
   }

   public static final class UdpEngineServer extends UdpEngine {
      public final ArrayList connections = new ArrayList();

      UdpEngineServer() {
         this.connections.add(new UdpConnection(this));
      }

      public void Send(ByteBuffer var1) {
         SinglePlayerClient.udpEngine.Receive(var1);
      }

      public void Receive(ByteBuffer var1) {
         int var2 = var1.get() & 255;
         short var3 = var1.getShort();
         SinglePlayerServer.addIncoming(var3, var1, (UdpConnection)SinglePlayerServer.udpEngine.connections.get(0));
      }
   }
}
