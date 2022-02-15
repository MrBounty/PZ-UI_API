package zombie.core.profiling;

import zombie.util.list.PZArrayUtil;

public class PerformanceProfileProbeList {
   final String m_prefix;
   final PerformanceProfileProbe[] layers;

   public static PerformanceProfileProbeList construct(String var0, int var1) {
      return new PerformanceProfileProbeList(var0, var1, PerformanceProfileProbe.class, PerformanceProfileProbe::new);
   }

   public static PerformanceProfileProbeList construct(String var0, int var1, Class var2, PerformanceProfileProbeList.Constructor var3) {
      return new PerformanceProfileProbeList(var0, var1, var2, var3);
   }

   protected PerformanceProfileProbeList(String var1, int var2, Class var3, PerformanceProfileProbeList.Constructor var4) {
      this.m_prefix = var1;
      this.layers = (PerformanceProfileProbe[])PZArrayUtil.newInstance(var3, var2 + 1);

      for(int var5 = 0; var5 < var2; ++var5) {
         this.layers[var5] = var4.get(var1 + "_" + var5);
      }

      this.layers[var2] = var4.get(var1 + "_etc");
   }

   public int count() {
      return this.layers.length;
   }

   public PerformanceProfileProbe at(int var1) {
      return var1 < this.count() ? this.layers[var1] : this.layers[this.count() - 1];
   }

   public PerformanceProfileProbe start(int var1) {
      PerformanceProfileProbe var2 = this.at(var1);
      var2.start();
      return var2;
   }

   public interface Constructor {
      PerformanceProfileProbe get(String var1);
   }
}
