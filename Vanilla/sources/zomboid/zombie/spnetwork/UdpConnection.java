package zombie.spnetwork;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;

public final class UdpConnection {
   final UdpEngine engine;
   private final Lock bufferLock = new ReentrantLock();
   private final ByteBuffer bb = ByteBuffer.allocate(1000000);
   private final ByteBufferWriter bbw;
   public final IsoPlayer[] players;

   public UdpConnection(UdpEngine var1) {
      this.bbw = new ByteBufferWriter(this.bb);
      this.players = IsoPlayer.players;
      this.engine = var1;
   }

   public boolean ReleventTo(float var1, float var2) {
      return true;
   }

   public ByteBufferWriter startPacket() {
      this.bufferLock.lock();
      this.bb.clear();
      return this.bbw;
   }

   public void endPacketImmediate() {
      this.bb.flip();
      this.engine.Send(this.bb);
      this.bufferLock.unlock();
   }

   public void cancelPacket() {
      this.bufferLock.unlock();
   }
}
