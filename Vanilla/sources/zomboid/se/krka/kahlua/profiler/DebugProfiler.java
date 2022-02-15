package se.krka.kahlua.profiler;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

public class DebugProfiler implements Profiler {
   private PrintWriter output;

   public DebugProfiler(Writer var1) {
      this.output = new PrintWriter(var1);
   }

   public synchronized void getSample(Sample var1) {
      this.output.println("Sample: " + var1.getTime() + " ms");
      Iterator var2 = var1.getList().iterator();

      while(var2.hasNext()) {
         StacktraceElement var3 = (StacktraceElement)var2.next();
         PrintWriter var10000 = this.output;
         String var10001 = var3.name();
         var10000.println("\t" + var10001 + "\t" + var3.type() + "\t" + var3.hashCode());
      }

   }
}
