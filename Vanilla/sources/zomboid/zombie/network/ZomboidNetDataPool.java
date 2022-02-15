package zombie.network;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ZomboidNetDataPool {
   public static final ZomboidNetDataPool instance = new ZomboidNetDataPool();
   final ConcurrentLinkedQueue Pool = new ConcurrentLinkedQueue();

   public ZomboidNetData get() {
      ZomboidNetData var1 = (ZomboidNetData)this.Pool.poll();
      return var1 == null ? new ZomboidNetData() : var1;
   }

   public void discard(ZomboidNetData var1) {
      var1.reset();
      if (var1.buffer.capacity() == 2048) {
         this.Pool.add(var1);
      }

   }

   public ZomboidNetData getLong(int var1) {
      return new ZomboidNetData(var1);
   }
}
