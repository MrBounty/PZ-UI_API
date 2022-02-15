package zombie.core.profiling;

import java.util.Stack;
import zombie.GameProfiler;

public class PerformanceProfileProbe extends AbstractPerformanceProfileProbe {
   private final Stack m_currentArea = new Stack();

   public PerformanceProfileProbe(String var1) {
      super(var1);
   }

   public PerformanceProfileProbe(String var1, boolean var2) {
      super(var1);
      this.setEnabled(var2);
   }

   protected void onStart() {
      this.m_currentArea.push(GameProfiler.getInstance().start(this.Name));
   }

   protected void onEnd() {
      GameProfiler.getInstance().end((GameProfiler.ProfileArea)this.m_currentArea.pop());
   }
}
