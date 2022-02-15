package zombie.spnetwork;

import java.nio.ByteBuffer;
import zombie.network.IZomboidPacket;

public final class ZomboidNetData implements IZomboidPacket {
   public short type;
   public short length;
   public ByteBuffer buffer;
   public UdpConnection connection;

   public ZomboidNetData() {
      this.buffer = ByteBuffer.allocate(2048);
   }

   public ZomboidNetData(int var1) {
      this.buffer = ByteBuffer.allocate(var1);
   }

   public void reset() {
      this.type = 0;
      this.length = 0;
      this.buffer.clear();
      this.connection = null;
   }

   public void read(short var1, ByteBuffer var2, UdpConnection var3) {
      this.type = var1;
      this.connection = var3;
      this.buffer.put(var2);
      this.buffer.flip();
   }

   public boolean isConnect() {
      return false;
   }

   public boolean isDisconnect() {
      return false;
   }
}
