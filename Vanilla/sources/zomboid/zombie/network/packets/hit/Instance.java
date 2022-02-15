package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.core.network.ByteBufferWriter;
import zombie.network.packets.INetworkPacket;

public abstract class Instance implements INetworkPacket {
   protected short ID;

   public void set(short var1) {
      this.ID = var1;
   }

   public void parse(ByteBuffer var1) {
      this.ID = var1.getShort();
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.ID);
   }

   public boolean isConsistent() {
      return this.ID != -1;
   }

   public String getDescription() {
      return "ID=" + this.ID;
   }
}
