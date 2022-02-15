package zombie.core.utils;

import java.util.ArrayList;

public final class DirectBufferAllocator {
   private static final Object LOCK = "DirectBufferAllocator.LOCK";
   private static final ArrayList ALL = new ArrayList();

   public static WrappedBuffer allocate(int var0) {
      synchronized(LOCK) {
         destroyDisposed();
         WrappedBuffer var2 = new WrappedBuffer(var0);
         ALL.add(var2);
         return var2;
      }
   }

   private static void destroyDisposed() {
      synchronized(LOCK) {
         for(int var1 = ALL.size() - 1; var1 >= 0; --var1) {
            WrappedBuffer var2 = (WrappedBuffer)ALL.get(var1);
            if (var2.isDisposed()) {
               ALL.remove(var1);
            }
         }

      }
   }

   public static long getBytesAllocated() {
      synchronized(LOCK) {
         destroyDisposed();
         long var1 = 0L;

         for(int var3 = 0; var3 < ALL.size(); ++var3) {
            WrappedBuffer var4 = (WrappedBuffer)ALL.get(var3);
            if (!var4.isDisposed()) {
               var1 += (long)var4.capacity();
            }
         }

         return var1;
      }
   }
}
