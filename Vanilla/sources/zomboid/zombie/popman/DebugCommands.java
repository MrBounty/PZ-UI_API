package zombie.popman;

import zombie.core.network.ByteBufferWriter;
import zombie.network.GameClient;
import zombie.network.PacketTypes;

final class DebugCommands {
   protected static final byte PKT_LOADED = 1;
   protected static final byte PKT_REPOP = 2;
   protected static final byte PKT_SPAWN_TIME_TO_ZERO = 3;
   protected static final byte PKT_CLEAR_ZOMBIES = 4;
   protected static final byte PKT_SPAWN_NOW = 5;

   private static native void n_debugCommand(int var0, int var1, int var2);

   public void SpawnTimeToZero(int var1, int var2) {
      if (ZombiePopulationManager.instance.bClient) {
         ByteBufferWriter var3 = GameClient.connection.startPacket();
         PacketTypes.PacketType.KeepAlive.doPacket(var3);
         var3.bb.put((byte)3);
         var3.bb.putShort((short)var1);
         var3.bb.putShort((short)var2);
         PacketTypes.PacketType.KeepAlive.send(GameClient.connection);
      } else {
         n_debugCommand(3, var1, var2);
      }
   }

   public void ClearZombies(int var1, int var2) {
      if (ZombiePopulationManager.instance.bClient) {
         ByteBufferWriter var3 = GameClient.connection.startPacket();
         PacketTypes.PacketType.KeepAlive.doPacket(var3);
         var3.bb.put((byte)4);
         var3.bb.putShort((short)var1);
         var3.bb.putShort((short)var2);
         PacketTypes.PacketType.KeepAlive.send(GameClient.connection);
      } else {
         n_debugCommand(4, var1, var2);
      }
   }

   public void SpawnNow(int var1, int var2) {
      if (ZombiePopulationManager.instance.bClient) {
         ByteBufferWriter var3 = GameClient.connection.startPacket();
         PacketTypes.PacketType.KeepAlive.doPacket(var3);
         var3.bb.put((byte)5);
         var3.bb.putShort((short)var1);
         var3.bb.putShort((short)var2);
         PacketTypes.PacketType.KeepAlive.send(GameClient.connection);
      } else {
         n_debugCommand(5, var1, var2);
      }
   }
}
