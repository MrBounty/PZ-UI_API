package zombie.core.utils;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BufferUtils {
   private static boolean trackDirectMemory = false;
   private static final ReferenceQueue removeCollected = new ReferenceQueue();
   private static final ConcurrentHashMap trackedBuffers = new ConcurrentHashMap();
   static BufferUtils.ClearReferences cleanupThread;
   private static final AtomicBoolean loadedMethods = new AtomicBoolean(false);
   private static Method cleanerMethod = null;
   private static Method cleanMethod = null;
   private static Method viewedBufferMethod = null;
   private static Method freeMethod = null;

   public static void setTrackDirectMemoryEnabled(boolean var0) {
      trackDirectMemory = var0;
   }

   private static void onBufferAllocated(Buffer var0) {
      if (trackDirectMemory) {
         if (cleanupThread == null) {
            cleanupThread = new BufferUtils.ClearReferences();
            cleanupThread.start();
         }

         BufferUtils.BufferInfo var1;
         if (var0 instanceof ByteBuffer) {
            var1 = new BufferUtils.BufferInfo(ByteBuffer.class, var0.capacity(), var0, removeCollected);
            trackedBuffers.put(var1, var1);
         } else if (var0 instanceof FloatBuffer) {
            var1 = new BufferUtils.BufferInfo(FloatBuffer.class, var0.capacity() * 4, var0, removeCollected);
            trackedBuffers.put(var1, var1);
         } else if (var0 instanceof IntBuffer) {
            var1 = new BufferUtils.BufferInfo(IntBuffer.class, var0.capacity() * 4, var0, removeCollected);
            trackedBuffers.put(var1, var1);
         } else if (var0 instanceof ShortBuffer) {
            var1 = new BufferUtils.BufferInfo(ShortBuffer.class, var0.capacity() * 2, var0, removeCollected);
            trackedBuffers.put(var1, var1);
         } else if (var0 instanceof DoubleBuffer) {
            var1 = new BufferUtils.BufferInfo(DoubleBuffer.class, var0.capacity() * 8, var0, removeCollected);
            trackedBuffers.put(var1, var1);
         }
      }

   }

   public static void printCurrentDirectMemory(StringBuilder var0) {
      long var1 = 0L;
      long var3 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      boolean var5 = var0 == null;
      if (var0 == null) {
         var0 = new StringBuilder();
      }

      if (trackDirectMemory) {
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         int var10 = 0;
         int var11 = 0;
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;
         Iterator var16 = trackedBuffers.values().iterator();

         while(var16.hasNext()) {
            BufferUtils.BufferInfo var17 = (BufferUtils.BufferInfo)var16.next();
            if (var17.type == ByteBuffer.class) {
               var1 += (long)var17.size;
               var12 += var17.size;
               ++var7;
            } else if (var17.type == FloatBuffer.class) {
               var1 += (long)var17.size;
               var11 += var17.size;
               ++var6;
            } else if (var17.type == IntBuffer.class) {
               var1 += (long)var17.size;
               var13 += var17.size;
               ++var8;
            } else if (var17.type == ShortBuffer.class) {
               var1 += (long)var17.size;
               var14 += var17.size;
               ++var9;
            } else if (var17.type == DoubleBuffer.class) {
               var1 += (long)var17.size;
               var15 += var17.size;
               ++var10;
            }
         }

         var0.append("Existing buffers: ").append(trackedBuffers.size()).append("\n");
         var0.append("(b: ").append(var7).append("  f: ").append(var6).append("  i: ").append(var8).append("  s: ").append(var9).append("  d: ").append(var10).append(")").append("\n");
         var0.append("Total   heap memory held: ").append(var3 / 1024L).append("kb\n");
         var0.append("Total direct memory held: ").append(var1 / 1024L).append("kb\n");
         var0.append("(b: ").append(var12 / 1024).append("kb  f: ").append(var11 / 1024).append("kb  i: ").append(var13 / 1024).append("kb  s: ").append(var14 / 1024).append("kb  d: ").append(var15 / 1024).append("kb)").append("\n");
      } else {
         var0.append("Total   heap memory held: ").append(var3 / 1024L).append("kb\n");
         var0.append("Only heap memory available, if you want to monitor direct memory use BufferUtils.setTrackDirectMemoryEnabled(true) during initialization.").append("\n");
      }

      if (var5) {
         System.out.println(var0.toString());
      }

   }

   private static Method loadMethod(String var0, String var1) {
      try {
         Method var2 = Class.forName(var0).getMethod(var1);
         var2.setAccessible(true);
         return var2;
      } catch (SecurityException | ClassNotFoundException | NoSuchMethodException var3) {
         return null;
      }
   }

   public static ByteBuffer createByteBuffer(int var0) {
      ByteBuffer var1 = ByteBuffer.allocateDirect(var0).order(ByteOrder.nativeOrder());
      var1.clear();
      onBufferAllocated(var1);
      return var1;
   }

   private static void loadCleanerMethods() {
      if (!loadedMethods.getAndSet(true)) {
         synchronized(loadedMethods) {
            cleanerMethod = loadMethod("sun.nio.ch.DirectBuffer", "cleaner");
            viewedBufferMethod = loadMethod("sun.nio.ch.DirectBuffer", "viewedBuffer");
            if (viewedBufferMethod == null) {
               viewedBufferMethod = loadMethod("sun.nio.ch.DirectBuffer", "attachment");
            }

            ByteBuffer var1 = createByteBuffer(1);
            Class var2 = var1.getClass();

            try {
               freeMethod = var2.getMethod("free");
            } catch (SecurityException | NoSuchMethodException var5) {
            }

         }
      }
   }

   public static void destroyDirectBuffer(Buffer var0) {
      if (var0.isDirect()) {
         loadCleanerMethods();

         try {
            if (freeMethod != null) {
               freeMethod.invoke(var0);
            } else {
               Object var1 = cleanerMethod.invoke(var0);
               if (var1 == null) {
                  Object var2 = viewedBufferMethod.invoke(var0);
                  if (var2 != null) {
                     destroyDirectBuffer((Buffer)var2);
                  } else {
                     Logger.getLogger(BufferUtils.class.getName()).log(Level.SEVERE, "Buffer cannot be destroyed: {0}", var0);
                  }
               }
            }
         } catch (IllegalArgumentException | InvocationTargetException | SecurityException | IllegalAccessException var3) {
            Logger.getLogger(BufferUtils.class.getName()).log(Level.SEVERE, "{0}", var3);
         }

      }
   }

   private static class ClearReferences extends Thread {
      ClearReferences() {
         this.setDaemon(true);
      }

      public void run() {
         try {
            while(true) {
               Reference var1 = BufferUtils.removeCollected.remove();
               BufferUtils.trackedBuffers.remove(var1);
            }
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }

   private static class BufferInfo extends PhantomReference {
      private final Class type;
      private final int size;

      public BufferInfo(Class var1, int var2, Buffer var3, ReferenceQueue var4) {
         super(var3, var4);
         this.type = var1;
         this.size = var2;
      }
   }
}
