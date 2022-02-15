package zombie.util;

import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import zombie.util.list.PZArrayUtil;

public final class Pool {
   private final Supplier m_allocator;
   private final ThreadLocal m_stacks = ThreadLocal.withInitial(Pool.PoolStacks::new);

   public Pool(Supplier var1) {
      this.m_allocator = var1;
   }

   public final IPooledObject alloc() {
      Supplier var1 = this.m_allocator;
      Pool.PoolStacks var2 = (Pool.PoolStacks)this.m_stacks.get();
      THashSet var3 = var2.inUse;
      List var4 = var2.released;
      IPooledObject var5;
      if (!var4.isEmpty()) {
         var5 = (IPooledObject)var4.remove(var4.size() - 1);
      } else {
         var5 = (IPooledObject)var1.get();
         if (var5 == null) {
            throw new NullPointerException("Allocator returned a nullPtr. This is not allowed.");
         }

         var5.setPool(this);
      }

      var5.setFree(false);
      var3.add(var5);
      return var5;
   }

   public final void release(IPooledObject var1) {
      Pool.PoolStacks var2 = (Pool.PoolStacks)this.m_stacks.get();
      THashSet var3 = var2.inUse;
      List var4 = var2.released;
      if (var1.getPool() != this) {
         throw new UnsupportedOperationException("Cannot release item. Not owned by this pool.");
      } else if (var1.isFree()) {
         throw new UnsupportedOperationException("Cannot release item. Already released.");
      } else {
         var3.remove(var1);
         var1.setFree(true);
         var4.add(var1);
         var1.onReleased();
      }
   }

   public static Object tryRelease(Object var0) {
      IPooledObject var1 = (IPooledObject)Type.tryCastTo(var0, IPooledObject.class);
      if (var1 != null && !var1.isFree()) {
         var1.release();
      }

      return null;
   }

   public static IPooledObject tryRelease(IPooledObject var0) {
      if (var0 != null && !var0.isFree()) {
         var0.release();
      }

      return null;
   }

   public static IPooledObject[] tryRelease(IPooledObject[] var0) {
      PZArrayUtil.forEach((Object[])var0, Pool::tryRelease);
      return null;
   }

   private static final class PoolStacks {
      final THashSet inUse = new THashSet();
      final List released = new ArrayList();

      PoolStacks() {
         this.inUse.setAutoCompactionFactor(0.0F);
      }
   }
}
