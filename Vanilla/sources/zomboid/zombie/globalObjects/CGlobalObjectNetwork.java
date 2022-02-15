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
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.TableNetworkUtils;
import zombie.spnetwork.SinglePlayerClient;

public final class CGlobalObjectNetwork {
   private static final ByteBuffer BYTE_BUFFER = ByteBuffer.allocate(1048576);
   private static final ByteBufferWriter BYTE_BUFFER_WRITER;
   private static KahluaTable tempTable;

   public static void receive(ByteBuffer var0) throws IOException {
      byte var1 = var0.get();
      switch(var1) {
      case 1:
         receiveServerCommand(var0);
      case 2:
      default:
         break;
      case 3:
         receiveNewLuaObjectAt(var0);
         break;
      case 4:
         receiveRemoveLuaObjectAt(var0);
         break;
      case 5:
         receiveUpdateLuaObjectAt(var0);
      }

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
            ExceptionLogger.logException(var6);
            return;
         }
      }

      CGlobalObjects.receiveServerCommand(var1, var2, var4);
   }

   private static void receiveNewLuaObjectAt(ByteBuffer var0) throws IOException {
      String var1 = GameWindow.ReadStringUTF(var0);
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      byte var4 = var0.get();
      if (tempTable == null) {
         tempTable = LuaManager.platform.newTable();
      }

      TableNetworkUtils.load(tempTable, var0);
      CGlobalObjectSystem var5 = CGlobalObjects.getSystemByName(var1);
      if (var5 != null) {
         var5.receiveNewLuaObjectAt(var2, var3, var4, tempTable);
      }
   }

   private static void receiveRemoveLuaObjectAt(ByteBuffer var0) {
      String var1 = GameWindow.ReadStringUTF(var0);
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      byte var4 = var0.get();
      CGlobalObjectSystem var5 = CGlobalObjects.getSystemByName(var1);
      if (var5 != null) {
         var5.receiveRemoveLuaObjectAt(var2, var3, var4);
      }
   }

   private static void receiveUpdateLuaObjectAt(ByteBuffer var0) throws IOException {
      String var1 = GameWindow.ReadStringUTF(var0);
      int var2 = var0.getInt();
      int var3 = var0.getInt();
      byte var4 = var0.get();
      if (tempTable == null) {
         tempTable = LuaManager.platform.newTable();
      }

      TableNetworkUtils.load(tempTable, var0);
      CGlobalObjectSystem var5 = CGlobalObjects.getSystemByName(var1);
      if (var5 != null) {
         var5.receiveUpdateLuaObjectAt(var2, var3, var4, tempTable);
      }
   }

   private static void sendPacket(ByteBuffer var0) {
      if (GameServer.bServer) {
         throw new IllegalStateException("can't call this method on the server");
      } else {
         ByteBufferWriter var1;
         if (GameClient.bClient) {
            var1 = GameClient.connection.startPacket();
            var0.flip();
            var1.bb.put(var0);
            PacketTypes.PacketType.GlobalObjects.send(GameClient.connection);
         } else {
            var1 = SinglePlayerClient.connection.startPacket();
            var0.flip();
            var1.bb.put(var0);
            SinglePlayerClient.connection.endPacketImmediate();
         }

      }
   }

   public static void sendClientCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
      BYTE_BUFFER.clear();
      writeClientCommand(var0, var1, var2, var3, BYTE_BUFFER_WRITER);
      sendPacket(BYTE_BUFFER);
   }

   private static void writeClientCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3, ByteBufferWriter var4) {
      PacketTypes.PacketType.GlobalObjects.doPacket(var4);
      var4.putByte((byte)(var0 != null ? var0.PlayerIndex : -1));
      var4.putByte((byte)2);
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
            ExceptionLogger.logException(var6);
         }
      } else {
         var4.putByte((byte)0);
      }

   }

   public static void Reset() {
      if (tempTable != null) {
         tempTable.wipe();
         tempTable = null;
      }

   }

   static {
      BYTE_BUFFER_WRITER = new ByteBufferWriter(BYTE_BUFFER);
   }
}
