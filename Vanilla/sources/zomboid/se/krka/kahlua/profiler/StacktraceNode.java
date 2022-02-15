package se.krka.kahlua.profiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StacktraceNode {
   private final long time;
   private final StacktraceElement element;
   private final List children;

   public StacktraceNode(StacktraceElement var1, List var2, long var3) {
      this.element = var1;
      this.children = var2;
      this.time = var3;
   }

   public static StacktraceNode createFrom(StacktraceCounter var0, StacktraceElement var1, int var2, double var3, int var5) {
      StacktraceNode var6 = new StacktraceNode(var1, new ArrayList(), var0.getTime());
      if (var2 > 0) {
         Map var7 = var0.getChildren();
         ArrayList var8 = new ArrayList(var7.entrySet());
         Collections.sort(var8, new Comparator() {
            public int compare(Entry var1, Entry var2) {
               return Long.signum(((StacktraceCounter)var2.getValue()).getTime() - ((StacktraceCounter)var1.getValue()).getTime());
            }
         });

         for(int var9 = var8.size() - 1; var9 >= var5; --var9) {
            var8.remove(var9);
         }

         Iterator var14 = var8.iterator();

         while(var14.hasNext()) {
            Entry var10 = (Entry)var14.next();
            StacktraceElement var11 = (StacktraceElement)var10.getKey();
            StacktraceCounter var12 = (StacktraceCounter)var10.getValue();
            if ((double)var12.getTime() >= var3 * (double)var0.getTime()) {
               StacktraceNode var13 = createFrom(var12, var11, var2 - 1, var3, var5);
               var6.children.add(var13);
            }
         }
      }

      return var6;
   }

   public void output(PrintWriter var1) {
      this.output(var1, "", this.time, this.time);
   }

   public void output(PrintWriter var1, String var2, long var3, long var5) {
      var1.println(String.format("%-40s   %4d ms   %5.1f%% of parent    %5.1f%% of total", var2 + this.element.name() + " (" + this.element.type() + ")", this.time, 100.0D * (double)this.time / (double)var3, 100.0D * (double)this.time / (double)var5));
      String var7 = var2 + "  ";
      Iterator var8 = this.children.iterator();

      while(var8.hasNext()) {
         StacktraceNode var9 = (StacktraceNode)var8.next();
         var9.output(var1, var7, this.time, var5);
      }

   }
}
