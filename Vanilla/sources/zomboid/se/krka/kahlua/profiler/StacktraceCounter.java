package se.krka.kahlua.profiler;

import java.util.HashMap;
import java.util.Map;

public class StacktraceCounter {
   private final Map children = new HashMap();
   private long time = 0L;

   public void addTime(long var1) {
      this.time += var1;
   }

   public StacktraceCounter getOrCreateChild(StacktraceElement var1) {
      StacktraceCounter var2 = (StacktraceCounter)this.children.get(var1);
      if (var2 == null) {
         var2 = new StacktraceCounter();
         this.children.put(var1, var2);
      }

      return var2;
   }

   public long getTime() {
      return this.time;
   }

   public Map getChildren() {
      return this.children;
   }
}
