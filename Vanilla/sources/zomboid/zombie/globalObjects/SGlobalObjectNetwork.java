package zombie.globalObjects;

import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.TableNetworkUtils;
import zombie.spnetwork.SinglePlayerServer;

public final class SGlobalObjectNetwork {
   public static final byte PACKET_ServerCommand = 1;
   public static final byte PACKET_ClientCommand = 2;
   public static final byte PACKET_NewLuaObjectAt = 3;
   public static final byte PACKET_RemoveLuaObjectAt = 4;
   public static final byte PACKET_UpdateLuaObjectAt = 5;
   private static final ByteBuffer BYTE_BUFFER = ByteBuffer.allocate(1048576);
   private static final ByteBufferWriter BYTE_BUFFER_WRITER;

   public static void receive(ByteBuffer var0, IsoPlayer var1) {
      byte var2 = var0.get();
      switch(var2) {
      case 2:
         receiveClientCommand(var0, var1);
      default:
      }
   }

   private static void sendPacket(ByteBuffer var0) {
      int var1;
      ByteBufferWriter var3;
      if (GameServer.bServer) {
         for(var1 = 0; var1 < GameServer.udpEngine.connections.size(); ++var1) {
            UdpConnection var2 = (UdpConnection)GameServer.udpEngine.connections.get(var1);
            var3 = var2.startPacket();
            var0.flip();
            var3.bb.put(var0);
            var2.endPacketImmediate();
         }
      } else {
         if (GameClient.bClient) {
            throw new IllegalStateException("can't call this method on the client");
         }

         for(var1 = 0; var1 < SinglePlayerServer.udpEngine.connections.size(); ++var1) {
            zombie.spnetwork.UdpConnection var4 = (zombie.spnetwork.UdpConnection)SinglePlayerServer.udpEngine.connections.get(var1);
            var3 = var4.startPacket();
            var0.flip();
            var3.bb.put(var0);
            var4.endPacketImmediate();
         }
      }

   }

   private static void writeServerCommand(String var0, String var1, KahluaTable var2, ByteBufferWriter var3) {
      PacketTypes.PacketType.GlobalObjects.doPacket(var3);
      var3.putByte((byte)1);
      var3.putUTF(var0);
      var3.putUTF(var1);
      if (var2 != null && !var2.isEmpty()) {
         var3.putByte((byte)1);

         try {
            KahluaTableIterator var4 = var2.iterator();

            while(var4.advance()) {
               if (!TableNetworkUtils.canSave(var4.getKey(), var4.getValue())) {
                  Object var10000 = var4.getKey();
                  DebugLog.log("ERROR: sendServerCommand: can't save key,value=" + var10000 + "," + var4.getValue());
               }
            }

            TableNetworkUtils.save(var2, var3.bb);
         } catch (IOException var5) {
            ExceptionLogger.logException(var5);
         }
      } else {
         var3.putByte((byte)0);
      }

   }

   public static void sendServerCommand(String var0, String var1, KahluaTable var2) {
      BYTE_BUFFER.clear();
      writeServerCommand(var0, var1, var2, BYTE_BUFFER_WRITER);
      sendPacket(BYTE_BUFFER);
   }

   public static void addGlobalObjectOnClient(SGlobalObject var0) throws IOException {
      BYTE_BUFFER.clear();
      ByteBufferWriter var1 = BYTE_BUFFER_WRITER;
      PacketTypes.PacketType.GlobalObjects.doPacket(var1);
      var1.putByte((byte)3);
      var1.putUTF(var0.system.name);
      var1.putInt(var0.getX());
      var1.putInt(var0.getY());
      var1.putByte((byte)var0.getZ());
      SGlobalObjectSystem var2 = (SGlobalObjectSystem)var0.system;
      TableNetworkUtils.saveSome(var0.getModData(), var1.bb, var2.objectSyncKeys);
      sendPacket(BYTE_BUFFER);
   }

   public static void removeGlobalObjectOnClient(GlobalObject var0) {
      BYTE_BUFFER.clear();
      ByteBufferWriter var1 = BYTE_BUFFER_WRITER;
      PacketTypes.PacketType.GlobalObjects.doPacket(var1);
      var1.putByte((byte)4);
      var1.putUTF(var0.system.name);
      var1.putInt(var0.getX());
      var1.putInt(var0.getY());
      var1.putByte((byte)var0.getZ());
      sendPacket(BYTE_BUFFER);
   }

   public static void updateGlobalObjectOnClient(SGlobalObject var0) throws IOException {
      BYTE_BUFFER.clear();
      ByteBufferWriter var1 = BYTE_BUFFER_WRITER;
      PacketTypes.PacketType.GlobalObjects.doPacket(var1);
      var1.putByte((byte)5);
      var1.putUTF(var0.system.name);
      var1.putInt(var0.getX());
      var1.putInt(var0.getY());
      var1.putByte((byte)var0.getZ());
      SGlobalObjectSystem var2 = (SGlobalObjectSystem)var0.system;
      TableNetworkUtils.saveSome(var0.getModData(), var1.bb, var2.objectSyncKeys);
      sendPacket(BYTE_BUFFER);
   }

   private static void receiveClientCommand(ByteBuffer var0, IsoPlayer var1) {
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

      SGlobalObjects.receiveClientCommand(var2, var3, var1, var5);
   }

   static {
      BYTE_BUFFER_WRITER = new ByteBufferWriter(BYTE_BUFFER);
   }
}
