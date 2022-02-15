package zombie.spnetwork;

import java.nio.ByteBuffer;

public abstract class UdpEngine {
   public abstract void Send(ByteBuffer var1);

   public abstract void Receive(ByteBuffer var1);
}
