package zombie.core.profiling;

import zombie.GameProfiler;
import zombie.util.Lambda;
import zombie.util.lambda.Invokers;

public abstract class AbstractPerformanceProfileProbe {
   public final String Name;
   private boolean m_isEnabled = true;
   private boolean m_isRunning = false;
   private boolean m_isProfilerRunning = false;

   protected AbstractPerformanceProfileProbe(String var1) {
      this.Name = var1;
   }

   protected abstract void onStart();

   protected abstract void onEnd();

   public void start() {
      if (this.m_isRunning) {
         throw new RuntimeException("start() already called. " + this.getClass().getSimpleName() + " is Non-reentrant. Please call end() first.");
      } else {
         this.m_isProfilerRunning = this.isEnabled() && GameProfiler.isRunning();
         if (this.m_isProfilerRunning) {
            this.m_isRunning = true;
            this.onStart();
         }
      }
   }

   public boolean isEnabled() {
      return this.m_isEnabled;
   }

   public void setEnabled(boolean var1) {
      this.m_isEnabled = var1;
   }

   public void end() {
      if (this.m_isProfilerRunning) {
         if (!this.m_isRunning) {
            throw new RuntimeException("end() called without calling start().");
         } else {
            this.onEnd();
            this.m_isRunning = false;
         }
      }
   }

   public void invokeAndMeasure(Runnable var1) {
      try {
         this.start();
         var1.run();
      } finally {
         this.end();
      }

   }

   public void invokeAndMeasure(Object var1, Invokers.Params1.ICallback var2) {
      Lambda.capture(this, var1, var2, (var0, var1x, var2x, var3) -> {
         var1x.invokeAndMeasure(var0.invoker(var2x, var3));
      });
   }

   public void invokeAndMeasure(Object var1, Object var2, Invokers.Params2.ICallback var3) {
      Lambda.capture(this, var1, var2, var3, (var0, var1x, var2x, var3x, var4) -> {
         var1x.invokeAndMeasure(var0.invoker(var2x, var3x, var4));
      });
   }
}
