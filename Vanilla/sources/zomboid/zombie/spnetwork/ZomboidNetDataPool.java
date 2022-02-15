package zombie.spnetwork;

import java.util.ArrayDeque;

public final class ZomboidNetDataPool {
   public static ZomboidNetDataPool instance = new ZomboidNetDataPool();
   private final ArrayDeque Pool = new ArrayDeque();

   public ZomboidNetData get() {
      synchronized(this.Pool) {
         return this.Pool.isEmpty() ? new ZomboidNetData() : (ZomboidNetData)this.Pool.pop();
      }
   }

   public void discard(ZomboidNetData var1) {
      var1.reset();
      if (var1.buffer.capacity() == 2048) {
         synchronized(this.Pool) {
            this.Pool.add(var1);
         }
      }

   }

   public ZomboidNetData getLong(int var1) {
      return new ZomboidNetData(var1);
   }
}
