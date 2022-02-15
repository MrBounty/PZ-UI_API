package se.krka.kahlua.profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import se.krka.kahlua.vm.Coroutine;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;

public class Sampler {
   private static final AtomicInteger NEXT_ID = new AtomicInteger();
   private final KahluaThread thread;
   private final Timer timer;
   private final long period;
   private final Profiler profiler;

   public Sampler(KahluaThread var1, long var2, Profiler var4) {
      this.thread = var1;
      this.period = var2;
      this.profiler = var4;
      this.timer = new Timer("Kahlua Sampler-" + NEXT_ID.incrementAndGet(), true);
   }

   public void start() {
      TimerTask var1 = new TimerTask() {
         public void run() {
            ArrayList var1 = new ArrayList();
            Sampler.this.appendList(var1, Sampler.this.thread.currentCoroutine);
            Sampler.this.profiler.getSample(new Sample(var1, Sampler.this.period));
         }
      };
      this.timer.scheduleAtFixedRate(var1, 0L, this.period);
   }

   private void appendList(List var1, Coroutine var2) {
      while(var2 != null) {
         LuaCallFrame[] var3 = var2.getCallframeStack();
         int var4 = Math.min(var3.length, var2.getCallframeTop());

         for(int var5 = var4 - 1; var5 >= 0; --var5) {
            LuaCallFrame var6 = var3[var5];
            int var7 = var6.pc - 1;
            LuaClosure var8 = var6.closure;
            JavaFunction var9 = var6.javaFunction;
            if (var8 != null) {
               var1.add(new LuaStacktraceElement(var7, var8.prototype));
            } else if (var9 != null) {
               var1.add(new JavaStacktraceElement(var9));
            }
         }

         var2 = var2.getParent();
      }

   }

   public void stop() {
      this.timer.cancel();
   }
}
