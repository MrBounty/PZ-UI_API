package se.krka.kahlua.threading;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;

public class BlockingKahluaThread extends KahluaThread {
   private final Lock lock = new ReentrantLock();

   public BlockingKahluaThread(Platform var1, KahluaTable var2) {
      super(var1, var2);
   }

   public BlockingKahluaThread(PrintStream var1, Platform var2, KahluaTable var3) {
      super(var1, var2, var3);
   }

   private void lock() {
      this.lock.lock();
   }

   private void unlock() {
      this.lock.unlock();
   }

   public int call(int var1) {
      this.lock();

      int var2;
      try {
         var2 = super.call(var1);
      } finally {
         this.unlock();
      }

      return var2;
   }

   public int pcall(int var1) {
      this.lock();

      int var2;
      try {
         var2 = super.pcall(var1);
      } finally {
         this.unlock();
      }

      return var2;
   }

   public Object[] pcall(Object var1) {
      this.lock();

      Object[] var2;
      try {
         var2 = super.pcall(var1);
      } finally {
         this.unlock();
      }

      return var2;
   }

   public final Object[] pcall(Object var1, Object[] var2) {
      this.lock();

      Object[] var3;
      try {
         var3 = super.pcall(var1, var2);
      } finally {
         this.unlock();
      }

      return var3;
   }

   public Object call(Object var1, Object var2, Object var3, Object var4) {
      this.lock();

      Object var5;
      try {
         var5 = super.call(var1, var2, var3, var4);
      } finally {
         this.unlock();
      }

      return var5;
   }

   public Object call(Object var1, Object[] var2) {
      this.lock();

      Object var3;
      try {
         var3 = super.call(var1, var2);
      } finally {
         this.unlock();
      }

      return var3;
   }

   public KahluaTable getEnvironment() {
      this.lock();

      KahluaTable var1;
      try {
         var1 = super.getEnvironment();
      } finally {
         this.unlock();
      }

      return var1;
   }

   public Object getMetaOp(Object var1, String var2) {
      this.lock();

      Object var3;
      try {
         var3 = super.getMetaOp(var1, var2);
      } finally {
         this.unlock();
      }

      return var3;
   }

   public Object getmetatable(Object var1, boolean var2) {
      this.lock();

      Object var3;
      try {
         var3 = super.getmetatable(var1, var2);
      } finally {
         this.unlock();
      }

      return var3;
   }

   public void setmetatable(Object var1, KahluaTable var2) {
      this.lock();

      try {
         super.setmetatable(var1, var2);
      } finally {
         this.unlock();
      }

   }

   public Object tableget(Object var1, Object var2) {
      this.lock();

      Object var3;
      try {
         var3 = super.tableget(var1, var2);
      } finally {
         this.unlock();
      }

      return var3;
   }

   public void tableSet(Object var1, Object var2, Object var3) {
      this.lock();

      try {
         super.tableSet(var1, var2, var3);
      } finally {
         this.unlock();
      }

   }

   public static void main(String[] var0) throws IOException, InterruptedException {
      J2SEPlatform var1 = new J2SEPlatform();
      KahluaTable var2 = var1.newEnvironment();
      final BlockingKahluaThread var3 = new BlockingKahluaThread(var1, var2);
      final LuaClosure var4 = LuaCompiler.loadstring("x = (x or 0) + 1", "", var3.getEnvironment());
      final AtomicInteger var5 = new AtomicInteger(0);

      for(int var6 = 0; var6 < 100; ++var6) {
         (new Thread(new Runnable() {
            public void run() {
               for(int var1 = 0; var1 < 100; ++var1) {
                  try {
                     Thread.sleep((long)(Math.random() * 10.0D));
                  } catch (InterruptedException var3x) {
                     var3x.printStackTrace();
                  }

                  var3.pcall(var4);
               }

               var5.incrementAndGet();
            }
         })).start();
      }

      while(var5.get() != 100) {
         Thread.sleep(100L);
      }

      LuaClosure var7 = LuaCompiler.loadstring("print('x='..x)", "", var3.getEnvironment());
      var3.pcall(var7);
   }
}
