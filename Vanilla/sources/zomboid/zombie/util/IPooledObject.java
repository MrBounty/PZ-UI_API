package zombie.util;

import java.util.List;

public interface IPooledObject {
   Pool getPool();

   void setPool(Pool var1);

   void release();

   boolean isFree();

   void setFree(boolean var1);

   default void onReleased() {
   }

   static void release(IPooledObject[] var0) {
      int var1 = 0;

      for(int var2 = var0.length; var1 < var2; ++var1) {
         Pool.tryRelease(var0[var1]);
      }

   }

   static void tryReleaseAndBlank(IPooledObject[] var0) {
      if (var0 != null) {
         releaseAndBlank(var0);
      }

   }

   static void releaseAndBlank(IPooledObject[] var0) {
      int var1 = 0;

      for(int var2 = var0.length; var1 < var2; ++var1) {
         var0[var1] = Pool.tryRelease(var0[var1]);
      }

   }

   static void release(List var0) {
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         Pool.tryRelease((IPooledObject)var0.get(var1));
      }

      var0.clear();
   }
}
