package zombie.ai;

import java.util.ArrayList;
import java.util.List;
import zombie.GameWindow;
import zombie.network.GameServer;
import zombie.popman.ObjectPool;

public final class KnownBlockedEdges {
   public int x;
   public int y;
   public int z;
   public boolean w;
   public boolean n;
   static final ObjectPool pool = new ObjectPool(KnownBlockedEdges::new);

   public KnownBlockedEdges init(KnownBlockedEdges var1) {
      return this.init(var1.x, var1.y, var1.z, var1.w, var1.n);
   }

   public KnownBlockedEdges init(int var1, int var2, int var3) {
      return this.init(var1, var2, var3, false, false);
   }

   public KnownBlockedEdges init(int var1, int var2, int var3, boolean var4, boolean var5) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
      this.n = var5;
      return this;
   }

   public boolean isBlocked(int var1, int var2) {
      if (this.x > var1 && this.w) {
         return true;
      } else {
         return this.y > var2 && this.n;
      }
   }

   public static KnownBlockedEdges alloc() {
      assert GameServer.bServer || Thread.currentThread() == GameWindow.GameThread;

      return (KnownBlockedEdges)pool.alloc();
   }

   public static void releaseAll(ArrayList var0) {
      assert GameServer.bServer || Thread.currentThread() == GameWindow.GameThread;

      pool.release((List)var0);
   }

   public void release() {
      assert GameServer.bServer || Thread.currentThread() == GameWindow.GameThread;

      pool.release((Object)this);
   }
}
