package zombie.core;

public class BoxedStaticValues {
   static Double[] doubles = new Double[10000];
   static Double[] negdoubles = new Double[10000];
   static Double[] doublesh = new Double[10000];
   static Double[] negdoublesh = new Double[10000];

   public static Double toDouble(double var0) {
      if (var0 >= 10000.0D) {
         return var0;
      } else if (var0 <= -10000.0D) {
         return var0;
      } else if ((double)((int)Math.abs(var0)) == Math.abs(var0)) {
         return var0 < 0.0D ? negdoubles[(int)(-var0)] : doubles[(int)var0];
      } else if ((double)((int)Math.abs(var0)) == Math.abs(var0) - 0.5D) {
         return var0 < 0.0D ? negdoublesh[(int)(-var0)] : doublesh[(int)var0];
      } else {
         return var0;
      }
   }

   static {
      for(int var0 = 0; var0 < 10000; ++var0) {
         doubles[var0] = (double)var0;
         negdoubles[var0] = -doubles[var0];
         doublesh[var0] = (double)var0 + 0.5D;
         negdoublesh[var0] = -(doubles[var0] + 0.5D);
      }

   }
}
