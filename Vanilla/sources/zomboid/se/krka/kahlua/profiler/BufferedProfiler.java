package se.krka.kahlua.profiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BufferedProfiler implements Profiler {
   private final List buffer = new ArrayList();

   public void getSample(Sample var1) {
      this.buffer.add(var1);
   }

   public void sendTo(Profiler var1) {
      Iterator var2 = this.buffer.iterator();

      while(var2.hasNext()) {
         Sample var3 = (Sample)var2.next();
         var1.getSample(var3);
      }

   }
}
