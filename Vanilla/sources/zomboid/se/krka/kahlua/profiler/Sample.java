package se.krka.kahlua.profiler;

import java.util.List;

public class Sample {
   private final List list;
   private final long time;

   public Sample(List var1, long var2) {
      this.list = var1;
      this.time = var2;
   }

   public List getList() {
      return this.list;
   }

   public long getTime() {
      return this.time;
   }
}
