package zombie.core.profiling;

import zombie.GameProfiler;

public class PerformanceProfileFrameProbe extends PerformanceProfileProbe {
   public PerformanceProfileFrameProbe(String var1) {
      super(var1);
   }

   protected void onStart() {
      GameProfiler.getInstance().startFrame(this.Name);
      super.onStart();
   }

   protected void onEnd() {
      super.onEnd();
      GameProfiler.getInstance().endFrame();
   }
}
