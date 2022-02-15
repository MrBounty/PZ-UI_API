package se.krka.kahlua.profiler;

public class AggregatingProfiler implements Profiler {
   private final StacktraceCounter root = new StacktraceCounter();

   public synchronized void getSample(Sample var1) {
      this.root.addTime(var1.getTime());
      StacktraceCounter var2 = this.root;

      for(int var3 = var1.getList().size() - 1; var3 >= 0; --var3) {
         StacktraceElement var4 = (StacktraceElement)var1.getList().get(var3);
         StacktraceCounter var5 = var2.getOrCreateChild(var4);
         var5.addTime(var1.getTime());
         var2 = var5;
      }

   }

   public StacktraceNode toTree(int var1, double var2, int var4) {
      return StacktraceNode.createFrom(this.root, new FakeStacktraceElement("Root", "root"), var1, var2, var4);
   }
}
